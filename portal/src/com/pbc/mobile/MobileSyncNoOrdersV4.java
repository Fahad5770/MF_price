package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

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
@WebServlet(description = "Mobile Sync No Orders", urlPatterns = { "/mobile/MobileSyncNoOrdersV4" })
public class MobileSyncNoOrdersV4 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileSyncNoOrdersV4() {
		super();
		// TODO Auto-generated constructor stub

		System.out.println("const()");

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("service() Mobile Sync No Order V4");

		try {

			PrintWriter out = response.getWriter();

			// System.out.println(Utilities.filterString(request.getParameter("SessionID"),
			// 1, 40000));
			MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));

			JSONObject json = new JSONObject();

			response.setContentType("json");

			if (!mr.isExpired()) {
				final String NoOrderID = Utilities.filterString(mr.getParameter("NoOrderID"), 1, 100);
				final int ReasonID = Utilities.parseInt(mr.getParameter("ReasonID"));
				final int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
				final int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
				final String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
				final String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
				final String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
				String Comments = Utilities.filterString(mr.getParameter("NoOrderComment"), 1, 100);
				final double Accuracy_d = Utilities.parseDouble(mr.getParameter("accuracy"));
				final long Accuracy = Math.round(Accuracy_d);
				final String Platform = Utilities.filterString(mr.getParameter("platform"), 1, 100);
				final String AppVersion = Utilities.filterString(mr.getParameter("version"), 1, 100);
				final long distributor_id = Utilities.parseLong(mr.getParameter("distributor_id"));
				final long tso_id = Utilities.parseLong(mr.getParameter("tso_id"));
				final long asm_id = Utilities.parseLong(mr.getParameter("asm_id"));
				final long rsm_id = Utilities.parseLong(mr.getParameter("rsm_id"));
				final int city_id = Utilities.parseInt(mr.getParameter("city_id"));
				final int pjpid = Utilities.parseInt(mr.getParameter("pjpid"));
				final int region_id = Utilities.parseInt(mr.getParameter("region_id"));
				final String device_id = Utilities.filterString(mr.getParameter("device_id"), 1, 1000);

				Datasource ds = new Datasource();

				try {

					ds.createConnection();
					ds.startTransaction();

					Statement s = ds.createStatement();
					Statement s2 = ds.createStatement();

					Comments = (Comments.equals("") || Comments.equals(null)) ? "" : Comments;

					System.out.println(
							"insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng,accuracy, mobile_timestamp, is_no_order, no_order_reason_type_v2,comments,distributor_id,beat_plan_id,region_id,asm_id,rsm_id,snd_id,city_id,platform,version,uuid ) values "
									+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat + ", "
									+ Lng + ", " + Accuracy + " ,'" + MobileTimestamp + "', 1, " + ReasonID + ",'"
									+ Comments + "' ," + distributor_id + ", " + pjpid + ", " + region_id + ", "
									+ tso_id + ", " + asm_id + ", " + rsm_id + ", " + city_id + ", '" + Platform
									+ "', '" + AppVersion + "', '" + device_id + "' ) ");

					s.executeUpdate(
							"insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng,accuracy, mobile_timestamp, is_no_order, no_order_reason_type_v2,comments,distributor_id,beat_plan_id,region_id,asm_id,rsm_id,snd_id,city_id,platform,version,uuid ) values "
									+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat + ", "
									+ Lng + ", " + Accuracy + " ,'" + MobileTimestamp + "', 1, " + ReasonID + ",'"
									+ Comments + "' ," + distributor_id + ", " + pjpid + ", " + region_id + ", "
									+ tso_id + ", " + asm_id + ", " + rsm_id + ", " + city_id + ", '" + Platform
									+ "', '" + AppVersion + "', '" + device_id + "' ) ");

					s2.close();
					s.close();
					ds.commit();
					json.put("success", "true");

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

			} else {
				json.put("success", "false");
				json.put("error_code", "101");
			}

			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
