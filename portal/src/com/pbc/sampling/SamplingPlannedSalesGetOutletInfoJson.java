package com.pbc.sampling;

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

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Sampling Planned Sales Get Outlet Info Json", urlPatterns = { "/sampling/SamplingPlannedSalesGetOutletInfoJson" })
public class SamplingPlannedSalesGetOutletInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingPlannedSalesGetOutletInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		long OutletID =Utilities.parseLong(request.getParameter("OutletID"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT request_id, activated_on, (SELECT name FROM common_outlets where id="+OutletID+") outlet_name FROM sampling where outlet_id = "+OutletID+" and active = 1");
			if(rs.first()){
				obj.put("success", "true");
				obj.put("OutletName", rs.getString("outlet_name"));
				obj.put("RequestID", rs.getString("request_id"));
				obj.put("ActivatedOn", Utilities.getDisplayDateFormat(rs.getDate("activated_on")));
				obj.put("EndDate", Utilities.getDisplayDateFormat( Utilities.getDateByDays( rs.getDate("activated_on"), 365)));
			}else{
				obj.put("success", "false");
				obj.put("error", "No outlet found.");
			}
			
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (ds != null){
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
