package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblWDCategoryRowMapper implements RowMapper<Tblwdcategory>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblwdcategory mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblwdcategory wdCat = new Tblwdcategory();
    	
		wdCat.setIwdcategoryId(rs.getInt("iwdcategoryId"));
		wdCat.setiInvestorID(rs.getInt("iInvestorID"));
		wdCat.setSwdcategoryDescription(rs.getString("swdcategoryDescription"));
		wdCat.setTblinvestor(new TblInvestorRowMapper().mapRow(rs, rowNum));		
		
 		return wdCat; 	    	
    }
}
