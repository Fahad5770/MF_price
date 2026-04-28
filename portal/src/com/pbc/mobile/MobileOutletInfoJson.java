package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Owner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Blob;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Outlet info json", urlPatterns = { "/mobile/MobileOutletInfoJson" })
public class MobileOutletInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileOutletInfoJson() {
        super();
        // TODO Auto-generated constructor stub
        
        
        
        
    }
    
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//aaa
		try{
		
			System.out.println("I am here, servelet called - Sync!!! ");
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		//if (!mr.isExpired()){
			
			
			
			
			
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			long Outletid = Utilities.parseLong(mr.getParameter("outletid"));
			//String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			
			
			
				///////////////////////////////////
				
			
			
			//System.out.println("Hello");
			
			
			
				
			Datasource ds = new Datasource();
			
			try{
				
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				response.setContentType("application/json");
				JSONObject obj=new JSONObject();
				JSONArray jr = new JSONArray();
				
				//System.out.println("select outlet_id from mrd_census where mobile_census_id="+MobileCensusID);
				
				long OutletID=0;
				String OutletName="";
				String OutletAddress="";
				String Channel="";
				String VPOClassification="";
				
				
				//System.out.println("SELECT * FROM pep.common_assets where bar_code='"+BarCode+"'");
				
				ResultSet rs2 = s.executeQuery("SELECT id,name,address,channel_id,(select label from common_outlets_channels coc where coc.id=channel_id) channel_label,vpo_classifications_id, (select label from common_outlets_vpo_classifications covc where covc.id=vpo_classifications_id) vpo_label FROM pep.common_outlets where id="+Outletid);
				if(rs2.first()){
					OutletID = rs2.getLong("id");
					OutletName = rs2.getString("name");
					
					OutletAddress = rs2.getString("Address");					
					Channel = rs2.getString("channel_label");
					VPOClassification = rs2.getString("vpo_label");
					
					
				}
				
				
				
				
				//Visit Frequency
				
			long VisitFreqCount=0;
			String VisitFreqLabel="";
			
			ResultSet rs23 = s.executeQuery("SELECT count(*) visit_fre FROM pep.distributor_beat_plan_schedule where outlet_id="+Outletid);
			while(rs23.next()){
				VisitFreqCount = rs23.getLong("visit_fre");
			}
				
			ResultSet rs24 = s.executeQuery("SELECT id,outlet_id,day_number, GROUP_CONCAT( (select short_name from common_days_of_week cdow where cdow.id=day_number ),'') visit_freq_label FROM pep.distributor_beat_plan_schedule where outlet_id="+Outletid);	
			while(rs24.next()){
				VisitFreqLabel = rs24.getString("visit_freq_label");
			}
			
			
			
			//# of Coolers
			
			long NoOfCooler=0;
			ResultSet rs25 = s.executeQuery("SELECT count(*) no_of_cooler FROM pep.common_assets where outlet_id_parsed="+Outletid+" and tot_status='INJECTED'");
			while(rs25.next()){
				NoOfCooler = rs25.getLong("no_of_cooler");
			}
			
			//Last Sale Date
			
			Date LastSaleDate = new Date();
			
			//ResultSet rs26 = s.executeQuery("SELECT max(cache_order_created_on_date) FROM pep.inventory_sales_adjusted_products where cache_outlet_id="+Outletid);
			//while(rs26.next()){
				//LastSaleDate = rs26.getDate(1);
			//}
			
			
			//Last Sale Quantity 
			long LastSaleQuantity =0;
			
			//ResultSet rs27 = s.executeQuery("SELECT sum(raw_cases) FROM pep.inventory_sales_adjusted_products where cache_outlet_id=73148 and id=(select max(id) FROM pep.inventory_sales_adjusted_products where cache_outlet_id="+Outletid+")");
			//while(rs27.next()){
			//	LastSaleQuantity = rs27.getLong(1);
			//}
			
			
			//MTD Drop Size
			
			////select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-04-01' and '2017-05-01' and isa.outlet_id=73148; -- MTD

			/* dropsize = rawcases/total invoice ---- total invoices*/ 
			/////select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2017-04-01' and '2017-05-01' and isa.outlet_id=73148; -- MTD
			
			Date StartDate = new Date();
			
			StartDate = Utilities.getStartDateByDate(StartDate); //getting starting of month
			
			Date EndDate = new Date();
			
			
			long RawCases=0;
			long TotalInvoices=0;
			
			double DropSize =0;
			
			ResultSet rs28 = s.executeQuery("select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+Outletid);
			while(rs28.next()){
				RawCases = rs28.getLong(1);
			}
			
			ResultSet rs29 = s.executeQuery("select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id="+Outletid);
			while(rs29.next()){
				TotalInvoices = rs29.getLong(1);
			}
			
			if(TotalInvoices!=0){
				DropSize = RawCases/TotalInvoices;
			}
			
			
			obj.put("OutletID", OutletID);
			obj.put("OutletName", OutletName);
			obj.put("Address", OutletAddress);
			obj.put("Channel", Channel);				
			obj.put("VPO", VPOClassification);
			
			obj.put("VisitFre", VisitFreqCount);
			obj.put("VisitFreLabel", VisitFreqLabel);
			obj.put("NoOfCooler", NoOfCooler);
			
			obj.put("LastSaleDate", Utilities.getDisplayDateFormat(LastSaleDate));
			obj.put("LastSaleQuantity", LastSaleQuantity);
			obj.put("DropSize", DropSize);
			
			
			
			obj.put("rows", jr);
					
					
				
				obj.put("success", "true");
				
				out.print(obj);
				out.close();	
				
					
				
				
				s.close();
				ds.commit();
				
				
			}catch(Exception e){
				
				ds.rollback();
				
				e.printStackTrace();
				//System.out.print(e);
				json.put("success", "false");
				json.put("error_code", "106");
				
			}finally{
				
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		//}else{
		//	json.put("success", "false");
		//	json.put("error_code", "101");
		//}
		
		out.print(json);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
