package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

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

/**
 * Servlet implementation class UserRight */

@WebServlet(description = "Dispatch Sales Delete", urlPatterns = { "/inventory/DispatchSalesDeleteExecute" })
public class DispatchSalesDeleteExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DispatchSalesDeleteExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}		
		
		try {
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			long[] InventorySalesID = Utilities.parseLong(request.getParameterValues("InventorySalesID"));
			int[] DispatchSalesSelect = Utilities.parseInt(request.getParameterValues("DispatchSalesSelect"));
			if(DispatchSalesSelect != null)
			{
				for(int x=0;x<DispatchSalesSelect.length;x++)
				{
					if(DispatchSalesSelect[x] == 1) //if selected
					{
						//System.out.println("Selected Dispatch "+InventorySalesID[x]);
						ResultSet rs = s.executeQuery("select order_id from inventory_sales_invoices where id="+InventorySalesID[x]);
						if(rs.first()){
							//System.out.println(rs.getLong("order_id"));
							s1.executeUpdate("update mobile_order set status_type_id=3,status_on=now() where id="+rs.getLong("order_id")); //updating mobile_order
							s1.executeUpdate("delete from inventory_sales_invoices where id="+InventorySalesID[x]);
						}
					}
				}
			}
			ds.commit();
			obj.put("success", "true");
			s.close();
			s1.close();
			ds.dropConnection();
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
	}

}
