package com.pbc.bi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class BiProcesses {

	Connection c;
	Datasource ds;
	
	public BiProcesses() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	
	public boolean CreateOrderBookerStatistics(java.util.Date FrmDate,java.util.Date ToDate){
		Date CurrentDate = FrmDate;
		boolean flag = true;
		/*while(true){
			//System.out.println("==> Current Date  "+CurrentDate);
			flag = CreateOrderBookerStatisticsDaily(CurrentDate);
			CreatePJPStatisticsDaily(CurrentDate);
			if(CurrentDate.equals(ToDate)){
				break;
			}
			CurrentDate = DateUtils.addDays(CurrentDate, 1);
		}*/
		return flag;
	}
	
	public boolean CreateOrderBookerStatisticsDaily(java.util.Date date){
		boolean flag = true;
		/*
		try {
		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		ds.startTransaction();
		ResultSet rs = s.executeQuery("select u.id, ( "+
				"select count(dbpv.outlet_id) total_visits_due from distributor_beat_plan_view dbpv  where dbpv.assigned_to = u.id and dbpv.day_number = dayofweek(date("+Utilities.getSQLDate(date)+")) "+
				") total_visits_due, ( "+
				"select  count(DISTINCT mo.outlet_id) total_number_of_visits from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id"+
				")total_number_of_visits,("+
				 "select count(mo.id) total_no_of_orders from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id "+
				") total_no_of_orders, ( "+
				 "select sum(isap.raw_cases) raw_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) and isap.is_promotion = 0 "+
				") raw_cases_sold, ( "+
				 "select count(id) total_no_of_invoices from inventory_sales_adjusted isa where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) "+
				") total_no_of_invoices, ( "+
				 "select count(isap.id) total_no_of_invoice_items from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) and isap.is_promotion = 0 "+
				") total_no_of_invoice_items, ("+
				"select sum(net_amount) total_amount_invoices from inventory_sales_adjusted isa where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) "+
				") total_amount_invoices, (" +
				"select sum(net_amount) total_amount_orders from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id "+
				") total_amount_orders from users u where u.current_reporting_level = 7 and u.is_active = 1");
		
		s1.executeUpdate("DELETE FROM bi_orderbooker_daily WHERE dated ="+Utilities.getSQLDate(date));
		while(rs.next()){
			s1.executeUpdate("INSERT INTO bi_orderbooker_daily(dated,orderbooker_id,total_visits_due,total_no_of_visits,total_no_of_orders,raw_cases_sold,total_no_of_invoices,total_no_of_invoice_items,created_on,total_amount_invoices,total_amount_orders) values("+Utilities.getSQLDate(date)+","+rs.getLong("id")+","+rs.getLong("total_visits_due")+","+rs.getLong("total_number_of_visits")+","+rs.getLong("total_no_of_orders")+","+rs.getLong("raw_cases_sold")+","+rs.getLong("total_no_of_invoices")+","+rs.getLong("total_no_of_invoice_items")+",now(), "+rs.getDouble("total_amount_invoices")+", "+rs.getDouble("total_amount_orders")+")");//deletting old records
			flag = true;
		}
		ds.commit();
		s.close();	
		s1.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	*/
		return flag;
	}
	
	public boolean CreatePJPStatisticsDaily(java.util.Date date){
		boolean flag = true;
		/*
		try {
		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		ds.startTransaction();
		ResultSet rs = s.executeQuery("select dbp.id, ( "+
				"select count(dbpv.outlet_id) total_visits_due from distributor_beat_plan_view dbpv  where dbpv.id = dbp.id and dbpv.day_number = dayofweek(date("+Utilities.getSQLDate(date)+")) "+
				") total_visits_due, ( "+
				"select  count(DISTINCT mo.outlet_id) total_number_of_visits from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id"+
				")total_number_of_visits,("+
				 "select count(mo.id) total_no_of_orders from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id "+
				") total_no_of_orders, ( "+
				 "select sum(isap.raw_cases) raw_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.beat_plan_id = dbp.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id) and isap.is_promotion = 0 "+
				") raw_cases_sold, ( "+
				 "select count(id) total_no_of_invoices from inventory_sales_adjusted isa where isa.beat_plan_id = dbp.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id) "+
				") total_no_of_invoices, ( "+
				 "select count(isap.id) total_no_of_invoice_items from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.beat_plan_id = dbp.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id) and isap.is_promotion = 0 "+
				") total_no_of_invoice_items, ("+
				"select sum(net_amount) total_amount_invoices from inventory_sales_adjusted isa where isa.beat_plan_id = dbp.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id) "+
				") total_amount_invoices, (" +
				"select sum(net_amount) total_amount_orders from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.beat_plan_id = dbp.id "+
				") total_amount_orders from distributor_beat_plan dbp");
		
		s1.executeUpdate("DELETE FROM bi_pjp_daily WHERE dated ="+Utilities.getSQLDate(date));
		while(rs.next()){
			s1.executeUpdate("INSERT INTO bi_pjp_daily(dated,pjp_id,total_visits_due,total_no_of_visits,total_no_of_orders,raw_cases_sold,total_no_of_invoices,total_no_of_invoice_items,created_on,total_amount_invoices,total_amount_orders) values("+Utilities.getSQLDate(date)+","+rs.getLong("id")+","+rs.getLong("total_visits_due")+","+rs.getLong("total_number_of_visits")+","+rs.getLong("total_no_of_orders")+","+rs.getLong("raw_cases_sold")+","+rs.getLong("total_no_of_invoices")+","+rs.getLong("total_no_of_invoice_items")+",now(), "+rs.getDouble("total_amount_invoices")+", "+rs.getDouble("total_amount_orders")+")");//deletting old records
			flag = true;
		}
		ds.commit();
		s.close();	
		s1.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	*/
		return flag;
	}
	public boolean CreateOrderBookerStatisticsDaily(long UserID, java.util.Date date){
		boolean flag = true;
		/*
		try {
		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		ds.startTransaction();
		ResultSet rs = s.executeQuery("select u.id, ( "+
				"select count(outlet_id) total_visits_due from employee_beat_plan ebp join employee_beat_plan_schedule ebps on ebp.beat_plan_id = ebps.beat_plan_id where ebp.assigned_to = u.id and day_number = dayofweek(date("+Utilities.getSQLDate(date)+")) "+
				") total_visits_due, ( "+
				"select  count(DISTINCT mo.outlet_id) total_number_of_visits from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id"+
				")total_number_of_visits,("+
				 "select count(mo.id) total_no_of_orders from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id "+
				") total_no_of_orders, ( "+
				 "select sum(isap.raw_cases) raw_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) and isap.is_promotion = 0 "+
				") raw_cases_sold, ( "+
				 "select count(id) total_no_of_invoices from inventory_sales_adjusted isa where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) "+
				") total_no_of_invoices, ( "+
				 "select count(isap.id) total_no_of_invoice_items from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) and isap.is_promotion = 0 "+
				") total_no_of_invoice_items, ("+
				"select sum(net_amount) total_amount_invoices from inventory_sales_adjusted isa where isa.booked_by = u.id and isa.order_id in (select mo.id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id) "+
				") total_amount_invoices, (" +
				"select sum(net_amount) total_amount_orders from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(date)+" and "+Utilities.getSQLDateNext(date)+" and mo.created_by = u.id "+
				") total_amount_orders from users u where u.id = "+UserID+" and u.current_reporting_level = 7 and u.is_active = 1");
			s1.executeUpdate("DELETE FROM bi_orderbooker_daily WHERE orderbooker_id = "+UserID+" and dated ="+Utilities.getSQLDate(date));
		while(rs.next()){
			s1.executeUpdate("INSERT INTO bi_orderbooker_daily(dated,orderbooker_id,total_visits_due,total_no_of_visits,total_no_of_orders,raw_cases_sold,total_no_of_invoices,total_no_of_invoice_items,created_on,total_amount_invoices,total_amount_orders) values("+Utilities.getSQLDate(date)+","+rs.getLong("id")+","+rs.getLong("total_visits_due")+","+rs.getLong("total_number_of_visits")+","+rs.getLong("total_no_of_orders")+","+rs.getLong("raw_cases_sold")+","+rs.getLong("total_no_of_invoices")+","+rs.getLong("total_no_of_invoice_items")+",now(), "+rs.getDouble("total_amount_invoices")+", "+rs.getDouble("total_amount_orders")+")");
			flag = true;
		}
		ds.commit();
		s.close();	
		s1.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	*/
		return flag;
	}
	
	public boolean createDistributorBeatPlanLog(){
		boolean flag = false;
		try {
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			
			
			
			
			s.executeUpdate("delete from distributor_beat_plan_log where log_date=curdate()");
			s1.executeUpdate("insert into distributor_beat_plan_log(id,label,distributor_id,product_group_id,product_group_name,created_on,created_by,updated_on,updated_by,outlet_id,day_number,assigned_to,assigned_on,assigned_by,log_date,log_created_on) SELECT id,label,distributor_id,product_group_id,product_group_name,created_on,created_by,updated_on,updated_by,outlet_id,day_number,assigned_to,assigned_on,assigned_by,curdate() as log_date,now() as log_created_on FROM distributor_beat_plan_all_view");
			
			//Alternate PJP Case - 19/07/2019 - Added by Zulqurnan 
			
			Date CurrentDate = new Date();
			
			int CurrentDayNumber = Utilities.getDayOfWeekByDate(CurrentDate);
			
			//System.out.println("SELECT distinct outlet_id, id FROM pep.distributor_beat_plan_schedule where is_alternative=1 and day_number="+CurrentDayNumber);
			ResultSet rs = s.executeQuery("SELECT distinct outlet_id, id FROM pep.distributor_beat_plan_schedule where is_alternative=1 and day_number="+CurrentDayNumber);
			while(rs.next()) {
				long PJPID=rs.getLong("id");
				long OutletID=rs.getLong("outlet_id");
				
				if(!IsAllowed(PJPID,OutletID,CurrentDayNumber)) {
					
					//Write here Delete Query
					
					///System.out.println("SELECT * FROM pep.distributor_beat_plan_log where outlet_id="+OutletID+" and id="+PJPID+" and log_date=curdate() and day_number="+CurrentDayNumber);
				
					s1.executeUpdate("delete FROM pep.distributor_beat_plan_log where outlet_id="+OutletID+" and id="+PJPID+" and log_date=curdate() and day_number="+CurrentDayNumber);
				}
			}
			
			
			
			
			
			/////////////////////////////////////////
			
			
			ds.commit();
			
			flag = true;
			s1.close();
			s.close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	
		return flag;
	}
	public boolean createPromotionsCache(){
		boolean flag = false;
		try {			
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			s.executeUpdate("delete from inventory_sales_promotions_active_mview");
			s1.executeUpdate("insert into inventory_sales_promotions_active_mview (product_promotion_id,outlet_id) SELECT product_promotion_id, outlet_id FROM inventory_sales_promotions_active_view where outlet_id != 0");
			
			
			
			
			
			//PJP Pricelist Query
			ResultSet rs2 = s.executeQuery("SELECT"+ 
			        "`iplpp`.`price_list_id` AS `price_list_id`,"+
			        "`co`.`id` AS `outlet_id`,"+
			        "`iplp`.`product_id` AS `product_id`,"+
			        "`iplp`.`raw_case` AS `raw_case`,"+
			       " `iplp`.`unit` AS `unit`"+
			    "FROM"+
			        "(((`inventory_price_list_pjp` `iplpp`"+
			        "JOIN `inventory_price_list` `ipl`)"+
			       " JOIN `inventory_price_list_products` `iplp`)"+
			        "JOIN `common_outlets` `co`)"+
			    "WHERE"+
			        "((`ipl`.`id` = `iplp`.`id`)"+
			            "AND (`ipl`.`id` = `iplpp`.`price_list_id`)"+
			            "AND (`iplpp`.`pjp_id` = `co`.`cache_beat_plan_id`)"+
			            "AND (`ipl`.`is_active` = 1)"+
			            "AND (`ipl`.`id` <> 1)"+
			            "AND (`ipl`.`valid_from` <= CURDATE())"+
			            "AND (`ipl`.`valid_to` >= CURDATE()))");
			while(rs2.next()) {
				
				long PriceListID=rs2.getLong("price_list_id");
				long OutletID = rs2.getLong("outlet_id");
				long ProductID=rs2.getLong("product_id");
				int Rawcases = rs2.getInt("raw_case");
				int Units = rs2.getInt("unit");
				
				
				s1.executeUpdate("delete from inventory_price_list_active_mview where outlet_id="+OutletID+" and product_id="+ProductID);
				
				s1.executeUpdate("insert into inventory_price_list_active_mview (price_list_id,outlet_id,product_id,raw_case,unit) values("+PriceListID+","+OutletID+","+ProductID+","+Rawcases+","+Units+")");
				
				
			}
			
			ds.commit();
			
			flag = true;
			s1.close();
			s.close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	
		return flag;
	}
	
	public boolean createHandToHandDiscount(){
		/*
		boolean flag = false;
		try{			
			
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			s2.executeUpdate("delete from inventory_price_list_hand_discount_mview");
			
			ResultSet rs = s.executeQuery("SELECT * FROM sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and curdate() between sp.valid_from and sp.valid_to and hand_to_hand != 0");
			while(rs.next()){
				
				if(rs.getLong("package") !=0 && rs.getLong("brand_id")!=0){
					//get product code from inventory_products against this package id and brand id
					ResultSet rs1 = s1.executeQuery("select id from inventory_products where package_id="+rs.getLong("package")+" and brand_id="+rs.getLong("brand_id")+" and category_id=1");
					if(rs1.first()){
						s2.executeUpdate("insert into inventory_price_list_hand_discount_mview(sampling_id, outlet_id,product_id,discount,created_on) values("+rs.getLong("sampling_id")+","+rs.getLong("outlet_id")+","+rs1.getLong("id")+","+rs.getLong("hand_to_hand")+",now())");
					}
					
				}
				else if(rs.getLong("package") !=0 && rs.getLong("brand_id")==0){				
					//get product code from inventory_products against this package id 
					ResultSet rs1 = s1.executeQuery("select id from inventory_products where package_id="+rs.getLong("package")+" and category_id=1");
					while(rs1.next()){					
						s2.executeUpdate("insert into inventory_price_list_hand_discount_mview(sampling_id, outlet_id,product_id,discount,created_on) values("+rs.getLong("sampling_id")+","+rs.getLong("outlet_id")+","+rs1.getLong("id")+","+rs.getLong("hand_to_hand")+",now())");
					}
				}
			}
			flag=true;
			ds.commit();
			s.close();
			s1.close();
			s2.close();
			
		} catch (Exception e) {
			flag=false;
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return flag;
		*/
		return true;
	}
	
	public static boolean IsAllowed(long Beatplanid, long outlet_id, int DayNumber) {

		boolean Allowed = true;
		try {
			Datasource ds = new Datasource();
			ds.createConnection();
			ds.startTransaction();
			Statement s1 = ds.createStatement();


			String sql = "SELECT * FROM pep.distributor_beat_plan_log where id='" + Beatplanid
					+ "' and outlet_id='" + outlet_id + "' and log_date="
					+ Utilities.getSQLDate(Utilities.getDateByDays(-7))+" and day_number="+DayNumber;
			
			ResultSet rs = s1.executeQuery(sql);
			if (rs.next()) {
				Allowed = false;
				//System.out.println(
						//"Exists in previos week " + "id=" + Beatplanid + " and outlet_id=" + outlet_id);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Allowed;
	}  
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
