package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class TaxesSelection  implements IValueObject
{
	private static final long serialVersionUID = 1L;

	public TaxesSelection()
	{
				
	}
	private Integer taxYear;
	private boolean taxableInd;
	private Integer taxGroupID;
	private String residenceState;
			
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("taxYear = " + taxYear + "\n");
		buf.append("taxGroupID = " + taxGroupID + "\n");
								
		return buf.toString();
	}

	public String getResidenceState()
	{
		return residenceState;
	}

	public void setResidenceState(String residenceState)
	{
		this.residenceState = residenceState;
	}

	public Integer getTaxGroupID()
	{
		return taxGroupID;
	}

	public void setTaxGroupID(Integer taxGroupID)
	{
		this.taxGroupID = taxGroupID;
	}

	public Integer getTaxYear()
	{
		return taxYear;
	}

	public void setTaxYear(Integer taxYear)
	{
		this.taxYear = taxYear;
	}

	public boolean isTaxableInd() {
		return taxableInd;
	}

	public void setTaxableInd(boolean taxableInd) {
		this.taxableInd = taxableInd;
	}

	
}
