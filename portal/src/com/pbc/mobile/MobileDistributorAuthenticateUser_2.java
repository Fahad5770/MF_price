package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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


@WebServlet(description = "Mobile Distributor Authenticate User", urlPatterns = { "/mobile/MobileDistributorAuthenticateUser_2" })
public class MobileDistributorAuthenticateUser_2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileDistributorAuthenticateUser_2() {
        super();
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		JSONArray jr20 = new JSONArray();
		JSONArray jr21 = new JSONArray();
		JSONArray jr22= new JSONArray();
		
		JSONArray jr23 = new JSONArray();
		JSONArray jr24 = new JSONArray();
		
		JSONArray jr25 = new JSONArray();
		JSONArray jr26 = new JSONArray();
		JSONArray jr27 = new JSONArray();
		
		
		//System.out.println("hello15");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			
			long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
			String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			
			int LogTypeID = 0;
			long DistributorID=0;
			
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
				Statement s7 = ds.createStatement();
				Statement s8 = ds.createStatement();
				Statement s9 = ds.createStatement();
				Statement s10 = ds.createStatement();
				
				ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+DeviceID+"'");
				if(rsD.first()){
					
				ResultSet rs = s.executeQuery("select md5('"+LoginPassword+"'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT, distributor_id, (select name from common_distributors cd where cd.distributor_id=u.distributor_id) dist_name from users u where ID="+LoginUsername+" and IS_ACTIVE=1 ");
				if(rs.first()){
					
					if (rs.getString(1).equals(rs.getString(2)) | LoginPassword.equals(Utilities.getMobileAdminPassword())){
						
						LogTypeID = 4;
						
						
					json.put("success", "true");
					json.put("UserID", LoginUsername);
					json.put("DisplayName", rs.getString("DISPLAY_NAME"));
					json.put("Designation", rs.getString("DESIGNATION"));
					json.put("Department", rs.getString("DEPARTMENT"));
					json.put("DistributorID", rs.getString("distributor_id")); 
					json.put("DistributorName", rs.getString("dist_name"));
					
					DistributorID = rs.getLong("distributor_id");
					/*
					ResultSet rs2 = s2.executeQuery("SELECT beat_plan_id FROM employee_beat_plan where assigned_to="+rs.getString("ID"));
					if(rs2.first()){
						json.put("BeatPlanID", rs2.getString("beat_plan_id"));
						
						ResultSet rs3 = s3.executeQuery("SELECT *, outlet_id as bp_outlet_id , (SELECT concat(Outlet_Name, ' ', Bsi_Name) outlet_name FROM outletmaster where outlet_id = bp_outlet_id) outlet_name, (SELECT Owner FROM outletmaster where outlet_id = bp_outlet_id) owner, (SELECT address FROM outletmaster where outlet_id = bp_outlet_id) address, (SELECT Telepohone FROM outletmaster where outlet_id = bp_outlet_id) telepohone FROM employee_beat_plan_schedule where beat_plan_id="+rs2.getString("beat_plan_id"));
						while(rs3.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							rows.put("OutletID", rs3.getString("outlet_id"));
							rows.put("OutletName", rs3.getString("outlet_name"));
							rows.put("DayNumber", rs3.getString("day_number"));
							rows.put("Owner", rs3.getString("owner"));
							rows.put("Address", rs3.getString("address"));
							rows.put("Telepohone", rs3.getString("telepohone"));
							
							
							jr.add(rows);
							
						}
						json.put("BeatPlanRows", jr);
					}*/
					
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
					
					
/***************************************************************************Farhan Work Starts***********************/
					
				//geting max(End Date)  and 2nd max date(Start Date) starts
					
					String maxDateQuery="";
					String DispatchDataQuery="";
					String InnoviceDataQuery="";
					Date TodayEndDate=null;
					
				//Date variables;
					Date TodayStartDate=null;
					Date StartDate=null;
					Date YesterdayEndDate=null;
					Date YesterdayStartDate=null;
					
				//Json Date variables
					String ToEndDate ="";
					String ToStartDate="";
					String YeEndDate="";
					String YeStartDate="";
					
					maxDateQuery="SELECT max(created_on) end_date FROM pep.inventory_sales_dispatch where distributor_id= "+DistributorID;
					ResultSet rs20 = s5.executeQuery(maxDateQuery);
					while(rs20.next()){
						StartDate= rs20.getDate("end_date");
						
						TodayEndDate=Utilities.getDateByDays(StartDate,+1);
						SimpleDateFormat formatneeded=new SimpleDateFormat("YYYY-MM-dd");
						ToEndDate = formatneeded.format(TodayEndDate);
						System.out.println("ToEndDate: "+ToEndDate);
						TodayStartDate=Utilities.getDateByDays(TodayEndDate,-1);
						ToStartDate = formatneeded.format(TodayStartDate);
						System.out.println("ToStartDate: "+ToStartDate);
						YesterdayEndDate=TodayStartDate;
						YeEndDate = formatneeded.format(YesterdayEndDate);
						System.out.println("YeEndDate: "+YeEndDate);
						YesterdayStartDate=Utilities.getDateByDays(TodayStartDate,-1);
						YeStartDate = formatneeded.format(YesterdayStartDate);
						System.out.println("YeStartDate: "+YeStartDate);
						//System.out.println("YesterdayStartDate"+"-"+"TodayStartDate ==> "+YesterdayStartDate+"-" + TodayStartDate);
					}
					
			//geting max(End Date)  and 2nd max date(Start Date) ends
								
			//geting vechile and Driver and Dispatch Type starts	
					//DistributorID=100914;
					//int counter1=0;
					DispatchDataQuery=("SELECT distinct vehicle_id, (SELECT vehicle_no FROM distribtuor_vehicles where id=vehicle_id) vehicle_name, driver_id, (SELECT name FROM distributor_employees where id=driver_id) driver_name, id,dispatch_type,created_on FROM inventory_sales_dispatch where distributor_id="+DistributorID+" and created_on between "+Utilities.getSQLDate(YesterdayStartDate)+" and "+Utilities.getSQLDateNext(TodayStartDate));
					ResultSet rs21 = s6.executeQuery(DispatchDataQuery);
					while(rs21.next()){
						LinkedHashMap Dispatchrows = new LinkedHashMap();
						Dispatchrows.put("VehcileID", rs21.getString("vehicle_id"));
						Dispatchrows.put("VehcileName", rs21.getString("vehicle_name"));
						Dispatchrows.put("DriverID", rs21.getString("driver_id"));
						Dispatchrows.put("DriverName", rs21.getString("driver_name"));
						Dispatchrows.put("DispatchID", rs21.getString("id"));
						Dispatchrows.put("DispatchType", rs21.getString("dispatch_type"));
						Dispatchrows.put("CreatedOn","'"+ rs21.getTimestamp("created_on")+"'");
						
						Dispatchrows.put("TodayStartDate","'"+ToEndDate+"'");
						Dispatchrows.put("TodayEndDate","'"+ToStartDate+"'");
						Dispatchrows.put("YesterdayEndDate","'"+YeEndDate+"'");
						Dispatchrows.put("YesterdayStartDate","'"+YeStartDate+"'");
					
						jr20.add(Dispatchrows);
						
						long DispatchID = Utilities.parseLong(rs21.getString("id"));
					//	SELECT isi.id, isi.created_on, isi.outlet_id, concat(om.Outlet_Name, ' ', om.Bsi_Name) as outlet_name, om.address as outlet_address, om.Owner as owner, om.Telepohone as telephone, isi.uvid, isi.invoice_amount, isi.wh_tax_amount, isi.total_amount, isi.discount, isi.net_amount, isi.distributor_id, (select name from common_distributors where distributor_id=isi.distributor_id) distributor_name , (SELECT DISPLAY_NAME FROM users where id=isi.created_by) user_name, (SELECT DISPLAY_NAME FROM users where id=isi.booked_by) booked_by_name FROM inventory_sales_invoices isi, outletmaster om where isi.outlet_id=om.Outlet_ID and isi.id in (select sales_id from inventory_sales_dispatch_invoices where id=89160) order by isi.id
						InnoviceDataQuery = "SELECT isi.id, isi.created_on, isi.outlet_id,(select co.account_number_bank_alfalah from common_outlets co where co.id=isi.outlet_id) outlet_account, concat(om.Outlet_Name, ' ', om.Bsi_Name) as outlet_name, om.address as outlet_address, om.Owner as owner, om.Telepohone as telephone, isi.uvid, isi.invoice_amount, isi.wh_tax_amount, isi.total_amount, isi.discount, isi.net_amount, isi.distributor_id, (select name from common_distributors where distributor_id=isi.distributor_id) distributor_name , (SELECT DISPLAY_NAME FROM users where id=isi.created_by) user_name, (SELECT DISPLAY_NAME FROM users where id=isi.booked_by) booked_by_name FROM inventory_sales_invoices isi, outletmaster om where isi.outlet_id=om.Outlet_ID and isi.id in (select sales_id from inventory_sales_dispatch_invoices where id="+DispatchID+") order by isi.id ";
						//System.out.println(InnoviceDataQuery);	 
						ResultSet rs22 = s7.executeQuery(InnoviceDataQuery);
						while(rs22.next()){
							LinkedHashMap innovicesrows = new LinkedHashMap();
							innovicesrows.put("DispatchID", DispatchID);
							long InvoiceID= Utilities.parseLong(rs22.getString("id"));
							//long outletAccount=Utilities.parseLong(rs22.getString("outlet_account"));
							//System.out.println("InvoiceID"+InvoiceID);
							Date CreatedOn = rs22.getTimestamp("created_on");
							innovicesrows.put("InnoviceID", rs22.getString("id"));
							innovicesrows.put("OutletID", rs22.getString("outlet_id"));
							innovicesrows.put("OutletName", rs22.getString("outlet_name"));
							innovicesrows.put("NetAmmount", rs22.getDouble("net_amount"));
							
					//New addition 	
							innovicesrows.put("OutletAddress", rs22.getString("outlet_address"));
							innovicesrows.put("Owner", rs22.getString("owner"));
							innovicesrows.put("Telephone", rs22.getString("telephone"));
							innovicesrows.put("DistributorID2", rs22.getDouble("distributor_id"));
							innovicesrows.put("DistributorName2", rs22.getString("distributor_name"));
							innovicesrows.put("OutletAccount", rs22.getString("outlet_account"));
							
							
							  double InvoiceAmount = rs22.getDouble("invoice_amount");
							  double WHTaxAmount = rs22.getDouble("wh_tax_amount");
							  double TotalAmount = rs22.getDouble("total_amount");
							  double Discount = rs22.getDouble("discount");
							  double FinalNetAmount = rs22.getDouble("net_amount");
							
						
							
							
						
							double TotalAmountBase = 0;
						  	
						  	int PromotionCounter = 0;
						//Nested Loop For Packages And Brands Starts
						  	ResultSet rs24 = s9.executeQuery("SELECT isip.id, isip.product_id, ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, isip.total_units, isip.rate_raw_cases, isip.total_amount, isip.is_promotion, ipv.unit_per_sku FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id="+InvoiceID+" order by isip.is_promotion, ipv.package_label, ipv.brand_label");
							while (rs24.next()){
								int PackageID = rs24.getInt("package_id");
								int BrandID = rs24.getInt("brand_id");
								String Package = Utilities.filterString(rs24.getString("package_label"), 2, 100);
								String Brand = Utilities.filterString(rs24.getString("brand_label"), 2, 100);
								long ProductCode = rs24.getLong("sap_code");
								long TotalUnits = rs24.getInt("total_units");
								double Rate = rs24.getDouble("rate_raw_cases");
								double NetAmount = rs24.getDouble("total_amount");
								int IsPromotion = rs24.getInt("is_promotion");
								int UnitPerSKU = rs24.getInt("unit_per_sku");
								int ProductID = rs24.getInt("product_id");			
								double RateRawCaseBase =0;
								double RateUnitBase=0;
								
								ResultSet rs25 = s10.executeQuery("select * from inventory_price_list_products_base where product_id="+ProductID+" and "+Utilities.getSQLDate(CreatedOn)+" between start_date and end_date");
								while(rs25.next()){
									RateRawCaseBase = rs25.getDouble("raw_case");
									RateUnitBase = rs25.getDouble("unit");
								}
								double NetAmountBase = RateUnitBase * TotalUnits;
								TotalAmountBase += NetAmountBase;
								
					
								LinkedHashMap productrows = new LinkedHashMap();
								productrows.put("DispatchID", DispatchID);
								productrows.put("InvoiceID", InvoiceID);
								productrows.put("ProductCode", ProductCode);
								productrows.put("Package", Package);
								productrows.put("Brand",Brand);
								productrows.put("Quantity", Utilities.convertToRawCases(TotalUnits, UnitPerSKU));
								productrows.put("Rate", Utilities.getDisplayCurrencyFormat(RateRawCaseBase));
								productrows.put("Amount",Utilities.getDisplayCurrencyFormat(NetAmountBase));
								jr23.add(productrows);
								
							}				
							double TotalDiscountAmount = TotalAmountBase - FinalNetAmount;
							if (TotalDiscountAmount > 0){
								Discount = TotalDiscountAmount;
							}
							
							innovicesrows.put("GrandTotal", InvoiceAmount+Discount);
							innovicesrows.put("GrandTotalDisplay", Utilities.getDisplayCurrencyFormat(InvoiceAmount+Discount));
							innovicesrows.put("WHTax",WHTaxAmount);
							innovicesrows.put("WHTaxDisplay", Utilities.getDisplayCurrencyFormat(WHTaxAmount));
							innovicesrows.put("Discount", Discount);
							innovicesrows.put("DiscountDisplay", Utilities.getDisplayCurrencyFormat( Discount));
							innovicesrows.put("NetAmountDisplay", Utilities.getDisplayCurrencyFormat(FinalNetAmount));
							
							jr21.add(innovicesrows);
						  
						//nested loop for packahes and brands ends
				//Ends	
						}
						
					
					}
					
				
		//geting vechile and Driver and Dispatch Type ends
					
				
					
					
/**************************************************************************************Farhan works ends **********************/
					
					
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
					json.put("UserFeatures",jr10);
					json.put("HandDiscount",jr11);
					json.put("Dispatch", jr20);
					json.put("Innovices",jr21);
					json.put("InovicePackages",jr23);
					
					
				
					
					
					json.put("TodayStartDate","'"+TodayStartDate+"'");
					json.put("TodayEndDate","'"+TodayEndDate+"'");
					json.put("YesterdayEndDate","'"+YesterdayEndDate+"'");
					json.put("YesterdayStartDate","'"+TodayEndDate+"'");
				
					
					
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
				
				s7.close();
				s6.close();
				s5.close();
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
		out.close();
		
	}
	
}
