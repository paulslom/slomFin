package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblpaychecktypeRowMapper implements RowMapper<Tblpaychecktype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblpaychecktype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblpaychecktype tblpaychecktype = new Tblpaychecktype();
		
		tblpaychecktype.setIpaycheckTypeId(rs.getInt("ipaycheckTypeId"));
		tblpaychecktype.setSpaycheckType(rs.getString("spaycheckType"));
		
 		return tblpaychecktype; 	    	
    }
}
