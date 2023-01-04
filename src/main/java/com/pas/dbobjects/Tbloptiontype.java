package com.pas.dbobjects;

import java.io.Serializable;

public class Tbloptiontype implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer ioptionTypeId;
    private String sdescription;

    /** default constructor */
    public Tbloptiontype() {
    }

    public Integer getIoptionTypeId() {
        return this.ioptionTypeId;
    }

    public void setIoptionTypeId(Integer ioptionTypeId) {
        this.ioptionTypeId = ioptionTypeId;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

   
}
