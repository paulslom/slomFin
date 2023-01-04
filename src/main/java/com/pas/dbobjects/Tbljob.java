package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Tbljob implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer ijobId;
	private Integer iinvestorId;
    private String semployer;
    private int ipaydaysPerYear;
    private Date djobStartDate;
    private Date djobEndDate;
    private BigDecimal mgrossPay;
    private BigDecimal mfederalWithholding;
    private BigDecimal mstateWithholding;
    private BigDecimal mretirementDeferred;
    private BigDecimal msswithholding;
    private BigDecimal mmedicareWithholding;
    private BigDecimal mdental;
    private BigDecimal mmedical;
    private BigDecimal mgroupLifeInsurance;
    private String sincomeState;
    private BigDecimal mgroupLifeIncome;
    private BigDecimal mvision;
    private BigDecimal mparking;
    private BigDecimal mcafeteria;
    private BigDecimal mroth401k;
    private BigDecimal mfsaAmount;
    private String semployerFedIdno;
    private String semployerStateIdno;

    private com.pas.dbobjects.Tblinvestor tblinvestor;
    
    public Tbljob() {
    }

    public Integer getIjobId() {
        return this.ijobId;
    }

    public void setIjobId(Integer ijobId) {
        this.ijobId = ijobId;
    }

    public String getSemployer() {
        return this.semployer;
    }

    public void setSemployer(String semployer) {
        this.semployer = semployer;
    }

    public int getIpaydaysPerYear() {
        return this.ipaydaysPerYear;
    }

    public void setIpaydaysPerYear(int ipaydaysPerYear) {
        this.ipaydaysPerYear = ipaydaysPerYear;
    }

    public Date getDjobStartDate() {
        return this.djobStartDate;
    }

    public void setDjobStartDate(Date djobStartDate) {
        this.djobStartDate = djobStartDate;
    }

    public Date getDjobEndDate() {
        return this.djobEndDate;
    }

    public void setDjobEndDate(Date djobEndDate) {
        this.djobEndDate = djobEndDate;
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

    public String getSincomeState() {
        return this.sincomeState;
    }

    public void setSincomeState(String sincomeState) {
        this.sincomeState = sincomeState;
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

    public String getSemployerFedIdno() {
        return this.semployerFedIdno;
    }

    public void setSemployerFedIdno(String semployerFedIdno) {
        this.semployerFedIdno = semployerFedIdno;
    }

    public String getSemployerStateIdno() {
        return this.semployerStateIdno;
    }

    public void setSemployerStateIdno(String semployerStateIdno) {
        this.semployerStateIdno = semployerStateIdno;
    }

    public com.pas.dbobjects.Tblinvestor getTblinvestor() {
        return this.tblinvestor;
    }

    public void setTblinvestor(com.pas.dbobjects.Tblinvestor tblinvestor) {
        this.tblinvestor = tblinvestor;
    }

    public BigDecimal getMfsaAmount() {
		return mfsaAmount;
	}

	public void setMfsaAmount(BigDecimal mfsaAmount) {
		this.mfsaAmount = mfsaAmount;
	}

	public Integer getIinvestorId() {
		return iinvestorId;
	}

	public void setIinvestorId(Integer iinvestorId) {
		this.iinvestorId = iinvestorId;
	}


}
