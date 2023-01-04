package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblPortfolioRowMapper implements RowMapper<Tblportfolio>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblportfolio mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblportfolio tblportfolio = new Tblportfolio();
		
		tblportfolio.setIportfolioId(rs.getInt("iportfolioId"));
		tblportfolio.setBtaxableInd(rs.getBoolean("btaxableInd"));
		tblportfolio.setSportfolioName(rs.getString("sportfolioName"));	
		
		tblportfolio.setTblinvestor(new TblInvestorRowMapper().mapRow(rs, rowNum));
		
 		return tblportfolio; 	    	
    }
}
