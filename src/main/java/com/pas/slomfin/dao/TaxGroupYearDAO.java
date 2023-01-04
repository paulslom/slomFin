package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		TaxGroupYearDAO
 * Project: 	Slomkowski Financial Application
 * Description: TaxGroupYear DAO extends BaseDBDAO. Implements the data access to the tblTaxGroupYear table
 * Copyright: 	Copyright (c) 2006
 */
public class TaxGroupYearDAO extends BaseDBDAO
{
    private static final TaxGroupYearDAO currentInstance = new TaxGroupYearDAO();

    private TaxGroupYearDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TaxGroupYearDAO
     */
    public static TaxGroupYearDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tbltaxgroupyear> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxGroupYearDAO add");
				
		Tbltaxgroupyear taxGroupYear = (Tbltaxgroupyear)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tbltaxgroupyear> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
									
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tbltaxgroupyear");
		
		if (Info instanceof Integer)
		{
			Integer taxgroupYearID = (Integer)Info;
							
			sbuf.append(" where itaxgroupyearId = ");
			sbuf.append(taxgroupYearID.toString());
			
			log.debug(methodName + "before inquiring for Tbltaxgroup. Key value is = " + taxgroupYearID);
	
		}		
		else			
		{
            //this is an inquire request for a list of TaxGroupYears - Investor object provided			
			log.debug("Will perform multiple rows retrieval for TaxGroupYears");
			
			String taxGroupID = ((Investor)Info).getTaxGroupID();
						
			sbuf.append(" where itaxgroupId = ");
			sbuf.append(taxGroupID);
			sbuf.append(" ORDER BY itaxYear DESC"); 
					
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltaxgroupyear> taxGroupYearList = new ArrayList();		
								
		log.debug("final list size is = " + taxGroupYearList.size());
		log.debug(methodName + "out");
		return taxGroupYearList;	
	}
	
	public List<Tbltaxgroupyear> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxGroupYearDAO update");
				
		Tbltaxgroupyear taxGroupYear = (Tbltaxgroupyear)Info;
			
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbltaxgroupyear> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering TaxGroupYearDAO delete");
						
		Tbltaxgroupyear taxGroupYear = (Tbltaxgroupyear)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
