package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class UnitsOwned implements IValueObject
{
	private String  investmentDescription;
	private BigDecimal unitsOwned;
				
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("investment Description  = " + investmentDescription + "\n");
		buf.append("unitsOwned = " + unitsOwned + "\n");		
				
		return buf.toString();
	}

	public String getInvestmentDescription()
	{
		return investmentDescription;
	}

	public void setInvestmentDescription(String investmentDescription)
	{
		this.investmentDescription = investmentDescription;
	}

	public BigDecimal getUnitsOwned()
	{
		return unitsOwned;
	}

	public void setUnitsOwned(BigDecimal unitsOwned)
	{
		this.unitsOwned = unitsOwned;
	}	
	
}