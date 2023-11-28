package com.training.dao;

import com.training.connect.DataConnect;
import com.training.pojo.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.*;
public class UserValidation {
	private Connection con;
	private PreparedStatement pstmt;
	private Scanner scan;
	private ProductDao pdao;
	int final_discount;
	String supercoins;
	private List<Purchase> purchases;
	
	public UserValidation() {
		con = DataConnect.getConnect();
		scan = new Scanner(System.in);
		pdao = new ProductDao();
		purchases= new ArrayList<Purchase>();
	}
	public void valid(String name, String pin) throws SQLException {
		double totalPrice=0;
		pstmt = con.prepareStatement("select * from user where username = ? and password = ?");
		pstmt.setString(1, name);
		pstmt.setString(2, pin);
		ResultSet res = pstmt.executeQuery();
		if(res.next()) {
			System.out.println("Welcome "+res.getString(1));
			System.out.println("Login Successful");
			String choice = "n";
			
			while(choice.equals("n")) {
				System.out.println("Available products in the store : ");
				pdao.display();
				System.out.println("1. Filter product based on category");
				System.out.println("2. Filter product based on price");
				System.out.println("Enter your choice");
				int ch = scan.nextInt();
				switch(ch) {
				case 1:
					System.out.println("Enter Category");
					String c = scan.next();
					pstmt = con.prepareStatement("select *  from product where Category = ?");
					pstmt.setString(1, c);
					ResultSet res1 = pstmt.executeQuery();
					
					while(res1.next()) {
						System.out.println("product name : "+res1.getString(2));
						System.out.println("Available Quantity : "+res1.getInt(4));
						System.out.println("product category : "+res1.getString(5));
						System.out.println("product price : "+res1.getDouble(3));
						System.out.println();
					}
						System.out.println("Enter product name to purchase");
						String pname = scan.next();
						System.out.println("enter how many products you want to purchase");
						int noofprod = scan.nextInt();
						PreparedStatement pstmt131 = con.prepareStatement("select p.availableQuantity from product p where productName=?");
						pstmt131.setString(1, pname);
						ResultSet res3 = pstmt131.executeQuery();
						res3.next();
						if(res3.getInt(1)>=noofprod) {
							pstmt = con.prepareStatement("update product set availableQuantity=availableQuantity-? where productname=?");
							pstmt.setInt(1, noofprod);
							pstmt.setString(2, pname);
							int res11 = pstmt.executeUpdate();
							PreparedStatement pstmt1 = con.prepareStatement("select * from product where productName=?");
							pstmt1.setString(1, pname);
							ResultSet res12 = pstmt1.executeQuery();
							if(res12.next()) {
								PreparedStatement pstmt3 = con.prepareStatement("insert into purchase values(?,?,?,?)");
								pstmt3.setString(1, name);
								pstmt3.setString(2, pname);
								pstmt3.setInt(3,noofprod );
								pstmt3.setDouble(4, res12.getDouble(3)*noofprod);	
								int res13 = pstmt3.executeUpdate();
								Purchase purchase = new Purchase();
								purchase.setProductName(pname);
								purchase.setQuantity(noofprod);
								double priceperunit=res12.getDouble(3);
								purchase.setPriceperunit(priceperunit);
								purchases.add(purchase);
								System.out.println("product add to cart");
								totalPrice+=res12.getDouble(3)*noofprod;
								
						    }
							else {
								System.out.println("PLease enter valid product name");
							}
						}
						else{
							System.out.println("insufficient availavle quantity. Available Quantity : "+res3.getInt(1));
						}	
					break;
				case 2:
					Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,0);
					ResultSet res2 = stmt.executeQuery("select * from product order by sellingPrice");
					while(res2.next()) {
						System.out.println("product id : "+res2.getInt(1));
						System.out.println("Product name : "+res2.getString(2));
						System.out.println("Product Category : "+res2.getString(5));
						System.out.println("Available Quanity : "+res2.getInt(4));
						System.out.println("Product price : "+res2.getDouble(3));
						System.out.println();
					}
				
					System.out.println("Enter product name to purchase");
					String pname1 = scan.next();
					System.out.println("enter how many products you want to purchase");
					int noofprod1 = scan.nextInt();
					res2.beforeFirst();					
					boolean productFound = false;
					
					while(res2.next()) {
						if(res2.getString(2).equals(pname1)) {
							productFound=true;
							if(res2.getInt(4)>=noofprod1) {
								pstmt = con.prepareStatement("update product set availableQuantity=availableQuantity-? where productname=?");
								pstmt.setInt(1, noofprod1);
								pstmt.setString(2, pname1);
								int res111 = pstmt.executeUpdate();
								PreparedStatement pstmt12 = con.prepareStatement("select * from product where productname=?");
								pstmt12.setString(1, pname1);
								ResultSet res121 = pstmt12.executeQuery();
								if(res121.next()) {
								PreparedStatement pstmt13 = con.prepareStatement("insert into purchase values(?,?,?,?)");
								pstmt13.setString(1, name);
								pstmt13.setString(2, pname1);
								pstmt13.setInt(3,noofprod1 );
								pstmt13.setDouble(4, res121.getDouble(3)*noofprod1);
								int res131 = pstmt13.executeUpdate();
								Purchase purchase = new Purchase();
								purchase.setProductName(pname1);
								purchase.setQuantity(noofprod1);
								double priceperunit=res121.getDouble(3);
								purchase.setPriceperunit(priceperunit);
								purchases.add(purchase);
								System.out.println("product add to cart");
								totalPrice+=res121.getDouble(3)*noofprod1;
								break;
							}
							else {
								System.out.println("PLease enter valid product name");
							}
							}		
							else {
								System.out.println("Insufficient quantity of that product, Available quantity : "+res2.getInt(4));
								break;
							}
						}
					}
					if(!productFound) {
						System.out.println("No such product with name : "+pname1);						
					}
					
				}
				System.out.println("Are you done with your shopping (y/n))");
				choice = scan.next();
				}
				System.out.println("Do you want to use supercoins(100 coins=5rupees) (y/n)");
				String choice1=scan.next();
				if(choice1.equals("y")) {
					if(res.getInt(5)>0) {
						System.out.println("...........");
						int discount=res.getInt(5)/100;
						final_discount=discount*5;
						PreparedStatement pstmt5 = con.prepareStatement("update user set superCoins=superCoins=? where username=?");
						pstmt5.setInt(1, discount*100);
						pstmt5.setString(2, name);
						int res1 = pstmt.executeUpdate();
						
						System.out.println("after using superCoins. available superCoins : "+(res.getInt(5)-(discount*100)));
						totalPrice-=final_discount;
						this.displayFinalBill();
						System.out.println("after using super coins");
						System.out.println("Final bill : "+totalPrice);
					}
					else {
						System.out.println("insufficient superCoins. Available superCoins : "+res.getInt(5));
						this.displayFinalBill();
						System.out.println("Final bill : "+totalPrice);
					}
				}
				else{
					this.displayFinalBill();
						System.out.println("Final bill : "+totalPrice);
					}
		}
		else {
			System.out.println("Incorrect Username or password!Please login again");
		}
	}
	
	public void displayFinalBill() {
		System.out.println("--------------------------------------");
		System.out.println("|          Purchase Details          |");
		System.out.println("--------------------------------------");
		System.out.println("|product Name|price per unit|quantity|");
		System.out.println("--------------------------------------");
		for(Purchase p:purchases) {
			System.out.println("|  "+p.getProductName()+"    |   "+p.getPriceperunit()+"    |    "+p.getQuantity()+"      |");
			System.out.println("|------------------------------------|");
		}
		
		
		
	}

	public void register() throws SQLException {
		PreparedStatement pstmt1 = con.prepareStatement("insert into user values(?,?,?,?,?)");
		System.out.println("enter user name");
		pstmt1.setString(1, scan.next());
		System.out.println("Enter user password");
		pstmt1.setString(2, scan.next());
		pstmt1.setString(3, "Customer");
		System.out.println("Enter mail id");
		pstmt1.setString(4, scan.next());
		pstmt1.setInt(5, 100);
		int res1 = pstmt1.executeUpdate();
		if(res1>0) {
			System.out.println("registered successfully.As a welcome bonus you got 100 super coins");
		}
		
		
	}
		
}