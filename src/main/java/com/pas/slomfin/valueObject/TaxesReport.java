package com.pas.slomfin.valueObject;

import java.math.BigDecimal;
import java.util.Date;

import com.pas.valueObject.IValueObject;

public class TaxesReport  implements IValueObject
{
	public TaxesReport()
	{
				
	}
	private Date taxDate;
	private String investorName;
	private String type;
	private String description;
	private Integer sortOrder;
	private Integer subSortOrder;
	private BigDecimal grossAmount;
	private BigDecimal federalWithholding;
	private BigDecimal stateWithholding;
	private BigDecimal retirementDeferred;
	private BigDecimal ssWithholding;
	private BigDecimal medicareWithholding;
	private BigDecimal dental;
	private BigDecimal medical;
	private BigDecimal vision;
	private BigDecimal groupLifeInsurance;
	private BigDecimal groupLifeIncome;
	
	//Federal items
	private BigDecimal fedTaxRate;
	private BigDecimal fedFixedTaxAmount;
	private BigDecimal fedFixedIncomeAmount;
	private BigDecimal taxCredits;
	private BigDecimal agi;
	private BigDecimal taxableIncome;
	private BigDecimal dividendTaxRate;
	private BigDecimal totalStateTaxesPaid;
	private BigDecimal totalItemizedDeductions;
	private BigDecimal prevStateRefund;
	private BigDecimal qualifiedDividends;
	private BigDecimal dividendsForeignTaxPaid;
	private BigDecimal dividendsReturnOfCapital;
	
	//State items
	private BigDecimal additions;
	private BigDecimal residencyRatio;
	private BigDecimal ncTaxCredits;
	private BigDecimal ncStandardDeduction;
	private BigDecimal ncExemptionAdjustment;
	private BigDecimal stateTaxesOwed;
	private BigDecimal ncTaxableIncome;
	private BigDecimal ncSurtaxOwed;
				
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("type = " + type + "\n");
		buf.append("description = " + description + "\n");
								
		return buf.toString();
	}

	public BigDecimal getDental()
	{
		return dental;
	}

	public void setDental(BigDecimal dental)
	{
		this.dental = dental;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public BigDecimal getFederalWithholding()
	{
		return federalWithholding;
	}

	public void setFederalWithholding(BigDecimal federalWithholding)
	{
		this.federalWithholding = federalWithholding;
	}

	public BigDecimal getGrossAmount()
	{
		return grossAmount;
	}

	public void setGrossAmount(BigDecimal grossAmount)
	{
		this.grossAmount = grossAmount;
	}

	public BigDecimal getGroupLifeIncome()
	{
		return groupLifeIncome;
	}

	public void setGroupLifeIncome(BigDecimal groupLifeIncome)
	{
		this.groupLifeIncome = groupLifeIncome;
	}

	public BigDecimal getGroupLifeInsurance()
	{
		return groupLifeInsurance;
	}

	public void setGroupLifeInsurance(BigDecimal groupLifeInsurance)
	{
		this.groupLifeInsurance = groupLifeInsurance;
	}

	public BigDecimal getMedical()
	{
		return medical;
	}

	public void setMedical(BigDecimal medical)
	{
		this.medical = medical;
	}

	public BigDecimal getMedicareWithholding()
	{
		return medicareWithholding;
	}

	public void setMedicareWithholding(BigDecimal medicareWithholding)
	{
		this.medicareWithholding = medicareWithholding;
	}

	public BigDecimal getRetirementDeferred()
	{
		return retirementDeferred;
	}

	public void setRetirementDeferred(BigDecimal retirementDeferred)
	{
		this.retirementDeferred = retirementDeferred;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public BigDecimal getSsWithholding()
	{
		return ssWithholding;
	}

	public void setSsWithholding(BigDecimal ssWithholding)
	{
		this.ssWithholding = ssWithholding;
	}

	public BigDecimal getStateWithholding()
	{
		return stateWithholding;
	}

	public void setStateWithholding(BigDecimal stateWithholding)
	{
		this.stateWithholding = stateWithholding;
	}

	public Integer getSubSortOrder()
	{
		return subSortOrder;
	}

	public void setSubSortOrder(Integer subSortOrder)
	{
		this.subSortOrder = subSortOrder;
	}

	public Date getTaxDate()
	{
		return taxDate;
	}

	public void setTaxDate(Date taxDate)
	{
		this.taxDate = taxDate;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public BigDecimal getAgi()
	{
		return agi;
	}

	public void setAgi(BigDecimal agi)
	{
		this.agi = agi;
	}

	public BigDecimal getDividendTaxRate()
	{
		return dividendTaxRate;
	}

	public void setDividendTaxRate(BigDecimal dividendTaxRate)
	{
		this.dividendTaxRate = dividendTaxRate;
	}

	public BigDecimal getFedFixedIncomeAmount()
	{
		return fedFixedIncomeAmount;
	}

	public void setFedFixedIncomeAmount(BigDecimal fedFixedIncomeAmount)
	{
		this.fedFixedIncomeAmount = fedFixedIncomeAmount;
	}

	public BigDecimal getFedFixedTaxAmount()
	{
		return fedFixedTaxAmount;
	}

	public void setFedFixedTaxAmount(BigDecimal fedFixedTaxAmount)
	{
		this.fedFixedTaxAmount = fedFixedTaxAmount;
	}

	public BigDecimal getFedTaxRate()
	{
		return fedTaxRate;
	}

	public void setFedTaxRate(BigDecimal fedTaxRate)
	{
		this.fedTaxRate = fedTaxRate;
	}

	public BigDecimal getPrevStateRefund()
	{
		return prevStateRefund;
	}

	public void setPrevStateRefund(BigDecimal prevStateRefund)
	{
		this.prevStateRefund = prevStateRefund;
	}

	public BigDecimal getTaxableIncome()
	{
		return taxableIncome;
	}

	public void setTaxableIncome(BigDecimal taxableIncome)
	{
		this.taxableIncome = taxableIncome;
	}

	public BigDecimal getTaxCredits()
	{
		return taxCredits;
	}

	public void setTaxCredits(BigDecimal taxCredits)
	{
		this.taxCredits = taxCredits;
	}

	public BigDecimal getTotalItemizedDeductions()
	{
		return totalItemizedDeductions;
	}

	public void setTotalItemizedDeductions(BigDecimal totalItemizedDeductions)
	{
		this.totalItemizedDeductions = totalItemizedDeductions;
	}

	public BigDecimal getTotalStateTaxesPaid()
	{
		return totalStateTaxesPaid;
	}

	public void setTotalStateTaxesPaid(BigDecimal totalStateTaxesPaid)
	{
		this.totalStateTaxesPaid = totalStateTaxesPaid;
	}

	public String getInvestorName()
	{
		return investorName;
	}

	public void setInvestorName(String investorName)
	{
		this.investorName = investorName;
	}

	public BigDecimal getAdditions()
	{
		return additions;
	}

	public void setAdditions(BigDecimal additions)
	{
		this.additions = additions;
	}

	public BigDecimal getNcExemptionAdjustment()
	{
		return ncExemptionAdjustment;
	}

	public void setNcExemptionAdjustment(BigDecimal ncExemptionAdjustment)
	{
		this.ncExemptionAdjustment = ncExemptionAdjustment;
	}

	public BigDecimal getNcStandardDeduction()
	{
		return ncStandardDeduction;
	}

	public void setNcStandardDeduction(BigDecimal ncStandardDeduction)
	{
		this.ncStandardDeduction = ncStandardDeduction;
	}

	public BigDecimal getNcTaxableIncome()
	{
		return ncTaxableIncome;
	}

	public void setNcTaxableIncome(BigDecimal ncTaxableIncome)
	{
		this.ncTaxableIncome = ncTaxableIncome;
	}

	public BigDecimal getNcTaxCredits()
	{
		return ncTaxCredits;
	}

	public void setNcTaxCredits(BigDecimal ncTaxCredits)
	{
		this.ncTaxCredits = ncTaxCredits;
	}

	public BigDecimal getResidencyRatio()
	{
		return residencyRatio;
	}

	public void setResidencyRatio(BigDecimal residencyRatio)
	{
		this.residencyRatio = residencyRatio;
	}

	public BigDecimal getStateTaxesOwed()
	{
		return stateTaxesOwed;
	}

	public void setStateTaxesOwed(BigDecimal stateTaxesOwed)
	{
		this.stateTaxesOwed = stateTaxesOwed;
	}

	public BigDecimal getQualifiedDividends()
	{
		return qualifiedDividends;
	}

	public void setQualifiedDividends(BigDecimal qualifiedDividends)
	{
		this.qualifiedDividends = qualifiedDividends;
	}

	public BigDecimal getDividendsForeignTaxPaid()
	{
		return dividendsForeignTaxPaid;
	}

	public void setDividendsForeignTaxPaid(BigDecimal dividendsForeignTaxPaid)
	{
		this.dividendsForeignTaxPaid = dividendsForeignTaxPaid;
	}

	public BigDecimal getDividendsReturnOfCapital()
	{
		return dividendsReturnOfCapital;
	}

	public void setDividendsReturnOfCapital(BigDecimal dividendsReturnOfCapital)
	{
		this.dividendsReturnOfCapital = dividendsReturnOfCapital;
	}

	public BigDecimal getVision()
	{
		return vision;
	}

	public void setVision(BigDecimal vision)
	{
		this.vision = vision;
	}

	public BigDecimal getNcSurtaxOwed()
	{
		return ncSurtaxOwed;
	}

	public void setNcSurtaxOwed(BigDecimal ncSurtaxOwed)
	{
		this.ncSurtaxOwed = ncSurtaxOwed;
	}
		
}
