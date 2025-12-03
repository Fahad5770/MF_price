package com.mf.controller.order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.mf.modals.ResponseModal;

import com.mf.interfaces.IOrder;
import com.mf.modals.ResponseModal;
import com.mf.utils.MFConfig;
import com.mf.utils.MFDateUtils;
import com.mf.utils.MFPathUtils;
import com.mf.utils.MFSQLUtils;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.FormulaUtills;
import com.pbc.util.MathUtils;
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Utilities;

public class Order2 implements IOrder {

	@Override
	public ResponseModal InsertOrder(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {

		return insertOrder(jsonData, request, 1);
	}

	@SuppressWarnings("resource")
	private ResponseModal insertOrder(JSONObject jsonData, HttpServletRequest request, int attendanceType)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, IOException, ServletException,
			InstantiationException, FileUploadException {
		ResponseModal ResponseModal = new ResponseModal();

		final Number UserId = (Number) jsonData.get("user_id");
		System.out.println(UserId);
		final String MobilerequestId = (String) jsonData.get("mobile_request_id");
		System.out.println(MobilerequestId);
		final String DeviceId = (String) jsonData.get("device_id");
		System.out.println(DeviceId);
		final long OutletId = (Long) jsonData.get("outlet_id");
		System.out.println(OutletId);
		final double Lat = (Double) jsonData.get("lat");
		System.out.println(Lat);
		final double Lng = (Double) jsonData.get("lng");
		System.out.println(Lng);
		final double Accuracy_d = (Double) jsonData.get("accuracy");
		final long Accuracy = Math.round(Accuracy_d);
		System.out.println(Accuracy);
		final Number BeatPlanId = (Number) jsonData.get("beat_plan_id");
		System.out.println(BeatPlanId);
		final String start_mobile_timestamp = (String) jsonData.get("start_mobile_timestamp");
		System.out.println(start_mobile_timestamp);
		final String end_mobile_timestamp = (String) jsonData.get("end_mobile_timestamp");
		System.out.println(end_mobile_timestamp);
		final String Version = (String) jsonData.get("version");
		System.out.println(Version);
		final long DistributorId = (Long) jsonData.get("distributor_id");
		System.out.println(DistributorId);
		final String Platform = (String) jsonData.get("platform");
		System.out.println(Platform);
		final String ImageBase64 = (String) jsonData.get("order_image");

		final byte[] ImageBytes = Base64.decodeBase64(ImageBase64);
		final JSONArray ProductsArray = (JSONArray) jsonData.get("products");

		Date d = new Date();
		int month = AlmoizDateUtils.getMonthNumberByDate(d);
		int year = AlmoizDateUtils.getYearByDate(d);
		int day = MFDateUtils.getDayNumberByDate(d);

		String uploadDir = MFPathUtils.getFilePathWithDay(MFConfig.Folders.Order.getFolderName(), year, month, day);

		String fileName = UserId + "_order_" + System.currentTimeMillis() + ".jpg";

		Datasource ds = new Datasource();

		try {

			File outputFile = new File(uploadDir, fileName);
			// Write the file
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(ImageBytes);
			fos.flush();

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();

			OrderFunctions orderFunctions = new OrderFunctions();

			// check order existence
			if (orderFunctions.GetOrderExists(ds, MobilerequestId)) {
				System.out.println(
						"INSERT INTO `pep`.`mobile_request_duplication`(`mobile_request_id`,`mobile_timestamp`,`request_nature`,`created_by`,`lat`,`lng`,`accuracy`,`platform`,`version`,`outlet_id`,`distributor_id`,`region_id`,`beat_plan_id`)"
								+ "VALUES(" + MobilerequestId + ", '" + end_mobile_timestamp + "', 'Order', " + UserId
								+ ", " + Lat + ", " + Lng + ", " + Accuracy + ", '" + Platform + "', '" + Version
								+ "', " + OutletId + ", " + DistributorId
								+ ", (select region_id from common_distributors where distributor_id=" + DistributorId
								+ "), " + BeatPlanId + " )");
				s.executeUpdate(
						"INSERT INTO `pep`.`mobile_request_duplication`(`mobile_request_id`,`mobile_timestamp`,`request_nature`,`created_by`,`lat`,`lng`,`accuracy`,`platform`,`version`,`outlet_id`,`distributor_id`,`region_id`,`beat_plan_id`)"
								+ "VALUES(" + MobilerequestId + ", '" + end_mobile_timestamp + "', 'order', " + UserId
								+ ", " + Lat + ", " + Lng + ", " + Accuracy + ", '" + Platform + "', '" + Version
								+ "', " + OutletId + ", " + DistributorId
								+ ", (select region_id from common_distributors where distributor_id=" + DistributorId
								+ "), " + BeatPlanId + " )");
				ds.commit();
				// ResponseModal.setErrorResponse("Order already exists for request " +
				// MobilerequestId);
				ResponseModal.setStatus(true);
				return ResponseModal;
			}

		

			/*System.out.println(
					"insert into mobile_order_unedited (mobile_order_no, outlet_id, created_on, created_by, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present) values "
							+ "(" + MobilerequestId + ", " + OutletId + ", now(), " + UserId + ", "
							+ getTaxeinfo.get("sales_tax_rate") + ", " + getTaxeinfo.get("wh_tax_rate") + ", '"
							+ DeviceId + "', '" + Platform + "', " + Lat + ", " + Lng + ", " + Accuracy + ", '"
							+ end_mobile_timestamp + "', 0 ,0, 0)");*/

			System.out.println(
					"insert into mobile_order_unedited (mobile_order_no, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
							+ "(" + MobilerequestId + ", " + OutletId + ", now(), " + UserId 
							+ ", '" + DeviceId
							+ "', '" + Platform + "', " + Lat + ", " + Lng + ", " + Accuracy + ", '"
							+ end_mobile_timestamp + "',0 ,0,0,0) ");

			s.executeUpdate(
					"insert into mobile_order_unedited (mobile_order_no, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
							+ "(" + MobilerequestId + ", " + OutletId + ", now(), " + UserId
							+ ", '" + DeviceId
							+ "', '" + Platform + "', " + Lat + ", " + Lng + ", " + Accuracy + ", '"
							+ end_mobile_timestamp + "',0 ,0,0,0) ");

			ResultSet rsInsertID = s.executeQuery("select LAST_INSERT_ID()");
			long OrderID = (rsInsertID.first()) ? rsInsertID.getLong(1) : 0;
			System.out.println(OrderID);

			double TotalInvoiceAmount = 0;
			double TotalInvoiceWHTaxAmount = 0;
			double TotalInvoiceSalesTaxAmount = 0;
			double TotalInvoiceNetAmount = 0;

			List<Integer> ProductIDArray = new ArrayList<Integer>();
			List<Long> TotalUnitsArray = new ArrayList<Long>();
			double TotalSpotDiscountAmount = 0;

			System.out.println(OrderID);

			System.out.println(ProductsArray);
			for (int i = 0; i < ProductsArray.size(); i++) {

				JSONObject productJson = (JSONObject) ProductsArray.get(i);

				int product_id = ((Number) productJson.get("product_id")).intValue();
				System.out.println("product_id " + product_id);
				double discount = (double) productJson.get("discount");
				int quantity = ((Number) productJson.get("quantity")).intValue();
				int unit_quantity = ((Number) productJson.get("unit_quantity")).intValue();
				int is_promotion = ((Number) productJson.get("is_promotion")).intValue();
				// int promotion_id = ((Number) productJson.get("promotion_id")).intValue();
			

				if (is_promotion == 0) {

					int UnitsPerSKU = 0;
					long LiquidInMLPerUnit = 0;
					System.out.println(
							"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
									+ product_id);
					ResultSet rs3 = s.executeQuery(
							"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
									+ product_id);
					if (rs3.first()) {
						UnitsPerSKU = rs3.getInt(1);
						LiquidInMLPerUnit = rs3.getLong(2);
					}

					int TotalUnits = (quantity * UnitsPerSKU);
					System.out.println("TotalUnits : " + TotalUnits);
					long LiquidinML = LiquidInMLPerUnit * TotalUnits;

					double UnitRates[] = Product.getSellingPrice_2(product_id, OutletId);
					System.out.println("11111");
					
					System.out.println("11111");
					
					
					System.out.println("discount : "+ discount);

					double RateRawCase = UnitRates[0] - (discount) ;
					System.out.println("RateRawCase : " + RateRawCase);
					double RateUnit = UnitRates[1];

					// patch for hand discount double RateAfterDiscount = UnitRates[0] *
					// discount[i]/100;
					System.out.println("2222");

					double SpotDiscountPercentage = discount;

					double totalrate = RateUnit * quantity;

					double SpotDiscountAmount = ((SpotDiscountPercentage * totalrate) / 100);
					TotalSpotDiscountAmount += SpotDiscountAmount;

					// double HandDiscountRate = UnitRates[2];
					long HandDiscountID = Math.round(UnitRates[3]);
					// double HandDiscountAmount = (HandDiscountRate * quantity);
					// String HandDiscountIDInsert = "null";
					if (HandDiscountID != 0) {
						// HandDiscountIDInsert = "" + HandDiscountID;
					}
					
					HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(product_id, OutletId);
					
					double WHTaxAmount = ProductsTax.get("wh_tax") * quantity;
					
					double SalesTaxAmount = ProductsTax.get("income_tax")* quantity;

					// end patch

					double AmountRawCases = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple((quantity * RateRawCase)));
					double AmountUnits = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple((unit_quantity * RateUnit)));
/*
					double TotalAmount = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases )));*/

//					double WHTaxAmount = Utilities.parseDouble(Utilities
//							.getDisplayCurrencyFormatSimple((TotalAmount * getTaxeinfo.get("wh_tax_rate") / 100)));

					double TotalAmount = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
									(AmountRawCases)));
					
					double NetAmount = Utilities.parseDouble(Utilities
							.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount + SalesTaxAmount)));
					TotalInvoiceAmount += TotalAmount;
					TotalInvoiceSalesTaxAmount +=SalesTaxAmount;
					TotalInvoiceWHTaxAmount += WHTaxAmount;
					TotalInvoiceNetAmount += NetAmount;

					String PromotionID = null;

					ProductIDArray.add(product_id);
					TotalUnitsArray.add(TotalUnits * 1l);

				/*	System.out.println(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, spot_discount_rate, spot_discount_amount) values ("
									+ OrderID + ", " + product_id + ", " + quantity + ", " + unit_quantity + ", "
									+ TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", "
									+ AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + WHTaxAmount
									+ "," + NetAmount + ", " + is_promotion + ", " + PromotionID + ", "
									+ SpotDiscountPercentage + ", " + SpotDiscountAmount + ", " + SpotDiscountAmount
									+ ", " + discount + ")");*/
					System.out.println(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
									+ OrderID + ", " + product_id + ", " + quantity
									+ ", " + unit_quantity + ", " + TotalUnits + ", "
									+ LiquidinML + ", " + RateRawCase + ", " + RateUnit
									+ ", " + AmountRawCases + ", " + AmountUnits + ", "
									+ TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + "," + NetAmount
									+ ", " + is_promotion + ", " + PromotionID + ", "
									+ SpotDiscountPercentage + ", " + SpotDiscountAmount + ", null"
									 + ")");
					s.executeUpdate(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
									+ OrderID + ", " + product_id + ", " + quantity
									+ ", " + unit_quantity + ", " + TotalUnits + ", "
									+ LiquidinML + ", " + RateRawCase + ", " + RateUnit
									+ ", " + AmountRawCases + ", " + AmountUnits + ", "
									+ TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + "," + NetAmount
									+ ", " + is_promotion + ", " + PromotionID + ", "
									+ SpotDiscountPercentage + ", " + SpotDiscountAmount + ", null"
									 + ")");

					// System.out.println("Product : " + orderUneditedProducts.toString());
				}
			}
			/******************************/
			
			
			/***************** Brand level Discount *************
				if (discount_brand_id.length > 0) {
					for (int j = 0; j < discount_brand_id.length; j++) {
						System.out.println(
								"INSERT INTO `pep`.`mobile_order_unedited_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("
										+ OrderID + ", " + discount_brand_id[j] + "," + d_brand_id[j]
										+ "," + discount_brand_amount[j] + ","
										+ discount_brand_cartons[j] + ")");
						s.executeUpdate(
								"INSERT INTO `pep`.`mobile_order_unedited_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("
										+ OrderID + ", " + discount_brand_id[j] + "," + d_brand_id[j]
										+ "," + discount_brand_amount[j] + ","
										+ discount_brand_cartons[j] + ")");
						brand_discount += discount_brand_amount[j];
						
						long newDiscountID = 0;
						ResultSet rsNewDiscountID = s.executeQuery("select LAST_INSERT_ID()");
						if (rsNewDiscountID.first()) {
							newDiscountID = rsNewDiscountID.getLong(1);
						}
						for (int i = 0; i < product_id.length; i++) {
							if (is_promotion != null) {
								if (is_promotion[i] == 0) {


									ResultSet rs3 = s.executeQuery(
											"SELECT unit_per_sku FROM inventory_products_view where product_id = "
													+ product_id[i] + " and lrb_type_id="+d_brand_id[j]);
									if (rs3.first()) {
										
										double cartonsOfProduct =  FormulaUtills.CalculateProductsInCartons(quantity[i], product_id[i]);
										int cartons =  (MathUtils.checkZeroAfterDecimal(cartonsOfProduct)) ? (int) cartonsOfProduct : 0;
					                     System.out.println("INSERT INTO `pep`.`mobile_order_unedited_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("+OrderID+", "+ discount_brand_id[j] +", "+d_brand_id[j]+", "+product_id[i]+", "+quantity[i]+" ,"+ cartons +")");
										s2.executeUpdate("INSERT INTO `pep`.`mobile_order_unedited_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("+OrderID+", "+ discount_brand_id[j] +", "+d_brand_id[j]+", "+product_id[i]+", "+quantity[i]+" ,"+ cartons +")");
										
									}// if product exists with brand

									
								}
								}
						}
					}
					
					
					
				}
				
				
				
			
			/***************** Brand level Discount *************/
			// mobile_order_unedited_brand_sku_discount

//			System.out.println("update mobile_order_unedited set brand_discount_amount = " + brand_discount
//					+ " where id = " + OrderID);
//			s.executeUpdate("update mobile_order_unedited set brand_discount_amount = " + brand_discount
//					+ " where id = " + OrderID);

			TotalInvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalInvoiceAmount));
			TotalInvoiceWHTaxAmount = Utilities
					.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalInvoiceWHTaxAmount));
			TotalInvoiceNetAmount = Utilities
					.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalInvoiceNetAmount));

//			double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
//					(TotalInvoiceAmount / (getTaxeinfo.get("sales_tax_rate") + 100)) * 100));
//
//			double SalesTaxAmount = Utilities
//					.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalInvoiceAmount - TotalAmountExSalesTax));

			String InoviceTotalAmountString = TotalInvoiceNetAmount + "";

			if (InoviceTotalAmountString.indexOf(".") != -1) {
				double Fraction = Utilities.parseDouble(InoviceTotalAmountString
						.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));

				InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));

				if (Fraction != 0) {
					InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString) + 1) + "";
				}
			}

			double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - TotalInvoiceNetAmount;
			
			/*s.executeUpdate("update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount
					+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", wh_tax_amount = "
					+ TotalInvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount
					+ ", fraction_adjustment = "
					+ Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = "
					+ InoviceTotalAmountString + ", spot_discount_amount=" + TotalSpotDiscountAmount
					+ " where id = " + OrderID);*/
			
			System.out.println("update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount
					+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", wh_tax_amount = " + TotalInvoiceWHTaxAmount
					+ ", total_amount = " + TotalInvoiceNetAmount + ", fraction_adjustment = "
					+ Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = "
					+ InoviceTotalAmountString + ", spot_discount_amount=" + TotalSpotDiscountAmount + " where id = "
					+ OrderID);
			s.executeUpdate("update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount
					+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", wh_tax_amount = " + TotalInvoiceWHTaxAmount
					+ ", total_amount = " + TotalInvoiceNetAmount + ", fraction_adjustment = "
					+ Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = "
					+ InoviceTotalAmountString + ", spot_discount_amount=" + TotalSpotDiscountAmount + " where id = "
					+ OrderID);
			

			System.out.println(
					"INSERT INTO `mobile_order_unedited_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`,`outlet_id`,`lat`,`lng`,`accuracy`,`uuid`,`mobile_timestamp`,`version`, `day`)VALUES('"
							+ OrderID + "','" + fileName + "','" + uploadDir + "/" + fileName + "',now(), "
							+ UserId + "," + 11 + "," + month + "," + year + ", " + OutletId + ", " + Lat + ", " + Lng
							+ ", " + Accuracy + ", '" + DeviceId + "', '" + start_mobile_timestamp + "', '"+Version+"', "+day+" )");
			s.executeUpdate(
					"INSERT INTO `mobile_order_unedited_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`,`outlet_id`,`lat`,`lng`,`accuracy`,`uuid`,`mobile_timestamp`,`version`, `day`)VALUES('"
							+ OrderID + "','" + fileName + "','" + uploadDir + "/" + fileName + "',now(), "
							+ UserId + "," + 11 + "," + month + "," + year + ", " + OutletId + ", " + Lat + ", " + Lng
							+ ", " + Accuracy + ", '" + DeviceId + "', '" + start_mobile_timestamp + "', '"+Version+"', "+day+" )");

			ds.commit();

			SalesPosting.splitOrder_2(OrderID);

			ResponseModal.setStatus(true);
			ResponseModal.setUserMessage("Order Punch marked successfully");

			s.close();
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("api/Order Error :- " + e1);
				ResponseModal.setErrorResponse("server Error " + e1);
			}

			System.out.println("api/Order Error :- " + e);
			ResponseModal.setErrorResponse("server Error " + e);
		}

		return ResponseModal;
	}

}
