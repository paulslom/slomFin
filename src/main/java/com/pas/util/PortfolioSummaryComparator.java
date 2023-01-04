package com.pas.util;

import java.math.BigDecimal;
import java.util.Comparator;

import com.pas.slomfin.valueObject.PortfolioSummary;

public class PortfolioSummaryComparator implements Comparator<PortfolioSummary>
{
	public int compare(PortfolioSummary psDetail, PortfolioSummary anotherPSDetail)
	{	
		Integer portfolioID1 = psDetail.getPortfolioID();
		Integer portfolioID2 = anotherPSDetail.getPortfolioID();
		
		String accountName1 = psDetail.getAccountName();
		String accountName2 = anotherPSDetail.getAccountName();
		
		BigDecimal amount1 = psDetail.getCurrentValue();
		BigDecimal amount2 = anotherPSDetail.getCurrentValue();
			
		if (portfolioID1.compareTo(portfolioID2) == 0) //means they are equal - need to go to accountName
		{	
			if (accountName1.compareTo(accountName2) == 0) //means they are equal - need to go to amounts
			    return amount2.compareTo(amount1); //compare 2 to 1 to get descending amounts
			else
				return accountName1.compareTo(accountName2);
		}
		else
			return portfolioID1.compareTo(portfolioID2);
	}

}
