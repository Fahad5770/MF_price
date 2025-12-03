package com.mf.controller.order;

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
import com.mf.modals.ResponseModal;
import com.mf.dao.OrderRequest;
import com.mf.interfaces.IOrder;
import com.mf.utils.MFConfig;
import com.mf.utils.MFDateUtils;
import com.mf.utils.MFPathUtils;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Utilities;

public class Order implements IOrder {

	@Override
	public ResponseModal InsertOrder(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {

		return insertOrder(jsonData, request, 1);
	}

	private ResponseModal insertOrder(JSONObject jsonData, HttpServletRequest request, int attendanceType) {
		ResponseModal responseModal = new ResponseModal();

		final OrderRequest OReq = new OrderRequest(jsonData);

		System.out.println(
				"*********************************************************************************************");

		System.out.println("Mobile Sync Order App " + OReq.getVersion());

		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			final OrderFunctions OF = new OrderFunctions();

			// check orde3r existence
			if (OF.GetOrderExists(ds, OReq.getMobileRequestId())) {
				responseModal.setErrorResponse("Order Already Exists : " + OReq.getMobileRequestId());
				return responseModal;
			}

			System.out.println(
					"insert into mobile_order_unedited (app_version, mobile_order_no, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
							+ "('"+OReq.getVersion()+"', " + OReq.getMobileRequestId() + ", " + OReq.getOutletId() + ", now(), "
							+ OReq.getUserId() + ", '" + OReq.getDeviceId() + "', '" + OReq.getPlatform() + "', "
							+ OReq.getLat() + ", " + OReq.getLng() + ", " + OReq.getAccuracy() + ", '"
							+ OReq.getEndMobileTimestamp() + "', 0,0,0,0) ");
			s.executeUpdate(
					"insert into mobile_order_unedited (app_version, mobile_order_no, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
							+ "('"+OReq.getVersion()+"', " + OReq.getMobileRequestId() + ", " + OReq.getOutletId() + ", now(), "
							+ OReq.getUserId() + ", '" + OReq.getDeviceId() + "', '" + OReq.getPlatform() + "', "
							+ OReq.getLat() + ", " + OReq.getLng() + ", " + OReq.getAccuracy() + ", '"
							+ OReq.getEndMobileTimestamp() + "', 0,0,0,0) ");

			long unedited_order_id = 0;
			ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
			if (rs2.first()) {
				unedited_order_id = rs2.getLong(1);
			}

			double TotalInvoiceAmount = 0, TotalInvoiceWHTaxAmount = 0, TotalInvoiceSalesTaxAmount = 0,
					TotalInvoiceNetAmount = 0, TotalPriceDiscount = 0, TotalSpotDiscountAmount = 0,
					TotalInvoiveNetAmount = 0;

			// Add Products
			for (OrderRequest.OrderRequestProducts OReqProducts : OReq.getProducts()) {
				int quantity = OReqProducts.getQuantity();
				if (OReqProducts.getIs_promotion() == 0) {

					int UnitsPerSKU = 0;
					long LiquidInMLPerUnit = 0;
					System.out.println(
							"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
									+ OReqProducts.getProduct_id());
					ResultSet rs_product = s2.executeQuery(
							"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
									+ OReqProducts.getProduct_id());
					if (rs_product.first()) {
						UnitsPerSKU = rs_product.getInt(1);
						LiquidInMLPerUnit = rs_product.getLong(2);
					}

					int TotalUnits = (quantity * UnitsPerSKU);

					long LiquidinML = LiquidInMLPerUnit * TotalUnits;

					double UnitRates[] = Product.getSellingPrice_2(OReqProducts.getProduct_id(), OReq.getOutletId());
					double RateRawCase = UnitRates[0];
					double RateUnit = UnitRates[1];
					double price_discount = UnitRates[2];
					// double SpotDiscountPercentage = OReqProducts.getDiscount();
					
					double SpotDiscountPercentage = 0;

					double AmountRawCases = RateRawCase * quantity;
					double AmountUnits = RateUnit * quantity;
					double SpotDiscountAmount = ((SpotDiscountPercentage * AmountRawCases) / 100);

					HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(OReqProducts.getProduct_id(),
							OReq.getOutletId());

					double WHTaxAmount = Utilities.parseDouble(
							Utilities.getDisplayCurrencyFormatSimple((ProductsTax.get("wh_tax") * quantity)));

					double SalesTaxAmount = Utilities.parseDouble(
							Utilities.getDisplayCurrencyFormatSimple((ProductsTax.get("income_tax") * quantity)));
					double TotalNetAmount = Utilities.parseDouble(
							Utilities.getDisplayCurrencyFormatSimple((AmountRawCases)));
					double InvoiveNetAmount = Utilities.parseDouble(
							Utilities.getDisplayCurrencyFormatSimple((TotalNetAmount + WHTaxAmount + SalesTaxAmount)));

					System.out.println(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units,price_discount, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
									+ unedited_order_id + ", " + OReqProducts.getProduct_id() + ", " + quantity + ", "
									+ quantity + ", " + TotalUnits + ", " + LiquidinML + ", "
									+ RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ","
									+ price_discount + ", " + TotalNetAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + ","
									+ InvoiveNetAmount + ", 0,null, " + SpotDiscountPercentage + ", "
									+ SpotDiscountAmount + ", null )");
					s.executeUpdate(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units,price_discount, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
									+ unedited_order_id + ", " + OReqProducts.getProduct_id() + ", " + quantity + ", "
									+ quantity + ", " + TotalUnits + ", " + LiquidinML + ", "
									+ RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ","
									+ price_discount + ", " + TotalNetAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + ","
									+ InvoiveNetAmount + ", 0,null, " + SpotDiscountPercentage + ", "
									+ SpotDiscountAmount + ", null )");

					TotalInvoiceAmount += AmountRawCases;
					TotalPriceDiscount += price_discount;
					TotalSpotDiscountAmount += SpotDiscountAmount;
					TotalInvoiceNetAmount += TotalNetAmount;
					TotalInvoiveNetAmount += InvoiveNetAmount;
					TotalInvoiceWHTaxAmount = WHTaxAmount;
					TotalInvoiceSalesTaxAmount = SalesTaxAmount;

				} else {

					PromotionItem PromotionProducts[] = Product.getPromotionItems(OReq.getOutletId(),
							OReqProducts.getProduct_id(), quantity);

					for (int i = 0; i < PromotionProducts.length; i++) {

						long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS,
								PromotionProducts[i].UNIT_PER_SKU);

						int ProProductID = 0;
						double ProSellingPriceRawCase = 0;
						double ProSellingPriceUnit = 0;
						long ProLiquidInML = 0;

						int BrandID = 0;
						int SelectedBrandID = OF.getBrandID(PromotionProducts[i].PROMOTION_ID,
								OReqProducts.getProduct_id(), OReqProducts.getPromotion_id());

						if (PromotionProducts[i].BRANDS.size() > 0) {
							BrandID = PromotionProducts[i].BRANDS.get(0);
						}

						if (SelectedBrandID != 0) {
							BrandID = SelectedBrandID;
						}

						if (BrandID != 0) {

							Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
							ProProductID = PromotionProduct.PRODUCT_ID;

							double rates[] = Product.getSellingPrice_2(PromotionProduct.SAP_CODE, OReq.getOutletId());
							ProSellingPriceRawCase = rates[0];
							ProSellingPriceUnit = rates[1];
							ProLiquidInML = PromotionProduct.LIQUID_IN_ML;

							double AmountRawCases = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((RawCasesAndUnits[0] * ProSellingPriceRawCase)));
							double AmountUnits = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((RawCasesAndUnits[1] * ProSellingPriceUnit)));

							HashMap<String, Double> ProductsTax = AlmoizFormulas
									.ProductsTax(OReqProducts.getProduct_id(), OReq.getOutletId());
							double WHTaxAmount = ProductsTax.get("wh_tax");

							double SalesTaxAmount = ProductsTax.get("income_tax");

							double TotalAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
							// double WHTaxAmount = WHTaxAmount;
							double NetAmount = Utilities
									.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
							System.out.println(
									"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
											+ unedited_order_id + ", " + ProProductID + ", " + RawCasesAndUnits[0]
											+ ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS
											+ ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", "
											+ ProSellingPriceUnit + ", " + AmountRawCases + ", " + AmountUnits + ", "
											+ TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + " ," + NetAmount
											+ ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
							s.executeUpdate(
									"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
											+ unedited_order_id + ", " + ProProductID + ", " + RawCasesAndUnits[0]
											+ ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS
											+ ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", "
											+ ProSellingPriceUnit + ", " + AmountRawCases + ", " + AmountUnits + ", "
											+ TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + " ," + NetAmount
											+ ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");

						}

					}

				}
			}

			String InoviceTotalAmountString = TotalInvoiveNetAmount + "";

			if (InoviceTotalAmountString.indexOf(".") != -1) {
				double Fraction = Utilities.parseDouble(InoviceTotalAmountString
						.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));

				InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));

				if (Fraction != 0) {
					InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString) + 1) + "";
				}
			}

			System.out.println(InoviceTotalAmountString);
			System.out.println(TotalInvoiveNetAmount);

			double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - TotalInvoiveNetAmount;
			// Update prices
			System.out.println(
					"update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount + ", sales_tax_amount  = "
							+ TotalInvoiceSalesTaxAmount + ", wh_tax_amount = " + TotalInvoiceWHTaxAmount
							+ ", total_amount = " + TotalInvoiceNetAmount + ", fraction_adjustment = " + FractionAmount
							+ ", net_amount = " + InoviceTotalAmountString + ", price_discount=" + TotalPriceDiscount
							+ ", spot_discount_amount=" + TotalSpotDiscountAmount + " where id = " + unedited_order_id);
			s.executeUpdate(
					"update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount + ", sales_tax_amount  = "
							+ TotalInvoiceSalesTaxAmount + ", wh_tax_amount = " + TotalInvoiceWHTaxAmount
							+ ", total_amount = " + TotalInvoiceNetAmount + ", fraction_adjustment = " + FractionAmount
							+ ", net_amount = " + InoviceTotalAmountString + ", price_discount=" + TotalPriceDiscount
							+ ", spot_discount_amount=" + TotalSpotDiscountAmount + " where id = " + unedited_order_id);

			Date today = new Date();
			int month = AlmoizDateUtils.getMonthNumberByDate(today);
			int year = AlmoizDateUtils.getYearByDate(today);
			int day = MFDateUtils.getDayNumberByDate(today);

			String uploadDir = MFPathUtils.getFilePathWithDay(MFConfig.Folders.Order.getFolderName(), year, month, day);

			String fileName = OReq.getUserId() + "_order_" + System.currentTimeMillis() + ".jpg";

			File outputFile = new File(uploadDir, fileName);
			// Write the file
			@SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(OReq.getOrder_image());
			fos.flush();

			// Add Files
			System.out.println(
					"INSERT INTO `mobile_order_unedited_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`,`outlet_id`,`lat`,`lng`,`accuracy`,`uuid`,`mobile_timestamp`,`version`, `day`)VALUES('"
							+ unedited_order_id + "','" + fileName + "','" + uploadDir + "/" + fileName + "',now(), "
							+ OReq.getUserId() + "," + 11 + "," + month + "," + year + ", " + OReq.getOutletId() + ", "
							+ OReq.getLat() + ", " + OReq.getLng() + ", " + OReq.getAccuracy() + ", '"
							+ OReq.getDeviceId() + "', '" + OReq.getStartMobileTimestamp() + "', '" + OReq.getVersion()
							+ "', " + day + " )");
			s.executeUpdate(
					"INSERT INTO `mobile_order_unedited_files`(`id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`month`,`year`,`outlet_id`,`lat`,`lng`,`accuracy`,`uuid`,`mobile_timestamp`,`version`, `day`)VALUES('"
							+ unedited_order_id + "','" + fileName + "','" + uploadDir + "/" + fileName + "',now(), "
							+ OReq.getUserId() + "," + 11 + "," + month + "," + year + ", " + OReq.getOutletId() + ", "
							+ OReq.getLat() + ", " + OReq.getLng() + ", " + OReq.getAccuracy() + ", '"
							+ OReq.getDeviceId() + "', '" + OReq.getStartMobileTimestamp() + "', '" + OReq.getVersion()
							+ "', " + day + " )");

			System.out.println(unedited_order_id);

			ds.commit();

			SalesPosting.splitOrder_2(unedited_order_id);
			responseModal.setSuccessResponse("Order has submitted.", new LinkedHashMap<String, Object>());
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException RollBackExcetion) {
				responseModal.setErrorResponse("RollBack : " + RollBackExcetion);
				System.out.println(RollBackExcetion);
				RollBackExcetion.printStackTrace();
			}

			responseModal.setErrorResponse("Exception : " + e);
			System.out.println(e);

		} finally {

			try {
				ds.dropConnection();
			} catch (SQLException DropConnectionExcetion) {
				responseModal.setErrorResponse("Exception : " + DropConnectionExcetion);
				System.out.println(DropConnectionExcetion);
				DropConnectionExcetion.printStackTrace();
			}
		}

		System.out.println(
				"*********************************************************************************************");

		
		return responseModal;
	}

}
