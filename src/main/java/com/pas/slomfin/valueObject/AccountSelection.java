package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class AccountSelection  implements IValueObject
{
	private static final long serialVersionUID = 1L;

	public AccountSelection()
	{
		portfolioID = null;						
	}
	private Integer portfolioID;
	private Integer investorID;
	private boolean closed;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("portfolioID = " + portfolioID + "\n");
		buf.append("closed = " + closed + "\n");
							
		return buf.toString();
	}

	public boolean isClosed()
	{
		return closed;
	}

	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	public Integer getPortfolioID()
	{
		return portfolioID;
	}

	public void setPortfolioID(Integer portfolioID)
	{
		this.portfolioID = portfolioID;
	}

	public Integer getInvestorID() {
		return investorID;
	}

	public void setInvestorID(Integer investorID) {
		this.investorID = investorID;
	}

	
}
