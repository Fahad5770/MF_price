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


@WebServlet(description = "Mobile Authenticate User", urlPatterns = { "/mobile/MobileAuthenticateUserMDEApp" })
public class MobileAuthenticateUserMDEApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileAuthenticateUserMDEApp() {
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
		
		JSONArray jr12 = new JSONArray();
		JSONArray jr13 = new JSONArray();
		
		JSONArray jr31 = new JSONArray();
		JSONArray jr32 = new JSONArray();
		JSONArray jr33 = new JSONArray();
		JSONArray jr34 = new JSONArray();
		JSONArray jr35 = new JSONArray();
		JSONArray jr36 = new JSONArray();
		JSONArray jr37 = new JSONArray();
		JSONArray jr38 = new JSONArray();
		JSONArray jr50 = new JSONArray();
		
		
		System.out.println("MDE App Login Called 1212");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			
			long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
			String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			
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
				Statement s5 = ds.createStatement();
				Statement s31 = ds.createStatement();
				Statement s32 = ds.createStatement();
				Statement s33 = ds.createStatement();
				Statement s34 = ds.createStatement();
				Statement s35 = ds.createStatement();
				Statement s36 = ds.createStatement();
				Statement s37 = ds.createStatement();
				Statement s38 = ds.createStatement();
				Statement s50 = ds.createStatement();
				
				long DistributorID=0;
				
				
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
				//	System.out.println("DDDDDDDD at top=>> "+DistributorID);
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
					
					
					ResultSet rs2ps = s2.executeQuery("SELECT product_group_id FROM common_distributors where distributor_id="+DistributorID);
					if(rs2ps.first()){
						json.put("ProductGroupID", rs2ps.getString("product_group_id"));
						//System.out.println("SELECT epl.product_id, ib.label brand, ipa.label package FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and epl.product_group_id="+rs2ps.getString("employee_product_group_id"));
						ResultSet rs3pg = s3.executeQuery("SELECT epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and epl.product_group_id="+rs2ps.getString("product_group_id"));
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
					
					
					
					
					//System.out.println("SELECT * FROM inventory_sales_promotions_request_distributors where distributor_id in("+DistributorID+") order by distributor_id");
					
										
					ResultSet rs6 = sr.executeQuery("SELECT * FROM inventory_sales_promotions_request_distributors where distributor_id in("+DistributorID+") order by distributor_id");
						while(rs6.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("PromotionID", rs6.getString("product_promotion_id"));
							rows.put("DistributorID", rs6.getString("distributor_id"));
							rows.put("OutletID", "0");
							
							jr4.add(rows);
							
						}
					
					
					///////////////////// get products on which promotion applied /////////////////
					
					//if (AllOutletsString.length() > 0){
						ResultSet rs7 = sr.executeQuery("SELECT distinct product_promotion_id FROM inventory_sales_promotions_request_distributors where distributor_id in("+DistributorID+") order by distributor_id");
						while(rs7.next()){
							//System.out.println("SELECT package_id, total_units FROM inventory_sales_promotions_request_products where id="+rs7.getString("product_promotion_id")+" and type_id = 1");
							
							ResultSet rs8 = s3.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_request_products where id="+rs7.getString("product_promotion_id")+" and type_id = 1");
							while(rs8.next()){
								LinkedHashMap rows = new LinkedHashMap();
								
								rows.put("PromotionID", rs7.getString("product_promotion_id"));
								rows.put("PackageID", rs8.getString("package_id"));
								rows.put("TotalUnits", rs8.getString("total_units"));
								
								jr5 = new JSONArray();
								//System.out.println("SELECT brand_id FROM inventory_sales_promotions_request_products_brands where id="+rs7.getString("product_promotion_id")+" and type_id = 1 and package_id = "+rs8.getString("package_id"));
								ResultSet rs9 = s4.executeQuery("SELECT brand_id FROM inventory_sales_promotions_request_products_brands where id="+rs7.getString("product_promotion_id")+" and type_id = 1 and package_id = "+rs8.getString("package_id"));
								while(rs9.next()){
									LinkedHashMap rows2 = new LinkedHashMap();
									
									rows2.put("BrandID", rs9.getString("brand_id"));
									
									jr5.add(rows2);
								}
								
								rows.put("Brands", jr5);
								
								jr6.add(rows);
							}
							
							
							
						} // end while
					//}
					///////////////////// get products available in promotion /////////////////
					
					//if (AllOutletsString.length() > 0){
						ResultSet rs10 = sr.executeQuery("SELECT distinct product_promotion_id FROM inventory_sales_promotions_request_distributors where distributor_id in("+DistributorID+") order by distributor_id");
						while(rs10.next()){
							
							ResultSet rs11 = s3.executeQuery("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_request_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="+rs10.getString("product_promotion_id")+" and ispp.type_id = 2");
							while(rs11.next()){
								LinkedHashMap rows = new LinkedHashMap();
								
								rows.put("PromotionID", rs10.getString("product_promotion_id"));
								rows.put("PackageID", rs11.getString("package_id"));
								rows.put("TotalUnits", rs11.getString("total_units"));
								rows.put("PackageLabel", rs11.getString("label"));
								rows.put("UnitPerCase", rs11.getString("unit_per_case"));
								
								jr7 = new JSONArray();
								ResultSet rs12 = s4.executeQuery("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_request_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="+rs10.getString("product_promotion_id")+" and isppb.type_id = 2 and isppb.package_id = "+rs11.getString("package_id"));
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
					//}
					
					//if (AllOutletsString.length() > 0){
						ResultSet rs13 = sr.executeQuery("SELECT * FROM inventory_price_list_active_view where outlet_id in(0) ");
						while(rs13.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("PriceListID", rs13.getString("price_list_id"));
							rows.put("OutletID", rs13.getString("outlet_id"));
							rows.put("ProductID", rs13.getString("product_id"));
							rows.put("RawCase", rs13.getString("raw_case"));
							rows.put("Unit", rs13.getString("unit"));
							
							jr9.add(rows);
							
						}
					//}
					
					//if (AllOutletsString.length() > 0){
						//System.out.println( " SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in("+AllOutletsString+") " );
						ResultSet rs15 = sr.executeQuery(" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in(0) ");
						while(rs15.next()){
							
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("OutletID", rs15.getString("outlet_id"));
							rows.put("ProductID", rs15.getString("product_id"));
							rows.put("Discount", rs15.getString("discount"));
							rows.put("CreatedOn", rs15.getString("created_on"));
							
							jr11.add(rows);
							
						}
					//}
					
					ResultSet rs14 = s2.executeQuery("SELECT feature_id FROM user_access where user_id="+LoginUsername);
					while(rs14.next()){
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("FeatureID", rs14.getString("feature_id"));
						
						jr10.add(rows);
					}
					
					
					/*********************************Farhan Work Starts***********************/
					
					//System.out.println("SELECT distinct id,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by FROM pep.distributor_beat_plan_schedule dbps where id in (SELECT id FROM pep.distributor_beat_plan where asm_id="+LoginUsername+") order by id");
					
					//Old Query
					//ResultSet rs20 = s2.executeQuery("SELECT distinct id,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by FROM pep.distributor_beat_plan_schedule dbps where id in (SELECT id FROM pep.distributor_beat_plan where asm_id="+LoginUsername+") order by id");
					
					//New query
					//String PerCasesql="SELECT distinct id,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select region_id from common_outlets co where co.id=dbps.outlet_id) region_idd,(SELECT  region_name FROM pep.common_regions cr where cr.region_id=region_idd) region_name,(SELECT  region_name FROM pep.common_regions cr where cr.region_id=region_idd) region_name,(select distributor_id from common_outlets co where co.id=dbps.outlet_id) distributor_idd,(SELECT name FROM common_distributors cd where cd.distributor_id=distributor_idd) distributor_name,(select channel_id from common_outlets co where co.id=dbps.outlet_id) channel_idd,(SELECT label FROM mrd_census_sub_channel mcsc where mcsc.id=channel_idd) channel_name,(SELECT contact_name FROM pep.common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_name,(SELECT contact_number FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_contact,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by FROM pep.distributor_beat_plan_schedule dbps where id in (SELECT id FROM pep.distributor_beat_plan where asm_id="+LoginUsername+") order by id";
					String PerCasesql="SELECT distinct id,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select region_id from common_outlets co where co.id=dbps.outlet_id) region_idd,(SELECT  region_name FROM pep.common_regions cr where cr.region_id=region_idd) region_name,(SELECT  region_short_name FROM pep.common_regions cr where cr.region_id=region_idd) region_short,(select distributor_id from common_outlets co where co.id=dbps.outlet_id) distributor_idd,(SELECT name FROM common_distributors cd where cd.distributor_id=distributor_idd) distributor_name,(select channel_id from common_outlets co where co.id=dbps.outlet_id) channel_idd,(SELECT label FROM mrd_census_sub_channel mcsc where mcsc.id=channel_idd) channel_name,(SELECT contact_name FROM pep.common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_name,(SELECT contact_number FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_contact,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by FROM pep.distributor_beat_plan_schedule dbps where id in (SELECT id FROM pep.distributor_beat_plan where asm_id="+LoginUsername+") order by id";
					
					ResultSet rs20 = s2.executeQuery(PerCasesql);
					//
					while(rs20.next()){
						LinkedHashMap rows = new LinkedHashMap();
						Date UpdatedOn=rs20.getDate("updated_on");
						String UpdatedBy=rs20.getString("updated_by");
						if(UpdatedOn!=null || (UpdatedBy!="0" && UpdatedBy!=null)){
							rows.put("LAT","NotZero" );
							rows.put("LNG","NotZero");
						}
						else{
							rows.put("LAT","Zero" );
							rows.put("LNG","Zero");
						}
						rows.put("OutletID", rs20.getString("outlet_id"));
						rows.put("OutletName", rs20.getString("outlet_name"));
						rows.put("OutletAddress", rs20.getString("outlet_address"));
						rows.put("OutletPJPID", rs20.getString("id"));
						rows.put("OutletPJPLabel", rs20.getString("pjp_label"));
			
						
						//new Addition for PerCasediscount feauture	starts	
						/*------------------------------------------------*/
						
						//System.out.println("DI ::::::-->"+rs20.getString("distributor_idd"));
						rows.put("DistributorID", rs20.getString("distributor_idd"));
						rows.put("DistributorName", rs20.getString("distributor_name"));
						rows.put("OwnerName", rs20.getString("owner_name"));
						rows.put("OwnerContact", rs20.getString("owner_contact"));
						//System.out.println("OnerContact"+rs20.getString("owner_contact"));
						rows.put("Region", rs20.getString("region_idd"));
						rows.put("RegionName", rs20.getString("region_name"));
						rows.put("RegionShortName", rs20.getString("region_short"));
						rows.put("ChannelID", rs20.getString("channel_idd"));
						rows.put("ChannelLabel", rs20.getString("channel_name"));
						
						/*------------------------------------------------*/
						//new Addition for PerCasediscount feauture	ends
						
						
						//System.out.println(rs20.getString("outlet_address")+"="+rs20.getString("id")+"="+rs20.getString("pjp_label"));
						
						jr12.add(rows);
					}
					
					
					ResultSet rs21 = s5.executeQuery("SELECT id,label FROM pci_sub_channel");
					while(rs21.next()){
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("ChannelID", rs21.getString("id"));
						rows.put("ChannelName", rs21.getString("label"));
						//System.out.println(rs21.getString("label")+"="+rs21.getString("parent_channel_id"));
						
						jr13.add(rows);
					}
					
					
					// /*********New Additions For PerCase And TOT Starts*************************/
					ResultSet rs31 = s31.executeQuery("SELECT * FROM mrd_census_sub_channel");
					while(rs31.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("SubChannelID", rs31.getString("id"));
						rows.put("ParentChannelID", rs31.getString("parent_channel_id"));
						rows.put("SubChannelName", rs31.getString("label"));
						
						jr31.add(rows);
						
					}
					

					ResultSet rs32 = s32.executeQuery("SELECT distinct package_id,package_label,brand_id,brand_label FROM pep.inventory_products_view order by package_id;");
					while(rs32.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("PackID", rs32.getString("package_id"));
						rows.put("PackLabel", rs32.getString("package_label"));
						rows.put("BrandID", rs32.getString("brand_id"));
						rows.put("BrandLabel", rs32.getString("brand_label"));
						jr32.add(rows);
						
					}
					
					ResultSet rs33 = s33.executeQuery("SELECT * FROM pep.common_assets_tot_sizes where is_visible=1");
					while(rs33.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("tot_size_id", rs33.getString("id"));
						rows.put("tot_size_label", rs33.getString("label"));
						rows.put("tot_size_price", rs33.getDouble("price"));
						//System.out.println("Pricesss:  "+rs33.getDouble("price"));
						jr33.add(rows);
						
					}
					
					
					ResultSet rs50 = s50.executeQuery("SELECT * FROM pep.common_assets_signage_type");
					while(rs50.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("signage_id", rs50.getString("id"));
						rows.put("signag_label", rs50.getString("label"));
						rows.put("signage_price", "");
						
						//System.out.println("signag_label:  "+rs50.getString("label"));
						jr50.add(rows);
						
					}
					
					
					
					ResultSet rs34 = s34.executeQuery("SELECT * FROM inventory_products_lrb_types");
					while(rs34.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("lrbTypeID", rs34.getString("id"));
						rows.put("lrbTypeLabel", rs34.getString("label"));
						jr34.add(rows);
						
					}
					
					ResultSet rs35 = s35.executeQuery("SELECT distinct package_id,package_label,lrb_type_id FROM pep.inventory_products_view order by package_id;");
					while(rs35.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("PackID", rs35.getString("package_id"));
						rows.put("PackLabel", rs35.getString("package_label"));
						rows.put("lrb_type_id", rs35.getString("lrb_type_id"));
						
						
						jr35.add(rows);
						
					}
					
					
					
					
					//Addition for all packs
					ResultSet rs36 = s36.executeQuery("select distinct package_id, package_label from inventory_products_view where category_id=1 order by package_sort_order");
					while(rs36.next()){
						String	LrbSql="";
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("PackID", rs36.getString(1));
						int PackageID=rs36.getInt(1);
						rows.put("PackLabel", rs36.getString(2));
						
						//loop for all brands
						if( PackageID != 0){
							
							//getting lrb type
							
							LrbSql = "SELECT distinct lrb_type_id,iplt.label lrb_label FROM inventory_products ip join inventory_products_lrb_types iplt on  ip.lrb_type_id=iplt.id  where ip.is_visible=1 and package_id="+PackageID+" order by label";
							
							ResultSet rs37 = s37.executeQuery(LrbSql);
							while(rs37.next()){
							
								LinkedHashMap rows1122 = new LinkedHashMap();
								rows1122.put("BrandID","a_"+ rs37.getString(1));
								rows1122.put("BrandLabel", "All "+rs37.getString(2));
								rows1122.put("PackageID", PackageID);
								jr37.add(rows1122);
								
								//SelectOptions3 += "<option value=a_"+rs21.getString(1)+">All "+rs21.getString(2)+"</option>";
								
								
							
							}
							
							//getting single brand
								LrbSql = "SELECT distinct ip.brand_id as id, (select label from inventory_brands where id = brand_id) label,ip.lrb_type_id,iplt.label lrb_label FROM inventory_products ip join inventory_products_lrb_types iplt on  ip.lrb_type_id=iplt.id where package_id="+PackageID+" order by label";
							
							
								// System.out.println(LrbSql);
						}
							
							ResultSet rs38 = s38.executeQuery(LrbSql);
							while(rs38.next()){
								LinkedHashMap rows1111 = new LinkedHashMap();
								rows1111.put("BrandID", rs38.getString("id"));
								rows1111.put("BrandLabel", rs38.getString("label"));
								rows1111.put("PackageID", PackageID);
								
								jr37.add(rows1111);
							}
							
						jr36.add(rows);
					}
					
					
					
					
					
					
					
					//	/*********New Additions For PerCase And TOT ends*************************/
					
					/*********************************Farhan works ends **********************/
					
					
					json.put("promotions_active", jr4);
					json.put("promotions_products", jr6);
					json.put("promotions_products_free", jr8);
					json.put("ActivePriceListRows", jr9);
					json.put("UserFeatures", jr10);
					json.put("HandDiscount", jr11);
					json.put("outletArray", jr12);
					json.put("outletChannelArray", jr13);
					// /*********New Additions For PerCase And TOT Starts*************************/
					json.put("SubChannelRows", jr31);
					json.put("PackageArray", jr32);
					json.put("TotSizesArray", jr33);
				
					json.put("SignageArray", jr50);
					
					json.put("LrbTypeArray", jr34);
					json.put("TOTPackagesArray", jr35);
					
					//latest Addition
					json.put("PerCasePackage", jr36);
					json.put("PerCaseBrand", jr37);
					
//					/*********New Additions For PerCase And TOT ends*************************/
					
					
					//Adding last 7 days sales for Graph
					///////////////////////////////
					
					//Date ToDate=Utilities.parseDate("19/07/2017");
					Date ToDate=Utilities.getDateByDays(new Date(),-1); //Starting from Yesterday 
					
					
					
					  
					   Date SevenDaysOlderDate=(Utilities.getDateByDays(ToDate,-7));
					   //System.out.println(SevenDaysOlderDate);
					   if(Utilities.getDayOfWeekByDate(SevenDaysOlderDate)==6){//checking if whether the day is Friday
					    SevenDaysOlderDate=(Utilities.getDateByDays(SevenDaysOlderDate,-1));//if true then add one more day
					    
					    //System.out.println("Its Friday 8 days date"+SevenDaysOlderDate);
					   }
					   String FromDate=Utilities.getSQLDate((SevenDaysOlderDate));

					   
					    String query="SELECT sum(isap.total_units) sales,created_on FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id where isa.distributor_id="+DistributorID+" and created_on between "+FromDate+" and "+Utilities.getSQLDateNext(ToDate)+" group by date(created_on)";
					    //System.out.println("-------------------------------- "+query);
					    
					    ResultSet rs1 = s.executeQuery(query);
					    int Date=11;
					    String Day="";
					    String D="";
					    String Sales="";
					    while(rs1.next()){
					     
					    	//System.out.println("Coming Date - "+rs1.getDate("created_on"));
					    	
					    	
					     Date=Utilities.getDayOfWeekByDate(rs1.getDate("created_on")); 
					     
					     //if(Date!=6){
					      if(Date==1){
					       Day="Sun";
					      }
					      else if(Date==2){
					       Day="Mon";
					      }
					      else if(Date==3){
					       Day="Tue";
					      }
					      else if(Date==4){
					       Day="Wed";
					      }
					      
					      else if(Date==5){
					       Day="Thu";
					      }
					      else if(Date==7){
					       Day="Sat";
					      }
					      
					     
					     D+=""+Day+",";
					    // }
					     
					     Sales+=rs1.getString("sales")+","; 
					     
					    
					    } 
					    if(D.length()>0){
					    	D=D.substring(0, D.length() - 1);
					    }
					    
					    if(Sales.length()>0){
					    	 Sales=Sales.substring(0, Sales.length() - 1);
					    }
					    
					   
					    
					    json.put("DaySales",D);
					    json.put("SalesSales", Sales);
					
					//System.out.println("Highcharts Days: "+D+" - Sales : "+Sales);
					
					
					
					
					/////////////////////////////////////////////////
					/////////////////////////////////////////////////
					////////////////////////////////////////////////
					
					
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
