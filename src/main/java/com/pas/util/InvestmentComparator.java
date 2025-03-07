package com.pas.util;

import java.util.Comparator;

import com.pas.beans.Investment;

public class InvestmentComparator implements Comparator<Investment>
{
	static int FIRST_ITEM_LESS_THAN_SECOND = -1;
	static int ITEMS_ARE_EQUAL = 0;
	static int FIRST_ITEM_GREATER_THAN_SECOND = 1;
	
	//each item label contains the full team name.
	public int compare(Investment obj1, Investment obj2)
	{
		return obj1.getDescription().compareTo(obj2.getDescription());
		
	}

}
