package com.pas.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CapitalGain implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(Account.class);	
	
	private Integer saleTransactionID;
	private Integer accountID;
    private String accountName;
    private Integer investmentID;
    private String investmentName;
    private Date saleDate;
    private BigDecimal salePrice;
    private BigDecimal saleProceeds;
    private Integer saleInvestmentTypeID;
    private String holdingPeriod;
    private BigDecimal costBasis;
    private BigDecimal unitsSold;
    private BigDecimal gainOrLoss;
    private String cgStyleClass;
    
	public Integer getAccountID() {
		return accountID;
	}
	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Integer getInvestmentID() {
		return investmentID;
	}
	public void setInvestmentID(Integer investmentID) {
		this.investmentID = investmentID;
	}
	public String getInvestmentName() {
		return investmentName;
	}
	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}
	public Date getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	public String getHoldingPeriod() {
		return holdingPeriod;
	}
	public void setHoldingPeriod(String holdingPeriod) {
		this.holdingPeriod = holdingPeriod;
	}
	public BigDecimal getCostBasis() {
		return costBasis;
	}
	public void setCostBasis(BigDecimal costBasis) {
		this.costBasis = costBasis;
	}
	public BigDecimal getUnitsSold() {
		return unitsSold;
	}
	public void setUnitsSold(BigDecimal unitsSold) {
		this.unitsSold = unitsSold;
	}
	public BigDecimal getGainOrLoss() {
		return gainOrLoss;
	}
	public void setGainOrLoss(BigDecimal gainOrLoss) {
		this.gainOrLoss = gainOrLoss;
	}
	public Integer getSaleTransactionID() {
		return saleTransactionID;
	}
	public void setSaleTransactionID(Integer saleTransactionID) {
		this.saleTransactionID = saleTransactionID;
	}
	public String getCgStyleClass() {
		return cgStyleClass;
	}
	public void setCgStyleClass(String cgStyleClass) {
		this.cgStyleClass = cgStyleClass;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public BigDecimal getSaleProceeds() {
		return saleProceeds;
	}
	public void setSaleProceeds(BigDecimal saleProceeds) {
		this.saleProceeds = saleProceeds;
	}
	public Integer getSaleInvestmentTypeID() {
		return saleInvestmentTypeID;
	}
	public void setSaleInvestmentTypeID(Integer saleInvestmentTypeID) {
		this.saleInvestmentTypeID = saleInvestmentTypeID;
	}
	
}
