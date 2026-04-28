package com.mf.controller.reports;



import com.mf.modals.ResponseModal;
import com.mf.dao.NoOrderReportResponse;
import com.mf.dao.OrderReportResponse;
import com.mf.dao.OutletDetailsOrdersReport;
import com.mf.dao.OutletDetailsStrikeReport;
import com.mf.dao.OutletOrdersReport;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mf.dao.OutletStrikeReport;
import com.mf.dao.StrikeRateReportResponse;
import com.mf.dao.noOrderResponseProducts;
import com.mf.interfaces.IMobileReports;

public  class MobileReports implements IMobileReports{

	@Override
	public ResponseModal MVStrikeRateReport(JSONObject jsonData, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return mv_strike_rate_report(jsonData, request);
	}
	@Override
	public ResponseModal MVNoOrderReport(JSONObject jsonData, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return mv_no_order_report(jsonData, request);
	}
	@Override
	public ResponseModal OrdersReport(JSONObject jsonData, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return orders_report(jsonData, request);
	}
	
	
	@SuppressWarnings("unused")
	private ResponseModal mv_strike_rate_report(JSONObject jsonData, HttpServletRequest request) {
		ResponseModal ResponseModal = new ResponseModal();

		final Number UserId = (Number) jsonData.get("user_id");
		final Number cityId = (Number) jsonData.get("city_id");

		final String StartDate = (String) jsonData.get("start_date");
		Date sDate = Utilities.parseDateYYYYMMDD(StartDate);
		final String EndDate = (String) jsonData.get("end_date");
		Date eDate = Utilities.parseDateYYYYMMDD(EndDate);
		String WhereCities = "";
	
		 
			if (cityId != null && cityId.intValue() == 0) {
				WhereCities = "and 1=1";
			}else {
				WhereCities = "and city_id="+cityId+"";
			}
		System.out.println(eDate);
		Datasource ds = new Datasource();
		int regionId = 0;
		String city = "";
		try {

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			double grandTotal = 0;
			// long distributorId = 0;
			

			System.out.println(
					"SELECT distinct distributor_id, (select city from common_distributors co where co.distributor_id=pjpv.distributor_id) city, (select region_id from common_distributors co where co.distributor_id=pjpv.distributor_id) as region_id  FROM distributor_beat_plan_view pjpv");
			ResultSet rsDistributor = s.executeQuery(
					"SELECT distinct distributor_id, (select city from common_distributors co where co.distributor_id=pjpv.distributor_id) city, (select region_id from common_distributors co where co.distributor_id=pjpv.distributor_id) as region_id  FROM distributor_beat_plan_view pjpv where   assigned_to="
							+ UserId);
			if (rsDistributor.first()) {
				city = rsDistributor.getString("city");
				regionId = rsDistributor.getInt("region_id");
			}
			ResultSet rsSoName = s.executeQuery("select DISPLAY_NAME from users where id=" + UserId);
			String soName = (rsSoName.first()) ? rsSoName.getString("DISPLAY_NAME") : "";

			ResultSet rsRegion = s.executeQuery("select region_name from common_regions where region_id=" + regionId);
			String region = (rsRegion.first()) ? rsRegion.getString("region_name") : "";

			StrikeRateReportResponse orderReportResponse = new StrikeRateReportResponse(StartDate, region,
					city);
			System.out.println("orderReportResponse : "+city);
			List<OutletStrikeReport> OutletOrdersReportList = new ArrayList<OutletStrikeReport>();

			System.out.println("SELECT DISTINCT dbpv.assigned_to, u.DISPLAY_NAME FROM distributor_beat_plan_view dbpv JOIN users u ON u.ID = dbpv.assigned_to where rsm_id="+UserId+" or asm_Id="+UserId+" or snd_id="+UserId);
			ResultSet rsUsers = s.executeQuery("SELECT DISTINCT dbpv.assigned_to, u.DISPLAY_NAME FROM distributor_beat_plan_view dbpv JOIN users u ON u.ID = dbpv.assigned_to where rsm_id="+UserId+" or asm_Id="+UserId+" or snd_id="+UserId);
			while (rsUsers.next()) {
				String userName = rsUsers.getString("u.DISPLAY_NAME");
				long userId = rsUsers.getLong("dbpv.assigned_to");
				//double totalOrderAmount = 0.0;
				List<OutletDetailsStrikeReport> outletDetailsOrdersReportlist = new ArrayList<OutletDetailsStrikeReport>();
				OutletStrikeReport outletOrdersReport = new OutletStrikeReport();
				System.out.println("SELECT (SELECT COUNT(DISTINCT mo.outlet_id) FROM mobile_order mo WHERE mo.mobile_timestamp BETWEEN " + Utilities.getSQLDate(sDate) + " AND " + Utilities.getSQLDateNext(eDate) + " AND mo.created_by=" + userId + ") AS visit_count, (SELECT COUNT(DISTINCT moz.outlet_id) FROM mobile_order_zero moz WHERE moz.mobile_timestamp BETWEEN " + Utilities.getSQLDate(sDate) + " AND " + Utilities.getSQLDateNext(eDate) + " AND moz.created_by=" + userId + ") AS no_visit_count");
					ResultSet rsOrderDetails = s2.executeQuery("SELECT (SELECT COUNT(DISTINCT mo.outlet_id) FROM mobile_order mo WHERE mo.mobile_timestamp BETWEEN " + Utilities.getSQLDate(sDate) + " AND " + Utilities.getSQLDateNext(eDate) + " AND mo.created_by=" + userId + ") AS visit_count, (SELECT COUNT(DISTINCT moz.outlet_id) FROM mobile_order_zero moz WHERE moz.mobile_timestamp BETWEEN " + Utilities.getSQLDate(sDate) + " AND " + Utilities.getSQLDateNext(eDate) + " AND moz.created_by=" + userId + ") AS no_visit_count");
				while (rsOrderDetails.next()) {

					// long ProductID = rs3.getInt("product_id");
					int visitCount = rsOrderDetails.getInt("visit_count");
					int noVisitCount = rsOrderDetails.getInt("no_visit_count");
					int totalVisits = visitCount + noVisitCount;
				//	int orderBookerName = totalVisits;

					OutletDetailsStrikeReport outletDetailsOrdersReport = new OutletDetailsStrikeReport(visitCount,
							noVisitCount, totalVisits);
					outletDetailsOrdersReportlist.add(outletDetailsOrdersReport);

				}
				//grandTotal += totalOrderAmount;
				//String outletAmount = Utilities.getDisplayCurrencyFormat(totalOrderAmount);
				outletOrdersReport.setUser(userId + "-" + userName);
				//outletOrdersReport.setTotalAmount(outletAmount);
				outletOrdersReport.setOrderDetails(outletDetailsOrdersReportlist);

				OutletOrdersReportList.add(outletOrdersReport);

			}

			orderReportResponse.setGrandTotal(Utilities.getDisplayCurrencyFormat(grandTotal));
			orderReportResponse.setOrder(OutletOrdersReportList);

			ResponseModal.setStatus(true);
			ResponseModal.setData(orderReportResponse.getIntoJson());
			s.close();
			s2.close();
			ds.dropConnection();
		} catch (

		Exception e) {

			e.printStackTrace();
			System.out.println("Orders Report Error :- " + e);
			ResponseModal.setErrorResponse("server Error " + e);
		}

		return ResponseModal;
	}

	@SuppressWarnings("unused")
	private ResponseModal mv_no_order_report(JSONObject jsonData, HttpServletRequest request) {
	    ResponseModal responseModal = new ResponseModal();
	    final Number userId = (Number) jsonData.get("user_id");
	    final String startDate = (String) jsonData.get("start_date");
	    Date sDate = Utilities.parseDateYYYYMMDD(startDate);
	    final String endDate = (String) jsonData.get("end_date");
	    Date eDate = Utilities.parseDateYYYYMMDD(endDate);
	    System.out.println(eDate);

	    Datasource ds = new Datasource();

	    try {
	        ds.createConnection();
	        Statement s = ds.createStatement();

	        List<NoOrderReportResponse> reportList = new ArrayList<>();

	        String query = "SELECT moz.id , CONCAT(moz.outlet_id, ' - ', co.name) AS outletName, moz.created_on, " +
	                       "CONCAT(moz.created_by, ' - ', u.DISPLAY_NAME) AS user, moz.mobile_order_no, mor.label AS noOrderReasonType " +
	                       "FROM pep.mobile_order_zero AS moz " +
	                       "JOIN pep.mobile_order_no_order_reason_type AS mor ON moz.no_order_reason_type_v2 = mor.id " +
	                       "JOIN common_outlets co ON moz.outlet_id = co.id " +
	                       "JOIN users u ON u.ID = moz.created_by " +
	                       "WHERE moz.created_on BETWEEN " + Utilities.getSQLDate(sDate) + " AND " + Utilities.getSQLDateNext(eDate) + 
	                       " AND moz.created_by=" + userId;

	        System.out.println(query);
	        ResultSet rsOutlets = s.executeQuery(query);

	        while (rsOutlets.next()) {
	            String outletName = rsOutlets.getString("outletName");
	            String createdOn = rsOutlets.getString("created_on");
	            String user = rsOutlets.getString("user");
	            String mobileOrderNo = rsOutlets.getString("mobile_order_no");
	            String noOrderReasonType = rsOutlets.getString("noOrderReasonType");

	            // ✅ Create response object without products
	            NoOrderReportResponse response = new NoOrderReportResponse();
	            response.setOutetName(outletName);
	            response.setCreatedOn(createdOn);
	            response.setMobile_order_no(mobileOrderNo);
	            response.setNoOrderReasonType(noOrderReasonType);
	            response.setUser(user);

	            reportList.add(response);
	        }

	        // Convert list to JSONArray
	        JSONArray responseArray = new JSONArray();
	        for (NoOrderReportResponse report : reportList) {
	            responseArray.add(report.getIntoJson());
	        }

	        responseModal.setStatus(true);
	        LinkedHashMap<String, Object> responseWrapper = new LinkedHashMap<>();
	        responseWrapper.put("data", responseArray);
	        responseModal.setData(responseWrapper);

	        rsOutlets.close();
	        s.close();
	        ds.dropConnection();

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Orders Report Error :- " + e);
	        responseModal.setErrorResponse("Server Error: " + e.getMessage());
	    }

	    return responseModal;
	}

	
	
	private ResponseModal orders_report(JSONObject jsonData, HttpServletRequest request) {
		ResponseModal ResponseModal = new ResponseModal();

		final Number UserId = (Number) jsonData.get("user_id");
		//final Number BeatPlanId = (Number) jsonData.get("beat_plan_id");
		final String StartDate = (String) jsonData.get("start_date");
		Date sDate = Utilities.parseDateYYYYMMDD(StartDate);
		final String EndDate = (String) jsonData.get("end_date");
		Date eDate = Utilities.parseDateYYYYMMDD(EndDate);
		//System.out.println("BeatPlanId"+BeatPlanId);
		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			double grandTotal = 0;
			long distributorId = 0;
			int regionId = 0;
			String city = "";
//System.out.println(
//		"SELECT distinct distributor_id, (select city from common_distributors co where co.distributor_id=pjpv.distributor_id) city, (select region_id from common_distributors co where co.distributor_id=pjpv.distributor_id) as region_id  FROM distributor_beat_plan_view pjpv where id="
//				+ BeatPlanId + " and assigned_to=" + UserId);
				System.out.println("inside If");
				System.out.println("select DISPLAY_NAME from users where id=" + UserId);
				ResultSet rsSoName = s.executeQuery("select DISPLAY_NAME from users where id=" + UserId);
				String soName = (rsSoName.first()) ? rsSoName.getString("DISPLAY_NAME") : "";

//				ResultSet rsRegion = s.executeQuery("select region_name from common_regions where region_id=" + regionId);
//				String region = (rsRegion.first()) ? rsRegion.getString("region_name") : "";

				OrderReportResponse orderReportResponse = new OrderReportResponse(soName, StartDate, "0", city);
				List<OutletOrdersReport> OutletOrdersReportList = new ArrayList<OutletOrdersReport>();
				System.out.println("select id, name from common_outlets where id in("
						+ "SELECT outlet_id FROM mobile_order where  mobile_timestamp between " + Utilities.getSQLDate(sDate) + " and "
						+ Utilities.getSQLDateNext(eDate) + " and created_by= " + UserId+")");

				ResultSet rsOutlets = s.executeQuery("select id, name from common_outlets where id in("
						+ "SELECT outlet_id FROM mobile_order where  mobile_timestamp between " + Utilities.getSQLDate(sDate) + " and "
						+ Utilities.getSQLDateNext(eDate) + " and created_by= " + UserId+")");
				while (rsOutlets.next()) {
					String outlet = rsOutlets.getString("name");
					long outletId = rsOutlets.getLong("id");
					double totalOrderAmount = 0.0;
					List<OutletDetailsOrdersReport> outletDetailsOrdersReportlist = new ArrayList<OutletDetailsOrdersReport>();

					OutletOrdersReport outletOrdersReport = new OutletOrdersReport();
					System.out.println(
							"SELECT product_id,(select brand_label from inventory_products_view ipv where ipv.product_id = mop.product_id) , (select package_label from inventory_products_view ipv where ipv.product_id = mop.product_id), sum(raw_cases), sum(net_amount),"
									+ "(select brand_id from inventory_products_view ipv where ipv.product_id = mop.product_id) FROM mobile_order_products mop where id "
									+ "in(SELECT id FROM mobile_order where  mobile_timestamp between " + Utilities.getSQLDate(sDate) + " and "
									+ Utilities.getSQLDateNext(eDate) + " and created_by= " + UserId + " and outlet_id ="
									+ outletId + "" + ") group by product_id");

					ResultSet rsOrderDetails = s2.executeQuery(
							"SELECT product_id,(select brand_label from inventory_products_view ipv where ipv.product_id = mop.product_id) , (select package_label from inventory_products_view ipv where ipv.product_id = mop.product_id), sum(raw_cases), sum(net_amount),"
									+ "(select brand_id from inventory_products_view ipv where ipv.product_id = mop.product_id) FROM mobile_order_products mop where id "
									+ "in(SELECT id FROM mobile_order where  mobile_timestamp between " + Utilities.getSQLDate(sDate) + " and "
									+ Utilities.getSQLDateNext(eDate) + " and created_by= " + UserId + " and outlet_id ="
									+ outletId + "" + ") group by product_id");
					while (rsOrderDetails.next()) {

						// long ProductID = rs3.getInt("product_id");
						String productLabel = rsOrderDetails.getString(3) + "-" + rsOrderDetails.getString(2);
						int quantity = rsOrderDetails.getInt(4);
						String amount = Utilities.getDisplayCurrencyFormat(rsOrderDetails.getDouble(5));

						OutletDetailsOrdersReport outletDetailsOrdersReport = new OutletDetailsOrdersReport(productLabel,
								quantity, amount);
						totalOrderAmount += rsOrderDetails.getDouble(5);
						outletDetailsOrdersReportlist.add(outletDetailsOrdersReport);

					}
					grandTotal += totalOrderAmount;
					String outletAmount = Utilities.getDisplayCurrencyFormat(totalOrderAmount);
					outletOrdersReport.setOutlet(outletId + "-" + outlet);
					outletOrdersReport.setTotalAmount(outletAmount);
					outletOrdersReport.setOrderDetails(outletDetailsOrdersReportlist);

					OutletOrdersReportList.add(outletOrdersReport);

				}
				orderReportResponse.setGrandTotal(Utilities.getDisplayCurrencyFormat(grandTotal));
				orderReportResponse.setOrder(OutletOrdersReportList);

				ResponseModal.setStatus(true);
				ResponseModal.setData(orderReportResponse.getIntoJson());
				s.close();
				s2.close();
				ds.dropConnection();
			
			

	
		} catch (

		Exception e) {

			e.printStackTrace();
			System.out.println("Orders Report Error :- " + e);
			ResponseModal.setErrorResponse("server Error " + e);
		}

		return ResponseModal;
	}

	
}
