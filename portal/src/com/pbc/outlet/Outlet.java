package com.pbc.outlet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;

public class Outlet {
	public long ID;
	public String NAME;
	public String REGION_SHORT_NAME;
	public String DISTRIBUTOR_NAME;
	public String ADDRESS;
	public long RSM_ID;
	public long ASM_ID;
	public long CR_ID;
	public double LONGITUDE;
	public double LATITUDE;
	public long DISTRIBUTOR_ID;
	
	public double getCurrentBalance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		double ret = 0;
		
		ResultSet rs = s.executeQuery("select sum(debit), sum(credit) from sampling_posting_accounts where outlet_id = "+this.ID);
		if (rs.first()){
			ret = rs.getDouble(1) - rs.getDouble(2);
		}
		
		s.close();
		
		ds.dropConnection();
		
		return ret;
		
	}
	public long[] getCSDDistributor() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long ret[] = new long[2];
		
		ResultSet rs = s.executeQuery("SELECT dbp.distributor_id, cd.region_id, cd.product_group_id, (select '1' from employee_product_groups_list where product_group_id = cd.product_group_id and product_id = 108) is_csd FROM distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id join common_distributors cd on dbp.distributor_id = cd.distributor_id where dbps.outlet_id = "+this.ID);
		if (rs.first()){
			ret[0] = rs.getLong("region_id");
			ret[1] = rs.getLong("distributor_id");
		}
		
		s.close();
		
		ds.dropConnection();
		
		return ret;
		
	}	
	
	/////// Added by ferhan ////////////////////
	
	
	public double getCurrentBalance1(String outletID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		double ret = 0;
		
		ResultSet rs = s.executeQuery("select sum(debit), sum(credit) from sampling_posting_accounts where outlet_id = "+outletID);
		if (rs.first()){
			ret = rs.getDouble(1) - rs.getDouble(2);
		}
		
		s.close();
		
		ds.dropConnection();
		
		return ret;
		
	}
	
	/////////////////////////////////////////
	
	
	
}
