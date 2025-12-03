package com.mf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.controller.attendance.Attendance;
import com.mf.controller.authentication.Authentication;
import com.mf.controller.visitOutlet.VisitOutletController;
import com.mf.interfaces.IMobileMarketVisit;
import com.mf.modals.ResponseModal;

@SuppressWarnings("serial")
public class MobileMarketVisitController extends Controller implements IMobileMarketVisit {

	public MobileMarketVisitController() {
		super();
	}

	@Override
	public ResponseModal login(JSONObject Payload, HttpServletRequest request) {
		System.out.print("inside MobileMarketVisitController");
		return new Authentication().market_visit_login(Payload, request);
	}

	@Override
	public ResponseModal check_in(JSONObject payload, HttpServletRequest request, int is_mv) {
		// TODO Auto-generated method stub
		ResponseModal responseModal = new ResponseModal();
		try {
			responseModal = new Attendance().checkIn(payload, request, is_mv);
		} catch (ClassNotFoundException | IllegalAccessException | IllegalStateException | InstantiationException
				| IOException | ServletException | FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseModal.setErrorResponse("Error in checkOut" + e);
		}
		return responseModal;
	}

	@Override
	public ResponseModal check_out(JSONObject payload, HttpServletRequest request, int is_mv) {
		// TODO Auto-generated method stub
		ResponseModal responseModal = new ResponseModal();
		try {
			responseModal = new Attendance().checkOut(payload, request, is_mv);
		} catch (ClassNotFoundException | IllegalAccessException | IllegalStateException | InstantiationException
				| IOException | ServletException | FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseModal.setErrorResponse("Error in checkOut" + e);
		}
		return responseModal;
	}

	@Override
	public ResponseModal outlet_visit(JSONObject jsonBodyData, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return new VisitOutletController().mv_visit_outlet(jsonBodyData, request);
	}

}
