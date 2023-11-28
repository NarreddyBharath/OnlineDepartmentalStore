package com.training.pojo;

public class Purchase {
	private String productName;
	private int quantity;
	private double priceperunit;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPriceperunit() {
		return priceperunit;
	}
	public void setPriceperunit(double priceperunit) {
		this.priceperunit = priceperunit;
	}
	
}
