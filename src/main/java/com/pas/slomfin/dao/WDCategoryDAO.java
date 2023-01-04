package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblWDCategoryRowMapper;
import com.pas.dbobjects.Tblwdcategory;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		WDCategoryDAO
 * Project: 	Slomkowski Financial Application
 * Description: WDCategory DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class WDCategoryDAO extends BaseDBDAO
{
    private static final WDCategoryDAO currentInstance = new WDCategoryDAO();

    private WDCategoryDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return WDCategoryDAO
     */
    public static WDCategoryDAO getDAOInstance()
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblwdcategory> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblwdcategory wdCategory = (Tblwdcategory)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblwdcategory (");
		sbuf.append("sWDCategoryDescription, iInvestorID");
		sbuf.append(") values(?,?)");	
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {wdCategory.getSwdcategoryDescription(), wdCategory.getiInvestorID()});			
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		    
	@SuppressWarnings("unchecked")
	public List<Tblwdcategory> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select wdcat.*, inv.*, txg.staxgroupname, hardcashaccount.*, acctyp.*, brk.*, portf.*");
		sbuf.append("  from Tblwdcategory wdcat");
		sbuf.append(" inner join Tblinvestor inv on wdcat.iinvestorid = inv.iinvestorid ");
		sbuf.append(" left join tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" left join tblaccount hardcashaccount on inv.ihardcashaccountid = hardcashaccount.iaccountid");
		sbuf.append(" left join tblportfolio portf on hardcashaccount.iPortfolioID = portf.iPortfolioID");
		sbuf.append(" left join tblaccounttype acctyp on hardcashaccount.iaccounttypeid = acctyp.iAccountTypeID");
		sbuf.append(" left join tblbroker brk on hardcashaccount.iBrokerID = brk.iBrokerID");
		
		if (Info instanceof Integer)
		{
			Integer wdCategoryID = (Integer)Info;
			
			//this is an inquire request for a particular row on the database
			
			sbuf.append(" where iwdcategoryId = ");
			sbuf.append(wdCategoryID.toString());
			
			log.debug(methodName + "before inquiring for WDCategory. Key value is = " + wdCategoryID.toString());
	
		}		
		else  //this is an inquire request for a list of WDCategorys - Investor object provided					
		{            	
			log.debug("Will perform multiple rows retrieval for WDCategorys");
			
			Integer investorID = Integer.valueOf (((Investor)Info).getInvestorID());
						
			sbuf.append(" where wdcat.iinvestorId = ");
			sbuf.append(investorID.toString());
					
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblwdcategory> wdCategoryList = this.getJdbcTemplate().query(sbuf.toString(), new TblWDCategoryRowMapper());
						
		log.debug("final list size is = " + wdCategoryList.size());
		log.debug(methodName + "out");
		return wdCategoryList;	
	}
	
	public List<Tblwdcategory> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering WDCategoryDAO update");
				
		Tblwdcategory wdCategory = (Tblwdcategory)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("UPDATE tblwdcategory set ");
		sbuf.append("sWDCategoryDescription = ?, iInvestorID = ?");
		sbuf.append(" where iwdcategoryid = ?");	
		
		log.info("about to run update.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {wdCategory.getSwdcategoryDescription(), wdCategory.getiInvestorID(), wdCategory.getIwdcategoryId()});			
	
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblwdcategory> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering WDCategoryDAO delete");
						
		Tblwdcategory wdCategory = (Tblwdcategory)Info;
		
		Integer id = wdCategory.getIwdcategoryId();
		
		log.info("about to delete id: " + id);
		
		String deleteStr = "delete from tblwdcategory where iwdcategoryid = ?";
		this.getJdbcTemplate().update(deleteStr, id);
		
		log.info("successfully deleted wdcategory id: " + id);
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
