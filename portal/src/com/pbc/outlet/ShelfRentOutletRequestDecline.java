package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(description = "Shelf Rent Decline Outlet", urlPatterns = { "/outlet/ShelfRentOutletRequestDecline" })
public class ShelfRentOutletRequestDecline extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShelfRentOutletRequestDecline() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		String userId = (String) session.getAttribute("UserID");

		if (userId == null) {
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
			return;
		}

		long outletId = Utilities.parseLong(request.getParameter("ID"));
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			ds.startTransaction();

			Statement stmt = ds.createStatement();
			
			String updateQuery = "UPDATE rental_discount SET is_declined = 1 WHERE outlet_id = " + outletId;
			System.out.println("UPDATE rental_discount SET is_declined = 1 WHERE outlet_id = " + outletId);
			stmt.executeUpdate(updateQuery);

			ds.commit();
			obj.put("success", "true");
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		out.print(obj);
		out.close();
	}
}
