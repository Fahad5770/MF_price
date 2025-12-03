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
@WebServlet(description = "Mobile Sync Zero Orders", urlPatterns = { "/mobile/MobileSyncZeroOrders" })
public class MobileSyncZeroOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncZeroOrders() {
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
			String OrderNo = Utilities.filterString(mr.getParameter("OrderNo"), 1, 100);
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
			String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			String Comments = Utilities.filterString(mr.getParameter("Comments"), 1, 100);
			
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			double Accuracy = Utilities.parseDouble(mr.getParameter("accuracy"));
			
			String DeviceUUID = Utilities.filterString(mr.getParameter("DeviceUUID"), 1, 100);
			String DevicePlatformVersion = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				long BeatPlanID = 0;
				long DistributorID = 0;
				int RegionID = 0;
				int SmID = 0;
				int TdmID = 0;
				int AsmID = 0;
				int RsmID = 0;
				int SndID = 0;
				
				ResultSet rs2 = s2.executeQuery("SELECT dbpv.id, dbpv.distributor_id, (select region_id from common_distributors where distributor_id=dbpv.distributor_id) region_id, sm_id, tdm_id, asm_id, rsm_id, snd_id FROM distributor_beat_plan_view dbpv where dbpv.outlet_id="+OutletID);
				if(rs2.first()){
					BeatPlanID = rs2.getLong("id");
					DistributorID = rs2.getLong("distributor_id");
					RegionID = rs2.getInt("region_id");
					SmID = rs2.getInt("sm_id");
					TdmID = rs2.getInt("tdm_id");
					AsmID = rs2.getInt("asm_id");
					RsmID = rs2.getInt("rsm_id");
					SndID = rs2.getInt("snd_id");
				}
				
				s.executeUpdate("INSERT INTO `mobile_order_zero`(`mobile_order_no`,`mobile_timestamp`,`outlet_id`,`comments`,`created_on`,`created_by`,`uuid`,`platform`,`lat`,`lng`,`accuracy`, `beat_plan_id`, `distributor_id`, `region_id`, `sm_id`, `tdm_id`, `asm_id`, `rsm_id`, `snd_id`)VALUES("+OrderNo+",'"+MobileTimestamp+"',"+OutletID+",'"+Comments+"',now(),"+MobileUserID+",'"+DeviceUUID+"','"+DevicePlatformVersion+"',"+Lat+","+Lng+","+Accuracy+", "+BeatPlanID+", "+DistributorID+", "+RegionID+", "+SmID+", "+TdmID+", "+AsmID+", "+RsmID+", "+SndID+")");
				
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
