package com.pas.slomfin.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.pas.dao.BaseDBDAO;
import com.pas.dbobjects.Tbltaxgroupyear;
import com.pas.exception.DAOException;
import com.pas.slomfin.constants.ISlomFinAppConstants;
import com.pas.slomfin.valueObject.CapitalGainTransaction;
import com.pas.slomfin.valueObject.TaxesReport;
import com.pas.slomfin.valueObject.TaxesSelection;
import com.pas.util.DateUtil;
import com.pas.util.Utils;

/**
 * Title: 		CapitalGainsDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class CapitalGainsDAO extends BaseDBDAO
{
	private static final Calendar calTaxDay = Calendar.getInstance();
	
	private static String LONG_TERM = "Long-Term";
	private static String SHORT_TERM = "Short-Term";
	private static String VARIOUS = "Var-";
 
    private static final CapitalGainsDAO currentInstance = new CapitalGainsDAO();

    private CapitalGainsDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return DividendDAO
     */
    public static CapitalGainsDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	public List<TaxesReport> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		List<TaxesReport> cgList = new ArrayList<TaxesReport>();
		
		TaxesSelection cgSelection = (TaxesSelection)info;		
						
		cgList = fedTaxesCapitalGains(cgSelection, null, cgSelection.isTaxableInd());
		
		log.debug("final list size is = " + cgList.size());
		log.debug(methodName + "out");
		return cgList;	
	}
	

	public List<TaxesReport> fedTaxesCapitalGains(TaxesSelection federalTaxesSelection, Tbltaxgroupyear taxGY, boolean taxableInd)
	{
		List<TaxesReport> tList = new ArrayList<TaxesReport>();
		
		List<TaxesReport> cgStocksOnly = fedTaxesCapitalGainsStocksOnly(federalTaxesSelection, taxableInd);
		List<TaxesReport> cgOptionsOnly = fedTaxesCapitalGainsOptionsOnly(federalTaxesSelection);
		
		if (taxGY != null
		&&  taxGY.getMcapitalLossCarryover() != null
		&&  taxGY.getMcapitalLossCarryover() != new BigDecimal(0.0)) //anything other than zero, record it
		{	
			TaxesReport taxesReportDetail = new TaxesReport();
			taxesReportDetail.setTaxDate(new Date(calTaxDay.getTimeInMillis())); //April 15th (tax Day!)	
			taxesReportDetail.setType("Capital Gain");
			taxesReportDetail.setDescription("Capital Loss Carryover");
			taxesReportDetail.setSortOrder(1);	
			taxesReportDetail.setSubSortOrder(5);	
			taxesReportDetail.setGrossAmount(taxGY.getMcapitalLossCarryover().multiply(new BigDecimal(-1.0)));										
			tList.add(taxesReportDetail);
		}
		
		tList.addAll(cgStocksOnly);
		tList.addAll(cgOptionsOnly);
		return tList;
	}
	
	private List<TaxesReport> fedTaxesCapitalGainsOptionsOnly(TaxesSelection federalTaxesSelection)
	{
		log.info("entering fedTaxesCapitalGainsOptionsOnly");
		
		List<TaxesReport> optionsCG = new ArrayList<TaxesReport>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("select portf.sportfolioName as Portfolio,"); 
		sbuf.append(" invm.sdescription as OptionInvestment,"); 
		sbuf.append(" trxOPEN.dtransactionDate as PurchaseDate,"); 
		sbuf.append(" trxCLOSE.dexpirationDate as SaleDate,"); 
		sbuf.append(" trxOPEN.mcostProceeds as CostBasis,"); 
		sbuf.append(" trxCLOSE.mcostProceeds as SaleProceeds,"); 
		sbuf.append(" optTyp.sdescription as OptionType,");
		sbuf.append(" trxCLOSE.mstrikePrice as StrikePrice,"); 
		sbuf.append(" trxOPEN.decUnits as Units,"); 
		sbuf.append(" trxOPENTyp.sdescription as OpeningTrxType,"); 
		sbuf.append(" trxCLOSETyp.sdescription as ClosingTrxType"); 
		sbuf.append(" from Tbltransaction trxCLOSE");
		sbuf.append(" INNER JOIN Tbltransaction trxOPEN on");
		sbuf.append("  (trxOPEN.dexpirationDate = trxCLOSE.dexpirationDate"); 
		sbuf.append("		and trxOPEN.iinvestmentId = trxCLOSE.iinvestmentId"); 
		sbuf.append("		and trxOPEN.decUnits = trxCLOSE.decUnits"); 
		sbuf.append("		and trxOPEN.mstrikePrice = trxCLOSE.mstrikePrice)");
		sbuf.append(" INNER JOIN TbltransactionType trxOPENTyp on trxOPEN.itransactionTypeid = trxOPENTyp.itransactionTypeid"); 
		sbuf.append(" INNER JOIN TbltransactionType trxCLOSETyp on trxCLOSE.itransactionTypeid = trxCLOSETyp.itransactionTypeid"); 
		sbuf.append(" INNER JOIN tbloptiontype optTyp on trxOPEN.iOptionTypeID = optTyp.iOptionTypeID"); 
		sbuf.append(" INNER JOIN tblinvestment invm on trxOPEN.iInvestmentID = invm.iInvestmentID"); 
		sbuf.append(" INNER JOIN tblaccount acct on trxOPEN.iaccountid = acct.iaccountid");
		sbuf.append(" INNER JOIN tblportfolio portf on acct.iportfolioid = portf.iportfolioid");
		sbuf.append(" INNER JOIN tblinvestor invstr on portf.iinvestorid = invstr.iinvestorid");
		sbuf.append(" where trxCLOSETyp.sdescription != 'Exercise Option'"); 
		sbuf.append(" and year(trxCLOSE.dtransactionDate) = ");
		sbuf.append(federalTaxesSelection.getTaxYear());
		sbuf.append(" and invstr.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" and portf.btaxableInd = true");
		sbuf.append(" and trxOPEN.bopeningTrxInd = true"); 
		sbuf.append(" and trxCLOSE.bopeningTrxInd = false"); 
		
		log.debug("about to run query: " + sbuf.toString());
		
		this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					String investmentDesc = "";
					if (rs.getString(1) != null)
					{
						investmentDesc = rs.getString(1);
					}
					
					Date saleDate = new Date();
					if (rs.getDate(4) != null)
					{
						saleDate = rs.getDate(4);
					}
					
					BigDecimal costBasis = new BigDecimal(0.0);
					if (rs.getBigDecimal(5) != null)
					{
						costBasis = rs.getBigDecimal(5);
					}
					
					BigDecimal saleProceeds = new BigDecimal(0.0);
					if (rs.getBigDecimal(6) != null)
					{
						saleProceeds = rs.getBigDecimal(6);
					}
					
					String optionType = "";
					if (rs.getString(7) != null)
					{
						optionType = rs.getString(7);
					}
					
					BigDecimal strikePrice = new BigDecimal(0.0);
					if (rs.getBigDecimal(8) != null)
					{
						strikePrice = rs.getBigDecimal(8);
					}
					
					BigDecimal units = new BigDecimal(0.0);
					if (rs.getBigDecimal(9) != null)
					{
						units = rs.getBigDecimal(9);
					}
					
					String openingtrxType = "";
					if (rs.getString(10) != null)
					{
						openingtrxType = rs.getString(10);
					}
					
					String closingtrxType = "";
					if (rs.getString(11) != null)
					{
						closingtrxType = rs.getString(11);			
					}
					
					log.debug("Option capital gain trx retrieved - specifics are: ");
					log.debug("Investment = " + investmentDesc);			
					log.debug("Sale Date = " + saleDate);
					log.debug("Cost Basis = " + costBasis);
					log.debug("Sale Proceeds = " + saleProceeds);
					log.debug("Option Type = " + optionType);
					log.debug("Strike Price = " + strikePrice);
					log.debug("Units = " + units);
					log.debug("Opening Trx Type = " + openingtrxType);
					log.debug("Closing Trx Type = " + closingtrxType);
					
					BigDecimal gainOrLoss = new BigDecimal(0.0);
					
					if (openingtrxType.equalsIgnoreCase("Sell"))
						gainOrLoss = saleProceeds.add(costBasis);		
					else
						gainOrLoss = saleProceeds.subtract(costBasis);			
					
					StringBuffer cgoDesc = new StringBuffer();
					cgoDesc.append(investmentDesc);
					cgoDesc.append(" ");
					cgoDesc.append(sdf.format(saleDate));
					cgoDesc.append(" ");
					cgoDesc.append(optionType);
					cgoDesc.append("@");
					cgoDesc.append(strikePrice.toString());
					
					log.debug("final description of cg option entry = " + cgoDesc);
					
					TaxesReport taxesReportDetail = new TaxesReport();
					taxesReportDetail.setTaxDate(saleDate);	
					taxesReportDetail.setType("Capital Gain");
					taxesReportDetail.setDescription(cgoDesc.toString());
					taxesReportDetail.setSortOrder(1);	
					taxesReportDetail.setSubSortOrder(5);	
					taxesReportDetail.setGrossAmount(gainOrLoss);										
					optionsCG.add(taxesReportDetail);				        
				}
				return tempList;
		    }
		});		
			
		log.info("exiting fedTaxesCapitalGainsOptionsOnly");
		
		return optionsCG;
	}

	private List<TaxesReport> fedTaxesCapitalGainsStocksOnly(TaxesSelection federalTaxesSelection, boolean taxableInd)
	{
		log.info("entering fedTaxesCapitalGainsStocksOnly");
		
		List<TaxesReport> stocksCG = new ArrayList<TaxesReport>();
		
		//First, get a list of transactions representing sales of Stocks for the year.
		
		StringBuffer sbuf = new StringBuffer();
			
		sbuf.append("SELECT trxINV.dtransactionDate as Date,");
		sbuf.append("   acct.iportfolioId as PortfolioID,");
		sbuf.append("   portf.sportfolioName as PortfolioName,");
		sbuf.append("   trxINV.iaccountId as AccountID,");
		sbuf.append("   acct.saccountName as AccountName,");
		sbuf.append("   trxINV.iinvestmentId as InvestmentID,");
		sbuf.append("   invm.sdescription as InvestmentName,");
		sbuf.append("   trxtyp.sdescription as TrxType,");
		sbuf.append("   trxINV.decUnits as Units,"); 
		sbuf.append("  trxINV.mcostProceeds as CostProceeds,"); 
		sbuf.append("  trxINV.mprice as Price,"); 
		sbuf.append("   invmtyp.sdescription as InvestmentType,"); 
		sbuf.append("  invstr.sfirstName as InvestorFirstName,"); 
		sbuf.append("  invstr.slastName as InvestorLastName");
		sbuf.append(" FROM Tbltransaction trxINV");
		sbuf.append(" INNER JOIN Tbltransactiontype trxtyp on trxINV.itransactiontypeid = trxtyp.itransactiontypeid");
		sbuf.append(" INNER JOIN Tblaccount acct on trxINV.iaccountid = acct.iaccountid");
		sbuf.append(" INNER JOIN Tblportfolio portf on acct.iportfolioid = portf.iportfolioid");
		sbuf.append(" INNER JOIN Tblinvestor invstr on portf.iinvestorid = invstr.iinvestorid");
		sbuf.append(" INNER JOIN Tblinvestment invm on trxINV.iinvestmentid = invm.iinvestmentid");
		sbuf.append(" INNER JOIN Tblinvestmenttype invmtyp on invm.iinvestmenttypeid = invmtyp.iinvestmenttypeid");
		sbuf.append(" WHERE trxtyp.sdescription = 'Sell' ");
		if (taxableInd)
		{
			sbuf.append(" AND portf.btaxableInd = 1");
		}
		else
		{
			sbuf.append(" AND portf.btaxableInd = 0");
		}
		sbuf.append(" AND invmtyp.sdescription IN ('Stock', 'Real Estate', 'Mutual Fund')"); 
		sbuf.append(" AND invstr.itaxGroupId = ");
		sbuf.append(federalTaxesSelection.getTaxGroupID());
		sbuf.append(" AND year(trxINV.dtransactionDate) = ");  
		sbuf.append(federalTaxesSelection.getTaxYear());
		
		log.debug("about to run query: " + sbuf.toString());
		
		//need to loop through this list now to get cap gains for each item.
		//The way to identify the transactions that relate to this sale is:
		//1) by PortfolioID
		//2) by InvestmentID
		
		List salesTrx = this.getJdbcTemplate().query(sbuf.toString(), new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					Integer salePortfolioID = rs.getInt(2);
					String salePortfolioName = rs.getString(3);
					Integer saleInvestmentID = rs.getInt(6);
					String saleInvestmentDesc = rs.getString(7);
					String saleInvestmentType = rs.getString(12);
					Calendar calSaleDate = Calendar.getInstance();
					calSaleDate.setTime(rs.getDate(1));
					BigDecimal saleUnits = rs.getBigDecimal(9);
					BigDecimal saleProceeds = rs.getBigDecimal(10);
					BigDecimal salePrice = rs.getBigDecimal(11);
					String investorName = rs.getString(13) + " " + rs.getString(14);
					
					log.debug("about to figure capital gains for " + saleInvestmentDesc + " for portfolio = " + salePortfolioName);
					
					//This next query is to retrieve transactions to be used to determine the cost basis.
					StringBuffer sbuf2 = new StringBuffer();
					
					sbuf2.append(" SELECT trx.dtransactionDate as trxDate,");   
					sbuf2.append("  acct.iportfolioId as PortfolioID,");   
					sbuf2.append("  portf.sportfolioName as PortfolioName,");   
					sbuf2.append("  trx.iaccountId as AccountID,");   
					sbuf2.append("  acct.saccountName as AccountName,");   
					sbuf2.append("  trx.iinvestmentId as InvestmentID,");    
					sbuf2.append("  invm.sdescription as InvestmentName,");    
					sbuf2.append("  trxtyp.sdescription as TrxType, ");  
					sbuf2.append("  trx.decUnits as Units,");   
					sbuf2.append("  trx.mcostProceeds as CostProceeds,");   
					sbuf2.append("  trx.mprice as Price ");
					sbuf2.append("  from Tbltransaction trx ");
					sbuf2.append("  INNER JOIN Tblaccount acct on trx.iaccountid = acct.iaccountid");
					sbuf2.append("  INNER JOIN Tblinvestment invm on trx.iinvestmentid = invm.iinvestmentid");
					sbuf2.append("  INNER JOIN Tblportfolio portf on acct.iportfolioid = portf.iportfolioid");
					sbuf2.append("  INNER JOIN Tbltransactiontype trxtyp on trx.itransactiontypeid = trxtyp.itransactiontypeid");
					sbuf2.append("  WHERE trxtyp.sdescription NOT IN ('Cash Dividend', 'Transfer In', 'Transfer Out')"); 
					sbuf2.append(" AND acct.iportfolioId =");  
					sbuf2.append(salePortfolioID);
					sbuf2.append(" AND trx.iinvestmentId = ");  
					sbuf2.append(saleInvestmentID);
					sbuf2.append(" AND trx.dtransactionDate <= '");  
					sbuf2.append(calSaleDate.get(Calendar.YEAR));
					if (calSaleDate.get(Calendar.MONTH) < Calendar.OCTOBER)
					{
						sbuf2.append("0");
					}
					sbuf2.append(calSaleDate.get(Calendar.MONTH) + 1); 
					if (calSaleDate.get(Calendar.DAY_OF_MONTH) < 10)
					{
						sbuf2.append("0");
					}
					sbuf2.append(calSaleDate.get(Calendar.DAY_OF_MONTH)); 
					sbuf2.append("' ORDER BY trx.dtransactionDate");
					
					log.debug("about to run query: " + sbuf2.toString());				
					
					//Turn the object array that came back from this query into
					//a list of CapitalGainTransaction Objects.
					
					List<CapitalGainTransaction> cgTrxList = new ArrayList<CapitalGainTransaction>();
					
					JdbcTemplate tempJdbcTemplate = new JdbcTemplate(currentInstance.getDatasource());
					
					tempJdbcTemplate.query(sbuf2.toString(), new ResultSetExtractor<List>() 
					{	   
						@Override
					    public List extractData(ResultSet rs2) throws SQLException, DataAccessException 
					    {
							List tempList = new ArrayList<>();
							
							while (rs2.next()) 
							{
								CapitalGainTransaction cgTrx = new CapitalGainTransaction();
								
								cgTrx.setAccountID(rs2.getInt(4));
								cgTrx.setAccountName(rs2.getString(5));
								cgTrx.setCgDate(rs2.getDate(1));
								cgTrx.setCostProceeds(rs2.getBigDecimal(10));
								cgTrx.setInvestmentID(rs2.getInt(6));
								cgTrx.setInvestmentName(rs2.getString(7));
								cgTrx.setPortfolioID(rs2.getInt(2));
								cgTrx.setPortfolioName(rs2.getString(3));
								cgTrx.setTransactionType(rs2.getString(8));
								cgTrx.setUnits(rs2.getBigDecimal(9));
								cgTrx.setPrice(rs2.getBigDecimal(11));
								
								cgTrxList.add(cgTrx);	        
							}
							return tempList;
					    }
					});
										
					//need to remove all transactions from this list
					//that have already been accounted for either in prior years or this year
					//First task - identify the sales and units associated
					
					BigDecimal totalUnitsFromSales = new BigDecimal(0.0);
					totalUnitsFromSales = totalUnitsFromSales.setScale(4, java.math.RoundingMode.HALF_UP); 
					
					List<CapitalGainTransaction> salesToRemoveList = new ArrayList<CapitalGainTransaction>();
					
					log.debug("removing Sales trx and tallying units from those sales");
					
					for (int j = 0; j < cgTrxList.size(); j++)
					{
						CapitalGainTransaction invTrx = cgTrxList.get(j);
						
						if (invTrx.getTransactionType().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))
							if (invTrx.getCgDate().getTime() < calSaleDate.getTimeInMillis())
						    {
							   totalUnitsFromSales = totalUnitsFromSales.add(invTrx.getUnits());
							   salesToRemoveList.add(invTrx);
							   log.debug("found a sale to remove");
							   log.debug("Trx number = " + (j+1));
							   log.debug("Trx Date = " + invTrx.getCgDate());
							   log.debug("Trx Type = " + invTrx.getTransactionType());
							   log.debug("Units = " + invTrx.getUnits());
						    }
						
					}
					
					cgTrxList.removeAll(salesToRemoveList);
					log.debug("total transactions for " + saleInvestmentDesc + " after removing sales trx = " + cgTrxList.size());
					
					if (totalUnitsFromSales.compareTo(new BigDecimal(0.0)) > 0) //if nothing to remove, skip this part
					{	
						BigDecimal unitsRemovalTally = new BigDecimal(0.0);		
						unitsRemovalTally = unitsRemovalTally.setScale(4, java.math.RoundingMode.HALF_UP); 
					
						List<CapitalGainTransaction> trxToRemoveList = new ArrayList<CapitalGainTransaction>();
						
						log.debug("removing buy and reinvest trx");
						log.debug("need to find " + totalUnitsFromSales + " units to remove");
						
						BigDecimal unitsAdjustment = new BigDecimal(0.0);
						BigDecimal newUnits =  new BigDecimal(0.0);
						
						for (int j = 0; j < cgTrxList.size(); j++)
						{
							CapitalGainTransaction purchTrx = cgTrxList.get(j);
							
							unitsRemovalTally = unitsRemovalTally.add(purchTrx.getUnits());
							
							log.debug("Trx number = " + (j+1));
							log.debug("Trx Date = " + purchTrx.getCgDate());
							log.debug("Trx Type = " + purchTrx.getTransactionType());
							log.debug("Units = " + purchTrx.getUnits());
							log.debug("Units removal tally = " + unitsRemovalTally);	
							
							if (unitsRemovalTally.compareTo(totalUnitsFromSales) < 0) //simple removal, haven't reached total
							{
								trxToRemoveList.add(purchTrx);
							}
							else if (unitsRemovalTally.compareTo(totalUnitsFromSales) == 0) //remove and leave loop; reached total
							{
								trxToRemoveList.add(purchTrx);
								break;
							}
							else //adjust the list item 
							{
								unitsAdjustment = unitsAdjustment.subtract(totalUnitsFromSales);
								newUnits = unitsRemovalTally.subtract(totalUnitsFromSales);
								
								purchTrx.setUnits(newUnits);
								purchTrx.setCostProceeds(newUnits.multiply(purchTrx.getPrice()));
								
								cgTrxList.set(j, purchTrx);
								
								log.debug("new Units = " + purchTrx.getUnits());
								log.debug("new Cost/Proceeds = " + purchTrx.getCostProceeds());				
								
								break;
							}		
							
						}
						
						if (trxToRemoveList.size() > 0)
						{
							cgTrxList.removeAll(trxToRemoveList);
							log.debug("total transactions for " + saleInvestmentDesc + " after removing buy and reinvest trx = " + cgTrxList.size());
						}
					}
					
					//Need to identify any Option exercise transactions.
					//These will influence the cost basis.
					//Only do this for Stock investment type.
					
					if (saleInvestmentType.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_STOCK))
					{	
						Calendar calLowDate = Calendar.getInstance();
						CapitalGainTransaction lowTrx = cgTrxList.get(0);
						Date lowDate = lowTrx.getCgDate();
						calLowDate.setTime(lowDate);
						
						Calendar calHighDate = Calendar.getInstance();
						CapitalGainTransaction highTrx = cgTrxList.get(cgTrxList.size()-1);
						Date highDate = highTrx.getCgDate();
						calHighDate.setTime(highDate);
						
						sbuf2.setLength(0); //clear buffer				
					   	
						sbuf2.append("select trxEXER.dexpirationDate as ExerciseDate,"); 
						sbuf2.append(" trxEXER.bopeningTrxInd as ExerciseOpenerInd,"); 
						sbuf2.append(" trxOPEN.bopeningTrxInd as OpenerOpenerInd,"); 
						sbuf2.append(" trxOPEN.meffectiveAmount as OpeningEffectiveAmount,"); 
						sbuf2.append(" trxOPENOptTyp.sdescription as OptionType,"); 
						sbuf2.append(" trxEXER.mstrikePrice as StrikePrice,"); 
						sbuf2.append(" trxEXERInvm.sdescription as OptionInvestment"); 
						sbuf2.append(" from Tbltransaction trxEXER");
						sbuf2.append(" INNER JOIN Tbltransaction trxOPEN on");
						sbuf2.append("	(trxOPEN.dexpirationDate = trxEXER.dexpirationDate"); 
						sbuf2.append("	 and trxOPEN.iinvestmentId = trxEXER.iinvestmentId"); 
						sbuf2.append("	 and trxOPEN.decUnits = trxEXER.decUnits"); 
						sbuf2.append("     and trxOPEN.mstrikePrice = trxEXER.mstrikePrice)");
						sbuf2.append(" INNER JOIN TbltransactionType trxEXERTyp on trxEXER.itransactionTypeid = trxEXERTyp.itransactionTypeid");
						sbuf2.append(" INNER JOIN tbloptiontype trxOPENOptTyp on trxOPEN.iOptionTypeID = trxOPENOptTyp.iOptionTypeID");
						sbuf2.append(" INNER JOIN tblinvestment trxEXERInvm on trxEXER.iInvestmentID = trxEXERInvm.iInvestmentID");
						sbuf2.append(" where trxEXERTyp.sdescription = 'Exercise Option'");
						sbuf2.append(" and trxEXER.dtransactionDate between '");
						sbuf2.append(calLowDate.get(Calendar.YEAR));
						if (calLowDate.get(Calendar.MONTH) < 10)
							sbuf2.append("0");
						sbuf2.append(calLowDate.get(Calendar.MONTH) + 1); 
						if (calLowDate.get(Calendar.DAY_OF_MONTH) < 10)
							sbuf2.append("0");
						sbuf2.append(calLowDate.get(Calendar.DAY_OF_MONTH)); 			
						sbuf2.append("' and '");
						sbuf2.append(calHighDate.get(Calendar.YEAR));
						if (calHighDate.get(Calendar.MONTH) < 10)
							sbuf2.append("0");
						sbuf2.append(calHighDate.get(Calendar.MONTH) + 1); 
						if (calHighDate.get(Calendar.DAY_OF_MONTH) < 10)
							sbuf2.append("0");
						sbuf2.append(calHighDate.get(Calendar.DAY_OF_MONTH)); 		
						sbuf2.append("' and trxOPEN.bopeningTrxInd = true"); 
						sbuf2.append(" and trxEXER.iinvestmentId IN"); 
						sbuf2.append("    (select us.iinvestmentId as OptionInvestmentID ");
						sbuf2.append("       from Tblunderlyingstocks us ");
						sbuf2.append("       where us.iUnderlyingInvID = ");
						sbuf2.append(saleInvestmentID);
						sbuf2.append(")");
						sbuf2.append(" order by trxEXER.dexpirationDate");
						
						log.debug("about to run query: " + sbuf2.toString());
						
						JdbcTemplate tempJdbcTemplate2 = new JdbcTemplate(currentInstance.getDatasource());
						
						List exerciseList = tempJdbcTemplate2.query(sbuf2.toString(), new ResultSetExtractor<List>() 
						{	   
							@Override
						    public List extractData(ResultSet rs3) throws SQLException, DataAccessException 
						    {
								List tempList = new ArrayList<>();
								
								while (rs3.next()) 
								{	
									BigDecimal saleProceeds = new BigDecimal(0.0);
									
									Date exerciseDate = rs3.getDate(1);
									BigDecimal strikePrice = rs3.getBigDecimal(6);
									String optionType = rs3.getString(5);
									
									if (rs3.getBigDecimal(4) != null)
									{	
										BigDecimal optionAdjustmentAmt = rs3.getBigDecimal(4);
										log.debug("Adjusting for exercise trx = exerciseDate = " + exerciseDate);
										log.debug("Strike price is = " + strikePrice);
										log.debug("Option Type = " + optionType);
										
										//find the trx to adjust the exercise amount on..
										for (int k = 0; k < cgTrxList.size(); k++)
										{
											boolean thisIsTheOne = true;
											
											CapitalGainTransaction cgt = cgTrxList.get(k);
											
											if (strikePrice.compareTo(cgt.getPrice()) != 0)
											{
												thisIsTheOne = false;
											}
											
										    int dateDiff = DateUtil.getDifferenceInDays(exerciseDate, cgt.getCgDate());
											
										    if (dateDiff > 5)
										    {
										    	thisIsTheOne = false;
										    }
										    
										    if (optionType.equalsIgnoreCase(ISlomFinAppConstants.OPTIONTYPE_PUT))
										    {	
										    	if (cgt.getTransactionType().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))
										    	{
										    		thisIsTheOne = false;
										    	}
										    }
										    
										    if (optionType.equalsIgnoreCase(ISlomFinAppConstants.OPTIONTYPE_CALL))
										    {	
										    	if (cgt.getTransactionType().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_BUY))
										    	{
										    		thisIsTheOne = false;
										    	}
										    }
										    
											if (thisIsTheOne)
											{
												if (cgt.getTransactionType().equalsIgnoreCase(ISlomFinAppConstants.TRXTYP_SELL))
												{
													cgt.setCostProceeds(cgt.getCostProceeds().add(optionAdjustmentAmt));
												}
												else
												{
													cgt.setCostProceeds(cgt.getCostProceeds().subtract(optionAdjustmentAmt));
												}
												
												//this might also be the actual sale trx we're working on...
												//let's find out.  If it is, update that also.
												boolean thisIsTheSale = false;
												
												if (strikePrice.compareTo(salePrice) != 0)
												{
													thisIsTheSale = false;
												}
												
											    int dateDiff2 = DateUtil.getDifferenceInDays(exerciseDate, new Date(calSaleDate.getTimeInMillis()));
												
											    if (dateDiff2 > 5)
											    {
											    	thisIsTheSale = false;
											    }
											    
											    if (optionType.equalsIgnoreCase(ISlomFinAppConstants.OPTIONTYPE_PUT))
											    {
											    	thisIsTheSale = false;
											    }
											 						 
												if (thisIsTheSale)
												{
													saleProceeds = saleProceeds.add(optionAdjustmentAmt);
												}
														
												break;
											}
										}
										log.debug("Cost Proceeds adjusted by option transaction by amount = " + optionAdjustmentAmt);
									}	
								        
								}
								return tempList;
						    }
						});	
						
						log.debug("exercise list row count = " + exerciseList.size());
						
					}
					
					//finally execute loop to determine cap gains/losses
					BigDecimal unitsTally = new BigDecimal(0.0);
					BigDecimal costBasisTally = new BigDecimal(0.0);
					BigDecimal gainOrLoss = new BigDecimal(0.0);
					String holdingPeriod = "";
					
					int holdingPeriodDiffInDays = 0;
					
					for (int j = 0; j < cgTrxList.size(); j++)
					{
						CapitalGainTransaction cgTrx = cgTrxList.get(j);
						
						unitsTally = unitsTally.add(cgTrx.getUnits());
						
						holdingPeriodDiffInDays = DateUtil.getDifferenceInDays(new Date(calSaleDate.getTimeInMillis()),cgTrx.getCgDate());
						
						BigDecimal tempCostProceeds = new BigDecimal(0.0);
						
						if (cgTrx.getCostProceeds() != null)
						{
							tempCostProceeds = cgTrx.getCostProceeds();
						}
						
						String oldHoldingPeriod = holdingPeriod;
						holdingPeriod = setHoldingPeriod(oldHoldingPeriod,holdingPeriodDiffInDays);
										
						if (unitsTally.compareTo(saleUnits) < 0) //haven't reached total
						{
							if (oldHoldingPeriod.contains(LONG_TERM) &&  holdingPeriod.contains(SHORT_TERM))
							{
								//This means you have to record the long-term row now, the short term one later.
								
								BigDecimal tempUnitsTally = unitsTally.subtract(cgTrx.getUnits()); 
								BigDecimal itemizedSaleProceeds = tempUnitsTally.multiply(salePrice);
								gainOrLoss = itemizedSaleProceeds.subtract(costBasisTally);
														
								TaxesReport taxesReportDetail = createTRD(saleInvestmentDesc, investorName, saleInvestmentType, calSaleDate, 					
									                                      itemizedSaleProceeds, gainOrLoss, tempUnitsTally, salePrice, oldHoldingPeriod);
								stocksCG.add(taxesReportDetail);
								
								//reset these now for short-term row later..
								costBasisTally = tempCostProceeds; 
								unitsTally = cgTrx.getUnits();
								saleUnits = saleUnits.subtract(tempUnitsTally);
								saleProceeds = saleProceeds.subtract(itemizedSaleProceeds);  
							}
							else //not transitioning, just keep going...
							{
								costBasisTally = costBasisTally.add(tempCostProceeds);
							}
							continue;
						}				
						else if (unitsTally.compareTo(saleUnits) == 0) //reached total exactly
						{				
							costBasisTally = costBasisTally.add(tempCostProceeds);
							gainOrLoss = saleProceeds.subtract(costBasisTally);
						}
						else //you're over...take the part of the trx you need....
						{
							//how much did we need?
							unitsTally = unitsTally.subtract(cgTrx.getUnits()); //reset now that we know we're over
							BigDecimal neededUnits = saleUnits.subtract(unitsTally);
							BigDecimal partialCostBasis = neededUnits.multiply(cgTrx.getPrice());
							costBasisTally = costBasisTally.add(partialCostBasis);
							gainOrLoss = saleProceeds.subtract(costBasisTally);
						}
						
						//jump out on real estate trx >= 2 years - no cap gains on those
						if (saleInvestmentType.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_REALESTATE)
						&&  holdingPeriodDiffInDays >= ISlomFinAppConstants.CAPITAL_GAIN_LONG_TERM_REALESTATE)
						{	
							break;
						}
						
						TaxesReport taxesReportDetail = createTRD(saleInvestmentDesc, investorName, saleInvestmentType, calSaleDate, saleProceeds, gainOrLoss, saleUnits, salePrice, holdingPeriod);		
										
						stocksCG.add(taxesReportDetail);
						
						break;
					}				
				        
				}
				return tempList;
		    }
		});
		
		log.info("exiting fedTaxesCapitalGainsStocksOnly");
		
		return stocksCG;
	
	}

	private String setHoldingPeriod(String oldHoldingPeriod, int holdingPeriodDiffInDays)
	{
		//note: we are always moving from long-term to short term in the loop, so no need to worry about if it was short-term now it is long term...just the reverse.
		
		StringBuffer hp = new StringBuffer();
		
		if (holdingPeriodDiffInDays >= ISlomFinAppConstants.CAPITAL_GAIN_LONG_TERM_STOCKS)
		{
			if (oldHoldingPeriod.contains(LONG_TERM)) 
			{
				hp.append(VARIOUS + LONG_TERM); //means various dates				
			}
			else //must be empty; first time in
			{
				hp.append(LONG_TERM); 
			}
		}
		else //short term (less than 365 days held_
		{
			if (oldHoldingPeriod.contains(SHORT_TERM)) 
			{
				hp.append(VARIOUS + SHORT_TERM); //means various dates				
			}			
			else //nothing was there previously, just make it short term.
			{	
				hp.append(SHORT_TERM); //means various dates
			}
		}
		
		return hp.toString();
	}

	private TaxesReport createTRD(String saleInvestmentDesc, String investorName, String saleInvestmentType, Calendar calSaleDate, BigDecimal saleProceeds, BigDecimal gainOrLoss,
			                        BigDecimal saleUnits, BigDecimal salePrice, String holdingPeriod) 
	{
		TaxesReport trd = new TaxesReport();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		StringBuffer cgsDesc = new StringBuffer();
		cgsDesc.append(saleInvestmentDesc);
		cgsDesc.append(" ");
		cgsDesc.append(sdf.format(new Date(calSaleDate.getTimeInMillis())));
		cgsDesc.append(" ");
		
		if (saleInvestmentType.equalsIgnoreCase(ISlomFinAppConstants.INVTYP_REALESTATE))
		{
			cgsDesc.append("at ");
			cgsDesc.append(saleProceeds.toString());
		}
		else
		{	
			cgsDesc.append(saleUnits);
			cgsDesc.append(" units at ");
			cgsDesc.append(salePrice.toString());
			cgsDesc.append(" ");
			cgsDesc.append(holdingPeriod.toString());
		}
		
		log.debug("final description of cg stock/real estate entry = " + cgsDesc);
		
		trd.setTaxDate(new Date(calSaleDate.getTimeInMillis()));	
		trd.setInvestorName(investorName);
		trd.setType("Capital Gain");
		trd.setDescription(cgsDesc.toString());
		trd.setSortOrder(1);	
		trd.setSubSortOrder(5);	
		trd.setGrossAmount(gainOrLoss);
		
		return trd;
	}

}
