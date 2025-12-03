package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

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

@WebServlet(description = "UserActivateChrome", urlPatterns = { "/employee/UserActivateChromeExecute" })
public class UserActivateChromeExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserActivateChromeExecute() {
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
		HttpSession session = request.getSession();
		long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

		try {
			if(Utilities.isAuthorized(150, SessionUserID) == false){
				response.sendRedirect("AccessDenied.jsp");
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//insert query to insert the record in user table
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		
		PrintWriter out = response.getWriter();
		
		//getting record against the sap code
		try {
			ds.createConnection();
			Statement s = ds.createStatement();			
			long ProcessID = Utilities.parseLong(request.getParameter("ProcessID"));
			long BrowserID = Utilities.parseLong(request.getParameter("BrowserID"));
			
			if(ProcessID==1){//Deactivate
			  s.executeUpdate("update user_access_chrome_activation set is_active=0,deactivated_on=now() where browser_id="+BrowserID);
			}
			else if(ProcessID==2){//Activate
				s.executeUpdate("update user_access_chrome_activation set is_active=1,activated_on=now() where browser_id="+BrowserID);
			}
			
			obj.put("success", "true");
			
			s.close();			
			ds.dropConnection();
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
	}

}
