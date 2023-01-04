package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Tblpayday implements Serializable 
{
   	private static final long serialVersionUID = 1L;
   	
	private Integer ipaydayId;
    private Integer itransactionTypeId;
    private Integer iinvestorId;
    private Integer ixferAccountId;
    private Integer iaccountId;
    private Integer iwdcategoryId;
    private Integer icashdepositTypeId;
    private BigDecimal mdefaultAmt;
    private Integer idefaultDay;
    private int bnextMonthInd;
    private String sdescription;

    private com.pas.dbobjects.Tbltransactiontype tbltransactiontype;
    private com.pas.dbobjects.Tblinvestor tblinvestor;
    private com.pas.dbobjects.Tblaccount tblaccountByIXferAccountId;
    private com.pas.dbobjects.Tblaccount tblaccountByIAccountId;
    private com.pas.dbobjects.Tblwdcategory tblwdcategory;
    private com.pas.dbobjects.Tblcashdeposittype tblcashdeposittype;

    /** default constructor */
    public Tblpayday() {
    }

    public Integer getIpaydayId() {
        return this.ipaydayId;
    }

    public void setIpaydayId(Integer ipaydayId) {
        this.ipaydayId = ipaydayId;
    }

    public BigDecimal getMdefaultAmt() {
        return this.mdefaultAmt;
    }

    public void setMdefaultAmt(BigDecimal mdefaultAmt) {
        this.mdefaultAmt = mdefaultAmt;
    }

    public Integer getIdefaultDay() {
        return this.idefaultDay;
    }

    public void setIdefaultDay(Integer idefaultDay) {
        this.idefaultDay = idefaultDay;
    }

    public int getBnextMonthInd() {
        return this.bnextMonthInd;
    }

    public void setBnextMonthInd(int bnextMonthInd) {
        this.bnextMonthInd = bnextMonthInd;
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

    public com.pas.dbobjects.Tblinvestor getTblinvestor() {
        return this.tblinvestor;
    }

    public void setTblinvestor(com.pas.dbobjects.Tblinvestor tblinvestor) {
        this.tblinvestor = tblinvestor;
    }

    public com.pas.dbobjects.Tblaccount getTblaccountByIXferAccountId() {
        return this.tblaccountByIXferAccountId;
    }

    public void setTblaccountByIXferAccountId(com.pas.dbobjects.Tblaccount tblaccountByIXferAccountId) {
        this.tblaccountByIXferAccountId = tblaccountByIXferAccountId;
    }

    public com.pas.dbobjects.Tblaccount getTblaccountByIAccountId() {
        return this.tblaccountByIAccountId;
    }

    public void setTblaccountByIAccountId(com.pas.dbobjects.Tblaccount tblaccountByIAccountId) {
        this.tblaccountByIAccountId = tblaccountByIAccountId;
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

	public Integer getItransactionTypeId() {
		return itransactionTypeId;
	}

	public void setItransactionTypeId(Integer itransactionTypeId) {
		this.itransactionTypeId = itransactionTypeId;
	}

	public Integer getIinvestorId() {
		return iinvestorId;
	}

	public void setIinvestorId(Integer iinvestorId) {
		this.iinvestorId = iinvestorId;
	}

	public Integer getIxferAccountId() {
		return ixferAccountId;
	}

	public void setIxferAccountId(Integer ixferAccountId) {
		this.ixferAccountId = ixferAccountId;
	}

	public Integer getIaccountId() {
		return iaccountId;
	}

	public void setIaccountId(Integer iaccountId) {
		this.iaccountId = iaccountId;
	}

	public Integer getIwdcategoryId() {
		return iwdcategoryId;
	}

	public void setIwdcategoryId(Integer iwdcategoryId) {
		this.iwdcategoryId = iwdcategoryId;
	}

	public Integer getIcashdepositTypeId() {
		return icashdepositTypeId;
	}

	public void setIcashdepositTypeId(Integer icashdepositTypeId) {
		this.icashdepositTypeId = icashdepositTypeId;
	}

}
