package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblappsecurityRowMapper;
import com.pas.dbobjects.Tblappsecurity;
import com.pas.exception.DAOException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.util.Utils;
import com.pas.valueObject.User;

/**
 * Title: 		LoginDAO
 * Project: 	Slomkowski Financial Application
 * Description: Login DAO extends BaseDBDAO. Implements the data access to the tblSecurity table
 * Copyright: 	Copyright (c) 2006
 */
public class LoginDAO extends BaseDBDAO
{
    private static final LoginDAO currentInstance = new LoginDAO();
    
    public LoginDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return LoginDAO
     */
    public static LoginDAO getDAOInstance() 
    {
    	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));
        return currentInstance;
    }
        
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	public List<Tblappsecurity> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		log.debug("entering LoginDAO inquire");
		
		//execute a query against the security table - if successful then user is valid
		
		User user =  (User)info;
		
		StringBuffer sbuf = new StringBuffer();
					
		sbuf.append("select * from  Tblappsecurity WHERE ");
		sbuf.append(" userId = '");
		sbuf.append(user.getUserId());
		sbuf.append("' AND password = '"); 		
		sbuf.append(user.getPassword());
		sbuf.append("'");
		
		log.debug(methodName + "attempting to find a record in " + ISlomFinAppConstants.TABLENAME_APPSECURITY + " with user id = " + user.getUserId());
	   
		List<Tblappsecurity> securityList = this.getJdbcTemplate().query(sbuf.toString(), new TblappsecurityRowMapper());			
			
		log.debug("final list size is = " + securityList.size());
		log.debug(methodName + "out");
		
		return securityList;
	}

}
