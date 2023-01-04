package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblcashdeposittype;
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tblpayday;
import com.pas.dbobjects.Tbltransactiontype;
import com.pas.dbobjects.Tblwdcategory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.PaycheckOutflowUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.CashDepositTypeDAO;
import com.pas.slomfin.dao.InvestorDAO;
import com.pas.slomfin.dao.PaycheckOutflowDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TransactionTypeDAO;
import com.pas.slomfin.dao.WDCategoryDAO;
import com.pas.slomfin.valueObject.Investor;

public class PaycheckOutflowUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside PaycheckOutflowUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());		
		Tblpayday paycheckOutflow = new Tblpayday();
		
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||	reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);		
		
		if (operation == IAppConstants.DELETE_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			String pcoID = req.getParameter("paycheckOutflowID"); //hidden field on jsp
			PaycheckOutflowDAO pcoDAOReference;
			
			try
			{
				pcoDAOReference = (PaycheckOutflowDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PAYCHECKOUTFLOW_DAO);
				List<Tblpayday> pcoList = pcoDAOReference.inquire(new Integer(pcoID));
				paycheckOutflow = pcoList.get(0);			
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
		}
		
		if (operation == IAppConstants.ADD_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			PaycheckOutflowUpdateForm paycheckOutflowForm = (PaycheckOutflowUpdateForm)form;
			
			AccountDAO accountDAOReference;
			CashDepositTypeDAO cdtypeDAOReference;
			InvestorDAO investorDAOReference;
			WDCategoryDAO wdCategoryDAOReference;
			TransactionTypeDAO trxTypeDAOReference;
			
			try
			{
				accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
				cdtypeDAOReference = (CashDepositTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.CASHDEPOSITTYPE_DAO);
				investorDAOReference = (InvestorDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTOR_DAO);
				wdCategoryDAOReference = (WDCategoryDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.WDCATEGORY_DAO);
				trxTypeDAOReference = (TransactionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTIONTYPE_DAO);
				
				List<Tblaccount> acctList = accountDAOReference.inquire(new Integer(paycheckOutflowForm.getAccountID()));
				paycheckOutflow.setTblaccountByIAccountId(acctList.get(0));
				paycheckOutflow.setIaccountId(paycheckOutflow.getTblaccountByIAccountId().getIaccountId());
				
				List<Tblinvestor> investorList = investorDAOReference.inquire(new Integer(investor.getInvestorID()));
				paycheckOutflow.setTblinvestor(investorList.get(0));
				paycheckOutflow.setIinvestorId(paycheckOutflow.getTblinvestor().getIinvestorId());
				
				List<Tbltransactiontype> trxTypeList = trxTypeDAOReference.inquire(new Integer(paycheckOutflowForm.getTransactionTypeID()));
				paycheckOutflow.setTbltransactiontype(trxTypeList.get(0));
				paycheckOutflow.setItransactionTypeId(paycheckOutflow.getTbltransactiontype().getItransactionTypeId());					
				
				if (!(paycheckOutflowForm.getCashDepositTypeID() == null))
				{
				   if (paycheckOutflowForm.getCashDepositTypeID().length() > 0)					
				   {
					   List<Tblcashdeposittype> cdTypeList = cdtypeDAOReference.inquire(new Integer(paycheckOutflowForm.getCashDepositTypeID()));
					   paycheckOutflow.setTblcashdeposittype(cdTypeList.get(0));
					   paycheckOutflow.setIcashdepositTypeId(paycheckOutflow.getTblcashdeposittype().getIcashDepositTypeId());
				   }	   
				   else
				   {
					   paycheckOutflow.setTblcashdeposittype(null);
				   }
				}					
				
				if (!(paycheckOutflowForm.getWdCategoryID() == null))
				{
					if (paycheckOutflowForm.getWdCategoryID().length() > 0)					
					{
						List<Tblwdcategory> wdcList = wdCategoryDAOReference.inquire(new Integer(paycheckOutflowForm.getWdCategoryID()));
						paycheckOutflow.setTblwdcategory(wdcList.get(0));
						paycheckOutflow.setIwdcategoryId(paycheckOutflow.getTblwdcategory().getIwdcategoryId());
					}
					else
					{
						paycheckOutflow.setTblwdcategory(null);
					}
				}
				
				if (!(paycheckOutflowForm.getXferAccountID() == null))
				{
					if (paycheckOutflowForm.getXferAccountID().length() > 0)				
					{
						List<Tblaccount> xferacctList = accountDAOReference.inquire(new Integer(paycheckOutflowForm.getXferAccountID()));
						paycheckOutflow.setTblaccountByIXferAccountId(xferacctList.get(0));
						paycheckOutflow.setIxferAccountId(paycheckOutflow.getTblaccountByIXferAccountId().getIaccountId());
					}					   
					else
					{
						paycheckOutflow.setTblaccountByIXferAccountId(null);
					}
				}
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
						
			paycheckOutflow.setMdefaultAmt(new BigDecimal(paycheckOutflowForm.getDefaultAmount()));
			
			if (paycheckOutflowForm.getDefaultDay() != null)
				if (paycheckOutflowForm.getDefaultDay().length() > 0)
			        paycheckOutflow.setIdefaultDay(new Integer(paycheckOutflowForm.getDefaultDay()));	
			
			paycheckOutflow.setSdescription(paycheckOutflowForm.getDescription());
			
			if (paycheckOutflowForm.isNextMonthInd())
				paycheckOutflow.setBnextMonthInd(1);
			else
				paycheckOutflow.setBnextMonthInd(0);
			
			cache.setGoToDBInd(req.getSession(), true);
												
		}
		
		cache.setObject("RequestObject", paycheckOutflow, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside PaycheckOutflowUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("paycheckOutflowShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting PaycheckOutflowUpdateAction postprocessAction");
				
	}

}
