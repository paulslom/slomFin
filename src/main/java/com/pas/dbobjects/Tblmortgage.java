package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblmortgage implements Serializable {

    /** identifier field */
    private Integer imortgageId;

    /** persistent field */
    private String sdescription;

    /** persistent field */
    private Date dmortgageStartDate;

    /** nullable persistent field */
    private Date dmortgageEndDate;

    /** persistent field */
    private BigDecimal moriginalLoanAmount;

    /** persistent field */
    private BigDecimal dinterestRate;

    /** persistent field */
    private int itermInYears;

    /** persistent field */
    private BigDecimal mhomeOwnersIns;

    /** persistent field */
    private BigDecimal mpmi;

    /** persistent field */
    private BigDecimal mpropertyTaxes;

    /** nullable persistent field */
    private BigDecimal mextraPrincipal;

    /** persistent field */
    private com.pas.dbobjects.Tblinvestor tblinvestor;

    /** persistent field */
    private com.pas.dbobjects.Tblaccount tblaccountByIPrincipalAccountId;

    /** persistent field */
    private com.pas.dbobjects.Tblaccount tblaccountByIPaymentAccountId;

    /** persistent field */
    private Set<Tblmortgagehistory> tblmortgagehistories;

    /** full constructor */
    public Tblmortgage(Integer imortgageId, String sdescription, Date dmortgageStartDate, Date dmortgageEndDate, BigDecimal moriginalLoanAmount, BigDecimal dinterestRate, int itermInYears, BigDecimal mhomeOwnersIns, BigDecimal mpmi, BigDecimal mpropertyTaxes, BigDecimal mextraPrincipal, com.pas.dbobjects.Tblinvestor tblinvestor, com.pas.dbobjects.Tblaccount tblaccountByIPrincipalAccountId, com.pas.dbobjects.Tblaccount tblaccountByIPaymentAccountId, Set<Tblmortgagehistory> tblmortgagehistories) {
        this.imortgageId = imortgageId;
        this.sdescription = sdescription;
        this.dmortgageStartDate = dmortgageStartDate;
        this.dmortgageEndDate = dmortgageEndDate;
        this.moriginalLoanAmount = moriginalLoanAmount;
        this.dinterestRate = dinterestRate;
        this.itermInYears = itermInYears;
        this.mhomeOwnersIns = mhomeOwnersIns;
        this.mpmi = mpmi;
        this.mpropertyTaxes = mpropertyTaxes;
        this.mextraPrincipal = mextraPrincipal;
        this.tblinvestor = tblinvestor;
        this.tblaccountByIPrincipalAccountId = tblaccountByIPrincipalAccountId;
        this.tblaccountByIPaymentAccountId = tblaccountByIPaymentAccountId;
        this.tblmortgagehistories = tblmortgagehistories;
    }

    /** default constructor */
    public Tblmortgage() {
    }

    /** minimal constructor */
    public Tblmortgage(Integer imortgageId, String sdescription, Date dmortgageStartDate, BigDecimal moriginalLoanAmount, BigDecimal dinterestRate, int itermInYears, BigDecimal mhomeOwnersIns, BigDecimal mpmi, BigDecimal mpropertyTaxes, com.pas.dbobjects.Tblinvestor tblinvestor, com.pas.dbobjects.Tblaccount tblaccountByIPrincipalAccountId, com.pas.dbobjects.Tblaccount tblaccountByIPaymentAccountId, Set<Tblmortgagehistory> tblmortgagehistories) {
        this.imortgageId = imortgageId;
        this.sdescription = sdescription;
        this.dmortgageStartDate = dmortgageStartDate;
        this.moriginalLoanAmount = moriginalLoanAmount;
        this.dinterestRate = dinterestRate;
        this.itermInYears = itermInYears;
        this.mhomeOwnersIns = mhomeOwnersIns;
        this.mpmi = mpmi;
        this.mpropertyTaxes = mpropertyTaxes;
        this.tblinvestor = tblinvestor;
        this.tblaccountByIPrincipalAccountId = tblaccountByIPrincipalAccountId;
        this.tblaccountByIPaymentAccountId = tblaccountByIPaymentAccountId;
        this.tblmortgagehistories = tblmortgagehistories;
    }

    public Integer getImortgageId() {
        return this.imortgageId;
    }

    public void setImortgageId(Integer imortgageId) {
        this.imortgageId = imortgageId;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public Date getDmortgageStartDate() {
        return this.dmortgageStartDate;
    }

    public void setDmortgageStartDate(Date dmortgageStartDate) {
        this.dmortgageStartDate = dmortgageStartDate;
    }

    public Date getDmortgageEndDate() {
        return this.dmortgageEndDate;
    }

    public void setDmortgageEndDate(Date dmortgageEndDate) {
        this.dmortgageEndDate = dmortgageEndDate;
    }

    public BigDecimal getMoriginalLoanAmount() {
        return this.moriginalLoanAmount;
    }

    public void setMoriginalLoanAmount(BigDecimal moriginalLoanAmount) {
        this.moriginalLoanAmount = moriginalLoanAmount;
    }

    public BigDecimal getDinterestRate() {
        return this.dinterestRate;
    }

    public void setDinterestRate(BigDecimal dinterestRate) {
        this.dinterestRate = dinterestRate;
    }

    public int getItermInYears() {
        return this.itermInYears;
    }

    public void setItermInYears(int itermInYears) {
        this.itermInYears = itermInYears;
    }

    public BigDecimal getMhomeOwnersIns() {
        return this.mhomeOwnersIns;
    }

    public void setMhomeOwnersIns(BigDecimal mhomeOwnersIns) {
        this.mhomeOwnersIns = mhomeOwnersIns;
    }

    public BigDecimal getMpmi() {
        return this.mpmi;
    }

    public void setMpmi(BigDecimal mpmi) {
        this.mpmi = mpmi;
    }

    public BigDecimal getMpropertyTaxes() {
        return this.mpropertyTaxes;
    }

    public void setMpropertyTaxes(BigDecimal mpropertyTaxes) {
        this.mpropertyTaxes = mpropertyTaxes;
    }

    public BigDecimal getMextraPrincipal() {
        return this.mextraPrincipal;
    }

    public void setMextraPrincipal(BigDecimal mextraPrincipal) {
        this.mextraPrincipal = mextraPrincipal;
    }

    public com.pas.dbobjects.Tblinvestor getTblinvestor() {
        return this.tblinvestor;
    }

    public void setTblinvestor(com.pas.dbobjects.Tblinvestor tblinvestor) {
        this.tblinvestor = tblinvestor;
    }

    public com.pas.dbobjects.Tblaccount getTblaccountByIPrincipalAccountId() {
        return this.tblaccountByIPrincipalAccountId;
    }

    public void setTblaccountByIPrincipalAccountId(com.pas.dbobjects.Tblaccount tblaccountByIPrincipalAccountId) {
        this.tblaccountByIPrincipalAccountId = tblaccountByIPrincipalAccountId;
    }

    public com.pas.dbobjects.Tblaccount getTblaccountByIPaymentAccountId() {
        return this.tblaccountByIPaymentAccountId;
    }

    public void setTblaccountByIPaymentAccountId(com.pas.dbobjects.Tblaccount tblaccountByIPaymentAccountId) {
        this.tblaccountByIPaymentAccountId = tblaccountByIPaymentAccountId;
    }

    public Set<Tblmortgagehistory> getTblmortgagehistories() {
        return this.tblmortgagehistories;
    }

    public void setTblmortgagehistories(Set<Tblmortgagehistory> tblmortgagehistories) {
        this.tblmortgagehistories = tblmortgagehistories;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("imortgageId", getImortgageId())
            .toString();
    }

}
