package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Budget;
import com.pas.slomfin.valueObject.BudgetSelection;
import com.pas.util.BudgetComparator;
import com.pas.util.Utils;

/**
 * Title: 		BudgetDAO
 * Project: 	Slomkowski Financial Application
 * Copyright: 	Copyright (c) 2007
 */
public class BudgetDAO extends BaseDBDAO
{
    private static final BudgetDAO currentInstance = new BudgetDAO();

    private BudgetDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return BudgetDAO
     */
    public static BudgetDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	@SuppressWarnings("unchecked")
	public List<Budget> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		//The result of this method is to return a list of Budget objects
		
		List<Budget> budgetList = new ArrayList<Budget>();
		
		//Figure out what year and investor first
		BudgetSelection budgetSelection = new BudgetSelection();
		budgetSelection = (BudgetSelection)Info;
		Integer budgetYear = budgetSelection.getBudgetYear();
		Integer investorID = budgetSelection.getBudgetInvestorID();			
		
		//First, income
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("SELECT SUM(PD.mgrossPay) as Pay,");
		sbuf.append(" -SUM(PD.mfederalWithholding) as FedWH,");   
		sbuf.append(" -SUM(PD.mstateWithholding) as StateWH,");   
		sbuf.append(" -SUM(PD.mretirementDeferred) as RetDeferred,");   
		sbuf.append(" -SUM(PD.msswithholding) as SocSec,");   
		sbuf.append(" -SUM(PD.mmedicareWithholding) as Medicare,");   
		sbuf.append(" -SUM(PD.mdental) as Dental,");   
		sbuf.append(" -SUM(PD.mmedical) as Medical,");   
		sbuf.append(" -SUM(PD.mgroupLifeInsurance) as GroupLifeIns");   
		sbuf.append(" FROM Tblpaydayhistory PD");
		sbuf.append(" WHERE PD.tbljob.tblinvestor.iinvestorId = ");
		sbuf.append(investorID.toString());
		sbuf.append(" AND YEAR(dpaydayHistoryDate) = ");
		sbuf.append(budgetYear.toString());
		
		log.debug("about to run query: " + sbuf.toString());
		
		List paycheckTotalsList = new ArrayList();		
		
		log.debug("List size returned from paycheck query is = " + paycheckTotalsList.size());				
		
		//Should only be one row returned here, so don't loop, just use element (0)
		
		Object[] paycheckObject = (Object[])paycheckTotalsList.get(0);
			            
		Budget budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Income");
		budgetDetail.setBudgetDescription("Gross Pay");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[0]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Federal Taxes WH");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[1]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("State Taxes WH");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[2]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("401k");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[3]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Social Security WH");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[4]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Medicare WH");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[5]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Dental Insurance Premium");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[6]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Medical Insurance Premium");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[7]);									
		budgetList.add(budgetDetail);
			
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Group Term Life Premium");
		budgetDetail.setBudgetTotal((BigDecimal)paycheckObject[8]);									
		budgetList.add(budgetDetail);
		
		sbuf.setLength(0); //this clears the stringbuffer
		
		//Next transactions from WD categories
		sbuf.append("SELECT trx.tblwdcategory.swdcategoryDescription, -SUM(trx.mcostProceeds) as TotalCost");
		sbuf.append(" FROM Tbltransaction trx"); 
		sbuf.append(" WHERE trx.tblaccount.tblportfolio.tblinvestor.iinvestorId = ");
		sbuf.append(investorID.toString());
		sbuf.append(" AND YEAR(trx.dtransactionDate) = ");
		sbuf.append(budgetYear.toString());
		sbuf.append("");  
		sbuf.append(" AND trx.tblwdcategory.swdcategoryDescription IS NOT NULL");  
		sbuf.append(" AND trx.tblwdcategory.swdcategoryDescription NOT IN ('Mortgage', 'Not Tracked', 'Not Applicable','House Purchase')");  
		sbuf.append(" AND trx.tbltransactiontype.bpositiveInd = 0");  
		sbuf.append(" AND trx.tblaccount.tblportfolio.btaxableInd = 1");  
		sbuf.append(" GROUP BY trx.tblwdcategory.swdcategoryDescription");
		
		log.debug("about to run query: " + sbuf.toString());
		
		List wdcList = new ArrayList();		
		
		log.debug("List size returned from wd categories is = " + wdcList.size());
		for (int i = 0; i < wdcList.size(); i++)
		{
			Object[] wdObject = (Object[])wdcList.get(i);
            
			budgetDetail = new Budget();			
			budgetDetail.setBudgetYear(budgetYear);
			budgetDetail.setBudgetType("Expense");
			budgetDetail.setBudgetDescription((String)wdObject[0]);
			budgetDetail.setBudgetTotal((BigDecimal)wdObject[1]);									
			budgetList.add(budgetDetail);
		}
	
		sbuf.setLength(0); //this clears the stringbuffer
		
		//Next miscellaneous income
		
		sbuf.append("SELECT trx.tblcashdeposittype.scashDepositTypeDesc, SUM(trx.mcostProceeds) as TotalCost");
		sbuf.append(" FROM Tbltransaction trx"); 
		sbuf.append(" WHERE trx.tblaccount.tblportfolio.tblinvestor.iinvestorId =");
		sbuf.append(investorID.toString());
		sbuf.append(" AND YEAR(trx.dtransactionDate) = ");
		sbuf.append(budgetYear.toString());
		sbuf.append(" AND trx.tblcashdeposittype.scashDepositTypeDesc IN ('Gift','Reimbursement','FFL''Rebates',");  
		sbuf.append(" 'Rebate','Tax Refund','ReturnOfGoods','SaleOfGoods')");		
		sbuf.append(" GROUP BY trx.tblcashdeposittype.scashDepositTypeDesc");
		
		log.debug("about to run query: " + sbuf.toString());
		
		List miscIncomeList = new ArrayList();		
		
		log.debug("List size returned from misc income query is = " + miscIncomeList.size());
		for (int i = 0; i < miscIncomeList.size(); i++)
		{
			Object[] miscIncObject = (Object[])miscIncomeList.get(i);
            
			budgetDetail = new Budget();			
			budgetDetail.setBudgetYear(budgetYear);
			budgetDetail.setBudgetType("Income");
			budgetDetail.setBudgetDescription((String)miscIncObject[0]);
			budgetDetail.setBudgetTotal((BigDecimal)miscIncObject[1]);									
			budgetList.add(budgetDetail);
		}
	
		sbuf.setLength(0); //this clears the stringbuffer
		
		//Next is investing
		
		sbuf.append("SELECT trx.tblcashdeposittype.scashDepositTypeDesc, -SUM(trx.mcostProceeds) as TotalCost");
		sbuf.append(" FROM Tbltransaction trx"); 
		sbuf.append(" WHERE trx.tblaccount.tblportfolio.tblinvestor.iinvestorId = ");
		sbuf.append(investorID.toString());
		sbuf.append(" AND YEAR(trx.dtransactionDate) = ");
		sbuf.append(budgetYear.toString());
		sbuf.append(" AND trx.tblaccount.tblaccounttype.saccountType IN ('Taxable Brokerage', 'Roth IRA')");  
		sbuf.append(" AND trx.tbltransactiontype.sdescription = 'Transfer In'");		
		sbuf.append(" GROUP BY trx.tblcashdeposittype.scashDepositTypeDesc");
			    
	    log.debug("about to run query: " + sbuf.toString());
		
		List invList = new ArrayList();		
		
		log.debug("List size returned from investing query is = " + invList.size());
		for (int i = 0; i < invList.size(); i++)
		{
			Object[] invObject = (Object[])invList.get(i);
            
			budgetDetail = new Budget();			
			budgetDetail.setBudgetYear(budgetYear);
			budgetDetail.setBudgetType("Expense");
			budgetDetail.setBudgetDescription((String)invObject[0]);
			budgetDetail.setBudgetTotal((BigDecimal)invObject[1]);									
			budgetList.add(budgetDetail);
		}
		
		sbuf.setLength(0); //this clears the stringbuffer
		
		//finally mortgage expenses
		
		sbuf.append(" SELECT -SUM(mh.mprincipalPaid) as MtgPrincipal,");
		sbuf.append("    -SUM(mh.minterestPaid) as MtgInterest,");
		sbuf.append("    -SUM(mh.mpropertyTaxesPaid) as PropertyTaxes,");
		sbuf.append("    -SUM(mh.mhomeownersInsPaid) as HomeownersIns");	
		sbuf.append(" FROM Tblmortgagehistory mh");
	    sbuf.append(" WHERE mh.tblmortgage.tblinvestor.iinvestorId = ");
	    sbuf.append(investorID.toString());
		sbuf.append(" AND YEAR(dpaymentDate) = ");
		sbuf.append(budgetYear.toString());
		
		log.debug("about to run query: " + sbuf.toString());
		
		List mortgageExpList = new ArrayList();		
		
		log.debug("List size returned from mortgage expenses query is = " + mortgageExpList.size());				
		
		//Should only be one row returned here, so don't loop, just use element (0)
		
		Object[] mortgageExpenseObject = (Object[])mortgageExpList.get(0);
			            
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Mortgage Principal");
		budgetDetail.setBudgetTotal((BigDecimal)mortgageExpenseObject[0]);									
		budgetList.add(budgetDetail);
		
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Mortgage Interest");
		budgetDetail.setBudgetTotal((BigDecimal)mortgageExpenseObject[1]);									
		budgetList.add(budgetDetail);
		
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Property Taxes");
		budgetDetail.setBudgetTotal((BigDecimal)mortgageExpenseObject[2]);									
		budgetList.add(budgetDetail);
		
		budgetDetail = new Budget();			
		budgetDetail.setBudgetYear(budgetYear);
		budgetDetail.setBudgetType("Expense");
		budgetDetail.setBudgetDescription("Homeowners Insurance");
		budgetDetail.setBudgetTotal((BigDecimal)mortgageExpenseObject[3]);									
		budgetList.add(budgetDetail);
		
		//now sort budgetList by type (desc) and Amount (desc)
		
		Collections.sort(budgetList, new BudgetComparator());
			
		log.debug("final list size is = " + budgetList.size());
		log.debug(methodName + "out");
		return budgetList;	
	}

}
