package com.cs490.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasypt.util.text.BasicTextEncryptor;

import com.cs490.dao.UserDAO;
import com.cs490.vo.UserVO;
import com.google.gson.JsonObject;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@WebServlet(name="MainServlet", displayName="MainServlet", urlPatterns= {
		"/webapps7/stock", "/webapps7/", "/webapps7/register", "/webapps7/forgotpass",
		"/webapps7/login"
})
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 389807010932642772L;

	@Override																	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		if(request.getRequestURI().equals("/webapps7/")){
			try {
				showLoginScreen(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(request.getRequestURI().contains("/stock")){
			try {
				displayStockInfo(request, response);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override																	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		if(request.getRequestURI().contains("/register")){
			try {
				registerUser(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		if(request.getRequestURI().contains("/login")){
			try {
				checkUserCredential(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		if(request.getRequestURI().contains("/forgotpass")){
			try {
				resetUserPassword(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
	}

	private void displayStockInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String symbol = "";
		if(request.getParameter("symbol") != null){
			symbol = request.getParameter("symbol");
		}
		Stock stock = YahooFinance.get(symbol);
		request.setAttribute("stock", stock);
		response.setContentType("text/html");
		request.getRequestDispatcher("/StockInfo.jsp").forward(request, response);
	}

	private void showLoginScreen(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
	}

	private void registerUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, AddressException, MessagingException {
		String userName = "", email = "",  password = "";
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		if(request.getParameter("username") != null) {
			userName = request.getParameter("username");
		}
		if(request.getParameter("email") != null) {
			email = request.getParameter("email");
		}
		if(request.getParameter("password") != null) {
			password = request.getParameter("password");
		}
		if(userName.equals("") || email.equals("") || password.equals("")){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameters");
			response.getWriter().write(json.toString());
			return;
		}
		if(UserDAO.checkDuplication(userName)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Username already exists in database");
			response.getWriter().write(json.toString());
			return;
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("keyForPass"); 
		String encryptedPassword = textEncryptor.encrypt(password);
		if(!UserDAO.registerUser(userName, email, encryptedPassword)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to add user into database");
			response.getWriter().write(json.toString());
			return;
		}
		UserDAO userDAO = new UserDAO();
		if(!userDAO.sendRegistrationEmail(email)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to send email to user address");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		json.addProperty("successMessage", "Account successfully created");
		response.getWriter().write(json.toString());
		return;
	}	

	
	private void checkUserCredential(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, AddressException, MessagingException {
		String userName = "",  password = "";
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		if(request.getParameter("username") != null) {
			userName = request.getParameter("username");
		}
		if(request.getParameter("password") != null) {
			password = request.getParameter("password");
		}
		if(userName.equals("") || password.equals("")){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameters");
			response.getWriter().write(json.toString());
			return;
		}
		String result = UserDAO.authenticate(userName, password);
		if(!result.equals("ok")){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", result);
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		json.addProperty("successMessage", "hey you are logged in");
		HttpSession session = request.getSession();
		session.setAttribute("userName",userName);
		response.getWriter().write(json.toString());
		return;
	}
	private void resetUserPassword(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, AddressException, MessagingException {
		String userName = "",  email = "";
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		if(request.getParameter("username") != null) {
			userName = request.getParameter("username");
		}
		if(request.getParameter("email") != null) {
			email = request.getParameter("email");
		}
		if(userName.equals("") || email.equals("")){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameters");
			response.getWriter().write(json.toString());
			return;
		}
		if(!UserDAO.checkUserEmail(userName, email)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Email address not correct");
			response.getWriter().write(json.toString());
			return;
		}
		UserDAO userDAO = new UserDAO();
		if(!userDAO.resetUserPassword(userName, email)) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to reset your password");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		response.getWriter().write(json.toString());
		return;
	}
}
