package com.pas.dbobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Tblinvestment implements Serializable 
{
    private Integer iinvestmentId;
    private Integer iinvestmentTypeId;
    private Integer iassetClassId;
    private String stickerSymbol;
    private String sdescription;
    private BigDecimal doptionMultiplier;
    private BigDecimal mcurrentPrice;
    private Integer idividendsPerYear;
    private BigDecimal ddividendRate;
    private Date dquoteDate;
    private String sstockLogo;
    private String sstockChart;
    private String currentPriceAsString;
    
    private com.pas.dbobjects.Tblassetclass tblassetclass;
    private com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype;

    /** default constructor */
    public Tblinvestment() {
    }

    public Integer getIinvestmentId() {
        return this.iinvestmentId;
    }

    public void setIinvestmentId(Integer iinvestmentId) {
        this.iinvestmentId = iinvestmentId;
    }

    public String getStickerSymbol() {
        return this.stickerSymbol;
    }

    public void setStickerSymbol(String stickerSymbol) {
        this.stickerSymbol = stickerSymbol;
    }

    public String getSdescription() {
        return this.sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public BigDecimal getDoptionMultiplier() {
        return this.doptionMultiplier;
    }

    public void setDoptionMultiplier(BigDecimal doptionMultiplier) {
        this.doptionMultiplier = doptionMultiplier;
    }

    public BigDecimal getMcurrentPrice() {
        return this.mcurrentPrice;
    }

    public void setMcurrentPrice(BigDecimal mcurrentPrice)
    {
        this.mcurrentPrice = mcurrentPrice;
        if (mcurrentPrice == null)
        {
        	this.setCurrentPriceAsString("");
        }
        else
        {
        	this.setCurrentPriceAsString(mcurrentPrice.toString());
        }
    }

    public Integer getIdividendsPerYear() {
        return this.idividendsPerYear;
    }

    public void setIdividendsPerYear(Integer idividendsPerYear) {
        this.idividendsPerYear = idividendsPerYear;
    }

    public BigDecimal getDdividendRate() {
        return this.ddividendRate;
    }

    public void setDdividendRate(BigDecimal ddividendRate) {
        this.ddividendRate = ddividendRate;
    }

    public Date getDquoteDate() {
        return this.dquoteDate;
    }

    public void setDquoteDate(Date dquoteDate) {
        this.dquoteDate = dquoteDate;
    }

    public String getSstockLogo() {
        return this.sstockLogo;
    }

    public void setSstockLogo(String sstockLogo) {
        this.sstockLogo = sstockLogo;
    }

    public String getSstockChart() {
        return this.sstockChart;
    }

    public void setSstockChart(String sstockChart) {
        this.sstockChart = sstockChart;
    }

    public com.pas.dbobjects.Tblassetclass getTblassetclass() {
        return this.tblassetclass;
    }

    public void setTblassetclass(com.pas.dbobjects.Tblassetclass tblassetclass) {
        this.tblassetclass = tblassetclass;
    }

    public com.pas.dbobjects.Tblinvestmenttype getTblinvestmenttype() {
        return this.tblinvestmenttype;
    }

    public void setTblinvestmenttype(com.pas.dbobjects.Tblinvestmenttype tblinvestmenttype) {
        this.tblinvestmenttype = tblinvestmenttype;
    }

    public String getCurrentPriceAsString()
	{
		return currentPriceAsString;
	}

	public void setCurrentPriceAsString(String currPrice)
	{
		this.currentPriceAsString = currPrice;
	}

	public Integer getIinvestmentTypeId() {
		return iinvestmentTypeId;
	}

	public void setIinvestmentTypeId(Integer iinvestmentTypeId) {
		this.iinvestmentTypeId = iinvestmentTypeId;
	}

	public Integer getIassetClassId() {
		return iassetClassId;
	}

	public void setIassetClassId(Integer iassetClassId) {
		this.iassetClassId = iassetClassId;
	}

}
