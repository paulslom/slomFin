package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.slomfin.valueObject.CostBasis;

public class CostBasisRowMapper implements RowMapper<CostBasis>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public CostBasis mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		CostBasis costBasisDetail = new CostBasis();
		
		costBasisDetail.setTransactionTypeDescription(rs.getString("trxtypedesc"));
		costBasisDetail.setCostProceeds(rs.getBigDecimal("McostProceeds"));
		costBasisDetail.setDecUnits(rs.getBigDecimal("DecUnits"));
		costBasisDetail.setEffectiveAmount(rs.getBigDecimal("MeffectiveAmount"));
		costBasisDetail.setPositiveInd(rs.getBoolean("bpositiveInd"));
		
 		return costBasisDetail; 	    	
    }
}
