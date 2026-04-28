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



@WebServlet(description = "Physical Stock Adjustment Validate", urlPatterns = { "/inventory/PhysicalStockAdjustmentValidate" })
public class PhysicalStockAdjustmentValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PhysicalStockAdjustmentValidate() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		int DocumentTypeID = 18;
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();		
		
		long Distributor_ID = Utilities.parseLong(request.getParameter("DistID"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			
			ResultSet rs1 = s1.executeQuery("SELECT id,invs.distributor_id,IFNULL((select concat(first_name,' ',last_name) as booked_name1 from users u where u.id=invs.booked_by),'Desk Sale') as booked_named,outlet_id,(select outlet_name from outletmaster om where om.outlet_id=invs.outlet_id) outlet_name,(select bsi_name from outletmaster om where om.outlet_id=invs.outlet_id) bsi_name,invs.created_by,(select concat(u.first_name,' ',u.last_name) from users u where invs.created_by=u.id) created_by_name,invs.total_amount,invs.discount,invs.net_amount,invs.created_on FROM inventory_sales_invoices invs where invs.distributor_id="+Distributor_ID+" and invs.is_dispatched=0 order by invs.booked_by,invs.created_on desc");
			//System.out.println("SELECT id,invs.distributor_id,IFNULL((select concat(first_name,' ',last_name) as booked_name1 from users u where u.id=invs.booked_by),'Desk Sale') as booked_named,outlet_id,(select outlet_name from outletmaster om where om.outlet_id=invs.outlet_id) outlet_name,(select bsi_name from outletmaster om where om.outlet_id=invs.outlet_id) bsi_name,invs.created_by,(select concat(u.first_name,' ',u.last_name) from users u where invs.created_by=u.id) created_by_name,invs.total_amount,invs.discount,invs.net_amount,invs.created_on FROM inventory_sales_invoices invs where invs.distributor_id="+Distributor_ID+" and invs.is_dispatched=0 order by invs.booked_by,invs.created_on desc");
			if( rs1.first() ){
				
				obj.put("success", "false");
				obj.put("error", "Please dispatch pending invoices before posting physical stock.");
				
			}else{
				obj.put("success", "true");
			}
			
			s1.close();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			
			obj.put("success", "false");
			obj.put("exception", e);
			e.printStackTrace();
			//out.print(e);
		}
		
		//System.out.println("before end close");
		out.print(obj);
		out.close();
		//System.out.println("after end close");
		
	}
	
}