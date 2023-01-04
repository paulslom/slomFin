package com.pas.dbobjects;

import java.io.Serializable;

public class Tblportfolio implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer iportfolioId;
	private Integer iinvestorId;
    private String sportfolioName;
    private boolean btaxableInd;

    private com.pas.dbobjects.Tblinvestor tblinvestor;

    /** default constructor */
    public Tblportfolio() {
    }

    public Integer getIportfolioId() {
        return this.iportfolioId;
    }

    public void setIportfolioId(Integer iportfolioId) {
        this.iportfolioId = iportfolioId;
    }

    public String getSportfolioName() {
        return this.sportfolioName;
    }

    public void setSportfolioName(String sportfolioName) {
        this.sportfolioName = sportfolioName;
    }

    public boolean isBtaxableInd() {
        return this.btaxableInd;
    }

    public void setBtaxableInd(boolean btaxableInd) {
        this.btaxableInd = btaxableInd;
    }

    public com.pas.dbobjects.Tblinvestor getTblinvestor() {
        return this.tblinvestor;
    }

    public void setTblinvestor(com.pas.dbobjects.Tblinvestor tblinvestor) {
        this.tblinvestor = tblinvestor;
    }

	public Integer getIinvestorId() {
		return iinvestorId;
	}

	public void setIinvestorId(Integer iinvestorId) {
		this.iinvestorId = iinvestorId;
	}

}
