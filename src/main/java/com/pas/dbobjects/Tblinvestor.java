package com.pas.dbobjects;

import java.io.Serializable;
import java.util.Date;

public class Tblinvestor implements Serializable {

    private Integer iinvestorId;
    private String sfirstName;
    private String slastName;
    private String sfullName;
    private String spictureFile;
    private String spictureFileSmall;
    private Date ddateOfBirth;
    private Integer itaxGroupId;
    private Integer ihardCashAccountId;

    /** persistent field */
    private com.pas.dbobjects.Tblaccount tblaccount;
    private com.pas.dbobjects.Tbltaxgroup tbltaxgroup;   

    /** default constructor */
    public Tblinvestor() {
    }

    public Integer getIinvestorId() {
        return this.iinvestorId;
    }

    public void setIinvestorId(Integer iinvestorId) {
        this.iinvestorId = iinvestorId;
    }

    public String getSfirstName() {
        return this.sfirstName;
    }

    public void setSfirstName(String sfirstName) {
        this.sfirstName = sfirstName;
    }

    public String getSlastName() {
        return this.slastName;
    }

    public void setSlastName(String slastName) {
        this.slastName = slastName;
    }

    public String getSfullName() {
        return this.sfullName;
    }

    public void setSfullName(String sfullName) {
        this.sfullName = sfullName;
    }

    public String getSpictureFile() {
        return this.spictureFile;
    }

    public void setSpictureFile(String spictureFile) {
        this.spictureFile = spictureFile;
    }

    public String getSpictureFileSmall() {
        return this.spictureFileSmall;
    }

    public void setSpictureFileSmall(String spictureFileSmall) {
        this.spictureFileSmall = spictureFileSmall;
    }

    public Date getDdateOfBirth() {
        return this.ddateOfBirth;
    }

    public void setDdateOfBirth(Date ddateOfBirth) {
        this.ddateOfBirth = ddateOfBirth;
    }

    public com.pas.dbobjects.Tblaccount getTblaccount() {
        return this.tblaccount;
    }

    public void setTblaccount(com.pas.dbobjects.Tblaccount tblaccount) {
        this.tblaccount = tblaccount;
    }

    public com.pas.dbobjects.Tbltaxgroup getTbltaxgroup() {
        return this.tbltaxgroup;
    }

    public void setTbltaxgroup(com.pas.dbobjects.Tbltaxgroup tbltaxgroup) {
        this.tbltaxgroup = tbltaxgroup;
    }

	public Integer getItaxGroupId() {
		return itaxGroupId;
	}

	public void setItaxGroupId(Integer itaxGroupId) {
		this.itaxGroupId = itaxGroupId;
	}

	public Integer getIhardCashAccountId() {
		return ihardCashAccountId;
	}

	public void setIhardCashAccountId(Integer ihardCashAccountId) {
		this.ihardCashAccountId = ihardCashAccountId;
	}

  

}
