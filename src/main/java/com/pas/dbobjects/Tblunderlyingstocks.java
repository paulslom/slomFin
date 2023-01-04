package com.pas.dbobjects;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblunderlyingstocks implements Serializable {

    /** identifier field */
    private Integer iunderlyingStocksId;

    /** persistent field */
    private com.pas.dbobjects.Tblinvestment tblinvestmentByIUnderlyingInvId;

    /** persistent field */
    private com.pas.dbobjects.Tblinvestment tblinvestmentByIInvestmentId;

    /** full constructor */
    public Tblunderlyingstocks(Integer iunderlyingStocksId, com.pas.dbobjects.Tblinvestment tblinvestmentByIUnderlyingInvId, com.pas.dbobjects.Tblinvestment tblinvestmentByIInvestmentId) {
        this.iunderlyingStocksId = iunderlyingStocksId;
        this.tblinvestmentByIUnderlyingInvId = tblinvestmentByIUnderlyingInvId;
        this.tblinvestmentByIInvestmentId = tblinvestmentByIInvestmentId;
    }

    /** default constructor */
    public Tblunderlyingstocks() {
    }

    public Integer getIunderlyingStocksId() {
        return this.iunderlyingStocksId;
    }

    public void setIunderlyingStocksId(Integer iunderlyingStocksId) {
        this.iunderlyingStocksId = iunderlyingStocksId;
    }

    public com.pas.dbobjects.Tblinvestment getTblinvestmentByIUnderlyingInvId() {
        return this.tblinvestmentByIUnderlyingInvId;
    }

    public void setTblinvestmentByIUnderlyingInvId(com.pas.dbobjects.Tblinvestment tblinvestmentByIUnderlyingInvId) {
        this.tblinvestmentByIUnderlyingInvId = tblinvestmentByIUnderlyingInvId;
    }

    public com.pas.dbobjects.Tblinvestment getTblinvestmentByIInvestmentId() {
        return this.tblinvestmentByIInvestmentId;
    }

    public void setTblinvestmentByIInvestmentId(com.pas.dbobjects.Tblinvestment tblinvestmentByIInvestmentId) {
        this.tblinvestmentByIInvestmentId = tblinvestmentByIInvestmentId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("iunderlyingStocksId", getIunderlyingStocksId())
            .toString();
    }

}
