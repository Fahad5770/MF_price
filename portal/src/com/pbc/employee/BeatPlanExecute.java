package com.pbc.employee;

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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Beat Plan Execute", urlPatterns = { "/employee/BeatPlanExecute" })
public class BeatPlanExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BeatPlanExecute() {
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
		
		boolean isEditCase = false;
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		if(EditID > 0){
			isEditCase =  true;
		}
		
		long AssignTo = Utilities.parseLong(request.getParameter("EmployeeBeatPlanMainFormAssignTo"));
		
		long OutletID[] = Utilities.parseLong(request.getParameterValues("EmployeeBeatPlanMainFormOutletID"));
		
		String sunday[] = Utilities.filterString(request.getParameterValues("sunday"), 1, 100);
		
		String monday[] = Utilities.filterString(request.getParameterValues("monday"), 1, 100);
		String tuesday[] = Utilities.filterString(request.getParameterValues("tuesday"), 1, 100);
		String wednesday[] = Utilities.filterString(request.getParameterValues("wednesday"), 1, 100);
		String thursday[] = Utilities.filterString(request.getParameterValues("thursday"), 1, 100);
		String friday[] = Utilities.filterString(request.getParameterValues("friday"), 1, 100);
		String saturday[] = Utilities.filterString(request.getParameterValues("saturday"), 1, 100);
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			if(isEditCase){
				s.executeUpdate("UPDATE `employee_beat_plan` set `assigned_to` = "+AssignTo+", `updated_on` = now(), `updated_by` = "+UserID+" where beat_plan_id="+EditID);
				
				if (OutletID == null){
					s.executeUpdate("delete from employee_beat_plan where beat_plan_id="+EditID);
				}
				
				
			}else{
				s.executeUpdate("INSERT INTO `employee_beat_plan`(`assigned_to`,`created_on`,`created_by`)VALUES("+AssignTo+",now(),"+UserID+")");
			}
			
			
			int BeatPlanID = 0;
			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			if(rs.first()){
				BeatPlanID = rs.getInt(1);
			}
			
			if(isEditCase){
				BeatPlanID = EditID;
			}
			 
			s.executeUpdate("delete from employee_beat_plan_schedule where beat_plan_id = "+BeatPlanID);
			if (OutletID != null){
			for(int i = 0; i < OutletID.length; i++){
				
				if(sunday != null){
					for(int x = 0; x < sunday.length; x++){
						if(sunday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",1)");
						}
					}
				}
				
				if(monday != null){
					for(int x = 0; x < monday.length; x++){
						if(monday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",2)");
						}
					}
				}
				
				if(tuesday != null){
					for(int x = 0; x < tuesday.length; x++){
						if(tuesday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",3)");
						}
					}
				}
				
				if(wednesday != null){
					for(int x = 0; x < wednesday.length; x++){
						if(wednesday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",4)");
						}
					}
				}
				
				if(thursday != null){
					for(int x = 0; x < thursday.length; x++){
						if(thursday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",5)");
						}
					}
				}
				
				if(friday != null){
					for(int x = 0; x < friday.length; x++){
						if(friday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",6)");
						}
					}
				}
				
				if(saturday != null){
					for(int x = 0; x < saturday.length; x++){
						if(saturday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `employee_beat_plan_schedule`(`beat_plan_id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",7)");
						}
					}
				}
				
				
			}
			}
			obj.put("success", "true");
			
			s2.close();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
