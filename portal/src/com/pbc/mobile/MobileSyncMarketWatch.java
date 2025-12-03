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
@WebServlet(description = "Mobile Sync Tasks", urlPatterns = { "/mobile/MobileSyncMarketWatch" })
public class MobileSyncMarketWatch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncMarketWatch() {
        super();
        // TODO Auto-generated constructor stub
        
        
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		
		
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
			int RegionID = Utilities.parseInt(mr.getParameter("RegionID"));
			int DistributorID = Utilities.parseInt(mr.getParameter("DistributorID"));
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
			
			String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			String OutletAddress = Utilities.filterString(mr.getParameter("OutletAddress"), 1, 100);
			int OutletCategory = Utilities.parseInt(mr.getParameter("OutletCategory"));
			
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			
			String DeviceUUID = Utilities.filterString(mr.getParameter("DeviceUUID"), 1, 100);
			//String DevicePlatformVersion = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			 
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String MarketWatchID = Utilities.filterString(mr.getParameter("MarketWatchID"), 1, 100);
			
			int CompanyID[] = Utilities.parseInt(mr.getParameterValues("CompanyID"));
			int PackageID[] = Utilities.parseInt(mr.getParameterValues("PackageID"));
			int SourceID[] = Utilities.parseInt(mr.getParameterValues("SourceID"));
			double Rate[] = Utilities.parseDouble(mr.getParameterValues("Rate"));
			String PromotionRemarks[] = Utilities.filterString(mr.getParameterValues("PromotionRemarks"), 1, 100);
			int DaysAgo[] = Utilities.parseInt(mr.getParameterValues("DaysAgo"));
			
			//System.out.println("Rate = "+Rate);
			
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				String OutletIDVal = "null";
				
				if(OutletID > 0){
					OutletIDVal = OutletID+"";
				}
				
				
				s.executeUpdate("replace INTO `crm_market_watch`(`mobile_uuid`,`region_id`,`distributor_id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_category`,`created_on`,`created_on_mobile`,`created_by`,`lat`,`lng`)VALUES('"+DeviceUUID+"',"+RegionID+","+DistributorID+","+OutletID+",'"+OutletName+"','"+OutletAddress+"',"+OutletCategory+",now(),'"+MobileTimestamp+"',"+MobileUserID+","+Lat+","+Lng+")");
				
				long DocumentID = 0;
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					DocumentID = rs.getLong(1);
				}
				
				for(int i = 0; i < CompanyID.length; i++){
					if (Rate[i] != 0){
						s.executeUpdate("INSERT INTO `crm_market_watch_rates`(`id`,`company_id`,`package_id`,`source_id`,`rate`,`promotion_remarks`,`days_ago`)VALUES("+DocumentID+","+CompanyID[i]+","+PackageID[i]+","+SourceID[i]+","+Rate[i]+",'"+PromotionRemarks[i]+"',"+DaysAgo[i]+")");
					}
				}
				
				
				s2.close();
				s.close();
				
				ds.commit();
				
				json.put("success", "true");
				
				
			}catch(Exception e){
				
				
				ds.rollback();
				
				e.printStackTrace();
				//System.out.print(e);
				json.put("success", "false");
				json.put("error_code", "106");
				
			}finally{
				
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
