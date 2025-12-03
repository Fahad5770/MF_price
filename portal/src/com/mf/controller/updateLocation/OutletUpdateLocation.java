package com.mf.controller.updateLocation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.interfaces.IOutletUpdateLocation;
import com.mf.modals.ResponseModal;
import com.pbc.util.Datasource;

public class OutletUpdateLocation implements IOutletUpdateLocation {

	@Override
	public ResponseModal UpdateOutletLocation(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {

		// TODO Auto-generated method stub
		return updateOutletLocation(jsonData, request);
	}

	private ResponseModal updateOutletLocation(JSONObject jsonData, HttpServletRequest request) {
		ResponseModal ResponseModal = new ResponseModal();

		final Number UserId = (Number) jsonData.get("user_id");
		final String MobilerequestId = (String) jsonData.get("mobile_request_id");
		final String DeviceId = (String) jsonData.get("device_id");
		final long OutletId = (Long) jsonData.get("outlet_id");
		final double Lat = (Double) jsonData.get("lat");
		final double Lng = (Double) jsonData.get("lng");
		final long Accuracy = Math.round((Double) jsonData.get("accuracy"));
		final String mobileTimestamp = (String) jsonData.get("mobile_timestamp");
		final String Version = (String) jsonData.get("version");
		final String Platform = (String) jsonData.get("platform");
		long DistributorId = 0;

		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();

			double oldLat = 0.0, oldLng = 0.0, oldAccuracy = 0.0;

			ResultSet rsOldLocation = s
					.executeQuery("select lat, lng, accuracy,distributor_id from common_outlets where id=" + OutletId);
			if (rsOldLocation.first()) {
				oldLat = rsOldLocation.getDouble("lat");
				oldLng = rsOldLocation.getDouble("lng");
				oldAccuracy = rsOldLocation.getDouble("accuracy");
				DistributorId = rsOldLocation.getLong("distributor_id");
			}

			ds.startTransaction();

			// logs_update_location

			s.executeUpdate(
					"INSERT INTO `peplogs`.`logs_update_location`(`outlet_id`, `lat`, `lng`, `accuracy`, `updated_by`,`distributor_id`,`mobile_request_id`,`device_id`,`mobile_timestamp`,`platform`,`version`,`beat_plan_id`)"
							+ "values(" + OutletId + ", " + oldLat + ", " + oldLng + ", " + oldAccuracy + " , " + UserId
							+ ", " + DistributorId + ", '" + MobilerequestId + "', '" + DeviceId + "', '"
							+ mobileTimestamp + "', '" + Platform + "', '" + Version + "', " + 0 + ")");
			s.executeUpdate("update common_outlets set lat=" + Lat + ", lng=" + Lng + ", accuracy=" + Accuracy
					+ " where id=" + OutletId);

			ResponseModal.setStatus(true);
			ResponseModal.setUserMessage("Outlet Location Update successfully");

			ds.commit();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Update Location Error :- " + e1);
				ResponseModal.setErrorResponse("server Error " + e1);
			}

			System.out.println("Update Location Error :- " + e);
			ResponseModal.setErrorResponse("server Error " + e);
		}

		return ResponseModal;
	}

}
