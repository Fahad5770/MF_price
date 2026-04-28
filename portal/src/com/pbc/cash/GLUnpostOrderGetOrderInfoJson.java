package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Util;
import com.pbc.sap.SAPUtilities;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

@WebServlet(description = "GL Unpost Order Get Order Info Json", urlPatterns = { "/sap/GLUnpostOrderGetOrderInfoJson" })

public class GLUnpostOrderGetOrderInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GLUnpostOrderGetOrderInfoJson() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("contructor() ...");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//System.out.println("service() ...");
		
		HttpSession session = request.getSession();
		
		
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		response.setContentType("application/json");
		JSONObject JsonObj = new JSONObject();
		PrintWriter out = response.getWriter();
		
		boolean JsonSuccess = false;
		String JsonErrorMessage = "";
		
		try{
		
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			long SaleOrderNo = Utilities.parseLong(request.getParameter("SaleOrderNo"));
			
			
			ResultSet rs = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=gop.customer_id) customer_name FROM gl_order_posting gop where gop.order_no = "+SaleOrderNo);
			if( rs.first() ){
				
				JsonSuccess = true;
				
				JsonObj.put("OrderDate", Utilities.getDisplayDateFormat(rs.getDate("order_date")));
				JsonObj.put("OrderAmount", rs.getString("order_amount"));
				
				JsonObj.put("CustomerID", rs.getString("customer_id"));
				JsonObj.put("CustomerName", rs.getString("customer_name"));
				JsonObj.put("CurrentBalance", rs.getString("current_balance"));
				JsonObj.put("CreditLimit", rs.getString("credit_limit"));
				
				boolean isLifted = false;
				ResultSet rs2 = s2.executeQuery("SELECT delivery_id FROM inventory_delivery_note where sap_order_no="+SaleOrderNo);
				if(rs2.first()){
					isLifted = true;
				}
				
				JsonObj.put("isLifted", isLifted);
				
			}else{
				JsonSuccess = false;
				JsonErrorMessage = "Order not posted yet";
			}
			
			s2.close();
			s.close();
			ds.dropConnection();
		
		}catch(Exception e){
			
			JsonSuccess = false;
			JsonErrorMessage = e.toString();
			e.printStackTrace();
		}
		
		JsonObj.put("success", JsonSuccess);
		JsonObj.put("error", JsonErrorMessage);
		
		out.print(JsonObj);
		out.close();
		
	}

}
