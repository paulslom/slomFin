package com.pas.slomfin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblaccount;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.AccountSelection;

public class AccountListAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside AccountListAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		AccountSelection accountSelection = new AccountSelection();
		String accStatus = req.getParameter("accStatus");
		if (accStatus == null)
			accountSelection = cache.getAccountSelection(req.getSession());
		else
		{
			boolean isAccountClosed = false;
			if (accStatus.equalsIgnoreCase("closed"))
				isAccountClosed = true;
			String portfolioID = req.getParameter("portfolioID");
			int intPortfolioID = Integer.parseInt(portfolioID);
			accountSelection.setPortfolioID(intPortfolioID);
			accountSelection.setClosed(isAccountClosed);
			cache.setAccountSelection(req.getSession(), accountSelection);
		}
		
		cache.setGoToDBInd(req.getSession(), true);
		cache.setObject("RequestObject", accountSelection, req.getSession());
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside AccountListAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tblaccount> AccountList = (List<Tblaccount>)cache.getObject("ResponseObject",req.getSession());		
		
		//Update ActionForm with Value Object returned from Business Layer
		
		if (AccountList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
					
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_ACCOUNTLIST, AccountList);	
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting AccountListAction postprocessAction");
				
	}

}
