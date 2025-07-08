package com.pas.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostBasis implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(Account.class);	
	
	private Integer accountID;
    private String accountName;
    private Integer investmentID;
    private String investmentName;
    private BigDecimal unitsOwned;
    private BigDecimal costBasis;
    private BigDecimal currentValue;
    private BigDecimal gainOrLoss;
    private String cbStyleClass;
    
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
	public BigDecimal getUnitsOwned() {
		return unitsOwned;
	}
	public void setUnitsOwned(BigDecimal unitsOwned) {
		this.unitsOwned = unitsOwned;
	}
	public BigDecimal getCostBasis() {
		return costBasis;
	}
	public void setCostBasis(BigDecimal costBasis) {
		this.costBasis = costBasis;
	}
	public BigDecimal getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}
	public BigDecimal getGainOrLoss() {
		return gainOrLoss;
	}
	public void setGainOrLoss(BigDecimal gainOrLoss) {
		this.gainOrLoss = gainOrLoss;
	}
	public String getCbStyleClass() {
		return cbStyleClass;
	}
	public void setCbStyleClass(String cbStyleClass) {
		this.cbStyleClass = cbStyleClass;
	}
    
	
}
