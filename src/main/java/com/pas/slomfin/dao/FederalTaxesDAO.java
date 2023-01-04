package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblinvestment;
import com.pas.dbobjects.Tbljob;
import com.pas.dbobjects.Tblmortgage;
import com.pas.dbobjects.Tblpaydayhistory;
import com.pas.dbobjects.Tbltaxformulas;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.util.FinancialUtilities;
import com.pas.slomfin.valueObject.AccountMargin;
import com.pas.slomfin.valueObject.CapitalGainTransaction;
import com.pas.slomfin.valueObject.Job;
import com.pas.slomfin.valueObject.MortgageAmortization;
import com.pas.slomfin.valueObject.MortgageProjected;
import com.pas.slomfin.valueObject.TaxesReport;
import com.pas.slomfin.valueObject.TaxesSelection;
import com.pas.slomfin.valueObject.TaxesW2;
import com.pas.util.DateUtil;
import com.pas.util.MortgageAmortizer;
import com.pas.util.TaxesComparator;
import com.pas.util.Utils;

/**
 * Title: 		FederalTaxesDAO
 * Project: 	Slomkowski Financial Application
 * Copyright: 	Copyright (c) 2007
 */
public class FederalTaxesDAO extends BaseDBDAO
{
    private List<TaxesReport> federalTaxesList = new ArrayList<TaxesReport>();	
	
	private static final FederalTaxesDAO currentInstance = new FederalTaxesDAO();

	private static final Calendar calTaxDay = Calendar.getInstance();
	
    private FederalTaxesDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return FederalTaxesDAO
     */
    public static FederalTaxesDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	public List<TaxesReport> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		if (federalTaxesList.size() > 0) //if this list has anything in it from a prior run, clear it out.
		{
			federalTaxesList.clear();
		}
		
		TaxesSelection federalTaxesSelection = (TaxesSelection)Info;				
		
		//many of the methods need formulas and taxgroupyear items for this year, let's get them first.
						
		Tbltaxformulas taxF = getTaxFormulaForThisYear(federalTaxesSelection);
		Tbltaxgroupyear taxGY = getTaxGroupYear(federalTaxesSelection);
			
		calTaxDay.set(Calendar.YEAR, taxGY.getItaxYear());
		calTaxDay.set(Calendar.MONTH, 3); //month is indexed 0..11, so 3 is April
	    calTaxDay.set(Calendar.DAY_OF_MONTH, 15);
	
		List<TaxesReport> wagesList = fedTaxesWages(federalTaxesSelection);
		List<TaxesReport> exemptionsList = fedTaxesExemptions(federalTaxesSelection,taxF,taxGY);
		List<TaxesReport> dividendsList = fedTaxesDividends(federalTaxesSelection,taxGY);	
		List<TaxesReport> interestList = fedTaxesInterest(federalTaxesSelection);		 		
 		List<TaxesReport> stateRefundList = fedTaxesStateRefund(taxGY); 		
 		List<TaxesReport> iraDistributionList = fedTaxesIRADistribution(taxGY); 		
 		List<TaxesReport> otherIncomeList = fedTaxesOtherIncome(taxGY); 		
 		List<TaxesReport> deductionsList = fedTaxesDeductions(federalTaxesSelection,taxF,taxGY);
 		
 		CapitalGainsDAO capitalGainsDAO;
		try 
		{
			capitalGainsDAO = (CapitalGainsDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.CAPITAL_GAINS_DAO);
		} 
		catch (SystemException e)
		{
			log.error("error establishing capitalgainsdao", e);
			throw new DAOException(e);
		}
 		List<TaxesReport> capitalGainsList = capitalGainsDAO.fedTaxesCapitalGains(federalTaxesSelection,taxGY, true); //default to true on taxableInd for taxes
		
 		//federalTaxesList is the list we'll return.
 		log.debug(methodName + "combining all lists into one");
		
 		federalTaxesList.addAll(wagesList);
 		federalTaxesList.addAll(dividendsList);
 		federalTaxesList.addAll(interestList);
 		federalTaxesList.addAll(capitalGainsList);
 		federalTaxesList.addAll(stateRefundList);
 		federalTaxesList.addAll(iraDistributionList);
 		federalTaxesList.addAll(otherIncomeList);
 		federalTaxesList.addAll(deductionsList);
 		federalTaxesList.addAll(exemptionsList); 		
 		 		
		log.debug("have the full list except for taxparms, now need to sort it..");
		
		Collections.sort(federalTaxesList, new TaxesComparator());		
		
		boolean creditsSwitch = true;
		
		if (federalTaxesSelection.getResidenceState() != null 
		&&  federalTaxesSelection.getResidenceState().equalsIgnoreCase("NC"))
		{
			creditsSwitch = false;
		}
		
		List<TaxesReport> taxParmsList = fedTaxesParameters(taxF,taxGY,creditsSwitch);
		federalTaxesList.addAll(taxParmsList);
						
		log.debug("final list size is = " + federalTaxesList.size());
		log.debug(methodName + "out");
		return federalTaxesList;
				
	}
	
	@SuppressWarnings("unchecked")
	public Tbltaxgroupyear getTaxGroupYear(TaxesSelection federalTaxesSelection) throws DAOException
	{
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select * from Tbltaxgroupyear");
		sbuf.append(" where itaxYear = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" and tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		
		log.debug("about to run query: " + sbuf.toString());
				
		List<Tbltaxgroupyear> taxGYList = new ArrayList();		
		
		if (taxGYList.size() == 0)
		{
			throw new DAOException("No entry found for Tax group year for this year..Check table tblTaxGroupYear");
		}
		
		Tbltaxgroupyear taxGY = taxGYList.get(0);
		
		return taxGY;
	}
	
	@SuppressWarnings("unchecked")
	private Tbltaxformulas getTaxFormulaForThisYear(TaxesSelection federalTaxesSelection) throws DAOException
	{
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select * from Tbltaxformulas");
		sbuf.append(" WHERE tbltaxformulatype.sformulaDescription = 'Federal Amount'");
		sbuf.append("   AND itaxYear = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
			
		log.debug("about to run query: " + sbuf.toString());
				
		List<Tbltaxformulas> taxFormulasList = new ArrayList();		
		
		if (taxFormulasList.size() == 0)
		{
			throw new DAOException("No entry found for Tax formulas for this year..Check table tblTaxFormulas");
		}
		
		Tbltaxformulas taxF = taxFormulasList.get(0);
		
		return taxF;

	}

	@SuppressWarnings("unchecked")
	private List getDividendPositions(TaxesSelection federalTaxesSelection)
	{
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT trx.tblaccount.tblportfolio.tblinvestor.sfullName as InvestorName,");
		sbuf.append(" trx.tblinvestment.sdescription as InvDesc,");
		sbuf.append(" SUM(CASE trx.tbltransactiontype.bpositiveInd");
		sbuf.append(" WHEN 0 THEN -trx.decUnits");
		sbuf.append(" WHEN 1 THEN trx.decUnits");
		sbuf.append(" ELSE 0 END) * tblinvestment.ddividendRate as DividendAmount,");
		sbuf.append(" trx.tblinvestment.idividendsPerYear as DividendsPerYear");
		sbuf.append(" FROM Tbltransaction trx");
		sbuf.append(" WHERE trx.tblaccount.tblportfolio.btaxableInd = 1");
		sbuf.append(" AND trx.tblaccount.tblportfolio.tblinvestor.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" AND trx.tblinvestment.idividendsPerYear > 0");
		sbuf.append(" GROUP BY trx.tblaccount.tblportfolio.tblinvestor.sfullName,");
		sbuf.append(" trx.tblinvestment.sdescription,");	 
		sbuf.append(" trx.tblinvestment.ddividendRate,");	
		sbuf.append(" trx.tblinvestment.idividendsPerYear");	 
		sbuf.append(" HAVING SUM(CASE trx.tbltransactiontype.bpositiveInd");
		sbuf.append(" WHEN 0 THEN -trx.decUnits");
		sbuf.append(" WHEN 1 THEN trx.decUnits ELSE 0 END) <> 0");
							
		log.debug("about to run query: " + sbuf.toString());
				
		List interestList = new ArrayList();		
				
		return interestList;

	}
		
	@SuppressWarnings("unchecked")
	private List getInterestAccounts(TaxesSelection federalTaxesSelection)
	{
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT trx.tblaccount.tblportfolio.tblinvestor.sfullName as InvestorName,");
		sbuf.append(" trx.tblaccount.saccountName as AccountName,");   
		sbuf.append(" SUM(CASE trx.tbltransactiontype.sdescription");    
		sbuf.append(" WHEN 'Sell' THEN trx.mcostProceeds");	    
		sbuf.append(" WHEN 'Buy'  THEN -trx.mcostProceeds");	    
		sbuf.append(" ELSE (CASE trx.tbltransactiontype.bpositiveInd");	    
		sbuf.append(" WHEN 0 THEN -trx.mcostProceeds");	    
		sbuf.append(" WHEN 1 THEN trx.mcostProceeds");	    
		sbuf.append(" ELSE 0 END) END) as AccountBalance, ");	    
		sbuf.append(" trx.tblaccount.dinterestRate as InterestRate,");	
		sbuf.append(" trx.tblaccount.iinterestPaymentsPerYear as InterestPaymentsPerYear");   
		sbuf.append(" FROM Tbltransaction trx");
		sbuf.append(" WHERE trx.tblaccount.tblportfolio.btaxableInd = 1");
		sbuf.append(" AND trx.tblaccount.tblportfolio.tblinvestor.tbltaxgroup.itaxGroupId =");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" AND trx.tblaccount.iinterestPaymentsPerYear > 0");
		sbuf.append(" AND trx.tbltransactiontype.sdescription != 'Reinvest'");
		sbuf.append(" AND trx.tblaccount.bclosed = false");
		sbuf.append(" GROUP BY trx.tblaccount.tblportfolio.tblinvestor.sfullName,	");	
		sbuf.append(" trx.tblaccount.tblaccounttype.saccountType,");		
		sbuf.append(" trx.tblaccount.iinterestPaymentsPerYear,");	
		sbuf.append(" trx.tblaccount.dinterestRate,");	
		sbuf.append(" trx.tblaccount.mminInterestBalance");	
		sbuf.append(" HAVING SUM(CASE trx.tbltransactiontype.sdescription");
		sbuf.append(" WHEN 'Sell' THEN trx.mcostProceeds");		
		sbuf.append(" WHEN 'Buy'  THEN -trx.mcostProceeds");		
		sbuf.append(" ELSE (CASE trx.tbltransactiontype.bpositiveInd");		
		sbuf.append(" WHEN 0 THEN -trx.mcostProceeds");		
		sbuf.append(" WHEN 1 THEN trx.mcostProceeds");		
		sbuf.append(" ELSE 0 END) END) > trx.tblaccount.mminInterestBalance");		
					
		log.debug("about to run query: " + sbuf.toString());
				
		List interestList = new ArrayList();		
				
		return interestList;

	}
	
	@SuppressWarnings("unchecked")
	private List<TaxesReport> fedTaxCredits(Tbltaxformulas taxF, Tbltaxgroupyear taxGY, Double agi) throws DAOException
	{
		log.debug("entering fedTaxCredits...");		
		
		List<TaxesReport> tList = new ArrayList<TaxesReport>();		
		
		Double childCredit = new Double(0.0);
		
		if (agi.compareTo(ISlomFinAppConstants.CHILD_CREDIT_LOW_THRESHHOLD) < 0) //not up to threshold - get full credit
		{
			childCredit = taxGY.getIdependents().doubleValue() * taxF.getMchildCredit().doubleValue();
		}
		else //means we're somewhere over the low threshhold - calc amount to deny
		{
			log.debug("figuring out child credits..somewhere over low threshhold");
			
			long agiNextThouInt = Math.round(agi / 1000.00) + 1; 
		    log.debug("agiNextThouInt = " + agiNextThouInt);
			long agiNextThousand = agiNextThouInt * 1000;
			log.debug("agiNextThousand = " + agiNextThousand);
			Double amountOverLowThreshold = agiNextThousand - ISlomFinAppConstants.CHILD_CREDIT_LOW_THRESHHOLD;
			log.debug("amountOverLowThreshold = " + amountOverLowThreshold);
			Double childCreditDenial = amountOverLowThreshold * .05;
			log.debug("childCreditDenial = " + childCreditDenial);
			childCredit = taxGY.getIdependents().doubleValue() * taxF.getMchildCredit().doubleValue() - childCreditDenial;		 
		}
		
		if (childCredit < 0) //cannot be negative - make it zero if it is
		{
			childCredit = new Double(0.0);
		}
		
		log.debug("childCredit = " + childCredit);
		
		TaxesReport taxesReportDetail = new TaxesReport();		
		taxesReportDetail.setType("Credits");
		taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)
		taxesReportDetail.setDescription("Child Credit");
		taxesReportDetail.setGrossAmount(new BigDecimal(childCredit));
		tList.add(taxesReportDetail);
		
		Double dayCareCredit = new Double(0.0);
		Double foreignTaxPaid = new Double(0.0);
		Double makingWorkPay = new Double(0.0); //added in 2009 Obama stimulus
		
		if (taxGY.getMdividendsForeignTax() != null)
			foreignTaxPaid = foreignTaxPaid + taxGY.getMdividendsForeignTax().doubleValue();
		
		TaxesReport taxesReportDetailftp = new TaxesReport();
		
		taxesReportDetailftp.setType("Credits");
		taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)
		taxesReportDetailftp.setDescription("Foreign Tax Paid");
		taxesReportDetailftp.setGrossAmount(new BigDecimal(foreignTaxPaid));
		tList.add(taxesReportDetailftp);
		
		if (taxGY.getMdayCareExpensesPaid() != null)
			if (taxF.getDfederalDaycareCreditRate() != null)
			    dayCareCredit = dayCareCredit + (taxGY.getMdayCareExpensesPaid().doubleValue() * taxF.getDfederalDaycareCreditRate().doubleValue());
		
		TaxesReport taxesReportDetaildc = new TaxesReport();
		
		taxesReportDetaildc.setType("Credits");
		taxesReportDetaildc.setDescription("Daycare Credit");
		taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)
		taxesReportDetaildc.setGrossAmount(new BigDecimal(dayCareCredit));
		tList.add(taxesReportDetaildc);
		
		if (taxF.getMfederalRecoveryAmount() != null)
			makingWorkPay = makingWorkPay + taxF.getMfederalRecoveryAmount().doubleValue();
		
		TaxesReport taxesReportDetailmwp = new TaxesReport();
		
		taxesReportDetailmwp.setType("Credits");
		taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)
		taxesReportDetailmwp.setDescription("Making Work Pay Obama Credit");
		taxesReportDetailmwp.setGrossAmount(new BigDecimal(makingWorkPay));
		tList.add(taxesReportDetailmwp);
		
		return tList;
	}
	
	@SuppressWarnings("unchecked")
	private List<TaxesReport> fedTaxesParameters(Tbltaxformulas taxF, Tbltaxgroupyear taxGY, boolean creditsSwitch) throws DAOException
	{		
		
		log.debug("entering fedTaxParameters...");
		
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
		
		TaxesReport taxesReportDetail = new TaxesReport();
		
		taxesReportDetail.setType("Variables");
		
		taxesReportDetail.setDividendTaxRate(taxGY.getDdividendTaxRate());
		taxesReportDetail.setQualifiedDividends(taxGY.getMqualifiedDividends());
		
		Double taxableIncome = new Double(0.0);
		Double agi = new Double(0.0);
		Double prevStateRefund = new Double(0.0);
		Double stateTaxes = new Double(0.0);
		Double itemizedDeductions = new Double(0.0);
		Double capitalGains = new Double(0.0);
		
		log.debug("looping through federalTaxesList to determine parameters");
		log.debug("list size is = " + federalTaxesList.size());
		
		for (int i = 0; i < federalTaxesList.size(); i++)
		{
			TaxesReport ftr = federalTaxesList.get(i);
			
			log.debug("tax type is - " + ftr.getType());
			log.debug("Gross Amount is - " + ftr.getGrossAmount());
			
			if (ftr.getType().equalsIgnoreCase("Capital Gain"))
			{
				log.debug("Capital gain type, so ignoring setting taxable income and AGI for now");
			}
			else
			{	
				if (ftr.getGrossAmount() != null)
				    taxableIncome = taxableIncome + ftr.getGrossAmount().doubleValue();			
				if (ftr.getGroupLifeIncome() != null)
				    taxableIncome = taxableIncome + ftr.getGroupLifeIncome().doubleValue();
				if (ftr.getRetirementDeferred() != null)
				    taxableIncome = taxableIncome - ftr.getRetirementDeferred().doubleValue();
				if (ftr.getMedical() != null)
				    taxableIncome = taxableIncome - ftr.getMedical().doubleValue();
				if (ftr.getDental() != null)
				    taxableIncome = taxableIncome - ftr.getDental().doubleValue();
				if (ftr.getVision() != null)
				    taxableIncome = taxableIncome - ftr.getVision().doubleValue();
				
				log.debug("taxable income is now = " + taxableIncome);
				
				if (ftr.getType().equalsIgnoreCase("Deduction")
				||	ftr.getType().equalsIgnoreCase("Exemption"))
				{				
				}
				else
				{
					if (ftr.getGrossAmount() != null)
						agi = agi + ftr.getGrossAmount().doubleValue();
					if (ftr.getGroupLifeIncome() != null)
						agi = agi + ftr.getGroupLifeIncome().doubleValue();
					if (ftr.getRetirementDeferred() != null)
						agi = agi - ftr.getRetirementDeferred().doubleValue();
					if (ftr.getMedical() != null)
						agi = agi - ftr.getMedical().doubleValue();
					if (ftr.getDental() != null)
						agi = agi - ftr.getDental().doubleValue();
					if (ftr.getVision() != null)
					    agi = agi - ftr.getVision().doubleValue();
				}
				
				log.debug("AGI is now = " + agi);
				
			}	
			
			if (ftr.getType().equalsIgnoreCase("StateRefund"))
				if (ftr.getGrossAmount() != null)
			        prevStateRefund = prevStateRefund + ftr.getGrossAmount().doubleValue();
			
			if (ftr.getStateWithholding() != null)
				if (ftr.getStateWithholding() != null)
			        stateTaxes = stateTaxes + ftr.getStateWithholding().doubleValue();
			
			if (ftr.getType().equalsIgnoreCase("Deduction"))
				if (ftr.getGrossAmount() != null)
				    itemizedDeductions = itemizedDeductions + ftr.getGrossAmount().doubleValue();
			
			if (ftr.getType().equalsIgnoreCase("Capital Gain"))
				if (ftr.getGrossAmount() != null)
				    capitalGains = capitalGains + ftr.getGrossAmount().doubleValue();
		}
		
		//now we know the total capital gains...
		if (capitalGains.compareTo(ISlomFinAppConstants.CAPITAL_LOSS_LIMIT) <= 0) //only set this to the limit
		{
			taxableIncome = taxableIncome - ISlomFinAppConstants.CAPITAL_LOSS_LIMIT;
			agi = agi - ISlomFinAppConstants.CAPITAL_LOSS_LIMIT;
		}
		else
		{
			taxableIncome = taxableIncome + capitalGains;
			agi = agi + capitalGains;
		}
		
		taxesReportDetail.setTaxableIncome(new BigDecimal(taxableIncome));
		log.debug("Taxable Income = " + taxableIncome);
		
		taxesReportDetail.setAgi(new BigDecimal(agi));
		log.debug("AGI = " + agi);
			
		taxesReportDetail.setPrevStateRefund(new BigDecimal(prevStateRefund));
		log.debug("Prior year state refund = " + prevStateRefund);
		
		taxesReportDetail.setTotalItemizedDeductions(new BigDecimal(itemizedDeductions));
		log.debug("Itemized deductions = " + itemizedDeductions);
		
		taxesReportDetail.setTotalStateTaxesPaid(new BigDecimal(stateTaxes));
		log.debug("State taxes paid = " + stateTaxes);
		
		//Do not want to do this if a state return...this routine is called on state returns also
		if (creditsSwitch)
		{
			Double taxCredits = new Double(0.0);
		
		
			List taxCreditsList = fedTaxCredits(taxF, taxGY, agi);
			for (int i = 0; i < taxCreditsList.size(); i++)
			{
				TaxesReport trd = (TaxesReport)taxCreditsList.get(i);
				taxCredits = taxCredits + trd.getGrossAmount().doubleValue();
				tList.add(trd);
			}
			
			taxesReportDetail.setTaxCredits(new BigDecimal(taxCredits));
			log.debug("Tax Credits = " + taxCredits);
		}
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select * from Tbltaxformulas");
		sbuf.append(" WHERE tbltaxformulatype.sformulaDescription = 'Federal Rate'");
		sbuf.append("   AND itaxYear = ");
		sbuf.append(taxF.getItaxYear());
		sbuf.append(" AND ");
		sbuf.append(taxableIncome);
		sbuf.append(" BETWEEN mincomeLow AND mincomeHigh");
			
		log.debug("about to run query: " + sbuf.toString());
				
		List<Tbltaxformulas> taxRateList = new ArrayList();		
		
		if (taxRateList.size() == 0)
		{
			throw new DAOException("No entry found for Tax rates for this year..Check table tblTaxFormulas");
		}
		
		Tbltaxformulas taxRateRow = taxRateList.get(0);
		
		taxesReportDetail.setFedTaxRate(taxRateRow.getDtaxRate());
		log.debug("Fed Tax Rate = " + taxRateRow.getDtaxRate());
		
		taxesReportDetail.setFedFixedTaxAmount(taxRateRow.getMfixedTaxAmount());
		log.debug("Fed Fixed Tax Amount = " + taxRateRow.getMfixedTaxAmount());
		
		taxesReportDetail.setFedFixedIncomeAmount(taxRateRow.getMincomeLow());
		log.debug("Fed Fixed Income Amount = " + taxRateRow.getMincomeLow());
		
		tList.add(taxesReportDetail);
		
		log.debug("exiting fedTaxesParameters");
		
		return tList;
	}
	
	public int getNumberOfExemptions(Tbltaxgroupyear taxGY)
	{
		int numberOfExemptions = 0;
		
		String filingStatus = taxGY.getSfilingStatus();
		
		//Self and spouse
		if (filingStatus.equalsIgnoreCase("Married"))
			numberOfExemptions = 2;
		else
			numberOfExemptions = 1;
		
		//Next the kids
		numberOfExemptions = numberOfExemptions + taxGY.getIdependents();
		
		return numberOfExemptions;
	}
	
	private List<TaxesReport> fedTaxesExemptions(TaxesSelection federalTaxesSelection, 
			         Tbltaxformulas taxF, Tbltaxgroupyear taxGY)
	{
		log.debug("entering fedTaxesExemptions");
		
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
		
		Double exemptionAmount = new Double(taxF.getMexemption().doubleValue());
		
		int numberOfExemptions = getNumberOfExemptions(taxGY);
						
		Double totalExemptions = exemptionAmount * numberOfExemptions;
		String description = Integer.valueOf(numberOfExemptions).toString() + " @ " + exemptionAmount.toString() + " each";	
	    TaxesReport taxesReportDetail = new TaxesReport();
				    
		taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
		taxesReportDetail.setType("Exemption");
		taxesReportDetail.setDescription(description);
		taxesReportDetail.setSortOrder(2);	
		taxesReportDetail.setSubSortOrder(2);	
		taxesReportDetail.setGrossAmount(new BigDecimal(-totalExemptions));
			
		tList.add(taxesReportDetail);
		
		log.debug("total exemptions is = " + taxesReportDetail.getGrossAmount());
		log.debug("leaving fedTaxesExemptions");
		
		return tList;
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsStateTaxes(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsStateTaxes");
		
		Double returnField = new Double(0.0);
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT SUM(ph.mstateWithholding) as TotalStateTaxes");		
		sbuf.append(" FROM Tblpaydayhistory ph");
		sbuf.append(" WHERE ph.tbljob.tblinvestor.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" AND YEAR(dpaydayHistoryDate) = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		
		log.debug("about to run query: " + sbuf.toString());
				
		List stateTaxesList = new ArrayList();		

		if (stateTaxesList.size() > 0)
			if (stateTaxesList.get(0) != null)
			    returnField = ((BigDecimal)stateTaxesList.get(0)).doubleValue();
		
		log.debug("total state taxes is = " + returnField);
		
		log.debug("leavingdeductionsStateTaxes");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsPropertyTaxes(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsPropertyTaxes");
		
		Double returnField = new Double(0.0);
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT SUM(mtaxPaymentAmount) as taxPayments");	 
		sbuf.append(" FROM Tbltaxpayment tx");
		sbuf.append(" WHERE tx.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" AND tx.itaxYear = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" AND tx.tbltaxpaymenttype.staxPaymentTypeDesc = 'Property Tax Payment'");
		
		log.debug("about to run query: " + sbuf.toString());
		
		List propertyTaxesList = new ArrayList();		

		if (propertyTaxesList.size() > 0)
			if (propertyTaxesList.get(0) != null)
			    returnField = ((BigDecimal)propertyTaxesList.get(0)).doubleValue();				
		
		log.debug("total property tax deductions is = " + returnField);
		
		log.debug("leaving deductionsPropertyTaxes");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsCarTownTaxes(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsCarTownTaxes");
		
		Double returnField = new Double(0.0);
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT SUM(mtaxPaymentAmount) as taxPayments");	 
		sbuf.append(" FROM Tbltaxpayment tx");
		sbuf.append(" WHERE tx.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" AND tx.itaxYear = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" AND tx.tbltaxpaymenttype.staxPaymentTypeDesc = 'Car/Town Tax Payment'");
		
		log.debug("about to run query: " + sbuf.toString());
		
		List carTownTaxesList = new ArrayList();		

		if (carTownTaxesList.size() > 0)
			if (carTownTaxesList.get(0) != null)
			    returnField = ((BigDecimal)carTownTaxesList.get(0)).doubleValue();		
		
		log.debug("total car/town tax deductions is = " + returnField);
		
		log.debug("leaving deductionsCarTownTaxes");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsMarginInterest(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsMarginInterest");
		
		Double returnField = new Double(0.0);
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT SUM(TX.mcostProceeds) as MarginInterest");
		sbuf.append(" FROM Tbltransaction TX");  
		sbuf.append(" WHERE YEAR(TX.dtransactionDate) = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" AND TX.tbltransactiontype.sdescription = 'Margin Interest'");   
		sbuf.append(" AND TX.tblaccount.tblportfolio.tblinvestor.tbltaxgroup.itaxGroupId = "); 
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		
		log.debug("about to run query: " + sbuf.toString());
		
		List marginInterestList = new ArrayList();		

		if (marginInterestList.size() > 0)
			if (marginInterestList.get(0) != null)
			    returnField = ((BigDecimal)marginInterestList.get(0)).doubleValue();
		
		log.debug("total Margin interest deductions is = " + returnField);
		
		log.debug("leaving deductionsMarginInterest");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsMortgageInterest(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsMortgageInterest");
		
		Double returnField = new Double(0.0);
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT SUM(MH.minterestPaid) as MortgageInterest");
		sbuf.append(" FROM Tblmortgagehistory MH ");  
		sbuf.append(" WHERE YEAR(MH.dpaymentDate) = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" AND MH.tblmortgage.tblinvestor.tbltaxgroup.itaxGroupId = ");   
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		
		log.debug("about to run query: " + sbuf.toString());
		
		List mortgageInterestList = new ArrayList();		

		if (mortgageInterestList.size() > 0)
			if (mortgageInterestList.get(0) != null)
			    returnField = ((BigDecimal)mortgageInterestList.get(0)).doubleValue();
		
		log.debug("total Mortgage interest deduction is = " + returnField);
		
		log.debug("leaving deductionsMortgageInterest");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsStateTaxesProjected(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsStateTaxesProjected");
		
		StringBuffer sbuf = new StringBuffer();
		
		Double returnField = new Double(0.0);		
		
		Calendar now = Calendar.getInstance();
		
		if (now.get(Calendar.YEAR) > federalTaxesSelection.getTaxYear()) //no projection needed if past year
			return returnField;
	
		//first get a list of active jobs
		sbuf.append("select * from Tbljob where djobEndDate is null");
		sbuf.append(" and tblinvestor.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbljob> jobList = new ArrayList();		

		for (int i = 0; i < jobList.size(); i++)
		{
			Tbljob job = jobList.get(i);
			
			sbuf.setLength(0);
			
			sbuf.append("select count(*)");
			sbuf.append(" from Tblpaydayhistory ph");
			sbuf.append(" where year(ph.dpaydayHistoryDate) = ");
			sbuf.append(federalTaxesSelection.getTaxYear());
			sbuf.append(" and ph.tbljob.ijobId = ");
			sbuf.append(job.getIjobId());
			sbuf.append(" and ph.tblpaychecktype.spaycheckType = 'Regular'");
			
			log.debug("about to run query: " + sbuf.toString());
			
			List existingPaydaysCountList = new ArrayList();		

			long existingPaychecksCount = 0;
			
			if (existingPaydaysCountList.size() > 0)
				if (existingPaydaysCountList.get(0) != null)
					existingPaychecksCount = (Long)existingPaydaysCountList.get(0) + existingPaychecksCount;
			
			log.debug("Existing paychecks for job " + job.getSemployer() + " is = " + existingPaychecksCount);

			long remainingPaychecks = job.getIpaydaysPerYear() - existingPaychecksCount;
			
			returnField = returnField + (remainingPaychecks * job.getMstateWithholding().doubleValue());
		}
		
		log.debug("total state taxes projected is = " + returnField);
		
		log.debug("leaving deductionsStateTaxesProjected");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private Double deductionsMarginProjected(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsMarginProjected");
		
		Double returnField = new Double(0.0);		
		
		Calendar now = Calendar.getInstance();
		
		if (now.get(Calendar.YEAR) > federalTaxesSelection.getTaxYear()) //no projection needed if past year
			return returnField;
			
		AccountDAO acctDAOReference;
		
		try 
		{
			acctDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
			List<AccountMargin> marginList = acctDAOReference.getAccountsOnMargin(federalTaxesSelection.getTaxGroupID());
			
			if (marginList.size() > 0) 
			{
				Calendar calEndofYear = Calendar.getInstance();
					
				calEndofYear.set(Calendar.YEAR, federalTaxesSelection.getTaxYear());
				calEndofYear.set(Calendar.MONTH, 11); //December
				calEndofYear.set(Calendar.DAY_OF_MONTH, 31); //last day of year
				
				int remainingDays = 0;
				
				if (now.get(Calendar.YEAR) == federalTaxesSelection.getTaxYear())
				    remainingDays = DateUtil.getDifferenceInDays(new Date(calEndofYear.getTimeInMillis()), new Date());
				else //a future year
					remainingDays = 365;
				
				for (int i = 0; i < marginList.size(); i++)
				{
					AccountMargin acctMargin = marginList.get(i);
					
					Double acctBal = acctMargin.getAccountBalance().doubleValue();
					Double interestRate = acctMargin.getMarginInterestRate().doubleValue();
					Double dailyInterestRate = interestRate / 365;
					Double dailyRateTimesRemainingDays = dailyInterestRate * remainingDays;
					returnField = returnField + Math.abs((acctBal * dailyRateTimesRemainingDays));
				}
			}
			
		}
		catch (SystemException e)
		{
			throw new DAOException(e);
		}
		
		log.debug("total margin projected is = " + returnField);
		
		log.debug("leaving deductionsMarginProjected");
		
		return returnField;
		
	}
	
	@SuppressWarnings("unchecked")
	private MortgageProjected deductionsProjectedMortgage(TaxesSelection federalTaxesSelection) throws DAOException
	{
		log.debug("entering deductionsProjectedMortgage");
		
		MortgageProjected mortgageProjected = new MortgageProjected();
		
		Double mortgageInterestProjected = new Double(0.0);
		Double propertyTaxesProjected = new Double(0.0);
		
		mortgageProjected.setProjectedInterest(mortgageInterestProjected);
		mortgageProjected.setProjectedPropertyTaxes(propertyTaxesProjected);
		
		Calendar now = Calendar.getInstance();
		
		if (now.get(Calendar.YEAR) > federalTaxesSelection.getTaxYear()) //no projection needed if past year
			return mortgageProjected;
	
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select * from Tblmortgage where dmortgageEndDate is null");
		sbuf.append(" and tblinvestor.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblmortgage> mortgageList = new ArrayList();		

		for (int i = 0; i < mortgageList.size(); i++)
		{
			Tblmortgage mortgage = mortgageList.get(i);
			
			Integer mortID = mortgage.getImortgageId();
			
			List<MortgageAmortization> mortgagePaymentList = MortgageAmortizer.getFullMortgageAmortization(mortID);
			
			for (int j = 0; j < mortgagePaymentList.size(); j++)
			{
				MortgageAmortization mortAmort = mortgagePaymentList.get(j);
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(mortAmort.getMortgagePaymentDate());
				
				if (cal.get(Calendar.YEAR) > federalTaxesSelection.getTaxYear())
				   break;
				
				if (!mortAmort.isActualPayment())
				{
					mortgageInterestProjected = mortgageInterestProjected + mortAmort.getInterestPaid().doubleValue();
					propertyTaxesProjected = propertyTaxesProjected + mortgage.getMpropertyTaxes().doubleValue(); 
				}
			}
		}
				
		mortgageProjected.setProjectedInterest(mortgageInterestProjected);
		mortgageProjected.setProjectedPropertyTaxes(propertyTaxesProjected);
		
		log.debug("total mortgage Interest projected is = " + mortgageInterestProjected);
		log.debug("total property taxes projected is = " + propertyTaxesProjected);
		
		log.debug("leaving deductionsProjectedMortgage");
		
		return mortgageProjected;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<TaxesReport> fedTaxesDeductions(TaxesSelection federalTaxesSelection, Tbltaxformulas taxF, Tbltaxgroupyear taxGY) throws DAOException
	{
		log.debug("entering fedTaxesDeductions");
		
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
					
		//Calculate State Taxes already on the books
		Double stateTaxes = deductionsStateTaxes(federalTaxesSelection);		
		log.debug("State taxes - already on the books = " + stateTaxes);
		
		//Calculate Property Taxes already on the books
		Double propertyTaxes = deductionsPropertyTaxes(federalTaxesSelection);
		log.debug("Property taxes - already on the books = " + propertyTaxes);
		
		//Calculate Car/Town Taxes already on the books
		Double carTownTaxes = deductionsCarTownTaxes(federalTaxesSelection);
		log.debug("Car-town taxes - already on the books = " + carTownTaxes);
		
		//Calculate Margin interest already on the books
		Double marginInterest = deductionsMarginInterest(federalTaxesSelection);		
		log.debug("Margin Interest - already on the books = " + marginInterest);
		
		//Calculate Mortgage Interest already on the books
		Double mortgageInterest = deductionsMortgageInterest(federalTaxesSelection);
		log.debug("Mortgage Interest - already on the books = " + mortgageInterest);
				
		//Calculate Projected Mortgage Interest and property taxes
		MortgageProjected mp = deductionsProjectedMortgage(federalTaxesSelection);			
		log.debug("Projected Mortgage Interest = " + mp.getProjectedInterest());
		log.debug("Projected property taxes = " + mp.getProjectedPropertyTaxes());
		
		//Calculate Projected State Taxes		
		Double stateTaxesProjected = deductionsStateTaxesProjected(federalTaxesSelection);		
		log.debug("Projected State taxes = " + stateTaxesProjected);
		
		//Calculate Projected Margin Interest
		Double marginInterestProjected = deductionsMarginProjected(federalTaxesSelection);
		log.debug("Projected Margin Interest = " + marginInterestProjected);
		
		//next go get the Standard deduction, for comparison purposes later.
		Double standardDeduction = taxF.getMstandardDeduction().doubleValue();
		log.debug("Standard Deduction is = " + standardDeduction);
		
		//get other itemized things
		Double otherItemized = new Double(0.0);
		if (taxGY.getMotherItemized() != null)
			otherItemized = taxGY.getMotherItemized().doubleValue();
		
		String otherItemizedDesc = taxGY.getSotherItemizedDesc();
		
		//now we have all the info we need.
		
		Double totalItemizedDeductions = new Double(0.0);
		
		totalItemizedDeductions = totalItemizedDeductions + stateTaxes; 
		totalItemizedDeductions = totalItemizedDeductions + propertyTaxes;
		totalItemizedDeductions = totalItemizedDeductions + carTownTaxes;
		totalItemizedDeductions = totalItemizedDeductions + marginInterest;
		totalItemizedDeductions = totalItemizedDeductions + mortgageInterest;
		totalItemizedDeductions = totalItemizedDeductions + mp.getProjectedInterest();
		totalItemizedDeductions = totalItemizedDeductions + mp.getProjectedPropertyTaxes();
		totalItemizedDeductions = totalItemizedDeductions + stateTaxesProjected; 
		totalItemizedDeductions = totalItemizedDeductions + marginInterestProjected;
		totalItemizedDeductions = totalItemizedDeductions + otherItemized;
		
		log.debug("Total Itemized Deductions = " + totalItemizedDeductions);
		
		if (standardDeduction.compareTo(totalItemizedDeductions) >= 0)
		{
			TaxesReport taxesReportDetail = new TaxesReport();
			    
			taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
			taxesReportDetail.setType("Deduction");
			taxesReportDetail.setDescription("Standard Deduction");
			taxesReportDetail.setSortOrder(2);	
			taxesReportDetail.setSubSortOrder(1);	
			taxesReportDetail.setGrossAmount(new BigDecimal(standardDeduction));
					
			tList.add(taxesReportDetail);

		}
		else //need to itemize
		{
			TaxesReport taxesReportDetail = new TaxesReport();
			
			if (stateTaxes.compareTo(0.0) > 0)
			{
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized State Taxes");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-stateTaxes));
						
				tList.add(taxesReportDetail);
			}
			
			if (propertyTaxes.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Property Taxes");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-propertyTaxes));
						
				tList.add(taxesReportDetail);
			}
			
			if (carTownTaxes.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Car Town Taxes");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-carTownTaxes));
				
				tList.add(taxesReportDetail);
			}
			
			if (marginInterest.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Margin Interest");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-marginInterest));
						
				tList.add(taxesReportDetail);
			}
			
			if (mortgageInterest.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Mortgage Interest");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-mortgageInterest));
						
				tList.add(taxesReportDetail);
			}
			
			if (mp.getProjectedInterest().compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Mortgage Interest - Projected");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-mp.getProjectedInterest()));
						
				tList.add(taxesReportDetail);
			}
			
			if (mp.getProjectedPropertyTaxes().compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Property Taxes - Projected");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-mp.getProjectedPropertyTaxes()));
						
				tList.add(taxesReportDetail);
			}
			
			if (stateTaxesProjected.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized State Taxes - Projected");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-stateTaxesProjected));
						
				tList.add(taxesReportDetail);
			}
			
			if (marginInterestProjected.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription("Itemized Margin Interest - Projected");
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-marginInterestProjected));
						
				tList.add(taxesReportDetail);
			}
			
			if (otherItemized.compareTo(0.0) > 0)
			{
				taxesReportDetail = new TaxesReport();
			    
				taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
				taxesReportDetail.setType("Deduction");
				taxesReportDetail.setDescription(otherItemizedDesc);
				taxesReportDetail.setSortOrder(2);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(new BigDecimal(-otherItemized));
						
				tList.add(taxesReportDetail);
			}
		}
		
		return tList;
	}
	private List<TaxesReport> fedTaxesOtherIncome(Tbltaxgroupyear taxGY)
	{
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
		
		Double otherIncome = new Double(0.0);
		String otherIncomeDesc = "";
		
		if (taxGY.getMotherIncome() != null)
			otherIncome = taxGY.getMotherIncome().doubleValue();
		
		if (taxGY.getSotherIncomeDesc() != null)
			otherIncomeDesc = taxGY.getSotherIncomeDesc();
		
		if (otherIncome.compareTo(0.0) != 0) //if zero, ignore.
		{						  
		    TaxesReport taxesReportDetail = new TaxesReport();
			
			taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
			taxesReportDetail.setType("Other Income");
			taxesReportDetail.setDescription(otherIncomeDesc);
			taxesReportDetail.setSortOrder(1);	
			taxesReportDetail.setSubSortOrder(4);	
			taxesReportDetail.setGrossAmount(new BigDecimal(otherIncome));
									
			tList.add(taxesReportDetail);
		}
		
		return tList;
	}
	private List<TaxesReport> fedTaxesIRADistribution(Tbltaxgroupyear taxGY)
	{
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
		
		Double iraDistribution = new Double(0.0);
		if (taxGY.getMiradistribution() != null)
			iraDistribution = taxGY.getMiradistribution().doubleValue();
		
		if (iraDistribution.compareTo(0.0) > 0) //if greater than zero, record it; otherwise ignore.
		{	
			TaxesReport taxesReportDetail = new TaxesReport();
			
			taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
			taxesReportDetail.setType("Distribution");
			taxesReportDetail.setDescription("IRA Distribution");
			taxesReportDetail.setSortOrder(1);	
			taxesReportDetail.setSubSortOrder(4);	
			taxesReportDetail.setGrossAmount(new BigDecimal(iraDistribution));
									
			tList.add(taxesReportDetail);
		}
		
		return tList;
	}
	private List<TaxesReport> fedTaxesStateRefund(Tbltaxgroupyear taxGY)
	{
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
			
		Double prevStateRefund = taxGY.getMprevYearStateRefund().doubleValue();
				
		if (prevStateRefund.compareTo(0.0) > 0) //if greater than zero, record it; otherwise ignore.
		{						  
		    TaxesReport taxesReportDetail = new TaxesReport();
			
			taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
			taxesReportDetail.setType("StateRefund");
			taxesReportDetail.setDescription("Prior Year State Refund");
			taxesReportDetail.setSortOrder(1);	
			taxesReportDetail.setSubSortOrder(6);	
			taxesReportDetail.setGrossAmount(new BigDecimal(prevStateRefund));
									
			tList.add(taxesReportDetail);
		}
		
		return tList;
	}
		
	@SuppressWarnings("unchecked")
	private List<TaxesReport> fedTaxesInterest(TaxesSelection federalTaxesSelection)
	{
		StringBuffer sbuf = new StringBuffer();

		List<TaxesReport> tList = new ArrayList<TaxesReport>();
	
		//if today has a year > year requested, no need to project.
		//if today has a year = year requested, project remaining interest for this year.
		//if today has a year < year requested, then project interest only.
		
		Integer requestedYear = federalTaxesSelection.getTaxYear();		
		Calendar cal = Calendar.getInstance();
		Integer thisYear = cal.get(Calendar.YEAR);
		
		//Just assign all projected dividend dates to the last day of the year
		Calendar divCal = Calendar.getInstance();
		divCal.set(Calendar.YEAR, federalTaxesSelection.getTaxYear());
		divCal.set(Calendar.MONTH, 11);
		divCal.set(Calendar.DAY_OF_MONTH, 31);

		if (requestedYear.compareTo(thisYear) <= 0)
		{
			sbuf.append("select * from Tbltransaction");
			sbuf.append(" where year(dtransactionDate) = ");
			sbuf.append(federalTaxesSelection.getTaxYear());
			sbuf.append(" and tbltransactiontype.sdescription IN ('Interest Earned')");
			sbuf.append(" and tblaccount.tblportfolio.btaxableInd = true");
			sbuf.append(" and tblaccount.tblportfolio.tblinvestor.tbltaxgroup.itaxGroupId = ");
			sbuf.append(federalTaxesSelection.getTaxGroupID());
			sbuf.append(" order by tblaccount.tblportfolio.tblinvestor.iinvestorId,");
			sbuf.append(" tblinvestment.iinvestmentId");
			
			log.debug("about to run query: " + sbuf.toString());
			List<Tbltransaction> interestList = new ArrayList();		
			
			for (int i = 0; i < interestList.size(); i++)
			{
				Tbltransaction intTrx = interestList.get(i);
			
				TaxesReport taxesReportDetail = new TaxesReport();
				
				taxesReportDetail.setTaxDate(intTrx.getDtransactionDate());	
				taxesReportDetail.setInvestorName(intTrx.getTblaccount().getTblportfolio().getTblinvestor().getSfullName());
				taxesReportDetail.setType("Interest");
				taxesReportDetail.setDescription(intTrx.getTblaccount().getSaccountName());
				taxesReportDetail.setSortOrder(1);	
				taxesReportDetail.setSubSortOrder(3);	
				taxesReportDetail.setGrossAmount(intTrx.getMcostProceeds());
										
				tList.add(taxesReportDetail);
			
			}
			
			if (requestedYear.compareTo(thisYear) == 0) //project the remaining interest
			{
				List futureInterestList = getInterestAccounts(federalTaxesSelection);
				
				for (int i = 0; i < futureInterestList.size(); i++)
				{
					Object[] interestObject = (Object[])futureInterestList.get(i);
					
					String futureDescription = (String)interestObject[1];
					
					//need to know how many payments already on the books this year...					
					
					int existingPayments = 0;
					
					for (int j = 0; j < tList.size(); j++)
					{
						TaxesReport tListDetail = tList.get(j);
						if (futureDescription.equalsIgnoreCase(tListDetail.getDescription()))
						{
							existingPayments = existingPayments + 1;
						}
					}
					
					int remainingPayments = (Integer)interestObject[4] - existingPayments;
					
					TaxesReport taxesReportDetail = new TaxesReport();
					
					taxesReportDetail.setTaxDate(new Date(divCal.getTimeInMillis()));	
					taxesReportDetail.setInvestorName((String)interestObject[0]);
					taxesReportDetail.setType("Interest");
					taxesReportDetail.setDescription(futureDescription + " - Projected");
					taxesReportDetail.setSortOrder(1);	
					taxesReportDetail.setSubSortOrder(3);
					
					Double accountBalance = ((BigDecimal)interestObject[2]).doubleValue();
					Double interestPerYear = accountBalance * ((BigDecimal)interestObject[3]).doubleValue();
					Double interestPerMonth = interestPerYear / 12;
					Double totalInterest = remainingPayments * interestPerMonth;
					taxesReportDetail.setGrossAmount(new BigDecimal(totalInterest));
													
					tList.add(taxesReportDetail);
					
				}	
			}
			
		}
		else //for a future year
		{
			List futureInterestList = getInterestAccounts(federalTaxesSelection);
						
			for (int i = 0; i < futureInterestList.size(); i++)
			{
				Object[] interestObject = (Object[])futureInterestList.get(i);						
						
				TaxesReport taxesReportDetail = new TaxesReport();
									
				taxesReportDetail.setTaxDate(new Date(divCal.getTimeInMillis()));	
				taxesReportDetail.setInvestorName((String)interestObject[0]);
				taxesReportDetail.setType("Interest");
				taxesReportDetail.setDescription((String)interestObject[1] + " - Projected");
				taxesReportDetail.setSortOrder(1);	
				taxesReportDetail.setSubSortOrder(3);
				Double accountBalance = ((BigDecimal)interestObject[2]).doubleValue();
				Double totalInterest = accountBalance * ((BigDecimal)interestObject[3]).doubleValue();
				taxesReportDetail.setGrossAmount(new BigDecimal(totalInterest));
												
				tList.add(taxesReportDetail);
				
			}
		}
		
		return tList;
	}
	@SuppressWarnings("unchecked")
	private List<TaxesReport> fedTaxesDividends(TaxesSelection federalTaxesSelection, Tbltaxgroupyear taxGY) throws DAOException
	{
		StringBuffer sbuf = new StringBuffer();

		List<TaxesReport> tList = new ArrayList<TaxesReport>();
	
		//if today has a year > year requested, no need to project dividends.
		//if today has a year = year requested, project dividends based on dividends received already
		//if today has a year < year requested, then project based on what's owned now.
		
		Integer requestedYear = federalTaxesSelection.getTaxYear();		
		Calendar cal = Calendar.getInstance();
		Integer thisYear = cal.get(Calendar.YEAR);
		
		//Just assign all projected dividend dates to the last day of the year
		Calendar divCal = Calendar.getInstance();
		divCal.set(Calendar.YEAR, federalTaxesSelection.getTaxYear());
		divCal.set(Calendar.MONTH, 11);
		divCal.set(Calendar.DAY_OF_MONTH, 31);
			
		if (requestedYear.compareTo(thisYear) <= 0)
		{
			FinancialUtilities finUtil = new FinancialUtilities();
			String queryToRun = finUtil.dividendsQuery(requestedYear, true, federalTaxesSelection.getTaxGroupID());	
			log.debug("about to run query: " + queryToRun);
			
			List<Tbltransaction> dividendsList = new ArrayList();		
			
			for (int i = 0; i < dividendsList.size(); i++)
			{
				Tbltransaction divTrx = dividendsList.get(i);
			
				TaxesReport taxesReportDetail = new TaxesReport();
				
				taxesReportDetail.setTaxDate(divTrx.getDtransactionDate());	
				taxesReportDetail.setInvestorName(divTrx.getTblaccount().getTblportfolio().getTblinvestor().getSfullName());
				taxesReportDetail.setType("Dividend");
				
				if (divTrx.getTblinvestment().getSdescription().equalsIgnoreCase("Cash"))
					taxesReportDetail.setDescription(divTrx.getSdescription());
				else
					taxesReportDetail.setDescription(divTrx.getTblinvestment().getSdescription());
				
				taxesReportDetail.setSortOrder(1);	
				taxesReportDetail.setSubSortOrder(2);	
				taxesReportDetail.setGrossAmount(divTrx.getMcostProceeds());
										
				tList.add(taxesReportDetail);
			
			}
			
			if (requestedYear.compareTo(thisYear) == 0) //project the remaining dividends
			{
				int dividendCounter = 0;
				String lastInvestorName = "@#%";
				String lastDescription = "@$^^";
				Double lastAmount = new Double(0.0);
				
				for (int j = 0; j < tList.size(); j++)
				{
					TaxesReport trDivs = tList.get(j);
					
					String investorName = trDivs.getInvestorName();
					String dividendDescription = trDivs.getDescription();
					
					if (investorName.equalsIgnoreCase(lastInvestorName)
					&&  lastDescription.equalsIgnoreCase(dividendDescription))
					{
						//if they're both equal, just keep going
					}
					else
						if (j > 0) //time to record a projected dividend
						{
							InvestmentDAO investmentDAOReference;
							
							try
							{
								investmentDAOReference = (InvestmentDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.INVESTMENT_DAO);
								List<Tblinvestment> investmentList = investmentDAOReference.inquire(lastDescription);
							
								if (investmentList.size() > 0) //if you don't find it, don't project.
								{
									Tblinvestment invDiv = investmentList.get(0);
									
									int remainingDividends = invDiv.getIdividendsPerYear() - dividendCounter;
									
									if (remainingDividends > 0)
									{	
										TaxesReport taxesReportDetail = new TaxesReport();
										
										taxesReportDetail.setTaxDate(new Date(divCal.getTimeInMillis()));	
										taxesReportDetail.setInvestorName(lastInvestorName);
										taxesReportDetail.setType("Dividend");
										taxesReportDetail.setDescription(lastDescription + " - Projected");
										taxesReportDetail.setSortOrder(1);	
										taxesReportDetail.setSubSortOrder(2);
										
										Double totalDividendAmount = lastAmount * remainingDividends;
										taxesReportDetail.setGrossAmount(new BigDecimal(totalDividendAmount));
																												
										tList.add(taxesReportDetail);
									}
								}
									
							}
							catch (SystemException e1)
							{
								log.error("SystemException encountered: " + e1.getMessage());
								e1.printStackTrace();
								throw new DAOException(e1);
							}								
							
							dividendCounter = 0;
						}
					
					dividendCounter = dividendCounter + 1;
					lastInvestorName = investorName;
					lastDescription = dividendDescription;
					lastAmount = trDivs.getGrossAmount().doubleValue();
					
				}
			}
			
		}
		else //for a future year
		{
			
			List futureDividendsList = getDividendPositions(federalTaxesSelection);
										
			for (int i = 0; i < futureDividendsList.size(); i++)
			{
				Object[] dividendObject = (Object[])futureDividendsList.get(i);						
						
				TaxesReport taxesReportDetail = new TaxesReport();
									
				taxesReportDetail.setTaxDate(new Date(divCal.getTimeInMillis()));	
				taxesReportDetail.setInvestorName((String)dividendObject[0]);
				taxesReportDetail.setType("Dividend");
				taxesReportDetail.setDescription((String)dividendObject[1] + " - Projected");
				taxesReportDetail.setSortOrder(1);	
				taxesReportDetail.setSubSortOrder(2);
				Double totalDividendAmount = ((BigDecimal)dividendObject[2]).doubleValue();
				totalDividendAmount = totalDividendAmount * (Integer)dividendObject[3];
				taxesReportDetail.setGrossAmount(new BigDecimal(totalDividendAmount));
												
				tList.add(taxesReportDetail);
				
			}
		}
		
		if (taxGY.getMdividendsForeignTax() != null)
		{
			TaxesReport taxesReportDetail = new TaxesReport();
			
			taxesReportDetail.setTaxDate(new Date(divCal.getTimeInMillis()));	
			taxesReportDetail.setType("Dividend");
			taxesReportDetail.setDescription("Foreign Tax on Dividends");
			taxesReportDetail.setSortOrder(1);	
			taxesReportDetail.setSubSortOrder(2);
			taxesReportDetail.setGrossAmount(taxGY.getMdividendsForeignTax());
											
			tList.add(taxesReportDetail);
		}
		
		if (taxGY.getMdividendsReturnOfCapital() != null)
		{
			TaxesReport taxesReportDetail = new TaxesReport();
			
			taxesReportDetail.setTaxDate(new Date(divCal.getTimeInMillis()));	
			taxesReportDetail.setType("Dividend");
			taxesReportDetail.setDescription("Return Of Capital on Dividends");
			taxesReportDetail.setSortOrder(1);	
			taxesReportDetail.setSubSortOrder(2);
			taxesReportDetail.setGrossAmount(taxGY.getMdividendsReturnOfCapital().multiply(new BigDecimal(-1.0)));
			
			tList.add(taxesReportDetail);
		}
		
		return tList;
	}
	@SuppressWarnings("unchecked")
	private List<TaxesReport> fedTaxesWages(TaxesSelection federalTaxesSelection)
	{
		StringBuffer sbuf = new StringBuffer();

		List<TaxesReport> tList = new ArrayList<TaxesReport>();
	
		//if today has a year > year requested, no need to project paydays.
		//if today has a year = year requested, project paydays based on jobs with paychecks
		//if today has a year < year requested, then need to go get jobs with no end dates.
		
		Integer requestedYear = federalTaxesSelection.getTaxYear();		
		Calendar cal = Calendar.getInstance();
		Integer thisYear = cal.get(Calendar.YEAR);
		
		if (requestedYear.compareTo(thisYear) <= 0)
		{
			sbuf.append("select * from Tblpaydayhistory");
			sbuf.append(" where tbljob.tblinvestor.tbltaxgroup.itaxGroupId = ");
			sbuf.append(federalTaxesSelection.getTaxGroupID());
			sbuf.append(" and year(dpaydayHistoryDate) = "); 
			sbuf.append(federalTaxesSelection.getTaxYear());
			sbuf.append(" order by tbljob.ijobId, dpaydayHistoryDate");
			
			log.debug("about to run query: " + sbuf.toString());
			List<Tblpaydayhistory> wagesList = new ArrayList();		
	
			List<Job> jobsWithPaychecks = new ArrayList<Job>();
			
			int paydaysOnTheBooks = 0;
			
			Integer lastJobID = -1;
			int totalJobs = 0;
			Date lastDate = new Date();
			
			//First, do paydays that have already occurred for the year requested
			for (int i = 0; i < wagesList.size(); i++)
			{
				TaxesReport taxesReportDetail = new TaxesReport();
				
				Tblpaydayhistory ph = wagesList.get(i);
				
				if (lastJobID.compareTo(ph.getTbljob().getIjobId()) != 0) //different job now
				{
					Tbljob tempJob = ph.getTbljob();
					Job tempVOJob = new Job();
					
					tempVOJob.setIpaydaysPerYear(tempJob.getIpaydaysPerYear());
					tempVOJob.setInvestorName(tempJob.getTblinvestor().getSfullName());
					tempVOJob.setSemployer(tempJob.getSemployer());					
					tempVOJob.setMgrossPay(tempJob.getMgrossPay());
					tempVOJob.setMfederalWithholding(tempJob.getMfederalWithholding());
					tempVOJob.setMstateWithholding(tempJob.getMstateWithholding());			
					tempVOJob.setMretirementDeferred(tempJob.getMretirementDeferred());	
					tempVOJob.setMsswithholding(tempJob.getMsswithholding());	
					tempVOJob.setMmedicareWithholding(tempJob.getMmedicareWithholding());		
					tempVOJob.setMdental(tempJob.getMdental());
					tempVOJob.setMmedical(tempJob.getMmedical());				
					tempVOJob.setMgroupLifeIncome(tempJob.getMgroupLifeIncome());	
					tempVOJob.setMgroupLifeInsurance(tempJob.getMgroupLifeInsurance());
					
					jobsWithPaychecks.add(tempVOJob);
					totalJobs = totalJobs + 1;
					
					if (i > 0)
					{
						Job tempJob2 = jobsWithPaychecks.get(totalJobs - 2); //why -2? zero-based index, plus get the prior one 
						tempJob2.setIpaydaysPerYear(tempJob2.getIpaydaysPerYear() - paydaysOnTheBooks);
						tempJob2.setDjobStartDate(lastDate);
						jobsWithPaychecks.set(totalJobs - 2,tempJob2);
						paydaysOnTheBooks = 0;
					}
				}
				
				taxesReportDetail.setTaxDate(ph.getDpaydayHistoryDate());	
				taxesReportDetail.setInvestorName(ph.getTbljob().getTblinvestor().getSfullName());
				taxesReportDetail.setType("Wages");
				taxesReportDetail.setDescription(ph.getTbljob().getSemployer());
				taxesReportDetail.setSortOrder(1);	
				taxesReportDetail.setSubSortOrder(1);	
				taxesReportDetail.setGrossAmount(ph.getMgrossPay());
				taxesReportDetail.setFederalWithholding(ph.getMfederalWithholding());
				taxesReportDetail.setStateWithholding(ph.getMstateWithholding());			
				taxesReportDetail.setRetirementDeferred(ph.getMretirementDeferred());	
				taxesReportDetail.setSsWithholding(ph.getMsswithholding());	
				taxesReportDetail.setMedicareWithholding(ph.getMmedicareWithholding());		
				taxesReportDetail.setDental(ph.getMdental());
				taxesReportDetail.setMedical(ph.getMmedical());	
				taxesReportDetail.setVision(ph.getMvision());
				taxesReportDetail.setGroupLifeIncome(ph.getMgroupLifeIncome());	
				taxesReportDetail.setGroupLifeInsurance(ph.getMgroupLifeInsurance());
						
				tList.add(taxesReportDetail);		
				
				if (ph.getTblpaychecktype().getSpaycheckType().equalsIgnoreCase("Regular"))
					paydaysOnTheBooks = paydaysOnTheBooks + 1;
				
				lastJobID = ph.getTbljob().getIjobId();
				lastDate = ph.getDpaydayHistoryDate();
				
			}
			
			//take care of the last job
			Job tempJob2 = jobsWithPaychecks.get(totalJobs - 1); //only subtract one here to get the prior one
			tempJob2.setIpaydaysPerYear(tempJob2.getIpaydaysPerYear() - paydaysOnTheBooks);
			tempJob2.setDjobStartDate(lastDate);
			jobsWithPaychecks.set(totalJobs - 1,tempJob2);
			
			if (requestedYear.compareTo(thisYear) == 0) //project the remaining paydays
			{
				for (int i = 0; i < jobsWithPaychecks.size(); i++)
				{					
					Job job = jobsWithPaychecks.get(i);
					
					if (job.getIpaydaysPerYear() > 0) //we may be done already, test for that
					{	
						Calendar calEndofYear = Calendar.getInstance();
						calEndofYear.set(Calendar.YEAR, thisYear);
						calEndofYear.set(Calendar.MONTH, 11); //December
						calEndofYear.set(Calendar.DAY_OF_MONTH, 31); //last day of year
						
						int remainingDays = DateUtil.getDifferenceInDays(new Date(calEndofYear.getTimeInMillis()), job.getDjobStartDate());
						
						int gapDays = remainingDays / job.getIpaydaysPerYear();
						
						Calendar wageCal = Calendar.getInstance();
						Date wageDate = job.getDjobStartDate(); //we stored last paycheck date in the dJobStartDate field above
						
						//we stored "remaining paydays" in the iPaydaysPerYear field above
						for (int j = 0; j < job.getIpaydaysPerYear(); j++)
						{
							wageDate = DateUtil.addDaystoDate(wageDate, gapDays);
							
							TaxesReport taxesReportDetail = new TaxesReport();
							
							taxesReportDetail.setTaxDate(wageDate);	
							taxesReportDetail.setInvestorName(job.getInvestorName());
							taxesReportDetail.setType("Wages");
							taxesReportDetail.setDescription(job.getSemployer() + " - Projected");
							taxesReportDetail.setSortOrder(1);	
							taxesReportDetail.setSubSortOrder(1);	
							taxesReportDetail.setGrossAmount(job.getMgrossPay());
							taxesReportDetail.setFederalWithholding(job.getMfederalWithholding());
							taxesReportDetail.setStateWithholding(job.getMstateWithholding());			
							taxesReportDetail.setRetirementDeferred(job.getMretirementDeferred());	
							taxesReportDetail.setSsWithholding(job.getMsswithholding());	
							taxesReportDetail.setMedicareWithholding(job.getMmedicareWithholding());		
							taxesReportDetail.setDental(job.getMdental());
							taxesReportDetail.setMedical(job.getMmedical());
							taxesReportDetail.setVision(job.getMvision());
							taxesReportDetail.setGroupLifeIncome(job.getMgroupLifeIncome());	
							taxesReportDetail.setGroupLifeInsurance(job.getMgroupLifeInsurance());
									
							tList.add(taxesReportDetail);
							
							wageCal.add(Calendar.DAY_OF_MONTH, gapDays);
							
						}
						
					}	
				}
			}
						
		}
		else //means today has a year < year requested, so this is all projections.  Get jobs with no end dates.
		{
			sbuf.append("select * from Tbljob");
			sbuf.append(" where tblinvestor.tbltaxgroup.itaxGroupId = ");
			sbuf.append(federalTaxesSelection.getTaxGroupID());
			sbuf.append(" and djobEndDate IS NULL "); 
			
			log.debug("about to run query: " + sbuf.toString());
			
			List<Tbljob> jobsList = new ArrayList();		
						
			for (int i = 0; i < jobsList.size(); i++)
			{
				TaxesReport taxesReportDetail = new TaxesReport();
				
				Tbljob job = jobsList.get(i);
				
				Integer gapDays = 365 / job.getIpaydaysPerYear();
				
				Calendar wageCal = Calendar.getInstance();
				wageCal.set(Calendar.YEAR, federalTaxesSelection.getTaxYear());
				wageCal.set(Calendar.MONTH, 0);
				wageCal.set(Calendar.DAY_OF_MONTH, 1);
								
				for (int j = 0; j < job.getIpaydaysPerYear(); j++)
				{
					taxesReportDetail.setTaxDate(new Date(wageCal.getTimeInMillis()));	
					taxesReportDetail.setInvestorName(job.getTblinvestor().getSfullName());
					taxesReportDetail.setType("Wages");
					taxesReportDetail.setDescription(job.getSemployer() + " - Projected");
					taxesReportDetail.setSortOrder(1);	
					taxesReportDetail.setSubSortOrder(1);	
					taxesReportDetail.setGrossAmount(job.getMgrossPay());
					taxesReportDetail.setFederalWithholding(job.getMfederalWithholding());
					taxesReportDetail.setStateWithholding(job.getMstateWithholding());			
					taxesReportDetail.setRetirementDeferred(job.getMretirementDeferred());	
					taxesReportDetail.setSsWithholding(job.getMsswithholding());	
					taxesReportDetail.setMedicareWithholding(job.getMmedicareWithholding());		
					taxesReportDetail.setDental(job.getMdental());
					taxesReportDetail.setMedical(job.getMmedical());
					taxesReportDetail.setVision(job.getMvision());
					taxesReportDetail.setGroupLifeIncome(job.getMgroupLifeIncome());	
					taxesReportDetail.setGroupLifeInsurance(job.getMgroupLifeInsurance());
							
					tList.add(taxesReportDetail);
					
					wageCal.add(Calendar.DAY_OF_MONTH, gapDays);
					
				}	
			}
		}
		
		log.debug("final list size for wages is = " + tList.size());	
		return tList;
	}
	@SuppressWarnings("unchecked")
	public List<TaxesW2> inquireW2(Object Info) throws DAOException 
	{
		final String methodName = "inquireW2::";
		log.debug(methodName + "in");		
				
		List<TaxesW2> w2List = new ArrayList<TaxesW2>();
						
		TaxesSelection federalTaxesSelection = (TaxesSelection)Info;
		
		//First, determine the year's Social Security Wage Limit
		
		Tbltaxformulas taxF = getTaxFormulaForThisYear(federalTaxesSelection);
			
		Double socialSecurityWageLimit = new Double(taxF.getMsocialSecurityWageLimit().doubleValue());
				
		//Now that we have the ss Wage limit, create W2 query.
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("SELECT PH.tbljob.ijobId as JobID,");
		sbuf.append(" PH.tbljob.semployer as Employer, ");   
		sbuf.append(" sum(PH.mgrossPay)- sum(PH.mretirementDeferred) - sum(PH.mdental) - sum(PH.mmedical) - sum(PH.mvision) + sum(PH.mgroupLifeIncome) as Box1TaxableWgs,  ");   
		sbuf.append(" sum(PH.mfederalWithholding) as Box2FedWH,");   
		sbuf.append(" CASE WHEN sum(PH.mgrossPay) >= ");
		sbuf.append(socialSecurityWageLimit.toString());
		sbuf.append(" THEN ");
		sbuf.append(socialSecurityWageLimit.toString());
		sbuf.append(" ELSE sum(PH.mgrossPay) END as Box3SSWages,");	    
		sbuf.append(" sum(PH.msswithholding) as Box4SocSecWH,");   
		sbuf.append(" sum(PH.mgrossPay) - sum(PH.mdental) - sum(PH.mmedical) - sum(PH.mvision) + sum(PH.mgroupLifeIncome) as Box5TotalWgs,");   
		sbuf.append(" sum(PH.mmedicareWithholding) as Box6MedicareWH,");   
		sbuf.append(" sum(PH.mgroupLifeIncome) as Box12cLifeIns,");   
		sbuf.append(" sum(PH.mretirementDeferred) as Box12dRetDeferred,");   
		sbuf.append(" sum(PH.mgrossPay) - sum(PH.mretirementDeferred) - sum(PH.mdental) - sum(PH.mmedical) - sum(PH.mvision) + sum(PH.mgroupLifeIncome) as Box16StateWages,  ");   
		sbuf.append(" sum(PH.mstateWithholding) as Box17StateWH,");   
		sbuf.append(" PH.tbljob.semployerFedIdno as FedIDNo,");   
		sbuf.append(" PH.tbljob.semployerStateIdno as StateIDNo");   
		sbuf.append(" FROM Tblpaydayhistory PH");
		sbuf.append(" WHERE PH.tbljob.tblinvestor.tbltaxgroup.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" and year(dpaydayHistoryDate) = "); 
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" GROUP BY PH.tbljob.ijobId, PH.tbljob.semployer, PH.tbljob.semployerFedIdno, PH.tbljob.semployerStateIdno");
	 	
		log.debug("about to run query: " + sbuf.toString());
		
		List w2FromDBList = new ArrayList();		
	
		log.debug(methodName + "looping through recordset to build W2 object(s)");
				
		for (int i = 0; i < w2FromDBList.size(); i++)
		{
			Object[] w2Object = (Object[])w2FromDBList.get(i);
			
			TaxesW2 w2 = new TaxesW2();
										  
			w2.setEmployer((String)w2Object[1]);
			w2.setEmployerFedIDNo((String)w2Object[12]);
			w2.setEmployerStateIDNo((String)w2Object[13]);
			w2.setBox1TaxableWgs((BigDecimal)w2Object[2]);
			w2.setBox2FedWH((BigDecimal)w2Object[3]);
			w2.setBox3SSWages(new BigDecimal((Double)w2Object[4]));
			w2.setBox4SocSecWH((BigDecimal)w2Object[5]);
			w2.setBox5TotalWgs((BigDecimal)w2Object[6]);
			w2.setBox6MedicareWH((BigDecimal)w2Object[7]);
			w2.setBox12cLifeIns((BigDecimal)w2Object[8]);
			w2.setBox12dRetDeferred((BigDecimal)w2Object[9]);
			w2.setBox16StateWages((BigDecimal)w2Object[10]);
			w2.setBox17StateWH((BigDecimal)w2Object[11]);
												
			w2List.add(w2);
		}
		
		log.debug("final list size is = " + w2List.size());
		log.debug(methodName + "out");
		return w2List;	
				
	}
	
}
