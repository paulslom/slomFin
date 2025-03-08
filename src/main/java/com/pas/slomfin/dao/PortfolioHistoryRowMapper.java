package com.pas.slomfin.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.beans.PortfolioHistory;
import com.pas.dynamodb.DateToStringConverter;

public class PortfolioHistoryRowMapper implements RowMapper<PortfolioHistory>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public PortfolioHistory mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		PortfolioHistory portfolioHistory = new PortfolioHistory();
			    
		portfolioHistory.setiPortfolioHistoryID(rowNum);
		portfolioHistory.setHistoryDate(rs.getString("dHistoryDate"));
		portfolioHistory.setTotalValue(rs.getBigDecimal("totalValue"));
							
 		return portfolioHistory; 	    	
    }
}
