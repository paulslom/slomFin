package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class PortfolioSummary implements IValueObject
{
	private Integer portfolioID;
	private Integer accountID;
	private String  portfolioName;
	private String  accountName;
	private String  investmentDescription;
	private BigDecimal unitsOwned;
	private BigDecimal currentPrice;
	private BigDecimal currentValue;
	private BigDecimal assetClassPercentage;
	private String assetClass;
	private BigDecimal newMoney;  //needed by goals report
	private String accountType;  //needed by goals report
			
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getNewMoney()
	{
		return newMoney;
	}

	public void setNewMoney(BigDecimal newMoney)
	{
		this.newMoney = newMoney;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("portfolio ID  = " + portfolioID + "\n");
		buf.append("portfolio Name = " + portfolioName + "\n");	
		buf.append("account ID  = " + portfolioID + "\n");
		buf.append("account Name = " + portfolioName + "\n");		
				
		return buf.toString();
	}	

	public Integer getPortfolioID()
	{
		return portfolioID;
	}

	public void setPortfolioID(Integer portfolioID)
	{
		this.portfolioID = portfolioID;
	}

	public String getPortfolioName()
	{
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName)
	{
		this.portfolioName = portfolioName;
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

	public String getAssetClass()
	{
		return assetClass;
	}

	public void setAssetClass(String assetClass)
	{
		this.assetClass = assetClass;
	}

	public BigDecimal getCurrentPrice()
	{
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice)
	{
		this.currentPrice = currentPrice;
	}

	public BigDecimal getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue)
	{
		this.currentValue = currentValue;
	}

	public String getInvestmentDescription()
	{
		return investmentDescription;
	}

	public void setInvestmentDescription(String investmentDescription)
	{
		this.investmentDescription = investmentDescription;
	}

	public BigDecimal getUnitsOwned()
	{
		return unitsOwned;
	}

	public void setUnitsOwned(BigDecimal unitsOwned)
	{
		this.unitsOwned = unitsOwned;
	}

	public BigDecimal getAssetClassPercentage()
	{
		return assetClassPercentage;
	}

	public void setAssetClassPercentage(BigDecimal assetClassPercentage)
	{
		this.assetClassPercentage = assetClassPercentage;
	}

	
	
}