package com.pbc.empty;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "GL Unpost Invoice Execute", urlPatterns = { "/empty/EmptyCreditReceiptUnpostExecute" })
public class EmptyCreditReceiptUnpostExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmptyCreditReceiptUnpostExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		long ReceiptID = Utilities.parseLong(request.getParameter("ReceiptID"));
		String Reason = Utilities.filterString(request.getParameter("Reason"), 2, 500);
		
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( ReceiptID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Receipt ID");
			isValidationClear = false;
		}// end validation
		
		if( isValidationClear ){
		
			Datasource ds = new Datasource();
			
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
					
				ResultSet rs = s.executeQuery("SELECT * FROM ec_empty_receipt where id = "+ReceiptID);
				if( rs.first() ){
					
					s2.executeUpdate("insert into ec_empty_receipt_unposted(id,created_on,created_by,vehicle_no,uvid,distributor_id,unposted_on,unposted_by,reason) values("+rs.getLong("id")+",'"+rs.getTimestamp("created_on")+"',"+rs.getLong("created_by")+",'"+rs.getString("vehicle_no")+"','"+rs.getString("uvid")+"',"+rs.getLong("distributor_id")+",now(),"+UserID+",'"+Reason+"')");
				
				}
				
				ResultSet rs1 = s.executeQuery("SELECT * FROM ec_empty_receipt_products where id = "+ReceiptID);
				while( rs1.next() ){
					
					s2.executeUpdate("insert into ec_empty_receipt_products_unposted(id,product_id,raw_cases,units,total_units,liquid_in_ml,type_id) values("+rs1.getLong("id")+","+rs1.getLong("product_id")+","+rs1.getLong("raw_cases")+","+rs1.getLong("units")+","+rs1.getLong("total_units")+","+rs1.getLong("liquid_in_ml")+","+rs1.getInt("type_id")+")");
				
				}	
					
					
					
					s.executeUpdate("delete from ec_transactions where empty_receipt_id="+ReceiptID);
					s.executeUpdate("delete from ec_empty_receipt_products where id="+ReceiptID);
					s.executeUpdate("delete from ec_empty_receipt where id="+ReceiptID);
					
					
					
					
					obj.put("success", "true");
					
				
				
				ds.commit();
				
				s3.close();
				s2.close();
				s.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				try {
					
					ds.rollback();
					
					obj.put("success", "false");
					obj.put("error", e.toString());
					e.printStackTrace();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
			}finally{
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		} // end if validation
		
		out.print(obj);
		out.close();
		
	}
	
}
