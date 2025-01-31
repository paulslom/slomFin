package com.pas.slomfin.dao;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.Account;
import com.pas.dynamodb.DynamoClients;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

public class AccountDAO implements Serializable 
{
	@Serial
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(AccountDAO.class);
		
	private Map<Integer,Account> fullAccountsMap = new HashMap<>(); 
	private List<Account> fullAccountsList = new ArrayList<>();
	
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<Account> accountsTable;
	private static final String AWS_TABLE_NAME = "SlomFinAccounts";
		
	public AccountDAO(DynamoClients dynamoClients2) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       accountsTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(Account.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing AccountsDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public Integer addAccount(Account account) throws Exception
	{
		Account account2 = dynamoUpsert(account);		
		 
		account.setiAccountID(account2.getiAccountID());
		
		logger.info("LoggedDBOperation: function-add; table:account; rows:1");
		
		refreshListsAndMaps("add", account);	
				
		logger.info("addAccount complete");		
		
		return account2.getiAccountID(); //this is the key that was just added
	}
	
	private Account dynamoUpsert(Account account) throws Exception 
	{
		Account dynamoAccount = new Account();
        
		if (account.getiAccountID() == null)
		{
			Integer currentMaxAccountID = 0;
			for (int i = 0; i < this.getFullAccountsList().size(); i++) 
			{
				Account account2 = this.getFullAccountsList().get(i);
				currentMaxAccountID = account2.getiAccountID();
			}
			dynamoAccount.setiAccountID(currentMaxAccountID + 1);
		}
		else
		{
			dynamoAccount.setiAccountID(account.getiAccountID());
		}
				
		PutItemEnhancedRequest<Account> putItemEnhancedRequest = PutItemEnhancedRequest.builder(Account.class).item(dynamoAccount).build();
		accountsTable.putItem(putItemEnhancedRequest);
			
		return dynamoAccount;
	}

	public void updateAccount(Account account)  throws Exception
	{
		dynamoUpsert(account);		
			
		logger.info("LoggedDBOperation: function-update; table:account; rows:1");
		
		refreshListsAndMaps("update", account);	
		
		logger.debug("update account table complete");		
	}
	
	public void readAccountsFromDB()
    {
		Iterator<Account> results = accountsTable.scan().items().iterator();
		
		while (results.hasNext()) 
        {
			Account account = results.next();
            this.getFullAccountsList().add(account);            
        }
		
		logger.info("LoggedDBOperation: function-inquiry; table:account; rows:" + this.getFullAccountsList().size());
		
		this.setFullAccountsMap(this.getFullAccountsList().stream().collect(Collectors.toMap(Account::getiAccountID, ply -> ply)));
		
		this.getFullAccountsList().sort(new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                return o1.getsAccountName().compareTo(o2.getsAccountName());
            }
        });
	}
	
	private void refreshListsAndMaps(String function, Account account)
	{
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullAccountsMap().remove(account.getiAccountID());		
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullAccountsMap().put(account.getiAccountID(), account);		
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullAccountsMap().remove(account.getiAccountID());		
			this.getFullAccountsMap().put(account.getiAccountID(), account);	
		}
		
		this.getFullAccountsList().clear();
		Collection<Account> values = this.getFullAccountsMap().values();
		this.setFullAccountsList(new ArrayList<>(values));
		
		Collections.sort(this.getFullAccountsList(), new Comparator<Account>() 
		{
		   public int compare(Account o1, Account o2) 
		   {
		      return o1.getiAccountID().compareTo(o2.getiAccountID());
		   }
		});
				
	}
	
	public List<Account> getFullAccountsList() 
	{
		return fullAccountsList;
	}

	public void setFullAccountsList(List<Account> fullAccountsList) 
	{
		this.fullAccountsList = fullAccountsList;
	}

	public Map<Integer, Account> getFullAccountsMap() {
		return fullAccountsMap;
	}

	public void setFullAccountsMap(Map<Integer, Account> fullAccountsMap) {
		this.fullAccountsMap = fullAccountsMap;
	}

	public Account getAccountByAccountID(int accountId) 
	{
		return this.getFullAccountsMap().get(accountId);
	}

	public List<Account> getActiveTaxableAccountsList()
	{
		List<Account> activeTaxableAccountsList = new ArrayList<>();
		for (int i = 0; i < this.getFullAccountsList().size(); i++)
		{
			Account account = this.getFullAccountsList().get(i);
			if (account.getbTaxableInd() && !account.getbClosed())
			{
				activeTaxableAccountsList.add(account);
			}
		} 
		return activeTaxableAccountsList;
	}

	public List<Account> getActiveRetirementAccountsList()
	{
		List<Account> activeRetirementAccountsList = new ArrayList<>();
		for (int i = 0; i < this.getFullAccountsList().size(); i++)
		{
			Account account = this.getFullAccountsList().get(i);
			if (!account.getbTaxableInd() && !account.getbClosed())
			{
				activeRetirementAccountsList.add(account);
			}
		}
		return activeRetirementAccountsList;
	}

	public List<Account> getClosedAccountsList()
	{
		List<Account> closedAccountsList = new ArrayList<>();
		for (int i = 0; i < this.getFullAccountsList().size(); i++)
		{
			Account account = this.getFullAccountsList().get(i);
			if (account.getbClosed())
			{
				closedAccountsList.add(account);
			}
		}
		return closedAccountsList;
	}

}
