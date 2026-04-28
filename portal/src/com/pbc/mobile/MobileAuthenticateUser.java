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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;


@WebServlet(description = "Mobile Authenticate User", urlPatterns = { "/mobile/MobileAuthenticateUser" })
public class MobileAuthenticateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileAuthenticateUser() {
        super();
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		
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
		
		System.out.println("MobileAuthenticateUserV2");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			
			long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
			String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			
			System.out.println("MobileAuthenticateUserV2 User:"+LoginUsername);
			
			int LogTypeID = 0;
			
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
				
				ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+DeviceID+"'");
				if(rsD.first()){
					
				ResultSet rs = s.executeQuery("select md5('"+LoginPassword+"'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT from users where ID="+LoginUsername+" and IS_ACTIVE=1 ");
				if(rs.first()){
					
					if (rs.getString(1).equals(rs.getString(2)) | LoginPassword.equals(Utilities.getMobileAdminPassword())){
						
						LogTypeID = 4;
						
						
					json.put("success", "true");
					json.put("UserID", LoginUsername);
					json.put("DisplayName", rs.getString("DISPLAY_NAME"));
					json.put("Designation", rs.getString("DESIGNATION"));
					json.put("Department", rs.getString("DEPARTMENT"));
					
					
					// Patch for PJP implementation
					
					
					json.put("BeatPlanID", 1);
					
					ResultSet rs3 = s3.executeQuery("select dbpv.outlet_id, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.assigned_to = "+rs.getString("ID"));
					while(rs3.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("OutletID", rs3.getString("outlet_id"));
						rows.put("OutletName", rs3.getString("outlet_name"));
						rows.put("DayNumber", rs3.getString("day_number"));
						rows.put("Owner", rs3.getString("owner"));
						rows.put("Address", rs3.getString("address"));
						rows.put("Telepohone", rs3.getString("telepohone"));
						rows.put("NFCTagID", rs3.getString("nfc_tag_id"));
						
						jr.add(rows);
						
					}
					System.out.println("MobileAuthenticateUserV2 User:"+LoginUsername+" Step1");
					
					json.put("BeatPlanRows", jr);
					
					
					ResultSet rs2ps = s2.executeQuery("SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="+rs.getString("ID"));
					if(rs2ps.first()){
						json.put("ProductGroupID", rs2ps.getString("employee_product_group_id"));
						//System.out.println("SELECT epl.product_id, ib.label brand, ipa.label package FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and epl.product_group_id="+rs2ps.getString("employee_product_group_id"));
						ResultSet rs3pg = s3.executeQuery("SELECT epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and epl.product_group_id="+rs2ps.getString("employee_product_group_id"));
						while(rs3pg.next()){
							LinkedHashMap rows = new LinkedHashMap();						
							rows.put("ProductID", rs3pg.getString("product_id"));
							rows.put("Brand", rs3pg.getString("brand"));
							rows.put("Package", rs3pg.getString("package"));
							rows.put("SortOrder", rs3pg.getString("sort_order"));
							rows.put("UnitPerCase", rs3pg.getString("unit_per_case"));
							
							rows.put("PackageID", rs3pg.getString("package_id"));
							rows.put("BrandID", rs3pg.getString("brand_id"));
							
							jr2.add(rows);
						}
						json.put("ProductGroupRows", jr2);
					}
					
					
	
					ResultSet rs4 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml FROM inventory_price_list_products where id = 1");
					while( rs4.next() ){
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("id", rs4.getString("id"));
						rows.put("ProductID", rs4.getString("product_id"));
						
						rows.put("PackageID", rs4.getString("package_id"));
						rows.put("BrandID", rs4.getString("brand_id"));
						rows.put("PackageLabel", rs4.getString("package_label"));
						rows.put("BrandLabel", rs4.getString("brand_label"));
						rows.put("UnitPerCase", rs4.getString("unit_per_case"));
						rows.put("LiquidInML", rs4.getString("liquid_in_ml"));
						
						rows.put("RawCasePrice", rs4.getString("raw_case"));
						rows.put("UnitPrice", rs4.getString("unit"));
						
						jr3.add(rows);
					}
					
					json.put("PriceListRows", jr3);
					
					System.out.println("MobileAuthenticateUserV2 User:"+LoginUsername+" Step2");
					// sync promotions
					
					String AllOutletsString = "";
					int counter = 0;
					
					ResultSet rs5 = sr.executeQuery("select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+rs.getString("ID"));
					while( rs5.next() ){
						if( counter == 0 ){
							AllOutletsString += rs5.getString("outlet_id");
						}else{
							AllOutletsString += ", "+rs5.getString("outlet_id");
						}
						
						counter++;
					}
					
					if (AllOutletsString.length() > 0){
						ResultSet rs6 = sr.executeQuery("SELECT * FROM inventory_sales_promotions_active_mview where outlet_id in("+AllOutletsString+") order by outlet_id");
						while(rs6.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("PromotionID", rs6.getString("product_promotion_id"));
							rows.put("OutletID", rs6.getString("outlet_id"));
							
							jr4.add(rows);
							
						}
					}
					///////////////////// get products on which promotion applied /////////////////
					
					if (AllOutletsString.length() > 0){
						ResultSet rs7 = sr.executeQuery("SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("+AllOutletsString+") order by outlet_id");
						while(rs7.next()){
							
							ResultSet rs8 = s3.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_products where id="+rs7.getString("product_promotion_id")+" and type_id = 1");
							while(rs8.next()){
								LinkedHashMap rows = new LinkedHashMap();
								
								rows.put("PromotionID", rs7.getString("product_promotion_id"));
								rows.put("PackageID", rs8.getString("package_id"));
								rows.put("TotalUnits", rs8.getString("total_units"));
								
								jr5 = new JSONArray();
								//System.out.println("SELECT brand_id FROM inventory_sales_promotions_products_brands where id="+rs3.getString("product_promotion_id")+" and type_id = 1 and package_id = "+rs4.getString("package_id"));
								ResultSet rs9 = s4.executeQuery("SELECT brand_id FROM inventory_sales_promotions_products_brands where id="+rs7.getString("product_promotion_id")+" and type_id = 1 and package_id = "+rs8.getString("package_id"));
								while(rs9.next()){
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
					
					System.out.println("MobileAuthenticateUserV2 User:"+LoginUsername+" Step3");
					
					if (AllOutletsString.length() > 0){
						ResultSet rs10 = sr.executeQuery("SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("+AllOutletsString+") order by outlet_id");
						while(rs10.next()){
							
							ResultSet rs11 = s3.executeQuery("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="+rs10.getString("product_promotion_id")+" and ispp.type_id = 2");
							while(rs11.next()){
								LinkedHashMap rows = new LinkedHashMap();
								
								rows.put("PromotionID", rs10.getString("product_promotion_id"));
								rows.put("PackageID", rs11.getString("package_id"));
								rows.put("TotalUnits", rs11.getString("total_units"));
								rows.put("PackageLabel", rs11.getString("label"));
								rows.put("UnitPerCase", rs11.getString("unit_per_case"));
								
								jr7 = new JSONArray();
								ResultSet rs12 = s4.executeQuery("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="+rs10.getString("product_promotion_id")+" and isppb.type_id = 2 and isppb.package_id = "+rs11.getString("package_id"));
								while(rs12.next()){
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
					
					if (AllOutletsString.length() > 0){
						ResultSet rs13 = sr.executeQuery("SELECT * FROM inventory_price_list_active_view where outlet_id in("+AllOutletsString+") ");
						while(rs13.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("PriceListID", rs13.getString("price_list_id"));
							rows.put("OutletID", rs13.getString("outlet_id"));
							rows.put("ProductID", rs13.getString("product_id"));
							rows.put("RawCase", rs13.getString("raw_case"));
							rows.put("Unit", rs13.getString("unit"));
							
							jr9.add(rows);
							
						}
					}
					
					System.out.println("MobileAuthenticateUserV2 User:"+LoginUsername+" Step4");
					
					if (AllOutletsString.length() > 0){
						//System.out.println( " SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in("+AllOutletsString+") " );
						ResultSet rs15 = sr.executeQuery(" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in("+AllOutletsString+") ");
						while(rs15.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("OutletID", rs15.getString("outlet_id"));
							rows.put("ProductID", rs15.getString("product_id"));
							rows.put("Discount", rs15.getString("discount"));
							rows.put("CreatedOn", rs15.getString("created_on"));
							
							jr11.add(rows);
							
						}
					}
					
					ResultSet rs14 = s2.executeQuery("SELECT feature_id FROM user_access where user_id="+LoginUsername);
					while(rs14.next()){
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("FeatureID", rs14.getString("feature_id"));
						
						jr10.add(rows);
					}
					
					json.put("promotions_active", jr4);
					json.put("promotions_products", jr6);
					json.put("promotions_products_free", jr8);
					json.put("ActivePriceListRows", jr9);
					json.put("UserFeatures", jr10);
					json.put("HandDiscount", jr11);
					
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
					
					int MonthToDateProductivity = OrderBooker.getProductivity(LoginUsername, MonthToDate, Today);
					int MonthToDateDropSize = OrderBooker.getDropSize(LoginUsername, MonthToDate, Today);
					double MonthToDateSKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, MonthToDate, Today);
					
					json.put("TodayProductivity", TodayProductivity+"");
					json.put("TodayDropSize", TodayDropSize+"");
					json.put("TodaySKUPerBill", Utilities.getDisplayCurrencyFormatOneDecimal(TodaySKUPerBill)+"");
					
					json.put("MonthToDateProductivity", MonthToDateProductivity+"");
					json.put("MonthToDateDropSize", MonthToDateDropSize+"");
					json.put("MonthToDateSKUPerBill", Utilities.getDisplayCurrencyFormatOneDecimal(MonthToDateSKUPerBill)+"");
					
					OrderBooker.close();
					
					System.out.println("MobileAuthenticateUserV2 User:"+LoginUsername+" Step5");
					
					}else{
						LogTypeID = 7;
						json.put("success", "false");
						json.put("error_code", "104");
					}
					
				}else{
					LogTypeID = 5;
					json.put("success", "false");
					json.put("error_code", "103");
				}
				
				}else{
					LogTypeID = 6;
					json.put("success", "false");
					json.put("error_code", "102");
				}
				
				if (LogTypeID != 0){
					s2.executeUpdate("insert into "+ds.logDatabaseName()+".log_user_login(user_id,password, ip_address, attempted_on,type_id, mobile_uuid) values("+LoginUsername+",'','"+request.getRemoteAddr()+"',now(),"+LogTypeID+",'"+DeviceID+"')");
					
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
			
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		out.close();*/
		
	}
	
}
