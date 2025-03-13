package com.pas.slomfin.dao;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
//import java.util.Comparator;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.dynamodb.DateToStringConverter;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoTransaction;
import com.pas.util.SlomFinUtil;
import com.pas.util.TransactionComparatorAscDate;
import com.pas.util.TransactionComparatorDescDate;

import jakarta.faces.model.SelectItem;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
//import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
//import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TransactionDAO implements Serializable 
{
	@Serial
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(TransactionDAO.class);
	
	private Map<Integer,DynamoTransaction> fullTransactionsMap = new HashMap<>();
	private List<DynamoTransaction> fullTransactionsList = new ArrayList<>();
	
	private Map<Integer,List<DynamoTransaction>> last2YearsTransactionsMapByAccountID = new HashMap<>();
	private Map<Integer,List<DynamoTransaction>> fullTransactionsMapByAccountID = new HashMap<>();
	
	private Map<Integer,String> wdCategoryMap = new HashMap<>();
	private List<SelectItem> wdCategoryDropdownList = new ArrayList<>();
	
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
		DynamoTransaction d2 = dynamoUpsert(dynamoTransaction);		
		 
		dynamoTransaction.setTransactionID(d2.getTransactionID());
		
		logger.info("LoggedDBOperation: function-add; table:transaction; rows:1");
	
		refreshListsAndMaps("add", dynamoTransaction);	
					
		logger.info("addTransaction complete. added transactionID: " + d2.getTransactionID());		
		
		return d2.getTransactionID(); //this is the key that was just added
	}
	
	public DynamoTransaction getTransactionByTransactionID(Integer transactionId)
	{
		return this.getFullTransactionsMap().get(transactionId);
	}
	
	private DynamoTransaction dynamoUpsert(DynamoTransaction dynamoTransaction) throws Exception 
	{
		if (dynamoTransaction.getTransactionID() == null)
		{
			Integer nextTrxID = determineNextTransactionId();
			dynamoTransaction.setTransactionID(nextTrxID);
		}
						
		PutItemEnhancedRequest<DynamoTransaction> putItemEnhancedRequest = PutItemEnhancedRequest.builder(DynamoTransaction.class).item(dynamoTransaction).build();
		transactionsTable.putItem(putItemEnhancedRequest);
			
		return dynamoTransaction;
	}

	private Integer determineNextTransactionId() 
	{
		int nextTrxID = 0;
		
		List<Integer> ids = this.getFullTransactionsMap().keySet().stream().toList();
		int max = Collections.max(ids);
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
		Key key = Key.builder().partitionValue(dynamoTransaction.getTransactionID()).sortValue(dynamoTransaction.getTransactionPostedDate()).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		transactionsTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:transaction; rows:1");
		
		refreshListsAndMaps("delete", dynamoTransaction);		
		
		logger.info("delete transaction complete");	
	}

	public void readAllTransactionsFromDB()
	{
		for (DynamoTransaction dynamoTransaction : transactionsTable.scan().items())
		{
			dynamoTransaction.setPostedDateJava(DateToStringConverter.unconvert(dynamoTransaction.getTransactionPostedDate()));
			dynamoTransaction.setEntryDateJava(DateToStringConverter.unconvert(dynamoTransaction.getTransactionEntryDate()));
			this.getFullTransactionsList().add(dynamoTransaction);
			this.getFullTransactionsMap().put(dynamoTransaction.getTransactionID(), dynamoTransaction);
		}

		logger.info("LoggedDBOperation: function-inquiry; table:transactions; rows:{}", this.getFullTransactionsList().size());

		Collections.sort(this.getFullTransactionsList(), new TransactionComparatorAscDate());
		
		//establish account trx map
		for (int i = 0; i < this.getFullTransactionsList().size(); i++) 
		{
			DynamoTransaction trx = this.getFullTransactionsList().get(i);
			
			logger.debug("looping transaction: " + i);
			
			if (this.getFullTransactionsMapByAccountID().containsKey(trx.getAccountID()))
			{
				List<DynamoTransaction> tempList = this.getFullTransactionsMapByAccountID().get(trx.getAccountID());
				tempList.add(trx);
				this.getFullTransactionsMapByAccountID().replace(trx.getAccountID(), tempList);
			}
			else
			{
				List<DynamoTransaction> tempList = new ArrayList<>();
				tempList.add(trx);
				this.getFullTransactionsMapByAccountID().put(trx.getAccountID(), tempList);	
			}
			
			if (trx.getWdCategoryID() != null && trx.getWdCategoryID() != 0)
			{
				if (!this.getWdCategoryMap().containsKey(trx.getWdCategoryID()))
				{
					this.getWdCategoryMap().put(trx.getWdCategoryID(), trx.getWdCategoryDescription());					
				}
			}
			
			if (trx.getUnits() != null)
			{
				if (trx.getTransactionTypeDescription().equalsIgnoreCase("Buy")
				|| 	trx.getTransactionTypeDescription().equalsIgnoreCase("Reinvest")
				||	trx.getTransactionTypeDescription().equalsIgnoreCase("Transfer In")
				||	trx.getTransactionTypeDescription().equalsIgnoreCase("Split"))
				{	
					trx.setUnitsStyleClass(SlomFinUtil.GREEN_STYLECLASS);
					trx.setDisplayUnits(trx.getUnits());
				}
				else if (trx.getTransactionTypeDescription().equalsIgnoreCase("Sell")
					 ||	 trx.getTransactionTypeDescription().equalsIgnoreCase("Transfer Out"))
				{
					trx.setUnitsStyleClass(SlomFinUtil.RED_STYLECLASS);
					trx.setDisplayUnits(trx.getUnits().multiply(new BigDecimal(-1.0)));
				}
				
			}
			
		}
		
		this.setWdCategoryMap(SlomFinUtil.sortHashMapByValues(this.getWdCategoryMap()));
		
		SelectItem si1 = new SelectItem();
		si1.setValue(-1);
		si1.setLabel("--Select--");
		this.getWdCategoryDropdownList().add(si1);
		
		for (Integer key : this.getWdCategoryMap().keySet()) 
		{
            String value = this.getWdCategoryMap().get(key);
            SelectItem si = new SelectItem();
			si.setValue(key);
			si.setLabel(value);
			this.getWdCategoryDropdownList().add(si);
        }
		
		//establish the individual account maps		
		for (Integer accountID : this.getFullTransactionsMapByAccountID().keySet()) 
		{
			logger.debug("working on transactions for: " + accountID);
			List<DynamoTransaction> newList = resetAccount2YearMap(accountID);
            logger.debug("total transactions for " + accountID + " = " + newList.size());            
            this.getLast2YearsTransactionsMapByAccountID().put(accountID, newList);
        }
	}

	private List<DynamoTransaction> resetAccount2YearMap(Integer accountID) 
	{
		Date twoYearsAgo = SlomFinUtil.getTwoYearsAgoDate();
		
		List<DynamoTransaction> tempList = this.getFullTransactionsMapByAccountID().get(accountID);
        Collections.sort(tempList, new TransactionComparatorAscDate());
        List<DynamoTransaction> currentBalanceList = SlomFinUtil.setAccountBalances(tempList, new BigDecimal(0.0));
        List<DynamoTransaction> newList = new ArrayList<>(currentBalanceList);
        List<DynamoTransaction> found = new ArrayList<>();
        for (int i = 0; i < newList.size(); i++) 
        {
        	DynamoTransaction trx = newList.get(i);
        	if (trx.getPostedDateJava().before(twoYearsAgo))
        	{
        		found.add(trx);
        	}
		}
        
        newList.removeAll(found);
        
        Collections.sort(newList, new TransactionComparatorDescDate());
        
		return newList;
	}

	public List<DynamoTransaction> searchTransactions(String searchTerm) 
	{
		List<DynamoTransaction> returnList = new ArrayList<>();
		
		for (int i = 0; i < this.getFullTransactionsList().size(); i++) 
		{
			DynamoTransaction trx = this.getFullTransactionsList().get(i);
			if (trx.getTransactionDescription() != null)
			{
				String tempString = trx.getTransactionDescription().toUpperCase();
				String inputStringCaps = searchTerm.toUpperCase();
				
				if (tempString.contains(inputStringCaps))
				{
					returnList.add(trx);
				}
			}
		}
		return returnList;
	}
	

	/*
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
	  	
		//int trxCount = 0;
		while (results.hasNext()) 
        {
			//trxCount++;
			//logger.info("iterating transaction " + trxCount);
			DynamoTransaction dynamoTransaction = results.next();
			dynamoTransaction.setPostedDateJava(DateToStringConverter.unconvert(dynamoTransaction.getTransactionPostedDate()));
            this.getTwoYearsTransactionsList().add(dynamoTransaction);
        }
		
		this.getTwoYearsTransactionsList().sort(new Comparator<DynamoTransaction>() {
            public int compare(DynamoTransaction o1, DynamoTransaction o2) {
                return o1.getTransactionPostedDate().compareTo(o2.getTransactionPostedDate());
            }
        });
		
		logger.info("LoggedDBOperation: function-inquiry; table:transaction (two years only); rows:" + this.getTwoYearsTransactionsList().size());
	}
    */
	
	private void refreshListsAndMaps(String function, DynamoTransaction dynamoTransaction)
	{
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullTransactionsMap().remove(dynamoTransaction.getTransactionID());
			
			List<DynamoTransaction> found = new ArrayList<>();
			List<DynamoTransaction> accountTrxList = this.getFullTransactionsMapByAccountID().get(dynamoTransaction.getAccountID());
			List<DynamoTransaction> newList = this.getFullTransactionsMapByAccountID().get(dynamoTransaction.getAccountID());
			for (int i = 0; i < accountTrxList.size(); i++) 
			{
				DynamoTransaction trx = accountTrxList.get(i);
				if (trx.getTransactionID() == dynamoTransaction.getTransactionID())
				{
					found.add(trx);
					break;
				}
			}
			newList.removeAll(found);
			this.getFullTransactionsMapByAccountID().replace(dynamoTransaction.getAccountID(), newList);	
			
			List<DynamoTransaction> newList2 = resetAccount2YearMap(dynamoTransaction.getAccountID());
			this.getLast2YearsTransactionsMapByAccountID().replace(dynamoTransaction.getAccountID(), newList2);		
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullTransactionsMap().put(dynamoTransaction.getTransactionID(), dynamoTransaction);
			
			if (!this.getFullTransactionsMapByAccountID().containsKey(dynamoTransaction.getAccountID()))
			{
				this.getFullTransactionsMapByAccountID().put(dynamoTransaction.getAccountID(), new ArrayList<DynamoTransaction>());
			}
			ArrayList<DynamoTransaction> newList = new ArrayList<>(this.getFullTransactionsMapByAccountID().get(dynamoTransaction.getAccountID()));
			newList.add(dynamoTransaction);
			this.getFullTransactionsMapByAccountID().replace(dynamoTransaction.getAccountID(), newList);			
			
			List<DynamoTransaction> newList2 = resetAccount2YearMap(dynamoTransaction.getAccountID());
			this.getLast2YearsTransactionsMapByAccountID().replace(dynamoTransaction.getAccountID(), newList2);	
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullTransactionsMap().remove(dynamoTransaction.getTransactionID());
			this.getFullTransactionsMap().put(dynamoTransaction.getTransactionID(), dynamoTransaction);
			
			List<DynamoTransaction> found = new ArrayList<>();
			List<DynamoTransaction> accountTrxList = this.getFullTransactionsMapByAccountID().get(dynamoTransaction.getAccountID());
			List<DynamoTransaction> newList = this.getFullTransactionsMapByAccountID().get(dynamoTransaction.getAccountID());
			for (int i = 0; i < accountTrxList.size(); i++) 
			{
				DynamoTransaction trx = accountTrxList.get(i);
				if (trx.getTransactionID() == dynamoTransaction.getTransactionID())
				{
					found.add(trx);
					break;
				}
			}
			newList.removeAll(found);
			newList.add(dynamoTransaction);
			this.getFullTransactionsMapByAccountID().replace(dynamoTransaction.getAccountID(), newList);
			
			List<DynamoTransaction> newList2 = resetAccount2YearMap(dynamoTransaction.getAccountID());
			this.getLast2YearsTransactionsMapByAccountID().replace(dynamoTransaction.getAccountID(), newList2);		
		}
				
		this.getFullTransactionsList().clear();
		Collection<DynamoTransaction> values = this.getFullTransactionsMap().values();
		this.setFullTransactionsList(new ArrayList<>(values));
		this.getFullTransactionsList().sort(new TransactionComparatorAscDate());

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

	public Map<Integer, String> getWdCategoryMap() {
		return wdCategoryMap;
	}

	public void setWdCategoryMap(Map<Integer, String> wdCategoryMap) {
		this.wdCategoryMap = wdCategoryMap;
	}

	public List<SelectItem> getWdCategoryDropdownList() {
		return wdCategoryDropdownList;
	}

	public void setWdCategoryDropdownList(List<SelectItem> wdCategoryDropdownList) {
		this.wdCategoryDropdownList = wdCategoryDropdownList;
	}

	public Map<Integer, List<DynamoTransaction>> getLast2YearsTransactionsMapByAccountID() 
	{
		return last2YearsTransactionsMapByAccountID;
	}

	public void setLast2YearsTransactionsMapByAccountID(Map<Integer, List<DynamoTransaction>> last2YearsTransactionsMapByAccountID) 
	{
		this.last2YearsTransactionsMapByAccountID = last2YearsTransactionsMapByAccountID;
	}

	public Map<Integer, List<DynamoTransaction>> getFullTransactionsMapByAccountID() {
		return fullTransactionsMapByAccountID;
	}

	public void setFullTransactionsMapByAccountID(Map<Integer, List<DynamoTransaction>> fullTransactionsMapByAccountID) {
		this.fullTransactionsMapByAccountID = fullTransactionsMapByAccountID;
	}

	
}
