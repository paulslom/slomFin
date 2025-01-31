package com.pas.slomfin.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.beans.Investment;

public class InvestmentsRowMapper implements RowMapper<Investment>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Investment mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Investment investment = new Investment();
				
		investment.setInvestmentID(rs.getInt("iInvestmentID"));
		investment.setInvestmentTypeID(rs.getInt("iInvestmentTypeID"));
		investment.setTickerSymbol(rs.getString("sTickerSymbol"));
		investment.setDescription(rs.getString("sDescription"));
		investment.setCurrentPrice(rs.getBigDecimal("mCurrentPrice"));
		investment.setAssetClassID(rs.getInt("iAssetClassID"));
		investment.setInvestmentTypeDescription(rs.getString("investmentTypeDescription"));
		investment.setAssetClass(rs.getString("sAssetClass"));
					
 		return investment; 	    	
    }
}
