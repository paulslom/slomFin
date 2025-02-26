package com.pas.util;

import java.util.Comparator;

import com.pas.dynamodb.DynamoTransaction;

public class TransactionComparatorAscDate implements Comparator<DynamoTransaction>
{
	static int FIRST_ITEM_LESS_THAN_SECOND = -1;
	static int ITEMS_ARE_EQUAL = 0;
	static int FIRST_ITEM_GREATER_THAN_SECOND = 1;
	
	//each item label contains the full team name.
	public int compare(DynamoTransaction trx1, DynamoTransaction trx2)
	{
		int accountCompare = trx1.getAccountID().compareTo(trx2.getAccountID());
		
		if (accountCompare != ITEMS_ARE_EQUAL) 
		{
			return accountCompare; 
		}
		
		//get this far and accounts are equal; return based on trx posted dates.
		int trxDateCompare =  trx1.getTransactionPostedDate().compareTo(trx2.getTransactionPostedDate());
		
		if (trxDateCompare != ITEMS_ARE_EQUAL) 
		{
			return trxDateCompare; 
		}
		
		//get this far and accounts AND dates are equal; return based on trx id.		
		return trx1.getTransactionID().compareTo(trx2.getTransactionID());
		
	}

}
