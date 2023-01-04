package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		PaycheckOutflow
 * Project: 	Portfolio
 * Copyright: 	Copyright (c) 2007
 */
public class PaycheckOutflow implements IValueObject
{	
	private Integer paycheckOutflowID;
	private Integer investorID;
	private Integer transactionTypeID;
	private String transactionTypeDesc;
	private Integer accountID;
	private String accountName;
	private java.math.BigDecimal defaultAmount;
	private String defaultDay;
	private boolean nextMonthInd;
	private String description;
	private Integer xferAccountID;
	private String xferAccountName;
	private Integer wdCategoryID;
	private String wdCategoryDesc;
	private Integer cashDepositTypeID;
	private String cashDepositTypeDesc;
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("paycheck Outflow ID  = " + paycheckOutflowID + "\n");
		buf.append("description  = " + description + "\n");
				
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
    public Integer getCashDepositTypeID()
    {
        return cashDepositTypeID;
    }
    public void setCashDepositTypeID(Integer cashDepositTypeID)
    {
        this.cashDepositTypeID = cashDepositTypeID;
    }
    public java.math.BigDecimal getDefaultAmount()
    {
        return defaultAmount;
    }
    public void setDefaultAmount(java.math.BigDecimal defaultAmount)
    {
        this.defaultAmount = defaultAmount;
    }
    public String getDefaultDay()
    {
        return defaultDay;
    }
    public void setDefaultDay(String defaultDay)
    {
        this.defaultDay = defaultDay;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public Integer getInvestorID()
    {
        return investorID;
    }
    public void setInvestorID(Integer investorID)
    {
        this.investorID = investorID;
    }
    public boolean isNextMonthInd()
    {
        return nextMonthInd;
    }
    public void setNextMonthInd(boolean nextMonthInd)
    {
        this.nextMonthInd = nextMonthInd;
    }
    public Integer getPaycheckOutflowID()
    {
        return paycheckOutflowID;
    }
    public void setPaycheckOutflowID(Integer paycheckOutflowID)
    {
        this.paycheckOutflowID = paycheckOutflowID;
    }
    public Integer getTransactionTypeID()
    {
        return transactionTypeID;
    }
    public void setTransactionTypeID(Integer transactionTypeID)
    {
        this.transactionTypeID = transactionTypeID;
    }
    public Integer getWdCategoryID()
    {
        return wdCategoryID;
    }
    public void setWdCategoryID(Integer wdCategoryID)
    {
        this.wdCategoryID = wdCategoryID;
    }
    public Integer getXferAccountID()
    {
        return xferAccountID;
    }
    public void setXferAccountID(Integer xferAccountID)
    {
        this.xferAccountID = xferAccountID;
    }

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getCashDepositTypeDesc()
	{
		return cashDepositTypeDesc;
	}

	public void setCashDepositTypeDesc(String cashDepositTypeDesc)
	{
		this.cashDepositTypeDesc = cashDepositTypeDesc;
	}

	public String getTransactionTypeDesc()
	{
		return transactionTypeDesc;
	}

	public void setTransactionTypeDesc(String transactionTypeDesc)
	{
		this.transactionTypeDesc = transactionTypeDesc;
	}

	public String getWdCategoryDesc()
	{
		return wdCategoryDesc;
	}

	public void setWdCategoryDesc(String wdCategoryDesc)
	{
		this.wdCategoryDesc = wdCategoryDesc;
	}

	public String getXferAccountName()
	{
		return xferAccountName;
	}

	public void setXferAccountName(String xferAccountName)
	{
		this.xferAccountName = xferAccountName;
	}
}