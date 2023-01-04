package com.pas.slomfin.action;

import java.math.BigDecimal;
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
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.TaxTableUpdateForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.dao.TaxTableDAO;

public class TaxTableUpdateAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside TaxTableUpdateAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		Tbltaxformulas taxTable = new Tbltaxformulas();
		
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
			String taxformulaID = req.getParameter("taxFormulaID"); //hidden field on jsp
			TaxTableDAO taxTableDAOReference;
			
			try
			{
				taxTableDAOReference = (TaxTableDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.TAXTABLE_DAO);
				List<Tbltaxformulas> txfList = taxTableDAOReference.inquire(new Integer(taxformulaID));
				taxTable = txfList.get(0);			
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
			TaxTableUpdateForm taxTableForm = (TaxTableUpdateForm)form;
		
			if (taxTableForm.getChildCredit() == null)
			   taxTable.setMchildCredit(null);
			else
			   taxTable.setMchildCredit(new BigDecimal(taxTableForm.getChildCredit()));	
			
			if (taxTableForm.getStateTaxCreditRate() == null)
			   taxTable.setDstateTaxCreditRate(null);
			else
			   taxTable.setDstateTaxCreditRate(new BigDecimal(taxTableForm.getStateTaxCreditRate()));	
						
			if (taxTableForm.getStatePropertyTaxLimit() == null)
			   taxTable.setMstatePropertyTaxLimit(null);
			else
			   taxTable.setMstatePropertyTaxLimit(new BigDecimal(taxTableForm.getStatePropertyTaxLimit()));	
			
			if (taxTableForm.getStandardDeduction() == null)
			   taxTable.setMstandardDeduction(null);
			else
			   taxTable.setMstandardDeduction(new BigDecimal(taxTableForm.getStandardDeduction()));	
			
			if (taxTableForm.getNcUnderExemptionAmount() == null)
			   taxTable.setMncunderExemptionAmount(null);
			else
			   taxTable.setMncunderExemptionAmount(new BigDecimal(taxTableForm.getNcUnderExemptionAmount()));	
			
			if (taxTableForm.getNcOverExemptionAmount() == null)
			   taxTable.setMncoverExemptionAmount(null);
			else
			   taxTable.setMncoverExemptionAmount(new BigDecimal(taxTableForm.getNcOverExemptionAmount()));	
			
			if (taxTableForm.getNcExemptionThreshold() == null)
			   taxTable.setMncexemptionThreshold(null);
			else
			   taxTable.setMncexemptionThreshold(new BigDecimal(taxTableForm.getNcExemptionThreshold()));	
			
			if (taxTableForm.getNcChildCreditThreshold() == null)
			   taxTable.setMncchildCreditThreshold(null);
			else
			   taxTable.setMncchildCreditThreshold(new BigDecimal(taxTableForm.getNcChildCreditThreshold()));	
			
			if (taxTableForm.getNc529DeductionAmount() == null)
			   taxTable.setMnc529Deduction(null);
			else
			   taxTable.setMnc529Deduction(new BigDecimal(taxTableForm.getNc529DeductionAmount()));	
			
			if (taxTableForm.getSocialSecurityWageLimit() == null)
			   taxTable.setMsocialSecurityWageLimit(null);
			else
			   taxTable.setMsocialSecurityWageLimit(new BigDecimal(taxTableForm.getSocialSecurityWageLimit()));	
						
			if (taxTableForm.getIncomeLow() == null)
			   taxTable.setMincomeLow(null);
			else
			   taxTable.setMincomeLow(new BigDecimal(taxTableForm.getIncomeLow()));	
			
			if (taxTableForm.getIncomeHigh() == null)
			   taxTable.setMincomeHigh(null);
			else
			   taxTable.setMincomeHigh(new BigDecimal(taxTableForm.getIncomeHigh()));	
			
			if (taxTableForm.getFixedTaxAmount() == null)
			   taxTable.setMfixedTaxAmount(null);
			else
			   taxTable.setMfixedTaxAmount(new BigDecimal(taxTableForm.getFixedTaxAmount()));	
			
			if (taxTableForm.getExemption() == null)
			   taxTable.setMexemption(null);
			else
			   taxTable.setMexemption(new BigDecimal(taxTableForm.getExemption()));	
			
			if (taxTableForm.getChildCredit() == null)
			   taxTable.setMchildCredit(null);
			else
			   taxTable.setMchildCredit(new BigDecimal(taxTableForm.getChildCredit()));	
			
			if (taxTableForm.getTaxRate() == null)
			   taxTable.setDtaxRate(null);
			else
			   taxTable.setDtaxRate(new BigDecimal(taxTableForm.getTaxRate()));	
			
			if (taxTableForm.getFederalDaycareCreditRate() == null)
			   taxTable.setDfederalDaycareCreditRate(null);
			else
			   taxTable.setDfederalDaycareCreditRate(new BigDecimal(taxTableForm.getFederalDaycareCreditRate()));	
			
			if (taxTableForm.getNcDaycareCreditRate() == null)
			   taxTable.setDncDaycareCreditRate(null);
			else
			   taxTable.setDncDaycareCreditRate(new BigDecimal(taxTableForm.getNcDaycareCreditRate()));	
			
			if (taxTableForm.getFederalRecoveryAmount() == null)
			   taxTable.setMfederalRecoveryAmount(null);
			else
			   taxTable.setMfederalRecoveryAmount(new BigDecimal(taxTableForm.getFederalRecoveryAmount()));	
			
			if (taxTableForm.getNcSurtaxRate() == null)
			   taxTable.setDncSurtaxRate(null);
			else
			   taxTable.setDncSurtaxRate(new BigDecimal(taxTableForm.getNcSurtaxRate()));
			
			cache.setGoToDBInd(req.getSession(), true);
						   									
		}
		
		cache.setObject("RequestObject", taxTable, req.getSession());	
		
		return true;
	}	
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside TaxTableUpdateAction postprocessAction");
		
		req.getSession().removeAttribute("taxTableShowParm");
		req.getSession().removeAttribute("tRate");
		req.getSession().removeAttribute("tCT");
		req.getSession().removeAttribute("tNC");
		req.getSession().removeAttribute("tFederal");
		req.getSession().removeAttribute("tAmount");
		req.getSession().removeAttribute("taxTableShowParm");
		
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
		
		log.debug("exiting TaxTableUpdateAction postprocessAction");
				
	}

}
