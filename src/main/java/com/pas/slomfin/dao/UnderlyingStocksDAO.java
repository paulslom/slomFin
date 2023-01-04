package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblunderlyingstocks;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		UnderlyingStocksDAO
 * Project: 	Slomkowski Financial Application
 * Description: UnderlyingStocks DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class UnderlyingStocksDAO extends BaseDBDAO
{
    private static final UnderlyingStocksDAO currentInstance = new UnderlyingStocksDAO();

    private UnderlyingStocksDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return tblunderlyingstocksDAO
     */
    public static UnderlyingStocksDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblunderlyingstocks> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblunderlyingstocks tblunderlyingstocks = (Tblunderlyingstocks)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblunderlyingstocks> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		Integer optionInvestmentID = (Integer)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblunderlyingstocks ");
		sbuf.append("where tblinvestmentByIInvestmentId.iinvestmentId = ");
		sbuf.append(optionInvestmentID.toString());
			
		log.debug(methodName + "looking for tblunderlyingstocks with investment id = " + optionInvestmentID.toString());
		
		log.debug("about to run query: " + sbuf.toString());
			
		List<Tblunderlyingstocks> tblunderlyingstocksList = new ArrayList();		
									
		log.debug("final list size is = " + tblunderlyingstocksList.size());
		log.debug(methodName + "out");
		return tblunderlyingstocksList;	
	}
	
	public List<Tblunderlyingstocks> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");		
							
		Tblunderlyingstocks tblunderlyingstocks = (Tblunderlyingstocks)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblunderlyingstocks> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");		
						
		Tblunderlyingstocks tblunderlyingstocks = (Tblunderlyingstocks)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
