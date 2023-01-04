package com.pas.slomfin.action;

import java.text.DecimalFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.TaxGroupYearUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.valueObject.DropDownBean;

public class TaxGroupYearShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxGroupYearShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		   
		TaxGroupYearUpdateForm taxGroupYearForm = (TaxGroupYearUpdateForm) form;		
		taxGroupYearForm.initialize();
		
		String tgyShowParm = req.getParameter("tgyShowParm");
				
		if (tgyShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);		
		else
		{
		   String taxGroupYearID = req.getParameter("taxGroupYearID");
		   cache.setObject("RequestObject", new Integer(taxGroupYearID), req.getSession());		
		   cache.setGoToDBInd(req.getSession(), true);		
		}   
			
		log.debug("exiting TaxGroupYearShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxGroupYearShowUpdateFormAction postprocessAction");
	
		String tgyShowParm = req.getParameter("tgyShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tbltaxgroupyear> taxGroupYearList = null;
		
		if (!(tgyShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   taxGroupYearList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tbltaxgroupyear taxGroupYear = new Tbltaxgroupyear();
		
		TaxGroupYearUpdateForm taxGroupYearForm = (TaxGroupYearUpdateForm)form; 
						
		if (tgyShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			taxGroupYearForm.initialize();
			Investor investor = new Investor();
			investor = cache.getInvestor(req.getSession());
			taxGroupYearForm.setTaxGroupID(new Integer(investor.getTaxGroupID()));
		}
		else //not an add	
		{	
		   	taxGroupYear = taxGroupYearList.get(0);			
			
			taxGroupYearForm.setTaxGroupYearID(taxGroupYear.getItaxGroupYearId());
			taxGroupYearForm.setTaxGroupID(taxGroupYear.getTbltaxgroup().getItaxGroupId());	
			taxGroupYearForm.setTaxGroupName(taxGroupYear.getTbltaxgroup().getStaxGroupName());	
			taxGroupYearForm.setTaxYear(taxGroupYear.getItaxYear());	
			taxGroupYearForm.setFilingStatus(taxGroupYear.getSfilingStatus());	
			taxGroupYearForm.setDependents(taxGroupYear.getIdependents().toString());	
			
			DecimalFormat amtFormat = new DecimalFormat("#####0.00");
			DecimalFormat unitsFormat = new DecimalFormat("######.####");
				
			if (taxGroupYear.getSotherItemizedDesc() == null)
			   taxGroupYearForm.setOtherItemizedDesc(null);
			else
			   taxGroupYearForm.setOtherItemizedDesc(taxGroupYear.getSotherItemizedDesc());
			
			if (taxGroupYear.getSotherIncomeDesc() == null)
			   taxGroupYearForm.setOtherIncomeDesc(null);
			else
			   taxGroupYearForm.setOtherIncomeDesc(taxGroupYear.getSotherIncomeDesc());
			
			if (taxGroupYear.getMcapitalLossCarryover() == null)
			   taxGroupYearForm.setCapitalLossCarryover(null);
			else
			   taxGroupYearForm.setCapitalLossCarryover(amtFormat.format(taxGroupYear.getMcapitalLossCarryover()));
			
			if (taxGroupYear.getMiradistribution() == null)
			  taxGroupYearForm.setIraDistribution(null);
			else
			   taxGroupYearForm.setIraDistribution(amtFormat.format(taxGroupYear.getMiradistribution()));
			
			if (taxGroupYear.getMprevYearStateRefund() == null)
			   taxGroupYearForm.setPrevYearStateRefund(null);
			else
			   taxGroupYearForm.setPrevYearStateRefund(amtFormat.format(taxGroupYear.getMprevYearStateRefund()));
			
			if (taxGroupYear.getMotherItemized() == null)
			   taxGroupYearForm.setOtherItemized(null);
			else
			   taxGroupYearForm.setOtherItemized(amtFormat.format(taxGroupYear.getMotherItemized()));
			
			if (taxGroupYear.getMcarTax() == null)
			   taxGroupYearForm.setCarTax(null);
			else
			   taxGroupYearForm.setCarTax(amtFormat.format(taxGroupYear.getMcarTax()));
			
			if (taxGroupYear.getDdividendTaxRate() == null)
			   taxGroupYearForm.setDividendTaxRate(null);
			else
			   taxGroupYearForm.setDividendTaxRate(unitsFormat.format(taxGroupYear.getDdividendTaxRate()));
			
			if (taxGroupYear.getMotherIncome() == null)
			   taxGroupYearForm.setOtherIncome(null);
			else
			   taxGroupYearForm.setOtherIncome(amtFormat.format(taxGroupYear.getMotherIncome()));
			
			if (taxGroupYear.getMdayCareExpensesPaid() == null)
			   taxGroupYearForm.setDayCareExpensesPaid(null);
			else
			   taxGroupYearForm.setDayCareExpensesPaid(amtFormat.format(taxGroupYear.getMdayCareExpensesPaid()));
			
			if (taxGroupYear.getMqualifiedDividends() == null)
			   taxGroupYearForm.setQualifiedDividends(null);
			else
			   taxGroupYearForm.setQualifiedDividends(amtFormat.format(taxGroupYear.getMqualifiedDividends()));
			
			if (taxGroupYear.getMdividendsForeignTax() == null)
			   taxGroupYearForm.setDividendsForeignTax(null);
			else
			   taxGroupYearForm.setDividendsForeignTax(amtFormat.format(taxGroupYear.getMdividendsForeignTax()));
			
			if (taxGroupYear.getMdividendsReturnOfCapital() == null)
			   taxGroupYearForm.setDividendsReturnOfCapital(null);
			else
			   taxGroupYearForm.setDividendsReturnOfCapital(amtFormat.format(taxGroupYear.getMdividendsReturnOfCapital()));
		}
		
		if (tgyShowParm.equalsIgnoreCase(IAppConstants.ADD)
		||	tgyShowParm.equalsIgnoreCase(IAppConstants.UPDATE))
		{	
			String taxGroupsListName = ISlomFinAppConstants.DROPDOWN_TAXGROUPS;
			List<DropDownBean> tgs = (List<DropDownBean>) req.getSession().getServletContext().getAttribute(taxGroupsListName);
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_DD_TAXGROUPS, tgs);	
		}
		
		cache.setObject("ResponseObject", taxGroupYear, req.getSession());
		req.getSession().setAttribute("tgyShowParm",tgyShowParm);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxGroupYearShowUpdateFormAction postprocessAction");
				
	}
						
}
