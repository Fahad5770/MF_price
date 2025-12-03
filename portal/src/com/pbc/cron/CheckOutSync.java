package com.pbc.cron;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.pushmail.AttendanceSyncDataPDF;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class CheckOutSync {
	public static final String filename = Utilities.getEmailAttachmentsPath()+ "/Attendance_Sync_Data_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static void main(String[] args) {
	//	int DayOfMonth =Utilities.getDayNumberByDate(new Date());
		int DayOfMonth =1;
	//	int DayOfMonth =33;
		//Date StartDate = Utilities.getDateByDays(new Date(), -1);
		Date StartDateToShow = Utilities.getStartDateByDate(Utilities.getDateByDays(new Date(), -DayOfMonth));
		Date StartDate = StartDateToShow;
		
		Date EndDate = Utilities.getDateByDays(new Date(), 0);
		
		
		
		//StartDate = Utilities.parseDate("01/11/2022");
		
		//EndDate = Utilities.parseDate("07/11/2022");
		int ManualFlag=0;  
		Date CurrentDate = StartDate;
		
		//String EmployeeIDs="0204211291,0204211283,0204211300,0204211299,0204211298";
		
		String EmployeeIDs=""; //for regular flow
		
		while(true){
			
			System.out.println(Utilities.getSQLDate(StartDate)+" - "+Utilities.getSQLDate(EndDate)+" - "+Utilities.getSQLDate(CurrentDate));
			
			PopulateData(CurrentDate,EmployeeIDs,ManualFlag, StartDateToShow);
			if(DateUtils.isSameDay(CurrentDate, EndDate)){
				break;
			}

			CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
		}
		
		
		//Date StartDate = Utilities.getDateByDays(new Date(), 0); //run evey month end on same day
		////////////Date StartDate = Utilities.getDateByDays(new Date(), -1); //regular cron job run
		///////PopulateData(StartDate);
		
	}
	
	public static void PopulateData(Date StartDate, String EmployeeID, int ManualFlag, Date StartDateToShow) {

		// TODO Auto-generated method stub
		//Date d = new Date();
		//Date EndDate= new Date();
		//Date StartDate = new Date();
		 //StartDate = Utilities.getDateByDays(StartDate, -2);
		 
		//boolean flag=true;
		
	long PrimaryID=7000001;
		
			 Datasource ds = new Datasource();
			 try {
				 ds.createConnection();
					
					ds.startTransaction();
					
					Statement s = ds.createStatement();
					Statement s2 = ds.createStatement();
					Statement s3 = ds.createStatement();
					Statement s4 = ds.createStatement();
					Statement s5 = ds.createStatement();
					Statement s6 = ds.createStatement();
					 int cFlag=0;
					 
					 String WhereEmpID="";
					 String ShowEmpID=" all employees ";
					 if(!EmployeeID.equals("")) {
						 WhereEmpID=" and empid in ("+EmployeeID+")";
						 ShowEmpID = " Employee ID: "+EmployeeID;
					 }
					 
					/*while(flag) {
						
						if (StartDate.compareTo(EndDate) == 0) {
							flag = false;
							//System.out.println("cSDAte "+cSDAte+" d "+d1);
						}*/
					 //ResultSet rs = s.executeQuery("SELECT distinct empid FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+WhereEmpID);	
					 //System.out.println("SELECT distinct empid FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+WhereEmpID);
					 ResultSet rs = s.executeQuery("SELECT distinct empid FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+WhereEmpID);
					 while(rs.next()) {
						 cFlag++;
						 
						 ResultSet rs45 = s3.executeQuery("select max(id) from employee_integration_attendance_mview");
							if(rs45.first()) {
								PrimaryID=rs45.getLong(1);
								if(PrimaryID==0) {PrimaryID=7000001;}
							}
						 
		
						ResultSet rsCheckIn = s2.executeQuery("SELECT min(Clockin) Clockin,empid,StatusID,TDate,PayrollID FROM pep.integration_attendance where clockin between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+" and Empid="+rs.getInt("empid")+" and StatusID=1");
						if(rsCheckIn.first()) {
							
							PrimaryID++;
							if(rsCheckIn.getString("Clockin")!=null) {
								ResultSet rsExist = s6.executeQuery("SELECT created_on FROM pep.employee_integration_attendance_mview where empid ="+rsCheckIn.getInt("empid")+" and statusid = "+rsCheckIn.getInt("StatusID")+" and tdate ='"+rsCheckIn.getTimestamp("TDate")+"' and clockin = '"+rsCheckIn.getString("Clockin")+"'");
								if(rsExist.first()) {
									String CreatedOn = rsExist.getString(1);
										s4.executeUpdate("delete from pep.employee_integration_attendance_mview where empid ="+rsCheckIn.getInt("empid")+" and statusid = "+rsCheckIn.getInt("StatusID")+"  and clockin = '"+rsCheckIn.getString("Clockin")+"'");//and tdate ='"+rsCheckIn.getTimestamp("TDate")+"'
										s5.executeUpdate("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckIn.getInt("empid")+","+rsCheckIn.getInt("StatusID")+",'"+rsCheckIn.getTimestamp("TDate")+"','"+rsCheckIn.getString("Clockin")+"','"+rsCheckIn.getString("PayrollID")+"','"+CreatedOn+"')");
								}else {
									s5.executeUpdate("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckIn.getInt("empid")+","+rsCheckIn.getInt("StatusID")+",'"+rsCheckIn.getTimestamp("TDate")+"','"+rsCheckIn.getString("Clockin")+"','"+rsCheckIn.getString("PayrollID")+"',now())");
								}
							}
						}
						
		
						ResultSet rsCheckOut = s2.executeQuery("SELECT max(Clockin) Clockin,empid,StatusID,TDate,PayrollID FROM pep.integration_attendance where clockin between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+" and Empid="+rs.getInt("empid")+" and StatusID=2");
						if(rsCheckOut.first()) {
							
							PrimaryID++;
							if(rsCheckOut.getString("Clockin")!=null) {
								ResultSet rsExist1 = s6.executeQuery("SELECT created_on FROM pep.employee_integration_attendance_mview where empid ="+rsCheckOut.getInt("empid")+" and statusid = "+rsCheckOut.getInt("StatusID")+" and tdate ='"+rsCheckOut.getTimestamp("TDate")+"' and clockin = '"+rsCheckOut.getString("Clockin")+"'");
								if(rsExist1.first()) {
									String CreatedOn = rsExist1.getString(1);
											s4.executeUpdate("delete from pep.employee_integration_attendance_mview where empid ="+rsCheckOut.getInt("empid")+" and statusid = "+rsCheckOut.getInt("StatusID")+" and clockin = '"+rsCheckOut.getString("Clockin")+"'");//and tdate ='"+rsCheckOut.getTimestamp("TDate")+"' 
											s5.executeUpdate("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckOut.getInt("empid")+","+rsCheckOut.getInt("StatusID")+",'"+rsCheckOut.getTimestamp("TDate")+"','"+rsCheckOut.getString("Clockin")+"','"+rsCheckOut.getString("PayrollID")+"','"+CreatedOn+"')");
								}else {
									s5.executeUpdate("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckOut.getInt("empid")+","+rsCheckOut.getInt("StatusID")+",'"+rsCheckOut.getTimestamp("TDate")+"','"+rsCheckOut.getString("Clockin")+"','"+rsCheckOut.getString("PayrollID")+"',now())");
								}
							}
						}
					 }
					 
					
					 try {
						
		
						if(ManualFlag==0) {
							Date CurDate=Utilities.getDateByDays(new Date(), 0);
							if(StartDate == CurDate) {
								AttendanceSyncDataPDF rep = new com.pbc.pushmail.AttendanceSyncDataPDF();
								rep.createPdf(filename,StartDate,EmployeeID);
								Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"rizwan.jamil@thalindustries.com"}, new String[]{"awais.ali@kale-labs.com"},null, "Attendance Sync Run Successfully", "Attendance Sync Run Successfully from "+Utilities.getDisplayFullDateFormat(StartDateToShow)+" till today and for" +ShowEmpID, new String[]{"Attendance_Sync_Data_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
							}
						}
						} catch (Exception e){
							e.printStackTrace();
							
						}
					 System.out.println("cFlag"+cFlag);
					ds.commit();
					s.close();
					s2.close();
					s3.close();
					s4.close();
					s5.close();
			 }catch(Exception e){
					try {
						ds.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(e);
					try {
							if(ManualFlag==0) {
							//	Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"rizwan.jamil@thalindustries.com"}, new String[]{"awais.ali@kale-labs.com"},null, "Attendance Sync Run Failed", "Attendance Sync Run Failed with error: "+e, null);
							}
						} catch (Exception e1){
							e1.printStackTrace();
						}
				}finally {
					try {
						ds.dropConnection();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			 
			
			 
			
	
	}

}
