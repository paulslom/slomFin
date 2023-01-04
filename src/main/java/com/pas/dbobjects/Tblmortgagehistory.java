package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblmortgagehistory implements Serializable {

    /** identifier field */
    private Integer imortgageHistoryId;

    /** nullable persistent field */
    private Date dpaymentDate;

    /** nullable persistent field */
    private BigDecimal mprincipalPaid;

    /** nullable persistent field */
    private BigDecimal minterestPaid;

    /** nullable persistent field */
    private BigDecimal mpropertyTaxesPaid;

    /** nullable persistent field */
    private BigDecimal mpmipaid;

    /** nullable persistent field */
    private BigDecimal mhomeownersInsPaid;

    /** persistent field */
    private com.pas.dbobjects.Tblmortgage tblmortgage;

    /** not a persistent field here - just need this for reports. **/
    private Integer mortgagePaymentYear;    
   
	/** full constructor */
    public Tblmortgagehistory(Integer imortgageHistoryId, Date dpaymentDate, BigDecimal mprincipalPaid, BigDecimal minterestPaid, BigDecimal mpropertyTaxesPaid, BigDecimal mpmipaid, BigDecimal mhomeownersInsPaid, com.pas.dbobjects.Tblmortgage tblmortgage) {
        this.imortgageHistoryId = imortgageHistoryId;
        this.dpaymentDate = dpaymentDate;
        this.mprincipalPaid = mprincipalPaid;
        this.minterestPaid = minterestPaid;
        this.mpropertyTaxesPaid = mpropertyTaxesPaid;
        this.mpmipaid = mpmipaid;
        this.mhomeownersInsPaid = mhomeownersInsPaid;
        this.tblmortgage = tblmortgage;
    }

    /** default constructor */
    public Tblmortgagehistory() {
    }

    /** minimal constructor */
    public Tblmortgagehistory(Integer imortgageHistoryId, com.pas.dbobjects.Tblmortgage tblmortgage) {
        this.imortgageHistoryId = imortgageHistoryId;
        this.tblmortgage = tblmortgage;
    }

    public Integer getImortgageHistoryId() {
        return this.imortgageHistoryId;
    }

    public void setImortgageHistoryId(Integer imortgageHistoryId) {
        this.imortgageHistoryId = imortgageHistoryId;
    }

    public Date getDpaymentDate() {
        return this.dpaymentDate;
    }

    public void setDpaymentDate(Date dpaymentDate) {
        this.dpaymentDate = dpaymentDate;
        setMortgagePaymentYear(dpaymentDate);
    }

    public Integer getMortgagePaymentYear()
    {
		return mortgagePaymentYear;
	}

	public void setMortgagePaymentYear(Date dpaymentDate)
	{
		Calendar calendar = Calendar.getInstance();	    

		calendar.setTime(dpaymentDate); 
		int year = calendar.get(Calendar.YEAR);
		
		this.mortgagePaymentYear = year;
	}

    public BigDecimal getMprincipalPaid() {
        return this.mprincipalPaid;
    }

    public void setMprincipalPaid(BigDecimal mprincipalPaid) {
        this.mprincipalPaid = mprincipalPaid;
    }

    public BigDecimal getMinterestPaid() {
        return this.minterestPaid;
    }

    public void setMinterestPaid(BigDecimal minterestPaid) {
        this.minterestPaid = minterestPaid;
    }

    public BigDecimal getMpropertyTaxesPaid() {
        return this.mpropertyTaxesPaid;
    }

    public void setMpropertyTaxesPaid(BigDecimal mpropertyTaxesPaid) {
        this.mpropertyTaxesPaid = mpropertyTaxesPaid;
    }

    public BigDecimal getMpmipaid() {
        return this.mpmipaid;
    }

    public void setMpmipaid(BigDecimal mpmipaid) {
        this.mpmipaid = mpmipaid;
    }

    public BigDecimal getMhomeownersInsPaid() {
        return this.mhomeownersInsPaid;
    }

    public void setMhomeownersInsPaid(BigDecimal mhomeownersInsPaid) {
        this.mhomeownersInsPaid = mhomeownersInsPaid;
    }

    public com.pas.dbobjects.Tblmortgage getTblmortgage() {
        return this.tblmortgage;
    }

    public void setTblmortgage(com.pas.dbobjects.Tblmortgage tblmortgage) {
        this.tblmortgage = tblmortgage;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("imortgageHistoryId", getImortgageHistoryId())
            .toString();
    }

}
