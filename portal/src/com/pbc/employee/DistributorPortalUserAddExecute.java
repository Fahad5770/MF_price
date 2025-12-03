package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.util.Datasource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.Date;
import java.sql.ResultSet;

@WebServlet(description = "Add Users", urlPatterns = { "/employee/DistributorPortalUserAddExecute" })
public class DistributorPortalUserAddExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorPortalUserAddExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		System.out.println("in user add");
		HttpSession session = request.getSession();
		long userid=Utilities.parseLong((String)session.getAttribute("UserID"));
		String UserID=request.getParameter("UserID");
		String NewPass =request.getParameter("NewPassword");
		String ConfirmPass=request.getParameter("ConfirmPass");
		String FirstName=request.getParameter("FisrtName");
		String LastName=request.getParameter("LastName");
		String DisplayName=request.getParameter("DisplayName");
		String Designation=request.getParameter("Designation");
		String Dept=request.getParameter("Dept");
		String Email=request.getParameter("Email");
		String Distributor=request.getParameter("Distributor");
		int checkID=0;
		//System.out.println("userid :"+userid+"oldPass :"+oldPass+"NewPass: "+NewPass);
		//String Msg="";
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		PrintWriter out = response.getWriter();
		try {
			
			System.out.println("in Print Writer");
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s3 = ds.createStatement();
		
			boolean valid = false;
			//System.out.println("select password, md5('"+oldPass+"') from users where id = "+userid+" and IS_ACTIVE= 0  and is_distributor_user=1");
//			ResultSet rs = s.executeQuery("select password, md5('"+oldPass+"') from users where id = "+userid+" and IS_ACTIVE=0  and is_distributor_user=1");
			ResultSet rs = s.executeQuery("select ID from users where ID='"+UserID+"'");
			ResultSet rs3 = s3.executeQuery("select distributor_id from common_distributors where distributor_id='"+Distributor+"' limit 1;");
			if (rs3.first()) {
				checkID=rs3.getInt("distributor_id");		
			}
			if (rs.first()){
				System.out.println("in already Exists");
				String ID=rs.getString("ID");
				if (ID!="") {
					System.out.println("already exists");
					obj.put("Msg","User Already Exists !");
					obj.put("isExist1","false");
				}
				
			}
			else if (checkID<1) {
				System.out.println("in common dist");
		
					obj.put("Msg","Distributor ID is invalid !");
					obj.put("isExist1","false");
			}
			else {
				//System.out.println("DoesMatch Query");
				//System.out.println("update users set password=md5('"+NewPass+"'),password_changed_on=now()  where id = "+userid+" and IS_ACTIVE=0  and is_distributor_user=1 ");
				//System.out.println("insert into users(ID,PASSWORD,FIRST_NAME,LAST_NAME,DISPLAY_NAME,DESIGNATION,DEPARTMENT,EMAIL,IS_ACTIVE,is_distributor_user,distributor_id,type_id) values("+UserID+",md5('"+NewPass+"'),'"+FirstName+"','"+LastName+"','"+DisplayName+"','"+Designation+"','"+Dept+"','"+Email+"',0,1,'"+Distributor+"',1) ");
				s1.executeUpdate("insert into users(ID,PASSWORD,FIRST_NAME,LAST_NAME,DISPLAY_NAME,DESIGNATION,DEPARTMENT,EMAIL,IS_ACTIVE,is_distributor_user,distributor_id,type_id) values("+UserID+",md5('"+NewPass+"'),'"+FirstName+"','"+LastName+"','"+DisplayName+"','"+Designation+"','"+Dept+"','"+Email+"',1,1,"+Integer.parseInt(Distributor)+",1) ");
				obj.put("Msg","User Created Successfully");
				obj.put("isExist1","true");
			
			}
			
			rs.close();
			s.close();
			s1.close();
			s3.close();
			
			
		
			ds.dropConnection();
			
		} catch (Exception e) {
			//sendErrorRedirect(request,response,Utilities.getErrorPageURL(request),e);
			obj.put("isExist1","false");
		}
		out.print(obj);
		out.close();
	}
	
	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}	

}
