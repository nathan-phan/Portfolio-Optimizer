package com.cs490.vo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.cs490.dao.PortfolioDAO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StockReportVO {
	private String symbol;
	private String name;
	private String currency;
	private StockVO stock;
	private int portfolioId;
	private int shares;
	private ArrayList<StockSnapshotVO> buyHistory;
	private ArrayList<StockSnapshotVO> sellHistory;
	private BigDecimal foreignPrice;
	private BigDecimal price;
	private BigDecimal value;
	private BigDecimal profit;
	
	public StockReportVO() {
		symbol = "";
		portfolioId = 0;
		shares = 0;
	}
	
	public StockReportVO(String symbol, int portId, LinkedHashMap<String, Double> rates) throws JsonParseException, JsonMappingException, IOException, SQLException, CloneNotSupportedException{
		this.symbol = symbol;
		this.portfolioId = portId;
		this.stock = new StockVO(symbol);
		this.name = stock.getName();
		this.currency = stock.getCurrency();
		this.shares = PortfolioDAO.findStockShares(symbol,portId);
		if(stock.getCurrency().equals("INR")){
			BigDecimal originalPrice = stock.getForeignPrice();
			this.foreignPrice = originalPrice;
			BigDecimal convertedPrice = originalPrice.divide(new BigDecimal(rates.get("USDINR")), 4, RoundingMode.HALF_UP);
			this.price = convertedPrice;
		} else if(stock.getCurrency().equals("SGD")){
			BigDecimal originalPrice = stock.getForeignPrice(); 
			this.foreignPrice = originalPrice;
			BigDecimal convertedPrice = originalPrice.divide(new BigDecimal(rates.get("USDSGD")), 4, RoundingMode.HALF_UP);
			stock.setPrice(convertedPrice);
			this.price = convertedPrice;
		} else {
			this.price = stock.getPrice();
		}
		this.value = this.price.multiply(new BigDecimal(shares));
		this.buyHistory = PortfolioDAO.findStockPurchaseHistory(symbol, portId);
		this.sellHistory = PortfolioDAO.findStockSellHistory(symbol, portId);
		ArrayList<BigDecimal> netArray = PortfolioDAO.findNetGain(buyHistory, sellHistory);
		this.profit = new BigDecimal(0);
		for(int i = 0; i < sellHistory.size(); i++) {
			sellHistory.get(i).setNet(netArray.get(i));
			this.profit = profit.add(netArray.get(i));
		}
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public StockVO getStock() {
		return stock;
	}

	public void setStock(StockVO stock) {
		this.stock = stock;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public ArrayList<StockSnapshotVO> getBuyHistory() throws SQLException {
		if(this.buyHistory == null) {
			return PortfolioDAO.findStockPurchaseHistory(symbol, portfolioId);
		}
		return buyHistory;
	}

	public void setBuyHistory(ArrayList<StockSnapshotVO> buyHistory) {
		this.buyHistory = buyHistory;
	}

	public ArrayList<StockSnapshotVO> getSellHistory() {
		return sellHistory;
	}

	public void setSellHistory(ArrayList<StockSnapshotVO> sellHistory) {
		this.sellHistory = sellHistory;
	}

	public int getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getForeignPrice() {
		return foreignPrice;
	}

	public void setForeignPrice(BigDecimal foreignPrice) {
		this.foreignPrice = foreignPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
