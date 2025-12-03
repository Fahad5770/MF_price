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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;


@WebServlet(description = "Mobile DistributorApp Execute", urlPatterns = { "/mobile/MobileDistributorInventoryCashInflowExecute" })
public class MobileDistributorInventoryCashInflowExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileDistributorInventoryCashInflowExecute() {
        super();
    }

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("json");
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();
		
		//System.out.println("in Distributor App 131servlet");
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		//System.out.println("mr "+mr);
		if (!mr.isExpired()){
			
			
			long InvoiceID = Utilities.parseLong(mr.getParameter("InvoiceIDLive"));
			long Uvid = Utilities.parseLong(mr.getParameter("UvidIDLIve"));
			long OutletID = Utilities.parseLong(mr.getParameter("OutletIDLive"));
			long DistributorID = Utilities.parseLong(mr.getParameter("DistributorIDLive"));
			int CreatedBy= Utilities.parseInt(mr.getParameter("CreatedBy"));
			Double NetAmount = Utilities.parseDouble(mr.getParameter("NetAmount"));
			String lat = Utilities.filterString(mr.getParameter("OutletLatlive"),1,100);
			String lng=Utilities.filterString(mr.getParameter("OutletLongLive"),1,100);
			String accu = Utilities.filterString(mr.getParameter("OutletAccuLive"),1,100);
			int Payment_type = Utilities.parseInt(mr.getParameter("PaymentTypeLive"));
			long DispatchID = Utilities.parseLong(mr.getParameter("dispatchIDlive"));
			String AccountFrom =Utilities.filterString(mr.getParameter("AccountFrom"), 1, 100);//mr.getParameter("AccountFrom");
			String AccountTo = Utilities.filterString(mr.getParameter("AccountTo"), 1, 100);//
			String PinCode=Utilities.filterString(mr.getParameter("pin_code"),1,100);
			String TRAfterPin=Utilities.filterString(mr.getParameter("TRAfterPin"),1,100);
			int IsPartial = Utilities.parseInt(mr.getParameter("is_partial"));
			
			
			//System.out.println(InvoiceID+" 1-2 "+Uvid+" 2-3 "+OutletID+" 3-4 "+DistributorID+" 4-5 "+CreatedBy+" 5-6 "+NetAmount+" 6-7 "+lat+" 7-8 "+lng+" 8-9 "+accu+" 9-10 "+Payment_type+"10-11"+DispatchID);
			//System.out.println("1AccountFrom "+AccountFrom+"AccountTo "+AccountTo+ " "+PinCode+""+TRAfterPin);
			
			Datasource ds = new Datasource();
			
			
			try {
				
				ds.createConnection();
				//ds.startTransaction();
				String LogInsertionQueryString="";
				
				Statement s = ds.createStatement();
				Statement s1=ds.createStatement();
				Statement s2 = ds.createStatement();
				if(Payment_type==1) {
					LogInsertionQueryString="INSERT INTO inventory_sales_dispatch_invoices_collection (invoice_id,uvid,outlet_id,distributor_id,created_on,created_by,cash_received,lat,lng,accuracy,payment_type,dispatch_id)VALUES("+InvoiceID+","+Uvid+", "+OutletID+","+DistributorID+",now(), "+CreatedBy+","+NetAmount+","+lat+","+lng+","+accu+","+Payment_type+","+DispatchID+")";
				}else if(Payment_type==2){
					LogInsertionQueryString="INSERT INTO inventory_sales_dispatch_invoices_collection (invoice_id,uvid,outlet_id,distributor_id,created_on,created_by,cash_received,lat,lng,accuracy,payment_type,dispatch_id,account_from,account_to,pin_code,transaction_refrence,is_partial)VALUES("+InvoiceID+","+Uvid+", "+OutletID+","+DistributorID+",now(), "+CreatedBy+","+NetAmount+","+lat+","+lng+","+accu+","+Payment_type+","+DispatchID+",'"+AccountFrom+"','"+AccountTo+"',"+PinCode+",'"+TRAfterPin+"',"+IsPartial+")";
					
				}
				System.out.println(LogInsertionQueryString);
				int InsertQuery= s1.executeUpdate(LogInsertionQueryString);		
				
				if(InsertQuery>=1){
					json.put("QueryExecute", "true");
				}else{
					json.put("QueryExecute", "false");
				}
				
				//ds.commit();
			}catch(Exception e)
			{
				
				   
				json.put("exists", "false");
				json.put("exception", e);
				   e.printStackTrace();
				   //out.print(e);
			}
			finally{
				   
			   try {
			    ds.dropConnection();
			   } catch (SQLException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   }
			  }
			  
			//System.out.println(AccountFrom+" : "+AccountTo+" : "+Amount+" : ");
			json.put("success", "true");
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		out.close();
		
	}
	
}
