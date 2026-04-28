package com.pbc.employee;

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

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "OrderBooker Target Execute", urlPatterns = { "/employee/OrderBookerTargetExecute" })
public class OrderBookerTargetExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public OrderBookerTargetExecute() {
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
		
		boolean isEditCase = false;
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		
		if(EditID > 0){
			isEditCase = true;
		}
		
		long EmployeeID =Utilities.parseLong(request.getParameter("EmployeeID"));
		int Month = Utilities.parseInt(request.getParameter("Month"));
		int Year = Utilities.parseInt(request.getParameter("Year"));
		
		int PackageID[] = Utilities.parseInt(request.getParameterValues("package_id"));
		int Quantity[] = Utilities.parseInt(request.getParameterValues("qty"));
		
		int DistributorTargetID = 0;
		boolean isAlreadyExist = false;
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			ResultSet rs_check = s.executeQuery("select employee_id from employee_targets where employee_id="+EmployeeID+" and month="+Month+" and year="+Year);
			if(rs_check.first()){
				isAlreadyExist = true;
			}
			
			if(!isAlreadyExist){
			
				String SQL = "";
				
				if(isEditCase){
					SQL = "UPDATE employee_targets SET `employee_id` = "+EmployeeID+",`month` = "+Month+",`year` = "+Year+", `created_by` = "+UserID+",`created_on` = now() WHERE `id` = "+EditID;
				}else{
					SQL = "INSERT INTO `employee_targets`(`employee_id`,`month`,`year`,`created_by`,`created_on`)VALUES("+EmployeeID+","+Month+","+Year+","+UserID+",now())";
				}
				
				s.executeUpdate(SQL);
				
				if(!isEditCase){
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						DistributorTargetID = rs.getInt(1);
					}
				}else{
					DistributorTargetID = EditID;
				}
				
				s.executeUpdate("delete from employee_targets_packages where id="+DistributorTargetID);
				
				
				s.executeUpdate("delete from employee_targets_packages_brands where id="+DistributorTargetID);
				
				for(int i = 0; i < PackageID.length; i++){
					
					if(Quantity[i] > 0){
						
						s.executeUpdate("INSERT INTO `employee_targets_packages`(`id`,`package_id`,`quantity`)VALUES("+DistributorTargetID+","+PackageID[i]+","+Quantity[i]+")");
					
						int BrandID[] = Utilities.parseInt(request.getParameterValues("brand_id_"+PackageID[i]));
						int BrandQuantity[] = Utilities.parseInt(request.getParameterValues("qty_"+PackageID[i]));
						
						
						for(int j = 0; j < BrandID.length; j++ ){
							
							if(BrandQuantity[j] > 0){
								
								s.executeUpdate("INSERT INTO `employee_targets_packages_brands`(`id`,`package_id`,`brand_id`,`quantity`)VALUES("+DistributorTargetID+","+PackageID[i]+","+BrandID[j]+","+BrandQuantity[j]+")");
							}
						}
					}
				}
				
				obj.put("success", "true");
			
			}else{
				obj.put("success", "false");
				obj.put("error", "Duplicate Entry");
			}
			
			ds.commit();
			s.close();
			
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
				if (ds != null){
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
