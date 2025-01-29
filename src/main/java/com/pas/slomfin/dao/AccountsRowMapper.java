package com.pas.slomfin.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.beans.Account;

public class AccountsRowMapper implements RowMapper<Account>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Account account = new Account();
		
	    account.setiAccountID(rs.getInt("iAccountID"));
	    account.setiPortfolioID(rs.getInt("iPortfolioID"));
	    account.setiAccountTypeID(rs.getInt("iAccountTypeID"));
	    account.setsAccountName(rs.getString("sAccountName"));
	    account.setsAccountNameAbbr(rs.getString("sAccountNameAbbr"));
	    account.setbClosed(rs.getBoolean("bClosed"));
	    account.setsPortfolioName(rs.getString("sPortfolioName"));
	    account.setbTaxableInd(rs.getBoolean("bTaxableInd"));
	    account.setsAccountType(rs.getString("sAccountType"));
					
 		return account; 	    	
    }
}
