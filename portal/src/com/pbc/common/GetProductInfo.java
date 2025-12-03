package com.pbc.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.inventory.Product;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class GetProductInfo
 */
@WebServlet(description = "Get Product Information", urlPatterns = { "/common/GetProductInfo" })
public class GetProductInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetProductInfo() {
		super();
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

		if (session.getAttribute("UserID") == null) {
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}

		long ProductID = Utilities.parseLong(request.getParameter("ProductCode"));
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));

		int PackageID = Utilities.parseInt(request.getParameter("PackageID"));
		int BrandID = Utilities.parseInt(request.getParameter("BrandID"));

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();

		try {

			response.setContentType("application/json");

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			if (PackageID != 0 && BrandID != 0) {

				ResultSet rs3 = s.executeQuery("select sap_code from inventory_products where package_id=" + PackageID
						+ " and brand_id=" + BrandID + " and category_id=1");
				if (rs3.first()) {
					ProductID = rs3.getLong("sap_code");
				}

			}

			String PackageLabel = "";
			String BrandLabel = "";

			ResultSet rs = s.executeQuery(
					"SELECT  package_id, (select label from inventory_packages where id = package_id) package_label, brand_id, (select label from inventory_brands where id = brand_id) brand_label, sap_code, id, unit_per_sku, (select liquid_in_ml from inventory_packages where id = package_id) liquid_in_ml, (select shell_product_id from inventory_products_map where product_id=id limit 1) shell_product_id, id,is_other_brand FROM inventory_products where sap_code = "
							+ ProductID);
			if (rs.first()) {

				obj.put("exists", "true");

				obj.put("PackageID", rs.getString(1));
				obj.put("PackageLabel", rs.getString(2));
				obj.put("BrandLabel", rs.getString(4));
				obj.put("ProductCode", rs.getString(5));
				obj.put("ProductID", rs.getString(6));
				obj.put("UnitPerSKU", rs.getString(7));
				obj.put("LiquidInML", rs.getString(8));
				obj.put("ShellProductID", rs.getString("shell_product_id"));
				obj.put("BrandID", rs.getString("brand_id"));
				obj.put("IsOtherBrand", rs.getString("is_other_brand"));

				// System.out.println(request.getParameter("OutletID"));
				if (request.getParameter("OutletID") != null) {
					double PriceArray[] = Product.getSellingPrice_2(ProductID, OutletID);

					obj.put("RawCasePricewithoutDisc", PriceArray[0]);
					obj.put("UnitPrice", PriceArray[1]);
					obj.put("Discount", PriceArray[2]);
					
					double RawCasePrice = PriceArray[0] + PriceArray[2];
					
					obj.put("RawCasePrice", Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(RawCasePrice));

					
					System.out.println("Discount" + PriceArray[2]);

					HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(rs.getInt(6), OutletID);

					obj.put("WHTaxAmount", ProductsTax.get("wh_tax"));
					obj.put("SalesTaxAmount", ProductsTax.get("income_tax"));

				}

				String DamageStockTypeOptions = "<option value=''>Select Type</option>";

				ResultSet rs2 = s2.executeQuery(
						"SELECT damaged_stock_type_id, (select label from inventory_damaged_stock_types where id=damaged_stock_type_id) damaged_stock_type_name FROM pep.inventory_products_damaged_type_map where product_id = "
								+ rs.getInt("id") + " order by damaged_stock_type_name;");
				while (rs2.next()) {
					DamageStockTypeOptions += "<option value='" + rs2.getString("damaged_stock_type_id") + "'>"
							+ rs2.getString("damaged_stock_type_name") + "</option>";
				}

				obj.put("DamageStockTypeOptions", DamageStockTypeOptions);

			} else {
				obj.put("exists", "false");
			}

			s.close();
			ds.dropConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}

		out.print(obj);
		out.close();

	}

}
