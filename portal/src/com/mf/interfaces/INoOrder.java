package com.mf.interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.json.simple.JSONObject;

import com.mf.modals.ResponseModal;

public interface INoOrder {
	public abstract ResponseModal InsertNoOrder(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException;

}
