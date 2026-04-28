package com.mf.interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IMobileOrderBooker {

	public ResponseModal login(JSONObject Payload, HttpServletRequest request);

	ResponseModal order(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

	ResponseModal no_order(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

	ResponseModal outlet_registration(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

	ResponseModal check_in(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

	ResponseModal chech_out(JSONObject Payload, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;



}
	