package com.mf.interfaces;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IVisitOutletController {
	public ResponseModal mv_visit_outlet(JSONObject Payload, HttpServletRequest request);

}
