package com.pas.dbobjects;

import java.io.Serializable;

public class Tblwdcategory implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer iwdcategoryId;
    private String swdcategoryDescription;
    private Integer iInvestorID;

    private com.pas.dbobjects.Tblinvestor tblinvestor;

    /** default constructor */
    public Tblwdcategory() {
    }

    public Integer getIwdcategoryId() {
        return this.iwdcategoryId;
    }

    public void setIwdcategoryId(Integer iwdcategoryId) {
        this.iwdcategoryId = iwdcategoryId;
    }

    public String getSwdcategoryDescription() {
        return this.swdcategoryDescription;
    }

    public void setSwdcategoryDescription(String swdcategoryDescription) {
        this.swdcategoryDescription = swdcategoryDescription;
    }

    public com.pas.dbobjects.Tblinvestor getTblinvestor() {
        return this.tblinvestor;
    }

    public void setTblinvestor(com.pas.dbobjects.Tblinvestor tblinvestor) {
        this.tblinvestor = tblinvestor;
    }

    public Integer getiInvestorID() {
		return iInvestorID;
	}

	public void setiInvestorID(Integer iInvestorID) {
		this.iInvestorID = iInvestorID;
	}

}
