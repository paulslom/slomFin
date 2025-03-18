package com.pas.util;

import java.util.Comparator;

import com.pas.pojo.CapitalGain;

public class CapitalGainComparator implements Comparator<CapitalGain>
{
	static int FIRST_ITEM_LESS_THAN_SECOND = -1;
	static int ITEMS_ARE_EQUAL = 0;
	static int FIRST_ITEM_GREATER_THAN_SECOND = 1;
	
	//each item label contains the full team name.
	public int compare(CapitalGain cg1, CapitalGain cg2)
	{
		int dateCompare = cg1.getSaleDate().compareTo(cg2.getSaleDate());
		
		if (dateCompare != ITEMS_ARE_EQUAL) 
		{
			return dateCompare; 
		}
		
		//get this far and dates are equal; return based on investment name.
		return cg1.getInvestmentName().compareTo(cg2.getInvestmentName());
		
	}

}
