package com.mf.mobile;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mf.controller.MobileOrderBookerController;
import com.mf.controller.refresh.outlets.RefreshOutlet;
import com.mf.controller.reports.MobileReports;
import com.mf.controller.stockPosition.StockPosition;
import com.mf.controller.updateLocation.OutletUpdateLocation;
import com.mf.modals.ResponseModal;
import com.mf.utils.MFAPIFunctions;

/**
 * Servlet implementation class OrderBookerMobile
 */
@WebServlet("/OrderBookerMobile")
public class MFOrderBookerMobile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public MFOrderBookerMobile() {
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
			MobileOrderBookerController mobileOrderBookerController = new MobileOrderBookerController();

			switch (endPoint) {
			case "login":
				responseModal = mobileOrderBookerController.login(Payload, request);
				break;
			case "order":
				System.out.println("oerder ....");
				responseModal = mobileOrderBookerController.order(Payload, request);
				break;
			case "no_order":
				responseModal = mobileOrderBookerController.no_order(Payload, request);
				break;
			case "outlet_registration":
				responseModal = mobileOrderBookerController.outlet_registration(Payload, request);
				break;
			case "check_in":
				responseModal = mobileOrderBookerController.check_in(Payload, request);
				break;
			case "check_out":
				responseModal = mobileOrderBookerController.chech_out(Payload, request);
				break;
			case "orders_report":
				responseModal = new MobileReports().OrdersReport(Payload, request);
				break;
			case "outlet_update_location":
				responseModal = new OutletUpdateLocation().UpdateOutletLocation(Payload, request);
				break;
			case "refresh_outlets":
				System.out.println("Refresh Outlets Hit Successfully...");
				responseModal = new RefreshOutlet().refresh_outlets_OB(Payload, request);
				break;
			case "stock_position":
				responseModal = new StockPosition().GetStockPosition(Payload, request);
				break;
			default:
				responseModal.setErrorResponse("Wrong end point");
			}
		} catch (ParseException | IllegalStateException | ClassNotFoundException | IllegalAccessException
				| InstantiationException | FileUploadException e) {
			System.out.println("Server Errorsss");
			System.out.println(e);
			responseModal.setErrorResponse("Server Error" + e);
		}

		out.print(responseModal.getResponseJson());
		out.close();
	}

}
