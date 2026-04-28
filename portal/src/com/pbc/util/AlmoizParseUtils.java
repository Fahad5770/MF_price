package com.pbc.util;

public class AlmoizParseUtils {
	
	/**
	 * To convert string to double
	 * 
	 * @param val
	 * @return
	 */
	public static double parseDouble(String val) {

		if (val == null) {
			val = "0";
		}

		double ret = 0;

		try {
			ret = Double.parseDouble(val);
		} catch (NumberFormatException e) {
		}

		return ret;
	}
	
	/**
	 * To convert string to long
	 * 
	 * @param val
	 * @return
	 */
	public static long parseLong(String val) {

		if (val == null) {
			val = "0";
		}

		long ret = 0;

		try {
			ret = Long.parseLong(val);
		} catch (NumberFormatException e) {
		}

		return ret;
	}
	

	/**
	 * To convert string to int
	 * 
	 * @param val
	 * @return
	 */
	public static int parseInt(String val) {

		if (val == null) {
			val = "0";
		}

		int ret = 0;

		try {
			ret = Integer.parseInt(val);
		} catch (NumberFormatException e) {
		}

		return ret;
	}

}
