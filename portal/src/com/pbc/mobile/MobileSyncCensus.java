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
@WebServlet(description = "Mobile Sync Census", urlPatterns = { "/mobile/MobileSyncCensus" })
public class MobileSyncCensus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncCensus() {
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
		
			//System.out.println("I am here, servelet called - Sync!!! ");
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		//if (!mr.isExpired()){
			
			
			
			
			
			long MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			String DeviceUUID = Utilities.filterString(mr.getParameter("DeviceUUID"), 1, 100);
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String MobileCensusID = Utilities.filterString(mr.getParameter("CensusID"), 1, 100);
			
			
			long OutletID = Utilities.parseLong(mr.getParameter("OutletID"));
			
			 
			 String OutletName=Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			 String OutletBoardName=Utilities.filterString(mr.getParameter("OutletBoardName"), 1, 100); 
			 int CRegionID = Utilities.parseInt(mr.getParameter("CRegionID"));
			 long CDistirubtorID =Utilities.parseInt(mr.getParameter("CDistirubtorID"));
			 String CDistributorName = Utilities.filterString(mr.getParameter("CDistributorName"), 1, 100);
			 int CArea =Utilities.parseInt(mr.getParameter("CArea"));
			 int CShopType =Utilities.parseInt(mr.getParameter("CShopType"));
			 String CShopAddress= Utilities.filterString(mr.getParameter("CShopAddress"), 1, 100);
			 String CDistributorTown= Utilities.filterString(mr.getParameter("CDistributorTown"), 1, 100);
			 int CDistributorTehsil =Utilities.parseInt(mr.getParameter("CDistributorTehsil"));
			 int CDistributorDistrict =Utilities.parseInt(mr.getParameter("CDistributorDistrict"));
			 String CLandMark=Utilities.filterString(mr.getParameter("CLandMark"), 1, 100);
			 
			 String OutletNameActual=Utilities.filterString(mr.getParameter("Coutlet_name_actual"), 1, 100);
			 
			 
			 
			 
			 int CShopClosed=Utilities.parseInt(mr.getParameter("CShopClosed"));
			 int CShopClosedStatus=Utilities.parseInt(mr.getParameter("CShopClosedStatus"));
			 int CDayOff=Utilities.parseInt(mr.getParameter("CDayOff"));
			 int CPartiallyTiming=Utilities.parseInt(mr.getParameter("CPartiallyTiming"));
			 int CSegment=Utilities.parseInt(mr.getParameter("CSegment"));
			 int CShopLocation=Utilities.parseInt(mr.getParameter("CShopLocation"));
			 String CContactPerson=Utilities.filterString(mr.getParameter("CContactPerson"), 1, 100);
			 String CContact1=Utilities.filterString(mr.getParameter("CContact1"), 1, 100);
			 String CContact2=Utilities.filterString(mr.getParameter("CContact2"), 1, 100);
			 String CCnic1=Utilities.filterString(mr.getParameter("CCnic1"), 1, 100);
			 String CContactRelation=Utilities.filterString(mr.getParameter("CContactRelation"), 1, 100);
			 String CRelConName=Utilities.filterString(mr.getParameter("CRelConName"), 1, 100);
			 String CRelCon3=Utilities.filterString(mr.getParameter("CRelCon3"), 1, 100);
			 String CRelCon2=Utilities.filterString(mr.getParameter("CRelCon2"), 1, 100);
			 String CFacebookID=Utilities.filterString(mr.getParameter("CFacebookID"), 1, 100);
			 String CEmailID=Utilities.filterString(mr.getParameter("CEmailID"), 1, 100);
			 int CFeedbackStock1=Utilities.parseInt(mr.getParameter("CFeedbackStock1"));
			 int CFeedbackStock2=Utilities.parseInt(mr.getParameter("CFeedbackStock2"));
			 int CFeedbackStock3=Utilities.parseInt(mr.getParameter("CFeedbackStock3"));
			 int CFinancialService1=Utilities.parseInt(mr.getParameter("CFinancialService1"));
			 int CFinancialService2=Utilities.parseInt(mr.getParameter("CFinancialService2"));
			 int CFinancialService3=Utilities.parseInt(mr.getParameter("CFinancialService3"));
			 int CFinancialService4=Utilities.parseInt(mr.getParameter("CFinancialService4"));
			 int CFinancialService5=Utilities.parseInt(mr.getParameter("CFinancialService5"));
			 int CFinancialService6=Utilities.parseInt(mr.getParameter("CFinancialService6"));
			 String CShopOPeningTime=Utilities.filterString(mr.getParameter("CShopOPeningTime"), 1, 100);
			 String CShopeClosingTime=Utilities.filterString(mr.getParameter("CShopeClosingTime"), 1, 100);
			 
			 int CFeedbackStock4=Utilities.parseInt(mr.getParameter("CFeedbackStock4"));
			 String CFeedbackStockPer1=Utilities.filterString(mr.getParameter("CFeedbackStockPer1"), 1, 100);
			 String CFeedbackStockPer2=Utilities.filterString(mr.getParameter("CFeedbackStockPer2"), 1, 100);
			 String CFeedbackStockPer3=Utilities.filterString(mr.getParameter("CFeedbackStockPer3"), 1, 100);
			 String CFeedbackStockPer4=Utilities.filterString(mr.getParameter("CFeedbackStockPer4"), 1, 100);
			 
			 
			 
			 int CShopStatus=Utilities.parseInt(mr.getParameter("CShopStatus"));
			 int CShopEstablishmentHistory=Utilities.parseInt(mr.getParameter("CShopEstablishmentHistory"));
			 int CBusinessStructure=Utilities.parseInt(mr.getParameter("CBusinessStructure"));
			 int CShopPotential=Utilities.parseInt(mr.getParameter("CShopPotential"));
			 int CCustomerType=Utilities.parseInt(mr.getParameter("CCustomerType"));
			 int CCustomerTypeWholeSale=Utilities.parseInt(mr.getParameter("CCustomerTypeWholeSale"));
			 int CCustomerTypeRetailer=Utilities.parseInt(mr.getParameter("CCustomerTypeRetailer"));
			 int CServiceType=Utilities.parseInt(mr.getParameter("CServiceType"));
			 int CTurnOver=Utilities.parseInt(mr.getParameter("CTurnOver"));
			 int CTraderChannel=Utilities.parseInt(mr.getParameter("CTraderChannel"));
			 int CTraderSubChannel=Utilities.parseInt(mr.getParameter("CTraderSubChannel"));
			
			
			//Beverages Tab
			 
			 
			 	int BCSD = Utilities.parseInt(mr.getParameter("BCSD"));
			 	int BSting = Utilities.parseInt(mr.getParameter("BSting"));
				int BKo = Utilities.parseInt(mr.getParameter("BKo"));
				int BSupplyFrequency = Utilities.parseInt(mr.getParameter("BSupplyFrequency"));
				int Bagreement1 = Utilities.parseInt(mr.getParameter("Bagreement1"));
				int Bagreement2 = Utilities.parseInt(mr.getParameter("Bagreement2"));
				int Bagreement3 = Utilities.parseInt(mr.getParameter("Bagreement3"));
				int Bagreement4 = Utilities.parseInt(mr.getParameter("Bagreement4"));
				int Bagreement5 = Utilities.parseInt(mr.getParameter("Bagreement5"));
				int BDiscountStatus1 = Utilities.parseInt(mr.getParameter("BDiscountStatus1"));
				int BDiscountStatus2 = Utilities.parseInt(mr.getParameter("BDiscountStatus2"));
				int BDiscountStatus3 = Utilities.parseInt(mr.getParameter("BDiscountStatus3"));
				int BCoolerVisibility = Utilities.parseInt(mr.getParameter("BCoolerVisibility"));
				int BPepsiPublicityMaterial1 = Utilities.parseInt(mr.getParameter("BPepsiPublicityMaterial1"));
				int BPepsiPublicityMaterial2 = Utilities.parseInt(mr.getParameter("BPepsiPublicityMaterial2"));
				int BPepsiPublicityMaterial3 = Utilities.parseInt(mr.getParameter("BPepsiPublicityMaterial3"));
				int BPepsiPublicityMaterial4 = Utilities.parseInt(mr.getParameter("BPepsiPublicityMaterial4"));
				int BPepsiPublicityMaterial5 = Utilities.parseInt(mr.getParameter("BPepsiPublicityMaterial5"));
				int BCokePublicityMaterial1 = Utilities.parseInt(mr.getParameter("BCokePublicityMaterial1"));
				int BCokePublicityMaterial2 = Utilities.parseInt(mr.getParameter("BCokePublicityMaterial2"));
				int BCokePublicityMaterial3 = Utilities.parseInt(mr.getParameter("BCokePublicityMaterial3"));
				int BCokePublicityMaterial4 = Utilities.parseInt(mr.getParameter("BCokePublicityMaterial4"));
				int BCokePublicityMaterial5 = Utilities.parseInt(mr.getParameter("BCokePublicityMaterial5"));
			
				int BCashMachineStatus = Utilities.parseInt(mr.getParameter("BCashMachineStatus"));
				int BCashMachineQuantity = Utilities.parseInt(mr.getParameter("BCashMachineQuantity"));
				
				int CStockStorageLoc1 = Utilities.parseInt(mr.getParameter("CStockStorageLoc1"));
				int CStockStorageLoc2 = Utilities.parseInt(mr.getParameter("CStockStorageLoc2"));
				int CStockStorageLoc3 = Utilities.parseInt(mr.getParameter("CStockStorageLoc3"));
				int CStockStorageLoc4 = Utilities.parseInt(mr.getParameter("CStockStorageLoc4"));
				
			
				// New Changes - 29/11/2016 //////
				
			int Ncensus_outlet_type = Utilities.parseInt(mr.getParameter("Ncensus_outlet_type"));
			int Ncensus_shop_structure = Utilities.parseInt(mr.getParameter("Ncensus_shop_structure"));
			int Ncensus_area_seq_feet = Utilities.parseInt(mr.getParameter("Ncensus_area_seq_feet"));
			int Ncensus_shop_researcher_closed_status = Utilities.parseInt(mr.getParameter("Ncensus_shop_researcher_closed_status"));
			int Ncensus_shop_researcher_closed_status_value = Utilities.parseInt(mr.getParameter("Ncensus_shop_researcher_closed_status_value"));
			int Nsupply_frequency_ko = Utilities.parseInt(mr.getParameter("Nsupply_frequency_ko"));
			int Nsupply_frequency_g = Utilities.parseInt(mr.getParameter("Nsupply_frequency_g"));
			int Ncensus_exclusivity_agreement_pkr_ssrb_1 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_1"));
			int Ncensus_exclusivity_agreement_pkr_ssrb_2 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_2"));
			int Ncensus_exclusivity_agreement_pkr_ssrb_3 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_3"));
			int Ncensus_exclusivity_agreement_pkr_ssrb_4 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_4"));
			int Ncensus_exclusivity_agreement_pkr_ssrb_5 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_5"));
			int Ncensus_exclusivity_agreement_pkr_pet_1 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_1"));
			int Ncensus_exclusivity_agreement_pkr_pet_2 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_2"));
			int Ncensus_exclusivity_agreement_pkr_pet_3 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_3"));
			int Ncensus_exclusivity_agreement_pkr_pet_4 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_4"));
			int Ncensus_exclusivity_agreement_pkr_pet_5 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_5"));
			int Naggr_exp_date_mm = Utilities.parseInt(mr.getParameter("Naggr_exp_date_mm"));
			int Naggr_exp_date_yy = Utilities.parseInt(mr.getParameter("Naggr_exp_date_yy"));
			int Ncensus_shop_agreement_type = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type"));
			int Ncensus_shop_agreement_period = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_period"));
			int Ncensus_shop_agreement_type1 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1"));
			int Ncensus_shop_selling_status_ssrb = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_status_ssrb"));
			int Ncensus_shop_selling_bev = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_bev"));
			int Ncensus_shop_selling_status_nrb = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_status_nrb"));
			int Ncensus_shop_selling_nrb = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_nrb"));
			int Ncensus_shop_selling_status_pet = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_status_pet"));
			int Ncensus_shop_selling_pet = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_pet"));
			
			int Ncensus_shop_selling_status_can = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_status_can"));
			int Ncensus_shop_selling_can = Utilities.parseInt(mr.getParameter("Ncensus_shop_selling_can"));
			
				
			String Gapp_gen_comments = Utilities.filterString(mr.getParameter("Gapp_gen_comments"),1,100);
			
			int SCensus_Shop_Agreement_Type1 = Utilities.parseInt(mr.getParameter("SCensus_Shop_Agreement_Type1"));
			int SCensus_Shop_Agreement_Type2 = Utilities.parseInt(mr.getParameter("SCensus_Shop_Agreement_Type2"));
			int SCensus_Shop_Agreement_Type3 = Utilities.parseInt(mr.getParameter("SCensus_Shop_Agreement_Type3"));
			int SCensus_Shop_Agreement_Type4 = Utilities.parseInt(mr.getParameter("SCensus_Shop_Agreement_Type4"));
			int SCensus_Shop_Agreement_Type5 = Utilities.parseInt(mr.getParameter("SCensus_Shop_Agreement_Type5"));
			
			String SCensus_Shop_Agreement_Type_Other = Utilities.filterString(mr.getParameter("SCensus_Shop_Agreement_Type_Other"),1,100);
			
			
			int Ncensus_exclusivity_agreement_pkr_ssrb_pi_1 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_pi_1"));
			int Ncensus_exclusivity_agreement_pkr_pet_pi_1 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_pi_1"));
			int Naggr_exp_date_mm_pi = Utilities.parseInt(mr.getParameter("Naggr_exp_date_mm_pi"));
			int Naggr_exp_date_yy_pi = Utilities.parseInt(mr.getParameter("Naggr_exp_date_yy_pi"));
			int Ncensus_shop_agreement_type_pi = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type_pi"));
			int Ncensus_shop_agreement_period_pi = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_period_pi"));
			int Ncensus_shop_agreement_type1_pi_1 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_pi_1"));
			int Ncensus_shop_agreement_type1_pi_2 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_pi_2"));
			int Ncensus_shop_agreement_type1_pi_3 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_pi_3"));
			int Ncensus_shop_agreement_type1_pi_4 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_pi_4"));
			int Ncensus_shop_agreement_type1_pi_5 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_pi_5"));
			
			String SCensus_Shop_Agreement_Type_PI_Other = Utilities.filterString(mr.getParameter("Ncensus_shop_agreement_type_pi_other"),1,100);
			
			
			int Ncensus_exclusivity_agreement_pkr_ssrb_ko_1 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_ssrb_ko_1"));
			int Ncensus_exclusivity_agreement_pkr_pet_ko_1 = Utilities.parseInt(mr.getParameter("Ncensus_exclusivity_agreement_pkr_pet_ko_1"));
			int Naggr_exp_date_mm_ko = Utilities.parseInt(mr.getParameter("Naggr_exp_date_mm_ko"));
			int Naggr_exp_date_yy_ko = Utilities.parseInt(mr.getParameter("Naggr_exp_date_yy_ko"));
			int Ncensus_shop_agreement_type_ko = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type_ko"));
			int Ncensus_shop_agreement_period_ko = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_period_ko"));
			int Ncensus_shop_agreement_type1_ko_1 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_ko_1"));
			int Ncensus_shop_agreement_type1_ko_2 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_ko_2"));
			int Ncensus_shop_agreement_type1_ko_3 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_ko_3"));
			int Ncensus_shop_agreement_type1_ko_4 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_ko_4"));
			int Ncensus_shop_agreement_type1_ko_5 = Utilities.parseInt(mr.getParameter("Ncensus_shop_agreement_type1_ko_5"));
			
			String SCensus_Shop_Agreement_Type_ko_Other = Utilities.filterString(mr.getParameter("Ncensus_shop_agreement_type_ko_other"),1,100);
			
			String Sshop_break_time = Utilities.filterString(mr.getParameter("Sshop_break_time"),1,100);
			
			int Ssupply_frequency_cn = Utilities.parseInt(mr.getParameter("Ssupply_frequency_cn"));
			
			int Scensus_feeded_stock_pi_1 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_pi_1"));
			int Scensus_feeded_stock_pi_2 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_pi_2"));
			int Scensus_feeded_stock_pi_3 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_pi_3"));
			int Scensus_feeded_stock_pi_4 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_pi_4"));
			
			int Scensus_feeded_stock_1_p_pi_21 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_1_p_pi_21"));
			int Scensus_feeded_stock_2_p_pi_21 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_2_p_pi_21"));
			int Scensus_feeded_stock_3_p_pi_21 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_3_p_pi_21"));
			int Scensus_feeded_stock_4_p_pi_21 = Utilities.parseInt(mr.getParameter("Scensus_feeded_stock_4_p_pi_21"));
			
			
			
			String CoolerAccess = Utilities.filterString(mr.getParameter("SCoolerAccess"),1,100);
			String CoolerVisible = Utilities.filterString(mr.getParameter("SCoolerVisible"),1,100);
			
			String OPJPID = Utilities.filterString(mr.getParameter("OPJPID"),1,100);
			
			
			double Accuracy = Utilities.parseDouble(mr.getParameter("Accu"));
			
				///////////////////////////////////
				
				
				
				
			
			/*
			
			String TOTCode[] = Utilities.filterString(mr.getParameterValues("TOTCode"), 1, 100);
			String CoolerSize[] = Utilities.filterString(mr.getParameterValues("CoolerSize"), 1, 100);
			String CoolerStatus[] = Utilities.filterString(mr.getParameterValues("CoolerStatus"), 1, 100);			
			String YearOfIssuance[] = Utilities.filterString(mr.getParameterValues("YearOfIssuance"), 1, 100);
			
			*/
			
			
			
			
				
			Datasource ds = new Datasource();
			
			try{
				
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				boolean isAlreadyEntered = false;
				
				//System.out.println("select outlet_id from mrd_census where mobile_census_id="+MobileCensusID);
				
				ResultSet rs2 = s.executeQuery("select outlet_id from mrd_census where census_id="+MobileCensusID);
				if(rs2.first()){
					isAlreadyEntered = true;
				}
				
				if(!isAlreadyEntered){
					
					
					if(Utilities.parseDouble(Lat)!=0){
						
					
					String OutletIDVal = OutletID+"";
					if(OutletID == 0){
						OutletIDVal = null;
					}
					
					//Created By 0 Patch 
					
					if(MobileUserID==0){ //if created by 0
						ResultSet rs23 = s.executeQuery("select created_by from mrd_census where device_id='"+DeviceUUID+"' and created_by !=0   order by id desc");
						if(rs23.first()){
							MobileUserID = rs23.getLong("created_by");
						}
					}
					
					//////////////////////////////////////
					
					
					
					String SQL = "insert into mrd_census (census_id, outlet_id, created_on, created_by, lat, lng, outlet_name,outlet_board_name, census_region_id, census_distributor_Id, census_distributor_name, census_area, census_shop_type, census_outlet_address,census_distributor_town, census_distributor_tehsil, census_distributor_district, land_mark,is_census_shop_closed,census_shop_closed_status,census_day_off,census_partially_timing,census_segment,census_shop_location,census_outlet_contact_person,census_outlet_contact_1,census_outlet_contact_2,census_owner_cnic_1,census_owner_contact_person_relation,census_outlet_contact_name,census_outlet_contact_3,census_owner_cnic_2,census_facebook_id,census_email_id,census_feeded_stock_1,census_feeded_stock_2,census_feeded_stock_3,census_financial_service_1,census_financial_service_2,census_financial_service_3,census_financial_service_4,census_financial_service_5,census_financial_service_6,census_owner_contact_person_shop_opening_time,census_owner_contact_person_shop_close_time,census_shop_status,census_shop_establishment_history,census_business_structure,census_shop_potential,census_customer_type,census_customer_type_wholesale_percent,census_customer_type_retailer_percent,census_service_type,census_csd_turnover_per_day,census_trader_channel,census_trader_channel_sub_channel,pi_csd,pi_sting,ko,supply_frequency,census_exclusivity_agreement_1,census_exclusivity_agreement_2,census_exclusivity_agreement_3,census_exclusivity_agreement_4,census_exclusivity_agreement_5,census_discount_status_1,census_discount_status_2,census_discount_status_3,census_cooler_visibility,census_pepsi_publicity_material_1,census_pepsi_publicity_material_2,census_pepsi_publicity_material_3,census_pepsi_publicity_material_4,census_pepsi_publicity_material_5,census_coke_publicity_material_1,census_coke_publicity_material_2,census_coke_publicity_material_3,census_coke_publicity_material_4,census_coke_publicity_material_5,census_outlet_type,census_shop_structure,census_area_seq_feet,census_shop_researcher_closed_status,census_shop_researcher_closed_status_value,supply_frequency_ko,supply_frequency_g,census_exclusivity_agreement_pkr_ssrb_1,census_exclusivity_agreement_pkr_ssrb_2,census_exclusivity_agreement_pkr_ssrb_3,census_exclusivity_agreement_pkr_ssrb_4,census_exclusivity_agreement_pkr_ssrb_5,census_exclusivity_agreement_pkr_pet_1,census_exclusivity_agreement_pkr_pet_2,census_exclusivity_agreement_pkr_pet_3,census_exclusivity_agreement_pkr_pet_4,census_exclusivity_agreement_pkr_pet_5,aggr_exp_date_mm,aggr_exp_date_yy,census_shop_agreement_type,census_shop_agreement_period,census_shop_agreement_type1,census_shop_selling_status_ssrb,census_shop_selling_bev,census_shop_selling_status_nrb,census_shop_selling_nrb,census_shop_selling_status_pet,census_shop_selling_pet,outlet_name_actual,census_feeded_stock_4,census_feeded_stock_percentage_1,census_feeded_stock_percentage_2,census_feeded_stock_percentage_3,census_feeded_stock_percentage_4,census_shop_selling_status_can,census_shop_selling_can,cash_machine_status,cash_machine_quantity,stock_storage_loc1,stock_storage_loc2,stock_storage_loc3,stock_storage_loc4,app_gen_comments,census_shop_agreement_type1_1,census_shop_agreement_type1_2,census_shop_agreement_type1_3,census_shop_agreement_type1_4,census_shop_agreement_type1_5,census_shop_agreement_type_other,census_exclusivity_agreement_pkr_ssrb_pi_1,census_exclusivity_agreement_pkr_pet_pi_1,aggr_exp_date_mm_pi,aggr_exp_date_yy_pi,census_shop_agreement_type_pi,census_shop_agreement_period_pi,census_shop_agreement_type1_pi_1,census_shop_agreement_type1_pi_2,census_shop_agreement_type1_pi_3,census_shop_agreement_type1_pi_4,census_shop_agreement_type1_pi_5,census_shop_agreement_type_pi_other,census_exclusivity_agreement_pkr_ssrb_ko_1,census_exclusivity_agreement_pkr_pet_ko_1,aggr_exp_date_mm_ko,aggr_exp_date_yy_ko,census_shop_agreement_type_ko,census_shop_agreement_period_ko,census_shop_agreement_type1_ko_1,census_shop_agreement_type1_ko_2,census_shop_agreement_type1_ko_3,census_shop_agreement_type1_ko_4,census_shop_agreement_type1_ko_5,census_shop_agreement_type_ko_other,shop_break_time,supply_frequency_cn,census_feeded_stock_pi_1,census_feeded_stock_pi_2,census_feeded_stock_pi_3,census_feeded_stock_pi_4,census_feeded_stock_1_p_pi_21,census_feeded_stock_2_p_pi_21,census_feeded_stock_3_p_pi_21,census_feeded_stock_4_p_pi_21,cooler_access,cooler_visible,pjp_id,device_id,accuracy,mobile_timestamp) values('"+MobileCensusID+"',"+OutletID+",now(),"+MobileUserID+","+Utilities.parseDouble(Lat)+","+Utilities.parseDouble(Lng)+","							
							+ "'"+OutletName+"','"+OutletBoardName+"',"+CRegionID+","+CDistirubtorID+",'"+CDistributorName+"',"+CArea+","+CShopType+",'"+CShopAddress+"','"+CDistributorTown+"',"+CDistributorTehsil+","+CDistributorDistrict+",'"+CLandMark+"',"
							+ CShopClosed+","+CShopClosedStatus+","+CDayOff+","+CPartiallyTiming+","+CSegment+","+CShopLocation+",'"+CContactPerson+"','"+CContact1+"','"+CContact2+"','"+CCnic1+"','"+CContactRelation+"','"+CRelConName+"','"+CRelCon3+"','"+CRelCon2+"','"+CFacebookID+"','"+CEmailID+"',"+CFeedbackStock1+","+CFeedbackStock2+","+CFeedbackStock3+","+CFinancialService1+","+CFinancialService2+","+CFinancialService3+","+CFinancialService4+","+CFinancialService5+","+CFinancialService6+",'"+CShopOPeningTime+"','"+CShopeClosingTime+"',"
							+ CShopStatus+","+CShopEstablishmentHistory+","+CBusinessStructure+","+CShopPotential+","+CCustomerType+","+CCustomerTypeWholeSale+","+CCustomerTypeRetailer+","+CServiceType+","+CTurnOver+","+CTraderChannel+","+CTraderSubChannel
							+ ","+BCSD+","+BSting+","+BKo+","+BSupplyFrequency+","+Bagreement1+","+Bagreement2+","+Bagreement3+","+Bagreement4+","+Bagreement5+","+BDiscountStatus1+","+BDiscountStatus2+","+BDiscountStatus3+","+BCoolerVisibility+","+BPepsiPublicityMaterial1+","+BPepsiPublicityMaterial2+","+BPepsiPublicityMaterial3+","+BPepsiPublicityMaterial4+","+BPepsiPublicityMaterial5+","+BCokePublicityMaterial1+","+BCokePublicityMaterial2+","+BCokePublicityMaterial3+","+BCokePublicityMaterial4+","+BCokePublicityMaterial5 
							
							+ ","+Ncensus_outlet_type 
							+ ","+Ncensus_shop_structure 
							+ ","+Ncensus_area_seq_feet 
							+ ","+Ncensus_shop_researcher_closed_status 
							+ ","+Ncensus_shop_researcher_closed_status_value 
							+ ","+Nsupply_frequency_ko 
							+ ","+Nsupply_frequency_g 
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_1 
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_2 
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_3 
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_4 
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_5 
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_1 
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_2 
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_3 
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_4 
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_5 
							+ ","+Naggr_exp_date_mm 
							+ ","+Naggr_exp_date_yy 
							+ ","+Ncensus_shop_agreement_type 
							+ ","+Ncensus_shop_agreement_period 
							+ ","+Ncensus_shop_agreement_type1 
							+ ","+Ncensus_shop_selling_status_ssrb 
							+ ","+Ncensus_shop_selling_bev 
							+ ","+Ncensus_shop_selling_status_nrb 
							+ ","+Ncensus_shop_selling_nrb 
							+ ","+Ncensus_shop_selling_status_pet 
							+ ","+Ncensus_shop_selling_pet 
							+",'"+OutletNameActual+"'"
							+ ","+CFeedbackStock4
							+",'"+CFeedbackStockPer1+"'"
							+",'"+CFeedbackStockPer2+"'"
							+",'"+CFeedbackStockPer3+"'"
							+",'"+CFeedbackStockPer4+"'"
							+ ","+Ncensus_shop_selling_status_can 
							+ ","+Ncensus_shop_selling_can 
							
							+ ","+BCashMachineStatus 
							+ ","+BCashMachineQuantity
							
							+ ","+CStockStorageLoc1
							+ ","+CStockStorageLoc2
							+ ","+CStockStorageLoc3
							+ ","+CStockStorageLoc4
							
							+",'"+Gapp_gen_comments+"'"
							
							+ ","+SCensus_Shop_Agreement_Type1
							+ ","+SCensus_Shop_Agreement_Type2
							+ ","+SCensus_Shop_Agreement_Type3
							+ ","+SCensus_Shop_Agreement_Type4
							+ ","+SCensus_Shop_Agreement_Type5
							
							+",'"+SCensus_Shop_Agreement_Type_Other+"'"
							
							
							
							
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_pi_1
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_pi_1
							+ ","+Naggr_exp_date_mm_pi
							+ ","+Naggr_exp_date_yy_pi
							+ ","+Ncensus_shop_agreement_type_pi
							+ ","+Ncensus_shop_agreement_period_pi
							+ ","+Ncensus_shop_agreement_type1_pi_1
							+ ","+Ncensus_shop_agreement_type1_pi_2
							+ ","+Ncensus_shop_agreement_type1_pi_3
							+ ","+Ncensus_shop_agreement_type1_pi_4
							+ ","+Ncensus_shop_agreement_type1_pi_5
							+",'"+SCensus_Shop_Agreement_Type_PI_Other+"'"
							
							+ ","+Ncensus_exclusivity_agreement_pkr_ssrb_ko_1
							+ ","+Ncensus_exclusivity_agreement_pkr_pet_ko_1
							+ ","+Naggr_exp_date_mm_ko
							+ ","+Naggr_exp_date_yy_ko
							+ ","+Ncensus_shop_agreement_type_ko
							+ ","+Ncensus_shop_agreement_period_ko
							+ ","+Ncensus_shop_agreement_type1_ko_1
							+ ","+Ncensus_shop_agreement_type1_ko_2
							+ ","+Ncensus_shop_agreement_type1_ko_3
							+ ","+Ncensus_shop_agreement_type1_ko_4
							+ ","+Ncensus_shop_agreement_type1_ko_5
							+",'"+SCensus_Shop_Agreement_Type_ko_Other+"'"
							+",'"+Sshop_break_time+"'"
							+ ","+Ssupply_frequency_cn
							
							+ ","+Scensus_feeded_stock_pi_1
							+ ","+Scensus_feeded_stock_pi_2
							+ ","+Scensus_feeded_stock_pi_3
							+ ","+Scensus_feeded_stock_pi_4
							
							+ ","+Scensus_feeded_stock_1_p_pi_21
							+ ","+Scensus_feeded_stock_2_p_pi_21
							+ ","+Scensus_feeded_stock_3_p_pi_21
							+ ","+Scensus_feeded_stock_4_p_pi_21
							
							
							+",'"+CoolerAccess+"'"
							+",'"+CoolerVisible+"'"
							
							+",'"+OPJPID+"'"
							+",'"+DeviceUUID+"'"
							+","+Accuracy
							+",'"+MobileTimestamp+"'"
							
							
							+ ")";
					
					
					//String SQL = "INSERT INTO `mrd_census` ( `mobile_census_id`, `mobile_uuid`, `outlet_id`, `outlet_name`, `region_id`, `distributor_id`, `area_id`, `shop_type_id`, `shop_closed_status_id`, `day_off`, `if_partially_on_timing`, `address`, `segment_id`, `shop_location_id`, `contact_person_type_id`) VALUES ("+MobileCensusID+", '"+DeviceUUID+"', "+OutletIDVal+", '"+OutletName+"', "+RegionID+", "+DistributorID+", "+Area+", "+ShopType+", "+ShopClosedStatus+", "+DayOff+", "+PartiallyTiming+", '"+Address+"', "+Segment+", "+ShopLocation+", '"+ContactPerson+"' ) ";      
					System.out.println(SQL);
					s.executeUpdate(SQL);
										
					long MasterID = 0;
					ResultSet rs = s.executeQuery("select last_insert_id()");
					if(rs.first()){
						MasterID = rs.getLong(1);
					}
					
					//inserting in sub tables.
					
					String CoolerTypesCompany[] = Utilities.filterString(mr.getParameterValues("CTCompanyID"), 1, 100);	
					String CoolerTypesType[] = Utilities.filterString(mr.getParameterValues("CTTypeID"), 1, 100);	
					String CoolerTypesQuantity[] = Utilities.filterString(mr.getParameterValues("CTQuantity"), 1, 100);	
					
					
					
					
					
					
					
					String CoolerPublicityCompany[] = Utilities.filterString(mr.getParameterValues("CPTCompanyID"), 1, 100);	
					String CoolerPublicityType[] = Utilities.filterString(mr.getParameterValues("CPTTypeID"), 1, 100);	
					String CoolerPublicityQuantity[] = Utilities.filterString(mr.getParameterValues("CPTQuantity"), 1, 100);	
					
					
					String CSDCompany[] = Utilities.filterString(mr.getParameterValues("CSDCompanyID"), 1, 100);	
					String CSDPackage[] = Utilities.filterString(mr.getParameterValues("CSDPackageID"), 1, 100);	
					String CSDBrand[] = Utilities.filterString(mr.getParameterValues("CSDBrandID"), 1, 100);	
					
					String NCBCompany[] = Utilities.filterString(mr.getParameterValues("NCBCompanyID"), 1, 100);	
					String NCBPackage[] = Utilities.filterString(mr.getParameterValues("NCBPackageID"), 1, 100);	
					String NCBBrand[] = Utilities.filterString(mr.getParameterValues("NCBBrandID"), 1, 100);	
					
					
					
					//Vol CSD
					
					String VolCSDCompany[] = Utilities.filterString(mr.getParameterValues("VolCSDCompanyID"), 1, 100);	
					String VolCSDPackage[] = Utilities.filterString(mr.getParameterValues("VolCSDPackageID"), 1, 100);	
					String VolCSDBrand[] = Utilities.filterString(mr.getParameterValues("VolCSDBrandID"), 1, 100);
					String VolCSDMDE[] = Utilities.filterString(mr.getParameterValues("VolCSDMDE"), 1, 100);
					
					
					
					
					
					//Vol Juice
					
					String VolJuiceCompany[] = Utilities.filterString(mr.getParameterValues("VolJuiceCompanyID"), 1, 100);	
					String VolJuicePackage[] = Utilities.filterString(mr.getParameterValues("VolJuicePackageID"), 1, 100);	
					String VolJuiceBrand[] = Utilities.filterString(mr.getParameterValues("VolJuiceBrandID"), 1, 100);
					String VolJuiceMDE[] = Utilities.filterString(mr.getParameterValues("VolJuiceMDE"), 1, 100);
					
					
					//Vol Drink
					
					String VolDrinkCompany[] = Utilities.filterString(mr.getParameterValues("VolDrinkCompanyID"), 1, 100);	
					String VolDrinkPackage[] = Utilities.filterString(mr.getParameterValues("VolDrinkPackageID"), 1, 100);	
					String VolDrinkBrand[] = Utilities.filterString(mr.getParameterValues("VolDrinkBrandID"), 1, 100);
					String VolDrinkMDE[] = Utilities.filterString(mr.getParameterValues("VolDrinkMDE"), 1, 100);
					
					
					
					//Pace
					
					String PaceCompany[] = Utilities.filterString(mr.getParameterValues("PaceCompanyID"), 1, 100);	
					String PacePackage[] = Utilities.filterString(mr.getParameterValues("PacePackageID"), 1, 100);	
					String PaceBrand[] = Utilities.filterString(mr.getParameterValues("PaceBrandID"), 1, 100);

					
					//TOT Code
					
					String ToTCode[] = Utilities.filterString(mr.getParameterValues("TOTCode"), 1, 100);
					
					
					//System.out.println(NCBBrand.length);
					
					
					if(CoolerTypesCompany!=null){
						for(int i=0;i<CoolerTypesCompany.length;i++){
							
							s.executeUpdate("insert into mrd_census_cooler_types(id,census_id,cooler_type_company_id,cooler_type_type_id,quantity) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(CoolerTypesCompany[i])+"','"+Utilities.parseInt(CoolerTypesType[i])+"','"+Utilities.parseInt(CoolerTypesQuantity[i])+"') ");
							
						}
					}
					
					if(CoolerPublicityCompany!=null){
						for(int i=0;i<CoolerPublicityCompany.length;i++){
							
							s.executeUpdate("insert into mrd_census_publicity_types(id,census_id,publicity_type_company_id,publicity_type_type_id,quantity) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(CoolerPublicityCompany[i])+"','"+Utilities.parseInt(CoolerPublicityType[i])+"','"+Utilities.parseInt(CoolerPublicityQuantity[i])+"') ");
							
						}
					}
					
					if(CSDCompany!=null){
						for(int i=0;i<CSDCompany.length;i++){
							
							s.executeUpdate("insert into mrd_census_csd_types(id,census_id,csd_company_id,csd_package_id,csd_brand) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(CSDCompany[i])+"','"+Utilities.parseInt(CSDPackage[i])+"','"+Utilities.parseInt(CSDBrand[i])+"') ");
							
						}
					}
					
					if(NCBCompany!=null){
						for(int i=0;i<NCBCompany.length;i++){
							
							System.out.println("insert into mrd_census_ncb_types(id,census_id,ncb_company_id,ncb_package_id,ncb_brand) values("+MasterID+",'"+MobileCensusID+"','"+NCBCompany[i]+"','"+NCBPackage[i]+"','"+NCBBrand[i]+"') ");
							
							s.executeUpdate("insert into mrd_census_ncb_types(id,census_id,ncb_company_id,ncb_package_id,ncb_brand) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(NCBCompany[i])+"','"+Utilities.parseInt(NCBPackage[i])+"','"+Utilities.parseInt(NCBBrand[i])+"') ");
							
						}
					}
					
					if(VolCSDCompany!=null){
						for(int i=0;i<VolCSDCompany.length;i++){
							
							//System.out.println("insert into mrd_census_vol_csd(id,census_id,vol_csd_company_id,vol_csd_package_id,vol_csd_brand) values("+MasterID+",'"+MobileCensusID+"',"+VolCSDCompany[i]+","+VolCSDPackage[i]+","+VolCSDBrand[i]+") ");
							
							s.executeUpdate("insert into mrd_census_vol_csd(id,census_id,vol_csd_company_id,vol_csd_package_id,vol_csd_brand,vol_csd_mde) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(VolCSDCompany[i])+"','"+Utilities.parseInt(VolCSDPackage[i])+"','"+Utilities.parseInt(VolCSDBrand[i])+"','"+Utilities.parseInt(VolCSDMDE[i])+"') ");
							
						}
					}
					
					if(VolJuiceCompany!=null){
						for(int i=0;i<VolJuiceCompany.length;i++){
							//System.out.println("insert into mrd_census_vol_juice(id,census_id,vol_juice_company_id,vol_juice_package_id,vol_juice_brand) values("+MasterID+",'"+MobileCensusID+"',"+VolJuiceCompany[i]+","+VolJuicePackage[i]+","+VolJuiceBrand[i]+") ");
							s.executeUpdate("insert into mrd_census_vol_juice(id,census_id,vol_juice_company_id,vol_juice_package_id,vol_juice_brand,vol_juice_mde) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(VolJuiceCompany[i])+"','"+Utilities.parseInt(VolJuicePackage[i])+"','"+Utilities.parseInt(VolJuiceBrand[i])+"','"+Utilities.parseInt(VolJuiceMDE[i])+"') ");
							
						}
					}
					
					if(VolDrinkCompany!=null){
						for(int i=0;i<VolDrinkCompany.length;i++){
							//System.out.println("insert into mrd_census_vol_drink(id,census_id,vol_drink_company_id,vol_drink_package_id,vol_drink_brand) values("+MasterID+",'"+MobileCensusID+"',"+VolDrinkCompany[i]+","+VolDrinkPackage[i]+","+VolDrinkBrand[i]+") ");
							s.executeUpdate("insert into mrd_census_vol_drink(id,census_id,vol_drink_company_id,vol_drink_package_id,vol_drink_brand,vol_drink_mde) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(VolDrinkCompany[i])+"','"+Utilities.parseInt(VolDrinkPackage[i])+"','"+Utilities.parseInt(VolDrinkBrand[i])+"','"+Utilities.parseInt(VolDrinkMDE[i])+"') ");
							
						}
					}
					
					if(PaceCompany!=null){
						for(int i=0;i<PaceCompany.length;i++){
							//System.out.println("insert into mrd_census_pace(id,census_id,pace_company_id,pace_package_id,pace_brand) values("+MasterID+",'"+MobileCensusID+"',"+PaceCompany[i]+","+PacePackage[i]+","+PaceBrand[i]+") ");
							s.executeUpdate("insert into mrd_census_pace(id,census_id,pace_company_id,pace_package_id,pace_brand) values("+MasterID+",'"+MobileCensusID+"','"+Utilities.parseInt(PaceCompany[i])+"','"+Utilities.parseInt(PacePackage[i])+"','"+Utilities.parseInt(PaceBrand[i])+"') ");
							
						}
					}
					
					
					if(ToTCode!=null){
						for(int i=0;i<ToTCode.length;i++){
							//System.out.println("insert into mrd_census_pace(id,census_id,pace_company_id,pace_package_id,pace_brand) values("+MasterID+",'"+MobileCensusID+"',"+PaceCompany[i]+","+PacePackage[i]+","+PaceBrand[i]+") ");
							s.executeUpdate("insert into mrd_census_cooler_code(id,census_id,tot_code) values("+MasterID+",'"+MobileCensusID+"','"+ToTCode[i]+"') ");
							
						}
					}
					
					
					
					/*
					if(TOTCode != null){
						for(int i = 0; i < TOTCode.length; i++){
							s.executeUpdate("INSERT INTO `mrd_census_tot`(`id`,`tot_code`, `cooler_size`, `cooler_status`,`year_of_issuance`)VALUES("+CensusID+",'"+TOTCode[i]+"', "+CoolerSize[i]+", "+CoolerStatus[i]+",'"+YearOfIssuance[i]+"')");
						}
					}*/
					
				}else{//end of lat=0 if
					json.put("success", "false");
					json.put("error_code", "Lat & Lng could not be 0");
					
				}
					
				}
				
				s.close();
				ds.commit();
				json.put("success", "true");
				System.out.println("Yes saved successfully and now returning success = true ");
				
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
