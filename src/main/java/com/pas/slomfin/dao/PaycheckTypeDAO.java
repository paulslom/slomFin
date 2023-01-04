package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblpaychecktype;
import com.pas.dbobjects.TblpaychecktypeRowMapper;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class PaycheckTypeDAO extends BaseDBDAO
{
    private static final PaycheckTypeDAO currentInstance = new PaycheckTypeDAO();

    private PaycheckTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static PaycheckTypeDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblpaychecktype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblpaychecktype paychecktype = (Tblpaychecktype)Info;
				
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblpaychecktype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblpaychecktype");
		
		if (info instanceof String)
		{
			String paychecktypeDesc = (String)info;
			sbuf.append(" where sdescription = '");
			sbuf.append(paychecktypeDesc);		
			sbuf.append("'");
		}
		else  //assume it's an integer
		{	
			Integer paychecktypeID = (Integer)info;
			sbuf.append(" where ipaycheckTypeId = ");
			sbuf.append(paychecktypeID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblpaychecktype> paychecktypeList = this.getJdbcTemplate().query(sbuf.toString(), new TblpaychecktypeRowMapper());
									
		log.debug("final list size is = " + paychecktypeList.size());
		log.debug(methodName + "out");
		return paychecktypeList;	
	}
	
	public List<Tblpaychecktype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblpaychecktype paychecktype = (Tblpaychecktype)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblpaychecktype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		Tblpaychecktype paychecktype = (Tblpaychecktype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
