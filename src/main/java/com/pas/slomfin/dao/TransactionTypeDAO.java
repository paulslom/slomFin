package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblTransactionTypeRowMapper;
import com.pas.dbobjects.Tbltransactiontype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class TransactionTypeDAO extends BaseDBDAO
{
	private static final TransactionTypeDAO currentInstance = new TransactionTypeDAO();

    private TransactionTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static TransactionTypeDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltransactiontype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tbltransactiontype trxtyp = (Tbltransactiontype)Info;
				
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tbltransactiontype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select trxtyp.*, trxtyp.sDescription as trxTypeDescription from Tbltransactiontype trxtyp");
		
		if (info instanceof String)
		{
			String trxTypeDesc = (String)info;
			sbuf.append(" where sdescription = '");
			sbuf.append(trxTypeDesc);		
			sbuf.append("'");
		}
		else  //assume it's an integer
		{	
			Integer trxTypeID = (Integer)info;
			sbuf.append(" where itransactionTypeId = ");
			sbuf.append(trxTypeID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltransactiontype> trxTypeList = this.getJdbcTemplate().query(sbuf.toString(), new TblTransactionTypeRowMapper());
									
		log.debug("final list size is = " + trxTypeList.size());
		log.debug(methodName + "out");
		return trxTypeList;	
	}
	
	public List<Tbltransactiontype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering JobDAO update");
						
		Tbltransactiontype trxType = (Tbltransactiontype)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltransactiontype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering JobDAO delete");
						
		Tbltransactiontype trxType = (Tbltransactiontype)Info;
				
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
