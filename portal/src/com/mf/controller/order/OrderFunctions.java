package com.mf.controller.order;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.mf.interfaces.IOrderFunctions;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class OrderFunctions implements IOrderFunctions {

	public boolean splitOrder(final long OrderID) {

		boolean success = false;
		final Datasource ds = new Datasource();
		try {
			ds.createConnection();
			final Statement s = ds.createStatement();
			final Statement s2 = ds.createStatement();
			final Statement s3 = ds.createStatement();
			final Statement s4 = ds.createStatement();
			final Statement s5 = ds.createStatement();

			long OrderSequence = 0L;
			System.out.println(
					"select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = "
							+ OrderID
							+ ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = "
							+ OrderID + ") order by codv.distributor_id desc");
			final ResultSet rs1 = s.executeQuery(
					"select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = "
							+ OrderID
							+ ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = "
							+ OrderID + ") order by codv.distributor_id desc");
			while (rs1.next()) {
				try {
					final long PJP_ID = rs1.getLong(2);
					final String SND_ID = rs1.getString("snd_id");
					final String RSM_ID = rs1.getString("rsm_id");
					if (PJP_ID == 0L) {
						continue;
					}
					ds.startTransaction();
					long SplitOrderID = 0L;
					double InvoiceAmount = 0.0;
					double InvoiceIncomeTaxAmount = 0.0;
					double InvoiceSalesTaxAmount = 0.0;
					double InvoiceNetAmount = 0.0;
					final List<Integer> ProductIDArray = new ArrayList<Integer>();
					final List<Long> TotalUnitsArray = new ArrayList<Long>();
					++OrderSequence;
					ds.startTransaction();
					final long DistributorID = rs1.getLong(1);
					long OutletID = 0L;
					System.out.println("select * from mobile_order_unedited where id = " + OrderID);
					final ResultSet rs2 = s2.executeQuery("select * from mobile_order_unedited where id = " + OrderID);
					if (rs2.first()) {
						InvoiceSalesTaxAmount = rs2.getDouble("sales_tax_amount");
						InvoiceIncomeTaxAmount = rs2.getDouble("income_tax_amount");
						OutletID = rs2.getLong("outlet_id");
						System.out.println(
								"insert into mobile_order (app_version,price_discount, mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by,brand_discount_amount, sales_tax_amount, wh_tax_amount, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id, is_spot_sale) values ('"
										+ rs2.getString("app_version") + "'," + rs2.getString("price_discount") + ", "
										+ OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", "
										+ DistributorID
										+ ", (select region_id from common_distributors where distributor_id = "
										+ DistributorID + "), now(), " + rs2.getString("created_by") + ", "
										+ rs2.getDouble("brand_discount_amount") + " ,"
										+ rs2.getString("sales_tax_amount") + ", " + rs2.getString("wh_tax_amount") + ", '"
										+ rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', "
										+ rs2.getString("lat") + ", " + rs2.getString("lng") + ", "
										+ rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', "
										+ OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID
										+ ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = "
										+ PJP_ID
										+ "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = "
										+ PJP_ID
										+ "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = "
										+ PJP_ID + "),'" + rs2.getString("is_spot_sale") + "' )");
						s3.executeUpdate(
								"insert into mobile_order (app_version,price_discount, mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by,brand_discount_amount, sales_tax_amount, wh_tax_amount, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id, is_spot_sale) values ('"
										+ rs2.getString("app_version") + "'," + rs2.getString("price_discount") + ", "
										+ OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", "
										+ DistributorID
										+ ", (select region_id from common_distributors where distributor_id = "
										+ DistributorID + "), now(), " + rs2.getString("created_by") + ", "
										+ rs2.getDouble("brand_discount_amount") + " ,"
										+ rs2.getString("sales_tax_amount") + ", " + rs2.getString("wh_tax_amount") + ", '"
										+ rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', "
										+ rs2.getString("lat") + ", " + rs2.getString("lng") + ", "
										+ rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', "
										+ OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID
										+ ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = "
										+ PJP_ID
										+ "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = "
										+ PJP_ID
										+ "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = "
										+ PJP_ID + "),'" + rs2.getString("is_spot_sale") + "' )");
						final ResultSet rs3 = s3.executeQuery("select LAST_INSERT_ID()");
						if (rs3.first()) {
							SplitOrderID = rs3.getLong(1);
						}
					}
					System.out.println(
							"SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "
									+ OrderID
									+ " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = "
									+ DistributorID + "))");
					final ResultSet rs3 = s2.executeQuery(
							"SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "
									+ OrderID
									+ " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = "
									+ DistributorID + "))");
					while (rs3.next()) {
						final int ProductID = rs3.getInt("product_id");
						final int TotalUnits = rs3.getInt("total_units");
						final int RawCases = rs3.getInt("raw_cases");
						final int Units = rs3.getInt("units");
						final int UnitsPerSKU = rs3.getInt("unit_per_sku");
						final long LiquidInMLPerUnit = rs3.getLong("liquid_in_ml");
						final long LiquidinML = LiquidInMLPerUnit * TotalUnits;
						final double RateRawCase = rs3.getDouble("rate_raw_cases");
						final double RateUnit = rs3.getDouble("rate_units");

						final double AmountRawCases = rs3.getDouble("amount_raw_cases");
						final double AmountUnits = rs3.getDouble("amount_units");
						final double TotalAmount = rs3.getDouble("total_amount");

						final double NetAmount = rs3.getDouble("net_amount");

						int price_discount_id = rs3.getInt("price_discount_id");
						double price_discount = rs3.getDouble("price_discount");
						double price_discount_amount = rs3.getDouble("price_discount_amount");
						int is_percentage_discount = rs3.getInt("is_percentage_discount");
						int is_with_tax_discount = rs3.getInt("is_with_tax_discount");
						double income_tax_rate = rs3.getDouble("income_tax_rate"),
								income_tax_amount = rs3.getDouble("income_tax_amount"),
								sales_tax_rate = rs3.getDouble("sales_tax_rate"),
								sales_tax_amount = rs3.getDouble("sales_tax_amount");

						InvoiceAmount += TotalAmount;
						// InvoiceWHTaxAmount += WHTaxAmount;
						InvoiceNetAmount += NetAmount;
						final String PromotionID = null;
						ProductIDArray.add(ProductID);
						TotalUnitsArray.add(TotalUnits * 1L);
						System.out.println(
								"insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, price_discount_id,price_discount , price_discount_amount, is_percentage_discount, is_with_tax_discount, total_amount, income_tax_rate, income_tax_amount, sales_tax_rate, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
										+ SplitOrderID + ", " + ProductID + ", " + RawCases + ", " + Units + ", "
										+ TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", "
										+ AmountRawCases + ", " + AmountUnits + "," + price_discount_id + ","
										+ price_discount + " ," + price_discount_amount + ", " + is_percentage_discount
										+ "," + is_with_tax_discount + "," + TotalAmount + ", " + income_tax_rate + ","
										+ income_tax_amount + "," + sales_tax_rate + "," + sales_tax_amount + ","
										+ NetAmount + ", 0, " + PromotionID + ")");
						s3.executeUpdate(
								"insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, price_discount_id,price_discount , price_discount_amount, is_percentage_discount, is_with_tax_discount, total_amount, income_tax_rate, income_tax_amount, sales_tax_rate, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
										+ SplitOrderID + ", " + ProductID + ", " + RawCases + ", " + Units + ", "
										+ TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", "
										+ AmountRawCases + ", " + AmountUnits + "," + price_discount_id + ","
										+ price_discount + " ," + price_discount_amount + ", " + is_percentage_discount
										+ "," + is_with_tax_discount + "," + TotalAmount + ", " + income_tax_rate + ","
										+ income_tax_amount + "," + sales_tax_rate + "," + sales_tax_amount + ","
										+ NetAmount + ", 0, " + PromotionID + ")");
						s3.executeUpdate("update mobile_order_unedited_products set is_processed = 1 where id = "
								+ OrderID + " and product_id = " + ProductID + " and is_promotion = 0");
					}

					System.out.println("select * from mobile_order_unedited_brand_discount where id=" + OrderID);
					final ResultSet rsBrandDiscount = s2
							.executeQuery("select * from mobile_order_unedited_brand_discount where id=" + OrderID);
					while (rsBrandDiscount.next()) {
						// System.out.println("INSERT INTO
						// `pep`.`mobile_order_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("+SplitOrderID+",
						// "+rsBrandDiscount.getLong("discount_brand_id")+","+rsBrandDiscount.getInt("brand_id")+","+rsBrandDiscount.getDouble("brand_discount_amount")+",
						// "+rsBrandDiscount.getInt("cartons")+")");
						s3.executeUpdate(
								"INSERT INTO `pep`.`mobile_order_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("
										+ SplitOrderID + ", " + rsBrandDiscount.getLong("discount_brand_id") + ","
										+ rsBrandDiscount.getInt("brand_id") + ","
										+ rsBrandDiscount.getDouble("brand_discount_amount") + ", "
										+ rsBrandDiscount.getInt("cartons") + ")");
					}

					System.out
							.println("select * from mobile_order_unedited_brand_discount_products where id=" + OrderID);
					final ResultSet rsBrandDiscountProducts = s2.executeQuery(
							"select * from mobile_order_unedited_brand_discount_products where id=" + OrderID);
					while (rsBrandDiscountProducts.next()) {
						System.out.println(
								"INSERT INTO `pep`.`mobile_order_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("
										+ SplitOrderID + ", " + rsBrandDiscountProducts.getLong("discount_brand_id")
										+ ", " + rsBrandDiscountProducts.getInt("brand_id") + ", "
										+ rsBrandDiscountProducts.getInt("product_id") + ", "
										+ rsBrandDiscountProducts.getInt("quantity") + " ,"
										+ rsBrandDiscountProducts.getInt("cartons") + ")");
						s3.executeUpdate(
								"INSERT INTO `pep`.`mobile_order_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("
										+ SplitOrderID + ", " + rsBrandDiscountProducts.getLong("discount_brand_id")
										+ ", " + rsBrandDiscountProducts.getInt("brand_id") + ", "
										+ rsBrandDiscountProducts.getInt("product_id") + ", "
										+ rsBrandDiscountProducts.getInt("quantity") + " ,"
										+ rsBrandDiscountProducts.getInt("cartons") + ")");
					}

					if (ProductIDArray != null) {
						if (ProductIDArray.size() > 0) {
							final PromotionItem[] PromotionProducts = Product.getPromotionItems(OutletID,
									ArrayUtils.toPrimitive(
											(Integer[]) ProductIDArray.toArray(new Integer[ProductIDArray.size()])),
									ArrayUtils.toPrimitive(
											(Long[]) TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
							for (int i = 0; i < PromotionProducts.length; ++i) {
								final long[] RawCasesAndUnits = Utilities.getRawCasesAndUnits(
										PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
								long ProSAPCode = 0L;
								int ProProductID = 0;
								double ProSellingPriceRawCase = 0.0;
								double ProSellingPriceUnit = 0.0;
								long ProLiquidInML = 0L;
								int BrandID = 0;
								int SelectedBrandID = 0;
								System.out.println(
										"SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "
												+ OrderID + " and moup.promotion_id = "
												+ PromotionProducts[i].PROMOTION_ID);
								final ResultSet rs4 = s4.executeQuery(
										"SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "
												+ OrderID + " and moup.promotion_id = "
												+ PromotionProducts[i].PROMOTION_ID);
								if (rs4.first()) {
									SelectedBrandID = rs4.getInt(1);
								}
								if (PromotionProducts[i].BRANDS.size() > 0) {
									BrandID = PromotionProducts[i].BRANDS.get(0);
								}
								if (SelectedBrandID != 0) {
									BrandID = SelectedBrandID;
								}
								System.out.println("here 1 ....");
								if (BrandID != 0) {
									final Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID,
											BrandID);
									ProProductID = PromotionProduct.PRODUCT_ID;
									ProSAPCode = PromotionProduct.SAP_CODE;
									final double[] rates = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
									ProSellingPriceRawCase = rates[0];
									ProSellingPriceUnit = rates[1];
									ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
									System.out.println("here 2 ....");
									final double AmountRawCases2 = Utilities
											.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
													RawCasesAndUnits[0] * ProSellingPriceRawCase));
									final double AmountUnits2 = Utilities.parseDouble(Utilities
											.getDisplayCurrencyFormatSimple(RawCasesAndUnits[1] * ProSellingPriceUnit));
									final double TotalAmount2 = Utilities.parseDouble(
											Utilities.getDisplayCurrencyFormatSimple(AmountRawCases2 + AmountUnits2));
									// final double WHTaxAmount2 =
									// Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 *
									// WHTaxRate / 100.0));
									System.out.println("here 3 ....");

									final double WHTaxAmount = 0;
									final double SalesTaxAmount = 0;
									System.out.println("here 4 ....");
									final double NetAmount2 = Utilities
											.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
													TotalAmount2 + WHTaxAmount + SalesTaxAmount));
									System.out.println(
											"replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
													+ SplitOrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0]
													+ ", " + RawCasesAndUnits[1] + ", "
													+ PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML + ", "
													+ ProSellingPriceRawCase + ", " + ProSellingPriceUnit + ", "
													+ AmountRawCases2 + ", " + AmountUnits2 + ", " + TotalAmount2 + ", "
													+ WHTaxAmount + "," + SalesTaxAmount + " ," + NetAmount2 + ", 1, "
													+ PromotionProducts[i].PROMOTION_ID + ")  ");
									s4.executeUpdate(
											"replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values ("
													+ SplitOrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0]
													+ ", " + RawCasesAndUnits[1] + ", "
													+ PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML + ", "
													+ ProSellingPriceRawCase + ", " + ProSellingPriceUnit + ", "
													+ AmountRawCases2 + ", " + AmountUnits2 + ", " + TotalAmount2 + ", "
													+ WHTaxAmount + "," + SalesTaxAmount + " ," + NetAmount2 + ", 1, "
													+ PromotionProducts[i].PROMOTION_ID + ")  ");
								}
							}
						}
					}
					System.out.println("here....");
					InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));

					InvoiceNetAmount = Utilities
							.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
					// final double TotalAmountExSalesTax =
					// Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount
					// / (SalesTaxRate + 100.0) * 100.0));
					// final double SalesTaxAmount =
					// Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount
					// - TotalAmountExSalesTax));
					String InoviceTotalAmountString = new StringBuilder(String.valueOf(InvoiceNetAmount)).toString();
					if (InoviceTotalAmountString.indexOf(".") != -1) {
						final double Fraction = Utilities.parseDouble(InoviceTotalAmountString
								.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
						InoviceTotalAmountString = InoviceTotalAmountString.substring(0,
								InoviceTotalAmountString.indexOf("."));
						if (Fraction != 0.0) {
							InoviceTotalAmountString = new StringBuilder(
									String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1)).toString();
						}
					}
					final double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
					s5.executeUpdate("update mobile_order set invoice_amount = " + InvoiceAmount
							+ ", sales_tax_amount  = " + InvoiceSalesTaxAmount + ", income_tax_amount = "
							+ InvoiceIncomeTaxAmount + ", sales_tax_amount=" + InvoiceSalesTaxAmount
							+ ",total_amount = " + InvoiceNetAmount + ", fraction_adjustment = "
							+ Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = "
							+ InoviceTotalAmountString + " where id = " + SplitOrderID);
					s5.executeUpdate("update mobile_order_unedited set is_processed = 1 where id = " + OrderID);
					if (InvoiceAmount != 0.0) {
						ds.commit();
						try {
							final long iOrderID = SplitOrderID;
							// final Thread smsthread = (Thread)new SalesPosting.SalesPosting$1(iOrderID);
							Thread smsthread = new Thread() {
								public void run() {
									try {
										Utilities.sendSMSOrderBookering(iOrderID);
									} catch (IOException e) {
										System.out.println("Sales Posting (SMS Attempt Thread):");
										e.printStackTrace();
									}
								}
							};
							smsthread.start();
						} catch (Exception e) {
							System.out.println("Sales Posting (SMS Attempt):");
							e.printStackTrace();
						}
					} else {
						ds.rollback();
					}
				} catch (SQLException e2) {
					System.out.println("Split Order: OrderID " + OrderID + "\n" + e2);
				}
			}
			s5.close();
			s4.close();
			s3.close();
			s2.close();
			s.close();
			success = true;
		} catch (Exception e3) {
			System.out.println("Split Order: OrderID " + OrderID + "\n" + e3);
			try {
				ds.rollback();
			} catch (SQLException e4) {
				e4.printStackTrace();
				System.out.println("Split Order: OrderID " + OrderID + "\n" + e4);
			}
			if (ds != null) {
				try {
					ds.dropConnection();
				} catch (SQLException e5) {
					e5.printStackTrace();
				}
				return success;
			}
			return success;
		} finally {
			if (ds != null) {
				try {
					ds.dropConnection();
				} catch (SQLException e5) {
					e5.printStackTrace();
				}
			}
		}
		if (ds != null) {
			try {
				ds.dropConnection();
			} catch (SQLException e5) {
				e5.printStackTrace();
			}
		}
		return success;

	}

	@Override
	public int getBrandID(long PromotionID, int ProductID, int PromotionIDs)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		int pret = 0;

		if (ProductID == PromotionID) {
			pret = ProductID;
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

	@Override
	public boolean GetOrderExists(final Datasource ds, final String MobileRequestId) {
		boolean isExists = false;
		try {
			Statement s = ds.createStatement();
			System.out.println("SELECT id from mobile_order_unedited where mobile_order_no = " + MobileRequestId);
			ResultSet rsOrderExists = s
					.executeQuery("SELECT id from mobile_order_unedited where mobile_order_no = " + MobileRequestId);
			if (rsOrderExists.first()) {
				isExists = true;
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Check Order Exists Error :- " + e);
			isExists = false;
		}

		return isExists;
	}

}
