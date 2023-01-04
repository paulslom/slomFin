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
import com.pas.dbobjects.Tblmortgage;
import com.pas.dbobjects.Tbltransaction;
import com.pas.dbobjects.Tbltransactiontype;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.MortgagePaymentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.CashDepositTypeDAO;
import com.pas.slomfin.dao.InvestmentDAO;
import com.pas.slomfin.dao.MortgageDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TransactionTypeDAO;
import com.pas.valueObject.DropDownBean;

public class MortgagePaymentPrincipalTrxAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside MortgagePaymentPrincipalTrxAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		MortgagePaymentUpdateForm mortgagePaymentForm = (MortgagePaymentUpdateForm)form;
		
		String cashInvID = (String) req.getSession().getServletContext().getAttribute(ISlomFinAppConstants.CASHINVESTMENTID);
				
		Tbltransaction trx = new Tbltransaction();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date;
		try
		{
			date = sdf.parse(mortgagePaymentForm.getMortgagePaymentDate().toStringYYYYMMDD());
			java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
			trx.setDtransactionDate(timestamp);
			trx.setDtranPostedDate(timestamp);
		}
		catch (ParseException e)
		{	
			log.error("error parsing Mortgage Payment Date in MortgagePaymentPrincipalTrxAction pre-process");
			e.printStackTrace();
			throw new PresentationException("error parsing Mortgage Payment date in MortgagePaymentPrincipalTrxAction pre-process");				
		}	
		
		CashDepositTypeDAO cdDAOReference;
		
		try
		{
			cdDAOReference = (CashDepositTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.CASHDEPOSITTYPE_DAO);
			List<Tblcashdeposittype> cdTypeList = cdDAOReference.inquire(getMortgagePrincipalCashDepID(req));
			trx.setTblcashdeposittype(cdTypeList.get(0));			
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
			List<Tbltransactiontype> trxTypeList = trxTypeDAOReference.inquire(new Integer(getTrxTypeIDForCashDeposit(req)));
			trx.setTbltransactiontype(trxTypeList.get(0));			
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}		
		
		AccountDAO acctDAOReference;
			
		try
		{
			acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			List<Tblaccount> acctList = acctDAOReference.inquire(new Integer(getMortgagePrincipalAccount(req.getParameter("mortgageID"))));
			trx.setTblaccount(acctList.get(0));			
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}
		
		trx.setMcostProceeds(new BigDecimal(mortgagePaymentForm.getPrincipalPaid()));
		trx.setMeffectiveAmount(trx.getMcostProceeds());
		
		InvestmentDAO investmentDAOReference;
		
		try
		{
			investmentDAOReference = (InvestmentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENT_DAO);
			List<Tblinvestment> investmentList = investmentDAOReference.inquire(new Integer(cashInvID));
			trx.setTblinvestment(investmentList.get(0));			
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new PASSystemException(e1);
		}
		
		trx.setSdescription("Principal Payment");
		
		cache.setObject("RequestObject", trx, req.getSession());	
		cache.setGoToDBInd(req.getSession(), true);
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside MortgagePaymentPrincipalTrxAction postprocessAction");
		
		req.getSession().removeAttribute("mortgagePaymentShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));	
		
		log.debug("exiting MortgagePaymentPrincipalTrxAction postprocessAction");
				
	}
	
	@SuppressWarnings("unchecked")
	private Integer getMortgagePrincipalAccount(String mortgageID)
	   throws PASSystemException, DAOException, SystemException
	{		
		MortgageDAO daoReference = (MortgageDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MORTGAGE_DAO);
			
		log.debug("retrieving mortgage for mortgage id = " + mortgageID);
				
		List<Tblmortgage> mortgageList = daoReference.inquire(new Integer(mortgageID));
		
		Tblmortgage mortgage = mortgageList.get(0);
		
		return mortgage.getTblaccountByIPrincipalAccountId().getIaccountId();
		
	}
	
	@SuppressWarnings("unchecked")
	private Integer getMortgagePrincipalCashDepID(HttpServletRequest req)
	{
		String cdTypesListName = ISlomFinAppConstants.DROPDOWN_CASHDEPOSITTYPES;
		List<DropDownBean> cdTypes = (List) req.getSession().getServletContext().getAttribute(cdTypesListName);
		
		String cdep = new String();
		
		for (int i = 0; i < cdTypes.size(); i++)
		{
			DropDownBean ddBean = cdTypes.get(i);
            
			if (ddBean.getDescription().equalsIgnoreCase(ISlomFinAppConstants.CASHDEPTYPE_MORTGAGE_PRINCIPAL))
            {	
                cdep = ddBean.getId();
                break;
            }
		}
		return new Integer(cdep);
	}
	
	@SuppressWarnings("unchecked")
	private Integer getTrxTypeIDForCashDeposit(HttpServletRequest req)
	{
		String txTypesListName = ISlomFinAppConstants.DROPDOWN_TRANSACTIONTYPES;
		List<DropDownBean> txTypes = (List) req.getSession().getServletContext().getAttribute(txTypesListName);
		
		String txtype = new String();
		
		for (int i = 0; i < txTypes.size(); i++)
		{
			DropDownBean ddBean = txTypes.get(i);
            
			if (ddBean.getDescription().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_CASHDEPOSIT))
            {	
                txtype = ddBean.getId();
                break;
            }
		}
		return new Integer(txtype);
	}
	
}
