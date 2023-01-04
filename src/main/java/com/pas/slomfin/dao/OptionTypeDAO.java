package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbloptiontype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class OptionTypeDAO extends BaseDBDAO
{
    private static final OptionTypeDAO currentInstance = new OptionTypeDAO();

    private OptionTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static OptionTypeDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbloptiontype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tbloptiontype opttyp = (Tbloptiontype)Info;
				
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tbloptiontype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbloptiontype");
		
		if (info instanceof String)
		{
			String optTypeDesc = (String)info;
			sbuf.append(" where sdescription = '");
			sbuf.append(optTypeDesc);		
			sbuf.append("'");
		}
		else  //assume it's an integer
		{	
			Integer optTypeID = (Integer)info;
			sbuf.append(" where ioptionTypeId = ");
			sbuf.append(optTypeID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbloptiontype> optTypeList = new ArrayList();		
									
		log.debug("final list size is = " + optTypeList.size());
		log.debug(methodName + "out");
		return optTypeList;	
	}
	
	public List<Tbloptiontype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering OptionTypeDAO update");
						
		Tbloptiontype optType = (Tbloptiontype)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbloptiontype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering OptionTypeDAO delete");
						
		Tbloptiontype optType = (Tbloptiontype)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
