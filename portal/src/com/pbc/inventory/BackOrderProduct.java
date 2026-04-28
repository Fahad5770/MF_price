package com.pbc.inventory;

import java.util.ArrayList;

public class BackOrderProduct {
	
	public long ORDER_ID;
	
	public int PRODUCT_ID;
	public int IS_PROMOTION;
	public int PROMOTION_ID;
	
	public long UNITS_AVAILABLE;
	public long UNITS_ORDERED;
	public long UNITS_INVOICED;
	
	public long UNITS_REQUIRED;
	public long UNITS_SHORT;	
	public long UNITS_SHORT_THIS_ORDER;
}

