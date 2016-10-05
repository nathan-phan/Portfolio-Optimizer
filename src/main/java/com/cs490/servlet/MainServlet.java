package com.cs490.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasypt.util.text.BasicTextEncryptor;

import com.cs490.dao.PortfolioDAO;
import com.cs490.dao.UserDAO;
import com.cs490.vo.PortfolioVO;
import com.google.gson.JsonObject;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@WebServlet(name="MainServlet", displayName="MainServlet", urlPatterns= {
		"/webapps7/stock", "/webapps7/login", "/webapps7/register", "/webapps7/forgotpass",
		"/webapps7/authenticate", "/webapps7/index", "/webapps7/portfolio/add",
		"/webapps7/portfolio/view"
})
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 389807010932642772L;

	@Override																	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		if(request.getRequestURI().equals("/webapps7/login")){
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
		if(request.getRequestURI().contains("/index")){
			try {
				displayMainScreen(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/portfolio/view")){
			try {
				displayPortfolio(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
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

		if(request.getRequestURI().contains("/authenticate")){
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

		if(request.getRequestURI().contains("/portfolio/add")){
			try {
				addPortfolio(request, response);
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

	private void displayMainScreen (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException{
		HttpSession session = request.getSession();
		if(session.getAttribute("userName") == null) {
			request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
			return;
		}
		String userName = (String) session.getAttribute("userName");
		ArrayList<PortfolioVO> ports = UserDAO.findUserPorfolio(userName);
		request.setAttribute("ports",ports);
		request.getRequestDispatcher("/UserIndex.jsp").forward(request, response);
	}

	private void addPortfolio(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
		String userName = "", portfolioName = "";
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		HttpSession session = request.getSession();
		if(session.getAttribute("userName") == null) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to retrieve username");
			response.getWriter().write(json.toString());
			return;
		}
		userName = (String) session.getAttribute("userName");
		if(request.getParameter("portfolioName") != null) {
			portfolioName = request.getParameter("portfolioName");
		}
		if(PortfolioDAO.checkDuplicatePortfolioName(portfolioName, userName)) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "There is already another portfolio with the same name for this account");
			response.getWriter().write(json.toString());
			return;
		}

		if(!PortfolioDAO.addPortfolio(portfolioName, userName)) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to add portfolio into database");
			response.getWriter().write(json.toString());
			return;
		}		
		json.addProperty("status", "success");
		json.addProperty("successMessage", "Portfolio created");
		response.getWriter().write(json.toString());
		return;
	}

	private void displayPortfolio(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException{
		int portfolioId = -1;
		if(request.getParameter("id") != null) {
			portfolioId = Integer.parseInt(request.getParameter("id"));
		}
		PortfolioVO portfolio = PortfolioDAO.getPortfolioById(portfolioId);
		request.setAttribute("portfolio", portfolio);
		request.getRequestDispatcher("/PortfolioView.jsp").forward(request, response);
	}
}
