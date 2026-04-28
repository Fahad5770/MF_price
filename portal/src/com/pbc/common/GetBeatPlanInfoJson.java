package com.pbc.common;

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

import com.pbc.inventory.Product;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class GetProductInfo
 */
@WebServlet(description = "Get Beat Plan Info Json", urlPatterns = { "/common/GetBeatPlanInfoJson" })
public class GetBeatPlanInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public GetBeatPlanInfoJson() {
        super();
        // TODO Auto-generated constructor stub
        
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		int PJPID = Utilities.parseInt(request.getParameter("PJPID"));
		
		Datasource ds = new Datasource();
		JSONObject obj=new JSONObject();
		
		try {
			
			response.setContentType("application/json");
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT label FROM distributor_beat_plan where id="+PJPID);
			if(rs.first()){
				obj.put("success", "true");
				obj.put("PJPName", rs.getString("label"));
			}
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}

		out.print(obj);
		out.close();
		
	}

}
