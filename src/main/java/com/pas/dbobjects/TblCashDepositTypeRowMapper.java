package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblCashDepositTypeRowMapper implements RowMapper<Tblcashdeposittype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblcashdeposittype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblcashdeposittype tblcashdeposittype = new Tblcashdeposittype();
		
		tblcashdeposittype.setIcashDepositTypeId(rs.getInt("icashDepositTypeId"));
		tblcashdeposittype.setScashDepositTypeDesc(rs.getString("scashDepositTypeDesc"));
		
 		return tblcashdeposittype; 	    	
    }
}
