package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblinvestment;
import com.pas.dbobjects.Tbltransaction;
import com.pas.dbobjects.Tbltransactiontype;
import com.pas.dbobjects.Tblwdcategory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.CashSpentForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.InvestmentDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TransactionTypeDAO;
import com.pas.slomfin.dao.WDCategoryDAO;
import com.pas.slomfin.valueObject.TransactionSelection;
import com.pas.util.PASUtil;

public class CashSpentAction extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside CashSpentAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		CashSpentForm csForm = (CashSpentForm)form;
		
		String cashInvID = (String) req.getSession().getServletContext().getAttribute(ISlomFinAppConstants.CASHINVESTMENTID);
				
		Tbltransaction trx = new Tbltransaction();
		
		//set up for a cash-spent transaction
		
		AccountDAO acctDAOReference;
		
		try
		{
			acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			List<Tblaccount> acctList = acctDAOReference.inquire(new Integer(csForm.getAccountID()));
			trx.setTblaccount(acctList.get(0));	
			trx.setIaccountID(trx.getTblaccount().getIaccountId());
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}
		
		InvestmentDAO investmentDAOReference;
		
		try
		{
			investmentDAOReference = (InvestmentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENT_DAO);
			List<Tblinvestment> investmentList = investmentDAOReference.inquire(new Integer(cashInvID));
			trx.setTblinvestment(investmentList.get(0));
			trx.setIinvestmentID(trx.getTblinvestment().getIinvestmentId());
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}
		
		TransactionTypeDAO trxTypeDAOReference;
		
		try
		{
			trxTypeDAOReference = (TransactionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTIONTYPE_DAO);
			List<Tbltransactiontype> trxTypeList = trxTypeDAOReference.inquire(new Integer(csForm.getTransactionTypeID()));
			trx.setTbltransactiontype(trxTypeList.get(0));	
			trx.setiTransactionTypeID(trx.getTbltransactiontype().getItransactionTypeId());
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}		
			
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		try
		{
			java.util.Date trxDate = sdf.parse(csForm.getTransactionDate().toStringMMDDYYYY());
			java.sql.Timestamp timestampTrxDate = new java.sql.Timestamp(trxDate.getTime());
			trx.setDtransactionDate(timestampTrxDate);
			
			java.util.Date trxPostedDate = sdf.parse(csForm.getTransactionPostedDate().toStringMMDDYYYY());
			java.sql.Timestamp timestampTrxPostedDate = new java.sql.Timestamp(trxPostedDate.getTime());
			trx.setDtranPostedDate(timestampTrxPostedDate);
			
		}
		catch (ParseException e)
		{	
			log.error("error parsing date in CashSpentAction pre-process");
			e.printStackTrace();
			throw new PresentationException("error parsing date in CashSpentAction pre-process");
			
		}	

		trx.setMcostProceeds(new BigDecimal(csForm.getCostProceeds()));
		trx.setMeffectiveAmount(PASUtil.getEffectiveAmount(trx.getMcostProceeds(),trx.getTbltransactiontype().getSdescription()));
		trx.setSdescription(csForm.getCashDescription());
		
		WDCategoryDAO wdcDAOReference;
		
		try
		{
			wdcDAOReference = (WDCategoryDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.WDCATEGORY_DAO);
			List<Tblwdcategory> wdcategoryList = wdcDAOReference.inquire(new Integer(csForm.getWdCategoryID()));
			trx.setTblwdcategory(wdcategoryList.get(0));
			trx.setiWDCategoryID(trx.getTblwdcategory().getIwdcategoryId());
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}		
				
		cache.setObject("RequestObject", trx, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside CashSpentAction postprocessAction");
	
		String buttonSelected = req.getParameter("operation");
		
		CashSpentForm csForm = (CashSpentForm)form;
		
		if (buttonSelected.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_ADDTHENADDANOTHERCASHSPENT))
			ac.setActionForward(mapping.findForward(ISlomFinAppConstants.AF_ADDANOTHER));
		else
		{	
			req.getSession().removeAttribute("WDCategories");
			ICacheManager cache =  CacheManagerFactory.getCacheManager();			
			TransactionSelection trxSel = new TransactionSelection();
			trxSel.setAccountID(Integer.parseInt(csForm.getAccountID()));
			trxSel.setRecentInd(true);
			log.debug("trxSel.setRecentInd set to true");
			cache.setTransactionSelection(req.getSession(), trxSel);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));
		}
				
		log.debug("exiting CashSpentAction postprocessAction");
				
	}

}
