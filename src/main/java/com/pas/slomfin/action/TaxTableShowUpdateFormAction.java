package com.pas.slomfin.action;

import java.text.DecimalFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.dbobjects.Tbltaxformulas;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.TaxTableUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;

public class TaxTableShowUpdateFormAction extends SlomFinStandardAction
{	
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxTableShowUpdateFormAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
			TaxTableUpdateForm taxTableForm = (TaxTableUpdateForm) form;		
		taxTableForm.initialize();
		
		String taxTableShowParm = req.getParameter("taxTableShowParm");
				
		if (taxTableShowParm.equalsIgnoreCase(IAppConstants.ADD))
			cache.setGoToDBInd(req.getSession(), false);
		else
		{
		   String taxFormulaID = req.getParameter("taxFormulaID");
		   cache.setObject("RequestObject", new Integer(taxFormulaID), req.getSession());	
		   cache.setGoToDBInd(req.getSession(), true);			
		}   
					
		log.debug("exiting TaxTableShowUpdateFormAction pre - process");
		
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxTableShowUpdateFormAction postprocessAction");
	
		String taxTableShowParm = req.getParameter("taxTableShowParm");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		
		List<Tbltaxformulas> taxTableList = null;
		
		if (!(taxTableShowParm.equalsIgnoreCase(IAppConstants.ADD)))
		   taxTableList = (List)cache.getObject("ResponseObject",req.getSession());		
		
		Tbltaxformulas taxTable = new Tbltaxformulas();
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		DecimalFormat unitsFormat = new DecimalFormat("######.####");
		
		String tRate = "false";
		String tAmount = "false";
		String tNC = "false";
		String tCT = "false";
		String tFederal = "false";
			
		TaxTableUpdateForm taxTableForm = (TaxTableUpdateForm)form; 
						
		if (taxTableShowParm.equalsIgnoreCase(IAppConstants.ADD))
		{
			taxTableForm.initialize();				
		}
		else //not an add	
		{	
		   	taxTable = taxTableList.get(0);
		   	
			String formulaTypeDesc = taxTable.getTbltaxformulatype().getSformulaDescription();
			
		   	taxTableForm.setTaxFormulaID(taxTable.getItaxFormulaId());
			taxTableForm.setTaxFormulaTypeDesc(formulaTypeDesc);
			taxTableForm.setTaxFormulaTypeID(taxTable.getTbltaxformulatype().getItaxFormulaTypeId());			
			taxTableForm.setTaxYear(taxTable.getItaxYear());
			
			if (formulaTypeDesc.endsWith("Rate"))
				tRate = "true";
			
			if (formulaTypeDesc.startsWith("CT"))
				tCT = "true";
			
			if (formulaTypeDesc.startsWith("NC"))
				tNC = "true";
			
			if (formulaTypeDesc.startsWith("Federal"))
				tFederal = "true";
			
			if (formulaTypeDesc.endsWith("Amount"))
				tAmount = "true";
			
		   	if (taxTable.getMchildCredit() == null)
			   taxTableForm.setChildCredit(null);
			else
			   taxTableForm.setChildCredit(amtFormat.format(taxTable.getMchildCredit()));	
			
			if (taxTable.getDstateTaxCreditRate() == null)
			   taxTableForm.setStateTaxCreditRate(null);
			else
			   taxTableForm.setStateTaxCreditRate(unitsFormat.format(taxTable.getDstateTaxCreditRate()));	
						
			if (taxTable.getMstatePropertyTaxLimit() == null)
			   taxTableForm.setStatePropertyTaxLimit(null);
			else
			   taxTableForm.setStatePropertyTaxLimit(amtFormat.format(taxTable.getMstatePropertyTaxLimit()));	
			
			if (taxTable.getMstandardDeduction() == null)
			   taxTableForm.setStandardDeduction(null);
			else
			   taxTableForm.setStandardDeduction(amtFormat.format(taxTable.getMstandardDeduction()));	
			
			if (taxTable.getMncunderExemptionAmount() == null)
			   taxTableForm.setNcUnderExemptionAmount(null);
			else
			   taxTableForm.setNcUnderExemptionAmount(amtFormat.format(taxTable.getMncunderExemptionAmount()));	
			
			if (taxTable.getMncoverExemptionAmount() == null)
			   taxTableForm.setNcOverExemptionAmount(null);
			else
			   taxTableForm.setNcOverExemptionAmount(amtFormat.format(taxTable.getMncoverExemptionAmount()));	
			
			if (taxTable.getMncexemptionThreshold() == null)
			   taxTableForm.setNcExemptionThreshold(null);
			else
			   taxTableForm.setNcExemptionThreshold(amtFormat.format(taxTable.getMncexemptionThreshold()));	
			
			if (taxTable.getMncchildCreditThreshold() == null)
			   taxTableForm.setNcChildCreditThreshold(null);
			else
			   taxTableForm.setNcChildCreditThreshold(amtFormat.format(taxTable.getMncchildCreditThreshold()));	
			
			if (taxTable.getMincomeLow() == null)
			   taxTableForm.setIncomeLow(null);
			else
			   taxTableForm.setIncomeLow(amtFormat.format(taxTable.getMincomeLow()));	
			
			if (taxTable.getMincomeHigh() == null)
			   taxTableForm.setIncomeHigh(null);
			else
			   taxTableForm.setIncomeHigh(amtFormat.format(taxTable.getMincomeHigh()));	
			
			if (taxTable.getMfixedTaxAmount() == null)
			   taxTableForm.setFixedTaxAmount(null);
			else
			   taxTableForm.setFixedTaxAmount(amtFormat.format(taxTable.getMfixedTaxAmount()));	
			
			if (taxTable.getMexemption() == null)
			   taxTableForm.setExemption(null);
			else
			   taxTableForm.setExemption(amtFormat.format(taxTable.getMexemption()));	
			
			if (taxTable.getMchildCredit() == null)
			   taxTableForm.setChildCredit(null);
			else
			   taxTableForm.setChildCredit(amtFormat.format(taxTable.getMchildCredit()));	
			
			if (taxTable.getDtaxRate() == null)
			   taxTableForm.setTaxRate(null);
			else
			   taxTableForm.setTaxRate(unitsFormat.format(taxTable.getDtaxRate()));	
			
			if (taxTable.getMnc529Deduction() == null)
			   taxTableForm.setNc529DeductionAmount(null);
			else
			   taxTableForm.setNc529DeductionAmount(amtFormat.format(taxTable.getMnc529Deduction()));	
			
			if (taxTable.getMsocialSecurityWageLimit() == null)
			   taxTableForm.setSocialSecurityWageLimit(null);
			else
			   taxTableForm.setSocialSecurityWageLimit(amtFormat.format(taxTable.getMsocialSecurityWageLimit()));	
									
			if (taxTable.getDfederalDaycareCreditRate() == null)
			   taxTableForm.setFederalDaycareCreditRate(null);
			else
			   taxTableForm.setFederalDaycareCreditRate(unitsFormat.format(taxTable.getDfederalDaycareCreditRate()));	
			
			if (taxTable.getDncDaycareCreditRate() == null)
			   taxTableForm.setNcDaycareCreditRate(null);
			else
			   taxTableForm.setNcDaycareCreditRate(unitsFormat.format(taxTable.getDncDaycareCreditRate()));	
			
			if (taxTable.getMfederalRecoveryAmount() == null)
			   taxTableForm.setFederalRecoveryAmount(null);
			else
			   taxTableForm.setFederalRecoveryAmount(amtFormat.format(taxTable.getMfederalRecoveryAmount()));	
									
			if (taxTable.getDncSurtaxRate() == null)
			   taxTableForm.setNcSurtaxRate(null);
			else
			   taxTableForm.setNcSurtaxRate(unitsFormat.format(taxTable.getDncSurtaxRate()));	
						
		}
		
		cache.setObject("ResponseObject", taxTable, req.getSession());
		req.getSession().setAttribute("taxTableShowParm",taxTableShowParm);
		req.getSession().setAttribute("tRate",tRate);
		req.getSession().setAttribute("tAmount",tAmount);
		req.getSession().setAttribute("tNC",tNC);
		req.getSession().setAttribute("tCT",tCT);
		req.getSession().setAttribute("tFederal",tFederal);
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxTableShowUpdateFormAction postprocessAction");
				
	}
						
}
