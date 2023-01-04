package com.pas.dbobjects;

import java.io.Serializable;

public class Tblcashdeposittype implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer icashDepositTypeId;
    private String scashDepositTypeDesc;

    /** default constructor */
    public Tblcashdeposittype() {
    }

    public Integer getIcashDepositTypeId() {
        return this.icashDepositTypeId;
    }

    public void setIcashDepositTypeId(Integer icashDepositTypeId) {
        this.icashDepositTypeId = icashDepositTypeId;
    }

    public String getScashDepositTypeDesc() {
        return this.scashDepositTypeDesc;
    }

    public void setScashDepositTypeDesc(String scashDepositTypeDesc) {
        this.scashDepositTypeDesc = scashDepositTypeDesc;
    }

    
}
