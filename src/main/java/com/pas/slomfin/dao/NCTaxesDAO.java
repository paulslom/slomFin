package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxformulas;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.TaxesReport;
import com.pas.slomfin.valueObject.TaxesSelection;
import com.pas.util.Utils;

/**
 * Title: 		FederalTaxesDAO
 * Project: 	Slomkowski Financial Application
 * Copyright: 	Copyright (c) 2007
 */
public class NCTaxesDAO extends BaseDBDAO
{
	private static final NCTaxesDAO currentInstance = new NCTaxesDAO();

	private static final Calendar calTaxDay = Calendar.getInstance();
	
    private NCTaxesDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return FederalTaxesDAO
     */
    public static NCTaxesDAO getDAOInstance()
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	@SuppressWarnings("unchecked")
	public List<TaxesReport> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		List<TaxesReport> ncTaxesList = new ArrayList<TaxesReport>();
						
		TaxesSelection ncTaxesSelection = (TaxesSelection)Info;
		ncTaxesSelection.setResidenceState("NC");
		Tbltaxgroupyear taxGY = new Tbltaxgroupyear();
		int numberOfExemptions = 0;
		
		calTaxDay.set(Calendar.YEAR, taxGY.getItaxYear());
		calTaxDay.set(Calendar.MONTH, 3); //month is indexed 0..11, so 3 is April
	    calTaxDay.set(Calendar.DAY_OF_MONTH, 15);
	    
		log.debug(methodName + "calling out to FederalTaxesDAO...");	
		
		FederalTaxesDAO federalTaxesDAOReference;
		
		try 
		{
			federalTaxesDAOReference = (FederalTaxesDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.FEDERALTAXES_DAO);
			ncTaxesList = federalTaxesDAOReference.inquire(ncTaxesSelection);
			taxGY = federalTaxesDAOReference.getTaxGroupYear(ncTaxesSelection);
			numberOfExemptions = federalTaxesDAOReference.getNumberOfExemptions(taxGY);
		}	
		catch (SystemException e)
		{
			throw new DAOException(e);
		}
		
		if (ncTaxesList.size() == 0)
		{
			throw new DAOException("The list returned from federalTaxesDAO inquire is zero..cannot continue");
		}
		
		log.debug("List returned from federalTaxesDAO inquire size is " + ncTaxesList.size());
		
		//first step is to remove the "Variables" entry as this is Federal and we need state vars
		
		int variablesIndex = -1;
		Double federalAGI = new Double(0.0);
		Double federalTaxableIncome = new Double(0.0);
		Double itemizedDeductions = new Double(0.0);
		Double wagesTotal = new Double(0.0);
		Double dividendsTotal = new Double(0.0);
		Double interestTotal = new Double(0.0);
		Double capitalGainsTotal = new Double(0.0);
		Double deductionsTotal = new Double(0.0);
		Double stateTaxesTotal = new Double(0.0);
		Double ncSurtax = new Double(0.0);
		
		for (int i = 0; i < ncTaxesList.size(); i++)
		{
			TaxesReport taxesReportDetail = ncTaxesList.get(i);
			String type = taxesReportDetail.getType();
			
			if (type.equalsIgnoreCase("Variables"))
			{	
			    variablesIndex = i;
			    federalAGI = Math.rint(taxesReportDetail.getAgi().doubleValue());
			    federalTaxableIncome = Math.rint(taxesReportDetail.getTaxableIncome().doubleValue());
			    itemizedDeductions = Math.rint(taxesReportDetail.getTotalItemizedDeductions().doubleValue());			    
			}
			else if (type.equalsIgnoreCase("Wages"))
			{	
				wagesTotal = wagesTotal + taxesReportDetail.getGrossAmount().doubleValue();
				stateTaxesTotal = stateTaxesTotal + taxesReportDetail.getStateWithholding().doubleValue();
			}
			else if (type.equalsIgnoreCase("Dividend"))
				dividendsTotal = dividendsTotal + taxesReportDetail.getGrossAmount().doubleValue();
			else if (type.equalsIgnoreCase("Interest"))
				interestTotal = interestTotal + taxesReportDetail.getGrossAmount().doubleValue();
			else if (type.equalsIgnoreCase("Capital Gain"))
				capitalGainsTotal = capitalGainsTotal + taxesReportDetail.getGrossAmount().doubleValue();
			else if (type.equalsIgnoreCase("StateRefund"))
				deductionsTotal = deductionsTotal + taxesReportDetail.getGrossAmount().doubleValue();
			
		}
		
		if (variablesIndex > -1)
			ncTaxesList.remove(variablesIndex);
		
		Double ncAGI = wagesTotal +	dividendsTotal + interestTotal + capitalGainsTotal - deductionsTotal;
		
		log.debug("NC AGI = " + ncAGI);
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select * from Tbltaxformulas");
		sbuf.append(" WHERE itaxYear = ");
		sbuf.append(ncTaxesSelection.getTaxYear());
		sbuf.append(" AND tbltaxformulatype.sformulaDescription = 'NC State Amount'");
		
		log.debug("about to run query: " + sbuf.toString());
		List<Tbltaxformulas> ncFormulasList = new ArrayList();		
		
		if (ncFormulasList.size() == 0)
		{
			throw new DAOException("No entry found for Tax formulas for this year..Check table tblTaxFormulas");
		}
		
		Tbltaxformulas taxFNC = ncFormulasList.get(0);
		
		//Exemptions
		
		Double ncExemptionAdjustment = new Double(0.0);
		if (taxFNC.getMncexemptionThreshold() != null)
		{	
		   if (federalAGI.compareTo(taxFNC.getMncexemptionThreshold().doubleValue()) > 0)
		   {	   
			   if (taxFNC.getMncoverExemptionAmount() != null)
			   {	   
			       ncExemptionAdjustment = taxFNC.getMncoverExemptionAmount().doubleValue() * numberOfExemptions;
			   }
		   }	   
		   else
			   if (taxFNC.getMncunderExemptionAmount() != null)
			   {	   
			       ncExemptionAdjustment = taxFNC.getMncunderExemptionAmount().doubleValue() * numberOfExemptions;
			   }
		}		   
		
		log.debug("NC exemption adjustment is = " + ncExemptionAdjustment);
		
		//Additions
		
		Double additions = new Double(0.0);
		Double ncStandardDeduction = new Double(0.0);
		
		if (taxFNC.getMstandardDeduction() != null)
			ncStandardDeduction = taxFNC.getMstandardDeduction().doubleValue();
		
		log.debug("NC Standard Deduction is = " + ncStandardDeduction);
		
		additions = (itemizedDeductions * -1); //line 29 form NC-d400
		additions = additions - ncStandardDeduction; //line 31 form NC-D400
		
		if (stateTaxesTotal.compareTo(additions) < 0)
		   additions = stateTaxesTotal; //line 33 form NC-D400

		additions = additions + ncExemptionAdjustment; //line 38 form NC-D400

		if (taxGY.getMotherIncome() != null)
		{
			additions = additions + Math.abs(taxGY.getMotherIncome().doubleValue());
		}
		
		log.debug("Additions = " + additions);
		
		Double nc529Deduction = new Double(0.0);
		
		if (taxFNC.getMnc529Deduction() != null)
			nc529Deduction = taxFNC.getMnc529Deduction().doubleValue();
		
		deductionsTotal = deductionsTotal + nc529Deduction;
		
		log.debug("Total Deductions = " + deductionsTotal);
		
		Double ncTaxableIncome = new Double(0.0);
		
		ncTaxableIncome = federalTaxableIncome + additions - deductionsTotal;

		log.debug("NC Taxable Income (Line 13 Form NC D-400) = " + ncTaxableIncome);
		
		Double stateTaxesOwed = new Double(0.0);
		
		sbuf.setLength(0); //clear the buffer for a new query
		sbuf.append("SELECT mfixedTaxAmount + ((");
		sbuf.append(ncTaxableIncome.toString());
		sbuf.append(" - mincomeLow - 1) * dtaxRate) AS StateTaxesOwed");
		sbuf.append(" FROM Tbltaxformulas");
		sbuf.append(" WHERE itaxYear = ");
		sbuf.append(ncTaxesSelection.getTaxYear());
		sbuf.append(" AND tbltaxformulatype.sformulaDescription = 'NC State Rate'");
		sbuf.append(" AND ");
		sbuf.append(ncTaxableIncome.toString());
		sbuf.append(" BETWEEN mincomeLow AND mincomeHigh");
		
		List taxRateList = new ArrayList();		
		
		if (taxRateList.size() == 0)
		{
			throw new DAOException("No entry found for Tax rates for this year..Check table tblTaxFormulas");
		}
		else
			if (taxRateList.get(0) != null)
			    stateTaxesOwed = (Double)taxRateList.get(0);
			
		log.debug("State Taxes Owed = " + stateTaxesOwed);
		
		stateTaxesOwed = Math.rint(stateTaxesOwed);
		
		log.debug("State Taxes Owed after rounding = " + stateTaxesOwed);
		
		ncSurtax = stateTaxesOwed * taxFNC.getDncSurtaxRate().doubleValue();
		ncSurtax = Math.rint(ncSurtax);
		
		log.debug("NC Surtax after rounding = " + ncSurtax);
		
		Double taxCredits = new Double(0.0);
		
		//first the child credit
		if (taxFNC.getMncchildCreditThreshold() != null)
		{	
		   if (federalAGI.compareTo(taxFNC.getMncchildCreditThreshold().doubleValue()) <= 0)
		   {
			   int dependents = 0;
			   if (taxGY.getIdependents() != null)
				   dependents = taxGY.getIdependents();
			   Double childCreditAmount = new Double(0.0);
			   if (taxFNC.getMchildCredit() != null)
				   childCreditAmount = taxFNC.getMchildCredit().doubleValue();
			   taxCredits = dependents * childCreditAmount; 
		   }
		}
		
		log.debug("NC child credit amount = " + taxCredits);
		
		TaxesReport taxesReportDetailncchildCredit = new TaxesReport();
		
		taxesReportDetailncchildCredit.setType("Credits");
		taxesReportDetailncchildCredit.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)
		taxesReportDetailncchildCredit.setDescription("NC Child Tax Credit");
		taxesReportDetailncchildCredit.setGrossAmount(new BigDecimal(taxCredits));
		ncTaxesList.add(taxesReportDetailncchildCredit);
		
		//next the daycare credit
		Double dayCareCredit = new Double(0.0);
			
		if (taxGY.getMdayCareExpensesPaid() != null)
			if (taxFNC.getDncDaycareCreditRate() != null)
			    dayCareCredit = dayCareCredit + (taxGY.getMdayCareExpensesPaid().doubleValue() * taxFNC.getDncDaycareCreditRate().doubleValue());
		
		dayCareCredit = Math.rint(dayCareCredit);
		
		log.debug("NC daycare credit amount = " + dayCareCredit);
		
		TaxesReport taxesReportDetailncdaycareCredit = new TaxesReport();
		
		taxesReportDetailncdaycareCredit.setType("Credits");
		taxesReportDetailncdaycareCredit.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)
		taxesReportDetailncdaycareCredit.setDescription("NC Daycare Credit");
		taxesReportDetailncdaycareCredit.setGrossAmount(new BigDecimal(dayCareCredit));
		ncTaxesList.add(taxesReportDetailncdaycareCredit);		
		
		taxCredits = taxCredits + dayCareCredit;
		
		log.debug("total NC tax credits = " + taxCredits);
		
		//next need to establish a variables row in the list for NC
								
		TaxesReport taxesReportDetail = new TaxesReport();
					
		taxesReportDetail.setType("Variables");
		taxesReportDetail.setSsWithholding(new BigDecimal(deductionsTotal));
		taxesReportDetail.setTaxableIncome(new BigDecimal(federalTaxableIncome));
		taxesReportDetail.setResidencyRatio(new BigDecimal(1.0)); //hard code this for now.
		taxesReportDetail.setNcStandardDeduction(new BigDecimal(ncStandardDeduction));
		taxesReportDetail.setNcExemptionAdjustment(new BigDecimal(ncExemptionAdjustment));
		taxesReportDetail.setAdditions(new BigDecimal(additions));		
		taxesReportDetail.setNcTaxableIncome(new BigDecimal(ncTaxableIncome));
		taxesReportDetail.setStateWithholding(new BigDecimal(stateTaxesTotal));		
		taxesReportDetail.setStateTaxesOwed(new BigDecimal(stateTaxesOwed));		
		taxesReportDetail.setNcTaxCredits(new BigDecimal(taxCredits));
		taxesReportDetail.setNcSurtaxOwed(new BigDecimal(ncSurtax));
		
		ncTaxesList.add(taxesReportDetail);
					
		log.debug("final list size is = " + ncTaxesList.size());
		log.debug(methodName + "out");
		return ncTaxesList;
				
	}
	
}
