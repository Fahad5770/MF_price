package com.mf.controller.noOrder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.dao.NoORderRequest;
import com.mf.dao.NoORderRequestProducts;
//import com.mmc.dao.NoORderRequest;
//import com.mmc.dao.NoORderRequestProducts;
import com.mf.interfaces.INoOrder;
//import com.mmc.modals.ResponseModal;
//import com.mmc.utils.MFConfig;
//import com.mmc.utils.MFPathUtils;

import com.mf.utils.MFConfig;
import com.mf.utils.MFPathUtils;
import com.mf.utils.MFSQLUtils;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.mf.modals.ResponseModal;

import com.pbc.util.Datasource;
import com.pbc.util.AlmoizDateUtils;

public class NoOrder implements INoOrder {

	public NoOrder() {

	}

	@Override
	public ResponseModal InsertNoOrder(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		return insertNoOrder(jsonData, request);
	}

	@SuppressWarnings("resource")
	private ResponseModal insertNoOrder(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, IOException, ServletException,
			InstantiationException, FileUploadException {

		ResponseModal ResponseModal = new ResponseModal();

		NoORderRequest noORderRequest = new NoORderRequest(Payload);
		System.out.println(noORderRequest.toString());

		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();

			ResultSet rsNoOrderExists = s.executeQuery(
					"SELECT id from mobile_order_zero where mobile_order_no = " + noORderRequest.getMobileRequestId());
			if (rsNoOrderExists.first()) {
				System.out.println(
						"INSERT INTO `pep`.`mobile_request_duplication`(`mobile_request_id`,`mobile_timestamp`,`request_nature`,`created_by`,`lat`,`lng`,`accuracy`,`platform`,`version`,`outlet_id`,`distributor_id`,`region_id`,`beat_plan_id`,`no_order_reason_type`)"
								+ "VALUES(" + noORderRequest.getMobileRequestId() + ", '"
								+ noORderRequest.getEndMobileTimestamp() + "', 'No Order', "
								+ noORderRequest.getUserId() + ", " + noORderRequest.getLat() + ", "
								+ noORderRequest.getLng() + ", " + noORderRequest.getAccuracy() + ", '"
								+ noORderRequest.getPlatform() + "', '" + noORderRequest.getVersion() + "', "
								+ noORderRequest.getOutletId() + ", " + noORderRequest.getDistributorId()
								+ ", (select region_id from common_distributors where distributor_id="
								+ noORderRequest.getDistributorId() + "), " + noORderRequest.getBeatPlanId() + ", "
								+ noORderRequest.getReasonId() + " )");
				s.executeUpdate(
						"INSERT INTO `pep`.`mobile_request_duplication`(`mobile_request_id`,`mobile_timestamp`,`request_nature`,`created_by`,`lat`,`lng`,`accuracy`,`platform`,`version`,`outlet_id`,`distributor_id`,`region_id`,`beat_plan_id`,`no_order_reason_type`)"
								+ "VALUES(" + noORderRequest.getMobileRequestId() + ", '"
								+ noORderRequest.getEndMobileTimestamp() + "', 'No Order', "
								+ noORderRequest.getUserId() + ", " + noORderRequest.getLat() + ", "
								+ noORderRequest.getLng() + ", " + noORderRequest.getAccuracy() + ", '"
								+ noORderRequest.getPlatform() + "', '" + noORderRequest.getVersion() + "', "
								+ noORderRequest.getOutletId() + ", " + noORderRequest.getDistributorId()
								+ ", (select region_id from common_distributors where distributor_id="
								+ noORderRequest.getDistributorId() + "), " + noORderRequest.getBeatPlanId() + ", "
								+ noORderRequest.getReasonId() + " )");
				// ResponseModal.setErrorResponse("No Order already exists for request " +
				// mobilerequestId);
				ResponseModal.setStatus(true);
				return ResponseModal;
			}

			System.out.println(
					"insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by,distributor_id,beat_plan_id,region_id,rsm_id,snd_id, lat, lng, accuracy, uuid, mobile_timestamp, is_no_order, no_order_reason_type_v2, platform,app_version,comments,start_mobile_timestamp) values ('"
							+ noORderRequest.getMobileRequestId() + "', " + noORderRequest.getOutletId() + ", now(), "
							+ noORderRequest.getUserId() + ", " + noORderRequest.getDistributorId() + ", "
							+ noORderRequest.getBeatPlanId()
							+ " , (select region_id from common_distributors where distributor_id = "
							+ noORderRequest.getDistributorId()
							+ ") , (select rsm_id from common_distributors where distributor_id = "
							+ noORderRequest.getDistributorId()
							+ "), (select snd_id from common_distributors where distributor_id = "
							+ noORderRequest.getDistributorId() + "),  " + noORderRequest.getLat() + ", "
							+ noORderRequest.getLng() + ", " + noORderRequest.getAccuracy() + " , '"
							+ noORderRequest.getDeviceId() + "' ,'" + noORderRequest.getEndMobileTimestamp() + "', 1, "
							+ noORderRequest.getReasonId() + ", '" + noORderRequest.getPlatform() + "', '"
							+ noORderRequest.getVersion() + "', '" + noORderRequest.getReason() + "', '"
							+ noORderRequest.getStartMobileTimestamp() + "') ");
			s.executeUpdate(
					"insert into mobile_order_zero (mobile_order_no, outlet_id, created_on, created_by,distributor_id,beat_plan_id,region_id,rsm_id,snd_id, lat, lng, accuracy, uuid, mobile_timestamp, is_no_order, no_order_reason_type_v2, platform,app_version,comments,start_mobile_timestamp) values ('"
							+ noORderRequest.getMobileRequestId() + "', " + noORderRequest.getOutletId() + ", now(), "
							+ noORderRequest.getUserId() + ", " + noORderRequest.getDistributorId() + ", "
							+ noORderRequest.getBeatPlanId()
							+ " , (select region_id from common_distributors where distributor_id = "
							+ noORderRequest.getDistributorId()
							+ ") , (select rsm_id from common_distributors where distributor_id = "
							+ noORderRequest.getDistributorId()
							+ "), (select snd_id from common_distributors where distributor_id = "
							+ noORderRequest.getDistributorId() + "),  " + noORderRequest.getLat() + ", "
							+ noORderRequest.getLng() + ", " + noORderRequest.getAccuracy() + " , '"
							+ noORderRequest.getDeviceId() + "' ,'" + noORderRequest.getEndMobileTimestamp() + "', 1, "
							+ noORderRequest.getReasonId() + ", '" + noORderRequest.getPlatform() + "', '"
							+ noORderRequest.getVersion() + "', '" + noORderRequest.getReason() + "', '"
							+ noORderRequest.getStartMobileTimestamp() + "') ");
		
			ResultSet rsInsertID = s.executeQuery("select LAST_INSERT_ID()");
			long NoOrderID = (rsInsertID.first()) ? rsInsertID.getLong(1) : 0;

			//
//			Date d = new Date();
//			int month = AlmoizDateUtils.getMonthNumberByDate(d);
//			int year = AlmoizDateUtils.getYearByDate(d);
//
//			String uploadDir = MFPathUtils.getFilePath(MFConfig.Folders.NoOrder.getFolderName(), year, month);
//
//			String fileName = noORderRequest.getUserId() + "_1_" + System.currentTimeMillis() + ".jpg";
//
//			File outputFile = new File(uploadDir, fileName);
//			// Write the file
//			FileOutputStream fos = new FileOutputStream(outputFile);
//			fos.write(noORderRequest.getImage_1());
//			fos.flush();
//
//			String ImageQueryValues = "(" + NoOrderID + ", '" + fileName + "', '" + (uploadDir + "/" + fileName)
//					+ "', now(), " + noORderRequest.getUserId() + ", 11, " + month + ", " + year + ", "
//					+ noORderRequest.getOutletId() + ", " + noORderRequest.getLat() + ", " + noORderRequest.getLng()
//					+ ", " + noORderRequest.getAccuracy() + ", '" + noORderRequest.getDeviceId() + "', '"
//					+ noORderRequest.getStartMobileTimestamp() + "')";
//
//			if (noORderRequest.getImage_2().length > 0) {
//
//				String fileName2 = noORderRequest.getUserId() + "_2_" + System.currentTimeMillis() + ".jpg";
//
//				File outputFile2 = new File(uploadDir, fileName2);
//				// Write the file
//				FileOutputStream fos2 = new FileOutputStream(outputFile2);
//				fos2.write(noORderRequest.getImage_2());
//				fos2.flush();
//
//				ImageQueryValues += ",(" + NoOrderID + ", '" + fileName2 + "', '" + (uploadDir + "/" + fileName2)
//						+ "', now(), " + noORderRequest.getUserId() + ", 11, " + month + ", " + year + ", "
//						+ noORderRequest.getOutletId() + ", " + noORderRequest.getLat() + ", " + noORderRequest.getLng()
//						+ ", " + noORderRequest.getAccuracy() + ", '" + noORderRequest.getDeviceId() + "', '"
//						+ noORderRequest.getStartMobileTimestamp() + "')";
//
//			}
//		System.out.println(
//					"INSERT INTO `mobile_order_zero_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`,`outlet_id`,`lat`,`lng`,`accuracy`,`uuid`, `mobile_timestamp`) VALUES"
//							+ ImageQueryValues);
//			s.executeUpdate(
//					"INSERT INTO `mobile_order_zero_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`,`outlet_id`,`lat`,`lng`,`accuracy`,`uuid`, `mobile_timestamp`) VALUES"
//							+ ImageQueryValues);
//			
//			for(int i=0; i < noORderRequest.getProducts().size(); i++) {
//				NoORderRequestProducts noORderRequestProducts = new NoORderRequestProducts(
//						noORderRequest.getProducts().get(i).getProduct_id(),
//						noORderRequest.getProducts().get(i).getQuantity()
//						);
//				System.out.println("INSERT INTO `pep`.`mobile_order_zero_products` (`id`,`product_id`,`quantity`)VALUES("+NoOrderID+", "+noORderRequestProducts.getProduct_id()+", "+noORderRequestProducts.getQuantity()+")");
//				s.executeUpdate("INSERT INTO `pep`.`mobile_order_zero_products` (`id`,`product_id`,`quantity`)VALUES("+NoOrderID+", "+noORderRequestProducts.getProduct_id()+", "+noORderRequestProducts.getQuantity()+")");
//			}

			ResponseModal.setStatus(true);
			ResponseModal.setUserMessage("No order marked successfully");
			ds.commit();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("api/attendance Error :- " + e1);
				ResponseModal.setErrorResponse("server Error " + e1);
			}
			System.out.println("api/attendance Error :- " + e);
			ResponseModal.setErrorResponse("server Error " + e);
		}

		return ResponseModal;
	}

}
