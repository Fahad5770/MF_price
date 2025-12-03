package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Price List ", urlPatterns = { "/inventory/BasePriceListExecute" })
public class BasePriceListExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BasePriceListExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		PrintWriter out = response.getWriter();

		try {
			HttpSession session = request.getSession();

			String UserID = null;

			if (session.getAttribute("UserID") != null) {
				UserID = (String) session.getAttribute("UserID");
			}

			if (UserID == null) {
				response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
			}

			String ValidFrom = Utilities.filterString(request.getParameter("BasePriceListValidFrom"), 1, 12);

			long[] ProductID = Utilities.parseLong(request.getParameterValues("PriceListProductCode"));
			double[] RawCases = Utilities.parseDouble(request.getParameterValues("PriceListRawCase"));

			String ValidTo = "31/12/2999";

			Date ValidToDate = Utilities.parseDate(ValidTo);
			Date ValidFromDate = Utilities.parseDatewithdash(ValidFrom);
			Date ExpDate = Utilities.getDateByDays(ValidFromDate, -1);

			long PacakgeID = Utilities.parseLong(request.getParameter("BrandExchangePackage"));
			long[] BrandIds = Utilities.parseLong(request.getParameterValues("PromotionBrandList"));

			try {
				// ds.startTransaction();
				ds.createConnection();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			for (int i = 0; i < RawCases.length; i++) {
				if (RawCases[i] != 0) {

					ResultSet rs = s
							.executeQuery("SELECT * FROM pep.inventory_products_view where product_id=" + ProductID[i]);

					if (!rs.first()) {
						obj.put("success", "false");
						obj.put("error", "No Related Product is in Mysql View");
					} else {
						ResultSet rs3 = s3
								.executeQuery("SELECT * FROM pep.inventory_price_list_products_base where product_id="
										+ rs.getInt("product_id"));

						if (!rs3.first()) {

							double unit = RawCases[i] / rs.getDouble(12);
							s2.executeUpdate(
									"insert into pep.inventory_price_list_products_base (product_id,raw_case,unit,created_on,created_by,start_date,end_date) values('"
											+ rs.getInt("product_id") + "','" + RawCases[i] + "','" + unit + "',now(),"
											+ UserID + "," + Utilities.getSQLDate(ValidFromDate) + ","
											+ Utilities.getSQLDate(ValidToDate) + ")");
							// ds.commit();
							obj.put("success", "true");
						} else {

							if (s1.executeUpdate("update pep.inventory_price_list_products_base set end_date="
									+ Utilities.getSQLDate(ExpDate)
									+ " where id=(select max(id) from (SELECT * FROM pep.inventory_price_list_products_base where product_id="
									+ ProductID[i] + ") as data ) and product_id=" + ProductID[i]) != 0) {

								double unit = RawCases[i] / rs.getDouble(12);

								s2.executeUpdate(
										"insert into pep.inventory_price_list_products_base (product_id,raw_case,unit,created_on,created_by,start_date,end_date) values('"
												+ rs.getInt("product_id") + "','" + RawCases[i] + "','" + unit
												+ "',now()," + UserID + "," + Utilities.getSQLDate(ValidFromDate) + ","
												+ Utilities.getSQLDate(ValidToDate) + ")");
								// ds.commit();
								obj.put("success", "true");
							}
						}
					}

				} else {
					//System.out.println("Raw cases are zero for product id : " + ProductID[i]);

				}
			}

		} catch (SQLException e) {
			obj.put("success", "false");
			obj.put("error", "Exception");
			e.printStackTrace();

		}

		out.print(obj);
		out.close();
	}

}
