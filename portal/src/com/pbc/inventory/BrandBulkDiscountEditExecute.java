package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Spot Discount ", urlPatterns = { "/inventory/BrandBulkDiscountEditExecute" })
public class BrandBulkDiscountEditExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BrandBulkDiscountEditExecute() {
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
		
		
		//Master table
		int Activeness = Utilities.parseInt(request.getParameter("Activeness"));
	//	System.out.println("Activeness"+Activeness);
		
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
	//	System.out.println("EditID"+EditID);
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
				

			
			String queryCompletion = (Activeness == 1) ? ", activated_on=now(), activated_by="+UserID : ", deactivated_on=now(), deactivated_by="+UserID;
			
		//	System.out.println("update inventory_hand_to_hand_discount_brand set is_active="+Activeness+queryCompletion+" where id="+EditID);				
			s.executeUpdate("update inventory_hand_to_hand_discount_brand set is_active=" + Activeness + queryCompletion
					+ " where id=" + EditID);
			
					
				obj.put("success", "true");
				ds.commit();
			
				s2.close();	
			s.close();	
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
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
