package com.pbc.outlet;

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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.mf.utils.MFParseUtils;
import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Active Outlet Execute", urlPatterns = { "/outlet/ActiveOutletExecute" })

public class ActiveOutletExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ActiveOutletExecute() {
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
		
		
		int active = Utilities.parseInt(request.getParameter("active"));
		int outlet = Utilities.parseInt(request.getParameter("outlet"));
		
		int channel_id = Utilities.parseInt(request.getParameter("channel_id"));
		
		boolean channel_update = MFParseUtils.parseBoolean(request.getParameter("channel_update"));
		
		//System.out.println("channel_update : "+channel_update);
		//System.out.println("channel_id : "+channel_id);
	
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
			
		
		try {
		
		
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			// update channel
			if(!channel_update) {
				//System.out.println("update common_outlets set pic_channel_id="+channel_id+" where id="+outlet);
				s2.executeUpdate("update common_outlets set pic_channel_id="+channel_id+" where id="+outlet);
			}
			
			
			ResultSet rsOldData = s.executeQuery("select is_active, updated_on,updated_by  from distributor_beat_plan_schedule where outlet_id="+outlet+" limit 1");
			if(rsOldData.first()) {
				
				int oldActive = rsOldData.getInt("is_active");
				String updated_on = (rsOldData.getString("updated_on") == null) ? null : "'"+rsOldData.getString("updated_on")+"'";
				String updated_by = (rsOldData.getString("updated_by") == null) ? null : "'"+rsOldData.getString("updated_by")+"'";
				
				//System.out.println("INSERT INTO "+ds.logDatabaseName()+".`distributor_beat_plan_outlet_logs`(`outlet_id`,`is_active`,`updated_on`,`updated_by`,`created_on`,`created_by`) VALUES ("+outlet+", "+oldActive+", "+updated_on+", "+updated_by+", now(), '"+UserID+"')");
				s2.executeUpdate("INSERT INTO "+ds.logDatabaseName()+".`distributor_beat_plan_outlet_logs`(`outlet_id`,`is_active`,`updated_on`,`updated_by`,`created_on`,`created_by`) VALUES ("+outlet+", "+oldActive+", "+updated_on+", "+updated_by+", now(), '"+UserID+"')");
				
				//System.out.println("update distributor_beat_plan_schedule set is_active="+active+", updated_on=now(), updated_by="+UserID+" where outlet_id="+outlet);
				s2.executeUpdate("update distributor_beat_plan_schedule set is_active="+active+", updated_on=now(), updated_by="+UserID+" where outlet_id="+outlet);
					//	s.executeUpdate("update distributor_beat_plan_schedule set is_active="+active+" where outlet_id="+outlet);
								
							//	String DayInsertSql="INSERT INTO distributor_beat_plan_schedule(id,outlet_id,day_number)values ("+BeatPlan+","+maxID+","+Day+")";
								
								//s9.executeUpdate(DayInsertSql);
				ds.commit();
				obj.put("success", "true");
			}else {
					obj.put("success", "false");
				obj.put("error", "Outlet Not Found");
			}
			
			s2.close();
			s.close();
			ds.dropConnection();
			
			//	obj.put("success", "false");
			//	obj.put("error", "City Not Found");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
