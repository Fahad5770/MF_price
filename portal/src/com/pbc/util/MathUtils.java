package com.pbc.util;

public class MathUtils {
	
	public static boolean checkZeroAfterDecimal(double value) {
		 if (Double.toString(value).contains(".")) {
             int decimalIndex = Double.toString(value).indexOf(".");
             String fractionalPart = Double.toString(value).substring(decimalIndex + 1);
            return fractionalPart.equals("0");
         }
		 return false;
	}

}
