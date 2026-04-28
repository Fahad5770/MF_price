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
@WebServlet(description = "Mobile Download Outlet Data", urlPatterns = { "/mobile/MobileDownloadOutletData" })
public class MobileDownloadOutletData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileDownloadOutletData() {
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
		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();
		JSONArray jr3 = new JSONArray();
		JSONArray jr4 = new JSONArray();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
			int DistributorID = Utilities.parseInt(mr.getParameter("DistributorID"));
			//int SegmentID = Utilities.parseInt(mr.getParameter("SegmentID"));
			
			
			String PJPIDs = Utilities.filterString(mr.getParameter("SPJPIds"),1,100);
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				Statement s = ds.createStatement();
				
				ResultSet rs1 = s.executeQuery("SELECT name FROM common_distributors where distributor_id="+DistributorID);
				if(rs1.first()){
					json.put("DistributorName", rs1.getString(1));
				}
				
				String WherePJPIDs = "";
				if(PJPIDs!=null || PJPIDs !=""){
					WherePJPIDs = " and dbps.id in ("+PJPIDs+")";
				}
				
				//String WhereSegment = "";
				//if(SegmentID > 0){
				//	WhereSegment = "and co.segment_id="+SegmentID;
				//}
				//System.out.println("SELECT distinct dbps.outlet_id id, co.name, co.address, coc.contact_name, coc.contact_number, coc.contact_nic FROM distributor_beat_plan_schedule dbps join common_outlets co join common_outlets_contacts coc on dbps.outlet_id=co.id and co.id=coc.outlet_id where co.cache_distributor_id="+DistributorID+" and co.segment_id="+SegmentID+" and coc.is_primary=1");
				
				//System.out.println("SELECT distinct dbps.outlet_id id, co.name, co.address, coc.contact_name, coc.contact_number, coc.contact_nic, dbps.id as pjp_id,(SELECT dbp.label FROM distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label FROM distributor_beat_plan_schedule dbps join common_outlets co join common_outlets_contacts coc on dbps.outlet_id=co.id and co.id=coc.outlet_id where co.cache_distributor_id="+DistributorID+WherePJPIDs+"  and coc.is_primary=1");
				
				ResultSet rs = s.executeQuery("SELECT distinct dbps.outlet_id id, co.name, co.address, coc.contact_name, coc.contact_number, coc.contact_nic, dbps.id as pjp_id,(SELECT dbp.label FROM distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label FROM distributor_beat_plan_schedule dbps join common_outlets co join common_outlets_contacts coc on dbps.outlet_id=co.id and co.id=coc.outlet_id where co.cache_distributor_id="+DistributorID+WherePJPIDs+"  and coc.is_primary=1");
				while(rs.next()){ 
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("OutletID", rs.getString("id"));
					rows.put("OutletName", rs.getString("name"));
					rows.put("OutletAddress", rs.getString("address"));
					
					rows.put("ContactName", rs.getString("contact_name"));
					rows.put("ContactNumber", rs.getString("contact_number"));
					rows.put("ContactNIC", rs.getString("contact_nic"));
					
					rows.put("PJPID", rs.getString("pjp_id"));
					rows.put("PJPLabel", rs.getString("pjp_label"));
					
					
					jr.add(rows);
					
					
				}
				
				ResultSet rs2 = s.executeQuery("SELECT *, (select label from common_distributors_towns where id=m.town_id) town_label FROM common_distributors_towns_map m where m.distributor_id="+DistributorID);
				while(rs2.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("TownID", rs2.getString("town_id"));
					rows.put("TownName", rs2.getString("town_label"));
					
					jr2.add(rows);
					
				}
				
				
				
				ResultSet rs3 = s.executeQuery("SELECT *, (SELECT label FROM common_distributors_tehsils where id=m.tehsil_id) tehsil_label FROM common_distributors_tehsils_map m where m.distributor_id="+DistributorID);
				while(rs3.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("TehsilID", rs3.getString("tehsil_id"));
					rows.put("TehsilName", rs3.getString("tehsil_label"));
					
					jr3.add(rows);
					
				}
				
				ResultSet rs4 = s.executeQuery("SELECT *, (SELECT label FROM common_distributors_districts where id=m.district_id) district_label FROM common_distributors_districts_map m where m.distributor_id="+DistributorID);
				while(rs4.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("DistrictID", rs4.getString("district_id"));
					rows.put("DistrictName", rs4.getString("district_label"));
					
					jr4.add(rows);
					
				}
				
				json.put("OutletDataRows", jr);
				json.put("DistributorTownRows", jr2);
				json.put("DistributorTehsilRows", jr3);
				json.put("DistributorDistrictRows", jr4);
				
				if(jr.size() > 0){
					json.put("success", "true");
				}else{
					json.put("success", "false");
					json.put("error", "No outlets found against this distributor.");
				}
				
				s.close();
				
			}catch(Exception e){
				
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
