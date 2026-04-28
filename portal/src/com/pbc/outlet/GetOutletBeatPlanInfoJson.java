package com.pbc.outlet;

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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Beat Plan Info Json", urlPatterns = { "/outlet/GetOutletBeatPlanInfoJson" })
public class GetOutletBeatPlanInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetOutletBeatPlanInfoJson() {
        super();
        
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			JSONArray jr2 = new JSONArray();
			
			
			ResultSet rs = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=distributor_beat_plan.distributor_id) distributor_name, ( SELECT concat(first_name, ' ', last_name) name FROM employee_view where sap_code=sm_id ) sm_name, ( SELECT concat(first_name, ' ', last_name) name FROM employee_view where sap_code=tdm_id ) tdm_name, ( SELECT concat(first_name, ' ', last_name) name FROM employee_view where sap_code=asm_id ) asm_name FROM distributor_beat_plan where id="+EditID);
			if(rs.first()){
				
				obj.put("exists", "true");
				obj.put("PJPName", rs.getString("label"));
				obj.put("DistributorID", rs.getString("distributor_id"));
				obj.put("DistributorName", rs.getString("distributor_name"));
				
				obj.put("SMID", rs.getString("sm_id"));
				obj.put("SMName", rs.getString("sm_name"));
				obj.put("TDMID", rs.getString("tdm_id"));
				obj.put("TDMName", rs.getString("tdm_name"));
				obj.put("ASMID", rs.getString("asm_id"));
				obj.put("ASMName", rs.getString("asm_name"));
				
				ResultSet rs4 = s.executeQuery("SELECT assigned_to, ( SELECT concat(first_name, ' ', last_name) name FROM employee_view where sap_code=assigned_to ) name FROM distributor_beat_plan_users where id="+EditID);
				if(rs4.next()){
					obj.put("OrderBookerID", rs4.getString("assigned_to"));
					obj.put("OrderBookerName", rs4.getString("name"));
				}
				
				String content = "";
				
				ResultSet rs3 = s.executeQuery("SELECT distinct outlet_id,is_alternative,is_active, (SELECT name FROM common_outlets where id=outlet_id) outlet_name FROM distributor_beat_plan_schedule  where id="+EditID);
				int counter = 0;
				while (rs3.next()){
					String Checked="";
					if(rs3.getInt("is_alternative")==1)
					{
						Checked="checked";
					}
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("OutletID", rs3.getString("outlet_id"));
					rows.put("OutletName", rs3.getString("outlet_name"));
					
					jr.add(rows);
					
					
					content += ""+
							"<tr id='BeatPlan"+counter+"'>"+
								"<td valign='middle'>"+rs3.getString("outlet_id")+" - "+rs3.getString("outlet_name")+"<input type='hidden' name='EmployeeBeatPlanMainFormOutletID' id='EmployeeBeatPlanMainFormOutletID' value='"+rs3.getString("outlet_id")+"' ></td>"+
								"<td valign='middle' style=\" color: "+(rs3.getInt("is_active") == 1 ? "green" : "red")+"  \"  >"+(rs3.getInt("is_active") == 1 ? "Active" : "InActive")+"</td>"+
								"<input type=hidden name='hibernation' value='"+rs3.getString("outlet_id")+"-"+rs3.getInt("is_active")+"' >"+
								"<td nowrap='nowrap'>"+
								
								"<fieldset data-role='controlgroup' data-type='horizontal'>"+
				
					                "<input type='checkbox' name='sunday' id='sunday"+rs3.getString("outlet_id")+"' onclick='VerifyIsalternative(this);'  value='"+rs3.getString("outlet_id")+"'  >"+
					                "<label for='sunday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003;</label>"+
					                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='monday' id='monday"+rs3.getString("outlet_id")+"' value='"+rs3.getString("outlet_id")+"'>"+
					                "<label for='monday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003;</label>"+
					                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='tuesday' id='tuesday"+rs3.getString("outlet_id")+"' value='"+rs3.getString("outlet_id")+"'>"+
					                "<label for='tuesday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003; </label>"+
					                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='wednesday' id='wednesday"+rs3.getString("outlet_id")+"' value='"+rs3.getString("outlet_id")+"'>"+
					                "<label for='wednesday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003; </label>"+
					                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='thursday' id='thursday"+rs3.getString("outlet_id")+"' value='"+rs3.getString("outlet_id")+"'>"+
					                "<label for='thursday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003; </label>"+
					                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='friday' id='friday"+rs3.getString("outlet_id")+"' value='"+rs3.getString("outlet_id")+"'>"+
					                "<label for='friday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003; </label>"+
					                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='saturday' id='saturday"+rs3.getString("outlet_id")+"' value='"+rs3.getString("outlet_id")+"'>"+
					                "<label for='saturday"+rs3.getString("outlet_id")+"' class='radio_style'>&#10003; </label>"+
					                
								
								"</fieldset></td>"+
								"<td><input type='checkbox' style='position: absolute !important;top:-18px !important;height:80px !important;width:20px !important;' name='Isalternative' id='Isalternative"+rs3.getString("outlet_id")+"' "+Checked+" value='"+rs3.getString("outlet_id")+"'></td>"+
								"<td valign='middle'><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"BeatPlanDeleteRow('BeatPlan"+counter+"');\">Delete</a></td>"+
							"</tr>";
					
					counter++;
									
				}
				
				obj.put("content", content);
				obj.put("content_rows", counter);
				
				obj.put("rows_distinct", jr);
				
				
				ResultSet rs2 = s.executeQuery("SELECT *, (select long_name from common_days_of_week where id=day_number) day_name  FROM distributor_beat_plan_schedule where id="+EditID);
				
				while (rs2.next()){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("OutletID", rs2.getString("outlet_id"));
					rows.put("DayNumber", rs2.getString("day_number"));
					rows.put("DayName", rs2.getString("day_name"));
					rows.put("IsActive", rs2.getString("is_active"));
					
					jr2.add(rows);
									
				}
				
				
				
				
				obj.put("rows", jr2);
				
				
				
			}
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
