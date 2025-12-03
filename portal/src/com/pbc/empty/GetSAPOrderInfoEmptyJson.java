package com.pbc.empty;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

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


@WebServlet(description = "Get Empty Brand List in JSON", urlPatterns = { "/empty/GetSAPOrderInfoEmptyJson" })
public class GetSAPOrderInfoEmptyJson extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public GetSAPOrderInfoEmptyJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		
		//System.out.println(PackageID);
		Datasource ds = new Datasource();
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		JSONArray jr = new JSONArray();
	
		
		PrintWriter out = response.getWriter();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			
			
			
			
			Statement s2 = ds.createStatement();
			
			Datasource dsSAP = new Datasource();
		    dsSAP.createConnectionToSAPDB();
		    Statement sSAP = dsSAP.createStatement();


		    String OrderNumber=Utilities.filterString(request.getParameter("OrderNumber"), 2, 200);
		    long DistributorID=0;
		    String EntryDate ="";
		    String OrderDate = "";
		    int RawCases =0;
		    int Units =0;
		    long TotalUnits =0;
		    int UnitPerSKU=0;
		    long SAPCode =0;
		    String DistributorName="";
		    String PackageID="";
		    String BrandID="";
		    String PackageName="";
		    String BrandName="";
		    int LiquidInML=0;
		    int ProductID=0;
		    
		  //  System.out.println("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join "+dsSAP.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.vbeln = "+OrderNumber);
		    
			ResultSet rs5 = sSAP.executeQuery("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.vbeln = "+OrderNumber);
			while(rs5.next()){
				
				 //System.out.println("asdfasdf");
				
				LinkedHashMap rows = new LinkedHashMap();
				
				OrderNumber = rs5.getString("order_number");
				DistributorID = rs5.getLong("distributor_id");
				EntryDate = rs5.getString("entry_date");
				OrderDate = rs5.getString("order_date");
				//Quantity = rs5.getString("KWMENG");
				SAPCode = rs5.getLong("matnr");
				 
				
				
				
				ResultSet rs = s.executeQuery("select name from common_distributors where distributor_id="+DistributorID);
				while(rs.next()){
					DistributorName = rs.getString("name");
				}
				
				ResultSet rs2 = s.executeQuery("select * from ec_empty_sale_mapping where sap_code="+SAPCode);
				if(rs2.first()){
					ProductID = rs2.getInt("product_id");
				}
				
				//System.out.println("SELECT package_id,(select label from inventory_packages where id=package_id) package_name,brand_id,(select label from inventory_brands where id=brand_id) brand_name,unit_per_sku,liquid_in_ml,product_id FROM inventory_products_view where sap_code="+SAPCode);
				
				ResultSet rs1 = s.executeQuery("SELECT package_id,(select label from inventory_packages where id=package_id) package_name,brand_id,(select label from inventory_brands where id=brand_id) brand_name,unit_per_sku,liquid_in_ml,product_id FROM inventory_products_view where product_id="+ProductID);
				while(rs1.next()){
					PackageID = rs1.getString("package_id");
					PackageName = rs1.getString("package_name");
					BrandID = rs1.getString("brand_id");
					BrandName = rs1.getString("brand_name");
					UnitPerSKU = rs1.getInt("unit_per_sku");
					LiquidInML = rs1.getInt("liquid_in_ml");
					//ProductID = rs1.getInt("product_id");
				}
				
				
				
				
				if(rs5.getString("vrkme").equals("KI") ){
					//rawcases
					Units=0;
	    			RawCases = Utilities.parseInt(rs5.getString("KWMENG"));
	    			TotalUnits = UnitPerSKU*(Utilities.parseInt(rs5.getString("KWMENG")));
				
				}else{
					RawCases=0;
					Units = Utilities.parseInt(rs5.getString("KWMENG"));
					TotalUnits = Units;
				}
				
				
				
				
				rows.put("OrderNumber", OrderNumber);
				rows.put("DistributorID", DistributorID);
				rows.put("EntryDate", EntryDate);
				rows.put("OrderDate", OrderDate);
				
				rows.put("SAPCode", SAPCode);
				rows.put("DistributorName", DistributorName);
				rows.put("PackageID", PackageID);
				rows.put("PackageName", PackageName);
				rows.put("BrandID", BrandID);
				rows.put("BrandName", BrandName);
				
				rows.put("RawCases", RawCases);
				rows.put("Units", Units);
				rows.put("UnitPerSKU", UnitPerSKU);
				rows.put("LiquidInML", LiquidInML);
				rows.put("ProductID", ProductID);
				rows.put("TotalUnits", TotalUnits);
				
				
				
				
				jr.add(rows); 	 
			}
			
			
			
			
			
			
			obj.put("success","true");
			obj.put("rows",jr);
			
			sSAP.close();
			s2.close();
				
			
			
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
		}		
		out.print(obj);
		out.close();
	}
	
	
	
}
