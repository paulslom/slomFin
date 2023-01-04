package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblPaydayRowMapper;
import com.pas.dbobjects.Tblpayday;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		PaycheckOutflowDAO
 * Project: 	Slomkowski Financial Application
 * Description: PaycheckOutflow DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class PaycheckOutflowDAO extends BaseDBDAO
{
    private static final PaycheckOutflowDAO currentInstance = new PaycheckOutflowDAO();

    private PaycheckOutflowDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return PaycheckOutflowDAO
     */
    public static PaycheckOutflowDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblpayday> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering PaycheckOutflowDAO add");
			
		Tblpayday paycheckOutflow = (Tblpayday)Info;		
	     
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblpayday (");
		sbuf.append("itransactionTypeId, iinvestorId, ixferAccountId, iaccountId, iwdcategoryId,");
		sbuf.append("icashdepositTypeId, mdefaultAmt, idefaultDay, bnextMonthInd, sdescription");
		sbuf.append(") values(?,?,?,?,?,?,?,?,?,?)");	
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {paycheckOutflow.getItransactionTypeId(), paycheckOutflow.getIinvestorId(), paycheckOutflow.getIxferAccountId(),
				paycheckOutflow.getIaccountId(), paycheckOutflow.getIwdcategoryId(), paycheckOutflow.getIcashdepositTypeId(), paycheckOutflow.getMdefaultAmt(),
				paycheckOutflow.getIdefaultDay(), paycheckOutflow.getBnextMonthInd(), paycheckOutflow.getSdescription()});	
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
	    
	@SuppressWarnings("unchecked")
	public List<Tblpayday> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select pd.*, acc.*, trxtyp.*, inv.*, wdcat.*, cdeptyp.*, acctyp.*, xferacctyp.*, brk.*, portf.*, txg.*,");
		sbuf.append(" pd.sDescription as paydayDescription, trxtyp.sDescription as trxTypeDescription,"); 
		sbuf.append(" xferacct.IaccountId as xferiaccountID, xferacct.saccountName as xfersaccountName, xferacct.saccountNameAbbr as xfersaccountNameAbbr");
		sbuf.append(" from tblpayday pd");
		sbuf.append(" inner join tblaccount acc on pd.iAccountID = acc.iAccountID");
		sbuf.append(" inner join tblportfolio portf on acc.iPortfolioID = portf.iPortfolioID");
		sbuf.append(" inner join tblaccounttype acctyp on acc.iAccountTypeID = acctyp.iAccountTypeID");
		sbuf.append(" inner join tbltransactiontype trxtyp on pd.itransactiontypeID = trxtyp.itransactiontypeID");
		sbuf.append(" inner join tblinvestor inv on pd.iinvestorID = inv.iInvestorID");
		sbuf.append(" inner join tbltaxgroup txg on inv.iTaxGroupID = txg.iTaxGroupID");
		sbuf.append(" left join tblaccount xferacct on pd.ixferAccountID = xferacct.iAccountID");
		sbuf.append(" left join tblbroker brk on acc.ibrokerid = brk.ibrokerid");
		sbuf.append(" left join tblaccounttype xferacctyp on xferacct.iAccountTypeID = xferacctyp.iAccountTypeID");
		sbuf.append(" left join tblwdcategory wdcat on pd.iwdCategoryID = wdcat.iwdCategoryID");
		sbuf.append(" left join tblcashdeposittype cdeptyp on pd.icashdeposittypeID = cdeptyp.icashdeposittypeID");
				
		if (Info instanceof Integer)
		{
			Integer paycheckOutflowID = (Integer)Info;
			
			//this is an inquire request for a particular row on the database
			
			log.debug("Tblpayday object provided - will perform a single row retrieval from DB");			
						
			sbuf.append(" where pd.ipaydayId = ");
			sbuf.append(paycheckOutflowID.toString());
			
			log.debug(methodName + "before inquiring for a particular paycheck outflow row. Payday ID is = " + paycheckOutflowID.toString());
	
		}		
		else  //this is an inquire request for a list of Paycheck Outflows - Investor object provided					
		{            	
			log.debug("Will perform multiple rows retrieval for paydays");
			
			Integer investorID = Integer.valueOf (((Investor)Info).getInvestorID());
						
			sbuf.append(" where pd.iinvestorId = ");
			sbuf.append(investorID.toString());
			sbuf.append(" order by pd.bnextmonthind");
			
			log.debug(methodName + "before inquiring for jobs for a particular investor. Investor ID is = " + investorID.toString());				
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblpayday> pcoList = this.getJdbcTemplate().query(sbuf.toString(), new TblPaydayRowMapper());	
		
		log.debug("final list size is = " + pcoList.size());
		log.debug(methodName + "out");
		return pcoList;	
	}
	
	public List<Tblpayday> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering PaycheckOutflowDAO update");
				
		Tblpayday paycheckOutflow = (Tblpayday)Info;
		
		//the following are nullable; and if we have an integer value of zero we need to null these out
		
		if (paycheckOutflow.getIcashdepositTypeId() == 0)
		{
			paycheckOutflow.setIcashdepositTypeId(null);
		}
		
		if (paycheckOutflow.getIwdcategoryId() == 0)
		{
			paycheckOutflow.setIwdcategoryId(null);
		}
		if (paycheckOutflow.getIxferAccountId() == 0)
		{
			paycheckOutflow.setIxferAccountId(null);
		}
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("UPDATE tblpayday set itransactionTypeId = ?, iinvestorId = ?, ixferAccountId = ?, iaccountId = ?, iwdcategoryId = ?,");
		sbuf.append("icashdepositTypeId = ?, mdefaultAmt = ?, idefaultDay = ?, bnextMonthInd = ?, sdescription = ?");
		sbuf.append(" where ipaydayID = ?");	
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {paycheckOutflow.getItransactionTypeId(), paycheckOutflow.getIinvestorId(), paycheckOutflow.getIxferAccountId(),
				paycheckOutflow.getIaccountId(), paycheckOutflow.getIwdcategoryId(), paycheckOutflow.getIcashdepositTypeId(), paycheckOutflow.getMdefaultAmt(),
				paycheckOutflow.getIdefaultDay(), paycheckOutflow.getBnextMonthInd(), paycheckOutflow.getSdescription(), paycheckOutflow.getIpaydayId()});	
		
		log.debug(methodName + "out");
						
		//no need to pass back a list on an update
		return null;	
	}
    
	public List<Tblpayday> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering PaycheckOutflowDAO delete");
		
		Tblpayday paycheckOutflow = (Tblpayday)Info;
		
		Integer pcoID = paycheckOutflow.getIpaydayId();
		
		log.info("about to delete payday (paycheckoutflow) id: " + pcoID);
		
		String deleteStr = "delete from tblpayday where ipaydayid = ?";
		this.getJdbcTemplate().update(deleteStr, pcoID);
				
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
