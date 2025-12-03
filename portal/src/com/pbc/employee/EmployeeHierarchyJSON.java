package com.pbc.employee;

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

import com.pbc.common.EmployeeHierarchy;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Employee Tree New Approach", urlPatterns = { "/employee/EmployeeHierarchyJSON" })
public class EmployeeHierarchyJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmployeeHierarchyJSON() {
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
		Datasource ds = new Datasource();
		JSONObject obj=new JSONObject();
		JSONArray jr = new JSONArray();	
		JSONArray jr1 = new JSONArray();
		JSONArray jr2 = new JSONArray();
		JSONArray jrpjp = new JSONArray();
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			String where="";
			int LevelID = Utilities.parseInt(request.getParameter("ForLevelID1"));
			
			String DownwardNodesQueryString = "";
			String DownwardNodesQueryString1 = "";
			String DownwardNodesQueryString2 = "";
			String DownwardNodesQueryString3 = "";
			String LevelClause="";
		    //UserID="2588";
			//UserID="2241";
			//UserID="2460";
			
			//int ShowAllNodes = Utilities.parseInt(request.getParameter("ShowAllNodes"));
			
			if(Utilities.isAuthorized(77, Utilities.parseLong(UserID+""))==false){//means show limited nodes
				EmployeeHierarchy EH = new EmployeeHierarchy();
				DownwardNodesQueryString= EH.getDownwardHierarchyQueryString(EH.getDownwardHierarchy(Utilities.parseLong(UserID+"")));
				if(DownwardNodesQueryString==""){ //if empty - mean no child exist then show that node itself
					DownwardNodesQueryString1 =" and u.id in("+UserID+")";
					DownwardNodesQueryString2 =" and u.id in("+UserID+")";
				}
				else{
					DownwardNodesQueryString1 =" and u.id in("+DownwardNodesQueryString+")";
					DownwardNodesQueryString2 =" and u.id in("+UserID+")";
				}
			}else{ //show all nodes - have full access
				DownwardNodesQueryString1="";
				DownwardNodesQueryString2="";
			}
			
			where = " and chl.authority_order>= (select chl1.authority_order from common_hierarchy_levels chl1 where chl1.id="+LevelID+")";
			
			if(Utilities.isAuthorized(78, Utilities.parseLong(UserID+""))){
				DownwardNodesQueryString1="";
				//DownwardNodesQueryString2 =" and u.current_reporting_to in("+UserID+")";
				LevelID =2;
				 //LevelClause = "and u.current_reporting_level=4";
				where=" and u.current_reporting_level>=2";
				
			}
			
			
				
			ResultSet rs = s.executeQuery("select u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,current_reporting_level,node_color as color_code,node_border_color as border_node_color,chl.authority_order from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null "+where+DownwardNodesQueryString1);
			//System.out.println("Query 1 "+"select u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null "+where+DownwardNodesQueryString1);	
			
			while (rs.next()){
					// add another query which will check the "reporting_to" -  "authority level" and if authority_level > user selected id's authority level then return another json row in new json obj
					
					
					if(Utilities.isAuthorized(77, Utilities.parseLong(UserID+""))==false){
						
						if(Utilities.isAuthorized(78, Utilities.parseLong(UserID+"")) && Utilities.isAuthorized(77, Utilities.parseLong(UserID+""))==false){ //limited access and level 4 right - case
							//DownwardNodesQueryString2 = " and u.id="+rs.getInt("current_reporting_to");
							ResultSet rs2 = s1.executeQuery("select distinct u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null and u.id="+rs.getInt("current_reporting_to"));
							//System.out.println("Query 2 "+"select distinct u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null and u.id="+rs.getInt("current_reporting_to"));
							if(rs2.first()){
								//System.out.println(rs.getInt("current_reporting_to")+" ==> "+rs1.getString("current_reporting_level"));
								int ReportingLevel2 = rs2.getInt("current_reporting_level"); 
								//System.out.println("Reporting Level from DB "+ReportingLevel+" - Selected Level "+LevelID);
								if (ReportingLevel2 != 0){
									if (ReportingLevel2 < LevelID){
										//System.out.println("Reporting Level 2 "+ReportingLevel2+" Level ID "+LevelID);
										
										//System.out.println("Yes me in if");
										
										//System.out.println("select u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null and id="+rs.getInt("current_reporting_to")+where);
										//System.out.println("Row Added "+rs2.getString("id"));
										LinkedHashMap rows2 = new LinkedHashMap();
										
										rows2.put("ID", rs2.getString("id"));
										rows2.put("FirstName", rs2.getString("first_name"));
										rows2.put("LastName", rs2.getString("last_name"));
										rows2.put("DisplayName", rs2.getString("display_name"));
										rows2.put("Designation", rs2.getString("designation"));
										rows2.put("Department", rs2.getString("department"));
										rows2.put("Email", rs2.getString("email"));
										rows2.put("LevelName", rs2.getString("level_name"));
										rows2.put("NodeColor", rs2.getString("color_code"));
										rows2.put("BorderNodeColor", rs2.getString("border_node_color"));
										rows2.put("CurrentReportingTo", rs2.getString("current_reporting_to"));
										
										//System.out.println("Inner Most Current Reporting Level [limited access and level 4 right] ===>   "+rs2.getString("current_reporting_level"));
										
										jr2.add(rows2);
									}
								}
							}
						
						
						
						
						}else{
							ResultSet rs1 = s1.executeQuery("select distinct u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null "+DownwardNodesQueryString2);
							//System.out.println("select distinct u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null "+DownwardNodesQueryString2);
							if(rs1.first()){
								LinkedHashMap rows1 = new LinkedHashMap();
								
								rows1.put("ID", rs1.getString("id"));
								rows1.put("FirstName", rs1.getString("first_name"));
								rows1.put("LastName", rs1.getString("last_name"));
								rows1.put("DisplayName", rs1.getString("display_name"));
								rows1.put("Designation", rs1.getString("designation"));
								rows1.put("Department", rs1.getString("department"));
								rows1.put("Email", rs1.getString("email"));
								rows1.put("LevelName", rs1.getString("level_name"));
								rows1.put("NodeColor", rs1.getString("color_code"));
								rows1.put("BorderNodeColor", rs1.getString("border_node_color"));
								rows1.put("CurrentReportingTo", rs1.getString("current_reporting_to"));
								//System.out.println("Inner Most Else Case Current Reporting Level [Not limited access and level 4 right] ===>   "+rs1.getString("current_reporting_level"));
								jr1.add(rows1);
							}
						}
						
						}else{
					ResultSet rs1 = s1.executeQuery("select distinct u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null and u.id="+rs.getInt("current_reporting_to")+DownwardNodesQueryString2);
							//System.out.println("select u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null and u.id="+rs.getInt("current_reporting_to"));
							if(rs1.first()){
								//System.out.println(rs.getInt("current_reporting_to")+" ==> "+rs1.getString("current_reporting_level"));
								int ReportingLevel = rs1.getInt("current_reporting_level"); 
								//System.out.println("Reporting Level from DB "+ReportingLevel+" - Selected Level "+LevelID);
								if (ReportingLevel != 0){
									if (ReportingLevel < LevelID){
										//System.out.println("Yes me in if");
										
										//System.out.println("select u.id,first_name,last_name,display_name,designation,department,email,current_reporting_to,short_name as level_name,node_color as color_code,node_border_color as border_node_color,chl.authority_order,u.current_reporting_level from users u,common_hierarchy_levels chl  where u.current_reporting_level=chl.id and current_reporting_to is not null and id="+rs.getInt("current_reporting_to")+where);
										
										LinkedHashMap rows1 = new LinkedHashMap();
										
										rows1.put("ID", rs1.getString("id"));
										rows1.put("FirstName", rs1.getString("first_name"));
										rows1.put("LastName", rs1.getString("last_name"));
										rows1.put("DisplayName", rs1.getString("display_name"));
										rows1.put("Designation", rs1.getString("designation"));
										rows1.put("Department", rs1.getString("department"));
										rows1.put("Email", rs1.getString("email"));
										rows1.put("LevelName", rs1.getString("level_name"));
										rows1.put("NodeColor", rs1.getString("color_code"));
										rows1.put("BorderNodeColor", rs1.getString("border_node_color"));
										rows1.put("CurrentReportingTo", rs1.getString("current_reporting_to"));
										System.out.println("Inner Most Current Reporting Level [Full access] ===>   "+rs1.getString("current_reporting_level"));
										jr1.add(rows1);
									}
								}
							}
					}
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ID", rs.getString("id"));
					rows.put("FirstName", rs.getString("first_name"));
					rows.put("LastName", rs.getString("last_name"));
					rows.put("DisplayName", rs.getString("display_name"));
					rows.put("Designation", rs.getString("designation"));
					rows.put("Department", rs.getString("department"));
					rows.put("Email", rs.getString("email"));
					rows.put("LevelName", rs.getString("level_name"));
					rows.put("NodeColor", rs.getString("color_code"));
					rows.put("BorderNodeColor", rs.getString("border_node_color"));
					rows.put("CurrentReportingTo", rs.getString("current_reporting_to"));
					rows.put("CurrentReportingLevel",rs.getString("current_reporting_level"));
					//System.out.println("Inner Most Current Reporting Level [Normal Case] ===>   "+rs.getString("current_reporting_level"));
					jr.add(rows);
					
					
					
					
					//PJP - Normal Case
					if(rs.getInt("current_reporting_level")==7){
						
						ResultSet rs5 = s1.executeQuery("SELECT *,(select name from common_distributors cd where cd.distributor_id=dbp.distributor_id) dist_name FROM distributor_beat_plan_users dbpu ,distributor_beat_plan dbp where dbpu.id=dbp.id and dbpu.assigned_to="+rs.getInt("id"));
						while(rs5.next()){
							LinkedHashMap rowspjp = new LinkedHashMap();
							//System.out.println(rs5.getString("id")+" - "+rs5.getString("assigned_to"));
							rowspjp.put("PJPID", rs5.getString("id"));
							rowspjp.put("PJPLabel", rs5.getString("label"));
							rowspjp.put("PJPDistName", rs5.getString("dist_name"));
							rowspjp.put("PJPReportingTo", rs5.getString("assigned_to"));
							jrpjp.add(rowspjp);
						}
					}
					
					
				}
				obj.put("rows", jr);
				obj.put("rows1", jr1);
				obj.put("rows2", jr2);
				obj.put("rowspjp",jrpjp);
				obj.put("success", "true");				
				out.print(obj);
				out.close();
			
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
