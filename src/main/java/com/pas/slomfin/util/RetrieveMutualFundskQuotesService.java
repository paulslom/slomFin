package com.pas.slomfin.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
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

public class RetrieveMutualFundskQuotesService
{
	// example call:   https://eodhistoricaldata.com/api/eod/FXAIX.US?api_token=63b9b590c730b8.91627795
	
	final static String baseURI = "https://eodhistoricaldata.com/api/eod/qqqq.US";
	protected static Logger log = LogManager.getLogger(RetrieveMutualFundskQuotesService.class);  
	final static int TIMEOUT = 5000; //5 seconds
	
	String specificURI = "";	
	
	private static String apiKey = "63b9b590c730b8.91627795";
	
	public BigDecimal getMutualFundQuote(String tickerSymbol, String marketCloseDate) 
	{
		BigDecimal price = new BigDecimal(0.00);
		
		specificURI = baseURI.replace("qqqq", tickerSymbol);
	 	
		log.info( "about to try eodhistorical quote API call with this URI: " + specificURI);					    
	    
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
		     
		    log.info( "successful Eodhistorical quote API call with this URI: " + specificURI);
		    
		    if (restResult != null 
    	    &&  restResult.getBody() != null		
    	    &&  restResult.getStatusCode() != null 
    	    && restResult.getStatusCode().is2xxSuccessful())
    	    {
    	    	if (restResult.getBody().length() == 0)
    	    	{
    	       		log.error("UN-successful Eodhistorical quote API call with this URI: " + specificURI); //no data is a fail 
    	    	}
    	       	else
    	       	{
    	       		String response = restResult.getBody(); //now parse that response.  need to loop until you get to market close date  	       		
    	       	 
    	       		//the format of this is one year of data - one line per business day of quotes.  So the last line is going to be the one we want.
    	       		//do a find of the date and then take the remainder of the string
    	       		
    	       		int quotedDayIndex = response.indexOf(marketCloseDate);
    	       		String dayQuote = response.substring(quotedDayIndex);
    				log.info("quoted day string = " + dayQuote);  
    				
    				StringTokenizer st = new StringTokenizer(dayQuote, ",");
    				st.nextToken(); //first token is date
    				st.nextToken(); //second token is open
    				st.nextToken(); //third token is high
    				st.nextToken(); //fourth token is low
    				String strPrice = st.nextToken(); //fifth token is close; this is the one we want
    				price = new BigDecimal(strPrice);
    				
    	       		log.info("Ticker symbol: " + tickerSymbol + "; Price: " + price);
    	       	}
    	    }			    
	    }
	    catch (Exception e)
	    {
	    	log.error("UN-successful Eodhistorical quote API call with this URI: " + specificURI, e); 
	    }	    
			
		return price;		
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
	
}