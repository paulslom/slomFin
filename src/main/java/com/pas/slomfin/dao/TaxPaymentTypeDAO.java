package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxpaymenttype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TaxPaymentDAO
 * Project: 	Slomkowski Financial Application
 * Description: TaxPayment DAO extends BaseDBDAO. Implements the data access to the tblTaxPayment table
 * Copyright: 	Copyright (c) 2006
 */
public class TaxPaymentTypeDAO extends BaseDBDAO
{
    private static final TaxPaymentTypeDAO currentInstance = new TaxPaymentTypeDAO();

    private TaxPaymentTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TaxPaymentDAO
     */
    public static TaxPaymentTypeDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltaxpaymenttype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxPaymentDAO add");
		
		Tbltaxpaymenttype taxPaymentType = (Tbltaxpaymenttype)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tbltaxpaymenttype> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
				
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltaxpaymenttype");
		
		Integer taxpaymentID = (Integer)Info;
							
		sbuf.append(" where itaxPaymentTypeId = ");
		sbuf.append(taxpaymentID.toString());
			
		log.debug(methodName + "before inquiring for Tbltaxpaymenttype. Key value is = " + taxpaymentID);
			
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltaxpaymenttype> taxPaymentList = new ArrayList();		
				
		log.debug("final list size is = " + taxPaymentList.size());
		log.debug(methodName + "out");
		return taxPaymentList;	
	}
	
	public List<Tbltaxpaymenttype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxPaymentDAO update");
				
		Tbltaxpaymenttype taxPaymentType = (Tbltaxpaymenttype)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltaxpaymenttype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxPaymentDAO delete");
				
		Tbltaxpaymenttype taxPaymentType = (Tbltaxpaymenttype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
