package com.mf.interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface IAttendance {
	public abstract ResponseModal checkIn(JSONObject jsonObject, HttpServletRequest request, int is_mv_app)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

	public abstract ResponseModal checkOut(JSONObject jsonObject, HttpServletRequest request, int is_mv_app)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;
}

