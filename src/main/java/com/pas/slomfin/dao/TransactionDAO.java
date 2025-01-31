package com.pas.slomfin.dao;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import com.pas.util.TransactionComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoTransaction;
import com.pas.util.Utils;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TransactionDAO implements Serializable 
{
	@Serial
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(TransactionDAO.class);
	
	private Map<Integer,DynamoTransaction> fullTransactionsMap = new HashMap<>();
	private List<DynamoTransaction> fullTransactionsList = new ArrayList<>();


    private static DynamoDbTable<DynamoTransaction> transactionsTable;
	private static final String AWS_TABLE_NAME = "slomFinTransactions";
	
	//private static String HTML_CRLF = "<br>";
	//private static String PDF_CRLF = "\r\n\r\n";
		
	public TransactionDAO(DynamoClients dynamoClients2) 
	{		
	   try 
	   {
           transactionsTable = dynamoClients2.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(DynamoTransaction.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing TransactionsDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public Integer addTransaction(DynamoTransaction dynamoTransaction) throws Exception
	{
		DynamoTransaction nflGame2 = dynamoUpsert(dynamoTransaction);		
		 
		dynamoTransaction.setTransactionID(nflGame2.getTransactionID());
		
		logger.info("LoggedDBOperation: function-add; table:transaction; rows:1");
	
		refreshListsAndMaps("add", dynamoTransaction);	
					
		logger.info("addTransaction complete. added transactionID: " + nflGame2.getTransactionID());		
		
		return nflGame2.getTransactionID(); //this is the key that was just added
	}
	
	public DynamoTransaction getTransactionByTransactionID(Integer transactionId)
	{
		return this.getFullTransactionsMap().get(transactionId);
	}
	
	private DynamoTransaction dynamoUpsert(DynamoTransaction dynamoTransaction) throws Exception 
	{
		if (dynamoTransaction.getTransactionID() == null)
		{
			Integer nextGameID = determineNextTransactionId();
			dynamoTransaction.setTransactionID(nextGameID);
		}
						
		PutItemEnhancedRequest<DynamoTransaction> putItemEnhancedRequest = PutItemEnhancedRequest.builder(DynamoTransaction.class).item(dynamoTransaction).build();
		transactionsTable.putItem(putItemEnhancedRequest);
			
		return dynamoTransaction;
	}

	private Integer determineNextTransactionId() 
	{
		int nextTrxID = 0;
		
		List<Integer> gameIds = this.getFullTransactionsMap().keySet().stream().toList();
		int max = Collections.max(gameIds);
        nextTrxID = max + 1;
        
		return nextTrxID;
	}

	public void updateTransaction(DynamoTransaction dynamoTransaction)  throws Exception
	{
		dynamoUpsert(dynamoTransaction);		
			
		logger.info("LoggedDBOperation: function-update; table:transaction; rows:1");
		
		refreshListsAndMaps("update", dynamoTransaction);	
		
		logger.debug("update transaction table complete");		
	}
	
	public void deleteTransaction(DynamoTransaction dynamoTransaction) throws Exception 
	{
		Key key = Key.builder().partitionValue(dynamoTransaction.getTransactionID()).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		transactionsTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:transaction; rows:1");
		
		refreshListsAndMaps("delete", dynamoTransaction);		
		
		logger.info("delete transaction complete");	
	}
	
	public void readTransactionsWithin2YearsFromDB() throws Exception 
    {
		logger.info("entering readTransactionsWithin2YearsFromDB");
		
		String twoYearsAgo = Utils.getTwoYearsAgoDate();

        logger.info("looking for transactions newer than: {}", twoYearsAgo);
		
		Map<String, AttributeValue> av = Map.of(":min_value", AttributeValue.fromS(twoYearsAgo));
		
		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .filterExpression(Expression.builder()
                        .expression("transactionPostedDate >= :min_value")
                        .expressionValues(av)
                        .build())
                .build();
		
		Iterator<DynamoTransaction> results = transactionsTable.scan(request).items().iterator();
	  	
		int trxCount = 0;
		while (results.hasNext()) 
        {
			trxCount++;
			logger.info("iterating transaction " + trxCount);
			DynamoTransaction dynamoTransaction = results.next();

            this.getFullTransactionsList().add(dynamoTransaction);
        }
		
		this.getFullTransactionsList().sort(new Comparator<DynamoTransaction>() {
            public int compare(DynamoTransaction o1, DynamoTransaction o2) {
                return o1.getTransactionPostedDate().compareTo(o2.getTransactionPostedDate());
            }
        });
		
		logger.info("LoggedDBOperation: function-inquiry; table:transaction; rows:" + this.getFullTransactionsList().size());
	}

	private void refreshListsAndMaps(String function, DynamoTransaction dynamoTransaction)
	{
		Integer transactionID = dynamoTransaction.getTransactionID();
		
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullTransactionsMap().remove(dynamoTransaction.getTransactionID());
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullTransactionsMap().put(dynamoTransaction.getTransactionID(), dynamoTransaction);
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullTransactionsMap().remove(dynamoTransaction.getTransactionID());
			this.getFullTransactionsMap().put(dynamoTransaction.getTransactionID(), dynamoTransaction);
		}
		
		this.getFullTransactionsList().clear();
		Collection<DynamoTransaction> values = this.getFullTransactionsMap().values();
		this.setFullTransactionsList(new ArrayList<>(values));
		this.getFullTransactionsList().sort(new TransactionComparator());

	}

	public Map<Integer, DynamoTransaction> getFullTransactionsMap() {
		return fullTransactionsMap;
	}

	public void setFullTransactionsMap(Map<Integer, DynamoTransaction> fullTransactionsMap) {
		this.fullTransactionsMap = fullTransactionsMap;
	}


    public List<DynamoTransaction> getFullTransactionsList() {
        return fullTransactionsList;
    }

    public void setFullTransactionsList(List<DynamoTransaction> fullTransactionsList) {
        this.fullTransactionsList = fullTransactionsList;
    }
}
