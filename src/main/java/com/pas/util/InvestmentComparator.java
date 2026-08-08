package com.pas.util;

import java.util.Comparator;

import com.pas.beans.Investment;

public class InvestmentComparator implements Comparator<Investment> 
{

    @Override
    public int compare(Investment o1, Investment o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }

    public static Comparator<Investment> byDescription() {
        return new InvestmentComparator();
    }

    public static Comparator<Investment> byHoldingPercentage() {
        return Comparator.comparing(Investment::getHoldingPercentage).reversed();
    }

}
