package com.cs490.vo;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RecordVO {
  private String type;
  private BigDecimal amount;
  private String symbol;
  private int shares;
  private BigDecimal price;
  private BigDecimal balance;
  private String time;
  
  public RecordVO() {
  	type = "";
  	amount = new BigDecimal(0);
  	symbol = "";
  	shares = 0;
  	price = new BigDecimal(0);
  	balance = new BigDecimal(0);
  	time = "";
  }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
