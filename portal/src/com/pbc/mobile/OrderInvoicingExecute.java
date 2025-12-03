package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Order Invoicing Execute", urlPatterns = { "/mobile/OrderInvoicingExecute" })
public class OrderInvoicingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderInvoicingExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		PrintWriter out = response.getWriter();
		
		long OrderID[] = Utilities.parseLong(request.getParameterValues("OrderID"));
		int StatusID = Utilities.parseInt(request.getParameter("StatusID"));
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
				
		
		JSONObject json = new JSONObject();
		
			if(StatusID == 2){
				
				for (int i = 0; i < OrderID.length; i++){
				
					long UVID = Utilities.getUniqueVoucherID(Utilities.parseLong(UserID));
					
					Product.insertBackOrder(DistributorID, OrderID[i], Utilities.parseLong(UserID));
					
					SalesPosting.postOrder2Invoice(OrderID[i], Utilities.parseLong(UserID), UVID);
					
					try {
						Thread.currentThread().sleep(150);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}else{
				Datasource ds = new Datasource();
				try{
					ds.createConnection();
					Statement s = ds.createStatement();
					for (int i = 0; i < OrderID.length; i++){
						s.executeUpdate("update mobile_order set status_type_id=3, status_on=now() where id="+OrderID[i]);
					}
					
					s.close();
				}catch(Exception e){
					System.out.print(e);
				}finally{
					try {
						ds.dropConnection();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			json.put("success", "true");
			
			
		out.print(json);
		
		
	}


}
