package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportFederalTaxesForm extends SlomFinBaseActionForm
{
	private String fedTaxRate;
	private String fedFixedTaxAmount;
	private String fedFixedIncomeAmount;
	private String taxCredits;
	private String agi;
	private String taxableIncome;
	private String dividendTaxRate;
	private String totalStateTaxesPaid;
	private String totalItemizedDeductions;
	private String prevStateRefund;
	
	private String reportTitle;
	
	private String totalGrossWages;
	private String totalTaxableWages;
	private String totalDividends;
	private String totalInterest;
	private String totalCapitalGains;
	private String totalDeductions;
	private String totalExemptions;
	private String totalFederalTaxesOwed;
	private String totalFederalWithholding;
	private String bottomLineString;
			
	public ReportFederalTaxesForm()
	{		
	}
		
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");		
		
		ActionErrors ae = new ActionErrors();
				
		return ae;

	}

		
	public String getReportTitle()
	{
		return reportTitle;
	}

	public void setReportTitle(String reportTitle)
	{
		this.reportTitle = reportTitle;
	}

	public String getBottomLineString()
	{
		return bottomLineString;
	}

	public void setBottomLineString(String bottomLineString)
	{
		this.bottomLineString = bottomLineString;
	}

	public String getTotalCapitalGains()
	{
		return totalCapitalGains;
	}

	public void setTotalCapitalGains(String totalCapitalGains)
	{
		this.totalCapitalGains = totalCapitalGains;
	}

	public String getTotalDeductions()
	{
		return totalDeductions;
	}

	public void setTotalDeductions(String totalDeductions)
	{
		this.totalDeductions = totalDeductions;
	}

	public String getTotalDividends()
	{
		return totalDividends;
	}

	public void setTotalDividends(String totalDividends)
	{
		this.totalDividends = totalDividends;
	}

	public String getTotalExemptions()
	{
		return totalExemptions;
	}

	public void setTotalExemptions(String totalExemptions)
	{
		this.totalExemptions = totalExemptions;
	}

	public String getTotalFederalTaxesOwed()
	{
		return totalFederalTaxesOwed;
	}

	public void setTotalFederalTaxesOwed(String totalFederalTaxesOwed)
	{
		this.totalFederalTaxesOwed = totalFederalTaxesOwed;
	}

	public String getTotalFederalWithholding()
	{
		return totalFederalWithholding;
	}

	public void setTotalFederalWithholding(String totalFederalWithholding)
	{
		this.totalFederalWithholding = totalFederalWithholding;
	}

	public String getTotalGrossWages()
	{
		return totalGrossWages;
	}

	public void setTotalGrossWages(String totalGrossWages)
	{
		this.totalGrossWages = totalGrossWages;
	}

	public String getTotalInterest()
	{
		return totalInterest;
	}

	public void setTotalInterest(String totalInterest)
	{
		this.totalInterest = totalInterest;
	}

	public void setAgi(String agi)
	{
		this.agi = agi;
	}

	public void setDividendTaxRate(String dividendTaxRate)
	{
		this.dividendTaxRate = dividendTaxRate;
	}

	public void setFedFixedIncomeAmount(String fedFixedIncomeAmount)
	{
		this.fedFixedIncomeAmount = fedFixedIncomeAmount;
	}

	public void setFedFixedTaxAmount(String fedFixedTaxAmount)
	{
		this.fedFixedTaxAmount = fedFixedTaxAmount;
	}

	public void setFedTaxRate(String fedTaxRate)
	{
		this.fedTaxRate = fedTaxRate;
	}

	public void setPrevStateRefund(String prevStateRefund)
	{
		this.prevStateRefund = prevStateRefund;
	}

	public void setTaxableIncome(String taxableIncome)
	{
		this.taxableIncome = taxableIncome;
	}

	public void setTaxCredits(String taxCredits)
	{
		this.taxCredits = taxCredits;
	}

	public void setTotalItemizedDeductions(String totalItemizedDeductions)
	{
		this.totalItemizedDeductions = totalItemizedDeductions;
	}

	public void setTotalStateTaxesPaid(String totalStateTaxesPaid)
	{
		this.totalStateTaxesPaid = totalStateTaxesPaid;
	}

	public String getAgi()
	{
		return agi;
	}

	public String getDividendTaxRate()
	{
		return dividendTaxRate;
	}

	public String getFedFixedIncomeAmount()
	{
		return fedFixedIncomeAmount;
	}

	public String getFedFixedTaxAmount()
	{
		return fedFixedTaxAmount;
	}

	public String getFedTaxRate()
	{
		return fedTaxRate;
	}

	public String getPrevStateRefund()
	{
		return prevStateRefund;
	}

	public String getTaxableIncome()
	{
		return taxableIncome;
	}

	public String getTaxCredits()
	{
		return taxCredits;
	}

	public String getTotalItemizedDeductions()
	{
		return totalItemizedDeductions;
	}

	public String getTotalStateTaxesPaid()
	{
		return totalStateTaxesPaid;
	}

	public String getTotalTaxableWages()
	{
		return totalTaxableWages;
	}

	public void setTotalTaxableWages(String totalTaxableWages)
	{
		this.totalTaxableWages = totalTaxableWages;
	}
	
}
