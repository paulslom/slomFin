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
import com.pas.dbobjects.Tblaccounttype;
import com.pas.dbobjects.Tblbroker;
import com.pas.dbobjects.Tblportfolio;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.AccountUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.AccountTypeDAO;
import com.pas.slomfin.dao.BrokerDAO;
import com.pas.slomfin.dao.PortfolioDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.AccountSelection;

public class AccountUpdateAction extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside AccountUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblaccount account = new Tblaccount();
		
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
		
		if (operation == IAppConstants.ADD_ACTION
		||  operation == IAppConstants.UPDATE_ACTION)
		{
			AccountUpdateForm accountForm = (AccountUpdateForm)form;
		
			if (operation == IAppConstants.UPDATE_ACTION)
			{
				String iacctID = req.getParameter("accountID");
				account.setIaccountId(Integer.valueOf(iacctID));
			}
			
			PortfolioDAO portDAOReference;
			
			try
			{
				portDAOReference = (PortfolioDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PORTFOLIO_DAO);
				List<Tblportfolio> portList = portDAOReference.inquire(new Integer(accountForm.getPortfolioID()));
				account.setTblportfolio(portList.get(0));
				account.setIportfolioId(account.getTblportfolio().getIportfolioId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			account.setSaccountName(accountForm.getAccountName());	
			
			if (accountForm.getAccountNameAbbr() == null)
			   account.setSaccountNameAbbr(null);
			else
			   account.setSaccountNameAbbr(accountForm.getAccountNameAbbr());	
			
			if (accountForm.getAccountNumber() == null)
			   account.setSaccountNumber(null);
			else
			   account.setSaccountNumber(accountForm.getAccountNumber());	
			
			AccountTypeDAO acctTypeDAOReference;
			
			try
			{
				acctTypeDAOReference = (AccountTypeDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNTTYPE_DAO);
				List<Tblaccounttype> acctTypeList = acctTypeDAOReference.inquire(new Integer(accountForm.getAccountTypeID()));
				account.setTblaccounttype(acctTypeList.get(0));	
				account.setIaccountTypeId(account.getTblaccounttype().getIaccountTypeId());
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
			
			if (accountForm.getBrokerID().length() == 0)
			   account.setTblbroker(null);
			else
			{	
				BrokerDAO brokerDAOReference;
				
				try
				{
					brokerDAOReference = (BrokerDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.BROKER_DAO);
					List<Tblbroker> brokerList = brokerDAOReference.inquire(new Integer(accountForm.getBrokerID()));
					account.setTblbroker(brokerList.get(0));			
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new PASSystemException(e1);
				}
			}
			
			if (accountForm.getEstimatedRateofReturn().length() == 0)
			   account.setDestimatedRateofReturn(null);
			else
			   account.setDestimatedRateofReturn(new BigDecimal(accountForm.getEstimatedRateofReturn()));	
			
			if (accountForm.getInterestPaymentsPerYear().length() == 0)
			   account.setIinterestPaymentsPerYear(null);
			else
			   account.setIinterestPaymentsPerYear(new Integer(accountForm.getInterestPaymentsPerYear()));	
			
			if (accountForm.getInterestRate().length() == 0)
			   account.setDinterestRate(null);
			else
			   account.setDinterestRate(new BigDecimal(accountForm.getInterestRate()));	
			
			if (accountForm.getMinInterestBalance().length() == 0)
			   account.setMminInterestBalance(null);
			else
			   account.setMminInterestBalance(new BigDecimal(accountForm.getMinInterestBalance()));	
			
			if (accountForm.getNewMoneyPerYear().length() == 0)
			   account.setMnewMoneyPerYear(null);
			else
			   account.setMnewMoneyPerYear(new BigDecimal(accountForm.getNewMoneyPerYear()));
			
			account.setBclosed(accountForm.isClosed());
			account.setBtaxableInd(accountForm.isTaxable());
			
			if (accountForm.getPin() == null)
			   account.setSpin(null);
			else
			   account.setSpin(accountForm.getPin());	
			
			if (accountForm.getStartingCheckNo().length() == 0)
			   account.setIstartingCheckNo(null);
			else
			   account.setIstartingCheckNo(new Integer(accountForm.getStartingCheckNo()));	
			
			if (accountForm.getVestingPercentage().length() == 0)
			   account.setDvestingPercentage(null);
			else
			   account.setDvestingPercentage(new BigDecimal(accountForm.getVestingPercentage()));
			
			cache.setGoToDBInd(req.getSession(), true);
						
		}
		
		if (operation == IAppConstants.DELETE_ACTION)
		{
			String acctID = req.getParameter("accountID"); //hidden field on jsp
			account.setIaccountId(new Integer(acctID));
			AccountDAO acctDAOReference;
			
			try
			{
				acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
				List<Tblaccount> acctList = acctDAOReference.inquire(new Integer(acctID));
				account = acctList.get(0);			
			}
			catch (SystemException e1)
			{
				log.error("SystemException encountered: " + e1.getMessage());
				e1.printStackTrace();
				throw new PASSystemException(e1);
			}
		}
		
		if  (!(reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||     reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN)
		||     reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_DELETE)))
		{
		   //do not attempt to derive accountSelection object from canceldelete, delete and return from view
		   //but do derive it for every other action
			
		   AccountSelection accountSelection = new AccountSelection();
		   if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD))
		   {
			   //we don't have much to return to on a cancel add...
			   //set the portfolioID = 1 and accstatus = open just as a guess
			   cache.setGoToDBInd(req.getSession(), false);
			   accountSelection.setPortfolioID(new Integer(1));
			   accountSelection.setClosed(false);
		   }
		   else //this means we have info on the form that will tell us what to use next in AccountListAction
		   {
			   AccountUpdateForm accountForm = (AccountUpdateForm)form;	
			   if (accountForm.getPortfolioID().length() == 0) //if user didn't enter anything default to 1
				   accountSelection.setPortfolioID(new Integer(1));
			   else
			       accountSelection.setPortfolioID(new Integer(accountForm.getPortfolioID()));
			   
			   if (req.getParameter("closed") == null) // means the checkbox was not posted - essentially means account is open
				   accountSelection.setClosed(false);
			   else
				   accountSelection.setClosed(accountForm.isClosed());
		   }
		   cache.setAccountSelection(req.getSession(), accountSelection);
		}
		
		cache.setObject("RequestObject", account, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside AccountUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("accountShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting AccountUpdateAction postprocessAction");
				
	}

}
