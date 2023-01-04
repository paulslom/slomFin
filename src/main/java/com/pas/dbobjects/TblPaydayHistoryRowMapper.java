package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblPaydayHistoryRowMapper implements RowMapper<Tblpaydayhistory>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tblpaydayhistory mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tblpaydayhistory tblpaydayhistory = new Tblpaydayhistory();
		tblpaydayhistory.setIpaydayHistoryId(rs.getInt("ipaydayHistoryId"));
		tblpaydayhistory.setIjobId(rs.getInt("ijobId"));
		tblpaydayhistory.setDpaydayHistoryDate(rs.getDate("dpaydayHistoryDate"));
		tblpaydayhistory.setMcafeteria(rs.getBigDecimal("mcafeteria"));
		tblpaydayhistory.setMdental(rs.getBigDecimal("mdental"));
		tblpaydayhistory.setMfederalWithholding(rs.getBigDecimal("mfederalWithholding"));
		tblpaydayhistory.setMfsaAmount(rs.getBigDecimal("mfsaAmount"));
		tblpaydayhistory.setMgrossPay(rs.getBigDecimal("mgrossPay"));
		tblpaydayhistory.setMgroupLifeIncome(rs.getBigDecimal("mgroupLifeIncome"));
		tblpaydayhistory.setMgroupLifeInsurance(rs.getBigDecimal("mgroupLifeInsurance"));
		tblpaydayhistory.setMmedical(rs.getBigDecimal("mmedical"));
		tblpaydayhistory.setMmedicareWithholding(rs.getBigDecimal("mmedicareWithholding"));
		tblpaydayhistory.setMparking(rs.getBigDecimal("mparking"));
		tblpaydayhistory.setMretirementDeferred(rs.getBigDecimal("mretirementDeferred"));
		tblpaydayhistory.setMroth401k(rs.getBigDecimal("mroth401k"));
		tblpaydayhistory.setMsswithholding(rs.getBigDecimal("msswithholding"));
		tblpaydayhistory.setMstateWithholding(rs.getBigDecimal("mstateWithholding"));
		tblpaydayhistory.setMvision(rs.getBigDecimal("mvision"));
		
		tblpaydayhistory.setTbljob(new TblJobRowMapper().mapRow(rs, rowNum));
		tblpaydayhistory.setTblpaychecktype(new TblpaychecktypeRowMapper().mapRow(rs, rowNum));
		
 		return tblpaydayhistory; 	    	
    }
}
