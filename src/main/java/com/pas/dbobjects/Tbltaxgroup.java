package com.pas.dbobjects;

import java.io.Serializable;

public class Tbltaxgroup implements Serializable 
{

    /** identifier field */
    private Integer itaxGroupId;

    /** persistent field */
    private String staxGroupName;

    /** default constructor */
    public Tbltaxgroup() {
    }

    public Integer getItaxGroupId() {
        return this.itaxGroupId;
    }

    public void setItaxGroupId(Integer itaxGroupId) {
        this.itaxGroupId = itaxGroupId;
    }

    public String getStaxGroupName() {
        return this.staxGroupName;
    }

    public void setStaxGroupName(String staxGroupName) {
        this.staxGroupName = staxGroupName;
    }
   
}
