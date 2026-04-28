package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Outlet Visit Duration MV", urlPatterns = {
		"/mobile/MobileSyncOutletVisitDurationMVV2" })
public class MobileSyncOutletVisitDurationMV extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileSyncOutletVisitDurationMV() {
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

		System.out.println("service() Mobile Sync Outlet Visit Duration MV");

		try {

			PrintWriter out = response.getWriter();

			// System.out.println(Utilities.filterString(request.getParameter("SessionID"),
			// 1, 40000));
			MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));

			JSONObject json = new JSONObject();

			response.setContentType("json");

			if (!mr.isExpired()) {
				final long MobileRequestID = Utilities.parseLong(mr.getParameter("id"));
				final long OutletID = Utilities.parseInt(mr.getParameter("outlet_id"));
				int VisitType = Utilities.parseInt(mr.getParameter("file_type"));
				final String MobileTimestamp = Utilities.filterString(mr.getParameter("mobile_timestamp"), 1, 100);
				final int MobileUserID = Utilities.parseInt(mr.getParameter("created_by"));
				final String UUID = Utilities.filterString(mr.getParameter("uuid"), 1, 100);
				final String Platform = Utilities.filterString(mr.getParameter("platform"), 1, 100);
				final String AppVersion = Utilities.filterString(mr.getParameter("version"), 1, 100);
				final String Lat = Utilities.filterString(mr.getParameter("lat"), 1, 100);
				final String Lng = Utilities.filterString(mr.getParameter("lng"), 1, 100);
				final double Accuracy_d = Utilities.parseDouble(mr.getParameter("accuracy"));
				final long Accuracy = Math.round(Accuracy_d);
				final long distributor_id = Utilities.parseLong(mr.getParameter("distributor_id"));
				final long tso_id = Utilities.parseLong(mr.getParameter("tso_id"));
				final long asm_id = Utilities.parseLong(mr.getParameter("asm_id"));
				final long rsm_id = Utilities.parseLong(mr.getParameter("rsm_id"));
				final int city_id = Utilities.parseInt(mr.getParameter("city_id"));
				final int pjpid = Utilities.parseInt(mr.getParameter("pjpid"));
				final int region_id = Utilities.parseInt(mr.getParameter("region_id"));

				// VisitType = (VisitType == 1) ? 8 : 9; // 8 Start Time // 9 End Time

				Datasource ds = new Datasource();

				try {

					ds.createConnection();
					ds.startTransaction();

					Statement s = ds.createStatement();
					Statement s2 = ds.createStatement();

					String args = MobileRequestID + ", '" + MobileTimestamp + "', " + OutletID + ", now(), "
							+ MobileUserID + ", " + VisitType + ", '" + UUID + "', " + Lat + ", " + Lng + ", "
							+ Accuracy + ", " + distributor_id + ", " + region_id + ", " + rsm_id + ", " + asm_id + ", "
							+ tso_id + ", " + pjpid + ", " + city_id + ",0, '" + Platform + "', '" + AppVersion + "'";
					System.out.println(
							"INSERT INTO `pep`.`common_outlets_visit_duration_MV`(`mobile_request_id`,`mobile_timestamp`,`outlet_id`,`created_on`,`created_by`,`visit_type`,`uuid`,`lat`,`lng`,`accuracy`,`distributor_id`,`region_id`,`snd_id`,`rsm_id`,`asm_id`,`beat_plan_id`,`city_id`,`distance_from_store`,`platform`,`version`) VALUES("
									+ args + ")");
					s.executeUpdate(
							"INSERT INTO `pep`.`common_outlets_visit_duration_MV`(`mobile_request_id`,`mobile_timestamp`,`outlet_id`,`created_on`,`created_by`,`visit_type`,`uuid`,`lat`,`lng`,`accuracy`,`distributor_id`,`region_id`,`snd_id`,`rsm_id`,`asm_id`,`beat_plan_id`,`city_id`,`distance_from_store`,`platform`,`version`) VALUES("
									+ args + ")");

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
			System.out.print(json);
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
