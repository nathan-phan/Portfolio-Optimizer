package com.cs490.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs490.dao.TestDAO;

@WebServlet(name="TestServlet", displayName="TestServlet", urlPatterns= {
		"/test"
})
public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7686373814180368388L;

	@Override																	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ArrayList<String> strings = TestDAO.testString();
			request.setAttribute("strings", strings);
			request.getRequestDispatcher("/WEB-INF/Index.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
