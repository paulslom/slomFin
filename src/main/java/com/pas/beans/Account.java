package com.pas.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Account implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(Account.class);	
	
	private Integer iAccountID;
	private Integer iPortfolioID;
    private Integer iAccountTypeID;
    private String sAccountName;
    private String sAccountNameAbbr;
    private Boolean bClosed;
    private String sPortfolioName;
    private Boolean bTaxableInd;
    private String sAccountType;
    
    //not to be saved to Dynamo
    private BigDecimal currentAccountValue;
    private BigDecimal yearlyContribution = new BigDecimal(0.0); //default to zero
    private BigDecimal percentReturn = new BigDecimal(5.0); //default to 5%
    private Integer projectionYear;
    
    public String toString()
    {
    	return this.getsAccountName();
    }

    @DynamoDbPartitionKey
	public Integer getiAccountID() {
		return iAccountID;
	}

	public void setiAccountID(Integer iAccountID) {
		this.iAccountID = iAccountID;
	}

	public Integer getiPortfolioID() {
		return iPortfolioID;
	}

	public void setiPortfolioID(Integer iPortfolioID) {
		this.iPortfolioID = iPortfolioID;
	}

	public Integer getiAccountTypeID() {
		return iAccountTypeID;
	}

	public void setiAccountTypeID(Integer iAccountTypeID) {
		this.iAccountTypeID = iAccountTypeID;
	}

	public String getsAccountName() {
		return sAccountName;
	}

	public void setsAccountName(String sAccountName) {
		this.sAccountName = sAccountName;
	}

	public String getsAccountNameAbbr() {
		return sAccountNameAbbr;
	}

	public void setsAccountNameAbbr(String sAccountNameAbbr) {
		this.sAccountNameAbbr = sAccountNameAbbr;
	}

	public Boolean getbClosed() {
		return bClosed;
	}

	public void setbClosed(Boolean bClosed) {
		this.bClosed = bClosed;
	}

	public String getsPortfolioName() {
		return sPortfolioName;
	}

	public void setsPortfolioName(String sPortfolioName) {
		this.sPortfolioName = sPortfolioName;
	}

	public Boolean getbTaxableInd() {
		return bTaxableInd;
	}

	public void setbTaxableInd(Boolean bTaxableInd) {
		this.bTaxableInd = bTaxableInd;
	}

	public String getsAccountType() {
		return sAccountType;
	}

	public void setsAccountType(String sAccountType) {
		this.sAccountType = sAccountType;
	}

	@DynamoDbIgnore
	public BigDecimal getCurrentAccountValue() {
		return currentAccountValue;
	}

	@DynamoDbIgnore
	public void setCurrentAccountValue(BigDecimal currentAccountValue) {
		this.currentAccountValue = currentAccountValue;
	}

	@DynamoDbIgnore
	public BigDecimal getYearlyContribution() {
		return yearlyContribution;
	}

	@DynamoDbIgnore
	public void setYearlyContribution(BigDecimal yearlyContribution) {
		this.yearlyContribution = yearlyContribution;
	}

	@DynamoDbIgnore
	public BigDecimal getPercentReturn() {
		return percentReturn;
	}

	@DynamoDbIgnore
	public void setPercentReturn(BigDecimal percentReturn) {
		this.percentReturn = percentReturn;
	}

	@DynamoDbIgnore
	public Integer getProjectionYear() {
		return projectionYear;
	}

	@DynamoDbIgnore
	public void setProjectionYear(Integer projectionYear) {
		this.projectionYear = projectionYear;
	}

	
}
