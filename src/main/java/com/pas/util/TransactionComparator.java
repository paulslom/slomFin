package com.pas.util;

import java.util.Comparator;

import com.pas.dynamodb.DynamoTransaction;
import jakarta.faces.model.SelectItem;

public class TransactionComparator implements Comparator<DynamoTransaction>
{
	//each item label contains the full team name.
	public int compare(DynamoTransaction trx1, DynamoTransaction trx2)
	{
		return trx1.getTransactionPostedDate().compareTo(trx2.getTransactionPostedDate());
	}

}
