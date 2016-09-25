package com.cs490.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.cs490.database.MySqlConnector;



public class TestDAO {
	public static ArrayList<String> testString() throws Exception{
		ArrayList<String> strings = new ArrayList<String>();
		Connection connection = new MySqlConnector().getConnection();
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			String query = "select * from example";
			prepStmt = connection.prepareStatement(query);
			rs = prepStmt.executeQuery();
			while(rs.next()){
				String result = rs.getString(1);
				strings.add(result);
			}
			prepStmt.close();
			
		} catch(Exception e){
			e.printStackTrace();
			return null;
			
		} finally {
			connection.close();
		}
		return strings;
	}
}
