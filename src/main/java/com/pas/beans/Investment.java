package com.pas.beans;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Investment implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(Investment.class);	
	
	private Integer investmentID;
	private Integer investmentTypeID;
	private String  tickerSymbol;
	private String description;
	private BigDecimal currentPrice; 
	private Integer assetClassID;
	private String investmentTypeDescription;
	private String assetClass;
	    
    public String toString()
	{
		return description;
	}

    @DynamoDbPartitionKey
	public Integer getInvestmentID() {
		return investmentID;
	}

	public void setInvestmentID(Integer iInvestmentID) {
		this.investmentID = iInvestmentID;
	}

	public Integer getInvestmentTypeID() {
		return investmentTypeID;
	}

	public void setInvestmentTypeID(Integer iInvestmentTypeID) {
		this.investmentTypeID = iInvestmentTypeID;
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
	
	
	
   	
}
