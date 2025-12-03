package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;


@WebServlet(description = "Mobile Create Session", urlPatterns = { "/mobile/MobileCreateSession" })
public class MobileCreateSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileCreateSession() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("html");
		
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			//System.out.println("case1");
			long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
			String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			
			int LogTypeID = 0;
			
			Datasource ds = new Datasource();
			
			try {
				
				ds.createConnection();
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				
				ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+DeviceID+"'");
				if(rsD.first()){
					//System.out.println("case2");
				ResultSet rs = s2.executeQuery("select password, md5('"+LoginPassword+"'), id, display_name, email, designation, department, type_id, distributor_id, (select customer_name from outletmaster where customer_id=distributor_id limit 1) distributor_name  from users where ID="+LoginUsername+" and IS_ACTIVE=1 ");
				if(rs.first()){
					//System.out.println("case3");
					if (rs.getString(1).equals(rs.getString(2)) | LoginPassword.equals(Utilities.getMobileAdminPassword())){
						//System.out.println("case4");
						LogTypeID = 4;
					
						session.setAttribute("UserID", rs.getString(3));
						session.setAttribute("UserDisplayName", rs.getString(4));
						session.setAttribute("UserPictureURL", request.getContextPath()+"/images/UserPictures/"+LoginUsername+".png");
						session.setAttribute("UserEmail", rs.getString(5));
						session.setAttribute("UserDesignation", rs.getString(6));
						session.setAttribute("UserDepartment", rs.getString(7));
						session.setAttribute("UserTypeID", rs.getString(8));
						session.setAttribute("UserDistributorID", rs.getString(9));
						session.setAttribute("UserDistributorName", rs.getString(10));
						session.setAttribute("isMobileSession", "1");
						
						
						response.sendRedirect("/portal/MobileReportCenterMain.jsp");
						
					}else{
						//System.out.println("case5");
						LogTypeID = 7;
						out.print("Error Code = "+104);
						//json.put("success", "false");
						//json.put("error_code", "104");
					}
					
				}else{
					//System.out.println("case6");
					LogTypeID = 5;
					out.print("Error Code = "+103);
					//json.put("success", "false");
					//json.put("error_code", "103");
				}
				
				}else{
					//System.out.println("case7");
					LogTypeID = 6;
					out.print("Error Code = "+102);
					//json.put("success", "false");
					//json.put("error_code", "102");
				}
				
				
				/*if (LogTypeID != 0){
					s2.executeUpdate("insert into "+ds.logDatabaseName()+".log_user_login(user_id,password, ip_address, attempted_on,type_id, mobile_uuid) values("+LoginUsername+",'','"+request.getRemoteAddr()+"',now(),"+LogTypeID+",'"+DeviceID+"')");
					
				}*/
				
				
				s2.close();
				s.close();
				ds.dropConnection();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			//System.out.println("case8");
			//json.put("success", "false");
			//json.put("error_code", "101");
			out.print("Error Code = "+101);
		}
		
		//out.print(json);
		out.close();
		
	}
	
}
