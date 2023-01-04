package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TbldropdownRowMapper implements RowMapper<Tbldropdown>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbldropdown mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbldropdown dd = new Tbldropdown();
    	
		dd.setIddid(rs.getInt("iddid"));
		dd.setSdddescColName(rs.getString("sdddescColName"));
		dd.setSdddescColNameShort(rs.getString("sdddescColNameShort"));
		dd.setSdddescription(rs.getString("sdddescription"));
		dd.setSddsort(rs.getString("sddsort"));
		dd.setSddtableName(rs.getString("sddtableName"));
		dd.setSddtype(rs.getString("sddtype"));
		dd.setSddvalueColName(rs.getString("sddvalueColName"));
		dd.setSddwhereAbbr1(rs.getString("sddwhereAbbr1"));
		dd.setSddwhereAbbr2(rs.getString("sddwhereAbbr2"));
		dd.setSddwhereAbbr3(rs.getString("sddwhereAbbr3"));
		dd.setSddwhereAbbr4(rs.getString("sddwhereAbbr4"));
		dd.setSddwhereAbbr5(rs.getString("sddwhereAbbr5"));
		dd.setSddwhereCol1(rs.getString("sddwhereCol1"));
		dd.setSddwhereCol2(rs.getString("sddwhereCol2"));
		dd.setSddwhereCol3(rs.getString("sddwhereCol3"));
		dd.setSddwhereCol4(rs.getString("sddwhereCol4"));
		dd.setSddwhereCol5(rs.getString("sddwhereCol5"));
		dd.setSddwhereTbl1(rs.getString("sddwhereTbl1"));
		dd.setSddwhereTbl2(rs.getString("sddwhereTbl2"));
		dd.setSddwhereTbl3(rs.getString("sddwhereTbl3"));
		dd.setSddwhereTbl4(rs.getString("sddwhereTbl4"));
		dd.setSddwhereTbl5(rs.getString("sddwhereTbl5"));
		dd.setSddwhereText(rs.getString("sddwhereText"));
	
 		return dd; 	    	
    }
}
