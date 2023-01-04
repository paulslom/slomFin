package com.pas.util;

import java.util.Comparator;
import java.util.Date;

import com.pas.slomfin.valueObject.TaxesReport;

public class TaxesComparator implements Comparator<TaxesReport>
{
	public int compare(TaxesReport taxesDetail, TaxesReport anotherTaxesDetail)
	{
		Integer sortOrder1 = taxesDetail.getSortOrder();
		Integer sortOrder2 = anotherTaxesDetail.getSortOrder();
	
		Integer subSortOrder1 = taxesDetail.getSubSortOrder();
		Integer subSortOrder2 = anotherTaxesDetail.getSubSortOrder();
		
		Date taxDate1= taxesDetail.getTaxDate();
		Date taxDate2 = anotherTaxesDetail.getTaxDate();
			
		if (sortOrder1.compareTo(sortOrder2) == 0) //means they are equal - need to go to subsort
		{
			if (subSortOrder1.compareTo(subSortOrder2) == 0) //means they are equal - need to go to subsort
		        return taxDate1.compareTo(taxDate2);
			else
				return subSortOrder1.compareTo(subSortOrder2);
		}	
		else
			return sortOrder1.compareTo(sortOrder2);
	}

}
