package com.pbc.distributor;

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


@WebServlet(description = "Distributor Target Execute", urlPatterns = { "/distributor/DistributorTargetExecute" })
public class DistributorTargetExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorTargetExecute() {
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
			isEditCase = true;
		}
		
		long DistributorID =Utilities.parseLong(request.getParameter("DistributorID"));
		int Month = Utilities.parseInt(request.getParameter("Month"));
		int Year = Utilities.parseInt(request.getParameter("Year"));
		Date StartDate=Utilities.parseDate(request.getParameter("StartDate"));
		Date EndDate=Utilities.parseDate(request.getParameter("EndDate"));
		int TargetType = Utilities.parseInt(request.getParameter("TargetType"));
		
		int PackageID[] = Utilities.parseInt(request.getParameterValues("package_id"));
		int Quantity[] = Utilities.parseInt(request.getParameterValues("qty"));
		
		int DistributorTargetID = 0;
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			String SQL = "";
			
			if(isEditCase){
				SQL = "UPDATE distributor_targets SET `distributor_id` = "+DistributorID+",`month` = "+Month+",`year` = "+Year+",`start_date` = "+Utilities.getSQLDate(StartDate)+",`end_date` = "+Utilities.getSQLDate(EndDate)+",`created_by` = "+UserID+",`created_on` = now(), type_id="+TargetType+" WHERE `id` = "+EditID;
			}else{
				SQL = "INSERT INTO `distributor_targets`(`distributor_id`,`month`,`year`,`start_date`,`end_date`,`created_by`,`created_on`, `type_id`)VALUES("+DistributorID+","+Month+","+Year+","+ Utilities.getSQLDate(StartDate) +","+ Utilities.getSQLDate(EndDate) +","+UserID+",now(), "+TargetType+")";
			}
			
			s.executeUpdate(SQL);
			
			if(!isEditCase){
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					DistributorTargetID = rs.getInt(1);
				}
			}else{
				DistributorTargetID = EditID;
			}
			
			s.executeUpdate("delete from distributor_targets_packages where id="+DistributorTargetID);
			
			
			s.executeUpdate("delete from distributor_targets_packages_brands where id="+DistributorTargetID);
			
			for(int i = 0; i < PackageID.length; i++){
				
				if(Quantity[i] > 0){
					
					s.executeUpdate("INSERT INTO `distributor_targets_packages`(`id`,`package_id`,`quantity`)VALUES("+DistributorTargetID+","+PackageID[i]+","+Quantity[i]+")");
				
					int BrandID[] = Utilities.parseInt(request.getParameterValues("brand_id_"+PackageID[i]));
					int BrandQuantity[] = Utilities.parseInt(request.getParameterValues("qty_"+PackageID[i]));
					
					
					for(int j = 0; j < BrandID.length; j++ ){
						
						if(BrandQuantity[j] > 0){
							
							s.executeUpdate("INSERT INTO `distributor_targets_packages_brands`(`id`,`package_id`,`brand_id`,`quantity`)VALUES("+DistributorTargetID+","+PackageID[i]+","+BrandID[j]+","+BrandQuantity[j]+")");
						}
					}
				}
			}
			
			
			//Sending notification email 
			int Notif = Utilities.parseInt(request.getParameter("SendEmailNoti"));
			
			
			if(isEditCase && Notif==1){
				//System.out.println("hello9");
				String DistributorNameSubject="";
				String SNDEmail = "";
				ResultSet rs = s.executeQuery("select cd.name, cd.snd_id, (SELECT email FROM users where id = cd.snd_id) email from common_distributors cd where cd.distributor_id="+DistributorID);
				if(rs.first()){
					DistributorNameSubject = DistributorID+" - "+rs.getString("name");
					SNDEmail = rs.getString("email");
				}
				
				//EmployeeHierarchy.
				Utilities.sendPBCHTMLEmail(new String[]{SNDEmail,"salman.baig@pbc.com.pk"}, new String[]{"obaid@pbc.com.pk"}, null, "Change Notification | Distributor Target | "+DistributorNameSubject, getHTMLMessage(DistributorTargetID) , null);
				
			}
			
			obj.put("success", "true");
			
			ds.commit();
			s.close();
			
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
	
	public String getHTMLMessage(long TargetID){
		String html = "";
		Datasource ds = new Datasource();
		
		try {

			ds.createConnection();
			

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			
			
			String HTMLMessage = "";
			

			int MasterSerialID = 0;
			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 550px;'>";
			
			
			
			
			
			String Distributor="";
			int Year=0;
			int Month=0;
			Date StartDate=null;
			Date EndDate=null;
			
			ResultSet rs = s.executeQuery("SELECT *,(select name from common_distributors cd where cd.distributor_id=dt.distributor_id) dist_name FROM distributor_targets dt where dt.id="+TargetID);
			while(rs.next()){
				
				Distributor = rs.getLong("distributor_id")+" - "+rs.getString("dist_name");
				Year = rs.getInt("year");
				Month = rs.getInt("month");
				StartDate = rs.getDate("start_date");
				EndDate = rs.getDate("end_date");
			}
			
			
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>"+Distributor+"</td>";
			html += "</tr>";
			
			html +="<tr>";
				html +="<td style='background-color: #EDEFF2;'>Month</td>";
				html +="<td style='background-color: #FCFFE6;'>"+Utilities.getMonthNameByNumber(Month)+"</td>";
			html +="</tr>";
		
			html +="<tr>";
				html +="<td style='background-color: #EDEFF2;'>Year</td>";
				html +="<td style='background-color: #FCFFE6;'>"+Year+"</td>";
			html +="</tr>";
	
			html +="<tr>";
				html +="<td style='background-color: #EDEFF2;'>Target Date</td>";
				html +="<td style='background-color: #FCFFE6;'>"+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate)+"</td>";
			html +="</tr>";
			
			html +="</table>";
			
			
			html +="<br/><br/>";
			
			html += "<table style='width: 400px;'>";
			
			
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Packages</td>";
			html += "</tr>";
			
			
			
			//System.out.println("SELECT *,(select label from inventory_packages ip where ip.id=dtp.package_id) pkg_name FROM distributor_targets_packages dtp where dtp.id="+TargetID);
			double Total =0;
			
			ResultSet rs1 = s.executeQuery("SELECT *,(select label from inventory_packages ip where ip.id=dtp.package_id) pkg_name,(select unit_per_case from inventory_packages ip where ip.id=dtp.package_id) unit_per_case,(select liquid_in_ml from inventory_packages ip where ip.id=dtp.package_id) liquid_in_ml FROM distributor_targets_packages dtp where dtp.id="+TargetID);
			while(rs1.next()){
				
			Total += 	(rs1.getLong("quantity") * rs1.getLong("unit_per_case") * rs1.getLong("liquid_in_ml"))/6000;
				
				
				
				html +="<tr>";
				html +="<td style='background-color: #EDEFF2; width:60%;'>"+rs1.getString("pkg_name")+"</td>";
				html +="<td style='background-color: #FCFFE6; width:30%; text-align:right;'>"+Utilities.getDisplayCurrencyFormatRounded(rs1.getLong("quantity"))+"</td>";
				html +="</tr>";
				
			}
			
			html +="<tr>";
			html +="<td style='background-color: #EDEFF2; width:60%;'><b>Total</b></td>";
			html +="<td style='background-color: #FCFFE6; width:30%; text-align:right;'>"+Utilities.getDisplayCurrencyFormatRounded(Total)+"</td>";
			html +="</tr>";
			
			
			
			
			html +="</table>";
		
		
		
		
		
	} catch (Exception e){
		e.printStackTrace();
	}
		return html;
}
	
}
