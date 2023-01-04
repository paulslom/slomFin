package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblAccountTypeRowMapper implements RowMapper<Tblaccounttype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblaccounttype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblaccounttype accountType = new Tblaccounttype();
    	
		accountType.setIaccountTypeId(rs.getInt("iaccountTypeId"));
		accountType.setSaccountType(rs.getString("saccountType"));
		
 		return accountType; 	    	
    }
}
