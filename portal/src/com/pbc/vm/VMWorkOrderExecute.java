package com.pbc.vm;

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


@WebServlet(description = "Price List ", urlPatterns = { "/vm/VMWorkOrderExecute" })
public class VMWorkOrderExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public VMWorkOrderExecute() {
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
		
		
		
		String VMVehicleNo=Utilities.filterString(request.getParameter("VMVehicleNo"), 1, 100);
		String VMVehicleType=Utilities.filterString(request.getParameter("VMVehicleType"), 1, 100);
		String VMMeterReading=Utilities.filterString(request.getParameter("VMMeterReading"), 1, 100);
		
		long VMRequestTo=Utilities.parseLong(request.getParameter("VMRequestTo"));
		long VMRequestBy=Utilities.parseLong(request.getParameter("VMRequestBy"));
		long VMInspectedBy=Utilities.parseLong(request.getParameter("VMInspectedBy"));
		
		String VMDepartment=Utilities.filterString(request.getParameter("VMDepartment"), 1, 100);
		
		long VMJobAssignedBy=Utilities.parseLong(request.getParameter("VMJobAssignedBy"));
		long VMJobAssignedTo=Utilities.parseLong(request.getParameter("VMJobAssignedTo"));
		long VMJobConfirmedBy=Utilities.parseLong(request.getParameter("VMJobConfirmedBy"));
		long VMCustodian=Utilities.parseLong(request.getParameter("VMCustodian"));
		
		
		Date VMJobCompletedDate=Utilities.parseDate(request.getParameter("VMJobCompletedDate"));
		
		
		long[] SelectVMInHouseAct = Utilities.parseLong(request.getParameterValues("SelectVMInHouseAct"));
		long[] SelectVMOutsourceAct = Utilities.parseLong(request.getParameterValues("SelectVMOutsourceAct"));
		
		String PartsIssuanceDetail=Utilities.filterString(request.getParameter("PartsIssuanceDetail"), 1, 200);
		
		double InHouseCost = Utilities.parseDouble(request.getParameter("InHouseCost"));
		double OutSourceCost = Utilities.parseDouble(request.getParameter("OutSourceCost"));
		double TotalCost = Utilities.parseDouble(request.getParameter("TotalCost"));
		
		String Comments=Utilities.filterString(request.getParameter("comments"), 1, 200);
		
		
		
		
		
		
		
		
		
		boolean ValidationCheck=false;
		
		JSONObject obj = new JSONObject();
		
		if(!VMVehicleNo.equals("")){ //checking vehicle number
			ValidationCheck=true;
		}
		
		
		if(ValidationCheck){
		
		Datasource ds = new Datasource();
		
		long MasterTableCoolerInjectionID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
			String SQL="INSERT INTO vm_workorder(vehicle_no,vehicle_type,meter_reading,request_to,request_by,inspected_by,department,custodian,job_assigned_by,job_assigned_to,job_confirmed_by,job_completion_date,part_issuance_detail,inhouse_activiy_cost,outsource_activity_cost,total_cost,comments,created_on,created_by)VALUES('"+VMVehicleNo+"','"+VMVehicleType+"','"+VMMeterReading+"',"+VMRequestTo+","+VMRequestBy+","+VMInspectedBy+",'"+VMDepartment+"',"+VMCustodian+","+VMJobAssignedBy+","+VMJobAssignedTo+","+VMJobConfirmedBy+","+Utilities.getSQLDate(VMJobCompletedDate)+",'"+PartsIssuanceDetail+"',"+InHouseCost+","+OutSourceCost+","+TotalCost+",'"+Comments+"',now(),"+UserID+")";	
												 
				
			s.executeUpdate(SQL);	
			
					
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						MasterTableCoolerInjectionID = rs.getInt(1); 
					}
					
				 
				
				//sales vol insertion
				
				if(SelectVMInHouseAct != null)
				{
					for(int i=0;i<SelectVMInHouseAct.length;i++)
					{					
						//System.out.println(SalesVolLRBType[i]+" - "+SalesVolPackageID[i]+" - "+SalesVolQuantity[i]);
						
						s.executeUpdate("insert into vm_wordorder_tasks(id,task_id) values("+MasterTableCoolerInjectionID+","+SelectVMInHouseAct[i]+")");
						
					}
				}
				
				
				
				//for outsource insertion
				
				if(SelectVMOutsourceAct !=null){
					for(int i=0; i<SelectVMOutsourceAct.length;i++){
						
						s.executeUpdate("insert into vm_wordorder_tasks(id,task_id) values("+MasterTableCoolerInjectionID+","+SelectVMOutsourceAct[i]+")");
						
					}
				}
				
				
				///////
				
				
				
				
				
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
