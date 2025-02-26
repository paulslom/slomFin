package com.pas.dynamodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pas.slomfin.dao.TransactionsRowMapper;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class Create_Transaction_Dynamo_Table_From_MySQL
{
	private static Logger logger = LogManager.getLogger(Create_Transaction_Dynamo_Table_From_MySQL.class); //log4j for Logging 
	
	private static String AWS_TABLE_NAME = "slomFinTransactions";
		
    public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 List<DynamoTransaction> gamesList = getTransactionsFromMySQLDB();	
    		 loadTable(dynamoClients, gamesList);       	
	    	
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception in Create_NFLGame_Dynamo_Table_From_MySQL " + e.getMessage(), e);
    	 }
		System.exit(1);
	}

    private static List<DynamoTransaction> getTransactionsFromMySQLDB() 
	{
		MysqlDataSource ds = getMySQLDatasource();
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);    
    	String sql = "select trx.iTransactionID, trx.iAccountID, trx.iInvestmentID, trx.iTransactionTypeID,"
    			+ "        trx.dTransactionDate, trx.decUnits, trx.mPrice, trx.mCostProceeds, trx.dTranEntryDate,"
    			+ "        trx.dTranPostedDate, trx.dTranChangeDate, trx.iDividendTaxableYear, trx.mEffectiveAmount,"
    			+ "        trx.iOptionTypeID, trx.mStrikePrice, trx.dExpirationDate, trx.bOpeningTrxInd, trx.iCheckNo,"
    			+ "        trx.sDescription as trxDescription, trx.iWDCategoryID, trx.iCashDepositTypeID, trx.bFinalTrxOfBillingCycle,"
    			+ "        acct.sAccountName, inv.sDescription as invDescription, wdc.sWDCategoryDescription, cdtyp.sCashDepositTypeDesc,"
    			+ "        trxtyp.sDescription as trxTypDescription, trxtyp.bPositiveInd as trxTypPositiveInd"
    			+ " from tbltransaction trx inner join tbltransactiontype trxtyp on trx.iTransactionTypeID = trxtyp.iTransactionTypeID"
    			+ "   inner join tblaccount acct on trx.iAccountID = acct.iAccountID"
    			+ "   inner join tblinvestment inv on trx.iInvestmentID = inv.iInvestmentID"
    			+ "   left outer join tblwdcategory wdc on trx.iWDCategoryID = wdc.iWDCategoryID"
    			+ "   left outer join tblcashdeposittype cdtyp on trx.iCashDepositTypeID = cdtyp.iCashDepositTypeID";		 
    	List<DynamoTransaction> transactionsList = jdbcTemplate.query(sql, new TransactionsRowMapper());
		return transactionsList;
	}
   
    private static void loadTable(DynamoClients dynamoClients, List<DynamoTransaction> transactionsList) throws Exception 
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
        DynamoDbTable<DynamoTransaction> gameTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
        
    	int putCount = 0;
    	
        if (transactionsList == null)
        {
        	logger.error("games list is Empty - can't do anything more so exiting");
        }
        else
        {
        	logger.info("About to try to put " + transactionsList.size() + " rows into table " + AWS_TABLE_NAME);
        	
        	for (int i = 0; i < transactionsList.size(); i++) 
        	{
        		DynamoTransaction trx = transactionsList.get(i);
        		gameTable.putItem(trx); 
				putCount++;
				logger.info(AWS_TABLE_NAME + " Put row count: " + putCount);
			} 
        	
        	logger.info("FINISHED inserting " + putCount + " rows into the table:" + AWS_TABLE_NAME);
        }        
	}
   
    private static DynamoDbTable<DynamoTransaction> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
        DynamoDbTable<DynamoTransaction> gamesTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(DynamoTransaction.class));
        
        // Create the DynamoDB table.  If it exists, it'll throw an exception
        
        try
        {        	
          	ArrayList<EnhancedGlobalSecondaryIndex> gsindices = new ArrayList<>();
            	
        	EnhancedGlobalSecondaryIndex gsi1 = EnhancedGlobalSecondaryIndex.builder()
        			.indexName("gsi_AccountID")
        			.projection(p -> p.projectionType(ProjectionType.ALL))
        			.build();
        	gsindices.add(gsi1);
        	            	  	
        	gamesTable.createTable(r -> r.globalSecondaryIndices(gsindices).build());
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
        
        return gamesTable;
    }    
    
    private static void deleteTable(DynamoDbEnhancedClient ddbEnhancedClient) throws Exception
    {
    	DynamoDbTable<DynamoTransaction> trxTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(DynamoTransaction.class));
       	trxTable.deleteTable();		
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