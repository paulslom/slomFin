package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Tblassetclass implements Serializable 
{
	private static final long serialVersionUID = 1L;
    private Integer iassetClassId;
    private String sassetClass;
    private BigDecimal dhistoricalReturn;

    /** default constructor */
    public Tblassetclass() {
    }

    public Integer getIassetClassId() {
        return this.iassetClassId;
    }

    public void setIassetClassId(Integer iassetClassId) {
        this.iassetClassId = iassetClassId;
    }

    public String getSassetClass() {
        return this.sassetClass;
    }

    public void setSassetClass(String sassetClass) {
        this.sassetClass = sassetClass;
    }

    public BigDecimal getDhistoricalReturn() {
        return this.dhistoricalReturn;
    }

    public void setDhistoricalReturn(BigDecimal dhistoricalReturn) {
        this.dhistoricalReturn = dhistoricalReturn;
    }

}
