package com.pbc.inventory;

import java.util.ArrayList;

public class PromotionItem {
	public long PROMOTION_ID;
	public int PACKAGE_ID;
	public String PACKAGE_LABEL;
	public int UNIT_PER_SKU;
	public ArrayList<Integer> BRANDS = new ArrayList<Integer>();
	public ArrayList<String> BRAND_LABELS = new ArrayList<String>();
	public long TOTAL_UNITS;
}
