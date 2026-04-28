package com.pbc.crm;

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
@WebServlet(description = "Mobile Sync Research", urlPatterns = { "/crm/ResearchReviewExecute" })
public class ResearchReviewExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResearchReviewExecute() {
        super();
        // TODO Auto-generated constructor stub
        
        
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		try{
			
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
			
			int OutletID = Utilities.parseInt(request.getParameter("OutletID"));
			long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
			long EditID = Utilities.parseLong(request.getParameter("EditID"));
			
			String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
			
			String DeviceUUID = Utilities.filterString(request.getParameter("DeviceUUID"), 1, 100);
			
			String OutletBusinessType = Utilities.filterString(request.getParameter("OutletBusinessType"), 1, 100);
			String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 100);
			String OutletClosedStatus = Utilities.filterString(request.getParameter("OutletClosedStatus"), 1, 100);
			String OutletSegment = Utilities.filterString(request.getParameter("OutletSegment"), 1, 100);
			String OutletTown = Utilities.filterString(request.getParameter("OutletTown"), 1, 100);
			String OutletDistrict = Utilities.filterString(request.getParameter("OutletDistrict"), 1, 100);
			String OutletContactPerson = Utilities.filterString(request.getParameter("OutletContactPerson"), 1, 100);
			String OutletContactNo = Utilities.filterString(request.getParameter("OutletContactNo"), 1, 100);
			String OutletContactPersonCnic = Utilities.filterString(request.getParameter("OutletContactPersonCnic"), 1, 100);
			String ClosedDays = Utilities.filterString(request.getParameter("ClosedDays"), 1, 100);
			String OutletStatus = Utilities.filterString(request.getParameter("OutletStatus"), 1, 100);
			
			int ChillersCompanyID[] = Utilities.parseInt(request.getParameterValues("ChillersCompanyIDHidden"));
			int ChillersQuantity[] = Utilities.parseInt(request.getParameterValues("ChillersQuantity"));
			
			int StockAvailabilityCompanyID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityCompanyIDHidden"));
			int StockAvailabilityPackageID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityPackageIDHidden"));
			int StockAvailabilityTypeID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityStockTypeIDHidden"));
			int StockAvailabilityBrandID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityBrandIDHidden"));
			int StockAvailabilityQuantity[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityQuantity"));
			
			int StockAvailabilityBrandsCompanyID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityBrandsCompanyIDHidden"));
			int StockAvailabilityBrandsPackageID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityBrandsPackageIDHidden"));			
			int StockAvailabilityBrandsBrandID[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityBrandsBrandIDHidden"));
			int StockAvailabilityBrandsValue[] = Utilities.parseInt(request.getParameterValues("StockAvailabilityBrandsValueHidden"));
			
			int SSRBGlassCompanyID[] = Utilities.parseInt(request.getParameterValues("SSRBGlassCompanyIDHidden"));
			int SSRBGlassQuantity[] = Utilities.parseInt(request.getParameterValues("SSRBGlassQuantity"));
			
			String AgreementStatus = Utilities.filterString(request.getParameter("AgreementStatus"), 1, 100);
			String AgreementEstimate = Utilities.filterString(request.getParameter("AgreementEstimate"), 1, 100);			
			String DiscountStatus = Utilities.filterString(request.getParameter("DiscountStatus"), 1, 100);
			String DiscountEstimate = Utilities.filterString(request.getParameter("DiscountEstimate"), 1, 100);
			
			String AssetCode[] = Utilities.filterString(request.getParameterValues("AssetCode"), 1, 100);
			String YearOfIssuance[] = Utilities.filterString(request.getParameterValues("YearOfIssuance"), 1, 100);
			String Comments[] = Utilities.filterString(request.getParameterValues("Comments"), 1, 100);
			
			String PepsiChillerSellingCoke = Utilities.filterString(request.getParameter("PepsiChillerSellingCoke"), 1, 100);
			String PepsiChillerSellingGourmet = Utilities.filterString(request.getParameter("PepsiChillerSellingGourmet"), 1, 100);
			String CokeChillerSellingPepsi = Utilities.filterString(request.getParameter("CokeChillerSellingPepsi"), 1, 100);
			String CokeChillerSellingGourmet = Utilities.filterString(request.getParameter("CokeChillerSellingGourmet"), 1, 100);
			String GourmetChillerSellingPepsi = Utilities.filterString(request.getParameter("GourmetChillerSellingPepsi"), 1, 100);
			String GourmetChillerSellingCoke = Utilities.filterString(request.getParameter("GourmetChillerSellingCoke"), 1, 100);
			String ChillerAirPercentage = Utilities.filterString(request.getParameter("ChillerAirPercentage"), 1, 100);
			String ChillerOutOfStock = Utilities.filterString(request.getParameter("ChillerOutOfStock"), 1, 100);
			String OutOfOrder = Utilities.filterString(request.getParameter("OutOfOrder"), 1, 100);
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				ResultSet rs = s.executeQuery("select * from mrd_research where id="+EditID);
				if(rs.first()){
					s2.executeUpdate("INSERT INTO `mrd_research_log`(`mrd_research_id`,`mobile_uuid`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_category`,`distributor_id`,`created_on`,`created_on_mobile`,`created_by`,`lat`,`lng`,`outlet_business_type`,`outlet_segment`,`outlet_town`,`outlet_district`,`outlet_contact_person`,`outlet_contact_no`,`outlet_contact_person_cnic`,`closed_days`,`outlet_status`,`agreement_status`,`agreement_estimate_id`,`discount_status`,`discount_estimate_id`,`updated_on`,`updated_by`, outlet_closed_status, pepsi_chiller_selling_coke, pepsi_chiller_selling_gourmet, coke_chiller_selling_pepsi, coke_chiller_selling_gourmet, gourmet_chiller_selling_pepsi, gourmet_chiller_selling_coke, chiller_air_percentage, chiller_out_of_stock, out_of_order)VALUES("+rs.getString("id")+",'"+rs.getString("mobile_uuid")+"',"+rs.getString("outlet_id")+",'"+rs.getString("outlet_name")+"','"+rs.getString("outlet_address")+"',"+rs.getString("outlet_category")+","+rs.getString("distributor_id")+",'"+rs.getString("created_on")+"','"+rs.getString("created_on_mobile")+"',"+rs.getString("created_by")+","+rs.getString("lat")+","+rs.getString("lng")+",'"+rs.getString("outlet_business_type")+"','"+rs.getString("outlet_segment")+"','"+rs.getString("outlet_town")+"','"+rs.getString("outlet_district")+"','"+rs.getString("outlet_contact_person")+"','"+rs.getString("outlet_contact_no")+"','"+rs.getString("outlet_contact_person_cnic")+"','"+rs.getString("closed_days")+"','"+rs.getString("outlet_status")+"',"+rs.getString("agreement_status")+","+rs.getString("agreement_estimate_id")+","+rs.getString("discount_status")+","+rs.getString("discount_estimate_id")+", now(), "+UserID+", "+rs.getString("outlet_closed_status")+", "+rs.getString("pepsi_chiller_selling_coke")+", "+rs.getString("pepsi_chiller_selling_gourmet")+", "+rs.getString("coke_chiller_selling_pepsi")+", "+rs.getString("coke_chiller_selling_gourmet")+", "+rs.getString("gourmet_chiller_selling_pepsi")+", "+rs.getString("gourmet_chiller_selling_coke")+", "+rs.getString("chiller_air_percentage")+", "+rs.getString("chiller_out_of_stock")+", "+rs.getString("out_of_order")+")");
				}
				
				long MarketResearchLogID = 0;
				ResultSet rs2 = s.executeQuery("select last_insert_id()");
				if(rs2.first()){
					MarketResearchLogID = rs2.getLong(1);
				}
				
				ResultSet rs3 = s.executeQuery("select * from mrd_research_company_chillers where id="+EditID);
				while(rs3.next()){
					s2.executeUpdate("INSERT INTO `mrd_research_company_chillers_log`(`id`,`mrd_research_id`,`company_id`,`no_of_chillers`)VALUES("+MarketResearchLogID+","+rs3.getString("id")+","+rs3.getString("company_id")+","+rs3.getString("no_of_chillers")+")");
				}
				
				ResultSet rs4 = s.executeQuery("select * from mrd_research_stock_availability where id="+EditID);
				while(rs4.next()){
					s2.executeUpdate("INSERT INTO `mrd_research_stock_availability_log`(`id`,`mrd_research_id`,`company_id`,`package_id`,`brand_id`,`quantity`, `stock_type_id`)VALUES("+MarketResearchLogID+","+rs4.getString("id")+","+rs4.getString("company_id")+","+rs4.getString("package_id")+","+rs4.getString("brand_id")+","+rs4.getString("quantity")+", "+rs4.getString("stock_type_id")+")");
				}
				
				ResultSet rs7 = s.executeQuery("select * from mrd_research_stock_availability_brands where id="+EditID);
				while(rs7.next()){
					s2.executeUpdate("INSERT INTO `mrd_research_stock_availability_brands_log`(`id`, `mrd_research_id`,`company_id`,`package_id`,`brand_id`,`is_available`)VALUES("+MarketResearchLogID+", "+rs7.getString("id")+", "+rs7.getString("company_id")+", "+rs7.getString("package_id")+", "+rs7.getString("brand_id")+", "+rs7.getString("is_available")+")");
				}
				
				ResultSet rs5 = s.executeQuery("select * from mrd_research_stock_ssrb where id="+EditID);
				while(rs5.next()){
					s2.executeUpdate("INSERT INTO `mrd_research_stock_ssrb_log`(`id`,`mrd_research_id`,`company_id`,`quantity`)VALUES("+MarketResearchLogID+","+rs5.getString("id")+","+rs5.getString("company_id")+","+rs5.getString("quantity")+")");
				}
				
				ResultSet rs6 = s.executeQuery("select * from mrd_research_tot where id="+EditID);
				while(rs6.next()){
					s2.executeUpdate("INSERT INTO `mrd_research_tot_log`(`id`,`mrd_research_id`,`asset_code`,`year_of_issuance`,`comments`)VALUES("+MarketResearchLogID+","+rs6.getString("id")+",'"+rs6.getString("asset_code")+"','"+rs6.getString("year_of_issuance")+"','"+rs6.getString("comments")+"')");
				}
				
				String Sql = "UPDATE `mrd_research` SET `outlet_id` = "+OutletID+",`outlet_name` = '"+OutletName+"',`outlet_address` = '"+OutletAddress+"',`distributor_id` = "+DistributorID+", `outlet_business_type` = '"+OutletBusinessType+"', `outlet_segment` = '"+OutletSegment+"', `outlet_contact_person` = '"+OutletContactPerson+"', `outlet_contact_no` = '"+OutletContactNo+"', `outlet_contact_person_cnic` = '"+OutletContactPersonCnic+"', `closed_days` = '"+ClosedDays+"', `outlet_status` = '"+OutletStatus+"', `agreement_status` = "+AgreementStatus+", `agreement_estimate_id` = "+AgreementEstimate+",`discount_status` = "+DiscountStatus+",`discount_estimate_id` = "+DiscountEstimate+", outlet_closed_status = "+OutletClosedStatus+", pepsi_chiller_selling_coke = "+PepsiChillerSellingCoke+", pepsi_chiller_selling_gourmet = "+PepsiChillerSellingGourmet+", coke_chiller_selling_pepsi = "+CokeChillerSellingPepsi+", coke_chiller_selling_gourmet = "+CokeChillerSellingGourmet+", gourmet_chiller_selling_pepsi = "+GourmetChillerSellingPepsi+", gourmet_chiller_selling_coke = "+GourmetChillerSellingCoke+", chiller_air_percentage = "+ChillerAirPercentage+", chiller_out_of_stock = "+ChillerOutOfStock+", out_of_order = "+OutOfOrder+" WHERE `id` = "+EditID;
				//System.out.println(Sql);
				s.executeUpdate(Sql);
				
				long MarketResearchID = EditID;
				
				s.executeUpdate("delete from mrd_research_company_chillers where id="+MarketResearchID);
				s.executeUpdate("delete from mrd_research_stock_availability where id="+MarketResearchID);
				s.executeUpdate("delete from mrd_research_stock_availability_brands where id="+MarketResearchID);
				s.executeUpdate("delete from mrd_research_stock_ssrb where id="+MarketResearchID);
				s.executeUpdate("delete from mrd_research_tot where id="+MarketResearchID);
				
				if(ChillersCompanyID != null){
					for(int i = 0; i < ChillersCompanyID.length; i++){
						//if(ChillersQuantity[i] > 0){
							s.executeUpdate("INSERT INTO `mrd_research_company_chillers`(`id`,`company_id`,`no_of_chillers`)VALUES("+MarketResearchID+", "+ChillersCompanyID[i]+", "+ChillersQuantity[i]+")");
						//}
					}
				}
				
				if(StockAvailabilityCompanyID != null){
					for(int i = 0; i < StockAvailabilityCompanyID.length; i++){
						//if(StockAvailabilityQuantity[i] > 0){
							s.executeUpdate("INSERT INTO `mrd_research_stock_availability`(`id`,`company_id`,`package_id`,`brand_id`,`quantity`, `stock_type_id`)VALUES("+MarketResearchID+", "+StockAvailabilityCompanyID[i]+", "+StockAvailabilityPackageID[i]+", "+StockAvailabilityBrandID[i]+", "+StockAvailabilityQuantity[i]+", "+StockAvailabilityTypeID[i]+")");
						//}
					}
				}
				
				if(StockAvailabilityBrandsValue != null){
					for(int i = 0; i < StockAvailabilityBrandsValue.length; i++){
						if(StockAvailabilityBrandsValue[i] > 0){							
							s.executeUpdate("INSERT INTO `mrd_research_stock_availability_brands`(`id`,`company_id`,`package_id`,`brand_id`,`is_available`)VALUES("+MarketResearchID+", "+StockAvailabilityBrandsCompanyID[i]+", "+StockAvailabilityBrandsPackageID[i]+", "+StockAvailabilityBrandsBrandID[i]+", "+StockAvailabilityBrandsValue[i]+")");
						}
					}
				}
				
				if(SSRBGlassCompanyID != null){
					for(int i = 0; i < SSRBGlassCompanyID.length; i++){
						//if(SSRBGlassQuantity[i] > 0){
							s.executeUpdate("INSERT INTO `mrd_research_stock_ssrb`(`id`,`company_id`,`quantity`)VALUES("+MarketResearchID+", "+SSRBGlassCompanyID[i]+", "+SSRBGlassQuantity[i]+")");
						//}
					}
				}
				
				if(AssetCode != null){
					for(int i = 0; i < AssetCode.length; i++){
						s.executeUpdate("INSERT INTO `mrd_research_tot`(`id`,`asset_code`,`year_of_issuance`,`comments`)VALUES("+MarketResearchID+",'"+AssetCode[i]+"','"+YearOfIssuance[i]+"','"+Comments[i]+"')");
					}
				}
				
				
				s2.close();
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
			
		
		
		out.print(json);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
