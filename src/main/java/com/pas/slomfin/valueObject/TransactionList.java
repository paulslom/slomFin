package com.pas.slomfin.valueObject;

import java.util.ArrayList;
import java.util.List;

import com.pas.valueObject.IValueObject;

public class TransactionList  implements IValueObject
{
	public TransactionList()
	{			
	}
	
	private List<PaycheckOutflowAdd> transactionList = new ArrayList<PaycheckOutflowAdd>();

	public List<PaycheckOutflowAdd> getTransactionList()
    {
        return transactionList;
    }
    public void setPcoAddList(List<PaycheckOutflowAdd> transactionList)
    {
        this.transactionList = transactionList;
    }
}