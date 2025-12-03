package com.pbc.mobile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Blob;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.AlmoizParseUtils;
import com.pbc.util.AlmoizPathUtils;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Merchandiser", urlPatterns = { "/mobile/MobileSyncMerchandiserWithImages" })
public class MobileSyncMerchandiserWithImages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileSyncMerchandiserWithImages() {
		super();
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("Merchandiser and Image upload called!!!");

		// System.out.println("MobileUploadOrdersImage service()");

		HttpSession session = request.getSession();

		Datasource ds = new Datasource();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		boolean success = false;

		if (isMultipart == true) {

			long MerchandiserID = 0, OutletID = 0, Accuracy = 0;
			int UserID = 0, imgType = 0;
			double Lat = 0.0, Lng = 0.0;
			String MobileTimestamp = "", platform = "", version = "", deviceId = "";

			int IsResolved = 0;
			String description = "";

			try {

				 
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				/*
				 * iterator = items.iterator(); while (iterator.hasNext()) {
				 * 
				 * FileItem item = (FileItem) iterator.next();
				 * 
				 * if (!item.isFormField()) {
				 * 
				 * String fileName = item.getName();
				 * 
				 * System.out.println("filename = "+fileName);
				 * 
				 * }
				 * 
				 * }
				 */

				// System.out.println("items size = "+items.size());

				while (iterator.hasNext()) {

					// System.out.println("iterator");

					FileItem item = (FileItem) iterator.next();

					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();

						// System.out.println(name + " " +value);

						if (name.equals("MerchandiserID")) {
							MerchandiserID = AlmoizParseUtils.parseLong(value);
						}

						if (name.equals("OutletID")) {
							OutletID = AlmoizParseUtils.parseLong(value);
						}

						if (name.equals("UserID")) {
							UserID = AlmoizParseUtils.parseInt(value);
						}

						if (name.equals("Lat")) {
							Lat = AlmoizParseUtils.parseDouble(value);
						}

						if (name.equals("Lng")) {
							Lng = AlmoizParseUtils.parseDouble(value);
						}

						if (name.equals("accuracy")) {
							Accuracy = Math.round(AlmoizParseUtils.parseDouble(value));
						}

						if (name.equals("MobileTimestamp")) {
							MobileTimestamp = Utilities.filterString(value, 1, 100);
						}

						if (name.equals("platform")) {
							platform = value;
						}

						if (name.equals("version")) {
							version = value;
						}

						if (name.equals("imgType")) {
							imgType = AlmoizParseUtils.parseInt(value);
						}

						if (name.equals("deviceId")) {
							deviceId = value;
						}

					}
				}

				ds.createConnection();
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();

				/**********************************
				 * Check in master Table
				 ******************************************/

				ResultSet rsExists = s
						.executeQuery("SELECT id FROM pep.mobile_merchandiser where mobile_order_no=" + MerchandiserID);
				long TabltMerchendisingId = (rsExists.first()) ? rsExists.getLong("id") : 0;

				if (TabltMerchendisingId < 1) {

					long distributor_id = 0;
					long rsm_id = 0;
					long asm_id = 0;
					long tso_id = 0;
					int pjpid = 0;
					int city = 0;
					int region_id=0;
					
					ResultSet rsDistributorID = s2.executeQuery("select distinct distributor_id from distributor_beat_plan_view where assigned_to="+MerchandiserID+" limit 1");
					distributor_id = (rsDistributorID.first() ? rsDistributorID.getLong("distributor_id") : 0);
					
					System.out.println("SELECT name, city_id, city, region_id, rsm_id as asm_id, (select DISPLAY_NAME from users where id=rsm_id) as asm, snd_id as rsm_id, (select DISPLAY_NAME from users where id=snd_id) as rsm FROM pep.common_distributors where distributor_id=" + distributor_id);
					ResultSet rsDistributor = s2.executeQuery("SELECT name, city_id, city, region_id, rsm_id as asm_id, (select DISPLAY_NAME from users where id=rsm_id) as asm, snd_id as rsm_id, (select DISPLAY_NAME from users where id=snd_id) as rsm FROM pep.common_distributors where distributor_id=" + distributor_id);
					if(rsDistributor.first()) {
						city = rsDistributor.getInt("city_id");
						region_id = rsDistributor.getInt("region_id");
						asm_id = rsDistributor.getLong("asm_id");
						rsm_id = rsDistributor.getLong("rsm_id");
						
					}
					
					
					
					System.out.println("select asm_id, (select DISPLAY_NAME from users where id=assigned_to) as tso, id , label from distributor_beat_plan_view  where assigned_to="+MerchandiserID+" and distributor_id="+distributor_id+" limit 1");
					ResultSet rsTSO = s2.executeQuery("select asm_id, (select DISPLAY_NAME from users where id=assigned_to) as tso, id , label from distributor_beat_plan_view where assigned_to="+MerchandiserID+" and distributor_id="+distributor_id+" limit 1");
					if(rsTSO.first()) {
						tso_id = rsTSO.getLong("asm_id");
						pjpid =rsTSO.getInt("id");
					}

//					ResultSet rsDistributor = s.executeQuery(
//							"select  codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to="
//									+ UserID
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

					System.out.println(
							"INSERT INTO `pep`.`mobile_merchandiser`(`mobile_order_no`,`mobile_timestamp`,`outlet_id`,`created_on`,`created_by`,`uuid`,`platform`,`lat`,`lng`,`accuracy`,`beat_plan_id`,`distributor_id`,`region_id`,`asm_id`,`rsm_id`,`snd_id`,`version`,`city_id`)"
									+ " VALUES(" + MerchandiserID + ", '" + MobileTimestamp + "', " + OutletID
									+ ", now(), " + UserID + ", '" + deviceId + "', '" + platform + "', " + Lat + ", "
									+ Lng + ", " + Accuracy + ", " + pjpid + ", " + distributor_id
									+ ", "+region_id+", " + tso_id + ", " + asm_id + ", " + rsm_id + ", '" + version
									+ "', " + city + " )");
					s.executeUpdate(
							"INSERT INTO `pep`.`mobile_merchandiser`(`mobile_order_no`,`mobile_timestamp`,`outlet_id`,`created_on`,`created_by`,`uuid`,`platform`,`lat`,`lng`,`accuracy`,`beat_plan_id`,`distributor_id`,`region_id`,`asm_id`,`rsm_id`,`snd_id`,`version`,`city_id`)"
									+ " VALUES(" + MerchandiserID + ", '" + MobileTimestamp + "', " + OutletID
									+ ", now(), " + UserID + ", '" + deviceId + "', '" + platform + "', " + Lat + ", "
									+ Lng + ", " + Accuracy + ", " + pjpid + ", " + distributor_id
									+ ", "+region_id+", " + tso_id + ", " + asm_id + ", " + rsm_id + ", '" + version
									+ "', " + city + " )");

					final ResultSet rsNewId = s2.executeQuery("select LAST_INSERT_ID()");
					if (rsNewId.first()) {
						TabltMerchendisingId = rsNewId.getLong(1);
					}

				}

				/**********************************
				 * Check in master Table
				 ******************************************/

				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {

				
						/*
						 * if( item.getContentType().toString().equals("audio/mpeg3") ){ FileTypeID = 2;
						 * }
						 */

						String fileName = item.getName();
						// System.out.println("fileName ==============>>>> "+fileName+" - Index of
						// "+fileName.indexOf("Outlet"));

						Date d = new Date();
						int month = AlmoizDateUtils.getMonthNumberByDate(d);
						int year = AlmoizDateUtils.getYearByDate(d);
						File path = new File(AlmoizPathUtils.getMerchandiserImagePath(year, month, OutletID));

						File uploadedFile = new File(path + "/Mer_" + MerchandiserID + "_" + fileName);
						item.write(uploadedFile);

						try {
							System.out.println(
									"INSERT INTO `pep`.`mobile_merchandiser_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`)"
											+ "VALUES(" + TabltMerchendisingId + ",'"
											+ ("Mer_" + MerchandiserID + "_" + fileName) + "','"
											+ (path + "/Mer_" + MerchandiserID + "_" + fileName) + "',now(), " + UserID
											+ "," + imgType + ", " + month + ", " + year + " )");
							s2.executeUpdate(
									"INSERT INTO `pep`.`mobile_merchandiser_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`)"
											+ "VALUES(" + TabltMerchendisingId + ",'"
											+ ("Mer_" + MerchandiserID + "_" + fileName) + "','"
											+ (path + "/Mer_" + MerchandiserID + "_" + fileName) + "',now(), " + UserID
											+ "," + imgType + ", " + month + ", " + year + " )");
							success = true;
						} catch (SQLException e) {
							e.printStackTrace();
						}

					}
				}

				s2.close();
				s.close();

				PrintWriter out = response.getWriter();
				if (success == true) {
					out.print("{\"success\": true}");
				} else {
					response.sendError(500);
				}

				out.close();

			} catch (Exception e) {
				response.sendError(500);
				e.printStackTrace();
			} finally {

				try {
					ds.dropConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

}
