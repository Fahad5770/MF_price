package com.mf.controller.attendance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.dao.AttendanceRequest;
import com.mf.interfaces.IAttendance;
import com.mf.modals.ResponseModal;
import com.mf.utils.MFPathUtils;
import com.mf.utils.MFAPIFunctions;
import com.mf.utils.MFConfig;
import com.pbc.util.Datasource;
import com.mf.utils.MFDateUtils;

public class Attendance implements IAttendance {

	@Override
	public ResponseModal checkIn(JSONObject payload, HttpServletRequest request, int is_mv_app)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		return insertAttendance(payload, request, 1, is_mv_app);
	}

	@Override
	public ResponseModal checkOut(JSONObject payload, HttpServletRequest request, int is_mv_app)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {

		System.out.println("checkout");

		return insertAttendance(payload, request, 2, is_mv_app);
	}

	@SuppressWarnings("resource")
	private ResponseModal insertAttendance(JSONObject payload, HttpServletRequest request, int attendanceType,
			int is_mv_app) throws ClassNotFoundException, IllegalAccessException, IllegalStateException, IOException,
			ServletException, InstantiationException, FileUploadException {

		ResponseModal responseModal = new ResponseModal();

		AttendanceRequest AR = new AttendanceRequest(payload, is_mv_app);

		Date d = new Date();

		Datasource ds = new Datasource();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			String DistributorIds = MFAPIFunctions.GetDistributorsOfUser(AR.getUser_id());

			boolean Flag = false;
			long CalculatedDistance=0;
//			System.out.println("Location Query ====>"
//					+ "SELECT * FROM common_distributor_location where distributor_id in(" + DistributorIds + ")");
			ResultSet rsLocations = s.executeQuery(
					"SELECT * FROM common_distributor_location where distributor_id in(" + DistributorIds + ")");
			while (rsLocations.next()) {
				 CalculatedDistance = MFAPIFunctions.GetDistance(rsLocations.getString("lat"),
						rsLocations.getString("lng"), AR.getLat(), AR.getLng());
				if(CalculatedDistance <= 250) {
					Flag=true;
				}
			}
			if (!Flag) {
				responseModal.setErrorResponse(
						"Your attendance could not be marked because your distance is too far from the office that is : "+CalculatedDistance);
				return responseModal;
			}

			Date date = MFDateUtils.parseDateYYYYMMDD(AR.getMobileTimeStamp());
//			System.out.println("select * from mobile_order_booker_attendance where created_by=" + AR.getUser_id()
//					+ " and attendance_type=" + attendanceType + " and mobile_timestamp between "
//					+ MFDateUtils.getSQLDate(date) + " and " + MFDateUtils.getSQLDateNext(date));
			ResultSet rsCheckExistts = s.executeQuery("select * from mobile_order_booker_attendance where created_by="
					+ AR.getUser_id() + " and attendance_type=" + attendanceType + " and mobile_timestamp between "
					+ MFDateUtils.getSQLDate(date) + " and " + MFDateUtils.getSQLDateNext(date));
			if (rsCheckExistts.first()) {
				responseModal.setUserMessage("Check " + ((attendanceType == 1) ? "In" : "Out") + " marked already.");
				responseModal.setStatus(true);
				return responseModal;
			}

			int month = MFDateUtils.getMonthNumberByDate(d);
			int year = MFDateUtils.getYearByDate(d);
			int day = MFDateUtils.getDayNumberByDate(d);
			
			String uploadDir = MFPathUtils.getFilePathWithDay(MFConfig.Folders.Attendance.getFolderName(), year, month, day);

			String fileName = AR.getUser_id() + "_" + System.currentTimeMillis() + ".jpg";

			File outputFile = new File(uploadDir, fileName);
			// Write the file
			FileOutputStream fos = new FileOutputStream(outputFile);
			System.out.println(AR.getImageBytes());
			fos.write(AR.getImageBytes());
			fos.flush();
			
			ds.startTransaction();
			
//			System.out.println(
//					"insert into mobile_order_booker_attendance (uuid,  created_on, created_by, lat, lng, accuracy, mobile_timestamp, attendance_type, device_id, platform, distributor_id, version, filename, uri,year,month,day, is_mv) values "
//							+ "(" + AR.getMobile_request_id() + ",  now(), " + AR.getUser_id() + ", " + AR.getLat()
//							+ ", " + AR.getLng() + "," + AR.getAccuracy() + ", '" + AR.getMobileTimeStamp() + "',"
//							+ attendanceType + ",'" + AR.getDevice_id() + "','" + AR.getPlatform() + "',  1 , '"
//							+ AR.getVersion() + "', '" + fileName + "', '" + (uploadDir + "/" + fileName) + "', " + year
//							+ ", " + month + ", "+day+", "+is_mv_app+")");

			s.executeUpdate(
					"insert into mobile_order_booker_attendance (uuid,  created_on, created_by, lat, lng, accuracy, mobile_timestamp, attendance_type, device_id, platform, distributor_id, version, filename, uri,year,month,day, is_mv) values "
							+ "(" + AR.getMobile_request_id() + ",  now(), " + AR.getUser_id() + ", " + AR.getLat()
							+ ", " + AR.getLng() + "," + AR.getAccuracy() + ", '" + AR.getMobileTimeStamp() + "',"
							+ attendanceType + ",'" + AR.getDevice_id() + "','" + AR.getPlatform() + "',  1 , '"
							+ AR.getVersion() + "', '" + fileName + "', '" + (uploadDir + "/" + fileName) + "', " + year
							+ ", " + month + ", "+day+", "+is_mv_app+")");

			responseModal.setStatus(true);
			responseModal.setUserMessage("Check "+ ((attendanceType == 1) ? "In" : "Out") +" Mark Successfully");

			ds.commit();

			s.close();
			ds.dropConnection();

		} catch (Exception e) {
			System.out.println("api/attendance Error");
			responseModal.setErrorResponse("Server Error " + e);
		}

		return responseModal;
	}

}
