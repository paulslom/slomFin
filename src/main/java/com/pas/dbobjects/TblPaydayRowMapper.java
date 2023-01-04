package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblPaydayRowMapper implements RowMapper<Tblpayday>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblpayday mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblpayday tblpayday = new Tblpayday();
		tblpayday.setBnextMonthInd(rs.getInt("bnextMonthInd"));
		tblpayday.setIaccountId(rs.getInt("iaccountId"));
		tblpayday.setIcashdepositTypeId(rs.getInt("icashdepositTypeId"));
		
		if (rs.getInt("idefaultDay") == 0)
		{
			tblpayday.setIdefaultDay(null);
		}
		else
		{
			tblpayday.setIdefaultDay(rs.getInt("idefaultDay"));
		}
		
		tblpayday.setIinvestorId(rs.getInt("iinvestorId"));
		tblpayday.setIpaydayId(rs.getInt("ipaydayId"));
		tblpayday.setItransactionTypeId(rs.getInt("itransactionTypeId"));
		tblpayday.setIwdcategoryId(rs.getInt("iwdcategoryId"));
		tblpayday.setIxferAccountId(rs.getInt("ixferAccountId"));
		tblpayday.setMdefaultAmt(rs.getBigDecimal("mdefaultAmt"));
		tblpayday.setSdescription(rs.getString("paydayDescription"));
			
		tblpayday.setTblcashdeposittype(new TblCashDepositTypeRowMapper().mapRow(rs, rowNum));		
		tblpayday.setTbltransactiontype(new TblTransactionTypeRowMapper().mapRow(rs, rowNum));		
		tblpayday.setTblinvestor(new TblInvestorRowMapper().mapRow(rs, rowNum));		
		tblpayday.setTblaccountByIAccountId(new TblAccountRowMapper().mapRow(rs, rowNum));
		tblpayday.setTblwdcategory(new TblWDCategoryRowMapper().mapRow(rs, rowNum));
		
		Tblaccount tblaccountByIXferAccountId = new Tblaccount();
		tblaccountByIXferAccountId.setIaccountId(rs.getInt("xferiaccountID"));
		tblaccountByIXferAccountId.setSaccountName(rs.getString("xfersaccountName"));
		tblaccountByIXferAccountId.setSaccountNameAbbr(rs.getString("xfersaccountNameAbbr"));
		tblpayday.setTblaccountByIXferAccountId(tblaccountByIXferAccountId);		
		
 		return tblpayday; 	    	
    }
}
