package com.pbc.util;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.mail.*;
import org.apache.commons.mail.HtmlEmail;

/**
 * It is a collection of tools and utilities necessary to development of any
 * component in this product
 * 
 * @author PBC
 *
 */
public class Utilities {

	public static String CompanyName = "Punjab Beverages Co. (Pvt.) Ltd.";
	
	
	/**
	 * @params String val
	 * @Returns Date in format yyyy/MM/dd
	 * 
	 **/

	public static String getSQLString(String val) {
		
		String ret=null;
		
		if(val!=null) {
			ret="'" + val + "'";
		}
		
		return ret;
		 	

	}
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
	 * This function returns Day off of Distributer either it is Sunday or Friday
	**/

	public static int IsDistributorOFFStatus(long distributorID) {

		int IsSundayOff = 0;
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();

			ResultSet rs = s.executeQuery(
					"SELECT is_sunday_off FROM common_distributors where distributor_id=" + distributorID + "");
			if (rs.first()) {

				IsSundayOff = rs.getInt("is_sunday_off");
			}

			ds.dropConnection();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return IsSundayOff;
	}

	/**
	 * 
	 * It is used to check authorization of a user for a specific feature. It
	 * maintains a log of all authorization checks (failed or authorized) in logs
	 * database.
	 * 
	 * @param FeatureID
	 * @param UserID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static boolean isAuthorized(int FeatureID, long UserID)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		Datasource ds = new Datasource();
		ds.createConnection();

		boolean isAuthorized = false;

		Statement s = ds.createStatement();

		System.out.println(
				"select created_on from user_access where user_id=" + UserID + " and feature_id=" + FeatureID);
		ResultSet rs = s.executeQuery(
				"select created_on from user_access where user_id=" + UserID + " and feature_id=" + FeatureID);
		if (rs.first()) {
			isAuthorized = true;
		}

		int isAuthorizedNumber = 0;
		if (isAuthorized == true) {
			isAuthorizedNumber = 1;
		}

		s.executeUpdate("insert into " + ds.logDatabaseName()
				+ ".log_user_access (user_id, feature_id, ip_address, created_on, is_authorized, feature_group_label, feature_label) values ("
				+ UserID + ", " + FeatureID + ", null, now(), " + isAuthorizedNumber
				+ ", (select fg.group_name from features f join feature_groups fg on f.group_id = fg.group_id where f.feature_id = "
				+ FeatureID + "),(select feature_name from features where feature_id = " + FeatureID + "))");

		ds.dropConnection();

		return isAuthorized;
	}

	/**
	 * It converts bottles into cases and returns in a standard format i.e.
	 * (cases~bottles)
	 * 
	 * @param units
	 * @param UnitsPerSKU
	 * @return
	 */
	public static String convertToRawCases(long units, int UnitsPerSKU) {

		String ret = "";
		if (UnitsPerSKU != 0) {
			double RawCasesDouble = (double) units / (double) UnitsPerSKU;
			String RawCasesString = RawCasesDouble + "";
			if (RawCasesString.indexOf(".") != -1) {
				RawCasesString = RawCasesString.substring(0, RawCasesString.indexOf("."));
			}
			long RawCases = Utilities.parseLong(RawCasesString);

			long RawCasesUnits = RawCases * UnitsPerSKU;

			long bottles = units - RawCasesUnits;

			if (bottles == 0) {
				ret = getDisplayCurrencyFormat(RawCases) + "";
			} else {
				ret = getDisplayCurrencyFormat(RawCases) + "~" + bottles;
			}
		}
		return ret;
	}

	/**
	 * It converts bottles into cases and returns in a standard format i.e.
	 * (cases~bottles) with negative figure in brackets
	 * 
	 * @param units
	 * @param UnitsPerSKU
	 * @return
	 */
	public static String convertToRawCasesAccounting(long units, int UnitsPerSKU) {

		boolean IsNegative = false;
		if (units < 0) {
			units = units * -1;
			IsNegative = true;
		}

		String ret = "";
		if (UnitsPerSKU != 0) {
			double RawCasesDouble = (double) units / (double) UnitsPerSKU;
			String RawCasesString = RawCasesDouble + "";
			if (RawCasesString.indexOf(".") != -1) {
				RawCasesString = RawCasesString.substring(0, RawCasesString.indexOf("."));
			}
			long RawCases = Utilities.parseLong(RawCasesString);

			long RawCasesUnits = RawCases * UnitsPerSKU;

			long bottles = units - RawCasesUnits;

			if (bottles == 0) {
				ret = getDisplayCurrencyFormat(RawCases) + "";
			} else {
				ret = getDisplayCurrencyFormat(RawCases) + "~" + bottles;
			}
		}

		if (IsNegative) {
			ret = "(" + ret + ")";
		}
		return ret;
	}

	/**
	 * It formats cases and bottles into standard format (i.e. cases~bottles)
	 * 
	 * @param RawCase
	 * @param Units
	 * @return
	 */
	public static String formatCasesAndUnits(long RawCase, long Units) {
		return RawCase + "~" + Units;
	}

	/**
	 * It converts bottles into cases and bottles, returns both in an array of
	 * long[]
	 *
	 * 
	 * @param units
	 * @param UnitsPerSKU
	 * @return
	 */
	public static long[] getRawCasesAndUnits(long units, int UnitsPerSKU) {

		long ret[] = new long[2];
		if (UnitsPerSKU != 0) {
			double RawCasesDouble = (double) units / (double) UnitsPerSKU;
			String RawCasesString = RawCasesDouble + "";
			if (RawCasesString.indexOf(".") != -1) {
				RawCasesString = RawCasesString.substring(0, RawCasesString.indexOf("."));
			}
			long RawCases = Utilities.parseLong(RawCasesString);

			long RawCasesUnits = RawCases * UnitsPerSKU;

			long bottles = units - RawCasesUnits;

			ret[0] = RawCases;
			ret[1] = bottles;

		}
		return ret;
	}

	/**
	 * This method is deprecated. It was being used earlier for discount system to
	 * get Distributor Name from SAP Outlet Masterdata
	 * 
	 * @param DirtributorID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static String getDistributorName(long DirtributorID)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		Datasource ds = new Datasource();
		ds.createConnection();
		String DistributorName = "";

		Statement s = ds.createStatement();

		ResultSet rs = s
				.executeQuery("select distinct Customer_name from outletmaster where Customer_ID=" + DirtributorID);
		if (rs.first()) {
			DistributorName = rs.getString("Customer_name");
		}

		ds.dropConnection();

		return DistributorName;

	}

	/**
	 * Returns company name to display on documents and reports
	 * 
	 * @return
	 */
	public static String getCompanyName() {
		return CompanyName;
	}

	/**
	 * 
	 * Used in all User Inputs to filter special characters for SQL Injection
	 * attacks etc Level 0 = Number Level 1 = Normal filter for String Level 2 =
	 * Strict filter for String
	 * 
	 * @param val
	 * @param level
	 * @param maxLength
	 * @return
	 */
	public static String filterString(String val, int level, int maxLength) { //

		String ret = val;

		if (val != null) {

			// Truncate to maximum length
			if (val.length() > maxLength) {
				val = val.substring(0, maxLength);
			}

			switch (level) {

			case 0: // Level 0 - Numbers Only
				if (!ret.matches("^[0-9,./-]+$")) {
					ret = "0";
				}

				break;

			case 1: // Level 1 - Minimum

				char Reserved[] = { '\'', '\"', '\\' };
				char Replacement[] = { '`', '`', '/' };

				int ct = 0;
				for (char iReserved : Reserved) {
					ret = ret.replace(iReserved, Replacement[ct]);
					ct++;
				}

				break;

			case 2: // Level 2 - Moderate

				char Reserved2[] = { '\'', '\"', '\\', '<', '>', '+', '=' };
				char Replacement2[] = { '`', '`', '/', ' ', ' ', ' ', '=' };

				int ct2 = 0;
				for (char iReserved2 : Reserved2) {
					ret = ret.replace(iReserved2, Replacement2[ct2]);
					ct2++;
				}

				break;

			}

		}

		return ret;
	}

	/**
	 * Used in all User Inputs to filter special characters for SQL Injection
	 * attacks etc Level 0 = Array of Number Level 1 = Normal filter for Array of
	 * String Level 2 = Strict filter for Array of String
	 * 
	 * @param val
	 * @param level
	 * @param maxLength
	 * @return
	 */
	public static String[] filterString(String val[], int level, int maxLength) { //
		if (val == null) {
			return null;
		}
		String ret[] = new String[val.length];

		for (int i = 0; i < val.length; i++) {
			ret[i] = filterString(val[i], level, maxLength);
		}

		return ret;
	}

	/**
	 * To get the URL for standard error page
	 * 
	 * @param request
	 * @return
	 */
	public static String getErrorPageURL(HttpServletRequest request) {
		return request.getContextPath() + "/GeneralException.jsp";
	}

	/**
	 * To get URL for Access Denied page to show when user is not authorized
	 * 
	 * @param request
	 * @return
	 */
	public static String getAccessDeniedPageURL(HttpServletRequest request) {
		return request.getContextPath() + "/AccessDenied.jsp";
	}

	/**
	 * To get URL for Session Expiry page to show when session is expired
	 * 
	 * @param request
	 * @return
	 */
	public static String getSessionExpiredPageURL(HttpServletRequest request) {
		return request.getContextPath() + "/SessionExpired.jsp";
	}

	/**
	 * To get Picture URL of a User ID
	 * 
	 * @param UserID
	 * @param request
	 * @return
	 */
	public static String getUserPictureURL(long UserID, HttpServletRequest request) {
		return request.getContextPath() + "/images/UserPictures/" + UserID + ".png";
	}

	/**
	 * To get path for Workflow Attachments
	 * 
	 * @return
	 */
	public static String getWorkflowAttachmentsPath() {
		 return "/home/ftpshared/WorkflowAttachments";
		// return "/home/ftpshared/WorkflowAttachments";
		// return "D:\\Almoiz\\Files\\GeneratedFiles";
		//return "C:\\Users\\Usman\\OneDrive - Kale Labs (Pvt) Limited\\Desktop";
	}

	/**
	 * To get path for image resources
	 * 
	 * @return
	 */
	public static String getImageResoucesPath() {
		// return "d:\\PBC\\GitHub\\Theia\\portal\\WebContent\\images";
		 return "/home/ftpshared/WorkflowAttachments";
	//	return "D:\\Almoiz\\Files\\GeneratedFiles";
	}

	/**
	 * To get patch for email attachments
	 * 
	 * @return
	 */
	public static String getEmailAttachmentsPath() {
		// return "d:\\PBC";
		 return "/home/ftpshared/EmailAttachments";
	//	return "D:\\Almoiz\\Files\\GeneratedFiles";
	}

	/**
	 * To get path for images stored against orders
	 * 
	 * @return
	 */
	public static String getOrderImagesPath() {
		 return "/home/ftpshared/OrderImages";
		// return "GeneratedExcelFiles";
	//	return "D:\\Almoiz\\Files\\GeneratedFiles";
	}
	
	/**
	 * To get path for images stored against orders
	 * 
	 * @return
	 */
	public static String getOutletImagesPath() {
		return "/home/ftpshared/OrderImages";
		// return "GeneratedExcelFiles";
		
	//	return "D:\\Almoiz\\Files\\GeneratedFiles";
	}

	/**
	 * To get path for CRM attachments
	 * 
	 * @return
	 */
	public static String getComplaintAttachmentsPath() {
		 return "/home/ftpshared/WorkflowAttachments";
	
	//	return "D:\\Almoiz\\Files\\GeneratedFiles";
	}

	public static String getCommonFilePath() {
		//return "D:\\";
		//// return "d:\\PBC";
		// return "/home/ftpshared/CommonFiles";
		//return "D:\\Almoiz\\Files\\GeneratedFiles";
		return "C:\\Users\\Usman\\OneDrive - Kale Labs (Pvt) Limited\\Desktop";
	}

	/**
	 * Converts a String in dd/MM/yyyy into a Date object.
	 * 
	 * @param val
	 * @return
	 */
	public static Date parseDate(String val) {

		Date d = null;

		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

	}

	/**
	 * Converts a String in yyyy-MM-dd into a Date object.
	 * 
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
	 * Converts a String in yyyyMMdd into a Date object.
	 * 
	 * @param val
	 * @return
	 */
	public static Date parseDateYYYYMMDDWithoutSeparator(String val) {

		Date d = null;

		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

	}

	/**
	 * Converts a String in dd/MM/yyyy, provided hour and minute into a Date object.
	 * 
	 * @param val
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date parseDateTime(String val, int hour, int minute) {

		Date d = null;
		val = val + " " + hour + ":" + minute;
		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

	}

	/**
	 * Converts a String in dd/MM/yyyy HH:mm into a Date object.
	 * 
	 * @param val
	 * @return
	 */
	public static Date parseDateTime(String val) {

		Date d = null;
		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

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
	 * To get date as string to embed in Oracle Query
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateOracle(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			return "'" + format.format(val) + "'";

		} else {

			return null;

		}

	}

	/**
	 * To get date as string to embed in Oracle Query without single quotes
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateWithoutQuotes(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return format.format(val);

		} else {

			return null;

		}

	}

	/**
	 * To get date in lifting day boundaries as string to embed in MySQL Query
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateLifting(Date val) {

		// Converts date into sql format and ignores time
		/*
		 * if (val != null) {
		 * 
		 * SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); return
		 * "date_format('" + format.format(val) + " 6:00','%Y-%m-%d %H:%i')";
		 * 
		 * } else {
		 * 
		 * return null;
		 * 
		 * }
		 */
		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "'" + format.format(val) + "'";

		} else {

			return null;

		}

	}

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
	 * To get date and time as string to embed in MySQL Query, it reduces one minute
	 * from date
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLFromDateTime(Date val) {

		// Reduces 1 minute from date and converts into query format (suited for
		// "between")

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd H:mm");

			Calendar cal = Calendar.getInstance();
			cal.setTime(val);
			cal.add(Calendar.MINUTE, -1);

			return "date_format('" + format.format(cal.getTime()) + "', '%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}

	/**
	 * To get date and time as string to embed in MySQL Query, it adds one minute
	 * into date
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLToDateTime(Date val) {

		// Adds 1 minute to date and converts into query format (suited for "between")

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd H:mm");

			Calendar cal = Calendar.getInstance();
			cal.setTime(val);
			cal.add(Calendar.MINUTE, +1);

			return "date_format('" + format.format(cal.getTime()) + "', '%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

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
	 * To get date as string to embed in MySQL Query, it adds one day of lifting day
	 * boundary into date
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateNextLifting(Date val) {

		// Adds another day to Date and converts date into SQL format, ignores time
		
		  if (val != null) {
		  
		  Date next = DateUtils.addDays(val, 1); SimpleDateFormat format = new
		  SimpleDateFormat("yyyy/MM/dd"); return "date_format('" + format.format(next)
		  + " 6:00','%Y-%m-%d %H:%i')";
		  
		  } else {
		  
		  return null;
		
		 }
		 
//		if (val != null) {
//
//			Date next = DateUtils.addDays(val, 1);
//			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//			return "'" + format.format(next) + "'";
//
//		} else {
//
//			return null;
//
//		}

	}

	/**
	 * To get date and time as string to embed in MySQL Query, it adds one day into
	 * date
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDateTimeNext(Date val) {

		// Adds another day to Date and Time and converts date into SQL format
		if (val != null) {

			Date next = DateUtils.addDays(val, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd H:mm");
			return "date_format('" + format.format(next) + "','%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}

	/**
	 * 
	 * To get date as string to embed in MySQL Query, it subtracts one day from date
	 * 
	 * @param val
	 * @return
	 */
	public static String getSQLDatePrevious(Date val) {

		// Substracts a day from Date and converts date into SQL format, ignores time

		if (val != null) {
			Date previous = DateUtils.addDays(val, -1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "'" + format.format(previous) + "'";

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
	 * To get date in string format for display on UI, it subtracts one day from
	 * date
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDatePrevious(Date val) {

		if (val != null) {
			Date previous = DateUtils.addDays(val, -1);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			return format.format(previous);

		} else {

			return null;

		}

	}

	/**
	 * To get date in string format for display on UI, it adds one day to date
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateNext(Date val) {

		if (val != null) {
			Date previous = DateUtils.addDays(val, 1);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			return format.format(previous);

		} else {

			return null;

		}

	}

	/**
	 * To get long date in string format for UI displays
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayFullDateFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMMM dd, yyyy");
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
	 * To get date and time in "dd/MM/yyyy h:mm a" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateTimeFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy h:mm a");
			return "" + format.format(val);

		} else {

			return null;

		}

	}

	/**
	 * To get date and time in "dd/MM/yyyy h:mm:ss" format as string to display on
	 * UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateTimeFormatUniversal(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
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
	 * To get date in "MMM, yyyy" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateMonthYearFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("MMM, yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

	}

	/**
	 * To get date in "MMMMM yyyy" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayDateFullMonthYearFormat(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("MMMMM yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

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
			return parseInt(format.format(val));
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
			return parseInt(format.format(val));
		} else {

			return 0;

		}
	}

	/**
	 * To get start date of year by providing providing date object
	 * 
	 * @param val
	 * @return
	 */
	public static Date getYearStartDateByDate(Date idate) {
		int iyear = Utilities.getYearByDate(idate);

		Calendar c = Calendar.getInstance();
		c.set(iyear, 0, 1);

		return c.getTime();
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
	 * To get number in "###,###.##" format
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormat(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.##");
		format.setMaximumFractionDigits(2);

		return format.format(val);

	}

	/**
	 * To get number in "###,###.#" format
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatOneDecimal(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(1);

		return format.format(val);

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
	 * To get number in "###,###.# Dr/Cr" format
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatTwoDecimalFixedAccountingDr(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		if (val < 0) {
			return format.format(val * -1) + " Cr";
		} else {
			return format.format(val) + " Dr";
		}

	}

	/**
	 * To get number in "###,###.#" format, in brackets if negative
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatTwoDecimalFixedAccounting(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		if (val < 0) {
			return "(" + format.format(val * -1) + ")";
		} else {
			return format.format(val);
		}

	}

	/**
	 * To get number in "###,###" format, in brackets if negative
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatRoundedAccounting(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###");
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);

		if (val < 0) {
			return "(" + format.format(val * -1) + ")";
		} else {
			return format.format(val);
		}

	}

	/**
	 * To get number in abbreviated format (K for thousand, M for million)
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatAbbreviatedOneDecimal(double val) {

		String symbol = "";

		if (val > 1000 && val < 1000000) {
			val = val / 1000;
			symbol = "K";
		}
		if (val > 1000000) {
			val = val / 1000000;
			symbol = "M";
		}

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(1);

		return format.format(val) + symbol;

	}

	/**
	 * To get number in abbreviated format (strictly in K for thousand)
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(double val) {

		String symbol = "";

		if (val > 1000) {
			val = val / 1000;
			symbol = "K";
		}

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(1);

		return format.format(val) + symbol;

	}

	/**
	 * To get number in "#.##" format
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatSimple(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("#.##");
		format.setMaximumFractionDigits(2);

		return format.format(val);

	}

	/**
	 * To get number in "###,###" format
	 * 
	 * @param val
	 * @return
	 */
	public static String getDisplayCurrencyFormatRounded(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###");
		format.setMaximumFractionDigits(0);

		return format.format(val);

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
	 * To convert string to boolean
	 * 
	 * @param val
	 * @return
	 */
	public static boolean parseBoolean(String val) {

		if (val == null) {
			val = "false";
		}

		boolean ret = false;
		ret = Boolean.parseBoolean(val);

		return ret;
	}

	/**
	 * To convert string array into double array
	 * 
	 * @param val
	 * @return
	 */
	public static double[] parseDouble(String val[]) {

		if (val != null) {
			double ret[] = new double[val.length];

			for (int i = 0; i < val.length; i++) {
				ret[i] = parseDouble(val[i]);
			}

			return ret;
		} else {
			return null;
		}
	}

	/**
	 * To convert array of string to array of double and ignore commas
	 * 
	 * @param val
	 * @return
	 */
	public static double[] parseDoubleAndFilterComma(String val[]) {

		if (val != null) {
			double ret[] = new double[val.length];

			for (int i = 0; i < val.length; i++) {
				if (val[i] != null) {
					val[i] = val[i].replaceAll(",", "");
				}
				ret[i] = parseDouble(val[i]);
			}

			return ret;
		} else {
			return null;
		}
	}

	/**
	 * To convert array of string into array of long
	 * 
	 * @param val
	 * @return
	 */
	public static long[] parseLong(String val[]) {

		if (val != null) {
			long ret[] = new long[val.length];

			for (int i = 0; i < val.length; i++) {
				ret[i] = parseLong(val[i]);
			}

			return ret;
		} else {
			return null;
		}
	}

	/**
	 * To convert array of string into array of int
	 * 
	 * @param val
	 * @return
	 */
	public static int[] parseInt(String val[]) {

		if (val != null) {
			int ret[] = new int[val.length];

			for (int i = 0; i < val.length; i++) {
				ret[i] = parseInt(val[i]);
			}

			return ret;
		} else {
			return null;
		}
	}

	/**
	 * To convert array of string into array of date
	 * 
	 * @param val
	 * @return
	 */
	public static Date[] parseDate(String val[]) {

		if (val != null) {
			Date ret[] = new Date[val.length];

			for (int i = 0; i < val.length; i++) {
				ret[i] = parseDate(val[i]);
			}

			return ret;
		} else {
			return null;
		}
	}

	/**
	 * To convert user parameters with serial number into a single array of string
	 * 
	 * @param parameter
	 * @param length
	 * @param request
	 * @return
	 */
	public static String[] getSerialParameterValues(String parameter, int length, HttpServletRequest request) {
		String ret[] = new String[length];

		for (int i = 0; i < length; i++) {
			ret[i] = request.getParameter(parameter + "" + (i + 1));
		}

		return ret;
	}

	/**
	 * To truncate string to a specified length
	 * 
	 * @param val
	 * @param MaxLength
	 * @return
	 */
	public static String truncateStringToMax(String val, int MaxLength) {
		if (val != null) {
			int len = val.length();
			if (len > MaxLength) {
				val = val.substring(0, MaxLength);
			}
		}
		return val;
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

	/**
	 * To get end date of a month
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public static Date getEndDateByMonth(int month, int year) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		return c.getTime();
	}

	/**
	 * To get end date of a month
	 * 
	 * @param idate
	 * @return
	 */
	public static Date getEndDateByDate(Date idate) {
		int imonth = Utilities.getMonthNumberByDate(idate);
		int iyear = Utilities.getYearByDate(idate);
		return Utilities.getEndDateByMonth(imonth - 1, iyear);
	}

	/**
	 * To get start date of a month
	 * 
	 * @param idate
	 * @return
	 */
	public static Date getStartDateByDate(Date idate) {
		int imonth = Utilities.getMonthNumberByDate(idate);
		int iyear = Utilities.getYearByDate(idate);
		return Utilities.getStartDateByMonth(imonth - 1, iyear);
	}

	/**
	 * It generates a time based unique number to use for barcodes or document
	 * identifiers
	 * 
	 * @param UserID
	 * @return
	 */
	public static long getUniqueVoucherID(long UserID) {

		String AUserID = UserID + "";

		while (AUserID.length() <= 4) {
			AUserID = "0" + AUserID;
		}

		AUserID = AUserID.substring(AUserID.length() - 4, AUserID.length());

		String UniqueNumber = AUserID + "" + (System.currentTimeMillis());

		return Utilities.parseLong(UniqueNumber);
	}

	/**
	 * It generates a short time based unique number to use for barcodes or document
	 * identifiers
	 * 
	 * @param UserID
	 * @return
	 */
	public static long getUniqueVoucherIDShort(long UserID) {

		String AUserID = UserID + "";

		while (AUserID.length() <= 2) {
			AUserID = "0" + AUserID;
		}

		AUserID = AUserID.substring(AUserID.length() - 2, AUserID.length());

		String UniqueNumber = AUserID + "" + (System.currentTimeMillis());

		return Utilities.parseLong(UniqueNumber);
	}

	/**
	 * It converts a long array into a string of comma separated values
	 * 
	 * @param arr
	 * @return
	 */
	public static String serializeForSQL(long arr[]) {
		String ret = "";
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if (i != 0) {
					ret += ",";
				}
				ret += arr[i];
			}
		}
		return ret;
	}

	/**
	 * It converts a String array into a string of comma separated values
	 * 
	 * @param arr
	 * @return
	 */
	public static String serializeForSQL(String arr[]) {

		String ret = "";
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if (i != 0) {
					ret += ",";
				}
				ret += "'" + arr[i] + "'";
			}
		}
		return ret;
	}

	/**
	 * To get admin password in MD5
	 * 
	 * @return
	 */
	public static String getMobileAdminPasswordMD5() {
		return "e3e6c68051fdfdf177c8933bfd76de48";
	}

	/**
	 * To get admin password
	 * 
	 * @return
	 */
	public static String getMobileAdminPassword() {
		return "wildspace12%36e";
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
			return parseInt(format.format(val));
		} else {

			return 0;

		}
	}

	/**
	 * To get Friday count of month
	 * 
	 * @param val
	 * @return
	 */
	public static int getFridayCountByDate(Date val) {
		int NumberOfFriday = 0;
		Date StartDate = Utilities.getStartDateByDate(val);
		Date EndDate = Utilities.getEndDateByDate(val);

		Date CurrentDate = StartDate;

		while (true) {
			if (Utilities.getDayOfWeekByDate(CurrentDate) == 6) { // if it is Friday
				NumberOfFriday++;
			}

			if (DateUtils.isSameDay(CurrentDate, EndDate)) {
				break;
			}

			CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
		}

		return NumberOfFriday;
	}

	/**
	 * To get subsequent Friday count of month
	 * 
	 * @param val
	 * @return
	 */
	public static int getSubsequentFridayCountByDate(Date val) {
		int NumberOfFriday = 0;
		Date StartDate = val;
		Date EndDate = Utilities.getEndDateByDate(val);

		Date CurrentDate = StartDate;

		while (true) {
			if (Utilities.getDayOfWeekByDate(CurrentDate) == 6) { // if it is Friday
				NumberOfFriday++;
			}

			if (DateUtils.isSameDay(CurrentDate, EndDate)) {
				break;
			}

			CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
		}

		return NumberOfFriday;
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
	 * To get month name by providing month number
	 * 
	 * @param Month
	 * @return
	 */
	public static String getMonthNameByNumber(int Month) {
		String MonthName = "";
		if (Month == 1) {
			MonthName = "January";
		} else if (Month == 2) {
			MonthName = "February";
		} else if (Month == 3) {
			MonthName = "March";
		} else if (Month == 4) {
			MonthName = "April";
		} else if (Month == 5) {
			MonthName = "May";
		} else if (Month == 6) {
			MonthName = "June";
		} else if (Month == 7) {
			MonthName = "July";
		} else if (Month == 8) {
			MonthName = "August";
		} else if (Month == 9) {
			MonthName = "September";
		} else if (Month == 10) {
			MonthName = "October";
		} else if (Month == 11) {
			MonthName = "November";
		} else if (Month == 12) {
			MonthName = "December";
		}
		return MonthName;
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
	 * To send SMS
	 * 
	 * @param number
	 * @param message
	 * @return
	 * @throws IOException
	 */
	public static boolean sendSMS(String number, String message) throws IOException {

		URL url = new URL("http://155.135.0.70/default.aspx?number=" + number + "&msg=" + URLEncoder.encode(message));
		URLConnection urlConnection = url.openConnection();

		HttpURLConnection connection = null;
		if (urlConnection instanceof HttpURLConnection) {
			connection = (HttpURLConnection) urlConnection;
		} else {
			System.out.println("Utilities.sendSMS: Invalid URL");
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String urlString = "";
		String current;
		while ((current = in.readLine()) != null) {
			urlString += current;
		}

		if (urlString != null && urlString.indexOf("true") == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * To send WhatsApp message
	 * 
	 * @param number
	 * @param message
	 * @param pictures
	 * @return
	 * @throws IOException
	 */
	public static boolean sendWhatsApp(String number, String message, String[] pictures) throws IOException {

		try {

			Datasource ds = new Datasource();
			ds.createConnection();

			Statement s = ds.createStatement();

			PreparedStatement ps = ds.getConnection().prepareStatement(
					"insert into sms_whatsapp_queue (created_on, type_id, message, file_url, number) values (now(), 1,?,null,?)");
			ps.setString(1, Utilities.filterString(message, 1, 2000));
			ps.setString(2, number);
			ps.executeUpdate();

			if (pictures != null) {
				for (int i = 0; i < pictures.length; i++) {
					if (pictures[i] != null) {
						s.executeUpdate(
								"insert into sms_whatsapp_queue (created_on, type_id, message, file_url, number) values (now(), 2, null,'"
										+ pictures[i] + "','" + number + "')");
					}
				}
			}

			ds.dropConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * To send Telegram message
	 * 
	 * @param number
	 * @param message
	 * @param pictures
	 * @return
	 * @throws IOException
	 */
	public static boolean sendTelegram(String number, String message, String[] pictures) throws IOException {

		try {

			Datasource ds = new Datasource();
			ds.createConnection();

			Statement s = ds.createStatement();

			PreparedStatement ps = ds.getConnection().prepareStatement(
					"insert into sms_telegram_queue (created_on, type_id, message, file_url, number) values (now(), 1,?,null,?)");
			ps.setString(1, Utilities.filterString(message, 1, 2000));
			ps.setString(2, number);
			ps.executeUpdate();

			if (pictures != null) {
				for (int i = 0; i < pictures.length; i++) {
					if (pictures[i] != null) {
						s.executeUpdate(
								"insert into sms_telegram_queue (created_on, type_id, message, file_url, number) values (now(), 2, null,'"
										+ pictures[i] + "','" + number + "')");
					}
				}
			}

			ds.dropConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * To send email
	 * 
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param message
	 * @param filename
	 * @return
	 * @throws EmailException
	 */
	public static String sendPBCEmail(String to[], String cc[], String bcc[], String subject, String message,
			String filename[]) throws EmailException {

		try {

			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("155.135.0.3");

			for (int i = 0; i < to.length; i++) {
				email.addTo(to[i]);
			}
			if (cc != null) {
				for (int i = 0; i < cc.length; i++) {
					email.addCc(cc[i]);
				}
			}
			if (bcc != null) {
				for (int i = 0; i < bcc.length; i++) {
					email.addBcc(bcc[i]);
				}
			}

			email.setFrom("theia@pbc.com.pk", "Theia");

			email.setSubject(subject);

			email.setMsg(message);

			if (filename != null) {

				for (int i = 0; i < filename.length; i++) {
					EmailAttachment attachment = new EmailAttachment();
					attachment.setPath(Utilities.getEmailAttachmentsPath() + "/" + filename[i]);
					attachment.setDisposition(EmailAttachment.ATTACHMENT);
					attachment.setDescription(filename[i]);
					attachment.setName(filename[i]);
					email.attach(attachment);
				}

			}

			return email.send();

		} catch (Exception e) {
			e.printStackTrace();

			return null;

		}
		// return true;
	}

	/**
	 * To send email in HTML format
	 * 
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param message
	 * @param filename
	 * @return
	 * @throws EmailException
	 */
	public static String sendPBCHTMLEmail(String to[], String cc[], String bcc[], String subject, String message,
			String filename[]) throws EmailException {

		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("155.135.0.3"); // local theia mail server ip

			// email.setHostName("203.124.57.23");

			for (int i = 0; i < to.length; i++) {
				email.addTo(to[i]);
			}
			if (cc != null) {
				for (int i = 0; i < cc.length; i++) {
					email.addCc(cc[i]);
				}
			}
			if (bcc != null) {
				for (int i = 0; i < bcc.length; i++) {
					email.addBcc(bcc[i]);
				}
			}

			email.setFrom("theia@pbc.com.pk", "Theia");

			email.setSubject(subject);

			email.setHtmlMsg(message);

			if (filename != null) {

				for (int i = 0; i < filename.length; i++) {
					try {
						EmailAttachment attachment = new EmailAttachment();
						attachment.setPath(Utilities.getEmailAttachmentsPath() + "/" + filename[i]);
						attachment.setDisposition(EmailAttachment.ATTACHMENT);
						attachment.setDescription(filename[i]);
						attachment.setName(filename[i]);
						email.attach(attachment);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}

			return email.send();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return true;
	}

	/**
	 * To send email on external addresses in HTML format
	 * 
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param message
	 * @param filename
	 * @return
	 * @throws EmailException
	 */
	public static String sendPBCHTMLEmailExternal(String to[], String cc[], String bcc[], String subject,
			String message, String filename[]) throws EmailException {

		try {

			HtmlEmail email = new HtmlEmail();
			email.setDebug(true);
			email.setHostName("mail.pbc.com.pk");
			email.setSmtpPort(465);
			// email.setAuthentication("theia@pbc.com.pk","1");
			// email.setAuthenticator(new DefaultAuthenticator("theia@pbc.com.pk", "1"));
			// email.setTLS(true);
			email.setSSL(true);
			// email.setSSLOnConnect(true);

			// email.setSSLCheckServerIdentity(false);
			// email.setStartTLSRequired(true);

			for (int i = 0; i < to.length; i++) {
				email.addTo(to[i]);
			}
			if (cc != null) {
				for (int i = 0; i < cc.length; i++) {
					email.addCc(cc[i]);
				}
			}
			if (bcc != null) {
				for (int i = 0; i < bcc.length; i++) {
					email.addBcc(bcc[i]);
				}
			}

			email.setFrom("theia@pbc.com.pk", "Theia");

			email.setSubject(subject);

			email.setHtmlMsg(message);

			if (filename != null) {

				for (int i = 0; i < filename.length; i++) {
					try {
						EmailAttachment attachment = new EmailAttachment();
						attachment.setPath(Utilities.getEmailAttachmentsPath() + "/" + filename[i]);
						attachment.setDisposition(EmailAttachment.ATTACHMENT);
						attachment.setDescription(filename[i]);
						attachment.setName(filename[i]);
						email.attach(attachment);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}

			return email.send();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return true;
	}

	/**
	 * To send SMS for provided Order ID
	 * 
	 * @param OrderID
	 * @throws IOException
	 */
	public static void sendSMSOrderBookering(long OrderID) throws IOException {
		/*
		 * try{
		 * 
		 * Datasource ds = new Datasource(); ds.createConnection();
		 * 
		 * Statement s = ds.createStatement();
		 * 
		 * long OutletID = 0; String OutletName = ""; long SMSNumber = 0; double
		 * NetAmount = 0; ResultSet rs = s.
		 * executeQuery("SELECT mo.outlet_id, (select name from common_outlets where id = mo.outlet_id) outlet_name, (SELECT mobile_no FROM common_outlets_contacts_sms where outlet_id = mo.outlet_id) mobile_no, mo.net_amount FROM mobile_order mo where mo.id = "
		 * +OrderID); if (rs.first()){ OutletID = rs.getLong(1); OutletName =
		 * rs.getString(2); SMSNumber = rs.getLong(3); NetAmount = rs.getDouble(4); }
		 * 
		 * 
		 * if (SMSNumber > 0 && NetAmount > 0){
		 * 
		 * String message =
		 * OutletID+" - "+OutletName+"\n"+Utilities.getDisplayDateFormat(new
		 * Date())+"\n"; message +=
		 * "Apka order# "+OrderID+" book ker lia gaya hai. Is ki raqam Rs. "+Utilities.
		 * getDisplayCurrencyFormatRounded(NetAmount)+" hai.\n";
		 * 
		 * ResultSet rs2 = s.
		 * executeQuery("select (SELECT concat(package_label, ' ',brand_label)  FROM inventory_products_view where product_id = mop.product_id) product, mop.raw_cases from mobile_order_products mop where id = "
		 * +OrderID+" and is_promotion = 0"); while(rs2.next()){ message +=
		 * rs2.getString(1)+" "+rs2.getInt(2)+"\n"; }
		 * 
		 * ResultSet rs3 = s.
		 * executeQuery("select (SELECT concat(package_label, ' ',brand_label)  FROM inventory_products_view where product_id = mop.product_id) product, mop.total_units from mobile_order_products mop where id = "
		 * +OrderID+" and is_promotion = 1"); while(rs3.next()){ if (rs3.isFirst()){
		 * message += "\nis k saath muft botlain:\n"; } message +=
		 * rs3.getString(1)+" "+rs3.getInt(2)+"\n"; } message +=
		 * "maal wasool kertay waqt invoice zaroor talab kerain. shukria.";
		 * 
		 * int isSent = 0;
		 * 
		 * URL url = new
		 * URL("http://155.135.0.70/default.aspx?number="+SMSNumber+"&msg="+URLEncoder.
		 * encode(message)); URLConnection urlConnection = url.openConnection();
		 * 
		 * HttpURLConnection connection = null; if(urlConnection instanceof
		 * HttpURLConnection){ connection = (HttpURLConnection) urlConnection; }else{
		 * System.out.println("Utilities.sendSMS: Invalid URL"); }
		 * 
		 * BufferedReader in = new BufferedReader( new
		 * InputStreamReader(connection.getInputStream())); String urlString = "";
		 * String current; while((current = in.readLine()) != null) { urlString +=
		 * current; }
		 * 
		 * if (urlString != null && urlString.indexOf("true") == -1){
		 * 
		 * }else{ isSent = 1; }
		 * 
		 * PreparedStatement ps = ds.getConnection().
		 * prepareStatement("insert into sms_shortcode_queue (created_on, message, number, order_id, is_sent, sent_on) values (now(), ?, ?, ?,?,now())"
		 * ); ps.setString(1, Utilities.truncateStringToMax(message, 159));
		 * ps.setLong(2, SMSNumber); ps.setLong(3, OrderID); ps.setInt(4, isSent);
		 * ps.executeUpdate();
		 * 
		 * }
		 * 
		 * 
		 * ds.dropConnection();
		 * 
		 * }catch(Exception e){ e.printStackTrace(); }
		 */

	}

	/**
	 * To add leading zeros in a string until specified length is achieved
	 * 
	 * @param number
	 * @param length
	 * @return
	 */
	public static String addLeadingZeros(String number, int length) {

		String SaleOrderStr = number;
		if (SaleOrderStr.length() < length) {
			int MissingDigits = length - SaleOrderStr.length();
			for (int i = 0; i < MissingDigits; i++) {
				SaleOrderStr = "0" + SaleOrderStr;
			}
		}
		return SaleOrderStr;

	}

	public static String sendPBCHTMLEmailExternalMoiz(String to[], String cc[], String bcc[], String subject,
			String message, String filename[]) throws EmailException {

		try {
			// System.out.println("asdfasdfas ata h");
			HtmlEmail email = new HtmlEmail();
			email.setDebug(true);
			email.setHostName("mail.moizfoods.com");
			email.setSmtpPort(587);
			// email.setAuthentication("theia@pbc.com.pk","smooking");
			email.setAuthenticator(new DefaultAuthenticator("theia@moizfoods.com", "Theia@Pbc.987"));
			// email.setTLS(true);
			// email.setSSL(true);
			// email.setSSLOnConnect(true);

			// email.setSSLCheckServerIdentity(false);
			// email.setStartTLSRequired(true);
			
			email.addTo("fahad.khalid@pbc.com.pk");
			email.addCc("fahad.khalid@pbc.com.pk");
		//	email.addBcc("fahad.khalid@pbc.com.pk");
//			for (int i = 0; i < to.length; i++) {
//				email.addTo(to[i]);
//			}
//			if (cc != null) {
//				for (int i = 0; i < cc.length; i++) {
//					email.addCc(cc[i]);
//				}
//			}
//			if (bcc != null) {
//				for (int i = 0; i < bcc.length; i++) {
//					email.addBcc(bcc[i]);
//				}
//			}

			email.setFrom("theia@moizfoods.com", "Theia");

			email.setSubject(subject);

			email.setHtmlMsg(message);

			if (filename != null) {

				for (int i = 0; i < filename.length; i++) {
					try {
						EmailAttachment attachment = new EmailAttachment();
						attachment.setPath(Utilities.getEmailAttachmentsPath() + "/" + filename[i]);
						attachment.setDisposition(EmailAttachment.ATTACHMENT);
						attachment.setDescription(filename[i]);
						attachment.setName(filename[i]);
						email.attach(attachment);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}

			return email.send();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return true;
	}

	/**
	 * To get From Date and To Date of current order booking week
	 * 
	 * @return
	 */
	public static Date[] getCurrentWeekInDates() {
		Date FromDate = new Date();
		Date ToDate = new Date();

		int i = 1;
		while (i <= 7) {

			Date CurDate = DateUtils.addDays(new Date(), i * (-1));

			Calendar cal = Calendar.getInstance();
			cal.setTime(CurDate);

			int DayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

			if (DayOfWeek == 6) {
				ToDate = CurDate;
			}

			if (DayOfWeek == 7) {

				FromDate = CurDate;

				break;
			}

			i++;

		} // end while

		Date temp[] = new Date[2];
		temp[0] = FromDate;
		temp[1] = ToDate;

		return temp;

	}

	public static boolean isCurrentMonth(Date val) {

		int imonth = Utilities.getMonthNumberByDate(val);
		int iyear = Utilities.getYearByDate(val);

		imonth--;

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		if (imonth == month && iyear == year) {
			return true;
		}

		return false;
	}

	public static int getNumberofDaysOfMonth(int month, int year) {

		int number_Of_DaysInMonth = 0;

		String MonthOfName = "";

		switch (month) {
		case 1:
			MonthOfName = "January";
			number_Of_DaysInMonth = 31;
			break;
		case 2:
			MonthOfName = "February";
			if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
				number_Of_DaysInMonth = 29;
			} else {
				number_Of_DaysInMonth = 28;
			}
			break;
		case 3:
			MonthOfName = "March";
			number_Of_DaysInMonth = 31;
			break;
		case 4:
			MonthOfName = "April";
			number_Of_DaysInMonth = 30;
			break;
		case 5:
			MonthOfName = "May";
			number_Of_DaysInMonth = 31;
			break;
		case 6:
			MonthOfName = "June";
			number_Of_DaysInMonth = 30;
			break;
		case 7:
			MonthOfName = "July";
			number_Of_DaysInMonth = 31;
			break;
		case 8:
			MonthOfName = "August";
			number_Of_DaysInMonth = 31;
			break;
		case 9:
			MonthOfName = "September";
			number_Of_DaysInMonth = 30;
			break;
		case 10:
			MonthOfName = "October";
			number_Of_DaysInMonth = 31;
			break;
		case 11:
			MonthOfName = "November";
			number_Of_DaysInMonth = 30;
			break;
		case 12:
			MonthOfName = "December";
			number_Of_DaysInMonth = 31;
		}
		// System.out.print(MonthOfName + " " + year + " has " + number_Of_DaysInMonth +
		// " days\n");
		return number_Of_DaysInMonth;
	}

	public static Date parseDatewithdash(String val) {
		// System.out.println("In utilities "+val);

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
}
