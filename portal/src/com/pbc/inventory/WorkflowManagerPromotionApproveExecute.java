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


@WebServlet(description = "Monthly Discount Request Execute", urlPatterns = { "/WM/WMP" })

public class WorkflowManagerPromotionApproveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowManagerPromotionApproveExecute() {
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
			
			ResultSet rs8 = s.executeQuery("select id,request_id from inventory_sales_promotions_request where uvid="+UVID);
			if(rs8.first()){
				PromotionID = rs8.getLong("id");
				RequestID =  rs8.getLong("request_id");
			}
			
			//System.out.println(PromotionID+" "+RequestID);
			
			boolean IsAlreadyProcessed = false;
			boolean IsAlreadyDeclined = false;
			
			ResultSet rs7 = s.executeQuery("select * from inventory_sales_promotions where request_id="+RequestID);
			if(rs7.first()){
				IsAlreadyProcessed = true;
				
			}
			
			
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
					
					ResultSet rs = s.executeQuery("select * from inventory_sales_promotions_request where id="+PromotionID);
					if(rs.first()){
						 RequestID  = rs.getLong("request_id");
						 UserID  = rs.getLong("created_by");
						 estimated_sales_volume = rs.getDouble("estimated_sales_volume");
						 sales_sku_price = rs.getDouble("sales_sku_price"); 
						 free_sku_price = rs.getDouble("free_sku_price");
						 variable_cost_and_taxes = rs.getDouble("variable_cost_and_taxes");
						 marginal_contribution = rs.getDouble("marginal_contribution");
						 
						 PriceListName = rs.getString("label");
						 ValidFrom = rs.getString("valid_from");
						 ValidTo = rs.getString("valid_to");
						 Active = rs.getInt("is_active");
					}
					
					
					/*
					s.executeUpdate("insert into inventory_sales_promotions(label,valid_from,valid_to,is_active,created_on,created_by,estimated_sales_volume,sales_sku_price,free_sku_price,variable_cost_and_taxes,marginal_contribution, request_id) values('"+PriceListName+"','"+ ValidFrom +"','"+ValidTo+"',"+Active+",now(),"+UserID+","+estimated_sales_volume+","+sales_sku_price+","+free_sku_price+","+variable_cost_and_taxes+","+marginal_contribution+","+RequestID+")");
					
					ResultSet rs4 = s.executeQuery("select LAST_INSERT_ID()");
					if(rs4.first()){
						MasterTablePromotionID = rs4.getInt(1); 
					}
					
					s.executeUpdate("insert into inventory_sales_promotions_products(id,package_id,raw_cases,units,type_id,total_units) select '"+MasterTablePromotionID+"',package_id,raw_cases,units,type_id,total_units  from inventory_sales_promotions_request_products where id="+PromotionID);
					
					s.executeUpdate("insert into inventory_sales_promotions_products_brands(id,package_id,brand_id,type_id) select '"+MasterTablePromotionID+"',package_id,brand_id,type_id from inventory_sales_promotions_request_products_brands where id="+PromotionID);
					
					s.executeUpdate("insert into inventory_sales_promotions_regions(product_promotion_id,region_id) select '"+MasterTablePromotionID+"', region_id from inventory_sales_promotions_request_regions where product_promotion_id="+PromotionID);		               
					
					s.executeUpdate("insert into inventory_sales_promotions_distributors(product_promotion_id,distributor_id) select '"+MasterTablePromotionID+"',distributor_id from inventory_sales_promotions_request_distributors where product_promotion_id="+PromotionID );
					
					s.executeUpdate("insert into inventory_sales_promotions_pjp(product_promotion_id,pjp_id) select '"+MasterTablePromotionID+"',pjp_id from inventory_sales_promotions_request_pjp where product_promotion_id="+PromotionID);
					
					s.executeUpdate("insert into inventory_sales_promotions_outlet(product_promotion_id,outlet_id) select '"+MasterTablePromotionID+"',outlet_id from inventory_sales_promotions_request_outlet where product_promotion_id="+PromotionID );
					
					s.executeUpdate("insert into inventory_sales_promotions_employee(product_promotion_id,employee_id) select '"+MasterTablePromotionID+"',employee_id from inventory_sales_promotions_request_employee where product_promotion_id="+PromotionID );
					*/
					
					
					
					
					if (isCOO != null){
						
						s.executeUpdate("update workflow_requests_steps set user_id = 2577 where request_id = "+RequestID+" and step_id = 4");
					}else{
						s.executeUpdate("update workflow_requests_steps set user_id = 9000123 where request_id = "+RequestID+" and step_id = 4");

					}
					
					ds.commit();
					
					out.println("Approved");
					
					Workflow wf = new Workflow();
					wf.doStepAction(RequestID, 4, true, 0, 0, "");
					wf.close();
					
					
					
					String HTMLEmail = WorkflowEmail.getPromotionRequestHTMLWithoutActionButtons(RequestID);
					
					String PromotionName="";
					ResultSet rs1 = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestID);
					while(rs1.next()){
						PromotionName = rs1.getString("label");
					}
					
					
					
					
					// Get SD Head
					String SDEmail = "";
					ResultSet rs2 = s.executeQuery("select wrs.user_id, (select email from users where id = wrs.user_id) email from workflow_requests_steps wrs where wrs.request_id = "+RequestID+" and wrs.step_id = 3");
					if (rs2.first()){
						SDEmail = rs2.getString(2);
					}
					
					
					
					
					
					
					
					Utilities.sendPBCHTMLEmail(new String[]{"hammad.ulhaq@pbc.com.pk","bilal.ahmed@pbc.com.pk", "fsdord@pbc.com.pk","mursil.mehmood@pbc.com.pk"}, new String[]{"nadeem@pbc.com.pk",SDEmail,"shahrukh.salman@pbc.com.pk","nawaz@pbc.com.pk","jazeb@pbc.com.pk","salman.baig@pbc.com.pk","omerfk@pbc.com.pk","asim.maan@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "[Approval Notification] Promotion | "+PromotionName+" | ID#"+RequestID, HTMLEmail, null);
					
				}else if(request.getParameter("action").equals("2")){ //decline
					
					
					
					
					
					
					long DeclinedBy = 2577;
					if (isCOO != null){
						DeclinedBy = 2577;
					}else{
						DeclinedBy = 9000123;
					}
					
					
					Workflow wf = new Workflow();
					wf.doDecline(RequestID, 4, DeclinedBy,"");
					wf.close();
				    out.println("Declined");
				    
				    
				    
				    String HTMLEmail = WorkflowEmail.getPromotionRequestHTMLWithoutActionButtons(RequestID);
					
					String PromotionName="";
					ResultSet rs1 = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestID);
					while(rs1.next()){
						PromotionName = rs1.getString("label");
					}
				    
				    Utilities.sendPBCHTMLEmail(new String[]{"obaid@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "[Decline Notification] Promotion | "+PromotionName+" | ID#"+RequestID, HTMLEmail, null);
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