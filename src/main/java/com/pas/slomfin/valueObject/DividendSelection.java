package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class DividendSelection  implements IValueObject
{
	private boolean taxableOnly;
	private Integer dividendYear;
	private Integer taxGroupID;
			
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("dividendYear = " + dividendYear + "\n");
		buf.append("taxGroupID = " + taxGroupID + "\n");
								
		return buf.toString();
	}

	public Integer getDividendYear() {
		return dividendYear;
	}

	public void setDividendYear(Integer dividendYear) {
		this.dividendYear = dividendYear;
	}

	public Integer getTaxGroupID() {
		return taxGroupID;
	}

	public void setTaxGroupID(Integer taxGroupID) {
		this.taxGroupID = taxGroupID;
	}

	public boolean isTaxableOnly() {
		return taxableOnly;
	}

	public void setTaxableOnly(boolean taxableOnly) {
		this.taxableOnly = taxableOnly;
	}

	
}
