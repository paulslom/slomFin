package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblJobRowMapper;
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tbljob;
import com.pas.exception.DAOException;
import com.pas.util.Utils;

/**
 * Title: 		JobDAO
 * Project: 	Slomkowski Financial Application
 * Description: Job DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class JobDAO extends BaseDBDAO
{
    private static final JobDAO currentInstance = new JobDAO();

    private JobDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return JobDAO
     */
    public static JobDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
    	return currentInstance;
    }
    
	public List<Tbljob> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tbljob job = (Tbljob)Info;
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("INSERT INTO tbljob (");
		sbuf.append("iInvestorID, sEmployer, iPaydaysPerYear, dJobStartDate, dJobEndDate, mGrossPay, mFederalWithholding,");
		sbuf.append("mStateWithholding, mRetirementDeferred, mSSWithholding, mDental, mMedical, mGroupLifeInsurance, sIncomeState,");
		sbuf.append("mGroupLifeIncome, mVision, mParking, mCafeteria, mRoth401k");
		sbuf.append(") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");			
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {job.getIinvestorId(), job.getSemployer(), job.getIpaydaysPerYear(), job.getDjobStartDate(),
				job.getDjobEndDate(), job.getMgrossPay(), job.getMfederalWithholding(), job.getMstateWithholding(), job.getMretirementDeferred(), job.getMsswithholding(),
				job.getMdental(), job.getMmedical(), job.getMgroupLifeInsurance(), job.getSincomeState(), job.getMgroupLifeIncome(), job.getMvision(),
				job.getMparking(), job.getMcafeteria(), job.getMroth401k()});			
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings({ "unchecked" })
	public List<Tbljob> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
					
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select job.*, inv.*, txg.*, acc.*, acctyp.*, brk.*, portf.*");
		sbuf.append(" from tbljob job");
		sbuf.append(" inner join tblinvestor inv on job.iinvestorid = inv.iinvestorid");
		sbuf.append(" left join tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" left join tblaccount acc on inv.ihardcashaccountid = acc.iaccountid");
		sbuf.append(" left join tblportfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append(" left join tblaccounttype acctyp on acc.iAccountTypeID = acctyp.iAccountTypeID");
		sbuf.append(" left join tblbroker brk on acc.ibrokerid = brk.ibrokerid");
		
		if (Info instanceof Integer)
		{
			Integer jobID = (Integer)Info;
			
			//this is an inquire request for a particular row on the database
			
			log.debug("Job object provided - will perform a single row retrieval from DB");			
						
			sbuf.append(" where job.ijobId = ");
			sbuf.append(jobID.toString());
			
			log.debug(methodName + "before inquiring for a particular Job. Job ID is = " + jobID.toString());
	
		}		
		else  //this is an inquire request for a list of Jobs - Investor object provided					
		{            	
			log.debug("Will perform multiple rows retrieval for Jobs");
			
			Tblinvestor tblinvestor = (Tblinvestor)Info;
			Integer investorID = tblinvestor.getIinvestorId();
						
			sbuf.append(" where inv.iinvestorId = ");
			sbuf.append(investorID.toString());
			
			log.debug(methodName + "before inquiring for jobs for a particular investor. Investor ID is = " + investorID.toString());					
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbljob> jobList = this.getJdbcTemplate().query(sbuf.toString(), new TblJobRowMapper());
									
		log.debug("final list size is = " + jobList.size());
		log.debug(methodName + "out");
		return jobList;	
	}
	
	public List<Tbljob> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");		
							
		Tbljob job = (Tbljob)Info;
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("UPDATE set tbljob ");
		sbuf.append("iInvestorID = ?, sEmployer = ?, iPaydaysPerYear = ?, dJobStartDate = ?, dJobEndDate = ?, mGrossPay = ?, mFederalWithholding = ?,");
		sbuf.append("mStateWithholding = ?, mRetirementDeferred = ?, mSSWithholding = ?, mDental = ?, mMedical = ?, mGroupLifeInsurance = ?, sIncomeState = ?,");
		sbuf.append("mGroupLifeIncome = ?, mVision = ?, mParking = ?, mCafeteria = ?, mRoth401k = ?");
		sbuf.append(" where ijobid = ?");			
		
		log.info("about to run update.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {job.getIinvestorId(), job.getSemployer(), job.getIpaydaysPerYear(), job.getDjobStartDate(),
				job.getDjobEndDate(), job.getMgrossPay(), job.getMfederalWithholding(), job.getMstateWithholding(), job.getMretirementDeferred(), job.getMsswithholding(),
				job.getMdental(), job.getMmedical(), job.getMgroupLifeInsurance(), job.getSincomeState(), job.getMgroupLifeIncome(), job.getMvision(),
				job.getMparking(), job.getMcafeteria(), job.getMroth401k(), job.getIjobId()});			
		
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tbljob> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");		
						
		Tbljob job = (Tbljob)Info;
		
		Integer jobID = job.getIjobId();
		
		log.info("about to delete job id: " + jobID);
		
		String deleteStr = "delete from tbljob where ijobid = ?";
		this.getJdbcTemplate().update(deleteStr, jobID);
		
		log.info("successfully deleted job id: " + jobID);
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
