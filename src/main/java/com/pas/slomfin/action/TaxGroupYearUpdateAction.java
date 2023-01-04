package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxgroup;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.TaxGroupYearUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TaxGroupDAO;
import com.pas.slomfin.dao.TaxGroupYearDAO;
import com.pas.slomfin.valueObject.Investor;

public class TaxGroupYearUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxGroupYearUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbltaxgroupyear taxGroupYear = new Tbltaxgroupyear();
			
		String reqParm = req.getParameter("operation");
		
		//go to DAO but do not do anything when cancelling an update or delete, or returning from an inquire
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			cache.setGoToDBInd(req.getSession(), false);
		
		if (operation == IAppConstants.DELETE_ACTION
		||	operation == IAppConstants.UPDATE_ACTION)
		{
			String taxGroupYearID = req.getParameter("taxGroupYearID"); //hidden field on jsp
			TaxGroupYearDAO taxGroupYearDAOReference;
			
			try
			{
				taxGroupYearDAOReference = (TaxGroupYearDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXGROUPYEAR_DAO);
				List<Tbltaxgroupyear> txgyList = taxGroupYearDAOReference.inquire(new Integer(taxGroupYearID));
				taxGroupYear = txgyList.get(0);			
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
			TaxGroupYearUpdateForm taxGroupYearForm = (TaxGroupYearUpdateForm)form;
		
			if (operation == IAppConstants.ADD_ACTION)
			{
				taxGroupYear.setItaxYear(taxGroupYearForm.getTaxYear());
				Investor investor = cache.getInvestor(req.getSession());

				TaxGroupDAO txgDAOReference;
				
				try
				{
					txgDAOReference = (TaxGroupDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXGROUP_DAO);
					List<Tbltaxgroup> txgList = txgDAOReference.inquire(new Integer(investor.getTaxGroupID()));
					taxGroupYear.setTbltaxgroup(txgList.get(0));			
				}
				catch (SystemException e1)
				{
					log.error("SystemException encountered: " + e1.getMessage());
					e1.printStackTrace();
					throw new PASSystemException(e1);
				}
			}
			
			taxGroupYear.setSfilingStatus(taxGroupYearForm.getFilingStatus());	
			taxGroupYear.setIdependents(new Integer(taxGroupYearForm.getDependents()));	
					
			if (taxGroupYearForm.getOtherItemizedDesc() == null)
			   taxGroupYear.setSotherItemizedDesc(null);
			else
			   taxGroupYear.setSotherItemizedDesc(taxGroupYearForm.getOtherItemizedDesc());
			
			if (taxGroupYearForm.getOtherIncomeDesc() == null)
			   taxGroupYear.setSotherIncomeDesc(null);
			else
			   taxGroupYear.setSotherIncomeDesc(taxGroupYearForm.getOtherIncomeDesc());
			
			if (taxGroupYearForm.getCapitalLossCarryover().length() == 0)
			   taxGroupYear.setMcapitalLossCarryover(null);
			else
			   taxGroupYear.setMcapitalLossCarryover(new BigDecimal(taxGroupYearForm.getCapitalLossCarryover()));
			
			if (taxGroupYearForm.getIraDistribution().length() == 0)
			  taxGroupYear.setMiradistribution(null);
			else
			   taxGroupYear.setMiradistribution(new BigDecimal(taxGroupYearForm.getIraDistribution()));
			
			if (taxGroupYearForm.getPrevYearStateRefund().length() == 0)
			   taxGroupYear.setMprevYearStateRefund(null);
			else
			   taxGroupYear.setMprevYearStateRefund(new BigDecimal(taxGroupYearForm.getPrevYearStateRefund()));
			
			if (taxGroupYearForm.getOtherItemized().length() == 0)
			   taxGroupYear.setMotherItemized(null);
			else
			   taxGroupYear.setMotherItemized(new BigDecimal(taxGroupYearForm.getOtherItemized()));
			
			if (taxGroupYearForm.getCarTax().length() == 0)
			   taxGroupYear.setMcarTax(null);
			else
			   taxGroupYear.setMcarTax(new BigDecimal(taxGroupYearForm.getCarTax()));
			
			if (taxGroupYearForm.getDividendTaxRate().length() == 0)
			   taxGroupYear.setDdividendTaxRate(null);
			else
			   taxGroupYear.setDdividendTaxRate(new BigDecimal(taxGroupYearForm.getDividendTaxRate()));
			
			if (taxGroupYearForm.getOtherIncome().length() == 0)
			   taxGroupYear.setMotherIncome(null);
			else
			   taxGroupYear.setMotherIncome(new BigDecimal(taxGroupYearForm.getOtherIncome()));
			
			if (taxGroupYearForm.getDayCareExpensesPaid().length() == 0)
			   taxGroupYear.setMdayCareExpensesPaid(null);
			else
			   taxGroupYear.setMdayCareExpensesPaid(new BigDecimal(taxGroupYearForm.getDayCareExpensesPaid()));
			
			if (taxGroupYearForm.getQualifiedDividends().length() == 0)
			   taxGroupYear.setMqualifiedDividends(null);
			else
			   taxGroupYear.setMqualifiedDividends(new BigDecimal(taxGroupYearForm.getQualifiedDividends()));
			
			if (taxGroupYearForm.getDividendsForeignTax().length() == 0)
			   taxGroupYear.setMdividendsForeignTax(null);
			else
			   taxGroupYear.setMdividendsForeignTax(new BigDecimal(taxGroupYearForm.getDividendsForeignTax()));
			
			if (taxGroupYearForm.getDividendsReturnOfCapital().length() == 0)
			   taxGroupYear.setMdividendsReturnOfCapital(null);
			else
			   taxGroupYear.setMdividendsReturnOfCapital(new BigDecimal(taxGroupYearForm.getDividendsReturnOfCapital()));
			   									
			cache.setGoToDBInd(req.getSession(), true);
			
		}
		
		cache.setObject("RequestObject", taxGroupYear, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxGroupYearUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("tgyShowParm");
				
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxGroupYearUpdateAction postprocessAction");
				
	}

}
