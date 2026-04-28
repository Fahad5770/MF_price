package com.mf.controller.visitOutlet;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.mf.interfaces.IVisitOutletController;
import com.mf.modals.ResponseModal;

public class VisitOutletController implements IVisitOutletController{

	@Override
	public ResponseModal mv_visit_outlet(JSONObject Payload, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return MVOutletVisit.mv_visit_outlet(Payload, request);
	}

}
