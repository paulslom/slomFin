package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxpayment;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		TaxPaymentDAO
 * Project: 	Slomkowski Financial Application
 * Description: TaxPayment DAO extends BaseDBDAO. Implements the data access to the tblTaxPayment table
 * Copyright: 	Copyright (c) 2006
 */
public class TaxPaymentDAO extends BaseDBDAO
{
    private static final TaxPaymentDAO currentInstance = new TaxPaymentDAO();

    private TaxPaymentDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TaxPaymentDAO
     */
    public static TaxPaymentDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltaxpayment> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxPaymentDAO add");
		
		Tbltaxpayment taxPayment = (Tbltaxpayment)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tbltaxpayment> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
				
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltaxpayment");
		
		if (Info instanceof Integer)
		{
			Integer taxpaymentID = (Integer)Info;
							
			sbuf.append(" where itaxPaymentId = ");
			sbuf.append(taxpaymentID.toString());
			
			log.debug(methodName + "before inquiring for Tbltaxpayment. Key value is = " + taxpaymentID);
	
		}
		else //this is an investor object
		{
		    //this is an inquire request for a list of Tax payments - Investor object provided			
			log.debug("Will perform multiple rows retrieval for Tax payments");
			
			String taxGroupID = ((Investor)Info).getTaxGroupID();
						
			sbuf.append(" where itaxgroupId = ");
			sbuf.append(taxGroupID);
			sbuf.append(" order by dtaxPaymentDate desc");

		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltaxpayment> taxPaymentList = new ArrayList();		
				
		log.debug("final list size is = " + taxPaymentList.size());
		log.debug(methodName + "out");
		return taxPaymentList;	
	}
	
	public List<Tbltaxpayment> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxPaymentDAO update");
				
		Tbltaxpayment taxPayment = (Tbltaxpayment)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltaxpayment> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxPaymentDAO delete");
				
		Tbltaxpayment taxPayment = (Tbltaxpayment)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
