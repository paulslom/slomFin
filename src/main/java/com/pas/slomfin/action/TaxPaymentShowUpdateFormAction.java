package com.pas.slomfin.action;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxpayment;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.TaxPaymentUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.AppDate;

public class TaxPaymentShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxPaymentShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		   
		TaxPaymentUpdateForm taxPaymentForm = (TaxPaymentUpdateForm) form;		
		taxPaymentForm.initialize();
		
		String taxPaymentShowParm = req.getParameter("taxPaymentShowParm");
				
		if (taxPaymentShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String taxPaymentID = req.getParameter("taxPaymentID");
		   cache.setObject("RequestObject", new Integer(taxPaymentID), req.getSession());		
		   cache.setGoToDBInd(req.getSession(), true);			
		}   
						
		
		
		log.debug("exiting TaxPaymentShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxPaymentShowUpdateFormAction postprocessAction");
	
		String taxPaymentShowParm = req.getParameter("taxPaymentShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tbltaxpayment> taxPaymentList = null;
		
		if (!(taxPaymentShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   taxPaymentList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tbltaxpayment taxPayment = new Tbltaxpayment();
		
		TaxPaymentUpdateForm taxPaymentForm = (TaxPaymentUpdateForm)form; 
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		
		if (taxPaymentShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			taxPaymentForm.initialize();
			
			AppDate pmtDate = new AppDate();
			pmtDate.initAppDateToToday();
			taxPaymentForm.setTaxPaymentDate(pmtDate);
			Investor investor = new Investor();
			investor = cache.getInvestor(req.getSession());
			taxPaymentForm.setTaxGroupID(new Integer(investor.getTaxGroupID()));
		}
		else //not an add	
		{	
		   	taxPayment = taxPaymentList.get(0);			
			
			taxPaymentForm.setTaxPaymentID(taxPayment.getItaxPaymentId());			
			taxPaymentForm.setTaxGroupID(taxPayment.getTbltaxgroup().getItaxGroupId());
			taxPaymentForm.setTaxGroupName(taxPayment.getTbltaxgroup().getStaxGroupName());
			taxPaymentForm.setTaxYear(taxPayment.getItaxYear());
			taxPaymentForm.setTaxPaymentTypeID(taxPayment.getTbltaxpaymenttype().getItaxPaymentTypeId());	
			taxPaymentForm.setTaxPaymentTypeDesc(taxPayment.getTbltaxpaymenttype().getStaxPaymentTypeDesc());
			taxPaymentForm.setTaxPaymentDesc(taxPayment.getStaxPaymentDesc());
						
			if (taxPayment.getMtaxPaymentAmount() == null)
				taxPaymentForm.setTaxPaymentAmount(null);
			else
				taxPaymentForm.setTaxPaymentAmount(amtFormat.format(taxPayment.getMtaxPaymentAmount()));	
			
			AppDate appDate = new AppDate();
			Calendar tempCal = Calendar.getInstance();
			
			if (taxPayment.getDtaxPaymentDate() == null)
				taxPaymentForm.setTaxPaymentDate(null);
			else
			{	
				tempCal.setTimeInMillis(taxPayment.getDtaxPaymentDate().getTime());
				appDate.setAppday(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
				appDate.setAppmonth(String.valueOf(tempCal.get(Calendar.MONTH)+1));
				appDate.setAppyear(String.valueOf(tempCal.get(Calendar.YEAR)));
				taxPaymentForm.setTaxPaymentDate(appDate);
			}
									
		}
		
		if (taxPaymentShowParm.equalsIgnoreCase(IAppConstants.ADD)
		||	taxPaymentShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
		{	
			String taxPaymentTypesListName = ISlomFinAppConstants.DROPDOWN_TAXPAYMENTTYPES;
			List txpts = (List) req.getSession().getServletContext().getAttribute(taxPaymentTypesListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_TAXPAYMENTTYPES, txpts);	
			
			String taxGroupsListName = ISlomFinAppConstants.DROPDOWN_TAXGROUPS;
			List tgs = (List) req.getSession().getServletContext().getAttribute(taxGroupsListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_TAXGROUPS, tgs);	
			
		}
		
		cache.setObject("ResponseObject", taxPayment, req.getSession());
		req.getSession().setAttribute("taxPaymentShowParm",taxPaymentShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxPaymentShowUpdateFormAction postprocessAction");
				
	}
						
}
