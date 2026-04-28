package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Devices Execute", urlPatterns = { "/mobile/MobileDevicesExecute" })
public class MobileDevicesExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public MobileDevicesExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		PrintWriter out = response.getWriter();
		
		long AssignToID = Utilities.parseLong(request.getParameter("MobileDevicesAssignToID"));
		
		String DeviceID = Utilities.filterString(request.getParameter("MobileDevicesDeviceID"), 1, 100);
		long EditID = Utilities.parseLong(request.getParameter("EditID"));
		long RecordID = Utilities.parseLong(request.getParameter("RecordID"));
		int SetActive = Utilities.parseInt(request.getParameter("SetActive"));
		String MobileNo = Utilities.filterString(request.getParameter("MobileDevicesMobileNo"), 1, 100);
		
		
		boolean isEditCase = false;
		if(EditID > 0){
			isEditCase = true;
		}
		
		JSONObject json = new JSONObject();
		
		Datasource ds = new Datasource();
		try{
			ds.createConnection();
			Statement s = ds.createStatement();
			
			String SQL = "";
			
			if(isEditCase){
				SQL = "update mobile_devices set uuid='"+DeviceID+"', issued_to="+AssignToID+", issued_on=now(), mobile_no='"+MobileNo+"' where id="+EditID;
			}else if(RecordID > 0){
				SQL = "update mobile_devices set is_active="+SetActive+" where id="+RecordID;
			}else{
				SQL = "INSERT INTO `mobile_devices`(`uuid`,`issued_to`,`mobile_no`,`issued_on`,`is_active`,`deactivated_on`,`deactivated_by`)VALUES('"+DeviceID+"',"+AssignToID+",'"+MobileNo+"',now(),1,null,null)";
			}
			
			//System.out.println(SQL);
			s.executeUpdate(SQL);
			
			json.put("success", "true");
			
			s.close();
		}catch(Exception e){
			System.out.print(e);
			json.put("success", "false");
			json.put("error", e.toString());
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
		
			
			
		out.print(json);
		
		
	}


}
