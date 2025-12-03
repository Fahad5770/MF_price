package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(description = "Mobile Sync Research", urlPatterns = { "/mobile/MobileSyncResearch" })
public class MobileSyncResearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncResearch() {
        super();
        // TODO Auto-generated constructor stub
        
        
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
			
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
			long DistributorID = Utilities.parseLong(mr.getParameter("DistributorID"));
			
			String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			
			String DeviceUUID = Utilities.filterString(mr.getParameter("DeviceUUID"), 1, 100);
			//String DevicePlatformVersion = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			
			String OutletBusinessType = Utilities.filterString(mr.getParameter("OutletBusinessType"), 1, 100);
			String OutletAddress = Utilities.filterString(mr.getParameter("OutletAddress"), 1, 100);
			String OutletClosedStatus = Utilities.filterString(mr.getParameter("OutletClosedStatus"), 1, 100);
			String OutletSegment = Utilities.filterString(mr.getParameter("OutletSegment"), 1, 100);
			String OutletTown = Utilities.filterString(mr.getParameter("OutletTown"), 1, 100);
			String OutletDistrict = Utilities.filterString(mr.getParameter("OutletDistrict"), 1, 100);
			String OutletContactPerson = Utilities.filterString(mr.getParameter("OutletContactPerson"), 1, 100);
			String OutletContactNo = Utilities.filterString(mr.getParameter("OutletContactNo"), 1, 100);
			String OutletContactPersonCnic = Utilities.filterString(mr.getParameter("OutletContactPersonCnic"), 1, 100);
			String ClosedDays = Utilities.filterString(mr.getParameter("ClosedDays"), 1, 100);
			String OutletStatus = Utilities.filterString(mr.getParameter("OutletStatus"), 1, 100);
			
			int ChillersCompanyID[] = Utilities.parseInt(mr.getParameterValues("ChillersCompanyID"));
			int ChillersQuantity[] = Utilities.parseInt(mr.getParameterValues("ChillersQuantity"));
			
			int StockAvailabilityCompanyID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityCompanyID"));
			int StockAvailabilityPackageID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityPackageID"));
			int StockAvailabilityTypeID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityTypeID"));
			int StockAvailabilityBrandID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityBrandID"));
			int StockAvailabilityQuantity[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityQuantity"));
			
			int StockAvailabilityBrandsCompanyID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityBrandsCompanyID"));
			int StockAvailabilityBrandsPackageID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityBrandsPackageID"));			
			int StockAvailabilityBrandsBrandID[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityBrandsBrandID"));
			int StockAvailabilityBrandsValue[] = Utilities.parseInt(mr.getParameterValues("StockAvailabilityBrandsValue"));
			
			int SSRBGlassCompanyID[] = Utilities.parseInt(mr.getParameterValues("SSRBGlassCompanyID"));
			int SSRBGlassQuantity[] = Utilities.parseInt(mr.getParameterValues("SSRBGlassQuantity"));
			
			String AgreementStatus = Utilities.filterString(mr.getParameter("AgreementStatus"), 1, 100);
			String AgreementEstimate = Utilities.filterString(mr.getParameter("AgreementEstimate"), 1, 100);			
			String DiscountStatus = Utilities.filterString(mr.getParameter("DiscountStatus"), 1, 100);
			String DiscountEstimate = Utilities.filterString(mr.getParameter("DiscountEstimate"), 1, 100);
			
			String AssetCode[] = Utilities.filterString(mr.getParameterValues("AssetCode"), 1, 100);
			String YearOfIssuance[] = Utilities.filterString(mr.getParameterValues("YearOfIssuance"), 1, 100);
			String Comments[] = Utilities.filterString(mr.getParameterValues("Comments"), 1, 100);
			
			String PepsiChillerSellingCoke = Utilities.filterString(mr.getParameter("PepsiChillerSellingCoke"), 1, 100);
			String PepsiChillerSellingGourmet = Utilities.filterString(mr.getParameter("PepsiChillerSellingGourmet"), 1, 100);
			String CokeChillerSellingPepsi = Utilities.filterString(mr.getParameter("CokeChillerSellingPepsi"), 1, 100);
			String CokeChillerSellingGourmet = Utilities.filterString(mr.getParameter("CokeChillerSellingGourmet"), 1, 100);
			String GourmetChillerSellingPepsi = Utilities.filterString(mr.getParameter("GourmetChillerSellingPepsi"), 1, 100);
			String GourmetChillerSellingCoke = Utilities.filterString(mr.getParameter("GourmetChillerSellingCoke"), 1, 100);
			String ChillerAirPercentage = Utilities.filterString(mr.getParameter("ChillerAirPercentage"), 1, 100);
			String ChillerOutOfStock = Utilities.filterString(mr.getParameter("ChillerOutOfStock"), 1, 100);
			String OutOfOrder = Utilities.filterString(mr.getParameter("OutOfOrder"), 1, 100);
			
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String MarketWatchID = Utilities.filterString(mr.getParameter("MarketWatchID"), 1, 100);
			
			//System.out.println("Rate = "+Rate);
			
			
			Datasource ds = new Datasource();
			
			try{
				
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				boolean isAlreadyEntered = false;
				ResultSet rs2 = s.executeQuery("select outlet_id from mrd_research where mobile_research_id="+MarketWatchID);
				if(rs2.first()){
					isAlreadyEntered = true;
				}
				
				if(!isAlreadyEntered){
					
					String OutletIDVal = OutletID+"";
					if(OutletID == 0){
						OutletIDVal = null;
					}
					
					
					System.out.println("replace INTO `mrd_research`(`mobile_research_id`, `mobile_uuid`,`outlet_id`,`outlet_name`,`created_on`,`created_on_mobile`,`created_by`,`lat`,`lng`, outlet_business_type, outlet_address, outlet_segment, outlet_district, outlet_contact_person, outlet_contact_no, outlet_contact_person_cnic, closed_days, outlet_status, agreement_status, agreement_estimate_id, discount_status, discount_estimate_id, distributor_id, outlet_closed_status, pepsi_chiller_selling_coke, pepsi_chiller_selling_gourmet, coke_chiller_selling_pepsi, coke_chiller_selling_gourmet, gourmet_chiller_selling_pepsi, gourmet_chiller_selling_coke, chiller_air_percentage, chiller_out_of_stock, out_of_order)VALUES("+MarketWatchID+", '"+DeviceUUID+"',"+OutletIDVal+",'"+OutletName+"', now(),'"+MobileTimestamp+"',"+MobileUserID+","+Lat+","+Lng+", '"+OutletBusinessType+"', '"+OutletAddress+"', '"+OutletSegment+"', '"+OutletDistrict+"', '"+OutletContactPerson+"', '"+OutletContactNo+"', '"+OutletContactPersonCnic+"', '"+ClosedDays+"', '"+OutletStatus+"', '"+AgreementStatus+"', '"+AgreementEstimate+"', '"+DiscountStatus+"', '"+DiscountEstimate+"', "+DistributorID+", "+OutletClosedStatus+", "+PepsiChillerSellingCoke+", "+PepsiChillerSellingGourmet+", "+CokeChillerSellingPepsi+", "+CokeChillerSellingGourmet+", "+GourmetChillerSellingPepsi+", "+GourmetChillerSellingCoke+", "+ChillerAirPercentage+", "+ChillerOutOfStock+", "+OutOfOrder+")");
					s.executeUpdate("replace INTO `mrd_research`(`mobile_research_id`, `mobile_uuid`,`outlet_id`,`outlet_name`,`created_on`,`created_on_mobile`,`created_by`,`lat`,`lng`, outlet_business_type, outlet_address, outlet_segment, outlet_district, outlet_contact_person, outlet_contact_no, outlet_contact_person_cnic, closed_days, outlet_status, agreement_status, agreement_estimate_id, discount_status, discount_estimate_id, distributor_id, outlet_closed_status, pepsi_chiller_selling_coke, pepsi_chiller_selling_gourmet, coke_chiller_selling_pepsi, coke_chiller_selling_gourmet, gourmet_chiller_selling_pepsi, gourmet_chiller_selling_coke, chiller_air_percentage, chiller_out_of_stock, out_of_order)VALUES("+MarketWatchID+", '"+DeviceUUID+"',"+OutletIDVal+",'"+OutletName+"', now(),'"+MobileTimestamp+"',"+MobileUserID+","+Lat+","+Lng+", '"+OutletBusinessType+"', '"+OutletAddress+"', '"+OutletSegment+"', '"+OutletDistrict+"', '"+OutletContactPerson+"', '"+OutletContactNo+"', '"+OutletContactPersonCnic+"', '"+ClosedDays+"', '"+OutletStatus+"', '"+AgreementStatus+"', '"+AgreementEstimate+"', '"+DiscountStatus+"', '"+DiscountEstimate+"', "+DistributorID+", "+OutletClosedStatus+", "+PepsiChillerSellingCoke+", "+PepsiChillerSellingGourmet+", "+CokeChillerSellingPepsi+", "+CokeChillerSellingGourmet+", "+GourmetChillerSellingPepsi+", "+GourmetChillerSellingCoke+", "+ChillerAirPercentage+", "+ChillerOutOfStock+", "+OutOfOrder+")");
					
					long MarketResearchID = 0;
					ResultSet rs = s.executeQuery("select last_insert_id()");
					if(rs.first()){
						MarketResearchID = rs.getLong(1);
					}
					
					if(ChillersCompanyID != null){
						for(int i = 0; i < ChillersCompanyID.length; i++){
							if(ChillersQuantity[i] > 0){
								s.executeUpdate("INSERT INTO `mrd_research_company_chillers`(`id`,`company_id`,`no_of_chillers`)VALUES("+MarketResearchID+", "+ChillersCompanyID[i]+", "+ChillersQuantity[i]+")");
							}
						}
					}
					
					if(StockAvailabilityCompanyID != null){
						for(int i = 0; i < StockAvailabilityCompanyID.length; i++){
							if(StockAvailabilityQuantity[i] > 0){
								s.executeUpdate("INSERT INTO `mrd_research_stock_availability`(`id`,`company_id`,`package_id`, `stock_type_id`,`brand_id`,`quantity`)VALUES("+MarketResearchID+", "+StockAvailabilityCompanyID[i]+", "+StockAvailabilityPackageID[i]+", "+StockAvailabilityTypeID[i]+", "+StockAvailabilityBrandID[i]+", "+StockAvailabilityQuantity[i]+")");
							}
						}
					}
					
					if(StockAvailabilityBrandsCompanyID != null){
						for(int i = 0; i < StockAvailabilityBrandsCompanyID.length; i++){
							if(StockAvailabilityBrandsValue[i] > 0){
								s.executeUpdate("INSERT INTO `mrd_research_stock_availability_brands`(`id`,`company_id`,`package_id`,`brand_id`,`is_available`)VALUES("+MarketResearchID+", "+StockAvailabilityBrandsCompanyID[i]+", "+StockAvailabilityBrandsPackageID[i]+", "+StockAvailabilityBrandsBrandID[i]+", "+StockAvailabilityBrandsValue[i]+")");
							}
						}
					}
					
					if(SSRBGlassCompanyID != null){
						for(int i = 0; i < SSRBGlassCompanyID.length; i++){
							if(SSRBGlassQuantity[i] > 0){
								s.executeUpdate("INSERT INTO `mrd_research_stock_ssrb`(`id`,`company_id`,`quantity`)VALUES("+MarketResearchID+", "+SSRBGlassCompanyID[i]+", "+SSRBGlassQuantity[i]+")");
							}
						}
					}
					
					if(AssetCode != null){
						for(int i = 0; i < AssetCode.length; i++){
							s.executeUpdate("INSERT INTO `mrd_research_tot`(`id`,`asset_code`,`year_of_issuance`,`comments`)VALUES("+MarketResearchID+",'"+AssetCode[i]+"','"+YearOfIssuance[i]+"','"+Comments[i]+"')");
						}
					}
				}
				
				s.close();
				ds.commit();
				json.put("success", "true");
				
				
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
			
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
