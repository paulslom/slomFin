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
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.TrxListForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.TransactionSelection;
import com.pas.valueObject.AppDate;

public class TrxListAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TrxListAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		TransactionSelection trxSel = new TransactionSelection();
				
		String trxListOrigin = req.getParameter("trxListOrigin");
		int intTrxListOrigin;
		intTrxListOrigin = Integer.parseInt(trxListOrigin);
				
		switch (intTrxListOrigin)
		{
			case ISlomFinAppConstants.TRXLIST_ORIGIN_MENU:
				log.debug("trxList Origin was a menu selection");
				String accountIDParm = req.getParameter("accountID");
				trxSel.setAccountID(Integer.parseInt(accountIDParm));
				break;
			case ISlomFinAppConstants.TRXLIST_ORIGIN_CASHSPENT:
				log.debug("trxList Origin was CashSpent - back to TrxList after entering cash spent");
				trxSel = cache.getTransactionSelection(req.getSession());
				break;
			case ISlomFinAppConstants.TRXLIST_ORIGIN_TRXLIST:
				log.debug("trxList Origin was TrxList - this happens on a date search");
				TrxListForm trxListForm = (TrxListForm) form;
				trxSel.setAccountID(trxListForm.getAccountID());
				if (!(trxListForm.getFromDate() == null))
					if (!(trxListForm.getFromDate().isEmpty()))
						trxSel.setFromDate(trxListForm.getFromDate().toStringYYYYMMDD());
				if (!(trxListForm.getToDate() == null))
					if (!(trxListForm.getToDate().isEmpty()))
						trxSel.setToDate(trxListForm.getToDate().toStringYYYYMMDD());
				break;
			case ISlomFinAppConstants.TRXLIST_ORIGIN_TRXVIEWCHGDEL:
				log.debug("trxList Origin was TrxViewChgDel");
				trxSel = cache.getTransactionSelection(req.getSession());
				break;
			case ISlomFinAppConstants.TRXLIST_ORIGIN_TRXADD:
				log.debug("trxList Origin was TrxAdd");
				trxSel = cache.getTransactionSelection(req.getSession());
				break;
			default:
				log.error("Invalid intTrxListOrigin = " + intTrxListOrigin);
				break;			
					
    	}
		
		trxSel.setRecentInd(true);
		log.debug("trxSel.setRecentInd set to true");
		
		cache.setGoToDBInd(req.getSession(), true);
		cache.setObject("RequestObject", trxSel, req.getSession());
		cache.setTransactionSelection(req.getSession(),trxSel);
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TrxListAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tbltransaction> trxList = (List<Tbltransaction>)cache.getObject("ResponseObject",req.getSession());		
		
		//Update ActionForm with Value Object returned from Business Layer
		
		if (trxList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{	
			Tbltransaction trx = trxList.get(0);
			TrxListForm trxListForm = (TrxListForm)form; 
			
			trxListForm.setPortfolioName(trx.getTblaccount().getTblportfolio().getSportfolioName());
			trxListForm.setAccountName(trx.getTblaccount().getSaccountName());
			trxListForm.setAccountID(trx.getTblaccount().getIaccountId());
			
			String trxListOrigin = req.getParameter("trxListOrigin");
			int intTrxListOrigin;
			intTrxListOrigin = Integer.parseInt(trxListOrigin);
			
			if (intTrxListOrigin == ISlomFinAppConstants.TRXLIST_ORIGIN_TRXLIST
			|| intTrxListOrigin == ISlomFinAppConstants.TRXLIST_ORIGIN_TRXVIEWCHGDEL)
			{
				TransactionSelection trxSel = new TransactionSelection();
				trxSel = cache.getTransactionSelection(req.getSession());
				
				if (trxSel.getFromDate() == null)
				   trxListForm.setFromDate(null);
				else
				{
					AppDate appDateFrom = new AppDate();
					appDateFrom.setAppmonth(trxSel.getFromDate().substring(0,2));
					appDateFrom.setAppday(trxSel.getFromDate().substring(3,5));					
					appDateFrom.setAppyear(trxSel.getFromDate().substring(6,10));
					trxListForm.setFromDate(appDateFrom);
				}
				
				if (trxSel.getToDate() == null)
					trxListForm.setToDate(null);
				else
				{
					AppDate appDateTo = new AppDate();
					appDateTo.setAppmonth(trxSel.getToDate().substring(0,2));
					appDateTo.setAppday(trxSel.getToDate().substring(3,5));					
					appDateTo.setAppyear(trxSel.getToDate().substring(6,10));
					trxListForm.setToDate(appDateTo);
				}	
			}			
			
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_TRXLIST, trxList);	
			
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		}
		
		log.debug("exiting TrxListAction postprocessAction");
				
	}

}
