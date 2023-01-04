package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblBrokerRowMapper implements RowMapper<Tblbroker>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblbroker mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblbroker tblbroker = new Tblbroker();
		
		tblbroker.setDmarginInterestRate(rs.getBigDecimal("dmargininterestrate"));
		tblbroker.setIbrokerId(rs.getInt("ibrokerId"));
		tblbroker.setMoptionContractCommission(rs.getBigDecimal("moptionContractCommission"));
		tblbroker.setSbrokerName(rs.getString("sbrokerName"));
	
 		return tblbroker; 	    	
    }
}
