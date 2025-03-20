package com.pas.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountPosition implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static Logger logger = LogManager.getLogger(Account.class);	
	
	private Integer accountID;
    private String accountName;
    private Integer investmentID;
    private String investmentName;
    private BigDecimal investmentPrice;
    private BigDecimal unitsOwned;
    private BigDecimal positionValue;
    private Boolean taxableAccount;
    
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
	public BigDecimal getInvestmentPrice() {
		return investmentPrice;
	}
	public void setInvestmentPrice(BigDecimal investmentPrice) {
		this.investmentPrice = investmentPrice;
	}
	public BigDecimal getUnitsOwned() {
		return unitsOwned;
	}
	public void setUnitsOwned(BigDecimal unitsOwned) {
		this.unitsOwned = unitsOwned;
	}
	public BigDecimal getPositionValue() {
		return positionValue;
	}
	public void setPositionValue(BigDecimal positionValue) {
		this.positionValue = positionValue;
	}
	public Boolean getTaxableAccount() {
		return taxableAccount;
	}
	public void setTaxableAccount(Boolean taxableAccount) {
		this.taxableAccount = taxableAccount;
	}
}
