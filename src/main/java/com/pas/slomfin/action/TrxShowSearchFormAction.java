package com.pas.slomfin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.TrxSearchForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.TransactionSearch;

public class TrxShowSearchFormAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TrxShowSearchFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		TransactionSearch trxSearch = new TransactionSearch();
		
		TrxSearchForm trxSearchForm = (TrxSearchForm)form;
		
		trxSearch.setTrxSearchInvestorID(new Integer(cache.getInvestor(req.getSession()).getInvestorID()));
		
		if (!(trxSearchForm.getFromDate() == null))
			if (!(trxSearchForm.getFromDate().isEmpty()))
				trxSearch.setFromDate(trxSearchForm.getFromDate().toStringYYYYMMDD());
		
		if (!(trxSearchForm.getToDate() == null))
			if (!(trxSearchForm.getToDate().isEmpty()))
				trxSearch.setToDate(trxSearchForm.getToDate().toStringYYYYMMDD());				
		
		
		trxSearch.setTrxDescriptionText(trxSearchForm.getTrxSearchText());
		cache.setGoToDBInd(req.getSession(), true);		
		cache.setObject("RequestObject", trxSearch, req.getSession());
				
		log.debug("exiting TrxShowSearchFormAction preprocessAction");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TrxShowSearchFormAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tbltransaction> trxList = (List<Tbltransaction>)cache.getObject("ResponseObject",req.getSession());		
		
		//Update ActionForm with Value Object returned from Business Layer
		
		TrxSearchForm trxSearchForm = (TrxSearchForm)form;
		
		if (trxList.size() < 1)
			if (!(trxSearchForm.getTrxSearchText() == null))
				if (!(trxSearchForm.getTrxSearchText().isEmpty()))
				{	
					ActionMessages am = new ActionMessages();
					am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
					ac.setMessages(am);
					ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));
					return;
				}	
				
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_TRXLIST, trxList);	
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
	
		
		log.debug("exiting TrxShowSearchFormAction postprocessAction");
				
	}

}
