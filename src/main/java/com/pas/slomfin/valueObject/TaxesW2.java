package com.pas.slomfin.valueObject;

import java.math.BigDecimal;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class TaxesW2 implements IValueObject
{
   private String employer; 
   private BigDecimal box1TaxableWgs;
   private BigDecimal box2FedWH;
   private BigDecimal box3SSWages;
   private BigDecimal box4SocSecWH;
   private BigDecimal box5TotalWgs;
   private BigDecimal box6MedicareWH;
   private BigDecimal box12cLifeIns;
   private BigDecimal box12dRetDeferred;
   private BigDecimal box16StateWages;
   private BigDecimal box17StateWH;
   private String employerFedIDNo;
   private String employerStateIDNo;
			
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("employer  = " + employer + "\n");
		buf.append("box1 = " + box1TaxableWgs + "\n");		
				
		return buf.toString();
	}

	public String getEmployer()
	{
		return employer;
	}

	public void setEmployer(String employer)
	{
		this.employer = employer;
	}

	public BigDecimal getBox1TaxableWgs()
	{
		return box1TaxableWgs;
	}

	public void setBox1TaxableWgs(BigDecimal box1TaxableWgs)
	{
		this.box1TaxableWgs = box1TaxableWgs;
	}

	public BigDecimal getBox2FedWH()
	{
		return box2FedWH;
	}

	public void setBox2FedWH(BigDecimal box2FedWH)
	{
		this.box2FedWH = box2FedWH;
	}

	public BigDecimal getBox3SSWages()
	{
		return box3SSWages;
	}

	public void setBox3SSWages(BigDecimal box3SSWages)
	{
		this.box3SSWages = box3SSWages;
	}

	public BigDecimal getBox4SocSecWH()
	{
		return box4SocSecWH;
	}

	public void setBox4SocSecWH(BigDecimal box4SocSecWH)
	{
		this.box4SocSecWH = box4SocSecWH;
	}

	public BigDecimal getBox5TotalWgs()
	{
		return box5TotalWgs;
	}

	public void setBox5TotalWgs(BigDecimal box5TotalWgs)
	{
		this.box5TotalWgs = box5TotalWgs;
	}

	public BigDecimal getBox6MedicareWH()
	{
		return box6MedicareWH;
	}

	public void setBox6MedicareWH(BigDecimal box6MedicareWH)
	{
		this.box6MedicareWH = box6MedicareWH;
	}

	public BigDecimal getBox12cLifeIns()
	{
		return box12cLifeIns;
	}

	public void setBox12cLifeIns(BigDecimal box12cLifeIns)
	{
		this.box12cLifeIns = box12cLifeIns;
	}

	public BigDecimal getBox12dRetDeferred()
	{
		return box12dRetDeferred;
	}

	public void setBox12dRetDeferred(BigDecimal box12dRetDeferred)
	{
		this.box12dRetDeferred = box12dRetDeferred;
	}

	public BigDecimal getBox16StateWages()
	{
		return box16StateWages;
	}

	public void setBox16StateWages(BigDecimal box16StateWages)
	{
		this.box16StateWages = box16StateWages;
	}

	public BigDecimal getBox17StateWH()
	{
		return box17StateWH;
	}

	public void setBox17StateWH(BigDecimal box17StateWH)
	{
		this.box17StateWH = box17StateWH;
	}

	public String getEmployerFedIDNo()
	{
		return employerFedIDNo;
	}

	public void setEmployerFedIDNo(String employerFedIDNo)
	{
		this.employerFedIDNo = employerFedIDNo;
	}

	public String getEmployerStateIDNo()
	{
		return employerStateIDNo;
	}

	public void setEmployerStateIDNo(String employerStateIDNo)
	{
		this.employerStateIDNo = employerStateIDNo;
	}

	}