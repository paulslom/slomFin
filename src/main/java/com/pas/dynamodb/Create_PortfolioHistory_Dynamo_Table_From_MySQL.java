package com.pas.dynamodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pas.beans.PortfolioHistory;
import com.pas.slomfin.dao.PortfolioHistoryRowMapper;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class Create_PortfolioHistory_Dynamo_Table_From_MySQL
{
	private static Logger logger = LogManager.getLogger(Create_PortfolioHistory_Dynamo_Table_From_MySQL.class); //log4j for Logging 
	
	private static String AWS_TABLE_NAME = "slomFinPortfolioHistory";
		
    public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 List<PortfolioHistory> phList = getFromMySQLDB();	
    		 loadTable(dynamoClients, phList);       	
	    	
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception in Create_PortfolioHistory_Dynamo_Table_From_MySQL " + e.getMessage(), e);
    	 }
		System.exit(1);
	}

    private static List<PortfolioHistory> getFromMySQLDB() 
	{
		MysqlDataSource ds = getMySQLDatasource();
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);    
    	String sql = "SELECT DATE_FORMAT(H.dhistoryDate, \"%Y-%m-%dT%H:%i:%S\") as dhistoryDate, SUM(H.mvalue) as totalValue FROM Tblportfoliohistory H GROUP BY H.dhistoryDate ORDER BY H.dhistoryDate";		 
    	List<PortfolioHistory> phList = jdbcTemplate.query(sql, new PortfolioHistoryRowMapper());
		return phList;
	}
   
    private static void loadTable(DynamoClients dynamoClients, List<PortfolioHistory> phList) throws Exception 
    {
        //Delete the table in DynamoDB Local if it exists.  If not, just catch the exception and move on
        try
        {
        	deleteTable(dynamoClients.getDynamoDbEnhancedClient());
        }
        catch (Exception e)
        {
        	logger.info(e.getMessage());
        }
        
        // Create a table in DynamoDB Local
        DynamoDbTable<PortfolioHistory> phTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
        
    	int putCount = 0;
    	
        if (phList == null)
        {
        	logger.error("games list is Empty - can't do anything more so exiting");
        }
        else
        {
        	logger.info("About to try to put " + phList.size() + " rows into table " + AWS_TABLE_NAME);
        	
        	for (int i = 0; i < phList.size(); i++) 
        	{
        		PortfolioHistory ph = phList.get(i);
        		phTable.putItem(ph); 
				putCount++;
				logger.info(AWS_TABLE_NAME + " Put portfolioHistory row count: " + putCount);
			} 
        	
        	logger.info("FINISHED inserting " + putCount + " rows into the table:" + AWS_TABLE_NAME);
        }        
	}
   
    private static DynamoDbTable<PortfolioHistory> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
        DynamoDbTable<PortfolioHistory> phTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(PortfolioHistory.class));
        
        // Create the DynamoDB table.  If it exists, it'll throw an exception
        
        try
        {        	
          	phTable.createTable(builder -> builder.build());
        }
        catch (ResourceInUseException riue)
        {
        	logger.info("Table already exists! " + riue.getMessage());
        	throw riue;
        }
        // The 'dynamoDbClient' instance that's passed to the builder for the DynamoDbWaiter is the same instance
        // that was passed to the builder of the DynamoDbEnhancedClient instance used to create the 'customerDynamoDbTable'.
        // This means that the same Region that was configured on the standard 'dynamoDbClient' instance is used for all service clients.
        
        try (DynamoDbWaiter waiter = DynamoDbWaiter.builder().client(ddbClient).build()) // DynamoDbWaiter is Autocloseable
        { 
            ResponseOrException<DescribeTableResponse> response = waiter
                    .waitUntilTableExists(builder -> builder.tableName(AWS_TABLE_NAME).build())
                    .matched();
            
            response.response().orElseThrow(
                    () -> new RuntimeException(AWS_TABLE_NAME + " was not created."));
            
            // The actual error can be inspected in response.exception()
            logger.info(AWS_TABLE_NAME + " table was created.");
        }        
        
        return phTable;
    }    
    
    private static void deleteTable(DynamoDbEnhancedClient ddbEnhancedClient) throws Exception
    {
    	DynamoDbTable<PortfolioHistory> portHistTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(PortfolioHistory.class));
       	portHistTable.deleteTable();		
	}

    private static MysqlDataSource getMySQLDatasource()
	{
		MysqlDataSource ds = null;
		
		Properties prop = new Properties();
		
	    try 
	    {
	    	//Use the prior project for these properties - they don't exist in this one
	    	InputStream stream = new FileInputStream(new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 9.0\\conf/catalina.properties"));
	    	prop.load(stream);   		
		
	    	ds = new MysqlDataSource();
	    		    			
		    String dbName = prop.getProperty("PORTFOLIO_DB_NAME");
		    String userName = prop.getProperty("PORTFOLIO_USERNAME");
		    String password = prop.getProperty("PORTFOLIO_PASSWORD");
		    String hostname = prop.getProperty("PORTFOLIO_HOSTNAME");
		    String port = prop.getProperty("PORTFOLIO_PORT");
		    String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
		    
		    //logger.info("jdbcUrl for datasource: " + jdbcUrl);
		    
		    ds.setURL(jdbcUrl);
		    ds.setPassword(password);
		    ds.setUser(userName);
		    
		 }
		 catch (Exception e) 
	     { 
		    logger.error(e.toString(), e);
		 }     		
       	
       	return ds;
	}
		
		
}