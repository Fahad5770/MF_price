package com.mf.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IMobileReports {


	ResponseModal MVStrikeRateReport(JSONObject jsonData, HttpServletRequest request);

	ResponseModal MVNoOrderReport(JSONObject jsonData, HttpServletRequest request);


	ResponseModal OrdersReport(JSONObject jsonData, HttpServletRequest request);

	ResponseModal SalesReport(JSONObject jsonData, HttpServletRequest request);	

	
}
