package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblPortfolioRowMapper;
import com.pas.dbobjects.Tblinvestor;
import com.pas.dbobjects.Tblportfolio;
import com.pas.exception.DAOException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.IDObject;
import com.pas.slomfin.valueObject.PortfolioSummary;
import com.pas.util.PortfolioSummaryByACComparator;
import com.pas.util.PortfolioSummaryComparator;
import com.pas.util.Utils;

/**
 * Title: 		PortfolioDAO
 * Project: 	Slomkowski Financial Application
 * Description: Portfolio DAO extends BaseDBDAO. Implements the data access to the tblPortfolio table
 * Copyright: 	Copyright (c) 2006
 */
public class PortfolioDAO extends BaseDBDAO
{
    private static final PortfolioDAO currentInstance = new PortfolioDAO();

    private PortfolioDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return PortfolioDAO
     */
    public static PortfolioDAO getDAOInstance() 
    {
       	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
    public List<Tblportfolio> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblportfolio port = (Tblportfolio)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblportfolio (");
		sbuf.append("iInvestorID, sPortfolioName, bTaxableInd");
		sbuf.append(") values(?,?,?)");	
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {port.getIinvestorId(), port.getSportfolioName(), port.isBtaxableInd()});	
		
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}	
    
	@SuppressWarnings("unchecked")
	public List inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");
		
		List<PortfolioSummary> portfolioSummaryList = new ArrayList<PortfolioSummary>();
		List<Tblportfolio> portfolioList = new ArrayList<Tblportfolio>();
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select portf.*, inv.*, txg.*, acc.*, acctyp.*, brk.* ");
		sbuf.append(" from tblportfolio portf");
		sbuf.append(" inner join tblinvestor inv on portf.iinvestorid = inv.iinvestorid");
		sbuf.append(" left join tbltaxgroup txg on inv.itaxgroupid = txg.itaxgroupid");
		sbuf.append(" left join tblaccount acc on inv.ihardcashaccountid = acc.iaccountid");
		sbuf.append(" left join tblaccounttype acctyp on acc.iAccountTypeID = acctyp.iAccountTypeID");
		sbuf.append(" left join tblbroker brk on acc.iBrokerID = brk.iBrokerID");

		  
		if (Info instanceof Integer)
		{
			Integer portfolioID = (Integer)Info;
							
			//this is an inquire request for an individual portfolio			
				
			sbuf.append(" where portf.iportfolioId = ");
			sbuf.append(portfolioID.toString());
			
			log.debug(methodName + "before inquiring for Portfolio. Key value is = " + portfolioID.toString());
			log.debug("about to run query: " + sbuf.toString());
			
			portfolioList = this.getJdbcTemplate().query(sbuf.toString(), new TblPortfolioRowMapper());		
			
			log.debug("final list size is = " + portfolioList.size());
			return portfolioList;	
		}		
		else if (Info instanceof Tblinvestor) //this is an inquire request for list of Portfolios - Tblinvestor object provided					
		{            	
			log.debug("Will perform multiple rows retrieval for Portfolios");
			
			Integer investorID = Integer.valueOf (((Tblinvestor)Info).getIinvestorId());
			
			sbuf.append(" where portf.iinvestorId = ");
			sbuf.append(investorID.toString());
			
			log.debug(methodName + "before inquiring for Portfolio using Investor ID = " + investorID.toString());
			log.debug("about to run query: " + sbuf.toString());
			
			portfolioList = this.getJdbcTemplate().query(sbuf.toString(), new TblPortfolioRowMapper());		
			
			log.debug("final list size is = " + portfolioList.size());
			return portfolioList;	
			
		}
		else //ID object provided - it's either a portfoliosummary or portfoliosummarybyassetclass request
		{
			IDObject idObject = (IDObject)Info;						
								
			if (idObject.getIdDescriptor().equalsIgnoreCase(ISlomFinAppConstants.PORTFOLIOSUMMARYBYASSETCLASSREQUEST))
			{
				portfolioSummaryList = portfolioSummaryByAssetClassBuild(idObject.getId());	
			}
			else if (idObject.getIdDescriptor().equalsIgnoreCase(ISlomFinAppConstants.ACCTPOSITIONS))
			{
				portfolioSummaryList = accountPositionsBuild(idObject.getId());	
			}
			else //this is a Portfolio Summary request
			{
				portfolioSummaryList = portfolioSummaryBuild(idObject.getId());												
			}
						
			return portfolioSummaryList;					
		}						
	}
		 
	private List<PortfolioSummary> accountPositionsBuild(String accountID) 
	{
		List<PortfolioSummary> portfolioSummaryList = new ArrayList<PortfolioSummary>();
		 
		//build the query to determine balances for securities
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append(" select acc.iportfolioId as portfolioID,");      
		sbuf.append("  trx.iaccountId as accountID,");                
		sbuf.append("  portf.sportfolioName as portfolioName,");     
		sbuf.append("  acc.saccountName as accountName,");		      
		sbuf.append("  invm.doptionMultiplier as optionMultiplier,"); 
		sbuf.append("  invm.sdescription as investmentDescription,"); 
		sbuf.append("  invm.mcurrentPrice as currentPrice,");         
		sbuf.append("  asscl.sassetClass as assetClass,");            
		sbuf.append("  acc.mnewMoneyPerYear as newMoney,");           
		sbuf.append("  acctyp.saccountType as accountType,");         
		sbuf.append("  SUM(CASE trxtyp.bpositiveInd");
		sbuf.append("  WHEN 0 THEN -trx.decUnits"); 
		sbuf.append("  WHEN 1 THEN trx.decUnits");  
		sbuf.append("  ELSE 0 END) as unitsOwned");                   
		sbuf.append("  from Tbltransaction trx");
		sbuf.append("  INNER JOIN tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
		sbuf.append("  INNER JOIN tblinvestment invm on trx.iinvestmentid = invm.iinvestmentid");
		sbuf.append("  INNER JOIN tblaccount acc on trx.iAccountID = acc.iAccountID");
		sbuf.append("  INNER JOIN tblaccounttype acctyp on acc.iaccounttypeid = acctyp.iaccounttypeid");
		sbuf.append("  INNER JOIN tblPortfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append("  INNER JOIN tblInvestor inv on portf.iinvestorid = inv.iinvestorid");
		sbuf.append("  LEFT JOIN tblassetclass asscl on invm.iassetclassid = asscl.iassetclassid");
		sbuf.append("  where acc.iaccountId = ");    
		sbuf.append(accountID);
		sbuf.append("  and acc.dvestingPercentage > 0");
		sbuf.append("  group by acc.iportfolioId,");
		sbuf.append("  acc.iaccountId,");
		sbuf.append("  portf.sportfolioName,");
		sbuf.append("  acc.saccountName,");
		sbuf.append("  invm.doptionMultiplier,");
		sbuf.append("  invm.sdescription,");	  
		sbuf.append("  invm.mcurrentPrice,");
		sbuf.append("  asscl.sassetClass,");
		sbuf.append("  acc.mnewMoneyPerYear,");
		sbuf.append("  acctyp.saccountType");
		sbuf.append("  having SUM(CASE trxtyp.bpositiveInd");
		sbuf.append("  WHEN 0 THEN -trx.decUnits");    
		sbuf.append("  WHEN 1 THEN trx.decUnits");    
		sbuf.append("  ELSE 0 END) <> 0"); 
		
		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					PortfolioSummary portfolioSummaryDetail = new PortfolioSummary();			
		            
					portfolioSummaryDetail.setPortfolioID(rs.getInt(1));
					portfolioSummaryDetail.setAccountID(rs.getInt(2));
					portfolioSummaryDetail.setPortfolioName(rs.getString(3));
					portfolioSummaryDetail.setAccountName(rs.getString(4));

					log.debug("Account name = " + portfolioSummaryDetail.getAccountName());
					
					portfolioSummaryDetail.setInvestmentDescription(rs.getString(6));
					
					log.debug("Investment = " + portfolioSummaryDetail.getInvestmentDescription());
					
					portfolioSummaryDetail.setCurrentPrice(rs.getBigDecimal(7));
					
					log.debug("Current Price = " + portfolioSummaryDetail.getCurrentPrice());
					
					portfolioSummaryDetail.setAssetClass(rs.getString(8));
					portfolioSummaryDetail.setNewMoney(rs.getBigDecimal(9));
					portfolioSummaryDetail.setAccountType(rs.getString(10));
					portfolioSummaryDetail.setUnitsOwned(rs.getBigDecimal(11));					
					
					log.debug("Units Owned = " + portfolioSummaryDetail.getUnitsOwned());
					
					portfolioSummaryDetail.setCurrentValue(portfolioSummaryDetail.getUnitsOwned().multiply(portfolioSummaryDetail.getCurrentPrice()));
					
					if (portfolioSummaryDetail.getAssetClass().equalsIgnoreCase("Options"))
					{
						BigDecimal optMult = rs.getBigDecimal(5);
						portfolioSummaryDetail.setCurrentValue(portfolioSummaryDetail.getCurrentValue().multiply(optMult));
					}
					log.debug("Current Value = " + portfolioSummaryDetail.getCurrentValue());
					
					portfolioSummaryList.add(portfolioSummaryDetail);						        
				}
				return tempList;
		    }
		});		
		
		//now need to build cash balances query
		sbuf.setLength(0); //this clears the stringbuffer
		
		sbuf.append("select acc.iportfolioId as portfolioID,");
		sbuf.append(" trx.iaccountId as accountID,");   
		sbuf.append(" portf.sportfolioName as portfolioName,"); 
		sbuf.append(" acc.saccountName as accountName,");
		sbuf.append(" acctyp.saccountType as accountType,");
		sbuf.append(" SUM(trx.meffectiveAmount) as unitsOwned");  
		sbuf.append(" from Tbltransaction trx");
		sbuf.append(" INNER JOIN tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
		sbuf.append(" INNER JOIN tblaccount acc on trx.iaccountid = acc.iaccountid");
		sbuf.append(" INNER JOIN tblaccounttype acctyp on acc.iaccounttypeid = acctyp.iaccounttypeid");
		sbuf.append(" INNER JOIN tblPortfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append(" INNER JOIN tblInvestor inv on portf.iinvestorid = inv.iinvestorid");
		sbuf.append("  where acc.iaccountId = ");    
		sbuf.append(accountID);
		sbuf.append("  and trxtyp.sdescription <> 'Reinvest'");
		sbuf.append("  and acctyp.saccountType <> 'Real Estate'");
		sbuf.append("  group by acc.iportfolioId,");
		sbuf.append("  acc.iaccountId,");    
		sbuf.append("  portf.sportfolioName,");    
		sbuf.append("  acc.saccountName,");
		sbuf.append("  acctyp.saccountType");
		sbuf.append("  having SUM(trx.meffectiveAmount) <> 0");

		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					PortfolioSummary portfolioSummaryDetail = new PortfolioSummary();			
		            
					portfolioSummaryDetail.setPortfolioID(rs.getInt(1));
					portfolioSummaryDetail.setAccountID(rs.getInt(2));
					portfolioSummaryDetail.setPortfolioName(rs.getString(3));
					portfolioSummaryDetail.setAccountName(rs.getString(4));
					
					log.debug("Account name = " + portfolioSummaryDetail.getAccountName());
					
					portfolioSummaryDetail.setInvestmentDescription("Cash");
					portfolioSummaryDetail.setAssetClass("Cash");
					
					log.debug("Investment = " + portfolioSummaryDetail.getInvestmentDescription());
					
					portfolioSummaryDetail.setCurrentPrice(new BigDecimal(0.0));
					
					log.debug("Current Price = " + portfolioSummaryDetail.getCurrentPrice());
					
					portfolioSummaryDetail.setAccountType(rs.getString(5));
					
					log.debug("Account Type = " + portfolioSummaryDetail.getAccountType());
					
					portfolioSummaryDetail.setUnitsOwned(rs.getBigDecimal(6));				
					
					log.debug("Units Owned = " + portfolioSummaryDetail.getUnitsOwned());
					
					portfolioSummaryDetail.setCurrentValue(portfolioSummaryDetail.getUnitsOwned());
					
					log.debug("Current Value = " + portfolioSummaryDetail.getCurrentValue());
					
					portfolioSummaryList.add(portfolioSummaryDetail);			        
				}
				return tempList;
		    }
		});
		
		//now sort portfolioSummaryList by portfolioID, AccountName, Amount
		
		Collections.sort(portfolioSummaryList, new PortfolioSummaryComparator());
		
		return portfolioSummaryList;
	}
	
	private List<PortfolioSummary> portfolioSummaryByAssetClassBuild(String investorID) throws DAOException
	{
		log.debug("inside portfolioSummaryByAssetClassBuild");
		
		//This first call retrieves a list of all accounts with individual investment positions by Investor
		//Which means it includes ALL positions and balances for this investor.
		
		List<PortfolioSummary> portfolioSummaryList = portfolioSummaryBuild(investorID);	
		List<PortfolioSummary> portfolioSummaryListPared = new ArrayList<PortfolioSummary>();
		
		for (int i = 0; i < portfolioSummaryList.size(); i++)
		{
			PortfolioSummary psTemp = new PortfolioSummary();
			psTemp = portfolioSummaryList.get(i);
			
			String acctType = psTemp.getAccountType();
			String acctName = psTemp.getAccountName();
			
			if (acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_TAXABLEBROKERAGE)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_ROTHIRA)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_ROTH401K)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_401K)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_PENSION)
			||	acctType.equalsIgnoreCase(ISlomFinAppConstants.ACCOUNTTYPE_TRADIRA)
			||  (acctName.startsWith("Amer") && (acctName.endsWith("Money Mkt") || acctName.endsWith("MnyMkt"))))
			{
				portfolioSummaryListPared.add(psTemp);
			}
		}
		//Now need to tally up entire value of this list
		//So that percentages can be arrived at
		Double netWorthTotal = new Double(0.0);
		
		for (int i = 0; i < portfolioSummaryListPared.size(); i++)
		{
			PortfolioSummary pSumm = portfolioSummaryListPared.get(i);
			netWorthTotal = netWorthTotal + pSumm.getCurrentValue().doubleValue();
		}
		
		log.debug("Net Worth Total is = " + netWorthTotal);
		
		//Sort the list so we can derive totals by asset class
		Collections.sort(portfolioSummaryListPared, new PortfolioSummaryByACComparator());
		
		List<PortfolioSummary> returnList = new ArrayList<PortfolioSummary>();
		
		Double assetClassTotal = new Double(0.0);
		String lastAssetClass = "@#@@";
		
		for (int i = 0; i < portfolioSummaryListPared.size(); i++)
		{
			PortfolioSummary pSumm = portfolioSummaryListPared.get(i);
			String assetClass = pSumm.getAssetClass();
			
			if (i>0) //ignore first time thru
			   if (!assetClass.equalsIgnoreCase(lastAssetClass)) //time to add a list item to returnList
			   {
				   PortfolioSummary returnDetail = new PortfolioSummary();
				   returnDetail.setAssetClass(lastAssetClass);
				   returnDetail.setCurrentValue(new BigDecimal(assetClassTotal));
				   Double acPercentage = 100 * (assetClassTotal/netWorthTotal);
				   returnDetail.setAssetClassPercentage(new BigDecimal(acPercentage));
				   log.debug("Adding asset class: " + lastAssetClass + " value = " + assetClassTotal + " percentage = " + acPercentage);
				   returnList.add(returnDetail);
				   assetClassTotal = 0.0;
			   }
			assetClassTotal = assetClassTotal + pSumm.getCurrentValue().doubleValue();
			lastAssetClass = assetClass;
		}
		
		//Add the last one
		
		PortfolioSummary returnDetail = new PortfolioSummary();
		returnDetail.setAssetClass(lastAssetClass);
		returnDetail.setCurrentValue(new BigDecimal(assetClassTotal));
		Double acPercentage = 100 * (assetClassTotal/netWorthTotal);
		returnDetail.setAssetClassPercentage(new BigDecimal(acPercentage));
		returnList.add(returnDetail);
		   
		return returnList;
	}
	
	public List<Tblportfolio> update(Object Info) throws DAOException
	 {
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		Tblportfolio port = (Tblportfolio)Info;
			
		//no need to pass back a list on an update
		return null;	
	 }
    
	 public List<Tblportfolio> delete(Object Info) throws DAOException
	 {
		final String methodName = "delete::";
		log.debug(methodName + "in");
										
		Tblportfolio port = (Tblportfolio)Info;
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	 }
		
	 @SuppressWarnings("unchecked")
	public List<PortfolioSummary> portfolioSummaryBuild(String investorID)
	{
		List<PortfolioSummary> portfolioSummaryList = new ArrayList<PortfolioSummary>();
				 
		//build the query to determine balances for securities
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append(" select acc.iportfolioId as portfolioID,");      
		sbuf.append("  trx.iaccountId as accountID,");                
		sbuf.append("  portf.sportfolioName as portfolioName,");     
		sbuf.append("  acc.saccountName as accountName,");		      
		sbuf.append("  invm.doptionMultiplier as optionMultiplier,"); 
		sbuf.append("  invm.sdescription as investmentDescription,"); 
		sbuf.append("  invm.mcurrentPrice as currentPrice,");         
		sbuf.append("  asscl.sassetClass as assetClass,");            
		sbuf.append("  acc.mnewMoneyPerYear as newMoney,");           
		sbuf.append("  acctyp.saccountType as accountType,");         
		sbuf.append("  SUM(CASE trxtyp.bpositiveInd");
		sbuf.append("  WHEN 0 THEN -trx.decUnits"); 
		sbuf.append("  WHEN 1 THEN trx.decUnits");  
		sbuf.append("  ELSE 0 END) as unitsOwned");                   
		sbuf.append("  from Tbltransaction trx");
		sbuf.append("  INNER JOIN tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
		sbuf.append("  INNER JOIN tblinvestment invm on trx.iinvestmentid = invm.iinvestmentid");
		sbuf.append("  INNER JOIN tblaccount acc on trx.iAccountID = acc.iAccountID");
		sbuf.append("  INNER JOIN tblaccounttype acctyp on acc.iaccounttypeid = acctyp.iaccounttypeid");
		sbuf.append("  INNER JOIN tblPortfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append("  INNER JOIN tblInvestor inv on portf.iinvestorid = inv.iinvestorid");
		sbuf.append("  LEFT JOIN tblassetclass asscl on invm.iassetclassid = asscl.iassetclassid");
		sbuf.append("  where inv.itaxGroupId = ");    
		sbuf.append("       (select inv.itaxGroupId from Tblinvestor inv");     
		sbuf.append("        where inv.iinvestorId = ");
		sbuf.append(investorID);
		sbuf.append(")");
		sbuf.append("  and acc.dvestingPercentage > 0");
		sbuf.append("  group by acc.iportfolioId,");
		sbuf.append("  acc.iaccountId,");
		sbuf.append("  portf.sportfolioName,");
		sbuf.append("  acc.saccountName,");
		sbuf.append("  invm.doptionMultiplier,");
		sbuf.append("  invm.sdescription,");	  
		sbuf.append("  invm.mcurrentPrice,");
		sbuf.append("  asscl.sassetClass,");
		sbuf.append("  acc.mnewMoneyPerYear,");
		sbuf.append("  acctyp.saccountType");
		sbuf.append("  having SUM(CASE trxtyp.bpositiveInd");
		sbuf.append("  WHEN 0 THEN -trx.decUnits");    
		sbuf.append("  WHEN 1 THEN trx.decUnits");    
		sbuf.append("  ELSE 0 END) <> 0"); 
		
		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					PortfolioSummary portfolioSummaryDetail = new PortfolioSummary();			
		            
					portfolioSummaryDetail.setPortfolioID(rs.getInt(1));
					portfolioSummaryDetail.setAccountID(rs.getInt(2));
					portfolioSummaryDetail.setPortfolioName(rs.getString(3));
					portfolioSummaryDetail.setAccountName(rs.getString(4));

					log.debug("Account name = " + portfolioSummaryDetail.getAccountName());
					
					portfolioSummaryDetail.setInvestmentDescription(rs.getString(6));
					
					log.debug("Investment = " + portfolioSummaryDetail.getInvestmentDescription());
					
					portfolioSummaryDetail.setCurrentPrice(rs.getBigDecimal(7));
					
					log.debug("Current Price = " + portfolioSummaryDetail.getCurrentPrice());
					
					portfolioSummaryDetail.setAssetClass(rs.getString(8));
					portfolioSummaryDetail.setNewMoney(rs.getBigDecimal(9));
					portfolioSummaryDetail.setAccountType(rs.getString(10));
					portfolioSummaryDetail.setUnitsOwned(rs.getBigDecimal(11));					
					
					log.debug("Units Owned = " + portfolioSummaryDetail.getUnitsOwned());
					
					portfolioSummaryDetail.setCurrentValue(portfolioSummaryDetail.getUnitsOwned().multiply(portfolioSummaryDetail.getCurrentPrice()));
					
					if (portfolioSummaryDetail.getAssetClass().equalsIgnoreCase("Options"))
					{
						BigDecimal optMult = rs.getBigDecimal(5);
						portfolioSummaryDetail.setCurrentValue(portfolioSummaryDetail.getCurrentValue().multiply(optMult));
					}
					log.debug("Current Value = " + portfolioSummaryDetail.getCurrentValue());
					
					portfolioSummaryList.add(portfolioSummaryDetail);						        
				}
				return tempList;
		    }
		});		
		
		//now need to build cash balances query
		sbuf.setLength(0); //this clears the stringbuffer
		
		sbuf.append("select acc.iportfolioId as portfolioID,");
		sbuf.append(" trx.iaccountId as accountID,");   
		sbuf.append(" portf.sportfolioName as portfolioName,"); 
		sbuf.append(" acc.saccountName as accountName,");
		sbuf.append(" acctyp.saccountType as accountType,");
		sbuf.append(" SUM(trx.meffectiveAmount) as unitsOwned");  
		sbuf.append(" from Tbltransaction trx");
		sbuf.append(" INNER JOIN tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
		sbuf.append(" INNER JOIN tblaccount acc on trx.iaccountid = acc.iaccountid");
		sbuf.append(" INNER JOIN tblaccounttype acctyp on acc.iaccounttypeid = acctyp.iaccounttypeid");
		sbuf.append(" INNER JOIN tblPortfolio portf on acc.iportfolioid = portf.iportfolioid");
		sbuf.append(" INNER JOIN tblInvestor inv on portf.iinvestorid = inv.iinvestorid");
		sbuf.append("  where inv.itaxGroupId = ");    
		sbuf.append("       (select inv.itaxGroupId from Tblinvestor inv");     
		sbuf.append("        where inv.iinvestorId = ");
		sbuf.append(investorID);
		sbuf.append(")");
		sbuf.append("  and trxtyp.sdescription <> 'Reinvest'");
		sbuf.append("  and acctyp.saccountType <> 'Real Estate'");
		sbuf.append("  group by acc.iportfolioId,");
		sbuf.append("  acc.iaccountId,");    
		sbuf.append("  portf.sportfolioName,");    
		sbuf.append("  acc.saccountName,");
		sbuf.append("  acctyp.saccountType");
		sbuf.append("  having SUM(trx.meffectiveAmount) <> 0");

		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					PortfolioSummary portfolioSummaryDetail = new PortfolioSummary();			
		            
					portfolioSummaryDetail.setPortfolioID(rs.getInt(1));
					portfolioSummaryDetail.setAccountID(rs.getInt(2));
					portfolioSummaryDetail.setPortfolioName(rs.getString(3));
					portfolioSummaryDetail.setAccountName(rs.getString(4));
					
					log.debug("Account name = " + portfolioSummaryDetail.getAccountName());
					
					portfolioSummaryDetail.setInvestmentDescription("Cash");
					portfolioSummaryDetail.setAssetClass("Cash");
					
					log.debug("Investment = " + portfolioSummaryDetail.getInvestmentDescription());
					
					portfolioSummaryDetail.setCurrentPrice(new BigDecimal(0.0));
					
					log.debug("Current Price = " + portfolioSummaryDetail.getCurrentPrice());
					
					portfolioSummaryDetail.setAccountType(rs.getString(5));
					
					log.debug("Account Type = " + portfolioSummaryDetail.getAccountType());
					
					portfolioSummaryDetail.setUnitsOwned(rs.getBigDecimal(6));				
					
					log.debug("Units Owned = " + portfolioSummaryDetail.getUnitsOwned());
					
					portfolioSummaryDetail.setCurrentValue(portfolioSummaryDetail.getUnitsOwned());
					
					log.debug("Current Value = " + portfolioSummaryDetail.getCurrentValue());
					
					portfolioSummaryList.add(portfolioSummaryDetail);			        
				}
				return tempList;
		    }
		});
		
		//now sort portfolioSummaryList by portfolioID,AccountName, Amount
		
		Collections.sort(portfolioSummaryList, new PortfolioSummaryComparator());
		
		return portfolioSummaryList;
		
	 }

}
