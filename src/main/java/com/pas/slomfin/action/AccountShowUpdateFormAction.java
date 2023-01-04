package com.pas.slomfin.action;

import java.util.List;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tblaccount;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.AccountUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;

public class AccountShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside AccountShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Integer accountID = new Integer(0);
        
		AccountUpdateForm accountForm = (AccountUpdateForm) form;		
		accountForm.initialize();
		
		String accountShowParm = req.getParameter("accountShowParm");
				
		if (accountShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String parmAccountID = req.getParameter("accountID");
		   accountID = Integer.valueOf(parmAccountID);
		   cache.setGoToDBInd(req.getSession(), true);
		}   
						
		cache.setObject("RequestObject", accountID, req.getSession());		
		
		log.debug("exiting AccountShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside AccountShowUpdateFormAction postprocessAction");
	
		String accountShowParm = req.getParameter("accountShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		Investor investor = new Investor();
		investor = cache.getInvestor(req.getSession());
		
		List<Tblaccount> accountList = null;
		
		if (!(accountShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   accountList = (List<Tblaccount>)cache.getObject("ResponseObject",req.getSession());		
		
		AccountUpdateForm accountForm = (AccountUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		DecimalFormat unitsFormat = new DecimalFormat("######.####");
		
		if (accountShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			accountForm.initialize();
			
			//we should be provided with a portfolioID as a menu selection parameter on an account add...
			
			String portID = req.getParameter("portfolioID");
			accountForm.setPortfolioID(portID);			
			
		}
		else //not an add	
		{	
		   	Tblaccount account = accountList.get(0);			
			
			accountForm.setAccountID(account.getIaccountId());			
			accountForm.setPortfolioID(account.getTblportfolio().getIportfolioId().toString());
			accountForm.setPortfolioName(account.getTblportfolio().getSportfolioName());
			
			if (account.getSaccountName() == null)
			   accountForm.setAccountName(null);
			else
			   accountForm.setAccountName(account.getSaccountName());	
			
			if (account.getSaccountNameAbbr() == null)
			   accountForm.setAccountNameAbbr(null);
			else
			   accountForm.setAccountNameAbbr(account.getSaccountNameAbbr());	
			
			if (account.getSaccountNumber() == null)
			   accountForm.setAccountNumber(null);
			else
			   accountForm.setAccountNumber(account.getSaccountNumber());	
			
			if (account.getTblaccounttype().getSaccountType() == null)
			   accountForm.setAccountTypeDescription(null);
			else
			   accountForm.setAccountTypeDescription(account.getTblaccounttype().getSaccountType());	
			
			if (account.getTblaccounttype().getIaccountTypeId() == null)
			   accountForm.setAccountTypeID(null);
			else
			   accountForm.setAccountTypeID(account.getTblaccounttype().getIaccountTypeId().toString());	
			
			if (account.getTblbroker() == null || account.getTblbroker().getIbrokerId() == null)
			   accountForm.setBrokerID(null);
			else
			   accountForm.setBrokerID(account.getTblbroker().getIbrokerId().toString());	
			
			if (account.getTblbroker() == null || account.getTblbroker().getSbrokerName() == null)
			   accountForm.setBrokerName(null);
			else
			   accountForm.setBrokerName(account.getTblbroker().getSbrokerName());	
			
			accountForm.setClosed(account.isBclosed());
			accountForm.setTaxable(account.isBtaxableInd());
			
			if (account.getDestimatedRateofReturn() == null)
			   accountForm.setEstimatedRateofReturn(null);
			else
			   accountForm.setEstimatedRateofReturn(unitsFormat.format(account.getDestimatedRateofReturn()));	
			
			if (account.getIinterestPaymentsPerYear() == null)
			   accountForm.setInterestPaymentsPerYear(null);
			else
			   accountForm.setInterestPaymentsPerYear(account.getIinterestPaymentsPerYear().toString());	
			
			if (account.getDinterestRate() == null)
			   accountForm.setInterestRate(null);
			else
			   accountForm.setInterestRate(unitsFormat.format(account.getDinterestRate()));	
			
			if (account.getMminInterestBalance() == null)
			   accountForm.setMinInterestBalance(null);
			else
			   accountForm.setMinInterestBalance(amtFormat.format(account.getMminInterestBalance()));	
			
			if (account.getMnewMoneyPerYear() == null)
			   accountForm.setNewMoneyPerYear(null);
			else
			   accountForm.setNewMoneyPerYear(amtFormat.format(account.getMnewMoneyPerYear()));
			
			if (account.getSpin() == null)
			   accountForm.setPin(null);
			else
			   accountForm.setPin(account.getSpin());	
			
			if (account.getIstartingCheckNo() == null)
			   accountForm.setStartingCheckNo(null);
			else
			   accountForm.setStartingCheckNo(account.getIstartingCheckNo().toString());	
			
			if (account.getDvestingPercentage() == null)
			   accountForm.setVestingPercentage(null);
			else
			   accountForm.setVestingPercentage(unitsFormat.format(account.getDvestingPercentage()));
						
		}
		
		String brkListName = ISlomFinAppConstants.DROPDOWN_BROKERS;			
		List brk = (List) req.getSession().getServletContext().getAttribute(brkListName);		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_BROKERS, brk);
		
		String portListName = ISlomFinAppConstants.DROPDOWN_PORTFOLIOS_BYINVESTOR + investor.getInvestorID();			
		List ports = (List) req.getSession().getServletContext().getAttribute(portListName);		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_PORTFOLIOS, ports);
		
		String accTypesListName = ISlomFinAppConstants.DROPDOWN_ACCOUNTTYPES;			
		List accTypes = (List) req.getSession().getServletContext().getAttribute(accTypesListName);		
		req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_ACCOUNTTYPES, accTypes);		
		
		req.getSession().setAttribute("accountShowParm",accountShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting AccountShowUpdateFormAction postprocessAction");
				
	}
						
}
