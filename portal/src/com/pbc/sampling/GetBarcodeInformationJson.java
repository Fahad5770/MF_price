package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Barcode Information in JSON", urlPatterns = { "/sampling/GetBarcodeInformationJson" })

public class GetBarcodeInformationJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetBarcodeInformationJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String barcode = request.getParameter("SamplingReceivingBarcode");
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			ResultSet rs;
			if(request.getParameter("SlipCancellationIsActive") != null) //mean slipcancellation case
			{
				 rs = s.executeQuery("SELECT sma.outlet_id, om.outlet_name, om.customer_id, om.customer_name, sma.net_payable, om.bsi_name, sma.approval_id, date_format(sma.month,'%M, %Y') month, sma.is_dispatched from sampling_monthly_approval sma, outletmaster om where sma.outlet_id = om.outlet_id and sma.barcode = '" + barcode + "' and sma.is_received = 0 and sma.is_cancelled=0");
			}
			else
			{
				 rs = s.executeQuery("SELECT sma.outlet_id, om.outlet_name, om.customer_id, om.customer_name, sma.net_payable, om.bsi_name, sma.approval_id, date_format(sma.month,'%M, %Y') month, sma.is_dispatched from sampling_monthly_approval sma, outletmaster om where sma.outlet_id = om.outlet_id and sma.barcode = '" + barcode + "' and sma.is_received = 0 and sma.is_cancelled=0");
			}
			
			if (rs.first()){
				
				obj.put("exists", "true");
				
				obj.put("ApprovalID", rs.getString("approval_id"));
				obj.put("OutletID", rs.getString(1));
				obj.put("OutletName", rs.getString(2) + " " + rs.getString("bsi_name"));
				obj.put("CustomerID", rs.getString(3));
				obj.put("CustomerName", rs.getString(4));
				obj.put("month", rs.getString("month"));
				obj.put("FormattedAmount", Utilities.getDisplayCurrencyFormat(rs.getDouble(5)));
				obj.put("Amount", rs.getDouble(5));
				obj.put("IsDispatched", rs.getInt("is_dispatched"));
				
			}else{

				obj.put("exists", "false");

			}
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
