package com.pas.slomfin.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.TblInvestmentRowMapper;
import com.pas.dbobjects.Tblinvestment;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		InvestmentDAO
 * Project: 	Slomkowski Financial Application
 * Description: Investment DAO extends BaseDBDAO. Implements the data access to the tblPaydayDefault table
 * Copyright: 	Copyright (c) 2006
 */
public class InvestmentDAO extends BaseDBDAO
{
    private static final InvestmentDAO currentInstance = new InvestmentDAO();

    private InvestmentDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return InvestmentDAO
     */
    public static InvestmentDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    
	public List<Tblinvestment> add(Object Info) throws DAOException
	{
		final String methodName = "add::";
		log.debug(methodName + "in");
		
		log.debug("entering InvestmentDAO add");		
				
		Tblinvestment inv = (Tblinvestment)Info;
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("INSERT INTO tblinvestment (");
		sbuf.append("iInvestmentTypeID, sTickerSymbol, sDescription, dOptionMultiplier, mCurrentPrice,");
		sbuf.append("iDividendsPerYear, dDividendRate, dQuoteDate, sStockLogo, sStockChart, iAssetClassID");
		sbuf.append(") values(?,?,?,?,?,?,?,?,?,?,?)");	
		
		log.info("about to run insert.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {inv.getIinvestmentTypeId(), inv.getStickerSymbol(), inv.getSdescription(), inv.getDoptionMultiplier(),
				inv.getMcurrentPrice(), inv.getIdividendsPerYear(), inv.getDdividendRate(), inv.getDquoteDate(), inv.getSstockLogo(), inv.getSstockChart(), inv.getIassetClassId()});			
			
		log.debug(methodName + "out");
		
		//no list to pass back on an add
		return null;	
	}
		
	@SuppressWarnings("unchecked")
	public List<Tblinvestment> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("select inv.*, invtyp.*, asscl.*, invtyp.sdescription as investmentTypeDescription");
		sbuf.append(" from tblinvestment inv, tblinvestmenttype invtyp, tblassetclass asscl");
		sbuf.append(" where inv.iInvestmentTypeID = invtyp.iInvestmentTypeID");
		sbuf.append(" and inv.iAssetClassID = asscl.iAssetClassID");

		if (Info instanceof Integer)
		{
			Integer investmentID = (Integer)Info;
			
			//this is an inquire request for a particular row on the database
			
			log.debug("Integer object provided - will perform a single row retrieval from DB");			
			
			sbuf.append(" and iinvestmentId = ");
			sbuf.append(investmentID.toString());
			
			log.debug(methodName + "before inquiring for Investment. Key value is = " + investmentID.toString());
	
		}		
		else if (Info instanceof Investor) //this is an inquire request for a list of Investment positions - Investor object provided			
		{
			Integer investorID = Integer.valueOf (((Investor)Info).getInvestorID());
			
			log.debug("Investor object provided - investor positions retrieval from DB");			
						
			sbuf.append(" and iinvestmentId IN ");
			sbuf.append(" (SELECT trx.iinvestmentId");	 
			sbuf.append(" FROM Tbltransaction trx");
			sbuf.append(" INNER JOIN tblaccount acc on trx.iaccountid = acc.iaccountid");
			sbuf.append(" INNER JOIN tblportfolio portf on acc.iportfolioid = portf.iportfolioid");
			sbuf.append(" INNER JOIN tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
			sbuf.append("  WHERE portf.iinvestorId = ");	
			sbuf.append(investorID.toString());
			sbuf.append(" GROUP by trx.iinvestmentId"); 	
			sbuf.append(" having SUM(CASE trxtyp.bpositiveInd"); 		
			sbuf.append(" WHEN 0 THEN -trx.decUnits"); 		
			sbuf.append(" WHEN 1 THEN trx.decUnits"); 		
			sbuf.append(" ELSE 0 END) <> 0 )");
			sbuf.append("   ORDER by stickerSymbol");
			
			log.debug(methodName + "before inquiring for InvestmentPositions. Investor is = " + (((Investor)Info).getInvestorID()));		
		}
		else if (Info instanceof String) //this is an inquire request for a single investment by description
		{
			String investmentDescription = (String)Info;
			
			//this is an inquire request for a particular row on the database
			
			log.debug("String object provided - will perform a single row retrieval from DB");			
			
			sbuf.append(" and sdescription = '");
			sbuf.append(investmentDescription);
			sbuf.append("'");
			
			log.debug(methodName + "before inquiring for Investment - Looking for description = " + investmentDescription);
	
		}
		else //this is an inquire request for all Investments	
		{            	
			log.debug("Will perform multiple rows retrieval for Investments");
			sbuf.append("   ORDER by inv.sDescription");
		}
		
		log.debug("about to run query: " + sbuf.toString());
		
		List<Tblinvestment> investmentList = this.getJdbcTemplate().query(sbuf.toString(), new TblInvestmentRowMapper());
						
		log.debug("final list size is = " + investmentList.size());
		log.debug(methodName + "out");
		return investmentList;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Tblinvestment> update(Object Info) throws DAOException
	{
		final String methodName = "update:";
		log.debug(methodName + "in");
					
		if (Info instanceof Tblinvestment)
		{
			Tblinvestment investment = (Tblinvestment)Info;
			doInvUpdate(investment);
		}
		else //must be a list of investment objects 
		{		
			 List<Tblinvestment> invList = (List)Info;
			 
			 for (int i=0; i<invList.size(); i++)
			 {	
				Tblinvestment investment = invList.get(i);	
				doInvUpdate(investment);
			 }	
		}
		//no need to pass back a list on an update
		return null;	
	}
    
	private void doInvUpdate(Tblinvestment inv)
	{
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("UPDATE tblinvestment set ");
		sbuf.append("iInvestmentTypeID = ?, sTickerSymbol = ?, sDescription = ?, dOptionMultiplier = ?, mCurrentPrice = ?,");
		sbuf.append("iDividendsPerYear = ?, dDividendRate = ?, dQuoteDate = ?, sStockLogo = ?, sStockChart = ?, iAssetClassID = ?");
		sbuf.append(" where iinvestmentid = ?");	
		
		log.info("about to run update.  Statement: " + sbuf.toString());
		
		this.getJdbcTemplate().update(sbuf.toString(), new Object[] {inv.getIinvestmentTypeId(), inv.getStickerSymbol(), inv.getSdescription(), inv.getDoptionMultiplier(),
				inv.getMcurrentPrice(), inv.getIdividendsPerYear(), inv.getDdividendRate(), inv.getDquoteDate(), inv.getSstockLogo(), inv.getSstockChart(), 
				inv.getIassetClassId(), inv.getIinvestmentId()});			
			
	}
	public List<Tblinvestment> delete(Object Info) throws DAOException
	{
		final String methodName = "delete::";
		log.debug(methodName + "in");
		
		log.debug("entering InvestmentDAO delete");
		
		Tblinvestment investment = (Tblinvestment)Info;
		
		Integer id = investment.getIinvestmentId();
		
		log.info("about to delete investment id: " + id);
		
		String deleteStr = "delete from tblinvestment where iinvestmentid = ?";
		this.getJdbcTemplate().update(deleteStr, id);
		
		log.info("successfully deleted investment id: " + id);
		
		log.debug(methodName + "out");
		
		//no list to pass back on a delete
		return null;	
	}

}
