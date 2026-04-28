package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * Servlet implementation class GetOutletCnicJson
 */
@WebServlet("/outlet/GetOutletCnicJson")
public class GetOutletCnicJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOutletCnicJson() {
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
		
		String CnicNum = Utilities.filterString(request.getParameter("CNICNo"), 1, 100);
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		String EnteredCnic = Utilities.filterString(request.getParameter("EnteredCnic"), 1, 100);
		
		PrintWriter out = response.getWriter();
try {
			//System.out.println("I am called CINIC...");
			ds.createConnection();
			
			Statement s = ds.createStatement();
			/*System.out.println("SELECT * FROM common_outlets_contacts where contact_nic = "+EnteredCnic);*/
		ResultSet rs2 = s.executeQuery("SELECT * FROM common_outlets_contacts where contact_nic = "+EnteredCnic);
		if(rs2.first()){
			obj.put("success", "true");
	}else {
		obj.put("success", "false");
	}
		out.print(obj);
		out.close();
}catch(Exception e) {
	e.printStackTrace();
}

	}
}
