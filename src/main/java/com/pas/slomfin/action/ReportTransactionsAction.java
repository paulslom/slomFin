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
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.TransactionSelection;

public class ReportTransactionsAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportTransactionsAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		TransactionSelection trxSel = new TransactionSelection();
		String accountIDParm = req.getParameter("accountID");
		if (accountIDParm == null) //means we are returning from a view-chg-del of a trx
		{	
			accountIDParm = (String)req.getSession().getAttribute("reportAcctID");
			req.getSession().removeAttribute("reportAcctID");
		}
		trxSel.setAccountID(Integer.parseInt(accountIDParm));
		trxSel.setRecentInd(false);
		log.debug("trxSel.setRecentInd set to false");
		cache.setObject("RequestObject", trxSel, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
						
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportTransactionsAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tbltransaction> reportTransactions = (List)cache.getObject("ResponseObject",req.getSession());	
		
		String showCheckNo = "false";	
		String showPrice = "false";		
		String showUnits = "false";
		String showAccountBalance = "true";
		String showDescription = "true";
		String reportTrxTitle = "";
		
		if (reportTransactions.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{
			Tbltransaction trxB = reportTransactions.get(0); //take the first transaction
			
			String accountType = trxB.getTblaccount().getTblaccounttype().getSaccountType();
			
			if (accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_CHECKING)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_MONEYMARKETWCHK))
				showCheckNo = "true";
				
			if (accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_TAXABLEBROKERAGE)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_TRADIRA)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_ROTHIRA)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_ROTH401K)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_PENSION)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_529PLAN)
			||	accountType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_401K))
			{
				showPrice = "true";
				showUnits = "true";
			}
			
			reportTrxTitle = trxB.getTblaccount().getSaccountName();
		}
					
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTTRXLIST, reportTransactions);	
		req.getSession().setAttribute("showUnits",showUnits);
		req.getSession().setAttribute("showPrice",showPrice);
		req.getSession().setAttribute("showCheckNo",showCheckNo);	
		req.getSession().setAttribute("showAccountBalance",showAccountBalance);	
		req.getSession().setAttribute("showDescription",showDescription);	
		req.getSession().setAttribute("reportTrxTitle",reportTrxTitle);	
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportTransactionsAction postprocessAction");
				
	}

}
