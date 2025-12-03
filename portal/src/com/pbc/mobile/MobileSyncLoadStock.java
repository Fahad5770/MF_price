package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Blob;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Load Stock", urlPatterns = { "/mobile/MobileSyncLoadStock" })
public class MobileSyncLoadStock extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncLoadStock() {
        super();
        // TODO Auto-generated constructor stub
        
        System.out.println("const()");
        
    }
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("service()");
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		
		
		
		JSONObject obj=new JSONObject();
		
		response.setContentType("json");
		
	
			String NoOrderID = Utilities.filterString(mr.getParameter("NoOrderID"), 1, 100);
			
			
			
			long Userid = Utilities.parseLong(mr.getParameter("userid"));
			
			
			
			
			
			
			long DistributorID=0;
			
			
			
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				
				response.setContentType("application/json");
				
				JSONArray jr = new JSONArray();
				
				
				Date Dated = new Date();
				
				
				//Getting distributor ID
				
				int BeatPlanID=0;
				
				ResultSet rs23=s.executeQuery("SELECT * FROM distributor_beat_plan_users where assigned_to="+Userid);
				if(rs23.first()){
					BeatPlanID = rs23.getInt("id");
				}
				
				ResultSet rs24=s.executeQuery("SELECT * FROM distributor_beat_plan where id="+BeatPlanID);
				if(rs24.first()){
					DistributorID = rs24.getInt("distributor_id");
				}
				
				
				
				
				
				String SelectedProductIDs = "";
				ResultSet rsx1 = s.executeQuery("select group_concat(distinct product_id) from inventory_sales_adjusted_products where cache_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(-60))+" and curdate()");
				if(rsx1.first()){
					SelectedProductIDs = rsx1.getString(1);
				}	
				
				
				ResultSet rs3 = s3.executeQuery("SELECT distinct ipv.package_id, ipv.package_label FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and product_id in ("+SelectedProductIDs+") order by ipv.package_id");
				while(rs3.next()){
					int PackageID = rs3.getInt("package_id");
					String PackageLabel = rs3.getString("package_label");
					
					
					System.out.println("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted_products isap where isap.cache_distributor_id = "+DistributorID+" and isap.product_id = ipv.product_id and isap.cache_created_on_date between "+Utilities.getSQLDate(Dated)+" and "+Utilities.getSQLDate(Dated)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" order by ipv.package_id");
					
					
					ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted_products isap where isap.cache_distributor_id = "+DistributorID+" and isap.product_id = ipv.product_id and isap.cache_created_on_date between "+Utilities.getSQLDate(Dated)+" and "+Utilities.getSQLDate(Dated)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" order by ipv.package_id");
					while(rs1.next()){
						
						int BrandID = rs1.getInt("brand_id");
						String BrandLabel = rs1.getString("brand_label");
						int ProductID = rs1.getInt("product_id");
						double UnitPerSKU = rs1.getDouble("unit_per_sku");
						double balance = rs1.getDouble("balance");
						if (balance < 0){
							balance = 0;
						}
						
						
						double BalanceRawCases = balance / UnitPerSKU;
						
						
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("PackageID", PackageID);
						rows.put("BrandID", BrandID);
						rows.put("Stock", Utilities.getDisplayCurrencyFormat(Math.round(BalanceRawCases)));
						
						rows.put("PackageLabel", PackageLabel);						
						rows.put("BrandLabel", BrandLabel);
						
						jr.add(rows);
						
					}
				}
				
				
				obj.put("success", "true");
				obj.put("rows", jr);
				
				
				
				s2.close();
				s.close();
				ds.commit();
				
				out.print(obj);
				out.close();
				
			}catch(Exception e){
				
				
				ds.rollback();
				
				e.printStackTrace();
				//System.out.print(e);
				obj.put("success", "false");
				obj.put("error_code", "106");
				
			}finally{
				
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		
		out.print(obj);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
