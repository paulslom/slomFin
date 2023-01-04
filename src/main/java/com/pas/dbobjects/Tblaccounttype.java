package com.pas.dbobjects;

import java.io.Serializable;

public class Tblaccounttype implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
	private Integer iaccountTypeId;
    private String saccountType;

    /** default constructor */
    public Tblaccounttype() {
    }

    public Integer getIaccountTypeId() {
        return this.iaccountTypeId;
    }

    public void setIaccountTypeId(Integer iaccountTypeId) {
        this.iaccountTypeId = iaccountTypeId;
    }

    public String getSaccountType() {
        return this.saccountType;
    }

    public void setSaccountType(String saccountType) {
        this.saccountType = saccountType;
    }

    
}
