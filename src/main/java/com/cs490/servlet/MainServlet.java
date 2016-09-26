package com.cs490.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs490.dao.UserDAO;
import com.cs490.vo.UserVO;

@WebServlet(name="TestServlet", displayName="TestServlet", urlPatterns= {
		"/webapps7/userview"
})
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 389807010932642772L;
	@Override																	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getRequestURI().contains("/userview")){
			try {
				displayUserInfo(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void displayUserInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String userName = "";
		if(request.getParameter("username") != null){
			userName = request.getParameter("username");
		}
		UserVO user = UserDAO.getUserInfo(userName);
		
		request.setAttribute("user", user);
		request.getRequestDispatcher("/UserIndex.jsp").forward(request, response);
	}
}
