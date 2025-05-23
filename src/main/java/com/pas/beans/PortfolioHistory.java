package com.pas.beans;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.pas.dynamodb.DateToStringConverter;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PortfolioHistory implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(PortfolioHistory.class);	
	
	private Integer iPortfolioHistoryID;
    private String historyDate; 
    private BigDecimal totalValue;
    
    //not stored to DB
    private Date historyDateJava;
    private BigDecimal thisYearsDollars;
    private BigDecimal thisYearsPercent;
    private String thisYearsPercentDisplay;
    private boolean renderThisYearsStuff = false;
    
    public String toString()
    {
    	return "date: " + historyDate + " account: " + " value: " + totalValue;
    }

    @DynamoDbPartitionKey	
    public Integer getiPortfolioHistoryID() {
		return iPortfolioHistoryID;
	}

	public void setiPortfolioHistoryID(Integer iPortfolioHistoryID) {
		this.iPortfolioHistoryID = iPortfolioHistoryID;
	}

	public String getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(String historyDate) {
		this.historyDate = historyDate;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	@DynamoDbIgnore
	public Date getHistoryDateJava() 
	{
		this.setHistoryDateJava(DateToStringConverter.unconvert(historyDate));
		return historyDateJava;
	}

	@DynamoDbIgnore
	public void setHistoryDateJava(Date historyDateJava) {
		this.historyDateJava = historyDateJava;
	}

	@DynamoDbIgnore
	public BigDecimal getThisYearsDollars() {
		return thisYearsDollars;
	}

	@DynamoDbIgnore
	public void setThisYearsDollars(BigDecimal thisYearsDollars) {
		this.thisYearsDollars = thisYearsDollars;
	}

	@DynamoDbIgnore
	public BigDecimal getThisYearsPercent() {
		return thisYearsPercent;
	}

	@DynamoDbIgnore
	public void setThisYearsPercent(BigDecimal thisYearsPercent) {
		this.thisYearsPercent = thisYearsPercent;
	}

	public boolean isRenderThisYearsStuff() {
		return renderThisYearsStuff;
	}

	public void setRenderThisYearsStuff(boolean renderThisYearsStuff) {
		this.renderThisYearsStuff = renderThisYearsStuff;
	}

	public String getThisYearsPercentDisplay() {
		return thisYearsPercentDisplay;
	}

	public void setThisYearsPercentDisplay(String thisYearsPercentDisplay) {
		this.thisYearsPercentDisplay = thisYearsPercentDisplay;
	}

    
}
