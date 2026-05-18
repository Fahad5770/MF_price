package com.mf.discounts;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price Discount ", urlPatterns = { "/discounts/PriceDiscountInfoJson" })
public class PriceDiscountInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PriceDiscountInfoJson() {
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
		
		
		//Master table
		int priceDiscountId = Utilities.parseInt(request.getParameter("PriceDiscountMasterTableID"));
/*		String ValidFrom = Utilities.filterString(request.getParameter("ProductPromotionsValidFrom"),1,12);
		String ValidTo = Utilities.filterString(request.getParameter("ProductPromotionsValidTo"),1,12);
		int Active = Utilities.parseInt(request.getParameter("ProductPromotionsIsActive"));*/
		
/*		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);*/

		//Detail table
/*		long[] SalesPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsMainFormPackage"));
		
		double[] SalesRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsMainFormRawCases"));
		double[] SalesUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsMainFormUnits"));
		
		long[] PromotionsPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsPMainFormPackage"));
		//long[] PromotionsBrandID = Utilities.parseLong(request.getParameterValues("ProductPromotionsPMainFormProductIDIssue"));
		double[] PromotionsRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormRawCases"));
		double[] PromotionsUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormUnits"));*/
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		JSONArray productsArray = new JSONArray();
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();

			
			String query1 = "Select * From inventory_price_discount where id = "+priceDiscountId;
			System.out.println(query1);
			ResultSet rs  = s.executeQuery(query1);
			if(rs.next()) {
				
				String FromDate =  Utilities.getDisplayDateFormat(rs.getDate("valid_from"));
				String ToDate =  Utilities.getDisplayDateFormat(rs.getDate("valid_to"));
				
				obj.put("product_discount_id",priceDiscountId);
				obj.put("discount_name", rs.getString("discount_name"));
				obj.put("valid_from", FromDate);
				obj.put("valid_to", ToDate);
				obj.put("is_active", rs.getString("is_active"));
				
				String query2 = "Select ipdp.discount_value,ipdp.is_with_tax, ipdp.product_id, ipv.sap_code From inventory_price_discount_products ipdp join inventory_products_view ipv on ipdp.product_id = ipv.product_id where ipdp.price_discount_id="+priceDiscountId;
				System.out.println(query2);
				ResultSet rs1 = s1.executeQuery(query2);
				while(rs1.next()) {					
				    JSONObject productObj = new JSONObject();
				    productObj.put("product_id", rs1.getInt("product_id"));
				    productObj.put("discount_value", rs1.getDouble("discount_value"));
				    productObj.put("is_with_tax", rs1.getShort("is_with_tax"));
				    productsArray.add(productObj);
				}
				
			}				
			System.out.println("HEreer");
				obj.put("products", productsArray);
				obj.put("success", "true");
				ds.commit();
				
				
				// Update promotions cache
				BiProcesses bip = new BiProcesses();
				bip.createPromotionsCache();
				bip.close();
				
				
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
