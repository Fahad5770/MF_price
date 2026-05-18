package com.pbc.sap;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Connect
 */
@WebServlet("/sap/Connect")
public class Connect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Connect() {
        super();
        
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			PrintWriter out = response.getWriter();
			
			SAPUtilities su = new SAPUtilities();
			try{
				out.print("Dropping existing connection...<br>");
				su.dropConnection();
			}catch(Exception e){
				out.print(e+"<br>");
				e.printStackTrace();
			}
			
			try{
				out.print("Establishing connection...<br>");
				su.connectPRD();
			}catch(Exception e){
				out.print(e+"<br>");
				e.printStackTrace();
			}
			
			out.close();
	}

}
