package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class TransactionSearch  implements IValueObject
{
	private Integer trxSearchInvestorID;
	private String trxDescriptionText;
	private String fromDate;
	private String toDate;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("select * from date = " + fromDate + "\n");
		buf.append("to date = " + toDate + "\n");
		buf.append("trxDescriptionText = " + trxDescriptionText + "\n");
				
		return buf.toString();
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getTrxDescriptionText() {
		return trxDescriptionText;
	}

	public void setTrxDescriptionText(String trxDescriptionText) {
		this.trxDescriptionText = trxDescriptionText;
	}

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public Integer getTrxSearchInvestorID() {
		return trxSearchInvestorID;
	}

	public void setTrxSearchInvestorID(Integer trxSearchInvestorID) {
		this.trxSearchInvestorID = trxSearchInvestorID;
	}
	
}
