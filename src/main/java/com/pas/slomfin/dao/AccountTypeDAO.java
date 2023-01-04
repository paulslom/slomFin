package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblAccountTypeRowMapper;
import com.pas.dbobjects.Tblaccounttype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class AccountTypeDAO extends BaseDBDAO
{
    private static final AccountTypeDAO currentInstance = new AccountTypeDAO();

    private AccountTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static AccountTypeDAO getDAOInstance() 
    {
      	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
        return currentInstance;
    }
    
	public List<Tblaccounttype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblaccounttype acctyp = (Tblaccounttype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblaccounttype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblaccounttype");
		
		if (info instanceof String)
		{
			String accTypeDesc = (String)info;
			sbuf.append(" where sdescription = '");
			sbuf.append(accTypeDesc);		
			sbuf.append("'");
		}
		else  //assume it's an integer
		{	
			Integer accTypeID = (Integer)info;
			sbuf.append(" where iaccountTypeId = ");
			sbuf.append(accTypeID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblaccounttype> accTypeList = this.getJdbcTemplate().query(sbuf.toString(), new TblAccountTypeRowMapper());
									
		log.debug("final list size is = " + accTypeList.size());
		log.debug(methodName + "out");
		return accTypeList;	
	}
	
	public List<Tblaccounttype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering JobDAO update");
						
		Tblaccounttype accType = (Tblaccounttype)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblaccounttype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering JobDAO delete");
						
		Tblaccounttype accType = (Tblaccounttype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
