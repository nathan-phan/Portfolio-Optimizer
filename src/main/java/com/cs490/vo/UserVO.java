package com.cs490.vo;

import java.util.ArrayList;

public class UserVO {
	private String userName;
	private String email;
	private ArrayList<PortfolioVO> userPortfolios;
	
	public UserVO() {
		userName = "";
		email = "";
		userPortfolios = new ArrayList<PortfolioVO>();
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<PortfolioVO> getUserPortfolios() {
		return userPortfolios;
	}

	public void setUserPortfolios(ArrayList<PortfolioVO> userPortfolios) {
		this.userPortfolios = userPortfolios;
	}
	
}
