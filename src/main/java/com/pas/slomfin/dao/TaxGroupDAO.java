package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxgroup;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TaxGroupDAO
 * Project: 	Slomkowski Financial Application
 * Description: TaxGroup DAO extends BaseDBDAO. Implements the data access to the tblTaxGroup table
 * Copyright: 	Copyright (c) 2006
 */
public class TaxGroupDAO extends BaseDBDAO 
{
    private static final TaxGroupDAO currentInstance = new TaxGroupDAO();

    private TaxGroupDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TaxGroupDAO
     */
    public static TaxGroupDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltaxgroup> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxGroupDAO add");
						
		Tbltaxgroup taxgroup = (Tbltaxgroup)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tbltaxgroup> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltaxgroup");
		
		if (Info instanceof Integer)
		{
			Integer taxgroupID = (Integer)Info;
							
			sbuf.append(" where itaxgroupId = ");
			sbuf.append(taxgroupID.toString());
			
			log.debug(methodName + "before inquiring for Tbltaxgroup. Key value is = " + taxgroupID);
	
		}	
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltaxgroup> taxGroupList = new ArrayList();		
							
		log.debug("final list size is = " + taxGroupList.size());
		log.debug(methodName + "out");
		return taxGroupList;	
	}
	
	public List<Tbltaxgroup> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TbltaxgroupDAO update");
						
		Tbltaxgroup taxgroup = (Tbltaxgroup)Info;
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltaxgroup> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TbltaxgroupDAO delete");
		
		Tbltaxgroup taxgroup = (Tbltaxgroup)Info;
			
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
