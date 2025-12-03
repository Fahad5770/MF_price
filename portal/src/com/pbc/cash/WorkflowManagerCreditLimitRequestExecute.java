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

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;


@WebServlet(description = "Credit Limit Request Execute", urlPatterns = { "/cash/WorkflowManagerCreditLimitRequestExecute" })

public class WorkflowManagerCreditLimitRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerCreditLimitRequestExecute() {
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
		String CustomerName = Utilities.filterString(request.getParameter("CustomerName"), 2, 200);
		
		

		//System.out.println("helll "+NextUserID);
	
		
		
		
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			Workflow wf = new Workflow();
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			
			
			
			
			String PromotionName="";
			
			
			PromotionName = "Customer Credit Limit Request# "+RequestIDVal;
			
			//System.out.println("Hello "+NextUserID);
			
			
			if (StepID == 1){
				wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
			}
			
			
			if (StepID == 2){
				wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
			}
			
			if (StepID == 3){
				 
				
				wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
				
				
				//Patch by Zulqurnan on 14/03/2017 (on Request by Sohaib "Theia Process Flow Change")
				
				double CreditLimitAmount=0;
				
				ResultSet rs = s.executeQuery("SELECT * FROM pep.gl_customer_credit_limit_request where request_id="+RequestIDVal);
				if(rs.first()){
					CreditLimitAmount = rs.getDouble("credit_limit");
				}
				
				
				if(CreditLimitAmount>=5000000){
					//System.out.println("Credit Limit >5000000");
					
					String HTMLEmail = WorkflowEmail.getCreditLimitRequestHTMLWithActionButtons(RequestIDVal);
					
					
					Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Credit Limit Request | "+CustomerName+" | ID#"+RequestIDVal, HTMLEmail, null);
				}else{
					//System.out.println("Credit limit in else case...");
					wf.doStepAction(RequestIDVal, StepID, isLastStep, EmployeeHierarchy.getHeadOfSales().USER_ID, NextActionID, WorkflowStepRemarks); //send it to Khuram Jaffer
				}
				
				
				//////////////////////////////////////////////////////////////////////////////////
				
			}
			
			//System.out.println("hello");
			
			
			
			
			
			
			if (isLastStep == false){
				WorkflowChat chat = new WorkflowChat(RequestIDVal);
				if (WorkflowStepRemarks != null && WorkflowStepRemarks.length() > 0){
					chat.createConversation(Long.parseLong(UserID), NextUserID, WorkflowStepRemarks);
				}
				chat.close();
			}
			
			if (StepID == 4){ // Final Approval
				System.out.println("This is final step 4 "+NextActionID);
				
				wf.doStepAction(RequestIDVal, 3, true, 0, 0, ""); //close this request
			}
			
			
			
			wf.close();
			
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