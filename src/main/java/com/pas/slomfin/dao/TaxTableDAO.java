package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxformulas;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.IDObject;
import com.pas.util.Utils;

/**
 * Title: 		TaxTableDAO
 * Project: 	Slomkowski Financial Application
 * Description: TaxTable DAO extends BaseDBDAO. Implements the data access to the tblTaxTable table
 * Copyright: 	Copyright (c) 2006
 */
public class TaxTableDAO extends BaseDBDAO
{
    private static final TaxTableDAO currentInstance = new TaxTableDAO();

    private TaxTableDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TaxTableDAO
     */
    public static TaxTableDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltaxformulas> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxTableDAO add");
		
		if (Info instanceof IDObject)
		{
			taxClone();
			return null;
		}
				
		Tbltaxformulas taxTable = (Tbltaxformulas)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings("unchecked")
	public List<Tbltaxformulas> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
					
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltaxformulas");
		
		if (Info instanceof Integer)
		{
			Integer taxFormulaID = (Integer)Info;
							
			sbuf.append(" where itaxFormulaId = ");
			sbuf.append(taxFormulaID.toString());
			
			log.debug(methodName + "before inquiring for Tbltaxformulas. Key value is = " + taxFormulaID);
	
		}
		
		sbuf.append(" ORDER BY itaxYear DESC,"); 
        sbuf.append(" tbltaxformulatype.sformulaDescription DESC,");
        sbuf.append(" mincomeLow");
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltaxformulas> taxFormulaList = new ArrayList();		
								
		log.debug("final list size is = " + taxFormulaList.size());
		log.debug(methodName + "out");
		return taxFormulaList;	
	}
	
	public List<Tbltaxformulas> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxTableDAO update");
				
		Tbltaxformulas taxTable = (Tbltaxformulas)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltaxformulas> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxTableDAO delete");
						
		Tbltaxformulas taxTable = (Tbltaxformulas)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public void taxClone() throws DAOException
	{
	   	final String methodName = "taxClone::";
		log.debug(methodName + "in");
		
		//First task is to find the max year of any tax formula.
		//This is in order to know what year to clone from and to.
		
		StringBuffer sbufMax = new StringBuffer();
		sbufMax.append("select max(taxf.itaxYear) as maxTaxYear");
		sbufMax.append(" from Tbltaxformulas taxf ");
     	
		log.debug("about to run query: " + sbufMax.toString());
	
		List taxfList = new ArrayList();		
		
		Integer maxTaxYear;
		Integer maxTaxYearPlusOne;
		
		if (taxfList.size() > 0)
		{
			maxTaxYear = (Integer)taxfList.get(0);
			maxTaxYearPlusOne = maxTaxYear + 1;			
			log.debug("We will be cloning Tax formulas from year = " + maxTaxYear.toString());
		}
		else
		{
			log.debug("Unable to determine Tax year to clone from");
			return; //if we can't determine this then forget it.
		}
		
		//Next task is to get a list of all TaxFormula records for the max Tax year
		//These will be the ones we'll clone from.
		
		StringBuffer sbufCloneFrom = new StringBuffer();
		sbufCloneFrom.append("select * from Tbltaxformulas");
		sbufCloneFrom.append(" where itaxYear = ");
		sbufCloneFrom.append(maxTaxYear.toString());
     	
		log.debug("about to run query: " + sbufCloneFrom.toString());
				
		List<Tbltaxformulas> taxCloneFromList = new ArrayList();		
		
		log.debug("total tax formula records to clone from year " + maxTaxYear.toString() + " is = " + taxCloneFromList.size());
		
		for (int i = 0; i < taxCloneFromList.size(); i++)
		{
			Tbltaxformulas taxCloneTo = new Tbltaxformulas();
			Tbltaxformulas taxCloneFrom = taxCloneFromList.get(i);
			taxCloneTo.setDstateTaxCreditRate(taxCloneFrom.getDstateTaxCreditRate());
			taxCloneTo.setItaxYear(maxTaxYearPlusOne);
			taxCloneTo.setDtaxRate(taxCloneFrom.getDtaxRate());
			taxCloneTo.setDfederalDaycareCreditRate(taxCloneFrom.getDfederalDaycareCreditRate());			
			taxCloneTo.setMfederalRecoveryAmount(taxCloneFrom.getMfederalRecoveryAmount());
			taxCloneTo.setDncSurtaxRate(taxCloneFrom.getDncSurtaxRate());			
			taxCloneTo.setDncDaycareCreditRate(taxCloneFrom.getDncDaycareCreditRate());
			taxCloneTo.setMchildCredit(taxCloneFrom.getMchildCredit());
			taxCloneTo.setMexemption(taxCloneFrom.getMexemption());
			taxCloneTo.setMfixedTaxAmount(taxCloneFrom.getMfixedTaxAmount());
			taxCloneTo.setMincomeHigh(taxCloneFrom.getMincomeHigh());
			taxCloneTo.setMincomeLow(taxCloneFrom.getMincomeLow());
			taxCloneTo.setMncchildCreditThreshold(taxCloneFrom.getMncchildCreditThreshold());
			taxCloneTo.setMncexemptionThreshold(taxCloneFrom.getMncexemptionThreshold());
			taxCloneTo.setMncoverExemptionAmount(taxCloneFrom.getMncoverExemptionAmount());
			taxCloneTo.setMncunderExemptionAmount(taxCloneFrom.getMncunderExemptionAmount());
			taxCloneTo.setMsocialSecurityWageLimit(taxCloneFrom.getMsocialSecurityWageLimit());
			taxCloneTo.setMstandardDeduction(taxCloneFrom.getMstandardDeduction());
			taxCloneTo.setMstatePropertyTaxLimit(taxCloneFrom.getMstatePropertyTaxLimit());
			taxCloneTo.setTbltaxformulatype(taxCloneFrom.getTbltaxformulatype());
		}
		
		//Next task is to get a list of all TaxGroupYear records for the max Tax year
		//These will be the ones we'll clone from.  There should actually only be one of these.
		
		StringBuffer sbufCloneFrom2 = new StringBuffer();
		sbufCloneFrom2.append("select * from Tbltaxgroupyear");
		sbufCloneFrom2.append(" where itaxYear = ");
		sbufCloneFrom2.append(maxTaxYear.toString());
     	
		log.debug("about to run query: " + sbufCloneFrom.toString());
				
		List<Tbltaxgroupyear> taxCloneFromList2 = new ArrayList();		
		
		log.debug("total tax group year records to clone from year " + maxTaxYear.toString() + " is = " + taxCloneFromList2.size());
		
		for (int i = 0; i < taxCloneFromList2.size(); i++)
		{
			Tbltaxgroupyear taxCloneTo = new Tbltaxgroupyear();
			Tbltaxgroupyear taxCloneFrom = taxCloneFromList2.get(i);			
			taxCloneTo.setItaxYear(maxTaxYearPlusOne);
			taxCloneTo.setDdividendTaxRate(taxCloneFrom.getDdividendTaxRate());
			taxCloneTo.setIdependents(taxCloneFrom.getIdependents());
			taxCloneTo.setMcapitalLossCarryover(taxCloneFrom.getMcapitalLossCarryover());
			taxCloneTo.setMcarTax(taxCloneFrom.getMcarTax());
			taxCloneTo.setMdayCareExpensesPaid(taxCloneFrom.getMdayCareExpensesPaid());
			taxCloneTo.setMdividendsForeignTax(taxCloneFrom.getMdividendsForeignTax());
			taxCloneTo.setMdividendsReturnOfCapital(taxCloneFrom.getMdividendsReturnOfCapital());
			taxCloneTo.setMiradistribution(taxCloneFrom.getMiradistribution());
			taxCloneTo.setMotherIncome(taxCloneFrom.getMotherIncome());
			taxCloneTo.setMotherItemized(taxCloneFrom.getMotherItemized());
			taxCloneTo.setMprevYearStateRefund(taxCloneFrom.getMprevYearStateRefund());
			taxCloneTo.setMqualifiedDividends(taxCloneFrom.getMqualifiedDividends());
			taxCloneTo.setSfilingStatus(taxCloneFrom.getSfilingStatus());
			taxCloneTo.setSotherIncomeDesc(taxCloneFrom.getSotherIncomeDesc());
			taxCloneTo.setSotherItemizedDesc(taxCloneFrom.getSotherItemizedDesc());
			taxCloneTo.setTbltaxgroup(taxCloneFrom.getTbltaxgroup());
		}
		log.debug(methodName + "out");
				
    }

}
