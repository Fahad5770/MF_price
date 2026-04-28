package com.pbc.distributor;

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

import org.json.simple.JSONObject;

import com.pbc.inventory.SalesPosting;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.FormulaUtills;
import com.pbc.util.MathUtils;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Dispatch Adjustment", urlPatterns = { "/distributor/DispatchAdjustmentExecute" })
public class DispatchAdjustmentExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DispatchAdjustmentExecute() {
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

		long DispatchID = Utilities.parseLong(request.getParameter("DispatchID"));
		long InvoiceID = Utilities.parseLong(request.getParameter("InvoiceID"));
		int ProductID = Utilities.parseInt(request.getParameter("ProductID"));
		int RawCases = Utilities.parseInt(request.getParameter("RawCases"));
		int Units = Utilities.parseInt(request.getParameter("Units"));
		long LiquidInMl = Utilities.parseLong(request.getParameter("LiquInMl"));
		long UnitPerSKU = Utilities.parseLong(request.getParameter("UnitPerSKU"));
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		int IsAddClcked = Utilities.parseInt(request.getParameter("IsAddClicked"));

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			if (Utilities.parseInt(request.getParameter("isEditCase")) == 2) // for delete case
			{
				long DispatchIDdlt = Utilities.parseLong(request.getParameter("DispatchID"));
				int ProductIDdlt = Utilities.parseInt(request.getParameter("ProductID"));
				long OutletIDdlt = Utilities.parseLong(request.getParameter("OutletID"));
				long InvoiceIDD = Utilities.parseLong(request.getParameter("InvoiceID"));
				int IsPromotion = Utilities.parseInt(request.getParameter("isPromotion"));

				/**************** Brand discount Working Start **************/
				
				if (IsPromotion == 0) {
					int deleteRawCases = Utilities.parseInt(request.getParameter("quantity"));
					//UpdateBrandDiscount(InvoiceIDD, ProductIDdlt, deleteRawCases, 0);
				}
				/**************** Brand discount Working End **************/

//				System.out.println(
//						"Delete from inventory_sales_dispatch_returned_products where is_empty=0 and dispatch_id="
//								+ DispatchIDdlt + " and product_id=" + ProductIDdlt + " and (invoice_id=" + InvoiceIDD
//								+ " or invoice_id is null ) and (outlet_id=" + OutletIDdlt + " or outlet_id is null )");
				s.executeUpdate(
						"Delete from inventory_sales_dispatch_returned_products where is_empty=0 and dispatch_id="
								+ DispatchIDdlt + " and product_id=" + ProductIDdlt + " and (invoice_id=" + InvoiceIDD
								+ " or invoice_id is null ) and (outlet_id=" + OutletIDdlt + " or outlet_id is null )");

//				System.out.println("delete from inventory_sales_dispatch_adjusted_products where dispatch_id="
//						+ DispatchIDdlt + " and outlet_id=" + OutletIDdlt + " and product_id=" + ProductIDdlt
//						+ " and invoice_id=" + InvoiceIDD + " and is_promotion=" + IsPromotion);
				s.executeUpdate("delete from inventory_sales_dispatch_adjusted_products where dispatch_id="
						+ DispatchIDdlt + " and outlet_id=" + OutletIDdlt + " and product_id=" + ProductIDdlt
						+ " and invoice_id=" + InvoiceIDD + " and is_promotion=" + IsPromotion);

				boolean posted = SalesPosting.post(InvoiceID, Long.parseLong(UserID));

				StockPosting sp = new StockPosting();
				boolean isPosted = sp.unPostStock(14, DispatchID);

				obj.put("success", "true");
			}

			if (IsAddClcked == 1) {
//				System.out.println(
//						"update inventory_sales_dispatch set is_liquid_returned=1,liquid_returned_on=now(),liquid_returned_by="
//								+ UserID + " where id=" + DispatchID);
				s.executeUpdate(
						"update inventory_sales_dispatch set is_liquid_returned=1,liquid_returned_on=now(),liquid_returned_by="
								+ UserID + " where id=" + DispatchID);

				// inserting in sub table

				long TotalUnits = (RawCases * UnitPerSKU) + Units;
				long LiquInML = TotalUnits * LiquidInMl;
				long AlreadyAdjusted = 0;
				long AlreadyAdjustedFinal = 0;
				long TotalQuanityDispatched = 0;
				long InvoiceTotalQuantity = 0;

//				System.out.println("select * from inventory_sales_dispatch_adjusted_products where dispatch_id="
//						+ DispatchID + " and outlet_id=" + OutletID + " and product_id=" + ProductID
//						+ " and invoice_id=" + InvoiceID + " and is_promotion=0");
				ResultSet rs = s
						.executeQuery("select * from inventory_sales_dispatch_adjusted_products where dispatch_id="
								+ DispatchID + " and outlet_id=" + OutletID + " and product_id=" + ProductID
								+ " and invoice_id=" + InvoiceID + " and is_promotion=0");
				if (rs.first()) {
					obj.put("error", "Package is already submitted");
				}
				// checking adjusted quanity and dispatched quantity
				else {
//					System.out.println(
//							"SELECT sum(total_units) already_adjusted FROM inventory_sales_dispatch_adjusted_products where dispatch_id="
//									+ DispatchID + " and product_id=" + ProductID);
					ResultSet rs1 = s.executeQuery(
							"SELECT sum(total_units) already_adjusted FROM inventory_sales_dispatch_adjusted_products where dispatch_id="
									+ DispatchID + " and product_id=" + ProductID);
					if (rs1.first()) {
						AlreadyAdjusted = Utilities.parseLong(rs1.getString("already_adjusted"));
						AlreadyAdjustedFinal = AlreadyAdjusted + TotalUnits; // virtual sum of units to find new
																				// adjusted units
					}
//					System.out.println(
//							"SELECT sum(total_units) units FROM inventory_sales_dispatch_returned_products where dispatch_id="
//									+ DispatchID + " and product_id=" + ProductID);
					ResultSet rs2 = s.executeQuery(
							"SELECT sum(total_units) units FROM inventory_sales_dispatch_returned_products where dispatch_id="
									+ DispatchID + " and product_id=" + ProductID);
					if (rs2.first()) {
						TotalQuanityDispatched = Utilities.parseLong(rs2.getString("units"));
					}

					// checking for quantity agains each invoice
//					System.out.println(
//							"SELECT sum(isip.total_units) units FROM inventory_sales_invoices_products isip where isip.id="
//									+ InvoiceID + " and isip.product_id=" + ProductID);
					ResultSet rs3 = s.executeQuery(
							"SELECT sum(isip.total_units) units FROM inventory_sales_invoices_products isip where isip.id="
									+ InvoiceID + " and isip.product_id=" + ProductID);

					if (rs3.first()) {
						if (rs3.getString("units") != null) {
							InvoiceTotalQuantity = rs3.getLong("units");

//								if(AlreadyAdjustedFinal>TotalQuanityDispatched)
//								{
//									obj.put("error", "Quantity Adjusted should be less than or equal to Dispatched quantity");
//								}
//								
//								else if(TotalUnits>InvoiceTotalQuantity) //check for invoice summary quantity
//								{
//									obj.put("error", "Quantity Adjusted should be less than or equal to Dispatched quantity against this Invoice Number");
//								}
//								else
//								{

//							System.out.println(
//									"insert into inventory_sales_dispatch_adjusted_products (dispatch_id,outlet_id,product_id,raw_cases,units,total_units,liquid_in_ml,invoice_id) values("
//											+ DispatchID + "," + OutletID + "," + ProductID + "," + RawCases + ","
//											+ Units + "," + TotalUnits + "," + LiquInML + "," + InvoiceID + ")");
							s.executeUpdate(
									"insert into inventory_sales_dispatch_adjusted_products (dispatch_id,outlet_id,product_id,raw_cases,units,total_units,liquid_in_ml,invoice_id) values("
											+ DispatchID + "," + OutletID + "," + ProductID + "," + RawCases + ","
											+ Units + "," + TotalUnits + "," + LiquInML + "," + InvoiceID + ")");

							// System.out.println("Delete from inventory_sales_dispatch_returned_products
							// where is_empty=0 and dispatch_id="+DispatchID+" and product_id="+ProductID);
							// s.executeUpdate("Delete from inventory_sales_dispatch_returned_products where
							// is_empty=0 and dispatch_id="+DispatchID+" and product_id="+ProductID);

//							System.out.println(
//									"insert into inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml,outlet_id,invoice_id) values("
//											+ DispatchID + "," + ProductID + "," + RawCases + "," + Units + ","
//											+ TotalUnits + "," + LiquInML + ", " + OutletID + ", " + InvoiceID + ")");
							s.executeUpdate(
									"insert into inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml,outlet_id,invoice_id) values("
											+ DispatchID + "," + ProductID + "," + RawCases + "," + Units + ","
											+ TotalUnits + "," + LiquInML + ", " + OutletID + ", " + InvoiceID + ")");

						

							
							ds.commit();

							boolean posted = SalesPosting.post(InvoiceID, Long.parseLong(UserID));

							System.out.println("posted " + posted);
							if (posted == false) {
//								System.out.println(
//										"delete from inventory_sales_dispatch_adjusted_products where invoice_id = "
//												+ InvoiceID + " and product_id = " + ProductID + " and dispatch_id = "
//												+ DispatchID);
								s.executeUpdate(
										"delete from inventory_sales_dispatch_adjusted_products where invoice_id = "
												+ InvoiceID + " and product_id = " + ProductID + " and dispatch_id = "
												+ DispatchID);
								
								throw new SQLException("Could not post adjustment into sales");
							}

							StockPosting sp = new StockPosting();
							boolean isPosted = sp.postDispatchLiquidReturn(DispatchID);
							
							sp.close();
							
							/**************** Brand discount Working Start **************/
						//	UpdateBrandDiscount(InvoiceID, ProductID, RawCases, 1);
							/**************** Brand discount Working End **************/

							if (isPosted == true) {
								obj.put("success", "true");
							} else {
								obj.put("success", "false");
								obj.put("error", "Could not post stock in store.");
							}

							// obj.put("success", "true");
							// }

						} else {
							obj.put("error", "This Product can not be added against this invoice number");

						}

					}

					// obj.put("error", "Package is already submitted");
				}
			}

			s.close();

		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (ds != null) {
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		out.print(obj);
		out.close();

	}

	public void UpdateBrandDiscount(long InvoiceID, int ProductID, int RawCases, int operation) {
		Datasource ds = new Datasource();
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			int brandID = 0, oldQuantity = 0, newCartonsBrand = 0, newCartons = 0, newQuantity = 0;
			long orderid = 0, discountID = 0;
			System.out.println("select lrb_type_id from inventory_products_view where product_id=" + ProductID);
			ResultSet discountbrand = s
					.executeQuery("select lrb_type_id from inventory_products_view where product_id=" + ProductID);
			if (discountbrand.first()) {
				brandID = discountbrand.getInt("lrb_type_id");
			}

			System.out.println(
					"select order_id, brand_discount_amount from inventory_sales_invoices where id=" + InvoiceID);
			ResultSet discountOrder = s.executeQuery(
					"select order_id, brand_discount_amount from inventory_sales_invoices where id=" + InvoiceID);
			if (discountOrder.first()) {
				orderid = discountOrder.getLong("order_id");
			}

			System.out.println("select discount_brand_id from mobile_order_brand_discount where id=" + orderid
					+ " and brand_id=" + brandID);
			ResultSet rsdiscountId = s
					.executeQuery("select discount_brand_id from mobile_order_brand_discount where id=" + orderid
							+ " and brand_id=" + brandID);
			if (rsdiscountId.first()) {
				discountID = rsdiscountId.getLong("discount_brand_id");
			}

			System.out.println("select quantity from mobile_order_brand_discount_products where id=" + orderid
					+ " and brand_id=" + brandID + " and product_id=" + ProductID);
			ResultSet rsProductOldQuantity = s
					.executeQuery("select quantity from mobile_order_brand_discount_products where id=" + orderid
							+ " and brand_id=" + brandID + " and product_id=" + ProductID);
			if (rsProductOldQuantity.first()) {
				oldQuantity = rsProductOldQuantity.getInt("quantity");
			}

			System.out.println("oldQuantity " + oldQuantity);
			System.out.println("RawCases " + RawCases);

			newQuantity = (operation == 0) ? (oldQuantity + RawCases) : (oldQuantity - RawCases);
			System.out.println("newQuantity " + newQuantity);
			double cartonsOfProduct = FormulaUtills.CalculateProductsInCartons(newQuantity, ProductID);
			System.out.println("cartonsOfProduct " + cartonsOfProduct);
			newCartons = (MathUtils.checkZeroAfterDecimal(cartonsOfProduct)) ? (int) cartonsOfProduct : 0;

			System.out.println("update mobile_order_brand_discount_products set quantity=" + newQuantity + ", cartons="
					+ newCartons + " where id=" + orderid + " and brand_id=" + brandID + " and discount_brand_id="
					+ discountID + " and product_id=" + ProductID);

			s.executeUpdate("update mobile_order_brand_discount_products set quantity=" + newQuantity + ", cartons="
					+ newCartons + " where id=" + orderid + " and brand_id=" + brandID + " and discount_brand_id="
					+ discountID + " and product_id=" + ProductID);

			System.out.println("select sum(cartons) as cartons from mobile_order_brand_discount_products where id="
					+ orderid + " and brand_id=" + brandID);
			ResultSet rsnewTotalCartons = s
					.executeQuery("select sum(cartons) as cartons from mobile_order_brand_discount_products where id="
							+ orderid + " and brand_id=" + brandID);
			if (rsnewTotalCartons.first()) {
				newCartonsBrand = rsnewTotalCartons.getInt("cartons");
			}
			
			double updatedbrandDiscount=0;
			System.out.println(
					"SELECT discount FROM pep.inventory_hand_to_hand_discount_brand_details where hand_discount_id="
							+ discountID + " and " + newCartonsBrand + " >= from_qty and " + newCartonsBrand
							+ " <=to_qty");
			ResultSet rsGwtDiscount = s2.executeQuery(
					"SELECT discount FROM pep.inventory_hand_to_hand_discount_brand_details where hand_discount_id="
							+ discountID + " and " + newCartonsBrand + " >= from_qty and " + newCartonsBrand
							+ " <=to_qty");
			if (rsGwtDiscount.first()) {
				updatedbrandDiscount=rsGwtDiscount.getDouble("discount");
				
			}
			
			System.out.println("Update mobile_order_brand_discount set brand_discount_amount="
					+ (updatedbrandDiscount * newCartonsBrand) + ", cartons=" + newCartonsBrand
					+ " where brand_id=" + brandID + " and discount_brand_id=" + discountID + " and id=" + orderid);
			s3.executeUpdate("Update mobile_order_brand_discount set brand_discount_amount="
					+ (updatedbrandDiscount * newCartonsBrand) + ", cartons=" + newCartonsBrand
					+ " where brand_id=" + brandID + " and discount_brand_id=" + discountID + " and id=" + orderid);

			double newBrandAmount = 0;

			System.out.println(
					"select sum(brand_discount_amount) as new_amount from mobile_order_brand_discount where id="
							+ orderid);
			ResultSet rsNewBrandAmount = s2.executeQuery(
					"select sum(brand_discount_amount) as new_amount from mobile_order_brand_discount where id="
							+ orderid);
			if (rsNewBrandAmount.first()) {
				newBrandAmount = rsNewBrandAmount.getDouble("new_amount");
			}

			System.out.println(
					"update mobile_order set brand_discount_amount=" + newBrandAmount + " where id=" + orderid);
			s1.executeUpdate(
					"update mobile_order set brand_discount_amount=" + newBrandAmount + " where id=" + orderid);
			System.out.println("update inventory_sales_invoices set brand_discount_amount=" + newBrandAmount
					+ " where id=" + InvoiceID + " and order_id=" + orderid);
			s1.executeUpdate("update inventory_sales_invoices set brand_discount_amount=" + newBrandAmount
					+ " where id=" + InvoiceID + " and order_id=" + orderid);
			System.out.println("update inventory_sales_adjusted set brand_discount_amount=" + newBrandAmount
					+ " where id=" + InvoiceID + " and order_id=" + orderid);
			s1.executeUpdate("update inventory_sales_adjusted set brand_discount_amount=" + newBrandAmount
					+ " where id=" + InvoiceID + " and order_id=" + orderid);
			
			s.close();
			s1.close();
			s2.close();
			s3.close();
			
			ds.commit();

		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} finally {
			try {
				if (ds != null) {
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
