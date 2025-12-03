package com.pbc.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.pbc.common.User;
import com.pbc.util.Datasource;

public class EmployeeHierarchy {

	public ArrayList<User> CRs = new ArrayList<User>();
	public ArrayList<User> DownwardHir = new ArrayList<User>();
	
	public User[] getReportingToAll(long UserID){
		long ReportingID=UserID;

		User u = new User();
		ArrayList<User> ReportingIDArray = new ArrayList<User>();	
		do{
			u= getReportingTo(ReportingID);
			ReportingID=u.USER_ID;

			if(ReportingID==getReportingTo(ReportingID).USER_ID){
				break;
			}
			
			if (ReportingID == 0){
				break;
			}
			
			ReportingIDArray.add(u);
		}while(ReportingID!=1);
		
		return ReportingIDArray.toArray(new User[ReportingIDArray.size()]);
	}

	public User getReportingTo(long UserID){
		Datasource ds = new Datasource();
		
		User ReportingID= new User();
		try {
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT ur.id reporting_to_id, ur.display_name, ur.designation, ur.current_reporting_level, (select short_name from common_hierarchy_levels where id = ur.current_reporting_level) current_reporting_level_label from users u join users ur on u.current_reporting_to = ur.id where u.id = "+UserID);
			if(rs.first()){
				ReportingID.USER_ID = rs.getLong("reporting_to_id");
				ReportingID.HIERARCHY_LEVEL_ID=rs.getInt("current_reporting_level");
				ReportingID.USER_DISPLAY_NAME = rs.getString("display_name");
				ReportingID.DESIGNATION = rs.getString("designation");
				ReportingID.HIERARCHY_LEVEL_LABEL = rs.getString("current_reporting_level_label"); 
			}
			
			s.close();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return ReportingID;
	}
	
	public void setReporteeCRs(long UserID){
		
		User child[] = getReportees(UserID);
		
		for (int i = 0; i < child.length; i++){
			if (child[i].HIERARCHY_LEVEL_ID == 7){
				CRs.add(child[i]);
			}
			setReporteeCRs(child[i].USER_ID);
		}
		
	}
	
	public String getReporteeCRsQueryString(){
		
		String ret = "";
		
		if (this.CRs != null){
			for (int i = 0; i < this.CRs.size(); i++){
				if (i != 0){
					ret += ",";					
				}
				ret = ret + this.CRs.get(i).USER_ID;
			
			}
		}else{
			ret = null;
		}
		
		return ret;
		
	}
	
public User[] getDownwardHierarchy(long UserID){
		
		User child[] = getReportees(UserID);
		
		//System.out.println("User ID "+UserID);
		//System.out.println("Get Reportees Array size "+child.length);
		
		for (int i = 0; i < child.length; i++){
			DownwardHir.add(child[i]);
			getDownwardHierarchy(child[i].USER_ID);
		}
		return DownwardHir.toArray(new User[DownwardHir.size()]);
	}
	
	public String getDownwardHierarchyQueryString(User list[]){
		
		String ret = "";
		
		if (list != null){
			for (int i = 0; i < list.length; i++){
				if (i != 0){
					ret += ",";					
				}
				ret = ret + list[i].USER_ID;
			
			}
		}else{
			ret = null;
		}
		
		return ret;
	}
	
	public User[] getReportees(long UserID){
		Datasource ds = new Datasource();
		ArrayList<User> ReportingIDArray = new ArrayList<User>();
		
		try {
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select u.id, u.current_reporting_level from users u where u.current_reporting_to="+UserID);
			while(rs.next()){
				User ReportingID= new User();
				ReportingID.USER_ID = rs.getLong("id");
				ReportingID.HIERARCHY_LEVEL_ID=rs.getInt("current_reporting_level");
				ReportingIDArray.add(ReportingID);
			}
			
			s.close();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	 return ReportingIDArray.toArray(new User[ReportingIDArray.size()]);
	}
	
	public User[] getAllCRs(){
		Datasource ds = new Datasource();
		ArrayList<User> ReportingIDArray = new ArrayList<User>();
		
		try {
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select u.id, u.display_name from users u where u.current_reporting_level = 7");
			while(rs.next()){
				User item= new User();
				item.USER_ID = rs.getLong("id");
				item.USER_DISPLAY_NAME = rs.getString("display_name");
				ReportingIDArray.add(item);
			}
			
			s.close();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	 return ReportingIDArray.toArray(new User[ReportingIDArray.size()]);
	}
	
	public int getHierarchyLevel(long UserID){
		
		Datasource ds = new Datasource();
		
		int HierarchyLevel = 0;
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT u.current_reporting_level from users u where u.id = "+UserID);
			if(rs.first()){
				HierarchyLevel = rs.getInt(1);
			}
			
			s.close();
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			
			e.printStackTrace();
			
		} finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return HierarchyLevel;
	}
}


