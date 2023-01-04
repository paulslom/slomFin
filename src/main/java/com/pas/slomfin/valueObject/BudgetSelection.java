package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class BudgetSelection  implements IValueObject
{
	public BudgetSelection()
	{
				
	}
	private Integer budgetYear;
	private Integer budgetInvestorID;
			
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("budgetYear = " + budgetYear + "\n");
		buf.append("budgetInvestorID = " + budgetInvestorID + "\n");
								
		return buf.toString();
	}

	public Integer getBudgetInvestorID()
	{
		return budgetInvestorID;
	}

	public void setBudgetInvestorID(Integer budgetInvestorID)
	{
		this.budgetInvestorID = budgetInvestorID;
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
