package com.pas.dbobjects;


import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblInvestmentTypeRowMapper implements RowMapper<Tblinvestmenttype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblinvestmenttype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblinvestmenttype tblinvestmenttype = new Tblinvestmenttype();
		tblinvestmenttype.setIinvestmentTypeId(rs.getInt("iinvestmentTypeId"));
		tblinvestmenttype.setSdescription(rs.getString("investmentTypeDescription"));
 		return tblinvestmenttype; 	    	
    }
}
