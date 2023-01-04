package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblCashDepositTypeRowMapper;
import com.pas.dbobjects.Tblcashdeposittype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class CashDepositTypeDAO extends BaseDBDAO
{
    private static final CashDepositTypeDAO currentInstance = new CashDepositTypeDAO();

    private CashDepositTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static CashDepositTypeDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblcashdeposittype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblcashdeposittype cdtyp = (Tblcashdeposittype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblcashdeposittype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblcashdeposittype");
		
		if (info instanceof String)
		{
			String cdtypeDesc = (String)info;
			sbuf.append(" where sdescription = '");
			sbuf.append(cdtypeDesc);		
			sbuf.append("'");
		}
		else  //assume it's an integer
		{	
			Integer cdtypeID = (Integer)info;
			sbuf.append(" where icashDepositTypeId = ");
			sbuf.append(cdtypeID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblcashdeposittype> cdtypeList = this.getJdbcTemplate().query(sbuf.toString(), new TblCashDepositTypeRowMapper()); 
									
		log.debug("final list size is = " + cdtypeList.size());
		log.debug(methodName + "out");
		return cdtypeList;	
	}
	
	public List<Tblcashdeposittype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering OptionTypeDAO update");
						
		Tblcashdeposittype cdtype = (Tblcashdeposittype)Info;
			
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblcashdeposittype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering OptionTypeDAO delete");
						
		Tblcashdeposittype cdtype = (Tblcashdeposittype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
