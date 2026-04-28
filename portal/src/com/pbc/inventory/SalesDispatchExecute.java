package com.pbc.inventory;

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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;
import org.omg.CORBA.portable.ValueFactory;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;



@WebServlet(description = "Executes Desk Sale", urlPatterns = { "/inventory/SalesDispatchExecute" })
public class SalesDispatchExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SalesDispatchExecute() {
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
		
		long InventryID = Utilities.parseLong(request.getParameter("InventryID"));

		PrintWriter out = response.getWriter();
		
		
		Datasource ds = new Datasource();
		JSONObject json = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM pep.inventory_sales_dispatch where id="+InventryID);
			if (rs.first()){
			
				String extendQuery="";
				if(rs.getInt("is_adjusted")==1) {
					extendQuery+="is_adjusted=0,adjusted_on=NULL,adjusted_by=NULL";
					if(rs.getInt("is_blocked")==1) {
						extendQuery+=",";
					}
				}
				if(rs.getInt("is_blocked")==1) {
					extendQuery+="is_blocked=0,blocked_on=NULL,blocked_by=NULL";
				}
				
					//System.out.println(rs.getString("extra_loaded_on"));
					String extra_loaded_on=null;
					String extra_loaded_by=null;
					String blocked_on=null;
					String blocked_by=null;
					String delivery_date=null;
					String created_on=null;
					String created_by=null;
					String adjusted_on=null;
					String adjusted_by=null;
					String empty_returned_on=null;
					String empty_returned_by=null;
					String liquid_returned_on=null;
					String liquid_returned_by=null;
					
					if(rs.getString("extra_loaded_on")!=null) {
						extra_loaded_on="'"+rs.getString("extra_loaded_on")+"'";
					}
					if(rs.getString("extra_loaded_by")!=null) {
						extra_loaded_by="'"+rs.getString("extra_loaded_by")+"'";
					}
					if(rs.getString("blocked_on")!=null) {
						blocked_on="'"+rs.getString("blocked_on")+"'";
					}
					if(rs.getString("blocked_by")!=null) {
						blocked_by="'"+rs.getString("blocked_by")+"'";
					}
					/*if(rs.getString("delivery_date")!=null) {
						delivery_date="'"+rs.getString("delivery_date")+"'";
					}*/
					if(rs.getString("created_on")!=null) {
						created_on="'"+rs.getString("created_on")+"'";
					}
					if(rs.getString("created_by")!=null) {
						created_by="'"+rs.getString("created_by")+"'";
					}
					if(rs.getString("adjusted_on")!=null) {
						adjusted_on="'"+rs.getString("adjusted_on")+"'";
					}
					if(rs.getString("adjusted_by")!=null) {
						adjusted_by="'"+rs.getString("adjusted_by")+"'";
					}
					if(rs.getString("empty_returned_on")!=null) {
						empty_returned_on="'"+rs.getString("empty_returned_on")+"'";
					}
					if(rs.getString("empty_returned_by")!=null) {
						empty_returned_by="'"+rs.getString("empty_returned_by")+"'";
					}
					if(rs.getString("liquid_returned_on")!=null) {
						liquid_returned_on="'"+rs.getString("liquid_returned_on")+"'";
					}
					if(rs.getString("liquid_returned_by")!=null) {
						liquid_returned_by="'"+rs.getString("liquid_returned_by")+"'";
					}
					
				
				s2.executeUpdate("INSERT INTO pep.inventory_sales_dispatch_logs(dispatch_id,dispatch_created_on,dispatch_created_by,dispatch_type,vehicle_id,driver_id,distributor_id,uvid,is_adjusted,dispatch_adjusted_on,dispatch_adjusted_by,is_empty_returned,empty_returned_on,empty_returned_by,is_no_liquid_returned,is_no_empty_returned,is_liquid_returned,dispatch_liquid_returned_on,dispatch_liquid_returned_by,is_extra_loaded,dispatch_extra_loaded_on,dispatch_extra_loaded_by,is_spot_selling,is_blocked,dispatch_blocked_on,dispatch_blocked_by,log_created_on,log_created_by)VALUES("+rs.getInt("id")+","+created_on+","+created_by+","+rs.getInt("dispatch_type")+","+rs.getInt("vehicle_id")+","+rs.getInt("driver_id")+","+rs.getInt("distributor_id")+","+rs.getLong("uvid")+","+rs.getInt("is_adjusted")+","+adjusted_on+","+adjusted_by+","+rs.getInt("is_empty_returned")+","+empty_returned_on+","+empty_returned_by+","+rs.getInt("is_no_liquid_returned")+","+rs.getInt("is_no_empty_returned")+","+rs.getInt("is_liquid_returned")+","+liquid_returned_on+","+liquid_returned_by+","+rs.getInt("is_extra_loaded")+","+extra_loaded_on+","+extra_loaded_by+","+rs.getInt("is_spot_selling")+","+rs.getInt("is_blocked")+","+blocked_on+","+blocked_by+",now(),'"+UserID+"')");
				
				s2.executeUpdate("UPDATE pep.inventory_sales_dispatch set "+extendQuery+" where id="+InventryID);
				json.put("success", "true");
				
				
				}
			json.put("success", "true");
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			json.put("success", "false");
			//obj.put("error", e.toString());
			json.put("error", "Server could not be reached");
			e.printStackTrace();
		}
		
		
		out.print(json);
		out.close();
		
	}
	
}