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
import com.pas.dbobjects.Tblcashdeposittype;
import com.pas.dbobjects.Tblinvestment;
import com.pas.dbobjects.Tbloptiontype;
import com.pas.dbobjects.Tbltransaction;
import com.pas.dbobjects.Tblwdcategory;
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
import com.pas.slomfin.dao.CashDepositTypeDAO;
import com.pas.slomfin.dao.InvestmentDAO;
import com.pas.slomfin.dao.OptionTypeDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.WDCategoryDAO;
import com.pas.util.PASUtil;

public class TrxUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TrxUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbltransaction trx = (Tbltransaction)cache.getObject("ResponseObject",req.getSession()); //this had been set in trxShowFormAction	
		
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
		{
			cache.setGoToDBInd(req.getSession(), false);
		}
		
		if (operation == IAppConstants.ADD_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			TrxUpdateForm trxForm = (TrxUpdateForm)form;
			
			String trxTypeDesc = trx.getTbltransactiontype().getSdescription();
			String invTypeDesc = req.getParameter("invTypeDesc");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(trxForm.getTransactionDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				trx.setDtransactionDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing Transaction date in TrxUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Transaction date in TrxSpentAction pre-process");				
			}	
			
			java.util.Date dateTrxPosted;
			try
			{
				dateTrxPosted = sdf.parse(trxForm.getTransactionPostedDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(dateTrxPosted.getTime());
				trx.setDtranPostedDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing Transaction date in TrxUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Transaction date in TrxSpentAction pre-process");				
			}	
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_FEE)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_LOAN)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			{
				trx.setSdescription(trxForm.getCashDescription());
				trx.setBfinalTrxOfBillingCycle(trxForm.isFinalTrxOfBillingCycle());
			}		    
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL))
			{
				trx.setIcheckNo(trxForm.getCheckNo());
			}
			
			trx.setIaccountID(trxForm.getAccountID());
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CHECKWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_FEE)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			{
				if (trxForm.getWdCategoryID() != null && trxForm.getWdCategoryID() != 0) // zero is nothing selected on the form, which is ok in some cases
				{	
					WDCategoryDAO wdcDAOReference;
					
					try
					{
						wdcDAOReference = (WDCategoryDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.WDCATEGORY_DAO);
						List<Tblwdcategory> wdcategoryList = wdcDAOReference.inquire(trxForm.getWdCategoryID());
						trx.setTblwdcategory(wdcategoryList.get(0));
						trx.setiWDCategoryID(trx.getTblwdcategory().getIwdcategoryId());
					}
					catch (SystemException e1)
					{
						log.error("SystemException encountered: " + e1.getMessage());
						e1.printStackTrace();
						throw new PASSystemException(e1);
					}
				}
				
			}
			
			if (operation == IAppConstants.ADD_ACTION)
			{
				if  (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)			
				||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
				{
					trx.setXferAccountID(trxForm.getXferAccountID());
					if (trxForm.getCashDescription().length() == 0)
					{	
						AccountDAO acctDAOReference;
						Tblaccount otheracct = new Tblaccount();
						try
						{
							acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
							List<Tblaccount> accntList = acctDAOReference.inquire(trxForm.getXferAccountID());
							otheracct = accntList.get(0);			
						}
						catch (SystemException e1)
						{
							log.error("SystemException encountered: " + e1.getMessage());
							e1.printStackTrace();
							throw new PASSystemException(e1);
						}
						if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
						{
							trx.setSdescription("To: " + otheracct.getSaccountName());
						}
						else //must be a transfer in
						{
							trx.setSdescription("From: " + otheracct.getSaccountName());
						}
					}						       
					
				}
			}
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHWITHDRAWAL)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN))
			{
				if (trxForm.getCashDepositTypeID() != null &&  trxForm.getCashDepositTypeID() != 0) // zero is nothing selected on the form, which is ok in some cases
				{	
					CashDepositTypeDAO cdDAOReference;
					
					try
					{
						cdDAOReference = (CashDepositTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.CASHDEPOSITTYPE_DAO);
						List<Tblcashdeposittype> cdTypeList = cdDAOReference.inquire(trxForm.getCashDepositTypeID());
						trx.setTblcashdeposittype(cdTypeList.get(0));
						trx.setiCashDepositTypeID(trx.getTblcashdeposittype().getIcashDepositTypeId());
					}
					catch (SystemException e1)
					{
						log.error("SystemException encountered: " + e1.getMessage());
						e1.printStackTrace();
						throw new PASSystemException(e1);
					}
				}
				
			}
						
			if (!(trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT) 
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)))
			{
				trx.setMcostProceeds(new BigDecimal(trxForm.getCostProceeds()));
			}
			
			trx.setMeffectiveAmount(PASUtil.getEffectiveAmount(trx.getMcostProceeds(),trx.getTbltransactiontype().getSdescription()));
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			{
				if (trxForm.getOptionTypeID() != null)
				{	
					OptionTypeDAO optTypeDAOReference;
					
					try
					{
						optTypeDAOReference = (OptionTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.OPTIONTYPE_DAO);
						List<Tbloptiontype> optTypeList = optTypeDAOReference.inquire(trxForm.getOptionTypeID());
						trx.setTbloptiontype(optTypeList.get(0));
						trx.setiOptionTypeID(trx.getTbloptiontype().getIoptionTypeId());
					}
					catch (SystemException e1)
					{
						log.error("SystemException encountered: " + e1.getMessage());
						e1.printStackTrace();
						throw new PASSystemException(e1);
					}
					
				}	
				
				if (trxForm.getStrikePrice() != null)
				{
					trx.setMstrikePrice(new BigDecimal(trxForm.getStrikePrice()));
				}
				
				trx.setBopeningTrxInd(trxForm.isOpeningTrxInd());
								
				if (trxForm.getExpirationDate().getAppday() != null)
				{
					try				
					{
						date = sdf.parse(trxForm.getExpirationDate().toStringYYYYMMDD());
						java.sql.Timestamp timestamp2 = new java.sql.Timestamp(date.getTime());
						trx.setDexpirationDate(timestamp2);
					}
					catch (ParseException e)
					{	
						log.error("error parsing expiration date in TrxUpdateAction pre-process");
						e.printStackTrace();
						throw new PresentationException("error parsing expiration date in TrxUpdateAction pre-process");				
					}
				}
			}
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXERCISEOPTION)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_EXPIREOPTION)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SPLIT)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFERIN)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_TRANSFEROUT))
			{
				if (trxForm.getDecUnits() == null)
				{
					trx.setDecUnits(null);
				}
				else
				{
					trx.setDecUnits(new BigDecimal(trxForm.getDecUnits()));
				}
			}
			
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY)
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)		
			||  trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))		
			{
				if (trxForm.getPrice() == null)
				{
					trx.setMprice(null);
				}
				else
				{
					trx.setMprice(new BigDecimal(trxForm.getPrice()));
				}
			}
			
			Integer invID = 0;
			
			if (invTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_CASH))
			{	
				String sinvID = (String) req.getSession().getServletContext().getAttribute(ISlomFinAppConstants.CASHINVESTMENTID);			
				invID = Integer.valueOf(sinvID);
			}
			else
				invID = trxForm.getInvestmentID();
							
			InvestmentDAO investmentDAOReference;
				
			try
			{
				investmentDAOReference = (InvestmentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENT_DAO);
				List<Tblinvestment> investmentList = investmentDAOReference.inquire(invID);
				trx.setTblinvestment(investmentList.get(0));
				trx.setIinvestmentID(trx.getTblinvestment().getIinvestmentId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
						
			if (trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_REINVEST)
			|| trxTypeDesc.equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDIVIDEND))
			{
				if (trxForm.getDividendTaxableYear() == null)
				{
					trx.setIdividendTaxableYear(null);
				}
				else
				{
					trx.setIdividendTaxableYear(trxForm.getDividendTaxableYear());	
				}
			}
			cache.setGoToDBInd(req.getSession(), true);
		}
		
		cache.setObject("RequestObject", trx, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TrxUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("trxShowParm");
		req.getSession().removeAttribute("trxShowUnits");
		req.getSession().removeAttribute("trxShowPrice");
		req.getSession().removeAttribute("trxShowAmount");
		req.getSession().removeAttribute("trxShowInvestmentID");
		req.getSession().removeAttribute("trxShowOptionFields");
		req.getSession().removeAttribute("trxShowCashFields");
		req.getSession().removeAttribute("trxShowCheckNo");
		req.getSession().removeAttribute("trxShowCashDepositType");
		req.getSession().removeAttribute("trxShowCashDescription");
		req.getSession().removeAttribute("trxShowWDCategory");
		req.getSession().removeAttribute("trxShowDividendTaxableYear");
		
		String trxUpdateOrig = (String)req.getSession().getAttribute("trxUpdateOrigin");
		
		if (trxUpdateOrig.equalsIgnoreCase(ISlomFinAppConstants.AF_RPTTRX))
			ac.setActionForward(mapping.findForward(ISlomFinAppConstants.AF_RPTTRX));		
		else	
			ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		req.getSession().removeAttribute("trxUpdateOrigin");
		
		log.debug("exiting TrxUpdateAction postprocessAction");
				
	}

}
