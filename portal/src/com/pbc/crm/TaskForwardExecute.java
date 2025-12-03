package com.pbc.crm;

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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.sun.jmx.snmp.tasks.Task;


@WebServlet(description = "Task Forward Execute", urlPatterns = { "/crm/TaskForwardExecute" })
public class TaskForwardExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TaskForwardExecute() {
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
		
		String TaskIDs[] = request.getParameterValues("TaskID"); 
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			for(int i = 0; i < TaskIDs.length; i++){
				
				boolean toAmirAftab = false;
				boolean toSaqib = false;
				boolean toNaeem = false;
				
				ResultSet rs = s.executeQuery("SELECT *, ( SELECT concat(region_short_name, ' - ', region_name) region_name  FROM common_regions where region_id=crm_tasks.region_id ) region_name, (SELECT count(id) no_of_files FROM crm_tasks_files where id=crm_tasks.id) no_of_files FROM crm_tasks WHERE id="+TaskIDs[i]);
				if(rs.first()){
					
					
					int RegionID = rs.getInt("region_id");
					

					
					int NumOfFiles = rs.getInt("no_of_files");
					String Message = rs.getString("region_name")+"\n\n";
					Message += "Complaint ID: "+TaskIDs[i]+"\n\n";
					
					if( rs.getString("outlet_id")!= null && !rs.getString("outlet_id").equals("") && !rs.getString("outlet_id").equals("0") ){
						Message += rs.getString("outlet_id")+" - ";
					}
					
					Message += rs.getString("outlet_name")+"\n";
					Message += rs.getString("outlet_address")+" "+rs.getString("outlet_contact_no")+"\n\n";
					
					ResultSet rs2 = s2.executeQuery("SELECT ctl.type_id, ctt.english_label FROM crm_tasks_list ctl, crm_tasks_types ctt where ctl.type_id=ctt.id and ctl.id="+TaskIDs[i]+" order by ctl.type_id");
					while( rs2.next() ){
						Message += rs2.getString("english_label")+"\n";
						
						
						// Amir Aftab
						if (RegionID == 5 | RegionID == 2 | RegionID == 4 | RegionID == 7){
							if (rs2.getInt(1) >= 3 && rs2.getInt(1) <= 14){
								toAmirAftab = true;
							}
							if (rs2.getInt(1) >= 21 && rs2.getInt(1) <= 24){
								toAmirAftab = true;
							}
							if (rs2.getInt(1) >= 1 && rs2.getInt(1) <= 2){
								toAmirAftab = true;
							}
							
						}
						
						// Saqib
						if (RegionID == 1 | RegionID == 3 | RegionID == 6){
							// Naeem
							if (rs2.getInt(1) == 3 | rs2.getInt(1) == 4){
								toNaeem = true;
							}
							
							if (rs2.getInt(1) >= 3 && rs2.getInt(1) <= 14){
								toSaqib = true;
							}
							if (rs2.getInt(1) >= 21 && rs2.getInt(1) <= 24){
								toSaqib = true;
							}
							if (rs2.getInt(1) >= 1 && rs2.getInt(1) <= 2){
								toSaqib = true;
							}
							
						}
						
					}
					
					Message += "\n"; 
					
					Message += rs.getString("description")+"\n\n";
					
					
					
					String Pictures[] = new String[NumOfFiles];
					int counter = 0;
					ResultSet rs3 = s2.executeQuery("SELECT filename FROM crm_tasks_files where is_resolved=0 and id="+TaskIDs[i]);
					while( rs3.next() ){
						Pictures[counter] = rs3.getString("filename");
						counter++;
					}
					
					try{
						
						
						// Amir Aftab
						if (toAmirAftab == true){
							//Utilities.sendWhatsApp("923444478381", Message, Pictures);
							
							// Amir Aftab Group with CEO
							Utilities.sendWhatsApp("923334566993-1406051187", Message, Pictures);
							//Thread.currentThread().sleep(2500);
							
							// Amir Aftab RSM Group A
							//Utilities.sendWhatsApp("923449279270-1409211750", Message, Pictures); old
							if (RegionID != 2){
								Utilities.sendWhatsApp("923334566993-1426082835", Message, Pictures);
							}else{
								Utilities.sendWhatsApp("923334566993-1426154744", Message, Pictures);
							}
							
							//Thread.currentThread().sleep(2500);
							
							
						}
							
						// Naeem
						if (toNaeem == true || toSaqib == true){
							//Utilities.sendWhatsApp("923444473610", Message, Pictures);
							//Utilities.sendWhatsApp("923334566993-1406051246", Message, Pictures);
							//Thread.currentThread().sleep(2500);
						}
						
						// Saqib
						if (toSaqib == true){
							//Utilities.sendWhatsApp("923444471111", Message, Pictures);
							Utilities.sendWhatsApp("923334566993-1406051356", Message, Pictures);
							//Thread.currentThread().sleep(2500);
							
							// RSM Group B
							//Utilities.sendWhatsApp("923449279270-1409220284", Message, Pictures);
							//Thread.currentThread().sleep(2500);
							
							if (RegionID == 3){
								Utilities.sendWhatsApp("923334566993-1426154967", Message, Pictures);
							}
							
							if (RegionID == 6){
								Utilities.sendWhatsApp("923334566993-1426155109", Message, Pictures);
							}
							
							if (RegionID == 1){
								Utilities.sendWhatsApp("923334566993-1426155302", Message, Pictures);
							}
							
							
							//923334566993-1426155302
							
							
							
							
						}
							
						// Shahrukh CC
						//Utilities.sendWhatsApp("923334566993", Message, Pictures);
						//Thread.currentThread().sleep(2500);
						
						// Omer Khan CC
						
						//Utilities.sendWhatsApp("923458468658", Message, Pictures);
						//Thread.currentThread().sleep(3000);
						
						// Anas CC
						//Utilities.sendWhatsApp("923008406444-1408475526", Message, Pictures);
						//Utilities.sendWhatsApp("923008406444-1408475526", Message, null);
						//Thread.currentThread().sleep(2500);
						
						
						//Utilities.sendWhatsApp("923444471426", Message, Pictures);
						
						
					}catch( java.net.ConnectException e ){
						System.out.println(e);
					}
					
					
					s.executeUpdate("update crm_tasks set is_sent=1, sent_on = now() where id="+TaskIDs[i]);
					ds.commit();
				}
				
				
				
			}
			
			
			obj.put("success", "true");
			
			ds.commit();
			
			s2.close();
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			try {
				
				ds.rollback();
				
				obj.put("success", "false");
				obj.put("error", e.toString());
				e.printStackTrace();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
