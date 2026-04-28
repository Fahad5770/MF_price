package com.mf.mobile;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mf.controller.MobileMarketVisitController;
import com.mf.controller.refresh.outlets.RefreshOutlet;
import com.mf.controller.reports.MobileReports;
import com.mf.modals.ResponseModal;
import com.mf.utils.MFAPIFunctions;

/**
 * Servlet implementation class OrderBookerMobile
 */
@WebServlet("/MFMarketVisitMobile")
public class MFMarketVisitMobile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public MFMarketVisitMobile() {
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
		JSONParser parser = new JSONParser();
		ResponseModal responseModal = new ResponseModal();

		try {

			final JSONObject ResponseBody = (JSONObject) parser.parse(MFAPIFunctions.getResponseString(request));
			final JSONObject Payload = (JSONObject) ResponseBody.get("data");
			String endPoint = (String) ResponseBody.get("end_point");

			// Check Token
			responseModal = MFAPIFunctions.validateToken(request, endPoint);
			if (!responseModal.isStatus()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				out.print(responseModal.getResponseJson());
				out.close();
				return;
			}

			MobileMarketVisitController mobileMarketVisitController = new MobileMarketVisitController();

			switch (endPoint) {
			case "login":
				System.out.println("Login Hit Successfully 2...");
				responseModal = mobileMarketVisitController.login(Payload, request);
				break;
			case "checkIn":
				System.out.println("Check In Hit Successfully...");
				responseModal = mobileMarketVisitController.check_in(Payload, request, 1);
				break;
			case "checkOut":
				System.out.println("Check Out Hit Successfully...");
				responseModal = mobileMarketVisitController.check_out(Payload, request, 1);
				break;
			case "outlet_visit":
				System.out.println("outlet visit Hit Successfully...");
				responseModal = mobileMarketVisitController.outlet_visit(Payload, request);
				break;
			case "refresh_outlets":
				System.out.println("Refresh Outlets Hit Successfully...");
				responseModal = new RefreshOutlet().refresh_outlets(Payload, request);
				break;
			case "mv_strike_rate_report":
				responseModal =new  MobileReports().MVStrikeRateReport(Payload, request);
				break;
			case "mv_no_order_report":
				responseModal =new  MobileReports().MVNoOrderReport(Payload, request);
				break;
			case "orders_report":
				responseModal =new MobileReports().OrdersReport(Payload, request);
				break;
			default:
				responseModal.setErrorResponse("Wrong end point");
			}

		} catch (IllegalStateException | ParseException e) {
			System.out.println("Server Error");
			System.out.println(e);
			responseModal.setErrorResponse("Server Error" + e);
		}

		out.print(responseModal.getResponseJson());
		out.close();
	}

}
