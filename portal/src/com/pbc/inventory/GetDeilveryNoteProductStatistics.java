package com.pbc.inventory;

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


@WebServlet(description = "Delivery Note Product Statistics", urlPatterns = { "/inventory/GetDeilveryNoteProductStatistics" })
public class GetDeilveryNoteProductStatistics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDeilveryNoteProductStatistics() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/html");
		PrintWriter out = response.getWriter();
		
		
		int ProductCode[] = Utilities.parseInt(request.getParameterValues("ProductCode"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormUnits"));
		
		Datasource ds = new Datasource();
		String Output = "";
		Output += "<table data-role='table' data-mode='reflow' class='ui-body-d table-stripe ui-responsive' style='font-size: 10pt; width:100%'>";
		Output += "<thead>";
		Output += "<tr class='ui-bar-c'>";
		Output += "<th data-priority='1' style='width:10%'>Package</th>";
		Output += "<th data-priority='1' style='width:10%'>Brand</th>";
		Output += "<th data-priority='1' style='width:10%; text-align:right'>Quantity</th>";
    	Output += "</thead>";
    	Output += "</tr>";
    	
    	if(ProductCode!=null){
    		
			try{
				
				ds.createConnection();
				Statement s = ds.createStatement();
				
				s.executeUpdate("create temporary table temp_delivery_note_products (product_sap_code int(11), raw_cases int(11), units int(11))");
				
				for(int i = 0; i < ProductCode.length; i++){
					s.executeUpdate("insert into temp_delivery_note_products (product_sap_code, raw_cases, units) values ( "+ProductCode[i]+", "+RawCases[i]+", "+Units[i]+" ) ");
				}
				
				ResultSet rs = s.executeQuery("select product_sap_code, sum(raw_cases) as raw_cases, sum(units) as units, (select package_id from inventory_products where sap_code=product_sap_code) package_id, (select brand_id from inventory_products where sap_code=product_sap_code) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label from temp_delivery_note_products group by product_sap_code");
				
				while(rs.next()){
					Output += "<tr>";
					Output += "<td>"+rs.getString("package_label")+"</td>";
					Output += "<td>"+rs.getString("brand_label")+"</td>";
					
					String UnitsFormatter = "";
					if(rs.getInt("units") > 0){
						UnitsFormatter = "/"+rs.getInt("units");
					}
					
					Output += "<td style='text-align:right'>"+rs.getString("raw_cases")+UnitsFormatter+"</td>";
					Output += "</tr>";
				}
				
				
				
				
				//s.executeUpdate("drop table temp_sales_order_status ");
				
				s.close();
				ds.dropConnection();
				
			}catch(Exception e){
				System.out.println(e);
			}
		
    	}else{
    		
    		Output += "<tr>";
			Output += "<td colspan='4'>No products added.</td>";
			Output += "</tr>";
    	}
    	
		Output += "</table>";
		
		out.print(Output);
		out.close();
		
	}
	
}
