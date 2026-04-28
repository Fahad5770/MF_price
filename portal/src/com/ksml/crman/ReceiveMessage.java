package com.ksml.crman;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pbc.mobile.MobileRequest;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class ReceiveMessage
 */
@WebServlet("/ksml/CRManReceiveMessage")
public class ReceiveMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceiveMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		System.out.println(mr.URL);
		
		if (!mr.isExpired()){
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnectionKSML();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				String message = Utilities.truncateStringToMax(Utilities.filterString(mr.getParameter("message"), 1, 100),100);
				String from = Utilities.filterString(mr.getParameter("from"), 1, 100);
				Date messagetimestamp = new Date(Utilities.parseLong(Utilities.filterString(mr.getParameter("messagetime"), 1, 100)));
				
				String msg[] = message.split("\\.");
				
				if (isValid(msg)){
					
					int MillID = Utilities.parseInt(msg[0]);
					int CenterID = Utilities.parseInt(msg[1]);
					String CenterType = Utilities.filterString(msg[2], 1, 50);
					long rate = Math.round(Utilities.parseDouble(msg[3]));
					String comments = Utilities.filterString(msg[4], 1, 50);
					try{
						s.executeUpdate("insert into crman_valid_messages (from_number, received_on, mill_id, center_id, center_type, rate, comments, created_on) values ('"+from+"',"+Utilities.getSQLDateTime(messagetimestamp)+","+MillID+","+CenterID+",'"+CenterType+"',"+rate+",'"+comments+"', now())");
					}catch(SQLException e){
						s.executeUpdate("insert into crman_invalid_messages (from_number, message, received_on, created_on) values ('"+from+"','"+message+"',"+Utilities.getSQLDateTime(messagetimestamp)+", now())");
					}
					
				}else{
					
					s.executeUpdate("insert into crman_invalid_messages (from_number, message, received_on, created_on) values ('"+from+"','"+message+"',"+Utilities.getSQLDateTime(messagetimestamp)+", now())");
					
				}
				
				
				// MillID<int> CenterID<int> CenterType<text> Rate<int> Comments<text> 
				
				
				
				ds.commit();
				
				out.print(" success ");
				
			}catch(Exception e){
				try {
					ds.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				
				out.print(" error ");
				
				response.sendError(500);
				
				
			}finally{
				
				out.close();
				
				try {
					
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	public boolean isValid(String msg[]){
		boolean isValid = false;
		if (msg.length >= 5){
				if (Utilities.parseInt(msg[0]) > 0 && Utilities.parseInt(msg[1]) > 0 && Utilities.parseInt(msg[3]) > 0){
					isValid = true;
				}
		}
		return isValid;
	}
	
}
