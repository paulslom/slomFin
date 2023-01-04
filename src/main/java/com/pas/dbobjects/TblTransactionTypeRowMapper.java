package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblTransactionTypeRowMapper implements RowMapper<Tbltransactiontype>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbltransactiontype mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbltransactiontype trxType = new Tbltransactiontype();
    	
		trxType.setBpositiveInd(rs.getBoolean("bpositiveInd"));
		trxType.setItransactionTypeId(rs.getInt("itransactionTypeId"));
		trxType.setSdescription(rs.getString("trxTypeDescription"));
		trxType.setSdescriptionAbbr(rs.getString("sdescriptionAbbr"));
		
 		return trxType; 	    	
    }
}
