package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;
import org.omg.CORBA.portable.ValueFactory;

import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Executes Desk Sale", urlPatterns = { "/inventory/DeskSaleExecute" })
public class DeskSaleExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeskSaleExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String UserID = null;

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();

		int DeskSaleEditID = Utilities.parseInt(request.getParameter("DeskSaleEditID"));
		boolean isEditCase = false;

		if (DeskSaleEditID > 0) {
			isEditCase = true;
		}

		long OutletID = Utilities.parseLong(request.getParameter("DeskSaleOutledIDHidden"));
		// System.out.println("OutletID : "+OutletID);
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		long DistributorID = Utilities.parseLong(request.getParameter("DeskSaleDistributorIDHidden"));
		long RegionID = Utilities.parseLong(request.getParameter("DeskSaleRegionIDHidden"));

		//double SalesTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleSalesTaxRateHidden"));
	//	double WHTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleWHTaxHidden"));

		// double InvoiceAmount =
		// Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalInvoiceAmount"));
		// double SalesTaxAmount =
		// Utilities.parseDouble(request.getParameter("DeskSaleMainFormSalesTax"));
		double WHTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormWithHoldingTax"));
		double TotalAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalAmount"));
		double TotalDiscout = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalDiscount"));
		double Adjustment = Utilities.parseDouble(request.getParameter("DeskSaleMainFormAdjustment"));
		double NetAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormFinalNetAmountRounded"));

		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnits"));

		double aRate[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormRateHidden"));
		double aUnitRate[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormUnitRateHidden"));

		double Discount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormDiscount"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnitPerSKU"));
		int LiquidInML[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormLiquidInML"));

		double RowAmount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormAmount"));
		double RowNetAmount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormNetAmount"));

		long PromotionID[] = Utilities.parseLong(request.getParameterValues("PromotionID"));

		int PromotionsProductID[] = Utilities.parseInt(request.getParameterValues("PromotionsProductID"));
		int PromotionsRawCases[] = Utilities.parseInt(request.getParameterValues("PromotionsRawCases"));
		int PromotionsUnits[] = Utilities.parseInt(request.getParameterValues("PromotionsUnits"));

		double PromotionsRate[] = Utilities.parseDouble(request.getParameterValues("PromotionsRateRawCase"));
		double PromotionsUnitRate[] = Utilities.parseDouble(request.getParameterValues("PromotionsRateUnit"));

		int PromotionsUnitPerSKU[] = Utilities.parseInt(request.getParameterValues("PromotionsUnitPerSKU"));
		int PromotionsLiquidInML[] = Utilities.parseInt(request.getParameterValues("PromotionsLiquidInML"));

		double PromotionsRowAmount[] = Utilities.parseDouble(request.getParameterValues("PromotionsAmount"));
		// System.out.println("UniqueVoucherID "+UniqueVoucherID);
		int ShelfRent = Utilities.parseInt(request.getParameter("ShelfRent1"));

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();

		Date VoucherDate = new java.util.Date();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();

			if (isEditCase) {
				ResultSet rs_date_check = s
						.executeQuery("select created_on from inventory_sales_invoices where id=" + DeskSaleEditID);
				if (rs_date_check.first()) {
					VoucherDate = rs_date_check.getDate(1);
				}
			}

			// if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){ // false only
			// in editcase on date conflict
			if (DateUtils.isSameDay(new java.util.Date(), VoucherDate)) { // false only in editcase on date conflict
				String SQLMain = "";

				ResultSet rs1 = s1
						.executeQuery("select id from inventory_sales_invoices where uvid=" + UniqueVoucherID);
				if (rs1.first()) {

					obj.put("success", "false");
					obj.put("error", "Already Exists");

				} else {

					boolean isMSU = true;

					int Lrb_GurrJar = 0, Lrb_GurrBox = 0, minimum_quantity = 0;

					for (int i = 0; i < ProductID.length; i++) {
						// System.out.println("select minimum_quantity,package_label,brand_label,(select
						// label from inventory_products_lrb_types where id=lrb_type_id) lrb from
						// inventory_products_view where lrb_type_id=8 and product_id="+ProductID[i]);
						ResultSet rsMSUData = s.executeQuery(
								"select minimum_quantity,package_label,brand_label,(select label from inventory_products_lrb_types where id=lrb_type_id) lrb from inventory_products_view where lrb_type_id=8 and product_id="
										+ ProductID[i]);
						if (rsMSUData.first()) {
							minimum_quantity = rsMSUData.getInt("minimum_quantity");

							Lrb_GurrJar += RawCases[i];

						}
					}

					isMSU = (Lrb_GurrJar >= minimum_quantity);
					// System.out.println(isMSU);
					String isMSUError = (isMSU) ? ""
							: "Please add Minimum Quantity " + minimum_quantity + " of Brand Gurr Jar";

					if (isMSU) {
						for (int i = 0; i < ProductID.length; i++) {
							// System.out.println("select minimum_quantity,package_label,brand_label,(select
							// label from inventory_products_lrb_types where id=lrb_type_id) lrb from
							// inventory_products_view where lrb_type_id=9 and product_id="+ProductID[i]);
							ResultSet rsMSUData = s.executeQuery(
									"select minimum_quantity,package_label,brand_label,(select label from inventory_products_lrb_types where id=lrb_type_id) lrb from inventory_products_view where lrb_type_id=9 and product_id="
											+ ProductID[i]);
							if (rsMSUData.first()) {
								minimum_quantity = rsMSUData.getInt("minimum_quantity");
								Lrb_GurrBox += RawCases[i];

							}
						}

						isMSU = (Lrb_GurrBox >= minimum_quantity);
						isMSUError = (isMSU) ? ""
								: "Please add Minimum Quantity " + minimum_quantity + " of Brand Gurr Box";
					}
					// String UrlString = request.getParameter("UrlString");
					// List<Long> productArray = new ArrayList<Long>();
					// List<Integer> rawArray = new ArrayList<Integer>();
					//
					// String[] pairs = UrlString.split("&");
					// for (String pair : pairs) {
					// String[] pairs2 = pair.split("=");
					//
					// if(pairs2[0].equals("ProductCode")){
					// System.out.println(pairs2[0] + " = " + pairs2[1]);
					// productArray.add(Long.parseLong(pairs2[1]));
					// }
					//
					// }
					//
					// for (String pair : pairs) {
					// String[] pairs2 = pair.split("=");
					//
					// if(pairs2[0].equals("DeskSaleMainFormRawCases")){
					// System.out.println(pairs2[0] + " = " + pairs2[1]);
					// rawArray.add(Integer.parseInt(pairs2[1]));
					// }
					//
					// }

					if (!isMSU) {
						obj.put("success", "false");
						obj.put("error", isMSUError);

					} else {

						ds.startTransaction();
						// s.getConnection().setAutoCommit(false);

					/*	int isFiler = 0;
						ResultSet rs31 = s.executeQuery("select is_filer from common_outlets where id=" + OutletID);
						if (rs31.first()) {
							isFiler = rs31.getInt("is_filer");
						}*/

						/*ResultSet rs4 = s.executeQuery("select * from inventory_sales_tax_rates");
						while (rs4.next()) {
							if (rs4.getInt("id") == 1) {
								// sales_tax_rate = rs4.getDouble("rate");
							} else if (rs4.getInt("id") == 2) {
								if (isFiler == 1) {
									WHTaxRate = rs4.getDouble("filer_rate");
								} else {
									WHTaxRate = rs4.getDouble("rate");
								}

								// wh_tax_rate = rs4.getDouble("rate");
							}
						}
*/
						// Patch for updating PJP ID

						long PJPID = 0;
						ResultSet rsp = s.executeQuery(
								"select distinct dbp.id from distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbps.outlet_id = "
										+ OutletID + " and dbp.distributor_id = " + DistributorID);
						while (rsp.next()) {
							PJPID = rsp.getLong(1);
						}

						//

						if (isEditCase) {
							SQLMain = "replace into inventory_sales_invoices (id, uvid, created_on, created_by, outlet_id, type_id, distributor_id, invoice_amount, sales_tax_rate, sales_tax_amount, wh_tax_rate, wh_tax_amount, total_amount, discount, net_amount, region_id, fraction_adjustment, beat_plan_id) values("
									+ DeskSaleEditID + ", " + UniqueVoucherID + ", now(), " + UserID + ", " + OutletID
									+ ", 1, " + DistributorID + ", 0, 0, 0, "
									+ "0 , 0, 0, 0, 0, " + RegionID + ", 0, " + ((PJPID == 0) ? null : PJPID) + " )";
						} else {
							SQLMain = "insert into inventory_sales_invoices (uvid, created_on, created_by, outlet_id, type_id, distributor_id, invoice_amount, sales_tax_rate, sales_tax_amount, wh_tax_rate, wh_tax_amount, total_amount, discount, net_amount, region_id, fraction_adjustment, beat_plan_id) values("
									+ UniqueVoucherID + ", now(), " + UserID + ", " + OutletID + ", 1, " + DistributorID
									+ ", 0,0, 0, 0, 0, 0, 0, 0, " + RegionID
									+ ", 0, " + ((PJPID == 0) ? null : PJPID) + " )";
						}

						System.out.println(SQLMain);
						s.executeUpdate(SQLMain);

						int DeskSaleID = 0;
						System.out.println("isEditCase : " + isEditCase);
						if (!isEditCase) {
							ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
							if (rs.first()) {
								DeskSaleID = rs.getInt(1);
							}
							// http: //
							// localhost:8080/portal/DeskSale.jsp?0.3144060707162011=0.9162932758758824# }
							// else {

						} else {
							DeskSaleID = DeskSaleEditID;
						}
						System.out.println("DeskSaleID : " + DeskSaleID);
						// System.out.println("delete from inventory_sales_invoices_products where
						// id="+DeskSaleID);
						s.executeUpdate("delete from inventory_sales_invoices_products where id=" + DeskSaleID);

						double InvoiceAmount = 0;
						double InvoiceWHTaxAmount = 0;
						double InvoiceSaleTaxAmount = 0;
						double InvoiceNetAmount = 0;

						for (int i = 0; i < ProductID.length; i++) {

							int UnitsPerSKU = 0;
							long LiquidInMLPerUnit = 0;
							int LrbTypeID = 0;

							ResultSet rs3 = s.executeQuery(
									"SELECT unit_per_sku, liquid_in_ml,lrb_type_id FROM inventory_products_view where product_id = "
											+ ProductID[i]);
							if (rs3.first()) {
								UnitsPerSKU = rs3.getInt(1);
								LiquidInMLPerUnit = rs3.getLong(2);
								LrbTypeID = rs3.getInt("lrb_type_id");
							}

							long TotalUnits = (RawCases[i] * UnitsPerSKU) + Units[i];
							long LiquidInMLValue = TotalUnits * LiquidInMLPerUnit;

							double UnitRates[] = Product.getSellingPrice_2(ProductID[i], OutletID);
							double RateRawCase = UnitRates[0];
							double RateUnit = UnitRates[1];

							double RawCaseAmount = RawCases[i] * RateRawCase;
							double UnitAmount = Units[i] * RateUnit;

							// patch for hand discount
							double HandDiscountRate = UnitRates[2];
							long HandDiscountID = Math.round(UnitRates[3]);
							double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
									(HandDiscountRate * RawCases[i]) + ((HandDiscountRate / UnitsPerSKU) * Units[i])));
							String HandDiscountIDInsert = "null";
							if (HandDiscountID != 0) {
								HandDiscountIDInsert = "" + HandDiscountID;
							}
							// end patch

							double ItemTotalAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple((RawCaseAmount + UnitAmount)));

							// double saleTax = 0.0;
							// double incomeTax = 0.0;
							double ItemWHTaxAmount = 0.0;
							System.out.println("UserID : " + UserID);
							HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(ProductID[i], OutletID);
							
							double WHTaxAmount = ProductsTax.get("wh_tax") * RawCases[i];
							
							double SalesTaxAmount = ProductsTax.get("income_tax")* RawCases[i];
//							if (Utilities.parseLong(UserID) == 1111) {
//								HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(ProductID[i],
//										OutletID);
//
//								ItemWHTaxAmount = ProductsTax.get("wh_tax") + ProductsTax.get("income_tax");
//								ItemWHTaxAmount = ItemWHTaxAmount * RawCases[i];
//							} else {
//								if (LrbTypeID != 1) {
//									ItemWHTaxAmount = Utilities.parseDouble(Utilities
//											.getDisplayCurrencyFormatSimple((ItemTotalAmount * WHTaxRate / 100)));
//								} else {
//									ItemWHTaxAmount = TotalUnits * AlmoizFormulas.BeetaTaxValue();
//								}
//							}
							// double SalesTaxAmount = ProductsTax.get("income_tax");

							// double ItemWHTaxAmount =
							// Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(((ItemTotalAmount
							// * WHTaxRate) / 100)));
							// double ItemWHTaxAmount = 0;

							double ItemNetAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + ItemWHTaxAmount)));

							InvoiceAmount += ItemTotalAmount;
							InvoiceWHTaxAmount += WHTaxAmount;
							InvoiceSaleTaxAmount += SalesTaxAmount;
							InvoiceNetAmount += ItemNetAmount;

							StockPosting sp = new StockPosting();

							long AvailableTotalUnits = sp.getClosingBalanceExInvoiced(DistributorID, ProductID[i],
									new Date());

							if (AvailableTotalUnits == 0) {
								String ProductName = getProductName(s, ProductID[i]);
								obj.put("success", "false");
								obj.put("error", "No Stock Available for " + ProductName);

								ds.rollback();

								out.print(obj);
								out.close();
								return;

							} else if (TotalUnits > AvailableTotalUnits) {

								String ProductName = getProductName(s, ProductID[i]);
								obj.put("success", "false");
								obj.put("error",
										"Quantity should not exceed "
												+ Utilities.convertToRawCases(AvailableTotalUnits, UnitPerSKU[i])
												+ " for " + ProductName);

								ds.rollback();

								out.print(obj);
								out.close();
								return;
							}

							System.out.println(
									"insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
											+ DeskSaleID + ", " + ProductID[i] + ", " + RawCases[i] + ", " + Units[i]
											+ ", " + TotalUnits + ", " + LiquidInMLValue + ", " + Discount[i] + ", "
											+ RateRawCase + ", " + RateUnit + ", " + RawCaseAmount + ", " + UnitAmount
											+ ", " + ItemTotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + "," + ItemNetAmount
											+ "," + HandDiscountRate + ", " + HandDiscountAmount + ", "
											+ HandDiscountIDInsert + ") ");
							s.executeUpdate(
									"insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("
											+ DeskSaleID + ", " + ProductID[i] + ", " + RawCases[i] + ", " + Units[i]
											+ ", " + TotalUnits + ", " + LiquidInMLValue + ", " + Discount[i] + ", "
											+ RateRawCase + ", " + RateUnit + ", " + RawCaseAmount + ", " + UnitAmount
											+ ", " + ItemTotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + "," + ItemNetAmount
											+ "," + HandDiscountRate + ", " + HandDiscountAmount + ", "
											+ HandDiscountIDInsert + ") ");
						}

						InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
						InvoiceWHTaxAmount = Utilities
								.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
						InvoiceNetAmount = Utilities
								.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));

//						double TotalAmountExSalesTax = Utilities.parseDouble(
//								Utilities.getDisplayCurrencyFormatSimple((InvoiceAmount / (SalesTaxRate + 100)) * 100));

					

						if (PromotionsProductID != null) {
							for (int i = 0; i < PromotionsProductID.length; i++) {

								int UnitsPerSKU = 0;
								long LiquidInMLPerUnit = 0;

								ResultSet rs3 = s.executeQuery(
										"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
												+ PromotionsProductID[i]);
								if (rs3.first()) {
									UnitsPerSKU = rs3.getInt(1);
									LiquidInMLPerUnit = rs3.getLong(2);
								}

								long TotalUnits = (PromotionsRawCases[i] * UnitsPerSKU) + PromotionsUnits[i];
								long LiquidInMLValue = TotalUnits * LiquidInMLPerUnit;

								double UnitRates[] = Product.getSellingPrice(ProductID[i], OutletID);
								double RateRawCase = UnitRates[0];
								double RateUnit = UnitRates[1];

								double RawCaseAmount = PromotionsRawCases[i] * RateRawCase;
								double UnitAmount = PromotionsUnits[i] * RateUnit;

								double ItemTotalAmount = Utilities.parseDouble(
										Utilities.getDisplayCurrencyFormatSimple((RawCaseAmount + UnitAmount)));
//								double ItemWHTaxAmount = Utilities.parseDouble(Utilities
//										.getDisplayCurrencyFormatSimple(((ItemTotalAmount * WHTaxRate) / 100)));

//								double ItemNetAmount = Utilities.parseDouble(
//										Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + ItemWHTaxAmount)));

								double ItemNetAmount = Utilities.parseDouble(
										Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + 0 )));
								StockPosting sp = new StockPosting();
								long AvailableTotalUnits = sp.getClosingBalanceExInvoiced(DistributorID,
										PromotionsProductID[i], new Date());

								if (AvailableTotalUnits == 0) {
									String ProductName = getProductName(s, PromotionsProductID[i]);
									obj.put("success", "false");
									obj.put("error", "No Stock Available for " + ProductName);

									ds.rollback();

									out.print(obj);
									out.close();
									return;

								} else if (TotalUnits > AvailableTotalUnits) {

									String ProductName = getProductName(s, PromotionsProductID[i]);
									obj.put("success", "false");
									obj.put("error",
											"Quantity should not exceed " + Utilities
													.convertToRawCases(AvailableTotalUnits, PromotionsUnitPerSKU[i])
													+ " for " + ProductName);

									ds.rollback();

									out.print(obj);
									out.close();
									return;
								}

								// System.out.println("promotion query");
								// System.out.println("insert into inventory_sales_invoices_products (id,
								// product_id, raw_cases, units, total_units, liquid_in_ml, discount,
								// rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount,
								// wh_tax_amount, net_amount, is_promotion, promotion_id) values
								// ("+DeskSaleID+", "+PromotionsProductID[i]+", "+PromotionsRawCases[i]+",
								// "+PromotionsUnits[i]+", "+TotalUnits+", "+LiquidInMLValue+", 0,
								// "+PromotionsRate[i]+", "+PromotionsUnitRate[i]+",
								// "+Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount)+",
								// "+Utilities.getDisplayCurrencyFormatSimple(UnitAmount)+",
								// "+ItemTotalAmount+", "+ItemWHTaxAmount+","+ItemNetAmount+", 1,
								// "+PromotionID[i]+") ");
								s.executeUpdate(
										"insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("
												+ DeskSaleID + ", " + PromotionsProductID[i] + ", "
												+ PromotionsRawCases[i] + ", " + PromotionsUnits[i] + ", " + TotalUnits
												+ ", " + LiquidInMLValue + ", 0, " + PromotionsRate[i] + ", "
												+ PromotionsUnitRate[i] + ", "
												+ Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount) + ", "
												+ Utilities.getDisplayCurrencyFormatSimple(UnitAmount) + ", "
												+ ItemTotalAmount + ", 0," + ItemNetAmount
												+ ", 1, " + PromotionID[i] + ") ");
							}
						}

						/*
						 * System.out.println("update inventory_sales_invoices set invoice_amount = " +
						 * InvoiceAmount + ", sales_tax_amount  = " + SalesTaxAmount +
						 * ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " +
						 * InvoiceNetAmount + ", fraction_adjustment = " +
						 * Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = "
						 * + InoviceTotalAmountString + " where id = " + DeskSaleID);
						 */
						//System.out.println(InvoiceNetAmount);
					//	System.out.println(ShelfRent);
						if (ShelfRent != 0) {
							if(InvoiceNetAmount < ShelfRent) {
								obj.put("success", "false");
								obj.put("error", "The invoice amount should be equal or more than the shelf rent");
								out.print(obj);
								out.close();
								return;
							}
						}
						
						InvoiceNetAmount = InvoiceNetAmount-ShelfRent;
						
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
						
//						System.out.println("update inventory_sales_invoices set invoice_amount = " + InvoiceAmount
//								+ ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount
//								+ ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount)
//								+ ", net_amount = " + InoviceTotalAmountString + " where id = " + DeskSaleID);
						s.executeUpdate("update inventory_sales_invoices set invoice_amount = " + InvoiceAmount
								+ ", sales_tax_amount= "+InvoiceSaleTaxAmount+" , wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount
								+ ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount)
								+ ", net_amount = " + InoviceTotalAmountString + ", shelf_rent="+ShelfRent+" where id = " + DeskSaleID);
						
//						/System.out.println("update rental_discount set is_released = 1, released_on=now(), released_by=" + UserID);
						s.executeUpdate("update rental_discount set is_released = 1, released_on=now(), released_by=" + UserID);
						ds.commit();

						// boolean posted = SalesPosting.post(DeskSaleID, Long.parseLong(UserID));

						// System.out.println("success case");

						obj.put("success", "true");
						obj.put("DeskSaleID", DeskSaleID);
					}

				}

			} else {
				obj.put("success", "false");
				obj.put("error", "You can only edit Today's voucher");
			}

			s1.close();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			obj.put("success", "false");
			obj.put("exception", e);
			e.printStackTrace();
			// out.print(e);
		} finally {

			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// System.out.println("before end close");
		out.print(obj);
		out.close();
		// System.out.println("after end close");

	}

	private String getProductName(Statement s, int ProductID) throws SQLException {

		String ProductName = "";
		ResultSet rs = s.executeQuery(
				"SELECT concat(package_label, ' ', brand_label) product_name FROM pep.inventory_products_view where product_id="
						+ ProductID);
		if (rs.first()) {
			ProductName = rs.getString("product_name");
		}

		return ProductName;

	}

}