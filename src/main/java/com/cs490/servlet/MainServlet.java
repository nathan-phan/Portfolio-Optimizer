package com.cs490.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import com.cs490.dao.PortfolioDAO;
import com.cs490.dao.UserDAO;
import com.cs490.vo.PortfolioVO;
import com.cs490.vo.RecordVO;
import com.cs490.vo.StockVO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@WebServlet(name="MainServlet", displayName="MainServlet", urlPatterns= {
		"/webapps7/stock", "/webapps7/login", "/webapps7/register", "/webapps7/forgotpass",
		"/webapps7/authenticate", "/webapps7/index", "/webapps7/portfolio/add",
		"/webapps7/portfolio/view", "/webapps7/logout",
		"/webapps7/stock/buy", "/webapps7/stock/sell",
		"/webapps7/withdraw","/webapps7/deposit",
		"/webapps7/portfolio/delete", "/webapps7/portfolio/history","webapps7/sheet/test"
})
public class MainServlet extends HttpServlet {
	public static final long serialVersionUID = 389807010932642772L;
	public static final String ALLOWED_STOCKS = "MMM,AXP,AAPL,BA,CAT,CVX,CSCO,KO,DIS,DD,XOM,"
			+ "GE,GS,HD,IBM,INTC,JNJ,JPM,MCD,MRK,MSFT,NKE,PFE,PG,TRV,UTX,"
			+ "UNH,VZ,V,WMT,NSE:ACC,NSE:ADANIPORTS,NSE:AMBUJACEM,NSE:ASIANPAINT,NSE:AXISBANK,"
			+ "NSE:BAJAJ-AUTO,NSE:BANKBARODA,NSE:BHARTIARTL,NSE:BHEL,NSE:BOSCHLTD,NSE:BPCL,NSE:CAIRN,"
			+ "NSE:CIPLA,NSE:COALINDIA,NSE:DRREDDY,NSE:GAIL,NSE:GRASIM,NSE:HCLTECH,NSE:HDFC,NSE:HDFCBANK"
			+ ",NSE:HEROMOTOCO,NSE:HINDALCO,NSE:HINDUNILVR,NSE:ICICIBANK,NSE:NSE:IDEA,NSE:INDUSINDBK,"
			+ "NSE:INFY,NSE:ITC,NSE:KOTAKBANK,NSE:LT,NSE:LUPIN,NSE:M&M,NSE:MARUTI,NSE:NTPC,NSE:ONGC,NSE:PNB,"
			+ "NSE:POWERGRID,NSE:RELIANCE,NSE:SBIN,NSE:SUNPHARMA,NSE:TATAMOTORS,NSE:TATAPOWER,"
			+ "NSE:TATASTEEL,NSE:TCS,NSE:TECHM,NSE:ULTRACEMCO,NSE:VEDL,NSE:WIPRO,NSE:YESBANK,NSE:ZEEL,"
			+ "Z74.SI,S58.SI,U14.SI,D05.SI,O39.SI,U11.SI,"
			+ "F34.SI,BN4.SI,H78.SI,G13.SI,C07.SI,C31.SI,"
			+ "C6L.SI,Y92.SI,MC0.SI,C09.SI,S63.SI,S51.SI,U96.SI,"
			+ "NS8U.SI,E5H.SI,C61U.SI,S68.SI,C38U.SI,A17U.SI,"
			+ "T39.SI,CC3.SI,BS6.SI,S59.SI,C52.SI,EB5.SI,"
			+ "S08.SI,T82U.SI,K71U.SI,N03.SI,JOBS,ATV,ACTS,"
			+ "GRO,AMCN,ACH,ATAI,BIDU,CYOU,CPC,STV,DL,CEA,JRJC,GRRF,LFC"
			+ ",CMM,CMED,CHL,CEO,NPD,SNP,ZNH,CSUN,CNTF,CHA,CHU,CEDU,CISG,"
			+ "CTRP,DGW,EJ,LONG,FMCN,GA,GSH,GU,HMIN,HNP,HRAY,JASO,KONG,"
			+ "LDK,LTON,LFT,MR,NTES,EDU,NINE,NED,PWRD,PTR,SOL,GAME,SNDA,"
			+ "SCR,SHI,SOLF,SPRD,STP,NCTY,TCM,TSL,VIT,VIMC,VISN,WH,WX,XSEL,"
			+ "XIN,YZC,YGE";

	public static final String DOMESTIC_STOCKS = "MMM,AXP,AAPL,BA,CAT,CVX,CSCO,KO,DIS,DD,XOM,"
			+ "GE,GS,HD,IBM,INTC,JNJ,JPM,MCD,MRK,MSFT,NKE,PFE,PG,TRV,UTX,"
			+ "UNH,VZ,V,WMT";

	public static final String NIFTY_STOCKS = "NSE:ACC,NSE:ADANIPORTS,NSE:AMBUJACEM,"
			+ "NSE:ASIANPAINT,NSE:AXISBANK,"
			+ "NSE:BAJAJ-AUTO,NSE:BANKBARODA,NSE:BHARTIARTL,NSE:BHEL,NSE:BOSCHLTD,NSE:BPCL,CAIRN,"
			+ "NSE:CIPLA,NSE:COALINDIA,NSE:DRREDDY,NSE:GAIL,NSE:GRASIM,NSE:HCLTECH,NSE:HDFC,NSE:HDFCBANK"
			+ ",NSE:HEROMOTOCO,NSE:HINDALCO,NSE:HINDUNILVR,NSE:ICICIBANK,NSE:IDEA,NSE:INDUSINDBK,"
			+ "NSE:INFY,NSE:ITC,NSE:KOTAKBANK,NSE:LT,NSE:LUPIN,NSE:M&M,NSE:MARUTI,NSE:NTPC,NSE:ONGC,NSE:PNB,"
			+ "NSE:POWERGRID,NSE:RELIANCE,NSE:SBIN,NSE:SUNPHARMA,NSE:TATAMOTORS,NSE:TATAPOWER,"
			+ "NSE:TATASTEEL,NSE:TCS,NSE:TECHM,NSE:ULTRACEMCO,NSE:VEDL,NSE:WIPRO,NSE:YESBANK,NSE:ZEEL";

	public static final String STRAIT_STOCKS = "Z74.SI,S58.SI,U14.SI,D05.SI,O39.SI,U11.SI,"
			+ "F34.SI,BN4.SI,H78.SI,G13.SI,C07.SI,C31.SI,"
			+ "C6L.SI,Y92.SI,MC0.SI,C09.SI,S63.SI,S51.SI,U96.SI,"
			+ "NS8U.SI,E5H.SI,C61U.SI,S68.SI,C38U.SI,A17U.SI,"
			+ "T39.SI,CC3.SI,BS6.SI,S59.SI,C52.SI,EB5.SI,"
			+ "S08.SI,T82U.SI,K71U.SI,N03.SI";

	public static final String ADR_STOCKS = "JOBS,ATV,ACTS,"
			+ "GRO,AMCN,ACH,ATAI,BIDU,CYOU,CPC,STV,DL,CEA,JRJC,GRRF,LFC"
			+ ",CMM,CMED,CHL,CEO,NPD,SNP,ZNH,CSUN,CNTF,CHA,CHU,CEDU,CISG,"
			+ "CTRP,DGW,EJ,LONG,FMCN,GA,GSH,GU,HMIN,HNP,HRAY,JASO,KONG,"
			+ "LDK,LTON,LFT,MR,NTES,EDU,NINE,NED,PWRD,PTR,SOL,GAME,SNDA,"
			+ "SCR,SHI,SOLF,SPRD,STP,NCTY,TCM,TSL,VIT,VIMC,VISN,WH,WX,XSEL,"
			+ "XIN,YZC,YGE";

	public static final String OVERSEA_STOCKS = "NSE:ACC,NSE:ADANIPORTS,NSE:AMBUJACEM,NSE:ASIANPAINT,NSE:AXISBANK,"
			+ "NSE:BAJAJ-AUTO,NSE:BANKBARODA,NSE:BHARTIARTL,NSE:BHEL,NSE:BOSCHLTD,NSE:BPCL,NSE:CAIRN,"
			+ "NSE:CIPLA,NSE:COALINDIA,NSE:DRREDDY,NSE:GAIL,NSE:GRASIM,NSE:HCLTECH,NSE:HDFC,NSE:HDFCBANK"
			+ ",NSE:HEROMOTOCO,NSE:HINDALCO,NSE:HINDUNILVR,NSE:ICICIBANK,NSE:IDEA,NSE:INDUSINDBK,"
			+ "NSE:INFY,NSE:ITC,NSE:KOTAKBANK,NSE:LT,NSE:LUPIN,NSE:M&M,NSE:MARUTI,NSE:NTPC,NSE:ONGC,NSE:PNB,"
			+ "NSE:POWERGRID,NSE:RELIANCE,NSE:SBIN,NSE:SUNPHARMA,NSE:TATAMOTORS,NSE:TATAPOWER,"
			+ "NSE:TATASTEEL,NSE:TCS,NSE:TECHM,NSE:ULTRACEMCO,NSE:VEDL,NSE:WIPRO,NSE:YESBANK,NSE:ZEEL,"
			+ "Z74.SI,S58.SI,U14.SI,D05.SI,O39.SI,U11.SI,"
			+ "F34.SI,BN4.SI,H78.SI,G13.SI,C07.SI,C31.SI,"
			+ "C6L.SI,Y92.SI,MC0.SI,C09.SI,S63.SI,S51.SI,U96.SI,"
			+ "NS8U.SI,E5H.SI,C61U.SI,S68.SI,C38U.SI,A17U.SI,"
			+ "T39.SI,CC3.SI,BS6.SI,S59.SI,C52.SI,EB5.SI,"
			+ "S08.SI,T82U.SI,K71U.SI,N03.SI,JOBS,ATV,ACTS,"
			+ "GRO,AMCN,ACH,ATAI,BIDU,CYOU,CPC,STV,DL,CEA,JRJC,GRRF,LFC"
			+ ",CMM,CMED,CHL,CEO,NPD,SNP,ZNH,CSUN,CNTF,CHA,CHU,CEDU,CISG,"
			+ "CTRP,DGW,EJ,LONG,FMCN,GA,GSH,GU,HMIN,HNP,HRAY,JASO,KONG,"
			+ "LDK,LTON,LFT,MR,NTES,EDU,NINE,NED,PWRD,PTR,SOL,GAME,SNDA,"
			+ "SCR,SHI,SOLF,SPRD,STP,NCTY,TCM,TSL,VIT,VIMC,VISN,WH,WX,XSEL,"
			+ "XIN,YZC,YGE";

	public static final double INITIAL_USD_INR = 66.722975;

	public static final double INITIAL_USD_SGD = 1.35552;

	public static final String CURRENCY_API_KEY = "da54f57878f7a80edcfce214d7889683";

	@Override																	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		if(request.getRequestURI().contains("/login")){
			try {
				showLoginScreen(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(request.getRequestURI().contains("/stock")){
			if(!checkLoggedIn(request, response)){
				request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
				return;
			}
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
			if(!checkLoggedIn(request, response)){
				request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
				return;
			}
			try {
				displayPortfolio(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/portfolio/history")){
			if(!checkLoggedIn(request, response)){
				request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
				return;
			}
			try {
				getRecords(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/sheet/test")){
			if(!checkLoggedIn(request, response)){
				request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
				return;
			}
			try {
				doSheetTest(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/logout")){
			try {
				logOut(request, response);
			} catch (Exception e) {
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

		if(request.getRequestURI().contains("/deposit")){
			try {
				depositMoney(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/withdraw")){
			try {
				withdrawMoney(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/portfolio/delete")){
			try {
				deletePortfolio(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/stock/buy")){
			try {
				buyStock(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if(request.getRequestURI().contains("/stock/sell")){
			try {
				sellStock(request, response);
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
		LinkedHashMap<String, Double> rates = PortfolioDAO.getExchangeRates();
		String[] straitArray = STRAIT_STOCKS.split(",");
		String[] adrArray = ADR_STOCKS.split(",");
		String[] niftyArray = NIFTY_STOCKS.split(",");
		String[] domesticArray = DOMESTIC_STOCKS.split(",");
		String[] overseaArray = OVERSEA_STOCKS.split(",");
		request.setAttribute("overseas", OVERSEA_STOCKS);
		PortfolioVO portfolio = PortfolioDAO.getPortfolioById(portfolioId);
		LinkedHashMap<StockVO, Integer> stocks = portfolio.getStocks();
		BigDecimal totalStockValue = new BigDecimal(0);
		BigDecimal overseaValue = new BigDecimal(0);
		for(StockVO stock : stocks.keySet()) {
			int shares = stocks.get(stock);
			if(stock.getCurrency().equals("INR")){
				BigDecimal originalPrice = stock.getForeignPrice(); 
				BigDecimal convertedPrice = originalPrice.divide(new BigDecimal(rates.get("USDINR")), 4, RoundingMode.HALF_UP);
				stock.setPrice(convertedPrice);
				BigDecimal prevClose = stock.getForeignPreviousClosingPrice();
				BigDecimal foreignChange = stock.getForeignChange();
				stock.setChange(foreignChange.divide(new BigDecimal(rates.get("USDINR")), 4, RoundingMode.HALF_UP));
				stock.setPreviousClosingPrice(prevClose.divide(new BigDecimal(rates.get("USDINR")), 4, RoundingMode.HALF_UP));
				stock.setPrice(convertedPrice);
				totalStockValue = totalStockValue.add(convertedPrice.multiply(new BigDecimal(shares)));
				overseaValue = overseaValue.add(convertedPrice.multiply(new BigDecimal(shares)));
			} else if(stock.getCurrency().equals("SGD")){
				BigDecimal originalPrice = stock.getForeignPrice(); 
				BigDecimal convertedPrice = originalPrice.divide(new BigDecimal(rates.get("USDSGD")), 4, RoundingMode.HALF_UP);
				stock.setPrice(convertedPrice);
				BigDecimal prevClose = stock.getForeignPreviousClosingPrice();
				stock.setPreviousClosingPrice(prevClose.divide(new BigDecimal(rates.get("USDSGD")), 4, RoundingMode.HALF_UP));
				totalStockValue = totalStockValue.add(convertedPrice.multiply(new BigDecimal(shares)));
				BigDecimal foreignChange = stock.getForeignChange();
				stock.setChange(foreignChange.divide(new BigDecimal(rates.get("USDSGD")), 4, RoundingMode.HALF_UP));
				overseaValue = overseaValue.add(convertedPrice.multiply(new BigDecimal(shares)));
			} else if(Arrays.asList(adrArray).contains(stock.getSymbol())){
				totalStockValue = totalStockValue.add(stock.getPrice().multiply(new BigDecimal(shares)));
				overseaValue = overseaValue.add(stock.getPrice().multiply(new BigDecimal(shares)));
			} else {
				totalStockValue = totalStockValue.add(stock.getPrice().multiply(new BigDecimal(shares)));
			}
		}
		portfolio.setStocks(stocks);
		if(totalStockValue.compareTo(new BigDecimal(0)) == 1){
			BigDecimal overseaPercent = overseaValue.divide(totalStockValue,4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			BigDecimal domesticPercent = new BigDecimal(100).subtract(overseaPercent);
			BigDecimal cashPercent = portfolio.getBalance().divide(totalStockValue.add(portfolio.getBalance()),4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			request.setAttribute("overseaPercent", overseaPercent);
			request.setAttribute("domesticPercent", domesticPercent);
			request.setAttribute("cashPercent", cashPercent);
			request.setAttribute("foreignValue", overseaValue);
			request.setAttribute("size", stocks.size());
		}
		request.setAttribute("totalValue", totalStockValue);
		request.setAttribute("size", stocks.size());
		request.setAttribute("portfolio", portfolio);
		ArrayList<RecordVO> records = PortfolioDAO.getPortfolioRecord(portfolioId);
		request.setAttribute("records", records);
		request.getRequestDispatcher("/PortfolioView.jsp").forward(request, response);
	}

	private void depositMoney(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		BigDecimal amount;
		int id = -1;
		if(request.getParameter("amount") == null || request.getParameter("id") == null ) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameter provided.");
			response.getWriter().write(json.toString());
			return;
		}
		amount = new BigDecimal(request.getParameter("amount"));
		id = Integer.parseInt(request.getParameter("id"));
		if(!PortfolioDAO.addMoneyToPortfolio(id, amount)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to add money to porfolio.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.recordDeposit(id, amount)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to add record to history.");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		response.getWriter().write(json.toString());
		return;
	}

	private void withdrawMoney(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		BigDecimal amount;
		int id = -1;
		if(request.getParameter("amount") == null || request.getParameter("id") == null ) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameter provided.");
			response.getWriter().write(json.toString());
			return;
		}
		amount = new BigDecimal(request.getParameter("amount"));
		id = Integer.parseInt(request.getParameter("id"));
		if(!PortfolioDAO.removeMoneyFromPortfolio(id, amount)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to withdraw money to porfolio.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.recordWithdraw(id, amount)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to add record to history.");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		response.getWriter().write(json.toString());
		return;
	}

	private void getRecords(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		int id = -1;
		if(request.getParameter("id") == null ) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameter provided.");
			response.getWriter().write(json.toString());
			return;
		}
		id = Integer.parseInt(request.getParameter("id"));
		ArrayList<RecordVO> records = PortfolioDAO.getPortfolioRecord(id);
		Gson gson = new Gson();
		String recordJson = gson.toJson(records);
		json.addProperty("status", "success");
		json.addProperty("records", recordJson);
		response.getWriter().write(json.toString());
		return;
	}

	private void deletePortfolio(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		int id = -1;
		if(request.getParameter("id") == null ) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameter provided.");
			response.getWriter().write(json.toString());
			return;
		}
		id = Integer.parseInt(request.getParameter("id"));
		if(!PortfolioDAO.deletePortfolio(id)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to delete Portfolio.");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		response.getWriter().write(json.toString());
		return;
	}

	private void doSheetTest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException{
	}

	private void buyStock(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException{
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		int shares = -1, id = -1;
		String symbol = "";
		if(request.getParameter("symbol") == null || request.getParameter("shares") == null ||
				request.getParameter("id") == null) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameter provided.");
			response.getWriter().write(json.toString());
			return;
		}
		symbol = request.getParameter("symbol");
		try{
			shares = Integer.parseInt(request.getParameter("shares"));
		} catch (Exception e) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Shares value too big.");
			response.getWriter().write(json.toString());
			return;
		}
		id = Integer.parseInt(request.getParameter("id"));
		String[] allowedArray = ALLOWED_STOCKS.split(",");
		if(!Arrays.asList(allowedArray).contains(symbol)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Stock not allowed.");
			response.getWriter().write(json.toString());
			return;
		}
		BigDecimal price;
		boolean firstTime = PortfolioDAO.checkForInitialPurchase(id, symbol);
		if(firstTime){
			price = PortfolioDAO.findInitialStockPrice(symbol);
			price = PortfolioDAO.initialCurrencyConvert(symbol, price);
		} else {
			price = PortfolioDAO.findCurrentStockPrice(symbol);
			price = PortfolioDAO.liveCurrencyConvert(symbol, price);
		}

		BigDecimal totalPrice = price.multiply(new BigDecimal(shares));
		BigDecimal balance =  PortfolioDAO.findPortfolioBalance(id);
		if(totalPrice.compareTo(balance) == 1){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Total price exceeds balance.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.insertStockToPortfolio(id, symbol, shares, firstTime)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to insert stock into databasse.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.removeMoneyFromPortfolio(id, totalPrice.negate())){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to substract money from balance.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.recordStockPurchase(symbol, shares, price, id)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to record transaction.");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		response.getWriter().write(json.toString());
		return;
	}

	private void sellStock(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException{
		JsonObject json = new JsonObject();
		response.setContentType("application/json");
		int shares = -1, id = -1;
		String symbol = "";
		if(request.getParameter("symbol") == null || request.getParameter("shares") == null ||
				request.getParameter("id") == null) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Invalid parameter provided.");
			response.getWriter().write(json.toString());
			return;
		}
		symbol = request.getParameter("symbol");
		try{
			shares = Integer.parseInt(request.getParameter("shares"));
		} catch (Exception e) {
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Shares value too big.");
			response.getWriter().write(json.toString());
			return;
		}
		
		id = Integer.parseInt(request.getParameter("id"));
		BigDecimal price;
		price = PortfolioDAO.findCurrentStockPrice(symbol);
		price = PortfolioDAO.liveCurrencyConvert(symbol, price);

		BigDecimal totalPrice = price.multiply(new BigDecimal(shares));
		BigDecimal balance =  PortfolioDAO.findPortfolioBalance(id);
		if(totalPrice.compareTo(balance) == 1){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Total price exceeds balance.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.removeStocksFromPortfolio(id, symbol, shares)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to remove stocks from portfolio.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.addMoneyToPortfolio(id, totalPrice)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to add money to balance.");
			response.getWriter().write(json.toString());
			return;
		}
		if(!PortfolioDAO.recordStockRemoval(symbol, shares, price, id)){
			json.addProperty("status", "failed");
			json.addProperty("errorMessage", "Failed to record transaction.");
			response.getWriter().write(json.toString());
			return;
		}
		json.addProperty("status", "success");
		response.getWriter().write(json.toString());
		return;
	}

	private void logOut(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException{
		HttpSession session=request.getSession();  
		session.invalidate();  
		request.getRequestDispatcher("/LogIn.jsp").forward(request, response);
	}

	private boolean checkLoggedIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.getAttribute("userName") == null) {
			return false;
		}
		return true;
	}

}
