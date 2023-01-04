package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class TransactionSelection  implements IValueObject
{
	public TransactionSelection()
	{
		portfolioList = null;
		accountID = null;
		investmentList = null;
		transactionTypeList = null;
		fromDate = null;
		toDate = null;
		investmentTypeList = null;
		
	}
	private String portfolioList;
	private Integer accountID;
	private String investmentList;
	private String transactionTypeList;
	private String fromDate;
	private String toDate;
	private String investmentTypeList;
	private boolean recentInd;
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("TransactionID = " + portfolioList + "\n");
		buf.append("AccountID = " + accountID + "\n");
		buf.append("investmentList = " + investmentList + "\n");
		buf.append("transactionTypeList = " + transactionTypeList + "\n");
		buf.append("select * from date = " + fromDate + "\n");
		buf.append("to date = " + toDate + "\n");
		buf.append("investmentTypeList = " + investmentTypeList + "\n");
				
		return buf.toString();
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getInvestmentList()
	{
		return investmentList;
	}

	public void setInvestmentList(String investmentList)
	{
		this.investmentList = investmentList;
	}

	public String getInvestmentTypeList()
	{
		return investmentTypeList;
	}

	public void setInvestmentTypeList(String investmentTypeList)
	{
		this.investmentTypeList = investmentTypeList;
	}

	public String getPortfolioList()
	{
		return portfolioList;
	}

	public void setPortfolioList(String portfolioList)
	{
		this.portfolioList = portfolioList;
	}

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public String getTransactionTypeList()
	{
		return transactionTypeList;
	}

	public void setTransactionTypeList(String transactionTypeList)
	{
		this.transactionTypeList = transactionTypeList;
	}

	public Integer getAccountID()
	{
		return accountID;
	}

	public void setAccountID(Integer accountID)
	{
		this.accountID = accountID;
	}

	public boolean isRecentInd()
	{
		return recentInd;
	}

	public void setRecentInd(boolean recentInd)
	{
		this.recentInd = recentInd;
	}

	
}
