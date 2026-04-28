package com.pbc.inventory;

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

/**
 * Servlet implementation class UserAdditionalRightExecute
 */
@WebServlet(description = "SpotDiscount GetActive SpotDiscount InfoJson", urlPatterns = {
		"/inventory/BulkDiscountGetActiveSpotDiscountInfoJson2" })
public class BulkDiscountGetActiveSpotDiscountInfoJson2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BulkDiscountGetActiveSpotDiscountInfoJson2() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// System.out.println("Hellooooooo ");

		// warehouse updatation
		// first deleting all the records from warehouse against user
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		String query = "";
		String query1 = "";
		String query2 = "";

		long label = Utilities.parseLong(request.getParameter("label"));
		System.out.println("label  =" + label);
		System.out.println("BulkDiscountGetActiveSpotDiscountInfoJson2");

		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			ResultSet rs = null;
			if (label != 0) // for region
			{

			
				 rs = s.executeQuery("SELECT * FROM  pci_sub_channel where id="+label);
			
				/*
				 * rs = s.
				 * executeQuery("SELECT * FROM pep.inventory_hand_to_hand_discount ipr,inventory_hand_to_hand_discount ipl where ipr.spot_discount_id=ipl.id and ipr.region_id="
				 * +label);
				 */

			}

			if (rs.first()) {
				if (label != label) {
					obj.put("label_exist", "true");
					obj.put("success", "false");
					obj.put("spot_discount_name", rs.getString("label"));
				}

			}

			s.close();
			ds.dropConnection();
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		out.print(obj);
		out.close();
	}

}
