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
import com.pas.dbobjects.Tblmortgagehistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.MortgagePaymentSelection;

public class MortgagePaymentListAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside MortgagePaymentListAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		MortgagePaymentSelection mortgagePaymentSelection = new MortgagePaymentSelection();
		mortgagePaymentSelection.setMortgageID(Integer.parseInt(req.getParameter("mortgageID")));
		cache.setObject("RequestObject", mortgagePaymentSelection, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside MortgagePaymentListAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tblmortgagehistory> mortgagePaymentList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		//Update ActionForm with Value Object returned from Business Layer
		
		if (mortgagePaymentList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
					
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_MORTGAGEPAYMENTLIST, mortgagePaymentList);	
			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting MortgagePaymentListAction postprocessAction");
				
	}

}
