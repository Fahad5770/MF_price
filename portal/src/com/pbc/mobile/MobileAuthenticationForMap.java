package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Mobile Authention For Map", urlPatterns = { "/mobile/MobileAuthenticationForMap" })
public class MobileAuthenticationForMap extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileAuthenticationForMap() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("json");

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();

		final JSONArray jr_cities = new JSONArray();

		System.out.println("Mobile Authention For Map");

		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 30000));

		if (!mr.isExpired()) {

			long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
			String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);

			System.out.println("Mobile Authention For Map User:" + LoginUsername);

			int LogTypeID = 0;

			Datasource ds = new Datasource();
			try {

				ds.createConnection();

				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				Statement s4 = ds.createStatement();

				if (LoginUsername == 204211264 || LoginUsername == 1003) {
					ResultSet rsD = s.executeQuery(
							"select id from mobile_devices where uuid = '" + DeviceID + "' and is_active=1");
					if (rsD.first()) {

						ResultSet rs = s2.executeQuery("select md5('" + LoginPassword
								+ "'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT,password_changed_on from users where ID="
								+ LoginUsername + " and IS_ACTIVE=1 ");
						if (rs.first()) {

							if (rs.getString(1).equals(rs.getString(2))
									| LoginPassword.equals(Utilities.getMobileAdminPassword())) {

								LogTypeID = 4;

								json.put("success", "true");
								json.put("UserID", LoginUsername);
								json.put("DisplayName", rs.getString("DISPLAY_NAME"));
								json.put("Designation", rs.getString("DESIGNATION"));
								json.put("Department", rs.getString("DEPARTMENT"));

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
																			// empty/null and Password is not Expired
																			// Yet so take it to Home Screen
									}

								} else {
									json.put("IsPasswordExpired", "1"); // If Password Changed on Field is empty/null so
																		// take it to Change Password Screen
								}

								final ResultSet Cities_qry = s3
										.executeQuery("select label as city , id from common_cities");
								while (Cities_qry.next()) {
									final LinkedHashMap Citiesrows = new LinkedHashMap();
									Citiesrows.put("city", Cities_qry.getString("city"));
									Citiesrows.put("id", Cities_qry.getInt("id"));
									jr_cities.add(Citiesrows);
								}
								json.put("Cities_Rows", jr_cities);

								System.out.println(
										"SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="
												+ rs.getString("ID"));
								ResultSet rs2ps = s3.executeQuery(
										"SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="
												+ rs.getString("ID"));
								if (rs2ps.first()) {
									System.out.println(
											"select  ipv.product_id,brand_label,package_label,unit_per_sku,(select sort_order from inventory_packages where id=ipv.package_id) as sort_order,brand_id,package_id,ipv.product_id,concat(package_label,' - ',brand_label) as product, (select label from pep.inventory_products_lrb_types where id=lrb_type_id) as lrb_label, lrb_type_id from inventory_products_view ipv join employee_product_groups_list epl on  epl.product_id = ipv.product_id where epl.product_group_id="
													+ rs2ps.getString("employee_product_group_id")
													+ "  and is_visible=1 order by lrb_type_id");
									ResultSet rs3pg = s3.executeQuery(
											"select  ipv.product_id,brand_label,package_label,unit_per_sku,(select sort_order from inventory_packages where id=ipv.package_id) as sort_order,brand_id,package_id,ipv.product_id,concat(package_label,' - ',brand_label) as product, (select label from pep.inventory_products_lrb_types where id=lrb_type_id) as lrb_label, lrb_type_id from inventory_products_view ipv join employee_product_groups_list epl on  epl.product_id = ipv.product_id where epl.product_group_id="
													+ rs2ps.getString("employee_product_group_id")
													+ "  and is_visible=1 order by lrb_type_id");
									while (rs3pg.next()) {
										final LinkedHashMap rows3 = new LinkedHashMap();
										rows3.put("ProductID", rs3pg.getString("product_id"));
										rows3.put("Brand", rs3pg.getString("brand_label"));
										rows3.put("Package", rs3pg.getString("package_label"));
										rows3.put("SortOrder", rs3pg.getString("sort_order"));
										rows3.put("UnitPerCase", rs3pg.getString("unit_per_sku"));
										rows3.put("PackageID", rs3pg.getString("package_id"));
										rows3.put("BrandID", rs3pg.getString("brand_id"));
										rows3.put("Lrb", rs3pg.getString("lrb_label"));
										rows3.put("LrbTypeID", rs3pg.getString("lrb_type_id"));
										rows3.put("Product", rs3pg.getString("product"));
										jr.add(rows3);
									}
									json.put("ProductGroupRows", jr);
								}
								System.out.println(
										"SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml FROM inventory_price_list_products where id = 1");
								ResultSet rs33 = s3.executeQuery(
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
									jr2.add(rows3);
								}
								json.put("PriceListRows", jr2);

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

					} else {
						LogTypeID = 6;
						json.put("success", "false");
						json.put("error_code", "102");
					}
				} else {
					LogTypeID = 5;
					json.put("success", "false");
					json.put("error_code", "103");
				}

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
		System.out.println(json);
		out.print(json);
		out.close();

	}

}