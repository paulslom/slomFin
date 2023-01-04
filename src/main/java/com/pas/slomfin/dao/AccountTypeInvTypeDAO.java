package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tblaccttypeinvtype;
import com.pas.dbobjects.TblaccttypeinvtypeRowMapper;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		TransactionTypeDAO
 * Project: 	Slomkowski Financial Application
 * Description: Transaction Type DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class AccountTypeInvTypeDAO extends BaseDBDAO
{
    private static final AccountTypeInvTypeDAO currentInstance = new AccountTypeInvTypeDAO();

    private AccountTypeInvTypeDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     */
    public static AccountTypeInvTypeDAO getDAOInstance() 
    {
      	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
        return currentInstance;
    }
		
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<Tblaccttypeinvtype> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		Integer accountTypeID = (Integer)info;
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select accttypeinvtype.*, accttype.*, invtype.*, invtype.sdescription as investmentTypeDescription");
		sbuf.append("  from tblaccttypeinvtype accttypeinvtype, tblaccounttype accttype, tblinvestmenttype invtype");
		sbuf.append(" where accttypeinvtype.iAccountTypeID = accttype.iAccountTypeID");
		sbuf.append("  and accttypeinvtype.iInvestmentTypeID = invtype.iInvestmentTypeID");
		sbuf.append(" and accttype.iaccountTypeId = ");
		sbuf.append(accountTypeID.toString());
		sbuf.append(" order by invtype.sdescription");
									 
		log.debug("about to run query: " + sbuf.toString());
				
		List<Tblaccttypeinvtype> rsAccTypeInvType = this.getJdbcTemplate().query(sbuf.toString(), new TblaccttypeinvtypeRowMapper());			
									
		log.debug("final list size is = " + rsAccTypeInvType.size());
		log.debug(methodName + "out");
		return rsAccTypeInvType;	
	}
	
}
