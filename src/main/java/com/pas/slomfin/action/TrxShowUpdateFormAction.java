package com.pas.slomfin.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tbltransaction;
import com.pas.dbobjects.Tbltransactiontype;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.TrxUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.MiscDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TransactionTypeDAO;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.TransactionSelection;
import com.pas.util.DateUtil;
import com.pas.valueObject.AppDate;
import com.pas.valueObject.DropDownBean;

public class TrxShowUpdateFormAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TrxShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbltransaction trx = new Tbltransaction();
        
		TrxUpdateForm trxForm = (TrxUpdateForm) form;		
		trxForm.initialize();
		
		String trxShowParm = req.getParameter("trxShowParm");
				
		if (trxShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String trxID = req.getParameter("transactionID");
		   trx.setItransactionId(Integer.parseInt(trxID));
		   cache.setGoToDBInd(req.getSession(), true);
		}   
						
		cache.setObject("RequestObject", trx, req.getSession());		
		
		log.debug("exiting TrxShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TrxShowUpdateFormAction postprocessAction");
	
		String trxShowParm = req.getParameter("trxShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<Tbltransaction> trxList = null;
		
		if (!(trxShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   trxList = (List<Tbltransaction>)cache.getObject("ResponseObject",req.getSession());		
		
		Investor investor = cache.getInvestor(req.getSession());
		
		Tbltransaction trx = new Tbltransaction();
		
		TrxUpdateForm trxForm = (TrxUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		String trxTypeDesc = "";
		String invTypeDesc = "";		
		String invTypeID = "";
		
		String trxShowAmount = "false";
		String trxShowCashDepositType = "false";
		String trxShowCashDescription = "false";
		String trxShowCashFields = "false";
		String trxShowCheckNo = "false";	
		String trxShowInvestmentID = "false";
		String trxShowOptionFields = "false";
		String trxShowPrice = "false";		
		String trxShowUnits = "false";
		String trxShowWDCategory = "false";
		String trxShowXferAccount = "false";
		String trxShowDividendTaxableYear = "false";
		String trxShowLastBillingCycleYN = "false";
		
		String reqParmOwned = "";
		
		if (trxShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			reqParmOwned = req.getParameter("owned");
													
			//first, set up trxForm for the add
			
			trxForm.initialize();
			
			AppDate trxDate = new AppDate();
			trxDate.initAppDateToToday();
			trxForm.setTransactionDate(trxDate);			
				
			trxForm.setPortfolioName(req.getParameter("portName"));
			trxForm.setAccountName(req.getParameter("acctName"));
			trxForm.setTrxTypeDesc(req.getParameter("ttDesc"));
			trxForm.setInvTypeDesc(req.getParameter("itDesc"));
			trxForm.setAccountID(new Integer(req.getParameter("acctID")));
			
			invTypeID = req.getParameter("itID");
			trxTypeDesc = trxForm.getTrxTypeDesc();
			invTypeDesc = trxForm.getInvTypeDesc();
						
			AccountDAO acctDAOReference;
			
			try
			{
				acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
				List<Tblaccount> acctList = acctDAOReference.inquire(new Integer(req.getParameter("acctID")));
				trx.setTblaccount(acctList.get(0));	
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
					
			if (trx.getTblaccount().getTblaccounttype().getSaccountType().equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_CREDITCARD))
			{
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH,2); //add 2 days on Credit card post dates
				AppDate trxPostedDate = new AppDate();
				trxPostedDate.setAppday(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				trxPostedDate.setAppmonth(String.valueOf(cal.get(Calendar.MONTH)+1));
				trxPostedDate.setAppyear(String.valueOf(cal.get(Calendar.YEAR)));
				trxForm.setTransactionPostedDate(trxPostedDate);
				trxShowLastBillingCycleYN = "true";
			}
			else
			{	
				AppDate trxPostedDate = new AppDate();
				trxPostedDate.initAppDateToToday();
				trxForm.setTransactionPostedDate(trxPostedDate);				
			}	
		
			TransactionTypeDAO trxTypeDAOReference;
			
			try
			{
				trxTypeDAOReference = (TransactionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TRANSACTIONTYPE_DAO);
				List<Tbltransactiontype> trxTypeList = trxTypeDAOReference.inquire(new Integer(req.getParameter("ttID")));
				trx.setTbltransactiontype(trxTypeList.get(0));
				trx.setiTransactionTypeID(trx.getTbltransactiontype().getItransactionTypeId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}		
			
			//next, set up a transaction selection object for use by trxListAction later
			
			TransactionSelection trxSel = new TransactionSelection();
			trxSel.setAccountID(trx.getTblaccount().getIaccountId());
			cache.setTransactionSelection(req.getSession(),trxSel);
			
			//finally, set up variables for JSTL on JSP
			
			if (invTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))
			{	
			   trxShowCashFields = "true";
			}
						
			if (invTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_OPTION))
			{
			   trxShowOptionFields = "true";
			   try
			   {
				   trxForm.setExpirationDate(DateUtil.getNextThirdFriday());
			   }
			   catch (ParseException e)
			   {
				   throw new PresentationException(e); 
			   }
			}
			
			if ((trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND)				   
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			&&  (!trxForm.getInvTypeDesc().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH)))
			{
				trxShowInvestmentID = "true"; 		   	     
			}
						
			if ((trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			&&  (!trxForm.getInvTypeDesc().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))) 
			{
				trxShowUnits = "true";			  
			}
		   				
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))		
			{
				trxShowPrice = "true";
			}
						
			if (!(trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT) 
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)))
			{
				trxShowAmount = "true";
			}
						
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL))
			{	
				trxShowCheckNo = "true";
				try
				{
					trxForm.setCheckNo(getNextCheckNo(trx.getTblaccount().getIaccountId()));
				}
				catch (SystemException e)
				{					
					log.error("TrxShowUpdateFormAction SystemException" + e.getMessage());
					e.printStackTrace();
					throw new PASSystemException(e);
				}
			}
						
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN))
			{
				trxShowCashDepositType = "true";
			}
						
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_FEE))
			{
				trxShowWDCategory = "true";
			}
						
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
			|| (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND)
			&&  invTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))	
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_FEE)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_LOAN)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			{
				trxShowCashDescription = "true";
			}
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)
			|| trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND))
			{
				trxForm.setDividendTaxableYear(new Integer(trxDate.getAppyear())); //default dividend year to this year on add
				trxShowDividendTaxableYear = "true";	
			}
			
			if  (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			{	
				trxShowXferAccount = "true";
				List<DropDownBean> xferAccounts = null; 
				try
				{
				   xferAccounts = getXferAccounts(trx.getTblaccount().getIaccountId());
				   req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_XFERACCOUNTS, xferAccounts);	
				}
				catch (SystemException e)
				{
				   log.error("TrxShowUpdateFormAction SystemException" + e.getMessage());
				   e.printStackTrace();
				   throw new PASSystemException(e);
				}
			}
		}
		else //not an add	
		{	
			trx = trxList.get(0);			
			
			trxForm.setAccountID(trx.getTblaccount().getIaccountId());
			trxForm.setAccountName(trx.getTblaccount().getSaccountName());
			if (trx.getTblcashdeposittype() != null)
			{	
				trxForm.setCashDepositTypeDescription(trx.getTblcashdeposittype().getScashDepositTypeDesc());
				trxForm.setCashDepositTypeID(trx.getTblcashdeposittype().getIcashDepositTypeId());
				trxShowCashDepositType = "true";
			}
			trxForm.setCashDescription(trx.getSdescription());
			if (trx.getTblwdcategory() != null)
			{	
				trxForm.setCategoryDescription(trx.getTblwdcategory().getSwdcategoryDescription());
				trxShowWDCategory = "true";
			}
			trxForm.setCheckNo(trx.getIcheckNo());
			
			if (trx.getMcostProceeds() == null)
			   trxForm.setCostProceeds(null);
			else
			   trxForm.setCostProceeds(amtFormat.format(trx.getMcostProceeds()));	
						
			if (trx.getDecUnits() == null)
				trxForm.setDecUnits(null);		
			else
			{
				DecimalFormat unitsFormat = new DecimalFormat("######.####");
				trxForm.setDecUnits(unitsFormat.format(trx.getDecUnits()));
			}
			
			if (trx.getIdividendTaxableYear() == null)
			   trxForm.setDividendTaxableYear(null);
			else
			   trxForm.setDividendTaxableYear(trx.getIdividendTaxableYear());	
			
			if (trx.getTblaccount().getTblaccounttype().getSaccountType().equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_CREDITCARD))
			{				
				trxShowLastBillingCycleYN = "true";
			}
			
			trxForm.setInvDesc(trx.getTblinvestment().getSdescription());
			trxForm.setInvestmentID(trx.getTblinvestment().getIinvestmentId());
			trxForm.setInvestmentTypeID(trx.getTblinvestment().getTblinvestmenttype().getIinvestmentTypeId());
			trxForm.setInvTypeDesc(trx.getTblinvestment().getTblinvestmenttype().getSdescription());
			trxForm.setOpeningTrxInd(trx.isBopeningTrxInd());
			trxForm.setFinalTrxOfBillingCycle(trx.isBfinalTrxOfBillingCycle());
			
			if (trx.getTbloptiontype() != null)
			{	
				trxForm.setOptionTypeID(trx.getTbloptiontype().getIoptionTypeId());
				trxForm.setOptionTypeDesc(trx.getTbloptiontype().getSdescription());
			}
			
			trxForm.setPortfolioID(trx.getTblaccount().getTblportfolio().getIportfolioId());
			trxForm.setPortfolioName(trx.getTblaccount().getTblportfolio().getSportfolioName());
						
			if (trx.getMprice() == null)
			{
				trxForm.setPrice(null);		
			}
			else
			{
				trxForm.setPrice(amtFormat.format(trx.getMprice()));			
			}
			
			if (trx.getMstrikePrice() == null)
			{
				trxForm.setStrikePrice(null);		
			}
			else
			{
				trxForm.setStrikePrice(amtFormat.format(trx.getMstrikePrice()));				
			}
			
			trxForm.setTransactionID(trx.getItransactionId());
			trxForm.setTransactionTypeID(trx.getTbltransactiontype().getItransactionTypeId());
			trxForm.setTrxTypeDesc(trx.getTbltransactiontype().getSdescription());
			
			if (trx.getTblwdcategory() != null)
			{
				trxForm.setWdCategoryID(trx.getTblwdcategory().getIwdcategoryId());
			}
			
			//trxForm.setXferAccountID(trx.getXferAccountID());
			
			AppDate appDate = new AppDate();
			Calendar tempCal = Calendar.getInstance();
			
			if (trx.getDtransactionDate() == null)
			{
				trxForm.setTransactionDate(null);
			}
			else
			{	
				tempCal.setTimeInMillis(trx.getDtransactionDate().getTime());
				appDate.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
				appDate.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
				appDate.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
				trxForm.setTransactionDate(appDate);				
			}
			
			AppDate appDate2 = new AppDate();
			Calendar tempCal2 = Calendar.getInstance();
			
			if (trx.getDexpirationDate() == null)
			{
				trxForm.setExpirationDate(null);
			}
			else
			{	
				tempCal2.setTimeInMillis(trx.getDexpirationDate().getTime());
				appDate2.setAppday(String.valueOf(tempCal2.get(Calendar.DAY_OF_MONTH)));
				appDate2.setAppmonth(String.valueOf(tempCal2.get(Calendar.MONTH)+1));
				appDate2.setAppyear(String.valueOf(tempCal2.get(Calendar.YEAR)));
				trxForm.setExpirationDate(appDate2);
			}
			
			AppDate appDate3 = new AppDate();
			Calendar tempCal3 = Calendar.getInstance();
			
			if (trx.getDtransactionDate() == null)
				trxForm.setTransactionDate(null);
			else
			{	
				tempCal3.setTimeInMillis(trx.getDtranPostedDate().getTime());
				appDate3.setAppday(String.valueOf(tempCal3.get(Calendar.DAY_OF_MONTH)));
				appDate3.setAppmonth(String.valueOf(tempCal3.get(Calendar.MONTH)+1));
				appDate3.setAppyear(String.valueOf(tempCal3.get(Calendar.YEAR)));
				trxForm.setTransactionPostedDate(appDate3);	
			}
			
			trxTypeDesc = trxForm.getTrxTypeDesc();
			
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			   if ((trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND)				   
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			   &&  (!trx.getTblinvestment().getTblinvestmenttype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH)))
				  trxShowInvestmentID = "true";		   	  
			else		
			   trxShowInvestmentID = "true";			 

			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
			   if ((trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			   &&  (!trx.getTblinvestment().getTblinvestmenttype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))) 
		   	      trxShowUnits = "true";		   	   
			}
			else
			   if (trx.getDecUnits() != null)
				   trxShowUnits = "true";			
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST))		
			    trxShowDividendTaxableYear = "true";
			
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
			   if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))		
			       trxShowPrice = "true";			  
			}	 
		    else
			   if (trx.getMprice() != null)
				   trxShowPrice = "true";
			
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
				if (!(trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT) 
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)))
					trxShowAmount = "true";				
			}
			else
				if (trx.getMcostProceeds() != null)
				   trxShowAmount = "true";
			
			if (trx.getDexpirationDate() != null)
				trxShowOptionFields = "true";
			
			if (trx.getSdescription() != null)
			   trxShowCashFields = "true";
			
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
				if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL))
					trxShowCheckNo = "true";
			}
			else
				if (trx.getIcheckNo() != null)
				   trxShowCheckNo = "true";
			
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
				if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN))
					trxShowCashDepositType = "true";
			}				
			
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
				if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_FEE))
					trxShowWDCategory = "true";
			}	
						
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
				if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
				|| (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND)
					&& trx.getTblinvestment().getTblinvestmenttype().getSdescription().equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))	
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_FEE)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_LOAN)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
					trxShowCashDescription = "true";
			}	
			else
				if (trx.getSdescription() != null)
				   if (!(trx.getSdescription().equalsIgnoreCase("")))
					   trxShowCashDescription = "true";				
		}
		
		if (trxShowInvestmentID.equalsIgnoreCase("true"))
		{	
			String investmentsListName = "";
			List invs = null; 			
						
			if (trxShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
			{	
			   investmentsListName = ISlomFinAppConstants.DROPDOWN_INVESTMENTS_BYINVTYP + trx.getTblinvestment().getTblinvestmenttype().getIinvestmentTypeId();
			   invs = (List)req.getSession().getServletContext().getAttribute(investmentsListName);
			}
			else
			{	
			   if (trxShowParm.equalsIgnoreCase(IAppConstants.ADD))
			   {				   	
				   if (reqParmOwned == null)
				   {
					   //if reinvest, split, exercise or expire show only owned
					   if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)  
					   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)
					   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
					   ||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION))								 
					   {	   
						  try
						  {
							  invs = getInvestmentsOwnedByType(trx.getTblaccount().getIaccountId(),invTypeDesc);
						  }
						  catch (SystemException e)
						  {
							  log.error("TrxShowUpdateFormAction SystemException" + e.getMessage());
							  e.printStackTrace();
							  throw new PASSystemException(e);
						  }
					   }
					   else //not reinvest, show all
					   {	   
					      investmentsListName = ISlomFinAppConstants.DROPDOWN_INVESTMENTS_BYINVTYP +  invTypeID;
					      invs = (List) req.getSession().getServletContext().getAttribute(investmentsListName);
					   }   
				   }
				   else //reqParmOwned something other than null
					  if (reqParmOwned.equalsIgnoreCase("yes"))					  
					  {	  
						  try
						  {
							  invs = getInvestmentsOwnedByType(trx.getTblaccount().getIaccountId(),invTypeDesc);
						  }
						  catch (SystemException e)
						  {
							  log.error("TrxShowUpdateFormAction SystemException" + e.getMessage());
							  e.printStackTrace();
							  throw new PASSystemException(e);
						  }
					  } 
					  else //parm was not null, but was not owned="yes", either
					  {	  
						  investmentsListName = ISlomFinAppConstants.DROPDOWN_INVESTMENTS_BYINVTYP +  invTypeID;
						  invs = (List) req.getSession().getServletContext().getAttribute(investmentsListName);
					  }										  
		    
			   }
			}
			
		    req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_INVESTMENTS, invs);	
		
		}
		
		if (trxShowOptionFields.equalsIgnoreCase("true"))
		{	
			String optionTypesListName = ISlomFinAppConstants.DROPDOWN_OPTIONTYPES;
			List opts = (List) req.getSession().getServletContext().getAttribute(optionTypesListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_OPTIONTYPES, opts);	
		}
		
		if (trxShowCashDepositType.equalsIgnoreCase("true"))
		{	
			String cdTypesListName = ISlomFinAppConstants.DROPDOWN_CASHDEPOSITTYPES;
			List cdts = (List) req.getSession().getServletContext().getAttribute(cdTypesListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_CASHDEPOSITTYPES, cdts);	
		}
		
		if (trxShowWDCategory.equalsIgnoreCase("true"))
		{	
			String wdCatListName = ISlomFinAppConstants.DROPDOWN_WDCATEGORIES_BYINVESTOR + investor.getInvestorID();			
			List wdCats = (List) req.getSession().getServletContext().getAttribute(wdCatListName);		
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_WDCATEGORIES, wdCats);
		}
		
		//set attributes at session (since if validation fails TrxUpdateForm will need them again) scope for jstl if tests
		
		req.getSession().setAttribute("trxShowParm",trxShowParm);
		req.getSession().setAttribute("trxShowUnits",trxShowUnits);
		req.getSession().setAttribute("trxShowPrice",trxShowPrice);
		req.getSession().setAttribute("trxShowAmount",trxShowAmount);
		req.getSession().setAttribute("trxShowInvestmentID",trxShowInvestmentID);
		req.getSession().setAttribute("trxShowOptionFields",trxShowOptionFields);
		req.getSession().setAttribute("trxShowCashFields",trxShowCashFields);
		req.getSession().setAttribute("trxShowCheckNo",trxShowCheckNo);
		req.getSession().setAttribute("trxShowCashDepositType",trxShowCashDepositType);
		req.getSession().setAttribute("trxShowCashDescription", trxShowCashDescription);
		req.getSession().setAttribute("trxShowWDCategory", trxShowWDCategory);
		req.getSession().setAttribute("trxShowXferAccount", trxShowXferAccount);
		req.getSession().setAttribute("trxShowDividendTaxableYear", trxShowDividendTaxableYear);
		req.getSession().setAttribute("trxShowLastBillingCycleYN", trxShowLastBillingCycleYN);
		
		String trxUpdateOrigin = req.getParameter("trxUpdateOrigin");
		
		if (trxUpdateOrigin == null)
			req.getSession().setAttribute("trxUpdateOrigin", "other");
		else //means we need to head back to reportTransactionsAction when done
		{	
			req.getSession().setAttribute("trxUpdateOrigin", trxUpdateOrigin);
			req.getSession().setAttribute("reportAcctID",trx.getTblaccount().getIaccountId().toString());
		}
		cache.setObject("ResponseObject", trx, req.getSession());
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting TrxShowUpdateFormAction postprocessAction");
				
	}
	
	private List<DropDownBean> getInvestmentsOwnedByType(Integer accountID, String investmentType)
	   throws PASSystemException, DAOException, SystemException
	{		
		AccountDAO daoReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			
		log.debug("retrieving dropdown values for investments owned in account " + accountID + " which are of investment type" + investmentType);
			
		return daoReference.accountPositions(accountID,investmentType);
	}
	
	private Integer getNextCheckNo(Integer accountID)
	   throws PASSystemException, DAOException, SystemException
	{		
		MiscDAO daoReference = (MiscDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MISC_DAO);
			
		log.debug("retrieving the next check number for account " + accountID);
			
		return daoReference.getNextCheckNo(accountID);
	}
	
	@SuppressWarnings("unchecked")
	private List<DropDownBean> getXferAccounts(Integer accountID)
	   throws PASSystemException, DAOException, SystemException
	{			
		AccountDAO daoReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			
		log.debug("retrieving the possible transfer accounts for account " + accountID);
			
		return daoReference.getXferAccounts(accountID);
	}

}
