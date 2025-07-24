package com.pas.beans;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Investment implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(Investment.class);	
	
	private Integer iInvestmentID;
	private Integer iInvestmentTypeID;
	private String investmentTypeDescription;
	private String  tickerSymbol;
	private String description;
	private BigDecimal currentPrice; 
	private Integer assetClassID;	
	private String assetClass;
	  
	//not saved to dynamo - used on report
	private BigDecimal unitsOwned;
	private BigDecimal currentValue;
	private BigDecimal holdingPercentage;
	
	@DynamoDbPartitionKey
	public Integer getiInvestmentID() {
		return iInvestmentID;
	}

	public void setiInvestmentID(Integer iInvestmentID) {
		this.iInvestmentID = iInvestmentID;
	}
	
    public String toString()
	{
		return description;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Integer getAssetClassID() {
		return assetClassID;
	}

	public void setAssetClassID(Integer assetClassID) {
		this.assetClassID = assetClassID;
	}

	public String getInvestmentTypeDescription() {
		return investmentTypeDescription;
	}

	public void setInvestmentTypeDescription(String investmentTypeDescription)
	{
		this.investmentTypeDescription = investmentTypeDescription;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public Integer getiInvestmentTypeID() {
		return iInvestmentTypeID;
	}

	public void setiInvestmentTypeID(Integer iInvestmentTypeID) {
		this.iInvestmentTypeID = iInvestmentTypeID;
	}

	@DynamoDbIgnore
	public BigDecimal getUnitsOwned() {
		return unitsOwned;
	}

	@DynamoDbIgnore
	public void setUnitsOwned(BigDecimal unitsOwned) {
		this.unitsOwned = unitsOwned;
	}

	@DynamoDbIgnore
	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	@DynamoDbIgnore
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	@DynamoDbIgnore
	public BigDecimal getHoldingPercentage() {
		return holdingPercentage;
	}

	@DynamoDbIgnore
	public void setHoldingPercentage(BigDecimal holdingPercentage) {
		this.holdingPercentage = holdingPercentage;
	}
   	
}
