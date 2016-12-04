package com.cs490.vo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import com.cs490.dao.PortfolioDAO;
import com.cs490.servlet.MainServlet;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockVO {
	private String name;
	private String symbol;
	private String exchange; 
	private BigDecimal price;
	private BigDecimal foreignPrice;
	private BigDecimal change;
	private BigDecimal foreignChange;
	private BigDecimal changePercent; 
	private BigDecimal previousClosingPrice;
	private BigDecimal foreignPreviousClosingPrice;
	private String currency;
	private double weight;

	public StockVO(String symbol) throws JsonParseException, JsonMappingException, IOException{
		String[] niftyArray = MainServlet.NIFTY_STOCKS.split(",");
		if(Arrays.asList(niftyArray).contains(symbol)){
			GoogleStock stock = new GoogleStock(symbol.replace("&", "%26"));
			this.symbol = symbol;
			this.exchange = stock.getExchange();
			this.price = stock.getPrice();
			this.foreignChange = stock.getChange();
			this.change = stock.getChange();
			this.changePercent = stock.getChangePercent();
			this.previousClosingPrice = stock.getPreviousClosingPrice();
			this.foreignPreviousClosingPrice = stock.getPreviousClosingPrice();
			this.name = stock.getName();
			this.foreignPrice = stock.getPrice();
			this.currency = "INR";
		} else {
			Stock stock = YahooFinance.get(symbol);
			this.symbol = symbol;
			this.exchange = stock.getStockExchange();
			this.foreignPrice = stock.getQuote().getPrice();
			this.price = stock.getQuote().getPrice();
			this.foreignChange = stock.getQuote().getChange();
			this.change = stock.getQuote().getChange();
			this.changePercent = stock.getQuote().getChangeInPercent();
			this.foreignPreviousClosingPrice = stock.getQuote().getPreviousClose();
			this.previousClosingPrice = stock.getQuote().getPreviousClose();
			this.name = stock.getName();
			this.currency = symbol.contains(".SI") ? "SGD":"USD";
		}
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public BigDecimal getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(BigDecimal changePercent) {
		this.changePercent = changePercent;
	}

	public BigDecimal getPreviousClosingPrice() {
		return previousClosingPrice;
	}

	public void setPreviousClosingPrice(BigDecimal previousClosingPrice) {
		this.previousClosingPrice = previousClosingPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getForeignPrice() {
		return foreignPrice;
	}

	public void setForeignPrice(BigDecimal foreignPrice) {
		this.foreignPrice = foreignPrice;
	}

	public BigDecimal getForeignPreviousClosingPrice() {
		return foreignPreviousClosingPrice;
	}

	public void setForeignPreviousClosingPrice(BigDecimal foreignPreviousClosingPrice) {
		this.foreignPreviousClosingPrice = foreignPreviousClosingPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getForeignChange() {
		return foreignChange;
	}

	public void setForeignChange(BigDecimal foreignChange) {
		this.foreignChange = foreignChange;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
