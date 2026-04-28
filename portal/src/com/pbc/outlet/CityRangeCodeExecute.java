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
 * Servlet implementation class CityRangeCodeExecute
 */
@WebServlet("/outlet/CityRangeCodeExecute")
public class CityRangeCodeExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityRangeCodeExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	doGet(request, response);
		 response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			String operation = request.getParameter("operation");
			System.out.println(operation);
			PrintWriter out = response.getWriter();
			Datasource ds = new Datasource();

			try {
				ds.createConnection();
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
		
		if(operation.equals("add")) 
		{
			//System.out.println("hi");
			int city = Utilities.parseInt(request.getParameter("city"));
			long start_code = Utilities.parseLong(request.getParameter("start_code"));
			long end_code = Utilities.parseLong(request.getParameter("end_code"));
			System.out.println(request.getParameter("city")+" "+start_code+" "+end_code);
			ResultSet rs = s.executeQuery("SELECT city_id FROM common_outlet_code_range");
			String query ="";
			while(rs.next()) { 
				
				if(rs.getInt("city_id") == city)
				{ 
					s3.executeUpdate("delete from common_outlet_code_range where city_id = "+city);
				}
			}
			System.out.println("INSERT INTO common_outlet_code_range(city_id,start_code,end_code)values('"+city+"','"+start_code+"','"+end_code+"')");
			s3.executeUpdate("INSERT INTO common_outlet_code_range(city_id,start_code,end_code)values('"+city+"','"+start_code+"','"+end_code+"')");
			//System.out.println(flag);
			
			
				//s2.executeUpdate("INSERT INTO common_cities(label)values('"+city+"')");
				obj.put("success", "true");
		}else {
			System.out.println("hi");
			int id = Utilities.parseInt(request.getParameter("id"));
			s3.executeUpdate("delete FROM common_outlet_code_range where id="+id);
			obj.put("success", "true");
		}
			s2.close();  
			ds.dropConnection();
	}catch (Exception e) {
		e.printStackTrace();
		obj.put("success", "false");
	}	
			out.print(obj);
			out.close();

}
	}
  

