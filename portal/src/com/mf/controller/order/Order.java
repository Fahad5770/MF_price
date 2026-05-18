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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

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

	@SuppressWarnings("static-access")
	private ResponseModal insertOrder(JSONObject jsonData, HttpServletRequest request, int attendanceType) {
		ResponseModal responseModal = new ResponseModal();

		final OrderRequest OReq = new OrderRequest(jsonData);

		System.out.println(
				"*************************************** New Price Structure ******************************************************");

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
							+ "('" + OReq.getVersion() + "', " + OReq.getMobileRequestId() + ", " + OReq.getOutletId()
							+ ", now(), " + OReq.getUserId() + ", '" + OReq.getDeviceId() + "', '" + OReq.getPlatform()
							+ "', " + OReq.getLat() + ", " + OReq.getLng() + ", " + OReq.getAccuracy() + ", '"
							+ OReq.getEndMobileTimestamp() + "', 0,0,0,0) ");
			s.executeUpdate(
					"insert into mobile_order_unedited (app_version, mobile_order_no, outlet_id, created_on, created_by, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
							+ "('" + OReq.getVersion() + "', " + OReq.getMobileRequestId() + ", " + OReq.getOutletId()
							+ ", now(), " + OReq.getUserId() + ", '" + OReq.getDeviceId() + "', '" + OReq.getPlatform()
							+ "', " + OReq.getLat() + ", " + OReq.getLng() + ", " + OReq.getAccuracy() + ", '"
							+ OReq.getEndMobileTimestamp() + "', 0,0,0,0) ");

			long unedited_order_id = 0;
			ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
			if (rs2.first()) {
				unedited_order_id = rs2.getLong(1);
			}

			double TotalInvoiceAmount = 0, TotalInvoiceIncomeTaxAmount = 0, TotalInvoiceSalesTaxAmount = 0,
					TotalInvoiceNetAmount = 0, TotalPriceDiscount = 0, TotalExtraPriceDiscount = 0, TotalInvoiveNetAmount = 0;

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
					System.out.println("heer 1");
					int TotalUnits = (quantity * UnitsPerSKU);
					System.out.println("heer 11111");
					long LiquidinML = LiquidInMLPerUnit * TotalUnits;
					System.out.println("heer 1.2323");
					// Price
					double RateRawCase = 0, RateUnit = 0;
					System.out.println("select raw_case, unit from inventory_price_list_products where product_id="
							+ OReqProducts.getProduct_id() + " and id=" + OReqProducts.getPrice_id());
					ResultSet rsPrice = s2
							.executeQuery("select raw_case, unit from inventory_price_list_products where product_id="
									+ OReqProducts.getProduct_id() + " and id=" + OReqProducts.getPrice_id());
					if (rsPrice.first()) {
						RateRawCase = rsPrice.getDouble("raw_case");
						RateUnit = rsPrice.getDouble("unit");
					}
					double AmountRawCases = RateRawCase * (double) quantity;
					double AmountUnits = RateUnit * (double) quantity;
					System.out.println("heer 2");
					
					// Extra Discount
					double extra_discount_rate = 0;
					int extra_disc_is_percentag = 0, extra_disc_is_with_tax = 0;
					System.out.println(
							"select discount_value, is_percentage,is_with_tax from inventory_extra_price_discount_products where product_id="
									+ OReqProducts.getProduct_id() + " and price_discount_id="
									+ OReqProducts.getExtra_discount_id());
					ResultSet rsExtraPriceDiscount = s2.executeQuery(
							"select discount_value, is_percentage,is_with_tax from inventory_extra_price_discount_products where product_id="
									+ OReqProducts.getProduct_id() + " and price_discount_id="
									+ OReqProducts.getExtra_discount_id());
					if (rsExtraPriceDiscount.first()) {
						extra_discount_rate = rsExtraPriceDiscount.getDouble("discount_value");
						extra_disc_is_percentag = rsExtraPriceDiscount.getInt("is_percentage");
						extra_disc_is_with_tax = rsExtraPriceDiscount.getInt("is_with_tax");
					}
					System.out.println("heer 3");
					double total_extra_diacount = extra_discount_rate * (double) quantity;
					if (extra_disc_is_percentag == 2) {
						total_extra_diacount = (AmountRawCases * extra_discount_rate) / 100;	
					}
					
					
					// Discount

					double discount_rate = 0;
					int is_percentag = 0, is_with_tax = 0;
					System.out.println(
							"select discount_value, is_percentage,is_with_tax from inventory_price_discount_products where product_id="
									+ OReqProducts.getProduct_id() + " and price_discount_id="
									+ OReqProducts.getDiscount_id());
					ResultSet rsPriceDiscount = s2.executeQuery(
							"select discount_value, is_percentage,is_with_tax from inventory_price_discount_products where product_id="
									+ OReqProducts.getProduct_id() + " and price_discount_id="
									+ OReqProducts.getDiscount_id());
					if (rsPriceDiscount.first()) {
						discount_rate = rsPriceDiscount.getDouble("discount_value");
						is_percentag = rsPriceDiscount.getInt("is_percentage");
						is_with_tax = rsPriceDiscount.getInt("is_with_tax");
					}
					System.out.println("heer 3");
					double total_diacount = discount_rate * (double) quantity;
					if (is_percentag == 2) {
					 total_diacount = (AmountRawCases * discount_rate) / 100;
						
					}
					System.out.println("heer 4");
					HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax_2(OReqProducts.getProduct_id(),
							OReq.getOutletId());

					double income_tax_rate = ProductsTax.get("income_tax"), income_tax_amount = 0,
							sales_tax_rate = ProductsTax.get("sales_tax"), sales_tax_amount = 0;
					System.out.println("heer 5");
					income_tax_amount = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple(income_tax_rate * (double) quantity));

					sales_tax_amount = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple(sales_tax_rate * quantity));

					double TotalNetAmount = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases)));
					double InvoiveNetAmount = Utilities.parseDouble(Utilities
							.getDisplayCurrencyFormatSimple((TotalNetAmount + sales_tax_amount + income_tax_amount - total_diacount - total_extra_diacount)));

					System.out.println(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, price_discount_id ,price_discount, price_discount_amount, is_percentage_discount, is_with_tax_discount,  total_amount, income_tax_rate, income_tax_amount,sales_tax_rate, sales_tax_amount, net_amount, is_promotion, promotion_id,extra_price_discount_id,extra_price_discount,extra_price_discount_amount,is_extra_percentage_discount,is_extra_with_tax_discount) values ("
									+ unedited_order_id + ", " + OReqProducts.getProduct_id() + ", " + quantity + ", "
									+ "0, " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit
									+ ", " + AmountRawCases + ", " + AmountUnits + "," + OReqProducts.getDiscount_id()
									+ "," + discount_rate + "," + total_diacount + "," + is_percentag + ", "
									+ is_with_tax + ", " + TotalNetAmount + ", " + income_tax_rate + ","
									+ income_tax_amount + "," + sales_tax_rate + ", " + sales_tax_amount + ","
									+ InvoiveNetAmount + ", 0,null,"+ OReqProducts.getExtra_discount_id()
									+ "," + extra_discount_rate + "," + total_extra_diacount + "," + extra_disc_is_percentag 
									+ "," + extra_disc_is_with_tax + ")");
					s2.executeUpdate(
							"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, price_discount_id ,price_discount, price_discount_amount, is_percentage_discount, is_with_tax_discount,  total_amount, income_tax_rate, income_tax_amount,sales_tax_rate, sales_tax_amount, net_amount, is_promotion, promotion_id,extra_price_discount_id,extra_price_discount,extra_price_discount_amount,is_extra_percentage_discount,is_extra_with_tax_discount) values ("
									+ unedited_order_id + ", " + OReqProducts.getProduct_id() + ", " + quantity + ", "
									+ "0, " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit
									+ ", " + AmountRawCases + ", " + AmountUnits + "," + OReqProducts.getDiscount_id()
									+ "," + discount_rate + "," + total_diacount + "," + is_percentag + ", "
									+ is_with_tax + ", " + TotalNetAmount + ", " + income_tax_rate + ","
									+ income_tax_amount + "," + sales_tax_rate + ", " + sales_tax_amount + ","
									+ InvoiveNetAmount + ", 0,null,"+ OReqProducts.getExtra_discount_id()
									+ "," + extra_discount_rate + "," + total_extra_diacount + "," + extra_disc_is_percentag
									+ "," + extra_disc_is_with_tax + ")");

					TotalInvoiceAmount += AmountRawCases;
					TotalPriceDiscount += total_diacount;
					TotalExtraPriceDiscount += total_extra_diacount; 
					TotalInvoiceNetAmount += TotalNetAmount;
					TotalInvoiveNetAmount += InvoiveNetAmount;
					TotalInvoiceIncomeTaxAmount += income_tax_amount;
					TotalInvoiceSalesTaxAmount += sales_tax_amount;

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

							double rates[] = Product.getSellingPrice_2(PromotionProduct.SAP_CODE, OReq.getOutletId(), OReq.getUserId());
							ProSellingPriceRawCase = rates[0];
							ProSellingPriceUnit = rates[1];
							ProLiquidInML = PromotionProduct.LIQUID_IN_ML;

							double AmountRawCases = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((RawCasesAndUnits[0] * ProSellingPriceRawCase)));
							double AmountUnits = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((RawCasesAndUnits[1] * ProSellingPriceUnit)));

							HashMap<String, Double> ProductsTax = AlmoizFormulas
									.ProductsTax_2(OReqProducts.getProduct_id(), OReq.getOutletId());

							double income_tax_rate = ProductsTax.get("income_tax"), income_tax_amount = 0,
									sales_tax_rate = ProductsTax.get("sales_tax"), sales_tax_amount = 0;

							income_tax_amount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple(income_tax_rate * (double) quantity));

							sales_tax_amount = Utilities
									.parseDouble(Utilities.getDisplayCurrencyFormatSimple(sales_tax_rate * quantity));

							double TotalAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
							// double WHTaxAmount = WHTaxAmount;
							double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
									(TotalAmount + sales_tax_amount + income_tax_amount)));
							System.out.println(
									"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, income_tax_rate, income_tax_amount,sales_tax_rate, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
											+ unedited_order_id + ", " + ProProductID + ", " + RawCasesAndUnits[0]
											+ ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS
											+ ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", "
											+ ProSellingPriceUnit + ", " + AmountRawCases + ", " + AmountUnits + ", "
											+ TotalAmount + ", " + income_tax_rate + ", " + income_tax_amount + ","
											+ sales_tax_rate + "," + sales_tax_amount + " ," + NetAmount + ", 1, "
											+ PromotionProducts[i].PROMOTION_ID + ")  ");
							s2.executeUpdate(
									"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, income_tax_rate, income_tax_amount,sales_tax_rate, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
											+ unedited_order_id + ", " + ProProductID + ", " + RawCasesAndUnits[0]
											+ ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS
											+ ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", "
											+ ProSellingPriceUnit + ", " + AmountRawCases + ", " + AmountUnits + ", "
											+ TotalAmount + ", " + income_tax_rate + ", " + income_tax_amount + ","
											+ sales_tax_rate + "," + sales_tax_amount + " ," + NetAmount + ", 1, "
											+ PromotionProducts[i].PROMOTION_ID + ")  ");

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
			System.out.println("update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount
					+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", income_tax_amount = "
					+ TotalInvoiceIncomeTaxAmount + ", total_amount = " + TotalInvoiceNetAmount
					+ ", fraction_adjustment = " + FractionAmount + ", net_amount = " + InoviceTotalAmountString
					+ ", price_discount=" + TotalPriceDiscount + ",extra_price_discount ="+ TotalExtraPriceDiscount + " where id = " + unedited_order_id);
			s.executeUpdate("update mobile_order_unedited set invoice_amount = " + TotalInvoiceAmount
					+ ", sales_tax_amount  = " + TotalInvoiceSalesTaxAmount + ", wh_tax_amount = "
					+ TotalInvoiceIncomeTaxAmount + ", total_amount = " + TotalInvoiceNetAmount
					+ ", fraction_adjustment = " + FractionAmount + ", net_amount = " + InoviceTotalAmountString
					+ ", price_discount=" + TotalPriceDiscount + ",extra_price_discount ="+ TotalExtraPriceDiscount + " where id = " + unedited_order_id);

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

			OF.splitOrder(unedited_order_id);
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
