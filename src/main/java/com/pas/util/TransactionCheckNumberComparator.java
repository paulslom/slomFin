package com.pas.util;

import java.util.Comparator;

import com.pas.dynamodb.DynamoTransaction;

public class TransactionCheckNumberComparator implements Comparator<DynamoTransaction>
{
	static int FIRST_ITEM_LESS_THAN_SECOND = -1;
	static int ITEMS_ARE_EQUAL = 0;
	static int FIRST_ITEM_GREATER_THAN_SECOND = 1;
	
	//each item label contains the full team name.
	public int compare(DynamoTransaction trx1, DynamoTransaction trx2)
	{
		return trx1.getCheckNo().compareTo(trx2.getCheckNo());		
	}

}
