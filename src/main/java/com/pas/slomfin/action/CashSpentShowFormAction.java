package com.pas.slomfin.action;

import java.util.Calendar;
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
import com.pas.slomfin.actionform.CashSpentForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.AppDate;
import com.pas.valueObject.DropDownBean;

public class CashSpentShowFormAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
		HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
		DAOException, PASSystemException
	{
		log.debug("inside CashSpentShowFormAction pre - process");
		
		ICacheManager cache = CacheManagerFactory.getCacheManager();
		
		CashSpentForm csForm = (CashSpentForm) form;
		
		csForm.initialize();
	
		String trxCommonID = req.getParameter("trxCommonID");
				
		if (trxCommonID == null) //means we should expect that cache has an ID for this
		{
			Tbltransactioncommon trxCommon = (Tbltransactioncommon)cache.getObject("lastCashTrx",req.getSession());
			cache.setObject("RequestObject", trxCommon, req.getSession());
		}
		else //means we were called from the investor page where a parameter is passed
		{
			Tbltransactioncommon trxCommon = new Tbltransactioncommon();
			trxCommon.setItrxCommonId(new Integer(trxCommonID));
			cache.setObject("RequestObject", trxCommon, req.getSession());
			cache.setObject("lastCashTrx", trxCommon, req.getSession());
		}
		cache.setGoToDBInd(req.getSession(), true);
		
		return true;
	}	
	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside CashSpentShowFormAction postprocessAction");
	
		ICacheManager cache = CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());
		
		//need to get the wd categories dropdown specific to this investor
		//then set that as an attribute available to the jsp
		
		String wdCatListName = ISlomFinAppConstants.DROPDOWN_WDCATEGORIES_BYINVESTOR + investor.getInvestorID();
		List<DropDownBean> wdCats = (List) req.getSession().getServletContext().getAttribute(wdCatListName);
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_WDCATEGORIES, wdCats);		
		
		//This next retrieval from cache should just be a single TransactionCommon Object
		List<Tbltransactioncommon> trxCommonList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tbltransactioncommon trxCommon = trxCommonList.get(0);
		
		CashSpentForm csForm = (CashSpentForm) form;
		
		csForm.setAccountID(trxCommon.getTblaccount().getIaccountId().toString());
		csForm.setAccountName(trxCommon.getTblaccount().getSaccountName());
		csForm.setTransactionTypeDesc(trxCommon.getTbltransactiontype().getSdescription());
		csForm.setTransactionTypeID(trxCommon.getTbltransactiontype().getItransactionTypeId().toString());
		
		if (trxCommon.getTblaccount().getTblaccounttype().getSaccountType().equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_CREDITCARD))
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH,2); //add 2 days on Credit card post dates
			AppDate trxPostedDate = new AppDate();
			trxPostedDate.setAppday(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			trxPostedDate.setAppmonth(String.valueOf(cal.get(Calendar.MONTH)+1));
			trxPostedDate.setAppyear(String.valueOf(cal.get(Calendar.YEAR)));
			csForm.setTransactionPostedDate(trxPostedDate);				
		}
		else
		{	
			AppDate trxPostedDate = new AppDate();
			trxPostedDate.initAppDateToToday();
			csForm.setTransactionPostedDate(trxPostedDate);				
		}	
		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_CASHSPENTPICTUREFILE, trxCommon.getSpictureName());
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		
		log.debug("exiting CashSpentShowFormAction postprocessAction");
		
		
	}
	
}
