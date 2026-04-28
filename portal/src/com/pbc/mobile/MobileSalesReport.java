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
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;
import com.pbc.common.Distributor;


@WebServlet(description = "Mobile Stock Position", urlPatterns = { "/mobile/MobileSalesReport" })
public class MobileSalesReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileSalesReport() {
        super();
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("json");
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		
		
		System.out.println("MobileSalesReport");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			String UserID = Utilities.filterString(mr.getParameter("UserID"), 1, 200);
			
			Date startDate = Utilities.parseDate(mr.getParameter("startDate"));
			Date endDate = Utilities.parseDate(mr.getParameter("endDate"));
		
			
			//System.out.println("MobileAuthenticateUserV5 User:"+LoginUsername);
			
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
				Statement s6 = ds.createStatement();
				
//				ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+DeviceID+"' and is_active=1");
//				if(rsD.first()){
					
					long distributorId = 0;
					ResultSet rs4 = s.executeQuery("SELECT distributor_id FROM distributor_beat_plan_view where assigned_to="+UserID);
					if(rs4.first()) {
						distributorId=rs4.getLong(1);
					}
					
					double grandTotalOrderQuantity = 0;
					double grandTotalOrderAmount = 0;
					
					double grandTotalSalesQuantity = 0;
					double grandTotalSalesAmount = 0;
					
					

					
					System.out.println("select ib.id, ib.label, ipv.package_sort_order from inventory_brands ib join inventory_products_view ipv on ib.id = ipv.brand_id where ipv.product_id in( SELECT product_id FROM mobile_order_products mop where id "
							+ "in(SELECT id FROM mobile_order where date(created_on) between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to ="+ UserID +" ) "
							+ " ) group by product_id) group by ib.id order by ipv.package_sort_order");
					/*
					ResultSet rs6 = s5.executeQuery("select id, label from inventory_brands where id in(select distinct brand_id from inventory_products_view where product_id in( SELECT product_id FROM mobile_order_products mop where id "
								+ "in(SELECT id FROM mobile_order where created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+ UserID +")"
								+ ") group by product_id))");*/

					ResultSet rs6 = s5.executeQuery("select ib.id, ib.label, ipv.package_sort_order from inventory_brands ib join inventory_products_view ipv on ib.id = ipv.brand_id where ipv.product_id in( SELECT product_id FROM mobile_order_products mop where id "
							+ "in(SELECT id FROM mobile_order where distributor_id = " + distributorId + " and date(created_on) between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to ="+ UserID +" ) "
							+ " ) group by product_id) group by ib.id order by ipv.package_sort_order");

					
					
					
					
					while(rs6.next()) {
						
						double totalOrderQuantity = 0;
						double totalOrderAmount = 0;
						
						double totalSalesQuantity = 0;
						double totalSalesAmount = 0;
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("ProductID", "");
						rows.put("SubcategoryLabel", rs6.getString(2));
						rows.put("ProductLabel", rs6.getString(2));
						rows.put("OrderQuantity", "");
						rows.put("OrderAmount", "");
						rows.put("SalesQuantity", "");
						rows.put("SalesAmount", "");
						rows.put("IsSubcategory", 1);
						rows.put("IsTotal", 0);

						jr.add(rows);
						
						System.out.println("SELECT product_id,(select brand_label from inventory_products_view ipv where ipv.product_id = mop.product_id), (select package_label from inventory_products_view ipv where ipv.product_id = mop.product_id), sum(raw_cases), sum(net_amount),"
								+ "(select brand_id from inventory_products_view ipv where ipv.product_id = mop.product_id) FROM mobile_order_products mop where id "
								+ "in(SELECT id FROM mobile_order where distributor_id = " + distributorId + " and created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+ UserID +")"
								+ ") and product_id in(select product_id from inventory_products_view where brand_id="+ rs6.getInt(1) +") group by product_id order by 2");

						
						ResultSet rs3 = s3.executeQuery("SELECT product_id,(select brand_label from inventory_products_view ipv where ipv.product_id = mop.product_id), (select package_label from inventory_products_view ipv where ipv.product_id = mop.product_id), sum(raw_cases), sum(net_amount),"
								+ "(select brand_id from inventory_products_view ipv where ipv.product_id = mop.product_id) FROM mobile_order_products mop where id "
								+ "in(SELECT id FROM mobile_order where distributor_id = " + distributorId + " and created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+ UserID +")"
								+ ") and product_id in(select product_id from inventory_products_view where brand_id="+ rs6.getInt(1) +") group by product_id order by 2");
						while(rs3.next()){
							
							long ProductID = rs3.getInt(1);
							String subcategoryLabel = rs3.getString(2);
							String productLabel = rs3.getString(3);
							String quantity = Utilities.getDisplayCurrencyFormat(rs3.getDouble(4)) ;
							String amount = Utilities.getDisplayCurrencyFormat(rs3.getDouble(5)) ;
							
							String salesQuantity = "0" ;
							String salesAmount = "0";
							ResultSet rs5 = s4.executeQuery("SELECT  sum(raw_cases), sum(net_amount) FROM inventory_sales_adjusted_products mop where mop.id "
									+ "in(SELECT id FROM inventory_sales_adjusted where order_id in("
									+ "SELECT id FROM mobile_order where distributor_id = " + distributorId + " and created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+ UserID +")"
									+ ")) and mop.product_id="+ProductID);
							if(rs5.next()){
								
								salesQuantity = Utilities.getDisplayCurrencyFormat(rs5.getDouble(1)) ;
								salesAmount = Utilities.getDisplayCurrencyFormat(rs5.getDouble(2)) ;
								
									
									totalSalesQuantity += rs5.getDouble(1);
									totalSalesAmount += rs5.getDouble(2);
									
									grandTotalSalesQuantity += rs5.getDouble(1);
									grandTotalSalesAmount += rs5.getDouble(2);	
									
								
									
								
							}
							
							totalOrderQuantity += rs3.getDouble(4);
							totalOrderAmount += rs3.getDouble(5);
							
							grandTotalOrderQuantity += rs3.getDouble(4);
							grandTotalOrderAmount += rs3.getDouble(5);
							
							LinkedHashMap rows1 = new LinkedHashMap();
							rows1.put("ProductID", ProductID);
							rows1.put("SubcategoryLabel", subcategoryLabel);
							rows1.put("ProductLabel", productLabel);
							rows1.put("OrderQuantity", quantity);
							rows1.put("OrderAmount", amount);
							rows1.put("IsSubcategory", 0);
							
							
							rows1.put("SalesQuantity", salesQuantity);
							rows1.put("SalesAmount", salesAmount);
							rows1.put("IsTotal", 0);
							
							jr.add(rows1);
							
							
							
							

						}
						
//						rows = new LinkedHashMap();
//						
//						rows.put("ProductID", "");
//						rows.put("SubcategoryLabel", rs6.getString(2));
//						rows.put("ProductLabel", "Total");
//						rows.put("OrderQuantity", Utilities.getDisplayCurrencyFormat(totalOrderQuantity));
//						rows.put("OrderAmount", Utilities.getDisplayCurrencyFormat(totalOrderAmount));
//						rows.put("SalesQuantity", Utilities.getDisplayCurrencyFormat(totalSalesQuantity));
//						rows.put("SalesAmount", Utilities.getDisplayCurrencyFormat(totalSalesAmount));
//						rows.put("IsSubcategory", 1);
//						rows.put("IsTotal", 1);
//						jr.add(rows);
						
						
						
						
					}
					LinkedHashMap rows2 = new LinkedHashMap();
					rows2.put("ProductID", "");
					rows2.put("SubcategoryLabel", "");
					rows2.put("ProductLabel", "Grand Total");
					rows2.put("OrderQuantity", "");
					rows2.put("OrderAmount", Utilities.getDisplayCurrencyFormat(grandTotalOrderAmount));
					rows2.put("IsSubcategory", 0);
					
					
					rows2.put("SalesQuantity", "");
					rows2.put("SalesAmount", Utilities.getDisplayCurrencyFormat(grandTotalSalesAmount));
					rows2.put("IsTotal", 1);
					jr.add(rows2);
					
					json.put("SalesSummary", jr);
					json.put("success", "true");
					
					
					
				
					
//				}else{
//					LogTypeID = 5;
//					json.put("success", "false");
//					json.put("error_code", "102");
//				}
				
				
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
		System.out.println(json);
		out.print(json);
		out.close();
		
	}
	
}
