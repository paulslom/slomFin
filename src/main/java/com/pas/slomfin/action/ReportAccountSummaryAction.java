package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.IDObject;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.PortfolioSummary;

public class ReportAccountSummaryAction extends SlomFinStandardAction
{
	private static BigDecimal bigDecimalZero = BigDecimal.ZERO;
	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportAccountSummaryAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		IDObject idObject = new IDObject();
		String investorIDParm = req.getParameter("investorID");
		if (investorIDParm == null) //could be the case if coming from investmentsownedaction
		{
			Investor investor = cache.getInvestor(req.getSession());	
			investorIDParm = investor.getInvestorID();
		}
		idObject.setId(investorIDParm);	
		idObject.setIdDescriptor("ISlomFinAppConstants.SP_PORTFOLIOSUMMARY");
		cache.setObject("RequestObject", idObject, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
						
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportAccountSummaryAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<PortfolioSummary> dataLayerList = (List)cache.getObject("ResponseObject",req.getSession());	
		
		if (dataLayerList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		
		List<PortfolioSummary> accountSummaryList = new ArrayList<>();
		BigDecimal totalAccountValue = bigDecimalZero;
		String workingOnThisAccountName = "xxxx";
				
		for (int i = 0; i < dataLayerList.size(); i++) 
		{			
			PortfolioSummary portfolioSummary = (PortfolioSummary)dataLayerList.get(i);
			String currentAccountName = portfolioSummary.getPortfolioName() + ": " + portfolioSummary.getAccountName();
				
			if (i == 0)
			{
				workingOnThisAccountName = portfolioSummary.getPortfolioName() + ": " + portfolioSummary.getAccountName();
				totalAccountValue = totalAccountValue.add(portfolioSummary.getCurrentValue());
				continue;
			}
			
			if (!currentAccountName.equalsIgnoreCase(workingOnThisAccountName))
			{
				PortfolioSummary accountSummaryEntry = new PortfolioSummary();
				accountSummaryEntry.setAccountName(workingOnThisAccountName);
				accountSummaryEntry.setCurrentValue(totalAccountValue);
				accountSummaryList.add(accountSummaryEntry);
				totalAccountValue = bigDecimalZero;
			}
			
			workingOnThisAccountName = portfolioSummary.getPortfolioName() + ": " + portfolioSummary.getAccountName();
			totalAccountValue = totalAccountValue.add(portfolioSummary.getCurrentValue());			
		}
		
		PortfolioSummary accountSummaryEntry = new PortfolioSummary();
		accountSummaryEntry.setAccountName(workingOnThisAccountName);
		accountSummaryEntry.setCurrentValue(totalAccountValue);
		accountSummaryList.add(accountSummaryEntry);
		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORT_ACCOUNT_SUMMARYLIST, accountSummaryList);	
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportAccountSummaryAction postprocessAction");
				
	}

}
