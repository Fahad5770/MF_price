package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;


@WebServlet(description = "Update location (lat,lng) of outlets", urlPatterns = { "/mobile/MobileMDEOutletInformationUpdateExecute" })
public class MobileMDEOutletInformationUpdateExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileMDEOutletInformationUpdateExecute() {
        super();
    }

    
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("json");
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
	
		
		
		//System.out.println("MDE Outlet Update Servlet Called");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		
		
		if (!mr.isExpired()){
		
			long OutletID = Utilities.parseLong(mr.getParameter("OutletID"));
			String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			double OutletLat = Utilities.parseDouble(mr.getParameter("OutletLat"));
			double OutletLng = Utilities.parseDouble(mr.getParameter("OutletLong"));
			double OutletAccuracy = Utilities.parseDouble(mr.getParameter("OutletAccu"));
			int SubChannelID = Utilities.parseInt(Utilities.filterString(mr.getParameter("Channel"), 1, 200));
			int Userid = Utilities.parseInt(Utilities.filterString(mr.getParameter("UserID"), 1, 200));
			
			
			
//			System.out.println("OutletID"+OutletID);
//			System.out.println("OutletName"+OutletName);
//			System.out.println("OutletLat"+OutletLat);
//			System.out.println("OutletLng"+OutletLng);
//			System.out.println("OutletAccuracy"+OutletAccuracy);
//			System.out.println("SubChannelID"+SubChannelID);
//			System.out.println("Userid"+Userid);
			
			
			Datasource ds = new Datasource();
			
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s1=ds.createStatement();
				Statement s2 = ds.createStatement();
				
					
				//if(InsertQuery>=1){
					//json.put("exists", "true");
				//}else{
					//json.put("exists", "false");
				//}
				
				//String getChannelQuery="SELECT parent_channel_id FROM pep.pci_sub_channel where id="+SubChannelID;
				//int ChannelID=0;
				//ResultSet rs=s.executeQuery(getChannelQuery);
				//while(rs.next()){
				//	ChannelID=rs.getInt("parent_channel_id");
				//}
				
			//	System.out.println("Lat and Lng  are ===========> "+OutletLat+"     "+OutletLng);
				
				if(Math.round(OutletLat)==0 || Math.round(OutletLng)==0){
					json.put("exists", "false");
					//return;
				}else{
					                                                                       
					String LogInsertionQueryString="INSERT INTO peplogs.log_common_outlets (id,name,type_id,address,region_id,distributor_id,lat,lng,is_active,created_on,created_by,deactivated_on,deactivated_by,channel_id,category_id,updated_on,updated_by,nfc_tag_id,cache_distributor_id,cache_distributor_name,cache_contact_name,cache_contact_number,cache_contact_nic,cache_snd_id,cache_rsm_id,cache_tdm_id,cache_orderbooker_id,cache_beat_plan_id,sap_customer_id,segment_id,agreed_daily_average_sales,kpo_request_id,vpo_classifications_id,account_number_bank_alfalah,discount_disbursement_id,pic_channel_id,accuracy,census_sub_channel_id,log_created_on) SELECT id,name,type_id,address,region_id,distributor_id,lat,lng,is_active,created_on,created_by,deactivated_on,deactivated_by,channel_id,category_id,updated_on,updated_by,nfc_tag_id,cache_distributor_id,cache_distributor_name,cache_contact_name,cache_contact_number,cache_contact_nic,cache_snd_id,cache_rsm_id,cache_tdm_id,cache_orderbooker_id,cache_beat_plan_id,sap_customer_id,segment_id,agreed_daily_average_sales,kpo_request_id,vpo_classifications_id,account_number_bank_alfalah,discount_disbursement_id,pic_channel_id,accuracy,census_sub_channel_id,now() FROM pep.common_outlets WHERE id="+OutletID;
					int InsertQuery= s1.executeUpdate(LogInsertionQueryString);	
					
					
					
					String sql="UPDATE pep.common_outlets SET lat="+OutletLat+",lng="+OutletLng+",pic_channel_id="+SubChannelID+" ,updated_on=now(), updated_by="+Userid+", accuracy="+OutletAccuracy+"  WHERE id="+OutletID;
					
					//System.out.println(sql);
					int UpdateQuery= s2.executeUpdate(sql);
				
					if(UpdateQuery>=1){
						json.put("statusCode", "200");
						json.put("success", "true");
						ds.commit();
					}else{
						json.put("statusCode", "200");
					}
					
				}
				
				
				
				
				
				
			}catch(Exception e)
			{
				try {
				    ds.rollback();
				   } catch (SQLException e1) {
				    // TODO Auto-generated catch block
				    e1.printStackTrace();
				   }
				   
				json.put("exists", "false");
				json.put("exception", e);
				   e.printStackTrace();
				   //out.print(e);
			}
			finally{
				   
			   try {
			    ds.dropConnection();
			   } catch (SQLException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   }
			  }
					
			}else{
				//json.put("success", "false");	
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		out.close();
		
	}
	
}
