package com.mf.controller.visitOutlet;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import java.sql.Statement;

import com.mf.dao.MVOutletVisitRequest;
import com.mf.dao.ProductsRequest;
import com.mf.modals.ResponseModal;
import com.pbc.util.Datasource;

public class MVOutletVisit {

	public static ResponseModal mv_visit_outlet(JSONObject Payload, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ResponseModal responseModal = new ResponseModal();

		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();

			MVOutletVisitRequest OVRequest = new MVOutletVisitRequest(Payload);

			String args = OVRequest.getMobileRequestId() + ", " + OVRequest.getOutletId() + ", now(), "
					+ OVRequest.getUserId() + ", '" + OVRequest.getDeviceId() + "', " + OVRequest.getLat() + ", "
					+ OVRequest.getLng() + ", " + OVRequest.getAccuracy() + ", " + OVRequest.getDistributorId() + ", "
					+ OVRequest.getRegion_id() + ", " + OVRequest.getRsm_id() + ", " + OVRequest.getAsm_id() + ", "
					+ OVRequest.getTso_id() + ", " + OVRequest.getBeatPlanId() + ", " + OVRequest.getCity_id() + ",0, '"
					+ OVRequest.getPlatform() + "', '" + OVRequest.getVersion() + "'";

			for (int i = 1; i < 3; i++) {
				// System.out.println(
				// "INSERT INTO
				// `pep`.`common_outlets_visit_duration_MV`(`mobile_request_id`,`outlet_id`,`created_on`,`created_by`,`uuid`,`lat`,`lng`,`accuracy`,`distributor_id`,`region_id`,`snd_id`,`rsm_id`,`asm_id`,`beat_plan_id`,`city_id`,`distance_from_store`,`platform`,`version`,`visit_type`,`mobile_timestamp`)
				// VALUES("
				// + args + ", " + i + ", '"
				// + ((i == 1) ? OVRequest.getStartMobileTimestamp() :
				// OVRequest.getEndMobileTimestamp())
				// + "')");
				s.executeUpdate(
						"INSERT INTO `pep`.`common_outlets_visit_duration_MV`(`mobile_request_id`,`outlet_id`,`created_on`,`created_by`,`uuid`,`lat`,`lng`,`accuracy`,`distributor_id`,`region_id`,`snd_id`,`rsm_id`,`asm_id`,`beat_plan_id`,`city_id`,`distance_from_store`,`platform`,`version`,`visit_type`,`mobile_timestamp`) VALUES("
								+ args + ", " + i + ", '"
								+ ((i == 1) ? OVRequest.getStartMobileTimestamp() : OVRequest.getEndMobileTimestamp())
								+ "')");

			}

			/*System.out.println(
					"insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng,accuracy, mobile_timestamp, is_no_order, no_order_reason_type_v2,distributor_id,`beat_plan_id`,`region_id`,`snd_id`,`rsm_id`,`asm_id`,`comments`,`platform`,`uuid`,`app_version`) values "
							+ "(" + OVRequest.getMobileRequestId() + ", " + OVRequest.getOutletId() + ", now(), "
							+ OVRequest.getUserId() + ", " + OVRequest.getLat() + ", " + OVRequest.getLng() + ","
							+ OVRequest.getAccuracy() + ", '" + OVRequest.getEndMobileTimestamp() + "', 1, "
							+ OVRequest.getReason_id() + " ," + OVRequest.getDistributorId() + ", "
							+ OVRequest.getBeatPlanId() + "," + OVRequest.getRegion_id() + ", " + OVRequest.getRsm_id()
							+ ", " + OVRequest.getRsm_id() + ", " + OVRequest.getTso_id() + ", '"
							+ OVRequest.getComments() + "', '" + OVRequest.getPlatform() + "', '"
							+ OVRequest.getDeviceId() + "', '" + OVRequest.getVersion() + "') ");*/
			s.executeUpdate(
					"insert into mobile_order_sm_zero (mobile_order_no, outlet_id, created_on, created_by, lat, lng,accuracy, mobile_timestamp, is_no_order, no_order_reason_type_v2,distributor_id,`beat_plan_id`,`region_id`,`snd_id`,`rsm_id`,`asm_id`,`comments`,`platform`,`uuid`,`app_version`) values "
							+ "(" + OVRequest.getMobileRequestId() + ", " + OVRequest.getOutletId() + ", now(), "
							+ OVRequest.getUserId() + ", " + OVRequest.getLat() + ", " + OVRequest.getLng() + ","
							+ OVRequest.getAccuracy() + ", '" + OVRequest.getEndMobileTimestamp() + "', 1, "
							+ OVRequest.getReason_id() + " ," + OVRequest.getDistributorId() + ", "
							+ OVRequest.getBeatPlanId() + "," + OVRequest.getRegion_id() + ", " + OVRequest.getRsm_id()
							+ ", " + OVRequest.getRsm_id() + ", " + OVRequest.getTso_id() + ", '"
							+ OVRequest.getComments() + "', '" + OVRequest.getPlatform() + "', '"
							+ OVRequest.getDeviceId() + "', '" + OVRequest.getVersion() + "') ");

			long OrderID = 0;
			ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
			if (rs2.first()) {
				OrderID = rs2.getLong(1);
			}

			for (ProductsRequest Product : OVRequest.getProducts()) {
				// System.out.println(
				// "insert into
				// mobile_retailer_sm_stock(order_no,product_id,raw_cases,units,is_no_order,outlet_id,created_on,mobile_no_order_id)
				// values("
				// + OVRequest.getMobileRequestId() + "," + Product.getProduct_id() + ","
				// + Product.getQuantity() + ",0,0," + OVRequest.getOutletId() + ",now()," +
				// OrderID
				// + ")");
				s.executeUpdate(
						"insert into mobile_retailer_sm_stock(order_no,product_id,raw_cases,units,is_no_order,outlet_id,created_on,mobile_no_order_id) values("
								+ OVRequest.getMobileRequestId() + "," + Product.getProduct_id() + ","
								+ Product.getQuantity() + ",0,0," + OVRequest.getOutletId() + ",now()," + OrderID
								+ ")");
			}

			s.close();
			ds.commit();
			responseModal.setStatus(true);
			responseModal.setUserMessage("Visit add successfully");
		} catch (Exception e) {
			System.out.println("Authentication Error" + e);
			responseModal.setErrorResponse("outlet Visit Error : " + e);
		}

		return responseModal;
	}

}
