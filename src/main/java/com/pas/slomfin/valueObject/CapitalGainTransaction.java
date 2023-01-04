package com.pas.slomfin.valueObject;

import java.math.BigDecimal;
import java.util.Date;

import com.pas.valueObject.IValueObject;

public class CapitalGainTransaction implements IValueObject
{
	private Date cgDate;
	private Integer investmentID;
	private String  investmentName;
	private Integer portfolioID;
	private String  portfolioName;
	private BigDecimal units;
	private String transactionType;
	private BigDecimal costProceeds;
	private BigDecimal price;
	private Integer accountID;	
	private String  accountName;
	
    
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("portfolio Name = " + portfolioName + "\n");	
		buf.append("account Name = " + accountName + "\n");		
		buf.append("investment Name = " + investmentName + "\n");		
		buf.append("Date = " + cgDate + "\n");	
		buf.append("Units = " + units + "\n");	
		buf.append("Trx Type = " + transactionType + "\n");	
		buf.append("Cost/Proceeds = " + costProceeds + "\n");	
		
		return buf.toString();
	}


	public Date getCgDate() {
		return cgDate;
	}


	public void setCgDate(Date cgDate) {
		this.cgDate = cgDate;
	}


	public Integer getInvestmentID() {
		return investmentID;
	}


	public void setInvestmentID(Integer investmentID) {
		this.investmentID = investmentID;
	}


	public String getInvestmentName() {
		return investmentName;
	}


	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}


	public Integer getPortfolioID() {
		return portfolioID;
	}


	public void setPortfolioID(Integer portfolioID) {
		this.portfolioID = portfolioID;
	}


	public String getPortfolioName() {
		return portfolioName;
	}


	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}


	public BigDecimal getUnits() {
		return units;
	}


	public void setUnits(BigDecimal units) {
		this.units = units;
	}


	public String getTransactionType() {
		return transactionType;
	}


	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


	public BigDecimal getCostProceeds() {
		return costProceeds;
	}


	public void setCostProceeds(BigDecimal costProceeds) {
		this.costProceeds = costProceeds;
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


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
}