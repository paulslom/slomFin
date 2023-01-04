package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

public class AccountMargin  implements IValueObject
{	
	private String accountName;
	private BigDecimal marginInterestRate;
	private BigDecimal accountBalance;
	
	public String getAccountName() 
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public BigDecimal getMarginInterestRate()
	{
		return marginInterestRate;
	}
	public void setMarginInterestRate(BigDecimal marginInterestRate)
	{
		this.marginInterestRate = marginInterestRate;
	}
	public BigDecimal getAccountBalance()
	{
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance)
	{
		this.accountBalance = accountBalance;
	}
	

	
	
}
