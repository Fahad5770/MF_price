package com.mf.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IAuthentication {

	ResponseModal market_visit_login(JSONObject jsonData, HttpServletRequest request);
	
	ResponseModal order_booker_login(JSONObject jsonData, HttpServletRequest request);

}