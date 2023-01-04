package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblAccountRowMapper;
import com.pas.dbobjects.TblBrokerRowMapper;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblbroker;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		BrokerDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class BrokerDAO extends BaseDBDAO
{
    private static final BrokerDAO currentInstance = new BrokerDAO();

    private BrokerDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static BrokerDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblbroker> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblbroker broker = (Tblbroker)Info;
					
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblbroker> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblbroker");
		
		if (info instanceof String)
		{
			String brokerDesc = (String)info;
			sbuf.append(" where sdescription = '");
			sbuf.append(brokerDesc);		
			sbuf.append("'");
		}
		else  //assume it's an integer
		{	
			Integer brokerID = (Integer)info;
			sbuf.append(" where ibrokerId = ");
			sbuf.append(brokerID.toString());		
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblbroker> brokerList =  this.getJdbcTemplate().query(sbuf.toString(), new TblBrokerRowMapper());		
										
		log.debug("final list size is = " + brokerList.size());
		log.debug(methodName + "out");
		return brokerList;	
	}
	
	public List<Tblbroker> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblbroker broker = (Tblbroker)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblbroker> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		Tblbroker broker = (Tblbroker)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
