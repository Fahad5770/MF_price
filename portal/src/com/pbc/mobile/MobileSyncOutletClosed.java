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
@WebServlet(description = "Mobile Sync Outlet Closed", urlPatterns = { "/mobile/MobileSyncOutletClosed" })
public class MobileSyncOutletClosed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncOutletClosed() {
        super();
        // TODO Auto-generated constructor stub
        
        System.out.println("const()");
        
    }
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("service() - ");
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		
		
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			String NoOrderID = Utilities.filterString(mr.getParameter("NoOrderID"), 1, 100);
			int ReasonID = Utilities.parseInt(mr.getParameter("ReasonID"));
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			long DistributorID=Utilities.parseLong(mr.getParameter("DistributorIDD"));
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				
				System.out.println("insert into mobile_outlet_closed (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp,distributor_id) values "+
						"("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+", '"+MobileTimestamp+"',"+DistributorID+")");
				
				s.executeUpdate("insert into mobile_outlet_closed (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp,distributor_id) values "+
						"("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+", '"+MobileTimestamp+"',"+DistributorID+")");
				
				
				
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
