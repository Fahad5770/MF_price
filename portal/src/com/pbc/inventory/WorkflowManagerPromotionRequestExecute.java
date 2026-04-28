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

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;


@WebServlet(description = "Monthly Discount Request Execute", urlPatterns = { "/inventory/WorkflowManagerPromotionRequestExecute" })

public class WorkflowManagerPromotionRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerPromotionRequestExecute() {
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
		

		double estimated_sales_volume = Utilities.parseDouble(request.getParameter("EstimatedSalesVolumeRawCases"));
		double sales_sku_price = Utilities.parseDouble(request.getParameter("SalesSKUPrice"));
		double free_sku_price = Utilities.parseDouble(request.getParameter("FreeSKUPriceBottles"));
		double variable_cost_and_taxes = Utilities.parseDouble(request.getParameter("VariableCost"));
		double marginal_contribution = Utilities.parseDouble(request.getParameter("MarginalContribution"));
		double NetPrice = Utilities.parseDouble(request.getParameter("netprice"));
	
		
		
		
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			Workflow wf = new Workflow();
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			
			
			if (StepID == 2){
				 
				
				
				NextUserID = EmployeeHierarchy.getSDHead(4).USER_ID;
				//NextUserID = 2011;
				
				ResultSet rs = s.executeQuery("SELECT isprr.region_id FROM inventory_sales_promotions_request ispr join inventory_sales_promotions_request_regions isprr on ispr.id = isprr.product_promotion_id where ispr.request_id = "+RequestIDVal);
				while(rs.next()){
					if (rs.getInt(1) == 2 || rs.getInt(1) == 7){
						NextUserID = EmployeeHierarchy.getSDHead(2).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
							//NextUserID = 3628;
						//}
						
						
					}
					if (rs.getInt(1) == 5 || rs.getInt(1) == 11 || rs.getInt(1) == 4){
						NextUserID = EmployeeHierarchy.getSDHead(1).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
							//NextUserID = 3628;
						//}
					}
					if (rs.getInt(1) == 1 || rs.getInt(1) == 10){
						NextUserID = EmployeeHierarchy.getSDHead(6).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
							//NextUserID = 3628;
						//}
					}
					if (rs.getInt(1) == 8){
						NextUserID = EmployeeHierarchy.getSDHead(5).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
							//NextUserID = 3628;
						//}
					}
					
					
				}

				ResultSet rs2 = s.executeQuery("SELECT isprd.distributor_id, (select region_id from common_distributors where distributor_id = isprd.distributor_id) region_id FROM inventory_sales_promotions_request ispr join inventory_sales_promotions_request_distributors isprd on ispr.id = isprd.product_promotion_id where ispr.request_id = "+RequestIDVal);
				while(rs2.next()){
					if (rs2.getInt(1) == 2 || rs2.getInt(1) == 7){
						NextUserID = EmployeeHierarchy.getSDHead(2).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
					if (rs2.getInt(2) == 5 || rs2.getInt(2) == 11 || rs2.getInt(2) == 4){
						NextUserID = EmployeeHierarchy.getSDHead(1).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
					if (rs2.getInt(2) == 1 || rs2.getInt(2) == 10){
						NextUserID = EmployeeHierarchy.getSDHead(6).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
					if (rs2.getInt(2) == 8){
						NextUserID = EmployeeHierarchy.getSDHead(5).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
					
					
				}

				ResultSet rs3 = s.executeQuery("SELECT dbp.distributor_id, (select region_id from common_distributors where distributor_id = dbp.distributor_id) region_id FROM inventory_sales_promotions_request ispr join inventory_sales_promotions_request_pjp isprp on ispr.id = isprp.product_promotion_id join distributor_beat_plan dbp on isprp.pjp_id = dbp.id where ispr.request_id = "+RequestIDVal);
				while(rs3.next()){
					if (rs3.getInt(1) == 2 || rs3.getInt(1) == 7){ 
						NextUserID = EmployeeHierarchy.getSDHead(2).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
						//NextUserID = 2252;
					}
					if (rs3.getInt(2) == 5 || rs3.getInt(2) == 11 || rs3.getInt(2) == 4){ 
						NextUserID = EmployeeHierarchy.getSDHead(1).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
					if (rs3.getInt(2) == 1 || rs3.getInt(2) == 10){ 
						NextUserID = EmployeeHierarchy.getSDHead(6).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
					if (rs3.getInt(2) == 8){ 
						NextUserID = EmployeeHierarchy.getSDHead(5).USER_ID;
						
						//if(NextUserID==3845){ //this sd is on hajj leaves so forward his req to 
						//	NextUserID = 3628;
						//}
					}
				}
				
				
				s.executeUpdate("update inventory_sales_promotions_request set estimated_sales_volume="+estimated_sales_volume+",sales_sku_price="+sales_sku_price+",free_sku_price="+free_sku_price+",variable_cost_and_taxes="+variable_cost_and_taxes+",marginal_contribution="+marginal_contribution+",net_price="+NetPrice+" where request_id="+RequestIDVal);
				
				
			}
			
			String PromotionName="";
			ResultSet rs1 = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestIDVal);
			while(rs1.next()){
				PromotionName = rs1.getString("label");
			}
		
			
			
			
			
			
			
			
			wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
			
			
			
			
			//System.out.println("hello");
			
			
			wf.close();
			
			
			
			if (isLastStep == false){
				WorkflowChat chat = new WorkflowChat(RequestIDVal);
				if (WorkflowStepRemarks != null && WorkflowStepRemarks.length() > 0){
					chat.createConversation(Long.parseLong(UserID), NextUserID, WorkflowStepRemarks);
				}
				chat.close();
			}
			
			if (StepID == 3){ // CE Approval
				String HTMLEmail = WorkflowEmail.getPromotionRequestHTMLWithActionButtons(RequestIDVal);
				String HTMLEmailCOO = WorkflowEmail.getPromotionRequestHTMLWithActionButtonsCOO(RequestIDVal);
				
				
				//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, new String[]{"dev@pbc.com.pk"}, "Promotion Request | "+PromotionName+" | ID#"+RequestIDVal, HTMLEmail, null);
				
				
				
				

				Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Promotion Request | "+PromotionName+" | ID#"+RequestIDVal, HTMLEmail, null);
				Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Promotion Request | "+PromotionName+" | ID#"+RequestIDVal, HTMLEmailCOO, null);
				
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