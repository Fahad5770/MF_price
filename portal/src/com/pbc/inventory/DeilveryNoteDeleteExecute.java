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


@WebServlet(description = "Executes Delivery Note Delete", urlPatterns = { "/inventory/DeilveryNoteDeleteExecute" })
public class DeilveryNoteDeleteExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeilveryNoteDeleteExecute() {
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
		
		int DeliveryIDInputForm = Utilities.parseInt(request.getParameter("DeliveryNoteEditID"));
		
		//System.out.println("Delivery ID " + DeliveryIDInputForm);
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();	
		
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			
			
			//Statement s = ds.createStatement();	
			Statement s1 = ds.createStatement();
			
			
			
			
			//Inserting in to log tables	
			
			String DeleteRightQuery = "SELECT is_received, is_delivered, created_on from inventory_delivery_note where delivery_id="+DeliveryIDInputForm;
			
			
			
			ResultSet rs = s1.executeQuery(DeleteRightQuery);
			if(rs.first())
			{
				
				
				
				//if(DateUtils.isSameDay(new java.util.Date(), rs.getDate("created_on")) && rs.getInt("is_received") == 0 && rs.getInt("is_delivered")== 0)
				if(rs.getInt("is_received") == 0 && rs.getInt("is_delivered")== 0)
					
				{
					String LogQueryDeliveryNoteProducts = "INSERT INTO deleted_inventory_delivery_note_products(delivery_id,product_id,raw_cases,units ,  total_units ,  liquid_in_ml,  batch_code,deleted_on,deleted_by)"+
							  "SELECT *,now(),'"+UserID+"' FROM inventory_delivery_note_products where delivery_id="+DeliveryIDInputForm;		
					s1.executeUpdate(LogQueryDeliveryNoteProducts);
					//now deleting from main table
					String DeleteDeliveryNoteProductQuery = "delete from inventory_delivery_note_products where delivery_id="+DeliveryIDInputForm;
					s1.executeUpdate(DeleteDeliveryNoteProductQuery);
					
					
					
					String LogQueryDeliveryNote = "INSERT INTO deleted_inventory_delivery_note(" +
													   
							"delivery_id,created_on,created_by,distributor_id,remarks,vehicle_no,sap_order_no,"
													  +"barcode ,"
													  +"is_delivered,"
													  +"delivered_on,"
													  +"delivered_by,"
													  +"is_received,"
													  +"received_on,"
													  +"received_by,"
													  +"converted_cases,"
													  +"warehouse_id,"
													  +"payment_method,"
													  +"is_partial,"
													  +"invoice_no,"
													  +"vehicle_type,"
													  +"uvid,"
													  +"invoice_amount,edited_on,edited_by,"
													  +"freight_amount,freight_contractor_id,outsourced_primary_sales_id,palletize_type_id,deleted_on,"
													  +"deleted_by)"
													  +"SELECT *,now(),'"+UserID+"' FROM inventory_delivery_note where delivery_id="+DeliveryIDInputForm;
					
					s1.executeUpdate(LogQueryDeliveryNote);
					String DeleteDeliveryNoteQuery = "delete from inventory_delivery_note where delivery_id="+DeliveryIDInputForm;
					s1.executeUpdate(DeleteDeliveryNoteQuery);
					 
					
					obj.put("success", "true");
					
					ds.commit();
					
					//s.close();
					s1.close();
					ds.dropConnection();
				}
				else
				{
					obj.put("success", "false");
					obj.put("error", "Could not delete Delivery Note. Gatepass exists or it was issued in past date.");					
				}
					
			}
			
			
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
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
