package com.pas.dynamodb;

import java.io.Serializable;
import java.math.BigDecimal;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoTransaction implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(DynamoTransaction.class);	
	
	private Integer transactionID;
	private Integer accountID;
	private Integer investmentID;
	private Integer transactionTypeID;
	private String transactionDate;
	private BigDecimal units;
	private BigDecimal price;
	private BigDecimal costProceeds;
	private String transactionEntryDate;	
    private String transactionPostedDate;
    private String transactionChangeDate;
    private Integer dividendTaxableYear;
    private BigDecimal effectiveAmount;
    private Integer optionTypeID;
    private BigDecimal strikePrice;
    private String expirationDate;
    private Boolean openingTrxInd;
    private Integer checkNo;
    private String transactionDescription;
    private Integer wdCategoryID;
    private Integer cashDepositTypeID;
    private Boolean finalTrxOfBillingCycle;
    private String accountName;
    private String investmentDescription;
    private String wdCategoryDescription;
    private String cashDepositTypeDescription;
        
    public String toString()
    {
    	return "transaction ID: " + transactionID + " desc: " + transactionDescription + " account: " + accountName + " amount: " + costProceeds;
    }

    @DynamoDbPartitionKey
	public Integer getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}

	@DynamoDbSecondaryPartitionKey(indexNames = "gsi_AccountID")
	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public Integer getInvestmentID() {
		return investmentID;
	}

	public void setInvestmentID(Integer investmentID) {
		this.investmentID = investmentID;
	}

	public Integer getTransactionTypeID() {
		return transactionTypeID;
	}

	public void setTransactionTypeID(Integer transactionTypeID) {
		this.transactionTypeID = transactionTypeID;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getCostProceeds() {
		return costProceeds;
	}

	public void setCostProceeds(BigDecimal costProceeds) {
		this.costProceeds = costProceeds;
	}

	public String getTransactionEntryDate() {
		return transactionEntryDate;
	}

	public void setTransactionEntryDate(String transactionEntryDate) {
		this.transactionEntryDate = transactionEntryDate;
	}

	@DynamoDbSortKey
	public String getTransactionPostedDate() {
		return transactionPostedDate;
	}

	public void setTransactionPostedDate(String transactionPostedDate) {
		this.transactionPostedDate = transactionPostedDate;
	}

	public String getTransactionChangeDate() {
		return transactionChangeDate;
	}

	public void setTransactionChangeDate(String transactionChangeDate) {
		this.transactionChangeDate = transactionChangeDate;
	}

	public Integer getDividendTaxableYear() {
		return dividendTaxableYear;
	}

	public void setDividendTaxableYear(Integer dividendTaxableYear) {
		this.dividendTaxableYear = dividendTaxableYear;
	}

	public BigDecimal getEffectiveAmount() {
		return effectiveAmount;
	}

	public void setEffectiveAmount(BigDecimal effectiveAmount) {
		this.effectiveAmount = effectiveAmount;
	}

	public Integer getOptionTypeID() {
		return optionTypeID;
	}

	public void setOptionTypeID(Integer optionTypeID) {
		this.optionTypeID = optionTypeID;
	}

	public BigDecimal getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(BigDecimal strikePrice) {
		this.strikePrice = strikePrice;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Boolean getOpeningTrxInd() {
		return openingTrxInd;
	}

	public void setOpeningTrxInd(Boolean openingTrxInd) {
		this.openingTrxInd = openingTrxInd;
	}

	public Integer getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(Integer checkNo) {
		this.checkNo = checkNo;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public Integer getWdCategoryID() {
		return wdCategoryID;
	}

	public void setWdCategoryID(Integer wdCategoryID) {
		this.wdCategoryID = wdCategoryID;
	}

	public Integer getCashDepositTypeID() {
		return cashDepositTypeID;
	}

	public void setCashDepositTypeID(Integer cashDepositTypeID) {
		this.cashDepositTypeID = cashDepositTypeID;
	}

	public Boolean getFinalTrxOfBillingCycle() {
		return finalTrxOfBillingCycle;
	}

	public void setFinalTrxOfBillingCycle(Boolean finalTrxOfBillingCycle) {
		this.finalTrxOfBillingCycle = finalTrxOfBillingCycle;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getInvestmentDescription() {
		return investmentDescription;
	}

	public void setInvestmentDescription(String investmentDescription) {
		this.investmentDescription = investmentDescription;
	}

	public String getWdCategoryDescription() {
		return wdCategoryDescription;
	}

	public void setWdCategoryDescription(String wdCategoryDescription) {
		this.wdCategoryDescription = wdCategoryDescription;
	}

	public String getCashDepositTypeDescription() {
		return cashDepositTypeDescription;
	}

	public void setCashDepositTypeDescription(String cashDepositTypeDescription) {
		this.cashDepositTypeDescription = cashDepositTypeDescription;
	}
  

}
