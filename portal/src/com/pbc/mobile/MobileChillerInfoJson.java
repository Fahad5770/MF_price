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
@WebServlet(description = "Mobile Chiller info json", urlPatterns = { "/mobile/MobileChillerInfoJson" })
public class MobileChillerInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileChillerInfoJson() {
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
			String BarCode = Utilities.filterString(mr.getParameter("barcode"), 1, 100);
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
				Date IssueDate= null;
				String Description="";
				
				String MainAssetNumber="";
				String AssetClass="";
				String Address="";
				String AgreeementRecord="";
				String RNDVerification="";
				String ContactName="";
				String ContactNumber="";
				
				String Lat="";
				String Lng="";
				
				//System.out.println("SELECT * FROM pep.common_assets where barcode='"+BarCode+"'");
				
				ResultSet rs2 = s.executeQuery("SELECT *, co.name as outlet_name, co.lat,co.lng,co.address,co.cache_contact_name,co.cache_contact_number FROM pep.common_assets ca join common_outlets co on co.id=ca.outlet_id  where barcode='"+BarCode+"'");
				if(rs2.first()){
					OutletID = rs2.getLong("outlet_id_parsed");
					OutletName = rs2.getString("outlet_name");
					IssueDate = rs2.getDate("movement_date_parsed");
					Description = rs2.getString("asset_description");
					
					
					 MainAssetNumber=rs2.getString("main_asset_number");
					 AssetClass=rs2.getString("asset_class");
					 Address=rs2.getString("address");
					 AgreeementRecord=rs2.getString("agreement_record");
					 RNDVerification=rs2.getString("rnd_verification");
					 Lat=rs2.getString("lat");
					 Lng=rs2.getString("lng");
					 
					 ContactName=rs2.getString("cache_contact_name");
					 ContactNumber=rs2.getString("cache_contact_number");
					
				}
				
				obj.put("OutletID", OutletID);
				obj.put("OutletName", OutletName);
				obj.put("IssueDate", Utilities.getDisplayDateFormat(IssueDate));
				obj.put("Description", Description);
				
				obj.put("MainAssetNumber", MainAssetNumber);
				obj.put("AssetClass", AssetClass);
				obj.put("Address", Address);
				obj.put("AgreeementRecord", AgreeementRecord);
				obj.put("RNDVerification", RNDVerification);
				obj.put("Lat", Lat);
				obj.put("Lng", Lng);
				
				obj.put("ContactName", ContactName);
				obj.put("ContactNumber", ContactNumber);
				
				
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
