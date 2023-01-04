package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblJobRowMapper implements RowMapper<Tbljob>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbljob mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbljob tbljob = new Tbljob();
		tbljob.setIjobId(rs.getInt("ijobId"));
		tbljob.setIinvestorId(rs.getInt("iinvestorId"));
		tbljob.setSemployer(rs.getString("semployer"));
		tbljob.setDjobEndDate(rs.getDate("djobEndDate"));
		tbljob.setDjobStartDate(rs.getDate("djobStartDate"));
		tbljob.setIpaydaysPerYear(rs.getInt("ipaydaysPerYear"));
		tbljob.setMcafeteria(rs.getBigDecimal("mcafeteria"));
		tbljob.setMdental(rs.getBigDecimal("mdental"));
		tbljob.setMfederalWithholding(rs.getBigDecimal("mfederalWithholding"));
		tbljob.setMfsaAmount(rs.getBigDecimal("mfsaAmount"));
		tbljob.setMgrossPay(rs.getBigDecimal("mgrossPay"));
		tbljob.setMgroupLifeIncome(rs.getBigDecimal("mgroupLifeIncome"));
		tbljob.setMgroupLifeInsurance(rs.getBigDecimal("mgroupLifeInsurance"));
		tbljob.setMmedical(rs.getBigDecimal("mmedical"));
		tbljob.setMmedicareWithholding(rs.getBigDecimal("mmedicareWithholding"));
		tbljob.setMparking(rs.getBigDecimal("mparking"));
		tbljob.setMretirementDeferred(rs.getBigDecimal("mretirementDeferred"));
		tbljob.setMroth401k(rs.getBigDecimal("mroth401k"));
		tbljob.setMsswithholding(rs.getBigDecimal("msswithholding"));
		tbljob.setMstateWithholding(rs.getBigDecimal("mstateWithholding"));
		tbljob.setMvision(rs.getBigDecimal("mvision"));
		tbljob.setSemployerFedIdno(rs.getString("semployerFedIdno"));
		tbljob.setSemployerStateIdno(rs.getString("semployerStateIdno"));
		tbljob.setSincomeState(rs.getString("sincomeState"));
		
		tbljob.setTblinvestor(new TblInvestorRowMapper().mapRow(rs, rowNum));
		
 		return tbljob; 	    	
    }
}
