package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Tblbroker implements Serializable 
{
    private static final long serialVersionUID = 1L;

	private Integer ibrokerId;
    private String sbrokerName;
    private BigDecimal moptionContractCommission;
    private BigDecimal dmarginInterestRate;
    
    private com.pas.dbobjects.Tblcommission tblcommission;
   
    /** default constructor */
    public Tblbroker() {
    }

    public Integer getIbrokerId() {
        return this.ibrokerId;
    }

    public void setIbrokerId(Integer ibrokerId) {
        this.ibrokerId = ibrokerId;
    }

    public String getSbrokerName() {
        return this.sbrokerName;
    }

    public void setSbrokerName(String sbrokerName) {
        this.sbrokerName = sbrokerName;
    }

    public BigDecimal getMoptionContractCommission() {
        return this.moptionContractCommission;
    }

    public void setMoptionContractCommission(BigDecimal moptionContractCommission) {
        this.moptionContractCommission = moptionContractCommission;
    }

    public BigDecimal getDmarginInterestRate() {
        return this.dmarginInterestRate;
    }

    public void setDmarginInterestRate(BigDecimal dmarginInterestRate) {
        this.dmarginInterestRate = dmarginInterestRate;
    }

    public com.pas.dbobjects.Tblcommission getTblcommission() {
        return this.tblcommission;
    }

    public void setTblcommission(com.pas.dbobjects.Tblcommission tblcommission) {
        this.tblcommission = tblcommission;
    }
   
}
