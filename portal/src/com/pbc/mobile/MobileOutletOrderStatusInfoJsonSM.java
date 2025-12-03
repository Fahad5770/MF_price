package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Owner;
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
@WebServlet(description = "Mobile Outlet order info json", urlPatterns = { "/mobile/MobileOutletOrderStatusInfoJsonSM" })
public class MobileOutletOrderStatusInfoJsonSM extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileOutletOrderStatusInfoJsonSM() {
        super();
        // TODO Auto-generated constructor stub
        
        
        
        
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//aaa
		try{
		
			System.out.println("I am here, servelet called - MobileOutletOrderStatusInfoJsonSM!!! ");
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		//if (!mr.isExpired()){
			
			
			
			
			
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			long Outletid = Utilities.parseLong(mr.getParameter("outletid"));
			//String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			
			
			
				///////////////////////////////////
				
			
			
			//System.out.println("Hello");
			
			
			
				
			Datasource ds = new Datasource();
			
			try{
				
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				response.setContentType("application/json");
				JSONObject obj=new JSONObject();
				JSONArray jr = new JSONArray();
				JSONArray jr1 = new JSONArray();
				JSONArray jr2 = new JSONArray();
				
				//System.out.println("select outlet_id from mrd_census where mobile_census_id="+MobileCensusID);
				
				long OutletID=0;
				String OutletName="";
				String OutletAddress="";
				String Channel="";
				String VPOClassification="";
				
				int OrderPlaced=0;
				
				
				Date StartDate = new Date();
				
				
				
				Date EndDate = new Date();
				
				//System.out.println("SELECT * FROM pep.mobile_order where outlet_id="+Outletid+" and mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
				
				ResultSet rs2 = s.executeQuery("SELECT * FROM mobile_order_sm where  mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
				while(rs2.next()){
					OrderPlaced=1;
					
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("OrderedOutletID", rs2.getString("outlet_id"));
					
					jr.add(rows);
					
				}
			
			
				//Outlet Visited but order not taken
				
				//System.out.println("SELECT * FROM pep.mobile_order_zero where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by id desc");
				
				ResultSet rs3 = s.executeQuery("SELECT * FROM mobile_order_sm_zero where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by id desc");
				while(rs3.next()){
					
					LinkedHashMap rows1 = new LinkedHashMap();
					
					rows1.put("VisitedNotOrdered", rs3.getString("outlet_id"));
					
					jr1.add(rows1);
					
				}
				
				
				//Outlets are closed
				
				ResultSet rs4 = s.executeQuery("SELECT * FROM mobile_outlet_sm_closed where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by id desc");
				while(rs4.next()){
					
					LinkedHashMap rows2 = new LinkedHashMap();
					
					rows2.put("OutletClosed", rs4.getString("outlet_id"));
					
					jr2.add(rows2);
					
				}
				
				
				//SELECT * FROM pep.mobile_order_zero where mobile_timestamp between '2017-04-28' and '2017-04-29' order by id desc;
				
				
				
			//obj.put("OrderPlaced", OrderPlaced);
			
			
			
			
			
			
			obj.put("rows", jr);
			obj.put("rows1", jr1);
			obj.put("rows2", jr2);
					
					
				
				obj.put("success", "true");
				
				out.print(obj);
				out.close();	
				
					
				
				
				s.close();
				ds.commit();
				
				
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
			
		//}else{
		//	json.put("success", "false");
		//	json.put("error_code", "101");
		//}
		
		out.print(json);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
