package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblInvestmentTypeRowMapper;
import com.pas.dbobjects.Tblinvestmenttype;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class InvestmentTypeDAO extends BaseDBDAO
{
    private static final InvestmentTypeDAO currentInstance = new InvestmentTypeDAO();

    private InvestmentTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static InvestmentTypeDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblinvestmenttype> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblinvestmenttype ityp = (Tblinvestmenttype)Info;
						
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblinvestmenttype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select invtyp.*, invtyp.sdescription as investmentTypeDescription from Tblinvestmenttype invtyp ");
		
		if (info instanceof String)
		{
			String inputStr = (String)info;
			sbuf.append(inputStr);
		}
		else  //assume it's an integer
		{	
			Integer itypeID = (Integer)info;
			sbuf.append(" where iinvestmentTypeId = ");
			sbuf.append(itypeID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblinvestmenttype> itypeList = this.getJdbcTemplate().query(sbuf.toString(), new TblInvestmentTypeRowMapper())	;	
									
		log.debug("final list size is = " + itypeList.size());
		log.debug(methodName + "out");
		return itypeList;	
	}
	
	public List<Tblinvestmenttype> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblinvestmenttype itype = (Tblinvestmenttype)Info;
			
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblinvestmenttype> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		Tblinvestmenttype itype = (Tblinvestmenttype)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
