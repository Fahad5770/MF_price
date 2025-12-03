package com.mf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.controller.attendance.Attendance;
import com.mf.controller.authentication.Authentication;
import com.mf.controller.noOrder.NoOrder;
import com.mf.controller.order.Order;
import com.mf.interfaces.IMobileOrderBooker;
import com.mf.modals.ResponseModal;
import com.mf.controller.outletregistration.OutletRegistration;
import com.mf.controller.reports.MobileReports;

@SuppressWarnings("serial")
public class MobileOrderBookerController extends Controller implements IMobileOrderBooker {

	public MobileOrderBookerController() {
		super();
	}

	@Override
	public ResponseModal login(JSONObject Payload, HttpServletRequest request) {
		return new Authentication().order_booker_login(Payload, request);
	}

	@Override
	public ResponseModal order(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		// TODO Auto-generated method stub
		return new Order().InsertOrder(Payload, request);
	}

	@Override
	public ResponseModal no_order(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		// TODO Auto-generated method stub
		return new NoOrder().InsertNoOrder(Payload, request);
	}

	@Override
	public ResponseModal outlet_registration(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		// TODO Auto-generated method stub
		return new OutletRegistration().OutletRegister(Payload, request);
	}
	@Override
	public ResponseModal check_in(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		// TODO Auto-generated method stub
		return new Attendance().checkIn(Payload, request,0);
	}

	@Override
	public ResponseModal chech_out(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		// TODO Auto-generated method stub
		return new Attendance().checkOut(Payload, request,0);
	}

}
