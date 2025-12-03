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
@WebServlet(description = "Mobile Sync Research", urlPatterns = { "/mobile/MobileSyncDuration" })
public class MobileSyncDuration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncDuration() {
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
			
			long DBID = Utilities.parseLong(mr.getParameter("MarkDurationDBID"));
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));			
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));	
			int TypeID = Utilities.parseInt(mr.getParameter("TyepID"));	
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			//String Timestamp = Utilities.filterString(mr.getParameter("timestamp"), 1, 100);
			
			Datasource ds = new Datasource();
			
			try{
				
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				boolean isAlreadyEntered = false;
				ResultSet rs2 = s.executeQuery("select outlet_id from mobile_outlet_check_in where mobile_db_id="+DBID);
				if(rs2.first()){
					isAlreadyEntered = true;
				}
				
				
				if(!isAlreadyEntered){
					String Sql = "INSERT INTO `mobile_outlet_check_in`(`mobile_db_id`,`outlet_id`,`user_id`,`type_id`,`check_timestamp`,`created_on`)VALUES("+DBID+","+OutletID+","+MobileUserID+","+TypeID+",'"+MobileTimestamp+"',now())";
					//System.out.println(Sql);
					s.executeUpdate(Sql);
				}
				
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
