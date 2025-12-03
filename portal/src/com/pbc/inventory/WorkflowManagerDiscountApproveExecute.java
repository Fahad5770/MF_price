package com.pbc.inventory;

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


@WebServlet(description = "Discount Request Execute", urlPatterns = { "/WM/WMD" })

public class WorkflowManagerDiscountApproveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerDiscountApproveExecute() {
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
			
			
			String isCOO = request.getParameter("secondary");
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			long RequestID = 0;
			long UVID = Utilities.parseLong(request.getParameter("token"));
			
			long PromotionID=0;
			
			ResultSet rs8 = s.executeQuery("select id,request_id from inventory_sales_discounts_request where uvid="+UVID);
			if(rs8.first()){
				PromotionID = rs8.getLong("id");
				RequestID =  rs8.getLong("request_id");
			}
			
			//System.out.println(PromotionID+" "+RequestID);
			
			boolean IsAlreadyProcessed = false;
			boolean IsAlreadyDeclined = false;
			
			/*
			ResultSet rs7 = s.executeQuery("select * from inventory_sales_promotions where request_id="+RequestID);
			if(rs7.first()){
				IsAlreadyProcessed = true;
				
			}
			*/
			
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
			
				
				//System.out.println("Promotion ID "+PromotionID+" UVID "+UVID);
				
				double estimated_sales_volume = 0;
				double sales_sku_price = 0;
				double free_sku_price = 0;
				double variable_cost_and_taxes = 0;
				double marginal_contribution = 0;
				
				
				String PriceListName = "";
				String ValidFrom = "";
				String ValidTo = "";
				int Active = 0;
				
				//long MasterTablePromotionID = 0;
				
				long UserID = 0;
				if(request.getParameter("action").equals("1")){//approve
					
					if (isCOO != null){
						s.executeUpdate("update workflow_requests_steps set user_id = 2577 where request_id = "+RequestID+" and step_id = 3");
					}else{
						//s.executeUpdate("update workflow_requests_steps set user_id = 9000123 where request_id = "+RequestID+" and step_id = 3");
						s.executeUpdate("update workflow_requests_steps set user_id = 3644 where request_id = "+RequestID+" and step_id = 3");

					}
					
					ds.commit();
					
					out.println("Approved");
					
					Workflow wf = new Workflow();
					wf.doStepAction(RequestID, 3, true, 0, 0, "");
					wf.close();
					
					
					
					String HTMLEmail = WorkflowEmail.getDiscountRequestHTMLWithoutActionButtons(RequestID);
					
					/*
					String PromotionName="";
					ResultSet rs1 = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestID);
					while(rs1.next()){
						PromotionName = rs1.getString("label");
					}
					*/
					
					
					
					// Get SD Head
					/*
					String SDEmail = "";
					ResultSet rs2 = s.executeQuery("select wrs.user_id, (select email from users where id = wrs.user_id) email from workflow_requests_steps wrs where wrs.request_id = "+RequestID+" and wrs.step_id = 3");
					if (rs2.first()){
						SDEmail = rs2.getString(2);
					}
					*/
					
					
					
					
					
					
					
					
					
					
					
					
					
					Utilities.sendPBCHTMLEmail(new String[]{"mashraf@pbc.com.pk","asim.maan@pbc.com.pk","fsdinv@pbc.com.pk","Khurram.jaffar@pbc.com.pk"}, new String[]{"omerfk@pbc.com.pk","salman.baig@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk","nawaz@pbc.com.pk","jazeb@pbc.com.pk",EmployeeHierarchy.getSDHead(2).USER_EMAIL,"sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "[Approval Notification] Upfront Discount | ID#"+RequestID, HTMLEmail, null);
					
					
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "[Approval Notification] Upfront Discount | ID#"+RequestID, HTMLEmail, null);
					
				}else if(request.getParameter("action").equals("2")){ //decline
					
					
					long DeclinedBy = 2577;
					if (isCOO != null){
						DeclinedBy = 2577;
					}else{
						DeclinedBy = 9000123;

					}
					
					
					Workflow wf = new Workflow();
					wf.doDecline(RequestID, 3, DeclinedBy,"");
					wf.close();
				    out.println("Declined");
				    
				    
				    
				    String HTMLEmail = WorkflowEmail.getDiscountRequestHTMLWithoutActionButtons(RequestID);
					
				    
				    
				    Utilities.sendPBCHTMLEmail(new String[]{"mashraf@pbc.com.pk","asim.maan@pbc.com.pk"}, new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk","jazeb@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "[Decline Notification] Upfront Discount | ID#"+RequestID, HTMLEmail, null);
				    
				    
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