package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;

@WebServlet(description = "Save or Get Product Tax with Logs", urlPatterns = { "/inventory/AddSalesTax" })
public class AddSalesTax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// GET: fetch existing tax values

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		String productId = request.getParameter("product_id");
		
		if (productId == null || productId.isEmpty()) {
			json.put("status", "error");
			json.put("message", "Product ID is missing");
			out.print(json.toJSONString());
			return;
		}

		Datasource ds = new Datasource();
		Statement st = null;
		ResultSet rs = null;

		try {
			ds.createConnection();
			Connection con = ds.getConnection();
			st = con.createStatement();

			// Sales Tax
			rs = st.executeQuery("SELECT * FROM inventory_products_sales_tax WHERE product_id = " + productId);
			if (rs.next()) {
				json.put("sales_tax_reg_filer", rs.getString("register_filer"));
				json.put("sales_tax_reg_nonfiler", rs.getString("register_non_filer"));
				json.put("sales_tax_unreg_filer", rs.getString("unregister_filer"));
				json.put("sales_tax_unreg_nonfiler", rs.getString("unregister_non_filer"));
			} else {
				json.put("sales_tax_reg_filer", "0.0");
				json.put("sales_tax_reg_nonfiler", "0.0");
				json.put("sales_tax_unreg_filer", "0.0");
				json.put("sales_tax_unreg_nonfiler", "0.0");
			}
			rs.close();

			// Income Tax
			rs = st.executeQuery("SELECT * FROM inventory_products_income_tax WHERE product_id = " + productId);
			if (rs.next()) {
				json.put("income_tax_reg_filer", rs.getString("register_filer"));
				json.put("income_tax_reg_nonfiler", rs.getString("register_non_filer"));
				json.put("income_tax_unreg_filer", rs.getString("unregister_filer"));
				json.put("income_tax_unreg_nonfiler", rs.getString("unregister_non_filer"));
			} else {
				json.put("income_tax_reg_filer", "0.0");
				json.put("income_tax_reg_nonfiler", "0.0");
				json.put("income_tax_unreg_filer", "0.0");
				json.put("income_tax_unreg_nonfiler", "0.0");
			}

			json.put("status", "success");
			ds.dropConnection();

		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "error");
			json.put("message", e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
		}

		out.print(json.toJSONString());
		out.close();
	}

	// POST: save/update tax values and maintain logs
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String UserID = null;

		response.setContentType("application/json");

		HttpSession session = request.getSession();

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		String productId = request.getParameter("Product");
		if (productId == null || productId.isEmpty()) {
			json.put("status", "error");
			json.put("message", "Product ID is missing");
			out.print(json.toJSONString());
			return;
		}

		Datasource ds = new Datasource();
		Statement st = null;
		ResultSet rsCheck = null;

		try {
			ds.createConnection();
			Connection con = ds.getConnection();
			st = con.createStatement();

			// ========== SALES TAX Start ==========
			
			String st_reg_filer = request.getParameter("sales_tax_reg_filer");
			String st_reg_nonfiler = request.getParameter("sales_tax_reg_nonfiler");
			String st_unreg_filer = request.getParameter("sales_tax_unreg_filer");
			String st_unreg_nonfiler = request.getParameter("sales_tax_unreg_nonfiler");

			rsCheck = st.executeQuery("SELECT * FROM inventory_products_sales_tax WHERE product_id = " + productId);

			if (rsCheck.next()) {

				// Insert old record into logs

				String logST = "INSERT INTO peplogs.inventory_products_sales_tax "
						+ "(product_id, register_filer, register_non_filer, unregister_filer, unregister_non_filer,updated_on,updated_by,created_on,created_by) "
						+ "VALUES (" + productId + ", '" + rsCheck.getString("register_filer") + "', '"
						+ rsCheck.getString("register_non_filer") + "', '" + rsCheck.getString("unregister_filer")
						+ "', '" + rsCheck.getString("unregister_non_filer") + "', now() , " + UserID + " , '" + rsCheck.getTimestamp("created_on") + "' , '" + rsCheck.getInt("created_by") + "')";
				
				st.executeUpdate(logST);

				// Delete old record
				st.executeUpdate("DELETE FROM inventory_products_sales_tax WHERE product_id = " + productId);
				
			}
			rsCheck.close();

			// Insert new record
			
			String insertST = "INSERT INTO inventory_products_sales_tax "
					+ "(product_id, register_filer, register_non_filer, unregister_filer, unregister_non_filer,created_on,created_by) "
					+ "VALUES (" + productId + ", '" + st_reg_filer + "', '" + st_reg_nonfiler + "', '" + st_unreg_filer
					+ "', '" + st_unreg_nonfiler + "', now() , " + UserID + ")";
			
			st.executeUpdate(insertST);
			
			// ========== SALES TAX End ==========


			// ========== INCOME TAX  Start ==========
			
			String it_reg_filer = request.getParameter("income_tax_reg_filer");
			String it_reg_nonfiler = request.getParameter("income_tax_reg_nonfiler");
			String it_unreg_filer = request.getParameter("income_tax_unreg_filer");
			String it_unreg_nonfiler = request.getParameter("income_tax_unreg_nonfiler");
			
			rsCheck = st.executeQuery("SELECT * FROM inventory_products_income_tax WHERE product_id = " + productId);
			
			if (rsCheck.next()) {
				
				// Insert old record into logs
				String logIT = "INSERT INTO peplogs.inventory_products_income_tax "
						+ "(product_id, register_filer, register_non_filer, unregister_filer, unregister_non_filer,updated_on,updated_by,created_on,created_by) "
						+ "VALUES (" + productId + ", '" + rsCheck.getString("register_filer") + "', '"
						+ rsCheck.getString("register_non_filer") + "', '" + rsCheck.getString("unregister_filer")
						+ "', '" + rsCheck.getString("unregister_non_filer") + "', now() , " + UserID + " , '" + rsCheck.getTimestamp("created_on") + "' , '" + rsCheck.getInt("created_by") + "')";
				
				st.executeUpdate(logIT);

				// Delete old record
				st.executeUpdate("DELETE FROM inventory_products_income_tax WHERE product_id = " + productId);
				
			}
			rsCheck.close();

			// Insert new record
			
			String insertIT = "INSERT INTO inventory_products_income_tax "
					+ "(product_id, register_filer, register_non_filer, unregister_filer, unregister_non_filer,created_on,created_by) "
					+ "VALUES (" + productId + ", '" + it_reg_filer + "', '" + it_reg_nonfiler + "', '" + it_unreg_filer
					+ "', '" + it_unreg_nonfiler + "' , now() , " + UserID + ")";
			
			st.executeUpdate(insertIT);
			
			// ========== INCOME TAX  End ==========


			ds.dropConnection();

			json.put("status", "success");
			json.put("message", "Product tax saved successfully!");

		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "error");
			json.put("message", e.getMessage());
		} finally {
			try {
				if (rsCheck != null)
					rsCheck.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
		}

		out.print(json.toJSONString());
		out.close();
	}
}
