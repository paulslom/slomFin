package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblcommission implements Serializable {

    /** identifier field */
    private Integer icommissionId;

    /** nullable persistent field */
    private String sdescription;

    /** nullable persistent field */
    private BigDecimal mfee;

    /** persistent field */
    private com.pas.dbobjects.Tblbroker tblbroker;

    /** persistent field */
    private Set<Tblbroker> tblbrokers;

    /** full constructor */
    public Tblcommission(Integer icommissionId, String sdescription, BigDecimal mfee, com.pas.dbobjects.Tblbroker tblbroker, Set<Tblbroker> tblbrokers) {
        this.icommissionId = icommissionId;
        this.sdescription = sdescription;
        this.mfee = mfee;
        this.tblbroker = tblbroker;
        this.tblbrokers = tblbrokers;
    }

    /** default constructor */
    public Tblcommission() {
    }

    /** minimal constructor */
    public Tblcommission(Integer icommissionId, com.pas.dbobjects.Tblbroker tblbroker, Set<Tblbroker> tblbrokers) {
        this.icommissionId = icommissionId;
        this.tblbroker = tblbroker;
        this.tblbrokers = tblbrokers;
    }

    public Integer getIcommissionId() {
        return this.icommissionId;
    }

    public void setIcommissionId(Integer icommissionId) {
        this.icommissionId = icommissionId;
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

    public com.pas.dbobjects.Tblbroker getTblbroker() {
        return this.tblbroker;
    }

    public void setTblbroker(com.pas.dbobjects.Tblbroker tblbroker) {
        this.tblbroker = tblbroker;
    }

    public Set<Tblbroker> getTblbrokers() {
        return this.tblbrokers;
    }

    public void setTblbrokers(Set<Tblbroker> tblbrokers) {
        this.tblbrokers = tblbrokers;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("icommissionId", getIcommissionId())
            .toString();
    }

}
