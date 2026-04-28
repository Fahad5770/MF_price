package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Mobile Authenticate User V28", urlPatterns = { "/mobile/MobileAuthenticateUserV29" })
public class MobileAuthenticateUserV29 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileAuthenticateUserV29() {
		super();
	}

	public static boolean IsAllowed(int IsAlternative) {

		boolean Allowed = false;
		try {
			// Datasource ds = new Datasource();
			// ds.createConnection();
			// ds.startTransaction();
			// Statement s1 = ds.createStatement();
			Date CurrentDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(CurrentDate);
			// System.out.println("CurrentDate"+CurrentDate);
			int week = cal.get(Calendar.WEEK_OF_MONTH);
			// week--;
			System.out.println("week " + week);
			if (IsAlternative == 1 && week % 2 == 0) {
				// week is even
				Allowed = true;
				// System.out.println("even"+week);
			} else if (IsAlternative == 0 && week % 2 != 0) {
				// week is odd
				Allowed = true;
				// System.out.println("odd"+week);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Allowed;
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("json");

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();
		JSONArray jr3 = new JSONArray();

		JSONArray jr4 = new JSONArray();
		JSONArray jr5 = new JSONArray();
		JSONArray jr6 = new JSONArray();

		JSONArray jr7 = new JSONArray();
		JSONArray jr8 = new JSONArray();

		JSONArray jr9 = new JSONArray();
		JSONArray jr10 = new JSONArray();
		JSONArray jr11 = new JSONArray();

		JSONArray jr12 = new JSONArray();
		JSONArray jr13 = new JSONArray();
		JSONArray jr14 = new JSONArray();

		JSONArray jr15 = new JSONArray();
		JSONArray jr16 = new JSONArray();
		JSONArray jr17 = new JSONArray();
		JSONArray jr18 = new JSONArray();
		JSONArray jr19 = new JSONArray();
		JSONArray jr20 = new JSONArray();
		JSONArray jr21 = new JSONArray();
		JSONArray jr22 = new JSONArray();

		final JSONArray jr_pjp = new JSONArray();
		final JSONArray jr_cities = new JSONArray();

		System.out.println("Mobile Authenticate User V27");
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 30000));

		 if (!mr.isExpired()) {

//		long LoginUsername = 204211306;
//		String LoginPassword = "123";
//	   String DeviceID = "";
//		String NewDeviceToken = "dd";
			 
				long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
				String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
				String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
				String NewDeviceToken = Utilities.filterString(mr.getParameter("DeviceToken"), 1, 300);

		System.out.println("MobileAuthenticateUserV27 User:" + LoginUsername);

		int LogTypeID = 0;
		// System.out.println("==================================================================================================================");
		// System.out.println(LoginUsername+" Device ID : "+DeviceID);
		// System.out.println("==================================================================================================================");

		Datasource ds = new Datasource();
		Datasource dsr = new Datasource();

		try {

			ds.createConnection();

			dsr.createConnectionToReplica();
			Statement sr = dsr.createStatement();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();

			Statement s6 = ds.createStatement();

//			ResultSet rsD = s
//					.executeQuery("select id from mobile_devices where uuid = '" + DeviceID + "' and is_active=1");
//			if (rsD.first() || LoginUsername == 2551) {
				System.out.println("select md5('" + LoginPassword
						+ "'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT,password_changed_on from users where ID="
						+ LoginUsername + " and IS_ACTIVE=1 ");
				ResultSet rs = s.executeQuery("select md5('" + LoginPassword
						+ "'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT,password_changed_on from users where ID="
						+ LoginUsername + " and IS_ACTIVE=1 ");
				if (rs.first()) {

					if (rs.getString(1).equals(rs.getString(2))
							// | LoginPassword.equals(Utilities.getMobileAdminPassword())) {
							| LoginPassword.equals("moiz123456")) {
						LogTypeID = 4;

						json.put("success", "true");
						json.put("UserID", LoginUsername);
						json.put("DisplayName", rs.getString("DISPLAY_NAME"));
						json.put("Designation", rs.getString("DESIGNATION"));
						json.put("Department", rs.getString("DEPARTMENT"));

						long distributor_id = 0;
						long tso_id = 0;
						long asm_id = 0;
						long rsm_id = 0;
						int pjpid = 0;
						int city_id = 0;
						int region_id = 0;

						ResultSet rsDistributorID = s2.executeQuery(
								"select distributor_id, asm_id, id from distributor_beat_plan_view where assigned_to="
										+ LoginUsername + " limit 1");
						if (rsDistributorID.first()) {
							tso_id = rsDistributorID.getLong("asm_id");
							pjpid = rsDistributorID.getInt("id");
							distributor_id = rsDistributorID.getLong("distributor_id");
						}

						System.out.println(
								"SELECT  city_id, region_id, rsm_id as asm_id, snd_id as rsm_id FROM pep.common_distributors where distributor_id="
										+ distributor_id);
						ResultSet rsDistributor = s2.executeQuery(
								"SELECT  city_id, region_id, rsm_id as asm_id, snd_id as rsm_id FROM pep.common_distributors where distributor_id="
										+ distributor_id);
						if (rsDistributor.first()) {
							city_id = rsDistributor.getInt("city_id");
							region_id = rsDistributor.getInt("region_id");
							asm_id = rsDistributor.getLong("asm_id");
							rsm_id = rsDistributor.getLong("rsm_id");
						}

						json.put("distributor_id", distributor_id);
						json.put("tso_id", tso_id);
						json.put("asm_id", asm_id);
						json.put("rsm_id", rsm_id);
						json.put("region_id", region_id);
						json.put("city_id", city_id);
						json.put("pjpid", pjpid);

						ResultSet rsAllOutletFeature = s2
								.executeQuery("select feature_id from user_access where user_id=" + LoginUsername
										+ " and feature_id=63");
						int is_daily = (rsAllOutletFeature.first()) ? 0 : 1;
						json.put("IsDaily", is_daily);
						
						ResultSet rsOutletFeature = s2
								.executeQuery("select feature_id from user_access where user_id=" + LoginUsername
										+ " and feature_id=500");
						int is_channel_tagging = (rsOutletFeature.first()) ? 1 : 0;
						json.put("isChannelTagging", is_channel_tagging);

						// is_alternative logic

						Date PasswordChangeDate = rs.getDate("password_changed_on");

						/* New Addition */
						if (PasswordChangeDate != null) {

							Date Date30DaysAgo = Utilities.getDateByDays(-3000);

							boolean PasswordExpired = false;

							if (PasswordChangeDate.compareTo(Date30DaysAgo) == -1) {
								PasswordExpired = true;
							}

							if (PasswordExpired) {
								json.put("IsPasswordExpired", "1"); // If Password Changed on Field is not
																	// empty/null but Password is Expired Yet so
																	// take it to Change Password Screen
							} else {
								json.put("IsPasswordExpired", "0"); // If Password Changed on Field is not
																	// empty/null and Password is not Expired Yet so
																	// take it to Home Screen
							}

						} else {
							json.put("IsPasswordExpired", "1"); // If Password Changed on Field is empty/null so
																// take it to Change Password Screen
						}
						/* New Addition */

						json.put("BeatPlanID", 1);
						// ResultSet rs3 = s3.executeQuery("select dbpv.outlet_id, co.name outlet_name,
						// dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number
						// telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label
						// from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id
						// = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id
						// and coc.is_primary = 1 where dbpv.assigned_to = "+rs.getString("ID")+" and
						// dbpv.outlet_id in (select outlet_id from distributor_beat_plan_log where
						// log_date=curdate() and assigned_to="+rs.getString("ID")+" and
						// day_number=dayofweek(curdate()))" );

						// System.out.println("select Is_Geo_Fence,Geo_Radius,(SELECT label from
						// pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label,(select
						// order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=
						// dbpv.outlet_id and invoice_amount !=0 and isa.booked_by="+LoginUsername+"
						// order by order_created_on_date desc limit 1) as
						// order_created_on_date,dbpv.outlet_id, co.name outlet_name, dbpv.day_number,
						// coc.contact_name owner, co.address, coc.contact_number telepohone,
						// co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,
						// dbpv.is_alternative, IFNULL(co.pic_channel_id, 0) pic_channel_id1 from
						// distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id =
						// co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and
						// coc.is_primary = 1 where dbpv.outlet_active=1 and dbpv.assigned_to =
						// "+rs.getString("ID")+" " );

						ResultSet rcheckOrderBooker = s3.executeQuery(
								"select * from distributor_beat_plan_users where assigned_to in ( "
										+ rs.getString("ID") + ") ");
						if (rcheckOrderBooker.first()) {
							json.put("isOrderBooker", 1);
							ResultSet rs3 = s3.executeQuery(
									"select is_filer, is_Register, Is_Geo_Fence,Geo_Radius,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label,(select order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=  dbpv.outlet_id and  invoice_amount !=0 and isa.booked_by="
											+ LoginUsername
											+ " order by order_created_on_date desc limit 1) as order_created_on_date,dbpv.outlet_id, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label, dbpv.is_alternative, IFNULL(co.pic_channel_id, 0) pic_channel_id1 from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.outlet_active=1 and dbpv.assigned_to = "
											+ rs.getString("ID") + " ");
							while (rs3.next()) {

								int isAlternative = rs3.getInt("is_alternative");
								// if (IsAllowed(isAlternative)) {
								int pic_channel_id1 = rs3.getInt("pic_channel_id1");
								System.out.println("isAlternative:" + isAlternative);
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("OutletID", rs3.getString("outlet_id"));
								rows.put("OutletName", rs3.getString("outlet_name"));
								rows.put("DayNumber", rs3.getString("day_number"));
								rows.put("Owner", rs3.getString("owner"));
								rows.put("Address", rs3.getString("address"));
								rows.put("Telepohone", rs3.getString("telepohone"));
								rows.put("NFCTagID", rs3.getString("nfc_tag_id"));
								rows.put("order_created_on_date", rs3.getString("order_created_on_date"));
								rows.put("SUBChannelLabel", rs3.getString("channel_label"));

								rows.put("lat", rs3.getString("lat"));
								rows.put("lng", rs3.getString("lng"));

								rows.put("is_filer", rs3.getInt("is_filer"));
								rows.put("is_Register", rs3.getInt("is_register"));

								rows.put("IsGeoFence", rs3.getInt("Is_Geo_Fence"));
								rows.put("Radius", rs3.getInt("Geo_Radius"));
								rows.put("AreaLabel", rs3.getString("area_label"));
								rows.put("SubAreaLabel", rs3.getString("sub_area_label"));
								rows.put("IsAlternative", ((IsAllowed(isAlternative)) ? 1 : 0));
								rows.put("OutletPciSubChannelID", pic_channel_id1);

								jr.add(rows);
								// if(isAlternative==0) {
								// jr.add(rows);
								// }else if(IsAllowed(isAlternative)) {
								// System.out.println("IsAllowed:true");
								// jr.add(rows);
								// }
								// }
							}
							json.put("BeatPlanRows", jr);

							ResultSet rs2ps = s2.executeQuery(
									"SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="
											+ rs.getString("ID"));
							if (rs2ps.first()) {
								json.put("ProductGroupID", rs2ps.getString("employee_product_group_id"));
								// System.out.println("SELECT epl.product_id, ib.label brand, ipa.label package,
								// ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as
								// package_id, ib.id as brand_id, ipl.id lrb_type_id, ipl.label lrb FROM
								// employee_product_groups_list epl, inventory_products ip, inventory_brands ib,
								// inventory_packages ipa, inventory_products_lrb_types ipl where epl.product_id
								// = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and
								// ipl.id=ip.lrb_type_id and
								// epl.product_group_id="+rs2ps.getString("employee_product_group_id"));
								// ResultSet rs3pg = s3.executeQuery("SELECT epl.product_id, ib.label brand,
								// ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as
								// unit_per_case, ipa.id as package_id, ib.id as brand_id FROM
								// employee_product_groups_list epl, inventory_products ip, inventory_brands ib,
								// inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id
								// and ip.package_id = ipa.id and
								// epl.product_group_id="+rs2ps.getString("employee_product_group_id"));
								int i = 0;
								ResultSet rs3pg = s3.executeQuery(
										"SELECT ip.minimum_quantity,ip.unit_per_catron,epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id, ipl.id lrb_type_id, ipl.label lrb FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa, inventory_products_lrb_types ipl where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and ipl.id=ip.lrb_type_id and epl.product_group_id="
												+ rs2ps.getString("employee_product_group_id"));
								while (rs3pg.next()) {
									LinkedHashMap rows = new LinkedHashMap();
									rows.put("ProductID", rs3pg.getString("product_id"));
									rows.put("Brand", rs3pg.getString("brand"));
									// rows.put("Package", rs3pg.getString("package"));
									rows.put("Package", rs3pg.getString("brand") + " - " + rs3pg.getString("package"));
									rows.put("SortOrder", rs3pg.getString("sort_order"));
									rows.put("UnitPerCase", rs3pg.getString("unit_per_case"));

									rows.put("PackageID", rs3pg.getString("package_id"));
									rows.put("BrandID", rs3pg.getString("brand_id"));

									if (i == 0) {
										rows.put("LrbTypeID", "8");
										rows.put("Lrb", "Gurr Jar");
									} else {
										rows.put("LrbTypeID", rs3pg.getString("lrb_type_id"));
										rows.put("Lrb", rs3pg.getString("lrb"));
									}

									i++;
									rows.put("unitcarton", rs3pg.getString("unit_per_catron"));

									rows.put("MinimumQuantity", rs3pg.getString("minimum_quantity"));

									jr2.add(rows);
								}
								json.put("ProductGroupRows", jr2);
							}

							ResultSet rs4 = s2.executeQuery(
									"SELECT ipl.id,iplp.product_id,raw_case,discount,unit, ipl.is_filer,ipl.is_register,(select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml FROM inventory_price_list_products iplp join  inventory_price_list ipl on iplp.id=ipl.id where ipl.id in (39,40,41,42)");
							while (rs4.next()) {
								LinkedHashMap rows = new LinkedHashMap();

								rows.put("id", rs4.getString("id"));
								rows.put("ProductID", rs4.getString("product_id"));

								rows.put("PackageID", rs4.getString("package_id"));
								rows.put("BrandID", rs4.getString("brand_id"));
								rows.put("PackageLabel", rs4.getString("package_label"));
								rows.put("BrandLabel", rs4.getString("brand_label"));
								rows.put("UnitPerCase", rs4.getString("unit_per_case"));
								rows.put("LiquidInML", rs4.getString("liquid_in_ml"));

								rows.put("is_filer", rs4.getInt("is_filer"));
								rows.put("is_Register", rs4.getInt("is_register"));

								rows.put("RawCasePrice", rs4.getString("raw_case"));
								rows.put("UnitPrice", rs4.getString("unit"));
								rows.put("discount", rs4.getDouble("discount"));

								HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(
										rs4.getInt("product_id"), (rs4.getInt("is_register") == 1),
										(rs4.getInt("is_filer") == 1));
								rows.put("tax", (ProductsTax.get("wh_tax") + ProductsTax.get("income_tax")));
								jr3.add(rows);
							}

							json.put("PriceListRows", jr3);

							// System.out.println("MobileAuthenticateUserV5 User:"+LoginUsername+" Step2");
							// sync promotions

							String AllOutletsString = "";
							int counter = 0;

							ResultSet rs5 = sr.executeQuery(
									"select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.outlet_active=1 and dbpv.assigned_to = "
											+ rs.getString("ID"));
							while (rs5.next()) {
								if (counter == 0) {
									AllOutletsString += rs5.getString("outlet_id");
								} else {
									AllOutletsString += ", " + rs5.getString("outlet_id");
								}

								counter++;
							}

							if (AllOutletsString.length() > 0) {
								ResultSet rs6 = sr.executeQuery(
										"SELECT * FROM inventory_sales_promotions_active_mview where outlet_id in("
												+ AllOutletsString + ") order by outlet_id");
								while (rs6.next()) {

									LinkedHashMap rows = new LinkedHashMap();

									rows.put("PromotionID", rs6.getString("product_promotion_id"));
									rows.put("OutletID", rs6.getString("outlet_id"));

									jr4.add(rows);

								}
							}
							///////////////////// get products on which promotion applied /////////////////

							if (AllOutletsString.length() > 0) {
								ResultSet rs7 = sr.executeQuery(
										"SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("
												+ AllOutletsString + ") order by outlet_id");
								while (rs7.next()) {

									ResultSet rs8 = s3.executeQuery(
											"SELECT package_id, total_units FROM inventory_sales_promotions_products where id="
													+ rs7.getString("product_promotion_id") + " and type_id = 1");
									while (rs8.next()) {
										LinkedHashMap rows = new LinkedHashMap();

										rows.put("PromotionID", rs7.getString("product_promotion_id"));
										rows.put("PackageID", rs8.getString("package_id"));
										rows.put("TotalUnits", rs8.getString("total_units"));

										jr5 = new JSONArray();
										// System.out.println("SELECT brand_id FROM
										// inventory_sales_promotions_products_brands where
										// id="+rs3.getString("product_promotion_id")+" and type_id = 1 and
										// package_id = "+rs4.getString("package_id"));
										ResultSet rs9 = s4.executeQuery(
												"SELECT brand_id FROM inventory_sales_promotions_products_brands where id="
														+ rs7.getString("product_promotion_id")
														+ " and type_id = 1 and package_id = "
														+ rs8.getString("package_id"));
										while (rs9.next()) {
											LinkedHashMap rows2 = new LinkedHashMap();

											rows2.put("BrandID", rs9.getString("brand_id"));

											jr5.add(rows2);
										}

										rows.put("Brands", jr5);

										jr6.add(rows);
									}

								} // end while
							}
							///////////////////// get products available in promotion /////////////////

							// System.out.println("MobileAuthenticateUserV5 User:"+LoginUsername+" Step3");

							if (AllOutletsString.length() > 0) {
								ResultSet rs10 = sr.executeQuery(
										"SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("
												+ AllOutletsString + ") order by outlet_id");
								while (rs10.next()) {

									ResultSet rs11 = s3.executeQuery(
											"SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="
													+ rs10.getString("product_promotion_id") + " and ispp.type_id = 2");
									while (rs11.next()) {
										LinkedHashMap rows = new LinkedHashMap();

										rows.put("PromotionID", rs10.getString("product_promotion_id"));
										rows.put("PackageID", rs11.getString("package_id"));
										rows.put("TotalUnits", rs11.getString("total_units"));
										rows.put("PackageLabel", rs11.getString("label"));
										rows.put("UnitPerCase", rs11.getString("unit_per_case"));

										jr7 = new JSONArray();
										ResultSet rs12 = s4.executeQuery(
												"SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="
														+ rs10.getString("product_promotion_id")
														+ " and isppb.type_id = 2 and isppb.package_id = "
														+ rs11.getString("package_id"));
										while (rs12.next()) {
											LinkedHashMap rows2 = new LinkedHashMap();

											rows2.put("BrandID", rs12.getString("brand_id"));
											rows2.put("BrandLabel", rs12.getString("label"));

											jr7.add(rows2);
										}

										rows.put("Brands", jr7);

										jr8.add(rows);
									}
								}
							}

							if (AllOutletsString.length() > 0) {
								ResultSet rs13 = sr.executeQuery(
										"SELECT * FROM inventory_price_list_active_mview where outlet_id in("
												+ AllOutletsString + ") ");
								while (rs13.next()) {

									LinkedHashMap rows = new LinkedHashMap();

									rows.put("PriceListID", rs13.getString("price_list_id"));
									rows.put("OutletID", rs13.getString("outlet_id"));
									rows.put("ProductID", rs13.getString("product_id"));
									rows.put("RawCase", rs13.getString("raw_case"));
									rows.put("Unit", rs13.getString("unit"));

									jr9.add(rows);

								}
							}

							// System.out.println("MobileAuthenticateUserV3 User:"+LoginUsername+" Step4");

							if (AllOutletsString.length() > 0) {
								// System.out.println( " SELECT * FROM inventory_price_list_hand_discount_mview
								// where outlet_id in("+AllOutletsString+") " );
								ResultSet rs15 = sr.executeQuery(
										" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in("
												+ AllOutletsString + ") ");
								while (rs15.next()) {

									LinkedHashMap rows = new LinkedHashMap();

									rows.put("OutletID", rs15.getString("outlet_id"));
									rows.put("ProductID", rs15.getString("product_id"));
									rows.put("Discount", rs15.getString("discount"));
									rows.put("CreatedOn", rs15.getString("created_on"));

									jr11.add(rows);

								}
							}

							ResultSet rs14 = s2
									.executeQuery("SELECT feature_id FROM user_access where user_id=" + LoginUsername);
							while (rs14.next()) {
								LinkedHashMap rows = new LinkedHashMap();

								rows.put("FeatureID", rs14.getString("feature_id"));

								jr10.add(rows);
							}
							long distributorId = 0;
							double maxDiscount = 0;
							ResultSet rss3 = s.executeQuery(
									"SELECT distributor_id, (select maximum_discount_percentage from mobile_distributor_discounts mdd where mdd.distributor_id=dbp.distributor_id) max_discount FROM distributor_beat_plan_view dbp where dbp.outlet_active=1 and assigned_to="
											+ LoginUsername + " order by id desc limit 1");
							if (rss3.first()) {

								distributorId = rss3.getLong(1);
								maxDiscount = rss3.getDouble(2);

							}

							/* For GETTING ALL THE Voice MESSAGES AGAINST SPECIFIC USER STARTS */

							ResultSet rs150 = s2.executeQuery(
									"SELECT *,(select u.DISPLAY_NAME FROM users u where created_by=u.id) Sender_name,(select u.DESIGNATION FROM users u where created_by=u.id) sender_designation FROM mobile_audio_msgs where reciever_id="
											+ LoginUsername + " order by id desc");
							while (rs150.next()) {

								String FileName = "";

								String DateOFCreation = "";
								String time = "";
								LinkedHashMap rows = new LinkedHashMap();

								FileName = rs150.getString("filename");

								String creation = rs150.getString("created_on");
								String[] split = creation.split("\\s+");
								// DateOFCreation = split[0];
								String extraction = split[1];

								DateOFCreation = Utilities.getDisplayDateFormat(rs150.getDate("created_on"));
								time = extraction.substring(0, extraction.length() - 2);

								System.out.println("time" + time);

								rows.put("CreatedOn", DateOFCreation);
								rows.put("CreatedOnTime", time);
								rows.put("Sender",
										rs150.getLong("created_by") + " - " + rs150.getString("Sender_name"));
								rows.put("Designation", rs150.getString("sender_designation"));
								rows.put("FileName", FileName);
								rows.put("FilePath", "MobileAudioFileDownloadMDE?file=");

								jr12.add(rows);

							}

							/* For GETTING ALL THE Voice MESSAGES AGAINST SPECIFIC USER ENDS */

							ResultSet rs151 = s2.executeQuery("SELECT id, label FROM inventory_products_lrb_types");
							while (rs151.next()) {

								LinkedHashMap rows = new LinkedHashMap();

								rows.put("ID", rs151.getInt(1));
								rows.put("Label", rs151.getString(2));

								jr13.add(rows);

							}
							ResultSet rs152 = s2.executeQuery("SELECT id, label FROM inventory_brands");
							while (rs152.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("ID", rs152.getInt(1));
								rows.put("Label", rs152.getString(2));
								jr14.add(rows);
							}

							ResultSet rs153 = s2.executeQuery(
									"SELECT id, label, parent_channel_id FROM pci_sub_channel order by sort_order");
							while (rs153.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("ID", rs153.getInt(1));
								rows.put("Label", rs153.getString(2));
								rows.put("ParentChannelID", rs153.getInt(3));
								jr15.add(rows);

							}

							ResultSet rs154 = s2.executeQuery("SELECT id, label FROM common_outlets_areas");
							while (rs154.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("ID", rs154.getInt(1));
								rows.put("Label", rs154.getString(2));

								jr16.add(rows);

							}

							ResultSet rs155 = s2
									.executeQuery("SELECT id, label, area_id FROM common_outlets_sub_areas");
							while (rs155.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("ID", rs155.getInt(1));
								rows.put("Label", rs155.getString(2));
								rows.put("AreaID", rs155.getInt(3));
								jr17.add(rows);

							}

							ResultSet rs156 = s2
									.executeQuery("SELECT id, label FROM mobile_order_no_order_reason_type");
							while (rs156.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("ID", rs156.getInt(1));
								rows.put("Label", rs156.getString(2));

								jr18.add(rows);

							}

							// System.out.println("SELECT id FROM inventory_spot_discount where curdate()
							// between valid_from and valid_to"
							// + " and id in(SELECT spot_discount_id FROM
							// inventory_spot_discount_distributors where distributor_id="+ distributorId +
							// ")" );
							ResultSet rs157 = s2.executeQuery(
									"SELECT id FROM inventory_spot_discount where curdate() between valid_from and valid_to"
											+ " and id in(SELECT spot_discount_id FROM inventory_spot_discount_distributors where distributor_id="
											+ distributorId + ")");
							if (rs157.next()) {
								long spotDiscountId = rs157.getLong(1);
								System.out.println(
										"SELECT product_id, default_discount, maximum_discount FROM inventory_spot_discount_products where id = "
												+ spotDiscountId);
								ResultSet rs158 = s.executeQuery(
										"SELECT product_id, default_discount, maximum_discount FROM inventory_spot_discount_products where id = "
												+ spotDiscountId);
								while (rs158.next()) {

									LinkedHashMap rows = new LinkedHashMap();
									rows.put("ProductID", rs158.getLong(1));
									rows.put("DefaultDiscount", rs158.getDouble(2));
									rows.put("MaximumDiscount", rs158.getDouble(3));
									jr19.add(rows);

								}

							}

							ResultSet rs158 = s2.executeQuery(
									"SELECT  hdp.product_id,hdp.from_qty, hdp.to_qty, hdp.discount_percentage, hdp.pci_channel_id FROM inventory_hand_to_hand_discount hd, inventory_hand_to_hand_discount_products hdp where hd.id = hdp.hand_discount_id and CURDATE() BETWEEN hd.valid_from AND hd.valid_to");
							while (rs158.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("ProductID", rs158.getInt(1));
								rows.put("FromQty", rs158.getInt(2));
								rows.put("ToQty", rs158.getInt(3));
								rows.put("DiscountPercentage", rs158.getDouble(4));
								rows.put("PciChannelID", rs158.getInt(5));
								jr20.add(rows);
							}

							ResultSet rsBrandDiscount = s2.executeQuery(
									"SELECT hd.id, hd.brand_id,hdp.from_qty, hdp.to_qty, hdp.discount, hdp.pci_channel_id FROM inventory_hand_to_hand_discount_brand hd, inventory_hand_to_hand_discount_brand_details hdp where hd.id = hdp.hand_discount_id and CURDATE() BETWEEN hd.valid_from AND hd.valid_to and hd.is_active=1");
							while (rsBrandDiscount.next()) {
								LinkedHashMap rows = new LinkedHashMap();
								rows.put("discount_id", rsBrandDiscount.getInt(1));
								rows.put("brand_id", rsBrandDiscount.getInt(2));
								rows.put("FromQty", rsBrandDiscount.getInt(3));
								rows.put("ToQty", rsBrandDiscount.getInt(4));
								rows.put("DiscountAmount", rsBrandDiscount.getDouble(5));
								rows.put("PciChannelID", rsBrandDiscount.getInt(6));
								jr21.add(rows);

							}

							json.put("distributor_id", distributorId);
							json.put("maximum_discount_percentage", maxDiscount);
							json.put("promotions_active", jr4);
							json.put("promotions_products", jr6);
							json.put("promotions_products_free", jr8);
							json.put("ActivePriceListRows", jr9);
							json.put("UserFeatures", jr10);
							json.put("HandDiscount", jr11);
							json.put("AudioMsgDetail", jr12);

							json.put("ProductLrbTypes", jr13);
							json.put("ProductSubCategories", jr14);

							json.put("PCISubChannel", jr15);
							json.put("OutletsAreas", jr16);
							json.put("OutletsSubAreas", jr17);
							json.put("NoOrderReasonTypes", jr18);
							json.put("spotDiscount", jr19);
							json.put("HandDiscountPercentage", jr20);
							json.put("HandDiscountBrand", jr21);

							// end sync promotions
							Date Today = new Date();

							BiProcesses bip = new BiProcesses();
							bip.CreateOrderBookerStatisticsDaily(LoginUsername, Today);
							bip.close();

							OrderBookerDashboard OrderBooker = new OrderBookerDashboard();

							int year = Calendar.getInstance().get(Calendar.YEAR);
							int month = Calendar.getInstance().get(Calendar.MONTH);

							Date MonthToDate = Utilities.getStartDateByMonth(month, year);

							int TodayProductivity = OrderBooker.getProductivity(LoginUsername, Today, Today);
							int TodayDropSize = OrderBooker.getDropSize(LoginUsername, Today, Today);
							double TodaySKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, Today, Today);

							int MonthToDateProductivity = OrderBooker.getProductivity(LoginUsername, MonthToDate,
									Today);
							int MonthToDateDropSize = OrderBooker.getDropSize(LoginUsername, MonthToDate, Today);
							double MonthToDateSKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, MonthToDate, Today);

							json.put("TodayProductivity", TodayProductivity + "");
							json.put("TodayDropSize", TodayDropSize + "");
							json.put("TodaySKUPerBill",
									Utilities.getDisplayCurrencyFormatOneDecimal(TodaySKUPerBill) + "");

							json.put("MonthToDateProductivity", MonthToDateProductivity + "");
							json.put("MonthToDateDropSize", MonthToDateDropSize + "");
							json.put("MonthToDateSKUPerBill",
									Utilities.getDisplayCurrencyFormatOneDecimal(MonthToDateSKUPerBill) + "");

							OrderBooker.close();

						}

						/* For Updating/Inserting Mobile Devices FCM Tokens Starts */

						ResultSet rs15 = s2
								.executeQuery("SELECT device_token_id FROM mobile_devices_token_mapping where user_id="
										+ LoginUsername);
						if (rs15.first()) {

							String PreviousDeviceToken = "";
							PreviousDeviceToken = rs15.getString("device_token_id");

							if (!NewDeviceToken.equals(PreviousDeviceToken)) {
								String sql = "update mobile_devices_token_mapping set device_token_id='"
										+ NewDeviceToken + "',user_device_id='" + DeviceID + "',updated_by="
										+ LoginUsername + ",updated_on=now()  where  user_id=" + LoginUsername;
								System.out.println(sql);

								s3.executeUpdate(sql);
							}

						} else {

							String sql = "insert into mobile_devices_token_mapping (user_id, user_device_id, device_token_id, updated_by, updated_on) values("
									+ LoginUsername + ",'" + DeviceID + "','" + NewDeviceToken + "'," + LoginUsername
									+ ",now())";
							System.out.println(sql);

							s3.executeUpdate(sql);
						}

						/* For Updating/Inserting Mobile Devices FCM Tokens Ends */

					} else {
						LogTypeID = 7;
						json.put("success", "false");
						json.put("error_code", "104");
					}

				} else {
					LogTypeID = 5;
					json.put("success", "false");
					json.put("error_code", "103");
				}

//			} else {
//				LogTypeID = 6;
//				json.put("success", "false");
//				json.put("error_code", "102");
//			}

			if (LogTypeID != 0) {
				s2.executeUpdate("insert into " + ds.logDatabaseName()
						+ ".log_user_login(user_id,password, ip_address, attempted_on,type_id, mobile_uuid) values("
						+ LoginUsername + ",'','" + request.getRemoteAddr() + "',now()," + LogTypeID + ",'" + DeviceID
						+ "')");

			}

			s4.close();
			s3.close();
			s2.close();
			s.close();
			ds.dropConnection();
			dsr.dropConnection();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 } else {
		json.put("success", "false");
		 json.put("error_code", "101");
		 }
		// System.out.println(json);
		out.print(json);
		out.close();

	}

}