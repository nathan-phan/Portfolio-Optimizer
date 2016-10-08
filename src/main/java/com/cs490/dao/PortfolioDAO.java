package com.cs490.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.cs490.database.MySqlConnector;
import com.cs490.vo.PortfolioVO;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

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

	public static boolean insertStockToPortfolio(int id, String symbol, int shares) throws SQLException {
		boolean result = false;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String query =  "INSERT INTO portfolio_stocks(portfolio_id, stock_symbol, shares) VALUES(?,?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			prepStmt.setString(2, symbol);
			prepStmt.setInt(3, shares);
			prepStmt.executeUpdate();
			prepStmt.close();
			result = true;
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}

	public static boolean addMoneyToPortfolio(int id, BigDecimal amount) throws SQLException{
		boolean result = false;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String query =  "UPDATE portfolios SET current_balance = (current_balance + ?) WHERE portfolio_id = ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setBigDecimal(1, amount);
			prepStmt.setInt(2, id);
			prepStmt.executeUpdate();
			prepStmt.close();
			result = true;
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}

	public static boolean removeMoneyFromPortfolio(int id, BigDecimal amount) throws SQLException{
		boolean result = false;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String query =  "UPDATE portfolios SET current_balance = (current_balance - ?) WHERE portfolio_id = ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setBigDecimal(1, amount);
			prepStmt.setInt(2, id);
			prepStmt.executeUpdate();
			prepStmt.close();
			result = true;
		} catch(Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}

	public static BigDecimal findPortfolioBalance(int id) throws SQLException {
		Connection connection = null;
		BigDecimal result = new BigDecimal(-1);
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT current_balance FROM portfolios WHERE portfolio_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			if(rs.next()){
				result = rs.getBigDecimal(1);
			}
			prepStmt.close();
			return result;
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
	}

	public static BigDecimal findInitialStockPrice(String symbol) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2016);
		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		Stock stock = YahooFinance.get(symbol);
		List<HistoricalQuote> history = stock.getHistory(calendar,calendar);
		return history.get(0).getClose();
	}

	public static boolean recordDeposit(int id, BigDecimal amount) throws SQLException{
		boolean result = false;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			BigDecimal currentBalance = findPortfolioBalance(id);
			String query =  "INSERT INTO cash_history (transaction_type, amount, balance, time, portfolio_id) VALUES (?,?,?,?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, "Deposit");
			prepStmt.setBigDecimal(2, amount);
			prepStmt.setBigDecimal(3, currentBalance);
			prepStmt.setString(4, timeStamp);
			prepStmt.setInt(5, id);
			prepStmt.executeUpdate();
			prepStmt.close();
			result = true;
		} catch(Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}
}
