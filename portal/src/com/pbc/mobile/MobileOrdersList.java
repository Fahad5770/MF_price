package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Orders List", urlPatterns = { "/mobile/MobileOrdersList" })
public class MobileOrdersList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileOrdersList() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("constructor ...");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("service() ...");
		
		PrintWriter out = response.getWriter();
		
		int FeatureID = 64;
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		
		
		if (!mr.isExpired()){
		
		long Username = Utilities.parseLong(mr.getParameter("Username"));
		String Password = Utilities.filterString(mr.getParameter("Identity"), 1, 100);
		
		Date FromDate = Utilities.parseDate(mr.getParameter("FromDate"));
		Date ToDate = Utilities.parseDate(mr.getParameter("ToDate"));
		
		long OutletID = Utilities.parseLong(mr.getParameter("OutletID"));
		
		String DeviceID = Utilities.filterString(mr.getParameter("UVID"), 1, 100);
		
		Datasource ds = new Datasource();
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			boolean isValidAccess = false;
			//System.out.print("0");
			ResultSet rs = s.executeQuery("select * from users where id="+Username);
			if(rs.first()){
				
				//System.out.print("a");
				if( Password.equals(rs.getString("password")) || Password.equals(Utilities.getMobileAdminPasswordMD5())){
					//System.out.print("b");
					//if( UserAccess.isMobileDeviceValid(DeviceID) ){
						//System.out.print("c");
						if( Utilities.isAuthorized(FeatureID, Username) ){
							//System.out.print("d");
							isValidAccess = true;
						}else{
							json.put("error_code", "105");
							isValidAccess = false;
						}
//					}else{
//						json.put("error_code", "102");
//						isValidAccess = false;
//					}
				}else{
					json.put("error_code", "104");
					isValidAccess = false;
				}
				
				if(isValidAccess){
					
					String Where = "";
					if(OutletID > 0){
						Where = " and outlet_id="+OutletID;
					}
					
					ResultSet rs2 = s2.executeQuery("SELECT *, (select name from common_outlets where id=mobile_order.outlet_id limit 1) outlet_name FROM mobile_order where created_by="+Username+" and created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate) + Where );
					while( rs2.next() ){
						
						json.put("success", "true");
						
						JSONObject rows = new JSONObject();
						
						rows.put("OrderNo", rs2.getString("id"));
						rows.put("OutletID", rs2.getString("outlet_id"));
						rows.put("CreatedOn", Utilities.getDisplayDateTimeFormatUniversal(rs2.getTimestamp("created_on")) );
						rows.put("OutletName", rs2.getString("outlet_name"));
						
						
						jr.add(rows);
						
					}
					
					json.put("OrderRows", jr);
					
				}else{
					json.put("success", "false");
				}
				
				
			}else{
				json.put("success", "false");
				json.put("error_code", "103");
			}
			
			s3.close();
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			json.put("success", "false");
			json.put("error_code", "106");
			e.printStackTrace();
		}
		
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		
		
	}


}
