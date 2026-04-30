package com.mf.dao;

import java.util.LinkedHashMap;

import com.pbc.util.Utilities;

public class SaleDetailsReport {

	private String product;
	private int orderQuantity;
	private double orderAmount;
	private int saleQuantity;
	private double saleAmount;

//	private ProductOrder productOrder;
//	private ProductSale productSale;

//	public ProductSale getProductSale() {
//		return productSale;
//	}
//
//	public void setProductSale(ProductSale productSale) {
//		this.productSale = productSale;
//	}
//
//	public ProductOrder getProductOrder() {
//		return productOrder;
//	}
//
//	public void setProductOrder(ProductOrder productOrder) {
//		this.productOrder = productOrder;
//	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public int getSaleQuantity() {
		return saleQuantity;
	}

	public void setSaleQuantity(int saleQuantity) {
		this.saleQuantity = saleQuantity;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	// Getters and setters
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("product", this.product);
		dataArray.put("order_quantity", this.orderQuantity);
		dataArray.put("order_amount", Utilities.getDisplayCurrencyFormat(this.orderAmount));
		dataArray.put("sale_quantity", this.saleQuantity);
		dataArray.put("sale_amount", Utilities.getDisplayCurrencyFormat(this.saleAmount));
//		dataArray.put("order_quantity", this.productSale.getQuantuity());
//		dataArray.put("order_amount", Utilities.getDisplayCurrencyFormat(this.productSale.getAmount()));
//		dataArray.put("sale_quantity", this.productSale.getQuantuity());
//		dataArray.put("sale_amount", Utilities.getDisplayCurrencyFormat(this.productSale.getAmount()));
		return dataArray;
	}

//	public class ProductOrder {
//		private int quantuity;
//		private double amount;
//
//		public ProductOrder() {
//
//		}
//
//		public int getQuantuity() {
//			return quantuity;
//		}
//
//		public void setQuantuity(int quantuity) {
//			this.quantuity = quantuity;
//		}
//
//		public double getAmount() {
//			return amount;
//		}
//
//		public void setAmount(double amount) {
//			this.amount = amount;
//		}
//
//	}
//
//	public class ProductSale {
//		private int quantuity;
//		private double amount;
//
//		public ProductSale() {
//
//		}
//
//		public int getQuantuity() {
//			return quantuity;
//		}
//
//		public void setQuantuity(int quantuity) {
//			this.quantuity = quantuity;
//		}
//
//		public double getAmount() {
//			return amount;
//		}
//
//		public void setAmount(double amount) {
//			this.amount = amount;
//		}
//
//	}

}
