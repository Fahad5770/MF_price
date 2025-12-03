package com.pbc.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class Cache_update_Visits {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

Datasource ds = new Datasource();
		
		boolean isMTD = false;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			int NoOfDaysToGoBack = -18;

			Date date = Utilities.getDateByDays(NoOfDaysToGoBack);
			//StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(NoOfDaysToGoBack));
			//Date EndDate = StartDate;

//			if (isMTD) {
//				StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(NoOfDaysToGoBack));
//				EndDate = Utilities.getDateByDays(NoOfDaysToGoBack);
//			} else {
//				EndDate = StartDate;
//			}

			// System.out.println("StartDate : " +
			// Utilities.getDisplayDateFormat(StartDate));
			// System.out.println("EndDate : " + Utilities.getDisplayDateFormat(EndDate));
		//	Date date = StartDate;
			//while (true) {

				/******** Work Start Here ***********/

				String mobileTImestamp = " mobile_timestamp between " + Utilities.getSQLDate(date) + " and "
						+ Utilities.getSQLDateNext(date);
				//System.out.println(mobileTImestamp);

				String orderQuery = "SELECT 1 as visit_type, (SELECT label FROM pep.psr_visit_type where id=1) as status, id, mo.snd_id as rsm_id, (select DISPLAY_NAME from users u where u.id=mo.snd_id) as rsm_name, mo.asm_id as tso_id, mo.rsm_id as asm_id, (select cache_contact_number from common_outlets co where co.id=outlet_id) as outlet_contact_number, (select cache_contact_name from common_outlets co where co.id=outlet_id) as outlet_contact_name, (select pic_channel_id from common_outlets co where co.id=outlet_id) as pci_channel_id, (select accuracy from common_outlets co where co.id=outlet_id) as outlet_accuracy, mobile_order_no, beat_plan_id, (SELECT label FROM pep.distributor_beat_plan where id=beat_plan_id) as beat_plan_label, lat, lng, outlet_id,mo.mobile_timestamp,mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name, (select city_id from common_distributors cd where cd.distributor_id=mo.distributor_id) as city_id,  (select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city, mo.distributor_id as distributor_id, mo.region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng, (select address from common_outlets co where co.id=outlet_id) as outlet_address, (select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments FROM mobile_order mo  where "
						+ mobileTImestamp + " group by outlet_id, DATE(mobile_timestamp)";

				String noOrder = "SELECT 2 as visit_type, (SELECT label FROM pep.psr_visit_type where id=2) as status, id, mo.snd_id as rsm_id, (select DISPLAY_NAME from users u where u.id=mo.snd_id) as rsm_name, mo.asm_id as tso_id, mo.rsm_id as asm_id, (select cache_contact_number from common_outlets co where co.id=outlet_id) as outlet_contact_number, (select cache_contact_name from common_outlets co where co.id=outlet_id) as outlet_contact_name, (select pic_channel_id from common_outlets co where co.id=outlet_id) as pci_channel_id, (select accuracy from common_outlets co where co.id=outlet_id) as outlet_accuracy, mobile_order_no, beat_plan_id, (SELECT label FROM pep.distributor_beat_plan where id=beat_plan_id) as beat_plan_label, lat, lng, outlet_id,mo.mobile_timestamp, mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name, (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name, (select city_id from common_distributors cd where cd.distributor_id=mo.distributor_id) as city_id, (select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id, mo.region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region , (select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng, (select address from common_outlets co where co.id=outlet_id) as outlet_address, (select name from common_outlets co where co.id=outlet_id) as outlet_name,(select label from mobile_order_no_order_reason_type where id=mo.no_order_reason_type_v2) as comments  FROM mobile_order_zero mo  where mo.outlet_id not in (select outlet_id from mobile_order where "
						+ mobileTImestamp + " ) and " + mobileTImestamp + " group by outlet_id, DATE(mobile_timestamp)";

				String justVisitQuery = "SELECT 3 as visit_type, (SELECT label FROM pep.psr_visit_type where id=3) as status, id, mo.snd_id as rsm_id, (select DISPLAY_NAME from users u where u.id=mo.snd_id) as rsm_name, mo.asm_id as tso_id, mo.rsm_id as asm_id, (select cache_contact_number from common_outlets co where co.id=outlet_id) as outlet_contact_number, (select cache_contact_name from common_outlets co where co.id=outlet_id) as outlet_contact_name, (select pic_channel_id from common_outlets co where co.id=outlet_id) as pci_channel_id, (select accuracy from common_outlets co where co.id=outlet_id) as outlet_accuracy, mobile_request_id as mobile_order_no, beat_plan_id, (SELECT label FROM pep.distributor_beat_plan where id=beat_plan_id) as beat_plan_label, lat, lng, outlet_id,mo.mobile_timestamp,mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name, (select city_id from common_distributors cd where cd.distributor_id=mo.distributor_id) as city_id, (select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id, mo.region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region, (select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng, (select address from common_outlets co where co.id=outlet_id) as outlet_address, (select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments from common_outlets_visit_duration mo where mo.visit_type=1 and "
						+ mobileTImestamp + " and outlet_id not in (SELECT outlet_id FROM mobile_order where "
						+ mobileTImestamp + ") and outlet_id not in ( SELECT outlet_id FROM mobile_order_zero where "
						+ mobileTImestamp + ") group by outlet_id, DATE(mobile_timestamp) ";

				// System.out.println(orderQuery+" UNION "+ noOrder + " UNION "+
				// justVisitQuery);
				int count = 1;
				ResultSet rsData = s.executeQuery(orderQuery + " UNION " + noOrder + " UNION " + justVisitQuery);
				while (rsData.next()) {

					int channel_id = rsData.getInt("pci_channel_id");
					ResultSet rsChannel = s2
							.executeQuery("SELECT label FROM pep.pci_sub_channel where id=" + channel_id);
					String channel = (rsChannel.first()) ? rsChannel.getString("label") : "";

					long distributor_id = rsData.getLong("distributor_id");
					ResultSet rsDistributor = s2.executeQuery(
							"SELECT address FROM pep.common_distributors where distributor_id=" + distributor_id);
					String distributor_address = (rsDistributor.first()) ? rsDistributor.getString("address") : "";

					long outlet_id = rsData.getLong("outlet_id");
					//int visit_type = rsData.getInt("visit_type");
					long psrId = rsData.getLong("created_by");
					int total=0;
//					System.out.println(
//					"select count(distinct outlet_id) as sum_orders from mobile_order where created_by="
//					+ psrId + " and " + mobileTImestamp);
					ResultSet rsSumOrdera = s2.executeQuery(
							"select count(distinct outlet_id) as sum_orders from mobile_order where created_by="
									+ psrId + " and " + mobileTImestamp);
					total += (rsSumOrdera.first()) ? rsSumOrdera.getInt("sum_orders") : 0;
					

//					System.out.println(
//					"select count(distinct outlet_id) as sum_no_orders from mobile_order_zero where created_by="
//					+ psrId + " and "+ mobileTImestamp + " and outlet_id not in (select outlet_id from mobile_order where created_by = "
//					+ psrId + " and " + mobileTImestamp +")");
					ResultSet rsSumNoOrdera = s2.executeQuery(
							"select count(distinct outlet_id) as sum_no_orders from mobile_order_zero where created_by="
									+ psrId + " and "+ mobileTImestamp + " and outlet_id not in (select outlet_id from mobile_order where created_by = "
									+ psrId + " and " + mobileTImestamp +")");
					total += (rsSumNoOrdera.first()) ? rsSumNoOrdera.getInt("sum_no_orders") : 0;

//					System.out.println(
//							"select count(distinct outlet_id) as sum_visits from common_outlets_visit_duration where created_by="
//									+ psrId + " and " + mobileTImestamp + " and visit_type=1 and outlet_id not in (select outlet_id from mobile_order_zero where created_by="
//									+ psrId + " and " + mobileTImestamp	+ ") and outlet_id not in (select outlet_id from mobile_order where created_by="
//									+ psrId + " and " + mobileTImestamp + ")");
					ResultSet rsSumVists = s2.executeQuery(
							"select count(distinct outlet_id) as sum_visits from common_outlets_visit_duration where created_by="
									+ psrId + " and " + mobileTImestamp + " and visit_type=1 and outlet_id not in (select outlet_id from mobile_order_zero where created_by="
									+ psrId + " and "+ mobileTImestamp	+ ") and outlet_id not in (select outlet_id from mobile_order where created_by="
									+ psrId + " and " + mobileTImestamp + ")");
					total += (rsSumVists.first()) ? rsSumVists.getInt("sum_visits") : 0;

				//	totalVisited += visits;
					

					Date Start_time = null, End_time = null;
					
//					System.out.println(
//							"select mobile_timestamp from common_outlets_visit_duration where visit_type=1 and outlet_id="
//									+ outlet_id + " and " + mobileTImestamp + " and created_by=" + psrId + " limit 1");
					ResultSet rs5 = s2.executeQuery(
							"select mobile_timestamp from common_outlets_visit_duration where visit_type=1 and outlet_id="
									+ outlet_id + " and " + mobileTImestamp + " and created_by=" + psrId + " limit 1");
					if (rs5.first()) {
						Start_time = rs5.getTimestamp("mobile_timestamp");
					}

//					System.out.println(
//							"select mobile_timestamp from common_outlets_visit_duration where visit_type=2 and outlet_id="
//									+ outlet_id + " and " + mobileTImestamp + " and created_by=" + psrId + " limit 1");
					ResultSet rs6 = s2.executeQuery(
							"select mobile_timestamp from common_outlets_visit_duration where visit_type=2 and outlet_id="
									+ outlet_id + " and " + mobileTImestamp + " and created_by=" + psrId + " limit 1");
					if (rs6.first()) {
						End_time = rs6.getTimestamp("mobile_timestamp");
					}

					long Shop_Time_milliseconds = (Start_time != null && End_time != null)
							? getTimeDifference(Start_time, End_time)
							: 0;
					// System.out.println(Shop_Time_milliseconds);
//			/long Shop_Time = (Shop_Time_milliseconds / 60000);
					long Shop_Time = (Shop_Time_milliseconds / 1000);
					// Shop_Time= (Shop_Time!=0) ? Shop_Time+1 : Shop_Time+0;

					/* Journey time */
					Date StartJourney = null;
					Date EndJourney = null;

//					System.out.println("SELECT mobile_timestamp FROM pep.common_outlets_visit_duration where "
//							+ mobileTImestamp + " and created_by=" + psrId + " and visit_type=1 order by id limit 1");
					ResultSet rsStartJourney = s2.executeQuery(
							"SELECT mobile_timestamp FROM pep.common_outlets_visit_duration where " + mobileTImestamp
									+ " and created_by=" + psrId + " and visit_type=1 order by id limit 1");
					if (rsStartJourney.first()) {
						StartJourney = rsStartJourney.getTimestamp("mobile_timestamp");
						// System.out.println(StartJourney);
					}

//					System.out.println(
//							"SELECT mobile_timestamp FROM pep.common_outlets_visit_duration where " + mobileTImestamp
//									+ " and created_by=" + psrId + " and visit_type=2 order by id desc limit 1");
					ResultSet rsEndJourney = s2.executeQuery(
							"SELECT mobile_timestamp FROM pep.common_outlets_visit_duration where " + mobileTImestamp
									+ " and created_by=" + psrId + " and visit_type=2 order by id desc limit 1");
					if (rsEndJourney.first()) {
						EndJourney = rsEndJourney.getTimestamp("mobile_timestamp");
						// System.out.println(EndJourney);
					}

					long Journey_Time_milliseconds = (StartJourney != null && EndJourney != null)
							? getTimeDifference(StartJourney, EndJourney)
							: 0;

					long Journey_Time = (Journey_Time_milliseconds / 60000);
					// Journey_Time= (Journey_Time!=0) ? Journey_Time+1 : Journey_Time+0;

					double distance =
							(rsData.getDouble("lat") == rsData.getDouble("outlet_lat") && rsData.getDouble("lng") == rsData.getDouble("outlet_lng"))
							? 1
							: HaversineDistanceCalculator.calculateHaversineDistance(rsData.getDouble("lat"),
							rsData.getDouble("lng"), rsData.getDouble("outlet_lat"), rsData.getDouble("outlet_lng"));
					
					

					String insertArgs = rsData.getLong("id") + ",'" + rsData.getTimestamp("mobile_timestamp") + "', "
							+ outlet_id + ", '" + rsData.getString("outlet_name") + "', '"
							+ rsData.getString("outlet_address") + "', " + rsData.getDouble("outlet_lat") + ", "
							+ rsData.getDouble("outlet_lng") + ", " + rsData.getDouble("outlet_accuracy") + ", "
							+ channel_id + ", '" + channel + "', '" + rsData.getString("outlet_contact_name") + "', '"
							+ rsData.getString("outlet_contact_number") + "', " + distributor_id + ", '"
							+ rsData.getString("Dis_name") + "', '" + distributor_address + "', "
							+ rsData.getLong("created_by") + ", '" + rsData.getString("psr_name") + "', "
							+ rsData.getInt("city_id") + ", '" + rsData.getString("city") + "', "
							+ rsData.getInt("region_id") + ", '" + rsData.getString("region") + "', "
							
							+ (rsData.getLong("tso_id") != 0 ? rsData.getLong("tso_id") : null) + ", "
							+ (rsData.getLong("tso_id") != 0 ? "'"+rsData.getString("tso_name")+"'" : null) + ", "
							
							+ (rsData.getLong("asm_id") != 0 ? rsData.getLong("asm_id") : null) + ", "
							+ (rsData.getLong("asm_id") != 0 ? "'"+rsData.getString("asm_name")+"'" : null) + ", "
							
							+ (rsData.getLong("rsm_id") != 0 ? rsData.getLong("rsm_id") : null) + ", "
							+ (rsData.getLong("rsm_id") != 0 ? "'"+rsData.getString("rsm_name")+"'" : null)  

							+ ", " + rsData.getDouble("lat") + ", "
							+ rsData.getDouble("lng") + ", " + distance + ", " + total + ", " + ((Start_time) != null ? "'"+Start_time+"'" : null) + ", "
							+ ((End_time) != null ? "'"+End_time+"'" : null)  + ", " + Shop_Time + ", " + Journey_Time + ", '" + rsData.getString("comments")
							+ "', " + rsData.getInt("visit_type") + ", now(), '" + rsData.getString("status") + "', "
							+ rsData.getInt("beat_plan_id") + ", '" + rsData.getString("beat_plan_label") + "'";

					System.out.println(
							count+" ===> INSERT INTO `pep`.`psr_2024_visits` (`ref_id`,`mobile_timestamp`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_lat`,`outlet_lng`,`outlet_accuracy`"
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
					count++;
				}

				/******** Work End Here ***********/

//				if (date.equals(EndDate)) {
//					break;
//				}
//				date = DateUtils.addDays(date, 1);
//			}

			s.close();
			s2.close();
			
			ds.commit();

		} catch (Exception e) {
			ds.rollback();
			e.printStackTrace();
		}

	}

	

	public static long getTimeDifference(Date startTime, Date endTime) {

		long startMillis = startTime.getTime();
		long endMillis = endTime.getTime();
		long timeDifferenceMillis = endMillis - startMillis;
	//	System.out.println("Time difference in milliseconds: " + timeDifferenceMillis);
		return endMillis - startMillis;

	}

}
