package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.slomfin.actionform.ReportNCTaxesForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.TaxesReport;
import com.pas.slomfin.valueObject.TaxesSelection;

public class ReportNCTaxesAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportNCTaxesAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		TaxesSelection ncTaxesSelection = new TaxesSelection();
		Investor investor = cache.getInvestor(req.getSession());
		ncTaxesSelection.setTaxGroupID(new Integer(investor.getTaxGroupID()));
		ncTaxesSelection.setTaxYear(Integer.parseInt(req.getParameter("Year")));
		cache.setObject("RequestObject",ncTaxesSelection, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportNCTaxesAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<TaxesReport> ncTaxesList = (List<TaxesReport>)cache.getObject("ResponseObject",req.getSession());		
		
		ReportNCTaxesForm repForm = (ReportNCTaxesForm)form;
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		
		if (ncTaxesList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{
			List<TaxesReport> wagesList = new ArrayList<TaxesReport>();
			List<TaxesReport> dividendsList = new ArrayList<TaxesReport>();
			List<TaxesReport> interestList = new ArrayList<TaxesReport>();
			List<TaxesReport> capitalGainsList = new ArrayList<TaxesReport>();
			List<TaxesReport> creditsList = new ArrayList<TaxesReport>();
			
			BigDecimal decAdditionsPlusFederal = new BigDecimal("0.00");
			BigDecimal decTaxesOwedMinusCredits = new BigDecimal("0.00");
			BigDecimal decTotalNCWithholding = new BigDecimal("0.00");
			BigDecimal decBottomLine = new BigDecimal("0.00");
			BigDecimal decTotalPrevStateRefunds = new BigDecimal("0.00");
						
			decAdditionsPlusFederal = decAdditionsPlusFederal.setScale(4, java.math.RoundingMode.HALF_UP);			
			decTaxesOwedMinusCredits = decTaxesOwedMinusCredits.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalNCWithholding = decTotalNCWithholding.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalPrevStateRefunds = decTotalPrevStateRefunds.setScale(4, java.math.RoundingMode.HALF_UP); 
			decBottomLine = decBottomLine.setScale(4, java.math.RoundingMode.HALF_UP);
						
			String taxType = "";
			
			for (int i=0; i < ncTaxesList.size(); i++)
			{
				TaxesReport taxesReportDetail = (TaxesReport)ncTaxesList.get(i);
				
				taxType = taxesReportDetail.getType();
				
				if (taxType.equalsIgnoreCase("Variables"))
				{
					repForm.setTaxableIncome("Federal Taxable Income (Line 6 form NC D-400): $" + amtFormat.format(taxesReportDetail.getTaxableIncome()));
					repForm.setAllStatesWithholding("Total State Withholding - all states (Line 33 Form NC D-400): $)" + amtFormat.format(taxesReportDetail.getStateWithholding()));
					repForm.setPersonalExemptionAdjustment("Personal Exemption Adjustment (Line 34 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getNcExemptionAdjustment()));
					repForm.setAdditions("Additions to Fed Taxable Income (Line 7 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getAdditions()));
					decAdditionsPlusFederal = decAdditionsPlusFederal.add(taxesReportDetail.getAdditions().add(taxesReportDetail.getTaxableIncome()));
					repForm.setAdditionsPlusFederal("Line 8 Form NC D-400: $" + amtFormat.format(decAdditionsPlusFederal));
					repForm.setDeductions("Deductions (Line 9 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getSsWithholding()));
					repForm.setAdditionsPlusFederalMinusStateRefund ("Line 11 Form NC D-400: $" + amtFormat.format(taxesReportDetail.getNcTaxableIncome()));
					repForm.setResidencyRatio("Residency Ratio (Line 12 Form NC D-400) " + amtFormat.format(taxesReportDetail.getResidencyRatio()));
					repForm.setNcTaxableIncome("North Carolina Taxable Income (Line 13 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getNcTaxableIncome()));
					repForm.setNcTaxesOwed("North Carolina Taxes Owed (Line 14 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getStateTaxesOwed()));
					repForm.setNcSurtaxOwed("North Carolina Surtax Owed (Line 15 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getNcSurtaxOwed()));
					repForm.setNcTaxCredits("Tax Credits (Line 17 Form NC D-400): $" + amtFormat.format(taxesReportDetail.getNcTaxCredits()));					
					decTaxesOwedMinusCredits = decTaxesOwedMinusCredits.add(taxesReportDetail.getStateTaxesOwed().subtract(taxesReportDetail.getNcTaxCredits()));
					decTaxesOwedMinusCredits = decTaxesOwedMinusCredits.add(taxesReportDetail.getNcSurtaxOwed());
					repForm.setNcTaxesOwedMinusCredits("Total NC Taxes Owed (Line 18 Form NC D-400): $" + amtFormat.format(decTaxesOwedMinusCredits));
				}
				if (taxType.equalsIgnoreCase("Wages"))
				{
					wagesList.add(taxesReportDetail);
					decTotalNCWithholding = decTotalNCWithholding.add(taxesReportDetail.getStateWithholding());					
				}
				if (taxType.equalsIgnoreCase("Dividend"))
				{
					dividendsList.add(taxesReportDetail);					
				}
				if (taxType.equalsIgnoreCase("StateRefund"))
				{
					decTotalPrevStateRefunds = decTotalPrevStateRefunds.add(taxesReportDetail.getGrossAmount());
				}
				if (taxType.equalsIgnoreCase("Interest"))
				{
					interestList.add(taxesReportDetail);					
				}
				if (taxType.equalsIgnoreCase("Capital Gain"))
				{
					capitalGainsList.add(taxesReportDetail);					
				}
				if (taxType.equalsIgnoreCase("Credits"))
				{
					creditsList.add(taxesReportDetail);					
				}
								
			}
			
			repForm.setReportTitle(req.getParameter("Year") + " NC State Taxes for " + cache.getInvestor(req.getSession()).getTaxGroupName());
			decAdditionsPlusFederal = decAdditionsPlusFederal.subtract(decTotalPrevStateRefunds);
			repForm.setTotalStateWithholding("Total State Withholding: $" + amtFormat.format(decTotalNCWithholding));
			
			decBottomLine = decBottomLine.add(decTaxesOwedMinusCredits.subtract(decTotalNCWithholding));
				
			if (decBottomLine.compareTo(new BigDecimal("0.00")) > 0)
				repForm.setBottomLineString("You owe the State of North Carolina: $" + amtFormat.format(decBottomLine)); 
			else
			{
				decBottomLine = decBottomLine.multiply(new BigDecimal("-1.0"));
				repForm.setBottomLineString("Your State Refund from the State of North Carolina is: $" + amtFormat.format(decBottomLine)); 
			}
			
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTNCWAGESLIST, wagesList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTNCDIVIDENDSLIST, dividendsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTNCINTERESTLIST, interestList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTNCCAPITALGAINSLIST, capitalGainsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTNCCREDITSLIST, creditsList);	
				
		}			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportNCTaxesAction postprocessAction");
				
	}
}
