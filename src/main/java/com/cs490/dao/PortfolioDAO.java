package com.cs490.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.cs490.database.MySqlConnector;
import com.cs490.vo.PortfolioVO;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class PortfolioDAO {
	public static boolean checkDuplicatePortfolioName(String portName, String userName) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT portfolio_name FROM portfolios WHERE user_id=? AND portfolio_name=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			prepStmt.setString(2, portName);
			ResultSet rs = prepStmt.executeQuery();
			if(!rs.next()){
				prepStmt.close();
				return false;
			}
			prepStmt.close();
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			connection.close();
		}
	}
	
	public static boolean addPortfolio(String portName, String userName) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "INSERT INTO portfolios (user_id, portfolio_name, current_balance) "
					+ "VALUES(?,?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			prepStmt.setString(2, portName);
			prepStmt.setBigDecimal(3, new BigDecimal(0));
			prepStmt.executeUpdate();
			prepStmt.close();
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			connection.close();
		}
	}
	
	public static LinkedHashMap<Stock, Integer> findStocksByPortfolioId(int portId) throws SQLException {
		Connection connection = null;
		LinkedHashMap<Stock, Integer> stocks = new LinkedHashMap<Stock, Integer>();
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT stock_symbol, shares FROM portfolio_stocks WHERE portfolio_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, portId);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				Stock stock = new Stock(rs.getString(1));
				stocks.put(stock, rs.getInt(2));
			}
			prepStmt.close();
			return stocks;
		} catch(Exception e){
			e.printStackTrace();
			return stocks;
		} finally {
			connection.close();
		}
	}

	public static PortfolioVO getPortfolioById(int portfolioId) throws SQLException {
		Connection connection = null;
		PortfolioVO port = new PortfolioVO();
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT portfolio_name, current_balance FROM portfolios WHERE portfolio_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, portfolioId);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				port.setName(rs.getString(1));
				port.setBalance(rs.getBigDecimal(2));
				port.setId(portfolioId);
				LinkedHashMap<Stock, Integer> stocks = findStocksByPortfolioId(portfolioId);
				port.setStocks(stocks);
				for(Stock stock:stocks.keySet()){
					stock = YahooFinance.get(stock.getSymbol());
				}
				port.setStocks(stocks);
			}
			prepStmt.close();
			return port;
		} catch(Exception e){
			e.printStackTrace();
			return port;
		} finally {
			connection.close();
		}
	}
	
}
