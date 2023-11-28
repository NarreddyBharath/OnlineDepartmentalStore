package com.training.menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.training.connect.DataConnect;
import com.training.service.UserService;
public class UserMenu {
	private Scanner scan;
	private PreparedStatement pstmt;
	private Connection con;
	String choice ="y";
	UserService us;
	int ch;
	public UserMenu() {
		scan = new Scanner(System.in);
		us = new UserService();
		con=DataConnect.getConnect();
	}
	public void menu() throws SQLException {
		while(choice.equals("y")) {
			System.out.println("1.Login");
			System.out.println("2.Register");
			System.out.println("3.Exit");
			System.out.println("Enter your choice");
			ch=scan.nextInt();
			switch(ch) {
			case 1:
				System.out.println("Enter username");
				String username=scan.next();
				System.out.println("Enter password");
				String pass=scan.next();
				pstmt = con.prepareStatement("select * from user u where u.username=? and u.password=?");
				pstmt.setString(1, username);
				pstmt.setString(2, pass);
				ResultSet res = pstmt.executeQuery();
				if(res.next()) {
						if(res.getString(3).equals("Customer")) {
								us.valid(username,pass);
							}
						else {
							us.validadmin();
						}
				}
				else{
					System.out.println("Incorrect username or password! Please enter valid uername or password");
				}
				break;
			case 2:
				us.acceptuser();
				break;
			case 3:
				System.exit(0);
			}
			System.out.println("Do you want to continue(y/n)");
			choice=scan.next();
		}
		
		
	}
}
