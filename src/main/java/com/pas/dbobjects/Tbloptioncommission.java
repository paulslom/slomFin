package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbloptioncommission implements Serializable {

    /** identifier field */
    private Integer ioptionCommissionId;

    /** nullable persistent field */
    private String sdescription;

    /** nullable persistent field */
    private BigDecimal mfee;

    /** nullable persistent field */
    private BigDecimal mperContractFee;

    /** persistent field */
    private com.pas.dbobjects.Tblbroker tblbroker;

    /** full constructor */
    public Tbloptioncommission(Integer ioptionCommissionId, String sdescription, BigDecimal mfee, BigDecimal mperContractFee, com.pas.dbobjects.Tblbroker tblbroker) {
        this.ioptionCommissionId = ioptionCommissionId;
        this.sdescription = sdescription;
        this.mfee = mfee;
        this.mperContractFee = mperContractFee;
        this.tblbroker = tblbroker;
    }

    /** default constructor */
    public Tbloptioncommission() {
    }

    /** minimal constructor */
    public Tbloptioncommission(Integer ioptionCommissionId, com.pas.dbobjects.Tblbroker tblbroker) {
        this.ioptionCommissionId = ioptionCommissionId;
        this.tblbroker = tblbroker;
    }

    public Integer getIoptionCommissionId() {
        return this.ioptionCommissionId;
    }

    public void setIoptionCommissionId(Integer ioptionCommissionId) {
        this.ioptionCommissionId = ioptionCommissionId;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public BigDecimal getMfee() {
        return this.mfee;
    }

    public void setMfee(BigDecimal mfee) {
        this.mfee = mfee;
    }

    public BigDecimal getMperContractFee() {
        return this.mperContractFee;
    }

    public void setMperContractFee(BigDecimal mperContractFee) {
        this.mperContractFee = mperContractFee;
    }

    public com.pas.dbobjects.Tblbroker getTblbroker() {
        return this.tblbroker;
    }

    public void setTblbroker(com.pas.dbobjects.Tblbroker tblbroker) {
        this.tblbroker = tblbroker;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("ioptionCommissionId", getIoptionCommissionId())
            .toString();
    }

}
