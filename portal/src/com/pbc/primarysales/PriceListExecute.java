package com.pbc.primarysales;

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

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Executes Gatepass", urlPatterns = { "/primarysales/PriceListExecute" })
public class PriceListExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PriceListExecute() {
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
		
		long DistributorID [] = Utilities.parseLong(request.getParameterValues("DistributorID"));
		long SapCode [] = Utilities.parseLong(request.getParameterValues("ProductCode"));
		double Price [] = Utilities.parseDouble(request.getParameterValues("Price"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			for(int i=0;i<DistributorID.length;i++){
				int ProductID=0;
				if(DistributorID[i]!=0 && SapCode[i]!=0 && Price[i]!=0){
					
					ResultSet rs = s1.executeQuery("SELECT id FROM pep.inventory_products where sap_code="+SapCode[i]);
					if(rs.first()){
						ProductID = rs.getInt("id");
					}
					
					s.executeUpdate("delete from primary_sales_price_list where distributor_id="+DistributorID[i]+" and product_id="+ProductID);
					s.executeUpdate("insert into primary_sales_price_list(distributor_id,product_id,selling_price) values("+DistributorID[i]+","+ProductID+","+Price[i]+")");
				}
				
			}
			
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
