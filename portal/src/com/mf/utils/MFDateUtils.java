package com.mf.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.mf.utils.MFParseUtils;

public class MFDateUtils {
	
	/**
	 * Converts a String in yyyy-MM-dd into a Date object.
	 * @param val
	 * @return
	 */
	public static Date parseDateYYYYMMDD(String val) {

		Date d = null;

		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

	}

	/**
	 * @param String
	 *            val - Input datetime in the format yyyy-MM-dd HH:mm:ss
	 * @return Time - Time in format HH:mm
	 **/
	public static Time extractTimeHoursAndMinutes(String val) {
		Time time = null;

		if (val != null && !val.isEmpty()) {
			// Define the format for parsing the input
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				// Parse the input string into a Date object
				Date fullDate = inputFormat.parse(val);
				// Convert the Date object to a java.sql.Time object
				time = new Time(fullDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace(); // Log or handle exceptions
			}
		}

		return time;
	}

	public static long calculateTimeDifferenceInMinutes(Time orderBookerAttendanceTime, Time fixedAttendanceTime) {
		LocalTime fixedTime = fixedAttendanceTime.toLocalTime();
		LocalTime orderBookerTime = orderBookerAttendanceTime.toLocalTime();

		Duration duration = Duration.between(fixedTime, orderBookerTime);
		return duration.toMinutes();
	}

	public static Time convertStringToTime(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			return new Time(sdf.parse(timeString).getTime());
		} catch (ParseException e) {
			System.err.println("Invalid time format. Expected HH:mm");
			e.printStackTrace();
			return null;
		}
	}

	public static String get_day_numbers_by_date(Date StartDate, Date EndDate) {

		String days = "";
		Date currentDateStr = null;
		int count = 0;
		while (!StartDate.after(EndDate)) {
			currentDateStr = StartDate;
			days += (count == 0) ? MFDateUtils.getDayOfWeekByDate(currentDateStr)
					: "," + MFDateUtils.getDayOfWeekByDate(currentDateStr);

			Calendar cal = Calendar.getInstance();
			cal.setTime(StartDate);
			cal.add(Calendar.DATE, 1);
			StartDate = cal.getTime();
			count++;
		}
		return days;

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
	 * To get date by providing number of days starting from specific date
	 * 
	 * @param val
	 * @param Days
	 * @return
	 */
	public static Date getDateByDays(Date val, int Days) {
		return DateUtils.addDays(val, Days);
	}

	/**
	 * To get date by providing number of days starting from today
	 * 
	 * @param Days
	 * @return
	 */
	public static Date getDateByDays(int Days) {
		return DateUtils.addDays(new Date(), Days);
	}

	/**
	 * To get date in "dd/MM/yyyy" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateShortMonthYearFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

	}

	/**
	 * Parses a date string and returns only the date portion.
	 *
	 * @param dateString
	 *            The date string to parse.
	 * @return The parsed Date object with only the date portion.
	 * @throws ParseException
	 *             If the date string is invalid.
	 */
	public static Date parseDateDateTime(String dateString) throws ParseException {

		Date d = null;
		try {
			SimpleDateFormat inputFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			SimpleDateFormat dateOnlyFormatter = new SimpleDateFormat("yyyy-MM-dd");
			// Parse the full date string
			Date fullDate = inputFormatter.parse(dateString);

			// Format to keep only the date part and parse back to Date
			String dateOnlyString = dateOnlyFormatter.format(fullDate);
			d = dateOnlyFormatter.parse(dateOnlyString);
		} catch (ParseException e) {
		}
		return d;
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
			return MFParseUtils.parseInt(format.format(val));
		} else {

			return 0;

		}
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
			return MFParseUtils.parseInt(format.format(val));
		} else {

			return 0;

		}
	}

	/**
	 * To get day of month
	 * 
	 * @param val
	 * @return
	 */
	public static int getDayNumberByDate(Date val) {
		if (val != null) {
			SimpleDateFormat format = new SimpleDateFormat("dd");
			return MFParseUtils.parseInt(format.format(val));
		} else {

			return 0;

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
	 * To get date in string format without single quotes to embed in MySQL Query
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateWithoutSeprator(Date val) {

		// Converts date into sql format and ignores time

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			return format.format(val);

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
	 * To get time in "h:mm a" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayTimeSecondsFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("h:mm:ss a");
			return "" + format.format(val);

		} else {

			return null;

		}

	}
}
