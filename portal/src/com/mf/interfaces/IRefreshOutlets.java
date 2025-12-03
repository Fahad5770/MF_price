package com.mf.interfaces;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;


public interface IRefreshOutlets {
	
	ResponseModal refresh_outlets(JSONObject jsonData, HttpServletRequest request);
	
	ResponseModal refresh_outlets_OB(JSONObject jsonData, HttpServletRequest request);


}
