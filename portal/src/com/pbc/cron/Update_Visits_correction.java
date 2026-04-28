package com.pbc.cron;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.reports.HaversineDistanceCalculator;
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class Update_Visits_correction {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
		//	Date dateForNow = Utilities.getDateByDays(-1);
			Date dateForNow = new Date();
			
			int NoOfDaysToGoBack = -1;
			

			Date date = Utilities.getDateByDays(NoOfDaysToGoBack);
			System.out.println("Date "+date );
			System.out.println("dateForNow "+dateForNow );
			String mobileTimestamp = " created_on between " + Utilities.getSQLDate(date) + " and "
					+ Utilities.getSQLDateNext(date);
			// System.out.println(mobileTImestamp);

			String orderQuery = "SELECT 1 as visit_type, (SELECT label FROM pep.psr_visit_type where id=1) as status, id, mo.snd_id as rsm_id, (select DISPLAY_NAME from users u where u.id=mo.snd_id) as rsm_name, mo.asm_id as tso_id, mo.rsm_id as asm_id, (select cache_contact_number from common_outlets co where co.id=outlet_id) as outlet_contact_number, (select cache_contact_name from common_outlets co where co.id=outlet_id) as outlet_contact_name, (select pic_channel_id from common_outlets co where co.id=outlet_id) as pci_channel_id, (select accuracy from common_outlets co where co.id=outlet_id) as outlet_accuracy, mobile_order_no, beat_plan_id, (SELECT label FROM pep.distributor_beat_plan where id=beat_plan_id) as beat_plan_label, lat, lng, outlet_id,mo.created_on,mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name, (select city_id from common_distributors cd where cd.distributor_id=mo.distributor_id) as city_id,  (select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city, mo.distributor_id as distributor_id, mo.region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng, (select address from common_outlets co where co.id=outlet_id) as outlet_address, (select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments FROM mobile_order mo  where "
					+ mobileTimestamp + " group by outlet_id, DATE(created_on)";

			String noOrder = "SELECT 2 as visit_type, (SELECT label FROM pep.psr_visit_type where id=2) as status, id, mo.snd_id as rsm_id, (select DISPLAY_NAME from users u where u.id=mo.snd_id) as rsm_name, mo.asm_id as tso_id, mo.rsm_id as asm_id, (select cache_contact_number from common_outlets co where co.id=outlet_id) as outlet_contact_number, (select cache_contact_name from common_outlets co where co.id=outlet_id) as outlet_contact_name, (select pic_channel_id from common_outlets co where co.id=outlet_id) as pci_channel_id, (select accuracy from common_outlets co where co.id=outlet_id) as outlet_accuracy, mobile_order_no, beat_plan_id, (SELECT label FROM pep.distributor_beat_plan where id=beat_plan_id) as beat_plan_label, lat, lng, outlet_id,mo.created_on, mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name, (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name, (select city_id from common_distributors cd where cd.distributor_id=mo.distributor_id) as city_id, (select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id, mo.region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region , (select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng, (select address from common_outlets co where co.id=outlet_id) as outlet_address, (select name from common_outlets co where co.id=outlet_id) as outlet_name,(select label from mobile_order_no_order_reason_type where id=mo.no_order_reason_type_v2) as comments  FROM mobile_order_zero mo  where mo.outlet_id not in (select outlet_id from mobile_order where "
					+ mobileTimestamp + " ) and " + mobileTimestamp + " group by outlet_id, DATE(created_on)";

			String justVisitQuery = "SELECT 3 as visit_type, (SELECT label FROM pep.psr_visit_type where id=3) as status, id, mo.snd_id as rsm_id, (select DISPLAY_NAME from users u where u.id=mo.snd_id) as rsm_name, mo.asm_id as tso_id, mo.rsm_id as asm_id, (select cache_contact_number from common_outlets co where co.id=outlet_id) as outlet_contact_number, (select cache_contact_name from common_outlets co where co.id=outlet_id) as outlet_contact_name, (select pic_channel_id from common_outlets co where co.id=outlet_id) as pci_channel_id, (select accuracy from common_outlets co where co.id=outlet_id) as outlet_accuracy, mobile_request_id as mobile_order_no, beat_plan_id, (SELECT label FROM pep.distributor_beat_plan where id=beat_plan_id) as beat_plan_label, lat, lng, outlet_id,mo.created_on,mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name, (select city_id from common_distributors cd where cd.distributor_id=mo.distributor_id) as city_id, (select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id, mo.region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region, (select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng, (select address from common_outlets co where co.id=outlet_id) as outlet_address, (select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments from common_outlets_visit_duration mo where mo.visit_type=1 and "
					+ mobileTimestamp + " and outlet_id not in (SELECT outlet_id FROM mobile_order where "
					+ mobileTimestamp + ") and outlet_id not in ( SELECT outlet_id FROM mobile_order_zero where "
					+ mobileTimestamp + ") group by outlet_id, DATE(created_on) ";

			int count = 1;
			System.out.println(orderQuery + " UNION " + noOrder + " UNION " + justVisitQuery);
			ResultSet rsData = s.executeQuery(orderQuery + " UNION " + noOrder + " UNION " + justVisitQuery);
			while (rsData.next()) {
				//if(count > 2000) {
					long psrId = rsData.getLong("created_by");
					int channel_id = rsData.getInt("pci_channel_id");
					ResultSet rsChannel = s2
							.executeQuery("SELECT label FROM pep.pci_sub_channel where id=" + channel_id);
					String channel = (rsChannel.first()) ? rsChannel.getString("label") : "";

					long distributor_id = rsData.getLong("distributor_id");
					
					String distributor_name =  rsData.getString("Dis_name");
					int city_id = rsData.getInt("city_id");
					String city = rsData.getString("city");
					int region_id = rsData.getInt("region_id");
					String region = rsData.getString("region");
					long tso_id = rsData.getLong("tso_id");
					String tso = rsData.getString("tso_name");
					long asm_id = rsData.getLong("asm_id");
					String asm = rsData.getString("asm_name");
					long rsm_id = rsData.getLong("rsm_id");
					String rsm = rsData.getString("rsm_name");
					int pjp =rsData.getInt("beat_plan_id");
					String pjpLabel =  rsData.getString("beat_plan_label");
					long outlet_id = rsData.getLong("outlet_id");
					
					if(distributor_id == 0 || city_id==0) {
				System.out.println("select distinct distributor_id from distributor_beat_plan_view where outlet_id="+outlet_id+" limit 1");		
					ResultSet rsDistributorID = s2.executeQuery("select distinct distributor_id from distributor_beat_plan_view where outlet_id="+outlet_id+" limit 1");
					distributor_id = (rsDistributorID.first() ? rsDistributorID.getLong("distributor_id") : 0);
					
					
					if(distributor_id == 0 ) {
						System.out.println("select distinct id from distributor_beat_plan_users where assigned_to="+psrId+" limit 1");
						ResultSet rspjp = s2.executeQuery("select distinct id from distributor_beat_plan_users where assigned_to="+psrId+" limit 1");
						pjp = (rspjp.first()) ? rspjp.getInt("id") : 0;
						
						System.out.println("select distinct distributor_id from distributor_beat_plan where id="+pjp+" limit 1");
						ResultSet rsDistributorID2 = s2.executeQuery("select distinct distributor_id from distributor_beat_plan where id="+pjp+" limit 1");
						distributor_id = (rsDistributorID2.first() ? rsDistributorID2.getLong("distributor_id") : 0);
					}
			
					
					System.out.println("SELECT name, city_id, city, region_id, rsm_id as asm_id, (select DISPLAY_NAME from users where id=rsm_id) as asm, snd_id as rsm_id, (select DISPLAY_NAME from users where id=snd_id) as rsm FROM pep.common_distributors where distributor_id=" + distributor_id);
					ResultSet rsDistributor = s2.executeQuery("SELECT name, city_id, city, region_id, rsm_id as asm_id, (select DISPLAY_NAME from users where id=rsm_id) as asm, snd_id as rsm_id, (select DISPLAY_NAME from users where id=snd_id) as rsm FROM pep.common_distributors where distributor_id=" + distributor_id);
					if(rsDistributor.first()) {
						distributor_name = rsDistributor.getString("name");
						city_id = rsDistributor.getInt("city_id");
						city = rsDistributor.getString("city");
						region_id = rsDistributor.getInt("region_id");
						asm_id = rsDistributor.getLong("asm_id");
						asm = rsDistributor.getString("asm");
						rsm_id = rsDistributor.getLong("rsm_id");
						rsm = rsDistributor.getString("rsm");
						
					}
					
					ResultSet rsRegion = s2.executeQuery("select region_name from common_regions where region_id="+region_id);
					region = (rsRegion.first() ? rsRegion.getString("region_name") : "");
					
					System.out.println("select asm_id, (select DISPLAY_NAME from users where id=assigned_to) as tso, id , label from distributor_beat_plan_view  where assigned_to="+psrId+" and distributor_id="+distributor_id+" limit 1");
					ResultSet rsTSO = s2.executeQuery("select asm_id, (select DISPLAY_NAME from users where id=assigned_to) as tso, id , label from distributor_beat_plan_view where assigned_to="+psrId+" and distributor_id="+distributor_id+" limit 1");
					if(rsTSO.first()) {
						tso_id = rsTSO.getLong("asm_id");
						 tso = rsTSO.getString("tso");
						 pjp =rsTSO.getInt("id");
						 pjpLabel =  rsTSO.getString("label");
					}
					
					}
					
					ResultSet rsDistributor = s2.executeQuery(
							"SELECT address FROM pep.common_distributors where distributor_id=" + distributor_id);
				String	distributor_address = (rsDistributor.first()) ? rsDistributor.getString("address") : "";

					
					// long psrId = rsData.getLong("created_by");
					int total = 0;
					ResultSet rsSumOrdera = s2.executeQuery(
							"select count(distinct outlet_id) as sum_orders from mobile_order where created_by=" + psrId
									+ " and " + mobileTimestamp);
					total += (rsSumOrdera.first()) ? rsSumOrdera.getInt("sum_orders") : 0;

					ResultSet rsSumNoOrdera = s2.executeQuery(
							"select count(distinct outlet_id) as sum_no_orders from mobile_order_zero where created_by="
									+ psrId + " and " + mobileTimestamp
									+ " and outlet_id not in (select outlet_id from mobile_order where created_by = "
									+ psrId + " and " + mobileTimestamp + ")");
					total += (rsSumNoOrdera.first()) ? rsSumNoOrdera.getInt("sum_no_orders") : 0;

					ResultSet rsSumVists = s2.executeQuery(
							"select count(distinct outlet_id) as sum_visits from common_outlets_visit_duration where created_by="
									+ psrId + " and " + mobileTimestamp
									+ " and visit_type=1 and outlet_id not in (select outlet_id from mobile_order_zero where created_by="
									+ psrId + " and " + mobileTimestamp
									+ ") and outlet_id not in (select outlet_id from mobile_order where created_by="
									+ psrId + " and " + mobileTimestamp + ")");
					total += (rsSumVists.first()) ? rsSumVists.getInt("sum_visits") : 0;

					Date Start_time = null, End_time = null;

					ResultSet rs5 = s2.executeQuery(
							"select created_on from common_outlets_visit_duration where visit_type=1 and outlet_id="
									+ outlet_id + " and " + mobileTimestamp + " and created_by=" + psrId + " limit 1");
					if (rs5.first()) {
						Start_time = rs5.getTimestamp("created_on");
					}

					ResultSet rs6 = s2.executeQuery(
							"select created_on from common_outlets_visit_duration where visit_type=2 and outlet_id="
									+ outlet_id + " and " + mobileTimestamp + " and created_by=" + psrId + " limit 1");
					if (rs6.first()) {
						End_time = rs6.getTimestamp("created_on");
					}

					long Shop_Time_milliseconds = (Start_time != null && End_time != null)
							? AlmoizDateUtils.getTimeDifference(Start_time, End_time)
							: 0;
					long Shop_Time = (Shop_Time_milliseconds / 1000);

					/* Journey time */
					Date StartJourney = null;
					Date EndJourney = null;

					ResultSet rsStartJourney = s2.executeQuery(
							"SELECT created_on FROM pep.common_outlets_visit_duration where " + mobileTimestamp
									+ " and created_by=" + psrId + " and visit_type=1 order by id limit 1");
					if (rsStartJourney.first()) {
						StartJourney = rsStartJourney.getTimestamp("created_on");

					}

					ResultSet rsEndJourney = s2.executeQuery(
							"SELECT created_on FROM pep.common_outlets_visit_duration where " + mobileTimestamp
									+ " and created_by=" + psrId + " and visit_type=2 order by id desc limit 1");
					if (rsEndJourney.first()) {
						EndJourney = rsEndJourney.getTimestamp("created_on");

					}

					long Journey_Time_milliseconds = (StartJourney != null && EndJourney != null)
							? AlmoizDateUtils.getTimeDifference(StartJourney, EndJourney)
							: 0;

					long Journey_Time = (Journey_Time_milliseconds / 60000);

					double distance = (rsData.getDouble("lat") == rsData.getDouble("outlet_lat")
							&& rsData.getDouble("lng") == rsData.getDouble("outlet_lng"))
									? 1
									: AlmoizFormulas.calculateHaversineDistance(rsData.getDouble("lat"),
											rsData.getDouble("lng"), rsData.getDouble("outlet_lat"),
											rsData.getDouble("outlet_lng"));

					String insertArgs = rsData.getLong("id") + ",'" + rsData.getTimestamp("created_on") + "', "
							+ outlet_id + ", '" + rsData.getString("outlet_name") + "', '"
							+ rsData.getString("outlet_address") + "', " + rsData.getDouble("outlet_lat") + ", "
							+ rsData.getDouble("outlet_lng") + ", " + rsData.getDouble("outlet_accuracy") + ", "
							+ channel_id + ", '" + channel + "', '" + rsData.getString("outlet_contact_name") + "', '"
							+ rsData.getString("outlet_contact_number") + "', " + distributor_id + ", '"
							+ distributor_name + "', '" + distributor_address + "', "
							+ rsData.getLong("created_by") + ", '" + rsData.getString("psr_name") + "', "
							+ city_id + ", '" + city + "', "
							+ region_id + ", '" + region + "', "

							+ ( tso_id != 0 ? tso_id : null) + ", "
							+ ( tso_id != 0 ? "'" + tso + "'" : null) + ", "

							+ ( asm_id != 0 ? asm_id : null) + ", "
							+ ( asm_id != 0 ? "'" + asm + "'" : null) + ", "

							+ ( rsm_id != 0 ? rsm_id : null) + ", "
							+ ( rsm_id != 0 ? "'" + rsm + "'" : null)

							+ ", " + rsData.getDouble("lat") + ", " + rsData.getDouble("lng") + ", " + distance + ", "
							+ total + ", " + ((Start_time) != null ? "'" + Start_time + "'" : null) + ", "
							+ ((End_time) != null ? "'" + End_time + "'" : null) + ", " + Shop_Time + ", "
							+ Journey_Time + ", '" + rsData.getString("comments") + "', " + rsData.getInt("visit_type")
							+ ", "+AlmoizDateUtils.getSQLDateTime(dateForNow)+" , '" + rsData.getString("status") + "', " + pjp  + ", '"
							+ pjpLabel + "'";
 // +AlmoizDateUtils.getSQLDate(dateForNow)+
					System.out.println(count
							+ " ===> INSERT INTO `pep`.`psr_2024_visits` (`ref_id`,`mobile_timestamp`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_lat`,`outlet_lng`,`outlet_accuracy`"
							+ ",`pci_channel_id`,`channel_name`,`outlet_contact_name`,`outlet_contact_number`,`distributor_id`,`distributor_name`,`distributor_address`"
							+ ",`psr_id`,`psr_name`,`city_id`,`city_name`,`region_id`,`region_name`,`tso_id`,`tso_name`,`asm_id`,`asm_name`,`rsm_id`,`rsm_name`,"
							+ "`visit_lat`,`visit_lng`,`visit_distance`,`total_orders_day`,`start_time`,`end_time`,`shop_time`,`journey_time`,`comments`,`visit_status_id`,`created_on`,`visit_status`, `beat_plan_id`, `beat_plan_label`) VALUES("
							+ insertArgs + ")");
					s2.executeUpdate(
							"INSERT INTO `pep`.`psr_2024_visits` (`ref_id`,`mobile_timestamp`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_lat`,`outlet_lng`,`outlet_accuracy`"
									+ ",`pci_channel_id`,`channel_name`,`outlet_contact_name`,`outlet_contact_number`,`distributor_id`,`distributor_name`,`distributor_address`"
									+ ",`psr_id`,`psr_name`,`city_id`,`city_name`,`region_id`,`region_name`,`tso_id`,`tso_name`,`asm_id`,`asm_name`,`rsm_id`,`rsm_name`,"
									+ "`visit_lat`,`visit_lng`,`visit_distance`,`total_orders_day`,`start_time`,`end_time`,`shop_time`,`journey_time`,`comments`,`visit_status_id`,`created_on`,`visit_status`, `beat_plan_id`, `beat_plan_label`) VALUES("
									+ insertArgs + ")");

					

					/******** Work End Here ***********/

					
				//}
				count++;
			}
			s.close();
			s2.close();

			ds.commit();
		} catch (Exception e) {
			ds.rollback();
			e.printStackTrace();
		}
	}

}
