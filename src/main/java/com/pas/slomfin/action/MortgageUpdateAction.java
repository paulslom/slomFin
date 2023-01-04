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
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tblmortgage;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.MortgageUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.AccountDAO;
import com.pas.slomfin.dao.InvestorDAO;
import com.pas.slomfin.dao.MortgageDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.Investor;

public class MortgageUpdateAction extends SlomFinStandardAction
{
	@SuppressWarnings("unchecked")
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside MortgageUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tblmortgage mortgage = new Tblmortgage();
		
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
		
		if (operation == IAppConstants.DELETE_ACTION
		||	operation == IAppConstants.UPDATE_ACTION)
		{
			String mortgageID = req.getParameter("mortgageID"); //hidden field on jsp
			MortgageDAO mortDAOReference;
			
			try
			{
				mortDAOReference = (MortgageDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.MORTGAGE_DAO);
				List<Tblmortgage> mortList = mortDAOReference.inquire(new Integer(mortgageID));
				mortgage = mortList.get(0);			
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
			MortgageUpdateForm mortgageForm = (MortgageUpdateForm)form;
			
			mortgage.setSdescription(mortgageForm.getDescription());
			
			if (operation == IAppConstants.ADD_ACTION)
			{	
				Tblaccount paymentAcct = new Tblaccount();
				Tblaccount principalAcct = new Tblaccount();
				
				AccountDAO acctDAOReference;
				
				try
				{
					acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
					List<Tblaccount> paymentAcctList = acctDAOReference.inquire(Integer.parseInt(mortgageForm.getPaymentAccountID()));
					paymentAcct = paymentAcctList.get(0);
					mortgage.setTblaccountByIPaymentAccountId(paymentAcct);
					List<Tblaccount> principalAcctList = acctDAOReference.inquire(Integer.parseInt(mortgageForm.getPrincipalAccountID()));
					principalAcct = principalAcctList.get(0);
					mortgage.setTblaccountByIPrincipalAccountId(principalAcct);		
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new PASSystemException(e1);
				}
				
				Investor investor = cache.getInvestor(req.getSession());
				
				InvestorDAO investorDAOReference;
				
				try
				{
					investorDAOReference = (InvestorDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTOR_DAO);
					List<Tblinvestor> investorList = investorDAOReference.inquire(new Integer(investor.getInvestorID()));
					mortgage.setTblinvestor(investorList.get(0));			
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new PASSystemException(e1);
				}
						
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			try
			{
				date = sdf.parse(mortgageForm.getMortgageStartDate().toStringYYYYMMDD());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				mortgage.setDmortgageStartDate(timestamp);
			}
			catch (ParseException e)
			{	
				log.error("error parsing Mortgage Start Date in MortgageUpdateAction pre-process");
				e.printStackTrace();
				throw new PresentationException("error parsing Mortgage Start date in MortgageUpdateAction pre-process");				
			}	
			
			if (mortgageForm.getMortgageEndDate().getAppday() != null)
				try
				{
					date = sdf.parse(mortgageForm.getMortgageEndDate().toStringYYYYMMDD());
					java.sql.Timestamp timestamp2 = new java.sql.Timestamp(date.getTime());
					mortgage.setDmortgageEndDate(timestamp2);
				}
				catch (ParseException e)
				{	
					log.error("error parsing Mortgage End date in MortgageUpdateAction pre-process");
					e.printStackTrace();
					throw new PresentationException("error parsing Mortgage End date in MortgageUpdateAction pre-process");				
				}	
			
			
			mortgage.setMoriginalLoanAmount(new BigDecimal(mortgageForm.getOriginalLoanAmount()));	
			mortgage.setDinterestRate(new BigDecimal(mortgageForm.getInterestRate()));
			mortgage.setItermInYears(mortgageForm.getTermInYears());
			mortgage.setMhomeOwnersIns(new BigDecimal(mortgageForm.getHomeownersInsurance()));	
			
			if (mortgageForm.getPmi() == null)
				mortgage.setMpmi(new BigDecimal(0));
			else
				if (mortgageForm.getPmi().length()==0)
					mortgage.setMpmi(new BigDecimal(0));
				else
			        mortgage.setMpmi(new BigDecimal(mortgageForm.getPmi()));
			
			mortgage.setMpropertyTaxes(new BigDecimal(mortgageForm.getPropertyTaxes()));	
			
			if (mortgageForm.getExtraPrincipal() != null)
				if (mortgageForm.getExtraPrincipal().length()!=0)
			        mortgage.setMextraPrincipal(new BigDecimal(mortgageForm.getExtraPrincipal()));
			
			cache.setGoToDBInd(req.getSession(), true);
									
		}
			
		cache.setObject("RequestObject", mortgage, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside MortgageUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("mortgageShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting MortgageUpdateAction postprocessAction");
				
	}

}
