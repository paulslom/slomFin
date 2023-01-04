package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		PaycheckOutflowAdd
 * Project: 	Portfolio
 * Copyright: 	Copyright (c) 2007
 */
public class PaycheckOutflowAdd implements IValueObject
{	
    private boolean processInd;
    private Integer pcoAddID;    
	private Integer accountID;
	private Integer investmentID;
	private String accountName;
	private Integer transactionTypeID;
	private String transactionTypeDesc;
	private String pcoAddDate;
	private String amount;
	private String checkNo;
	private String description;
	private Integer xferAccountID;
	private String xferAccountName;
	private Integer wdCategoryID;
	private Integer cashDepositTypeID;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

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
   
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
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

	public String getTransactionTypeDesc()
	{
		return transactionTypeDesc;
	}

	public void setTransactionTypeDesc(String transactionTypeDesc)
	{
		this.transactionTypeDesc = transactionTypeDesc;
	}
	
	public String getXferAccountName()
	{
		return xferAccountName;
	}

	public void setXferAccountName(String xferAccountName)
	{
		this.xferAccountName = xferAccountName;
	}
    public String getAmount()
    {
        return amount;
    }
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    public String getCheckNo()
    {
        return checkNo;
    }
    public void setCheckNo(String checkNo)
    {
        this.checkNo = checkNo;
    }
    
    public boolean isProcessInd()
    {
        return processInd;
    }
    public void setProcessInd(boolean processInd)
    {
        this.processInd = processInd;
    }
    public Integer getPcoAddID()
    {
        return pcoAddID;
    }
    public void setPcoAddID(Integer pcoAddID)
    {
        this.pcoAddID = pcoAddID;
    }
    public Integer getInvestmentID()
    {
        return investmentID;
    }
    public void setInvestmentID(Integer investmentID)
    {
        this.investmentID = investmentID;
    }

	public String getPcoAddDate()
	{
		return pcoAddDate;
	}

	public void setPcoAddDate(String pcoAddDate)
	{
		this.pcoAddDate = pcoAddDate;
	}

}