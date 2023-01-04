package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblinvestment;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.InvestmentsOwnedUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.util.RetrieveStockQuotesService;
import com.pas.slomfin.valueObject.Investor;

public class InvestmentsOwnedShowFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestmentsOwnedShowFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = new Investor();
		investor = cache.getInvestor(req.getSession());		
		cache.setObject("RequestObject", investor, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
		
		log.debug("exiting InvestmentsOwnedShowFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestmentsOwnedShowFormAction postprocessAction");	
			
		//populate the list in the form object from the investmentsOwnedList retrieved by this action
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tblinvestment> investmentsOwnedList = null;
		investmentsOwnedList = (List<Tblinvestment>)cache.getObject("ResponseObject",req.getSession());		
		
		List<Tblinvestment> investmentsOwnedListWithUpdatedPrices = new ArrayList<>();
		
		try 
		{
			investmentsOwnedListWithUpdatedPrices = assignStockPrices(investmentsOwnedList);
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
			throw new PASSystemException(e.getMessage());
		}
		
		InvestmentsOwnedUpdateForm investmentsOwnedUpdateForm = (InvestmentsOwnedUpdateForm)form; 
				
		investmentsOwnedUpdateForm.setInvOwnedList(investmentsOwnedListWithUpdatedPrices); 
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting InvestmentsOwnedShowFormAction postprocessAction");
				
	}
	
	private List<Tblinvestment> assignStockPrices(List<Tblinvestment> inputInvList) throws Exception
	{
		List<Tblinvestment> returnList = new ArrayList<Tblinvestment>();
		
		RetrieveStockQuotesService rqs = new RetrieveStockQuotesService();
		
		String marketCloseDate = getMarketCloseDate();
				
		int stocksCounter = 0;
		
		for (int i = 0; i < inputInvList.size(); i++) 
		{
			Tblinvestment inv = inputInvList.get(i); 
			if (inv.getTblinvestmenttype().getSdescription().equalsIgnoreCase("Stock"))
			{
				stocksCounter++;
				log.debug("quoting stock: " + inv.getStickerSymbol() + " - count = " + stocksCounter);
				BigDecimal stockprice = rqs.getStockQuote(inv.getStickerSymbol(), marketCloseDate);
				inv.setMcurrentPrice(stockprice);
				
				//can only do 5 api calls per minute (under their free plan) so need to sleep for a bit before resuming this...
				if (stocksCounter % 5 == 0)
				{
					TimeUnit.SECONDS.sleep(70);					
				}
			}
			
			returnList.add(inv);			
		}
		
		return returnList;
	}
	private String getMarketCloseDate() throws Exception
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
		RetrieveStockQuotesService rqs = new RetrieveStockQuotesService();
		boolean dateNotFound = true;
		String returnDateString = "";
		
		int count = 0;
		
		while (dateNotFound && count < 5)
		{			
			Date calReturnDate = calReturn.getTime();
		
			String attemptDateString = sdf.format(calReturnDate);			
			BigDecimal stockprice = rqs.getStockQuote("AAPL", attemptDateString);
			
			BigDecimal zero = BigDecimal.ZERO;
		    if (stockprice.compareTo(zero) > 0)
		    {
		    	dateNotFound = false;
		    	returnDateString = sdf.format(calReturnDate);	
		    }
		    else
		    {
		    	calReturn.add(Calendar.DAY_OF_MONTH, -1);  //send back one day	
		    	count++;
		    }
		    
		}
		
		if (count == 5)
		{
			throw new Exception("unable to get a quote for AAPL for last 5 business days.  Something is wrong with the polygon service!");
		}
		return returnDateString;
	}
						
}
