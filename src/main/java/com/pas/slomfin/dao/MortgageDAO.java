package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblmortgage;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		MortgageDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgage table
 * Copyright: 	Copyright (c) 2006
 */
public class MortgageDAO extends BaseDBDAO
{
    private static final MortgageDAO currentInstance = new MortgageDAO();

    private MortgageDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return MortgageDAO
     */
    public static MortgageDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
    public List<Tblmortgage> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblmortgage mortgage = (Tblmortgage)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		    
	@SuppressWarnings("unchecked")
	public List<Tblmortgage> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblmortgage");
		
		if (Info instanceof Integer)
		{
			Integer mortgageID = (Integer)Info;
			
			//this is an inquire request for a particular row on the database
			
			sbuf.append(" where imortgageId = ");
			sbuf.append(mortgageID.toString());
			
			log.debug(methodName + "before inquiring for mortgage. Key value is = " + mortgageID.toString());
	
		}		
		else  //this is an inquire request for a list of mortgages - Investor object provided					
		{            	
			log.debug("Will perform multiple rows retrieval for mortgages");
			
			Integer investorID = Integer.valueOf (((Investor)Info).getInvestorID());
						
			sbuf.append(" where iinvestorId = ");
			sbuf.append(investorID.toString());
					
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblmortgage> mortgageList = new ArrayList();		
						
		log.debug("final list size is = " + mortgageList.size());
		log.debug(methodName + "out");
		return mortgageList;	
	}
	
	public List<Tblmortgage> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering mortgageDAO update");
				
		Tblmortgage mortgage = (Tblmortgage)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblmortgage> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering mortgageDAO delete");
						
		Tblmortgage mortgage = (Tblmortgage)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}
	
}
