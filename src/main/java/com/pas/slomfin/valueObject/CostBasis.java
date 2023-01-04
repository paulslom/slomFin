package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class CostBasis implements IValueObject
{
	private Integer investmentID;
	private String  investmentDescription;
	private String transactionTypeDescription;
	private String  portfolioName;
	private BigDecimal costProceeds;
	private BigDecimal decUnits;
	private BigDecimal effectiveAmount;
	private BigDecimal unitsOwned;
	private BigDecimal totalCost;
	private BigDecimal costBasis;
	private BigDecimal currentValue;
	private BigDecimal gainLoss;	
	private Integer accountID;	
	private String  accountName;
	private Boolean positiveInd;
				
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("account ID  = " + accountID + "\n");
		buf.append("account Name = " + accountName + "\n");		
				
		return buf.toString();
	}

	public Integer getAccountID()
	{
		return accountID;
	}

	public void setAccountID(Integer accountID)
	{
		this.accountID = accountID;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public BigDecimal getCostBasis()
	{
		return costBasis;
	}

	public void setCostBasis(BigDecimal costBasis)
	{
		this.costBasis = costBasis;
	}

	public BigDecimal getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue)
	{
		this.currentValue = currentValue;
	}

	public BigDecimal getGainLoss()
	{
		return gainLoss;
	}

	public void setGainLoss(BigDecimal gainLoss)
	{
		this.gainLoss = gainLoss;
	}

	public String getInvestmentDescription()
	{
		return investmentDescription;
	}

	public void setInvestmentDescription(String investmentDescription)
	{
		this.investmentDescription = investmentDescription;
	}

	public Integer getInvestmentID()
	{
		return investmentID;
	}

	public void setInvestmentID(Integer investmentID)
	{
		this.investmentID = investmentID;
	}

	public String getPortfolioName()
	{
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName)
	{
		this.portfolioName = portfolioName;
	}

	public BigDecimal getTotalCost()
	{
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost)
	{
		this.totalCost = totalCost;
	}

	public BigDecimal getUnitsOwned()
	{
		return unitsOwned;
	}

	public void setUnitsOwned(BigDecimal unitsOwned)
	{
		this.unitsOwned = unitsOwned;
	}

	public String getTransactionTypeDescription() {
		return transactionTypeDescription;
	}

	public void setTransactionTypeDescription(String transactionTypeDescription) {
		this.transactionTypeDescription = transactionTypeDescription;
	}

	public BigDecimal getCostProceeds() {
		return costProceeds;
	}

	public void setCostProceeds(BigDecimal costProceeds) {
		this.costProceeds = costProceeds;
	}

	public BigDecimal getDecUnits() {
		return decUnits;
	}

	public void setDecUnits(BigDecimal decUnits) {
		this.decUnits = decUnits;
	}

	public BigDecimal getEffectiveAmount() {
		return effectiveAmount;
	}

	public void setEffectiveAmount(BigDecimal effectiveAmount) {
		this.effectiveAmount = effectiveAmount;
	}

	public Boolean getPositiveInd() {
		return positiveInd;
	}

	public void setPositiveInd(Boolean positiveInd) {
		this.positiveInd = positiveInd;
	}	

	
}