package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Payday implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(PortfolioHistory.class);	
	
	private Integer paydayID;
	private Integer transactionTypeID;
    private String accountName;
    private Integer accountID;
    private String trxTypeDescription;
    private BigDecimal defaultAmount;
    private Integer defaultDay;
    private Boolean nextMonthInd;
    private String paydayDescription;
    private Integer xferAccountID;
    
    public String toString()
    {
    	return "paydayID: " + paydayID + " account: " + accountName + " paydayDescription: " + paydayDescription + " amount: " + defaultAmount;
    }

    @DynamoDbPartitionKey	
	public Integer getPaydayID() {
		return paydayID;
	}

	public void setPaydayID(Integer paydayID) {
		this.paydayID = paydayID;
	}

	public Integer getTransactionTypeID() {
		return transactionTypeID;
	}

	public void setTransactionTypeID(Integer transactionTypeID) {
		this.transactionTypeID = transactionTypeID;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public String getTrxTypeDescription() {
		return trxTypeDescription;
	}

	public void setTrxTypeDescription(String trxTypeDescription) {
		this.trxTypeDescription = trxTypeDescription;
	}

	public BigDecimal getDefaultAmount() {
		return defaultAmount;
	}

	public void setDefaultAmount(BigDecimal defaultAmount) {
		this.defaultAmount = defaultAmount;
	}

	public Integer getDefaultDay() {
		return defaultDay;
	}

	public void setDefaultDay(Integer defaultDay) {
		this.defaultDay = defaultDay;
	}

	public Boolean getNextMonthInd() {
		return nextMonthInd;
	}

	public void setNextMonthInd(Boolean nextMonthInd) {
		this.nextMonthInd = nextMonthInd;
	}

	public String getPaydayDescription() {
		return paydayDescription;
	}

	public void setPaydayDescription(String paydayDescription) {
		this.paydayDescription = paydayDescription;
	}

	public Integer getXferAccountID() {
		return xferAccountID;
	}

	public void setXferAccountID(Integer xferAccountID) {
		this.xferAccountID = xferAccountID;
	}
    
}
