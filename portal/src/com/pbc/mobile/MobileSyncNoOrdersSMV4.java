package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Blob;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync No Orders", urlPatterns = { "/mobile/MobileSyncNoOrdersSMV4" })
public class MobileSyncNoOrdersSMV4 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileSyncNoOrdersSMV4() {
		super();
		// TODO Auto-generated constructor stub

		System.out.println("MobileSyncNoOrdersSMV4");

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("service() MobileSyncNoOrdersSMV2 AL-MOiz");

		try {

			PrintWriter out = response.getWriter();

			// System.out.println(Utilities.filterString(request.getParameter("SessionID"),
			// 1, 40000));
			MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));

			JSONObject json = new JSONObject();

			

			// if (!mr.isExpired()){
			String NoOrderID = Utilities.filterString(mr.getParameter("order_no"), 1, 100);
			int ReasonID = Utilities.parseInt(mr.getParameter("ReasonID"));
			int OutletID = Utilities.parseInt(mr.getParameter("outlet_id"));
			int MobileUserID = Utilities.parseInt(mr.getParameter("created_by"));
			String Lat = Utilities.filterString(mr.getParameter("lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("lng"), 1, 100);
			String MobileTimestamp = Utilities.filterString(mr.getParameter("timestamp"), 1, 100);
			// Mobile_retailer_stock
			System.out.println("MobileTimestamp: " + MobileTimestamp);
			int qp_product_id[] = Utilities.parseInt(mr.getParameterValues("product_id"));

			int raw_cases[] = Utilities.parseInt(mr.getParameterValues("quantity"));
			int units[] = Utilities.parseInt(mr.getParameterValues("quantity"));
			int is_no_order[] = Utilities.parseInt(mr.getParameterValues("NoOrderID"));

			// New Addition on 8/30/2018
			String Comments = Utilities.filterString(mr.getParameter("order_comments"), 1, 100);
			System.out.println("Comments" + Comments);
			long DistributorID = Utilities.parseLong(mr.getParameter("PJPID"));
			System.out.println("DistributorID: " + DistributorID);
			System.out.println("quantity: " + raw_cases);

			String product_id = "";
			String WhereDistributors = "";
			if (qp_product_id != null && qp_product_id.length > 0) {
				for (int i = 0; i < qp_product_id.length; i++) {
					if (i == 0) {
						product_id += qp_product_id[i];
					} else {
						product_id += ", " + qp_product_id[i];
					}
				}
				WhereDistributors = " and distributor_id in (" + product_id + ") ";
				// out.print(WhereDistributors);
			}
			System.out.println("product_id: " + product_id);

			String Quantity = "";
			String Quantities = "";
			if (raw_cases != null && raw_cases.length > 0) {
				for (int i = 0; i < raw_cases.length; i++) {
					if (i == 0) {
						Quantity += raw_cases[i];
					} else {
						Quantity += ", " + raw_cases[i];
					}
				}
				Quantities = " and distributor_id in (" + Quantity + ") ";
				// out.print(WhereDistributors);
			}
			System.out.println("Quantity: " + Quantity);
			Datasource ds = new Datasource();

			try {

				ds.createConnection();
				ds.startTransaction();

				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Date mobile_date = null;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try {
					mobile_date = dateFormat.parse(MobileTimestamp);

					System.out.println("Parsed Date: " + mobile_date.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

				/*
				 * boolean Insert = true; System.out.
				 * println("select mobile_timestamp from mobile_order_sm_zero where outlet_id="
				 * +OutletID+" and created_by="+MobileUserID+" and mobile_timestamp between "
				 * +Utilities.getSQLDate(mobile_date)+" and "+Utilities.getSQLDateNext(
				 * mobile_date)); ResultSet rsExists = s.
				 * executeQuery("select mobile_timestamp from mobile_order_sm_zero where outlet_id="
				 * +OutletID+" and created_by="+MobileUserID+" and mobile_timestamp between "
				 * +Utilities.getSQLDate(mobile_date)+" and "+Utilities.getSQLDateNext(
				 * mobile_date)); if(rsExists.first()) {
				 * 
				 * Date lastTime = rsExists.getTimestamp("mobile_timestamp");
				 * System.out.println("lastTime: "+lastTime.toString()); long diffInMillis =
				 * mobile_date.getTime() - lastTime.getTime(); long diffInMinutes =
				 * TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
				 * System.out.println("diffInMinutes: "+diffInMinutes); if(diffInMinutes > 3) {
				 * Insert = false; }
				 * 
				 * }
				 */

				
				/*************************
				 * Geo Fancing Start
				 ***********************/

				/*********** New logic **********

				// Getting geofancing permission
				long CalculatedDistance = 0;
				int isGeoFancing = 0;
				int radius = 0;
				String latOutlet = "", lngOutlet = "";
				ResultSet rsOutlet = s.executeQuery(
						"SELECT lat,lng,Is_Geo_Fence, Geo_Radius FROM pep.common_outlets where id=" + OutletID);
				if (rsOutlet.first()) {
					isGeoFancing = rsOutlet.getInt("Is_Geo_Fence");
					radius = rsOutlet.getInt("Geo_Radius");
					latOutlet = rsOutlet.getString("lat");
					lngOutlet = rsOutlet.getString("lng");
				}
				System.out.println(
						"SELECT lat,lng,Is_Geo_Fence, Geo_Radius FROM pep.common_outlets where id=" + OutletID);

				boolean VerifyGeoFancing = true;

				if (isGeoFancing == 1) {
					CalculatedDistance = GetDistance(latOutlet, lngOutlet, Lat, Lng);
					System.out.println("CalculatedDistance : "+CalculatedDistance + " radius : "+radius);
					VerifyGeoFancing = (CalculatedDistance < radius) ? true : false;
				} // end of geofancing Condition

				/**********************************/

			//	System.out.println("VerifyGeoFancing : "+VerifyGeoFancing);
				/*********************
				 * Geo Fancing End
				 *********************/
				

					System.out.println("Inside If");
					// System.out.println("insert into mobile_order_zero (mobile_order_no,
					// outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order,
					// no_order_reason_type) values "+
					// "("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+",
					// '"+MobileTimestamp+"', 1, "+ReasonID+" ) ");
					if (Comments.equals("") || Comments.equals(null)) {
						System.out.println(
								"insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order, no_order_reason_type_v2,distributor_id) values "
										+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat
										+ ", " + Lng + ", '" + MobileTimestamp + "', 1, " + ReasonID + " ,"
										+ DistributorID + ") ");
						s.executeUpdate(
								"insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order, no_order_reason_type_v2,distributor_id) values "
										+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat
										+ ", " + Lng + ", '" + MobileTimestamp + "', 1, " + ReasonID + " ,"
										+ DistributorID + ") ");
					} else {
						s.executeUpdate(
								"insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order, no_order_reason_type_v2,comments,distributor_id) values "
										+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat
										+ ", " + Lng + ", '" + MobileTimestamp + "', 1, " + ReasonID + ",'" + Comments
										+ "' ," + DistributorID + " ) ");

					}

					long OrderID = 0;
					ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
					if (rs2.first()) {
						OrderID = rs2.getLong(1);
					}

					// System.out.println("no_order_product_id"+Arrays.toString(qp_product_id));
					// System.out.println("no_order_raw_cases"+Arrays.toString(raw_cases));
					System.out.println("no_order_units" + Arrays.toString(units));
					System.out.println("no_order_is_no_order" + Arrays.toString(is_no_order));

					if (qp_product_id != null) {
						for (int i = 0; i < qp_product_id.length; i++) {
							System.out.println(
									"insert into mobile_retailer_sm_stock(order_no,product_id,raw_cases,units,is_no_order,outlet_id,created_on,mobile_no_order_id) values("
											+ NoOrderID + "," + qp_product_id[i] + "," + raw_cases[i] + ",0,1,"
											+ OutletID + ",now()," + OrderID + ")");
							s.executeUpdate(
									"insert into mobile_retailer_sm_stock(order_no,product_id,raw_cases,units,is_no_order,outlet_id,created_on,mobile_no_order_id) values("
											+ NoOrderID + "," + qp_product_id[i] + "," + raw_cases[i] + ",0,1,"
											+ OutletID + ",now()," + OrderID + ")");

						}
					}

					
					json.put("success", "true");
			
				s2.close();
				s.close();
				ds.commit();
			} catch (Exception e) {

				ds.rollback();

				e.printStackTrace();
				// System.out.print(e);
				json.put("success", "false");
				json.put("error_code", "106");

			} finally {

				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

//	}else{
//			json.put("success", "false");
//			json.put("error_code", "101");
//		}

			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected long GetDistance(String lat1, String lng1, String lat2, String lng2) {

		Datasource ds = new Datasource();

		long Distance = 0;

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

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

}