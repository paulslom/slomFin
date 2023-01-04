package com.pas.dbobjects;

import java.io.Serializable;

public class Tblinvestmenttype implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
	private Integer iinvestmentTypeId;
    private String sdescription;

    /** default constructor */
    public Tblinvestmenttype() {
    }

    public Integer getIinvestmentTypeId() {
        return this.iinvestmentTypeId;
    }

    public void setIinvestmentTypeId(Integer iinvestmentTypeId) {
        this.iinvestmentTypeId = iinvestmentTypeId;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

}
