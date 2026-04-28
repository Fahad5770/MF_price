package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;

@WebServlet(description = "Mobile Authenticate User V27", urlPatterns = { "/mobile/MobileAuthenticateUserV27" })
public class MobileAuthenticateUserV27 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileAuthenticateUserV27() {
		super();
	}

	public static boolean IsAllowed(int IsAlternative) {

		boolean Allowed = false;
		try {
		//	Datasource ds = new Datasource();
		//	ds.createConnection();
		//	ds.startTransaction();
		//	Statement s1 = ds.createStatement();
			Date CurrentDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(CurrentDate);
			// System.out.println("CurrentDate"+CurrentDate);
			int week = cal.get(Calendar.WEEK_OF_MONTH);
			//week--;
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
//			//get first monday of month
//			Date CurrentDate = new Date();
//			int month = Utilities.getMonthNumberByDate(CurrentDate);
//			System.out.println("month"+month);
//			int year = Utilities.getYearByDate(CurrentDate);
//			System.out.println("year"+year);
//			Date StartDateThisMonth = Utilities.getStartDateByDate(CurrentDate);   
//			System.out.println("StartDateThisMonth"+StartDateThisMonth);
//			Date iDate = StartDateThisMonth;
//			int Counter=0;
//			int MondayCounter = 0;
//			while(true) {
//				System.out.println("Utilities.getDayOfWeekByDate(iDate)"+Utilities.getDayOfWeekByDate(iDate));
//				if(Utilities.getDayOfWeekByDate(iDate)==2) {
//					MondayCounter++;
//				}
//				if(DateUtils.isSameDay(CurrentDate,iDate)){
//					System.out.println("its same date");
//					break;
//				}
//				if(Counter>31) {
//					break;
//				}
//				iDate = Utilities.getDateByDays(iDate, 1);
//				Counter++;
//			}
//			//System.out.println("outlet_id"+outlet_id);
//			System.out.println("MondayCounter"+MondayCounter);
//			if(IsAlternative ==1 && (MondayCounter==1 || MondayCounter==3 || MondayCounter==5)) {
//				Allowed=true;
//			}else if(IsAlternative ==2 && (MondayCounter==2 || MondayCounter==4)) {
//				Allowed=true;
//			}else  {
//				Allowed=false;
//			}
			/*
			 * String sql = "SELECT * FROM pep.distributor_beat_plan_log where id='" +
			 * Beatplanid + "' and outlet_id='" + outlet_id + "' and log_date=" +
			 * Utilities.getSQLDate(Utilities.getDateByDays(-7))+" and day_number="
			 * +DayNumber;
			 * 
			 * ResultSet rs = s1.executeQuery(sql); if (rs.next()) { Allowed = false;
			 * //System.out.println( //"Exists in previos week " + "id=" + Beatplanid +
			 * " and outlet_id=" + outlet_id); }
			 */

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

		System.out.println("Mobile Authenticate User V28");

		//MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 30000));

	//	if (!mr.isExpired()) {

			long LoginUsername = 204211306;
			String LoginPassword = "123";
		   String DeviceID = "";
			String NewDeviceToken = "dd";

			System.out.println("MobileAuthenticateUserV27 User:" + LoginUsername);

			int LogTypeID = 0;
//			System.out.println("==================================================================================================================");
//			System.out.println(LoginUsername+" Device ID : "+DeviceID);
//			System.out.println("==================================================================================================================");

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
				System.out.println("MobileAuthenticateUse ");

//				ResultSet rsD = s
//						.executeQuery("select id from mobile_devices where uuid = '" + DeviceID + "' and is_active=1");
//				if (rsD.first() || LoginUsername == 2551) {
					System.out.println("select md5('" + LoginPassword
							+ "'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT,password_changed_on from users where ID="
							+ LoginUsername + " and IS_ACTIVE=1 ");
					ResultSet rs = s.executeQuery("select md5('" + LoginPassword
							+ "'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT,password_changed_on from users where ID="
							+ LoginUsername + " and IS_ACTIVE=1 ");
					if (rs.first()) {

						if (rs.getString(1).equals(rs.getString(2))
							//	| LoginPassword.equals(Utilities.getMobileAdminPassword())) {
								| LoginPassword.equals("123")) {
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
									"select Geo_Radius from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.outlet_active=1 and dbpv.assigned_to = "
											+ rs.getString("ID") + " ");
							if (rcheckOrderBooker.first()) {
								json.put("isOrderBooker", 1);
								ResultSet rs3 = s3.executeQuery(
										"select is_filer, is_Register, Is_Geo_Fence,Geo_Radius,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label,(select order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=  dbpv.outlet_id and  invoice_amount !=0 and isa.booked_by="
												+ LoginUsername
												+ " order by order_created_on_date desc limit 1) as order_created_on_date,dbpv.outlet_id, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label, dbpv.is_alternative, IFNULL(co.pic_channel_id, 0) pic_channel_id1 from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.outlet_active=1 and dbpv.assigned_to = "
												+ rs.getString("ID") + " ");
								while (rs3.next()) {

									int isAlternative = rs3.getInt("is_alternative");
							//	if (IsAllowed(isAlternative)) {
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
										rows.put("IsAlternative", ( ( IsAllowed(isAlternative)) ? 1 : 0) );
										rows.put("OutletPciSubChannelID", pic_channel_id1);

										jr.add(rows);
										// if(isAlternative==0) {
										// jr.add(rows);
										// }else if(IsAllowed(isAlternative)) {
										// System.out.println("IsAllowed:true");
										// jr.add(rows);
										// }
								//	}
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
									ResultSet rs3pg = s3.executeQuery(
											"SELECT ip.minimum_quantity,ip.unit_per_catron,epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id, ipl.id lrb_type_id, ipl.label lrb FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa, inventory_products_lrb_types ipl where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and ipl.id=ip.lrb_type_id and epl.product_group_id="
													+ rs2ps.getString("employee_product_group_id"));
									while (rs3pg.next()) {
										LinkedHashMap rows = new LinkedHashMap();
										rows.put("ProductID", rs3pg.getString("product_id"));
										rows.put("Brand", rs3pg.getString("brand"));
										// rows.put("Package", rs3pg.getString("package"));
										rows.put("Package",
												rs3pg.getString("brand") + " - " + rs3pg.getString("package"));
										rows.put("SortOrder", rs3pg.getString("sort_order"));
										rows.put("UnitPerCase", rs3pg.getString("unit_per_case"));

										rows.put("PackageID", rs3pg.getString("package_id"));
										rows.put("BrandID", rs3pg.getString("brand_id"));

										rows.put("LrbTypeID", rs3pg.getString("lrb_type_id"));
										rows.put("Lrb", rs3pg.getString("lrb"));

										rows.put("unitcarton", rs3pg.getString("unit_per_catron"));

										rows.put("MinimumQuantity", rs3pg.getString("minimum_quantity"));

										jr2.add(rows);
									}
									json.put("ProductGroupRows", jr2);
								}

								ResultSet rs4 = s2.executeQuery(
										"SELECT ipl.id,iplp.product_id,raw_case,unit, ipl.is_filer,ipl.is_register,(select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml FROM inventory_price_list_products iplp join  inventory_price_list ipl on iplp.id=ipl.id where ipl.id in (39,40,41,42)");
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
														+ rs10.getString("product_promotion_id")
														+ " and ispp.type_id = 2");
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

								ResultSet rs14 = s2.executeQuery(
										"SELECT feature_id FROM user_access where user_id=" + LoginUsername);
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
								double MonthToDateSKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, MonthToDate,
										Today);

								json.put("TodayProductivity", TodayProductivity + "");
								json.put("TodayDropSize", TodayDropSize + "");
								json.put("TodaySKUPerBill",
										Utilities.getDisplayCurrencyFormatOneDecimal(TodaySKUPerBill) + "");

								json.put("MonthToDateProductivity", MonthToDateProductivity + "");
								json.put("MonthToDateDropSize", MonthToDateDropSize + "");
								json.put("MonthToDateSKUPerBill",
										Utilities.getDisplayCurrencyFormatOneDecimal(MonthToDateSKUPerBill) + "");

								OrderBooker.close();

							} else { //// SO - APP
								json.put("isOrderBooker", 0);

								String Cities = "";
								final ResultSet Cities_qry = s6
										.executeQuery("select label as city , id from common_cities");
								while (Cities_qry.next()) {
									final LinkedHashMap Citiesrows = new LinkedHashMap();
									// Citiesrows.put("ProductID", Cities_qry.getString("product_id"));
									Citiesrows.put("city", Cities_qry.getString("city"));
									Citiesrows.put("id", Cities_qry.getInt("id"));

									jr_cities.add(Citiesrows);
								}
								json.put("Cities_Rows", jr_cities);
								// user is not order booker

								String distributorsList = "";
								String UserTypeCheck = "";

								final String sql_dist = "SELECT group_concat(distinct distributor_id) distributor_ids FROM common_distributors where snd_id="
										+ LoginUsername + " or rsm_id=" + LoginUsername;
								System.out.println(sql_dist);
								final ResultSet rs_dist = s6.executeQuery(sql_dist);
								if (rs_dist.first()) {
									System.out.println("distributor_ids = " + rs_dist.getString(1));
									distributorsList = rs_dist.getString(1);
									if (rs_dist.getString(1) == null) {
										final String sql_dist_asm = "SELECT group_concat(distinct distributor_id) distributor_ids FROM distributor_beat_plan where asm_id="
												+ LoginUsername;
										System.out.println(sql_dist_asm);
										final ResultSet rs_dist_asm = s5.executeQuery(sql_dist_asm);
										if (rs_dist_asm.first()) {
											System.out.println("distributor_ids - asm = " + rs_dist_asm.getString(1));
											distributorsList = rs_dist_asm.getString(1);
										}
									}
								}
								System.out.println("distributorsList = " + distributorsList);
								// String sql_pjp = "SELECT distinct id, label FROM distributor_beat_plan where
								// distributor_id in (" + distributorsList + ") order by label";
								String sql_pjp = "select distinct id, label, city from common_distributors cd join distributor_beat_plan bp on cd.distributor_id=bp.distributor_id where cd.distributor_id in ("
										+ distributorsList + ") and cd.is_active=1 order by label";
								// String sql_pjp = " SELECT dbp.id, label FROM pep.distributor_beat_plan dbp
								// left join distributor_beat_plan_users dbpu on dbp.id=dbpu.id where
								// dbp.asm_id="+LoginUsername+" or dbpu.assigned_to="+LoginUsername+" order by
								// dbp.distributor_id asc";
								if (LoginUsername == 1003 || LoginUsername == 204211264 || LoginUsername == 204211220
										|| LoginUsername == 204230058) {
									// sql_pjp = "SELECT distinct id, concat((select name from common_distributors
									// cd where cd.distributor_id=bp.distributor_id),' - ',label) as label FROM
									// distributor_beat_plan bp order by label";
									sql_pjp = "select distinct id, concat( name ,' - ',label) as label, city  from common_distributors cd join distributor_beat_plan bp on cd.distributor_id=bp.distributor_id where cd.is_active=1";
								}
								System.out.println(sql_pjp);
								final ResultSet rs_pjp = s6.executeQuery(sql_pjp);
								while (rs_pjp.next()) {

									final LinkedHashMap rows = new LinkedHashMap();
									rows.put("value", rs_pjp.getString("id"));
									rows.put("text", rs_pjp.getString("label"));
									rows.put("city", rs_pjp.getString("city"));
									jr_pjp.add(rows);
								}
								json.put("pjp_rows", jr_pjp);

								int is_complex_outlets = 0;
								ResultSet rsCheck = s6.executeQuery(
										"select id from distributor_beat_plan_view where outlet_active=1 and asm_id="
												+ rs.getString("ID"));
								if (rsCheck.first()) {
									UserTypeCheck = "dbpv.asm_id";
								} else {
									ResultSet rsCheck2 = s6.executeQuery(
											"select id from distributor_beat_plan_view where outlet_active=1 and assigned_to="
													+ rs.getString("ID"));
									if (rsCheck2.first()) {
										UserTypeCheck = "dbpv.assigned_to";
									} else {
										ResultSet rsCheck3 = s6.executeQuery(
												"select distributor_id from common_distributors where snd_id="
														+ rs.getString("ID"));
										if (rsCheck3.first()) {
											UserTypeCheck = "dbpv.distributor_id in(select distributor_id from common_distributors where snd_id="
													+ rs.getString("ID") + " )";
											is_complex_outlets = 1;
										} else {
											UserTypeCheck = "dbpv.distributor_id in(select distributor_id from common_distributors where rsm_id="
													+ rs.getString("ID") + " )";
											is_complex_outlets = 1;
										}

									}
								}
								json.put("BeatPlanID", 1);

								String PJPquery = "select dbpv.outlet_id, co.name outlet_name,dbpv.distributor_id ,dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.outlet_active=1 and "
										+ ((is_complex_outlets == 1) ? UserTypeCheck
												: UserTypeCheck + " = " + rs.getString("ID"));
								System.out.println(PJPquery);
								ResultSet rs2 = s3.executeQuery(PJPquery);
								while (rs2.next()) {
									final LinkedHashMap rows2 = new LinkedHashMap();
									rows2.put("OutletID", rs2.getString("outlet_id"));
									rows2.put("OutletName", rs2.getString("outlet_name"));
									rows2.put("DayNumber", rs2.getString("day_number"));
									rows2.put("Owner", rs2.getString("owner"));
									rows2.put("Address", rs2.getString("address"));
									rows2.put("Telepohone", rs2.getString("telepohone"));
									rows2.put("NFCTagID", rs2.getString("nfc_tag_id"));
									rows2.put("DistributorID", rs2.getString("distributor_id"));
									jr.add(rows2);
								}
								System.out.println("MobileAuthenticateUserV3 User:" + LoginUsername + " Step1");
								json.put("BeatPlanRows", jr);

								if (jr_pjp.size() > 0) {
									json.put("BeatPlanRows", null);
								}
								ResultSet rs2ps = s2.executeQuery(
										"SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="
												+ rs.getString("ID"));
								if (rs2ps.first()) {
									json.put("ProductGroupID", rs2ps.getString("employee_product_group_id"));
									// ResultSet rs3pg = s3.executeQuery("SELECT epl.product_id, ib.label brand,
									// ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as
									// unit_per_case, ipa.id as package_id, ib.id as brand_id FROM
									// employee_product_groups_list epl, inventory_products ip, inventory_brands ib,
									// inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id
									// and ip.package_id = ipa.id and epl.product_group_id=" +
									// rs2ps.getString("employee_product_group_id"));
									ResultSet rs3pg = s3.executeQuery(
											"select  ipv.minimum_quantity,unit_per_catron,lrb_type_id,brand_label,package_label,(select unit_per_case from inventory_packages where id=ipv.package_id) as unit_per_case,(select sort_order from inventory_packages where id=ipv.package_id) as sort_order,ipv.brand_id,ipv.package_id,ipv.product_id,concat(package_label,' - ',brand_label) as product, (select label from pep.inventory_products_lrb_types where id=lrb_type_id) as lrb_label from inventory_products_view ipv join employee_product_groups_list epl on  epl.product_id = ipv.product_id where epl.product_group_id="
													+ rs2ps.getString("employee_product_group_id")
													+ "  and is_visible=1 order by lrb_type_id");
									while (rs3pg.next()) {
										final LinkedHashMap rows3 = new LinkedHashMap();
										rows3.put("ProductID", rs3pg.getString("product_id"));
										rows3.put("Brand", rs3pg.getString("brand_label"));
										rows3.put("Package", rs3pg.getString("package_label"));
										rows3.put("SortOrder", rs3pg.getString("sort_order"));
										rows3.put("UnitPerCase", rs3pg.getString("unit_per_case"));
										rows3.put("PackageID", rs3pg.getString("package_id"));
										rows3.put("BrandID", rs3pg.getString("brand_id"));
										rows3.put("Lrb", rs3pg.getString("lrb_label"));
										rows3.put("LrbTypeID", rs3pg.getString("lrb_type_id"));
										rows3.put("Product", rs3pg.getString("product"));
										rows3.put("unitcarton", rs3pg.getString("unit_per_catron"));
										rows3.put("MinimumQuantity", rs3pg.getString("minimum_quantity"));
										jr2.add(rows3);
									}
									json.put("ProductGroupRows", jr2);
								}
								ResultSet rs33 = s2.executeQuery(
										"SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml FROM inventory_price_list_products where id = 1");
								while (rs33.next()) {
									final LinkedHashMap rows3 = new LinkedHashMap();
									rows3.put("id", rs33.getString("id"));
									rows3.put("ProductID", rs33.getString("product_id"));
									rows3.put("PackageID", rs33.getString("package_id"));
									rows3.put("BrandID", rs33.getString("brand_id"));
									rows3.put("PackageLabel", rs33.getString("package_label"));
									rows3.put("BrandLabel", rs33.getString("brand_label"));
									rows3.put("UnitPerCase", rs33.getString("unit_per_case"));
									rows3.put("LiquidInML", rs33.getString("liquid_in_ml"));
									rows3.put("RawCasePrice", rs33.getString("raw_case"));
									rows3.put("UnitPrice", rs33.getString("unit"));
									jr3.add(rows3);
								}
								json.put("PriceListRows", jr3);
								System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + " Step2");
								final ResultSet rs4 = sr.executeQuery(
										"select distinct assigned_to,(select DISPLAY_NAME from users where id=assigned_to) orderbookerName from distributor_beat_plan_view dbpv   where dbpv.outlet_active=1 and "
												+ ((is_complex_outlets == 1) ? UserTypeCheck
														: UserTypeCheck + " = " + rs.getString("ID")));
								while (rs4.next()) {
									final LinkedHashMap rows4 = new LinkedHashMap();
									rows4.put("OrderbookerID", rs4.getString("assigned_to"));
									rows4.put("OrderbookerName", rs4.getString("orderbookerName"));
									jr12.add(rows4);
								}
								json.put("OrderbookersList", jr12);
								String AllOutletsString = "";
								int counter = 0;
								ResultSet rs5 = sr.executeQuery(
										"select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.outlet_active=1 and "
												+ ((is_complex_outlets == 1) ? UserTypeCheck
														: UserTypeCheck + " = " + rs.getString("ID")));
								while (rs5.next()) {
									if (counter == 0) {
										AllOutletsString = String.valueOf(AllOutletsString)
												+ rs5.getString("outlet_id");
									} else {
										AllOutletsString = String.valueOf(AllOutletsString) + ", "
												+ rs5.getString("outlet_id");
									}
									++counter;
								}
								if (AllOutletsString.length() > 0) {
									final ResultSet rs6 = sr.executeQuery(
											"SELECT * FROM inventory_sales_promotions_active_mview where outlet_id in("
													+ AllOutletsString + ") order by outlet_id");
									while (rs6.next()) {
										final LinkedHashMap rows5 = new LinkedHashMap();
										rows5.put("PromotionID", rs6.getString("product_promotion_id"));
										rows5.put("OutletID", rs6.getString("outlet_id"));
										jr4.add(rows5);
									}
								}
								if (AllOutletsString.length() > 0) {
									final ResultSet rs7 = sr.executeQuery(
											"SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("
													+ AllOutletsString + ") order by outlet_id");
									while (rs7.next()) {
										final ResultSet rs8 = s3.executeQuery(
												"SELECT package_id, total_units FROM inventory_sales_promotions_products where id="
														+ rs7.getString("product_promotion_id") + " and type_id = 1");
										while (rs8.next()) {
											final LinkedHashMap rows6 = new LinkedHashMap();
											rows6.put("PromotionID", rs7.getString("product_promotion_id"));
											rows6.put("PackageID", rs8.getString("package_id"));
											rows6.put("TotalUnits", rs8.getString("total_units"));
											jr5 = new JSONArray();
											final ResultSet rs9 = s4.executeQuery(
													"SELECT brand_id FROM inventory_sales_promotions_products_brands where id="
															+ rs7.getString("product_promotion_id")
															+ " and type_id = 1 and package_id = "
															+ rs8.getString("package_id"));
											while (rs9.next()) {
												final LinkedHashMap rows7 = new LinkedHashMap();
												rows7.put("BrandID", rs9.getString("brand_id"));
												jr5.add(rows7);
											}
											rows6.put("Brands", jr5);
											jr6.add(rows6);
										}
									}
								}
								// System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + "
								// Step3");
								if (AllOutletsString.length() > 0) {
									final ResultSet rs10 = sr.executeQuery(
											"SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("
													+ AllOutletsString + ") order by outlet_id");
									while (rs10.next()) {
										final ResultSet rs11 = s3.executeQuery(
												"SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="
														+ rs10.getString("product_promotion_id")
														+ " and ispp.type_id = 2");
										while (rs11.next()) {
											final LinkedHashMap rows6 = new LinkedHashMap();
											rows6.put("PromotionID", rs10.getString("product_promotion_id"));
											rows6.put("PackageID", rs11.getString("package_id"));
											rows6.put("TotalUnits", rs11.getString("total_units"));
											rows6.put("PackageLabel", rs11.getString("label"));
											rows6.put("UnitPerCase", rs11.getString("unit_per_case"));
											jr7 = new JSONArray();
											final ResultSet rs12 = s4.executeQuery(
													"SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="
															+ rs10.getString("product_promotion_id")
															+ " and isppb.type_id = 2 and isppb.package_id = "
															+ rs11.getString("package_id"));
											while (rs12.next()) {
												final LinkedHashMap rows7 = new LinkedHashMap();
												rows7.put("BrandID", rs12.getString("brand_id"));
												rows7.put("BrandLabel", rs12.getString("label"));
												jr7.add(rows7);
											}
											rows6.put("Brands", jr7);
											jr8.add(rows6);
										}
									}
								}
								if (AllOutletsString.length() > 0) {
									final ResultSet rs13 = sr.executeQuery(
											"SELECT * FROM inventory_price_list_active_view where outlet_id in("
													+ AllOutletsString + ") ");
									while (rs13.next()) {
										final LinkedHashMap rows5 = new LinkedHashMap();
										rows5.put("PriceListID", rs13.getString("price_list_id"));
										rows5.put("OutletID", rs13.getString("outlet_id"));
										rows5.put("ProductID", rs13.getString("product_id"));
										rows5.put("RawCase", rs13.getString("raw_case"));
										rows5.put("Unit", rs13.getString("unit"));
										jr9.add(rows5);
									}
								}
								// System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + "
								// Step4");
								if (AllOutletsString.length() > 0) {
									final ResultSet rs14 = sr.executeQuery(
											" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in("
													+ AllOutletsString + ") ");
									while (rs14.next()) {
										final LinkedHashMap rows5 = new LinkedHashMap();
										rows5.put("OutletID", rs14.getString("outlet_id"));
										rows5.put("ProductID", rs14.getString("product_id"));
										rows5.put("Discount", rs14.getString("discount"));
										rows5.put("CreatedOn", rs14.getString("created_on"));
										jr11.add(rows5);
									}
								}
								final ResultSet rs15 = s2.executeQuery(
										"SELECT feature_id FROM user_access where user_id=" + LoginUsername);
								while (rs15.next()) {
									final LinkedHashMap rows5 = new LinkedHashMap();
									rows5.put("FeatureID", rs15.getString("feature_id"));
									jr10.add(rows5);
								}
								final ResultSet rs16 = s5.executeQuery("SELECT id,label FROM pci_sub_channel");
								while (rs16.next()) {
									final LinkedHashMap rows6 = new LinkedHashMap();
									rows6.put("ChannelID", rs16.getString("id"));
									rows6.put("ChannelName", rs16.getString("label"));
									jr13.add(rows6);
								}

								String PerCasesql = "SELECT  distinct id,(select Is_Geo_Fence from common_outlets co where co.id=dbps.outlet_id) Is_Geo_Fence,(select Geo_Radius from common_outlets co where co.id=dbps.outlet_id) Geo_Radius,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select region_id from common_outlets co where co.id=dbps.outlet_id) region_idd,(SELECT  region_name FROM common_regions cr where cr.region_id=region_idd) region_name,(SELECT  region_short_name FROM common_regions cr where cr.region_id=region_idd) region_short,(select distributor_id from common_outlets co where co.id=dbps.outlet_id) distributor_idd,(SELECT name FROM common_distributors cd where cd.distributor_id=distributor_idd) distributor_name,(select channel_id from common_outlets co where co.id=dbps.outlet_id) channel_idd,(SELECT label FROM mrd_census_sub_channel mcsc where mcsc.id=channel_idd) channel_name,(SELECT contact_name FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_name,(SELECT contact_number FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_contact,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by,day_number FROM distributor_beat_plan_schedule dbps where dbps.is_active=1 and id in (SELECT distinct id FROM distributor_beat_plan_view dbpv where dbpv.outlet_active=1 and "
										+ ((is_complex_outlets == 1) ? UserTypeCheck
												: UserTypeCheck + " = " + rs.getString("ID"))
										+ " ) order by id";

								if (LoginUsername == 1003 || LoginUsername == 204211264 || LoginUsername == 204211220
										|| LoginUsername == 204230058) {
									PerCasesql = "SELECT  distinct id,(select Is_Geo_Fence from common_outlets co where co.id=dbps.outlet_id) Is_Geo_Fence,(select Geo_Radius from common_outlets co where co.id=dbps.outlet_id) Geo_Radius,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select region_id from common_outlets co where co.id=dbps.outlet_id) region_idd,(SELECT  region_name FROM common_regions cr where cr.region_id=region_idd) region_name,(SELECT  region_short_name FROM common_regions cr where cr.region_id=region_idd) region_short,(select distributor_id from common_outlets co where co.id=dbps.outlet_id) distributor_idd,(SELECT name FROM common_distributors cd where cd.distributor_id=distributor_idd) distributor_name,(select channel_id from common_outlets co where co.id=dbps.outlet_id) channel_idd,(SELECT label FROM mrd_census_sub_channel mcsc where mcsc.id=channel_idd) channel_name,(SELECT contact_name FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_name,(SELECT contact_number FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_contact,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by,day_number  FROM distributor_beat_plan_view dbps where dbps.outlet_active=1  order by id";
								}

								final ResultSet rs17 = s2.executeQuery(PerCasesql);
								System.out.println(PerCasesql);
								while (rs17.next()) {
									final LinkedHashMap rows8 = new LinkedHashMap();
									rows8.put("OutletID", rs17.getString("outlet_id"));
									rows8.put("OutletName", rs17.getString("outlet_name"));
									rows8.put("OutletAddress", rs17.getString("outlet_address"));
									rows8.put("OutletPJPID", rs17.getString("id"));
									rows8.put("OutletPJPLabel", rs17.getString("pjp_label"));
									rows8.put("DistributorID", rs17.getString("distributor_idd"));
									rows8.put("DistributorName", rs17.getString("distributor_name"));
									rows8.put("OwnerName", rs17.getString("owner_name"));
									rows8.put("OwnerContact", rs17.getString("owner_contact"));
									rows8.put("Region", rs17.getString("region_idd"));
									rows8.put("RegionName", rs17.getString("region_name"));
									rows8.put("RegionShortName", rs17.getString("region_short"));
									rows8.put("ChannelID", rs17.getString("channel_idd"));
									rows8.put("ChannelLabel", rs17.getString("channel_name"));
									rows8.put("DayNumber", rs17.getString("day_number"));
									rows8.put("lat", rs17.getString("lat"));
									rows8.put("lng", rs17.getString("lng"));
									rows8.put("IsGeoFence", rs17.getInt("Is_Geo_Fence"));
									rows8.put("Radius", rs17.getInt("Geo_Radius"));

									jr14.add(rows8);
								}
								json.put("OutletsforMV", jr14);
								final ResultSet rs18 = s5.executeQuery("SELECT id,label FROM pci_sub_channel");
								while (rs18.next()) {
									final LinkedHashMap rows9 = new LinkedHashMap();
									rows9.put("ChannelID", rs18.getString("id"));
									rows9.put("ChannelName", rs18.getString("label"));
									jr15.add(rows9);
								}
								json.put("outletChannelArray", jr13);
								json.put("promotions_active", jr4);
								json.put("promotions_products", jr6);
								json.put("promotions_products_free", jr8);
								json.put("ActivePriceListRows", jr9);
								json.put("UserFeatures", jr10);
								json.put("HandDiscount", jr11);
								json.put("outletArray", jr14);
								final Date Today = new Date();
								final BiProcesses bip = new BiProcesses();
								bip.CreateOrderBookerStatisticsDaily(LoginUsername, Today);
								bip.close();
								final OrderBookerDashboard OrderBooker = new OrderBookerDashboard();
								final int year = Calendar.getInstance().get(1);
								final int month = Calendar.getInstance().get(2);
								final Date MonthToDate = Utilities.getStartDateByMonth(month, year);
								final int TodayProductivity = OrderBooker.getProductivity(LoginUsername, Today, Today);
								final int TodayDropSize = OrderBooker.getDropSize(LoginUsername, Today, Today);
								final double TodaySKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, Today, Today);
								final int MonthToDateProductivity = OrderBooker.getProductivity(LoginUsername,
										MonthToDate, Today);
								final int MonthToDateDropSize = OrderBooker.getDropSize(LoginUsername, MonthToDate,
										Today);
								final double MonthToDateSKUPerBill = OrderBooker.getSKUPerBill(LoginUsername,
										MonthToDate, Today);
								json.put("TodayProductivity",
										new StringBuilder(String.valueOf(TodayProductivity)).toString());
								json.put("TodayDropSize", new StringBuilder(String.valueOf(TodayDropSize)).toString());
								json.put("TodaySKUPerBill",
										new StringBuilder(String
												.valueOf(Utilities.getDisplayCurrencyFormatOneDecimal(TodaySKUPerBill)))
												.toString());
								json.put("MonthToDateProductivity",
										new StringBuilder(String.valueOf(MonthToDateProductivity)).toString());
								json.put("MonthToDateDropSize",
										new StringBuilder(String.valueOf(MonthToDateDropSize)).toString());
								json.put("MonthToDateSKUPerBill",
										new StringBuilder(String.valueOf(
												Utilities.getDisplayCurrencyFormatOneDecimal(MonthToDateSKUPerBill)))
												.toString());
								OrderBooker.close();
								System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + " Step5");
							}

							/* For Updating/Inserting Mobile Devices FCM Tokens Starts */

							ResultSet rs15 = s2.executeQuery(
									"SELECT device_token_id FROM mobile_devices_token_mapping where user_id="
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
										+ LoginUsername + ",'" + DeviceID + "','" + NewDeviceToken + "',"
										+ LoginUsername + ",now())";
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

//				} else {
//					LogTypeID = 6;
//					json.put("success", "false");
//					json.put("error_code", "102");
//				}

				if (LogTypeID != 0) {
					s2.executeUpdate("insert into " + ds.logDatabaseName()
							+ ".log_user_login(user_id,password, ip_address, attempted_on,type_id, mobile_uuid) values("
							+ LoginUsername + ",'','" + request.getRemoteAddr() + "',now()," + LogTypeID + ",'"
							+ DeviceID + "')");

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

//		} else {
//			json.put("success", "false");
//			json.put("error_code", "101");
//		}
	//	System.out.println(json);
		out.print(json);
		out.close();

	}

}