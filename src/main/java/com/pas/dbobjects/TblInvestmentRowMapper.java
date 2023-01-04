package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblInvestmentRowMapper implements RowMapper<Tblinvestment>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblinvestment mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblinvestment tblinvestment = new Tblinvestment();
		tblinvestment.setIinvestmentId(rs.getInt("iinvestmentId"));
		tblinvestment.setIassetClassId(rs.getInt("iassetClassId"));
		tblinvestment.setIinvestmentTypeId(rs.getInt("iinvestmentTypeId"));
		tblinvestment.setDdividendRate(rs.getBigDecimal("ddividendRate"));
		tblinvestment.setDoptionMultiplier(rs.getBigDecimal("doptionMultiplier"));
		tblinvestment.setDquoteDate(rs.getDate("dquoteDate"));
		tblinvestment.setIdividendsPerYear(rs.getInt("idividendsPerYear"));
		tblinvestment.setMcurrentPrice(rs.getBigDecimal("mcurrentPrice"));
		tblinvestment.setSdescription(rs.getString("sdescription"));
		tblinvestment.setSstockChart(rs.getString("sstockChart"));
		tblinvestment.setSstockLogo(rs.getString("sstockLogo"));
		tblinvestment.setStickerSymbol(rs.getString("stickerSymbol"));		
		
		tblinvestment.setTblassetclass(new TblassetclassRowMapper().mapRow(rs, rowNum));		
		tblinvestment.setTblinvestmenttype(new TblInvestmentTypeRowMapper().mapRow(rs, rowNum));		
		
 		return tblinvestment; 	    	
    }
}
