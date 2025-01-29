package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class PortfolioHistory implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(PortfolioHistory.class);	
	
	private Integer iPortfolioHistoryID;
    private String accountName;
    private Integer accountID;
    private String historyDate; 
    private BigDecimal value;
    
    public String toString()
    {
    	return "date: " + historyDate + " account: " + " value: " + value;
    }

    @DynamoDbPartitionKey	
	public Integer getiPortfolioHistoryID() {
		return iPortfolioHistoryID;
	}

	public void setiPortfolioHistoryID(Integer iPortfolioHistoryID) {
		this.iPortfolioHistoryID = iPortfolioHistoryID;
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

	@DynamoDbSortKey
	public String getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(String historyDate) {
		this.historyDate = historyDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
    
    
}
