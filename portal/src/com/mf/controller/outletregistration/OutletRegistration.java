package com.mf.controller.outletregistration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.controller.order.OrderFunctions;
import com.mf.dao.OrderRequest.OrderRequestProducts;
import com.mf.dao.OutletRegistrationRequest;
import com.mf.interfaces.IOutletRegistration;
import com.mf.modals.ResponseModal;
import com.mf.utils.MFConfig.Folders;
import com.mf.utils.MFDateUtils;
import com.mf.utils.MFPathUtils;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class OutletRegistration implements IOutletRegistration {
	public ResponseModal OutletRegister(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		return this.outlet_register(jsonData);
	}

	@SuppressWarnings("resource")
	private ResponseModal outlet_register(JSONObject jsonData) {
		ResponseModal responseModal = new ResponseModal();

		Datasource ds = new Datasource();
		Date d = new Date();
		int month = AlmoizDateUtils.getMonthNumberByDate(d);
		int year = AlmoizDateUtils.getYearByDate(d);
		int day = MFDateUtils.getDayNumberByDate(d);
		OutletRegistrationRequest ORQ = new OutletRegistrationRequest(jsonData);

		String uploadDir = MFPathUtils.getFilePathWithDay(Folders.NewOutletImages.getFolderName(), year, month, day);
		String fileName1 = ORQ.getUserId() + "_NewOutlet_1_" + System.currentTimeMillis() + ".jpg";
		String fileName2 = ORQ.getUserId() + "_NewOutlet_2_" + System.currentTimeMillis() + ".jpg";

		try {
			File outputFile1 = new File(uploadDir, fileName1);
			FileOutputStream fos1 = new FileOutputStream(outputFile1);
			fos1.write(ORQ.getNew_outlet_image1());
			fos1.flush();

			File outputFile2 = new File(uploadDir, fileName2);
			FileOutputStream fos2 = new FileOutputStream(outputFile2);
			fos2.write(ORQ.getNew_outlet_image2());
			fos2.flush();

			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			ORFunctions orFunctions = new ORFunctions();
			if (orFunctions.GetOuletExists(ds, ORQ.getMobileRequestId())) {
				System.out.println(
						"INSERT INTO `pep`.`mobile_request_duplication`(`mobile_request_id`,`mobile_timestamp`,`request_nature`,`created_by`,`lat`,`lng`,`accuracy`,`platform`,`version`,`outlet_id`,`distributor_id`,`region_id`,`beat_plan_id`)VALUES("
								+ ORQ.getMobileRequestId() + ", '" + ORQ.getEndMobileTimestamp()
								+ "', 'New Outlet Opening', " + ORQ.getUserId() + ", " + ORQ.getLat() + ", "
								+ ORQ.getLng() + ", " + ORQ.getAccuracy() + ", '" + ORQ.getPlatform() + "', '"
								+ ORQ.getVersion() + "', 0, " + ORQ.getDistributorId()
								+ ", (select region_id from common_distributors where distributor_id="
								+ ORQ.getDistributorId() + "), " + ORQ.getBeatPlanId() + " )");

				s.executeUpdate(
						"INSERT INTO `pep`.`mobile_request_duplication`(`mobile_request_id`,`mobile_timestamp`,`request_nature`,`created_by`,`lat`,`lng`,`accuracy`,`platform`,`version`,`outlet_id`,`distributor_id`,`region_id`,`beat_plan_id`)VALUES("
								+ ORQ.getMobileRequestId() + ", '" + ORQ.getEndMobileTimestamp()
								+ "', 'New Outlet Opening', " + ORQ.getUserId() + ", " + ORQ.getLat() + ", "
								+ ORQ.getLng() + ", " + ORQ.getAccuracy() + ", '" + ORQ.getPlatform() + "', '"
								+ ORQ.getVersion() + "', 0, " + ORQ.getDistributorId()
								+ ", (select region_id from common_distributors where distributor_id="
								+ ORQ.getDistributorId() + "), " + ORQ.getBeatPlanId() + " )");

				responseModal.setSuccessResponse("Outlet already opened for request ",
						new LinkedHashMap<String, Object>());
				return responseModal;
			}

			System.out.println(Utilities.parseDateYYYYMMDD(ORQ.getStartMobileTimestamp()));

			int Day_Number = Utilities.getDayOfWeekByDate(Utilities.parseDateYYYYMMDD(ORQ.getStartMobileTimestamp()));
			System.out.println(
					"======================================================================================================================");

			ORQ.setBeatPlanId(orFunctions.GetBeatPlanIDByOrderBooker(ds, ORQ.getUserId()));

			ORQ.setDistributorId(orFunctions.GetDistributorByBeatPlanID(ds, ORQ.getBeatPlanId()));

			String args = "'"+ORQ.getVersion()+"', "+ ORQ.getIsOrder() + "," + ORQ.getUserId() + ",'" + ORQ.getOutletName() + "', 1, "
					+ Utilities.getSQLString(ORQ.getAddress()) + ", " + Utilities.getSQLString(ORQ.getOwnerName())
					+ ", " + Utilities.getSQLString(ORQ.getOwnerMobileNo()) + ", "
					+ Utilities.getSQLString(ORQ.getOwnerCNIC()) + ", " + ORQ.getSubChannelId() + ", "
					+ ORQ.getDistributorId() + ", " + ORQ.getUserId() + ", NOW(), 1, " + ORQ.getBeatPlanId() + ", 1, "
					+ (orFunctions.GetCityByDistributor(ds, ORQ.getDistributorId())) + ", "
					+ Utilities.getSQLString(ORQ.getStartMobileTimestamp()) + ", '" + ORQ.getAreaLabel() + "', '"
					+ ORQ.getSubAreaLabel() + "', '" + ORQ.getPurchaserName() + "', "
					+ Utilities.getSQLString(ORQ.getPurchaserMobileNo()) + ", " + ORQ.getIsOwnerPurchaser() + ", "
					+ ORQ.getLat() + ", " + ORQ.getLng() + ", " + ORQ.getAccuracy() + ", '" + ORQ.getDeviceId() + "', '"
					+ ORQ.getPlatform() + "', " + Day_Number + "," + ORQ.getMobileRequestId();
			String sql = "INSERT INTO `common_outlets_request`(`app_version`,`is_order`,`request_by`,`outlet_name`,`shop_category`,`outlet_address`,`owner_name`,`owner_contact_number`,`owner_cnic_number`,`sub_channel_id`, `distributor_id`,`created_by`,`created_on`,`vpo_classifications_id`, `beat_plan_id`,`category_id`,`city_id`,`mobile_time_stamp`,`area_label`, `sub_area_label`, `purchaser_name`, `purchaser_number`, `is_owner_purchaser`, `lat`, `lng`, `accuracy`, `device_id`, `platform`,`day`,`mobile_transaction_no`) VALUES("
					+ args + ")";
			System.out.println(sql);
			s.executeUpdate(sql);

			ResultSet rsInsertId = s.executeQuery("SELECT LAST_INSERT_ID()");
			long outletRequestId = 0;
			if (rsInsertId.next()) {
				outletRequestId = rsInsertId.getLong(1);
			}

			System.out.print(
					"INSERT INTO pep.mobile_outlets_request_files (`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`,`year`,`month`,`day`) VALUES('"
							+ ORQ.getMobileRequestId() + "','" + fileName1 + "','" + uploadDir + "/" + fileName1
							+ "',now(), " + ORQ.getUserId() + " , 1, " + ORQ.getLat() + ", " + ORQ.getLng() + ", "
							+ ORQ.getAccuracy() + ", " + Utilities.getSQLString(ORQ.getStartMobileTimestamp()) + ", "
							+ year + ", " + month + "," + day + ")");
			s.executeUpdate(
					"INSERT INTO pep.mobile_outlets_request_files (`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`,`year`,`month`,`day`) VALUES('"
							+ ORQ.getMobileRequestId() + "','" + fileName1 + "','" + uploadDir + "/" + fileName1
							+ "',now(), " + ORQ.getUserId() + " , 1, " + ORQ.getLat() + ", " + ORQ.getLng() + ", "
							+ ORQ.getAccuracy() + ", " + Utilities.getSQLString(ORQ.getStartMobileTimestamp()) + ", "
							+ year + ", " + month + "," + day + ")");

			System.out.print(
					"INSERT INTO pep.mobile_outlets_request_files (`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`,`year`,`month`,`day`) VALUES('"
							+ ORQ.getMobileRequestId() + "','" + fileName2 + "','" + uploadDir + "/" + fileName2
							+ "',now(), " + ORQ.getUserId() + " , 1, " + ORQ.getLat() + ", " + ORQ.getLng() + ", "
							+ ORQ.getAccuracy() + ", " + Utilities.getSQLString(ORQ.getStartMobileTimestamp()) + ", "
							+ year + ", " + month + "," + day + ")");
			s.executeUpdate(
					"INSERT INTO pep.mobile_outlets_request_files (`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`,`year`,`month`,`day`) VALUES('"
							+ ORQ.getMobileRequestId() + "','" + fileName2 + "','" + uploadDir + "/" + fileName2
							+ "',now(), " + ORQ.getUserId() + " , 1, " + ORQ.getLat() + ", " + ORQ.getLng() + ", "
							+ ORQ.getAccuracy() + ", " + Utilities.getSQLString(ORQ.getStartMobileTimestamp()) + ", "
							+ year + ", " + month + "," + day + ")");

			// Order During Outlet Registeration

			final OrderFunctions OF = new OrderFunctions();

			if (ORQ.getIsOrder() == 1) {
				System.out.println("in order");

				System.out.println(
						"insert into mobile_order_unregistered (beat_plan_id, Request_id,mobile_order_no, app_version, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
								+ "(" + ORQ.getBeatPlanId() + "," + outletRequestId + "," + ORQ.getMobileRequestId()
								+ ", '" + ORQ.getVersion() + "' ," + outletRequestId + ", now(), " + ORQ.getUserId()
								+ ", '" + ORQ.getDeviceId() + "', '" + ORQ.getPlatform() + "', " + ORQ.getLat() + ", "
								+ ORQ.getLng() + ", " + ORQ.getAccuracy() + ", '" + ORQ.getStartMobileTimestamp()
								+ "', 0,0,0,0) ");
				s.executeUpdate(
						"insert into mobile_order_unregistered (beat_plan_id, Request_id,mobile_order_no, app_version, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
								+ "(" + ORQ.getBeatPlanId() + "," + outletRequestId + "," + ORQ.getMobileRequestId()
								+ ", '" + ORQ.getVersion() + "' ," + outletRequestId + ", now(), " + ORQ.getUserId()
								+ ", '" + ORQ.getDeviceId() + "', '" + ORQ.getPlatform() + "', " + ORQ.getLat() + ", "
								+ ORQ.getLng() + ", " + ORQ.getAccuracy() + ", '" + ORQ.getStartMobileTimestamp()
								+ "', 0,0,0,0) ");

				long unedited_order_id = 0;
				ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
				if (rs2.first()) {
					unedited_order_id = rs2.getLong(1);
				}

				double TotalInvoiceAmount = 0, TotalInvoiceWHTaxAmount = 0, TotalInvoiceSalesTaxAmount = 0,
						TotalInvoiceNetAmount = 0, TotalPriceDiscount = 0, TotalSpotDiscountAmount = 0,
						TotalInvoiveNetAmount = 0;

				// Add Products
				for (OrderRequestProducts product : ORQ.getProducts()) {
					int quantity = product.getQuantity();
					if (product.getIs_promotion() == 0) {

						int UnitsPerSKU = 0;
						long LiquidInMLPerUnit = 0;
						System.out.println(
								"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
										+ product.getProduct_id());
						ResultSet rs_product = s2.executeQuery(
								"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
										+ product.getProduct_id());
						if (rs_product.first()) {
							UnitsPerSKU = rs_product.getInt(1);
							LiquidInMLPerUnit = rs_product.getLong(2);
						}

						int TotalUnits = (quantity * UnitsPerSKU);

						long LiquidinML = LiquidInMLPerUnit * TotalUnits;

						double UnitRates[] = Product.getSellingPrice_2(product.getProduct_id(), 1);
						double RateRawCase = UnitRates[0];
						double price_discount = UnitRates[2];
						// double SpotDiscountPercentage = OReqProducts.getDiscount();
						double SpotDiscountPercentage = 0;

						double AmountRawCases = RateRawCase * quantity;
						double SpotDiscountAmount = ((SpotDiscountPercentage * AmountRawCases) / 100);

						HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(product.getProduct_id(), 1);

						double WHTaxAmount = Utilities.parseDouble(
								Utilities.getDisplayCurrencyFormatSimple((ProductsTax.get("wh_tax") * quantity)));

						double SalesTaxAmount = Utilities.parseDouble(
								Utilities.getDisplayCurrencyFormatSimple((ProductsTax.get("income_tax") * quantity)));
						double TotalNetAmount = Utilities.parseDouble(
								Utilities.getDisplayCurrencyFormatSimple((AmountRawCases - SpotDiscountAmount)));
						double InvoiveNetAmount = Utilities.parseDouble(Utilities
								.getDisplayCurrencyFormatSimple((TotalNetAmount + WHTaxAmount + SalesTaxAmount)));

						System.out.println(
								"replace into mobile_order_unregistered_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units,price_discount, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
										+ unedited_order_id + ", " + product.getProduct_id() + ", " + quantity + ", "
										+ quantity + ", " + quantity + ", " + TotalUnits + ", " + LiquidinML + ", "
										+ RateRawCase + ", " + RateRawCase + ", " + AmountRawCases + ", "
										+ price_discount + "," + +TotalNetAmount + ", " + WHTaxAmount + ","
										+ SalesTaxAmount + "," + InvoiveNetAmount + ", 0,null, "
										+ SpotDiscountPercentage + ", " + SpotDiscountAmount + ", null )");
						s.executeUpdate(
								"replace into mobile_order_unregistered_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units,price_discount, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
										+ unedited_order_id + ", " + product.getProduct_id() + ", " + quantity + ", "
										+ quantity + ", " + quantity + ", " + TotalUnits + ", " + LiquidinML + ", "
										+ RateRawCase + ", " + RateRawCase + ", " + AmountRawCases + ", "
										+ price_discount + ", " + +TotalNetAmount + ", " + WHTaxAmount + ","
										+ SalesTaxAmount + "," + InvoiveNetAmount + ", 0,null, "
										+ SpotDiscountPercentage + ", " + SpotDiscountAmount + ", null )");

						TotalInvoiceAmount += AmountRawCases;
						TotalPriceDiscount += price_discount;
						TotalSpotDiscountAmount += SpotDiscountAmount;
						TotalInvoiceNetAmount += TotalNetAmount;
						TotalInvoiveNetAmount += InvoiveNetAmount;
						TotalInvoiceWHTaxAmount = WHTaxAmount;
						TotalInvoiceSalesTaxAmount = SalesTaxAmount;

					} else {

						PromotionItem PromotionProducts[] = Product.getPromotionItems(1, product.getProduct_id(),
								quantity);

						for (int i = 0; i < PromotionProducts.length; i++) {

							long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS,
									PromotionProducts[i].UNIT_PER_SKU);

							int ProProductID = 0;
							double ProSellingPriceRawCase = 0;
							double ProSellingPriceUnit = 0;
							long ProLiquidInML = 0;

							int BrandID = 0;
							int SelectedBrandID = OF.getBrandID(PromotionProducts[i].PROMOTION_ID,
									product.getProduct_id(), product.getPromotion_id());

							if (PromotionProducts[i].BRANDS.size() > 0) {
								BrandID = PromotionProducts[i].BRANDS.get(0);
							}

							if (SelectedBrandID != 0) {
								BrandID = SelectedBrandID;
							}

							if (BrandID != 0) {

								Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
								ProProductID = PromotionProduct.PRODUCT_ID;

								double rates[] = Product.getSellingPrice_2(PromotionProduct.SAP_CODE, 1);
								ProSellingPriceRawCase = rates[0];
								ProSellingPriceUnit = rates[1];
								ProLiquidInML = PromotionProduct.LIQUID_IN_ML;

								double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
										(RawCasesAndUnits[0] * ProSellingPriceRawCase)));
								double AmountUnits = Utilities.parseDouble(Utilities
										.getDisplayCurrencyFormatSimple((RawCasesAndUnits[1] * ProSellingPriceUnit)));

								HashMap<String, Double> ProductsTax = AlmoizFormulas
										.ProductsTax(product.getProduct_id(), 1);
								double WHTaxAmount = ProductsTax.get("wh_tax");

								double SalesTaxAmount = ProductsTax.get("income_tax");

								double TotalAmount = Utilities.parseDouble(
										Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
								// double WHTaxAmount = WHTaxAmount;
								double NetAmount = Utilities.parseDouble(
										Utilities.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
								System.out.println(
										"replace into mobile_order_unregistered_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
												+ unedited_order_id + ", " + ProProductID + ", " + RawCasesAndUnits[0]
												+ ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS
												+ ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", "
												+ ProSellingPriceUnit + ", " + AmountRawCases + ", " + AmountUnits
												+ ", " + TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + " ,"
												+ NetAmount + ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
								s.executeUpdate(
										"replace into mobile_order_unregistered_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
												+ unedited_order_id + ", " + ProProductID + ", " + RawCasesAndUnits[0]
												+ ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS
												+ ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", "
												+ ProSellingPriceUnit + ", " + AmountRawCases + ", " + AmountUnits
												+ ", " + TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + " ,"
												+ NetAmount + ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");

							}

						}

					}
				}

				String InoviceTotalAmountString = TotalInvoiveNetAmount + "";

				if (InoviceTotalAmountString.indexOf(".") != -1) {
					double Fraction = Utilities.parseDouble(InoviceTotalAmountString
							.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));

					InoviceTotalAmountString = InoviceTotalAmountString.substring(0,
							InoviceTotalAmountString.indexOf("."));

					if (Fraction != 0) {
						InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString) + 1) + "";
					}
				}

				System.out.println(InoviceTotalAmountString);
				System.out.println(TotalInvoiveNetAmount);

				double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - TotalInvoiveNetAmount;

				// Update prices

				System.out.println("update mobile_order_unregistered set invoice_amount = " + TotalInvoiceAmount
						+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", wh_tax_amount = "
						+ TotalInvoiceWHTaxAmount + ", total_amount = " + TotalInvoiceNetAmount
						+ ", fraction_adjustment = " + FractionAmount + ", net_amount = " + InoviceTotalAmountString
						+ ", price_discount=" + TotalPriceDiscount + ", spot_discount_amount=" + TotalSpotDiscountAmount
						+ " where id = " + unedited_order_id);
				s.executeUpdate("update mobile_order_unregistered set invoice_amount = " + TotalInvoiceAmount
						+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", wh_tax_amount = "
						+ TotalInvoiceWHTaxAmount + ", total_amount = " + TotalInvoiceNetAmount
						+ ", fraction_adjustment = " + FractionAmount + ", net_amount = " + InoviceTotalAmountString
						+ ", price_discount=" + TotalPriceDiscount + ", spot_discount_amount=" + TotalSpotDiscountAmount
						+ " where id = " + unedited_order_id);

			}

			responseModal.setStatus(true);
			responseModal.setUserMessage("Outlet Add successfully");
			ds.commit();
			s.close();
			ds.dropConnection();
		} catch (Exception var59) {
			try {
				ds.rollback();
			} catch (SQLException var58) {
				var58.printStackTrace();
				System.out.println("api/attendance Error :- " + var58);
				responseModal.setErrorResponse("server Error " + var58);
			}

			System.out.println("api/attendance Error :- " + var59);
			responseModal.setErrorResponse("server Error " + var59);
		}

		return responseModal;
	}

	@SuppressWarnings("unused")
	private int getBrandID(long PromotionID, int[] ProductID, int[] PromotionIDs)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		int pret = 0;

		int ret;
		for (ret = 0; ret < ProductID.length; ++ret) {
			if ((long) PromotionIDs[ret] == PromotionID) {
				pret = ProductID[ret];
			}
		}

		ret = 0;
		if (pret != 0) {
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			ResultSet rs = s.executeQuery("select brand_id from inventory_products where id  =" + pret);
			if (rs.first()) {
				ret = rs.getInt(1);
			}

			s.close();
			ds.dropConnection();
		}

		return ret;
	}
}
