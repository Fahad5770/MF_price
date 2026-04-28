package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.common.Distributor;
import com.pbc.util.UserAccess;


@WebServlet(description = "Get Outlet Master Information in JSON", urlPatterns = { "/outlet/GetOutletInfoJsonforTax" })
public class GetOutletInfoJsonforTax extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private long SessionUserID;
    public long OutletID;
    public int FeatureID;
    
    public GetOutletInfoJsonforTax() {
        super();
    }

    
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		response.setContentType("application/json");
		JSONObject obj=new JSONObject();
		
		PrintWriter out = response.getWriter();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		
		
		
		this.SessionUserID = Utilities.parseLong(session.getAttribute("UserID").toString());
		this.OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		this.FeatureID = Utilities.parseInt(request.getParameter("FeatureID"));
		//System.out.println("SessionUserID"+SessionUserID+"OutletID"+OutletID+"FeatureID"+FeatureID);
		try {
			
			if( this.FeatureID == 0 ){
				obj.put("success", "false");
			}else{
				if(Utilities.isAuthorized(this.FeatureID, this.SessionUserID) == false){
					obj.put("success", "false");
				}else{
					obj = processRequest();
				}
			
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		out.print(obj);
		out.close();
		
		
	}
	
	public JSONObject processRequest(){
		
		
		JSONObject obj=new JSONObject();
		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();
		JSONArray jr3 = new JSONArray();
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			String UserAccessWhere = "";
			if(this.FeatureID != 0)
			{
				Distributor [] DistributorObj = UserAccess.getUserFeatureDistributor(this.SessionUserID, this.FeatureID);
				String DistributorsIds = UserAccess.getDistributorQueryString(DistributorObj);
				if(!DistributorsIds.equals("")){
					UserAccessWhere = " and distributor_id in("+DistributorsIds+")";
				}
			}
			
			String SQL = "SELECT name, address, region_id, (select region_short_name from common_regions where region_id=common_outlets.region_id) region_name, lat, lng, distributor_id, (select name from common_distributors where distributor_id=common_outlets.distributor_id ) distributor_name, channel_id, nfc_tag_id, sap_customer_id, segment_id, agreed_daily_average_sales, vpo_classifications_id,account_number_bank_alfalah,discount_disbursement_id,is_filer,is_register,STN,NTN FROM common_outlets where id = "+this.OutletID+UserAccessWhere;
			System.out.println(SQL);
			ResultSet rs = s.executeQuery(SQL);
			
			if (rs.first()){
				
				obj.put("success", "true");
				
				ResultSet rs2 = s2.executeQuery("SELECT id, contact_name, contact_number, contact_nic, type_id, (SELECT label FROM common_outlets_contacts_types where id=5) relation FROM common_outlets_contacts where outlet_id="+this.OutletID+" and type_id=5 and is_primary=1");
				while(rs2.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ContactID", rs2.getString("id"));
					rows.put("ContactName", rs2.getString("contact_name"));
					rows.put("ContactNumber", rs2.getString("contact_number"));
					rows.put("ContactNIC", rs2.getString("contact_nic"));
					rows.put("ContactRelation", rs2.getString("relation"));
					
					
					jr.add(rows);
				}
				obj.put("ContactRows", jr);
				/*
				ResultSet rs21 = s2.executeQuery("SELECT distributor_id, (select name from common_distributors where distributor_id=common_outlets_distributors.distributor_id) distributor_name from common_outlets_distributors where outlet_id="+this.OutletID);
				while(rs21.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("DistributorID", rs21.getString("distributor_id"));
					rows.put("DistributorName", rs21.getString("distributor_name"));
					
					jr2.add(rows);
				}
				obj.put("DistributorRows", jr2);
				*/
				
				ResultSet rs21 = s2.executeQuery("SELECT distinct dbp.id, dbp.label, dbp.distributor_id, co.name distributor_name, co.product_group_id, epg.product_group_name FROM distributor_beat_plan dbp, distributor_beat_plan_schedule dbps, common_distributors co, employee_product_groups epg where dbp.id=dbps.id and dbp.distributor_id=co.distributor_id and co.product_group_id=epg.product_group_id and dbps.outlet_id="+this.OutletID);
				while(rs21.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("PJPID", rs21.getString("id"));
					rows.put("PJPName", rs21.getString("label"));
					
					rows.put("DistributorID", rs21.getString("distributor_id"));
					rows.put("DistributorName", rs21.getString("distributor_name"));
					
					rows.put("ProductGroupID", rs21.getString("product_group_id"));
					rows.put("ProductGroupName", rs21.getString("product_group_name"));
					
					jr2.add(rows);
				}
				obj.put("PJPRows", jr2);
				
				
				
				ResultSet rs22 = s2.executeQuery("SELECT * FROM common_outlets_contacts_sms where outlet_id="+this.OutletID);
				if(rs22.first()){
					//System.out.println(rs22.getString("mobile_no"));
					obj.put("SMSNumber", rs22.getString("mobile_no"));
				}
				obj.put("SMSRows", jr3);
				
				obj.put("OutletName", rs.getString("name"));
				obj.put("Address", rs.getString("address"));
				obj.put("RegionID", rs.getString("region_id"));
				obj.put("RegionName", rs.getString("region_name"));
				
				obj.put("Lat", rs.getString("lat"));
				obj.put("Lng", rs.getString("lng"));
				
				obj.put("PrimaryDistributorID", rs.getString("distributor_id"));
				obj.put("PrimaryDistributorName", rs.getString("distributor_name"));
				obj.put("ChannelID", rs.getString("channel_id"));
				obj.put("NFCTagID", rs.getString("nfc_tag_id"));
				obj.put("SAPCustomerID", rs.getString("sap_customer_id"));
				obj.put("SegmentID", rs.getString("segment_id"));
				obj.put("AgreedDailyAvgSales", rs.getString("agreed_daily_average_sales"));
				obj.put("VPOClassificationsID", rs.getString("vpo_classifications_id"));
				obj.put("accountnumberbankalfalah", rs.getString("account_number_bank_alfalah"));
				obj.put("DiscountDisbursementID", rs.getString("discount_disbursement_id"));
				obj.put("isFiler", rs.getInt("is_filer"));
				obj.put("isRegister", rs.getInt("is_register"));
				obj.put("StnID", rs.getString("STN"));
				obj.put("NtnID", rs.getString("NTN"));
				
			}else{
				obj.put("success", "false");
			}
			
			s2.close();
			s.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return obj;
	}
	
	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}
	
}
