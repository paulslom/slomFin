package com.pas.dynamodb;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pas.util.TransactionComparatorAscDate;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class Create_Transactions_JSON_file_From_Dynamo
{
	private static Logger logger = LogManager.getLogger(Create_Transactions_JSON_file_From_Dynamo.class); //log4j for Logging 
	
	private static DynamoDbTable<DynamoTransaction> transactionsTable;
	private static String AWS_TABLE_NAME = "slomFinTransactions";
	private static String jsonFilePath = "src/main/resources/data/transactions.json";
	
    public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    		 
    		 establishTable(dynamoClients);
    		 List<DynamoTransaction> transactionsList = readAllTransactionsFromDynamoDB();	
    		 createJSON(transactionsList);       	
	    	
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception in Create_NFLGame_Dynamo_Table_From_MySQL " + e.getMessage(), e);
    	 }
		System.exit(1);
	}

    private static void createJSON(List<DynamoTransaction> transactionsList) throws IOException 
    {
    	FileWriter fileWriter = new FileWriter(jsonFilePath); 
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(fileWriter, transactionsList);
    }

	private static void establishTable(DynamoClients dynamoClients) 
    {
       try 
 	   {
            transactionsTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(DynamoTransaction.class));
 	   } 
 	   catch (final Exception ex) 
 	   {
 	      logger.error("Got exception while initializing TransactionsDAO. Ex = " + ex.getMessage(), ex);
 	   }		// TODO Auto-generated method stub
		
	}

	public static List<DynamoTransaction> readAllTransactionsFromDynamoDB()
	{
		List<DynamoTransaction> fullTransactionsList = new ArrayList<>();
	
		for (DynamoTransaction dynamoTransaction : transactionsTable.scan().items())
		{
			fullTransactionsList.add(dynamoTransaction);
		}

		Collections.sort(fullTransactionsList, new TransactionComparatorAscDate());

		return fullTransactionsList;
	}
			
		
}