package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Tblpaydayhistory implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer ipaydayHistoryId;
	private Integer ijobId;
	private Integer ipaychecktypeid;
    private Date dpaydayHistoryDate;
    private BigDecimal mgrossPay;
    private BigDecimal mfederalWithholding;
    private BigDecimal mstateWithholding;
    private BigDecimal mretirementDeferred;
    private BigDecimal msswithholding;
    private BigDecimal mmedicareWithholding;
    private BigDecimal mfsaAmount;
    private BigDecimal mdental;
    private BigDecimal mmedical;
    private BigDecimal mgroupLifeInsurance;
    private BigDecimal mgroupLifeIncome;
    private BigDecimal mvision;
    private BigDecimal mparking;
    private BigDecimal mcafeteria;
    private BigDecimal mroth401k;

    private com.pas.dbobjects.Tblpaychecktype tblpaychecktype;
    private com.pas.dbobjects.Tbljob tbljob;

    /** default constructor */
    public Tblpaydayhistory() {
    }

    public Integer getIpaydayHistoryId() {
        return this.ipaydayHistoryId;
    }

    public void setIpaydayHistoryId(Integer ipaydayHistoryId) {
        this.ipaydayHistoryId = ipaydayHistoryId;
    }

    public Date getDpaydayHistoryDate() {
        return this.dpaydayHistoryDate;
    }

    public void setDpaydayHistoryDate(Date dpaydayHistoryDate) {
        this.dpaydayHistoryDate = dpaydayHistoryDate;
    }

    public BigDecimal getMgrossPay() {
        return this.mgrossPay;
    }

    public void setMgrossPay(BigDecimal mgrossPay) {
        this.mgrossPay = mgrossPay;
    }

    public BigDecimal getMfederalWithholding() {
        return this.mfederalWithholding;
    }

    public void setMfederalWithholding(BigDecimal mfederalWithholding) {
        this.mfederalWithholding = mfederalWithholding;
    }

    public BigDecimal getMstateWithholding() {
        return this.mstateWithholding;
    }

    public void setMstateWithholding(BigDecimal mstateWithholding) {
        this.mstateWithholding = mstateWithholding;
    }

    public BigDecimal getMretirementDeferred() {
        return this.mretirementDeferred;
    }

    public void setMretirementDeferred(BigDecimal mretirementDeferred) {
        this.mretirementDeferred = mretirementDeferred;
    }

    public BigDecimal getMsswithholding() {
        return this.msswithholding;
    }

    public void setMsswithholding(BigDecimal msswithholding) {
        this.msswithholding = msswithholding;
    }

    public BigDecimal getMmedicareWithholding() {
        return this.mmedicareWithholding;
    }

    public void setMmedicareWithholding(BigDecimal mmedicareWithholding) {
        this.mmedicareWithholding = mmedicareWithholding;
    }

    public BigDecimal getMdental() {
        return this.mdental;
    }

    public void setMdental(BigDecimal mdental) {
        this.mdental = mdental;
    }

    public BigDecimal getMmedical() {
        return this.mmedical;
    }

    public void setMmedical(BigDecimal mmedical) {
        this.mmedical = mmedical;
    }

    public BigDecimal getMgroupLifeInsurance() {
        return this.mgroupLifeInsurance;
    }

    public void setMgroupLifeInsurance(BigDecimal mgroupLifeInsurance) {
        this.mgroupLifeInsurance = mgroupLifeInsurance;
    }

    public BigDecimal getMgroupLifeIncome() {
        return this.mgroupLifeIncome;
    }

    public void setMgroupLifeIncome(BigDecimal mgroupLifeIncome) {
        this.mgroupLifeIncome = mgroupLifeIncome;
    }

    public BigDecimal getMvision() {
        return this.mvision;
    }

    public void setMvision(BigDecimal mvision) {
        this.mvision = mvision;
    }

    public BigDecimal getMparking() {
        return this.mparking;
    }

    public void setMparking(BigDecimal mparking) {
        this.mparking = mparking;
    }

    public BigDecimal getMcafeteria() {
        return this.mcafeteria;
    }

    public void setMcafeteria(BigDecimal mcafeteria) {
        this.mcafeteria = mcafeteria;
    }

    public BigDecimal getMroth401k() {
        return this.mroth401k;
    }

    public void setMroth401k(BigDecimal mroth401k) {
        this.mroth401k = mroth401k;
    }

    public com.pas.dbobjects.Tblpaychecktype getTblpaychecktype() {
        return this.tblpaychecktype;
    }

    public void setTblpaychecktype(com.pas.dbobjects.Tblpaychecktype tblpaychecktype) {
        this.tblpaychecktype = tblpaychecktype;
    }

    public com.pas.dbobjects.Tbljob getTbljob() {
        return this.tbljob;
    }

    public void setTbljob(com.pas.dbobjects.Tbljob tbljob) {
        this.tbljob = tbljob;
    }

    public BigDecimal getMfsaAmount() {
		return mfsaAmount;
	}

	public void setMfsaAmount(BigDecimal mfsaAmount) {
		this.mfsaAmount = mfsaAmount;
	}

	public Integer getIjobId() {
		return ijobId;
	}

	public void setIjobId(Integer ijobId) {
		this.ijobId = ijobId;
	}

	public Integer getIpaychecktypeid() {
		return ipaychecktypeid;
	}

	public void setIpaychecktypeid(Integer ipaychecktypeid) {
		this.ipaychecktypeid = ipaychecktypeid;
	}

	
}
