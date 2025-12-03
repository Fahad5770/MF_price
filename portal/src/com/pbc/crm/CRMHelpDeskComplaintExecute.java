package com.pbc.crm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.mysql.jdbc.Util;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;


@WebServlet(description = "Register Help Desk Complaint Execute", urlPatterns = { "/crm/CRMHelpDeskComplaintExecute" })
public class CRMHelpDeskComplaintExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CRMHelpDeskComplaintExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null || UserID.equals("0")){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		//System.out.println(UserID);
		PrintWriter out = response.getWriter();
		
		int CategoryId = Utilities.parseInt(request.getParameter("category"));
		int SubCategoryId = Utilities.parseInt(request.getParameter("subcategory"));
		long OutletID = Utilities.parseLong(request.getParameter("outlet_id"));
		String AssetCode =Utilities.filterString(request.getParameter("Asset_code"), 1, 500);
		String TotCode = Utilities.filterString(request.getParameter("tot_code"), 1, 500);
		//String Others = Utilities.filterString(request.getParameter("others"), 1, 500);
		
		String PersonContactName = Utilities.filterString(request.getParameter("PersonContactName"), 1, 100);
		String ContactNo = Utilities.filterString(request.getParameter("ContactNo"), 1, 100);		
		String Description = Utilities.filterString(request.getParameter("Description"), 1, 1000);	
		long ComplaintNO=0;
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			
			String SQL="";
			//if Asset Code is not entered
			if(AssetCode.equals("")){
				SQL="INSERT INTO crm_help_desk_complaint(category_id,sub_category_id,outlet_id,name,contact_person,description,created_on,created_by,tot_code)VALUES("+CategoryId+", "+SubCategoryId+", "+OutletID+", '"+PersonContactName+"', '"+ContactNo+"', '"+Description+"', now(),"+UserID+",'"+TotCode+"')";
			}
			//if Outlet ID is not entered
			else if(OutletID==0){
				SQL="INSERT INTO crm_help_desk_complaint(category_id,sub_category_id,name,contact_person,description,created_on,created_by,asset_code,tot_code)VALUES("+CategoryId+", "+SubCategoryId+", '"+PersonContactName+"', '"+ContactNo+"', '"+Description+"', now(),"+UserID+",'"+AssetCode+"','"+TotCode+"')";
			
			}
			//if Both Asset code and outlet Id are not empty
			else if(OutletID!=0 && AssetCode!=""){
				SQL ="INSERT INTO crm_help_desk_complaint(category_id,sub_category_id,outlet_id,name,contact_person,description,created_on,created_by,asset_code,tot_code)VALUES("+CategoryId+", "+SubCategoryId+", "+OutletID+", '"+PersonContactName+"', '"+ContactNo+"', '"+Description+"', now(),"+UserID+",'"+AssetCode+"','"+TotCode+"')";
			}
			
			//Get Outlet name
			String OutletName = "";
			ResultSet rs3 = s3.executeQuery("select * from common_outlets where id=" + OutletID);
			while(rs3.next()) {
				OutletName=rs3.getString("name");
			
			}
			//System.out.println(SQL);
			s.executeUpdate(SQL);
			
			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
		     if( rs.first() ){
		    	 ComplaintNO = rs.getLong(1);
		     }
			
		     	ResultSet rs4=s4.executeQuery("select DATE_FORMAT(created_on,'%b %d %Y') as created_on  from crm_help_desk_complaint where id = " + ComplaintNO);
				String RegisteredDate="";
				while(rs4.next()){
					RegisteredDate=(rs4.getString("created_on"));	
					//System.out.println("This is date "+RegisteredDate);
					
				}
			
		     if(CategoryId==1 || CategoryId==4 ){ //For sending mails to Sales & services/ others department
		     //Sending mail
		    	String HTMLEmail = getCRMHelpDeskComplaintHTML(ComplaintNO,UserID,OutletID,AssetCode,TotCode,PersonContactName,ContactNo,CategoryId,SubCategoryId,Description,RegisteredDate);
		        Utilities.sendPBCHTMLEmail(new String[]{"aamir.zafar@pbc.com.pk"},new String[]{"jazeb@pbc.com.pk","anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Request# "+ComplaintNO, HTMLEmail, null);
		     }else if(CategoryId==2){//For sending mails to Quality department
		    	//Sending mail
			     String HTMLEmail = getCRMHelpDeskComplaintHTML(ComplaintNO,UserID,OutletID,AssetCode,TotCode,PersonContactName,ContactNo,CategoryId,SubCategoryId,Description,RegisteredDate);
			     Utilities.sendPBCHTMLEmail(new String[]{"qalab@pbc.com.pk"},new String[]{"baber.masood@pbc.com.pk","anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Request# "+ComplaintNO, HTMLEmail, null);
			    
		     }else if(CategoryId==3){//For sending mails to MEM department
		    	//Sending mail
			     String HTMLEmail = getCRMHelpDeskComplaintHTML(ComplaintNO,UserID,OutletID,AssetCode,TotCode,PersonContactName,ContactNo,CategoryId,SubCategoryId,Description,RegisteredDate);
			     Utilities.sendPBCHTMLEmail(new String[]{"tot1@pbc.com.pk"},new String[]{"ateeq@pbc.com.pk","khalid.waseem@pbc.com.pk","anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Request# "+ComplaintNO, HTMLEmail, null);
			 }		   
			obj.put("success", "true");
			obj.put("ComplaintNO", ComplaintNO);
			
			ds.commit();
			
			
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
	

	
public static String getCRMHelpDeskComplaintHTML(long ComplaintNumber,String RegisterBy,long outletId,String AssetCode,String TOTCode,String PersonName,String ContactNum,int Department,int Type,String Description,String Date) {

	Datasource ds = new Datasource();

	String html = "";
	try {

		ds.createConnection();
		ds.startTransaction();

		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		Statement s4 = ds.createStatement();
		
		//Date date = new Date();
		
		//System.out.println(ComplaintNumber);
		//System.out.println(RegisterBy);
		//System.out.println(outletId);
		//System.out.println(AssetCode);
		//System.out.println(TOTCode);
		

		
		String OutletName = "";
		ResultSet rs = s.executeQuery("select * from common_outlets where id=" + outletId);
		while(rs.next()) {
			OutletName=rs.getString("name");
		
		}
		
		String DepartmentName = "";
		ResultSet rs1 = s1.executeQuery("select * from crm_help_desk_complaint_category where id = " + Department);
		while (rs1.next()) {
			DepartmentName = rs1.getString("label");
		
		}

		String TypeName = "";
		ResultSet rs2 = s2.executeQuery("select * from crm_help_desk_complaint_sub_category where id = " + Type);
		while (rs2.next()) {
			TypeName = rs2.getString("label");
		
		}
		
		String userName = "";
		ResultSet rs3 = s3.executeQuery("select * from users where id = " + RegisterBy);
		while (rs3.next()) {
			userName = rs3.getString("DISPLAY_NAME");
		
		}
		
		
		

		String HTMLMessage = "";
		//String display="if(outlet==0){display:none}";
		
		html = "<html>";
		html += "<body><br>";

		html += "<table style='width: 400px;'>";
		
		//Main heading  Complaint No
		//html += "<tr>";
		//html += "<td  colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Complaint #"+ComplaintNumber+"</td>";
		
		
		
		//sub heading of complaint number * Outlet Name *
		//html += "</tr>";
	
		
		
		if(outletId==0){
			html += "<tr>";
			
			html += "<td colspan='2' style=' background-color: #3D5AB3; color: white;height: 18px; text-align: left;'></td>";
			html += "</tr>";
		}else if(outletId!=0){
			html += "<tr>";
			//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Outlet Name</td>";
			html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: left;font-weight: bold;'>"+outletId+"-"+OutletName+"</td>";
			html += "</tr>";	
		}
		
		//sub heading of complaint number * Asset Code *
		
		if(AssetCode.equals("")){
			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Asset Code</td>";
			html += "<td style=' height: 18px; text-align: left; '></td>";
			html += "</tr>";
		
		}else if(!AssetCode.equals("")){
			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Asset Code</td>";
			html += "<td style=' height: 18px; text-align: left; '>"+AssetCode+"</td>";
			html += "</tr>";	
		}
		
		//sub heading of complaint number * TOT Code*
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>TOT Code</td>";
		html += "<td style=' height: 18px; text-align: left; '>"+TOTCode+"</td>";
		html += "</tr>";
				
			
		
		
		//sub heading of complaint number * Name *
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Name</td>";
		html += "<td style=' height: 18px; text-align: left; '>"+PersonName+"</td>";
		html += "</tr>";
		
		//sub heading of complaint number * Contact Number *
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Contact #</td>";
		html += "<td style=' height: 18px; text-align: left; '>"+ContactNum+"</td>";
		html += "</tr>";
		
		//sub heading of complaint number * Department *
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Department</td>";
		html += "<td style=' height: 18px; text-align: left; '>"+DepartmentName+"</td>";
		html += "</tr>";
		
		//sub heading of complaint number * Type *
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Type</td>";
		html += "<td style=' height: 18px; text-align: left; '>"+TypeName+"</td>";
		html += "</tr>";
		
		
		//Main heading Complaint Description
		html += "<tr>";
		html += "<td  colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Description</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<td colspan='2' style='height: 18px; text-align: left;'>"+Description+"</td>";
		html += "</tr>";
		html += "<tr>";
		html += "</tr>";
		html += "<tr>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2;width:30%;height: 18px; text-align: left;font-weight: bold;'>Registered By</td>";
		html += "<td style='height: 18px; text-align: left;'>"+RegisterBy+" - "+userName+"</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2;height: 18px; text-align: left;font-weight: bold;'>Registered On</td>";
		html += "<td style='height: 18px; text-align: left;'>"+Date+"</td>";
		html += "</tr>";
		
		/* 
		html += "</tr>";

		String CustomerName = "";
		double CreditLimit = 0;
		Date ExpiryDate = null;
		long CustomerID = 0;

		ResultSet rs6 = s1.executeQuery(
				"SELECT *,(select name from common_distributors cd where cd.distributor_id=customer_id) customer_name FROM gl_customer_credit_limit_request where request_id="
						+ RequestIDVal);
		while (rs6.next()) {

			CustomerName = rs6.getLong("customer_id") + " - " + rs6.getString("customer_name");
			CreditLimit = rs6.getDouble("credit_limit");
			CustomerID = rs6.getLong("customer_id");
		}

		html += "<td style='background-color: #EDEFF2;'>Syed Ali FARHAN";// + CustomerName;
		// html += "<td valign='top' style='background-color: #EDEFF2;
		// text-align: left;
		// '>"+Utilities.getDisplayCurrencyFormat(CreditLimit);

		html += "</tr>";
		html += "<tr>";
		// html += "<td style='background-color: #EDEFF2; height: 18px;
		// text-align: left; font-weight: bold;'>Customer</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit</td>";
		html += "</tr>";

		html += "<tr>";
		html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>10000";
		html += "</tr>";
		html += "<tr>";
		// html += "<td style='background-color: #EDEFF2; height: 18px;
		// text-align: left; font-weight: bold;'>Customer</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Current Balance</td>";
		html += "</tr>";

		html += "<tr>";
		html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>2381";
		html += "</tr>";

		html += "<tr>";
		html += "<td colspan='4'>&nbsp;";
		html += "</tr>";

		html += "</table>";

		html += "<table style='width: 400px;'>";

		String ValidToDateString = "";
		String ValidToTimeString = "";

		ResultSet rs3 = s.executeQuery(
				"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid from gl_customer_credit_limit_request where request_id=2381");
		while (rs3.next()) {
			ValidToDateString = rs3.getString("sent_date_valid");

			html += "<tr><td>&nbsp;</td></tr>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>No reasons</td>";

			html += "</tr>";

		}

		html += "</table>";
		html += "<br>";
		html += "<table style='width: 400px;'>";
		html += "<tr>";
		html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
		html += "</tr>";
		html += "</table>";
		
		ResultSet rs4 = s.executeQuery(
				"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
						+ RequestIDVal);
		while (rs4.next()) {

			html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
					+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

		}

		html += "<br>";
		*/
				
		html += "</body>";

		html += "</html>";

	} catch (Exception e) {
		try {
			ds.rollback();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		e.printStackTrace();
	} finally {
		try {
			ds.dropConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	return html;

}

}
