package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbltaxpayment implements Serializable {

    /** identifier field */
    private Integer itaxPaymentId;

    /** persistent field */
    private int itaxYear;

    /** nullable persistent field */
    private String staxPaymentDesc;

    /** nullable persistent field */
    private BigDecimal mtaxPaymentAmount;

    /** nullable persistent field */
    private Date dtaxPaymentDate;

    /** persistent field */
    private com.pas.dbobjects.Tbltaxpaymenttype tbltaxpaymenttype;

    /** persistent field */
    private com.pas.dbobjects.Tbltaxgroup tbltaxgroup;

    /** full constructor */
    public Tbltaxpayment(Integer itaxPaymentId, int itaxYear, String staxPaymentDesc, BigDecimal mtaxPaymentAmount, Date dtaxPaymentDate, com.pas.dbobjects.Tbltaxpaymenttype tbltaxpaymenttype, com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.itaxPaymentId = itaxPaymentId;
        this.itaxYear = itaxYear;
        this.staxPaymentDesc = staxPaymentDesc;
        this.mtaxPaymentAmount = mtaxPaymentAmount;
        this.dtaxPaymentDate = dtaxPaymentDate;
        this.tbltaxpaymenttype = tbltaxpaymenttype;
        this.tbltaxgroup = tbltaxgroup;
    }

    /** default constructor */
    public Tbltaxpayment() {
    }

    /** minimal constructor */
    public Tbltaxpayment(Integer itaxPaymentId, int itaxYear, com.pas.dbobjects.Tbltaxpaymenttype tbltaxpaymenttype, com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.itaxPaymentId = itaxPaymentId;
        this.itaxYear = itaxYear;
        this.tbltaxpaymenttype = tbltaxpaymenttype;
        this.tbltaxgroup = tbltaxgroup;
    }

    public Integer getItaxPaymentId() {
        return this.itaxPaymentId;
    }

    public void setItaxPaymentId(Integer itaxPaymentId) {
        this.itaxPaymentId = itaxPaymentId;
    }

    public int getItaxYear() {
        return this.itaxYear;
    }

    public void setItaxYear(int itaxYear) {
        this.itaxYear = itaxYear;
    }

    public String getStaxPaymentDesc() {
        return this.staxPaymentDesc;
    }

    public void setStaxPaymentDesc(String staxPaymentDesc) {
        this.staxPaymentDesc = staxPaymentDesc;
    }

    public BigDecimal getMtaxPaymentAmount() {
        return this.mtaxPaymentAmount;
    }

    public void setMtaxPaymentAmount(BigDecimal mtaxPaymentAmount) {
        this.mtaxPaymentAmount = mtaxPaymentAmount;
    }

    public Date getDtaxPaymentDate() {
        return this.dtaxPaymentDate;
    }

    public void setDtaxPaymentDate(Date dtaxPaymentDate) {
        this.dtaxPaymentDate = dtaxPaymentDate;
    }

    public com.pas.dbobjects.Tbltaxpaymenttype getTbltaxpaymenttype() {
        return this.tbltaxpaymenttype;
    }

    public void setTbltaxpaymenttype(com.pas.dbobjects.Tbltaxpaymenttype tbltaxpaymenttype) {
        this.tbltaxpaymenttype = tbltaxpaymenttype;
    }

    public com.pas.dbobjects.Tbltaxgroup getTbltaxgroup() {
        return this.tbltaxgroup;
    }

    public void setTbltaxgroup(com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.tbltaxgroup = tbltaxgroup;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("itaxPaymentId", getItaxPaymentId())
            .toString();
    }

}
