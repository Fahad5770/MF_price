package com.pbc.util;

import java.io.File;
import java.util.Date;

public class AlmoizPathUtils {

	final static String BasicFolder = "/home/ftpshared/";
	final static String OrderImages = "OrderImages";
	final static String NoOrderImages = "NoOrderImages";
	final static String OutletImages = "OutletImages";
	final static String CommonFiles = "CommonFiles";
	final static String MerchandiserImages = "MerchandiserImages";
	
	/**
	 * To get path for images stored against outlets
	 * 
	 * @return
	 */
	public static String getMerchandiserImagePath(int year, int month, long outletId) {
		AlmoizPathUtils.mkDIrsForYearAndMonthsWithOutlet(MerchandiserImages, year, month, outletId);
		return BasicFolder + MerchandiserImages + "/" + year + "/" + month + "/" + outletId;
	}
	
	/**
	 * To get path for Common Files
	 * 
	 * @return
	 */
	public static String getLocaSystemPath() {
		return "D:\\Lowa\\CommonFiles";
	}
	
	/**
	 * To get path for images stored against outlets
	 * 
	 * @return
	 */
	public static String getOutletImagePath(int year, int month) {
		AlmoizPathUtils.mkDIrsForYearAndMonths(OutletImages, year, month);
		return BasicFolder + OutletImages + "/" + year + "/" + month;
	}

	/**
	 * To get path for images stored against orders
	 * 
	 * @return
	 */
	public static String getOrderImagesPath(int year, int month) {
		AlmoizPathUtils.mkDIrsForYearAndMonths(OrderImages, year, month);
		return BasicFolder + OrderImages + "/" + year + "/" + month;
	}

	/**
	 * To get path for images stored against No orders
	 * 
	 * @return
	 */
	public static String getNoOrderImagesPath(int year, int month) {
		AlmoizPathUtils.mkDIrsForYearAndMonths(NoOrderImages, year, month);
		return BasicFolder + NoOrderImages + "/" + year + "/" + month;
	}
	
	/**
	 * To get path for Common Files
	 * 
	 * @return
	 */
	public static String getCommonPath(Date date) {
		int year = AlmoizDateUtils.getYearByDate(date);
		int month = AlmoizDateUtils.getMonthNumberByDate(date);
		AlmoizPathUtils.mkDIrsForYearAndMonths(CommonFiles, year, month);
		return BasicFolder + CommonFiles + "/" + year + "/" + month;
	}
	
	public static void mkDIrsForYearAndMonthsWithOutlet(String mainFoler, int year, int month, long outletId) {

		String yearPath = BasicFolder + mainFoler + "/" + year; // Specify the path to the folder you want to check and
		//System.out.println(yearPath);
		File yearFolder = new File(yearPath);

		if (!yearFolder.exists() && !yearFolder.isDirectory()) {
			yearFolder.mkdirs();
		}

		String monthPath = BasicFolder + mainFoler + "/" + year + "/" + month; // Specify the path to the folder you
		//System.out.println(monthPath);
		File monthFolder = new File(monthPath);

		if (!monthFolder.exists() && !monthFolder.isDirectory()) {
			monthFolder.mkdirs();
		}
		
		String outletPath = BasicFolder + mainFoler + "/" + year + "/" + month + "/" + outletId; // Specify the path to the folder you
		//System.out.println(monthPath);
		File outletFolder = new File(outletPath);

		if (!outletFolder.exists() && !outletFolder.isDirectory()) {
			outletFolder.mkdirs();
		}

	}

	public static void mkDIrsForYearAndMonths(String mainFoler, int year, int month) {

		String yearPath = BasicFolder + mainFoler + "/" + year; // Specify the path to the folder you want to check and
		//System.out.println(yearPath);
		File yearFolder = new File(yearPath);

		if (!yearFolder.exists() && !yearFolder.isDirectory()) {
			yearFolder.mkdirs();
		}

		String monthPath = BasicFolder + mainFoler + "/" + year + "/" + month; // Specify the path to the folder you
		//System.out.println(monthPath);
		File monthFolder = new File(monthPath);

		if (!monthFolder.exists() && !monthFolder.isDirectory()) {
			monthFolder.mkdirs();
		}

	}

}
