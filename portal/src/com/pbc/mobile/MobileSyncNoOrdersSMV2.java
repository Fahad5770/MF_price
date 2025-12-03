package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

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
@WebServlet(description = "Mobile Sync No Orders", urlPatterns = { "/mobile/MobileSyncNoOrdersSMV2" })
public class MobileSyncNoOrdersSMV2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncNoOrdersSMV2() {
        super();
        // TODO Auto-generated constructor stub
        
        System.out.println("MobileSyncNoOrdersSMV2");
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("service() MobileSyncNoOrdersSMV2 AL-MOiz");
		
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
			//Mobile_retailer_stock

			int qp_product_id[] = Utilities.parseInt(mr.getParameterValues("no_order_product_id"));
			int raw_cases[] = Utilities.parseInt(mr.getParameterValues("no_order_raw_cases"));
			int units[] = Utilities.parseInt(mr.getParameterValues("no_order_units"));
			int is_no_order[] = Utilities.parseInt(mr.getParameterValues("no_order_is_no_order"));
			
			//New Addition on 8/30/2018
			String Comments = Utilities.filterString(mr.getParameter("NoOrderComment"), 1, 100);
			System.out.println("Comments"+Comments);
			long DistributorID=Utilities.parseLong(mr.getParameter("DistributorIDD"));
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Date mobile_date = null;
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  try {
			             mobile_date = dateFormat.parse(MobileTimestamp);
			           
			            System.out.println("Parsed Date: " + mobile_date);
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				  
			
				  
				  boolean Insert = true;
				
				ResultSet rsExists = s.executeQuery("select mobile_timestamp from mobile_order_sm_zero where outlet_id="+OutletID+" and created_by="+MobileUserID+" order by mobile_timestamp desc limit 1");
				if(rsExists.first()) {
					
					Date lastTime = rsExists.getTimestamp("mobile_timestamp");
					
					  long diffInMillis = mobile_date.getTime() - lastTime.getTime();
			          long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
			          
			          if(diffInMinutes < 5) {
			        	  Insert = false;
			          }
					
				}
				
				if(Insert) {
				//System.out.println("insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order, no_order_reason_type) values "+
					//	"("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+", '"+MobileTimestamp+"', 1, "+ReasonID+" ) "); 
				if(Comments.equals("") || Comments.equals(null) ) {
					
					s.executeUpdate("insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order, no_order_reason_type_v2,distributor_id) values "+
						"("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+", '"+MobileTimestamp+"', 1, "+ReasonID+" ,"+DistributorID+") ");
				}
				else {
					s.executeUpdate("insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng, mobile_timestamp, is_no_order, no_order_reason_type_v2,comments,distributor_id) values "+
							"("+NoOrderID+", "+OutletID+", now(), "+MobileUserID+", "+Lat+", "+Lng+", '"+MobileTimestamp+"', 1, "+ReasonID+",'"+Comments+"' ,"+DistributorID+" ) ");
				
				}
				
				
				long OrderID = 0;
				ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
				if(rs2.first()){
					OrderID = rs2.getLong(1);
				}			
				
				System.out.println("no_order_product_id"+Arrays.toString(qp_product_id));
				System.out.println("no_order_raw_cases"+Arrays.toString(raw_cases));
				System.out.println("no_order_units"+Arrays.toString(units));
				System.out.println("no_order_is_no_order"+Arrays.toString(is_no_order));
				
				if(qp_product_id!=null){
					for(int i=0;i<qp_product_id.length;i++){
						
						s.executeUpdate("insert into mobile_retailer_sm_stock(order_no,product_id,raw_cases,units,is_no_order,outlet_id,created_on,mobile_no_order_id) values("+NoOrderID+","+qp_product_id[i]+","+raw_cases[i]+","+units[i]+","+is_no_order[i]+","+OutletID+",now(),"+OrderID+") ");
						
						
					}
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
