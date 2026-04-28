package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Stock Stock Adjustment", urlPatterns = { "/inventory/StockTransferExecute" })
public class StockTransferExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StockTransferExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		int DocumentTypeID = 67;

		String UserID = null;

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

		int EditID = Utilities.parseInt(request.getParameter("PhysicalStockAdjustmentEditID"));
		boolean isEditCase = false;

		if (EditID > 0) {
			isEditCase = true;
		}
		Date StartDate = (Date) session.getAttribute(UniqueSessionID + "_SR1StartDate");
		Date EndDate = (Date) session.getAttribute(UniqueSessionID + "_SR1EndDate");

		if (StartDate == null) {
			StartDate = new Date();
		}

		if (EndDate == null) {
			EndDate = new Date();
		}
		Date ClosingDate = DateUtils.addDays(StartDate, 1);
		System.out.println("OpeningDate=======>" + ClosingDate);

		long DistributorID = Utilities.parseLong(request.getParameter("DeliveryNoteDistributorID"));
		long DistributorID3 = Utilities.parseLong(request.getParameter("DeliveryNoteDistributorID4"));

		System.out.println("Distributor================>>>>>>>>>> " + DistributorID);
		System.out.println("Distributor================>>>>>>>>>> " + DistributorID3);

		String VehicleNo = Utilities.filterString(request.getParameter("DeliveryNoteVehicleNo"), 1, 100);

		String Remarks = Utilities.filterString(request.getParameter("DeliveryNoteRemarks"), 1, 100);

		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		//int RawCases[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormUnits"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormUnitPerSKU"));
		double LiquidInML[] = Utilities.parseDouble(request.getParameterValues("DeliveryNoteMainFormLiquidInML"));
		//double Raw_Cases_Price[] = Utilities.parseDouble(request.getParameterValues("Raw_cases_price"));

		/*
		 * //String BatchCode[] = Utilities.filterString(request.getParameterValues(
		 * "DeliveryNoteMainFormBatchCode"), 1, 100);
		 */

		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));


		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();

		Date VoucherDate = new java.util.Date();

		StockDocument PhysicalStockAdjustmentDocument = new StockDocument();
		StockDocument PhysicalStockAdjustmentDocuments = new StockDocument();

		try {

			StockPosting PhysicalStockAdjustmentStockPosting = new StockPosting();

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();


			String SQLMain = "";
			String Error = "";
			ResultSet rs1 = s1
					.executeQuery("select id from inventory_distributor_stock_transfer where uvid=" + UniqueVoucherID);
			if (rs1.first()) {

				obj.put("success", "false");
				obj.put("error", "Already Exists");

			} else {
				int PhysicalStockAdjustmentID = 0;
					if(ProductID.length > 0) {
						if (isEditCase) {
							SQLMain = "replace into inventory_distributor_stock_transfer (id, uvid, created_on, created_by, from_distributor_id) values("
									+ EditID + ", " + UniqueVoucherID + ", now(), " + UserID + ", "
									+ DistributorID + ")";
						} else {
							SQLMain = "insert into inventory_distributor_stock_transfer (uvid, created_on, created_by, from_distributor_id,to_distributor_id) values("
									+ UniqueVoucherID + ", now(), " + UserID + ", " + DistributorID + ", "
									+ DistributorID3 + " )";
						}

						s.executeUpdate(SQLMain);
						System.out.println(SQLMain);
						if (!isEditCase) {
							ResultSet rs_1 = s.executeQuery("select LAST_INSERT_ID()");
							if (rs_1.first()) {
								PhysicalStockAdjustmentID = rs_1.getInt(1);
							}
							//System.out.println("================if");

						} else {
							PhysicalStockAdjustmentID = EditID;

							PhysicalStockAdjustmentStockPosting.unPostStock(DocumentTypeID,
								PhysicalStockAdjustmentID);
							//System.out.println("================else");
						}
					}
				for (int i = 0; i < ProductID.length; i++) {
					double Raw_cases_price = 0.0;
					double Unit_Price = 0.0;
					//double Raw_cases_Value = RawCases[i] * Raw_Cases_Price[i];
				//	System.out.println("select raw_case,unit from inventory_price_list_products where product_id="+ProductID[i]);
					ResultSet rs10 = s2.executeQuery("select raw_case,unit from inventory_price_list_products where product_id="+ProductID[i]);
					while(rs10.next()){
						Raw_cases_price = rs10.getDouble("raw_case");
						Unit_Price = rs10.getDouble("unit");

					}
					//double Raw_cases_Value = Raw_cases_price * RawCases[i];
					double units_Value = Unit_Price * Units[i];

					//System.out.println("Units........."+Units[i]);
					if( Units[i] != 0 ) {
					StockPosting sp = new StockPosting(true);
					long TotalUnit = ( UnitPerSKU[i]) * Units[i];

					double LiquidInMLValues = TotalUnit * LiquidInML[i];
					//System.out.println("TotalUnits2   :" + TotalUnit);
					//System.out.println("LiquidInMLValue" + LiquidInMLValues);
					//System.out.println("LiquidInML : " + LiquidInML[i]);

					long UnitAfterDispatch = sp.getBalanceafterdispatch(DistributorID, (int) ProductID[i]);

					long ClosingUnits = sp.getClosingBalance(DistributorID, (int) ProductID[i], ClosingDate);

					//System.out.println("Units : " + Units[i]);

					long RemainingUnits = ClosingUnits - UnitAfterDispatch;
					//System.out.println("RemainingUnits" + RemainingUnits);
					//System.out.println(Units[i] > RemainingUnits);
					//System.out.println(Units[i]);
					
					if (Units[i] > RemainingUnits) {
						
						if ( Units[i] != 0) {
							String Error_lable = "";
							ResultSet rs = s.executeQuery(
									"select concat(brand_label,\"-\",package_label) as label from inventory_products_view where product_id="
											+ ProductID[i]);
							if (rs.first()) {
								Error_lable = rs.getString("label");
							}
							Error +=" " +Error_lable + " " + "does Not Transfer due to shortage of Stock..." + " ";

						}
					//	System.out.println("================ifmain");

					} else {
						

						

								// s.getConnection().setAutoCommit(false);

							

					

								// set document object

								PhysicalStockAdjustmentDocument.DISTRIBUTOR_ID = DistributorID;
								PhysicalStockAdjustmentDocument.DOCUMENT_TYPE_ID = DocumentTypeID;
								PhysicalStockAdjustmentDocument.DOCUMENT_ID = PhysicalStockAdjustmentID;
								PhysicalStockAdjustmentDocument.CREATED_ON = new java.util.Date();
								PhysicalStockAdjustmentDocument.CREATED_BY = Long.parseLong(UserID);

								/*
								 * s.
								 * executeUpdate("delete from inventory_distributor_stock_transfer_products where id="
								 * + PhysicalStockAdjustmentID);
								 */

								long TotalUnits =  UnitPerSKU[i] * Units[i];
								

								double LiquidInMLValue = TotalUnits * LiquidInML[i];
								//System.out.println("TotalUnits2   :" + TotalUnits);
								//System.out.println("LiquidInMLValue" + LiquidInMLValue);
								//System.out.println("LiquidInML : " + LiquidInML[i]);

								// double LiquidInMLValue = TotalUnits * LiquidInMLs[i];
								if (Units[i] != 0 ) {
									System.out.println(
											"insert into inventory_distributor_stock_transfer_products (id, product_id, raw_cases, units, total_units, liquid_in_ml,Raw_Cases_Amount,Units_Amount) values ("
													+ PhysicalStockAdjustmentID + ", " + ProductID[i] + ", "
													 + "0, " + Units[i] + ", " + TotalUnits + ", "
													+ LiquidInMLValue + ","+ 0 + ","+ units_Value + ") "
											);
									s.executeUpdate(
											"insert into inventory_distributor_stock_transfer_products (id, product_id, raw_cases, units, total_units, liquid_in_ml,Raw_Cases_Amount,Units_Amount) values ("
													+ PhysicalStockAdjustmentID + ", " + ProductID[i] + ", "
													+ 0 + ", " + Units[i] + ", " + TotalUnits + ", "
													+ LiquidInMLValue + ","+ 0 + ","+ units_Value + ") ");

									int DocumentTransactionType = 1;

									long TotalUnitsClosingBalance = PhysicalStockAdjustmentStockPosting
											.getClosingBalance(DistributorID, ProductID[i], new java.util.Date());

									long UnitsStockDifference = TotalUnitsClosingBalance - TotalUnits;

									/*
									 * if (UnitsStockDifference > 0) { DocumentTransactionType = 1; } else {
									 * DocumentTransactionType = 2; }
									 */
									/*
									 * if (UnitsStockDifference != 0) {
									 * 
									 * if (UnitsStockDifference < 0) { UnitsStockDifference *= -1; }
									 */

									long RawCasesDifference = Utilities.getRawCasesAndUnits(UnitsStockDifference,
											UnitPerSKU[i])[0];
									long UnitsDifference = Utilities.getRawCasesAndUnits(UnitsStockDifference,
											UnitPerSKU[i])[1];

									double LiquidInMLValueDifference = UnitsStockDifference * LiquidInML[i];

									StockDocumentItems PhysicalStockAdjustmentStockDocumentItems = new StockDocumentItems();

									PhysicalStockAdjustmentStockDocumentItems.PRODUCT_ID = ProductID[i];
									//PhysicalStockAdjustmentStockDocumentItems.RAW_CASES = (int) RawCases[i];
									PhysicalStockAdjustmentStockDocumentItems.UNITS = (int) Units[i];
									PhysicalStockAdjustmentStockDocumentItems.TOTAL_UNITS = (int) TotalUnits;
									PhysicalStockAdjustmentStockDocumentItems.LIQUID_IN_MLs = LiquidInMLValue;
									PhysicalStockAdjustmentStockDocumentItems.TRANSACTION_TYPE = DocumentTransactionType;
									PhysicalStockAdjustmentStockDocumentItems.LOCATION_ID = 1;

									PhysicalStockAdjustmentDocument.PRODUCTS
											.add(PhysicalStockAdjustmentStockDocumentItems);
									DocumentTransactionType = 2;
									StockDocumentItems PhysicalStockAdjustmentStockDocumentItem = new StockDocumentItems();

									PhysicalStockAdjustmentStockDocumentItem.PRODUCT_ID = ProductID[i];
									//PhysicalStockAdjustmentStockDocumentItem.RAW_CASES = (int) RawCases[i];
									PhysicalStockAdjustmentStockDocumentItem.UNITS = (int) Units[i];
									PhysicalStockAdjustmentStockDocumentItem.TOTAL_UNITS = (int) TotalUnits;
									PhysicalStockAdjustmentStockDocumentItem.LIQUID_IN_MLs = LiquidInMLValue;
									PhysicalStockAdjustmentStockDocumentItem.TRANSACTION_TYPE = DocumentTransactionType;
									PhysicalStockAdjustmentStockDocumentItem.LOCATION_ID = 1;

									PhysicalStockAdjustmentDocuments.PRODUCTS
											.add(PhysicalStockAdjustmentStockDocumentItem);

									// }
								}else {
									System.out.println("================");
								}
								ds.commit();

								PhysicalStockAdjustmentStockPosting.postStock(PhysicalStockAdjustmentDocument);
								DocumentTypeID = 68;

								PhysicalStockAdjustmentDocuments.DISTRIBUTOR_ID = DistributorID3;
								PhysicalStockAdjustmentDocuments.DOCUMENT_TYPE_ID = DocumentTypeID;
								PhysicalStockAdjustmentDocuments.DOCUMENT_ID = PhysicalStockAdjustmentID;
								PhysicalStockAdjustmentDocuments.CREATED_ON = new java.util.Date();
								PhysicalStockAdjustmentDocuments.CREATED_BY = Long.parseLong(UserID);
								PhysicalStockAdjustmentStockPosting.postStock(PhysicalStockAdjustmentDocuments);

						

						

					}
				}
				}

				System.out.println("Error=============>" + Error);
				Error = (Error.equals("")) ? "Success" : Error;

				obj.put("success", "true");
				obj.put("error", Error);

				// obj.put("success", "true");

				s.close();
				// boolean posted = SalesPosting.post(DeskSaleID, Long.parseLong(UserID));

				// System.out.println("success case");

				/*
				 * obj.put("success", "true"); obj.put("PhysicalStockAdjustmentID",
				 * PhysicalStockAdjustmentID);
				 */

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

}