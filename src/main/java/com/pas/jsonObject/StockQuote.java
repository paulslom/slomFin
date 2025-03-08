package com.pas.jsonObject;

import java.math.BigDecimal;

public class StockQuote 
{
	private String status;
	private String from;
	private String symbol;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
    private Long volume;
    private BigDecimal afterHours;
    private BigDecimal preMarket;
    
    public StockQuote() 
    {		
	}
    
    public StockQuote(String status, String from, String symbol, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close,
    					Long volume, BigDecimal afterHours, BigDecimal preMarket) 
    {
    	this.status = status;
    	this.from = from;
    	this.symbol = symbol;
    	this.open = open;
    	this.high = high;
    	this.low = low;
    	this.close = close;
    	this.volume = volume;
    	this.afterHours = afterHours;
    	this.preMarket = preMarket;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public Long getVolume() {
		return volume;
	}
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	public BigDecimal getAfterHours() {
		return afterHours;
	}
	public void setAfterHours(BigDecimal afterHours) {
		this.afterHours = afterHours;
	}
	public BigDecimal getPreMarket() {
		return preMarket;
	}
	public void setPreMarket(BigDecimal preMarket) {
		this.preMarket = preMarket;
	}

}
