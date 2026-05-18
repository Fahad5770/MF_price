package com.pbc.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Get Outlets by Search in JSON", urlPatterns = { "/common/GetOutletsByID" })
public class GetOutletsByID extends HttpServlet {
	private static final long serialVersionUID = 1654111L;

	public GetOutletsByID() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long SAPCode = Utilities.parseLong(request.getParameter("SAPCode"));

		Datasource ds = new Datasource();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();

			response.setContentType("application/json");
			JSONObject obj = new JSONObject();

			Statement s2 = ds.createStatement();
			Date today = new Date();
			int month = Utilities.getMonthNumberByDate(today);
			System.out.println("SELECT shelf_rent as shelf_rent FROM pep.rental_discount where outlet_id="
					+ SAPCode + " and approved_1=1 and approved_2=1 and is_released=0 and month=" + month);
			ResultSet rs2 = s2
					.executeQuery("SELECT shelf_rent as shelf_rent FROM pep.rental_discount where outlet_id="
							+ SAPCode + " and approved_1=1 and approved_2=1 and is_released=0 and month=" + month);
			int shelf_rent = (rs2.first()) ? rs2.getInt("shelf_rent") : 0;

			obj.put("ShelfRent", shelf_rent);
			obj.put("exists", "true");

			rs2.close();
			s2.close();

			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();

			s.close();
			ds.dropConnection();

		} catch (Exception e) {
			sendErrorRedirect(request, response, Utilities.getErrorPageURL(request), e);
		}

	}

	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL,
			Throwable e) throws ServletException, IOException {
		request.setAttribute("jakarta.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}

}
