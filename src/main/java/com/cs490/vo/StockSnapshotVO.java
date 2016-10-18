package com.cs490.vo;

import java.math.BigDecimal;

public class StockSnapshotVO implements Cloneable{
	private String date;
	private String symbol;
	private int shares;
	private BigDecimal price;
	private BigDecimal net;

	public StockSnapshotVO(){
		date = "";
		symbol = "";
		shares = 0;
		price = new BigDecimal(0);
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public BigDecimal getNet() {
		return net;
	}

	public void setNet(BigDecimal net) {
		this.net = net;
	}
}
