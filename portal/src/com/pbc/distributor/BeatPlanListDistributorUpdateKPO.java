package com.pbc.distributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
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

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "BeatPlan List Distributor Update KPO", urlPatterns = { "/distributor/BeatPlanListDistributorUpdateKPO" })
public class BeatPlanListDistributorUpdateKPO extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BeatPlanListDistributorUpdateKPO() {
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
		
		long CurrentDistributorID = Utilities.parseLong(request.getParameter("CurrentDistributorID"));
		long KPOID = Utilities.parseLong(request.getParameter("KPOID"));
		
		boolean RemoveKPO = Utilities.parseBoolean(request.getParameter("RemoveKPO"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		JSONArray jr = new JSONArray();
		
		try {
			
			/*
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			if(RemoveKPO){
				s.executeUpdate("delete from common_distributors_kpos where distributor_id="+CurrentDistributorID+" and kpo_id="+KPOID);
			}else{
				s.executeUpdate("insert into common_distributors_kpos (distributor_id, kpo_id, created_on, created_by) values("+CurrentDistributorID+", "+KPOID+", now(), "+UserID+")");
			}
			
			obj.put("success", "true");
			
			ResultSet rs = s.executeQuery("SELECT kpo_id, (select concat(first_name, ' ', last_name) from employee_view where sap_code=kpo_id ) kpo_name FROM common_distributors_kpos where distributor_id="+CurrentDistributorID);
			while(rs.next()){
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("KPOID", rs.getLong("kpo_id"));
				rows.put("KPOName", rs.getString("kpo_name"));
				
				jr.add(rows);
			}
			
			obj.put("rows", jr);
			
			
			ds.commit();
			s.close();
			*/
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
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
