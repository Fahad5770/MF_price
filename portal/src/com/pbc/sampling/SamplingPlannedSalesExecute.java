package com.pbc.sampling;

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


@WebServlet(description = "Sampling Planned Sales Execute", urlPatterns = { "/sampling/SamplingPlannedSalesExecute" })
public class SamplingPlannedSalesExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingPlannedSalesExecute() {
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
		
		long OutletID =Utilities.parseLong(request.getParameter("OutletID"));
		
		Date StartDate=Utilities.parseDate(request.getParameter("StartDate"));
		Date EndDate=Utilities.parseDate(request.getParameter("EndDate"));
		long RequestID =Utilities.parseLong(request.getParameter("RequestID"));
		
		int PackageID[] = Utilities.parseInt(request.getParameterValues("package_id"));
		int Quantity[] = Utilities.parseInt(request.getParameterValues("qty"));
		
		int PackageID2[] = Utilities.parseInt(request.getParameterValues("package_id_t2"));
		int Quantity2[] = Utilities.parseInt(request.getParameterValues("qty_t2"));
		
		int RecordID = 0;
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			String SQL = "";
			
			if(isEditCase){
				//SQL = "UPDATE sampling_planned_sales SET `outlet_id` = "+OutletID+",`start_date` = "+Utilities.getSQLDate(StartDate)+",`end_date` = "+Utilities.getSQLDate(EndDate)+",`created_by` = "+UserID+",`created_on` = now() WHERE `id` = "+EditID;
			}else{
				SQL = "INSERT INTO `sampling_planned_sales`(`outlet_id`,`start_date`,`end_date`,`created_by`,`created_on`, `request_id`)VALUES("+OutletID+","+ Utilities.getSQLDate(StartDate) +","+ Utilities.getSQLDate(EndDate) +","+UserID+",now(), "+RequestID+")";
			}
			
			s.executeUpdate(SQL);
			
			if(!isEditCase){
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					RecordID = rs.getInt(1);
				}
			}else{
				RecordID = EditID;
			}
			
			//s.executeUpdate("delete from sampling_planned_sales_packages where id="+RecordID);
			
			
			//s.executeUpdate("delete from sampling_planned_sales_packages_brands where id="+RecordID);
			
			for(int i = 0; i < PackageID.length; i++){
				
				if(Quantity[i] > 0){
					
					s.executeUpdate("INSERT INTO `sampling_planned_sales_packages`(`id`,`package_id`,`quantity`, `type_id`)VALUES("+RecordID+","+PackageID[i]+","+Quantity[i]+", 1)");
				
					int BrandID[] = Utilities.parseInt(request.getParameterValues("brand_id_"+PackageID[i]));
					int BrandQuantity[] = Utilities.parseInt(request.getParameterValues("qty_"+PackageID[i]));
					
					
					for(int j = 0; j < BrandID.length; j++ ){
						
						if(BrandQuantity[j] > 0){
							
							s.executeUpdate("INSERT INTO `sampling_planned_sales_packages_brands`(`id`,`package_id`,`brand_id`,`quantity`, `type_id`)VALUES("+RecordID+","+PackageID[i]+","+BrandID[j]+","+BrandQuantity[j]+", 1)");
						}
					}
				}
			}
			
			for(int i = 0; i < PackageID2.length; i++){
				
				if(Quantity2[i] > 0){
					
					s.executeUpdate("INSERT INTO `sampling_planned_sales_packages`(`id`,`package_id`,`quantity`, `type_id`)VALUES("+RecordID+","+PackageID2[i]+","+Quantity2[i]+", 2)");
				
					int BrandID2[] = Utilities.parseInt(request.getParameterValues("brand_id_"+PackageID2[i]+"_t2"));
					int BrandQuantity2[] = Utilities.parseInt(request.getParameterValues("qty_"+PackageID2[i]+"_t2"));
					
					
					for(int j = 0; j < BrandID2.length; j++ ){
						
						if(BrandQuantity2[j] > 0){
							
							s.executeUpdate("INSERT INTO `sampling_planned_sales_packages_brands`(`id`,`package_id`,`brand_id`,`quantity`, `type_id`)VALUES("+RecordID+","+PackageID2[i]+","+BrandID2[j]+","+BrandQuantity2[j]+", 2)");
						}
					}
				}
			}
			
			
			
			obj.put("success", "true");
			
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
