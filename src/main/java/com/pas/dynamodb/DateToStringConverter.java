package com.pas.dynamodb;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.pas.util.Utils;

public class DateToStringConverter
{
	public static String dynamoDbDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss";
	public static String dynamoDbDateTimePatternNoSeconds = "yyyy-MM-dd'T'HH:mm";
	
	public static String mysqlDateTimePattern2 = "yyyy-MM-dd HH:mm:ss";  //example: 2004-09-09 21:12:13
	public static String mysqlDateTimePattern1 = "yyyy-MM-dd HH:mm:ss.s";  //example: 2004-09-09 21:00:00.0
	
	public static Date unconvert(String s) 
    {
    	//Example of what we are unconverting: 2020-03-21T00:00:00.000-04:00
		Date returnDate = null;
		   	
    	try
    	{
    		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(dynamoDbDateTimePattern, Locale.ENGLISH);
    		LocalDateTime ldt = LocalDateTime.parse(s, inputFormatter);
    		returnDate = Date.from(ldt.atZone(ZoneId.of(Utils.MY_TIME_ZONE)).toInstant());
    	}
    	catch (Exception e)
    	{
    		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(dynamoDbDateTimePatternNoSeconds, Locale.ENGLISH);
    		LocalDateTime ldt = LocalDateTime.parse(s, inputFormatter);
    		returnDate = Date.from(ldt.atZone(ZoneId.of(Utils.MY_TIME_ZONE)).toInstant());
    	}
        return returnDate;
    }
    
    public static String convertDateToDynamoStringFormat(Date inputDate)
    {
    	String returnString = "";
    	SimpleDateFormat sdf = new SimpleDateFormat(dynamoDbDateTimePattern);
    	sdf.setTimeZone(TimeZone.getTimeZone(Utils.MY_TIME_ZONE));
		returnString = sdf.format(inputDate);
    	return returnString;
    }
    
    public static String convertMySqlDateTimeToDynamoStringFormat(String inputMySqlTimestamp)
    {
    	String returnString = "";
    	
    	if (inputMySqlTimestamp != null)    	
		{
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mysqlDateTimePattern2); 
    		LocalDateTime ldt = LocalDateTime.parse(inputMySqlTimestamp, dtf);
    		returnString = ldt.toString();
		}
       	
       	return returnString;
    }
    
    public static void main(String[] args)
    {
    	/*
    	String dateTimeString = "Mon 2025-01-13 08:00 PM";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(userInputDateTimePattern);  
    	LocalDateTime datetime = LocalDateTime.parse(dateTimeString, dtf); 
    	System.out.println("date format : " + datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    	
    	
    	String dateTimeString = "2025-01-13 20:00:00";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mysqlDateTimePattern);  
    	LocalDateTime datetime = LocalDateTime.parse(dateTimeString, dtf); 
    	System.out.println("date format : " + datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    	*/    	
    	
    	String inputMySqlTimestamp = "2024-09-08 13:10:20";
    	String inputMySqlTimestamp2 = "2024-09-08 13:10:20.0";
    	String returnString = "";
    	
    	try
    	{
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mysqlDateTimePattern2); 
    		LocalDateTime ldt = LocalDateTime.parse(inputMySqlTimestamp2, dtf);
           	returnString = ldt.toString();
    	}
    	catch (Exception e)
    	{
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mysqlDateTimePattern2); 
    		LocalDateTime ldt = LocalDateTime.parse(inputMySqlTimestamp, dtf);
    		returnString = ldt.toString();
    	}
    	
    	System.out.println(returnString);
    	
    }
}
