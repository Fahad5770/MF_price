package com.pbc.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;


@WebServlet(description = "Executed password change", urlPatterns = { "/util/ChangePasswordExecute" })

public class ChangePasswordExecute extends HttpServlet {
	private static final long serialVersionUID = 291L;
       
    public ChangePasswordExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		String Password = Utilities.filterString(request.getParameter("CurrentPassword"), 1, 20);
		String NewPassword = Utilities.filterString(request.getParameter("ConfirmPassword"), 1, 20);
		
		
		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		Datasource ds = new Datasource();
		response.setContentType("application/json");		
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select password, md5('"+Password+"') from users where id = '"+UserID+"'");
			if (rs.first()){
				if (rs.getString(1).equals(rs.getString(2))){
					s2.executeUpdate("update users set password = md5('"+NewPassword+"') where id = '"+UserID+"'");
					obj.put("success", "true");
					obj.put("invalid", "false");
				}else{
					obj.put("success", "false");
					obj.put("invalid", "true");
				}
			}
			
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();
		
	}
	
}