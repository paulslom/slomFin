package com.pas.util;

import java.util.Comparator;

import jakarta.faces.model.SelectItem;

public class TransactionTypeComparator implements Comparator<SelectItem>
{
	static int FIRST_ITEM_LESS_THAN_SECOND = -1;
	static int ITEMS_ARE_EQUAL = 0;
	static int FIRST_ITEM_GREATER_THAN_SECOND = 1;
	
	//each item label contains the full team name.
	public int compare(SelectItem si1, SelectItem si2)
	{
		return si1.getLabel().compareTo(si2.getLabel());
		
	}

}
