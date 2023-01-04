package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblaccttypetrxtypeRowMapper implements RowMapper<Tblaccttypetrxtype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblaccttypetrxtype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblaccttypetrxtype tblaccttypetrxtype = new Tblaccttypetrxtype();
    	
		tblaccttypetrxtype.setIacctTypeTrxType(rs.getInt("iacctTypeTrxType"));
		tblaccttypetrxtype.setiTransactionTypeID(rs.getInt("iTransactionTypeID"));
		tblaccttypetrxtype.setIaccountTypeID(rs.getInt("iaccountTypeID"));		
		
		tblaccttypetrxtype.setTblaccounttype(new TblAccountTypeRowMapper().mapRow(rs, rowNum));
		tblaccttypetrxtype.setTbltransactiontype(new TblTransactionTypeRowMapper().mapRow(rs, rowNum));
		
 		return tblaccttypetrxtype; 	
    	
    }
}
