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
import com.pas.dbobjects.Tbltransaction;
import com.pas.exception.DAOException;
import com.pas.slomfin.util.FinancialUtilities;
import com.pas.slomfin.valueObject.Dividend;
import com.pas.slomfin.valueObject.DividendSelection;
import com.pas.slomfin.valueObject.TaxesReport;
import com.pas.util.Utils;

/**
 * Title: 		DividendDAO
 * Project: 	Slomkowski Financial Application
 * Description: Mortgage DAO extends BaseDBDAO. Implements the data access to the tblMortgageHistory table
 * Copyright: 	Copyright (c) 2007
 */
public class DividendsDAO extends BaseDBDAO
{
    private static final DividendsDAO currentInstance = new DividendsDAO();

    private DividendsDAO()
    {
        super();
    }
    /**
     * Returns the singleton instance of this class
     * 
     * @return DividendDAO
     */
    public static DividendsDAO getDAOInstance() 
    {
     	currentInstance.setDataSource(Utils.getDatasourceProperties());
    	currentInstance.setJdbcTemplate(new JdbcTemplate(currentInstance.getDatasource()));
    	currentInstance.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(currentInstance.getDatasource()));  
 
        return currentInstance;
    }
    	
	@SuppressWarnings("unchecked")
	public List<Dividend> inquire(Object info) throws DAOException 
	{
		final String methodName = "inquire::";
		log.debug(methodName + "in");		
		
		List<Dividend> dividendList = new ArrayList<Dividend>();
		
		DividendSelection dividendSelection = (DividendSelection)info;		
		
		Integer requestedYear = dividendSelection.getDividendYear();	
		boolean taxableOnly = false;
		if (dividendSelection.getTaxableOnly() != null && dividendSelection.getTaxableOnly().equalsIgnoreCase("Yes"))
		{
			taxableOnly = true;
		}
		
		FinancialUtilities finUtil = new FinancialUtilities();
		String queryToRun = finUtil.dividendsQuery(requestedYear, taxableOnly, dividendSelection.getTaxGroupID());	
		log.debug("about to run query: " + queryToRun);
		
		this.getJdbcTemplate().query(queryToRun, new ResultSetExtractor<List>() 
		{	   
			@Override
		    public List extractData(ResultSet rs) throws SQLException, DataAccessException 
		    {
				List tempList = new ArrayList<>();
				
				while (rs.next()) 
				{
					Dividend dividendDetail = new Dividend();
					
					dividendDetail.setAccountID(rs.getInt("IaccountId"));
					dividendDetail.setAccountName(rs.getString("SaccountNameAbbr"));
					dividendDetail.setDividendDate(rs.getDate("DtranPostedDate"));
					dividendDetail.setDividendPayableYear(String.valueOf(rs.getInt("IdividendTaxableYear")));
					
					String description = rs.getString("invdesc");
					if (description != null && description.equalsIgnoreCase("Cash"))
					{
						description = rs.getString("invdesc");
					}
					dividendDetail.setInvestmentDescription(description);
					
					dividendDetail.setInvestmentID(rs.getInt("IinvestmentId"));
					dividendDetail.setCostProceeds(rs.getBigDecimal("McostProceeds"));
					dividendDetail.setPortfolioName(rs.getString("SportfolioName"));
					
					boolean taxable = rs.getBoolean("bTaxableInd");
					String taxableYN = "";
					if (taxable)
					{
						taxableYN = "Y";					
					}
					else
					{
						taxableYN = "N";
					}
					dividendDetail.setTaxableYN(taxableYN);
					
					dividendDetail.setUnits(rs.getBigDecimal("DecUnits"));
											
					dividendList.add(dividendDetail);
				}
				return tempList;
		    }
		});		
				
		log.debug("final list size is = " + dividendList.size());
		log.debug(methodName + "out");
		return dividendList;	
	}

}
