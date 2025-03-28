package com.pas.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pas.jsonObject.StockQuote;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

public class RetrieveStockQuotesService
{
	// example call:   https://api.polygon.io/v1/open-close/DOCU/2021-10-29?adjusted=true&apiKey=<API_KEY>
	
	final static String baseURI = "https://api.polygon.io/v1/open-close/qqqq/zzzz?adjusted=true";
	protected static Logger log = LogManager.getLogger(RetrieveStockQuotesService.class);  
	final static int TIMEOUT = 5000; //5 seconds
	
	String specificURI = "";	
	
	private static String apiKey = System.getenv("POLYGON_API_KEY");
	
	public BigDecimal getStockQuote(String tickerSymbol, String marketCloseDate) 
	{
		BigDecimal stockPrice = new BigDecimal(0.00);
		
		specificURI = baseURI.replace("qqqq", tickerSymbol);
		specificURI = specificURI.replace("zzzz", marketCloseDate);
	 	
		log.info( "about to try Polygon quote API call with this URI: " + specificURI);					    
	    
	    WebClient webClient = getWebClient(TIMEOUT, TIMEOUT, specificURI);
	    
	    try
	    {
	    	ResponseEntity<String> restResult = webClient.get().uri(specificURI).
		    		 headers(httpHeaders -> {
		                    httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
		                    httpHeaders.setBearerAuth(apiKey);
		                	}).
		                    retrieve().
		                    toEntity(new ParameterizedTypeReference<String>() {
		                    }).
		                    block();
		     
		    log.info( "successful Polygon quote API call with this URI: " + specificURI);
		    
		    if (restResult != null 
    	    &&  restResult.getBody() != null		
    	    &&  restResult.getStatusCode() != null 
    	    && restResult.getStatusCode().is2xxSuccessful())
    	    {
    	    	if (restResult.getBody().length() == 0 || restResult.getBody().equalsIgnoreCase("[]")) //empty array is usually the response if no data
    	    	{
    	       		log.error("UN-successful Polygon quote API call with this URI: " + specificURI); //no data is a fail 
    	    	}
    	       	else
    	       	{
    	       		String jsonResponse = restResult.getBody(); //now parse that json resp
    	       		
    	       	    // Convert JSON to a StockQuote object
    				ObjectMapper objectMapper = new ObjectMapper();		    	
    		    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT); //Set pretty printing of json
    		    	objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    		    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));			    	
    		    	TypeReference<StockQuote> mapType = new TypeReference<StockQuote>() {};
    		    	
    				try 
    				{
    					StockQuote stockQuote = (StockQuote) objectMapper.readValue(jsonResponse, mapType);
    					Double stockPriceTemp = stockQuote.getClose().doubleValue();	
    					stockPrice = new BigDecimal(stockPriceTemp).setScale(4, RoundingMode.DOWN);
    				} 
    				catch (IOException e) 
    				{
    					String message = "error parsing JSON from Stock Quote API";
    					log.error(message);
    					e.printStackTrace();
    				}
    				
    	       		log.info("Ticker symbol: " + tickerSymbol + "; close date: " + marketCloseDate + "; Stock Price: " + stockPrice);
    	       	}
    	    }			    
	    }
	    catch (Exception e)
	    {
	    	log.error("UN-successful Polygon quote API call with this URI: " + specificURI, e); //no data is a fail
	    }	    
			
		return stockPrice;		
	}
	
	private WebClient getWebClient(int connectTimeoutSeconds, int readTimeoutSeconds, String url) 
	{
        final HttpClient httpClient = HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutSeconds * 1000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(readTimeoutSeconds, TimeUnit.SECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(readTimeoutSeconds, TimeUnit.SECONDS));
                });

        return WebClient.builder().baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
	
	public String getMarketCloseDate() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calReturn = Calendar.getInstance();
		
		Calendar calToday = Calendar.getInstance();
		
		int day_of_week = calToday.get(Calendar.DAY_OF_WEEK);
		
		switch (day_of_week)
        {
            case Calendar.MONDAY:
            	
            	 calReturn.add(Calendar.DAY_OF_MONTH, -3);  //send back to Friday	
                 break;
                 
            case Calendar.TUESDAY:             
            case Calendar.WEDNESDAY:               
            case Calendar.THURSDAY:
            case Calendar.FRIDAY:            	
            case Calendar.SATURDAY:
            	
            	 calReturn.add(Calendar.DAY_OF_MONTH, -1);  //send back one day		
                 break;
                
            case Calendar.SUNDAY:
            	
            	calReturn.add(Calendar.DAY_OF_MONTH, -2);  //send back to friday			
                break;
                
            default:                
                break;
        }
		
		//now prove it using AAPL.  Could be a holiday or something so if so keep going backward.
		boolean dateNotFound = true;
		String returnDateString = "";
		
		int count = 0;
		
		while (dateNotFound && count < 5)
		{			
			Date calReturnDate = calReturn.getTime();
		
			String attemptDateString = sdf.format(calReturnDate);			
			BigDecimal stockprice = getStockQuote("AAPL", attemptDateString);
			count++;
			
			BigDecimal zero = BigDecimal.ZERO;
		    if (stockprice.compareTo(zero) > 0)
		    {
		    	dateNotFound = false;
		    	returnDateString = sdf.format(calReturnDate);	
		    }
		    else
		    {
		    	calReturn.add(Calendar.DAY_OF_MONTH, -1);  //send back one day		    	
		    }
		    
		}
		
		if (count == 5)
		{
			throw new Exception("unable to get a quote for AAPL for last 5 business days.  Something is wrong with the polygon service!");
		}
		
		return count + "~" + returnDateString;
	}
	
	public static void main(String[] args)
	{
		/* example call
		https://api.polygon.io/v1/open-close/DOCU/2021-10-29?adjusted=true&apiKey=<API_KEY>
		*/
		
	    String tickerSymbol = "DOCU"; //docusign
	    String marketCloseDate = "2021-10-29"; //closing price for this day was: 278.29
	    
	    BigDecimal stockPrice = new BigDecimal(0.00);
		
		String specificURI = baseURI.replace("qqqq", tickerSymbol);
		specificURI = specificURI.replace("zzzz", marketCloseDate);
	     
	    final HttpClient httpClient = HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT * 1000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS));
                });

	    WebClient webClient =  WebClient.builder().baseUrl(specificURI).clientConnector(new ReactorClientHttpConnector(httpClient)).build();	    
		
		log.info( "about to try Polygon quote API call with this URI: " + specificURI);					    
	 
	    ResponseEntity<String> restResult = webClient.get().uri(specificURI).
	    		 headers(httpHeaders -> {
	                    httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
	                    httpHeaders.setBearerAuth(apiKey);
	                	}).
	                    retrieve().
	                    toEntity(new ParameterizedTypeReference<String>() {
	                    }).
	                    block();
	     
	    log.info( "successful Polygon quote API call with this URI: " + specificURI);
		
	    
	    if (restResult != null 
	    &&  restResult.getBody() != null		
	    &&  restResult.getStatusCode() != null 
	    && restResult.getStatusCode().is2xxSuccessful())
	    {
	       	if (restResult.getBody().length() == 0 || restResult.getBody().equalsIgnoreCase("[]")) //empty array is usually the response if no data
	    	{
	       		log.error("UN-successful Polygon quote API call with this URI: " + specificURI); //no data is a fail 
	    	}
	       	else
	       	{
	       		String jsonResponse = restResult.getBody(); //now parse that json resp
	       		
	       	    // Convert JSON to a StockQuote object
				ObjectMapper objectMapper = new ObjectMapper();		    	
		    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT); //Set pretty printing of json
		    	objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));			    	
		    	TypeReference<StockQuote> mapType = new TypeReference<StockQuote>() {};
		    	
				try 
				{
					StockQuote stockQuote = (StockQuote) objectMapper.readValue(jsonResponse, mapType);
					stockPrice = stockQuote.getClose();					
				} 
				catch (IOException e) 
				{
					String message = "error parsing JSON from Stock Quote API";
					log.error(message);
					e.printStackTrace();
				}
				
	       		System.out.println("Ticker symbol: " + tickerSymbol);
	    	    System.out.println("Quote Date: " + marketCloseDate);
	    	    System.out.println("Stock Price: " + stockPrice);
	       	}
	    }
	    
	   		
	}

}