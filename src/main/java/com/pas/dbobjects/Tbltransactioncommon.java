package com.pas.dbobjects;

import java.io.Serializable;

public class Tbltransactioncommon implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
	private Integer itrxCommonId;
    private String spictureName;
    private Integer itrxCommonInvestorId;
    private Integer itrxCommonAccountId;
    private Integer itrxCommonTrxTypeId;
    
    private com.pas.dbobjects.Tbltransactiontype tbltransactiontype;
    private com.pas.dbobjects.Tblinvestor tblinvestor;
    private com.pas.dbobjects.Tblaccount tblaccount;

    /** default constructor */
    public Tbltransactioncommon() {
    }

    public Integer getItrxCommonId() {
        return this.itrxCommonId;
    }

    public void setItrxCommonId(Integer itrxCommonId) {
        this.itrxCommonId = itrxCommonId;
    }

    public String getSpictureName() {
        return this.spictureName;
    }

    public void setSpictureName(String spictureName) {
        this.spictureName = spictureName;
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

    public com.pas.dbobjects.Tblaccount getTblaccount() {
        return this.tblaccount;
    }

    public void setTblaccount(com.pas.dbobjects.Tblaccount tblaccount) {
        this.tblaccount = tblaccount;
    }

	public Integer getItrxCommonInvestorId() {
		return itrxCommonInvestorId;
	}

	public void setItrxCommonInvestorId(Integer itrxCommonInvestorId) {
		this.itrxCommonInvestorId = itrxCommonInvestorId;
	}

	public Integer getItrxCommonAccountId() {
		return itrxCommonAccountId;
	}

	public void setItrxCommonAccountId(Integer itrxCommonAccountId) {
		this.itrxCommonAccountId = itrxCommonAccountId;
	}

	public Integer getItrxCommonTrxTypeId() {
		return itrxCommonTrxTypeId;
	}

	public void setItrxCommonTrxTypeId(Integer itrxCommonTrxTypeId) {
		this.itrxCommonTrxTypeId = itrxCommonTrxTypeId;
	}   

}
