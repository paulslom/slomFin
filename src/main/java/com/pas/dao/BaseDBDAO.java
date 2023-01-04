package com.pas.dao; 

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.pas.exception.DAOException;
import com.pas.exception.PASSystemException;
import com.pas.util.IPropertyReader;

/**
 * Title: 		BaseDBDAO
 * Project: 	SlomFin
 * 
 * Description: This is the base class for the data access layer. All DAO
 * concrete classes need to extend this class. 
 *  
 */
public class BaseDBDAO implements IDBDAO
{
    // reference to Logger
	protected static Logger log = LogManager.getLogger(BaseDBDAO.class);  
      
    protected static IPropertyReader pr;
    
    private MysqlDataSource dataSource = null;
    private JdbcTemplate jdbcTemplate = null;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
    /**
     * This implementation throws a SystemException
     */
    @SuppressWarnings("unchecked")
	public List add(Object valueObject) throws DAOException, PASSystemException
    {
        log.error("add:: Not implemented");
        throw new PASSystemException("add not implemented");
    }

    /**
     * This implementation throws a SystemException
     */
    @SuppressWarnings("unchecked")
	public List update(Object valueObject) throws DAOException, PASSystemException
    {
        log.error("update(Object):: Not implemented");
        throw new PASSystemException("update not implemented");
    }   

    /**
     * This implementation throws a SystemException
     */
    @SuppressWarnings("unchecked")
	public List delete(Object deleteCriteria) throws DAOException, PASSystemException
    {
        log.error("delete(Object):: Not implemented");
        throw new PASSystemException("delete not implemented");
    }   

    /**
     * This implementation throws a SystemException
     */
    @SuppressWarnings("unchecked")
	public List inquire(Object searchCriteria) throws DAOException, PASSystemException {
        log.error("inquire(Object):: Not implemented");
        throw new PASSystemException("inquire not implemented");
    }

	public MysqlDataSource getDatasource() 
	{
		return dataSource;
	}

	public void setDataSource(MysqlDataSource dataSource) 
	{
		this.dataSource = dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
}
