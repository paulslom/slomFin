package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbltaxformulas implements Serializable {

    /** identifier field */
    private Integer itaxFormulaId;

    /** persistent field */
    private int itaxYear;

    /** nullable persistent field */
    private BigDecimal mincomeLow;

    /** nullable persistent field */
    private BigDecimal mincomeHigh;

    /** nullable persistent field */
    private BigDecimal dtaxRate;

    /** nullable persistent field */
    private BigDecimal mfixedTaxAmount;

    /** nullable persistent field */
    private BigDecimal mchildCredit;

    /** nullable persistent field */
    private BigDecimal mstandardDeduction;

    /** nullable persistent field */
    private BigDecimal mexemption;

    /** nullable persistent field */
    private BigDecimal dstateTaxCreditRate;

    /** nullable persistent field */
    private BigDecimal mstatePropertyTaxLimit;

    /** nullable persistent field */
    private BigDecimal mncexemptionThreshold;

    /** nullable persistent field */
    private BigDecimal mncoverExemptionAmount;

    /** nullable persistent field */
    private BigDecimal mncunderExemptionAmount;

    /** nullable persistent field */
    private BigDecimal mncchildCreditThreshold;

    /** nullable persistent field */
    private BigDecimal msocialSecurityWageLimit;

    /** nullable persistent field */
    private BigDecimal mnc529Deduction;
    
    /** nullable persistent field */
    private BigDecimal dncDaycareCreditRate;
    
    /** nullable persistent field */
    private BigDecimal dfederalDaycareCreditRate;
    
    /** nullable persistent field */
    private BigDecimal mfederalRecoveryAmount;
    
    /** nullable persistent field */
    private BigDecimal dncSurtaxRate;
    
    /** persistent field */
    private com.pas.dbobjects.Tbltaxformulatype tbltaxformulatype;

    /** full constructor */
    public Tbltaxformulas(Integer itaxFormulaId, int itaxYear, BigDecimal mincomeLow, BigDecimal mincomeHigh, BigDecimal dtaxRate, BigDecimal mfixedTaxAmount, BigDecimal mchildCredit, BigDecimal mstandardDeduction, BigDecimal mexemption, BigDecimal dstateTaxCreditRate, BigDecimal mstatePropertyTaxLimit, BigDecimal mncexemptionThreshold, BigDecimal mncoverExemptionAmount, BigDecimal mncunderExemptionAmount, BigDecimal mncchildCreditThreshold, BigDecimal msocialSecurityWageLimit, com.pas.dbobjects.Tbltaxformulatype tbltaxformulatype) {
        this.itaxFormulaId = itaxFormulaId;
        this.itaxYear = itaxYear;
        this.mincomeLow = mincomeLow;
        this.mincomeHigh = mincomeHigh;
        this.dtaxRate = dtaxRate;
        this.mfixedTaxAmount = mfixedTaxAmount;
        this.mchildCredit = mchildCredit;
        this.mstandardDeduction = mstandardDeduction;
        this.mexemption = mexemption;
        this.dstateTaxCreditRate = dstateTaxCreditRate;
        this.mstatePropertyTaxLimit = mstatePropertyTaxLimit;
        this.mncexemptionThreshold = mncexemptionThreshold;
        this.mncoverExemptionAmount = mncoverExemptionAmount;
        this.mncunderExemptionAmount = mncunderExemptionAmount;
        this.mncchildCreditThreshold = mncchildCreditThreshold;
        this.msocialSecurityWageLimit = msocialSecurityWageLimit;
        this.tbltaxformulatype = tbltaxformulatype;
    }

    /** default constructor */
    public Tbltaxformulas() {
    }

    /** minimal constructor */
    public Tbltaxformulas(Integer itaxFormulaId, int itaxYear, com.pas.dbobjects.Tbltaxformulatype tbltaxformulatype) {
        this.itaxFormulaId = itaxFormulaId;
        this.itaxYear = itaxYear;
        this.tbltaxformulatype = tbltaxformulatype;
    }

    public Integer getItaxFormulaId() {
        return this.itaxFormulaId;
    }

    public void setItaxFormulaId(Integer itaxFormulaId) {
        this.itaxFormulaId = itaxFormulaId;
    }

    public int getItaxYear() {
        return this.itaxYear;
    }

    public void setItaxYear(int itaxYear) {
        this.itaxYear = itaxYear;
    }

    public BigDecimal getMincomeLow() {
        return this.mincomeLow;
    }

    public void setMincomeLow(BigDecimal mincomeLow) {
        this.mincomeLow = mincomeLow;
    }

    public BigDecimal getMincomeHigh() {
        return this.mincomeHigh;
    }

    public void setMincomeHigh(BigDecimal mincomeHigh) {
        this.mincomeHigh = mincomeHigh;
    }

    public BigDecimal getDtaxRate() {
        return this.dtaxRate;
    }

    public void setDtaxRate(BigDecimal dtaxRate) {
        this.dtaxRate = dtaxRate;
    }

    public BigDecimal getMfixedTaxAmount() {
        return this.mfixedTaxAmount;
    }

    public void setMfixedTaxAmount(BigDecimal mfixedTaxAmount) {
        this.mfixedTaxAmount = mfixedTaxAmount;
    }

    public BigDecimal getMchildCredit() {
        return this.mchildCredit;
    }

    public void setMchildCredit(BigDecimal mchildCredit) {
        this.mchildCredit = mchildCredit;
    }

    public BigDecimal getMstandardDeduction() {
        return this.mstandardDeduction;
    }

    public void setMstandardDeduction(BigDecimal mstandardDeduction) {
        this.mstandardDeduction = mstandardDeduction;
    }

    public BigDecimal getMexemption() {
        return this.mexemption;
    }

    public void setMexemption(BigDecimal mexemption) {
        this.mexemption = mexemption;
    }

    public BigDecimal getDstateTaxCreditRate() {
        return this.dstateTaxCreditRate;
    }

    public void setDstateTaxCreditRate(BigDecimal dstateTaxCreditRate) {
        this.dstateTaxCreditRate = dstateTaxCreditRate;
    }

    public BigDecimal getMstatePropertyTaxLimit() {
        return this.mstatePropertyTaxLimit;
    }

    public void setMstatePropertyTaxLimit(BigDecimal mstatePropertyTaxLimit) {
        this.mstatePropertyTaxLimit = mstatePropertyTaxLimit;
    }

    public BigDecimal getMncexemptionThreshold() {
        return this.mncexemptionThreshold;
    }

    public void setMncexemptionThreshold(BigDecimal mncexemptionThreshold) {
        this.mncexemptionThreshold = mncexemptionThreshold;
    }

    public BigDecimal getMncoverExemptionAmount() {
        return this.mncoverExemptionAmount;
    }

    public void setMncoverExemptionAmount(BigDecimal mncoverExemptionAmount) {
        this.mncoverExemptionAmount = mncoverExemptionAmount;
    }

    public BigDecimal getMncunderExemptionAmount() {
        return this.mncunderExemptionAmount;
    }

    public void setMncunderExemptionAmount(BigDecimal mncunderExemptionAmount) {
        this.mncunderExemptionAmount = mncunderExemptionAmount;
    }

    public BigDecimal getMncchildCreditThreshold() {
        return this.mncchildCreditThreshold;
    }

    public void setMncchildCreditThreshold(BigDecimal mncchildCreditThreshold) {
        this.mncchildCreditThreshold = mncchildCreditThreshold;
    }

    public BigDecimal getMsocialSecurityWageLimit() {
        return this.msocialSecurityWageLimit;
    }

    public void setMsocialSecurityWageLimit(BigDecimal msocialSecurityWageLimit) {
        this.msocialSecurityWageLimit = msocialSecurityWageLimit;
    }

    public com.pas.dbobjects.Tbltaxformulatype getTbltaxformulatype() {
        return this.tbltaxformulatype;
    }

    public void setTbltaxformulatype(com.pas.dbobjects.Tbltaxformulatype tbltaxformulatype) {
        this.tbltaxformulatype = tbltaxformulatype;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("itaxFormulaId", getItaxFormulaId())
            .toString();
    }

	public BigDecimal getMnc529Deduction()
	{
		return mnc529Deduction;
	}

	public void setMnc529Deduction(BigDecimal mnc529Deduction)
	{
		this.mnc529Deduction = mnc529Deduction;
	}

	public BigDecimal getDncDaycareCreditRate()
	{
		return dncDaycareCreditRate;
	}

	public void setDncDaycareCreditRate(BigDecimal dncDaycareCreditRate)
	{
		this.dncDaycareCreditRate = dncDaycareCreditRate;
	}

	public BigDecimal getDfederalDaycareCreditRate()
	{
		return dfederalDaycareCreditRate;
	}

	public void setDfederalDaycareCreditRate(BigDecimal dfederalDaycareCreditRate)
	{
		this.dfederalDaycareCreditRate = dfederalDaycareCreditRate;
	}

	public BigDecimal getDncSurtaxRate()
	{
		return dncSurtaxRate;
	}

	public void setDncSurtaxRate(BigDecimal dncSurtaxRate)
	{
		this.dncSurtaxRate = dncSurtaxRate;
	}

	public BigDecimal getMfederalRecoveryAmount()
	{
		return mfederalRecoveryAmount;
	}

	public void setMfederalRecoveryAmount(BigDecimal mfederalRecoveryAmount)
	{
		this.mfederalRecoveryAmount = mfederalRecoveryAmount;
	}

	

}
