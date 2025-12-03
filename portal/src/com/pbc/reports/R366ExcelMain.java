package com.pbc.reports;

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
@WebServlet(description = "Gurr Chana Visibility DRIVE Shops List - National", urlPatterns = { "/reports/R366ExcelMain" })
public class R366ExcelMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public R366ExcelMain() {
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
		
		
		//System.out.println("hellooo ");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		
		Datasource ds = new Datasource();
		try{
			
			
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			
			//if(!DistributorIDs.equals("")) {
				String StartDate = Utilities.filterString(request.getParameter("StartDate"), 1, 100);
				String EndDate = Utilities.filterString(request.getParameter("EndDate"), 1, 100);
				String DistributorIDs = request.getParameter("DistributorIDs");
				String BrandIDs = request.getParameter("BrandIDs");
				String SKUIDs = request.getParameter("SKUIDs");
				String RegionIDs = request.getParameter("RegionIDs");
				
				
				String filename_Sales = "Sales_Detail_Visibility_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".csv";
				
				//System.out.println("asdfasdf ");
				/*DO CHANGE THE FILE PATH FUNCTION BEFORE UPPING THE FILE */
				new com.pbc.reports.R366Excel().createPdf(Utilities.getCommonFilePath()+ "/" + filename_Sales, 0,StartDate,EndDate,DistributorIDs,BrandIDs,SKUIDs,RegionIDs);
				
				
				
				
				json.put("success", "true");
				json.put("FileName", filename_Sales);
			/*}else {
				json.put("success", "false");
				json.put("error", "Please select at least one distributor");
			}*/
			
				
			
			
			s.close();
		}catch(Exception e){
			System.out.println(e);
			json.put("success", "false");
			json.put("error", e.toString());
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
		
			
			
		out.print(json);
		
		
	}


}
