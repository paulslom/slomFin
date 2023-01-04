package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblTaxGroupRowMapper implements RowMapper<Tbltaxgroup>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbltaxgroup mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbltaxgroup tbltaxgroup = new Tbltaxgroup();
		tbltaxgroup.setItaxGroupId(rs.getInt("ItaxGroupId"));
		tbltaxgroup.setStaxGroupName(rs.getString("staxGroupName"));	
 		return tbltaxgroup; 	    	
    }
}
