package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

//import sun.security.ec.ECDSASignature.Raw;

@WebServlet(description = "Update Data", urlPatterns = { "/inventory/InventoryDeliveryOrderUpdateExecute" })
public class InventoryDeliveryOrderUpdateExecute extends HttpServlet {

	private static final long serialVersionUID = 1L;       

    public InventoryDeliveryOrderUpdateExecute() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	}
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		System.out.println("in Servlet");
		String UserID = null;
		PrintWriter out = response.getWriter();
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		try {
			 
 
			
			response.setContentType("application/json");
			ds.createConnection();
			ds.startTransaction();	
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s22 = ds.createStatement();
			JSONArray jr = new JSONArray();
			String SQLMain = "";
			int SapOrderNumber=Utilities.parseInt( request.getParameter("SapOrder"));
			//System.out.println(SapOrderNumber);
			long OrderID=Utilities.parseLong( request.getParameter("OrderID"));
			//System.out.println(OrderID);
			int Action= Utilities.parseInt(request.getParameter("ActionID"));
			
			String Decline= request.getParameter("DeclineReason");
			String Hold= request.getParameter("HoldReason");
			String HoldCheckBox=request.getParameter("HoldReasonCheckBox");
			System.out.println(Decline+" Declined");
			if(Action==2) {
				
				SQLMain = "Update inventory_delivery_order set status_type_id="+Action+",status_on= now(),sap_order_no="+SapOrderNumber+" where id="+OrderID+"";
			ResultSet rsCon=s.executeQuery("select * from inventory_delivery_order where sap_order_no="+SapOrderNumber+"");
			if(rsCon.first())
			{
				obj.put("Cmsg", "This sap order already exists !");
			}
			else {
				s.executeUpdate(SQLMain);
				obj.put("Cmsg", "Order has been Confirmed");
			}
			}else if(Action==4) {
				SQLMain = "Update inventory_delivery_order set status_type_id="+Action+",status_on= now(),decline_reason='"+Decline+"' where id="+OrderID+"";
				s.executeUpdate(SQLMain);
				obj.put("Dmsg","Order has been  Declined");
			}else if(Action==5) {
			SQLMain = "Update inventory_delivery_order set status_type_id="+Action+",status_on= now(),decline_reason='"+Hold+"',hold_reason='"+HoldCheckBox+"' where id="+OrderID+"";
			s.executeUpdate(SQLMain);
			obj.put("Dmsg","Order has been Updated");
			}
//			System.out.println(SQLMain);
			 
			obj.put("exists", "true");
			
			ds.commit();
	
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				 
				e1.printStackTrace();
			}
			 
			obj.put("exists", "false");
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
