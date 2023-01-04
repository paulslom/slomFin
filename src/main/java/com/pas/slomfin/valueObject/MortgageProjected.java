package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class MortgageProjected  implements IValueObject
{	
	private Double projectedInterest;
	private Double projectedPropertyTaxes;
	
	public Double getProjectedInterest()
	{
		return projectedInterest;
	}
	public void setProjectedInterest(Double projectedInterest)
	{
		this.projectedInterest = projectedInterest;
	}
	public Double getProjectedPropertyTaxes() 
	{
		return projectedPropertyTaxes;
	}
	public void setProjectedPropertyTaxes(Double projectedPropertyTaxes)
	{
		this.projectedPropertyTaxes = projectedPropertyTaxes;
	}	
	
}
