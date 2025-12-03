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

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Beat Plan Create Execute", urlPatterns = { "/outlet/BeatPlanCreateExecute" })
public class BeatPlanCreateExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BeatPlanCreateExecute() {
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
		
		
		String OutletGroupName = Utilities.filterString(request.getParameter("BeatPlanCreateMainFormOutletGroupName"), 1, 100);
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		long OrderBookerID = Utilities.parseLong(request.getParameter("OrderBookerID"));
		long IsalternativeID[] =Utilities.parseLong(request.getParameterValues("Isalternative"));
		
		long SMID = Utilities.parseLong(request.getParameter("SMID"));
		long TDMID = Utilities.parseLong(request.getParameter("TDMID"));
		long ASMID = Utilities.parseLong(request.getParameter("ASMID"));
		
		String SMIDInsert = "null";
		String TDMIDInsert = "null";
		String ASMIDInsert = "null";
		
		if (SMID != 0){
			SMIDInsert = ""+SMID;
		}
		if (TDMID != 0){
			TDMIDInsert = ""+TDMID;
		}
		if (ASMID != 0){
			ASMIDInsert = ""+ASMID;
		}
		
		
		long OutletID[] = Utilities.parseLong(request.getParameterValues("EmployeeBeatPlanMainFormOutletID"));
		
		String sunday[] = Utilities.filterString(request.getParameterValues("sunday"), 1, 100);
		
		String monday[] = Utilities.filterString(request.getParameterValues("monday"), 1, 100);
		String tuesday[] = Utilities.filterString(request.getParameterValues("tuesday"), 1, 100);
		String wednesday[] = Utilities.filterString(request.getParameterValues("wednesday"), 1, 100);
		String thursday[] = Utilities.filterString(request.getParameterValues("thursday"), 1, 100);
		String friday[] = Utilities.filterString(request.getParameterValues("friday"), 1, 100);
		String saturday[] = Utilities.filterString(request.getParameterValues("saturday"), 1, 100);
		String hibernation[] = Utilities.filterString(request.getParameterValues("hibernation"), 1, 100);
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			if(isEditCase){
				s.executeUpdate("UPDATE `distributor_beat_plan` set `label`='"+OutletGroupName+"', `distributor_id` = "+DistributorID+", `updated_on` = now(), `updated_by` = "+UserID+", sm_id="+SMIDInsert+", tdm_id="+TDMIDInsert+", asm_id="+ASMIDInsert+" where id="+EditID);
			}else{
				s.executeUpdate("INSERT INTO `distributor_beat_plan`(`label`, `distributor_id`,`created_on`,`created_by`, `sm_id`, `tdm_id`, `asm_id`)VALUES('"+OutletGroupName+"', "+DistributorID+",now(),"+UserID+", "+SMIDInsert+", "+TDMIDInsert+", "+ASMIDInsert+")");
			}
			
			
			
			
			int BeatPlanID = 0;
			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			if(rs.first()){
				BeatPlanID = rs.getInt(1);
			}
			
			if(isEditCase){
				BeatPlanID = EditID;
			}
			
			s.executeUpdate("delete from distributor_beat_plan_users where id = "+BeatPlanID);
			if(OrderBookerID > 0){
				s.executeUpdate("insert into distributor_beat_plan_users (id, assigned_to, assigned_on, assigned_by) values ("+BeatPlanID+", "+OrderBookerID+", now(), "+UserID+") ");
			}
			
			s.executeUpdate("delete from distributor_beat_plan_schedule where id = "+BeatPlanID);
			if (OutletID != null){
			for(int i = 0; i < OutletID.length; i++){
				
				String	isActive="0";
				if(hibernation != null){
					for(int x = 0; x < hibernation.length; x++){
						String[] arrOfHibernation = hibernation[x].split("-", 2);
						if(arrOfHibernation[0].equals(OutletID[i]+"")) {
					//		System.out.println("Outlet ID : "+arrOfHibernation[0]+" is "+arrOfHibernation[1]);
							isActive =	arrOfHibernation[1];
							
						}
						//System.out.println("Outlet ID : "+arrOfHibernation[0]+" is "+arrOfHibernation[1]);
						//System.out.println("Outlet ID : "+OutletID[i]+" is "+arrOfHibernation[1]);
						
					}
				}
				
				
				
				boolean hasWeekDay = false;
				
				if(sunday != null){
					for(int x = 0; x < sunday.length; x++){
						if(sunday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",1,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				if(monday != null){
					for(int x = 0; x < monday.length; x++){
						if(monday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",2,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				if(tuesday != null){
					for(int x = 0; x < tuesday.length; x++){
						if(tuesday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",3,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				if(wednesday != null){
					for(int x = 0; x < wednesday.length; x++){
						if(wednesday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",4,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				if(thursday != null){
					for(int x = 0; x < thursday.length; x++){
						if(thursday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",5,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				if(friday != null){
					for(int x = 0; x < friday.length; x++){
						if(friday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",6,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				if(saturday != null){
					for(int x = 0; x < saturday.length; x++){
						if(saturday[x].equals(OutletID[i]+"")){
							s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`,`is_active`)VALUES("+BeatPlanID+","+OutletID[i]+",7,"+isActive+")");
							hasWeekDay = true;
						}
					}
				}
				
				/*if(hasWeekDay == false){
					s2.executeUpdate("INSERT INTO `distributor_beat_plan_schedule`(`id`,`outlet_id`,`day_number`)VALUES("+BeatPlanID+","+OutletID[i]+",0)");
				}
				*/
				
		}
			}
			
			if(IsalternativeID!=null)
			{	if(IsalternativeID.length>0)
			{
			   for(int x = 0; x < IsalternativeID.length; x++)
			   {/* System.out.println(IsalternativeID[x]);
			     System.out.println(BeatPlanID);
				 System.out.println("update `distributor_beat_plan_schedule` set Isalternative=1 where id='"+BeatPlanID+"' and outlet_id='"+IsalternativeID[x]+"'");
				*/
				   s2.executeUpdate("update `distributor_beat_plan_schedule` set is_alternative=1 where id='"+BeatPlanID+"' and outlet_id='"+IsalternativeID[x]+"'");
					
				}
			}}
			
			ds.commit();
			
			// Update promotions cache
			/*
			BiProcesses bip = new BiProcesses();
			bip.createPromotionsCache();
			bip.close();
			*/
			
			obj.put("success", "true");
			
			s2.close();
			s.close();
			ds.dropConnection();
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
