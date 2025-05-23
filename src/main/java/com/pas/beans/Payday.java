package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Payday implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(PortfolioHistory.class);	
	
	private Integer paydayID;
	private String paydayDescription;
	private Integer transactionTypeID;
	private String trxTypeDescription;
	private Integer accountID;
    private String accountName;    
    private BigDecimal defaultAmount;
    private Integer defaultDay;
    private Boolean nextMonthInd;    
    private Integer xferAccountID;
    private String xferAccountName;
    
    //non-dynamoFields
    private Boolean processInd;
    private Date paydayTrxDate;
    private String pdRowStyleClass;
    
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

	public String getXferAccountName() {
		return xferAccountName;
	}

	public void setXferAccountName(String xferAccountName) {
		this.xferAccountName = xferAccountName;
	}

	@DynamoDbIgnore
	public Boolean getProcessInd() {
		return processInd;
	}

	@DynamoDbIgnore
	public void setProcessInd(Boolean processInd) {
		this.processInd = processInd;
	}

	@DynamoDbIgnore
	public Date getPaydayTrxDate() {
		return paydayTrxDate;
	}

	@DynamoDbIgnore
	public void setPaydayTrxDate(Date paydayTrxDate) {
		this.paydayTrxDate = paydayTrxDate;
	}

	@DynamoDbIgnore
	public String getPdRowStyleClass() {
		return pdRowStyleClass;
	}

	@DynamoDbIgnore
	public void setPdRowStyleClass(String pdRowStyleClass) {
		this.pdRowStyleClass = pdRowStyleClass;
	}
    
}
