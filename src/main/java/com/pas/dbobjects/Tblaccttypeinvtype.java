package com.pas.dbobjects;

import java.io.Serializable;

public class Tblaccttypeinvtype implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
    private Integer iacctTypeInvTypeId;
    private Integer iaccountTypeID;
    private Integer iInvestmentTypeID;

    private com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype;
    private com.pas.dbobjects.Tblaccounttype tblaccounttype;

    /** default constructor */
    public Tblaccttypeinvtype() {
    }

    public Integer getIacctTypeInvTypeId() {
        return this.iacctTypeInvTypeId;
    }

    public void setIacctTypeInvTypeId(Integer iacctTypeInvTypeId) {
        this.iacctTypeInvTypeId = iacctTypeInvTypeId;
    }

    public com.pas.dbobjects.Tblinvestmenttype getTblinvestmenttype() {
        return this.tblinvestmenttype;
    }

    public void setTblinvestmenttype(com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype) {
        this.tblinvestmenttype = tblinvestmenttype;
    }

    public com.pas.dbobjects.Tblaccounttype getTblaccounttype() {
        return this.tblaccounttype;
    }

    public void setTblaccounttype(com.pas.dbobjects.Tblaccounttype tblaccounttype) {
        this.tblaccounttype = tblaccounttype;
    }

	public Integer getIaccountTypeID() {
		return iaccountTypeID;
	}

	public void setIaccountTypeID(Integer iaccountTypeID) {
		this.iaccountTypeID = iaccountTypeID;
	}

	public Integer getiInvestmentTypeID() {
		return iInvestmentTypeID;
	}

	public void setiInvestmentTypeID(Integer iInvestmentTypeID) {
		this.iInvestmentTypeID = iInvestmentTypeID;
	}

}
