package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Tblaccount implements Serializable 
{
    private Integer iaccountId;
    private Integer iaccountTypeId;
    private Integer iportfolioId;
    private Integer ibrokerId;
    private String saccountName;
    private String saccountNameAbbr;
    private Integer istartingCheckNo;
    private String saccountNumber;
    private String spin;
    private boolean bclosed;
    private Integer iinterestPaymentsPerYear;
    private BigDecimal dinterestRate;
    private BigDecimal mminInterestBalance;
    private boolean btaxableInd;
    private BigDecimal mnewMoneyPerYear;
    private BigDecimal destimatedRateofReturn;
    private BigDecimal dvestingPercentage;
    
    private com.pas.dbobjects.Tblportfolio tblportfolio;   
    private com.pas.dbobjects.Tblbroker tblbroker;
    private com.pas.dbobjects.Tblaccounttype tblaccounttype;
   
    private String openOrClosedString;    

	/** default constructor */
    public Tblaccount() 
    {
    }

    public Integer getIaccountId() {
        return this.iaccountId;
    }

    public void setIaccountId(Integer iaccountId) {
        this.iaccountId = iaccountId;
    }

    public String getSaccountName() {
        return this.saccountName;
    }

    public void setSaccountName(String saccountName) {
        this.saccountName = saccountName;
    }

    public String getSaccountNameAbbr() {
        return this.saccountNameAbbr;
    }

    public void setSaccountNameAbbr(String saccountNameAbbr) {
        this.saccountNameAbbr = saccountNameAbbr;
    }

    public Integer getIstartingCheckNo() {
        return this.istartingCheckNo;
    }

    public void setIstartingCheckNo(Integer istartingCheckNo) {
        this.istartingCheckNo = istartingCheckNo;
    }

    public String getSaccountNumber() {
        return this.saccountNumber;
    }

    public void setSaccountNumber(String saccountNumber) {
        this.saccountNumber = saccountNumber;
    }

    public String getSpin() {
        return this.spin;
    }

    public void setSpin(String spin) {
        this.spin = spin;
    }

    public boolean isBclosed() {
        return this.bclosed;
    }

    public void setBclosed(boolean bclosed) {
        this.bclosed = bclosed;
        this.setOpenOrClosedString(bclosed);
    }

    public Integer getIinterestPaymentsPerYear() {
        return this.iinterestPaymentsPerYear;
    }

    public void setIinterestPaymentsPerYear(Integer iinterestPaymentsPerYear) {
        this.iinterestPaymentsPerYear = iinterestPaymentsPerYear;
    }

    public BigDecimal getDinterestRate() {
        return this.dinterestRate;
    }

    public void setDinterestRate(BigDecimal dinterestRate) {
        this.dinterestRate = dinterestRate;
    }

    public BigDecimal getMminInterestBalance() {
        return this.mminInterestBalance;
    }

    public void setMminInterestBalance(BigDecimal mminInterestBalance) {
        this.mminInterestBalance = mminInterestBalance;
    }

    public boolean isBtaxableInd() {
        return this.btaxableInd;
    }

    public void setBtaxableInd(boolean btaxableInd) {
        this.btaxableInd = btaxableInd;
    }

    public BigDecimal getMnewMoneyPerYear() {
        return this.mnewMoneyPerYear;
    }

    public void setMnewMoneyPerYear(BigDecimal mnewMoneyPerYear) {
        this.mnewMoneyPerYear = mnewMoneyPerYear;
    }

    public BigDecimal getDestimatedRateofReturn() {
        return this.destimatedRateofReturn;
    }

    public void setDestimatedRateofReturn(BigDecimal destimatedRateofReturn) {
        this.destimatedRateofReturn = destimatedRateofReturn;
    }

    public BigDecimal getDvestingPercentage() {
        return this.dvestingPercentage;
    }

    public void setDvestingPercentage(BigDecimal dvestingPercentage) {
        this.dvestingPercentage = dvestingPercentage;
    }

    public com.pas.dbobjects.Tblportfolio getTblportfolio() {
        return this.tblportfolio;
    }

    public void setTblportfolio(com.pas.dbobjects.Tblportfolio tblportfolio) {
        this.tblportfolio = tblportfolio;
    }

    public com.pas.dbobjects.Tblbroker getTblbroker() {
        return this.tblbroker;
    }

    public void setTblbroker(com.pas.dbobjects.Tblbroker tblbroker) {
        this.tblbroker = tblbroker;
    }

    public com.pas.dbobjects.Tblaccounttype getTblaccounttype() {
        return this.tblaccounttype;
    }

    public void setTblaccounttype(com.pas.dbobjects.Tblaccounttype tblaccounttype) {
        this.tblaccounttype = tblaccounttype;
    }

   
    public String getOpenOrClosedString()
	{
		return openOrClosedString;
	}

	public void setOpenOrClosedString(boolean bclosed2)
	{
		if (bclosed2)
		   this.openOrClosedString = "Closed";
		else
		   this.openOrClosedString = "Open";	
	}

	public void setOpenOrClosedString(String openOrClosedString) {
		this.openOrClosedString = openOrClosedString;
	}

	public Integer getIaccountTypeId() {
		return iaccountTypeId;
	}

	public void setIaccountTypeId(Integer iaccountTypeId) {
		this.iaccountTypeId = iaccountTypeId;
	}

	public Integer getIportfolioId() {
		return iportfolioId;
	}

	public void setIportfolioId(Integer iportfolioId) {
		this.iportfolioId = iportfolioId;
	}

	public Integer getIbrokerId() {
		return ibrokerId;
	}

	public void setIbrokerId(Integer ibrokerId) {
		this.ibrokerId = ibrokerId;
	}

}
