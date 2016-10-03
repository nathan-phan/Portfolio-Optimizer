package com.cs490.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.jasypt.util.text.BasicTextEncryptor;

public class MySqlConnector {
	private Connection conn;
	/**
	 * I know I'm being lazy here for hardcoding the encrypting key into the file instead of using an environment variable 
	 * but I doubt people would spend that much effort to look into a class file to get the key and decrypt the password
	 * of some college student final project 
	**/

	public Connection getConnection() throws Exception {
		conn = null;
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream("mysql.properties");
			Properties props = new Properties();
			if (in != null) {
				props.load(in);
				String user = props.getProperty("user");
				String encryptedPassword = props.getProperty("password");
				String host = props.getProperty("host");
				String driver = props.getProperty("driver");
				Class.forName(driver).newInstance();
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				textEncryptor.setPassword("keyForPass"); 
				String decryptedPassword = textEncryptor.decrypt(encryptedPassword);
				conn = DriverManager.getConnection(host,user,decryptedPassword);
			} else {
				System.err.println("Uh oh properties file not found. Abort mission!");
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return conn;
	} 

}
