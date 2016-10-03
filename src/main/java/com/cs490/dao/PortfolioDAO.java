package com.cs490.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cs490.database.MySqlConnector;

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
}
