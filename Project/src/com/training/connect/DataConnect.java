package com.training.connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnect {
	private static Connection con;
	
	public static Connection getConnect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/OnlineStore","root","password123");
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return con;
	}
	public static void main(String[] args) {
		DataConnect c = new DataConnect();
		getConnect();
	}
}
