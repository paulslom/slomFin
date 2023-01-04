package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblOptionTypeRowMapper implements RowMapper<Tbloptiontype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbloptiontype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbloptiontype optionType = new Tbloptiontype();
		
    	optionType.setIoptionTypeId(rs.getInt("ioptiontypeid"));
		optionType.setSdescription(rs.getString("optTypeDescription"));
	
 		return optionType; 	    	
    }
}
