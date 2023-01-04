package com.pas.slomfin.valueObject;

import java.util.Date;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class PortfolioHistory implements IValueObject
{
	private Date portfolioHistoryDate;
	private java.math.BigDecimal netWorth;
	private java.math.BigDecimal yearlyDollarsGainLoss;
	private java.math.BigDecimal yearlyPercentageGainLoss;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("portfolio History Date  = " + portfolioHistoryDate + "\n");
		buf.append("net worth = " + netWorth + "\n");		
				
		return buf.toString();
	}

	public java.math.BigDecimal getNetWorth()
	{
		return netWorth;
	}

	public void setNetWorth(java.math.BigDecimal netWorth)
	{
		this.netWorth = netWorth;
	}

	public Date getPortfolioHistoryDate()
	{
		return portfolioHistoryDate;
	}

	public void setPortfolioHistoryDate(Date portfolioHistoryDate)
	{
		this.portfolioHistoryDate = portfolioHistoryDate;
	}

	public java.math.BigDecimal getYearlyDollarsGainLoss()
	{
		return yearlyDollarsGainLoss;
	}

	public void setYearlyDollarsGainLoss(java.math.BigDecimal yearlyDollarsGainLoss)
	{
		this.yearlyDollarsGainLoss = yearlyDollarsGainLoss;
	}

	public java.math.BigDecimal getYearlyPercentageGainLoss()
	{
		return yearlyPercentageGainLoss;
	}

	public void setYearlyPercentageGainLoss(
			java.math.BigDecimal yearlyPercentageGainLoss)
	{
		this.yearlyPercentageGainLoss = yearlyPercentageGainLoss;
	}

	
}