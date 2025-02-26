package com.pas.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pas.dynamodb.DynamoTransaction;
import com.pas.slomfin.constants.IAppConstants;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Utils 
{
	private static Logger logger = LogManager.getLogger(Utils.class);	
		
	public static String MY_TIME_ZONE = "America/New_York";
	public static String GREEN_STYLECLASS = "resultGreen";
	public static String RED_STYLECLASS = "resultRed";
	public static String YELLOW_STYLECLASS = "resultYellow";
	
	public static String getLastYearsLastDayDate() 
	{
	    Calendar prevYear = Calendar.getInstance();
	    prevYear.add(Calendar.YEAR, -1);
	    String returnDate = prevYear.get(Calendar.YEAR) + "-12-31";
	    return returnDate;
	}
	
	public static String getOneMonthAgoDate() 
	{
	    Calendar calOneMonthAgo = Calendar.getInstance();
	    calOneMonthAgo.add(Calendar.MONTH, -1);
	    Date dateOneMonthAgo = calOneMonthAgo.getTime();
	    Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
	    String returnDate = formatter.format(dateOneMonthAgo);
	    return returnDate;
	}
	
	public static Date getTwoDaysFromNowDate() 
	{
	    Calendar calTwoDaysAway = Calendar.getInstance();
	    calTwoDaysAway.add(Calendar.DATE, 2);
	    Date returnDate = calTwoDaysAway.getTime();
	    return returnDate;
	}
	
	public static Date getTwoYearsAgoDate() 
	{
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR, -2);
	    Date date = cal.getTime();
	    return date;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isAdminUser()
	{
		boolean adminUser = false;
	
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		for (Iterator<SimpleGrantedAuthority> iterator = authorities.iterator(); iterator.hasNext();) 
		{
			SimpleGrantedAuthority simpleGrantedAuthority = (SimpleGrantedAuthority) iterator.next();
			if (simpleGrantedAuthority.getAuthority().contains("ADMIN"))
			{
				adminUser = true; //admin user can see all the rounds
				break;
			}			
		}
		
		return adminUser;
		
	}
	
	public static String getLoggedInUserName()
	{		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		String currentUser = (String) session.getAttribute("currentUser");
		
		currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		logger.info("current logged in user is: " + currentUser);
		
	    return currentUser == null ? null : currentUser.toLowerCase().trim();
	}	

	public static String getDayofWeekString(Date date) 
	{
		Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("EEEE", locale);
	    return formatter.format(date);
	}
		
	public static String getEncryptedPassword(String unencryptedPassword)
	{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encryptedPW = passwordEncoder.encode(unencryptedPassword);
		return encryptedPW;
	}
	
	public static String getContextRoot()
	{
		String contextRoot = "";
		try
		{
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = request.getSession();	
	        contextRoot = (String) httpSession.getAttribute(IAppConstants.CONTEXT_ROOT);
		}
		catch (Exception e)
		{			
		}
		
		return contextRoot;
	}
	
	public static Map<Integer, String> sortHashMapByValues(Map<Integer, String> hm) 
	{
	    HashMap<Integer, String> temp = hm.entrySet().stream().sorted((i1, i2)-> 
	            i1.getValue().compareTo(i2.getValue())).collect(
	            Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,
	            (e1, e2) -> e1, LinkedHashMap::new));
	    
	    return temp;
	}

	//Assumes input parameter tempList is already sorted.
	public static List<DynamoTransaction> setAccountBalances(List<DynamoTransaction> tempList, BigDecimal startingBalance) 
	{
		//establish balance
	    BigDecimal currentBalance = startingBalance;
	    
	    for (int i = 0; i < tempList.size(); i++)
	    {
	    	DynamoTransaction trx = tempList.get(i);
	    	
	    	if (trx.getCostProceeds() != null)
	    	{
	    		if (trx.getTransactionTypeDescription().equalsIgnoreCase("Buy"))
	    		{
	    			trx.setTrxStyleClass(RED_STYLECLASS);
		    		currentBalance = currentBalance.subtract(trx.getCostProceeds());
	    		}
	    		else if (trx.getTransactionTypeDescription().equalsIgnoreCase("Sell"))
	    		{
	    			trx.setTrxStyleClass(GREEN_STYLECLASS);		    		
		    		currentBalance = currentBalance.add(trx.getCostProceeds());
	    		}
	    		else if (trx.getTransactionTypeDescription().equalsIgnoreCase("Reinvest"))
	    		{
	    			//do nothing
	    		}
	    		else if (trx.getTrxTypPositiveInd())
		    	{
		    		trx.setTrxStyleClass(GREEN_STYLECLASS);		    		
		    		currentBalance = currentBalance.add(trx.getCostProceeds());
		    	}
		    	else
		    	{
		    		trx.setTrxStyleClass(RED_STYLECLASS);
		    		currentBalance = currentBalance.subtract(trx.getCostProceeds());
		    	}
	    	}
	    	
	    	trx.setCurrentBalance(currentBalance);
	    	
	    	if (currentBalance.compareTo(BigDecimal.ZERO) > 0)
	    	{
	            trx.setBalanceStyleClass(GREEN_STYLECLASS);
	        }
	    	else if (currentBalance.compareTo(BigDecimal.ZERO) < 0)
	    	{
	            trx.setBalanceStyleClass(RED_STYLECLASS);
	        }
	    	
		}
	    	    
		return tempList;
	}
	
	
}
