package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblAccountRowMapper;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblinvestment;
import com.pas.dbobjects.Tblinvestmenttype;
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.AccountMargin;
import com.pas.slomfin.valueObject.AccountSelection;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;
import com.pas.valueObject.DropDownBean;

/**
 * Title: 		AccountDAO
 * Project: 	Slomkowski Financial Application
 * Description: Investor DAO extends BaseDBDAO. Implements the data access to the tblAccount table
 * Copyright: 	Copyright (c) 2006
 */
public class AccountDAO extends BaseDBDAO
{
    private static final AccountDAO currentInstance = new AccountDAO();

    private AccountDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return AccountDAO
     */
    public static AccountDAO getDAOInstance()
    {
      	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
        return currentInstance;
    }	
        
    @SuppressWarnings("unchecked")
	public List<DropDownBean> accountPositions(Integer accountID, String investmentType) throws DAOException
    {
    	final String methodName = "accountPositions::";
		log.debug(methodName + "in");
		
		List<DropDownBean> ddList = new ArrayList<DropDownBean>(); 
    	
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select trx.iaccountId as accountID, trx.iinvestmentId as investmentID,"); 
		sbuf.append(" invm.sdescription as description, tblinvtype.sdescription as invType,");
		sbuf.append(" SUM(CASE trxtyp.bpositiveInd WHEN 0 THEN -trx.decUnits WHEN 1 THEN trx.decUnits ELSE 0 END) as unitsOwned"); 
		sbuf.append(" from Tbltransaction trx, Tblinvestment invm, TblTransactionType trxtyp, TblInvestmentType tblinvtype");
		sbuf.append(" where trx.iinvestmentID = invm.iinvestmentID");
		sbuf.append(" and trx.itransactiontypeid = trxtyp.itransactiontypeid");
		sbuf.append(" and invm.iinvestmenttypeid = tblinvtype.iinvestmenttypeid");
		sbuf.append(" and trx.iaccountId = "); 
		sbuf.append(accountID.toString());
		sbuf.append(" and tblinvtype.sdescription = '");
		sbuf.append(investmentType);
		sbuf.append("' group by trx.iaccountId, trx.iinvestmentId, invm.sdescription, tblinvtype.sdescription"); 
		sbuf.append(" having SUM(CASE trxtyp.bpositiveInd WHEN 0 THEN -trx.decUnits WHEN 1 THEN trx.decUnits ELSE 0 END) <> 0");
		
		log.debug("Account Positions SQL to execute is: " + sbuf.toString());
				 
		List<Tbltransaction> positionsList = this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List<Tbltransaction>>() 
		{	   
			@Override
		    public List<Tbltransaction> extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List<Tbltransaction> tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					Tbltransaction trx = new Tbltransaction();
					trx.setIaccountID(rs.getInt("accountId"));
					trx.setIinvestmentID(rs.getInt("investmentID"));
					trx.setSdescription(rs.getString("description"));
					Tblinvestment inv = new Tblinvestment();
					Tblinvestmenttype invType = new Tblinvestmenttype();
					invType.setSdescription(rs.getString("invType"));
					inv.setTblinvestmenttype(invType);
					trx.setTblinvestment(inv);
					trx.setDecUnits(rs.getBigDecimal("unitsOwned"));
			        tempList.add(trx);			        
				}
				return tempList;
		    }
		});
		
		log.debug(methodName + "looping to build Account Position dropdown object(s)");
		
		for (int i = 0; i < positionsList.size(); i++)
		{
			Tbltransaction trx = positionsList.get(i);			
			DropDownBean ddBean = new DropDownBean();			
	        ddBean.setId(trx.getIinvestmentID().toString());
			ddBean.setDescription(trx.getSdescription());
			ddList.add(ddBean);      
		}
									
		log.debug("final list size is = " + ddList.size());
		log.debug(methodName + "out");
		return ddList;	
		
    }
	
    public List<Tblaccount> add(Object Info) throws DAOException
	{       
        final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblaccount account = (Tblaccount)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblaccount (");
		sbuf.append("iPortfolioID, iAccountTypeID, iBrokerID, sAccountName, sAccountNameAbbr, iStartingCheckNo,");
		sbuf.append("sAccountNumber, sPIN, bClosed, iInterestPaymentsPerYear, dInterestRate, mMinInterestBalance,");
		sbuf.append("bTaxableInd, mNewMoneyPerYear, dEstimatedRateofReturn, dVestingPercentage");
		sbuf.append(") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");	
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {account.getIportfolioId(), account.getIaccountTypeId(), account.getIbrokerId(), account.getSaccountName(),
				account.getSaccountNameAbbr(), account.getIstartingCheckNo(), account.getSaccountNumber(), account.getSpin(), account.isBclosed(), account.getIinterestPaymentsPerYear(),
				account.getDinterestRate(), account.getMminInterestBalance(), account.isBtaxableInd(), account.getMnewMoneyPerYear(),
				account.getDestimatedRateofReturn(), account.getDvestingPercentage()});			
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	

	}
	 
    public List<Tblaccount> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		Tblaccount account = (Tblaccount)Info;
		
		Integer acctID = account.getIaccountId();
		
		log.info("about to delete account id: " + acctID);
		
		String deleteStr = "delete from tblaccount where iaccountid = ?";
		this.getJdbcTemplate().update(deleteStr, acctID);
		
		log.info("successfully deleted account id: " + acctID);
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	

	}
    
    @SuppressWarnings("unchecked")
	public BigDecimal getAccountBalance(Integer accountID) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
	
		BigDecimal acctBalance = new BigDecimal(0.0);
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select sum(meffectiveAmount) from Tbltransaction");
		sbuf.append(" where iaccountId = ?");
			
		log.debug(methodName + "before inquiring for Account Balance. Key value is = " + accountID.toString());
	
		acctBalance = this.getJdbcTemplate().queryForObject(sbuf.toString(), new Object[] {accountID}, BigDecimal.class); 	
		
		log.debug(methodName + "out");
		
		return acctBalance;	
	}
    
    @SuppressWarnings("unchecked")
    public List<AccountMargin> getAccountsOnMargin(Integer taxGroupID) throws DAOException
    {
    	final String methodName = "getAccountsOnMargin::";
		log.debug(methodName + "in");
		
		List<AccountMargin> marginList = new ArrayList<AccountMargin>();
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("SELECT TX.tblaccount.saccountName as AccountName,");
		sbuf.append(" SUM(TX.meffectiveAmount) as accountBalance,");   
		sbuf.append(" TX.tblaccount.tblbroker.dmarginInterestRate");    
		sbuf.append(" FROM Tbltransaction TX"); 
		sbuf.append(" WHERE TX.tblaccount.tblaccounttype.saccountType = 'Taxable Brokerage'"); 
		sbuf.append(" AND TX.tblaccount.tblportfolio.tblinvestor.tbltaxgroup.itaxGroupId = ");
		sbuf.append(taxGroupID);
		sbuf.append(" GROUP by TX.tblaccount.iaccountId"); 
		
		log.debug("about to run query: " + sbuf.toString());
		
		List accountList = new ArrayList();		
			
		for (int i = 0; i < accountList.size(); i++)
		{
			Object[] marginObject = (Object[])accountList.get(i);
			
			BigDecimal acctBal = (BigDecimal)marginObject[1];
			
			if (acctBal.compareTo(new BigDecimal(0.0)) < 0)
			{
				AccountMargin acctMargin = new AccountMargin();
				acctMargin.setAccountBalance(acctBal);
				acctMargin.setAccountName((String)marginObject[0]);
				acctMargin.setMarginInterestRate((BigDecimal)marginObject[2]);
				marginList.add(acctMargin);
			}
		}
		return marginList;
    }
    
    @SuppressWarnings("unchecked")
	public List<DropDownBean> getOpenTaxableAccounts(Integer investorID) throws DAOException
    {
    	final String methodName = "getOpenTaxableAccounts::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select * from Tblaccount acc");
		sbuf.append(" where acc.tblportfolio.tblinvestor.iinvestorId = ");
		sbuf.append(investorID.toString());
		sbuf.append(" and acc.bclosed = false");
		sbuf.append(" and acc.tblportfolio.btaxableInd = true");  
			
		log.debug(methodName + "before inquiring for open taxable accounts.  Investor ID is = " + investorID.toString());
	
		List<Tblaccount> accountList = new ArrayList();		
				 
		List<DropDownBean> ddList = new ArrayList<DropDownBean>(); 
        		
		log.debug(methodName + "looping through recordset to build open-Taxable-Account dropdown object(s)");
				
		for (int i = 0; i < accountList.size(); i++)
		{
			Tblaccount acct = accountList.get(i);
			
			DropDownBean ddBean = new DropDownBean();
	                        
			String ddId = String.valueOf(acct.getIaccountId());
	                        
			if (ddId != null && !ddId.equals(""))
			{
				ddId = ddId.trim();
			}
			ddBean.setId(ddId);
			ddBean.setDescription(acct.getSaccountName());
		    ddList.add(ddBean);	
		    
		}				
		log.debug("final list size is = " + ddList.size());
		log.debug(methodName + "out");
		return ddList;	
		
    }
    
    @SuppressWarnings("unchecked")
	public List<DropDownBean> getAllAccountsByInvestor(Integer investorID) throws DAOException
    {
    	final String methodName = "getAllAccountsByInvestor::";
		log.debug(methodName + "in");
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select acc.*, acctyp.*, brk.*, portf.*"); 
		sbuf.append("   from tblaccount acc");
		sbuf.append("   inner join tblaccounttype acctyp on acc.iAccountTypeID = acctyp.iAccountTypeID"); 
		sbuf.append("   inner join tblportfolio portf on acc.iPortfolioID = portf.iPortfolioID"); 
		sbuf.append("   left join tblbroker brk on acc.iBrokerID = brk.iBrokerID ");
		sbuf.append(" and portf.iinvestorId = ");
		sbuf.append(investorID.toString());
			
		log.debug(methodName + "before inquiring for all accounts.  Investor ID is = " + investorID.toString());
	
		List<Tblaccount> accountList = this.getJdbcTemplate().query(sbuf.toString(), new TblAccountRowMapper());
				 
		List<DropDownBean> ddList = new ArrayList<DropDownBean>(); 
        		
		log.debug(methodName + "looping through recordset to build Account dropdown object(s)");
				
		for (int i = 0; i < accountList.size(); i++)
		{
			Tblaccount acct = accountList.get(i);
			
			DropDownBean ddBean = new DropDownBean();
	                        
			String ddId = String.valueOf(acct.getIaccountId());
	                        
			if (ddId != null && !ddId.equals(""))
			{
				ddId = ddId.trim();
			}
			ddBean.setId(ddId);
			ddBean.setDescription(acct.getSaccountName());
		    ddList.add(ddBean);	
		    
		}				
		log.debug("final list size is = " + ddList.size());
		log.debug(methodName + "out");
		return ddList;	
		
    }
    @SuppressWarnings("unchecked")
	public List<DropDownBean> getXferAccounts(Integer accountID) throws DAOException
    {
    	final String methodName = "getXferAccounts::";
		log.debug(methodName + "in");
		
		List<DropDownBean> ddList = new ArrayList<DropDownBean>(); 
     			
		//first thing to do is determine what portfolio this account belongs to
		log.debug("about to determine which portfolio account " + accountID.toString() + " belongs to");
		
		StringBuffer sbuf1 = new StringBuffer();
		sbuf1.append("select iportfolioID from Tblaccount");
		sbuf1.append(" where iaccountId = ");
		sbuf1.append(accountID.toString());
		
		Integer portfolioID = this.getJdbcTemplate().queryForObject(sbuf1.toString(), Integer.class); 	
		
		log.debug(methodName + "determined this account belongs to portfolio " + portfolioID.toString());
		
		StringBuffer sbuf2 = new StringBuffer();
		sbuf2.append("select iaccountid, saccountname from Tblaccount");
		sbuf2.append(" where iportfolioId = ");
		sbuf2.append(portfolioID.toString());
		sbuf2.append(" and bclosed = false ");
		sbuf2.append(" and iaccountId <> ");
		sbuf2.append(accountID.toString());
		
		List<Tblaccount> acctList2 = this.getJdbcTemplate().query(sbuf2.toString(), new ResultSetExtractor<List<Tblaccount>>() 
		{	   
			@Override
		    public List<Tblaccount> extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List<Tblaccount> tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					Tblaccount acc = new Tblaccount();
					acc.setIaccountId(rs.getInt("iaccountId"));
					acc.setSaccountName(rs.getString("saccountName"));
			        tempList.add(acc);			        
				}
				return tempList;
		    }
		});
				
		log.debug(methodName + "looping through list of accounts to build XferAccount dropdown object(s)");
				
		for (int i = 0; i < acctList2.size(); i++)
		{	
			Tblaccount tblacc = acctList2.get(i);
		
			DropDownBean ddBean = new DropDownBean();
	                        
			String ddId = String.valueOf(tblacc.getIaccountId());
	                        
			if (ddId != null && !ddId.equals(""))
			{
				ddId = ddId.trim();
			}
			ddBean.setId(ddId);
			ddBean.setDescription(tblacc.getSaccountName());
			ddList.add(ddBean);					    
		}
		
		log.debug("final list size is = " + ddList.size());
		log.debug(methodName + "out");
		return ddList;	
		
    }
	@SuppressWarnings("unchecked")
	public List<Tblaccount> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
						
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select acc.*, acctyp.*, brk.*, portf.*"); 
		sbuf.append("   from tblaccount acc");
		sbuf.append("   inner join tblaccounttype acctyp on acc.iAccountTypeID = acctyp.iAccountTypeID"); 
		sbuf.append("   inner join tblportfolio portf on acc.iPortfolioID = portf.iPortfolioID"); 
		sbuf.append("   left join tblbroker brk on acc.iBrokerID = brk.iBrokerID ");
		
		if (Info instanceof Integer)
		{
			Integer accountID = (Integer)Info;
						
			//this is an inquire request for a particular row on the database
			
			log.debug("Integer object provided - will perform a single row retrieval from DB");			
			
			sbuf.append(" where iaccountId = ");
			sbuf.append(accountID.toString());
			
			log.debug(methodName + "before inquiring for Account. Key value is = " + accountID.toString());
	
		}		
		else if (Info instanceof Investor) //this is a request for a list of Open Accounts by investor - Investor object provided		
		{
			log.debug("Will perform multiple rows retrieval for Open Accounts by Investor");
			
			Integer investorID = Integer.valueOf (((Investor)Info).getInvestorID());			
			
			sbuf.append(" where portf.iinvestorId = ");
			sbuf.append(investorID.toString());	
			sbuf.append(" and bclosed = false");
				
		}
		else //means we have an AccountSelection object - this is an inquire request for a list of Accounts open or closed by Portfolio
		{	
			log.debug("Will perform multiple rows retrieval for Accounts");	
			
			AccountSelection accSel = (AccountSelection)Info;
			Integer portfolioID = accSel.getPortfolioID();
			Integer investorID = accSel.getInvestorID();
			
			if (investorID != null)
			{
				sbuf.append(" where portf.iinvestorId = ");
				sbuf.append(investorID.toString());	
				sbuf.append(" order by portf.iportfolioid, acc.sAccountName");
			}
			else
			{
				Integer openOrClosedInt = null;
				boolean openOrClosedBln = ((AccountSelection)Info).isClosed();
				if (openOrClosedBln)
				{
					openOrClosedInt = Integer.valueOf (1); //One means Closed
				}
				else
				{
					openOrClosedInt = Integer.valueOf (0); //zero means Open
				}
				
				sbuf.append(" where portf.iportfolioId = ");
				sbuf.append(portfolioID.toString());
				sbuf.append(" and acc.bclosed = ");
				sbuf.append(openOrClosedInt.toString());
				sbuf.append(" order by acc.sAccountName");
			}				
		}				
			
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblaccount> accountList = this.getJdbcTemplate().query(sbuf.toString(), new TblAccountRowMapper());		
									
		log.debug("final list size is = " + accountList.size());
		log.debug(methodName + "out");
		return accountList;	
	}

	public List<Tblaccount> update(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering AccountbDAO update");
		
		Tblaccount account = (Tblaccount)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("UPDATE tblaccount set ");
		sbuf.append("iPortfolioID = ?, iAccountTypeID = ?, iBrokerID = ?, sAccountName = ?, sAccountNameAbbr = ?, iStartingCheckNo = ?,");
		sbuf.append("sAccountNumber = ?, sPIN = ?, bClosed = ?, iInterestPaymentsPerYear = ?, dInterestRate = ?, mMinInterestBalance = ?,");
		sbuf.append("bTaxableInd = ?, mNewMoneyPerYear = ?, dEstimatedRateofReturn = ?, dVestingPercentage = ?");
		sbuf.append(" where iaccountid = ?");
		
		log.debug("about to run update statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {account.getIportfolioId(), account.getIaccountTypeId(), account.getIbrokerId(), account.getSaccountName(),
				account.getSaccountNameAbbr(), account.getIstartingCheckNo(), account.getSaccountNumber(), account.getSpin(), account.isBclosed(), account.getIinterestPaymentsPerYear(),
				account.getDinterestRate(), account.getMminInterestBalance(), account.isBtaxableInd(), account.getMnewMoneyPerYear(),
				account.getDestimatedRateofReturn(), account.getDvestingPercentage(), account.getIaccountId()});		
		
		//no need to pass back a list on an update
		return null;	
	}
  
}
