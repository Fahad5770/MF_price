package com.pbc.pushmail;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class CRMHelpDeskPendingRequestSendEmail {
	static Datasource ds = new Datasource();
	public static void main(String[] args) {
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1=ds.createStatement();
			
			
			Date date=new Date();//current Date
			//date= Utilities.parseDate("05/06/2017");
			
			// Duration Days for sending in mail
			String Threedays="";
			String ToPersonFor3="zahoor@pbc.com.pk";
			
			//////////////String ToPersonFor3="ali.farhan@pbc.com.pk";
			
			
			
			//For Getting Three Days Older Date
			Date ThreeDaysOlderDate=(Utilities.getDateByDays(date,-3));//Three days Previous Date
			//System.out.println((Utilities.getDayOfWeekByDate(ThreeDaysOlderDate)));
			Threedays="Three";
			if(Utilities.getDayOfWeekByDate(ThreeDaysOlderDate)==1){//checking if whether the day is Sunday
				 ThreeDaysOlderDate=(Utilities.getDateByDays(ThreeDaysOlderDate,-1));//if true then add one more day
				 Threedays="Four";
			//System.out.println("Its Sunday 3 days date");
			}
			

			String threeDayPrintDate=(new SimpleDateFormat("dd/MM/yyyy").format(ThreeDaysOlderDate));
	        String FromDateThreeDayOlder=Utilities.getSQLDate((ThreeDaysOlderDate));//Converting into SQL Format
	        //System.out.println(FromDateThreeDayOlder);
	        String ToDateThreeDayOlder=Utilities.getSQLDateNext((ThreeDaysOlderDate));//Converting into SQL Format and getting one Day ahead date
			//System.out.println("This is To Date "+ToDateThreeDayOlder);
			
			
			//For Getting 6 Days Older Date
			String sixdays;
			String ToPersonFor6="salman.baig@pbc.com.pk";
			
			////////////////String ToPersonFor6="ali.farhan@pbc.com.pk";
			
			
	        Date SixDaysOlderDate=(Utilities.getDateByDays(date,-6));//Six days Previous Date
	        sixdays="Six";
			if(Utilities.getDayOfWeekByDate(SixDaysOlderDate)==1){//checking if whether the day is Sunday
				SixDaysOlderDate=(Utilities.getDateByDays(SixDaysOlderDate,-1));//if true then add one more day
				sixdays="Seven";
				//System.out.println("Its Sunday 6 days date");
			}

			String SixDayPrintDate=(new SimpleDateFormat("dd/MM/yyyy").format(SixDaysOlderDate));
	        String FromDateSixDayOlder=Utilities.getSQLDate((SixDaysOlderDate));//Converting into SQL Format
	        //System.out.println(FromDateSixDayOlder);
	        String ToDateSixDayOlder=Utilities.getSQLDateNext((SixDaysOlderDate));//Converting into SQL Format and getting one Day ahead date
			
			
			//sends three days pending mails
			String HTMLEmailThreeDay= getCRMHelpDeskComplaintHTML(FromDateThreeDayOlder,ToDateThreeDayOlder,ThreeDaysOlderDate,Threedays,threeDayPrintDate,ToPersonFor3);//For Three Days Previous Complaints
			
			//sends  six days pending mails 
			String HTMLEmailSixDay = getCRMHelpDeskComplaintHTML(FromDateSixDayOlder,ToDateSixDayOlder,SixDaysOlderDate,sixdays,SixDayPrintDate,ToPersonFor6);//For Six Days Previous Complaints
			 
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
				e.printStackTrace();
			}			
			finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		

		
	}
																																																			
	public static String getCRMHelpDeskComplaintHTML(String fromdate,String todate,Date date,String days,String PrintDate,String person) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			

			//-----------------------------------Starts-----------------------------------------------
			

			
			
			ds.createConnection();
			//ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1=ds.createStatement();
			
			
	        
			
	       
			
			//Html body starts here
			String HTMLMessage = "";
			
			
						
			String query1="select id from crm_help_desk_complaint_category ";
			//created_on between "+FromDate+" and "+ToDate+" and is_resolved=0";
			ResultSet rs = s.executeQuery(query1);
		    while(rs.next())
		    	
		    {
		    	
	    	 	html = "<html>";
				html += "<body>"+"<br>";
				
		    	int CategoryID=rs.getInt("id");
		    	//System.out.println(CategoryID);
		    	int count = 0;
		    	int DateAndDurationRowFlag=0;
		    	
		    	String query2="SELECT id,category_id,(select label from crm_help_desk_complaint_category chdcc where chdcc.id=category_id) category_label,sub_category_id,(select label from crm_help_desk_complaint_sub_category chdcsc where chdcsc.id=sub_category_id) Sub_category_label,outlet_id,(select name from common_outlets co where co.id=outlet_id) outlet_name,created_by,(select DISPLAY_NAME from users us where us.id=created_by) user_name,created_on,asset_code,tot_code,name,contact_person,description,created_on FROM pep.crm_help_desk_complaint where category_id="+CategoryID+" and created_on between "+fromdate+" and "+todate+" and is_resolved=0 order by category_id asc";
		    	//if(CategoryID==3 || CategoryID==4){System.out.println(query2);}
		    	ResultSet rs1 = s1.executeQuery(query2);
			     while(rs1.next()){
		    	
						 
				    	 //--------------collected Data to be send in mail-------------------------
				    	
			    	 int ComplaintNumber=rs1.getInt("id");
			    	 long OutletID=rs1.getLong("outlet_id");
			    	
			    	 //System.out.println(OutletID);
			    	 String OutletName=rs1.getString("outlet_name");
			    	 if(OutletName!=null ){
			    		 OutletName="-"+rs1.getString("outlet_name");
			    	 }
			    	 else{
			    		 OutletName="";
			    	 }
			    	 String OutletDisplay=OutletID+OutletName;
			    	 String AssetCode=rs1.getString("asset_code");
			    	 String TOTcode=rs1.getString("tot_code");
			    	 String Name=rs1.getString("name");
			    	 String ContactNumber=rs1.getString("contact_person");
			    	 int Department=rs1.getInt("category_id");
			    	 //System.out.println(Department);
			    	 String DepartmentName=rs1.getString("category_label");
			    	 String TypeName=rs1.getString("Sub_category_label"); 
			    	 String RegisterBy=rs1.getInt("created_by")+"-"+rs1.getString("user_name");
			    	 //System.out.println(RegisterBy);
			    	 String Descripton=rs1.getString("description");
			    	 String RegisteredOn=rs1.getString("created_on");
			    	 //Date date=new Date();//current Date	
			    	 //String Date = PrintDate.toString().substring(0, 10);
			    	 String DayDigit="";
			    	 if(days.equals("Three")){
			    		 DayDigit="3";
			    	 }
			    	 if(days.equals("Four")){
			    		 DayDigit="4";
			    	 }
			    	 if(days.equals("Six")){
			    		 DayDigit="6";
			    	 }
			    	 if(days.equals("Seven")){
			    		 DayDigit="7";
			    	 }
			    	 
			    	 
							html += "<table style='width: 450px;'>";
							
							if(DateAndDurationRowFlag==0){
							//date row
							html += "<tr>";
							html += "<th colspan='2' style='height: 18px; text-align: left;'><b>Registered On : </b> "+Utilities.getDisplayFullDateFormat(date)+" -  "+days+"("+DayDigit+") Days Ago </b></th>";
							//html += "<td  style=' background-color: #DFDFDF; color: Black;height: 19px; text-align: left;'>"+PrintDate+"</td>";
							
							html += "</tr>";
							//how many days Older
							html += "<tr>";
							html += "<th  style=' height: 18px; text-align: left;'> </th>";
							//html += "<td  style=' height: 19px; text-align: left;'> "+days+" Days Older Mails</td>";
							
							html += "</tr>";
							}
							//Main heading  Complaint No
							if(OutletID==0 ){
								html += "<tr>";
								
								html += "<td colspan='2' style=' background-color: #3D5AB3; color: white;height: 18px; text-align: left;'></td>";
								html += "</tr>";
							}else if(OutletID!=0){
								html += "<tr>";
								//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Outlet Name</td>";
								html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: left;font-weight: bold;'>"+OutletDisplay+"</td>";
								html += "</tr>";	
							}
							
							//sub heading of Outlet Name * Complaint number *
							html += "<tr>";
							html += "<td style='background-color: #EDEFF2;height: 18px; text-align: left;font-weight: bold;'>Complaint</td>";
							html += "<td style='height: 18px; text-align: left;'>"+ComplaintNumber+"</td>";
							html += "</tr>";
							//sub heading of Outlet Name * Asset Code *
							
							if(AssetCode=="" || AssetCode ==null){
								html += "<tr>";
								html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Asset Code</td>";
								html += "<td style=' height: 18px; text-align: left; '></td>";
								html += "</tr>";
							
							}else if(AssetCode!=""){
								html += "<tr>";
								html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Asset Code</td>";
								html += "<td style=' height: 18px; text-align: left; '>"+AssetCode+"</td>";
								html += "</tr>";	
							}
							
							
							//sub heading of Outlet Name * TOT Code*
							//html += "<tr>";
							//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>TOT Code</td>";
							//html += "<td style=' height: 18px; text-align: left; '>"+TOTCode+"</td>";
							//html += "</tr>";
								
								
							
							
							//sub heading of Outlet Name * Name * | * Contact Number *
							html += "<tr>";
							html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Name | Contact </td>";
							html += "<td style=' height: 18px; text-align: left; '>"+Name+" | "+ContactNumber+" </td>";
							html += "</tr>";
							
							//sub heading of Outlet Name * Contact Number *
							//html += "<tr>";
							//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Contact #</td>";
							//html += "<td style=' height: 18px; text-align: left; '></td>";
							//html += "</tr>";
							
							
							//sub heading of Outlet Name * Department *
							//html += "<tr>";
							//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Department</td>";
							//html += "<td style=' height: 18px; text-align: left; '>"+DepartmentName+"</td>";
							//html += "</tr>";
							
							
							//sub heading of Outlet Name * Type *
							html += "<tr>";
							html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Type</td>";
							html += "<td style=' height: 18px; text-align: left; '>"+TypeName+"</td>";
							html += "</tr>";
							
							
							//Main heading Complaint Description
							html += "<tr>";
							html += "<td  style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Description</td>";
							html += "<td  style='height: 18px; text-align: left;'>"+Descripton+"</td>";
							html += "</tr>";
							
							
							
							//sub heading of Complaint Description *Registered By*
							html += "<tr>";
							html += "<td style='background-color: #EDEFF2;width:30%;height: 18px; text-align: left;font-weight: bold;'>Registered By</td>";
							html += "<td style='height: 18px; text-align: left;'>"+RegisterBy+"</td>";
							html += "</tr>";
							
							
							//html += "<tr>";
							//html += "<td style='background-color: #EDEFF2;height: 18px; text-align: left;font-weight: bold;'>Registered On</td>";
							//html += "<td style='height: 18px; text-align: left;'>"+Date+"</td>";
							//html += "</tr>";
							
							html += "</table>";
							
							html += "<br>";
							count++;
							DateAndDurationRowFlag++;
				
			     }
			     html += "</body>";

			     html += "</html>";
			     //System.out.println(Utilities.getDisplayDateFormat(Utilities.parseDate("01/06/2017"))); 
			    if(count>=1){
				     if(CategoryID==1 ){
			    		 //Sending mail	"ali.farhan@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"
					     Utilities.sendPBCHTMLEmail(new String[]{person}, new String[]{"anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Pending Requests | Sales Services | "+PrintDate, html, null);
					  }
			    	
			    	//for sending mails to Quality Department
			    	 else if(CategoryID==2){
			    		 //Sending mail
			    		 Utilities.sendPBCHTMLEmail(new String[]{person}, new String[]{"anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Pending Requests | Quality | "+PrintDate, html, null);
					     		 
			    	 }
					 //for sending mails to MEM Department
			    	 else if(CategoryID==3){
			    		 //Sending mail	    
			    		 Utilities.sendPBCHTMLEmail(new String[]{person}, new String[]{"anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Pending Requests | MEM | "+PrintDate, html, null);
					     
			    	 }
			    	 //for sending mails to Other Department
			    	 else if(CategoryID==4){
			    		 //Sending mail	    
			    		 Utilities.sendPBCHTMLEmail(new String[]{person}, new String[]{"anas.wahab@pbc.com.pk"}, new String[]{"zulqurnan.aslam@pbc.com.pk"}, "CRM Help Desk | Pending Requests | Other | "+PrintDate, html, null);
					     
			    	 }
			    	
				     
			    }
		     }
		    
		     
			//-----------------------------------ENDs-------------------------------------------------
		    
			
			s.close();
			s1.close();
		
		} catch (Exception e) {
			

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
