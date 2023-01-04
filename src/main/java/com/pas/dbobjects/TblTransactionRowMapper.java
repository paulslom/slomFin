package com.pas.dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TblTransactionRowMapper implements RowMapper<Tbltransaction>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public Tbltransaction mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		Tbltransaction trx = new Tbltransaction();
                
		trx.setBfinalTrxOfBillingCycle(rs.getBoolean("bFinalTrxOfBillingCycle"));
		trx.setBopeningTrxInd(rs.getBoolean("bopeningTrxInd"));
		trx.setDecUnits(rs.getBigDecimal("decUnits"));
		trx.setDexpirationDate(rs.getDate("dexpirationDate"));
		trx.setDtranChangeDate(rs.getDate("dtranChangeDate"));
		trx.setDtranEntryDate(rs.getDate("dtranEntryDate"));
		trx.setDtranPostedDate(rs.getDate("dtranPostedDate"));
		trx.setDtransactionDate(rs.getDate("dtransactionDate"));
		trx.setIaccountID(rs.getInt("iaccountID"));
		trx.setiCashDepositTypeID(rs.getInt("iCashDepositTypeID"));
		
		if (rs.getInt("icheckNo") == 0)
		{
			trx.setIcheckNo(null);
		}
		else
		{
			trx.setIcheckNo(rs.getInt("icheckNo"));
		}
		
		trx.setIdividendTaxableYear(rs.getInt("idividendTaxableYear"));
		trx.setiTransactionTypeID(rs.getInt("itransactionTypeId"));
		trx.setIinvestmentID(rs.getInt("iinvestmentID"));
		trx.setiOptionTypeID(rs.getInt("iOptionTypeID"));
		trx.setItransactionId(rs.getInt("itransactionId"));
		trx.setiWDCategoryID(rs.getInt("iWDCategoryID"));
		trx.setMcostProceeds(rs.getBigDecimal("mcostProceeds"));
		trx.setMeffectiveAmount(rs.getBigDecimal("meffectiveAmount"));
		trx.setMprice(rs.getBigDecimal("mprice"));
		trx.setMstrikePrice(rs.getBigDecimal("mstrikePrice"));
		trx.setSdescription(rs.getString("trxDescription"));
		
		trx.setTbloptiontype(new TblOptionTypeRowMapper().mapRow(rs, rowNum));
		trx.setTblcashdeposittype(new TblCashDepositTypeRowMapper().mapRow(rs, rowNum));
		trx.setTbltransactiontype(new TblTransactionTypeRowMapper().mapRow(rs, rowNum));
		trx.setTblinvestment(new TblInvestmentRowMapper().mapRow(rs, rowNum));
		trx.setTblaccount(new TblAccountRowMapper().mapRow(rs, rowNum));		
		trx.setTblwdcategory(new TblWDCategoryRowMapper().mapRow(rs, rowNum));
	
 		return trx; 	    	
    }
}
