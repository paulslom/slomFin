package com.pas.slomfin.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.pas.slomfin.constants.ISlomFinAppConstants;


/** 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TaxTableUpdateForm extends SlomFinBaseActionForm
{
	public TaxTableUpdateForm()
	{		
	}
	
	private Integer taxFormulaID;
	private Integer taxYear;
	private Integer taxFormulaTypeID;
	private String taxFormulaTypeDesc;
	private String incomeLow;
	private String incomeHigh;
	private String taxRate;
	private String fixedTaxAmount;
	private String childCredit;
	private String standardDeduction;
	private String exemption;
	private String stateTaxCreditRate;
	private String statePropertyTaxLimit;
	private String ncExemptionThreshold;
	private String ncOverExemptionAmount;
	private String ncUnderExemptionAmount;
	private String ncChildCreditThreshold;
	private String socialSecurityWageLimit;
	private String nc529DeductionAmount;
	private String federalDaycareCreditRate;
	private String ncDaycareCreditRate;
	private String federalRecoveryAmount;
	private String ncSurtaxRate;
	
	public String getFederalRecoveryAmount()
	{
		return federalRecoveryAmount;
	}
	public void setFederalRecoveryAmount(String federalRecoveryAmount)
	{
		this.federalRecoveryAmount = federalRecoveryAmount;
	}
	public String getNcSurtaxRate()
	{
		return ncSurtaxRate;
	}
	public void setNcSurtaxRate(String ncSurtaxRate)
	{
		this.ncSurtaxRate = ncSurtaxRate;
	}
	public String getSocialSecurityWageLimit()
	{
		return socialSecurityWageLimit;
	}
	public void setSocialSecurityWageLimit(String socialSecurityWageLimit)
	{
		this.socialSecurityWageLimit = socialSecurityWageLimit;
	}
	public String getNc529DeductionAmount()
	{
		return nc529DeductionAmount;
	}
	public void setNc529DeductionAmount(String nc529DeductionAmount)
	{
		this.nc529DeductionAmount = nc529DeductionAmount;
	}
	public void initialize()
	{
		//initialize all variables
		
		String methodName = "initialize :: ";
		log.debug(methodName + " In");
		log.debug(methodName + " Out");
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{

		String methodName = "validate :: ";
		log.debug(methodName + " In");
		
		ActionErrors ae = new ActionErrors();

		String reqParm = request.getParameter("operation");
		
		//do not perform validation when cancelling or returning from an inquire or delete
		if (reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELADD)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELUPDATE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_CANCELDELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_DELETE)
		||  reqParm.equalsIgnoreCase(ISlomFinAppConstants.BUTTON_RETURN))
			return ae;
		
		ae = super.validate(mapping, request);		
		
		return ae;
		
	}
	public String getChildCredit()
	{
		return childCredit;
	}
	public void setChildCredit(String childCredit)
	{
		this.childCredit = childCredit;
	}
	public String getExemption()
	{
		return exemption;
	}
	public void setExemption(String exemption)
	{
		this.exemption = exemption;
	}
	public String getFixedTaxAmount()
	{
		return fixedTaxAmount;
	}
	public void setFixedTaxAmount(String fixedTaxAmount)
	{
		this.fixedTaxAmount = fixedTaxAmount;
	}
	public String getIncomeHigh()
	{
		return incomeHigh;
	}
	public void setIncomeHigh(String incomeHigh)
	{
		this.incomeHigh = incomeHigh;
	}
	public String getIncomeLow()
	{
		return incomeLow;
	}
	public void setIncomeLow(String incomeLow)
	{
		this.incomeLow = incomeLow;
	}
	public String getNcChildCreditThreshold()
	{
		return ncChildCreditThreshold;
	}
	public void setNcChildCreditThreshold(String ncChildCreditThreshold)
	{
		this.ncChildCreditThreshold = ncChildCreditThreshold;
	}
	public String getNcExemptionThreshold()
	{
		return ncExemptionThreshold;
	}
	public void setNcExemptionThreshold(String ncExemptionThreshold)
	{
		this.ncExemptionThreshold = ncExemptionThreshold;
	}
	public String getNcOverExemptionAmount()
	{
		return ncOverExemptionAmount;
	}
	public void setNcOverExemptionAmount(String ncOverExemptionAmount)
	{
		this.ncOverExemptionAmount = ncOverExemptionAmount;
	}
	public String getNcUnderExemptionAmount()
	{
		return ncUnderExemptionAmount;
	}
	public void setNcUnderExemptionAmount(String ncUnderExemptionAmount)
	{
		this.ncUnderExemptionAmount = ncUnderExemptionAmount;
	}
	public String getStandardDeduction()
	{
		return standardDeduction;
	}
	public void setStandardDeduction(String standardDeduction)
	{
		this.standardDeduction = standardDeduction;
	}
	public String getStatePropertyTaxLimit()
	{
		return statePropertyTaxLimit;
	}
	public void setStatePropertyTaxLimit(String statePropertyTaxLimit)
	{
		this.statePropertyTaxLimit = statePropertyTaxLimit;
	}
	public String getStateTaxCreditRate()
	{
		return stateTaxCreditRate;
	}
	public void setStateTaxCreditRate(String stateTaxCreditRate)
	{
		this.stateTaxCreditRate = stateTaxCreditRate;
	}
	public Integer getTaxFormulaID()
	{
		return taxFormulaID;
	}
	public void setTaxFormulaID(Integer taxFormulaID)
	{
		this.taxFormulaID = taxFormulaID;
	}
	public String getTaxFormulaTypeDesc()
	{
		return taxFormulaTypeDesc;
	}
	public void setTaxFormulaTypeDesc(String taxFormulaTypeDesc)
	{
		this.taxFormulaTypeDesc = taxFormulaTypeDesc;
	}
	public Integer getTaxFormulaTypeID()
	{
		return taxFormulaTypeID;
	}
	public void setTaxFormulaTypeID(Integer taxFormulaTypeID)
	{
		this.taxFormulaTypeID = taxFormulaTypeID;
	}
	public String getTaxRate()
	{
		return taxRate;
	}
	public void setTaxRate(String taxRate)
	{
		this.taxRate = taxRate;
	}
	public Integer getTaxYear()
	{
		return taxYear;
	}
	public void setTaxYear(Integer taxYear)
	{
		this.taxYear = taxYear;
	}
	public String getFederalDaycareCreditRate()
	{
		return federalDaycareCreditRate;
	}
	public void setFederalDaycareCreditRate(String federalDaycareCreditRate)
	{
		this.federalDaycareCreditRate = federalDaycareCreditRate;
	}
	public String getNcDaycareCreditRate()
	{
		return ncDaycareCreditRate;
	}
	public void setNcDaycareCreditRate(String ncDaycareCreditRate)
	{
		this.ncDaycareCreditRate = ncDaycareCreditRate;
	}
	
	
}
