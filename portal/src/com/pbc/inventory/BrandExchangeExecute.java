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



@WebServlet(description = "Brand Exchange ", urlPatterns = { "/inventory/BrandExchangeExecute" })
public class BrandExchangeExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BrandExchangeExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		int DocumentTypeID = 17;
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		int BrandExchangeEditIDInputForm = Utilities.parseInt(request.getParameter("BrandExchangeEditID"));
		boolean isEditCase = false;
		
		if (BrandExchangeEditIDInputForm > 0){
			isEditCase = true;
		}
		
		long DistributorID = Utilities.parseLong(request.getParameter("UserDistributorID"));
		
		String Remarks = Utilities.filterString(request.getParameter("BrandExchangeMainFormRemarks"), 1, 100);
	
		long Package[] = Utilities.parseLong(request.getParameterValues("BrandExchangeMainFormPackage"));
		int ProductIDIssue[] = Utilities.parseInt(request.getParameterValues("BrandExchangeMainFormProductIDIssue"));
		int ProductIDReceive[] = Utilities.parseInt(request.getParameterValues("BrandExchangeMainFormProductIDReceive"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("BrandExchangeMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("BrandExchangeMainFormUnits"));
		
		int UnitPerSKUIssue[] = Utilities.parseInt(request.getParameterValues("BrandExchangeMainFormUnitPerSKUIssue"));
		int UnitPerSKUReceive[] = Utilities.parseInt(request.getParameterValues("BrandExchangeMainFormUnitPerSKUReceive"));
		
		long LiquidInML[] = Utilities.parseLong(request.getParameterValues("BrandExchangeMainFormLiquidInML"));
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		StockDocument BrandExchangeDocument = new StockDocument();
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			if(isEditCase){
				ResultSet rs_date_check = s.executeQuery("select created_on from inventory_brand_exchange where document_id="+BrandExchangeEditIDInputForm);
				if(rs_date_check.first()){
					VoucherDate = rs_date_check.getDate(1);
				}
			}
			
			if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
			
				String SQLMain = "";
				
				if(isEditCase){
					SQLMain = "replace into inventory_brand_exchange (document_id, created_on, created_by, distributor_id, remarks) values('"+BrandExchangeEditIDInputForm+"', now(), "+UserID+", "+DistributorID+", '"+Remarks+"')";
				}else{
					SQLMain = "insert into inventory_brand_exchange (created_on, created_by, distributor_id, remarks) values(now(), "+UserID+", "+DistributorID+", '"+Remarks+"')";
				}
				
				s.executeUpdate(SQLMain);
				
				int BrandExchangeID = 0;
				
				if(!isEditCase){
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						BrandExchangeID = rs.getInt(1);
					}
				}else{
					BrandExchangeID = BrandExchangeEditIDInputForm;
					
					StockPosting BrandExchangeStockPosting = new StockPosting();
					BrandExchangeStockPosting.unPostStock(DocumentTypeID, BrandExchangeID);
					
				}
				
				// set document object
				
				BrandExchangeDocument.DISTRIBUTOR_ID = DistributorID;
				BrandExchangeDocument.DOCUMENT_TYPE_ID = DocumentTypeID;
				BrandExchangeDocument.DOCUMENT_ID = BrandExchangeID;
				BrandExchangeDocument.CREATED_ON = new java.util.Date();
				BrandExchangeDocument.CREATED_BY = Long.parseLong(UserID);
				
				s.executeUpdate("delete from inventory_brand_exchange_products where document_id="+BrandExchangeID);
				
				for(int i = 0; i < Package.length; i++){
					
					int TotalUnitsIssue = ( RawCases[i] * UnitPerSKUIssue[i] ) + Units[i];
					int TotalUnitsReceive = ( RawCases[i] * UnitPerSKUReceive[i] ) + Units[i];
					
					long LiquidInMLValueIssue = TotalUnitsIssue * LiquidInML[i];
					long LiquidInMLValueReceive = TotalUnitsReceive * LiquidInML[i];
					
					s.executeUpdate("insert into inventory_brand_exchange_products (document_id, product_id_issued, product_id_received, raw_cases, units, total_units) values ("+BrandExchangeID+", "+ProductIDIssue[i]+", "+ProductIDReceive[i]+", "+RawCases[i]+","+Units[i]+", "+TotalUnitsReceive+")");
					
					// set document items object
					
					StockDocumentItems BrandExchangeStockDocumentItemsIssued = new StockDocumentItems();
					
					BrandExchangeStockDocumentItemsIssued.PRODUCT_ID = ProductIDIssue[i];
					BrandExchangeStockDocumentItemsIssued.RAW_CASES = RawCases[i];
					BrandExchangeStockDocumentItemsIssued.UNITS = Units[i];
					BrandExchangeStockDocumentItemsIssued.TOTAL_UNITS = TotalUnitsIssue;
					BrandExchangeStockDocumentItemsIssued.LIQUID_IN_ML = LiquidInMLValueIssue;
					BrandExchangeStockDocumentItemsIssued.TRANSACTION_TYPE = 1;
					BrandExchangeStockDocumentItemsIssued.LOCATION_ID = 1;
					
					StockDocumentItems BrandExchangeStockDocumentItemsReceived = new StockDocumentItems();
					
					BrandExchangeStockDocumentItemsReceived.PRODUCT_ID = ProductIDReceive[i];
					BrandExchangeStockDocumentItemsReceived.RAW_CASES = RawCases[i];
					BrandExchangeStockDocumentItemsReceived.UNITS = Units[i];
					BrandExchangeStockDocumentItemsReceived.TOTAL_UNITS = TotalUnitsReceive;
					BrandExchangeStockDocumentItemsReceived.LIQUID_IN_ML = LiquidInMLValueReceive;
					BrandExchangeStockDocumentItemsReceived.TRANSACTION_TYPE = 2;
					BrandExchangeStockDocumentItemsReceived.LOCATION_ID = 1;
					
					BrandExchangeDocument.PRODUCTS.add(BrandExchangeStockDocumentItemsIssued);
					BrandExchangeDocument.PRODUCTS.add(BrandExchangeStockDocumentItemsReceived);
					
				}
				
				StockPosting BrandExchangeStockPosting = new StockPosting();
				BrandExchangeStockPosting.postStock(BrandExchangeDocument);
				
				obj.put("success", "true");
				obj.put("BrandExchangeID", BrandExchangeID);
			
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
