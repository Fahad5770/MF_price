package com.pbc.empty;

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

import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Empty Credit Receipt ", urlPatterns = { "/empty/EmptyCreditReceiptExecute" })
public class EmptyCreditReceiptExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmptyCreditReceiptExecute() {
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
		
		long DispatchID =Utilities.parseLong(request.getParameter("DispatchIDForInsertion"));
		int[] ProductID = Utilities.parseInt(request.getParameterValues("ProductID"));
		int[] RawCases=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormRawCases"));
		int[] Units=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormUnits"));
		long[] LiquidInMl=Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormLiquidInML"));
		long[] UnitPerSKU = Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormUnitPerSKU"));
		long[] TypeID = Utilities.parseLong(request.getParameterValues("EmptyCreditReceiptType"));
		
		String VehicleNumber = Utilities.filterString(request.getParameter("DispatchReturnsVehicleNumberhidden"), 1, 150);
		long ArrivalNumber = Utilities.parseLong(request.getParameter("DispatchReturnsArrivalNumberhidden"));
		long SerialNo = Utilities.parseLong(request.getParameter("MainFormSerialNo"));
		int isEditHiddenFlag = Utilities.parseInt(request.getParameter("isEditHiddenFlag"));
		long ReceiptFromProductionEditID = Utilities.parseLong(request.getParameter("ReceiptFromProductionEditID"));
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		long DistributorID = Utilities.parseLong(request.getParameter("EmptyCreditReceiptMainDistributorHidden"));
		
		long WareHouseID = Utilities.parseLong(request.getParameter("EmptyCreditReceiptWarehouseIDHidden"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();	
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			long MasterTableID=0;
			Date date = new Date();
			boolean IsSameDay=false;
			boolean IsReceivedValid=false;
			
			//System.out.println("insert into inventory_production_receipts(created_on,created_by,vehicle_no) values(now(),"+UserID+",'"+VehicleNumber+"')");
			if(isEditHiddenFlag==1){ //1=insertion
				//System.out.println("insert into inventory_production_receipts(created_on,created_by,vehicle_no,uvid, serial_no) values(now(),"+UserID+",'"+VehicleNumber+"',"+UniqueVoucherID+", "+SerialNo+")");
				s.executeUpdate("insert into ec_empty_receipt(created_on,created_by,vehicle_no,uvid, distributor_id,warehouse_id, arrival_no) values(now(),"+UserID+",'"+VehicleNumber+"',"+UniqueVoucherID+", "+DistributorID+","+WareHouseID+", "+ArrivalNumber+")");
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					MasterTableID = rs.getInt(1); 
				}
				IsSameDay = true; //making this true so that our sub table query can run in insertion case
				IsReceivedValid=true;
				
				
			}else if(isEditHiddenFlag==2){ //2 = edit case
				ResultSet rs4 = s.executeQuery("select created_on,is_received from ec_empty_receipt where id="+ReceiptFromProductionEditID);
				if(rs4.first()){
					if(DateUtils.isSameDay(new java.util.Date(), rs4.getDate("created_on"))){
						IsSameDay = true;
					}
					if(rs4.getInt("is_received")==0){
						IsReceivedValid=true;
					}
				}
				
				
				
			}
			
			
			//in update case
			
			
				if(ProductID !=null) {
					for(int i=0; i<ProductID.length; i++) {
						long TotalUnits =(RawCases[i]*UnitPerSKU[i])+Units[i];
						long LiquInML = TotalUnits*LiquidInMl[i];
						s.executeUpdate("insert into ec_empty_receipt_products (id,product_id,raw_cases,units,total_units,liquid_in_ml,type_id) values("+MasterTableID+","+ProductID[i]+","+RawCases[i]+","+Units[i]+","+TotalUnits+","+LiquInML+","+TypeID[i]+")");
						s.executeUpdate("insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,raw_cases_received,units_received,total_units_received,remarks,empty_receipt_id,warehouse_id) values(now(),"+Utilities.getSQLDate(new Date())+","+UserID+","+DistributorID+","+ProductID[i]+","+TypeID[i]+",0,0,0,"+RawCases[i]+","+Units[i]+","+TotalUnits+",'Empty Received Vehicle# "+VehicleNumber+"',"+MasterTableID+","+WareHouseID+")");
						//System.out.println("insert into inventory_production_receipts_products (id,product_id,raw_cases,units,total_units,liquid_in_ml) values("+DispatchID+","+ProductID[i]+","+RawCases[i]+","+Units[i]+","+TotalUnits+","+LiquInML+")");
					}
					
					obj.put("success", "true");
					obj.put("MasterTableID", MasterTableID);
				}
			
			
			ds.commit();
				
			
			s.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
