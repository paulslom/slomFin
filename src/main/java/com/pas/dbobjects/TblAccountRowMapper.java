package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblAccountRowMapper implements RowMapper<Tblaccount>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblaccount mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblaccount tblaccount = new Tblaccount();
		tblaccount.setIaccountId(rs.getInt("iaccountid"));
		tblaccount.setIportfolioId(rs.getInt("iportfolioId"));
		tblaccount.setIaccountTypeId(rs.getInt("iaccounttypeid"));
		tblaccount.setIbrokerId(rs.getInt("ibrokerId"));
		tblaccount.setBclosed(rs.getBoolean("bclosed"));
		tblaccount.setBtaxableInd(rs.getBoolean("btaxableInd"));
		tblaccount.setDestimatedRateofReturn(rs.getBigDecimal("destimatedRateofReturn"));
		tblaccount.setDinterestRate(rs.getBigDecimal("dinterestRate"));
		tblaccount.setDvestingPercentage(rs.getBigDecimal("dvestingPercentage"));
		tblaccount.setIinterestPaymentsPerYear(rs.getInt("iinterestPaymentsPerYear"));
		tblaccount.setIstartingCheckNo(rs.getInt("istartingCheckNo"));
		tblaccount.setMminInterestBalance(rs.getBigDecimal("mminInterestBalance"));
		tblaccount.setMnewMoneyPerYear(rs.getBigDecimal("mnewMoneyPerYear"));
		tblaccount.setSaccountName(rs.getString("saccountName"));
		tblaccount.setSaccountNameAbbr(rs.getString("saccountNameAbbr"));
		tblaccount.setSaccountNumber(rs.getString("saccountNumber"));
		tblaccount.setSpin(rs.getString("spin"));
		
		tblaccount.setTblaccounttype(new TblAccountTypeRowMapper().mapRow(rs, rowNum));		
		tblaccount.setTblbroker(new TblBrokerRowMapper().mapRow(rs, rowNum));
	
		Tblportfolio tblportfolio = new Tblportfolio();
		tblportfolio.setIportfolioId(rs.getInt("iportfolioId"));
		tblportfolio.setBtaxableInd(rs.getBoolean("btaxableInd"));
		tblportfolio.setSportfolioName(rs.getString("sportfolioName"));
		tblaccount.setTblportfolio(tblportfolio);
		
 		return tblaccount; 	    	
    }
}
