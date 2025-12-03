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


@WebServlet(description = "MobileOrderReport", urlPatterns = { "/mobile/MobileOrdersReport" })
public class MobileOrdersReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileOrdersReport() {
        super();
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("json");
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		
		
		System.out.println("MobileOrdersReport");
		
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
					
					
					
					System.out.println("select id, name from common_outlets where id in("
							+ "SELECT outlet_id FROM mobile_order where created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+ UserID +"))");
					
					ResultSet rs6 = s5.executeQuery("select id, name from common_outlets where id in("
							+ "SELECT outlet_id FROM mobile_order where distributor_id = " + distributorId + " and created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id in(select dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to = "+ UserID +"))");
					while(rs6.next()) {
						
						double totalOrderQuantity = 0;
						double totalOrderAmount = 0;
						
						double totalSalesQuantity = 0;
						double totalSalesAmount = 0;
						
						LinkedHashMap rows = new LinkedHashMap();
						rows = new LinkedHashMap();
						rows.put("ProductID", "");
						rows.put("ProductLabel", rs6.getString(1) + " - " +  rs6.getString(2));
						//rows.put("OrderQuantity", Utilities.getDisplayCurrencyFormat(totalOrderQuantity));
						rows.put("OrderQuantity", "");
						rows.put("OrderAmount", Utilities.getDisplayCurrencyFormat(totalOrderAmount));
						rows.put("IsSubcategory", 1);
						rows.put("IsTotal", 1);
						jr.add(rows);
						int index = jr.size()-1;
						
						System.out.println("SELECT product_id,(select brand_label from inventory_products_view ipv where ipv.product_id = mop.product_id), (select package_label from inventory_products_view ipv where ipv.product_id = mop.product_id), sum(raw_cases), sum(net_amount),"
								+ "(select brand_id from inventory_products_view ipv where ipv.product_id = mop.product_id) FROM mobile_order_products mop where id "
								+ "in(SELECT id FROM mobile_order where created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id ="+ rs6.getLong(1) +""
								+ ") group by product_id");

						
						ResultSet rs3 = s3.executeQuery("SELECT product_id,(select brand_label from inventory_products_view ipv where ipv.product_id = mop.product_id), (select package_label from inventory_products_view ipv where ipv.product_id = mop.product_id), sum(raw_cases), sum(net_amount),"
								+ "(select brand_id from inventory_products_view ipv where ipv.product_id = mop.product_id) FROM mobile_order_products mop where id "
								+ "in(SELECT id FROM mobile_order where distributor_id = " + distributorId + " and created_on between "+ Utilities.getSQLDate(startDate) +" and "+ Utilities.getSQLDateNext(endDate) +" and outlet_id ="+ rs6.getLong(1) +""
								+ ") group by product_id" );
						while(rs3.next()){
							
							long ProductID = rs3.getInt(1);
							String subcategoryLabel = rs3.getString(2);
							String productLabel = rs3.getString(3);
							String quantity = Utilities.getDisplayCurrencyFormat(rs3.getDouble(4)) ;
							String amount = Utilities.getDisplayCurrencyFormat(rs3.getDouble(5)) ;
						
							
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
							
							rows1.put("IsTotal", 0);
							
							jr.add(rows1);
							
							
							

						}
						jr.remove(index);
						rows = new LinkedHashMap();
						rows.put("ProductID", "");
						rows.put("ProductLabel", rs6.getString(1) + " - " +  rs6.getString(2));
						//rows.put("OrderQuantity", Utilities.getDisplayCurrencyFormat(totalOrderQuantity));
						rows.put("OrderQuantity", "");
						rows.put("OrderAmount", Utilities.getDisplayCurrencyFormat(totalOrderAmount));
						rows.put("IsSubcategory", 1);
						rows.put("IsTotal", 1);
						
						jr.add(index,rows);
						
						
						
					}
					LinkedHashMap rows2 = new LinkedHashMap();
					rows2.put("ProductID", "");
					rows2.put("SubcategoryLabel", "");
					rows2.put("ProductLabel", "Grand Total");
					//rows2.put("OrderQuantity", Utilities.getDisplayCurrencyFormat(grandTotalOrderQuantity));
					rows2.put("OrderQuantity", "");
					rows2.put("OrderAmount", Utilities.getDisplayCurrencyFormat(grandTotalOrderAmount));
					rows2.put("IsSubcategory", 1);
					
					
					rows2.put("SalesQuantity", Utilities.getDisplayCurrencyFormat(grandTotalSalesQuantity));
					rows2.put("SalesAmount", Utilities.getDisplayCurrencyFormat(grandTotalSalesAmount));
					rows2.put("IsTotal", 1);
					jr.add(rows2);
					
					json.put("OrdersSummary", jr);
					json.put("success", "true");
					
					
					
				
//					
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
