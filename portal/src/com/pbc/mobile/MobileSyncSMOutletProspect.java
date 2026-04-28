package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.pbc.common.EmployeeHierarchy;
import com.pbc.common.User;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Outlet Prospectus", urlPatterns = { "/mobile/MobileSyncSMOutletProspect" })
public class MobileSyncSMOutletProspect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncSMOutletProspect() {
        super();
    }
    

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("MobileSyncSMOutletProspect - AL-MOiz");
		
		
		PrintWriter out = response.getWriter();
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		JSONObject json = new JSONObject();
		
		if (!mr.isExpired()){
		
			String uuidID = Utilities.filterString(mr.getParameter("ID"), 1, 100);
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("LAT"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("LNG"), 1, 100);
			String Accurracy = Utilities.filterString(mr.getParameter("Accu"), 1, 100);
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String Deviceid = Utilities.filterString(mr.getParameter("UUID"), 1, 100);
			String platform = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			
			String OutletName= Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			String ShopkeeperName= Utilities.filterString(mr.getParameter("ShopKeeperName"), 1, 100);
			int Channel= Utilities.parseInt(mr.getParameter("Channel"));
			String Address= Utilities.filterString(mr.getParameter("Address"), 1, 100);
			String ContactNumber= Utilities.filterString(mr.getParameter("ContactNumber"), 1, 100);
			String Comments= Utilities.filterString(mr.getParameter("Comments"), 1, 1000);
			int  OutletPotential=  Utilities.parseInt(mr.getParameter("OutletPotential"));
			
			
			
			
		
		Datasource ds = new Datasource();
		
		try {
			ds.createConnection();
		
			
			Statement s = ds.createStatement();
			
			
			boolean shouldIgnore = false;
			ResultSet rs5 = s.executeQuery("SELECT id from mobile_order_sm_outlet_prospect where uuid = "+uuidID);
			if (rs5.first()){
				shouldIgnore = true;
			}
			
			if (shouldIgnore == false){
				
				
			System.out.println("insert into mobile_order_sm_outlet_prospect (uuid,  created_on, created_by, lat, lng,accuracy, mobile_timestamp, device_id, platform,comments, outlet_name, shop_keeper_name, channel, address, contact_number, is_potential_outlet) values "+
					"("+uuidID+",  now(), "+MobileUserID+", "+Lat+", "+Lng+","+Accurracy+", '"+MobileTimestamp+"','"+Deviceid+"','"+platform+"','"+Comments+"','"+OutletName+"','"+ShopkeeperName+"',"+Channel+",'"+Address+"','"+ContactNumber+"',"+OutletPotential+")");
			
			s.executeUpdate("insert into mobile_order_sm_outlet_prospect (uuid,  created_on, created_by, lat, lng,accuracy, mobile_timestamp, device_id, platform,comments, outlet_name, shop_keeper_name, channel, address, contact_number, is_potential_outlet) values "+
					"("+uuidID+",  now(), "+MobileUserID+", "+Lat+", "+Lng+","+Accurracy+", '"+MobileTimestamp+"','"+Deviceid+"','"+platform+"','"+Comments+"','"+OutletName+"','"+ShopkeeperName+"',"+Channel+",'"+Address+"','"+ContactNumber+"',"+OutletPotential+")");
		
				
				
			}
			
			
		
			
			s.close();
			ds.dropConnection();
			
			json.put("success", "true");

			
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			json.put("success", "false");
			json.put("error_code", "106");
			e.printStackTrace();
			System.out.println(e);
			//out.print(e);
		}finally{
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//} // else of order#
	
		
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		
	}
	
    private int getBrandID(long PromotionID, int ProductID[], int PromotionIDs[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		int pret = 0;
		for (int i = 0; i < ProductID.length; i++){
			
			if (PromotionIDs[i] == PromotionID){
				pret = ProductID[i];
			}
			
		}
		
		int ret = 0;
		
		if (pret != 0){
			
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select brand_id from inventory_products where id  ="+pret);
			if (rs.first()){
				ret = rs.getInt(1);
			}
			
			s.close();
			ds.dropConnection();
			
		}
		
		
		return ret;
	}
}
