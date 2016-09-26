package com.cs490.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.cs490.database.MySqlConnector;
import com.cs490.vo.UserVO;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class UserDAO {
	public static UserVO getUserInfo(String userId) throws SQLException{
		UserVO user = null;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			String query = "select user_id, original_balance, current_balance from users where user_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userId);
			rs = prepStmt.executeQuery();
			while(rs.next()){
				user = new UserVO();
				user.setUserName(rs.getString(1));
				user.setBalance(rs.getBigDecimal(3));
				user.setOriginalBalance(rs.getBigDecimal(2));
			}
			
			query = "select stock_symbol, shares from user_stocks where user_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userId);
			rs = prepStmt.executeQuery();
			LinkedHashMap<Stock, Integer> stocks = user.getUserStocks();
			while(rs.next()){
				String stockSymbol = rs.getString(1);
				Stock stock = YahooFinance.get(stockSymbol);
				stocks.put(stock, rs.getInt(2));
			};
			user.setUserStocks(stocks);
			prepStmt.close();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		} finally {
			connection.close();
		}
		return user;
	}
}
