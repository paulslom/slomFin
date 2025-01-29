package com.pas.slomfin.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.beans.Payday;

public class PaydayRowMapper implements RowMapper<Payday>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Payday mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Payday payday = new Payday();
	 
		payday.setPaydayID(rs.getInt("iPaydayID"));
		payday.setAccountID(rs.getInt("iAccountID"));
		payday.setAccountName(rs.getString("sAccountName"));
		payday.setTrxTypeDescription(rs.getString("trxTypeDescription"));
		payday.setDefaultAmount(rs.getBigDecimal("mDefaultAmt"));
		payday.setPaydayDescription(rs.getString("paydayDescription"));
		payday.setXferAccountID(rs.getInt("ixferAccountID"));
		payday.setTransactionTypeID(rs.getInt("iTransactionTypeID"));
		payday.setDefaultDay(rs.getInt("iDefaultDay"));
		payday.setNextMonthInd(rs.getBoolean("bNextMonthInd"));
							
 		return payday; 	    	
    }
}
