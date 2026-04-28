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
@WebServlet(description = "R364 - Orders Vs Execution Report", urlPatterns = { "/reports/R364ExcelMain" })
public class R364ExcelMain extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public R364ExcelMain() {
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

			String OrderBooker = Utilities.filterString(request.getParameter("OrderBooker"), 1, 100);

			String ASMs = Utilities.filterString(request.getParameter("ASM"), 1, 100);

			String TSOs = Utilities.filterString(request.getParameter("TSO"), 1, 100);

			String Regions = Utilities.filterString(request.getParameter("Region"), 1, 100);

			String Cities = Utilities.filterString(request.getParameter("City"), 1, 100);
			
			String Brands = Utilities.filterString(request.getParameter("Brands"), 1, 100);
			
			String SKUs = Utilities.filterString(request.getParameter("SKUs"), 1, 100);



//			System.out.println("Recieved Distributors : " + Distributors);
//			System.out.println("Recieved Order Bookers : " + OrderBooker);
//			System.out.println("Recieved ASMs : " + ASMs);
//			System.out.println("Recieved TSOs : " + TSOs);
//			System.out.println("Recieved Region : " + Regions);
//			System.out.println("Recieved City : " + Cities);

			String filename_Sales = "Orders_Vs_Execution_Report_"
					+ Utilities.getSQLDateWithoutSeprator(new java.util.Date()) + ".xlsx";

			new com.pbc.reports.R364Excel().createPdf(Utilities.getCommonFilePath() + "/" + filename_Sales, StartDate,
					EndDate, Distributors, OrderBooker, ASMs, TSOs, Regions, Cities,Brands,SKUs);

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
