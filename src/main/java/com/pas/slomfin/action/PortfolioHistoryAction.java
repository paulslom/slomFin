package com.pas.slomfin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.valueObject.IDObject;
import com.pas.slomfin.valueObject.Investor;

public class PortfolioHistoryAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PortfolioHistoryAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		IDObject idObject = new IDObject();
		Investor investor = cache.getInvestor(req.getSession());

		idObject.setId(investor.getInvestorID());
		
		cache.setObject("RequestObject", idObject, req.getSession());	
		
		cache.setGoToDBInd(req.getSession(), true);
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PortfolioHistoryAction postprocessAction");
								
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PortfolioHistoryAction postprocessAction");
				
	}

}
