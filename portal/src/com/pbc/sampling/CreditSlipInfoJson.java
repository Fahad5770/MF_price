package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Credit Slip Info Json", urlPatterns = { "/sampling/CreditSlipInfoJson" })
public class CreditSlipInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreditSlipInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String EditID = Utilities.filterString(request.getParameter("EditID"), 0, 20);
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			
			ResultSet rs = s.executeQuery("SELECT * FROM sampling_credit_slip where id="+EditID);
			if (rs.first()){
				
				obj.put("success", "true");
				
				if( !DateUtils.isSameDay(new java.util.Date(), rs.getDate("created_on")) ){
					obj.put("isTodaysVoucher", "false");
				}else{
					obj.put("isTodaysVoucher", "true");
				}
				
				
				
				obj.put("Month", rs.getString("month"));
				obj.put("Year", rs.getString("year"));
				obj.put("Type", rs.getString("type_id"));
				obj.put("SlipDescription", rs.getString("slip_description"));
				obj.put("Description", rs.getString("internal_description"));
				
				
				ResultSet rs2 = s2.executeQuery("select *, (select name from common_outlets where id=sampling_credit_slip_outlets.outlet_id ) outlet_name from sampling_credit_slip_outlets where id="+EditID);
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("OutletID", rs2.getString("outlet_id"));
					rows.put("OutletName", rs2.getString("outlet_name"));
					rows.put("Amount", rs2.getString("amount"));
					
					jr.add(rows);
				}
				
				
				obj.put("rows", jr);
				
			}else{

				obj.put("success", "false");

			}
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
