package com.pas.util;

import java.math.BigDecimal;
import java.util.Comparator;

import com.pas.slomfin.valueObject.PortfolioSummary;

public class PortfolioSummaryByACComparator implements Comparator<PortfolioSummary>
{
	public int compare(PortfolioSummary psDetail, PortfolioSummary anotherPSDetail)
	{	
		String assetClass1 = psDetail.getAssetClass();
		String assetClass2 = anotherPSDetail.getAssetClass();
		
		BigDecimal amount1 = psDetail.getCurrentValue();
		BigDecimal amount2 = anotherPSDetail.getCurrentValue();
			
		if (assetClass1.compareTo(assetClass2) == 0) //means they are equal - need to go to amount
		    return amount2.compareTo(amount1); //compare 2 to 1 to get descending amounts
			
		return assetClass1.compareTo(assetClass2);
	}

}
