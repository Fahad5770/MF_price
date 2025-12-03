package com.pbc.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class GetProductInfo
 */
@WebServlet(description = "Get Product Information", urlPatterns = { "/common/GetProductByLRBInfo" })
public class GetProductByLRBInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetProductByLRBInfo() {
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

		int BrandID = Utilities.parseInt(request.getParameter("brand"));

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		JSONArray jrProducts = new JSONArray();

		try {

			response.setContentType("application/json");

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			ResultSet rsBransLabel = s.executeQuery(
					"select label  from inventory_products_lrb_types where id="
							+ BrandID);
			if (rsBransLabel.first()) {
				obj.put("brand", rsBransLabel.getString("label"));
				
			}

			ResultSet rsProducts = s.executeQuery(
					"select product_id, concat(package_label, brand_label) as product  from inventory_products_view where lrb_type_id="
							+ BrandID);
			while (rsProducts.next()) {
				LinkedHashMap rows = new LinkedHashMap();
				rows.put("product_id", rsProducts.getInt("product_id"));
				rows.put("product", rsProducts.getString("product"));
				jrProducts.add(rows);
			}
			
			
			
			
			
			obj.put("success", "true");
			obj.put("products", jrProducts);

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
