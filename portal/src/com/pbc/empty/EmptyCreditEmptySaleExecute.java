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


@WebServlet(description = "Empty Credit Empty Sale ", urlPatterns = { "/empty/EmptyCreditEmptySaleExecute" })
public class EmptyCreditEmptySaleExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmptyCreditEmptySaleExecute() {
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
		
		
		
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorIdd"));
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));		
		long WareHouseID = Utilities.parseLong(request.getParameter("WarehouseID"));
		
		int[] ProductID = Utilities.parseInt(request.getParameterValues("ProductIdd"));
		int[] RawCases=Utilities.parseInt(request.getParameterValues("Rawcases"));
		int[] Units=Utilities.parseInt(request.getParameterValues("Unitss"));
		long[] LiquidInMl=Utilities.parseLong(request.getParameterValues("LiquidInMll"));
		long[] UnitPerSKU = Utilities.parseLong(request.getParameterValues("UnitPerSKUU"));
		long[] TotalUnits = Utilities.parseLong(request.getParameterValues("TotalUnitss"));
		
		
		
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
			
			
		
				
				s.executeUpdate("insert into ec_empty_sale_receipt(created_on,created_by,uvid, distributor_id,warehouse_id) values(now(),"+UserID+","+UniqueVoucherID+", "+DistributorID+","+WareHouseID+")");
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					MasterTableID = rs.getInt(1); 
				}
				IsSameDay = true; //making this true so that our sub table query can run in insertion case
				IsReceivedValid=true;
				
				
			
			
			
			//in update case
			
			
				if(ProductID !=null) {
					for(int i=0; i<ProductID.length; i++) {
						
						long LiquInML = TotalUnits[i]*LiquidInMl[i];
						s.executeUpdate("insert into ec_empty_sale_receipt_products (id,product_id,raw_cases,units,total_units,liquid_in_ml) values("+MasterTableID+","+ProductID[i]+","+RawCases[i]+","+Units[i]+","+TotalUnits[i]+","+LiquInML+")");
						s.executeUpdate("insert into ec_transactions (created_on,created_on_date,created_by,distributor_id,product_id,type_id,raw_cases_issued,units_issued,total_units_issued,raw_cases_received,units_received,total_units_received,remarks,empty_sale_id,warehouse_id) values(now(),"+Utilities.getSQLDate(new Date())+","+UserID+","+DistributorID+","+ProductID[i]+",1,0,0,0,"+RawCases[i]+","+Units[i]+","+TotalUnits[i]+",'Empty Sale',"+MasterTableID+","+WareHouseID+")");
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
