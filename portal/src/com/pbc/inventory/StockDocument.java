package com.pbc.inventory;
import java.util.ArrayList;
import java.util.Date;

public class StockDocument {
	public long DISTRIBUTOR_ID;
	public int DOCUMENT_TYPE_ID;
	public long DOCUMENT_ID;
	public Date CREATED_ON;
	public long CREATED_BY;
	
	public ArrayList<StockDocumentItems> PRODUCTS = new ArrayList<StockDocumentItems>();
	
	public StockDocumentItems[] getProducts(){
		return PRODUCTS.toArray(new StockDocumentItems[PRODUCTS.size()]);
	}
	
}
