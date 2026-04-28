package com.mf.utils;

import java.io.File;
import java.util.Date;

public class MFPathUtils {

	final static String BasicFolder = "/home/ftpshared/Files/";
	final static String CommonFiles = "CommonFiles";

	/**
	 * To get path for images stored against Merchant
	 * 
	 * @return
	 */
	public static String getFilePathFirstLevel(String filePath, int year, int month, String firstLevel) {
		MFPathUtils.mkDIrsForYearAndMonthsWithfirstLevel(filePath, year, month, firstLevel);
		return BasicFolder + filePath + "/" + year + "/" + month + "/" + firstLevel;
	}

	/**
	 * To get path for images stored against Merchant
	 * 
	 * @return
	 */
	public static String getFilePath(String filePath, int year, int month) {
		MFPathUtils.mkDIrsForYearAndMonths(filePath, year, month);
		return BasicFolder + filePath + "/" + year + "/" + month;
	}

	public static String getFilePathWithDay(String filePath, int year, int month, int day) {
		MFPathUtils.mkDIrsForYearMonthsAndDay(filePath, year, month, day);
		return BasicFolder + filePath + "/" + year + "/" + month + "/" + (day + "-" + month + "-" + year);
	}

	/**
	 * To get path for Common Files
	 * 
	 * @return
	 */
	public static String getLocaSystemPath() {
		// return "D:\\Lowa\\CommonFiles";
		return "D:\\Lowa\\CommonFiles";
	}

	/**
	 * To get path for Common Files
	 * 
	 * @return
	 */
	public static String getCommonPath(Date date) {
		int year = MFDateUtils.getYearByDate(date);
		int month = MFDateUtils.getMonthNumberByDate(date);
		MFPathUtils.mkDIrsForYearAndMonths(CommonFiles, year, month);
		return BasicFolder + CommonFiles + "/" + year + "/" + month;
	}

	public static void mkDIrsForYearAndMonthsWithfirstLevel(String mainFoler, int year, int month, String firstLevel) {

		MFPathUtils.mkDIrsForYearAndMonths(mainFoler, year, month);

		String firstLevelPath = BasicFolder + mainFoler + "/" + year + "/" + month + "/" + firstLevel;

		File firstLevelFolder = new File(firstLevelPath);

		if (!firstLevelFolder.exists() && !firstLevelFolder.isDirectory()) {
			firstLevelFolder.mkdirs();
		}

	}

	public static void mkDIrsForYearMonthsAndDay(String mainFoler, int year, int month, int day) {

		MFPathUtils.mkDIrsForYearAndMonths(mainFoler, year, month);

		String DayPath = BasicFolder + mainFoler + "/" + year + "/" + month + "/" + (day + "-" + month + "-" + year);

		File DayFolder = new File(DayPath);

		if (!DayFolder.exists() && !DayFolder.isDirectory()) {
			DayFolder.mkdirs();
		}

	}

	public static void mkDIrsForYearAndMonths(String mainFoler, int year, int month) {

		String filePath = BasicFolder + mainFoler; // Specify the path to the folder you want to check and

		File fileFolder = new File(filePath);

		if (!fileFolder.exists() && !fileFolder.isDirectory()) {
			fileFolder.mkdirs();
		}

		String yearPath = BasicFolder + mainFoler + "/" + year; // Specify the path to the folder you want to check and
		// System.out.println(yearPath);
		File yearFolder = new File(yearPath);

		if (!yearFolder.exists() && !yearFolder.isDirectory()) {
			yearFolder.mkdirs();
		}

		String monthPath = BasicFolder + mainFoler + "/" + year + "/" + month; // Specify the path to the folder you
		// System.out.println(monthPath);
		File monthFolder = new File(monthPath);

		if (!monthFolder.exists() && !monthFolder.isDirectory()) {
			monthFolder.mkdirs();
		}

	}
	

}
