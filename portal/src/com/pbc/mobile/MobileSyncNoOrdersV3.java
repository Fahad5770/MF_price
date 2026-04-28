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
@WebServlet(description = "Mobile Sync No Orders", urlPatterns = { "/mobile/MobileSyncNoOrdersV3" })
public class MobileSyncNoOrdersV3 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileSyncNoOrdersV3() {
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

		System.out.println("service() Mobile Sync No Order V2 AL-MOiz");

		try {

			PrintWriter out = response.getWriter();

			// System.out.println(Utilities.filterString(request.getParameter("SessionID"),
			// 1, 40000));
			MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));

			JSONObject json = new JSONObject();

			response.setContentType("json");

			if (!mr.isExpired()) {
				String NoOrderID = Utilities.filterString(mr.getParameter("NoOrderID"), 1, 100);
				int ReasonID = Utilities.parseInt(mr.getParameter("ReasonID"));
				int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
				int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
				String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
				String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
				String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
				// Mobile_retailer_stock

				int qp_product_id[] = Utilities.parseInt(mr.getParameterValues("no_order_product_id"));
				int raw_cases[] = Utilities.parseInt(mr.getParameterValues("no_order_raw_cases"));
				int units[] = Utilities.parseInt(mr.getParameterValues("no_order_units"));
				int is_no_order[] = Utilities.parseInt(mr.getParameterValues("no_order_is_no_order"));

				// New Addition on 8/30/2018
				String Comments = Utilities.filterString(mr.getParameter("NoOrderComment"), 1, 100);
				System.out.println("Comments" + Comments);
				// long DistributorID = Utilities.parseLong(mr.getParameter("DistributorIDD"));

				final double Accuracy_d = Utilities.parseDouble(mr.getParameter("accuracy"));
				final long Accuracy = Math.round(Accuracy_d);

				final String Platform = Utilities.filterString(mr.getParameter("platform"), 1, 100);
				final String AppVersion = Utilities.filterString(mr.getParameter("version"), 1, 100);

				Datasource ds = new Datasource();

				try {

					ds.createConnection();
					ds.startTransaction();

					Statement s = ds.createStatement();
					Statement s2 = ds.createStatement();

					long distributor_id = 0;
					long tso_id = 0;
					long asm_id = 0;
					long rsm_id = 0;
					int pjpid = 0;
					int city = 0;
					int region_id=0;
					
//					ResultSet rsDistributorID = s2.executeQuery("select distinct distributor_id from distributor_beat_plan_view where assigned_to="+MobileUserID+" limit 1");
//					distributor_id = (rsDistributorID.first() ? rsDistributorID.getLong("distributor_id") : 0);
//					
//					System.out.println("SELECT name, city_id, city, region_id, rsm_id as asm_id, (select DISPLAY_NAME from users where id=rsm_id) as asm, snd_id as rsm_id, (select DISPLAY_NAME from users where id=snd_id) as rsm FROM pep.common_distributors where distributor_id=" + distributor_id);
//					ResultSet rsDistributor = s2.executeQuery("SELECT name, city_id, city, region_id, rsm_id as asm_id, (select DISPLAY_NAME from users where id=rsm_id) as asm, snd_id as rsm_id, (select DISPLAY_NAME from users where id=snd_id) as rsm FROM pep.common_distributors where distributor_id=" + distributor_id);
//					if(rsDistributor.first()) {
//						city = rsDistributor.getInt("city_id");
//						region_id = rsDistributor.getInt("region_id");
//						asm_id = rsDistributor.getLong("asm_id");
//						rsm_id = rsDistributor.getLong("rsm_id");
//						
//					}
					
					
					
					System.out.println("select asm_id, (select DISPLAY_NAME from users where id=assigned_to) as tso, id , label from distributor_beat_plan_view  where assigned_to="+MobileUserID+" and distributor_id="+distributor_id+" limit 1");
					ResultSet rsTSO = s2.executeQuery("select asm_id, (select DISPLAY_NAME from users where id=assigned_to) as tso, id , label from distributor_beat_plan_view where assigned_to="+MobileUserID+" and distributor_id="+distributor_id+" limit 1");
					if(rsTSO.first()) {
						tso_id = rsTSO.getLong("asm_id");
						pjpid =rsTSO.getInt("id");
					}
//					ResultSet rsDistributor = s.executeQuery(
//							"select  codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to="
//									+ MobileUserID
//									+ " limit 1 ) pjp_id, (select city_id from common_distributors where distributor_id = codv.distributor_id) city_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id="
//									+ OutletID + " order by codv.distributor_id desc");
//					if (rsDistributor.first()) {
//						distributo_id = rsDistributor.getLong("distributor_id");
//						rsmid = rsDistributor.getLong("rsm_id");
//						sndid = rsDistributor.getLong("snd_id");
//						pjpid = rsDistributor.getInt("pjp_id");
//						city = rsDistributor.getInt("city_id");
//						ResultSet rsASM = s2.executeQuery(
//								"SELECT if(asm_id = 0, null,asm_id) as asm_id FROM distributor_beat_plan where id = "
//										+ pjpid);
//						if (rsASM.first()) {
//							asmid = rsASM.getLong("asm_id");
//						}
//					}
					// System.out.println("insert into mobile_order_zero (mobile_order_no,
					// outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order,
					// no_order_reason_type) values "+
					// "("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+",
					// '"+MobileTimestamp+"', 1, "+ReasonID+" ) ");
					Comments = (Comments.equals("") || Comments.equals(null)) ? "" : Comments;

					System.out.println(
							"insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng,accuracy, mobile_timestamp, is_no_order, no_order_reason_type_v2,comments,distributor_id,beat_plan_id,region_id,asm_id,rsm_id,snd_id,city_id,platform,version) values "
									+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat + ", "
									+ Lng + ", "+Accuracy+" ,'" + MobileTimestamp + "', 1, " + ReasonID + ",'" + Comments + "' ,"
									+ distributor_id + ", " + pjpid
									+ ", "+region_id+", " + tso_id + ", " + asm_id + ", " + rsm_id + ", "+city+", '" + Platform
									+ "', '" + AppVersion + "' ) ");

					s.executeUpdate(
							"insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng,accuracy, mobile_timestamp, is_no_order, no_order_reason_type_v2,comments,distributor_id,beat_plan_id,region_id,asm_id,rsm_id,snd_id,city_id,platform,version) values "
									+ "(" + NoOrderID + ", " + OutletID + ", now(), " + MobileUserID + ", " + Lat + ", "
									+ Lng + ", "+Accuracy+" ,'" + MobileTimestamp + "', 1, " + ReasonID + ",'" + Comments + "' ,"
									+ distributor_id + ", " + pjpid
									+ ", "+region_id+", " + tso_id + ", " + asm_id + ", " + rsm_id + ", "+city+", '" + Platform
									+ "', '" + AppVersion + "' ) ");

					long OrderID = 0;
					ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
					if (rs2.first()) {
						OrderID = rs2.getLong(1);
					}

//					System.out.println("no_order_product_id" + Arrays.toString(qp_product_id));
//					System.out.println("no_order_raw_cases" + Arrays.toString(raw_cases));
//					System.out.println("no_order_units" + Arrays.toString(units));
//					System.out.println("no_order_is_no_order" + Arrays.toString(is_no_order));

					if (qp_product_id != null) {
						for (int i = 0; i < qp_product_id.length; i++) {

							s.executeUpdate(
									"insert into mobile_retailer_stock(order_no,product_id,raw_cases,units,is_no_order,outlet_id,created_on,mobile_no_order_id) values("
											+ NoOrderID + "," + qp_product_id[i] + "," + raw_cases[i] + "," + units[i]
											+ "," + is_no_order[i] + "," + OutletID + ",now()," + OrderID + ") ");

						}
					}

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
