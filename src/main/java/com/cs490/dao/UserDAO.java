package com.cs490.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import com.cs490.database.MySqlConnector;
import com.cs490.vo.PortfolioVO;
import com.cs490.vo.UserVO;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class UserDAO {
	public static boolean registerUser(String userName, String email, String encryptedPassword) throws SQLException{
		boolean result = false;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "INSERT INTO users (user_id, email, password) " + 
					"VALUES (?, ?, ?);";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			prepStmt.setString(2, email);
			prepStmt.setString(3, encryptedPassword);
			prepStmt.executeUpdate();
			prepStmt.close();
			result = true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			connection.close();
		}
		return result;
	}

	public static boolean checkDuplication (String userName) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT user_id FROM users WHERE user_id = ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			ResultSet rs = prepStmt.executeQuery();
			if(rs.next()){
				return true;
			}
			prepStmt.close();
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			connection.close();
		}
		return false;
	}

	public static String authenticate(String userName, String password) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT user_id, password FROM users WHERE user_id = ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			ResultSet rs = prepStmt.executeQuery();
			if(!rs.next()){
				prepStmt.close();
				return "Username not found";
			} else {
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				textEncryptor.setPassword("keyForPass"); 
				if(textEncryptor.decrypt(rs.getString(2)).equals(password)){
					return "ok";
				} else {
					return "Failed to authenticate user";
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			return "Database error";
		} finally {
			connection.close();
		}
	}

	public boolean sendRegistrationEmail(String email) throws AddressException, MessagingException, IOException {
		try{
			Properties mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			MimeMessage message = new MimeMessage(getMailSession);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("Hello from Portfolio Gorilla");
			String emailBody = "Welcome to Portfolio Gorilla! This is a CS490 final project. Please enjoy your stay!" + "<br><br> Regards, <br>Porfolio Gorilla creators";
			message.setContent(emailBody, "text/html");
			Transport transport = getMailSession.getTransport("smtp");
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword("keyForPass"); 
			InputStream in = getClass().getClassLoader().getResourceAsStream("mysql.properties");
			Properties props = new Properties();
			String gmail_encrypted = "";
			if (in != null) {
				props.load(in);
				gmail_encrypted = props.getProperty("gmail_password");
			}
			String decryptedGmailPass = textEncryptor.decrypt(gmail_encrypted);
			transport.connect("smtp.gmail.com", "portfolio.gorilla@gmail.com", decryptedGmailPass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean checkUserEmail(String userName, String email) throws SQLException{
		boolean result = false;
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT email FROM users WHERE user_id = ?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			ResultSet rs = prepStmt.executeQuery();
			if(!rs.next()){
				prepStmt.close();
				return false;
			} else {
				if(email.equals(rs.getString(1))){
					result = true;
				} 
			}
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			connection.close();
		}
		return result;
	}
	
	public boolean resetUserPassword (String userName, String email) throws AddressException, MessagingException, IOException, SQLException {
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("keyForPass"); 
		String randomPassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
		String encryptedRandom = textEncryptor.encrypt(randomPassword);
		Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		MimeMessage message = new MimeMessage(getMailSession);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		message.setSubject("Portfolio Gorilla - Password reset request");
		String emailBody = "Hello from Portfolio Gorilla! We received your request for a new password so here it is: " + randomPassword +
				"<br>You can use it to log in your account immediately." + "<br><br> Regards, <br>Porfolio Gorilla";
		message.setContent(emailBody, "text/html");
		Transport transport = getMailSession.getTransport("smtp");
		
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		PreparedStatement prepStmt = null;
		try {
			String query = "UPDATE users SET password=? WHERE user_id=?";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, encryptedRandom);
			prepStmt.setString(2, userName);
			prepStmt.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			connection.close();
		}
		InputStream in = getClass().getClassLoader().getResourceAsStream("mysql.properties");
		Properties props = new Properties();
		String gmail_encrypted = "";
		if (in != null) {
			props.load(in);
			gmail_encrypted = props.getProperty("gmail_password");
		}
		String decryptedGmailPass = textEncryptor.decrypt(gmail_encrypted);
		transport.connect("smtp.gmail.com", "portfolio.gorilla@gmail.com", decryptedGmailPass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		return true;
	}
	
	public static ArrayList<PortfolioVO> findUserPorfolio (String userName) throws SQLException {
		Connection connection = null;
		try {
			connection = new MySqlConnector().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<PortfolioVO> portfolios = new ArrayList<PortfolioVO>();
		PreparedStatement prepStmt = null;
		try {
			String query = "SELECT portfolio_id, portfolio_name, current_balance FROM portfolios WHERE user_id = ? AND flag=0";
			prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, userName);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				PortfolioVO port = new PortfolioVO();
				port.setId(rs.getInt(1));
				port.setName(rs.getString(2));
				port.setBalance(rs.getBigDecimal(3));
				port.setUserName(userName);
				LinkedHashMap<Stock, Integer> stocks = PortfolioDAO.findStocksByPortfolioId(rs.getInt(1));
				port.setStocks(stocks);
				portfolios.add(port);
			}
		} catch(Exception e){
			e.printStackTrace();
			return null;
		} finally {
			connection.close();
		}
		return portfolios;
	}
}
