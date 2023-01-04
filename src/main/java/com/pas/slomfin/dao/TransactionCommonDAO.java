package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblTransactionCommonRowMapper;
import com.pas.dbobjects.Tbltransactioncommon;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		TransactionCommonDAO
 * Project: 	Slomkowski Financial Application
 * Description: TransactionCommonDAO DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class TransactionCommonDAO extends BaseDBDAO
{
    private static final TransactionCommonDAO currentInstance = new TransactionCommonDAO();

    private TransactionCommonDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return TransactionCommonDAO
     */
    public static TransactionCommonDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	@SuppressWarnings("unchecked")
	public List<Tbltransactioncommon> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
				
		StringBuffer sbuf = new StringBuffer();
			
		sbuf.append("select trxc.*, acc.*, acctyp.*, trxt.*, inv.*, brk.*, portf.*, txg.*, trxt.sDescription as trxTypeDescription"); 
		sbuf.append(" from tbltransactioncommon trxc");
		sbuf.append(" inner join tblaccount acc on trxc.iTrxCommonAccountID = acc.iAccountID"); 
		sbuf.append(" inner join tblportfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append(" inner join tblaccounttype acctyp on acc.iAccountTypeID = acctyp.iAccountTypeID");   
		sbuf.append(" inner join tbltransactiontype trxt on trxc.iTrxCommonTrxTypeID = trxt.iTransactionTypeID");
		sbuf.append(" inner join tblinvestor inv on trxc.iTrxCommonInvestorID = inv.iInvestorID"); 
		sbuf.append(" left join tblbroker brk on acc.ibrokerid = brk.ibrokerid");
		sbuf.append(" left join tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
				
		if (info instanceof Investor)
		{
			//this is an inquire request for a particular row on the database			
			log.debug("Investor object provided to transactioncommondao");			
			sbuf.append(" where inv.iinvestorId = ");
			sbuf.append(((Investor)info).getInvestorID());
			log.debug(methodName + "before inquiring for TransactionCommon. Key value is = " + (((Investor)info).getInvestorID()));
		}		
		else			
		{
            //this is an inquire request for a list of the contents of the Investor table			
			log.debug("TransactionCommon object provided to transactioncommonDao");	
			Tbltransactioncommon trxCommon = (Tbltransactioncommon)info;
			sbuf.append(" where trxc.itrxCommonId = ");
			sbuf.append(trxCommon.getItrxCommonId());
			log.debug(methodName + "before inquiring for TransactionCommon. Key value is = " + trxCommon.getItrxCommonId());	
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tbltransactioncommon> transactionCommonList = this.getJdbcTemplate().query(sbuf.toString(), new TblTransactionCommonRowMapper());			 
					
		log.debug("final list size is = " + transactionCommonList.size());
		log.debug(methodName + "out");
		return transactionCommonList;	
	}

}
