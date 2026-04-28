package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;
import com.mf.utils.MFAPIFunctions;
import com.pbc.bi.BiProcesses;
import com.pbc.common.Distributor;


@WebServlet(description = "Mobile Stock Position", urlPatterns = { "/mobile/MobileStockPosition" })
public class MobileStockPosition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileStockPosition() {
        super();
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("json");
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		
		
		System.out.println("MobileStockPosition");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			String UserID = Utilities.filterString(mr.getParameter("UserID"), 1, 200);
			
		
			
			//System.out.println("MobileAuthenticateUserV5 User:"+LoginUsername);
			
			int LogTypeID = 0;
			
			Datasource ds = new Datasource();
			Datasource dsr = new Datasource();
			
			try {
				
				ds.createConnection();
				
				dsr.createConnectionToReplica();
				Statement sr = dsr.createStatement();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				Statement s4 = ds.createStatement();
				
//				ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+DeviceID+"' and is_active=1");
//				if(rsD.first()){
					
					long distributorId = 0;
					ResultSet rs4 = s.executeQuery("SELECT distributor_id FROM distributor_beat_plan_view where assigned_to="+UserID);
					if(rs4.first()) {
						distributorId=rs4.getLong(1);
					}
					
					long SelectedDistributorsArray[] = {distributorId};
					
					
					StockPosting sp = new StockPosting(true);	
					
					int ProductID = 0;
					int UnitPerSKU = 0;
					ResultSet rs3 = s3.executeQuery("SELECT ipv.product_id, ipv.unit_per_sku from inventory_products_view ipv ");
					while(rs3.next()){
						ProductID = rs3.getInt(1);
						UnitPerSKU = rs3.getInt(2);
						
						Date YesterdayDate = DateUtils.addDays(new Date(), -1);
						long ClosingUnits = sp.getClosingBalance(SelectedDistributorsArray, ProductID, YesterdayDate);
						
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("ProductID", ProductID);
						rows.put("ClosingUnits", ClosingUnits);
						rows.put("ClosingRawCases", MFAPIFunctions.convertToRawCases(ClosingUnits, UnitPerSKU));
						jr.add(rows);
						System.out.println("Closing Stock "+ClosingUnits);
						System.out.println("getClosingBalance "+ClosingUnits);
					}
					json.put("StockPosition", jr);
					json.put("success", "true");
					
					sp.close();
					
				
					
				/*}else{
					LogTypeID = 5;
					json.put("success", "false");
					json.put("error_code", "102");
				}*/
				
				
				s4.close();
				s3.close();
				s2.close();
				s.close();
				ds.dropConnection();
				dsr.dropConnection();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		System.out.println(json);
		out.print(json);
		out.close();
		
	}
	
}
