 package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Aprrove Outlet", urlPatterns = { "/outlet/OutletRequestApproveExecuteNew" })
public class OutletRequestApproveExecuteNew extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OutletRequestApproveExecuteNew() {
		super();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		
		System.out.println("here");
		System.out.println("OutletRequestApproveExecuteNew Called");

		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		
		
		long ID = Utilities.parseLong(request.getParameter("ID"));
		long DistributorID = Utilities.parseLong(request.getParameter("distributorID"));
		int declineOutletFlag = Utilities.parseInt(request.getParameter("declineFlag"));
		
		String ContactNum = Utilities.filterString(request.getParameter("OutletContactNo"), 1, 300);			
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 300);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 1000);
		String OwnerName = Utilities.filterString(request.getParameter("OwnerName"), 1, 300);	
		String CnicNum = Utilities.filterString(request.getParameter("CNICNo"), 1, 100);
		String SubChannel = Utilities.filterString(request.getParameter("ChannelID"), 1, 100);
		
		String Area = Utilities.filterString(request.getParameter("OutletArea"), 1, 300);			
		String SubArea = Utilities.filterString(request.getParameter("OutletSubArea"), 1, 300);
		String PurchaserName = Utilities.filterString(request.getParameter("PurchaserName"), 1, 1000);
		String PurchaserMobile = Utilities.filterString(request.getParameter("PurchaserMobile"), 1, 1000);

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
			
		
		try {
		
		
			ds.createConnection();
		
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3=ds.createStatement();
			Statement s4=ds.createStatement();
			Statement s5=ds.createStatement();
			Statement s6=ds.createStatement();
			Statement s7=ds.createStatement();
			Statement s8=ds.createStatement();
			Statement s9=ds.createStatement();
			Statement s10=ds.createStatement();
			Statement s11=ds.createStatement();
			Statement s12=ds.createStatement();
			Statement s13=ds.createStatement();

		
			if (declineOutletFlag==0) {
				int CityID=0;
				boolean isCityOutletExist=false;
				long OutletID=0;
				int RegionID=0;
				String DistributorName="";
				String OutletName1= "";
				String outlet_address= "";
				int distributor_id= 0;
				String lat= "";
				String lng="";
				String owner_name= "";
				String owner_contact_number= "";
				long owner_cnic_number= 0;
				int sub_channel_id= 0;
				String accuracy= "";
				int id= 0;
				String area_label= "";
				String sub_area_label= "";
				String purchaser_name= "";
				String purchaser_mobile_no= "";
				int request_by=0;
				String Channel_ID="";
				 int pjp_id=0, day_number=0;
				 long mobile_transaction_no = 0;
				 int beat_plan_id=0,is_order=0;

				
				
				  ResultSet rs = s.
				  executeQuery("select city_id,name from common_distributors where distributor_id = "+DistributorID); 
				  if(rs.first()) { 
					  CityID = rs.getInt(1); 
					  DistributorName = rs.getString(2); 
				}
				 
				
				System.out.println("select max(id) from common_outlets where city_id="+CityID); 
				  ResultSet rs2 =
				  s.executeQuery("select max(id) from common_outlets where city_id="+CityID); 
				  if(rs2.first()) {
					  isCityOutletExist=true; 
					  OutletID = rs2.getLong(1); 
				  }
				 
				
				if(OutletID==0) {
					System.out.println("select * from common_outlet_code_range where city_id="+CityID);
					ResultSet rs3 = s.executeQuery("select * from common_outlet_code_range where city_id="+CityID);
					if(rs3.first()) {
						OutletID = rs3.getLong("start_code");
					}
				}
				else {
					OutletID=OutletID+1;
				}

				
				//Region Query
			     ResultSet rs7=s8.executeQuery("select region_id from common_distributors where distributor_id="+DistributorID);
			     while(rs7.next()){
			    	 RegionID=rs7.getInt("region_id");
			     }
			     System.out.println("==============");
			     System.out.println("select is_order,request_by,outlet_name,sub_channel_id,outlet_address,lat,lng,distributor_id,owner_name,owner_contact_number,owner_cnic_number,sub_channel_id,accuracy,id,area_label,sub_area_label,purchaser_name,purchaser_number,mobile_time_stamp,mobile_transaction_no,beat_plan_id,device_id from common_outlets_request where id="+ID);		
			     ResultSet rs8=s9.executeQuery("select is_order,request_by,outlet_name,sub_channel_id,outlet_address,lat,lng,distributor_id,owner_name,owner_contact_number,owner_cnic_number,sub_channel_id,accuracy,id,area_label,sub_area_label,purchaser_name,purchaser_number,mobile_time_stamp,mobile_transaction_no,beat_plan_id,device_id from common_outlets_request where id="+ID);			     
			     if(rs8.first()){
			    	 OutletName1=rs8.getString("outlet_name");
			    	 outlet_address=rs8.getString("outlet_address");
			    	 lat=rs8.getString("lat");
			    	 lng=rs8.getString("lng");
			    	 distributor_id=rs8.getInt("distributor_id");
			    	 owner_name=rs8.getString("owner_name");
			    	 owner_contact_number=rs8.getString("owner_contact_number");
			    	 owner_cnic_number=rs8.getLong("owner_cnic_number");
			    	 sub_channel_id=rs8.getInt("sub_channel_id");
			    	 accuracy=rs8.getString("accuracy");
			    	 id=rs8.getInt("id");
			    	 area_label=rs8.getString("area_label");
			    	 sub_area_label=rs8.getString("sub_area_label");
			    	 purchaser_name=rs8.getString("purchaser_name");
			    	 purchaser_mobile_no=rs8.getString("purchaser_number");
			    	 request_by=rs8.getInt("request_by");
			    	 Channel_ID = rs8.getString("sub_channel_id");
			    	 mobile_transaction_no=rs8.getLong("mobile_transaction_no");
			    	 beat_plan_id=rs8.getInt("beat_plan_id");
			    	 is_order=rs8.getInt("is_order");
			    	// pjp_id = rs8.getInt("PJP");
			    	// day_number = rs8.getInt("day_number");
			    	 day_number = Utilities.getDayOfWeekByDate(Utilities.parseDateYYYYMMDD(rs8.getString("mobile_time_stamp")));
			    	 

			     }
			     //Updating the Outlet Records
			    // System.out.println( "+OutletID+",'"+OutletName1+"',1,'"+outlet_address+"',"+RegionID+","+distributor_id+","+lat+","+lng+",1,now(),"+UserID+","+distributor_id+",'"+DistributorName+"','"+owner_name+"',"+owner_contact_number+","+((owner_cnic_number == 0) ? null : owner_cnic_number) +","+((sub_channel_id == 0) ? null : sub_channel_id)+","+accuracy+",'"+area_label+"','"+sub_area_label+"','"+purchaser_name+"',"+purchaser_mobile_no+","+CityID+","+day_number+","+mobile_transaction_no+","+beat_plan_id+",'"+rs8.getString("mobile_time_stamp")+"','"+rs8.getString("device_id")+"')");
			     String InsertQuery = "insert into common_outlets(id,name,type_id,address,region_id,distributor_id,lat,lng,is_active,created_on,created_by,cache_distributor_id,cache_distributor_name,cache_contact_name,cache_contact_number,cache_contact_nic,pic_channel_id,accuracy,area_label,sub_area_label,purchaser_name,purchaser_number,city_id,day,mobile_transaction_no,cache_beat_plan_id,mobile_time_stamp,device_id) Values( "+OutletID+",'"+OutletName1+"',1,'"+outlet_address+"',"+RegionID+","+distributor_id+","+lat+","+lng+",1,now(),"+UserID+","+distributor_id+",'"+DistributorName+"','"+owner_name+"',"+owner_contact_number+","+((owner_cnic_number == 0) ? null : owner_cnic_number) +","+((sub_channel_id == 0) ? null : sub_channel_id)+","+accuracy+",'"+area_label+"','"+sub_area_label+"','"+purchaser_name+"',"+purchaser_mobile_no+","+CityID+","+day_number+","+mobile_transaction_no+","+beat_plan_id+",'"+rs8.getString("mobile_time_stamp")+"','"+rs8.getString("device_id")+"')";
			
			     System.out.println(InsertQuery);
			     s11.executeUpdate(InsertQuery);
			    /* 
			     long NewOutletID=0;
			     ResultSet rs1=s7.executeQuery("select max(id) from common_outlets");
			     
			     if(rs1.first()){
			    	 NewOutletID=rs1.getLong(1);
			    	 
			     };		*/
			     
			     
			     // common_outlets_contacts Query
			     System.out.println("INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary)values ("+OutletID+",'"+OwnerName+"','"+ContactNum+"','"+CnicNum+"',5,1)");
				 s3.executeUpdate("INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary)values ("+OutletID+",'"+OwnerName+"','"+ContactNum+"','"+CnicNum+"',5,1)");
				
			     
			     
			     //variables of table common_regions
				    String RegionShortName="";
				    String RegionName="";
			      
				//table common_regions data fetching query
				 String query4="select * from common_regions where region_id="+RegionID;
				    
				 ResultSet rs6 = s6.executeQuery(query4);
				 while( rs6.next() ){
					 RegionShortName=rs6.getString("region_short_name");
					 RegionName=rs6.getString("region_name");
				 }
					
				 
				 //table outletmaster insertion query
				// System.out.println("INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted) VALUES(0,"+OutletID+",'"+OutletName+"',"+DistributorID+",'"+DistributorName+"','"+RegionShortName+"','"+RegionName+"','"+OwnerName+"','"+OutletAddress+"','"+ContactNum+"','PBCIT',now(), 0,0,0,'',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
				// s5.executeUpdate("INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted) VALUES(0,"+OutletID+",'"+OutletName+"',"+DistributorID+",'"+DistributorName+"','"+RegionShortName+"','"+RegionName+"','"+OwnerName+"','"+OutletAddress+"','"+ContactNum+"','PBCIT',now(), 0,0,0,'',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
				 
			     
			     
			  
			     
			 
				
				    
				    
				    if(pjp_id == 0) {
				     System.out.println("SELECT id FROM distributor_beat_plan_users where assigned_to="+request_by);
				     ResultSet rs23 = s12.executeQuery("SELECT id FROM distributor_beat_plan_users where assigned_to="+request_by);
					if(rs23.first()) {
						pjp_id = rs23.getInt("id");
					}
				    }
				    
				    if(pjp_id == 0) {
					     System.out.println("SELECT id FROM distributor_beat_plan where distributor_id="+distributor_id);
					     ResultSet rs23 = s12.executeQuery("SELECT id FROM distributor_beat_plan where distributor_id="+distributor_id);
						if(rs23.first()) {
							pjp_id = rs23.getInt("id");
						}
					    }
				    
				    if(day_number == 0) {
					  System.out.println("select mobile_timestamp from common_outlets_request  where id="+ID);
					ResultSet rsmobile_timestamp = s13.executeQuery("select mobile_timestamp from common_outlets_request  where id="+ID);
					if(rsmobile_timestamp.first()) {
						day_number = Utilities.getDayOfWeekByDate(Utilities.parseDateYYYYMMDD(rsmobile_timestamp.getString("mobile_timestamp")));
					}
				    }
//					ResultSet rs24 = s.executeQuery("SELECT distinct day_number FROM distributor_beat_plan_schedule where id="+dbp_id);
//					while(rs24.first()) {
//						dbps_id = rs24.getInt("day_number");
//					};
					 System.out.println("insert into distributor_beat_plan_schedule(id,outlet_id,day_number,is_alternative) VALUES("+pjp_id+","+OutletID+","+day_number+",0 )");
					   s3.executeUpdate("insert into distributor_beat_plan_schedule(id,outlet_id,day_number,is_alternative) VALUES("+pjp_id+","+OutletID+","+day_number+",0 )");
					   
				

					     System.out.println("Done...........");
					     
					     
					     /**** Order Punch ****/
					     
					     System.out.println(
									"Update mobile_order_unregistered set outlet_id=" + OutletID + " where Request_id=" + ID + " and mobile_order_no="+mobile_transaction_no);
							s.executeUpdate(
									"Update mobile_order_unregistered set outlet_id=" + OutletID + " where Request_id=" + ID + " and mobile_order_no="+mobile_transaction_no);
							
							System.out.println(
									"select id from mobile_order_unregistered  where Request_id=" + ID + " and mobile_order_no="+mobile_transaction_no);
							ResultSet rsOrder_id = s.executeQuery(
									"select id from mobile_order_unregistered  where Request_id=" + ID + " and mobile_order_no="+mobile_transaction_no);
							long Order_id = (rsOrder_id.first()) ? rsOrder_id.getLong("id") : 0;
					     /**** Order Punch ****/
					     
						   s.executeUpdate("Update common_outlets_request set is_approved=1 where id="+ID+"");
//						   System.out.println("Update mobile_order_unregistered set outlet_id="+OutletID+" where Request_id="+ID+"");
//						     s.executeUpdate("Update mobile_order_unregistered set outlet_id="+OutletID+" where Request_id="+ID+"");  
						   if(is_order==1) {
							//   SalesPosting.splitOrderUnregistered(Order_id);
						   }
						   
					ds.commit();
				
					obj.put("success", "true");
					obj.put("OutletIDGen", OutletID);
			}else { // decline case					
					//System.out.println("Update common_outlets_request set is_declined=1 where id=" + ID + "");
					s.executeUpdate("Update common_outlets_request set is_declined=1 where id=" + ID + "");
					 ds.commit();
					obj.put("success", "true");
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
					
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
