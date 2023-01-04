package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportNCTaxesForm extends SlomFinBaseActionForm
{
	private String reportTitle;
	
	private String taxableIncome;
	private String allStatesWithholding;
	private String personalExemptionAdjustment;
	private String additions;
	private String additionsPlusFederal;
	private String deductions;
	private String additionsPlusFederalMinusStateRefund;
	private String residencyRatio;
	private String ncTaxableIncome;
	private String ncTaxesOwed;
	private String ncTaxCredits;
	private String ncSurtaxOwed;
	private String ncTaxesOwedMinusCredits;
	private String totalStateWithholding;
	
	private String bottomLineString;
	
	public ReportNCTaxesForm()
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

	public String getAdditions()
	{
		return additions;
	}

	public void setAdditions(String additions)
	{
		this.additions = additions;
	}

	public String getAdditionsPlusFederal()
	{
		return additionsPlusFederal;
	}

	public void setAdditionsPlusFederal(String additionsPlusFederal)
	{
		this.additionsPlusFederal = additionsPlusFederal;
	}

	public String getAdditionsPlusFederalMinusStateRefund()
	{
		return additionsPlusFederalMinusStateRefund;
	}

	public void setAdditionsPlusFederalMinusStateRefund(
			String additionsPlusFederalMinusStateRefund)
	{
		this.additionsPlusFederalMinusStateRefund = additionsPlusFederalMinusStateRefund;
	}

	public String getAllStatesWithholding()
	{
		return allStatesWithholding;
	}

	public void setAllStatesWithholding(String allStatesWithholding)
	{
		this.allStatesWithholding = allStatesWithholding;
	}

	public String getNcTaxableIncome()
	{
		return ncTaxableIncome;
	}

	public void setNcTaxableIncome(String ncTaxableIncome)
	{
		this.ncTaxableIncome = ncTaxableIncome;
	}

	public String getNcTaxCredits()
	{
		return ncTaxCredits;
	}

	public void setNcTaxCredits(String ncTaxCredits)
	{
		this.ncTaxCredits = ncTaxCredits;
	}

	public String getNcTaxesOwed()
	{
		return ncTaxesOwed;
	}

	public void setNcTaxesOwed(String ncTaxesOwed)
	{
		this.ncTaxesOwed = ncTaxesOwed;
	}

	public String getNcTaxesOwedMinusCredits()
	{
		return ncTaxesOwedMinusCredits;
	}

	public void setNcTaxesOwedMinusCredits(String ncTaxesOwedMinusCredits)
	{
		this.ncTaxesOwedMinusCredits = ncTaxesOwedMinusCredits;
	}

	public String getPersonalExemptionAdjustment()
	{
		return personalExemptionAdjustment;
	}

	public void setPersonalExemptionAdjustment(String personalExemptionAdjustment)
	{
		this.personalExemptionAdjustment = personalExemptionAdjustment;
	}

	public String getDeductions()
	{
		return deductions;
	}

	public void setDeductions(String deductions)
	{
		this.deductions = deductions;
	}

	public String getReportTitle()
	{
		return reportTitle;
	}

	public void setReportTitle(String reportTitle)
	{
		this.reportTitle = reportTitle;
	}

	public String getResidencyRatio()
	{
		return residencyRatio;
	}

	public void setResidencyRatio(String residencyRatio)
	{
		this.residencyRatio = residencyRatio;
	}

	public String getTaxableIncome()
	{
		return taxableIncome;
	}

	public void setTaxableIncome(String taxableIncome)
	{
		this.taxableIncome = taxableIncome;
	}

	public String getTotalStateWithholding()
	{
		return totalStateWithholding;
	}

	public void setTotalStateWithholding(String totalStateWithholding)
	{
		this.totalStateWithholding = totalStateWithholding;
	}

	public String getBottomLineString()
	{
		return bottomLineString;
	}

	public void setBottomLineString(String bottomLineString)
	{
		this.bottomLineString = bottomLineString;
	}

	public String getNcSurtaxOwed()
	{
		return ncSurtaxOwed;
	}

	public void setNcSurtaxOwed(String ncSurtaxOwed)
	{
		this.ncSurtaxOwed = ncSurtaxOwed;
	}
	
}
