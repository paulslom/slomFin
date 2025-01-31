package com.pas.slomfin.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.beans.PortfolioHistory;

public class PortfolioHistoryRowMapper implements RowMapper<PortfolioHistory>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public PortfolioHistory mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		PortfolioHistory portfolioHistory = new PortfolioHistory();
			    
		portfolioHistory.setPortfolioHistoryID(rs.getInt("iPortfolioHistoryID"));
		portfolioHistory.setAccountID(rs.getInt("iAccountID"));
		portfolioHistory.setAccountName(rs.getString("sAccountName"));
		portfolioHistory.setHistoryDate(rs.getTimestamp("dHistoryDate").toString());
		portfolioHistory.setValue(rs.getBigDecimal("mValue"));
							
 		return portfolioHistory; 	    	
    }
}
