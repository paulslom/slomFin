package com.pas.slomfin.action;

import java.util.Calendar;
import java.util.List;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
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
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.AppDate;
import com.pas.valueObject.DropDownBean;

public class MortgageShowUpdateFormAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("in pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Integer mortID = new Integer(0);
        
		MortgageUpdateForm mortgageForm = (MortgageUpdateForm) form;		
		mortgageForm.initialize();
		
		String mortgageShowParm = req.getParameter("mortgageShowParm");
				
		if (mortgageShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String mortgageID = req.getParameter("mortgageID");
		   mortID= Integer.valueOf(mortgageID);
		   cache.setGoToDBInd(req.getSession(), true);
		}   
						
		cache.setObject("RequestObject", mortID, req.getSession());		
		
		log.debug("exiting MortgageShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside MortgageShowUpdateFormAction postprocessAction");
	
		String mortgageShowParm = req.getParameter("mortgageShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Investor investor = cache.getInvestor(req.getSession());
		
		List<Tblmortgage> mortgageList = null;
		
		if (!(mortgageShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   mortgageList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tblmortgage mortgage = new Tblmortgage();
		
		MortgageUpdateForm mortgageForm = (MortgageUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
				
		if (mortgageShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			mortgageForm.initialize();				
		}
		else //not an add	
		{	
			mortgage = mortgageList.get(0);			
			
			mortgageForm.setMortgageID(mortgage.getImortgageId());
			
			mortgageForm.setDescription(mortgage.getSdescription());
			mortgageForm.setPaymentAccountID(mortgage.getTblaccountByIPaymentAccountId().getIaccountId().toString());
			mortgageForm.setPaymentAccountDescription(mortgage.getTblaccountByIPaymentAccountId().getSaccountName());
			mortgageForm.setPrincipalAccountID(mortgage.getTblaccountByIPrincipalAccountId().getIaccountId().toString());
			mortgageForm.setPrincipalAccountDescription(mortgage.getTblaccountByIPrincipalAccountId().getSaccountName());
			mortgageForm.setTermInYears(mortgage.getItermInYears());
		
			if (mortgage.getMoriginalLoanAmount() == null)
			   mortgageForm.setOriginalLoanAmount(null);
			else
			   mortgageForm.setOriginalLoanAmount(amtFormat.format(mortgage.getMoriginalLoanAmount()));	
						
			if (mortgage.getDinterestRate() == null)
			   mortgageForm.setInterestRate(null);
			else
			{
				DecimalFormat rateFormat = new DecimalFormat("######.####");
				mortgageForm.setInterestRate(rateFormat.format(mortgage.getDinterestRate()));			   
			}
			
			if (mortgage.getMhomeOwnersIns() == null)
			   mortgageForm.setHomeownersInsurance(null);
			else
			   mortgageForm.setHomeownersInsurance(amtFormat.format(mortgage.getMhomeOwnersIns()));	
			
			if (mortgage.getMpmi() == null)
			   mortgageForm.setPmi(null);
			else
			   mortgageForm.setPmi(amtFormat.format(mortgage.getMpmi()));	
			
			if (mortgage.getMpropertyTaxes() == null)
			   mortgageForm.setPropertyTaxes(null);
			else
			   mortgageForm.setPropertyTaxes(amtFormat.format(mortgage.getMpropertyTaxes()));
			
			if (mortgage.getMextraPrincipal() == null)
			   mortgageForm.setExtraPrincipal(null);
			else
			   mortgageForm.setExtraPrincipal(amtFormat.format(mortgage.getMextraPrincipal()));	
			
			AppDate appDate = new AppDate();
			Calendar tempCal = Calendar.getInstance();
			
			if (mortgage.getDmortgageStartDate() == null)
				mortgageForm.setMortgageStartDate(null);
			else
			{	
				tempCal.setTimeInMillis(mortgage.getDmortgageStartDate().getTime());
				appDate.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
				appDate.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
				appDate.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
				mortgageForm.setMortgageStartDate(appDate);
			}
			
			AppDate appDate2 = new AppDate();
			Calendar tempCal2 = Calendar.getInstance();
			
			if (mortgage.getDmortgageEndDate() == null)
				mortgageForm.setMortgageEndDate(null);
			else
			{	
				tempCal2.setTimeInMillis(mortgage.getDmortgageEndDate().getTime());
				appDate2.setAppday(String.valueOf(tempCal2.get(Calendar.DAY_OF_MONTH)));
				appDate2.setAppmonth(String.valueOf(tempCal2.get(Calendar.MONTH)+1));
				appDate2.setAppyear(String.valueOf(tempCal2.get(Calendar.YEAR)));
				mortgageForm.setMortgageEndDate(appDate2);
			}
						
		}
		
		if (mortgageShowParm.equalsIgnoreCase(IAppConstants.ADD)
		||	mortgageShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
		{	
			List mortgageAccounts = null; 
			try
			{
			   mortgageAccounts = getMortgageAccounts(new Integer(investor.getInvestorID()));
			   req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_MTGACCOUNTS, mortgageAccounts);	
			}
			catch (SystemException e)
			{
			   log.error("MortgageShowUpdateFormAction SystemException" + e.getMessage());
			   e.printStackTrace();
			   throw new PASSystemException(e);
			}
		}
		
		cache.setObject("ResponseObject", mortgage, req.getSession());
		
		req.getSession().setAttribute("mortgageShowParm",mortgageShowParm);
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting MortgageShowUpdateFormAction postprocessAction");
				
	}
	
	private List<DropDownBean> getMortgageAccounts(Integer investorID)
	   throws PASSystemException, DAOException, SystemException
	{		
		
		AccountDAO daoReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			
		log.debug("retrieving mortgage account dropdown items for investor id = " + investorID);
			
		return daoReference.getOpenTaxableAccounts(investorID);
	}
			
}
