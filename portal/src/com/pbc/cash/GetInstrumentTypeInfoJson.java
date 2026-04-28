package com.pbc.cash;

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


@WebServlet(description = "Get Instrument Type Info Json", urlPatterns = { "/cash/GetInstrumentTypeInfoJson" })
public class GetInstrumentTypeInfoJson extends HttpServlet { 
	private static final long serialVersionUID = 1L;
       
    public GetInstrumentTypeInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String InstrumentID = Utilities.filterString(request.getParameter("InstrumentID"), 0, 20);
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
						
			ResultSet rs = s.executeQuery("SELECT * FROM gl_cash_instruments where id="+InstrumentID);
			if (rs.first()){
				
				obj.put("success", "true");
				
				obj.put("InstrumentID", rs.getString("id"));
				obj.put("InstrumentLabel", rs.getString("label"));
				obj.put("InstrumentCategoryID", rs.getString("category_id"));
				obj.put("CaptureInstrumentNo", rs.getString("capture_reference_no"));
				obj.put("CaptureDate", rs.getString("capture_date"));
								
			}else{

				obj.put("success", "false");

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
