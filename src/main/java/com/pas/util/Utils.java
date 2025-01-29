package com.pas.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

public class Utils 
{
	private static Logger logger = LogManager.getLogger(Utils.class);	
		
	public static String MY_TIME_ZONE = "America/New_York";
	
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
	
	public static String getTwoYearsAgoDate() 
	{
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR, -2);
	    Date date = cal.getTime();
	    Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
	    String returnDate = formatter.format(date);
	    return returnDate;
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
		
	
	
}
