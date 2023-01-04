package com.pas.slomfin.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.pas.action.ActionComposite;
import com.pas.constants.IAppConstants;
import com.pas.exception.BusinessException;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.exception.PresentationException;
import com.pas.exception.SystemException;
import com.pas.slomfin.actionform.ReportFederalTaxesForm;
import com.pas.slomfin.cache.CacheManagerFactory;
import com.pas.slomfin.cache.ICacheManager;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.constants.ISlomFinMessageConstants;
import com.pas.slomfin.dao.FederalTaxesDAO;
import com.pas.slomfin.dao.SlomFinDAOFactory;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.TaxesReport;
import com.pas.slomfin.valueObject.TaxesSelection;
import com.pas.slomfin.valueObject.TaxesW2;

public class ReportFederalTaxesAction extends SlomFinStandardAction
{
	public boolean preprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation) throws PresentationException, BusinessException,
			DAOException, PASSystemException
	{
		log.debug("inside ReportFederalTaxesAction pre - process");
		
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		TaxesSelection federalTaxesSelection = new TaxesSelection();
		Investor investor = cache.getInvestor(req.getSession());
		federalTaxesSelection.setTaxGroupID(new Integer(investor.getTaxGroupID()));
		federalTaxesSelection.setTaxYear(Integer.parseInt(req.getParameter("Year")));
		cache.setObject("RequestObject",federalTaxesSelection, req.getSession());
		cache.setGoToDBInd(req.getSession(), true);
				
		return true;
	}	
	@SuppressWarnings("unchecked")
	public void postprocessAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res, int operation,
			ActionComposite ac) throws PresentationException,
			BusinessException, DAOException, PASSystemException
	{    	
		log.debug("inside ReportFederalTaxesAction postprocessAction");
	
		ICacheManager cache =  CacheManagerFactory.getCacheManager();
		List<TaxesReport> federalTaxesList = (List<TaxesReport>)cache.getObject("ResponseObject",req.getSession());		
		
		ReportFederalTaxesForm repForm = (ReportFederalTaxesForm)form;
		
		DecimalFormat amtFormat = new DecimalFormat("#####0.00");
		
		if (federalTaxesList.size() < 1)
		{
			ActionMessages am = new ActionMessages();
			am.add(ISlomFinMessageConstants.NO_RECORDS_FOUND,new ActionMessage(ISlomFinMessageConstants.NO_RECORDS_FOUND));
			ac.setMessages(am);
			ac.setActionForward(mapping.findForward(IAppConstants.AF_FAILURE));		
		}
		else
		{
			//W2 
			
			TaxesSelection federalTaxesSelection = new TaxesSelection();
			Investor investor = cache.getInvestor(req.getSession());
			federalTaxesSelection.setTaxGroupID(new Integer(investor.getTaxGroupID()));
			federalTaxesSelection.setTaxYear(Integer.parseInt(req.getParameter("Year")));
			
			List<TaxesW2> w2List = new ArrayList();
			
			try
			{				
				FederalTaxesDAO daoReference = (FederalTaxesDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.FEDERALTAXES_DAO);
				log.debug("retrieving W2 records for tax Group ID = " + federalTaxesSelection.getTaxGroupID());				
				w2List = daoReference.inquireW2(federalTaxesSelection);
			} 
			catch (SystemException e)
			{
				throw new PASSystemException(e);
			}
			
			//Lists such as wages, dividends, deductions, etc
			
			List<TaxesReport> wagesList = new ArrayList<TaxesReport>();
			List<TaxesReport> dividendsList = new ArrayList<TaxesReport>();
			List<TaxesReport> interestList = new ArrayList<TaxesReport>();
			List<TaxesReport> capitalGainsList = new ArrayList<TaxesReport>();
			List<TaxesReport> miscIncomeList = new ArrayList<TaxesReport>();
			List<TaxesReport> deductionsList = new ArrayList<TaxesReport>();
			List<TaxesReport> exemptionsList = new ArrayList<TaxesReport>();
			List<TaxesReport> creditsList = new ArrayList<TaxesReport>();
			
			BigDecimal decFedTaxRate = new BigDecimal("0.00");
			BigDecimal decTotalGrossWages = new BigDecimal("0.00");
			BigDecimal decTotalTaxableWages = new BigDecimal("0.00");
			BigDecimal decTotalDividends = new BigDecimal("0.00");
			BigDecimal decTotalInterest = new BigDecimal("0.00");
			BigDecimal decTotalCapitalGains = new BigDecimal("0.00");
			BigDecimal decTotalExemptions = new BigDecimal("0.00");
			BigDecimal decTotalFederalTaxesOwed = new BigDecimal("0.00");
			BigDecimal decTotalFederalWithholding = new BigDecimal("0.00");
			BigDecimal decBottomLine = new BigDecimal("0.00");
			
			Double decAGI = new Double("0.00");
			Double decDeductions = new Double("0.00");
			Double decTaxableIncome = new Double("0.00");
			Double decTaxCredits = new Double("0.00");
			
			BigDecimal oneHundred = new BigDecimal("100.00");
			
			decFedTaxRate = decFedTaxRate.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalGrossWages = decTotalGrossWages.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalTaxableWages = decTotalTaxableWages.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalDividends = decTotalDividends.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalInterest = decTotalInterest.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalCapitalGains = decTotalCapitalGains.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalExemptions = decTotalExemptions.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalFederalTaxesOwed = decTotalFederalTaxesOwed.setScale(4, java.math.RoundingMode.HALF_UP); 
			decTotalFederalWithholding = decTotalFederalWithholding.setScale(4, java.math.RoundingMode.HALF_UP); 
			decBottomLine = decBottomLine.setScale(4, java.math.RoundingMode.HALF_UP);
			
			oneHundred = oneHundred.setScale(4, java.math.RoundingMode.HALF_UP); 
			
			TaxesReport trVars = new TaxesReport();
			
			String taxType = "";
			
			for (int i=0; i < federalTaxesList.size(); i++)
			{
				TaxesReport taxesReportDetail = (TaxesReport)federalTaxesList.get(i);
				
				taxType = taxesReportDetail.getType();
				
				if (taxType.equalsIgnoreCase("Variables"))
				{
					trVars = taxesReportDetail;
					
					decAGI = Math.rint(taxesReportDetail.getAgi().doubleValue());
					decDeductions = Math.rint(taxesReportDetail.getTotalItemizedDeductions().doubleValue());
					decTaxableIncome = Math.rint(taxesReportDetail.getTaxableIncome().doubleValue());
					decTaxCredits = Math.rint(taxesReportDetail.getTaxCredits().doubleValue());
					
					repForm.setAgi("Adjusted Gross Income (Line 37 Form 1040): $" + amtFormat.format(decAGI));
					decFedTaxRate = decFedTaxRate.add(taxesReportDetail.getFedTaxRate());
					decFedTaxRate = decFedTaxRate.multiply(oneHundred);
					repForm.setFedTaxRate("Fed Tax Bracket: " + amtFormat.format(decFedTaxRate) + "%");
					decFedTaxRate = decFedTaxRate.divide(oneHundred);
					repForm.setPrevStateRefund("Prior Year State Refund (Line 10 Form 1040): $" + amtFormat.format(taxesReportDetail.getPrevStateRefund()));
					repForm.setTaxableIncome("Taxable Income (Line 43 form 1040): $" + amtFormat.format(decTaxableIncome));
					repForm.setTaxCredits("Federal Tax Credits (Line 56 Form 1040): $" + amtFormat.format(decTaxCredits));
					repForm.setTotalItemizedDeductions("Total Deductions (Line 40 Form 1040): $" + amtFormat.format(decDeductions));
				}
				if (taxType.equalsIgnoreCase("Wages"))
				{
					wagesList.add(taxesReportDetail);
					decTotalFederalWithholding = decTotalFederalWithholding.add(taxesReportDetail.getFederalWithholding());
					decTotalGrossWages = decTotalGrossWages.add(taxesReportDetail.getGrossAmount());
					decTotalTaxableWages = decTotalTaxableWages.add(taxesReportDetail.getGrossAmount());
					if (taxesReportDetail.getRetirementDeferred() != null)
					   decTotalTaxableWages = decTotalTaxableWages.subtract(taxesReportDetail.getRetirementDeferred());
					if (taxesReportDetail.getDental() != null)
					   decTotalTaxableWages = decTotalTaxableWages.subtract(taxesReportDetail.getDental());
					if (taxesReportDetail.getMedical() != null)
					   decTotalTaxableWages = decTotalTaxableWages.subtract(taxesReportDetail.getMedical());
					if (taxesReportDetail.getVision() != null)
					   decTotalTaxableWages = decTotalTaxableWages.subtract(taxesReportDetail.getVision());
					if (taxesReportDetail.getGroupLifeIncome() != null)
					   decTotalTaxableWages = decTotalTaxableWages.add(taxesReportDetail.getGroupLifeIncome());
				}
				if (taxType.equalsIgnoreCase("Dividend"))
				{
					dividendsList.add(taxesReportDetail);
					decTotalDividends = decTotalDividends.add(taxesReportDetail.getGrossAmount());
				}
				if (taxType.equalsIgnoreCase("Interest"))
				{
					interestList.add(taxesReportDetail);
					decTotalInterest = decTotalInterest.add(taxesReportDetail.getGrossAmount());
				}
				if (taxType.equalsIgnoreCase("Capital Gain"))
				{
					capitalGainsList.add(taxesReportDetail);
					decTotalCapitalGains = decTotalCapitalGains.add(taxesReportDetail.getGrossAmount());
				}
				if (taxType.equalsIgnoreCase("StateRefund")
				||  taxType.equalsIgnoreCase("Other Income"))
				{
					miscIncomeList.add(taxesReportDetail);
				}
				if (taxType.equalsIgnoreCase("Deduction"))
				{
					deductionsList.add(taxesReportDetail);
				}
				if (taxType.equalsIgnoreCase("Credits"))
				{
					creditsList.add(taxesReportDetail);
				}
				if (taxType.equalsIgnoreCase("Exemption"))
				{
					exemptionsList.add(taxesReportDetail);
					decTotalExemptions = decTotalExemptions.add(taxesReportDetail.getGrossAmount());
				}
				
			}
			
			if (decTotalCapitalGains.doubleValue() <= ISlomFinAppConstants.CAPITAL_LOSS_LIMIT) //only set this to the limit
			{
				decTotalCapitalGains = new BigDecimal(ISlomFinAppConstants.CAPITAL_LOSS_LIMIT * -1.0);
			}
			
			repForm.setReportTitle(req.getParameter("Year") + " Taxes for " + cache.getInvestor(req.getSession()).getTaxGroupName());
					 
			repForm.setTotalGrossWages("Total Gross Wages: $" + amtFormat.format(decTotalGrossWages));
			repForm.setTotalTaxableWages("Total Taxable Wages (Line 7 Form 1040): $" + amtFormat.format(decTotalTaxableWages));
			repForm.setTotalDividends("Total Dividends (Line 9a Form 1040): $" + amtFormat.format(decTotalDividends));
			repForm.setTotalInterest("Total Interest (Line 8a Form 1040): $" + amtFormat.format(decTotalInterest));
			repForm.setTotalCapitalGains("Total Capital Gain (Line 13 Form 1040):$" + amtFormat.format(decTotalCapitalGains));
			repForm.setTotalExemptions("Total Exemptions (Line 42 Form 1040): $" + amtFormat.format(decTotalExemptions));
			repForm.setTotalFederalWithholding("Total Federal Withholding (Line 71 Form 1040): $" + amtFormat.format(decTotalFederalWithholding));
			
			BigDecimal qualifiedDividends = new BigDecimal(0.0);
			if (trVars.getQualifiedDividends() != null)
				qualifiedDividends = qualifiedDividends.add(trVars.getQualifiedDividends());
			
			decTotalFederalTaxesOwed = decTotalFederalTaxesOwed.add(qualifiedDividends.multiply(trVars.getDividendTaxRate()));
			decTotalFederalTaxesOwed = decTotalFederalTaxesOwed.add(trVars.getFedFixedTaxAmount());
			decTotalFederalTaxesOwed = decTotalFederalTaxesOwed.add(decFedTaxRate.multiply(trVars.getTaxableIncome().subtract(qualifiedDividends).subtract(trVars.getFedFixedIncomeAmount())));
		
			repForm.setTotalFederalTaxesOwed("Federal Taxes Owed (Line 63 Form 1040): $" + amtFormat.format(decTotalFederalTaxesOwed));
			
			decBottomLine = decBottomLine.add(decTotalFederalWithholding.subtract(decTotalFederalTaxesOwed.subtract(trVars.getTaxCredits())));
				
			if (decBottomLine.compareTo(new BigDecimal("0.00")) < 0)
				repForm.setBottomLineString("(Line 75 Form 1040) You owe the IRS: $" + amtFormat.format(decBottomLine)); 
			else
				repForm.setBottomLineString("(Line 75 Form 1040) Your Federal Refund is: $" + amtFormat.format(decBottomLine)); 
						
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXWAGESLIST, wagesList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXDIVIDENDSLIST, dividendsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXINTERESTLIST, interestList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXCAPITALGAINSLIST, capitalGainsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXMISCINCOMELIST, miscIncomeList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXDEDUCTIONSLIST, deductionsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXCREDITSLIST, creditsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXEXEMPTIONSLIST, exemptionsList);	
			req.getSession().setAttribute(ISlomFinAppConstants.SESSION_REPORTFTXW2LIST, w2List);	
		}			
		ac.setActionForward(mapping.findForward(IAppConstants.AF_SUCCESS));		
				
		log.debug("exiting ReportFederalTaxesAction postprocessAction");
				
	}

}
