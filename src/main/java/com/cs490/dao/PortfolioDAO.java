package com.cs490.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.net.SyslogAppender;

import com.cs490.database.MySqlConnector;
import com.cs490.servlet.MainServlet;
import com.cs490.vo.GoogleStock;
import com.cs490.vo.PortfolioVO;
import com.cs490.vo.RecordVO;
import com.cs490.vo.StockPriceComparator;
import com.cs490.vo.StockSnapshotVO;
import com.cs490.vo.StockVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonParser;

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
			String query = "SELECT portfolio_name FROM portfolios WHERE user_id=? AND portfolio_name=? and flag=0";
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

	public static LinkedHashMap<StockVO, Integer> findCurrentStocksByPortfolioId(int portId) throws SQLException {
		Connection connection = null;
		LinkedHashMap<StockVO, Integer> stocks = new LinkedHashMap<StockVO, Integer>();
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT stock_symbol, shares FROM portfolio_stocks WHERE portfolio_id=? and shares <> 0";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, portId);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				StockVO stock = new StockVO(rs.getString(1));
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

	public static LinkedHashMap<StockVO, Integer> findAllStocksByPortfolioId(int portId) throws SQLException {
		Connection connection = null;
		LinkedHashMap<StockVO, Integer> stocks = new LinkedHashMap<StockVO, Integer>();
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
				StockVO stock = new StockVO(rs.getString(1));
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

	public static int findStockShares(String symbol, int id) throws SQLException {
		Connection connection = null;
		int result = -1;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT shares FROM portfolio_stocks WHERE portfolio_id=? and stock_symbol=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			prepStmt.setString(2, symbol);
			ResultSet rs = prepStmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
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
				LinkedHashMap<StockVO, Integer> stocks = findCurrentStocksByPortfolioId(portfolioId);
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

	public static PortfolioVO getPortfolioByIdForReport(int portfolioId) throws SQLException {
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
				LinkedHashMap<StockVO, Integer> stocks = findAllStocksByPortfolioId(portfolioId);
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

	public static boolean insertStockToPortfolio(int id, String symbol, int shares, boolean firstTime) throws SQLException {
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
			if(firstTime){
				String query =  "INSERT INTO portfolio_stocks(portfolio_id, stock_symbol, shares) VALUES(?,?,?)";
				prepStmt = connection.prepareStatement(query);
				prepStmt.setInt(1, id);
				prepStmt.setString(2, symbol);
				prepStmt.setInt(3, shares);
				prepStmt.executeUpdate();
				prepStmt.close();
			} else {
				String query =  "UPDATE portfolio_stocks SET shares = (shares + ?) WHERE portfolio_id = ? AND stock_symbol = ?";
				prepStmt = connection.prepareStatement(query);
				prepStmt.setInt(1, shares);
				prepStmt.setInt(2, id);
				prepStmt.setString(3, symbol);
				prepStmt.executeUpdate();
				prepStmt.close();
			}

			result = true;
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}

	public static boolean removeStocksFromPortfolio(int id, String symbol, int shares) throws SQLException {
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
			String query =  "UPDATE portfolio_stocks SET shares = (shares - ?) WHERE portfolio_id = ? AND stock_symbol = ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, shares);
			prepStmt.setInt(2, id);
			prepStmt.setString(3, symbol);
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
			//amount is negative here. Probably shouldnt have made this method
			String query =  "UPDATE portfolios SET current_balance = (current_balance + ?) WHERE portfolio_id = ?";
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

	public static BigDecimal findInitialStockPrice(String symbol) throws SQLException {
		String[] niftyArray = MainServlet.NIFTY_STOCKS.split(",");
		if(Arrays.asList(niftyArray).contains(symbol)){
			Connection connection = null;
			try {
				connection = new MySqlConnector().getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			PreparedStatement prepStmt = null;
			BigDecimal result = new BigDecimal(-1);
			try {
				String query = "SELECT price FROM nifty_initial WHERE symbol=?";
				prepStmt = connection.prepareStatement(query);
				prepStmt.setString(1, symbol);
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
		} else {
			Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.YEAR, 2016);
			calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
			calendar.set(Calendar.DAY_OF_MONTH, 12);
			Stock stock = YahooFinance.get(symbol);
			List<HistoricalQuote> history = stock.getHistory(calendar,calendar);
			return history.get(0).getClose();
		}
	}

	public static BigDecimal findCurrentStockPrice(String symbol) throws SQLException, JsonParseException, JsonMappingException, IOException {
		String[] niftyArray = MainServlet.NIFTY_STOCKS.split(",");
		if(Arrays.asList(niftyArray).contains(symbol)){
			GoogleStock stock = new GoogleStock(symbol);
			return stock.getPrice();
		} else {
			Stock stock = YahooFinance.get(symbol);
			return stock.getQuote().getPrice();
		}
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
			String query =  "INSERT INTO history (transaction_type, money_amount, balance, time, portfolio_id) VALUES (?,?,?,?,?)";
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

	public static boolean recordWithdraw(int id, BigDecimal amount) throws SQLException{
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
			String query =  "INSERT INTO history (transaction_type, money_amount, balance, time, portfolio_id) VALUES (?,?,?,?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, "Withdraw");
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

	public static ArrayList<RecordVO> getPortfolioRecord(int id) throws SQLException {
		Connection connection = null;
		ArrayList<RecordVO> records = new  ArrayList<RecordVO>();
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT transaction_type, money_amount, stock_symbol, shares, share_price, balance, time FROM history WHERE portfolio_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				RecordVO record = new RecordVO();
				record.setType(rs.getString(1));
				record.setAmount(rs.getBigDecimal(2));
				record.setSymbol(rs.getString(3));
				record.setShares(rs.getInt(4));
				record.setPrice(rs.getBigDecimal(5));
				record.setBalance(rs.getBigDecimal(6));
				record.setTime(rs.getString(7));
				records.add(record);
			}
			prepStmt.close();
			return records;
		} catch(Exception e){
			e.printStackTrace();
			return records;
		} finally {
			connection.close();
		}
	}

	public static ArrayList<RecordVO> getMoneyRecord(int id) throws SQLException {
		Connection connection = null;
		ArrayList<RecordVO> records = new  ArrayList<RecordVO>();
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT transaction_type, money_amount, balance, time "
					+ "FROM history WHERE portfolio_id=? "
					+ "AND transaction_type IN ('Deposit', 'Withdraw') ";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				RecordVO record = new RecordVO();
				record.setType(rs.getString(1));
				record.setAmount(rs.getBigDecimal(2));
				record.setBalance(rs.getBigDecimal(3));
				record.setTime(rs.getString(4));
				records.add(record);
			}
			prepStmt.close();
			return records;
		} catch(Exception e){
			e.printStackTrace();
			return records;
		} finally {
			connection.close();
		}
	}

	public static boolean deletePortfolio(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "UPDATE portfolios SET flag=1 WHERE portfolio_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
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

	public static boolean recordStockPurchase(String symbol, int shares, BigDecimal price, int id, boolean firstTime) throws SQLException{
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
			String timeStamp = firstTime? "2016-09-12 00:00:00" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			BigDecimal currentBalance = findPortfolioBalance(id);
			String query =  "INSERT INTO history (transaction_type, stock_symbol, shares, share_price, balance, time, portfolio_id) VALUES (?,?,?,?,?,?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, "Buy");
			prepStmt.setString(2, symbol);
			prepStmt.setInt(3, shares);
			prepStmt.setBigDecimal(4, price);
			prepStmt.setBigDecimal(5, currentBalance);
			prepStmt.setString(6, timeStamp);
			prepStmt.setInt(7, id);
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

	public static boolean recordStockRemoval(String symbol, int shares, BigDecimal price, int id) throws SQLException{
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
			String query =  "INSERT INTO history (transaction_type, stock_symbol, shares, share_price, balance, time, portfolio_id) VALUES (?,?,?,?,?,?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, "Sell");
			prepStmt.setString(2, symbol);
			prepStmt.setInt(3, shares);
			prepStmt.setBigDecimal(4, price);
			prepStmt.setBigDecimal(5, currentBalance);
			prepStmt.setString(6, timeStamp);
			prepStmt.setInt(7, id);
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

	public static void insertNifty() throws SQLException{
		HashMap<String, Double> x = new HashMap<String, Double>(); 
		x.put("NSE:ACC",	1584.0);
		x.put("NSE:ADANIPORTS",	259.75);
		x.put("NSE:AMBUJACEM",	260.0);
		x.put("NSE:ASIANPAINT",	1155.0);
		x.put("NSE:AXISBANK",	594.5);
		x.put("NSE:BAJAJ-AUTO",	2953.0);
		x.put("NSE:BANKBARODA",	162.1);
		x.put("NSE:BHARTIARTL",	317.8);
		x.put("NSE:BHEL",	147.6);
		x.put("NSE:BOSCHLTD",	23400.1);
		x.put("NSE:BPCL",	566.0);
		x.put("NSE:CAIRN",	191.25);
		x.put("NSE:CIPLA",	570.5);
		x.put("NSE:COALINDIA",	330.3);
		x.put("NSE:DRREDDY",	3140.0);
		x.put("NSE:GAIL",	386.25);
		x.put("NSE:GRASIM",	4642.0);
		x.put("NSE:HCLTECH",	780.2);
		x.put("NSE:HDFC",	1398.0);
		x.put("NSE:HDFCBANK",	1279.6);
		x.put("NSE:HEROMOTOCO",	3537.0);
		x.put("NSE:HINDALCO",	141.2);
		x.put("NSE:HINDUNILVR",	919.0);
		x.put("NSE:ICICIBANK",	267.85);
		x.put("NSE:IDEA",	82.7);
		x.put("NSE:INDUSINDBK",	1195.35);
		x.put("NSE:INFY",	1055.0);
		x.put("NSE:ITC",	251.85);
		x.put("NSE:KOTAKBANK",	814.0);
		x.put("NSE:LT",	1463.0);
		x.put("NSE:LUPIN",	1533.2);
		x.put("NSE:M&M",	1424.9);
		x.put("NSE:MARUTI",	5330.0);
		x.put("NSE:NTPC",	153.0);
		x.put("NSE:ONGC",	250.8);
		x.put("NSE:PNB"	,137.0);
		x.put("NSE:POWERGRID",	180.05);
		x.put("NSE:RELIANCE",	1046.95);
		x.put("NSE:SBIN",	253.65);
		x.put("NSE:SUNPHARMA",	786.0);
		x.put("NSE:TATAMOTORS",	553.75);
		x.put("NSE:TATAPOWER",	74.15);
		x.put("NSE:TATASTEEL",	371.95);
		x.put("NSE:TCS",	2358.0);
		x.put("NSE:TECHM"	,464.1);
		x.put("NSE:ULTRACEMCO", 	3869.05);
		x.put("NSE:VEDL",	163.0);
		x.put("NSE:WIPRO",	482.0);
		x.put("NSE:YESBANK",	1199.5);
		x.put("NSE:ZEEL",	517.35);
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query =  "INSERT INTO nifty_initial (symbol, price) VALUES (?,?)";
			prepStmt = connection.prepareStatement(query);
			for(String a:x.keySet()){
				BigDecimal b = new BigDecimal(x.get(a));
				prepStmt.setString(1, a);
				prepStmt.setBigDecimal(2, b);
				prepStmt.executeUpdate();
			}
			prepStmt.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public static BigDecimal initialCurrencyConvert (String symbol, BigDecimal price) {
		String[] niftyArray = MainServlet.NIFTY_STOCKS.split(",");
		String[] straitArray = MainServlet.STRAIT_STOCKS.split(",");
		if(Arrays.asList(niftyArray).contains(symbol)){
			return price.divide(new BigDecimal(MainServlet.INITIAL_USD_INR),4, RoundingMode.HALF_UP);
		}
		if(Arrays.asList(straitArray).contains(symbol)){
			return price.divide(new BigDecimal(MainServlet.INITIAL_USD_SGD),4, RoundingMode.HALF_UP);
		}
		return price;
	}

	public static LinkedHashMap<String, Double> getExchangeRates() throws MalformedURLException, IOException{
		String jsonContent = IOUtils.toString(new URL("http://www.apilayer.net/api/live?access_key=" + MainServlet.CURRENCY_API_KEY + "&format=1"), Charset.forName("UTF-8"));
		JsonParser jsonParser = new JsonParser();
		String inrRate = jsonParser.parse(jsonContent).getAsJsonObject().get("quotes").getAsJsonObject().get("USDINR").getAsString();
		String sgdRate = jsonParser.parse(jsonContent).getAsJsonObject().get("quotes").getAsJsonObject().get("USDSGD").getAsString();
		LinkedHashMap<String, Double> rates = new LinkedHashMap<String, Double>();
		rates.put("USDINR", Double.valueOf(inrRate));
		rates.put("USDSGD", Double.valueOf(sgdRate));
		return rates;
	}

	public static BigDecimal liveCurrencyConvert(String symbol, BigDecimal price) throws MalformedURLException, IOException {
		String jsonContent = IOUtils.toString(new URL("http://www.apilayer.net/api/live?access_key=" + MainServlet.CURRENCY_API_KEY + "&format=1"), Charset.forName("UTF-8"));
		JsonParser jsonParser = new JsonParser();
		String inrRate = jsonParser.parse(jsonContent).getAsJsonObject().get("quotes").getAsJsonObject().get("USDINR").getAsString();
		String sgdRate = jsonParser.parse(jsonContent).getAsJsonObject().get("quotes").getAsJsonObject().get("USDSGD").getAsString();
		String[] niftyArray = MainServlet.NIFTY_STOCKS.split(",");
		String[] straitArray = MainServlet.STRAIT_STOCKS.split(",");
		if(Arrays.asList(niftyArray).contains(symbol)){
			return price.divide(new BigDecimal(inrRate), 4, RoundingMode.HALF_UP);
		}
		if(Arrays.asList(straitArray).contains(symbol)){
			return price.divide(new BigDecimal(sgdRate),4, RoundingMode.HALF_UP);
		}
		return price;
	}

	public static boolean checkForInitialPurchase(int id, String symbol) throws SQLException{
		Connection connection = null;
		boolean result = true;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT 1 FROM portfolio_stocks WHERE portfolio_id=? AND stock_symbol=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			prepStmt.setString(2, symbol);
			ResultSet rs = prepStmt.executeQuery();
			if(rs.next()){
				result = false;
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
	public static boolean checkForEditDupe(int id, String name, String user) throws SQLException{
		Connection connection = null;
		boolean result = false;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT 1 FROM portfolios WHERE portfolio_name=? AND user_id = ? AND portfolio_id <> ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, name);
			prepStmt.setString(2, user);
			prepStmt.setInt(3, id);
			ResultSet rs = prepStmt.executeQuery();
			if(rs.next()){
				result = true;
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

	public static boolean updatePortfolioName(int id, String name) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "UPDATE portfolios SET portfolio_name=? WHERE portfolio_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, name);
			prepStmt.setInt(2, id);
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

	public static ArrayList<StockSnapshotVO> findStockPurchaseHistory(String symbol, int id) throws SQLException {
		ArrayList<StockSnapshotVO> history = new ArrayList<StockSnapshotVO>();
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT shares, share_price, time FROM history WHERE portfolio_id=? AND transaction_type='Buy' AND stock_symbol=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			prepStmt.setString(2, symbol);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				StockSnapshotVO snapshot = new StockSnapshotVO();
				snapshot.setShares(rs.getInt(1));
				snapshot.setDate(rs.getString(3));
				snapshot.setPrice(rs.getBigDecimal(2));
				snapshot.setSymbol(symbol);
				history.add(snapshot);
			}
			prepStmt.close();
			rs.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return history;
	}

	public static ArrayList<StockSnapshotVO> findStockSellHistory(String symbol, int id) throws SQLException {
		ArrayList<StockSnapshotVO> history = new ArrayList<StockSnapshotVO>();
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT shares, share_price, time FROM history WHERE portfolio_id=? AND transaction_type='Sell' AND stock_symbol=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			prepStmt.setString(2, symbol);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				StockSnapshotVO snapshot = new StockSnapshotVO();
				snapshot.setShares(rs.getInt(1));
				snapshot.setDate(rs.getString(3));
				snapshot.setPrice(rs.getBigDecimal(2));
				snapshot.setSymbol(symbol);
				history.add(snapshot);
			}
			prepStmt.close();
			rs.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return history;
	}

	public static ArrayList<StockSnapshotVO> findStockTransactionHistory(String symbol, int id) throws SQLException {
		ArrayList<StockSnapshotVO> history = new ArrayList<StockSnapshotVO>();
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT shares, share_price, time, transaction_type FROM history WHERE portfolio_id=? AND transaction_type IN ('Sell','Buy') AND stock_symbol=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			prepStmt.setString(2, symbol);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				StockSnapshotVO snapshot = new StockSnapshotVO();
				snapshot.setShares(rs.getInt(1));
				snapshot.setDate(rs.getString(3));
				snapshot.setPrice(rs.getBigDecimal(2));
				snapshot.setSymbol(symbol);
				snapshot.setType(rs.getString(4));
				history.add(snapshot);
			}
			prepStmt.close();
			rs.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return history;
	}

	public static ArrayList<BigDecimal> findNetGain(ArrayList<StockSnapshotVO> buyHistory, ArrayList<StockSnapshotVO> sellHistory) throws CloneNotSupportedException {
		ArrayList<StockSnapshotVO> sortedBuy = new ArrayList<StockSnapshotVO>(buyHistory.size());
		ArrayList<BigDecimal> profit = new ArrayList<BigDecimal>();
		for (StockSnapshotVO x: buyHistory) {
			sortedBuy.add((StockSnapshotVO)x.clone());
		}
		Collections.sort(sortedBuy, new StockPriceComparator());
		for(StockSnapshotVO s : sellHistory) {
			if(sortedBuy.isEmpty()) return profit;
			BigDecimal tempNet = new BigDecimal(0);
			BigDecimal sellPrice = s.getPrice();
			int sellShares = s.getShares();
			for(int i=0; i<sortedBuy.size();i++) {
				StockSnapshotVO b = sortedBuy.get(i);
				int buyShares = b.getShares();
				if(buyShares <= sellShares) {
					sellShares -= buyShares;
					BigDecimal buyPrice = b.getPrice();
					BigDecimal currDiff = sellPrice.subtract(buyPrice);
					tempNet = tempNet.add(currDiff.multiply(new BigDecimal(buyShares)));
					sortedBuy.remove(i);
					i--;
					continue;
				} else {
					buyShares -= sellShares;
					b.setShares(buyShares);
					BigDecimal buyPrice = b.getPrice();
					BigDecimal currDiff = sellPrice.subtract(buyPrice);
					tempNet = tempNet.add(currDiff.multiply(new BigDecimal(sellShares)));
				}
				break;
			}
			profit.add(tempNet);
		}
		return profit;
	}

	//This is not an elegant method at all but since user will probably have fewer than 10 stocks
	//and each stock has fewer than 10 transactions, this method is probably acceptable
	public static ArrayList<BigDecimal> findNetGain(ArrayList<StockSnapshotVO> history) throws CloneNotSupportedException {
		ArrayList<BigDecimal> profit = new ArrayList<BigDecimal>();
		for(int i=0; i<history.size(); i++) {
			if(history.get(i).getType().equals("Sell")) {
				ArrayList<StockSnapshotVO> previousTransactions = new ArrayList<StockSnapshotVO>( history.subList(0, i));
				ArrayList<StockSnapshotVO> previousPurchases = new ArrayList<StockSnapshotVO>();
				for(StockSnapshotVO ss: previousTransactions){
					if(ss.getType().equals("Buy")){
						previousPurchases.add(ss);
					}
				}
				ArrayList<StockSnapshotVO> sortedBuy = new ArrayList<StockSnapshotVO>(previousPurchases.size());
				for (StockSnapshotVO x: previousPurchases) {
					sortedBuy.add((StockSnapshotVO)x.clone());
				}
				Collections.sort(sortedBuy, new StockPriceComparator());
				if(sortedBuy.isEmpty()) return profit;
				BigDecimal tempNet = new BigDecimal(0);
				BigDecimal sellPrice = history.get(i).getPrice();
				int sellShares = history.get(i).getShares();
				for(int j=0; j<sortedBuy.size();j++) {
					StockSnapshotVO originalB = new StockSnapshotVO();
					StockSnapshotVO b = sortedBuy.get(j);
					for(StockSnapshotVO x:history){
						if(x.getType().equals("Buy") && x.getDate().equals(b.getDate())){
							originalB = x;
							break;
						}
					}
					int buyShares = b.getShares();
					if(buyShares <= sellShares) {
						sellShares -= buyShares;
						BigDecimal buyPrice = b.getPrice();
						BigDecimal currDiff = sellPrice.subtract(buyPrice);
						tempNet = tempNet.add(currDiff.multiply(new BigDecimal(buyShares)));
						sortedBuy.remove(j);
						history.remove(originalB);
						i--;
						j--;
						continue;
					} else {
						buyShares -= sellShares;
						b.setShares(buyShares);
						originalB.setShares(buyShares);
						BigDecimal buyPrice = b.getPrice();
						BigDecimal currDiff = sellPrice.subtract(buyPrice);
						tempNet = tempNet.add(currDiff.multiply(new BigDecimal(sellShares)));
					}
					break;
				}
				profit.add(tempNet);
			}
		}
		return profit;
	}

	public static boolean insertExpectedReturns(String symbol, double returnVal) throws SQLException {
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
			String query =  "INSERT INTO stock_returns(symbol,expected_return) VALUES(?,?)";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1,symbol);
			prepStmt.setDouble(2, returnVal);
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

	public static boolean insertCovariances(LinkedHashMap<String, Double> map) throws SQLException {
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
			String query =  "INSERT IGNORE INTO stock_covariances(symbol_pair,covariance) VALUES(?,?)";
			prepStmt = connection.prepareStatement(query);
			int count=0;
			for(String x: map.keySet()){
				prepStmt.setString(1, x);
				prepStmt.setDouble(2, map.get(x));
				prepStmt.executeUpdate();
				System.out.println("Inserted " + x);
				count++;
				System.out.println(count + " entries");
				Thread.sleep(100);
			}
			prepStmt.close();
			result = true;
			System.out.println("Done");
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}

}
