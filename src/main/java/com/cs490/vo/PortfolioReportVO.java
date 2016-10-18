package com.cs490.vo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.cs490.dao.PortfolioDAO;
import com.cs490.servlet.MainServlet;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class PortfolioReportVO {
	private String name;
	private int id;
	private BigDecimal balance;
	private BigDecimal totalStockValue;
	private BigDecimal overseaValue;
	private BigDecimal domesticValue;
	private BigDecimal profit;
	private BigDecimal overseaPercent;
	private BigDecimal domesticPercent;
	private BigDecimal cashPercent;
	private LinkedHashMap<StockVO, Integer> stocks;
	private ArrayList<RecordVO> history;
	private ArrayList<StockReportVO> stockReports;
	
	public PortfolioReportVO(int id) throws SQLException, JsonParseException, JsonMappingException, IOException, CloneNotSupportedException{
		PortfolioVO port = PortfolioDAO.getPortfolioByIdForReport(id);
		this.name = port.getName();
		this.balance = port.getBalance();
		this.stocks = port.getStocks();
		this.stockReports = new ArrayList<StockReportVO>();
		LinkedHashMap<String, Double> rates = PortfolioDAO.getExchangeRates();
		String[] adrArray = MainServlet.ADR_STOCKS.split(",");
		this.overseaValue = new BigDecimal(0);
		this.domesticValue = new BigDecimal(0);
		this.totalStockValue = new BigDecimal(0);
		this.overseaPercent = new BigDecimal(0);
		this.domesticPercent = new BigDecimal(0);
		this.cashPercent = new BigDecimal(0);
		this.profit = new BigDecimal(0);
		for(StockVO stock:stocks.keySet()){
			StockReportVO report = new StockReportVO(stock.getSymbol(), id, rates);
			stockReports.add(report);
			if(report.getCurrency().equals("INR") || report.getCurrency().equals("SGD")
					|| Arrays.asList(adrArray).contains(report.getSymbol())){
				totalStockValue = totalStockValue.add(report.getValue());
				overseaValue = overseaValue.add(report.getValue());
			} else {
				domesticValue = domesticValue.add(report.getValue());
				totalStockValue = totalStockValue.add(report.getValue());
			}
			profit = profit.add(report.getProfit());
		}
		if(totalStockValue.compareTo(new BigDecimal(0)) == 1){
			overseaPercent = overseaValue.divide(totalStockValue,4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			domesticPercent = (new BigDecimal(100).subtract(overseaPercent));
			cashPercent = balance.divide(totalStockValue.add(balance),4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
		}
		this.history = PortfolioDAO.getMoneyRecord(id);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getTotalStockValue() {
		return totalStockValue;
	}
	public void setTotalStockValue(BigDecimal totalStockValue) {
		this.totalStockValue = totalStockValue;
	}
	public BigDecimal getOverseaValue() {
		return overseaValue;
	}
	public void setOverseaValue(BigDecimal overseaValue) {
		this.overseaValue = overseaValue;
	}
	public BigDecimal getDomesticValue() {
		return domesticValue;
	}
	public void setDomesticValue(BigDecimal domesticValue) {
		this.domesticValue = domesticValue;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public ArrayList<RecordVO> getHistory() {
		return history;
	}
	public void setHistory(ArrayList<RecordVO> history) {
		this.history = history;
	}
	public ArrayList<StockReportVO> getStockReports() {
		return stockReports;
	}
	public void setStockReports(ArrayList<StockReportVO> stockReports) {
		this.stockReports = stockReports;
	}

	public LinkedHashMap<StockVO, Integer> getStocks() {
		return stocks;
	}

	public void setStocks(LinkedHashMap<StockVO, Integer> stocks) {
		this.stocks = stocks;
	}

	public BigDecimal getDomesticPercent() {
		return domesticPercent;
	}

	public void setDomesticPercent(BigDecimal domesticPercent) {
		this.domesticPercent = domesticPercent;
	}
	
	public BigDecimal getOverseaPercent() {
		return overseaPercent;
	}

	public void setOverseaPercent(BigDecimal overseaPercent) {
		this.overseaPercent = overseaPercent;
	}

	public BigDecimal getCashPercent() {
		return cashPercent;
	}

	public void setCashPercent(BigDecimal cashPercent) {
		this.cashPercent = cashPercent;
	}
	
}
