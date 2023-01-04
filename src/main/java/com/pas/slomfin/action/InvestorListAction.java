package com.pas.slomfin.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblinvestor;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

/**
 * Title: 		InvestorListAction
 * Description: User has chosen which investor to work on - send them to that investor's screen
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class InvestorListAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside InvestorListAction pre - process");
				
		//need to send something other than an investor into InvestorDAO inquire
		//so that it will not go after that particular investor, but instead the whole list.
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		cache.setObject("RequestObject", new Integer(0), req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
		
		log.debug("exiting InvestorListAction pre - process");
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside InvestorListAction postprocessAction");
	
		//get Value object from the request
		ICacheManager cache = CacheManagerFactory.getCacheManager();
		List<Tblinvestor> reqObj = (List)cache.getObject("ResponseObject", req.getSession());
		
		//Update ActionForm with Value Object returned from Business Layer
		
		req.setAttribute("investorList", reqObj);
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		
		log.debug("exiting InvestorListAction postprocessAction");
		
		
	}
}
