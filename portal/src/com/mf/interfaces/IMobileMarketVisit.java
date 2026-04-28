package com.mf.interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IMobileMarketVisit {

	public ResponseModal login(JSONObject jsonBodyData, HttpServletRequest request);
	
	public ResponseModal check_in(JSONObject jsonBodyData, HttpServletRequest request, int is_mv) throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException, IOException, ServletException, FileUploadException;
	
	public ResponseModal check_out(JSONObject jsonBodyData, HttpServletRequest request, int is_mv);
	
	public ResponseModal outlet_visit(JSONObject jsonBodyData, HttpServletRequest request);

}
