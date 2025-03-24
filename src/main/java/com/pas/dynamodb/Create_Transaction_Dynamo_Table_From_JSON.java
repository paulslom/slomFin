package com.pas.dynamodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;
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

public class Create_Transaction_Dynamo_Table_From_JSON
{
	private static Logger logger = LogManager.getLogger(Create_Transaction_Dynamo_Table_From_JSON.class); //log4j for Logging 
	
	private static String AWS_TABLE_NAME = "slomFinTransactions";
		
    public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 List<DynamoTransaction> trxList = getTransactionsFromJSON();	
    		 loadTable(dynamoClients, trxList);       	
	    	
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception in Create_Transaction_Dynamo_Table_From_JSON " + e.getMessage(), e);
    	 }
		System.exit(1);
	}

    private static List<DynamoTransaction> getTransactionsFromJSON() throws FileNotFoundException 
	{
    	String jsonFilePath = "C:\\Paul\\GitHub\\slomFin\\src\\main\\resources\\data\\transactions.json";
        File jsonFile = new File(jsonFilePath);                    
        InputStream inputStream = new FileInputStream(jsonFile);
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        DynamoTransaction[] trxArray = new Gson().fromJson(reader, DynamoTransaction[].class);
       	List<DynamoTransaction> transactionsList = Arrays.asList(trxArray);
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
        DynamoDbTable<DynamoTransaction> trxTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
        
    	int putCount = 0;
    	
        if (transactionsList == null)
        {
        	logger.error("trx list is Empty - can't do anything more so exiting");
        }
        else
        {
        	logger.info("About to try to put " + transactionsList.size() + " rows into table " + AWS_TABLE_NAME);
        	
        	for (int i = 0; i < transactionsList.size(); i++) 
        	{
        		DynamoTransaction trx = transactionsList.get(i);
        		trxTable.putItem(trx); 
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

		
}