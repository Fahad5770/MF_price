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

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Executes GRN", urlPatterns = { "/inventory/GRNExecute" })
public class GRNExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GRNExecute() {
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
		
		long Barcode = Utilities.parseLong(request.getParameter("GRNBarcode"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		StockDocument GRNStockDocument = new StockDocument();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			
			ResultSet rs = s.executeQuery("select distributor_id, created_on, created_by, delivery_id from inventory_delivery_note where barcode="+Barcode);
			if( rs.first() ){
				
				GRNStockDocument.DISTRIBUTOR_ID = rs.getLong("distributor_id");
				GRNStockDocument.DOCUMENT_TYPE_ID = 11;
				GRNStockDocument.DOCUMENT_ID = rs.getLong("delivery_id");
				GRNStockDocument.CREATED_ON = rs.getTimestamp("created_on");
				GRNStockDocument.CREATED_BY = rs.getLong("created_by");
				
				
				ResultSet rs2 = s2.executeQuery("select idnp.product_id, idnp.raw_cases, idnp.units, idnp.total_units, idnp.liquid_in_ml from inventory_delivery_note_products idnp, inventory_products_view ipv where idnp.product_id = ipv.product_id and idnp.delivery_id = "+rs.getLong("delivery_id")+" and ipv.category_id = 1");
				while( rs2.next() ){
					
					StockDocumentItems GRNStockDocumentItems = new StockDocumentItems();
					
					GRNStockDocumentItems.PRODUCT_ID = rs2.getLong("product_id");
					GRNStockDocumentItems.RAW_CASES = rs2.getInt("raw_cases");
					GRNStockDocumentItems.UNITS = rs2.getInt("units");
					GRNStockDocumentItems.TOTAL_UNITS = rs2.getInt("total_units");
					GRNStockDocumentItems.LIQUID_IN_ML = rs2.getLong("liquid_in_ml");
					GRNStockDocumentItems.TRANSACTION_TYPE = 2;
					
					GRNStockDocument.PRODUCTS.add(GRNStockDocumentItems);
				}
				
			}
			
			StockPosting GRNStockPosting = new StockPosting();
			
			boolean isPosted = GRNStockPosting.postStock(GRNStockDocument);
			
			GRNStockPosting.close();
			if(isPosted)
			{
				obj.put("success", "true");
				s.executeUpdate("update inventory_delivery_note set is_received=1, received_on=now(), received_by="+UserID+" where barcode="+Barcode);
			}
			else
			{
				obj.put("success", "false");
				obj.put("error", "Document could not be posted in Inventory");
			}
			
			
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (ds != null){
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
