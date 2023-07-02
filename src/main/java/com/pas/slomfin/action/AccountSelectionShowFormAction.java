package com.pas.slomfin.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.pas.dbobjects.Tblaccount;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.AccountSelection;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.DropDownBean;

public class AccountSelectionShowFormAction extends DispatchAction
{	
	protected static Logger log = LogManager.getLogger(AccountSelectionShowFormAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		log.debug("inside AccountSelectionShowFormAction");	
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		Investor investor = new Investor();
		investor = cache.getInvestor(request.getSession());
		
		String closedIndParm = request.getParameter("closedInd");
		String taxableIndParm = request.getParameter("taxableInd");
		String positionsParm = request.getParameter("positions");
		
		int portfolioID = 0;
		boolean closedInd = false;
		boolean taxableInd = false;
		
		if (closedIndParm != null && closedIndParm.equalsIgnoreCase("Y"))
		{
			closedInd = true;
		}
		
		if (taxableIndParm != null && taxableIndParm.equalsIgnoreCase("Y"))
		{
			taxableInd = true;
		}
		
		AccountDAO accountDAOReference;
			
		try 
		{
			accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
		} 
		catch (SystemException e) 
		{
			log.error("error retrieving accounts", e);
			throw new DAOException(e);
		}	
		
		AccountSelection accSel = new AccountSelection();
		accSel.setInvestorID(Integer.valueOf(investor.getInvestorID()));
							
		List<Tblaccount> rsAccounts = accountDAOReference.inquire(accSel);
		List<DropDownBean> investorAccounts = new ArrayList<>();
		
		log.debug("looping through recordset to build Accounts.  There are " + rsAccounts.size() + " accounts in this list..");

		for (int i = 0; i < rsAccounts.size(); i++)
		{
			Tblaccount acct = rsAccounts.get(i);
			
			//log.debug("Working on Account ID = " + acct.getIaccountId() + " name = " + acct.getSaccountName());
			
			if (i ==0)
			{
				portfolioID = acct.getTblportfolio().getIportfolioId();
			}			
					
			if (closedInd)		
			{				
				if (acct.isBclosed()) //closed accounts
				{
					DropDownBean ddBean = new DropDownBean();
					ddBean.setId(String.valueOf(acct.getIaccountId()));
					ddBean.setDescription(acct.getSaccountName());
					investorAccounts.add(ddBean);
				}
			}
			else //user is looking for open accounts
			{
				if (!acct.isBclosed()) //this means it's an open account
				{
					if (positionsParm != null && positionsParm.equalsIgnoreCase("true")) //just do all open accounts when doing positions
					{
						DropDownBean ddBean = new DropDownBean();
						ddBean.setId(String.valueOf(acct.getIaccountId()));
						ddBean.setDescription(acct.getSaccountName());
						investorAccounts.add(ddBean);
					}
					else if (taxableInd && acct.isBtaxableInd()) //open, and taxable.
					{
						DropDownBean ddBean = new DropDownBean();
						ddBean.setId(String.valueOf(acct.getIaccountId()));
						ddBean.setDescription(acct.getSaccountName());
						investorAccounts.add(ddBean);
					}
					else if (!taxableInd && !acct.isBtaxableInd())  //user is looking for open, retirement accounts		
					{
						DropDownBean ddBean = new DropDownBean();
						ddBean.setId(String.valueOf(acct.getIaccountId()));
						ddBean.setDescription(acct.getSaccountName());
						investorAccounts.add(ddBean);
					}
				}
			}
		}
		
		request.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_ACCOUNTS, investorAccounts);			
		request.getSession().setAttribute("showTrxItems", !closedInd);	
		request.getSession().setAttribute("portfolioID", portfolioID);	
		
		String action = "success";
		
		if (positionsParm != null && positionsParm.equalsIgnoreCase("true"))
		{
			action = "successPositions";
		}
		return mapping.findForward(action);
	}
	
}
