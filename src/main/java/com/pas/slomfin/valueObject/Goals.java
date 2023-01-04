package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		Goals
 */
public class Goals implements IValueObject
{	
	private String  portfolioName;
	private String  accountName;
	private BigDecimal accountValue;
						
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("portfolio name  = " + portfolioName + "\n");
		buf.append("account Name = " + accountName + "\n");		
				
		return buf.toString();
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public BigDecimal getAccountValue()
	{
		return accountValue;
	}

	public void setAccountValue(BigDecimal accountValue)
	{
		this.accountValue = accountValue;
	}	

	public String getPortfolioName()
	{
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName)
	{
		this.portfolioName = portfolioName;
	}	
	
}