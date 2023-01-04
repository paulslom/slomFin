package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblWDCategoryRowMapper;
import com.pas.dbobjects.Tblaccount;
import com.pas.dbobjects.Tblportfoliohistory;
import com.pas.exception.DAOException;
import com.pas.exception.SystemException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.IDObject;
import com.pas.slomfin.valueObject.Investor;
import com.pas.slomfin.valueObject.PortfolioHistory;
import com.pas.slomfin.valueObject.PortfolioSummary;
import com.pas.util.Utils;

public class PortfolioHistoryDAO extends BaseDBDAO
{
    private static final PortfolioHistoryDAO currentInstance = new PortfolioHistoryDAO();

    private PortfolioHistoryDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return PortfolioDAO
     */
    public static PortfolioHistoryDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
     
    public List<Tblportfoliohistory> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		if (Info instanceof IDObject)
		{
			portfolioHistoryBuild(new Integer(((IDObject)Info).getId()));
			return null;
		}
						
		Tblportfoliohistory portHist = (Tblportfoliohistory)Info;		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblportfoliohistory (");
		sbuf.append("iAccountID, dHistoryDate, mValue");
		sbuf.append(") values(?,?,?)");	
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {portHist.getIaccountId(), portHist.getDhistoryDate(), portHist.getMvalue()});	
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}	
    
	@SuppressWarnings("unchecked")
	public List<PortfolioHistory> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		Investor investor = (Investor)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("SELECT H.dhistoryDate as Date, SUM(H.mvalue) as totalValue");
		sbuf.append(" FROM Tblportfoliohistory H ");
		sbuf.append(" INNER JOIN tblaccount acc on H.iaccountid = acc.iaccountid");
		sbuf.append("  INNER JOIN tblPortfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append("  INNER JOIN tblInvestor inv on portf.iinvestorid = inv.iinvestorid");
		sbuf.append("  INNER JOIN tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" WHERE txg.itaxgroupid = ");
		sbuf.append("  (select inv.itaxGroupId from Tblinvestor inv ");  
		sbuf.append("   where inv.iinvestorId = ");
		sbuf.append(investor.getInvestorID());
		sbuf.append(")");		
		sbuf.append(" GROUP BY H.dhistoryDate");
		sbuf.append(" ORDER BY H.dhistoryDate");
		 
		log.debug("about to run query: " + sbuf.toString());
		
		List<PortfolioHistory> portfolioHistoryList = this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List<PortfolioHistory>>() 
		{	   
			@Override
		    public List<PortfolioHistory> extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List<PortfolioHistory> tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					PortfolioHistory ph = new PortfolioHistory();
					ph.setPortfolioHistoryDate(rs.getDate(1));
					ph.setNetWorth(rs.getBigDecimal(2));					
			        tempList.add(ph);			        
				}
				return tempList;
		    }
		});
				
		log.debug("final list size is = " + portfolioHistoryList.size());
		log.debug(methodName + "out");
		return portfolioHistoryList;	
	 }
	
	 public List<PortfolioHistory> portfolioHistoryBuild(Integer investorID) throws DAOException
	 {
    	final String methodName = "portfolioHistoryBuild::";
		log.debug(methodName + "in");
		
		PortfolioDAO portfolioDAOReference;
		List<PortfolioSummary> portfolioSummaryList = null;
		
		try
		{
			portfolioDAOReference = (PortfolioDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.PORTFOLIO_DAO);
			portfolioSummaryList = portfolioDAOReference.portfolioSummaryBuild(investorID.toString());
		}
		catch (SystemException e1)
		{
			log.error("SystemException encountered: " + e1.getMessage());
			e1.printStackTrace();
			throw new DAOException(e1);
		}
		
		Date today = new Date();
		
		//once we have this list, need to sum up each account and insert a record into Tblportfoliohistory with todays date
		Integer lastAccountID = new Integer(0);
		Double accountTotal = new Double(0.0);
		
		for (int i = 0; i < portfolioSummaryList.size(); i++)
		{
			PortfolioSummary pSumm = portfolioSummaryList.get(i);
			
			Integer accountID = pSumm.getAccountID();
			
			if (accountID.compareTo(lastAccountID) != 0)
			{
				if (accountTotal > 0)			
			    {
				   Tblportfoliohistory pHistory = new Tblportfoliohistory();
				   pHistory.setDhistoryDate(today);
				   pHistory.setMvalue(new BigDecimal(accountTotal));
				   
				   AccountDAO accountDAOReference;
					
				   try
				   {
						accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
						List<Tblaccount> accntList = accountDAOReference.inquire(lastAccountID);
						pHistory.setTblaccount(accntList.get(0));
						pHistory.setIaccountId(pHistory.getTblaccount().getIaccountId());
				   }
			       catch (SystemException e1)
				   {
					   log.error("SystemException encountered: " + e1.getMessage());
					   e1.printStackTrace();
					   throw new DAOException(e1);
				   }
			       
				   add(pHistory);	
			       
				   accountTotal = 0.0;				   
			   }
			}
			
			accountTotal = accountTotal + pSumm.getCurrentValue().doubleValue();
			lastAccountID = accountID;			
		}	
		
		//this will do the last one
		
		if (accountTotal > 0)
	    {
		   Tblportfoliohistory pHistory = new Tblportfoliohistory();
		   pHistory.setDhistoryDate(today);
		   pHistory.setMvalue(new BigDecimal(accountTotal));
		   
		   AccountDAO accountDAOReference;
			
		   try
		   {
				accountDAOReference = (AccountDAO)SlomFinDAOFactory.getDAOInstance(ISlomFinAppConstants.ACCOUNT_DAO);
				List<Tblaccount> accntList = accountDAOReference.inquire(lastAccountID);
				pHistory.setTblaccount(accntList.get(0));
				pHistory.setIaccountId(pHistory.getTblaccount().getIaccountId());
		   }
	       catch (SystemException e1)
		   {
			   log.error("SystemException encountered: " + e1.getMessage());
			   e1.printStackTrace();
			   throw new DAOException(e1);
		   }
	       
		   add(pHistory);
	    }			
				
		log.debug(methodName + "out");
		return null;	
			
		
	}
}
