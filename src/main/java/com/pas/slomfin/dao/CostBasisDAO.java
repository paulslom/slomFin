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
import com.pas.dbobjects.CostBasisRowMapper;
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.DAOException;
import com.pas.slomfin.valueObject.CostBasis;
import com.pas.slomfin.valueObject.Dividend;
import com.pas.slomfin.valueObject.Investor;
import com.pas.util.Utils;

/**
 * Title: 		CostBasisDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class CostBasisDAO extends BaseDBDAO
{
    private static final CostBasisDAO currentInstance = new CostBasisDAO();

    private CostBasisDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return CostBasisDAO
     */
    public static CostBasisDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	@SuppressWarnings("unchecked")
	public List<CostBasis> inquire(Object Info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		List<CostBasis> costBasisList = new ArrayList<CostBasis>();
		
		Investor investor = (Investor)Info;
						
		StringBuffer sbuf = new StringBuffer();
				
		sbuf.append(" select acc.iportfolioId as portfolioID,"); 
		sbuf.append(" trx.iaccountId as accountID,"); 
		sbuf.append(" portf.sportfolioName as portfolioName,"); 
		sbuf.append(" acc.saccountName as accountName,"); 
		sbuf.append(" invm.doptionMultiplier as optionMultiplier,"); 
		sbuf.append(" invm.sdescription as investmentDescription,"); 
		sbuf.append(" trx.iinvestmentId as investmentID,"); 
		sbuf.append(" invm.mcurrentPrice as currentPrice,"); 
		sbuf.append(" asscl.sassetClass as assetClass,"); 
		sbuf.append(" SUM(CASE trxtyp.bpositiveInd");  
		sbuf.append("   WHEN 0 THEN -trx.decUnits WHEN 1 THEN trx.decUnits ELSE 0 END) as unitsOwned"); 
		sbuf.append("  from Tbltransaction trx"); 
		sbuf.append(" INNER JOIN Tblaccount acc on trx.iaccountid = acc.iaccountid"); 
		sbuf.append(" INNER JOIN Tblinvestment invm on trx.iinvestmentid = invm.iinvestmentid");
		sbuf.append(" INNER JOIN Tblassetclass asscl on invm.iassetclassid = asscl.iassetclassid");
		sbuf.append(" INNER JOIN TblPortfolio portf on acc.iportfolioid = portf.iportfolioid"); 
		sbuf.append(" INNER JOIN Tblinvestor invstr on portf.iinvestorid = invstr.iinvestorid"); 
		sbuf.append(" INNER JOIN TbltransactionType trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid"); 
		sbuf.append(" where portf.iinvestorId = ");
		sbuf.append(investor.getInvestorID()); 
		sbuf.append(" and acc.dvestingPercentage > 0"); 
		sbuf.append(" group by acc.iportfolioId,"); 
		sbuf.append("          trx.iaccountId,"); 
		sbuf.append("          portf.sportfolioName,"); 
		sbuf.append("          acc.saccountName,"); 
		sbuf.append("         invm.doptionMultiplier,"); 
		sbuf.append("         invm.sdescription,"); 
		sbuf.append("         trx.iinvestmentId,"); 
		sbuf.append("        invm.mcurrentPrice,"); 
		sbuf.append("        asscl.sassetClass");  
		sbuf.append(" having SUM(CASE trxtyp.bpositiveInd"); 
		sbuf.append("        WHEN 0 THEN -trx.decUnits WHEN 1 THEN trx.decUnits ELSE 0 END) <> 0");
	  
		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					CostBasis costBasisDetail = new CostBasis();
					
					costBasisDetail.setAccountID(rs.getInt(2));
					costBasisDetail.setAccountName(rs.getString(4));
					costBasisDetail.setInvestmentDescription(rs.getString(6));
					costBasisDetail.setInvestmentID(rs.getInt(7));
					costBasisDetail.setPortfolioName(rs.getString(3));
					costBasisDetail.setUnitsOwned(rs.getBigDecimal(10));
					
					log.debug("Account name = " + costBasisDetail.getAccountName());
					log.debug("Investment = " + costBasisDetail.getInvestmentDescription());
					
					BigDecimal currentPrice = rs.getBigDecimal(8);
						
					log.debug("Current Price = " + currentPrice);				
					log.debug("Units Owned = " + costBasisDetail.getUnitsOwned());
					
					costBasisDetail.setCurrentValue(costBasisDetail.getUnitsOwned().multiply(currentPrice));
					
					String assetClass = rs.getString(9);
					
					if (assetClass.equalsIgnoreCase("Options"))
					{
						BigDecimal optMult = rs.getBigDecimal(5);
						costBasisDetail.setCurrentValue(costBasisDetail.getCurrentValue().multiply(optMult));
					}
					log.debug("Current Value = " + costBasisDetail.getCurrentValue());
					
					costBasisList.add(costBasisDetail);				
				}
				return tempList;
		    }
		});				
			
		log.debug("intermediate list size is = " + costBasisList.size());
		
		//Now we have the positions established
		//Loop through each investment in the list and get the transactions for that investment.
		//Establish cost basis through those transactions.
		
		for (int i = 0; i < costBasisList.size(); i++)
		{
			CostBasis costBasisDetail = costBasisList.get(i);
		
			log.debug("About to get cost basis for account = " + costBasisDetail.getAccountName() + " and investment = " + costBasisDetail.getInvestmentDescription());
		
			sbuf.setLength(0); //this clears the stringbuffer
			
			sbuf.append(" select trx.*, trxtyp.sdescription as trxtypedesc, trxtyp.bpositiveind from Tbltransaction trx"); 
			sbuf.append("  INNER JOIN tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
			sbuf.append("  where trx.iinvestmentId = ");
			sbuf.append(costBasisDetail.getInvestmentID().toString());
			sbuf.append("  and trx.iaccountId = ");
			sbuf.append(costBasisDetail.getAccountID().toString());
			sbuf.append("  and trxtyp.sdescription <> 'Cash Dividend'"); 
			sbuf.append("  order by trx.dtransactionDate desc"); 

			Double totalCost = new Double(0);
			Double totalUnits = new Double(0);
			Double costBasis = new Double(0);
			Double gainLoss = new Double(0);

			log.debug("about to run query: " + sbuf.toString());
						
			List<CostBasis> trxList = this.getJdbcTemplate().query(sbuf.toString(), new CostBasisRowMapper());
			
			log.debug("Stop once we hit units = " + costBasisDetail.getUnitsOwned().toString());
			
			for (int j = 0; j < trxList.size(); j++)
			{
				CostBasis trx = trxList.get(j);
				
				if (trx.getTransactionTypeDescription().equalsIgnoreCase("Reinvest"))
				{	
					totalCost = totalCost - trx.getCostProceeds().doubleValue();
					totalUnits = totalUnits + trx.getDecUnits().doubleValue();	
				}
				else
				{
					totalCost = totalCost - trx.getEffectiveAmount().doubleValue();
					
					if (trx.getPositiveInd())
					{
						totalUnits = totalUnits + trx.getDecUnits().doubleValue();	
					}
					else
					{
						totalUnits = totalUnits - trx.getDecUnits().doubleValue();
					}					
				}
				log.debug("total cost so far = " + totalCost);
				log.debug("total units so far = " + totalUnits);
				
				if (totalUnits.compareTo(costBasisDetail.getUnitsOwned().doubleValue()) == 0)
				{
					break;
				}
			}
			
			log.debug("Finished looping on trx for account = " + costBasisDetail.getAccountName() + " and investment = " + costBasisDetail.getInvestmentDescription());
			
			log.debug("Total cost = " + totalCost);
			
			costBasis = totalCost.doubleValue() / costBasisDetail.getUnitsOwned().doubleValue();
			log.debug("cost Basis = " + costBasis);
			
			gainLoss = costBasisDetail.getCurrentValue().doubleValue() - totalCost.doubleValue();
			log.debug("Gain Loss = " + gainLoss);
									
			//now that we have 3 vars totalCost, costBasis and gainLoss, update the current costBasisDetail object.
			costBasisDetail.setTotalCost(new BigDecimal(totalCost));
			costBasisDetail.setGainLoss(new BigDecimal(gainLoss));
			costBasisDetail.setCostBasis(new BigDecimal(costBasis));
			
			costBasisList.set(i, costBasisDetail);
			
		}
		
		log.debug("final list size is = " + costBasisList.size());
		log.debug(methodName + "out");
		return costBasisList;	
	}

}
