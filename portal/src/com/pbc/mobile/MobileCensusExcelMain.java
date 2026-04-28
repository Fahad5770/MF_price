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
@WebServlet(description = "Mobile Devices Execute", urlPatterns = { "/mobile/MobileCensusExcelMain" })
public class MobileCensusExcelMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public MobileCensusExcelMain() {
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
		
		JSONObject json = new JSONObject();
		
		Datasource ds = new Datasource();
		try{
			ds.createConnection();
			Statement s = ds.createStatement();
			
			long CensusID = Utilities.parseLong(request.getParameter("CenID"));
			
			System.out.println("Hello "+CensusID);
			
			String filename_Mobile_Census_ = "Mobile_Census_"+CensusID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
			
			
			
			new com.pbc.mobile.MobileCensusExcel().createPdf(Utilities.getOrderImagesPath()+ "/" + filename_Mobile_Census_, 0,CensusID);
			
			
			json.put("success", "true");
			json.put("FileName", filename_Mobile_Census_);
			
			s.close();
		}catch(Exception e){
			System.out.print(e);
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
