package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblPaydayHistoryRowMapper;
import com.pas.dbobjects.Tbljob;
import com.pas.dbobjects.Tblpaydayhistory;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		PaycheckHistoryDAO
 * Project: 	Slomkowski Financial Application
 * Description: PaycheckHistory DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class PaycheckHistoryDAO extends BaseDBDAO
{
    private static final PaycheckHistoryDAO currentInstance = new PaycheckHistoryDAO();

    private PaycheckHistoryDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return PaycheckHistoryDAO
     */
    public static PaycheckHistoryDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblpaydayhistory> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering PaycheckHistoryDAO add");
		
		Tblpaydayhistory paycheckHistory = (Tblpaydayhistory)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblpaydayhistory (");
		sbuf.append("ijobId, ipaychecktypeid, dpaydayHistoryDate, mgrossPay, mfederalWithholding, mstateWithholding, mretirementDeferred, msswithholding,");
		sbuf.append("mmedicareWithholding, mfsaAmount, mdental, mmedical, mgroupLifeInsurance, mgroupLifeIncome,");
		sbuf.append("mvision, mparking, mcafeteria, mroth401k");
		sbuf.append(") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");			
		
		int insertedRows = this.getJdbcTemplate().update(sbuf.toString(), new Object[] {paycheckHistory.getIjobId(), paycheckHistory.getIpaychecktypeid(), paycheckHistory.getDpaydayHistoryDate(), paycheckHistory.getMgrossPay(),
				paycheckHistory.getMfederalWithholding(), paycheckHistory.getMstateWithholding(), paycheckHistory.getMretirementDeferred(), paycheckHistory.getMsswithholding(),
				paycheckHistory.getMmedicareWithholding(), paycheckHistory.getMfsaAmount(), paycheckHistory.getMdental(), paycheckHistory.getMmedical(),
				paycheckHistory.getMgroupLifeInsurance(), paycheckHistory.getMgroupLifeIncome(), paycheckHistory.getMvision(), paycheckHistory.getMparking(),
				paycheckHistory.getMcafeteria(), paycheckHistory.getMroth401k()});	
		
		log.debug("paycheck history rows inserted = " + insertedRows);
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings("unchecked")
	public List<Tblpaydayhistory> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select pdh.*, jb.*, pchtyp.*, inv.*, txg.staxgroupname, hardcashaccount.*, acctyp.*, brk.*, portf.*");
		sbuf.append("  from Tblpaydayhistory pdh");
		sbuf.append(" inner join tbljob jb on pdh.ijobid = jb.ijobid");
		sbuf.append(" inner join tblpaychecktype pchtyp on pdh.ipaychecktypeid = pchtyp.ipaychecktypeid"); 
		sbuf.append(" inner join Tblinvestor inv on jb.iinvestorid = inv.iinvestorid");
		sbuf.append(" left join tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" left join tblaccount hardcashaccount on inv.ihardcashaccountid = hardcashaccount.iaccountid");
		sbuf.append(" inner join tblportfolio portf on hardcashaccount.iPortfolioID = portf.iPortfolioID");
		sbuf.append(" left join tblaccounttype acctyp on hardcashaccount.iaccounttypeid = acctyp.iAccountTypeID");
		sbuf.append(" left join tblbroker brk on hardcashaccount.iBrokerID = brk.iBrokerID");		 
		
		if (Info instanceof Integer)
		{
			Integer paycheckHistoryID = (Integer)Info;
			
			//this is an inquire request for a particular row on the database
			
			sbuf.append(" where pdh.ipaydayhistoryId = ");
			sbuf.append(paycheckHistoryID.toString());
			
			log.debug(methodName + "before inquiring for a particular paycheck outflow row. PaydayHistoryID is = " + paycheckHistoryID.toString());	
		}		
		else  //this is an inquire request for a list of Paychecks for a particular job				
		{            	
			log.debug("Will perform multiple rows retrieval for PaycheckHistory");
			
			Integer jobID = Integer.valueOf (((Tbljob)Info).getIjobId());
			
			sbuf.append(" where pdh.ijobId = ");
			sbuf.append(jobID.toString());
			sbuf.append(" order by dpaydayHistoryDate desc");
		
			log.debug(methodName + "before inquiring for paychecks for a particular job. Job ID is = " + jobID.toString());				
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblpaydayhistory> paycheckHistoryList = this.getJdbcTemplate().query(sbuf.toString(), new TblPaydayHistoryRowMapper())	;
						
		log.debug("final list size is = " + paycheckHistoryList.size());
		log.debug(methodName + "out");
		return paycheckHistoryList;	
	}
	
	public List<Tblpaydayhistory> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering PaycheckHistoryDAO update");
					
		Tblpaydayhistory paycheckHistory = (Tblpaydayhistory)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("update tblpaydayhistory set ");
		sbuf.append("ijobId = ?, dpaydayHistoryDate = ?, mgrossPay = ?, mfederalWithholding = ?, mstateWithholding = ?, mretirementDeferred = ?, msswithholding = ?,");
		sbuf.append("mmedicareWithholding = ?, mfsaAmount = ?, mdental = ?, mmedical = ?, mgroupLifeInsurance = ?, mgroupLifeIncome = ?,");
		sbuf.append("mvision = ?, mparking = ?, mcafeteria = ?, mroth401k = ?");
		sbuf.append(" where ipaydayhistoryid = ?");			
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {paycheckHistory.getIjobId(), paycheckHistory.getDpaydayHistoryDate(), paycheckHistory.getMgrossPay(),
				paycheckHistory.getMfederalWithholding(), paycheckHistory.getMstateWithholding(), paycheckHistory.getMretirementDeferred(), paycheckHistory.getMsswithholding(),
				paycheckHistory.getMmedicareWithholding(), paycheckHistory.getMfsaAmount(), paycheckHistory.getMdental(), paycheckHistory.getMmedical(),
				paycheckHistory.getMgroupLifeInsurance(), paycheckHistory.getMgroupLifeIncome(), paycheckHistory.getMvision(), paycheckHistory.getMparking(),
				paycheckHistory.getMcafeteria(), paycheckHistory.getMroth401k(), paycheckHistory.getIpaydayHistoryId()});	
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblpaydayhistory> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering PaycheckHistoryDAO delete");
				
		Tblpaydayhistory paycheckHistory = (Tblpaydayhistory)Info;
		
		Integer pdhID = paycheckHistory.getIpaydayHistoryId();
		
		log.info("about to delete payday history id: " + pdhID);
		
		String deleteStr = "delete from tblpaydayhistory where ipaydayhistoryid = ?";
		this.getJdbcTemplate().update(deleteStr, pdhID);
		
		log.info("successfully deleted paydayhistory id: " + pdhID);
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
