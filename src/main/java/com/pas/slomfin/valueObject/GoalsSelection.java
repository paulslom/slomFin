package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

public class GoalsSelection  implements IValueObject
{
	public GoalsSelection()
	{
				
	}
	
	private Integer goalsInvestorID;
	private Integer goalsRateOfReturn;	
	private Integer goalsProjectionYears;
			
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("goals investor ID = " + goalsInvestorID + "\n");
		buf.append("goals Rate of Return = " + goalsRateOfReturn + "\n");
								
		return buf.toString();
	}

	public Integer getGoalsInvestorID()
	{
		return goalsInvestorID;
	}

	public void setGoalsInvestorID(Integer goalsInvestorID)
	{
		this.goalsInvestorID = goalsInvestorID;
	}

	public Integer getGoalsProjectionYears()
	{
		return goalsProjectionYears;
	}

	public void setGoalsProjectionYears(Integer goalsProjectionYears)
	{
		this.goalsProjectionYears = goalsProjectionYears;
	}

	public Integer getGoalsRateOfReturn()
	{
		return goalsRateOfReturn;
	}

	public void setGoalsRateOfReturn(Integer goalsRateOfReturn)
	{
		this.goalsRateOfReturn = goalsRateOfReturn;
	}

		
}
