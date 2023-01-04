package com.pas.dbobjects;

import java.io.Serializable;

public class Tblaccttypetrxtype implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer iacctTypeTrxType;
	private Integer iaccountTypeID;
    private Integer iTransactionTypeID;
    
    private com.pas.dbobjects.Tbltransactiontype tbltransactiontype;
    private com.pas.dbobjects.Tblaccounttype tblaccounttype;

    /** default constructor */
    public Tblaccttypetrxtype() {
    }

    public Integer getIacctTypeTrxType() {
        return this.iacctTypeTrxType;
    }

    public void setIacctTypeTrxType(Integer iacctTypeTrxType) {
        this.iacctTypeTrxType = iacctTypeTrxType;
    }

    public com.pas.dbobjects.Tbltransactiontype getTbltransactiontype() {
        return this.tbltransactiontype;
    }

    public void setTbltransactiontype(com.pas.dbobjects.Tbltransactiontype tbltransactiontype) {
        this.tbltransactiontype = tbltransactiontype;
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

	public Integer getiTransactionTypeID() {
		return iTransactionTypeID;
	}

	public void setiTransactionTypeID(Integer iTransactionTypeID) {
		this.iTransactionTypeID = iTransactionTypeID;
	}

}
