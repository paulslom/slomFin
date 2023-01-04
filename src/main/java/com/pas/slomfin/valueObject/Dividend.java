package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class Dividend implements IValueObject
{
	private String dividendPayableYear;
	private String  portfolioName;
	private Integer accountID;	
	private String  accountName;
	private String  taxableYN;
	private Integer investmentID;
	private String  investmentDescription;
	private java.util.Date dividendDate;	
	private BigDecimal units;
	private BigDecimal costProceeds;
				
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("Portfolio Name = " + portfolioName  + "\n");
		buf.append("account ID  = " + accountID + "\n");
		buf.append("account Name = " + accountName + "\n");
		buf.append("Dividend Year = " + dividendPayableYear + "\n");
		buf.append("Date = " + dividendDate + "\n");
		buf.append("Investment ID = " + investmentID + "\n");
		buf.append("Investment Name = " + investmentDescription + "\n");
		buf.append("Amount = " + costProceeds + "\n");
		buf.append("taxableYN = " + taxableYN + "\n");
					
		return buf.toString();
	}
	
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTaxableYN() {
		return taxableYN;
	}

	public void setTaxableYN(String taxableYN) {
		this.taxableYN = taxableYN;
	}

	public Integer getInvestmentID() {
		return investmentID;
	}

	public void setInvestmentID(Integer investmentID) {
		this.investmentID = investmentID;
	}

	public String getInvestmentDescription() {
		return investmentDescription;
	}

	public void setInvestmentDescription(String investmentDescription) {
		this.investmentDescription = investmentDescription;
	}

	public java.util.Date getDividendDate() {
		return dividendDate;
	}

	public void setDividendDate(java.util.Date dividendDate) {
		this.dividendDate = dividendDate;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public BigDecimal getCostProceeds() {
		return costProceeds;
	}

	public void setCostProceeds(BigDecimal costProceeds) {
		this.costProceeds = costProceeds;
	}

	public String getDividendPayableYear() {
		return dividendPayableYear;
	}

	public void setDividendPayableYear(String dividendPayableYear) {
		this.dividendPayableYear = dividendPayableYear;
	}

	
	
}