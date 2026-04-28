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
@WebServlet(description = "R355 - PSR Targets VS Attainment SKU Wise [Tons]", urlPatterns = { "/reports/R355ExcelMain" })
public class R355ExcelMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public R355ExcelMain() {
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
		String whereDistributorIDs="";
		
		whereDistributorIDs = request.getParameter("DistributorIDs");
		
	
		
		
		//System.out.println("hellooo ");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		
		Datasource ds = new Datasource();
		try{
			
			
			
			ds.createConnection();
			Statement s = ds.createStatement();
			String startDate = Utilities.filterString(request.getParameter("StartDate"), 1, 100);
			System.out.println(startDate);
			Date StartDate=Utilities.parseDateYYYYMMDDWithBackSlash(startDate);
			System.out.println(StartDate);
			//if(!DistributorIDs.equals("")) {
				
				
				String filename_Sales = "PSR_Targets_VS_Attainment_SKU_Wise_Tons_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
			//	System.out.println("DistributorIDs "+DistributorIDs);
				//System.out.println("asdfasdf ");
				/*DO CHANGE THE FILE PATH FUNCTION BEFORE UPPING THE FILE */
				new com.pbc.reports.R355Excel().createPdf(Utilities.getCommonFilePath()+ "/" + filename_Sales,StartDate,whereDistributorIDs);
				
				
				
				
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
