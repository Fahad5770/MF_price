package com.pbc.tot;

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

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price List ", urlPatterns = { "/tot/TOTCoolerInjectionExecute" })
public class TOTCoolerInjectionExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TOTCoolerInjectionExecute() {
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
		
		long OutletID=Utilities.parseLong(request.getParameter("ProductCoolerInjectionOutletID"));
		
		String OutletName=Utilities.filterString(request.getParameter("OutletName"), 1, 50);
		
		String OutletAddress=Utilities.filterString(request.getParameter("OutletAddress"), 1, 50);
		String OutletContactNumber=Utilities.filterString(request.getParameter("OutletContactNumber"), 1, 50);
		String OutletContactName=Utilities.filterString(request.getParameter("OutletContactName"), 1, 50);
		String OutletContactNic=Utilities.filterString(request.getParameter("OutletContactNic"), 1, 50);
		long out_channel_id=Utilities.parseLong(request.getParameter("out_channel_id"));
		long out_region_id=Utilities.parseLong(request.getParameter("out_region_id"));
		long out_rsm_id=Utilities.parseLong(request.getParameter("out_rsm_id"));
		long out_snd_id=Utilities.parseLong(request.getParameter("out_snd_id"));
		long out_distributor_id=Utilities.parseLong(request.getParameter("out_distributor_id"));
		String EmptyStatus=Utilities.filterString(request.getParameter("EmptyStatus"), 1, 50);		
		String TOTCode = Utilities.filterString(request.getParameter("TOTCode"), 1, 50);
		long ExistingTOTSize = Utilities.parseLong(request.getParameter("ExistingTOTSize"));
		String MeterNumber=Utilities.filterString(request.getParameter("MeterNumber"), 1, 50);
		
		
		
		//For new Outlet
		
		
		
		String OutletNameN=Utilities.filterString(request.getParameter("OutletNameN"), 1, 50);
		
		String OutletAddressN=Utilities.filterString(request.getParameter("OutletAddressN"), 1, 50);
		String OutletContactNumberN=Utilities.filterString(request.getParameter("OutletContactNumberN"), 1, 50);
		String OutletContactNameN=Utilities.filterString(request.getParameter("OutletContactNameN"), 1, 50);
		String OutletContactNicN=Utilities.filterString(request.getParameter("OutletContactNicN"), 1, 50);
		long out_channel_idN=Utilities.parseLong(request.getParameter("out_channel_idN"));
		
		long out_distributor_idN=Utilities.parseLong(request.getParameter("OutletDistributorN"));
		String MeterNumberN=Utilities.filterString(request.getParameter("MeterNumberN"), 1, 50);
		
		
		
		
		
		//System.out.println(TOTCode+" - "+ExistingTOTSize);
		
		long[] SalesVolLRBType = Utilities.parseLong(request.getParameterValues("SelectPCItemTypeDetail"));
		long[] SalesVolPackageID = Utilities.parseLong(request.getParameterValues("SelectPCItemPackDetail"));
		long[] SalesVolQuantity = Utilities.parseLong(request.getParameterValues("SelectPCItemQuantityDetail"));
		
		
		long[] ReqTOTSize = Utilities.parseLong(request.getParameterValues("TOTSize"));
		
		
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		
		
		
		int IsNewOutlet = Utilities.parseInt(request.getParameter("NewOutletBox"));
		
		
		
		boolean ValidationCheck=false;
		
		JSONObject obj = new JSONObject();
		
		if(IsNewOutlet==0){ //Normal Case 
			if(OutletID!=0 && SalesVolLRBType!=null && ReqTOTSize!=null) //outlet id not be 0, LRB type must be selected and TOT size atleast one
			{
				ValidationCheck=true;
			}
		}else{
			if(OutletNameN!="" && SalesVolLRBType!=null && ReqTOTSize!=null) //outlet name not be empty, LRB type must be selected and TOT size atleast one
			{
				ValidationCheck=true;
			}
		}
		
		
		if(ValidationCheck){
		
		Datasource ds = new Datasource();
		
		long MasterTableCoolerInjectionID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
			String SQL="";	
			
			if(IsNewOutlet==0){ //normal case
				SQL = "INSERT INTO tot_issue_request(outlet_id,outlet_name,outlet_address,outlet_contact_number,outlet_contact_name,outlet_contact_cnic,outlet_channel,outlet_region,outlet_rsm,outlet_snd,outlet_distributor_id,tot_empty_status,existing_tot,tot_code,created_on,created_by,uvid,meter_number)"+
						" VALUES("+OutletID+",'"+OutletName+"','"+OutletAddress+"','"+OutletContactNumber+"','"+OutletContactName+"','"+OutletContactNic+"',"+out_channel_id+","+out_region_id+","+out_rsm_id+","+out_snd_id+","+out_distributor_id+",'"+EmptyStatus+"',"+ExistingTOTSize+",'"+TOTCode+"',now(),"+UserID+","+UniqueVoucherID+",'"+MeterNumber+"')";
			}else if (IsNewOutlet==1){
				SQL = "INSERT INTO tot_issue_request(outlet_name,outlet_address,outlet_contact_number,outlet_contact_name,outlet_contact_cnic,outlet_channel,outlet_distributor_id,tot_empty_status,existing_tot,tot_code,created_on,created_by,uvid,meter_number,is_new_outlet)"+
						" VALUES('"+OutletNameN+"','"+OutletAddressN+"','"+OutletContactNumberN+"','"+OutletContactNameN+"','"+OutletContactNicN+"',"+out_channel_idN+","+out_distributor_idN+",'"+EmptyStatus+"',"+ExistingTOTSize+",'"+TOTCode+"',now(),"+UserID+","+UniqueVoucherID+",'"+MeterNumberN+"',1)";
			}
			
				
			s.executeUpdate(SQL);	
			
					
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						MasterTableCoolerInjectionID = rs.getInt(1); 
					}
					
				 
				
				//sales vol insertion
				
				if(SalesVolLRBType != null)
				{
					for(int i=0;i<SalesVolLRBType.length;i++)
					{					
						//System.out.println(SalesVolLRBType[i]+" - "+SalesVolPackageID[i]+" - "+SalesVolQuantity[i]);
						
						s.executeUpdate("insert into tot_issue_request_sales(id,type,package_id,quantity) values("+MasterTableCoolerInjectionID+","+SalesVolLRBType[i]+","+SalesVolPackageID[i]+","+SalesVolQuantity[i]+")");
						
					}
				}
				
				
				
				//Beat plan insertion
				long[] BeatplanDays = Utilities.parseLong(request.getParameterValues("outletbeatplan"));
				
				
				if(BeatplanDays !=null){
					for(int i=0; i<BeatplanDays.length;i++){
						
						s.executeUpdate("insert into tot_issue_request_beat_plans(id,day_number) values("+MasterTableCoolerInjectionID+","+BeatplanDays[i]+")");
						
					}
				}
				
				
				///////
				
				
				
				// TOT Size insertion
				
				
				long ReqTOTQuantity = 0;
				
				if(ReqTOTSize !=null){
					for(int i=0; i<ReqTOTSize.length;i++){
						
						if(ReqTOTSize[i]+"" !=null){
							ReqTOTQuantity = Utilities.parseLong(request.getParameter("TOTSizeQuantity"+ReqTOTSize[i]));
						}
						
							//System.out.println("Hello - "+ReqTOTSize[i]+" - Quantity "+ReqTOTQuantity);
							
							s.executeUpdate("insert into tot_issue_request_sizes(id,tot_size_id,quantity) values("+MasterTableCoolerInjectionID+","+ReqTOTSize[i]+","+ReqTOTQuantity+")");
						
					}
				}
				
				
				long ProcessUserID = 0;
							ResultSet rs1 = s.executeQuery("select * from workflow_processes_steps where step_id=2 and process_id=6");
							
							if(rs1.first()){
								ProcessUserID = rs1.getLong("user_id");
							}
							
							
							Workflow wf = new Workflow();
							long WorkFlowRequestID = wf.createRequest(6, Integer.parseInt(UserID), ProcessUserID, 5, "Cooler Injection Request Raised");
							s.executeUpdate("update tot_issue_request set request_id="+WorkFlowRequestID+" where id="+MasterTableCoolerInjectionID);
							
							
				
				
				
				obj.put("product_promotion_id",MasterTableCoolerInjectionID);
				obj.put("success", "true");
				ds.commit();
				
		
				
				
				
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}else{
			obj.put("success", "false");
			obj.put("error", "Please insert valid data");
		}
		
		
		
		
		out.print(obj);
		out.close();
		
	}
	
}
