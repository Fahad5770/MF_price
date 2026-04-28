package com.pbc.distributor;

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
import com.pbc.inventory.SalesPosting;
import com.pbc.inventory.StockPosting;

@WebServlet(description = "Dispatch Execute Update ", urlPatterns = { "/distributor/DispatchSalesExecuteUpdate" })
public class DispatchSalesExecuteUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatchSalesExecuteUpdate() {
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
		
		//String DistributorVehicleNumber = Utilities.filterString(request.getParameter("VehicleNum"), 1, 100);
		int EditID = Utilities.parseInt(request.getParameter("EditIDForExecute"));
		
		int DispatchType = Utilities.parseInt(request.getParameter("DispatchVehicleType"));
		String VehicleIDLabel = Utilities.filterString(request.getParameter("DispatchVehicleSelect"),1,200);
		long DriverID = Utilities.parseLong(request.getParameter("DistributorDriverName"));
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorIDD"));
		long MasterTableID =0;
		long UVID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		String VehicleIDLabelArray[] = VehicleIDLabel.split(","); //vehicle id contain id,label --> id at 0 index
		long VehicleID = Utilities.parseLong(VehicleIDLabelArray[0]);
		String DriverIDString=null;
		String VehicleIDString=null;
		if(DriverID != 0)
		{
			DriverIDString = DriverID+""; 
		}
		if(VehicleID != 0)
		{
			VehicleIDString=VehicleID+"";
		}
		
		
		Date VoucherDate = new java.util.Date();
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			
			ResultSet rs_date_check = s.executeQuery("select created_on from inventory_sales_dispatch where id="+EditID);
			if(rs_date_check.first()){
				VoucherDate = rs_date_check.getDate(1);
			}
			
			if( (DateUtils.isSameDay(new java.util.Date(), VoucherDate)) || (DateUtils.isSameDay(DateUtils.addDays(new java.util.Date(), -1), VoucherDate)) ){
				String Query = "update inventory_sales_dispatch set created_on=now(), created_by="+UserID+", dispatch_type="+DispatchType+", vehicle_id="+VehicleIDString+", driver_id="+DriverIDString+", distributor_id="+DistributorID+" where id="+EditID;
				//System.out.println(Query);
				s.executeUpdate(Query);
				
				obj.put("success", "true");
			}else{
				obj.put("success", "false");
				obj.put("error", "You can only edit Today OR Yesterday's voucher");
			}
			
			
			
			s.close();

		} catch (Exception e) {
			
			
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
