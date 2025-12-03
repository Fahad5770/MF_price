package com.pbc.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "R358 -  Outlet Brand-SKU Wise Sales", urlPatterns = { "/reports/R358ExcelMain" })
public class R358ExcelMain extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public R358ExcelMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		HttpSession session = request.getSession();

		String UserID = null;

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		// System.out.println("hellooo ");
		PrintWriter out = response.getWriter();

		JSONObject json = new JSONObject();

		Datasource ds = new Datasource();
		try {

			ds.createConnection();
			Statement s = ds.createStatement();

			String startDate = Utilities.filterString(request.getParameter("StartDate"), 1, 100);
			Date StartDate = Utilities.parseDateYYYYMMDDWithBackSlash(startDate);

			String endDate = Utilities.filterString(request.getParameter("EndDate"), 1, 100);
			Date EndDate = Utilities.parseDateYYYYMMDDWithBackSlash(endDate);

			String Distributors = Utilities.filterString(request.getParameter("Distributors"), 1, 100);

			String OrderBookers = Utilities.filterString(request.getParameter("OrderBookers"), 1, 100);

			String TSOs = Utilities.filterString(request.getParameter("TSOs"), 1, 100);

			String ASMs = Utilities.filterString(request.getParameter("ASMs"), 1, 100);

			String Regions = Utilities.filterString(request.getParameter("Regions"), 1, 100);

			String Channels = Utilities.filterString(request.getParameter("Channels"), 1, 100);

			String Brands = Utilities.filterString(request.getParameter("Brands"), 1, 100);

			String SKUs = Utilities.filterString(request.getParameter("SKUs"), 1, 100);

			String City = Utilities.filterString(request.getParameter("City"), 1, 100);

//			System.out.println("StartDate : " + StartDate);
//			System.out.println("EndDate : " + EndDate);
//			System.out.println("Distributors : " + Distributors);
//			System.out.println("orderBookers : " + OrderBookers);
//			System.out.println("TSOs : " + TSOs);
//			System.out.println("ASMs : " + ASMs);
//			System.out.println("Regions : " + Regions);
//			System.out.println("channels : " + Channels);
//			System.out.println("Brands : " + Brands);
//			System.out.println("SKUs : " + SKUs);
//			System.out.println("City : " + City);

			// if(!DistributorIDs.equals("")) {

			String filename_Sales = " Outlet Brand-SKU Wise Sales_"
					+ Utilities.getSQLDateWithoutSeprator(new java.util.Date()) + ".xlsx";

			new com.pbc.reports.R358Excel().createPdf(Utilities.getCommonFilePath() + "/" + filename_Sales, StartDate,
					EndDate, Distributors, OrderBookers, TSOs, ASMs, Regions, Channels, Brands, SKUs, City);

			json.put("success", "true");
			json.put("FileName", filename_Sales);
			/*
			 * }else { json.put("success", "false"); json.put("error",
			 * "Please select at least one distributor"); }
			 */

			s.close();
		} catch (Exception e) {
			System.out.println(e);
			json.put("success", "false");
			json.put("error", e.toString());
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		out.print(json);

	}

}
