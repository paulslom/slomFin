package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblassetclass;
import com.pas.dbobjects.TblassetclassRowMapper;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		AssetClassDAO
 * Project: 	Slomkowski Financial Application
 * Copyright: 	Copyright (c) 2006
 */
public class AssetClassDAO extends BaseDBDAO
{
    private static final AssetClassDAO currentInstance = new AssetClassDAO();

    private AssetClassDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static AssetClassDAO getDAOInstance() 
    {
      	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
        return currentInstance;
    }
    
	public List<Tblassetclass> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblassetclass ac = (Tblassetclass)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblassetclass (");
		sbuf.append("sAssetClass, dHistoricalReturn");
		sbuf.append(") values(?,?)");	
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {ac.getSassetClass(), ac.getDhistoricalReturn()});		
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblassetclass> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblassetclass");
		
		if (info instanceof String)
		{
			log.debug("instance of string, no where clause needed");
		}
		else  //assume it's an integer
		{	
			Integer acID = (Integer)info;
			sbuf.append(" where iassetclassId = ");
			sbuf.append(acID.toString());		
		}
		
		sbuf.append(" order by sAssetClass");	
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblassetclass> cdtypeList = this.getJdbcTemplate().query(sbuf.toString(), new TblassetclassRowMapper());
									
		log.debug("final list size is = " + cdtypeList.size());
		log.debug(methodName + "out");
		return cdtypeList;	
	}
	
	public List<Tblassetclass> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblassetclass ac = (Tblassetclass)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("UPDATE tblassetclass set ");
		sbuf.append("sAssetClass = ?, dHistoricalReturn = ?");
		sbuf.append(" where iassetclassid = ?");	
		
		log.info("about to run update.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {ac.getSassetClass(), ac.getDhistoricalReturn(), ac.getIassetClassId()});	
		
    	//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblassetclass> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");		
						
		Tblassetclass ac = (Tblassetclass)Info;
		
		Integer id = ac.getIassetClassId();
		
		log.info("about to delete id: " + id);
		
		String deleteStr = "delete from tblassetclass where iassetclassid = ?";
		this.getJdbcTemplate().update(deleteStr, id);
		
		log.info("successfully deleted asset class id: " + id);
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
