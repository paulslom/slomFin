package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbltaxgroupyear implements Serializable {

    /** identifier field */
    private Integer itaxGroupYearId;

    /** persistent field */
    private int itaxYear;

    /** nullable persistent field */
    private String sfilingStatus;

    /** nullable persistent field */
    private Integer idependents;

    /** nullable persistent field */
    private BigDecimal mcapitalLossCarryover;

    /** nullable persistent field */
    private BigDecimal miradistribution;

    /** nullable persistent field */
    private BigDecimal mprevYearStateRefund;

    /** nullable persistent field */
    private BigDecimal motherItemized;

    /** nullable persistent field */
    private String sotherItemizedDesc;

    /** nullable persistent field */
    private BigDecimal mcarTax;

    /** nullable persistent field */
    private BigDecimal ddividendTaxRate;

    /** nullable persistent field */
    private BigDecimal motherIncome;

    /** nullable persistent field */
    private String sotherIncomeDesc;

    /** nullable persistent field */
    private BigDecimal mdayCareExpensesPaid;

    /** nullable persistent field */
    private BigDecimal mqualifiedDividends;

    /** nullable persistent field */
    private BigDecimal mdividendsForeignTax;

    /** nullable persistent field */
    private BigDecimal mdividendsReturnOfCapital;

    /** persistent field */
    private com.pas.dbobjects.Tbltaxgroup tbltaxgroup;

    /** full constructor */
    public Tbltaxgroupyear(Integer itaxGroupYearId, int itaxYear, String sfilingStatus, Integer idependents, BigDecimal mcapitalLossCarryover, BigDecimal miradistribution, BigDecimal mprevYearStateRefund, BigDecimal motherItemized, String sotherItemizedDesc, BigDecimal mcarTax, BigDecimal ddividendTaxRate, BigDecimal motherIncome, String sotherIncomeDesc, BigDecimal mdayCareExpensesPaid, BigDecimal mqualifiedDividends, BigDecimal mdividendsForeignTax, BigDecimal mdividendsReturnOfCapital, com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.itaxGroupYearId = itaxGroupYearId;
        this.itaxYear = itaxYear;
        this.sfilingStatus = sfilingStatus;
        this.idependents = idependents;
        this.mcapitalLossCarryover = mcapitalLossCarryover;
        this.miradistribution = miradistribution;
        this.mprevYearStateRefund = mprevYearStateRefund;
        this.motherItemized = motherItemized;
        this.sotherItemizedDesc = sotherItemizedDesc;
        this.mcarTax = mcarTax;
        this.ddividendTaxRate = ddividendTaxRate;
        this.motherIncome = motherIncome;
        this.sotherIncomeDesc = sotherIncomeDesc;
        this.mdayCareExpensesPaid = mdayCareExpensesPaid;
        this.mqualifiedDividends = mqualifiedDividends;
        this.mdividendsForeignTax = mdividendsForeignTax;
        this.mdividendsReturnOfCapital = mdividendsReturnOfCapital;
        this.tbltaxgroup = tbltaxgroup;
    }

    /** default constructor */
    public Tbltaxgroupyear() {
    }

    /** minimal constructor */
    public Tbltaxgroupyear(Integer itaxGroupYearId, int itaxYear, com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.itaxGroupYearId = itaxGroupYearId;
        this.itaxYear = itaxYear;
        this.tbltaxgroup = tbltaxgroup;
    }

    public Integer getItaxGroupYearId() {
        return this.itaxGroupYearId;
    }

    public void setItaxGroupYearId(Integer itaxGroupYearId) {
        this.itaxGroupYearId = itaxGroupYearId;
    }

    public int getItaxYear() {
        return this.itaxYear;
    }

    public void setItaxYear(int itaxYear) {
        this.itaxYear = itaxYear;
    }

    public String getSfilingStatus() {
        return this.sfilingStatus;
    }

    public void setSfilingStatus(String sfilingStatus) {
        this.sfilingStatus = sfilingStatus;
    }

    public Integer getIdependents() {
        return this.idependents;
    }

    public void setIdependents(Integer idependents) {
        this.idependents = idependents;
    }

    public BigDecimal getMcapitalLossCarryover() {
        return this.mcapitalLossCarryover;
    }

    public void setMcapitalLossCarryover(BigDecimal mcapitalLossCarryover) {
        this.mcapitalLossCarryover = mcapitalLossCarryover;
    }

    public BigDecimal getMiradistribution() {
        return this.miradistribution;
    }

    public void setMiradistribution(BigDecimal miradistribution) {
        this.miradistribution = miradistribution;
    }

    public BigDecimal getMprevYearStateRefund() {
        return this.mprevYearStateRefund;
    }

    public void setMprevYearStateRefund(BigDecimal mprevYearStateRefund) {
        this.mprevYearStateRefund = mprevYearStateRefund;
    }

    public BigDecimal getMotherItemized() {
        return this.motherItemized;
    }

    public void setMotherItemized(BigDecimal motherItemized) {
        this.motherItemized = motherItemized;
    }

    public String getSotherItemizedDesc() {
        return this.sotherItemizedDesc;
    }

    public void setSotherItemizedDesc(String sotherItemizedDesc) {
        this.sotherItemizedDesc = sotherItemizedDesc;
    }

    public BigDecimal getMcarTax() {
        return this.mcarTax;
    }

    public void setMcarTax(BigDecimal mcarTax) {
        this.mcarTax = mcarTax;
    }

    public BigDecimal getDdividendTaxRate() {
        return this.ddividendTaxRate;
    }

    public void setDdividendTaxRate(BigDecimal ddividendTaxRate) {
        this.ddividendTaxRate = ddividendTaxRate;
    }

    public BigDecimal getMotherIncome() {
        return this.motherIncome;
    }

    public void setMotherIncome(BigDecimal motherIncome) {
        this.motherIncome = motherIncome;
    }

    public String getSotherIncomeDesc() {
        return this.sotherIncomeDesc;
    }

    public void setSotherIncomeDesc(String sotherIncomeDesc) {
        this.sotherIncomeDesc = sotherIncomeDesc;
    }

    public BigDecimal getMdayCareExpensesPaid() {
        return this.mdayCareExpensesPaid;
    }

    public void setMdayCareExpensesPaid(BigDecimal mdayCareExpensesPaid) {
        this.mdayCareExpensesPaid = mdayCareExpensesPaid;
    }

    public BigDecimal getMqualifiedDividends() {
        return this.mqualifiedDividends;
    }

    public void setMqualifiedDividends(BigDecimal mqualifiedDividends) {
        this.mqualifiedDividends = mqualifiedDividends;
    }

    public BigDecimal getMdividendsForeignTax() {
        return this.mdividendsForeignTax;
    }

    public void setMdividendsForeignTax(BigDecimal mdividendsForeignTax) {
        this.mdividendsForeignTax = mdividendsForeignTax;
    }

    public BigDecimal getMdividendsReturnOfCapital() {
        return this.mdividendsReturnOfCapital;
    }

    public void setMdividendsReturnOfCapital(BigDecimal mdividendsReturnOfCapital) {
        this.mdividendsReturnOfCapital = mdividendsReturnOfCapital;
    }

    public com.pas.dbobjects.Tbltaxgroup getTbltaxgroup() {
        return this.tbltaxgroup;
    }

    public void setTbltaxgroup(com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.tbltaxgroup = tbltaxgroup;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("itaxGroupYearId", getItaxGroupYearId())
            .toString();
    }

}
