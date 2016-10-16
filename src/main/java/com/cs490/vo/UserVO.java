package com.cs490.vo;

import java.util.ArrayList;

public class UserVO {
	private String userName;
	private String email;
	private String encryptedPassword;
	private String plainPassword;
	private ArrayList<PortfolioVO> userPortfolios;
	
	public UserVO() {
		userName = "";
		email = "";
		encryptedPassword = "";
		plainPassword = "";
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

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
	
}
