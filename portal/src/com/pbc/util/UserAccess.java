package com.pbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pbc.common.Warehouse;
import com.pbc.common.Region;
import com.pbc.common.Distributor;
import com.pbc.common.EmployeeHierarchy;
import com.pbc.common.User;
import com.pbc.outlet.Outlet;

/**
 * It provides utilities to check authorization matrix of users
 * @author PBC
 *
 */
public class UserAccess {
	
	
	/**
	 * To get a list of authorized warehouses for a specific user
	 * @param UserID
	 * @param FeatureID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Warehouse[] getUserFeatureWarehouse(long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		List<Warehouse> WarehouseList = new ArrayList<Warehouse>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		ResultSet rs = s.executeQuery("select warehouse_id, (select label from common_warehouses where id=warehouse_id) warehouse_name from user_access_warehouses where user_id="+UserID+" and feature_id="+FeatureID);
		while( rs.next() ){
			
			Warehouse temp = new Warehouse();
			
			temp.WAREHOUSE_ID = rs.getInt("warehouse_id");
			temp.WAREHOUSE_NAME = rs.getString("warehouse_name");
			
			WarehouseList.add(temp);
		}
		
		
		
		if( WarehouseList.size() == 0 ){			
			 rs = s.executeQuery("select id,label from common_warehouses");
			while( rs.next() ){
				
				Warehouse temp = new Warehouse();
				
				temp.WAREHOUSE_ID = rs.getInt("id");
				temp.WAREHOUSE_NAME = rs.getString("label");
				
				WarehouseList.add(temp);
			}
			
			s.close();
			ds.dropConnection();
			return WarehouseList.toArray(new Warehouse[WarehouseList.size()]);
		}else{
			s.close();
			ds.dropConnection();
			return WarehouseList.toArray(new Warehouse[WarehouseList.size()]);
		}
		
	}
	/**
	 * To converted array of warehouses into a string of comma separated values
	 * @param list
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static String getWarehousesQueryString(Warehouse[] list) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		String ret = "";
		
		if (list != null){
			for (int i = 0; i < list.length; i++){
				if (i != 0){
					ret += ",";					
				}
				ret = ret + list[i].WAREHOUSE_ID;
			
			}
		}else{
			ret = null;
		}
		
		return ret;
	}
	
	
	/**
	 * To get a list of authorized regions for a specific user 
	 * @param UserID
	 * @param FeatureID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Region[] getUserFeatureRegion(long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		List<Region> RegionList = new ArrayList<Region>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		ResultSet rs = s.executeQuery("select region_id,(select region_name from common_regions cr where cr.region_id=uar.region_id) region_name,(select region_short_name from common_regions cr where cr.region_id=uar.region_id) region_short_name from user_access_regions uar where user_id="+UserID+" and feature_id="+FeatureID);
		while( rs.next() ){
			
			Region temp = new Region();
			
			temp.REGION_ID = rs.getInt("region_id");
			temp.REGION_SHORT_NAME = rs.getString("region_short_name");
			temp.REGION_NAME = rs.getString("region_name");
			
			RegionList.add(temp);
		}
		
		
		
		if( RegionList.size() == 0 ){			
			 rs = s.executeQuery("select * from common_regions");
			while( rs.next() ){
				
				Region temp = new Region();
				
				temp.REGION_ID = rs.getInt("region_id");
				temp.REGION_SHORT_NAME = rs.getString("region_short_name");
				temp.REGION_NAME = rs.getString("region_name");
				
				RegionList.add(temp);
			}
			
			s.close();
			ds.dropConnection();
			return RegionList.toArray(new Region[RegionList.size()]);
		}else{
			s.close();
			ds.dropConnection();
			return RegionList.toArray(new Region[RegionList.size()]);
		}
		
	}

	/**
	 * To convert array of regions into a string of comma separated values
	 * @param list
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static String getRegionQueryString(Region[] list) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		String ret = "";
		
		if (list != null){
			for (int i = 0; i < list.length; i++){
				if (i != 0){
					ret += ",";					
				}
				ret = ret + list[i].REGION_ID;
			
			}
		}else{
			ret = null;
		}
		
		return ret;
	}

	/**
	 * To get list of authorized outlets for a specific user
	 * @param UserID
	 * @param FeatureID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static Outlet[] getUserFeatureOutlet(long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		String dlist = getDistributorQueryString(getUserFeatureDistributor(UserID, FeatureID));
		
		List<Outlet> OutletList = new ArrayList<Outlet>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		
		if (dlist != null && dlist.length() > 0){
			ResultSet rs = s.executeQuery("select id, name from common_outlets co where id in (select outlet_id from common_outlets_distributors_view where distributor_id in ("+dlist+")) or distributor_id in ("+dlist+")");
			while(rs.next()){
				Outlet item = new Outlet();
				
				item.ID = rs.getLong(1);
				item.NAME = rs.getString(2);
				
				OutletList.add(item);
				
			}
			
		}
		
		s.close();
		ds.dropConnection();
		
		return OutletList.toArray(new Outlet[OutletList.size()]);
		
	}
	
	
	/**
	 * To get list of authorized order bookers for a specific user
	 * @param UserID
	 * @param FeatureID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static User[] getUserFeatureOrderBooker(long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		List<User> UserList = new ArrayList<User>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		
		int HierarchyLevel = 0;
		ResultSet rs2 = s.executeQuery("select current_reporting_level from users where id = "+UserID);
		while(rs2.next()){
			HierarchyLevel = rs2.getInt(1);
		}
		
		String where = "";
		if (HierarchyLevel != 1){
			where = " where cd.rsm_id = "+UserID+" or cd.snd_id = "+UserID;
		}
		
		ResultSet rs = s.executeQuery("SELECT distinct dbpv.assigned_to, u.display_name, u.designation FROM common_distributors cd join distributor_beat_plan_view dbpv on cd.distributor_id = dbpv.distributor_id join users u on u.id = dbpv.assigned_to "+where);
		while(rs.next()){
			User user = new User();
			user.USER_ID = rs.getLong(1);
			user.USER_DISPLAY_NAME = rs.getString(2);
			user.DESIGNATION = rs.getString(3);
			UserList.add(user);
		}
		
	
		
		
		
		//SELECT distinct dbpv.assigned_to, u.display_name FROM common_distributors cd join distributor_beat_plan_view dbpv on cd.distributor_id = dbpv.distributor_id join users u on u.id = dbpv.assigned_to where cd.rsm_id = 2241 or cd.snd_id = 2241;
		
		/*
		EmployeeHierarchy eh = new EmployeeHierarchy();
		eh.setReporteeCRs(UserID);
		
		if (eh.CRs.size() > 0){
			UserList = Arrays.asList(eh.CRs.toArray(new User[eh.CRs.size()]));
		}else{
			if (eh.getHierarchyLevel(UserID) == 1){
				UserList = Arrays.asList(eh.getAllCRs());
			}
		}
		*/
		
		s.close();
		ds.dropConnection();
		
		return UserList.toArray(new User[UserList.size()]);
		
	}

	
	/**
	 * To convert array of users into a string of comma separated values 
	 * @param list
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static String getOrderBookerQueryString(User[] list) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
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

	/**
	 * To check if a specific outlet is authorized to a user
	 * @param OutletID
	 * @param UserID
	 * @param FeatureID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static boolean isOutletAllowed(long OutletID, long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		boolean ret = false;
		
		Outlet list[] =  getUserFeatureOutlet(UserID, FeatureID);
		
		for (int i = 0; i < list.length; i++){
			if (OutletID == list[i].ID){
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	public static boolean isDistributorAllowed(long DistributorID, long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		boolean ret = false;
		
		Distributor list[] =  getUserFeatureDistributor(UserID, FeatureID);
		
		for (int i = 0; i < list.length; i++){
			if (DistributorID == list[i].DISTRIBUTOR_ID){
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	public static boolean isOrderBookerAllowed(long OrderBookerID, long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		boolean ret = false;
		
		User list[] =  getUserFeatureOrderBooker(UserID, FeatureID);
		
		for (int i = 0; i < list.length; i++){
			if (OrderBookerID == list[i].USER_ID){
				ret = true;
				break;
			}
		}
		
		return ret;
	}


public static Distributor[] getUserFeatureDistributor(long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	List<Distributor> DistributorList = new ArrayList<Distributor>();
	
	Datasource ds = new Datasource();
	ds.createConnection();
	Statement s = ds.createStatement();
	Statement s2 = ds.createStatement();
	
	// Adds default distributor in case of secondary user 
	ResultSet rs1 = s.executeQuery("select common_distributors.distributor_id,common_distributors.name from users, common_distributors where users.id="+UserID+" and users.type_id = 2 and users.distributor_id=common_distributors.distributor_id");
	if(rs1.first())
	{
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rs1.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rs1.getString("name");
		
		DistributorList.add(temp);

	}
	
	
	// Add distributors which are defined against scope
	ResultSet rs = s.executeQuery("select distributor_id,(select name from common_distributors cd where cd.distributor_id=uad.distributor_id) name from user_access_distributors uad where user_id="+UserID+" and feature_id="+FeatureID);
	while( rs.next() ){
		
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rs.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rs.getString("name");		
		
		DistributorList.add(temp);
	}
	
	
	// Add distribtuors if a Distributor Group  is assigned
	ResultSet rss = s.executeQuery("select uadgl.distributor_id,name from user_access_distributor_groups uadg,common_distributor_groups_list uadgl,common_distributors cd where uadg.group_id=uadgl.id and cd.distributor_id=uadgl.distributor_id and user_id="+UserID+" and feature_id="+FeatureID);
	while( rss.next() ){
		
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rss.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rss.getString("name");		
		
		DistributorList.add(temp);
	}
	
	
	
	// In case no distributors found in above cases: 
	
	
	if( DistributorList.size() == 0 ){		
		
		
		// Checks if default distributor group is assgined
		ResultSet rsss = s.executeQuery("select id, default_distributor_group from users where id = "+UserID);
		if( rsss.first() ){
			if(rsss.getInt("default_distributor_group") == 0){ // No default distributor case: adds all distributors
				
				
				// check if SM or RSM
				boolean ifSMorRSM = false;
				ResultSet rs9 = s2.executeQuery("select distributor_id, name from common_distributors where snd_id = " +UserID+" or rsm_id = "+UserID+"  or tdm_id = "+UserID+" union SELECT distinct dbp.distributor_id, (select name from common_distributors where distributor_id = dbp.distributor_id) name FROM distributor_beat_plan dbp where dbp.sm_id = "+UserID+" or dbp.asm_id = "+UserID);
				while(rs9.next()){
					
					ifSMorRSM = true;
					Distributor temp = new Distributor();
					
					temp.DISTRIBUTOR_ID = rs9.getInt("distributor_id");
					temp.DISTRIBUTOR_NAME = rs9.getString("name");		
					
					DistributorList.add(temp);					
					
				}
				if (ifSMorRSM == true){
				
				}else{ // If not SM or RSM: all distributors
					rs = s.executeQuery("select * from common_distributors");
					while( rs.next() ){
						
						Distributor temp = new Distributor();
						
						temp.DISTRIBUTOR_ID = rs.getInt("distributor_id");
						temp.DISTRIBUTOR_NAME = rs.getString("name");		
						
						DistributorList.add(temp);
					}
				}
				
			}else { // In case a default distributor group is assigned:
				
				ResultSet rssss = s.executeQuery("select uadgl.distributor_id,name from common_distributor_groups_list uadgl,common_distributors cd where cd.distributor_id=uadgl.distributor_id and uadgl.id="+rsss.getInt("default_distributor_group"));
				while( rssss.next() ){
					
					Distributor temp = new Distributor();
					
					temp.DISTRIBUTOR_ID = rssss.getInt("distributor_id");
					temp.DISTRIBUTOR_NAME = rssss.getString("name");		
					
					DistributorList.add(temp);
				}
			}
			
		}
		s.close();
		s2.close();
		ds.dropConnection();
		return DistributorList.toArray(new Distributor[DistributorList.size()]);
	}else{
		s2.close();
		s.close();
		ds.dropConnection();
		return DistributorList.toArray(new Distributor[DistributorList.size()]);
	}
	
}

public static Distributor[] getSNDDistributors(long SNDID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	List<Distributor> DistributorList = new ArrayList<Distributor>();
	
	Datasource ds = new Datasource();
	ds.createConnection();
	Statement s = ds.createStatement();
	Statement s2 = ds.createStatement();
	
	
	
	ResultSet rs = s.executeQuery("select distributor_id, name from common_distributors where snd_id in ("+SNDID+") or rsm_id in ("+SNDID+")");
	while( rs.next() ){
		
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rs.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rs.getString("name");		
		
		DistributorList.add(temp);
		
	}
	
	s.close();
	s2.close();
	ds.dropConnection();
	
	return DistributorList.toArray(new Distributor[DistributorList.size()]);
	
}

public static Distributor[] getUserFeatureDistributorSecondarySales(long UserID, int FeatureID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	List<Distributor> DistributorList = new ArrayList<Distributor>();
	
	Datasource ds = new Datasource();
	ds.createConnection();
	Statement s = ds.createStatement();
	Statement s2 = ds.createStatement();
	
	// Adds default distributor in case of secondary user 
	ResultSet rs1 = s.executeQuery("select common_distributors.distributor_id,common_distributors.name from users, common_distributors where users.id="+UserID+" and users.type_id = 2 and users.distributor_id=common_distributors.distributor_id");
	if(rs1.first())
	{
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rs1.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rs1.getString("name");
		
		DistributorList.add(temp);

	}
	
	
	// Add distributors which are defined against scope
	ResultSet rs = s.executeQuery("select distributor_id,(select name from common_distributors cd where cd.distributor_id=uad.distributor_id) name from user_access_distributors uad where user_id="+UserID+" and feature_id="+FeatureID);
	while( rs.next() ){
		
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rs.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rs.getString("name");		
		
		DistributorList.add(temp);
	}
	
	
	// Add distribtuors if a Distributor Group  is assigned
	ResultSet rss = s.executeQuery("select uadgl.distributor_id,name from user_access_distributor_groups uadg,common_distributor_groups_list uadgl,common_distributors cd where uadg.group_id=uadgl.id and cd.distributor_id=uadgl.distributor_id and user_id="+UserID+" and feature_id="+FeatureID);
	while( rss.next() ){
		
		Distributor temp = new Distributor();
		
		temp.DISTRIBUTOR_ID = rss.getInt("distributor_id");
		temp.DISTRIBUTOR_NAME = rss.getString("name");		
		
		DistributorList.add(temp);
	}
	
	
	
	// In case no distributors found in above cases: 
	
	
	if( DistributorList.size() == 0 ){		
		
		
		// Checks if default distributor group is assgined
		ResultSet rsss = s.executeQuery("select id, default_distributor_group from users where id = "+UserID);
		if( rsss.first() ){
			if(rsss.getInt("default_distributor_group") == 0){ // No default distributor case: adds all distributors
				
				
				// check if SND/SM/TDM/RSM/ASM
				boolean ifSMorRSM = false;
				ResultSet rs9 = s2.executeQuery("select distributor_id, name from common_distributors where snd_id = " +UserID+" or rsm_id = "+UserID + " union SELECT distinct dbp.distributor_id, (select name from common_distributors where distributor_id = dbp.distributor_id) name FROM distributor_beat_plan dbp where dbp.sm_id = "+UserID+" or dbp.tdm_id = "+UserID+" or dbp.asm_id = "+UserID);
				while(rs9.next()){
					
					ifSMorRSM = true;
					Distributor temp = new Distributor();
					
					temp.DISTRIBUTOR_ID = rs9.getInt("distributor_id");
					temp.DISTRIBUTOR_NAME = rs9.getString("name");		
					
					DistributorList.add(temp);					
					
				}
				
				if (ifSMorRSM == true){
					
				}else{ // if not SM or RSM: all distributors
					rs = s.executeQuery("select * from common_distributors where type_id = 2");
					while( rs.next() ){
						
						Distributor temp = new Distributor();
						
						temp.DISTRIBUTOR_ID = rs.getInt("distributor_id");
						temp.DISTRIBUTOR_NAME = rs.getString("name");		
						
						DistributorList.add(temp);
					}
				}
				
			}else { // In case a default distributor group is assigned:
				
				ResultSet rssss = s.executeQuery("select uadgl.distributor_id,name from common_distributor_groups_list uadgl,common_distributors cd where cd.distributor_id=uadgl.distributor_id and uadgl.id="+rsss.getInt("default_distributor_group"));
				while( rssss.next() ){
					
					Distributor temp = new Distributor();
					
					temp.DISTRIBUTOR_ID = rssss.getInt("distributor_id");
					temp.DISTRIBUTOR_NAME = rssss.getString("name");		
					
					DistributorList.add(temp);
				}
			}
			
		}
		s.close();
		s2.close();
		ds.dropConnection();
		
		return DistributorList.toArray(new Distributor[DistributorList.size()]);
	}else{
		s2.close();
		s.close();
		ds.dropConnection();
		return DistributorList.toArray(new Distributor[DistributorList.size()]);
	}
	
}

public static String getDistributorQueryString(Distributor[] list) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	String ret = "";
	
	if (list != null){
		for (int i = 0; i < list.length; i++){
			if (i != 0){
				ret += ",";					
			}
			ret = ret + list[i].DISTRIBUTOR_ID;
		
		}
	}else{
		ret = null; 
	}
	
	return ret;
}

public static boolean isMobileDeviceValid(String DeviceID) throws SQLException{
	boolean isDeviceValid = false;
	Datasource ds = new Datasource();
	try {
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT id, uuid FROM mobile_devices where uuid='"+DeviceID+"'");
		if(rs.first()){
			
			if(rs.getString("uuid").equals(DeviceID)){
				isDeviceValid = true;
			}
				
		}
		
		s.close();
		
	} catch (ClassNotFoundException | InstantiationException
			| IllegalAccessException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		ds.dropConnection();
	}
	
	return isDeviceValid;
	
}


public static boolean isMobileUserValid(long UserID, String Password) throws SQLException{
	boolean isUserValid = false;
	
	
	Datasource ds = new Datasource();
	try {
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT id, password FROM users where id="+UserID);
		if(rs.first()){
			if(rs.getString("password").equals(Password) || Password.equals(Utilities.getMobileAdminPasswordMD5()) ){
				isUserValid = true;
			}
		}
		
		s.close();
		
	} catch (ClassNotFoundException | InstantiationException
			| IllegalAccessException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		ds.dropConnection();
	}
	
	return isUserValid;
	
}

public static boolean isAuthorized(int FeatureID, long UserID, HttpServletRequest request) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

	Datasource ds = new Datasource();
	ds.createConnection();

	boolean isAuthorized = false;
	boolean isChromeOnly = false;
	
	Statement s = ds.createStatement();

	ResultSet rs = s.executeQuery("select created_on, (select is_chrome_only from features where feature_id = "+FeatureID+") is_chrome_only from user_access where user_id=" + UserID + " and feature_id=" + FeatureID);
	if (rs.first()) {
		isAuthorized = true;
		if (rs.getInt(2) == 1){
			isChromeOnly = true;
		}
	}
	
	if (isChromeOnly == true && isAuthorized == true){
		
		HttpSession session = request.getSession();
		if (session != null){
			
			int isMobileSession = Utilities.parseInt((String)session.getAttribute("isMobileSession"));
			
			if (isMobileSession != 1){
				long BrowserID = 0;
				if (session.getAttribute("BrowserID") != null){
					BrowserID = ((Long)session.getAttribute("BrowserID")).longValue();
				}
				
				ResultSet rs2 = s.executeQuery("select user_id from user_access_chrome_activation where browser_id = "+BrowserID+" and user_id= "+UserID+" and is_active = 1");
				if (!rs2.first()){
					isAuthorized = false;
				}
			}
			
		}else{
			isAuthorized = false;
		}
		
	}
	
	
	
	int isAuthorizedNumber = 0;
	if (isAuthorized == true){
		isAuthorizedNumber = 1;
	}
	
	s.executeUpdate("insert into " + ds.logDatabaseName() + ".log_user_access (user_id, feature_id, ip_address, created_on, is_authorized, feature_group_label, feature_label) values ("+UserID+", "+FeatureID+", '"+request.getRemoteAddr()+"', now(), "+isAuthorizedNumber+", (select fg.group_name from features f join feature_groups fg on f.group_id = fg.group_id where f.feature_id = "+FeatureID+"),(select feature_name from features where feature_id = "+FeatureID+"))" );
	
	ds.dropConnection();

	return isAuthorized;
}


}
