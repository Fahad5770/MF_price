package com.mf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class MFAPIFunctions {
	
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
			    ret = String.valueOf(RawCases);  // no commas
			} else {
			    ret = RawCases + "~" + bottles;  // no commas
			}
		}
		return ret;
	}
	

	public static long GetDistance(String lat1, String lng1, double lat2, double lng2) {

		Datasource ds = new Datasource();

		long Distance = 0;

		try {

			ds.createConnection();

			Statement s = ds.createStatement();

			System.out.println("Attendance Distance Query ============>" + "SELECT (( 3959 * acos( cos( radians('"
					+ lat1 + "') ) * cos( radians( '" + lat2 + "' ) ) * cos( radians( '" + lng2 + "' ) - radians('"
					+ lng1 + "') ) + sin ( radians('" + lat1 + "') )  * sin( radians( '" + lat2
					+ "' ) ) ) ) * 1609.34 ) AS distance");

			ResultSet rs = s.executeQuery("SELECT (( 3959 * acos( cos( radians('" + lat1 + "') ) * cos( radians( '"
					+ lat2 + "' ) ) * cos( radians( '" + lng2 + "' ) - radians('" + lng1 + "') ) + sin ( radians('"
					+ lat1 + "') )  * sin( radians( '" + lat2 + "' ) ) ) ) * 1609.34 ) AS distance");
			if (rs.first()) {
				Distance = rs.getLong("distance");
			}

			System.out.println("Attendance Distance ==============>" + Distance);

		} catch (Exception e) {

		}
		return Distance;
	}

	public static void sendJsonResponse(JSONObject json, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(json.toJSONString());
		out.flush();
	}

	public static String getFirstCapitalAlphabet(final String val) throws IOException {
		return val.substring(0, 1).toUpperCase() + val.substring(1);
	}

	public static ResponseModal validateToken(HttpServletRequest request, String endPoint) {
		System.out.println(endPoint);
		ResponseModal responseModal = new ResponseModal();
		String authToken = request.getHeader("Authorization");
		System.out.println(authToken);
		if (authToken == null) {
			if (!endPoint.equals("login")) {
				responseModal.setErrorResponse("Invalid Token");
			}
			responseModal.setStatus(true);
		} else {
			// System.out.println(LowaJWTTocken.validateToken(authToken.substring(7)));
			// System.out.println(endPoint.equals("login"));
			if (!MFJWTToken.validateToken(authToken.substring(7)) && !(endPoint.equals("login"))) {
				// System.out.println(endPoint.equals("Here ......"));
				responseModal.setErrorResponse("Token Expires");

			} else {
				responseModal.setStatus(true);
			}
		}
		return responseModal;

	}

	public static String GetDistributorsOfUser(int user_id) {

		String DistributorID = "";
		Datasource ds = new Datasource();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			int count = 0;
			String Query = "select distributor_id from distributor_beat_plan where id in( "
					+ "SELECT id FROM distributor_beat_plan_users where assigned_to="
					+ user_id + ") or id in ( SELECT id FROM distributor_beat_plan_view where asm_id=" + user_id + " or snd_id="+user_id+" or rsm_id="+user_id+")";

			System.out.println(Query);
			ResultSet rsDistributors = s.executeQuery(Query);
			if (rsDistributors.first()) {
				DistributorID += ((count==0) ? "" : ",") + rsDistributors.getString("distributor_id");
				count++;
			}
		} catch (Exception e) {
			DistributorID = "";
		}
		return DistributorID;
	}

	public static int GetIdOfAppVersion(String app_version) {

		Datasource ds = new Datasource();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();

			System.out.println("select id from ob_app_versions where version='" + app_version + "'");

			ResultSet rs = s.executeQuery("select id from ob_app_versions where version='" + app_version + "'");
			int id = (rs.first()) ? rs.getInt("id") : 0;
			return id;

		} catch (Exception e) {
			return 0;
		}

	}

	// Method to read InputStream content as String
	public static String getResponseString(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try {

			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			System.out.println("Buffered Reader : " + e);
		}
		return sb.toString();
	}
}
