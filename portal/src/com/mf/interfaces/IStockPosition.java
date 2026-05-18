package com.mf.interfaces;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IStockPosition {

	public ResponseModal GetStockPosition(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

}
