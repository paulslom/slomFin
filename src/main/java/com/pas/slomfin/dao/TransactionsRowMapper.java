package com.pas.slomfin.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pas.dynamodb.DynamoTransaction;

public class TransactionsRowMapper implements RowMapper<DynamoTransaction>, Serializable 
{
    private static final long serialVersionUID = 1L;

	@Override
    public DynamoTransaction mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
		DynamoTransaction dynamoTransaction = new DynamoTransaction();
		  
	    dynamoTransaction.setTransactionID(rs.getInt("iTransactionID"));
	    dynamoTransaction.setAccountID(rs.getInt("iAccountID"));
	    dynamoTransaction.setTransactionTypeID(rs.getInt("iTransactionTypeID"));
	    dynamoTransaction.setInvestmentID(rs.getInt("iInvestmentID"));
	    dynamoTransaction.setTransactionDate(rs.getString("dTransactionDate"));
	    dynamoTransaction.setUnits(rs.getBigDecimal("decUnits"));
	    dynamoTransaction.setPrice(rs.getBigDecimal("mPrice"));
	    dynamoTransaction.setCostProceeds(rs.getBigDecimal("mCostProceeds"));		
	    dynamoTransaction.setTransactionEntryDate(rs.getString("dTranEntryDate"));    
	    dynamoTransaction.setTransactionPostedDate(rs.getString("dTranPostedDate"));
	    dynamoTransaction.setTransactionChangeDate(rs.getString("dTranChangeDate"));
	    dynamoTransaction.setDividendTaxableYear(rs.getInt("iDividendTaxableYear"));
	    dynamoTransaction.setEffectiveAmount(rs.getBigDecimal("mEffectiveAmount"));
	    dynamoTransaction.setOptionTypeID(rs.getInt("iOptionTypeID"));
	    dynamoTransaction.setStrikePrice(rs.getBigDecimal("mStrikePrice"));
	    dynamoTransaction.setExpirationDate(rs.getString("dExpirationDate"));
	    dynamoTransaction.setOpeningTrxInd(rs.getBoolean("bOpeningTrxInd"));
	    dynamoTransaction.setCheckNo(rs.getInt("iCheckNo"));
	    dynamoTransaction.setTransactionDescription(rs.getString("trxDescription"));
	    dynamoTransaction.setWdCategoryID(rs.getInt("iWDCategoryID"));
	    dynamoTransaction.setCashDepositTypeID(rs.getInt("iCashDepositTypeID"));
	    dynamoTransaction.setFinalTrxOfBillingCycle(rs.getBoolean("bFinalTrxOfBillingCycle"));
	    dynamoTransaction.setAccountName(rs.getString("sAccountName"));
	    dynamoTransaction.setInvestmentDescription(rs.getString("invDescription"));
	    dynamoTransaction.setWdCategoryDescription(rs.getString("sWDCategoryDescription"));
	    dynamoTransaction.setCashDepositTypeDescription(rs.getString("sCashDepositTypeDesc"));
					
 		return dynamoTransaction; 	    	
    }
}
