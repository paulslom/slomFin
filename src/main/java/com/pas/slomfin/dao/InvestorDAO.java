package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblInvestorRowMapper;
import com.pas.dbobjects.Tblinvestor;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		InvestorDAO
 * Project: 	Slomkowski Financial Application
 * Description: Investor DAO extends BaseDBDAO. Implements the data access to the tblInvestor table
 * Copyright: 	Copyright (c) 2006
 */
public class InvestorDAO extends BaseDBDAO 
{

    private static final InvestorDAO currentInstance = new InvestorDAO();

    private InvestorDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return IVRMenuOptionCodeDAO
     */
    public static InvestorDAO getDAOInstance()
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
        
	@SuppressWarnings("unchecked")
	public List<Tblinvestor> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		
		log.debug("entering InvestorDAO inquire");			
		  	
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select inv.*, txg.staxgroupname, hardcashaccount.*, acctyp.*, brk.*, portf.*"); 
		sbuf.append(" from Tblinvestor inv"); 
		sbuf.append(" left join tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" left join tblaccount hardcashaccount on inv.ihardcashaccountid = hardcashaccount.iaccountid"); 
		sbuf.append(" left join tblportfolio portf on hardcashaccount.iPortfolioID = portf.iPortfolioID");
		sbuf.append(" left join tblaccounttype acctyp on hardcashaccount.iaccounttypeid = acctyp.iAccountTypeID");
		sbuf.append(" left join tblbroker brk on hardcashaccount.iBrokerID = brk.iBrokerID");
			
		Integer i = (Integer)Info;
		
		if (i != 0) //zero means give me all investors, non-zero is going after a particular one
		{	
		    log.debug("Investor object provided - will perform a single row retrieval from DB");			
			sbuf.append(" WHERE inv.iinvestorId = ");
			sbuf.append(i);
			log.debug(methodName + "before inquiring for Investor. Key value is = " + i);
		}		
		else			
		{
            //this is an inquire request for a list of the contents of the Investor table			
			log.debug("Will perform all rows retrieval from DB on Investor table");						
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblinvestor> investorList = this.getJdbcTemplate().query(sbuf.toString(), new TblInvestorRowMapper());			
		
		log.debug(methodName + "looping through recordset to build Investor Detail object(s)");
					
		log.debug("final list size is = " + investorList.size());
		log.debug(methodName + "out");
		return investorList;
	}
	
}
