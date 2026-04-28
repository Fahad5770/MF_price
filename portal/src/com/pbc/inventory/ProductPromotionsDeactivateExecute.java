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

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price List ", urlPatterns = { "/inventory/ProductPromotionsDeactivateExecute" })
public class ProductPromotionsDeactivateExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductPromotionsDeactivateExecute() {
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
		
		
		
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			long ProductPromotionID = Utilities.parseLong(request.getParameter("ProductPromotionID"));
			long DeactivatedBy = Utilities.parseLong(request.getParameter("DeactivatedBy"));
			String DeactivateReason = Utilities.filterString(request.getParameter("Reason"), 1, 500);
			
			s.executeUpdate("update inventory_sales_promotions set is_active=0,deactivated_on=now(),deactivated_by="+DeactivatedBy+",deactivated_reason='"+DeactivateReason+"'  where id="+ProductPromotionID);	

			// Update promotions cache
			BiProcesses bip = new BiProcesses();
			bip.createPromotionsCache();
			bip.close();
							
			obj.put("success", "true");	
			
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");	
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
