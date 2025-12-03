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

@WebServlet(description = "Distributors", urlPatterns = { "/employee/DistributorGroupsExecute" })
public class DistributorGroupsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DistributorGroupsExecute() {
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
		
		//insert query to insert the record in user table
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		//getting record against the sap code
		try {
			ds.createConnection();
			Statement s = ds.createStatement();			
			
					
					String [] SelectedDistributors = request.getParameterValues("DistributorIdsName");
					long GroupID = Utilities.parseLong(request.getParameter("GroupIDName"));
					if(request.getParameter("isDeleteCase").equals("0")) //insertion case
					{
						if(SelectedDistributors != null)
						{
							for(int i =0;i<SelectedDistributors.length;i++)
							{
								//insert query to features table							
								s.executeUpdate("replace into common_distributor_groups_list(id,distributor_id) values("+GroupID+","+SelectedDistributors[i]+")");
								
								//System.out.println("Hello "+SelectedFeatures[i]);
							}
						}
					
					}
					else if(request.getParameter("isDeleteCase").equals("1")) //Delete case
					{
						s.executeUpdate("delete from common_distributor_groups_list where id="+Utilities.parseLong(request.getParameter("GrpID"))+" and distributor_id="+Utilities.parseLong(request.getParameter("DistID")));
						obj.put("deletesuccess", "true");
					}
					else if(request.getParameter("isDeleteCase").equals("2")) //Group Insertion case in common distributor group table
					{
						String UserID = null;
						if (session.getAttribute("UserID") != null){
							UserID = (String)session.getAttribute("UserID");
						}
						
						if (UserID == null){
							response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
						}
						String GroupName = request.getParameter("AddDistributorNameTxtBx");
						s.executeUpdate("insert into " +
				                "common_distributor_groups(label,created_on,created_by)" +
				                "values('"+GroupName+"',now(),"+UserID+")");
						obj.put("insertionsuccess", "true");
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
