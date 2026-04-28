package com.pbc.tot;

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

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;


@WebServlet(description = "Monthly Discount Request Execute", urlPatterns = { "/tot/WorkflowManagerTOTCoolerInjectionRequestExecute" })

public class WorkflowManagerTOTCoolerInjectionRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerTOTCoolerInjectionRequestExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		boolean isEditCase = false;
		long EditID = Utilities.parseLong(request.getParameter("EditID"));
		if(EditID > 0){
			isEditCase = true;
		}

		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		
		
		long RequestIDVal = Utilities.parseLong(request.getParameter("RequestID"));
		String WorkflowStepRemarks = Utilities.filterString(request.getParameter("WorkflowStepRemarks"), 1, 100);
		int StepID = Utilities.parseInt(request.getParameter("StepID"));
		long NextStepID = Utilities.parseLong(request.getParameter("NextStepID"));
		int NextActionID = Utilities.parseInt(request.getParameter("NextActionID"));
		long NextUserID = Utilities.parseLong(request.getParameter("NextUserID"));
		boolean isLastStep = Utilities.parseBoolean(request.getParameter("isLastStep"));
		

		
	
		
		
		
		
		Datasource ds = new Datasource();
		
		try {
			
			//System.out.println("Hello");
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			Workflow wf = new Workflow();
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			
			
			if (StepID == 2){
				 
				
				//Email here for testing
				long NewOutletID=Utilities.parseLong(request.getParameter("NewOutletID"));
				
				//String HTMLEmail = WorkflowEmail.getPromotionRequestHTMLWithActionButtons(RequestIDVal);
				int isNewOutlet=0;
				ResultSet rs = s.executeQuery("select is_new_outlet from tot_issue_request where request_id="+RequestIDVal);
				while(rs.next()){
					isNewOutlet=rs.getInt("is_new_outlet");
				}
				
				if(isNewOutlet==1){
					s.executeUpdate("update tot_issue_request set outlet_id="+NewOutletID+" where request_id="+RequestIDVal);
				}
				//s.executeUpdate("update inventory_sales_promotions_request set estimated_sales_volume="+estimated_sales_volume+",sales_sku_price="+sales_sku_price+",free_sku_price="+free_sku_price+",variable_cost_and_taxes="+variable_cost_and_taxes+",marginal_contribution="+marginal_contribution+",net_price="+NetPrice+" where request_id="+RequestIDVal);
				
			}
			
			
		
			
			
			//System.out.println(RequestIDVal+" - "+StepID+" - "+isLastStep+" - "+NextUserID+" - "+NextActionID+" - "+WorkflowStepRemarks);
			
			wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
			
			
			
			
			//System.out.println("hello");
			
			String CollerName="tesst";
			
			
			
			
			
			
			if (isLastStep == false){
				WorkflowChat chat = new WorkflowChat(RequestIDVal);
				if (WorkflowStepRemarks != null && WorkflowStepRemarks.length() > 0){
					chat.createConversation(Long.parseLong(UserID), NextUserID, WorkflowStepRemarks);
				}
				chat.close();
			}
			
			if (StepID == 5){ // CE Approval
				
				//String HTMLEmailCOO = WorkflowEmail.getCoolerInjectionRequestHTMLWithActionButtonsCOO(RequestIDVal);
				
				

				
				///////////////////////Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk"}, null, null, "Cooler Injection Request | ID#"+RequestIDVal, HTMLEmailCOO, null);
				
			
			}
			if(StepID == 6){  //final step
				
				String AssetNumber =Utilities.filterString(request.getParameter("TOTAssetNumber"),1,100);
				
				String CoolerInjectionCode="";
				ResultSet rs123 = s.executeQuery("select * from tot_issue_request where request_id="+RequestIDVal);
				while(rs123.next()){
					CoolerInjectionCode = rs123.getString("tot_code");
				}
				
				s.executeUpdate("update tot_issue_request set is_active=1,asset_number='"+AssetNumber+"' where request_id="+RequestIDVal);
				
				
				ds.commit();
				
				System.out.println("Approved");
				
				
				wf.doStepAction(RequestIDVal, 6, true, 0, 0, "");
				wf.close();
				
				
				
				String HTMLEmail = WorkflowEmail.getCoolerInjectionRequestHTMLWithoutActionButtons(RequestIDVal);
				
				
				
				Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null,null,  "[Approval Notification] TOT Coooler Injection | "+CoolerInjectionCode+" | ID#"+RequestIDVal, HTMLEmail, null);
			}
			


			
			
			//response.setContentType("application/json");
			
			obj.put("success", "true");
			obj.put("RequestID", ""+RequestIDVal);
			

			
			ds.commit();
			
			ds.dropConnection();
			
			
			if(!isEditCase){
				//response.sendRedirect("home.jsp");
			}
			
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		out.print(obj);
		out.close();
		
	}
	
}