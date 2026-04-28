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


@WebServlet(description = "Monthly Discount Request Execute", urlPatterns = { "/inventory/WorkflowManagerDiscountRequestExecute" })

public class WorkflowManagerDiscountRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerDiscountRequestExecute() {
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
		
		long[] SalesPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsMainFormPackage"));
		int ProductTableID =  Utilities.parseInt(request.getParameter("ProductTableID"));
		
		
		double Quantity[] = Utilities.parseDouble(request.getParameterValues("Quantity"));
		double DiscountRate[] = Utilities.parseDouble(request.getParameterValues("DiscountRate"));
		double SellingPrice[] = Utilities.parseDouble(request.getParameterValues("SellingPrice"));
		double VariableCost[] = Utilities.parseDouble(request.getParameterValues("VariableCost"));
		double PromotionCost[] = Utilities.parseDouble(request.getParameterValues("PromotionCost"));
		double MarginalContribution[] = Utilities.parseDouble(request.getParameterValues("MarginalContribution"));
		
		
		
		Datasource ds = new Datasource();
		
		try {
			
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			Workflow wf = new Workflow();
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			
			
			/*
			if (StepID == 2){
				
				NextUserID = EmployeeHierarchy.getSDHead(1).USER_ID;
				
				ResultSet rs2 = s.executeQuery("SELECT isprd.distributor_id, (select region_id from common_distributors where distributor_id = isprd.distributor_id) region_id FROM inventory_sales_discounts_request ispr join inventory_sales_discounts_request_distributors isprd on ispr.id = isprd.product_promotion_id where ispr.request_id = "+RequestIDVal);
				while(rs2.next()){
					if (rs2.getInt(2) == 1 || rs2.getInt(2) == 3 || rs2.getInt(2) == 6 || rs2.getInt(2) == 10){
						NextUserID = EmployeeHierarchy.getSDHead(2).USER_ID;
					}
				}

				
				
			
				
				//updating 
				
				if(SalesPacakgeID != null)
				{
					for(int i=0;i<SalesPacakgeID.length;i++)
					{					
						
						
						//System.out.println("update inventory_sales_discounts_request_products set quantity="+Quantity[i]+",discount_rate="+DiscountRate[i]+",selling_price="+SellingPrice[i]+",variable_cost="+VariableCost[i]+",promotion_cost="+PromotionCost[i]+",marginal_contribution="+MarginalContribution[i]+" where package_id="+SalesPacakgeID[i]+" and id="+ProductTableID);
						
						s.executeUpdate("update inventory_sales_discounts_request_products set quantity="+Quantity[i]+",discount_rate="+DiscountRate[i]+",selling_price="+SellingPrice[i]+",variable_cost="+VariableCost[i]+",promotion_cost="+PromotionCost[i]+",marginal_contribution="+MarginalContribution[i]+" where package_id="+SalesPacakgeID[i]+" and id="+ProductTableID);
				}
				
				
				
			}
			}*/
			
			
			
			wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
			
			
			
			
			//System.out.println("hello");
			
			
			wf.close();
			
			
			/*
			if (isLastStep == false){
				WorkflowChat chat = new WorkflowChat(RequestIDVal);
				if (WorkflowStepRemarks != null && WorkflowStepRemarks.length() > 0){
					chat.createConversation(Long.parseLong(UserID), NextUserID, WorkflowStepRemarks);
				}
				chat.close();
			}
			*/
			
			
			
			
			
			
			
			if (StepID == 2){ // CE Approval
				String HTMLEmail = WorkflowEmail.getDiscountRequestHTMLWithActionButtons(RequestIDVal);
				String HTMLEmailCOO = WorkflowEmail.getDiscountRequestHTMLWithActionButtonsCOO(RequestIDVal);
				
				
				String DistributorName ="";
				ResultSet rs = s.executeQuery("SELECT distributor_id,(select name from common_distributors cd where isdrd.distributor_id=cd.distributor_id) name FROM inventory_sales_discounts_request isdr join inventory_sales_discounts_request_distributors isdrd on isdr.id=isdrd.product_promotion_id where isdr.request_id="+RequestIDVal);
				if(rs.first()){
					DistributorName = rs.getString("name");
				}
				
				
				
				//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, new String[]{"dev@pbc.com.pk"}, "Upfront Discount Request | ID#"+RequestIDVal, HTMLEmail, null);
				
				
				
				
				
				
				
				Utilities.sendPBCHTMLEmail(new String[]{"Khurram.jaffar@pbc.com.pk"},null, new String[]{"anas.wahab@pbc.com.pk"}, "Upfront Discount Request | "+DistributorName+" | ID#"+RequestIDVal, HTMLEmail, null);
				Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Upfront Discount Request | "+DistributorName+" | ID#"+RequestIDVal, HTMLEmailCOO, null);
				
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