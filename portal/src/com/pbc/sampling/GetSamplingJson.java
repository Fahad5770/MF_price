package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Sampling Information in JSON", urlPatterns = { "/sampling/GetSamplingJson" })
public class GetSamplingJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetSamplingJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String RequestID = Utilities.filterString(request.getParameter("RequestID"), 0, 20);
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			
			ResultSet rs = s.executeQuery("SELECT s.request_id, s.sampling_id, s.outlet_id, s.outlet_name, s.business_type, s.address, s.region, s.asm, s.cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m where s.outlet_id = m.outlet_id and s.request_id ="+RequestID);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				obj.put("request_id", rs.getString(1));
				obj.put("sampling_id", rs.getString(2));
				obj.put("outlet_id", rs.getString(3));
				obj.put("outlet_name", rs.getString(4));
				obj.put("business_type", rs.getString(5));
				obj.put("address", rs.getString(6));
				obj.put("region", rs.getString(7));
				obj.put("asm", rs.getString(8));
				obj.put("cr", rs.getString(9));
				obj.put("market", rs.getString(10));
				obj.put("vehicle", rs.getString(11));
				obj.put("advance_company_share", rs.getDouble(12));
				obj.put("advance_agency_share", rs.getDouble(13));
				obj.put("advance_valid_from", Utilities.getDisplayDateFormat(rs.getDate(14)));
				obj.put("advance_valid_to", Utilities.getDisplayDateFormat(rs.getDate(15)));
				obj.put("fixed_company_share", rs.getDouble(16));
				obj.put("fixed_agency_share", rs.getDouble(17));
				obj.put("fixed_deduction_term", rs.getDouble(18));
				obj.put("fixed_valid_from", Utilities.getDisplayDateFormat(rs.getDate(19)));
				obj.put("fixed_valid_to", Utilities.getDisplayDateFormat(rs.getDate(20)));
				obj.put("fixed_company_share_offpeak", rs.getDouble("fixed_company_share_offpeak"));
				obj.put("fixed_agency_share_offpeak", rs.getDouble("fixed_agency_share_offpeak"));
				obj.put("fixed_deduction_term_offpeak", rs.getDouble("fixed_deduction_term_offpeak"));
				
				obj.put("latitude", rs.getString(21));
				obj.put("longitude", rs.getString(22));
				
				
				
				
				JSONArray jr = new JSONArray();
				
				Statement s2 = ds.createStatement();
				
				ResultSet rs2 = s2.executeQuery("select package, agency_share, company_share, deduction_term, valid_from, valid_to, brand_id, hand_to_hand from sampling_percase where sampling_id ="+rs.getString(2));
				while(rs2.next()){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("package", rs2.getString(1));
					rows.put("brand_id", rs2.getString(7));
					rows.put("agency_share", rs2.getDouble(2));
					rows.put("company_share", rs2.getDouble(3));
					rows.put("deduction_term", rs2.getDouble(4));
					rows.put("valid_from", Utilities.getDisplayDateFormat(rs2.getDate(5)));
					rows.put("valid_to", Utilities.getDisplayDateFormat(rs2.getDate(6)));
					rows.put("hand_to_hand", rs2.getString(8));
					
					jr.add(rows);
				}

				JSONArray jr2 = new JSONArray();
				
				Statement s3 = ds.createStatement();
				
				ResultSet rs3 = s3.executeQuery("select percentage, converted_sales, discount from sampling_fixed_threshold where sampling_id ="+rs.getString(2)+" order by percentage desc");
				while(rs3.next()){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("percentage", rs3.getString(1));
					rows.put("converted_sales", rs3.getString(2));
					rows.put("discount", rs3.getString(3));
					
					jr2.add(rows);
				}
				
				
				obj.put("rows",jr);
				obj.put("threshold_rows",jr2);
				
				rs2.close();
				s2.close();
			}else{

				obj.put("exists", "false");

			}
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			sendErrorRedirect(request,response,Utilities.getErrorPageURL(request),e);
		}		
		
	}
	
	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}
	
}
