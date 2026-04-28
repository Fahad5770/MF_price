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
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Add Outlet Execute", urlPatterns = { "/outlet/AddOutletExecute" })

public class AddOutletExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddOutletExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		
		
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		
		int QueryFlag = Utilities.parseInt(request.getParameter("QueryFlag"));
		int ID = Utilities.parseInt(request.getParameter("ID"));
		
		
		//Requester Detail
		String RequestBy = Utilities.filterString(request.getParameter("Requestedby"), 1, 100);
		//String ContactNum = Utilities.filterString(request.getParameter("ContactNo"), 1, 300);
		//int ContactNum=Utilities.parseInt(request.getParameter("ContactNo"));
		int SapCode=Utilities.parseInt(request.getParameter("SAPcode"));
		String RequestFor = Utilities.filterString(request.getParameter("Requestfor"), 1, 300);
		
		//long AgencyID=Utilities.parseLong(request.getParameter("AgencyID"));
	//	String AgencyName = Utilities.filterString(request.getParameter("AgencyName"), 1, 300);
		
		
		//Outlet Detail
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 300);
		long OutletId=Utilities.parseLong(request.getParameter("OutletID"));
		//int Shopcategory=Utilities.parseInt(request.getParameter("ShopCategory"));
		//String Shopcategory = Utilities.filterString(request.getParameter("ShopCategory"), 1, 100);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 1000);
		String OwnerNameFromField = Utilities.filterString(request.getParameter("OwnerName"), 1, 300);
		String OwnerName=OwnerNameFromField ;
		String OutletContactNum = Utilities.filterString(request.getParameter("OutletContactNo"), 1, 100);
		//int OutletContactNum=Utilities.parseInt(request.getParameter("ContactNo"));
		String CnicNum = Utilities.filterString(request.getParameter("CNICNo"), 1, 100);
		//int VpoClass=Utilities.parseInt(request.getParameter("vpoclassification"));
		long CRNameAndSapID = Utilities.parseLong(request.getParameter("CRName&SAPID"));
		int SubChannel=Utilities.parseInt(request.getParameter("Subchannels"));
		//String VehicalNum = Utilities.filterString(request.getParameter("VehicleNo"), 1, 100);
		int BeatPlan=Utilities.parseInt(request.getParameter("BeatPlan"));
		//int BeatDaysPlan = Utilities.filterString(request.getParameter("BeatPlanDays"), 1, 100);
		//String RequestRemarks = Utilities.filterString(request.getParameter("RequesterRemarks"), 1, 1000);
		//String SNDRemarks = Utilities.filterString(request.getParameter("SNDRemarks"), 1, 1000);
		int[] BeatPlanDay=Utilities.parseInt(request.getParameterValues("BeatPlanDays"));
		
		
		
		//Area Detail
		long RMID = Utilities.parseLong(request.getParameter("RMID"));
		int CommonCategory=Utilities.parseInt(request.getParameter("commoncategory"));
		//long DistributorID = Utilities.parseLong(request.getParameter("DistributionID"));
		
		//int CityID=Utilities.parseInt(request.getParameter("outletcity"));
		
		String DistributorName="";
		
		
		long LastEneteredRequestID=0;
		long CommLastEneteredRequestID=0;
		long RegionID=0;
		String objval="";
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
			
			int CityID=0;
			int DistributorID=0;
			
			
			/*ResultSet rs23 = s11.executeQuery("SELECT city_id FROM "+ds.getDatabaseName()+".common_distributors where distributor_id in (SELECT distinct distributor_id FROM users where ID=2272 and type_id=2)");*/
			ResultSet rs23 = s11.executeQuery("SELECT distributor_id, (select city_id FROM "+ds.getDatabaseName()+".common_distributors as ctdis where ctdis.distributor_id=u.distributor_id) as city_id FROM "+ds.getDatabaseName()+".users as u where ID="+UserID+" and type_id=2");
			if(rs23.first()) {
				CityID = rs23.getInt("city_id");
				DistributorID = rs23.getInt("distributor_id");
				
			}
			//System.out.println(CityID+" "+DistributorID);
			if(CityID!=0) { //If city Not Exists
				if(QueryFlag==1){/* "+CRNameAndSapID+"*/
					 
				s.executeUpdate("INSERT INTO common_outlets_request(outlet_name,outlet_address, owner_name,owner_contact_number,owner_cnic_number,distributor_id,created_by, created_on,sub_channel_id,beat_plan_id,category_id,city_id)VALUES"
						+ "('"+OutletName+"','"+OutletAddress+"', '"+OwnerName+"','"+OutletContactNum+"','"+CnicNum+"','"+DistributorID+"','"+UserID+"',now(),'"+SubChannel+"','"+BeatPlan+"','"+CommonCategory+"','"+CityID+"')");
				/*System.out.println("INSERT INTO common_outlets_request(agency_id,agency_name,outlet_name,outlet_address, owner_name,owner_contact_number,owner_cnic_number,distributor_id,created_by, created_on,sub_channel_id,beat_plan_id,category_id,city_id)VALUES"
						+ "('"+AgencyID+"','"+AgencyName+"','"+OutletName+"','"+OutletAddress+"', '"+OwnerName+"','"+OutletContactNum+"','"+CnicNum+"','"+DistributorID+"','"+UserID+"',now(),'"+SubChannel+"','"+BeatPlan+"','"+CommonCategory+"','"+CityID+"')");*/
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			     if( rs.first() ){
			      LastEneteredRequestID = rs.getLong(1);
			     }
			     long maxID=0;
			     System.out.println("select max(id) from common_outlets where city_id="+CityID);
			     ResultSet rs1=s7.executeQuery("select max(id) from common_outlets where city_id="+CityID);
			     
			     if(rs1.first()){
			    	 maxID=rs1.getLong(1);
			    	 
			     };
			     
			   //if maxid=1 mean there no no outlet against this city then go to range table and get starting code
			     if(maxID==0) {
			    	 
			    	 ResultSet RsRange = s7.executeQuery("SELECT * FROM pep.common_outlet_code_range where city_id="+CityID);
			    	 if(RsRange.first()) {
			    		 maxID = RsRange.getLong("start_code");
			    	 }
			    	 
			     }else {
			    	 maxID+=1; 
			     }
			     
			     System.out.println("maxID "+maxID);
			     
			     //System.out.println("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,cache_contact_name,cache_contact_number,cache_contact_nic,cache_snd_id,cache_rsm_id,category_id,cache_beat_plan_id,channel_id,vpo_classification_id,kpo_request_id)VALUES("+maxID+",'"+OutletName+"', '"+OutletAddress+"',"+RouteAreaID+","+DistributorID+",now(), "+UserID+", '"+OwnerName+"', '"+OutletContactNum+"','"+CnicNum+"', "+CRNameAndSapID+","+RMID+","+Shopcategory+", "+BeatDaysPlan+", "+SubChannel+","+VpoClass+","+LastEneteredRequestID+")");
			     
			     //Region Query
			     ResultSet rs7=s8.executeQuery("select region_id from common_distributors where distributor_id="+DistributorID);
			     while(rs7.next()){
			    	 RegionID=rs7.getLong("region_id");
			     }
			     
			     // common_outlets Query
			    
			   //  s2.executeUpdate("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,category_id,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_snd_id,cache_rsm_id,category_id,cache_beat_plan_id,channel_id,vpo_classification_id,kpo_request_id)VALUES("+maxID+",'"+OutletName+"', '"+OutletAddress+"',"+RegionID+","+DistributorID+",now(), "+UserID+","+VpoClass+","+LastEneteredRequestID+","+CommonCategory+","+CityID+","+DistributorID+")");
			     s2.executeUpdate("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,category_id,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_beat_plan_id,kpo_request_id)VALUES"
			     		+ "('"+maxID+"','"+OutletName+"','"+OutletAddress+"','"+RegionID+"','"+DistributorID+"',now(), '"+UserID+"','"+CommonCategory+"','"+CityID+"','"+OwnerName+"', '"+OutletContactNum+"','"+CnicNum+"','"+BeatPlan+"','"+LastEneteredRequestID+"')");
			     /*System.out.println("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,category_id,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_snd_id,cache_rsm_id,cache_beat_plan_id,channel_id,kpo_request_id)VALUES"
				     		+ "('"+maxID+"','"+OutletName+"','"+OutletAddress+"','"+RegionID+"','"+DistributorID+"',now(), '"+UserID+"','"+CommonCategory+"','"+CityID+"',"+OwnerName+"', '"+OutletContactNum+"','"+CnicNum+"', '"+CRNameAndSapID+"','"+RMID+"','"+BeatPlan+"', '"+SubChannel+"','"+LastEneteredRequestID+"')");*/
			     CommLastEneteredRequestID = maxID;
			     
				 
				 // common_outlets_contacts Query
				 s3.executeUpdate("INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary)values ("+maxID+",'"+OwnerName+"','"+OutletContactNum+"','"+CnicNum+"',5,1)");
				
				 //table common_distributors data fetching query
			     String query3="select * from common_distributors where distributor_id="+DistributorID;
			    
			     ResultSet rs4 = s4.executeQuery(query3);
			     while( rs4.next() ){
			    	 DistributorName=rs4.getString("name");
			    	 
			     }
			     
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
				 //s5.executeUpdate("INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES(0,"+maxID+",'"+OutletName+"',"+DistributorID+",'"+DistributorName+"','"+RegionShortName+"','"+RegionName+"','"+OwnerName+"','"+OutletAddress+"','"+OutletContactNum+"','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"+CityID+")");
				 s5.executeUpdate("INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES"
				 		+ "(0,"+maxID+",'"+OutletName+"',"+DistributorID+",'"+DistributorName+"','"+RegionShortName+"','"+RegionName+"','"+OwnerName+"','"+OutletAddress+"','"+OutletContactNum+"','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"+CityID+")");
				 
				 //Beat Plan Days Query
			      if(BeatPlanDay !=null){
			    	  
						
			    	  
						
						for(int i = 0; i <BeatPlanDay.length; i++)
						{
							int Day= (BeatPlanDay [i]);
							
							String DayInsertSql="INSERT INTO distributor_beat_plan_schedule(id,outlet_id,day_number)values ("+BeatPlan+","+maxID+","+Day+")";
							
							s9.executeUpdate(DayInsertSql);
						}
					}
			      
				}else if(QueryFlag!=1){
					long EntryID=Utilities.parseLong(request.getParameter("EntryID"));
					String EnteredCnic=request.getParameter("EnteredCnic");
					String CheckCNICDupication="Select contact_nic from common_outlets_contacts";
					ResultSet rs10=s10.executeQuery(CheckCNICDupication);
					while(rs10.next()){
						String Cnic=rs10.getString("contact_nic");
						if(EnteredCnic.equals(Cnic)){
							//System.out.println("CNIC matches");
							//obj.put("Exist", "true");
							objval="true";
							break;
						}else{
							//obj.put("Exist", "false");
							objval="false";
						}
					}
				}
					
				ds.commit();
				obj.put("present",objval);
				obj.put("success", "true");
				obj.put("newoutletID", CommLastEneteredRequestID);
			
			s10.close();	
			s9.close();
			s8.close();
			s7.close();
			s3.close();
			s2.close();
			s.close();
			ds.dropConnection();
			}else{//end of main if
				obj.put("success", "false");
				obj.put("error", "City Not Found");
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
