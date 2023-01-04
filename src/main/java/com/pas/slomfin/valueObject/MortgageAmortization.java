package com.pas.slomfin.valueObject;

import java.util.Calendar;
import java.util.Date;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		Mortgage Payment
 * Copyright: 	Copyright (c) 2007
 */
public class MortgageAmortization implements IValueObject
{
	
	private Integer mortgagePaymentNumber;
	private Date mortgagePaymentDate;
	private String mortgagePaymentYear;
	private Date mortgagePaymentFromDate;
	private Date mortgagePaymentToDate;
	private java.math.BigDecimal extraPrincipalPaid;
	private java.math.BigDecimal principalPaid;
	private java.math.BigDecimal interestPaid;
	private java.math.BigDecimal propertyTaxesPaid;
	private java.math.BigDecimal pmiPaid;
	private java.math.BigDecimal homeownersInsurancePaid;
	private java.math.BigDecimal mortgageBalance;
	private boolean actualPayment;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("mortgage payment ID  = " + mortgagePaymentNumber + "\n");
		buf.append("date  = " + mortgagePaymentDate + "\n");
				
		return buf.toString();
	}

	/**
	 * @return Returns the homeownersInsurancePaid.
	 */
	public java.math.BigDecimal getHomeownersInsurancePaid() {
		return homeownersInsurancePaid;
	}
	/**
	 * @param homeownersInsurancePaid The homeownersInsurancePaid to set.
	 */
	public void setHomeownersInsurancePaid(
			java.math.BigDecimal homeownersInsurancePaid) {
		this.homeownersInsurancePaid = homeownersInsurancePaid;
	}
	/**
	 * @return Returns the interestPaid.
	 */
	public java.math.BigDecimal getInterestPaid() {
		return interestPaid;
	}
	/**
	 * @param interestPaid The interestPaid to set.
	 */
	public void setInterestPaid(java.math.BigDecimal interestPaid) {
		this.interestPaid = interestPaid;
	}
	/**
	 * @return Returns the mortgagePaymentDate.
	 */
	public java.util.Date getMortgagePaymentDate() {
		return mortgagePaymentDate;
	}
	/**
	 * @param mortgagePaymentDate The mortgagePaymentDate to set.
	 */
	public void setMortgagePaymentDate(java.util.Date mortgagePaymentDate)
	{
		this.mortgagePaymentDate = mortgagePaymentDate;
		setMortgagePaymentYear(mortgagePaymentDate);
	}
	/**
	 * @return Returns the pmiPaid.
	 */
	public java.math.BigDecimal getPmiPaid() {
		return pmiPaid;
	}
	/**
	 * @param pmiPaid The pmiPaid to set.
	 */
	public void setPmiPaid(java.math.BigDecimal pmiPaid) {
		this.pmiPaid = pmiPaid;
	}
	/**
	 * @return Returns the principalPaid.
	 */
	public java.math.BigDecimal getPrincipalPaid() {
		return principalPaid;
	}
	/**
	 * @param principalPaid The principalPaid to set.
	 */
	public void setPrincipalPaid(java.math.BigDecimal principalPaid) {
		this.principalPaid = principalPaid;
	}
	/**
	 * @return Returns the propertyTaxesPaid.
	 */
	public java.math.BigDecimal getPropertyTaxesPaid() {
		return propertyTaxesPaid;
	}
	/**
	 * @param propertyTaxesPaid The propertyTaxesPaid to set.
	 */
	public void setPropertyTaxesPaid(java.math.BigDecimal propertyTaxesPaid) {
		this.propertyTaxesPaid = propertyTaxesPaid;
	}

	public String getMortgagePaymentYear()
	{
		return mortgagePaymentYear;
	}

	public void setMortgagePaymentYear(Date mortgagePaymentDate)
	{
		Calendar tempCal = Calendar.getInstance();
		tempCal.setTimeInMillis(mortgagePaymentDate.getTime());
		this.mortgagePaymentYear = String.valueOf(tempCal.get(Calendar.YEAR));
	}

	public java.math.BigDecimal getExtraPrincipalPaid()
	{
		return extraPrincipalPaid;
	}

	public void setExtraPrincipalPaid(java.math.BigDecimal extraPrincipalPaid)
	{
		this.extraPrincipalPaid = extraPrincipalPaid;
	}

	public java.util.Date getMortgagePaymentFromDate()
	{
		return mortgagePaymentFromDate;
	}

	public void setMortgagePaymentFromDate(java.util.Date mortgagePaymentFromDate)
	{
		this.mortgagePaymentFromDate = mortgagePaymentFromDate;
	}

	public Integer getMortgagePaymentNumber()
	{
		return mortgagePaymentNumber;
	}

	public void setMortgagePaymentNumber(Integer mortgagePaymentNumber)
	{
		this.mortgagePaymentNumber = mortgagePaymentNumber;
	}

	public java.util.Date getMortgagePaymentToDate()
	{
		return mortgagePaymentToDate;
	}

	public void setMortgagePaymentToDate(java.util.Date mortgagePaymentToDate)
	{
		this.mortgagePaymentToDate = mortgagePaymentToDate;
	}

	public void setMortgagePaymentYear(String mortgagePaymentYear)
	{
		this.mortgagePaymentYear = mortgagePaymentYear;
	}

	public java.math.BigDecimal getMortgageBalance()
	{
		return mortgageBalance;
	}

	public void setMortgageBalance(java.math.BigDecimal mortgageBalance)
	{
		this.mortgageBalance = mortgageBalance;
	}

	public boolean isActualPayment()
	{
		return actualPayment;
	}

	public void setActualPayment(boolean actualPayment)
	{
		this.actualPayment = actualPayment;
	}
}