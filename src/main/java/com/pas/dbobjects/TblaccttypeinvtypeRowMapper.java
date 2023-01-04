package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblaccttypeinvtypeRowMapper implements RowMapper<Tblaccttypeinvtype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblaccttypeinvtype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblaccttypeinvtype tblaccttypeinvtype = new Tblaccttypeinvtype();
    	
		tblaccttypeinvtype.setIacctTypeInvTypeId(rs.getInt("iacctTypeInvTypeId"));
		tblaccttypeinvtype.setIaccountTypeID(rs.getInt("iaccountTypeID"));
		tblaccttypeinvtype.setiInvestmentTypeID(rs.getInt("iInvestmentTypeID"));
		
		tblaccttypeinvtype.setTblaccounttype(new TblAccountTypeRowMapper().mapRow(rs, rowNum));
		tblaccttypeinvtype.setTblinvestmenttype(new TblInvestmentTypeRowMapper().mapRow(rs, rowNum));
		
 		return tblaccttypeinvtype;     	
    }
}
