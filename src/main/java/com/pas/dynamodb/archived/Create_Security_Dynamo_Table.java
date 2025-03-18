package com.pas.dynamodb.archived;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.AppSecurity;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;
import com.pas.util.SlomFinUtil;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class Create_Security_Dynamo_Table
{
	private static Logger logger = LogManager.getLogger(Create_Security_Dynamo_Table.class); //log4j for Logging 
	
	private static String AWS_TABLE_NAME = "slomFinSecurity";
	
    public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 loadTable(dynamoClients);
    		 
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception: " + e.getMessage(), e);
    	 }
		System.exit(1);
	}
   
    private static void loadTable(DynamoClients dynamoClients) throws Exception 
    {
        //Delete the table if it exists.  If not, just catch the exception and move on
        try
        {
        	deleteTable(dynamoClients.getDynamoDbEnhancedClient());
        }
        catch (Exception e)
        {
        	logger.info(e.getMessage());
        }
        
        // Create a table in DynamoDB Local
        DynamoDbTable<AppSecurity> slomFinSecurityTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
         
       	AppSecurity slomFinSecurity = new AppSecurity();
       	slomFinSecurity.setUserName("paulslom");       	
       	String encodedPW = "";	
		encodedPW = SlomFinUtil.getEncryptedPassword(slomFinSecurity.getUserName());
       	slomFinSecurity.setPassword(encodedPW);
		slomFinSecurityTable.putItem(slomFinSecurity); 
		
		
			  
	}
   
    private static DynamoDbTable<AppSecurity> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
        DynamoDbTable<AppSecurity> teamTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(AppSecurity.class));
        
        // Create the DynamoDB table.  If it exists, it'll throw an exception
        
        try
        {
	        teamTable.createTable(builder -> builder.build());
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
        
        return teamTable;
    }    
    
    private static void deleteTable(DynamoDbEnhancedClient ddbEnhancedClient) throws Exception
    {
    	DynamoDbTable<AppSecurity> teamTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(AppSecurity.class));
       	teamTable.deleteTable();		
	}
		
}