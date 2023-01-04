package com.pas.dbobjects;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblinvtypetrxtype implements Serializable {

    /** identifier field */
    private Integer iinvTypeTrxTypeId;

    /** persistent field */
    private com.pas.dbobjects.Tbltransactiontype tbltransactiontype;

    /** persistent field */
    private com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype;

    /** full constructor */
    public Tblinvtypetrxtype(Integer iinvTypeTrxTypeId, com.pas.dbobjects.Tbltransactiontype tbltransactiontype, com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype) {
        this.iinvTypeTrxTypeId = iinvTypeTrxTypeId;
        this.tbltransactiontype = tbltransactiontype;
        this.tblinvestmenttype = tblinvestmenttype;
    }

    /** default constructor */
    public Tblinvtypetrxtype() {
    }

    public Integer getIinvTypeTrxTypeId() {
        return this.iinvTypeTrxTypeId;
    }

    public void setIinvTypeTrxTypeId(Integer iinvTypeTrxTypeId) {
        this.iinvTypeTrxTypeId = iinvTypeTrxTypeId;
    }

    public com.pas.dbobjects.Tbltransactiontype getTbltransactiontype() {
        return this.tbltransactiontype;
    }

    public void setTbltransactiontype(com.pas.dbobjects.Tbltransactiontype tbltransactiontype) {
        this.tbltransactiontype = tbltransactiontype;
    }

    public com.pas.dbobjects.Tblinvestmenttype getTblinvestmenttype() {
        return this.tblinvestmenttype;
    }

    public void setTblinvestmenttype(com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype) {
        this.tblinvestmenttype = tblinvestmenttype;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("iinvTypeTrxTypeId", getIinvTypeTrxTypeId())
            .toString();
    }

}
