package com.pbc.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "R373 - Newly Created Outlets without Approval Martix", urlPatterns = { "/reports/R373ExcelMain" })
public class R373ExcelMain extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public R373ExcelMain() {
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

			String Regions = Utilities.filterString(request.getParameter("Regions"), 1, 100);

			String Channels = Utilities.filterString(request.getParameter("Channels"), 1, 100);
			
			String CityIDs = Utilities.filterString(request.getParameter("CityIDs"), 1, 100);
						
			String OrderBookersIDs = Utilities.filterString(request.getParameter("OrderBookersIDs"), 1, 100);
			
			String ASMIDs = Utilities.filterString(request.getParameter("ASMIDs"), 1, 100);

			String TSOIDs = Utilities.filterString(request.getParameter("TSOIDs"), 1, 100);			

		
			String filename_Sales = "Post_Sales_Report"
					+ Utilities.getSQLDateWithoutSeprator(new java.util.Date()) + ".xlsx";

			new com.pbc.reports.R373Excel().createPdf(Utilities.getCommonFilePath() + "/" + filename_Sales, StartDate,
					EndDate, Distributors, Regions, Channels, CityIDs,OrderBookersIDs, ASMIDs, TSOIDs);

			json.put("success", "true");
			json.put("FileName", filename_Sales);

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
