package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class CityRangeCodeJson
 */
@WebServlet("/outlet/CityRangeCodeJson")
public class CityRangeCodeJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityRangeCodeJson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		int cityid = Utilities.parseInt(request.getParameter("cityid"));
		//System.out.println(city);
		
		
		
		Datasource ds = new Datasource();
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			ResultSet rs = s.executeQuery("SELECT * FROM common_outlet_code_range where city_id="+cityid);
			System.out.println("SELECT * FROM common_outlet_code_range where city_id="+cityid);
			if(rs.first()) { 
				obj.put("StartCode", rs.getString("start_code"));
				obj.put("EndCode", rs.getString("end_code"));
				obj.put("success", "true");
			}else {
				obj.put("success", "false");
			}

			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close(); 
			ds.dropConnection();
	}catch (Exception e) {
		e.printStackTrace();
	}	
	}

}
