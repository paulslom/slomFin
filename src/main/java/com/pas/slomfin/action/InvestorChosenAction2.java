package com.pas.slomfin.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltransactioncommon;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;

/**
 * Title: 		InvestorChosenAction2
 * Description: Verifies User Login to the Portfolio database
 * Copyright: 	Copyright (c) 2006
 */
public class InvestorChosenAction2 extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestorChosenAction2 pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		Investor investor = cache.getInvestor(req.getSession());
				
		cache.setObject("RequestObject", investor, req.getSession());
		
		cache.setGoToDBInd(req.getSession(), true);
		
		log.debug("exiting InvestorChosenAction2 pre - process");
		
		return true;
    }
    
    @SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestorChosenAction2 postprocessAction");
	
		//get Value object from the request - should just be a single investor object
		
		ICacheManager cache = CacheManagerFactory.getCacheManager();
		List<Tbltransactioncommon> transactionCommonList = (List)cache.getObject("ResponseObject", req.getSession());		
			
		//put the list into session for the jsp to use with DisplayTag
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_TRXCOMMONLIST, transactionCommonList);
		
		//set the action forward
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		
		log.debug("exiting InvestorChosenAction2 postprocessAction");
		
		
	}
}
