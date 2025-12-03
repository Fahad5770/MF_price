package com.pbc.util;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DateUtils {
	
	/**
	 * @params String val
	 * @Returns Date in format yyyy/MM/dd
	 * 
	 **/

	
	public static Date parseDateYYYYMMDDWithBackSlash(String val) {

		Date d = null;

		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

	}


	    public static long getTimeDifference(Date startTime, Date endTime) {
	    	
	        long startMillis = startTime.getTime();
	        long endMillis = endTime.getTime();
	        long timeDifferenceMillis = endMillis - startMillis;
	        System.out.println("Time difference in milliseconds: " + timeDifferenceMillis);
	        return endMillis - startMillis;
	    
	}
}
