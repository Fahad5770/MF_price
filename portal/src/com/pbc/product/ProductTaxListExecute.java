package com.pbc.product;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Price List", urlPatterns = { "/product/ProductTaxListExecute" })
public class ProductTaxListExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductTaxListExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		HttpSession session = request.getSession();
		String UserID = (String) session.getAttribute("UserID");

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
			return;
		}

		long[] PriceLTaxSapCode = Utilities.parseLong(request.getParameterValues("PriceLTaxSapCode"));
		double[] PriceTaxRegisterFiler = Utilities.parseDouble(request.getParameterValues("PriceTaxRegisterFiler"));
		double[] PriceTaxRegisterNonFiler = Utilities
				.parseDouble(request.getParameterValues("PriceTaxRegisterNonFiler"));
		double[] PriceTaxUnRegisterFiler = Utilities.parseDouble(request.getParameterValues("PriceTaxUnRegisterFiler"));
		double[] PriceTaxUnRegisterNonFiler = Utilities
				.parseDouble(request.getParameterValues("PriceTaxUnRegisterNonFiler"));

		try {
			ds.createConnection();
			ds.startTransaction(); // Begin transaction

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			// System.out.println ("delete from inventory_product_tax_rates");
			

			for (int i = 0; i < PriceLTaxSapCode.length; i++) {
				long sapCode = PriceLTaxSapCode[i];
				double registerFiler = PriceTaxRegisterFiler[i];
				double registerNonFiler = PriceTaxRegisterNonFiler[i];
				double unregisterFiler = PriceTaxUnRegisterFiler[i];
				double unregisterNonFiler = PriceTaxUnRegisterNonFiler[i];

				String selectQuery = "SELECT id FROM pep.inventory_products WHERE sap_code = " + sapCode;
				ResultSet rsProductIds = s.executeQuery(selectQuery);

				if (rsProductIds.next()) {
					int productId = rsProductIds.getInt("id");

					s1.executeUpdate(
							"INSERT INTO `peplogs`.`inventory_product_tax_rates_logs`(`product_id`,`register_filer`,`register_non_filer`,`unregister_filer`,`unregister_non_filer`,`created_on`,`created_by`,`is_active`,`updated_on`,`updated_by`) select *, now(), "+UserID+" from inventory_product_tax_rates where product_id="+productId);
					s1.executeUpdate("delete from inventory_product_tax_rates where product_id="+productId);
					s1.executeUpdate(
							"INSERT INTO inventory_product_tax_rates (product_id, register_filer, register_non_filer, unregister_filer, unregister_non_filer, created_on, created_by, is_active) "
									+ "VALUES (" + productId + ", " + registerFiler + ", " + registerNonFiler + ", "
									+ unregisterFiler + ",  " + unregisterNonFiler + " , NOW(), " + UserID + ", 1)");

				}
			}

			ds.commit();
			s.close();
			s1.close();
			ds.dropConnection();

			obj.put("success", "true");

		} catch (SQLException e) {
			obj.put("success", "false");
			obj.put("error", e.getMessage());
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.print(obj);
			out.close();
		}
	}
}
