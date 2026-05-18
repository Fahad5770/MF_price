package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Credit Slip Receiving Execute", urlPatterns = { "/sampling/CreditSlipReceivingExecute" })
public class CreditSlipReceivingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreditSlipReceivingExecute() {
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
		
		long Barcode = Utilities.parseLong(request.getParameter("Barcode"));
		int DocumentType = Utilities.parseInt(request.getParameter("DocumentType"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			String SQL = "update sampling_credit_slip_outlets set is_received=1, received_on=now(), received_by="+UserID+" where barcode="+Barcode;
			if(DocumentType == 2){
				SQL = "update sampling_credit_slip_distributor set is_received=1, received_on=now(), received_by="+UserID+" where uvid="+Barcode;
			}
			s.executeUpdate(SQL);
			
			obj.put("success", "true");
			
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
