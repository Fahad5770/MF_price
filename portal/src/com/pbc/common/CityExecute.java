package com.pbc.common;

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

/**
 * Servlet implementation class CityExecute
 */
@WebServlet("/common/CityExecute")
public class CityExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		 response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			String operation = request.getParameter("operation");
			PrintWriter out = response.getWriter();
			Datasource ds = new Datasource();
			try {
				ds.createConnection();
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				
				System.out.println(operation);
				
				//System.out.println(request.getParameter("cityid"));
				
				if(operation.equals("add")) {
					String city = request.getParameter("city").toLowerCase();
					ResultSet rs = s.executeQuery("SELECT * FROM common_cities");
					boolean flag = false;
					while(rs.next()) { 
						String tempCity = rs.getString("label").toLowerCase();
						if(tempCity.equals(city))
						{
							flag = true;
						}
					}
					if(flag == false) {
						s2.executeUpdate("INSERT INTO common_cities(label)values('"+city+"')");
						obj.put("success", "true");
						int generatedKey = 0;
						ResultSet rs2 = s2.executeQuery("select id from common_cities where label='"+city+"'");
						if (rs2.first()) {
						    generatedKey = rs2.getInt("id");
						}
						obj.put("newId", generatedKey);
					}else {
						obj.put("success", "false");
					}
				}else {
					//System.out.println("hi");
					String id = request.getParameter("cityid");
					try {
						s3.executeUpdate("delete FROM common_cities where id="+id);
						obj.put("success", "true");
					}catch(Exception e) {
						e.printStackTrace();
						obj.put("success", "false");
					}
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
