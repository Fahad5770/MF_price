package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.workflow.Workflow;

@WebServlet(description = "Mobile MDE MobileSyncOutletRegistration V2", urlPatterns = {
		"/mobile/MobileSyncOutletRegistration_V2" })
public class MobileSyncOutletRegistration_V2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileSyncOutletRegistration_V2() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("json");

		System.out.println("Mobile Sync Outlet Registration V2");

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));

		String mobileTimeStamp = Utilities.filterString(mr.getParameter("mobile_timestamp"), 1, 200);
		String mobile_transaction_no = Utilities.filterString(mr.getParameter("mobile_transaction_no"), 1, 200);
		String OutletName = Utilities.filterString(mr.getParameter("outlet_name"), 1, 200);
		int ChannelID = Utilities.parseInt(mr.getParameter("channel_id"));
		String AreaLabel = Utilities.filterString(mr.getParameter("area_label"), 1, 200);
		String SubAreaLabel = Utilities.filterString(mr.getParameter("sub_area_label"), 1, 200);
		String OutletAddress = Utilities.filterString(mr.getParameter("address"), 1, 200);
		String OwnerName = Utilities.filterString(mr.getParameter("owner_name"), 1, 200);
		String OwnerCNIC = Utilities.filterString(mr.getParameter("owner_cnic"), 1, 200);
		String OwnerContactNumber = Utilities.filterString(mr.getParameter("owner_mobile_no"), 1, 200);
		String PurchaserName = Utilities.filterString(mr.getParameter("owner_name"), 1, 200);
		String PurchaserNumber = Utilities.filterString(mr.getParameter("owner_mobile_no"), 1, 200);
		int IsOwnerPurchaser = Utilities.parseInt(mr.getParameter("is_owner_purchaser"));
		double Lat = Utilities.parseDouble(mr.getParameter("lat"));
		double Lng = Utilities.parseDouble(mr.getParameter("lng"));
		double Accuracy = Utilities.parseDouble(mr.getParameter("accuracy"));
		long CreatedBy = Utilities.parseLong(mr.getParameter("created_by"));
		String DeviceUUID = Utilities.filterString(mr.getParameter("uuid"), 1, 200);
		String platform = Utilities.filterString(mr.getParameter("platform"), 1, 200);
		int beatPlanID = Utilities.parseInt(mr.getParameter("pjp_id"));
		long DistributorId = Utilities.parseLong(mr.getParameter("distributor_id"));
		String app_version = Utilities.filterString(mr.getParameter("app_version"), 1, 200);

		int day = Utilities.getDayOfWeekByDate(Utilities.parseDateYYYYMMDD(mobileTimeStamp));

		Datasource ds = new Datasource();

		try {
			if (ChannelID != 0) {

				ds.createConnection();
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();

				// Outlet ID
				long maxID = 0;
				int CityID = 0, id = 0, RegionID = 0;
				String DistributorName = "";

//				System.out.println(
//						"select city_id,region_id,name FROM common_distributors where distributor_id=" + DistributorId);
				ResultSet rs23 = s.executeQuery(
						"select city_id,region_id,name FROM common_distributors where distributor_id=" + DistributorId);
				if (rs23.first()) {
					CityID = rs23.getInt("city_id");
					RegionID = rs23.getInt("region_id");
					DistributorName = rs23.getString("name");
				}

				// System.out.println("select max(id) from common_outlets where city_id=" +
				// CityID);
				ResultSet rs1 = s.executeQuery("select max(id) from common_outlets where city_id=" + CityID);
				if (rs1.first()) {
					maxID = rs1.getLong(1);
				}

				if (maxID == 0) {
					// System.out.println("SELECT * FROM common_outlet_code_range where city_id=" +
					// CityID);
					ResultSet RsRange = s
							.executeQuery("SELECT * FROM common_outlet_code_range where city_id=" + CityID);
					if (RsRange.first()) {
						maxID = RsRange.getLong("start_code");
					}
				} else {
					maxID += 1;
				}

				String RegionShortName = "";
				String RegionName = "";

				// table common_regions data fetching query
				// System.out.println("select * from common_regions where region_id=" +
				// RegionID);
				ResultSet rs6 = s.executeQuery("select * from common_regions where region_id=" + RegionID);
				if (rs6.first()) {
					RegionShortName = rs6.getString("region_short_name");
					RegionName = rs6.getString("region_name");
				}

				String common_outlets_args = maxID + ", '" + OutletName + "', '" + OutletAddress + "', " + RegionID
						+ ", " + DistributorId + ", now(), " + CreatedBy + ", " + CityID + ", '" + OwnerName + "' , '"
						+ OwnerContactNumber + "', '" + OwnerCNIC + "', " + beatPlanID + ", 1" + ", " + Lat + ", " + Lng
						+ ", " + Accuracy + ", " + ChannelID + ", 1, '" + AreaLabel + "', '" + SubAreaLabel + "', "
						+ IsOwnerPurchaser + ", '" + PurchaserName + "', '" + PurchaserNumber + "', '" + mobileTimeStamp
						+ "', '" + DeviceUUID + "', '" + platform + "', '" + app_version + "', " + day + ", "
						+ mobile_transaction_no;

//				System.out.println(
//						"INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_beat_plan_id,is_mobile"
//								+ ",lat,lng,accuracy,pic_channel_id,is_active,area_label,sub_area_label,is_owner_purchaser,purchaser_name, purchaser_number, mobile_time_stamp, device_id, platform, app_version,day, mobile_transaction_no)VALUES"
//								+ "(" + common_outlets_args + ")");
				s.executeUpdate(
						"INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_beat_plan_id,is_mobile"
								+ ",lat,lng,accuracy,pic_channel_id,is_active,area_label,sub_area_label,is_owner_purchaser,purchaser_name, purchaser_number, mobile_time_stamp, device_id, platform, app_version,day, mobile_transaction_no)VALUES"
								+ "(" + common_outlets_args + ")");
//				System.out.println(
//						"INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary,puchaser_name,purchase_number,is_owner_purchaser)values ("
//								+ maxID + ",'" + OwnerName + "','" + OwnerContactNumber + "','" + OwnerCNIC + "',5,1,'"
//								+ PurchaserName + "', '" + PurchaserNumber + "', " + IsOwnerPurchaser + ")");
				s.executeUpdate(
						"INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary,puchaser_name,purchase_number,is_owner_purchaser)values ("
								+ maxID + ",'" + OwnerName + "','" + OwnerContactNumber + "','" + OwnerCNIC + "',5,1,'"
								+ PurchaserName + "', '" + PurchaserNumber + "', " + IsOwnerPurchaser + ")");

//				System.out.println(
//						"INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES"
//								+ "(0," + maxID + ",'" + OutletName + "'," + DistributorId + ",'" + DistributorName
//								+ "','" + RegionShortName + "','" + RegionName + "','" + OwnerName + "','"
//								+ OutletAddress + "','" + OwnerContactNumber
//								+ "','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," + CityID + ")");
				s.executeUpdate(
						"INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES"
								+ "(0," + maxID + ",'" + OutletName + "'," + DistributorId + ",'" + DistributorName
								+ "','" + RegionShortName + "','" + RegionName + "','" + OwnerName + "','"
								+ OutletAddress + "','" + OwnerContactNumber
								+ "','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," + CityID + ")");

				String DayInsertSql = "INSERT INTO distributor_beat_plan_schedule(id,outlet_id,day_number)values ("
						+ beatPlanID + "," + maxID + "," + day + ")";
				// System.out.println(DayInsertSql);
				s.executeUpdate(DayInsertSql);

				// System.out.println("select label from pci_sub_channel where id=" +
				// ChannelID);
				ResultSet rsChannel = s.executeQuery("select label from pci_sub_channel where id=" + ChannelID);
				String channel_label = (rsChannel.first()) ? rsChannel.getString("label") : "";

				/*********** Beat Plan Response ***********/
				JSONArray jr = new JSONArray();
				LinkedHashMap rows = new LinkedHashMap();
				rows.put("OutletID", Long.toString(maxID));
				rows.put("OutletName", OutletName);
				rows.put("DayNumber", Integer.toString(day));
				rows.put("Owner", OwnerName);
				rows.put("Address", OutletAddress);
				rows.put("Telepohone", OwnerContactNumber);
				rows.put("NFCTagID", "0");
				rows.put("order_created_on_date", "0");
				rows.put("SUBChannelLabel", channel_label);

				rows.put("lat", Double.toString(Lat));
				rows.put("lng", Double.toString(Lng));

				rows.put("IsGeoFence", 0);
				rows.put("Radius", 0);

				rows.put("AreaLabel", AreaLabel);
				rows.put("SubAreaLabel", SubAreaLabel);
				rows.put("IsAlternative", 0);
				rows.put("OutletPciSubChannelID", ChannelID);

				jr.add(rows);
				json.put("BeatPlanRows", jr);
				/*********** Beat Plan Response ***********/

				/*********** Promo Response ***********/
				
				List<Integer> PromoIds = new ArrayList<Integer>();
				JSONArray jrPromoResponse = new JSONArray();

				System.out.println(
						"select * from inventory_sales_promotions_distributors pd join inventory_sales_promotions isp on isp.id=pd.product_promotion_id where pd.distributor_id= "+DistributorId+" and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
				ResultSet rsPromoIdByDistributor = s.executeQuery(
						"select * from inventory_sales_promotions_distributors pd join inventory_sales_promotions isp on isp.id=pd.product_promotion_id where pd.distributor_id= "+DistributorId+" and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
				while(rsPromoIdByDistributor.next()) {
					PromoIds.add(rsPromoIdByDistributor.getInt("product_promotion_id"));
				}
				

				
					System.out.println(
							"select product_promotion_id from inventory_sales_promotions_regions ispr join inventory_sales_promotions isp on isp.id=ispr.product_promotion_id  where ispr.region_id="
									+ RegionID + " and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
					ResultSet rsPromoIdByRegion = s.executeQuery(
							"select product_promotion_id from inventory_sales_promotions_regions ispr join inventory_sales_promotions isp on isp.id=ispr.product_promotion_id  where ispr.region_id="
									+ RegionID + " and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
					while(rsPromoIdByRegion.next()) {
						PromoIds.add(rsPromoIdByRegion.getInt("product_promotion_id"));
					}
				

			
					System.out.println(
							"select product_promotion_id from inventory_sales_promotions_pjp ispp join inventory_sales_promotions isp on isp.id=ispp.pjp_id  where ispp.pjp_id=" + beatPlanID
							+ " and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
					ResultSet rsPromoIdByPJP = s.executeQuery(
							"select product_promotion_id from inventory_sales_promotions_pjp ispp join inventory_sales_promotions isp on isp.id=ispp.pjp_id  where ispp.pjp_id=" + beatPlanID
									+ " and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
					while(rsPromoIdByPJP.next()) {
						PromoIds.add(rsPromoIdByPJP.getInt("product_promotion_id"));
					}
				

				
//					System.out.println("SELECT id FROM pep.common_distributor_groups_list where distributor_id="
//							+ DistributorId + " order by id desc limit 1");
					ResultSet rsGroupId = s
							.executeQuery("SELECT id FROM pep.common_distributor_groups_list where distributor_id="
									+ DistributorId + " order by id desc limit 1");
					int groupID = (rsGroupId.first()) ? rsGroupId.getInt("id") : 0;
					System.out.println(
							"select product_promotion_id from inventory_sales_promotions_distributor_groups ispdg join inventory_sales_promotions isp on isp.id=ispdg.group_id  where ispdg.group_id="
									+ groupID + " and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
					ResultSet rsPromoIdByDisGroup = s.executeQuery(
							"select product_promotion_id from inventory_sales_promotions_distributor_groups ispdg join inventory_sales_promotions isp on isp.id=ispdg.group_id  where ispdg.group_id="
									+ groupID + " and isp.is_active=1 and  (`isp`.`valid_to` >= CURDATE()) AND (`isp`.`valid_from` <= CURDATE())");
					while(rsPromoIdByDisGroup.next()) {
						PromoIds.add(rsPromoIdByDisGroup.getInt("product_promotion_id"));
					}
				

				if (PromoIds.size() != 0) {
					for(int promoId : PromoIds) {
//						System.out.println(
//						"insert into inventory_sales_promotions_outlet(product_promotion_id, outlet_id) values("
//								+ promoId + "," + maxID + ")");
						s.executeUpdate(
								"insert into inventory_sales_promotions_outlet(product_promotion_id, outlet_id) values("
										+ promoId + "," + maxID + ")");
						
						LinkedHashMap rows2 = new LinkedHashMap();
						rows2.put("PromotionID", promoId + "");
						rows2.put("OutletID", maxID );

						jrPromoResponse.add(rows2);
					}

					
				}

				
				

				/*********** Promo Response ***********/

				json.put("promotions_active", jrPromoResponse);

				json.put("success", "true");
				json.put("outlet_id", maxID);
				s.close();
				s2.close();
				s3.close();
			} else {
				json.put("success", "false");
				json.put("msg", "Channel Tagging Incorrect ");
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("success", "false");
			json.put("msg", "Server Error ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("success", "false");
			json.put("msg", "Server Error ");
		}

//		  if (!mr.isExpired()){
//		  
//		  }else{
//		  
//		  json.put("success", "false"); json.put("error_code", "101"); }

		out.print(json);
		out.close();

	}

}
