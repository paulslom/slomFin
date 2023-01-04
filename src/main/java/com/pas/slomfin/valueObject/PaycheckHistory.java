package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		PaycheckHistory
 * Project: 	Portfolio
 * Copyright: 	Copyright (c) 2007
 */
public class PaycheckHistory implements IValueObject
{	
    private Integer paycheckHistoryID;
    private java.sql.Timestamp paycheckHistoryDate;
	private Integer jobID;
	private Integer paycheckTypeID;
	private String employer;
	private java.math.BigDecimal grossPay;
	private java.math.BigDecimal federalWithholding;
	private java.math.BigDecimal stateWithholding;
	private java.math.BigDecimal retirementDeferred;
	private java.math.BigDecimal ssWithholding;
	private java.math.BigDecimal medicareWithholding;
	private java.math.BigDecimal dental;
	private java.math.BigDecimal medical;
	private java.math.BigDecimal groupLifeInsurance;
	private java.math.BigDecimal groupLifeIncome;
	private java.math.BigDecimal vision;
	private java.math.BigDecimal parking;
	private java.math.BigDecimal cafeteria;
	private java.math.BigDecimal roth401k;
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("paycheck History ID  = " + paycheckHistoryID + "\n");
		buf.append("description  = " + employer + "\n");
				
		return buf.toString();
	}	
   
    public java.math.BigDecimal getDental()
    {
        return dental;
    }
    public void setDental(java.math.BigDecimal dental)
    {
        this.dental = dental;
    }
    public String getEmployer()
    {
        return employer;
    }
    public void setEmployer(String employer)
    {
        this.employer = employer;
    }
    public java.math.BigDecimal getFederalWithholding()
    {
        return federalWithholding;
    }
    public void setFederalWithholding(java.math.BigDecimal federalWithholding)
    {
        this.federalWithholding = federalWithholding;
    }
    public java.math.BigDecimal getGrossPay()
    {
        return grossPay;
    }
    public void setGrossPay(java.math.BigDecimal grossPay)
    {
        this.grossPay = grossPay;
    }
    public java.math.BigDecimal getGroupLifeIncome()
    {
        return groupLifeIncome;
    }
    public void setGroupLifeIncome(java.math.BigDecimal groupLifeIncome)
    {
        this.groupLifeIncome = groupLifeIncome;
    }
    public java.math.BigDecimal getGroupLifeInsurance()
    {
        return groupLifeInsurance;
    }
    public void setGroupLifeInsurance(java.math.BigDecimal groupLifeInsurance)
    {
        this.groupLifeInsurance = groupLifeInsurance;
    }
    public Integer getJobID()
    {
        return jobID;
    }
    public void setJobID(Integer jobID)
    {
        this.jobID = jobID;
    }
    public java.math.BigDecimal getMedical()
    {
        return medical;
    }
    public void setMedical(java.math.BigDecimal medical)
    {
        this.medical = medical;
    }
    public java.math.BigDecimal getMedicareWithholding()
    {
        return medicareWithholding;
    }
    public void setMedicareWithholding(java.math.BigDecimal medicareWithholding)
    {
        this.medicareWithholding = medicareWithholding;
    }
    public java.sql.Timestamp getPaycheckHistoryDate()
    {
        return paycheckHistoryDate;
    }
    public void setPaycheckHistoryDate(java.sql.Timestamp paycheckHistoryDate)
    {
        this.paycheckHistoryDate = paycheckHistoryDate;
    }
    public Integer getPaycheckHistoryID()
    {
        return paycheckHistoryID;
    }
    public void setPaycheckHistoryID(Integer paycheckHistoryID)
    {
        this.paycheckHistoryID = paycheckHistoryID;
    }
    public java.math.BigDecimal getRetirementDeferred()
    {
        return retirementDeferred;
    }
    public void setRetirementDeferred(java.math.BigDecimal retirementDeferred)
    {
        this.retirementDeferred = retirementDeferred;
    }
    public java.math.BigDecimal getSsWithholding()
    {
        return ssWithholding;
    }
    public void setSsWithholding(java.math.BigDecimal ssWithholding)
    {
        this.ssWithholding = ssWithholding;
    }
    public java.math.BigDecimal getStateWithholding()
    {
        return stateWithholding;
    }
    public void setStateWithholding(java.math.BigDecimal stateWithholding)
    {
        this.stateWithholding = stateWithholding;
    }

	public Integer getPaycheckTypeID()
	{
		return paycheckTypeID;
	}

	public void setPaycheckTypeID(Integer paycheckTypeID)
	{
		this.paycheckTypeID = paycheckTypeID;
	}

	public java.math.BigDecimal getVision() {
		return vision;
	}

	public void setVision(java.math.BigDecimal vision) {
		this.vision = vision;
	}

	public java.math.BigDecimal getParking() {
		return parking;
	}

	public void setParking(java.math.BigDecimal parking) {
		this.parking = parking;
	}

	public java.math.BigDecimal getCafeteria() {
		return cafeteria;
	}

	public void setCafeteria(java.math.BigDecimal cafeteria) {
		this.cafeteria = cafeteria;
	}

	public java.math.BigDecimal getRoth401k() {
		return roth401k;
	}

	public void setRoth401k(java.math.BigDecimal roth401k) {
		this.roth401k = roth401k;
	}
}