package com.pas.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.cj.jdbc.MysqlDataSource;

public class Utils 
{
	private static Logger log = LogManager.getLogger(Utils.class);	
		
	public static MysqlDataSource getDatasourceProperties()
	{
		MysqlDataSource ds = null;
		
       	if (System.getProperty("PORTFOLIO_HOSTNAME") != null) 
   		{
   		    try 
   		    {
   		    	ds = new MysqlDataSource();
   		    	
   			    String dbName = System.getProperty("PORTFOLIO_DB_NAME");
   			    String userName = System.getProperty("PORTFOLIO_USERNAME");
   			    String password = System.getProperty("PORTFOLIO_PASSWORD");
   			    String hostname = System.getProperty("PORTFOLIO_HOSTNAME");
   			    String port = System.getProperty("PORTFOLIO_PORT");
   			    String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
   			    
   			    log.info("jdbcUrl for datasource: " + jdbcUrl);
   			    
   			    ds.setURL(jdbcUrl);
   			    ds.setPassword(password);
   			    ds.setUser(userName);   			    
   			  }
   			  catch (Exception e) 
   		      { 
   				  log.error(e.toString());
   			  }   			  
   		}
       	
       	return ds;
	}
	
	public static String getDayofWeekString(Date date) 
	{
		Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("EEEE", locale);
	    return formatter.format(date);
	}
		
}
