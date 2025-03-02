package com.pas.dynamodb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoTransaction implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(DynamoTransaction.class);	
	
	
	private Integer accountID;
	private String accountName;
	private Integer cashDepositTypeID;
	private String cashDepositTypeDescription;
	private Integer checkNo;
	private BigDecimal costProceeds;
	private Integer dividendTaxableYear;
	private String expirationDate;
	private Boolean finalTrxOfBillingCycle;
	private Integer investmentID;
	private String investmentDescription;
	private Boolean openingTrxInd;
	private Integer optionTypeID;
	private BigDecimal price;
	private BigDecimal strikePrice;
	private String transactionDescription;
	private Integer transactionID;
	private Integer transactionTypeID;
	private String transactionTypeDescription;
	private String transactionDate;
	private String transactionEntryDate;	
    private String transactionPostedDate;
    private String transactionChangeDate;
    private Boolean trxTypPositiveInd;
    private BigDecimal units;
    private Integer wdCategoryID;
    private String wdCategoryDescription;
    
    //dynamodbignore fields
    private Date entryDateJava;
    private Date postedDateJava;
    private BigDecimal currentBalance;
    private String trxStyleClass;
    private String balanceStyleClass;
    private Integer investmentTypeID;
    private String investmentTypeDescription;
    private Integer transferAccountID;
     
    public DynamoTransaction() 
    {		
	}
    
    public DynamoTransaction(Integer accountID, String accountName, Integer checkNo, BigDecimal costProceeds, Integer dividendTaxableYear, 
    			Integer investmentID, String investmentDescription, BigDecimal price, String transactionDescription, Integer transactionID, Integer transactionTypeID,
    			String transactionTypeDescription, String transactionDate, String transactionEntryDate, String transactionPostedDate, String transactionChangeDate,
    			Boolean trxTypPositiveInd, BigDecimal units, String wdCategoryDescription, Integer wdCategoryID, Date transactionEntryDateJava, Date transactionPostedDateJava)
    {
    	this.setAccountID(accountID);
    	this.setAccountName(accountName); 
    	this.setCheckNo(checkNo); 
    	this.setCostProceeds(costProceeds); 
    	this.setDividendTaxableYear(dividendTaxableYear);
    	this.setInvestmentID(investmentID); 
    	this.setInvestmentDescription(investmentDescription); 
    	this.setPrice(price); 
    	this.setTransactionDescription(transactionDescription); 
    	this.setTransactionID(transactionID); 
    	this.setTransactionTypeID(transactionTypeID); 
    	this.setTransactionTypeDescription(transactionTypeDescription); 
    	this.setTransactionDate(transactionDate); 
    	this.setTransactionEntryDate(transactionEntryDate); 
    	this.setTransactionPostedDate(transactionPostedDate); 
    	this.setTransactionChangeDate(transactionChangeDate); 
    	this.setTrxTypPositiveInd(trxTypPositiveInd); 
    	this.setUnits(units); 
    	this.setWdCategoryDescription(wdCategoryDescription); 
    	this.setWdCategoryID(wdCategoryID); 
    	this.setEntryDateJava(transactionEntryDateJava);
    	this.setPostedDateJava(transactionPostedDateJava);
    }
    
    public DynamoTransaction(DynamoTransaction dtr)
    {
    	this(dtr.getAccountID(), dtr.getAccountName(), dtr.getCheckNo(), dtr.getCostProceeds(), dtr.getDividendTaxableYear(), 
    			dtr.getInvestmentID(), dtr.getInvestmentDescription(), dtr.getPrice(),dtr.getTransactionDescription(), dtr.getTransactionID(), dtr.getTransactionTypeID(),
    			dtr.getTransactionTypeDescription(), dtr.getTransactionDate(), dtr.getTransactionEntryDate(), dtr.getTransactionPostedDate(), dtr.getTransactionChangeDate(),
    			dtr.getTrxTypPositiveInd(), dtr.getUnits(), dtr.getWdCategoryDescription(), dtr.getWdCategoryID(), dtr.getEntryDateJava(), dtr.getPostedDateJava());
    }	
	   
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

	@DynamoDbIgnore
	public Date getPostedDateJava() {
		return postedDateJava;
	}

	@DynamoDbIgnore
	public void setPostedDateJava(Date postedDateJava) {
		this.postedDateJava = postedDateJava;
	}

	@DynamoDbIgnore
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	@DynamoDbIgnore
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getTransactionTypeDescription() {
		return transactionTypeDescription;
	}

	public void setTransactionTypeDescription(String transactionTypeDescription) {
		this.transactionTypeDescription = transactionTypeDescription;
	}

	@DynamoDbIgnore
	public String getTrxStyleClass() {
		return trxStyleClass;
	}

	@DynamoDbIgnore
	public void setTrxStyleClass(String trxStyleClass) {
		this.trxStyleClass = trxStyleClass;
	}

	public Boolean getTrxTypPositiveInd() {
		return trxTypPositiveInd;
	}

	public void setTrxTypPositiveInd(Boolean trxTypPositiveInd) {
		this.trxTypPositiveInd = trxTypPositiveInd;
	}

	@DynamoDbIgnore
	public String getBalanceStyleClass() {
		return balanceStyleClass;
	}

	@DynamoDbIgnore
	public void setBalanceStyleClass(String balanceStyleClass) {
		this.balanceStyleClass = balanceStyleClass;
	}

	@DynamoDbIgnore
	public Date getEntryDateJava() {
		return entryDateJava;
	}

	@DynamoDbIgnore
	public void setEntryDateJava(Date entryDateJava) {
		this.entryDateJava = entryDateJava;
	}

	@DynamoDbIgnore
	public Integer getInvestmentTypeID() {
		return investmentTypeID;
	}

	@DynamoDbIgnore
	public void setInvestmentTypeID(Integer investmentTypeID) {
		this.investmentTypeID = investmentTypeID;
	}

	@DynamoDbIgnore
	public String getInvestmentTypeDescription() {
		return investmentTypeDescription;
	}

	@DynamoDbIgnore
	public void setInvestmentTypeDescription(String investmentTypeDescription) {
		this.investmentTypeDescription = investmentTypeDescription;
	}

	@DynamoDbIgnore
	public Integer getTransferAccountID() {
		return transferAccountID;
	}

	@DynamoDbIgnore
	public void setTransferAccountID(Integer transferAccountID) {
		this.transferAccountID = transferAccountID;
	}
  

}
