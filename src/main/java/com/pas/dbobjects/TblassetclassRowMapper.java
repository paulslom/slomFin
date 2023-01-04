package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblassetclassRowMapper implements RowMapper<Tblassetclass>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblassetclass mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblassetclass tblassetclass = new Tblassetclass();
    	tblassetclass.setIassetClassId(rs.getInt("IassetClassId"));
		tblassetclass.setDhistoricalReturn(rs.getBigDecimal("dhistoricalReturn"));
		tblassetclass.setSassetClass(rs.getString("sassetClass"));
		
 		return tblassetclass; 	    	
    }
}
