package com.training.dao;

import com.training.connect.DataConnect;
import com.training.pojo.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
public class ProductDao {
	private Scanner scan;
	private Map<Integer,Product> product;
	private double totalamount;
	private Connection con;
	private PreparedStatement pstmt;
	public ProductDao() {
		scan = new Scanner(System.in);
		product = new HashMap<>();
		con = DataConnect.getConnect();
	}
	
	public void insertProduct() throws SQLException {
		System.out.println("enter how many products you want to insert");
		int noofproducts = scan.nextInt();
		pstmt = con.prepareStatement("insert into product values(?,?,?,?,?,?)");
		for(int i=1;i<=noofproducts;i++) {
			System.out.println("Enter product id");
			pstmt.setInt(1, scan.nextInt());
			System.out.println("Enter product name");
			pstmt.setString(2, scan.next());
			System.out.println("Enter buying price");
			double bprice = scan.nextDouble();
			double sprice = ((bprice*0.5)+bprice);
			pstmt.setDouble(3, sprice);
			System.out.println("Enter available quantity");
			pstmt.setInt(4, scan.nextInt());
			System.out.println("Enter category");
			pstmt.setString(5, scan.next());
			System.out.println("Enter buying price");
			pstmt.setDouble(6,bprice);
			pstmt.addBatch();
		}
		int[] res = pstmt.executeBatch();
		if(res[0]>0) {
			System.out.println("Product added successfully");
		}
	}
	public void display() throws SQLException {
		pstmt = con.prepareStatement("select * from product");
		ResultSet res = pstmt.executeQuery();
		while(res.next()) {
			System.out.println("product id         : "+res.getInt(1));
			System.out.println("product name       : "+res.getString(2));
			System.out.println("Available Quantity : "+res.getInt(4));
			System.out.println("product category   : "+res.getString(5));
			System.out.println("product price      : "+res.getDouble(3));
			System.out.println();
		}
//		Set<Map.Entry<Integer,Product>> productmap = product.entrySet();
//		productmap.stream().forEach(p->
//		{
//			System.out.println("product id : "+p.getValue().getProductid());
//			System.out.println("Product name : "+p.getValue().getProductName());
//			System.out.println("Product Category : "+p.getValue().getCategory());
//			System.out.println("Available Quanity : "+p.getValue().getAvailableQuantity());
//			System.out.println("Product price : "+p.getValue().getSellingPrice());
//			System.out.println();
//		});
	}
	public void serachByProductId() throws SQLException {
		System.out.println("Enter Product id");
		int id = scan.nextInt();
		pstmt = con.prepareStatement("select *  from product where productid = ?");
		pstmt.setInt(1, id);
		ResultSet res = pstmt.executeQuery();
		while(res.next()) {
			System.out.println("product id : "+res.getInt(1));
			System.out.println("product name : "+res.getString(2));
			System.out.println("Available Quantity : "+res.getInt(4));
			System.out.println("product category : "+res.getString(5));
			System.out.println("product price : "+res.getDouble(3));
			System.out.println();
		}
		
//		Set<Map.Entry<Integer,Product>> productmap = product.entrySet();
//		productmap.stream().filter(p->p.getValue().getProductid()==id).forEach(res->
//		{
//			System.out.println("Product name : "+res.getValue().getProductName());
//			System.out.println("Product Category : "+res.getValue().getCategory());
//			System.out.println("Available Quanity : "+res.getValue().getAvailableQuantity());
//			System.out.println("Product price : "+res.getValue().getSellingPrice());
//		});
	}
	public void serachByCategory() throws SQLException {
		System.out.println("Enter Category");
		String c = scan.next();
		pstmt = con.prepareStatement("select *  from product where Category = ?");
		pstmt.setString(1, c);
		ResultSet res = pstmt.executeQuery();
		while(res.next()) {
			System.out.println("product name : "+res.getString(2));
			System.out.println("Available Quantity : "+res.getInt(4));
			System.out.println("product category : "+res.getString(5));
			System.out.println("product price : "+res.getDouble(3));
			System.out.println();
		}
		
//		Set<Map.Entry<Integer,Product>> productmap = product.entrySet();
//		productmap.stream().filter(p->p.getValue().getCategory().equals(c)).forEach(res->
//		{
//			System.out.println("Product name : "+res.getValue().getProductName());
//			System.out.println("Product Category : "+res.getValue().getCategory());
//			System.out.println("Available Quanity : "+res.getValue().getAvailableQuantity());
//			System.out.println("Product price : "+res.getValue().getSellingPrice());
//		});
	}
	public void serachByName() throws SQLException {
		System.out.println("Enter Product name");
		String name = scan.next();
		pstmt = con.prepareStatement("select *  from product where productName = ?");
		pstmt.setString(1, name);
		ResultSet res = pstmt.executeQuery();
		while(res.next()) {
			System.out.println("product name : "+res.getString(2));
			System.out.println("Available Quantity : "+res.getInt(4));
			System.out.println("product category : "+res.getString(5));
			System.out.println("product price : "+res.getDouble(3));
			System.out.println();
		}
		
//		Set<Map.Entry<Integer,Product>> productmap = product.entrySet();
//		productmap.stream().filter(p->p.getValue().getProductName().equals(name)).forEach(res->
//		{
//			System.out.println("Product name : "+res.getValue().getProductName());
//			System.out.println("Product Category : "+res.getValue().getCategory());
//			System.out.println("Available Quanity : "+res.getValue().getAvailableQuantity());
//			System.out.println("Product price : "+res.getValue().getSellingPrice());
//		});
	}
	public void totalAmount() throws SQLException {
		pstmt = con.prepareStatement("select sum(buyingPrice) from product");
		ResultSet res = pstmt.executeQuery();
		if(res.next()) {
			System.out.println("Total amount spend on products "+res.getInt(1));
		}
//		Set<Map.Entry<Integer,Product>> productmap = product.entrySet();
//		productmap.stream().map(n->n.getValue().getBuyingPrice()).forEach(r->
//		{
//			totalamount= totalamount+r;
//		});
//		System.out.println("Total amount spend on products "+totalamount);
	}
	public void profitAmount() throws SQLException {
		pstmt = con.prepareStatement("select Category,sum(sellingPrice)-sum(buyingPrice) as profit from product group by Category");
		ResultSet res = pstmt.executeQuery();
		while(res.next()) {
			System.out.println("profit on "+res.getString(1)+" is "+res.getDouble(2));
		}
//		Set<Map.Entry<Integer,Product>> productmap = product.entrySet();
//		productmap.stream().forEach(res->
//		{
//			double profit = res.getValue().getSellingPrice() - res.getValue().getBuyingPrice();
//			System.out.println("Profit on "+res.getValue().getCategory()+" is "+profit);
//		});
	}
	
}
