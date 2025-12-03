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

@WebServlet(description = "User Right Admin Execute", urlPatterns = { "/employee/UserRightAdminExecute" })
public class UserRightAdminExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserRightAdminExecute() {
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
			if(Utilities.isAuthorized(110, SessionUserID) == false){
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
		
		long UserID = Utilities.parseLong(request.getParameter("SapCode"));
		String Password = request.getParameter("password");
		String FirstName = request.getParameter("FirstName");
		String LastName = request.getParameter("LastName");
		String DisplayName = request.getParameter("DisplayName");
		String Designation = request.getParameter("Disignation");
		String Department = request.getParameter("Department");
		String Email = request.getParameter("Email");				
		int TypeID = Integer.parseInt(request.getParameter("SaleTypeID"));
		int DefaultGroupID = Utilities.parseInt(request.getParameter("UserRightDistributorGrp"));
		int IsActive = Utilities.parseInt(request.getParameter("UserRightUserActive"));
		
		String DistributorID =null;
		String query = "";
		String query1 = "";
		String query2 = "";
		boolean isSelection = false;
		boolean isInsertion = false;
		boolean isUpdation = false;
		
		boolean isSelectionForInsertion = false;
		int isEdit = Integer.parseInt(request.getParameter("isEdit"));
		int isEditForSelection = Integer.parseInt(request.getParameter("isEditForSelection"));
		
		if(isEdit == 0)
		{ isSelection = true; }
		if(isEdit == 1)
		{ isInsertion = true; }
		if(isEdit == 2)
		{ isUpdation = true; }
		
		if(isEditForSelection == 1)
		{ isSelectionForInsertion = true; }
		
		
		System.out.println("isEdit "+isEdit);
		
		System.out.println("isSelection "+isSelection);
		
		System.out.println("isInsertion "+isInsertion);

		System.out.println("isUpdation "+isUpdation);
		
		//getting record against the sap code
		try {
			ds.createConnection();
			Statement s = ds.createStatement();			
			
			if(isSelectionForInsertion)
			{
				//selection insertion case
				ResultSet rs = s.executeQuery("select * from employee_view where sap_code=" +UserID);
				if(rs.first())//if it has some record
				{
					obj.put("SapCode", rs.getString("sap_code"));
					
					obj.put("FirstName", rs.getString("first_name"));
					obj.put("LastName", rs.getString("last_name"));
					obj.put("DisplayName", rs.getString("first_name")+" "+rs.getString("last_name"));
					obj.put("Designation", rs.getString("designation_label"));
					obj.put("Department", rs.getString("department_label"));
					
					
					
					obj.put("isExist1","true");
					obj.put("success", "true");
					//System.out.println("Hello me in selection insertion case "+UserID);
				}	
				
				else
				{ 
					obj.put("isExist1","false");
				}
				//System.out.println("select * from employee_view where  sap_code=" +UserID);
				
				
			}
			
			if(isSelection)//0 for Selection 1 for insertion 2 for updation)
			{ 
				//System.out.println("I am in selection case "+UserID);
				
					ResultSet rs = s.executeQuery("select id,first_name,last_name,display_name,designation,department,email,type_id,distributor_id,md5(password) as PASSWORD,(select name from common_distributors where distributor_id = u.distributor_id) AS Distributor_Name,default_distributor_group,is_active FROM users u where id="+UserID);
					if(rs.first())//if it has some record
					{
						obj.put("SapCode", rs.getString("ID"));
						//obj.put("Password",rs.getString("PASSWORD"));
						obj.put("FirstName", rs.getString("FIRST_NAME"));
						obj.put("LastName", rs.getString("LAST_NAME"));
						obj.put("DisplayName", rs.getString("DISPLAY_NAME"));
						obj.put("Designation", rs.getString("DESIGNATION"));
						obj.put("Department", rs.getString("DEPARTMENT"));
						obj.put("Email", rs.getString("EMAIL"));
						obj.put("TypeID", rs.getString("type_id"));
						obj.put("DefaultGroupID", rs.getString("default_distributor_group"));
						
						
						obj.put("DistributorID", rs.getString("distributor_id"));
						obj.put("DistributorName", rs.getString("Distributor_Name"));
						obj.put("UserActive", rs.getInt("is_active"));
						obj.put("isExist","true");
						
					}	
					
					else
					{ 
						obj.put("isExist","false");
					}
					
					//getting features
					
					//ds.createConnection();
					
					ResultSet rs1 = s.executeQuery("select * from user_access_admin where user_id="+UserID);
					JSONArray jr = new JSONArray();
					while(rs1.next())
					{
						LinkedHashMap rows = new LinkedHashMap();
						
						rows.put("FeatureId", rs1.getString("feature_id"));	
						jr.add(rows);
						
					}
					
					
					
					obj.put("rows", jr);
					//obj.put("rows1", jr1);
					//obj.put("rows2", jr2);
					//obj.put("rows3", jr3);
					obj.put("success", "true");
			}
			else if(isInsertion)//1 for insertion 2 for edit
			{
				//System.out.println("Insertion Case ");
				
				
					
					if(TypeID == 2) //for secondary sale
					{
						DistributorID = Utilities.filterString(request.getParameter("DistributorID"),1,20);
						query = "insert into " +
				                "`users`(`id`,`password`,`first_name`,`last_name`,`display_name`,`designation`,`department`,`email`,`is_active`,`type_id`,`distributor_id`,default_distributor_group)" +
				                "values("+UserID+",MD5('"+Password+"'),'"+FirstName+"','"+LastName+"','"+DisplayName+"','"+Designation+"','"+Department+"','"+Email+"',"+IsActive+","+TypeID+","+DistributorID+","+DefaultGroupID+")";
					}
					else //no distrubutor
					{
						query = "insert into " +
				                "`users`(`id`,`password`,`first_name`,`last_name`,`display_name`,`designation`,`department`,`email`,`is_active`,`type_id`,default_distributor_group)" +
				                "values("+UserID+",MD5('"+Password+"'),'"+FirstName+"','"+LastName+"','"+DisplayName+"','"+Designation+"','"+Department+"','"+Email+"',"+IsActive+","+TypeID+","+DefaultGroupID+")";
					}
					
					//System.out.println();
					s.executeUpdate(query);
					
					String [] SelectedFeatures = request.getParameterValues("FeatureCheckbox");
					//System.out.println("Hello "+SelectedFeatures);
					if(SelectedFeatures != null)
					{
						for(int i =0;i<SelectedFeatures.length;i++)
						{
							//insert query to features table
							query1 = "insert into " +
					                "`user_access_admin`(`user_id`,`feature_id`,`created_on`,`created_by`)" +
					                "values("+UserID+","+SelectedFeatures[i]+",now(),"+Utilities.parseInt(session.getAttribute("UserID").toString())+")";
							s.executeUpdate(query1);
							
							//System.out.println("Hello "+SelectedFeatures[i]);
						}
					}
					
					
					
					
					obj.put("success", "true");
					
			}
			else if(isUpdation)  //for edit case
			{
						
				//System.out.println("Active Status "+IsActive);
					if(TypeID == 1) //primary
					{
						DistributorID = null;
					}
					else
					{ 
						DistributorID = DistributorID = Utilities.filterString(request.getParameter("DistributorID"),1,20);
					}
					if(Password.equalsIgnoreCase(""))//if password empty then no need to change the pass
					{
						query = "update `users`" +
								"set `first_name`='"+FirstName+"',`last_name`='"+LastName+"',`display_name`='"+DisplayName+"',`designation`='"+Designation+"',`department`='"+Department+"',`email`='"+Email+"',`type_id`="+TypeID+",`distributor_id`="+DistributorID+",default_distributor_group="+DefaultGroupID+",is_active="+IsActive+" where ID = "+UserID;	
					}
					else
					{
						query = "update `users`" +
								"set `password`=MD5('"+Password+"'),`first_name`='"+FirstName+"',`last_name`='"+LastName+"',`display_name`='"+DisplayName+"',`designation`='"+Designation+"',`department`='"+Department+"',`email`='"+Email+"',`type_id`="+TypeID+",`distributor_id`="+DistributorID+",default_distributor_group="+DefaultGroupID+",is_active="+IsActive+" where ID = "+UserID;
					}
						
						
						s.executeUpdate(query); 
						//updating the features 
						
						//first deleting all the previous rights of that user then will add new rights.
						
						query = "delete from `user_access_admin` where user_id = "+UserID;
						s.executeUpdate(query);
						
						//addding new records
						
						String [] SelectedFeatures = request.getParameterValues("FeatureCheckbox");
						//System.out.println("Hello "+SelectedFeatures[1]);
						if(SelectedFeatures != null)
						{
							for(int i =0;i<SelectedFeatures.length;i++)
							{
								//insert query to features table
								query1 = "insert into " +
						                "`user_access_admin`(`user_id`,`feature_id`,`created_on`,`created_by`)" +
						                "values("+UserID+","+SelectedFeatures[i]+",now(),"+Utilities.parseInt(session.getAttribute("UserID").toString())+")";
								System.out.println(query1);
								s.executeUpdate(query1);
								
								//System.out.println("Hello "+SelectedFeatures[i]);
							}
						}
						
						
						
						obj.put("success", "true");
			}
			
			
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
