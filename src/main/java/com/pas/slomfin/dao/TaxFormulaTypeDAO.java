package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxformulatype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TaxFormulaDAO
 * Project: 	Slomkowski Financial Application
 * Description: TaxFormula DAO extends BaseDBDAO. Implements the data access to the tblTaxFormula table
 * Copyright: 	Copyright (c) 2006
 */
public class TaxFormulaTypeDAO extends BaseDBDAO
{
    private static final TaxFormulaTypeDAO currentInstance = new TaxFormulaTypeDAO();

    private TaxFormulaTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TaxFormulaDAO
     */
    public static TaxFormulaTypeDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltaxformulatype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxFormulaDAO add");
		
		Tbltaxformulatype taxFormulaType = (Tbltaxformulatype)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tbltaxformulatype> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
				
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltaxformulatype");
		
		Integer taxformulaID = (Integer)Info;
							
		sbuf.append(" where itaxFormulaTypeId = ");
		sbuf.append(taxformulaID.toString());
			
		log.debug(methodName + "before inquiring for Tbltaxformulatype. Key value is = " + taxformulaID);
			
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltaxformulatype> taxFormulaList = new ArrayList();		
				
		log.debug("final list size is = " + taxFormulaList.size());
		log.debug(methodName + "out");
		return taxFormulaList;	
	}
	
	public List<Tbltaxformulatype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxFormulaDAO update");
				
		Tbltaxformulatype taxFormulaType = (Tbltaxformulatype)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltaxformulatype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxFormulaDAO delete");
				
		Tbltaxformulatype taxFormulaType = (Tbltaxformulatype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
