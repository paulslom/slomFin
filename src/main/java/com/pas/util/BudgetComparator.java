package com.pas.util;

import java.math.BigDecimal;
import java.util.Comparator;

import com.pas.slomfin.valueObject.Budget;

public class BudgetComparator implements Comparator<Budget>
{
	public int compare(Budget budgetDetail, Budget anotherBudgetDetail)
	{
		String budgetType1 = budgetDetail.getBudgetType();
		String budgetType2 = anotherBudgetDetail.getBudgetType();
	
		BigDecimal amount1 = budgetDetail.getBudgetTotal();
		BigDecimal amount2 = anotherBudgetDetail.getBudgetTotal();
			
		if (budgetType2.compareTo(budgetType1) == 0) //means they are equal - need to go to amounts
		    return amount1.compareTo(amount2);
			
		return budgetType2.compareTo(budgetType1);
	}

}
