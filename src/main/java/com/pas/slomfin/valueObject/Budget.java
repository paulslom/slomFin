package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class Budget implements IValueObject
{	
	private Integer budgetYear;
	private String budgetType;
	private String budgetDescription;
	private java.math.BigDecimal budgetTotal;
		
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("mortgage ID  = " + budgetType + "\n");
		buf.append("description  = " + budgetDescription + "\n");
				
		return buf.toString();
	}

	public String getBudgetDescription()
	{
		return budgetDescription;
	}

	public void setBudgetDescription(String budgetDescription)
	{
		this.budgetDescription = budgetDescription;
	}

	public java.math.BigDecimal getBudgetTotal()
	{
		return budgetTotal;
	}

	public void setBudgetTotal(java.math.BigDecimal budgetTotal)
	{
		this.budgetTotal = budgetTotal;
	}

	public String getBudgetType()
	{
		return budgetType;
	}

	public void setBudgetType(String budgetType)
	{
		this.budgetType = budgetType;
	}

	public Integer getBudgetYear()
	{
		return budgetYear;
	}

	public void setBudgetYear(Integer budgetYear)
	{
		this.budgetYear = budgetYear;
	}

	
}