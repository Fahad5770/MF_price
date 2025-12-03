package com.pbc.empty;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;


@WebServlet(description = "Empty Credit Limit Request Approve", urlPatterns = { "/WM/WMECL" })

public class WorkflowManagerEmptyCreditLimitApproveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerEmptyCreditLimitApproveExecute() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Datasource ds = new Datasource();
		
		
		PrintWriter out = response.getWriter();
		
		try {
			
			
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			long RequestID = 0;
			long UVID = Utilities.parseLong(request.getParameter("token"));
			
			long PromotionID=0;
			String CustomerName="";
			long CustomerID=0;
			
			
			ResultSet rs8 = s.executeQuery("select id,request_id,ececlr.distributor_id,(select name from common_distributors cd where cd.distributor_id = ececlr.distributor_id) customer_name from ec_empty_credit_limit_request ececlr where uvid="+UVID);
			if(rs8.first()){
				PromotionID = rs8.getLong("id");
				RequestID =  rs8.getLong("request_id");
				CustomerName = rs8.getLong("distributor_id")+" - "+rs8.getString("customer_name");
				CustomerID= rs8.getLong("distributor_id");
			}
			
			//System.out.println(PromotionID+" "+RequestID);
			
			boolean IsAlreadyProcessed = false;
			boolean IsAlreadyDeclined = false;
			
			//ResultSet rs7 = s.executeQuery("select * from inventory_sales_promotions where request_id="+RequestID);
			//if(rs7.first()){
			//	IsAlreadyProcessed = true;
				
			//}
			
			
			ResultSet rs9 = s.executeQuery("SELECT status_id FROM workflow_requests where request_id="+RequestID);
			if(rs9.first()){
				
				if(rs9.getInt("status_id")==3 || rs9.getInt("status_id")==2){
					IsAlreadyDeclined = true;
				}
				
			}
			
			if (PromotionID == 0 || RequestID == 0){
				IsAlreadyProcessed = true;
				IsAlreadyDeclined = true;
			}
			
			
			if(IsAlreadyProcessed || IsAlreadyDeclined){
				out.println("URL Expired");
			}else{
			
				
				
				
				
				String PriceListName = "";
				String ValidFrom = "";
				String ValidTo = "";
				int Active = 0;
				
				//long MasterTablePromotionID = 0;
				
				long UserID = 0;
				if(request.getParameter("action").equals("1")){//approve
					
					
					long MainTableID=0;
					
					ResultSet rs = s.executeQuery("select id from ec_empty_credit_limit_request where request_id="+RequestID);
					if(rs.first()){
						MainTableID = rs.getLong("id");
					}
					
					
					
					
					System.out.println("Request ID :"+RequestID+" - "+MainTableID);
					
					//Update Empty Credit Requst table is_active=1
					
					s.executeUpdate("update ec_empty_credit_limit_request set is_active=1, activated_on=now() where id="+MainTableID);
					
					
					///////////////////////////////////////////////
					//////////////////////////////////////////////
					
					
					/*int MasterTableID=0;
					
					s.executeUpdate("INSERT INTO ec_empty_credit_limit (created_on,created_by,uvid,distributor_id,credit_type,reason,start_date,end_date,is_active,activated_on,deactivated_on,request_id) select created_on,created_by,uvid,distributor_id,credit_type,reason,start_date,end_date,is_active,activated_on,deactivated_on,request_id from ec_empty_credit_limit_request where request_id="+RequestID);
					
					ResultSet rs11 = s.executeQuery("select LAST_INSERT_ID()");
					if(rs11.first()){
						MasterTableID = rs11.getInt(1); 
					}
					
					
					
					//System.out.println(MainTableID);
					
					ResultSet rs12 = s.executeQuery("select * from ec_empty_credit_limit_request_products where id="+MainTableID);
					while(rs12.next()){
						s2.executeUpdate("INSERT INTO ec_empty_credit_limit_products (id,package_id,raw_cases,units,total_units,liquid_in_ml,type_id) values("+MasterTableID+","+rs12.getInt("package_id")+","+rs12.getInt("raw_cases")+","+rs12.getInt("units")+","+rs12.getInt("total_units")+","+rs12.getInt("liquid_in_ml")+","+rs12.getInt("type_id")+")");
					}
					
					
					
					*/
					
					
					out.println("Approved");
					int NextUserID = 0;
					
					ResultSet rs1 = s.executeQuery("SELECT user_id FROM pep.workflow_processes_steps where step_id=4 and process_id=9");
					if(rs1.first()){
						NextUserID = rs1.getInt("user_id");
					}
					
					
					Workflow wf = new Workflow();
					wf.doStepAction(RequestID, 3, true, NextUserID, 4, "");
					wf.close();
					
					
					
					String HTMLEmail = WorkflowEmail.getEmptyCreditLimitRequestHTMLWithoutActionButtons(RequestID);
					
					
					
					
					
					
					// Get SD Head
					String SDEmail = "";
					ResultSet rs2 = s.executeQuery("select wrs.user_id, (select email from users where id = wrs.user_id) email from workflow_requests_steps wrs where wrs.request_id = "+RequestID+" and wrs.step_id = 2");
					if (rs2.first()){
						SDEmail = rs2.getString(2);
					}
					
					
					
					//Utilities.sendPBCHTMLEmail(new String[]{"imran.hashim@pbc.com.pk",SDEmail,"obaid@pbc.com.pk"},new String[]{"atiq.baloch@pbc.com.pk","nawaz@pbc.com.pk"},new String[]{"anas.wahab@pbc.com.pk"}, "[Approval Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
					
					
					/////Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk"},null,null, "[Approval Notification] Empty Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
					
					
					Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk",SDEmail,"sohaib.zahid@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","nawaz@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","abid.hussain@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk"},new String[]{"anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"}, "[Approval Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
					
					
				}else if(request.getParameter("action").equals("2")){ //decline
					
					int FinalUserID = 0;
					
					ResultSet rs1 = s.executeQuery("SELECT user_id FROM pep.workflow_processes_steps where step_id=4 and process_id=9");
					if(rs1.first()){
						FinalUserID = rs1.getInt("user_id");
					}
					
					
					
					Workflow wf = new Workflow();
					wf.doDecline(RequestID, 3, FinalUserID,"");
					wf.close();
				    out.println("Declined");
				    
				    
				    
				    String HTMLEmail = WorkflowEmail.getEmptyCreditLimitRequestHTMLWithoutActionButtons(RequestID);
					
				
				   ///// Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk"}, null, null, "[Decline Notification] Empty Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
				    
				    Utilities.sendPBCHTMLEmail(new String[]{"sohaib.zahid@pbc.com.pk","abid.hussain@pbc.com.pk","salman.baig@pbc.com.pk","omerfk@pbc.com.pk","asim.maan@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"}, "[Decline Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
				    
				}
				
				
				ds.commit();
				
				
			}
			
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		out.close();
		
		
		
	}
	
}