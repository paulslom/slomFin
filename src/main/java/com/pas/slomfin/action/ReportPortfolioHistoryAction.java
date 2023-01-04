package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.ReportPortfolioHistoryForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.PortfolioHistory;

public class ReportPortfolioHistoryAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportPortfolioHistoryAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());			
		cache.setObject("RequestObject", investor, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportPortfolioHistoryAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<PortfolioHistory> dataLayerList = (List)cache.getObject("ResponseObject",req.getSession());	
		ArrayList<PortfolioHistory> phList = new ArrayList<PortfolioHistory>();
		
		if (dataLayerList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else //list has more than 1 item - loop through it to populate the yearly items
		{
			BigDecimal yearlyGainLossSubTotal = new BigDecimal("0.00");
			BigDecimal firstYearlyAmount = new BigDecimal("0.00");
			BigDecimal percentGainLoss = new BigDecimal("0.00");
			BigDecimal oneHundred = new BigDecimal("100.00");
			
			yearlyGainLossSubTotal = yearlyGainLossSubTotal.setScale(4, java.math.RoundingMode.HALF_UP); 
			firstYearlyAmount = firstYearlyAmount.setScale(4, java.math.RoundingMode.HALF_UP); 
			percentGainLoss = percentGainLoss.setScale(4, java.math.RoundingMode.HALF_UP); 
			oneHundred = oneHundred.setScale(4, java.math.RoundingMode.HALF_UP); 
			
			int previousYear = 1900;
			int phYear = 0;
			int i;
			
			for (i=0; i < dataLayerList.size(); i++)
			{
				PortfolioHistory ph1 = (PortfolioHistory)dataLayerList.get(i);
								
				phList.add(ph1);
				
				Calendar tempCal = Calendar.getInstance();
				
				tempCal.setTimeInMillis(ph1.getPortfolioHistoryDate().getTime());
								
				phYear = tempCal.get(Calendar.YEAR);
				
				if (phYear != previousYear) //set list items then reset total
				{
					if (i != 0) //don't want to do anything first time through
					{
						PortfolioHistory ph2 = (PortfolioHistory)dataLayerList.get(i-1); //This is the previous entry
						yearlyGainLossSubTotal = yearlyGainLossSubTotal.add(ph2.getNetWorth().subtract(firstYearlyAmount));
						ph2.setYearlyDollarsGainLoss(yearlyGainLossSubTotal);						
						percentGainLoss = percentGainLoss.multiply(yearlyGainLossSubTotal.divide(firstYearlyAmount,4,java.math.RoundingMode.HALF_UP));
						ph2.setYearlyPercentageGainLoss(percentGainLoss);
						phList.set(i-1, ph2);
					}
					firstYearlyAmount = firstYearlyAmount.subtract(firstYearlyAmount);
					firstYearlyAmount = firstYearlyAmount.add(ph1.getNetWorth());
					yearlyGainLossSubTotal = yearlyGainLossSubTotal.subtract(yearlyGainLossSubTotal);
					percentGainLoss = percentGainLoss.subtract(percentGainLoss);
					percentGainLoss = percentGainLoss.add(oneHundred);
					previousYear = phYear;
					
				}												
			}
			
			//this is for the last item in the list
			
			PortfolioHistory ph2 = (PortfolioHistory)dataLayerList.get(i-1); //This is the previous entry
			yearlyGainLossSubTotal = yearlyGainLossSubTotal.add(ph2.getNetWorth().subtract(firstYearlyAmount));
			ph2.setYearlyDollarsGainLoss(yearlyGainLossSubTotal);						
			percentGainLoss = percentGainLoss.multiply(yearlyGainLossSubTotal.divide(firstYearlyAmount,4,java.math.RoundingMode.HALF_UP));
			ph2.setYearlyPercentageGainLoss(percentGainLoss);
			phList.set(i-1, ph2);
			
		}
					
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTPORTFOLIOHISTORYLIST, phList);	
		
		ReportPortfolioHistoryForm repForm = (ReportPortfolioHistoryForm)form;
		repForm.setReportTitle("Portfolio History");
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportPortfolioHistoryAction postprocessAction");
				
	}

}
