package com.cs490.vo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import yahoofinance.Stock;

public class UserVO {
	private String userName;
	private BigDecimal originalBalance;
	private BigDecimal balance;
	private LinkedHashMap<Stock, Integer> userStocks;
	
	public UserVO() {
		userName = "";
		balance = new BigDecimal(0);
		originalBalance = new BigDecimal(0);
		userStocks = new LinkedHashMap<Stock, Integer>();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getOriginalBalance() {
		return originalBalance;
	}
	public void setOriginalBalance(BigDecimal originalBalance) {
		this.originalBalance = originalBalance;
	}
	public LinkedHashMap<Stock, Integer> getUserStocks() {
		return userStocks;
	}
	public void setUserStocks(LinkedHashMap<Stock, Integer> stocks) {
		this.userStocks = stocks;
	}
}
