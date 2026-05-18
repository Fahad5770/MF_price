package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Brand Exchange Info in JSON", urlPatterns = { "/inventory/GetBrandExchangeInfo" })
public class GetBrandExchangeInfo extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public GetBrandExchangeInfo() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		Integer PackageID = Utilities.parseInt(request.getParameter("PackageID"));
		Integer BrandIssue = Utilities.parseInt(request.getParameter("BrandIssue"));
		Integer BrandReceive = Utilities.parseInt(request.getParameter("BrandReceive"));		
		
		Datasource ds = new Datasource();
		
		try {
			
			JSONObject obj = new JSONObject();
			
			Product ProductIssue = new Product(1, PackageID, BrandIssue);
			Product ProductReceive = new Product(1, PackageID, BrandReceive);
			
			obj.put("exists", "true");
			
			obj.put("UnitPerSKUIssue", ProductIssue.UNIT_PER_SKU);
			obj.put("ProductCodeIssue", ProductIssue.PRODUCT_ID);
			
			
			obj.put("UnitPerSKUReceive", ProductReceive.UNIT_PER_SKU);
			obj.put("ProductCodeReceive", ProductReceive.PRODUCT_ID);
			
			obj.put("LiquidInML", ProductIssue.LIQUID_IN_ML);
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
		} catch (Exception e) {
			sendErrorRedirect(request,response,Utilities.getErrorPageURL(request),e);
		}		
		
	}
	
	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("jakarta.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}
	
}
