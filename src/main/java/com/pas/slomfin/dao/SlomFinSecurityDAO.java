package com.pas.slomfin.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.SlomFinMain;
import com.pas.beans.AppSecurity;
import com.pas.dynamodb.DynamoClients;
import com.pas.util.Utils;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

public class SlomFinSecurityDAO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SlomFinSecurityDAO.class);
	
	private Map<String,AppSecurity> fullUserMap = new HashMap<>();

	private static DynamoClients dynamoClients;
	private static DynamoDbTable<AppSecurity> usersTable;
	private static final String AWS_TABLE_NAME = "nflsecurity";
	
	public SlomFinSecurityDAO(DynamoClients dynamoClients2, SlomFinMain nflmain) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       usersTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(AppSecurity.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing NflSecurityDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public void readAllUsersFromDB() throws Exception
	{				
		Iterator<AppSecurity> results = usersTable.scan().items().iterator();
            
        while (results.hasNext()) 
        {
            AppSecurity gu = results.next();
            
            if (this.getFullUserMap().containsKey(gu.getUserName()))
			{
				logger.error("duplicate user: " + gu.getUserName());
			}
			else
			{
				this.getFullUserMap().put(gu.getUserName(), gu);				
			}
        }
          	
		logger.info("LoggedDBOperation: function-inquiry; table:nflsecurity; rows:" + this.getFullUserMap().size());
				
		//this loop only for debugging purposes
		/*
		for (Map.Entry<String, NflSecurity> entry : this.getFullUserMap().entrySet()) 
		{
		    String key = entry.getKey();
		    NflSecurity golfUser = entry.getValue();

		    logger.info("Key = " + key + ", value = " + golfUser.getUserName());
		}
		*/
		
		logger.info("exiting");
		
	}
		
	public AppSecurity getNflSecurity(String username)
    {	    	
		AppSecurity gu = this.getFullUserMap().get(username);			
    	return gu;
    }	
	
	private void deleteUser(String username) throws Exception
	{
		Key key = Key.builder().partitionValue(username).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		usersTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:nflsecurity; rows:1");
		
		AppSecurity gu = new AppSecurity();
		gu.setUserName(username);
		refreshListsAndMaps("delete", gu);	
	}
	
	public void addUser(AppSecurity gu, String pw) throws Exception
	{
		String encodedPW = "";
		
		if (pw == null || pw.trim().length() == 0)
		{	
			encodedPW = Utils.getEncryptedPassword(gu.getUserName());
		}
		else
		{
			encodedPW = Utils.getEncryptedPassword(pw);
			
		}
		
		gu.setPassword(encodedPW);		
		
		PutItemEnhancedRequest<AppSecurity> putItemEnhancedRequest = PutItemEnhancedRequest.builder(AppSecurity.class).item(gu).build();
		usersTable.putItem(putItemEnhancedRequest);
			
		logger.info("LoggedDBOperation: function-update; table:nflsecurity; rows:1");
					
		refreshListsAndMaps("add", gu);	
	}
	
	public void updateUser(AppSecurity gu) throws Exception
	{
		deleteUser(gu.getUserName());
		addUser(gu, gu.getPassword());		
		refreshListsAndMaps("update", gu);	
	}

	private void refreshListsAndMaps(String function, AppSecurity golfuser) 
	{
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullUserMap().remove(golfuser.getUserName());	
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullUserMap().put(golfuser.getUserName(), golfuser);	
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullUserMap().remove(golfuser.getUserName());	
			this.getFullUserMap().put(golfuser.getUserName(), golfuser);		
		}
		
	}
	
	public Map<String, AppSecurity> getFullUserMap() 
	{
		return fullUserMap;
	}

	public void setFullUserMap(Map<String, AppSecurity> fullUserMap) 
	{
		this.fullUserMap = fullUserMap;
	}

	
}
