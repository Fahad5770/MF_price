package com.pbc.download;

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

import com.pbc.util.AlmoizParseUtils;
import com.pbc.util.AlmoizPathUtils;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class MobileFileDownloadMDE
 */


@WebServlet("/download/MobileFileDownloadMerchendising")
public class MobileFileDownloadMerchendising extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileFileDownloadMerchendising() {
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
			int year = AlmoizParseUtils.parseInt(request.getParameter("year"));
			int month = AlmoizParseUtils.parseInt(request.getParameter("month"));
			long OutletID = AlmoizParseUtils.parseLong(request.getParameter("outletId"));
			
			System.out.println(AlmoizPathUtils.getMerchandiserImagePath(year, month, OutletID)+"/"+ filename);
			String fileuri = AlmoizPathUtils.getMerchandiserImagePath(year, month, OutletID)+"/"+ filename;
			//System.out.println("SELECT * FROM pep.mobile_common_outlets_files where uri like '%OrderImages%'and filename='"+filename+"'");
			
			
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
