package com.pas.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pas.beans.AppSecurity;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;
import com.pas.slomfin.dao.SlomFinSecurityDAO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService 
{
	  private static Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);	
	  
	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	  {
		  logger.info("User " + username + " attempting to log in");
		  
		  DynamoClients dynamoClients;
		  AppSecurity appSecurity = null;
		  
		  try 
		  {
			  dynamoClients = DynamoUtil.getDynamoClients();
			  SlomFinSecurityDAO slomFinSecurityDao = new SlomFinSecurityDAO(dynamoClients, null);
			  slomFinSecurityDao.readAllUsersFromDB();
		      appSecurity = slomFinSecurityDao.getNflSecurity(username);
		  } 
		  catch (Exception e) 
		  {
			  logger.error("Exception loading security table: " + e.getMessage());
		  }
	      

	      UserBuilder builder = null;
	   
	     if (appSecurity != null && appSecurity.getUserName() != null && !appSecurity.getUserName().trim().isEmpty())
	     {
	         builder = org.springframework.security.core.userdetails.User.withUsername(username);
	         builder.password(appSecurity.getPassword());
	         
	         logger.info("User " + username + " successfully found on database as " + appSecurity.getUserName());
	     } 
	     else 
	     {
             logger.info("User {} not found on database.", username);
	         throw new UsernameNotFoundException("User not found.");
	     }

	     return builder.build();
	  }
	  
}