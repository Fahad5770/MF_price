package com.pbc.mobile;

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


@WebServlet("/mobile/MobileFileDownloadOutlet")
public class MobileFileDownloadOutlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileFileDownloadOutlet() {
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
			Datasource ds = new Datasource();
			
			try {
				
				ds.createConnection();
				
				Statement s = ds.createStatement();
				Statement s1 = ds.createStatement();
			
			String filename = request.getParameter("file");
			String fileuri = Utilities.getOutletImagesPath() +"/"+ filename;
			//System.out.println("SELECT * FROM pep.mobile_common_outlets_files where uri like '%OrderImages%'and filename='"+filename+"'");
			ResultSet rs = s.executeQuery("SELECT * FROM pep.mobile_common_outlets_files where uri like '%OrderImages%'and filename='"+filename+"'");
			if(rs.first()) {
				 fileuri = Utilities.getOrderImagesPath() +"/"+ filename;
			}
			
			// Find this file id in database to get file name, and file type
			// You must tell the browser the file type you are going to send
			// for example application/pdf, text/plain, text/html, image/jpg
			//response.setContentType(fileType);
			
			response.setContentType("application/octet-stream");
			
			// Make sure to show the download dialog
			response.setHeader("Content-disposition","attachment; filename="+filename);
			// Assume file name is retrieved from database
			// For example D:\\file\\test.pdf
			File my_file = new File(fileuri);
			// This should send the file to browser
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(my_file);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0){
			   out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
} catch (Exception e) {
				
			}  
		}
		
	}

}
