package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.pbc.common.EmployeeHierarchy;
import com.pbc.common.User;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.FormulaUtills;
import com.pbc.util.MathUtils;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Orders Unedited", urlPatterns = { "/mobile/MobileSyncOrdersV11" })
public class MobileSyncOrdersV11 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileSyncOrdersV11() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.setContentType("json");
		System.out.println("MobileSyncOrderV11 - TheiaApp 1.4.0aaa");

		PrintWriter out = response.getWriter();

		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));

		JSONObject json = new JSONObject();

		if (!mr.isExpired()) {
			// order_no: unique, datetime and userid
			String order_no = Utilities.filterString(mr.getParameter("order_no"), 1, 100);

			long outlet_id = Utilities.parseLong(mr.getParameter("outlet_id"));
			// created_on: order saved on in local db
			String created_on = Utilities.filterString(mr.getParameter("created_on"), 1, 100);
			long created_by = Utilities.parseLong(mr.getParameter("created_by"));
			// sales_tax_rate: ignore
			double sales_tax_rate = Utilities.parseDouble(mr.getParameter("sales_tax_rate"));

			if (sales_tax_rate != 0) {
				sales_tax_rate = sales_tax_rate - 100;
			}
			// wh_tax_rate: ignore
			double wh_tax_rate = Utilities.parseDouble(mr.getParameter("wh_tax_rate"));

			String uuid = Utilities.filterString(mr.getParameter("uuid"), 1, 100);
			String platform = Utilities.filterString(mr.getParameter("platform"), 1, 100);
			// lat, lng: when order saved on in local db
			String lat = mr.getParameter("lat");
			String lng = mr.getParameter("lng");
			double accuracy_d = Utilities.parseDouble(mr.getParameter("accuracy"));
			// is_nfc: ignore
			double is_nfc = Utilities.parseDouble(mr.getParameter("is_nfc"));

			long accuracy = Math.round(accuracy_d);
			int product_id[] = Utilities.parseInt(mr.getParameterValues("product_id"));

			// int product_id[] = Utilities.parseInt(mr.getParameterValues("product_id"));
			System.out.println("product_id: " + product_id.length);
			// is_cooler_present, is_barcode_present: ignore
			int Is_Cooler_Present = Utilities.parseInt(mr.getParameter("is_cooler_present"));
			int Is_Barcode_Present = Utilities.parseInt(mr.getParameter("is_barcode_present"));

			int Is_Spot_Sale = Utilities.parseInt(mr.getParameter("is_spot_sale"));

			double brand_discount = 0.0;

			long discount_brand_id[] = Utilities.parseLong(mr.getParameterValues("discount_brand_id"));

			int d_brand_id[] = Utilities.parseInt(mr.getParameterValues("d_brand_id"));

			double discount_brand_amount[] = Utilities.parseDouble(mr.getParameterValues("discount_brand_amount"));
			int discount_brand_cartons[] = Utilities.parseInt(mr.getParameterValues("discount_brand_cartons"));

			/*
			 * try{ System.out.println(product_id); System.out.println(product_id.length);
			 * }catch(Exception e){ System.out.println(e); }
			 * 
			 */
			if (product_id != null) {

				int quantity[] = Utilities.parseInt(mr.getParameterValues("quantity"));
				double discount[] = Utilities.parseDouble(mr.getParameterValues("discount"));
				// ignore: unit_quantity
				int unit_quantity[] = Utilities.parseInt(mr.getParameterValues("unit_quantity"));
				// ignore: is_promotion, promotion_id, send 0
				int is_promotion[] = Utilities.parseInt(mr.getParameterValues("is_promotion"));
				int promotion_id[] = Utilities.parseInt(mr.getParameterValues("promotion_id"));

				Datasource ds = new Datasource();

				try {
					ds.createConnection();
					ds.startTransaction();

					Statement s = ds.createStatement();
					Statement s2 = ds.createStatement();

					boolean IsValidDeviceID = true;

//					ResultSet rsD = s
//							.executeQuery("select id from mobile_devices where uuid = '" + uuid + "' and is_active=1");
//					if (rsD.first()) {
//						IsValidDeviceID = true;
//					} else {
//						IsValidDeviceID = false;
//					}

					if (IsValidDeviceID) {

						boolean shouldIgnore = false;
						System.out.println("SELECT id from mobile_order_unedited where mobile_order_no = " + order_no);
						ResultSet rs5 = s.executeQuery(
								"SELECT id from mobile_order_unedited where mobile_order_no = " + order_no);
						if (rs5.first()) {
							shouldIgnore = true;
							System.out.println("UserId: " + created_by
									+ ",  Order ID already exists, ignoring mobile order no: " + order_no);
						}

						if (shouldIgnore == false) {

							ResultSet rs4 = s.executeQuery("select * from inventory_sales_tax_rates");
							while (rs4.next()) {
								if (rs4.getInt("id") == 1) {
									sales_tax_rate = rs4.getDouble("rate");
								} else if (rs4.getInt("id") == 2) {
									wh_tax_rate = rs4.getDouble("rate");
								}
							}

							System.out.println(
									"insert into mobile_order_unedited (mobile_order_no, outlet_id, created_on, created_by, brand_discount_amount, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
											+ "(" + order_no + ", " + outlet_id + ", now(), " + created_by + ", "
											+ brand_discount + ", " + sales_tax_rate + ", " + wh_tax_rate + ", '" + uuid
											+ "', '" + platform + "', " + lat + ", " + lng + ", " + accuracy + ", '"
											+ created_on + "', " + is_nfc + " ," + Is_Cooler_Present + ","
											+ Is_Barcode_Present + "," + Is_Spot_Sale + ") ");
							s.executeUpdate(
									"insert into mobile_order_unedited (mobile_order_no, outlet_id, created_on, created_by, brand_discount_amount, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, is_nfc,is_cooler_present,is_bar_code_present,is_spot_sale) values "
											+ "(" + order_no + ", " + outlet_id + ", now(), " + created_by + ", "
											+ brand_discount + ", " + sales_tax_rate + ", " + wh_tax_rate + ", '" + uuid
											+ "', '" + platform + "', " + lat + ", " + lng + ", " + accuracy + ", '"
											+ created_on + "', " + is_nfc + " ," + Is_Cooler_Present + ","
											+ Is_Barcode_Present + "," + Is_Spot_Sale + ") ");

							long OrderID = 0;
							ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
							if (rs2.first()) {
								OrderID = rs2.getLong(1);
							}

							double InvoiceAmount = 0;
							double InvoiceWHTaxAmount = 0;
							double InvoiceNetAmount = 0;

							// Product and Quantity Array for Promotion Calculation

							List<Integer> ProductIDArray = new ArrayList<Integer>();
							List<Long> TotalUnitsArray = new ArrayList<Long>();

							double TotalSpotDiscountAmount = 0;
							if (product_id != null) {

								for (int i = 0; i < product_id.length; i++) {
									if (is_promotion != null) {
										if (is_promotion[i] == 0) {

											boolean isAlreadyExists = false;
											// check if same non-promotion product already exists
											ResultSet crs = s.executeQuery(
													"select product_id from mobile_order_unedited_products where id = "
															+ OrderID + " and product_id = " + product_id[i]
															+ " and promotion_id is null");
											if (crs.first()) {
												isAlreadyExists = true;
											}

											if (isAlreadyExists == false) {
												int UnitsPerSKU = 0;
												long LiquidInMLPerUnit = 0;

												ResultSet rs3 = s.executeQuery(
														"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
																+ product_id[i]);
												if (rs3.first()) {
													UnitsPerSKU = rs3.getInt(1);
													LiquidInMLPerUnit = rs3.getLong(2);
												}

												int TotalUnits = (quantity[i] * UnitsPerSKU) + unit_quantity[i];
												long LiquidinML = LiquidInMLPerUnit * TotalUnits;

												double UnitRates[] = Product.getSellingPrice(product_id[i], outlet_id);
												double RateRawCase = UnitRates[0] - discount[i];
												double RateUnit = UnitRates[1];

												// patch for hand discount

												double SpotDiscountRate = discount[i];

												double SpotDiscountAmount = (SpotDiscountRate * quantity[i]);
												TotalSpotDiscountAmount += SpotDiscountRate;

												double HandDiscountRate = UnitRates[2];
												long HandDiscountID = Math.round(UnitRates[3]);
												double HandDiscountAmount = (HandDiscountRate * quantity[i]);
												String HandDiscountIDInsert = "null";
												if (HandDiscountID != 0) {
													HandDiscountIDInsert = "" + HandDiscountID;
												}

												// brand_discount

												// end patch

												double AmountRawCases = Utilities.parseDouble(Utilities
														.getDisplayCurrencyFormatSimple((quantity[i] * RateRawCase)));
												double AmountUnits = Utilities.parseDouble(Utilities
														.getDisplayCurrencyFormatSimple((unit_quantity[i] * RateUnit)));

												double TotalAmount = Utilities
														.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
																(AmountRawCases + AmountUnits)));
												double WHTaxAmount = Utilities
														.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
																(TotalAmount * wh_tax_rate / 100)));
												double NetAmount = Utilities.parseDouble(Utilities
														.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));

												InvoiceAmount += TotalAmount;
												InvoiceWHTaxAmount += WHTaxAmount;
												InvoiceNetAmount += NetAmount;

												String PromotionID = null;

												ProductIDArray.add(product_id[i]);
												TotalUnitsArray.add(TotalUnits * 1l);

												System.out.println(
														"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
																+ OrderID + ", " + product_id[i] + ", " + quantity[i]
																+ ", " + unit_quantity[i] + ", " + TotalUnits + ", "
																+ LiquidinML + ", " + RateRawCase + ", " + RateUnit
																+ ", " + AmountRawCases + ", " + AmountUnits + ", "
																+ TotalAmount + ", " + WHTaxAmount + "," + NetAmount
																+ ", " + is_promotion[i] + ", " + PromotionID + ", "
																+ SpotDiscountRate + ", " + SpotDiscountAmount + ", "
																+ HandDiscountIDInsert + ")");
												s.executeUpdate(
														"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
																+ OrderID + ", " + product_id[i] + ", " + quantity[i]
																+ ", " + unit_quantity[i] + ", " + TotalUnits + ", "
																+ LiquidinML + ", " + RateRawCase + ", " + RateUnit
																+ ", " + AmountRawCases + ", " + AmountUnits + ", "
																+ TotalAmount + ", " + WHTaxAmount + "," + NetAmount
																+ ", " + is_promotion[i] + ", " + PromotionID + ", "
																+ SpotDiscountRate + ", " + SpotDiscountAmount + ", "
																+ HandDiscountIDInsert + ")");
											}
										}
									}
								}

								if (ProductIDArray != null && ProductIDArray.size() > 0) {

									PromotionItem PromotionProducts[] = Product.getPromotionItems(outlet_id,
											ArrayUtils.toPrimitive(
													ProductIDArray.toArray(new Integer[ProductIDArray.size()])),
											ArrayUtils.toPrimitive(
													TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));

									for (int i = 0; i < PromotionProducts.length; i++) {

										long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(
												PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);

										long ProSAPCode = 0;
										int ProProductID = 0;
										double ProSellingPriceRawCase = 0;
										double ProSellingPriceUnit = 0;
										long ProLiquidInML = 0;

										int BrandID = 0;
										int SelectedBrandID = getBrandID(PromotionProducts[i].PROMOTION_ID, product_id,
												promotion_id);

										if (PromotionProducts[i].BRANDS.size() > 0) {
											BrandID = PromotionProducts[i].BRANDS.get(0);
										}

										if (SelectedBrandID != 0) {
											BrandID = SelectedBrandID;
										}

										if (BrandID != 0) {

											Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID,
													BrandID);
											ProProductID = PromotionProduct.PRODUCT_ID;
											ProSAPCode = PromotionProduct.SAP_CODE;
											double rates[] = Product.getSellingPrice(PromotionProduct.SAP_CODE,
													outlet_id);
											ProSellingPriceRawCase = rates[0];
											ProSellingPriceUnit = rates[1];
											ProLiquidInML = PromotionProduct.LIQUID_IN_ML;

											double AmountRawCases = Utilities
													.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
															(RawCasesAndUnits[0] * ProSellingPriceRawCase)));
											double AmountUnits = Utilities
													.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
															(RawCasesAndUnits[1] * ProSellingPriceUnit)));

											double TotalAmount = Utilities.parseDouble(Utilities
													.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
											double WHTaxAmount = Utilities.parseDouble(Utilities
													.getDisplayCurrencyFormatSimple((TotalAmount * wh_tax_rate / 100)));
											double NetAmount = Utilities.parseDouble(Utilities
													.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
											System.out.println(
													"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("
															+ OrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0]
															+ ", " + RawCasesAndUnits[1] + ", "
															+ PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML
															+ ", " + ProSellingPriceRawCase + ", " + ProSellingPriceUnit
															+ ", " + AmountRawCases + ", " + AmountUnits + ", "
															+ TotalAmount + ", " + WHTaxAmount + " ," + NetAmount
															+ ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
											s.executeUpdate(
													"replace into mobile_order_unedited_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("
															+ OrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0]
															+ ", " + RawCasesAndUnits[1] + ", "
															+ PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML
															+ ", " + ProSellingPriceRawCase + ", " + ProSellingPriceUnit
															+ ", " + AmountRawCases + ", " + AmountUnits + ", "
															+ TotalAmount + ", " + WHTaxAmount + " ," + NetAmount
															+ ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");

										}

									}

								}

							}

							if (discount_brand_id != null) {
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
								
								
								
							}

							// mobile_order_unedited_brand_sku_discount

							s.executeUpdate("update mobile_order_unedited set brand_discount_amount = " + brand_discount
									+ " where id = " + OrderID);

							InvoiceAmount = Utilities
									.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
							InvoiceWHTaxAmount = Utilities
									.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
							InvoiceNetAmount = Utilities
									.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));

							double TotalAmountExSalesTax = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((InvoiceAmount / (sales_tax_rate + 100)) * 100));

							double SalesTaxAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));

							String InoviceTotalAmountString = InvoiceNetAmount + "";

							if (InoviceTotalAmountString.indexOf(".") != -1) {
								double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(
										InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));

								InoviceTotalAmountString = InoviceTotalAmountString.substring(0,
										InoviceTotalAmountString.indexOf("."));

								if (Fraction != 0) {
									InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString) + 1) + "";
								}
							}

							double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;

							s.executeUpdate("update mobile_order_unedited set invoice_amount = " + InvoiceAmount
									+ ", sales_tax_amount  = " + SalesTaxAmount + ", wh_tax_amount = "
									+ InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount
									+ ", fraction_adjustment = "
									+ Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = "
									+ InoviceTotalAmountString + ", spot_discount_amount=" + TotalSpotDiscountAmount
									+ " where id = " + OrderID);

							// Adding Chiller Barcodes

							String CoolerBarcodes[] = Utilities.filterString(mr.getParameterValues("barcode"), 1, 100);

							if (CoolerBarcodes != null) {
								for (int i = 0; i < CoolerBarcodes.length; i++) {

									System.out.println("insert into mobile_order_assets(id,barcode) values(" + OrderID
											+ ",'" + CoolerBarcodes[i] + "') ");

									s.executeUpdate("insert into mobile_order_assets(id,barcode) values(" + OrderID
											+ ",'" + CoolerBarcodes[i] + "') ");

								}
							}

							// Adding Logs timings

							String MobileSessionLogsTypeID[] = Utilities
									.filterString(mr.getParameterValues("sessiontypeid"), 1, 100);
							String MobileSessionLogsTime[] = Utilities
									.filterString(mr.getParameterValues("sessiontime"), 1, 100);

							if (MobileSessionLogsTypeID != null) {
								for (int i = 0; i < MobileSessionLogsTypeID.length; i++) {

									System.out.println(
											"insert into mobile_order_timestamps(mobile_order_no,type_id,timestamps) values("
													+ OrderID + "," + MobileSessionLogsTypeID[i] + ",'"
													+ MobileSessionLogsTime[i] + "') ");

									s.executeUpdate(
											"insert into mobile_order_timestamps(mobile_order_no,type_id,timestamps) values("
													+ OrderID + "," + MobileSessionLogsTypeID[i] + ",'"
													+ MobileSessionLogsTime[i] + "') ");
									
									
								}
							}

							ds.commit();
							System.out.println("SalesPosting.splitOrder(OrderID)" + OrderID);
							SalesPosting.splitOrder(OrderID);

						}
						ds.commit();

						s.close();
						ds.dropConnection();

						json.put("success", "true");

					} else {
						json.put("success", "false");
						json.put("error_code", "102 - Device Inactive");
					}

				} catch (Exception e) {

					try {
						ds.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					json.put("success", "false");
					json.put("error_code", "106");
					e.printStackTrace();
					System.out.println(e);
					// out.print(e);
				} finally {

					try {
						ds.dropConnection();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// } // else of order#

			} else {
				System.out.println(request.getParameter("SessionID") + " " + mr.URL);
				json.put("success", "true");
				System.out.println("Hellooooooooooooooooo");
			}

		} else {
			json.put("success", "false");
			json.put("error_code", "101");
		}

		System.out.println(json);
		out.print(json);
		out.close();
	}

	private int getBrandID(long PromotionID, int ProductID[], int PromotionIDs[])
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		int pret = 0;
		for (int i = 0; i < ProductID.length; i++) {

			if (PromotionIDs[i] == PromotionID) {
				pret = ProductID[i];
			}

		}

		int ret = 0;

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
