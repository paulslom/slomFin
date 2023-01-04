package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tbltransaction implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer itransactionId;
	private Integer iaccountID;
	private Integer iinvestmentID;
	private Integer iTransactionTypeID;
	private Integer iOptionTypeID;
	private Integer iWDCategoryID;
	private Integer iCashDepositTypeID;
	private Date dtransactionDate;
    private BigDecimal decUnits;
    private BigDecimal mprice;
    private BigDecimal mcostProceeds;
    private Date dtranEntryDate;
    private Date dtranPostedDate;
    private Date dtranChangeDate;
    private BigDecimal meffectiveAmount;
    private Integer idividendTaxableYear;
    private BigDecimal mstrikePrice;
    private Date dexpirationDate;
    private boolean bopeningTrxInd;
    private boolean bfinalTrxOfBillingCycle;
    private Integer icheckNo;
    private String sdescription;

    private com.pas.dbobjects.Tbltransactiontype tbltransactiontype;
    private com.pas.dbobjects.Tblaccount tblaccount;
    private com.pas.dbobjects.Tbloptiontype tbloptiontype;
    private com.pas.dbobjects.Tblinvestment tblinvestment;
    private com.pas.dbobjects.Tblwdcategory tblwdcategory;
    private com.pas.dbobjects.Tblcashdeposittype tblcashdeposittype;

    //additional fields not on DB
    private String trxDateDayOfWeek;   
    private Integer xferAccountID;
    private BigDecimal macctBalance;
    private boolean spawnedXfer = false;

	/** default constructor */
    public Tbltransaction() {
    }

    public Integer getItransactionId() {
        return this.itransactionId;
    }

    public void setItransactionId(Integer itransactionId) {
        this.itransactionId = itransactionId;
    }

    public Date getDtransactionDate() {
        return this.dtransactionDate;
    }

    public void setDtransactionDate(Date dtransactionDate) {
        this.dtransactionDate = dtransactionDate;
        setTrxDateDayOfWeek(dtransactionDate);
    }

    public BigDecimal getDecUnits() {
        return this.decUnits;
    }

    public void setDecUnits(BigDecimal decUnits) {
        this.decUnits = decUnits;
    }

    public BigDecimal getMprice() {
        return this.mprice;
    }

    public void setMprice(BigDecimal mprice) {
        this.mprice = mprice;
    }

    public BigDecimal getMcostProceeds() {
        return this.mcostProceeds;
    }

    public void setMcostProceeds(BigDecimal mcostProceeds) {
        this.mcostProceeds = mcostProceeds;
    }

    public Date getDtranEntryDate() {
        return this.dtranEntryDate;
    }

    public void setDtranEntryDate(Date dtranEntryDate) {
        this.dtranEntryDate = dtranEntryDate;
    }

    public Date getDtranChangeDate() {
        return this.dtranChangeDate;
    }

    public void setDtranChangeDate(Date dtranChangeDate) {
        this.dtranChangeDate = dtranChangeDate;
    }

    public BigDecimal getMacctBalance() {
        return this.macctBalance;
    }

    public void setMacctBalance(BigDecimal macctBalance) {
        this.macctBalance = macctBalance;
    }

    public Integer getIdividendTaxableYear() {
        return this.idividendTaxableYear;
    }

    public void setIdividendTaxableYear(Integer idividendTaxableYear) {
        this.idividendTaxableYear = idividendTaxableYear;
    }

    public BigDecimal getMstrikePrice() {
        return this.mstrikePrice;
    }

    public void setMstrikePrice(BigDecimal mstrikePrice) {
        this.mstrikePrice = mstrikePrice;
    }

    public Date getDexpirationDate() {
        return this.dexpirationDate;
    }

    public void setDexpirationDate(Date dexpirationDate) {
        this.dexpirationDate = dexpirationDate;
    }

    public boolean isBopeningTrxInd() {
        return this.bopeningTrxInd;
    }

    public void setBopeningTrxInd(boolean bopeningTrxInd) {
        this.bopeningTrxInd = bopeningTrxInd;
    }

    public Integer getIcheckNo() {
        return this.icheckNo;
    }

    public void setIcheckNo(Integer icheckNo) {
        this.icheckNo = icheckNo;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public com.pas.dbobjects.Tbltransactiontype getTbltransactiontype() {
        return this.tbltransactiontype;
    }

    public void setTbltransactiontype(com.pas.dbobjects.Tbltransactiontype tbltransactiontype) {
        this.tbltransactiontype = tbltransactiontype;
    }

    public com.pas.dbobjects.Tblaccount getTblaccount() {
        return this.tblaccount;
    }

    public void setTblaccount(com.pas.dbobjects.Tblaccount tblaccount) {
        this.tblaccount = tblaccount;
    }

    public com.pas.dbobjects.Tbloptiontype getTbloptiontype() {
        return this.tbloptiontype;
    }

    public void setTbloptiontype(com.pas.dbobjects.Tbloptiontype tbloptiontype) {
        this.tbloptiontype = tbloptiontype;
    }

    public com.pas.dbobjects.Tblinvestment getTblinvestment() {
        return this.tblinvestment;
    }

    public void setTblinvestment(com.pas.dbobjects.Tblinvestment tblinvestment) {
        this.tblinvestment = tblinvestment;
    }

    public com.pas.dbobjects.Tblwdcategory getTblwdcategory() {
        return this.tblwdcategory;
    }

    public void setTblwdcategory(com.pas.dbobjects.Tblwdcategory tblwdcategory) {
        this.tblwdcategory = tblwdcategory;
    }

    public com.pas.dbobjects.Tblcashdeposittype getTblcashdeposittype() {
        return this.tblcashdeposittype;
    }

    public void setTblcashdeposittype(com.pas.dbobjects.Tblcashdeposittype tblcashdeposittype) {
        this.tblcashdeposittype = tblcashdeposittype;
    }

    public String getTrxDateDayOfWeek()
    {
		return trxDateDayOfWeek;
	}

    public void setTrxDateDayOfWeek(Date transactionDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE");
		this.trxDateDayOfWeek = sdf.format(transactionDate);
	}
    
    public Integer getXferAccountID()
	{
		return xferAccountID;
	}

	public void setXferAccountID(Integer xferAccountID)
	{
		this.xferAccountID = xferAccountID;
	}

	public BigDecimal getMeffectiveAmount()
	{
		return meffectiveAmount;
	}

	public void setMeffectiveAmount(BigDecimal meffectiveAmount)
	{
		this.meffectiveAmount = meffectiveAmount;
	}

	public Date getDtranPostedDate()
	{
		return dtranPostedDate;
	}

	public void setDtranPostedDate(Date dtranPostedDate)
	{
		this.dtranPostedDate = dtranPostedDate;
	}
	public boolean isBfinalTrxOfBillingCycle() {
		return bfinalTrxOfBillingCycle;
	}

	public void setBfinalTrxOfBillingCycle(boolean bFinalTrxOfBillingCycle) {
		this.bfinalTrxOfBillingCycle = bFinalTrxOfBillingCycle;
	}

	public Integer getiTransactionTypeID() {
		return iTransactionTypeID;
	}

	public void setiTransactionTypeID(Integer iTransactionTypeID) {
		this.iTransactionTypeID = iTransactionTypeID;
	}

	public Integer getiOptionTypeID() {
		return iOptionTypeID;
	}

	public void setiOptionTypeID(Integer iOptionTypeID) {
		this.iOptionTypeID = iOptionTypeID;
	}

	public Integer getiWDCategoryID() {
		return iWDCategoryID;
	}

	public void setiWDCategoryID(Integer iWDCategoryID) {
		this.iWDCategoryID = iWDCategoryID;
	}

	public Integer getiCashDepositTypeID() {
		return iCashDepositTypeID;
	}

	public void setiCashDepositTypeID(Integer iCashDepositTypeID) {
		this.iCashDepositTypeID = iCashDepositTypeID;
	}

	public Integer getIinvestmentID() {
		return iinvestmentID;
	}

	public void setIinvestmentID(Integer iinvestmentID) {
		this.iinvestmentID = iinvestmentID;
	}

	public Integer getIaccountID() {
		return iaccountID;
	}

	public void setIaccountID(Integer iaccountID) {
		this.iaccountID = iaccountID;
	}

	public boolean isSpawnedXfer() {
		return spawnedXfer;
	}

	public void setSpawnedXfer(boolean spawnedXfer) {
		this.spawnedXfer = spawnedXfer;
	}


}
