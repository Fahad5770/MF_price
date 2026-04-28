package com.pbc.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class MobileFileDownloadMDE
 */


@WebServlet("/common/CommonFileCSVDownload")
public class CommonFileCSVDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommonFileCSVDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
		if (SessionUserID == 0){
			response.sendRedirect("AccessDenied.jsp");
		}else{
			String filename = request.getParameter("file");
			String fileuri = Utilities.getCommonFilePath() +"/"+ filename;
			// Find this file id in database to get file name, and file type
			// You must tell the browser the file type you are going to send
			// for example application/pdf, text/plain, text/html, image/jpg
			//response.setContentType(fileType);
			
			response.reset();
			response.setBufferSize(4096);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");

			response.setContentType("text/csv; charset=UTF-8");

			
			// Make sure to show the download dialog
			response.setHeader("Content-disposition","attachment; filename="+filename);
			// Assume file name is retrieved from database
			// For example D:\\file\\test.pdf
			File my_file = new File(fileuri);
			// This should send the file to browser
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(my_file);
			byte[] buffer = new byte[16384];
			int length;
			while ((length = in.read(buffer)) > 0){
			   out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
	         
		}
		
	}

}
