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


@WebServlet(description = "Executes Delivery Note", urlPatterns = { "/inventory/DeilveryNoteExecute" })
public class DeilveryNoteExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeilveryNoteExecute() {
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
		boolean isEditCase = false;
		
		if (DeliveryIDInputForm > 0){
			isEditCase = true;
		} 
		
		
		
		long DistributorID = Utilities.parseLong(request.getParameter("DeliveryNoteDistributorID"));
		
		
		System.out.println("Distributor "+DistributorID);
		
		
		String VehicleNo = Utilities.filterString(request.getParameter("DeliveryNoteVehicleNo"), 1, 100);
		
		
		String Remarks = Utilities.filterString(request.getParameter("DeliveryNoteRemarks"), 1, 100);
		
		
		
		
		
		
		
		long ProductID[] = Utilities.parseLong(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormUnits"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormUnitPerSKU"));
		int LiquidInML[] = Utilities.parseInt(request.getParameterValues("DeliveryNoteMainFormLiquidInML"));
		String BatchCode[] = Utilities.filterString(request.getParameterValues("DeliveryNoteMainFormBatchCode"), 1, 100);
		
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		
		//System.out.println("RawCases Array Length "+RawCases.length);
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		try {
			
			if (!isEditCase){
			
			ds.createConnection();
			ds.startTransaction();	
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
				
			int isDelivered = 0;
			if(isEditCase){
				System.out.println("select created_on, is_delivered from inventory_delivery_note where delivery_id="+DeliveryIDInputForm);
				ResultSet rs_date_check = s.executeQuery("select created_on, is_delivered from inventory_delivery_note where delivery_id="+DeliveryIDInputForm);
				if(rs_date_check.first()){
					VoucherDate = rs_date_check.getDate(1);
					isDelivered = rs_date_check.getInt(2);
				}
			}			
			
			
			
			//System.out.println("Hello delivery note ready from development to live!!!!");
		
				String SQLMain = "";
				
				
					
					
					
					SQLMain = "insert into inventory_delivery_note (created_on, created_by, distributor_id, remarks, vehicle_no,  barcode,     uvid) values(now(), "+UserID+", "+DistributorID+", '"+Remarks+"', '"+VehicleNo+"', "+UniqueVoucherID+","+UniqueVoucherID+")";
				
				
				System.out.println(SQLMain);
				s.executeUpdate(SQLMain);
				
				int DeliveryID = 0;
				
				
				if(!isEditCase){
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						DeliveryID = rs.getInt(1);
					}
					
					//Updating the SAP Code - Replace SAP Code with Delivery ID so that reports can run - Patch by Zulqunan on 25/07/2019
					System.out.println("update inventory_delivery_note set sap_order_no="+DeliveryID+" where delivery_id="+DeliveryID);
					s2.executeUpdate("update inventory_delivery_note set sap_order_no="+DeliveryID+" where delivery_id="+DeliveryID);
					
				}else{
					DeliveryID = DeliveryIDInputForm;
				}
				
				
				
				
				s.executeUpdate("delete from inventory_delivery_note_products where delivery_id="+DeliveryID);
				try{
					for(int i = 0; i < ProductID.length; i++){
						
							if( RawCases[i]==0 && Units[i]==0) {
								//DoNothing
							}else {                                      /////////////////////if((RawCases[i]!=0 &&  Units[i]!=0) || (RawCases[i]!=0 &&  Units[i]==0)) {
								long TotalUnits = ( RawCases[i] * UnitPerSKU[i] ) + Units[i];
								long LiquidInMLValue = TotalUnits * LiquidInML[i];
								System.out.println("1 : insert into inventory_delivery_note_products (delivery_id, product_id, raw_cases, units, total_units, liquid_in_ml, batch_code) values ("+DeliveryID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", '"+BatchCode[i]+"')");
								s.executeUpdate("insert into inventory_delivery_note_products (delivery_id, product_id, raw_cases, units, total_units, liquid_in_ml, batch_code) values ("+DeliveryID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", '"+BatchCode[i]+"')");
								
								System.out.println("SELECT empty_product_id FROM inventory_products_map where product_id="+ProductID[i]);
								ResultSet rs = s.executeQuery("SELECT empty_product_id FROM inventory_products_map where product_id="+ProductID[i]);
								if(rs.first()){
									System.out.println("2 : insert into inventory_delivery_note_products (delivery_id, product_id, raw_cases, units, total_units, liquid_in_ml, batch_code) values ("+DeliveryID+", "+rs.getInt("empty_product_id")+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", '"+BatchCode[i]+"')");
									s2.executeUpdate("insert into inventory_delivery_note_products (delivery_id, product_id, raw_cases, units, total_units, liquid_in_ml, batch_code) values ("+DeliveryID+", "+rs.getInt("empty_product_id")+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", '"+BatchCode[i]+"')");
								}
								
								
//								if (ProductID[i] == 109 || ProductID[i] == 110 || ProductID[i] == 157 || ProductID[i] == 158 || ProductID[i] == 159 || ProductID[i] == 184 || ProductID[i] == 185 || ProductID[i] == 186){ // Shell, pallet and sheet issuance in empty module
//									System.out.println("3 : insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,remarks,delivery_note_id) values(now(),curdate(),"+UserID+","+DistributorID+","+ProductID[i]+",1,"+RawCases[i]+", 0, "+RawCases[i]+",'Order#',"+DeliveryID+")");
//									s2.executeUpdate("insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,remarks,delivery_note_id) values(now(),curdate(),"+UserID+","+DistributorID+","+ProductID[i]+",1,"+RawCases[i]+", 0, "+RawCases[i]+",'Order#',"+DeliveryID+")");
//								}
							
							}
						//}
						
					}
					
					
					
					
					System.out.println("select idnp.product_id, (SELECT empty_product_id FROM ec_empty_products_map where product_id=idnp.product_id) empty_product_id, (select unit_per_sku from inventory_products where id = idnp.product_id) unit_per_sku, sum(total_units), (SELECT shell_product_id FROM ec_empty_products_map where product_id=idnp.product_id) shell_product_id from inventory_delivery_note_products idnp where idnp.delivery_id = "+DeliveryID+" and idnp.product_id in (select product_id from ec_empty_products_map) group by idnp.product_id");
					
					ResultSet rs3 = s.executeQuery("select idnp.product_id, (SELECT empty_product_id FROM ec_empty_products_map where product_id=idnp.product_id) empty_product_id, (select unit_per_sku from inventory_products where id = idnp.product_id) unit_per_sku, sum(total_units), (SELECT shell_product_id FROM ec_empty_products_map where product_id=idnp.product_id) shell_product_id from inventory_delivery_note_products idnp where idnp.delivery_id = "+DeliveryID+" and idnp.product_id in (select product_id from ec_empty_products_map) group by idnp.product_id");
					while(rs3.next()){
						int iEmptyProductID = rs3.getInt(2);
						int iUnitPerSKU = rs3.getInt(3);
						long iTotalUnits = rs3.getInt(4);
						long RawCasesUnits[] = Utilities.getRawCasesAndUnits(iTotalUnits, iUnitPerSKU);
						int iShellProductID = rs3.getInt(5);
						
						long ShellQty = RawCasesUnits[0];
						if (RawCasesUnits[1] > 0){
							ShellQty++;
						}
						System.out.println("insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,remarks,delivery_note_id) values(now(),curdate(),"+UserID+","+DistributorID+","+iEmptyProductID+",1,"+RawCasesUnits[0]+", "+RawCasesUnits[1]+", "+iTotalUnits+",'Order#',"+DeliveryID+")");
						s2.executeUpdate("insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,remarks,delivery_note_id) values(now(),curdate(),"+UserID+","+DistributorID+","+iEmptyProductID+",1,"+RawCasesUnits[0]+", "+RawCasesUnits[1]+", "+iTotalUnits+",'Order#',"+DeliveryID+")");
						
						if (iShellProductID != 0){
							//s2.executeUpdate("insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,remarks,delivery_note_id, warehouse_id) values(now(),curdate(),"+UserID+","+DistributorID+","+iShellProductID+",1,"+ShellQty+", 0, "+ShellQty+",'Order# "+SAPOrderNo+"',"+DeliveryID+","+WarehouseID+")");
						}
						
					}
					
					
				}catch(Exception e){
					System.out.println("Exception during inserting products in delivery note");
					System.out.println(e);
				}
				
				/*	
				s.executeUpdate("delete from inventory_delivery_note_partial_orders where sap_order_no="+SAPOrderNo);
				if(isPartial == 1){
					s.executeUpdate("insert into inventory_delivery_note_partial_orders (sap_order_no) values ("+SAPOrderNo+")");
				}
				*/
				

				
				
				
				
					
				/*if (DistributorID == 100356 || DistributorID == 100951 || DistributorID == 100431 || DistributorID == 201027 || DistributorID == 100961){
					Utilities.sendSMS("923334566993", "Delivery Alert: Customer ID:"+DistributorID+" Order#"+SAPOrderNo+" Vehicle#"+VehicleNo);
					Utilities.sendPBCEmail(new String[]{"shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk"}, null, null, "Delivery Alert: Customer ID:"+DistributorID+" Order#"+SAPOrderNo, "Customer ID:"+DistributorID+" Order#"+SAPOrderNo+" Vehicle#"+VehicleNo, null);
				}*/
					
							
				
				obj.put("success", "true");
				obj.put("DeliveryID", DeliveryID);
				
			
			
			
			ds.commit();
			s2.close();
			s.close();
			//ds.dropConnection();
			
			
			}else{
				
				obj.put("success", "false");
				obj.put("error", "The document could not be edited.");
			}
			
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
