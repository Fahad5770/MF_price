package com.pbc.inventory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

import com.opencsv.CSVWriter;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class R381GetSalesCSV {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;

	Date StartDate = Utilities.parseDate("01/03/2020");
	Date EndDate = Utilities.parseDate("20/03/2020");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public R381GetSalesCSV() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	@SuppressWarnings("unused")
	public void createPdf(String filename, long SND_ID, String StartDate, String EndDate, String DistributorID)
			throws IOException, SQLException {

		FileWriter outputfile = new FileWriter(filename);

		CSVWriter writer = new CSVWriter(outputfile);

		String WhereDistributor = "";
		if (!DistributorID.equals("")) {
			WhereDistributor = " and (SD_DISTRIBUTOR_ID in(" + DistributorID + "))";
		}

		String[] header = { "Created Date", "Adjusted Date",
				"Distributor ID", "Distributor Name", "City Name",  "Order ID", "Outlet ID", "Outlet Name","Order Booker ID", "Order Booker Name", "SND Name", "RSM Name",
				 "Channel Label", "Region Name", "PJP ID", "PJP Name",
				"Package Label", "Brand Label", "Cases", "Is Promotion", "Converted Cases",
				"Promotion Amount", "Promotion Cases", "Pre Tax Amount", "Tax Amount", "SKU Amount", "Discount Rate",
				"Hand To Hand Amount", "Is Dispatch Adjusted",
				"Sales Type" };
		writer.writeNext(header);
		Datasource ds = new Datasource();
		try {
			ds.createConnection();

			Statement s = ds.createStatement();
			
			System.out.println("Select * from get_sale WHERE"
					+ " (`SD_DISTRIBUTOR_ID` <> 123) and (SD_DISPATCH_CREATED_ON between '" + StartDate + "' and '"
					+ EndDate + "' )   " + WhereDistributor);
			ResultSet rs = s.executeQuery("Select * from get_sale WHERE"
					+ " (`SD_DISTRIBUTOR_ID` <> 123) and (SD_DISPATCH_CREATED_ON between '" + StartDate + "' and '"
					+ EndDate + "' )   " + WhereDistributor);

			while (rs.next()) {
				int saleTypeId = rs.getInt("SD_SALES_TYPE_ID");

				double WHTaxAmount = rs.getDouble("SD_WH_TAX_AMOUNT");
				double SalesTaxAmount = rs.getDouble("SD_SALES_TAX_AMOUNT");
				//System.out.println(WHTaxAmount);
				//System.out.println(SalesTaxAmount);
				if(WHTaxAmount==0) {
					HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(rs.getInt("SD_PRODUCT_ID"),
							rs.getLong("SD_OUTLET_ID"));
					WHTaxAmount = rs.getDouble("SD_CASES") * ProductsTax.get("wh_tax");
				}
				
				if(SalesTaxAmount==0) {
					HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(rs.getInt("SD_PRODUCT_ID"),
							rs.getLong("SD_OUTLET_ID"));
					SalesTaxAmount = rs.getDouble("SD_CASES") * ProductsTax.get("income_tax");
				}
				
				/*if (saleTypeId == 3) {
				    rsTax = s2.executeQuery(
				        "SELECT wh_tax_amount, sales_tax_amount " +
				        "FROM mobile_order_products " +
				        "WHERE id=" + rs.getString("SD_ORDER_ID") + 
				        " AND product_id=" + rs.getString("SD_PRODUCT_ID"));
				} else if (saleTypeId == 1) {
				    rsTax = s2.executeQuery(
				        "SELECT wh_tax_amount, sales_tax_amount " +
				        "FROM inventory_sales_invoices_products " +
				        "WHERE id=" + rs.getString("SD_ORDER_ID") + 
				        " AND product_id=" + rs.getString("SD_PRODUCT_ID"));
				}

				if (rsTax.first()) {
				    WHTaxAmount = rsTax.getDouble("wh_tax_amount");
				    SalesTaxAmount = rsTax.getDouble("sales_tax_amount");
				}*/

				
				double TaxAmount = WHTaxAmount + SalesTaxAmount;
				double product_amount = rs.getDouble("SD_SKU_AMOUNT") + WHTaxAmount + SalesTaxAmount;

				String[] data1 = { rs.getString("SD_CREATED_ON_DATE"),
						rs.getString("SD_ADJUSTED_ON_DATE"),
						rs.getString("SD_DISTRIBUTOR_ID"),
						rs.getString("SD_DISTRIBUTOR_LABEL"),
						rs.getString("SD_CITY"),						
						((rs.getString("SD_ORDER_ID") == null) ? rs.getString("ISA_SD_DOCUMENT_ID"): rs.getString("SD_ORDER_ID")),
						rs.getString("SD_OUTLET_ID"),
						rs.getString("SD_OUTLET_LABEL"),
						rs.getString("SD_ORDER_BOOKER_ID"),
						rs.getString("SD_ORDER_BOOKER_NAME"),
						rs.getString("SD_SND_NAME"),
						rs.getString("SD_RSM_NAME"),
						rs.getString("SD_CHANNEL_LABEL"),
						rs.getString("SD_REGION_LABEL"),
						rs.getString("PJP_ID"),
						rs.getString("PJP_LABEL"),
						rs.getString("SD_PACKAGE_LABEL"),
						rs.getString("SD_BRAND_LABEL"),
						rs.getString("SD_CASES"),
						rs.getString("SD_IS_PROMOTION"),
						rs.getString("SD_CONVERTED_CASES"),
						rs.getString("SD_PROMOTION_AMOUNT"),
						rs.getString("SD_PROMOTION_CASES"),
						rs.getString("SD_SKU_PRE_TAX_Amount"),
						TaxAmount+"",
						String.valueOf(product_amount),
						/* rs.getString("SD_SKU_AMOUNT"), */ 
						rs.getString("SD_DISCOUNT_RATE"),
						rs.getString("SD_HAND_TO_HAND_AMOUNT"),
						rs.getString("SD_IS_DISPATCH_ADJUSTED"),
						rs.getString("SD_SALES_TYPE"),
				};

				writer.writeNext(data1);
				// }

			}
		} catch (Exception e) {

			e.printStackTrace();

		}
		writer.close();

		ds.dropConnection();
	}

}
