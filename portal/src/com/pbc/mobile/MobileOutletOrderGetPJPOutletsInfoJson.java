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
@WebServlet(description = "Mobile Outlet order get pjp outlets info json", urlPatterns = { "/mobile/MobileOutletOrderGetPJPOutletsInfoJson" })
public class MobileOutletOrderGetPJPOutletsInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileOutletOrderGetPJPOutletsInfoJson() {
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
		
			System.out.println("I am here, servelet called - MobileOutletOrderGetPJPOutletsInfoJson!!! ");
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		

			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			long PJPID = Utilities.parseLong(mr.getParameter("pjpID"));
			
			System.out.println("=================>>>>>>>>> MobileUserID = "+MobileUserID);
			System.out.println("=================>>>>>>>>> PJPID = "+PJPID); 

				
			Datasource ds = new Datasource();
			
			try{
				
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				response.setContentType("application/json");
				JSONObject obj=new JSONObject();
				JSONArray jr = new JSONArray();
				
				long OutletID=0;
				String OutletName="";
				String OutletAddress="";
				String Channel="";
				String VPOClassification="";
				
				int OrderPlaced=0;
				
				
				Date StartDate = new Date();
				
				
				
				Date EndDate = new Date();
				
				//System.out.println("SELECT * FROM pep.mobile_order where outlet_id="+Outletid+" and mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
				
				ResultSet rs2 = s.executeQuery("select dbpv.outlet_id, co.name outlet_name,dbpv.distributor_id ,dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.id="+PJPID+ " order by dbpv.distributor_id");
				while(rs2.next()){
					
					
					LinkedHashMap rows = new LinkedHashMap();
					rows.put("OutletID", rs2.getString("outlet_id"));
					rows.put("OutletName", rs2.getString("outlet_name"));
					rows.put("DayNumber", rs2.getString("day_number"));
					rows.put("Owner", rs2.getString("owner"));
					rows.put("Address", rs2.getString("address"));
					rows.put("Telepohone", rs2.getString("telepohone"));
					rows.put("NFCTagID", rs2.getString("nfc_tag_id"));
					//System.out.println("rs3.getString(distributor_id:"+rs3.getString("distributor_id"));
					rows.put("DistributorID", rs2.getString("distributor_id"));
					
					jr.add(rows);
				}

				obj.put("rows", jr);
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
