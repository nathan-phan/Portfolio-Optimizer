package com.cs490.vo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import yahoofinance.Stock;

public class PortfolioVO {
	private int id;
	private String name;
	private BigDecimal balance;
	private String userName;
	private LinkedHashMap<Stock, Integer> stocks;

	public PortfolioVO() {
		id = -1;
		name = "";
		balance = new BigDecimal(0);
		userName = "";
		stocks = new LinkedHashMap<Stock, Integer>();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LinkedHashMap<Stock, Integer> getStocks() {
		return stocks;
	}

	public void setStocks(LinkedHashMap<Stock, Integer> stocks) {
		this.stocks = stocks;
	}


}
