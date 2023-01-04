package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Tblportfoliohistory implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer iportfolioHistoryId;
	private Integer iaccountId;
    private Date dhistoryDate;
    private BigDecimal mvalue;

    private com.pas.dbobjects.Tblaccount tblaccount;

    public Tblportfoliohistory() {
    }

    /** minimal constructor */
    public Tblportfoliohistory(Integer iportfolioHistoryId, Date dhistoryDate, com.pas.dbobjects.Tblaccount tblaccount) {
        this.iportfolioHistoryId = iportfolioHistoryId;
        this.dhistoryDate = dhistoryDate;
        this.tblaccount = tblaccount;
    }

    public Integer getIportfolioHistoryId() {
        return this.iportfolioHistoryId;
    }

    public void setIportfolioHistoryId(Integer iportfolioHistoryId) {
        this.iportfolioHistoryId = iportfolioHistoryId;
    }

    public Date getDhistoryDate() {
        return this.dhistoryDate;
    }

    public void setDhistoryDate(Date dhistoryDate) {
        this.dhistoryDate = dhistoryDate;
    }

    public BigDecimal getMvalue() {
        return this.mvalue;
    }

    public void setMvalue(BigDecimal mvalue) {
        this.mvalue = mvalue;
    }

    public com.pas.dbobjects.Tblaccount getTblaccount() {
        return this.tblaccount;
    }

    public void setTblaccount(com.pas.dbobjects.Tblaccount tblaccount) {
        this.tblaccount = tblaccount;
    }

	public Integer getIaccountId() {
		return iaccountId;
	}

	public void setIaccountId(Integer iaccountId) {
		this.iaccountId = iaccountId;
	}

}
