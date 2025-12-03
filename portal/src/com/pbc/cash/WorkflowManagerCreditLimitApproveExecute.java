package com.pbc.cash;

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


@WebServlet(description = "Credit Limit Request Approve", urlPatterns = { "/WM/WMCL" })

public class WorkflowManagerCreditLimitApproveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerCreditLimitApproveExecute() {
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
			
			
			ResultSet rs8 = s.executeQuery("select id,request_id,customer_id,(select name from common_distributors cd where cd.distributor_id = customer_id) customer_name from gl_customer_credit_limit_request where uvid="+UVID);
			if(rs8.first()){
				PromotionID = rs8.getLong("id");
				RequestID =  rs8.getLong("request_id");
				CustomerName = rs8.getLong("customer_id")+" - "+rs8.getString("customer_name");
				CustomerID= rs8.getLong("customer_id");
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
					
					
					
					//posting record to original table from request table
					//System.out.println("update gl_customer_credit_limit set is_active=0 where customer_id="+CustomerID);
					
					/////////////////////////////////////////s.executeUpdate("update gl_customer_credit_limit set is_active=0 where customer_id="+CustomerID);
					
					/////////////////////////////////////////s.executeUpdate("insert into gl_customer_credit_limit(customer_id,credit_limit,valid_from,valid_to,uvid,is_active,activated_on,activated_by,deactivated_on,deactivated_by,one_time_credit_account_id,request_id,comments,cheque_no,cheque_bank,cheque_branch) select customer_id,credit_limit,valid_from,valid_to,uvid,is_active,activated_on,activated_by,deactivated_on,deactivated_by,one_time_credit_account_id,request_id,comments,cheque_no,cheque_bank,cheque_branch from gl_customer_credit_limit_request where request_id="+RequestID);
					
					
					
					
					
					out.println("Approved");
					
					Workflow wf = new Workflow();
					wf.doStepAction(RequestID, 3, true, 0, 0, "");
					wf.close();
					
					
					
					String HTMLEmail = WorkflowEmail.getCreditLimitRequestHTMLWithoutActionButtons(RequestID);
					
					
					
					
					
					
					// Get SD Head
					String SDEmail = "";
					ResultSet rs2 = s.executeQuery("select wrs.user_id, (select email from users where id = wrs.user_id) email from workflow_requests_steps wrs where wrs.request_id = "+RequestID+" and wrs.step_id = 2");
					if (rs2.first()){
						SDEmail = rs2.getString(2);
					}
					
					
					
					
					Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk",SDEmail,"sohaib.zahid@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","nawaz@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","abid.hussain@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk"},new String[]{"anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, "[Approval Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
					
					
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null,null, "[Approval Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
					
					
				}else if(request.getParameter("action").equals("2")){ //decline
					Workflow wf = new Workflow();
					wf.doDecline(RequestID, 3, 2577,"");
					wf.close();
				    out.println("Declined");
				    
				    
				    
				    String HTMLEmail = WorkflowEmail.getCreditLimitRequestHTMLWithoutActionButtons(RequestID);
					
				
				    //Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "[Decline Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
				    
				    Utilities.sendPBCHTMLEmail(new String[]{"sohaib.zahid@pbc.com.pk","abid.hussain@pbc.com.pk","salman.baig@pbc.com.pk","omerfk@pbc.com.pk","asim.maan@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "[Decline Notification] Credit Limit | "+CustomerName+" | ID#"+RequestID, HTMLEmail, null);
				    
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