package com.pbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

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



@WebServlet(description = "Activate Chrome", urlPatterns = { "/ActivateChrome" })
public class ActivateChrome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivateChrome() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		
		try {
			int UserID = Utilities.parseInt(request.getParameter("UserID"));
			long BrowserID = Utilities.parseLong(request.getParameter("BrowserID"));
			double lat = Utilities.parseDouble(request.getParameter("lat"));
			double lng = Utilities.parseDouble(request.getParameter("lng"));
			String BrowserAgent = Utilities.filterString(request.getParameter("BrowserAgent"), 1, 200);
			String os = Utilities.filterString(request.getParameter("os"), 1, 100);
			String OSArchitecture = Utilities.filterString(request.getParameter("OSArchitecture"), 1, 100);
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			s.executeUpdate("insert into user_access_chrome_activation (created_on, user_id, browser_id, lat, lng, browser_agent, os, os_architecture) values(now(), "+UserID+","+BrowserID+","+lat+","+lng+",'"+BrowserAgent+"','"+os+"','"+OSArchitecture+"')");
			
			obj.put("success", "true");
			s.close();
			s1.close();
			ds.dropConnection();
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			obj.put("success", "false");
			obj.put("error", "106");
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
