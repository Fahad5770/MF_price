package com.pbc.myscripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class CheckOutSyncCustome {

	public static void main(String[] args) {
		
		//Date StartDate = Utilities.getDateByDays(new Date(), -47);
		
		//Date EndDate = Utilities.getDateByDays(new Date(), -1);
		
		
		Date StartDate =Utilities.parseDate("01/09/2021");
		
		Date EndDate = Utilities.parseDate("01/10/2021");
		
		//int EmpID[]= {5017,9514,9516,9515,2300};
		
		
		
		///for(int i=0;i<EmpID.length;i++) {
			Date CurrentDate = StartDate;
			while(true){
				
				System.out.println(Utilities.getSQLDate(StartDate)+" - "+Utilities.getSQLDate(EndDate)+" - "+Utilities.getSQLDate(CurrentDate));
				
				PopulateData(CurrentDate,0);
				if(DateUtils.isSameDay(CurrentDate, EndDate)){
					break;
				}
	
				CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
			}
		///}
		
		
		
	}
	
	public static void PopulateData(Date StartDate, int EmpID) {

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
					 int c=0;
					/*while(flag) {
						
						if (StartDate.compareTo(EndDate) == 0) {
							flag = false;
							//System.out.println("cSDAte "+cSDAte+" d "+d1);
						}*/
						
					 String WhereUser="";
					 if(EmpID!=0) {//mean specific user
						 WhereUser = " and empid="+EmpID;
					 }
					 System.out.println("delete from employee_integration_attendance_mview where 1=1 "+WhereUser+" and tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate));
					 s.executeUpdate("delete from employee_integration_attendance_mview where 1=1 "+WhereUser+" and tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate));
					 
					 System.out.println("SELECT distinct empid FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+WhereUser);
					 ResultSet rs = s.executeQuery("SELECT distinct empid FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(StartDate)+WhereUser);
					 while(rs.next()) {
						
						 
						 ResultSet rs45 = s3.executeQuery("select max(id) from employee_integration_attendance_mview");
							if(rs45.first()) {
								PrimaryID=rs45.getLong(1);
								if(PrimaryID==0) {PrimaryID=7000001;}
							}
						 
						System.out.println("SELECT min(Clockin),empid,StatusID,TDate,PayrollID FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(StartDate)+" and Empid="+rs.getInt("empid")+" and StatusID=1");
						ResultSet rsCheckIn = s2.executeQuery("SELECT min(Clockin) Clockin,empid,StatusID,TDate,PayrollID FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(StartDate)+" and Empid="+rs.getInt("empid")+" and StatusID=1");
						if(rsCheckIn.first()) {
							
							PrimaryID++;
							if(rsCheckIn.getString("Clockin")!=null) {
							System.out.println("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckIn.getInt("empid")+","+rsCheckIn.getInt("StatusID")+",'"+rsCheckIn.getTimestamp("TDate")+"','"+rsCheckIn.getString("Clockin")+"','"+rsCheckIn.getString("PayrollID")+"',now())");
							s2.executeUpdate("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckIn.getInt("empid")+","+rsCheckIn.getInt("StatusID")+",'"+rsCheckIn.getTimestamp("TDate")+"','"+rsCheckIn.getString("Clockin")+"','"+rsCheckIn.getString("PayrollID")+"',now())");
							}
						}
						
						System.out.println("SELECT max(Clockin),empid,StatusID,TDate,PayrollID FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(StartDate)+" and Empid="+rs.getInt("empid")+" and StatusID=2");
						ResultSet rsCheckOut = s2.executeQuery("SELECT max(Clockin) Clockin,empid,StatusID,TDate,PayrollID FROM pep.integration_attendance where tdate between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(StartDate)+" and Empid="+rs.getInt("empid")+" and StatusID=2");
						if(rsCheckOut.first()) {
							
							PrimaryID++;
							if(rsCheckOut.getString("Clockin")!=null) {
							System.out.println("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckOut.getInt("empid")+","+rsCheckOut.getInt("StatusID")+",'"+rsCheckOut.getTimestamp("TDate")+"',''"+rsCheckOut.getString("Clockin")+"','"+rsCheckOut.getString("PayrollID")+"',now())");
							s2.executeUpdate("INSERT INTO pep.employee_integration_attendance_mview(id,empid,statusid,tdate,clockin,payroll_id,created_on) VALUES("+PrimaryID+","+rsCheckOut.getInt("empid")+","+rsCheckOut.getInt("StatusID")+",'"+rsCheckOut.getTimestamp("TDate")+"','"+rsCheckOut.getString("Clockin")+"','"+rsCheckOut.getString("PayrollID")+"',now())");
							}
						}
					 }
					 
					/* StartDate = Utilities.getDateByDays(StartDate, 1);
					 
			 }// end of date
*/					 
					 System.out.println(c);
					ds.commit();
					s.close();
					s2.close();
			 }catch(Exception e){
					try {
						ds.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(e);
					
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
