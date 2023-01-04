package com.pas.slomfin.valueObject;

import com.pas.valueObject.IValueObject;

/**
 * Title: 		IVRMenuOptionCodeDetail
 * Project: 	Table Maintenance Application
 * Copyright: 	Copyright (c) 2006
 * Company: 	Lincoln Life
 */
public class Investor implements IValueObject
{
	
	private String investorID;
	private String firstName;
	private String lastName;
	private String fullName;
	private String pictureFile;
	private String pictureFileSmall;
	private String taxGroupID;
	private String taxGroupName;
	private String dateOfBirth;
	private String hardCashAccountID;	
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("Investor ID  = " + investorID + "\n");
		buf.append("First Name = " + firstName + "\n");
		buf.append("Last Name = " + lastName + "\n");
		buf.append("Full Name = " + fullName + "\n");
		buf.append("Picture File = " + pictureFile + "\n");
		buf.append("Picture File small = " + pictureFileSmall + "\n");
		buf.append("Tax Group ID = " + taxGroupID + "\n");
		buf.append("Date of Birth = " + dateOfBirth + "\n");
		buf.append("Hard Cash account id = " + hardCashAccountID + "\n");
		
		return buf.toString();
	}

	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getHardCashAccountID()
	{
		return hardCashAccountID;
	}

	public void setHardCashAccountID(String hardCashAccountID)
	{
		this.hardCashAccountID = hardCashAccountID;
	}

	public String getInvestorID()
	{
		return investorID;
	}

	public void setInvestorID(String investorID)
	{
		this.investorID = investorID;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getPictureFile()
	{
		return pictureFile;
	}

	public void setPictureFile(String pictureFile)
	{
		this.pictureFile = pictureFile;
	}

	public String getPictureFileSmall()
	{
		return pictureFileSmall;
	}

	public void setPictureFileSmall(String pictureFileSmall)
	{
		this.pictureFileSmall = pictureFileSmall;
	}

	public String getTaxGroupID()
	{
		return taxGroupID;
	}

	public void setTaxGroupID(String taxGroupID)
	{
		this.taxGroupID = taxGroupID;
	}

	public String getTaxGroupName()
	{
		return taxGroupName;
	}

	public void setTaxGroupName(String taxGroupName)
	{
		this.taxGroupName = taxGroupName;
	}

	
}