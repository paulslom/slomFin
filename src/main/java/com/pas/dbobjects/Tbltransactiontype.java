package com.pas.dbobjects;

import java.io.Serializable;

public class Tbltransactiontype implements Serializable 
{
    private Integer itransactionTypeId;
    private boolean bpositiveInd;
    private String sdescription;
    private String sdescriptionAbbr;

    /** default constructor */
    public Tbltransactiontype() {
    }

    public Integer getItransactionTypeId() {
        return this.itransactionTypeId;
    }

    public void setItransactionTypeId(Integer itransactionTypeId) {
        this.itransactionTypeId = itransactionTypeId;
    }

    public boolean isBpositiveInd() {
        return this.bpositiveInd;
    }

    public void setBpositiveInd(boolean bpositiveInd) {
        this.bpositiveInd = bpositiveInd;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public String getSdescriptionAbbr() {
        return this.sdescriptionAbbr;
    }

    public void setSdescriptionAbbr(String sdescriptionAbbr) {
        this.sdescriptionAbbr = sdescriptionAbbr;
    }

   
}
