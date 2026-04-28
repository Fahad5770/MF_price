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
import org.omg.CORBA.portable.ValueFactory;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;



@WebServlet(description = "Executes Physical Stock Adjustment", urlPatterns = { "/inventory/PhysicalStockAdjustmentExecute" })
public class PhysicalStockAdjustmentExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PhysicalStockAdjustmentExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		int DocumentTypeID = 18;
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		int EditID = Utilities.parseInt(request.getParameter("PhysicalStockAdjustmentEditID"));
		boolean isEditCase = false;
		
		if (EditID > 0){
			isEditCase = true;
		} 
		
		long DistributorID = Utilities.parseLong(request.getParameter("PhysicalStockAdjustmentDistributorID"));
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		
		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("PhysicalStockAdjustmentMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("PhysicalStockAdjustmentMainFormUnits"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("PhysicalStockAdjustmentMainFormUnitPerSKU"));
		int LiquidInML[] = Utilities.parseInt(request.getParameterValues("PhysicalStockAdjustmentMainFormLiquidInML"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		StockDocument PhysicalStockAdjustmentDocument = new StockDocument();
		
		
		try {
			
			StockPosting PhysicalStockAdjustmentStockPosting = new StockPosting();
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			String SQLMain = "";
			
			ResultSet rs1 = s1.executeQuery("select id from inventory_distributor_stock_adjustment where uvid="+UniqueVoucherID);
			if( rs1.first() ){
				
				obj.put("success", "false");
				obj.put("error", "Already Exists");
				
			}else{
				
				s.getConnection().setAutoCommit(false);
				
				if(isEditCase){
					SQLMain = "replace into inventory_distributor_stock_adjustment (id, uvid, created_on, created_by, distributor_id) values("+EditID+", "+UniqueVoucherID+", now(), "+UserID+", "+DistributorID+")";
				}else{
					SQLMain = "insert into inventory_distributor_stock_adjustment (uvid, created_on, created_by, distributor_id) values("+UniqueVoucherID+", now(), "+UserID+", "+DistributorID+" )";
				}
				
				s.executeUpdate(SQLMain);
				
				int PhysicalStockAdjustmentID = 0;
				
				if(!isEditCase){
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						PhysicalStockAdjustmentID = rs.getInt(1);
					}
				}else{
					PhysicalStockAdjustmentID = EditID;
					
					PhysicalStockAdjustmentStockPosting.unPostStock(DocumentTypeID, PhysicalStockAdjustmentID);
				}
				
				// set document object
				
				PhysicalStockAdjustmentDocument.DISTRIBUTOR_ID = DistributorID;
				PhysicalStockAdjustmentDocument.DOCUMENT_TYPE_ID = DocumentTypeID;
				PhysicalStockAdjustmentDocument.DOCUMENT_ID = PhysicalStockAdjustmentID;
				PhysicalStockAdjustmentDocument.CREATED_ON = new java.util.Date();
				PhysicalStockAdjustmentDocument.CREATED_BY = Long.parseLong(UserID);
				
				s.executeUpdate("delete from inventory_distributor_stock_adjustment_products where id="+PhysicalStockAdjustmentID);

					for(int i = 0; i < ProductID.length; i++){
						
						long TotalUnits = ( RawCases[i] * UnitPerSKU[i] ) + Units[i];
						long LiquidInMLValue = TotalUnits * LiquidInML[i];
						
						s.executeUpdate("insert into inventory_distributor_stock_adjustment_products (id, product_id, raw_cases, units, total_units, liquid_in_ml) values ("+PhysicalStockAdjustmentID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+") ");
						
						int DocumentTransactionType = 0;
						
						
						
						long TotalUnitsClosingBalance = PhysicalStockAdjustmentStockPosting.getClosingBalance(DistributorID, ProductID[i], new java.util.Date());
						
						
						
						long UnitsStockDifference = TotalUnitsClosingBalance - TotalUnits;
						
						
						
						if(UnitsStockDifference > 0){
							DocumentTransactionType = 1;
						}else{
							DocumentTransactionType = 2;
						}
						
						
						
						if(UnitsStockDifference != 0){
							
							if(UnitsStockDifference < 0){
								UnitsStockDifference *= -1;
							}
							
							long RawCasesDifference = Utilities.getRawCasesAndUnits(UnitsStockDifference, UnitPerSKU[i])[0];
							long UnitsDifference = Utilities.getRawCasesAndUnits(UnitsStockDifference, UnitPerSKU[i])[1];
							
							long LiquidInMLValueDifference = UnitsStockDifference * LiquidInML[i];
							
							StockDocumentItems PhysicalStockAdjustmentStockDocumentItems = new StockDocumentItems();
							
							PhysicalStockAdjustmentStockDocumentItems.PRODUCT_ID = ProductID[i];
							PhysicalStockAdjustmentStockDocumentItems.RAW_CASES = (int) RawCasesDifference;
							PhysicalStockAdjustmentStockDocumentItems.UNITS = (int) UnitsDifference;
							PhysicalStockAdjustmentStockDocumentItems.TOTAL_UNITS = (int) UnitsStockDifference;
							PhysicalStockAdjustmentStockDocumentItems.LIQUID_IN_ML = LiquidInMLValueDifference;
							PhysicalStockAdjustmentStockDocumentItems.TRANSACTION_TYPE = DocumentTransactionType;
							PhysicalStockAdjustmentStockDocumentItems.LOCATION_ID = 1;
							
							PhysicalStockAdjustmentDocument.PRODUCTS.add(PhysicalStockAdjustmentStockDocumentItems);
							
						}
					}
					
				ds.commit();
				
				PhysicalStockAdjustmentStockPosting.postStock(PhysicalStockAdjustmentDocument);
				
				//boolean posted = SalesPosting.post(DeskSaleID, Long.parseLong(UserID));
				
				//System.out.println("success case");
				
				obj.put("success", "true");
				obj.put("PhysicalStockAdjustmentID", PhysicalStockAdjustmentID);
				
			}
			
			s1.close();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			obj.put("success", "false");
			obj.put("exception", e);
			e.printStackTrace();
			//out.print(e);
		}finally{
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("before end close");
		out.print(obj);
		out.close();
		//System.out.println("after end close");
		
	}
	
}