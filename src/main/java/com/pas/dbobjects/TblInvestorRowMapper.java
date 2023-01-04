package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblInvestorRowMapper implements RowMapper<Tblinvestor>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblinvestor mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblinvestor inv = new Tblinvestor();
    	
		inv.setDdateOfBirth(rs.getDate("ddateOfBirth"));
		inv.setIinvestorId(rs.getInt("iinvestorId"));
		inv.setSfirstName(rs.getString("sfirstName"));
		inv.setSfullName(rs.getString("sfullName"));
		inv.setSlastName(rs.getString("slastName"));
		inv.setSpictureFile(rs.getString("spictureFile"));
		inv.setSpictureFileSmall(rs.getString("spictureFileSmall"));
		inv.setIhardCashAccountId(rs.getInt("ihardCashAccountId"));
		inv.setItaxGroupId(rs.getInt("itaxGroupId"));
		
		Tblaccount tblaccount = new TblAccountRowMapper().mapRow(rs, rowNum);				
		tblaccount.setIaccountId(inv.getIhardCashAccountId());
		inv.setTblaccount(tblaccount);		
		
		inv.setTbltaxgroup(new TblTaxGroupRowMapper().mapRow(rs, rowNum));
	
 		return inv; 	    	
    }
}
