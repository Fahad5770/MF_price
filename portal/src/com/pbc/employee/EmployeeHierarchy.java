package com.pbc.employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pbc.common.User;
import com.pbc.util.Datasource;

public class EmployeeHierarchy {
	
	public static User[] getRSMs() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		List <User>list = new ArrayList<User>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT distinct rsm_id, display_name, u.email, cd.region_id, cr.region_name from common_distributors cd join users u on cd.rsm_id = u.id join common_regions cr on cd.region_id = cr.region_id where cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted)");
		while(rs.next()){
			User u = new User();
			u.USER_ID = rs.getLong(1);
			u.USER_DISPLAY_NAME = rs.getString(2);
			u.USER_EMAIL = rs.getString(3);
			u.REGION_ID = rs.getInt(4);
			u.REGION_NAME = rs.getString(5);
			list.add(u);
		}
		
		s.close();
		ds.dropConnection();
		
		return list.toArray(new User[list.size()]);
	}

	public static User[] getTDMs() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		List <User>list = new ArrayList<User>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT distinct tdm_id, display_name, u.email, cd.region_id, cr.region_name from common_distributors cd join users u on cd.tdm_id = u.id join common_regions cr on cd.region_id = cr.region_id where cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted) and u.is_active = 1");
		while(rs.next()){
			User u = new User();
			u.USER_ID = rs.getLong(1);
			u.USER_DISPLAY_NAME = rs.getString(2);
			u.USER_EMAIL = rs.getString(3);
			u.REGION_ID = rs.getInt(4);
			u.REGION_NAME = rs.getString(5);
			list.add(u);
		}
		
		s.close();
		ds.dropConnection();
		
		return list.toArray(new User[list.size()]);
	}
	
	
	public static User getSND(long RSM_ID) {
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("select id, display_name, email from users where id in (select distinct snd_id from common_distributors where rsm_id = "+RSM_ID+" and snd_id != 1639)");
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
				u.USER_EMAIL = rs.getString(3);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static User getRSMByTDM(long TDM_ID){
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("select id, display_name, email from users where id in (select distinct rsm_id from common_distributors where tdm_id = "+TDM_ID+")");
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
				u.USER_EMAIL = rs.getString(3);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static User getRSM(long OrderBookerID){
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("SELECT distinct cd.rsm_id, (select display_name from users where id = cd.rsm_id) display_name FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where cd.name not like '%NCSD%' and dbpv.assigned_to = "+OrderBookerID);
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static User getSDHead(int SD_ID){
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("SELECT csg.snd_id, u.display_name, u.email FROM common_sd_groups csg join users u on csg.snd_id = u.id where csg.id = "+SD_ID);
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
				u.USER_EMAIL = rs.getString(3);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static User getHeadOfSales(){
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("SELECT u.id, u.display_name, u.email FROM users u where role_id = 3");
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
				u.USER_EMAIL = rs.getString(3);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static User getCOO(){
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("SELECT u.id, u.display_name, u.email FROM users u where role_id = 2");
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
				u.USER_EMAIL = rs.getString(3);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static User getCEO(){
		try{
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			User u = new User();
			
			ResultSet rs = s.executeQuery("SELECT u.id, u.display_name, u.email FROM users u where role_id = 1");
			while(rs.next()){
				u.USER_ID = rs.getLong(1);
				u.USER_DISPLAY_NAME = rs.getString(2);
				u.USER_EMAIL = rs.getString(3);
			}
			
			s.close();
			ds.dropConnection();
			
			return u;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
