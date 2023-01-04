package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblappsecurityRowMapper implements RowMapper<Tblappsecurity>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblappsecurity mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblappsecurity sec = new Tblappsecurity();
    	
		sec.setUserId(rs.getString("userId"));
		sec.setPassword(rs.getString("password"));
		
 		return sec; 	
    	
    }
}
