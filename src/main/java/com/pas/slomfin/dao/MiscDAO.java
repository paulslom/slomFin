package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		MiscDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction DAO extends BaseDBDAO. Implements the data access to the tblTransaction table
 * Copyright: 	Copyright (c) 2006
 */
public class MiscDAO extends BaseDBDAO
{
    private static final MiscDAO currentInstance = new MiscDAO();

    private MiscDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TransactionDAO
     */
    public static MiscDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }    
		
	@SuppressWarnings("unchecked")
	public Integer getNextCheckNo(Integer accountID) throws DAOException
    {
    	final String methodName = "getNextCheckNo::";
		log.debug(methodName + "in");
		
		Integer checkNo = null;
		
		log.debug(methodName + "before querying to get the next check no");
				
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select max(trx.icheckNo) as maxCheckNo");
		sbuf.append(" from Tbltransaction trx ");
        sbuf.append("where trx.iaccountId = ");
		sbuf.append(accountID.toString());
		
		log.debug("about to run query: " + sbuf.toString());
			
		Integer maxCheckNo = this.getJdbcTemplate().queryForObject(sbuf.toString(), Integer.class); 	
		
		if (maxCheckNo != null)
		{
			checkNo = maxCheckNo + 1;
		}
								
		log.debug(methodName + "out");
		return checkNo;
		
    }	
	
}
