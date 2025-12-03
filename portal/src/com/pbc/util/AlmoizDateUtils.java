package com.pbc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class AlmoizDateUtils {
	
	/**
	 * To get date and time as string to embed in MySQL Query
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateTime(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd H:mm");
			return "date_format('" + format.format(val) + "', '%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}
	
	/**
	 * To get time in "h:mm a" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayTimeFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("h:mm a");
			return "" + format.format(val);

		} else {

			return null;

		}

	}
	
	
	/**
	 * To get date in "dd/MM/yyyy" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

	}
	
	/**
	 * To get abbreviated long date in string format for UI displays
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayFullDateFormatShort(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

	}
	
	/**
	 * To get start date of a month
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public static Date getStartDateByMonth(int month, int year) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);

		return c.getTime();
	}
	public static boolean isCurrentMonth(Date val){
		  
		  int imonth = Utilities.getMonthNumberByDate(val);
		  int iyear = Utilities.getYearByDate(val);
		  
		  imonth--;
		  
		  Calendar cal = Calendar.getInstance();
		  int year = cal.get(Calendar.YEAR);
		  int month = cal.get(Calendar.MONTH);
		  
		  if(imonth == month && iyear == year){
		   return true;
		  }
		  
		  return false;
	}
	public static Date getEndDateByMonth(int month, int year) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		return c.getTime();
	}
	public static Date getDateByDays(Date val, int Days){
		return DateUtils.addDays(val, Days);
	}
	
	/**
	 * To get month number as int by providing date object
	 * 
	 * @param val
	 * @return
	 */
	public static int getMonthNumberByDate(Date val) {
		if (val != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM");
			return AlmoizParseUtils.parseInt(format.format(val));
		} else {

			return 0;

		}
	}
	
	/**
	 * To get array of past dates on providing a date object and number of months
	 * 
	 * @param val
	 * @param NumberOfMonthsInPast
	 * @return
	 */
	public static Date[] getPastMonthsInDate(Date val, int NumberOfMonthsInPast) {

		int month = getMonthNumberByDate(val);
		int year = getYearByDate(val);

		Date ret[] = new Date[NumberOfMonthsInPast];

		for (int i = (NumberOfMonthsInPast - 1); i >= 0; i--) {

			int imonth = (month - i);
			int iyear = year;

			if (imonth < 1) {
				imonth = imonth + 12;
				iyear = iyear - 1;
			}

			ret[(NumberOfMonthsInPast - i - 1)] = getStartDateByMonth(imonth - 1, iyear);

		}

		return ret;
	}
	
	/**
	 * To get year as int by providing providing date object
	 * 
	 * @param val
	 * @return
	 */
	public static int getYearByDate(Date val) {
		if (val != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			return AlmoizParseUtils.parseInt(format.format(val));
		} else {

			return 0;

		}
	}

	
	/**
	 * To get number in "###,###.#" format
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatTwoDecimalFixed(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		return format.format(val);

	}
	
	/**
	 * To get date as string to embed in MySQL Query, it adds one day into date
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateNext(Date val) {

		// Adds another day to Date and converts date into SQL format, ignores time
		if (val != null) {

			Date next = DateUtils.addDays(val, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "'" + format.format(next) + "'";

		} else {

			return null;

		}

	}
	
	/**
	 * To get date as string to embed in MySQL Query
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDate(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "'" + format.format(val) + "'";

		} else {

			return null;

		}

	}

	
	/**
	 * To get day of week of provided date
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeekByDate(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return DayOfWeek;

	}
	
	/**
	 * To get day numbers with comma separated
	 * 
	 * @param Date 1 one and Date 1
	 * @return String
	 */
	
	public static String getDayNumbersString(Date Start_Date, Date End_Date) {
		String dayNumbers = "";
		int c=0;
		Date date = Start_Date;
		while (true) {
			//System.out.println(Utilities.getDayOfWeekByDate(date));
			dayNumbers += (c == 0) ? getDayOfWeekByDate(date) : ","+getDayOfWeekByDate(date);
			if (DateUtils.isSameDay(date,End_Date)) {
				break;
			}
			date = DateUtils.addDays(date, 1);
			c++;
		}
		return dayNumbers;
	}

	/**
	 * To get date in time difference of two times format as string to display on UI
	 * 
	 * @param time one and time 2
	 * @return long
	 */
	public static long getTimeDifference(Date startTime, Date endTime) {
		long startMillis = startTime.getTime();
		long endMillis = endTime.getTime();
		return endMillis - startMillis;
	}

	/**
	 * To get date in "dd/MM/yyyy" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateFormatMMDDYYYY(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

	}

}
