package com.cs490.vo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

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
	private BigDecimal change; 
	private BigDecimal changePercent; 
	private BigDecimal previousClosingPrice;

	public StockVO(String symbol) throws JsonParseException, JsonMappingException, IOException{
		String[] niftyArray = MainServlet.NIFTY_STOCKS.split(",");
		if(Arrays.asList(niftyArray).contains(symbol)){
			GoogleStock stock = new GoogleStock(symbol);
			this.symbol = stock.getSymbol();
			this.exchange = stock.getExchange();
			this.price = stock.getPrice();
			this.change = stock.getChange();
			this.changePercent = stock.getChangePercent();
			this.previousClosingPrice = stock.getPreviousClosingPrice();
			this.name = stock.getName();
		} else {
			Stock stock = YahooFinance.get(symbol);
			this.symbol = stock.getSymbol();
			this.exchange = stock.getStockExchange();
			this.price = stock.getQuote().getPrice();
			this.change = stock.getQuote().getChange();
			this.changePercent = stock.getQuote().getChangeInPercent();
			this.previousClosingPrice = stock.getQuote().getPreviousClose();
			this.name = stock.getName();
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


}
