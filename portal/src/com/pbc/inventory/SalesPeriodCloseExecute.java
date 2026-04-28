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



@WebServlet(description = "Executes Desk Sale", urlPatterns = { "/inventory/SalesPeriodCloseExecute" })
public class SalesPeriodCloseExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SalesPeriodCloseExecute() {
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
		
		
		
		
		long ActionCheckbox [] = Utilities.parseLong(request.getParameterValues("ActionCheckbox"));
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		
		
		try {
			
			ds.createConnection();
			ds.startTransaction();			
			
			
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			if(ActionCheckbox!=null) {
				for(int i=0;i<ActionCheckbox.length;i++) {
					s.executeUpdate("insert into inventory_sales_period_status_log(start_date,end_date,month_number,year_number,remarks,distributor_id,is_closed,status_changed_on,status_changed_by) select start_date,end_date,month_number,year_number,remarks,distributor_id,1,now(),"+UserID+" from  inventory_sales_period_status where id="+ActionCheckbox[i]);
					s.executeUpdate("delete from inventory_sales_period_status where id="+ActionCheckbox[i]);
				}			
				
				obj.put("success", "true");
			}else {
				obj.put("success", "false");
				obj.put("error", "Please select some distributor to close the period");
			}
			
			
			
			ds.commit();
			s1.close();
			s.close();
			ds.dropConnection();
			
			
			//s.executeUpdate("insert into inventory_sales_period_status_log(start_date,end_date,month_number,year_number,closed_on,closed_by,remarks,status_changed_on,status_changed_by) select start_date,end_date,month_number,year_number,closed_on,closed_by,remarks,now(),"+UserID+" from inventory_sales_period_status where id="+PeriodID);
			
			
			
			
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			obj.put("success", "false");
			obj.put("exception", e);
			e.printStackTrace();
			//out.print(e);
		}finally{
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("before end close");
		out.print(obj);
		out.close();
		//System.out.println("after end close");
		
	}
	
	private String getProductName(Statement s, int ProductID) throws SQLException{
		
		String ProductName = "";
		ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product_name FROM pep.inventory_products_view where product_id="+ProductID);
		if(rs.first()){
			ProductName = rs.getString("product_name");
		}
		
		return ProductName;
		
	}
	
}