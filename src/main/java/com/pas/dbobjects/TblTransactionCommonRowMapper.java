package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblTransactionCommonRowMapper implements RowMapper<Tbltransactioncommon>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbltransactioncommon mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbltransactioncommon trxCommon = new Tbltransactioncommon();
    	
		trxCommon.setItrxCommonAccountId(rs.getInt("itrxCommonAccountId"));
		trxCommon.setItrxCommonId(rs.getInt("itrxCommonId"));
		trxCommon.setItrxCommonInvestorId(rs.getInt("itrxCommonInvestorId"));
		trxCommon.setItrxCommonTrxTypeId(rs.getInt("itrxCommonTrxTypeId"));
		trxCommon.setSpictureName(rs.getString("spictureName"));
		
		trxCommon.setTblaccount(new TblAccountRowMapper().mapRow(rs, rowNum));
		trxCommon.setTblinvestor(new TblInvestorRowMapper().mapRow(rs, rowNum));
		trxCommon.setTbltransactiontype(new TblTransactionTypeRowMapper().mapRow(rs, rowNum));		
	
 		return trxCommon; 	    	
    }
}
