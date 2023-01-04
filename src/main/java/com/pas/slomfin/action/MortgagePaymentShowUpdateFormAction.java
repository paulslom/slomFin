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
import com.pas.dbobjects.Tblmortgagehistory;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.MortgagePaymentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.MortgagePaymentSelection;
import com.pas.valueObject.AppDate;

public class MortgagePaymentShowUpdateFormAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside MortgagePaymentShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		        
		MortgagePaymentUpdateForm mortgagePaymentForm = (MortgagePaymentUpdateForm) form;		
		mortgagePaymentForm.initialize();
		
		String mortgagePaymentShowParm = req.getParameter("mortgagePaymentShowParm");
				
		if (mortgagePaymentShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{	
		   cache.setGoToDBInd(req.getSession(), true);
		   MortgagePaymentSelection mortgagePaymentSelection = new MortgagePaymentSelection();
		   mortgagePaymentSelection.setMortgagePaymentID(new Integer(-1));
		   mortgagePaymentSelection.setMortgageID(new Integer(req.getParameter("mortgageID")));
		   cache.setObject("RequestObject", mortgagePaymentSelection, req.getSession());		
		}
		else
		{
		   cache.setGoToDBInd(req.getSession(), true);	
		   String mortgagePaymentID = req.getParameter("mortgagePaymentID");		  
		   cache.setObject("RequestObject", new Integer(mortgagePaymentID), req.getSession());		
		}   
			
		log.debug("exiting MortgagePaymentShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside MortgagePaymentShowUpdateFormAction postprocessAction");
	
		String mortgagePaymentShowParm = req.getParameter("mortgagePaymentShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
				
		List<Tblmortgagehistory> mortgagePaymentList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tblmortgagehistory mortgagePayment = new Tblmortgagehistory();
		
		MortgagePaymentUpdateForm mortgagePaymentUpdateForm = (MortgagePaymentUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
				
		mortgagePayment = mortgagePaymentList.get(0);			
			
		if (!(mortgagePaymentShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		{
			mortgagePaymentUpdateForm.setMortgagePaymentID(mortgagePayment.getImortgageHistoryId());
		}
			
		mortgagePaymentUpdateForm.setMortgageID(mortgagePayment.getTblmortgage().getImortgageId());
		mortgagePaymentUpdateForm.setMortgageDescription(mortgagePayment.getTblmortgage().getSdescription());
					
		if (mortgagePayment.getMprincipalPaid() == null)
		   mortgagePaymentUpdateForm.setPrincipalPaid(null);
		else
		   mortgagePaymentUpdateForm.setPrincipalPaid(amtFormat.format(mortgagePayment.getMprincipalPaid()));	
		
		if (mortgagePayment.getMinterestPaid() == null)
		   mortgagePaymentUpdateForm.setInterestPaid(null);
		else
		   mortgagePaymentUpdateForm.setInterestPaid(amtFormat.format(mortgagePayment.getMinterestPaid()));
		
		if (mortgagePayment.getMpropertyTaxesPaid() == null)
		   mortgagePaymentUpdateForm.setPropertyTaxesPaid(null);
		else
		   mortgagePaymentUpdateForm.setPropertyTaxesPaid(amtFormat.format(mortgagePayment.getMpropertyTaxesPaid()));
		
		if (mortgagePayment.getMhomeownersInsPaid() == null)
		   mortgagePaymentUpdateForm.setHomeownersInsurancePaid(null);
		else
		   mortgagePaymentUpdateForm.setHomeownersInsurancePaid(amtFormat.format(mortgagePayment.getMhomeownersInsPaid()));	
		
		if (mortgagePayment.getMpmipaid() == null)
		   mortgagePaymentUpdateForm.setPmiPaid(null);
		else
		   mortgagePaymentUpdateForm.setPmiPaid(amtFormat.format(mortgagePayment.getMpmipaid()));	
					
		AppDate appDate = new AppDate();
		Calendar tempCal = Calendar.getInstance();
		
		if (mortgagePayment.getDpaymentDate() == null)
			mortgagePaymentUpdateForm.setMortgagePaymentDate(null);
		else
		{	
			tempCal.setTimeInMillis(mortgagePayment.getDpaymentDate().getTime());
			appDate.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
			appDate.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
			appDate.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
			mortgagePaymentUpdateForm.setMortgagePaymentDate(appDate);
		}
				
		if (mortgagePaymentShowParm.equalsIgnoreCase(IAppConstants.ADD)
		||	mortgagePaymentShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
		{	
			String mortgagesListName = ISlomFinAppConstants.DROPDOWN_MORTGAGES;
			List morts = (List) req.getSession().getServletContext().getAttribute(mortgagesListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_MORTGAGES, morts);	
		}
		
		String mtgPmtUpdateOrigin = req.getParameter("mtgPmtUpdateOrigin");
		
		if (mtgPmtUpdateOrigin == null)
			req.getSession().setAttribute("mtgPmtUpdateOrigin", "other");
		else //means we need to head back to reportMortgagePaymentsAction when done
		{	
			req.getSession().setAttribute("mtgPmtUpdateOrigin", mtgPmtUpdateOrigin);
			req.getSession().setAttribute("reportMortgageID",mortgagePayment.getTblmortgage().getImortgageId().toString());
		}

		cache.setObject("ResponseObject", mortgagePayment, req.getSession());
		
		req.getSession().setAttribute("mortgagePaymentShowParm",mortgagePaymentShowParm);
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting MortgagePaymentShowUpdateFormAction postprocessAction");
				
	}
				
}
