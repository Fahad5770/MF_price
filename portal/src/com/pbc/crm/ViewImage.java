package com.pbc.crm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class ViewImage
 */
@WebServlet("/crm/ViewImage")
public class ViewImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			String filename = Utilities.filterString(request.getParameter("filename"),1,100);
			String uri = "";
			
			ResultSet rs = s.executeQuery("select uri from crm_complaints_files where filename = '"+filename+"'");
			if (rs.first()){
				uri = rs.getString(1);
			}
			
			File f = new File(uri);
			 
			FileInputStream fin = new FileInputStream(f);
			ServletOutputStream outStream = response.getOutputStream();
			response.setContentType("image/jpeg");
			int i = 0;
			while (i != -1) {
			i = fin.read();
			outStream.write(i);
			}
			fin.close();
			
			s.close();
			ds.dropConnection();
			
		}catch(Exception e){
			System.out.println("crm/ViewImage:"+e);
		}		

	}

}
