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


@WebServlet(description = "Executes Damaged Stock", urlPatterns = { "/inventory/DamagedStockExecute" })
public class DamagedStockExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DamagedStockExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		int DocumentTypeID = 12;
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		int DamagedStockEditIDInputForm = Utilities.parseInt(request.getParameter("DamagedStockEditID"));
		boolean isEditCase = false;
		
		if (DamagedStockEditIDInputForm > 0){
			isEditCase = true;
		} 
		
		long DistributorID = Utilities.parseLong(session.getAttribute("UserDistributorID")+"");
		
		String Remarks = Utilities.filterString(request.getParameter("DamagedStockMainFormRemarks"), 1, 100);
		
		long ProductID[] = Utilities.parseLong(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DamagedStockMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DamagedStockMainFormUnits"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DamagedStockMainFormUnitPerSKU"));
		int LiquidInML[] = Utilities.parseInt(request.getParameterValues("DamagedStockMainFormLiquidInML"));
		String BatchCode[] = Utilities.filterString(request.getParameterValues("DamagedStockMainFormBatchCode"), 1, 100);
		
		int DamageType[] = Utilities.parseInt(request.getParameterValues("DamagedStockMainFormType"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		StockDocument DamagedStockDocument = new StockDocument();
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			if(isEditCase){
				ResultSet rs_date_check = s.executeQuery("select created_on from inventory_damaged_stock where document_id="+DamagedStockEditIDInputForm);
				if(rs_date_check.first()){
					VoucherDate = rs_date_check.getDate(1);
				}
			}
			
			if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
			
				String SQLMain = "";
				
				if(isEditCase){
					SQLMain = "replace into inventory_damaged_stock (document_id, created_on, created_by, distributor_id, remarks) values('"+DamagedStockEditIDInputForm+"', now(), "+UserID+", "+DistributorID+", '"+Remarks+"')";
				}else{
					SQLMain = "insert into inventory_damaged_stock (created_on, created_by, distributor_id, remarks) values(now(), "+UserID+", "+DistributorID+", '"+Remarks+"')";
				}
				
				s.executeUpdate(SQLMain);
				
				int DamagedStockID = 0;
				
				if(!isEditCase){
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						DamagedStockID = rs.getInt(1);
					}
				}else{
					DamagedStockID = DamagedStockEditIDInputForm;
					
					StockPosting DamagedStockPosting = new StockPosting();
					DamagedStockPosting.unPostStock(DocumentTypeID, DamagedStockID);
					
				}
				
				// set document object
				
				DamagedStockDocument.DISTRIBUTOR_ID = DistributorID;
				DamagedStockDocument.DOCUMENT_TYPE_ID = DocumentTypeID;
				DamagedStockDocument.DOCUMENT_ID = DamagedStockID;
				DamagedStockDocument.CREATED_ON = new java.util.Date();
				DamagedStockDocument.CREATED_BY = Long.parseLong(UserID);
				
				s.executeUpdate("delete from inventory_damaged_stock_products where document_id="+DamagedStockID);
				
				for(int i = 0; i < ProductID.length; i++){
					int TotalUnits = ( RawCases[i] * UnitPerSKU[i] ) + Units[i];
					long LiquidInMLValue = TotalUnits * LiquidInML[i];
					s.executeUpdate("insert into inventory_damaged_stock_products (document_id, product_id, raw_cases, units, total_units, liquid_in_ml, batch_code, type_id) values ("+DamagedStockID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", '"+BatchCode[i]+"', '"+DamageType[i]+"')");
					
					// set document items object
					
					StockDocumentItems DamagedStockDocumentItemsIssued = new StockDocumentItems();
					
					DamagedStockDocumentItemsIssued.PRODUCT_ID = ProductID[i];
					DamagedStockDocumentItemsIssued.RAW_CASES = RawCases[i];
					DamagedStockDocumentItemsIssued.UNITS = Units[i];
					DamagedStockDocumentItemsIssued.TOTAL_UNITS = TotalUnits;
					DamagedStockDocumentItemsIssued.LIQUID_IN_ML = LiquidInMLValue;
					DamagedStockDocumentItemsIssued.TRANSACTION_TYPE = 1;
					DamagedStockDocumentItemsIssued.LOCATION_ID = 1;
					
					StockDocumentItems DamagedStockDocumentItemsReceived = new StockDocumentItems();
					
					DamagedStockDocumentItemsReceived.PRODUCT_ID = ProductID[i];
					DamagedStockDocumentItemsReceived.RAW_CASES = RawCases[i];
					DamagedStockDocumentItemsReceived.UNITS = Units[i];
					DamagedStockDocumentItemsReceived.TOTAL_UNITS = TotalUnits;
					DamagedStockDocumentItemsReceived.LIQUID_IN_ML = LiquidInMLValue;
					DamagedStockDocumentItemsReceived.TRANSACTION_TYPE = 2;
					DamagedStockDocumentItemsReceived.LOCATION_ID = 2;
					
					DamagedStockDocument.PRODUCTS.add(DamagedStockDocumentItemsIssued);
					DamagedStockDocument.PRODUCTS.add(DamagedStockDocumentItemsReceived);
					
				}
				
				StockPosting DamagedStockPosting = new StockPosting();
				DamagedStockPosting.postStock(DamagedStockDocument);
				
				obj.put("success", "true");
				obj.put("DamagedStockID", DamagedStockID);
			
			}else{
				obj.put("success", "false");
				obj.put("error", "You can only edit today's voucher");
			}
			
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
