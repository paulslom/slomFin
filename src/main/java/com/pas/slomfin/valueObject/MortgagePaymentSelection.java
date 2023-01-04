package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class MortgagePaymentSelection  implements IValueObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MortgagePaymentSelection()
	{
		mortgagePaymentID = null;
		mortgageID = null;		
		fromDate = null;
		toDate = null;				
	}
	private Integer mortgagePaymentID;
	private Integer mortgageID;
	private String fromDate;
	private String toDate;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("mortgagePaymentID = " + mortgagePaymentID + "\n");
		buf.append("mortgageID = " + mortgageID + "\n");
		buf.append("select * from date = " + fromDate + "\n");
		buf.append("to date = " + toDate + "\n");
						
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

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}
	
	public Integer getMortgageID()
	{
		return mortgageID;
	}
	
	public void setMortgageID(Integer mortgageID)
	{
		this.mortgageID = mortgageID;
	}
	
	public Integer getMortgagePaymentID()
	{
		return mortgagePaymentID;
	}
	
	public void setMortgagePaymentID(Integer mortgagePaymentID)
	{
		this.mortgagePaymentID = mortgagePaymentID;
	}
}
