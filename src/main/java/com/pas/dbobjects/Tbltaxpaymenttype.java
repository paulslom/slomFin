package com.pas.dbobjects;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbltaxpaymenttype implements Serializable {

    /** identifier field */
    private Integer itaxPaymentTypeId;

    /** persistent field */
    private String staxPaymentTypeDesc;

    /** persistent field */
    private Set<Tbltaxpayment> tbltaxpayments;

    /** full constructor */
    public Tbltaxpaymenttype(Integer itaxPaymentTypeId, String staxPaymentTypeDesc, Set<Tbltaxpayment> tbltaxpayments) {
        this.itaxPaymentTypeId = itaxPaymentTypeId;
        this.staxPaymentTypeDesc = staxPaymentTypeDesc;
        this.tbltaxpayments = tbltaxpayments;
    }

    /** default constructor */
    public Tbltaxpaymenttype() {
    }

    public Integer getItaxPaymentTypeId() {
        return this.itaxPaymentTypeId;
    }

    public void setItaxPaymentTypeId(Integer itaxPaymentTypeId) {
        this.itaxPaymentTypeId = itaxPaymentTypeId;
    }

    public String getStaxPaymentTypeDesc() {
        return this.staxPaymentTypeDesc;
    }

    public void setStaxPaymentTypeDesc(String staxPaymentTypeDesc) {
        this.staxPaymentTypeDesc = staxPaymentTypeDesc;
    }

    public Set<Tbltaxpayment> getTbltaxpayments() {
        return this.tbltaxpayments;
    }

    public void setTbltaxpayments(Set<Tbltaxpayment> tbltaxpayments) {
        this.tbltaxpayments = tbltaxpayments;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("itaxPaymentTypeId", getItaxPaymentTypeId())
            .toString();
    }

}
