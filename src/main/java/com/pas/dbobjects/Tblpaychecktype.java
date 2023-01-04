package com.pas.dbobjects;

import java.io.Serializable;

public class Tblpaychecktype implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Integer ipaycheckTypeId;
    private String spaycheckType;

    /** default constructor */
    public Tblpaychecktype() {
    }

    public Integer getIpaycheckTypeId() {
        return this.ipaycheckTypeId;
    }

    public void setIpaycheckTypeId(Integer ipaycheckTypeId) {
        this.ipaycheckTypeId = ipaycheckTypeId;
    }

    public String getSpaycheckType() {
        return this.spaycheckType;
    }

    public void setSpaycheckType(String spaycheckType) {
        this.spaycheckType = spaycheckType;
    }

   
}
