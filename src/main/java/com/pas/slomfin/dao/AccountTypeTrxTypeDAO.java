package com.pas.slomfin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblaccounttype;
import com.pas.dbobjects.Tblaccttypetrxtype;
import com.pas.dbobjects.TblaccttypetrxtypeRowMapper;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class AccountTypeTrxTypeDAO extends BaseDBDAO
{
    private static final AccountTypeTrxTypeDAO currentInstance = new AccountTypeTrxTypeDAO();

    private AccountTypeTrxTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static AccountTypeTrxTypeDAO getDAOInstance() 
    {
      	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
        return currentInstance;
    }
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblaccttypetrxtype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		Integer accountTypeID = (Integer)info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select accttypetrxtype.*, accttype.*, trxtype.*, trxtype.sdescription as trxTypeDescription");
		sbuf.append("  from tblaccttypetrxtype accttypetrxtype, tblaccounttype accttype, tbltransactiontype trxtype");
		sbuf.append(" where accttypetrxtype.iAccountTypeID = accttype.iAccountTypeID");
		sbuf.append(" and accttypetrxtype.iTransactionTypeID = trxtype.iTransactionTypeID");
		sbuf.append(" and accttype.iaccountTypeId = ");
		sbuf.append(accountTypeID.toString());
		sbuf.append(" order by trxtype.sdescription");
		
		log.debug("about to run query: " + sbuf.toString());
				
		List<Tblaccttypetrxtype> rsAccTypeTrxType = this.getJdbcTemplate().query(sbuf.toString(), new TblaccttypetrxtypeRowMapper());			
									
		log.debug("final list size is = " + rsAccTypeTrxType.size());
		log.debug(methodName + "out");
		return rsAccTypeTrxType;	
	}
	
}
