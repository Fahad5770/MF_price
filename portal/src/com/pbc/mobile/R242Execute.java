package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Devices Execute", urlPatterns = { "/mobile/R242Execute" })
public class R242Execute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public R242Execute() {
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

		PrintWriter out = response.getWriter();
		
		
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		
		//---------------------------------Button pressed starts------------------------------//
		int ButtonID=0;
		//---------------------------------Button pressed ends------------------------------//
		
		//------------------------------------GENERAL-------------------------------------//
		
		long OutletID = Utilities.parseLong(request.getParameter("outlet_general"));
		String SystemOutletName=Utilities.filterString(request.getParameter("System_Outlet_Name_general"), 1,300);
		//System.out.println(SystemOutletName);
		String OutletNameOnBoardgeneral=Utilities.filterString(request.getParameter("Outlet_Name_On_Board_general"), 1,200);
		String ActualOutletNamegeneral=Utilities.filterString(request.getParameter("Actual_Outlet_Name_general"), 1,100);
		Long DistributorIDgeneral=Utilities.parseLong(request.getParameter("DistributorID_general"));
		String DistributorName=Utilities.filterString(request.getParameter("DistributorName_general"), 1,200);
		long PJPID = Utilities.parseLong(request.getParameter("PJPID_general"));
		String PJPNAme=Utilities.filterString(request.getParameter("PJPName_general"), 1,200);
		int Area=Utilities.parseInt(request.getParameter("Area_general"));
		int Location=Utilities.parseInt(request.getParameter("location_general"));
		int ShopLocation=Utilities.parseInt(request.getParameter("Shop_location_general"));
		int SocioEconimicClassification=Utilities.parseInt(request.getParameter("SocioEconimicClassification_general"));
		int OutletType=Utilities.parseInt(request.getParameter("OutletType_general"));
		String MVillage=Utilities.filterString(request.getParameter("MVillage_general"), 1,300);
		String MLandMark=Utilities.filterString(request.getParameter("MLandMark_general"), 1,300);
		
		int OutletStructure=Utilities.parseInt(request.getParameter("OutletStructure_general"));
		int ServiceType=Utilities.parseInt(request.getParameter("ServiceType_general"));
		String OwnerName=Utilities.filterString(request.getParameter("OwnerName_general"), 1,200);
		String landline=Utilities.filterString(request.getParameter("OwnerContactNo1_general"), 1,100);
		String Mobile=Utilities.filterString(request.getParameter("OwnerContactNo2_general"), 1,100);
		String OwnerCNIC=Utilities.filterString(request.getParameter("OwnerCNIC_general"), 1,100);
		String ContactPersonName=Utilities.filterString(request.getParameter("ContactPersonName_general"), 1,200);
		String ContactPersonNo=Utilities.filterString(request.getParameter("ContactPersonNo_general"), 1,200);
		String ContactPersonCNIC=Utilities.filterString(request.getParameter("ContactPersonCNIC_general"), 1,200);
		String EmailID=Utilities.filterString(request.getParameter("EmailID_general"), 1,200);
		int ShopType=Utilities.parseInt(request.getParameter("ShopType_general"));
		int AreaSqFT=Utilities.parseInt(request.getParameter("AreaSqFT"));
		//String AreaSqFT=Utilities.filterString(request.getParameter("AreaSqFT"), 1,300);
		long CreatedbyID= Utilities.parseLong(request.getParameter("Createdbyid_general"));
		String CreatedbyName=Utilities.filterString(request.getParameter("Createdbyname_general"), 1,300);
		String OutletAddress=Utilities.filterString(request.getParameter("OutletAddress"), 1,300);
		
		int District=Utilities.parseInt(request.getParameter("District"));
		int Tehsil=Utilities.parseInt(request.getParameter("Tehsil"));
		int TradeSubChannel=Utilities.parseInt(request.getParameter("TradeSubChannel"));
		int ContactPerson=Utilities.parseInt(request.getParameter("ContactPerson"));
		int IsShopClosedStatus=Utilities.parseInt(request.getParameter("IsShopClosedStatus"));
		int ShopClosedStatus=Utilities.parseInt(request.getParameter("ShopClosedStatus"));
		//System.out.println(ShopClosedStatus);
		int ID=Utilities.parseInt(request.getParameter("ID"));
		
		
		//Shopkeeper Parameters
		
		int ShopStatusShopkeeper=Utilities.parseInt(request.getParameter("ShopStatusShopkeeper"));
		int DayOff_Shopkeeper=Utilities.parseInt(request.getParameter("DayOff_Shopkeeper"));
		
		String ShopOpeningTime_Shopkeeper=Utilities.filterString(request.getParameter("ShopOpeningTime_Shopkeeper"), 1,100);
		String ShopClosingTime_Shopkeeper=Utilities.filterString(request.getParameter("ShopClosingTime_Shopkeeper"), 1,100);
		String BreakTime=Utilities.filterString(request.getParameter("BreakTime_Shopkeeper"), 1,100);
		int FinancialServicebtn1=Utilities.parseInt(request.getParameter("FinancialServicebtn1"));
		int FinancialServicebtn2=Utilities.parseInt(request.getParameter("FinancialServicebtn2"));
		int FinancialServicebtn3=Utilities.parseInt(request.getParameter("FinancialServicebtn3"));
		int FinancialServicebtn4=Utilities.parseInt(request.getParameter("FinancialServicebtn4"));
		int FinancialServicebtn5=Utilities.parseInt(request.getParameter("FinancialServicebtn5"));
		int FinancialServicebtn6=Utilities.parseInt(request.getParameter("FinancialServicebtn6"));
		int SSRB=Utilities.parseInt(request.getParameter("SSRB"));
		int PET=Utilities.parseInt(request.getParameter("PET"));
		int TEtra=Utilities.parseInt(request.getParameter("TEtra"));
		int CAN=Utilities.parseInt(request.getParameter("CAN"));
		int ExAgreeBase=Utilities.parseInt(request.getParameter("ExAgreeBase"));
		int PerRawCaseSSRB=Utilities.parseInt(request.getParameter("PerRawCaseSSRB"));
		int PerRawCasePET=Utilities.parseInt(request.getParameter("PerRawCasePET"));
		int Month1=Utilities.parseInt(request.getParameter("Month1"));
		int Year1=Utilities.parseInt(request.getParameter("Year1"));
		int AgreeType=Utilities.parseInt(request.getParameter("AgreeType"));
		int AgreePeriod=Utilities.parseInt(request.getParameter("AgreePeriod"));
		int AgreeType1a=Utilities.parseInt(request.getParameter("AgreeType1a"));
		int AgreeType1b=Utilities.parseInt(request.getParameter("AgreeType1b"));
		int AgreeType1c=Utilities.parseInt(request.getParameter("AgreeType1c"));
		int AgreeType1d=Utilities.parseInt(request.getParameter("AgreeType1d"));
		int AgreeType1e=Utilities.parseInt(request.getParameter("AgreeType1e"));
		//int AgreeType1f=Utilities.parseInt(request.getParameter("AgreeType1f"));
		String AgreeType1f=Utilities.filterString(request.getParameter("AgreeType1f"), 1,100);
		                     // PI
		int MonthPI=Utilities.parseInt(request.getParameter("MonthPI"));
		int YearPI=Utilities.parseInt(request.getParameter("YearPI"));
		int AgreeTypePI=Utilities.parseInt(request.getParameter("AgreeTypePI"));
		int AgreePeriodPI=Utilities.parseInt(request.getParameter("AgreePeriodPI"));
		int AgreeType2PIa=Utilities.parseInt(request.getParameter("AgreeType2PIa"));
		int AgreeType2PIb=Utilities.parseInt(request.getParameter("AgreeType2PIb"));
		int AgreeType2PIc=Utilities.parseInt(request.getParameter("AgreeType2PIc"));
		int AgreeType2PId=Utilities.parseInt(request.getParameter("AgreeType2PId"));
		int AgreeType2PIe=Utilities.parseInt(request.getParameter("AgreeType2PIe"));
		//int AgreeType2PIf=Utilities.parseInt(request.getParameter("AgreeType2PIf"));
		String AgreeType2PIf=Utilities.filterString(request.getParameter("AgreeType2PIf"), 1,100);
		int PerRawCaseSSRBPI=Utilities.parseInt(request.getParameter("PerRawCaseSSRBPI"));
		int PerRawCasePETPI=Utilities.parseInt(request.getParameter("PerRawCasePETPI"));
		
							//KO
		int MonthKO=Utilities.parseInt(request.getParameter("MonthKO"));
		int YearKO=Utilities.parseInt(request.getParameter("YearKO"));
		int AgreeTypeKO=Utilities.parseInt(request.getParameter("AgreeTypeKO"));
		int AgreePeriodKO=Utilities.parseInt(request.getParameter("AgreePeriodKO"));
		int AgreeTypeKOa=Utilities.parseInt(request.getParameter("AgreeTypeKOa"));
		int AgreeTypeKOb=Utilities.parseInt(request.getParameter("AgreeTypeKOb"));
		int AgreeTypeKOc=Utilities.parseInt(request.getParameter("AgreeTypeKOc"));
		int AgreeTypeKOd=Utilities.parseInt(request.getParameter("AgreeTypeKOd"));
		int AgreeTypeKOe=Utilities.parseInt(request.getParameter("AgreeTypeKOe"));
		//int AgreeTypeKOf=Utilities.parseInt(request.getParameter("AgreeTypeKOf"));
		String AgreeTypeKOf=Utilities.filterString(request.getParameter("AgreeTypeKOf"), 1,100);
		int PerCaseSSRBKO=Utilities.parseInt(request.getParameter("PerCaseSSRBKO"));
		int PerCasePETKO=Utilities.parseInt(request.getParameter("PerCasePETKO"));
		int PartiallycloseTime=Utilities.parseInt(request.getParameter("PartiallycloseTime"));
		int SuppliedKoDistributor=Utilities.parseInt(request.getParameter("SuppliedKoDistributor"));
		int SuppliedKoWholeseller=Utilities.parseInt(request.getParameter("SuppliedKoWholeseller"));
		int SuppliedKoMobiler=Utilities.parseInt(request.getParameter("SuppliedKoMobiler"));
		int SuppliedKoDealer=Utilities.parseInt(request.getParameter("SuppliedKoDealer"));
		int SuppliedPIDistributor=Utilities.parseInt(request.getParameter("SuppliedPIDistributor"));
		int SuppliedPIWholeseller=Utilities.parseInt(request.getParameter("SuppliedPIWholeseller"));
		int SuppliedPIMobiler=Utilities.parseInt(request.getParameter("SuppliedPIMobiler"));
		int SuppliedPIDealer=Utilities.parseInt(request.getParameter("SuppliedPIDealer"));
		
		
		
		
		//Status parameters
		
		
		int SupplyFrequencyPI=Utilities.parseInt(request.getParameter("SupplyFrequencyPI"));
		int SupplyFrequencyKO=Utilities.parseInt(request.getParameter("SupplyFrequencyKO"));
		int SupplyFrequencyGourment=Utilities.parseInt(request.getParameter("SupplyFrequencyGourment"));
		int SupplyFrequencyColaNext=Utilities.parseInt(request.getParameter("SupplyFrequencyColaNext"));
		int CoveredbyresearcherCompany=Utilities.parseInt(request.getParameter("CoveredbyresearcherCompany"));
		int StockStoragelocation1=Utilities.parseInt(request.getParameter("StockStoragelocation1"));
		int StockStoragelocation2=Utilities.parseInt(request.getParameter("StockStoragelocation2"));
		int StockStoragelocation3=Utilities.parseInt(request.getParameter("StockStoragelocation3"));
		int StockStoragelocation4=Utilities.parseInt(request.getParameter("StockStoragelocation4"));
		
		int CashMachineQuantity_Status=Utilities.parseInt(request.getParameter("Quantity_Status"));
		int ShopEstablishedHistory_status=Utilities.parseInt(request.getParameter("ShopEstablishedHistory_status"));
		int BusinessStrutcure=Utilities.parseInt(request.getParameter("BusinessStrutcure"));
		int ShopType_status=Utilities.parseInt(request.getParameter("ShopType_status"));
		
		int pi_csd=Utilities.parseInt(request.getParameter("PICSD_status"));
		
		int pi_sting=Utilities.parseInt(request.getParameter("PISting_status"));
		int ko=Utilities.parseInt(request.getParameter("KO_status"));		
		String wholeSalePer_status=Utilities.filterString(request.getParameter("wholeSalePer_status"), 1,100);
		String RetailerPer_status=Utilities.filterString(request.getParameter("RetailerPer_status"), 1,100);
		//CoolerCode
		String Visible_CoolerCode=Utilities.filterString(request.getParameter("VisibleCooler"), 1,100);
		String Accessible_CoolderCode=Utilities.filterString(request.getParameter("AccessibleCooler"), 1,100);
		
		//COOLERTYPE
		int TableID=Utilities.parseInt(request.getParameter("tableId"));
		String[] CoolTypeCo=request.getParameterValues("cooltype_co");
		String[] CoolTypePack =request.getParameterValues("cooltype_pack");
		String[] CoolTypeBrand =request.getParameterValues("cooltype_brand");
		String CoolTypeId="";
		

		//Cooler Code
		int CoolCodeTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] CoolCode=request.getParameterValues("SnCooler");
		String CoolCode_id="";
		//System.out.println( CoolCodeTableMainID);
		//System.out.println(Visible_CoolerCode);
		//System.out.println( Accessible_CoolderCode);
		
		//Publicity
		int PUBTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] PubCo=request.getParameterValues("pub_co");
		String[] PubPack =request.getParameterValues("pub_pack");
		String[] PubBrand =request.getParameterValues("pub_brand");
		String pub_id="";
		
		//CSD
		int TableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] CSDCo=request.getParameterValues("csd_co");
		String[] CSDPack =request.getParameterValues("csd_pack");
		String[] CSDBrand =request.getParameterValues("csd_brand");
		String CSD_id="";
		//System.out.println( TableMainID);
		
		//CoolerPlacement
		int CoolerTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] CoolerPlCo=request.getParameterValues("coolplace_co");
		String[] CoolerPlPack =request.getParameterValues("coolplace_pack");
		String[] CoolerPlBrand =request.getParameterValues("coolplace_brand");
		String CoolerPl_id="";
		//System.out.println( CoolerTableMainID);
		
		//NCB
		int NCBTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] NCBCo=request.getParameterValues("ncb_co");
		String[] NCBPack =request.getParameterValues("ncb_pack");
		String[] NCBBrand =request.getParameterValues("ncb_brand");
		String NCB_id="";
		//System.out.println( NCBTableMainID);
		
		//VolCSD
		int VolCsdTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] VolCsdCo=request.getParameterValues("volcsd_co");
		String[] VolCsdPack =request.getParameterValues("volcsd_pack");
		String[] VolCsdBrand =request.getParameterValues("volcsd_brand1");
		String[] VolCsdBrand1 =request.getParameterValues("volcsd_brand2");
		String VolCsd_id="";
		//System.out.println( VolCsdTableMainID);
		
		//VolJuice
		int VolJuiceTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] VolJuiceCo=request.getParameterValues("voljuice_co");
		String[] VolJuicePack =request.getParameterValues("voljuice_pack");
		String[] VolJuiceBrand =request.getParameterValues("voljuice_brand1");
		String[] VolJuiceBrand1 =request.getParameterValues("voljuice_brand2");
		String VolJuice_id="";
		//System.out.println( VolJuiceTableMainID);
		
		//VolDrinks
		int VolDrinkTableMainID=Utilities.parseInt(request.getParameter("tableId"));
		String[] VolDrinkCo=request.getParameterValues("voldrink_co");
		String[] VolDrinkPack =request.getParameterValues("voldrink_pack");
		String[] VolDrinkBrand =request.getParameterValues("voldrink_brand1");
		String[] VolDrinkBrand1 =request.getParameterValues("voldrink_brand2");
		String VolDrink_id="";
		
		
		
		
		
		try {
			
							//---------------------- SAVE BUTTON starts here-------------------------------//
			ds.createConnection();
			ds.startTransaction();
			Statement S1 = ds.createStatement();
			ButtonID=Integer.parseInt(request.getParameter("btnId"));
		//	System.out.println("This is Button number"+ButtonID);
			if(ButtonID==1){
			
			
			String SQLMain = "Update mrd_census SET outlet_id='"+OutletID +"',created_by='"+CreatedbyID +"',is_census_shop_closed='"+IsShopClosedStatus +"',census_shop_closed_status='"+ShopClosedStatus +"',census_shop_structure='"+OutletStructure +"',census_outlet_type='"+OutletType +"',pjp_id='"+PJPID +"',shop_break_time='"+BreakTime +"',outlet_name_actual='"+ ActualOutletNamegeneral+"',census_service_type='"+ ServiceType+"',created_by='"+CreatedbyID +"',outlet_name='"+SystemOutletName +"',outlet_board_name='"+OutletNameOnBoardgeneral +"',census_distributor_Id='"+DistributorIDgeneral +"',census_distributor_name='"+DistributorName +"',census_segment='"+Location +"',census_area='"+Area +"',census_shop_type='"+ShopType +"',census_shop_potential='"+SocioEconimicClassification +"',census_distributor_town='"+MVillage +"',land_mark='"+ MLandMark+"',census_shop_location='"+ShopLocation +"',census_outlet_contact_person='"+OwnerName +"',census_outlet_contact_1='"+landline +"',census_outlet_contact_2='"+Mobile +"',census_owner_cnic_1='"+OwnerCNIC +"',census_outlet_contact_name='"+ ContactPersonName+"',census_outlet_contact_3='"+ContactPersonNo +"',census_owner_cnic_2='"+ContactPersonCNIC +"',census_email_id='"+ EmailID+"',census_outlet_address='"+OutletAddress+"',census_outlet_type='"+ OutletType+"',census_owner_contact_person_relation='"+ContactPerson +"',census_distributor_district='"+District +"',census_distributor_tehsil='"+ Tehsil+"',census_trader_channel_sub_channel='"+TradeSubChannel+"',census_area_seq_feet='"+AreaSqFT +"',census_shop_status='"+ShopStatusShopkeeper+"',census_day_off='"+DayOff_Shopkeeper +"',census_owner_contact_person_shop_opening_time='"+ShopOpeningTime_Shopkeeper+"',census_owner_contact_person_shop_close_time='"+ShopClosingTime_Shopkeeper +"',census_financial_service_1='"+ FinancialServicebtn1+"',census_financial_service_2='"+ FinancialServicebtn2+"',census_financial_service_3='"+FinancialServicebtn3 +"',census_financial_service_4='"+FinancialServicebtn4 +"',census_financial_service_5='"+FinancialServicebtn5 +"',census_financial_service_6='"+FinancialServicebtn6 +"',census_shop_selling_bev='"+SSRB +"',census_shop_selling_pet='"+PET +"',census_shop_selling_nrb='"+TEtra +"',census_shop_selling_can='"+CAN +"',census_exclusivity_agreement_1='"+ExAgreeBase +"',census_exclusivity_agreement_pkr_ssrb_1='"+PerRawCaseSSRB +"',census_exclusivity_agreement_pkr_pet_1='"+PerRawCasePET+"',aggr_exp_date_mm='"+ Month1+"',aggr_exp_date_yy='"+Year1+"',census_shop_agreement_type='"+AgreeType +"',census_shop_agreement_period='"+AgreePeriod +"',census_shop_agreement_type1_1='"+AgreeType1a +"',census_shop_agreement_type1_2='"+AgreeType1b +"',census_shop_agreement_type1_3='"+AgreeType1c +"',census_shop_agreement_type1_4='"+ AgreeType1d+"',census_shop_agreement_type1_5='"+AgreeType1e+"',census_shop_agreement_type_other='"+AgreeType1f +"',aggr_exp_date_mm_pi='"+ MonthPI+"',aggr_exp_date_yy_pi='"+YearPI +"',census_shop_agreement_type_pi='"+AgreeTypePI +"',census_shop_agreement_period_pi='"+AgreePeriodPI +"',census_shop_agreement_type1_pi_1='"+ AgreeType2PIa+"',census_shop_agreement_type1_pi_2='"+AgreeType2PIb+"',census_shop_agreement_type1_pi_3='"+AgreeType2PIc +"',census_shop_agreement_type1_pi_4='"+AgreeType2PId +"',census_shop_agreement_type1_pi_5='"+AgreeType2PIe +"',census_shop_agreement_type_pi_other='"+AgreeType2PIf +"',census_exclusivity_agreement_pkr_ssrb_pi_1='"+PerRawCaseSSRBPI +"',census_exclusivity_agreement_pkr_pet_pi_1='"+  PerRawCasePETPI+"',aggr_exp_date_mm_ko='"+MonthKO +"',aggr_exp_date_yy_ko='"+YearKO +"',census_shop_agreement_type_ko='"+AgreeTypeKO +"',census_shop_agreement_period_ko='"+ AgreePeriodKO+"',census_shop_agreement_type1_ko_1='"+ AgreeTypeKOa+"',census_shop_agreement_type1_ko_2='"+AgreeTypeKOb +"',census_shop_agreement_type1_ko_3='"+AgreeTypeKOc +"',census_shop_agreement_type1_ko_4='"+AgreeTypeKOd +"',census_shop_agreement_type1_ko_5='"+AgreeTypeKOe +"',census_shop_agreement_type_ko_other='"+AgreeTypeKOf +"',census_exclusivity_agreement_pkr_ssrb_ko_1='"+PerCaseSSRBKO +"',census_exclusivity_agreement_pkr_pet_ko_1='"+PerCasePETKO +"',census_partially_timing='"+PartiallycloseTime +"',census_feeded_stock_percentage_1='"+SuppliedKoDistributor +"',census_feeded_stock_percentage_2='"+SuppliedKoWholeseller +"',census_feeded_stock_percentage_3='"+ SuppliedKoMobiler+"',census_feeded_stock_percentage_4='"+ SuppliedKoDealer+"',census_feeded_stock_1_p_pi_21='"+SuppliedPIDistributor+"',census_feeded_stock_2_p_pi_21='"+SuppliedPIWholeseller +"',census_feeded_stock_3_p_pi_21='"+SuppliedPIMobiler +"',census_feeded_stock_4_p_pi_21='"+SuppliedPIDealer +"',supply_frequency='"+SupplyFrequencyPI +"',supply_frequency_ko='"+SupplyFrequencyKO +"',supply_frequency_g='"+SupplyFrequencyGourment +"',supply_frequency_cn='"+SupplyFrequencyColaNext +"',census_shop_researcher_closed_status='"+CoveredbyresearcherCompany +"',cash_machine_quantity='"+CashMachineQuantity_Status +"',census_shop_establishment_history='"+ShopEstablishedHistory_status +"',census_business_structure='"+BusinessStrutcure +"',census_customer_type='"+ ShopType_status+"',census_customer_type_wholesale_percent='"+wholeSalePer_status +"',census_customer_type_retailer_percent='"+RetailerPer_status +"',pi_csd='"+ pi_csd+"',pi_sting='"+pi_sting +"',stock_storage_loc1='"+StockStoragelocation1 +"',stock_storage_loc2='"+StockStoragelocation2 +"',stock_storage_loc3='"+StockStoragelocation3 +"',stock_storage_loc4='"+StockStoragelocation4 +"',cooler_visible='"+Visible_CoolerCode +"',cooler_access='"+Accessible_CoolderCode +"',ko='"+ko +"', updated_on=now(),updated_by="+UserID+" where id="+ID;
			Statement GenShopStatusStat = ds.createStatement();
			GenShopStatusStat.executeUpdate(SQLMain);
			//System.out.println(SQLMain);
			
			//COOL TYPES
			Statement CoolTypeS1 =ds.createStatement();
			Statement CoolTypeS2 =ds.createStatement();
			Statement CoolTypeS3 = ds.createStatement();
			Statement CoolTypeS4 = ds.createStatement();
			String CoolselectCenID="Select census_id FROM mrd_census_cooler_types  where id="+TableID; 
			//System.out.println(CoolselectCenID);
			 ResultSet rsselectcooltype = CoolTypeS1.executeQuery(CoolselectCenID);
		      
		     
		      while (rsselectcooltype.next())
		      {
		        CoolTypeId = rsselectcooltype.getString("census_id");
		      }
		        
		        // print the results
		       
			String CoolTypedeleteQuery="Delete FROM mrd_census_cooler_types  where id="+TableID;
			CoolTypeS2.executeUpdate(CoolTypedeleteQuery);
			//System.out.println(CoolTypedeleteQuery);
			//System.out.println(CoolTypeCo.length);
			
			if(CoolTypeCo!=null){
			
				
				for(int i = 0; i <CoolTypeCo.length; i++)
				{
					String CoolTypeCompanyName= (CoolTypeCo[i]);
					String CoolTypeLabel= (CoolTypePack [i]);
					String CoolTypebrand= (CoolTypeBrand[i]);
					
					String Coolinsertsql="insert into mrd_census_cooler_types (id,census_id,cooler_type_company_id,cooler_type_type_id, quantity) VALUES ('"+TableID+"','"+CoolTypeId+"','"+ CoolTypeCompanyName+"','"+CoolTypeLabel +"','"+CoolTypebrand +"')";
					
					
					CoolTypeS3.executeUpdate(Coolinsertsql);
					//System.out.println(Coolinsertsql);
					//System.out.println(i);
				}
			}
			
			
			//Cooler COde
			
			Statement CoolCodeS1 = ds.createStatement();
			Statement CoolCodeS2=ds.createStatement();
			Statement CoolCodeS3 = ds.createStatement();
			Statement CoolCodeS4 = ds.createStatement();
			String CoolCselectCenID="Select census_id FROM  mrd_census_cooler_code where id="+CoolCodeTableMainID; 
			//System.out.println(CoolCselectCenID);
			 ResultSet rsselectCoolCode = CoolCodeS1.executeQuery(CoolCselectCenID);
		      
		      // iterate through the java resultset
		      while (rsselectCoolCode.next())
		      {
		    	  CoolCode_id = rsselectCoolCode.getString("census_id");
		      }
		      //System.out.println("this is"+CoolCode_id);
				 
		        // print the results
		       
			String CoolCodedeleteQuery="Delete FROM mrd_census_cooler_code  where id="+CoolCodeTableMainID;
			CoolCodeS2.executeUpdate(CoolCodedeleteQuery);
			//System.out.println(CoolCodedeleteQuery);
			
			
			if(CoolCode!=null){
			
				
				for(int i = 0; i <CoolCode.length; i++)
				{
					String CoolCodeTOT= (CoolCode[i]);
					if(CoolCode[i]!=""){
					
					String CoolCodeinsertsql="insert into mrd_census_cooler_code (id,census_id,tot_code) VALUES ('"+CoolCodeTableMainID +"','"+CoolCode_id +"','"+ CoolCodeTOT+"')";
					
					//System.out.println(CoolCodeinsertsql);
					CoolCodeS3.executeUpdate(CoolCodeinsertsql);
					}
				}
			}
			
			//Publicity
			Statement PubS1 = ds.createStatement();
			Statement PubS2=ds.createStatement();
			Statement PubS3 = ds.createStatement();
			Statement PubS4 = ds.createStatement();
			String PubselectCenID="Select census_id FROM  mrd_census_publicity_types where id="+PUBTableMainID; 
			//System.out.println(PubselectCenID);
			 ResultSet rsselectPub = PubS1.executeQuery(PubselectCenID);
		      
		      // iterate through the java resultset
		      while (rsselectPub.next())
		      {
		    	 pub_id = rsselectPub.getString("census_id");
		      }
		     // System.out.println(pub_id);
				 
		        // print the results
		       
			String deleteQuery="Delete FROM mrd_census_publicity_types  where id="+PUBTableMainID;
			PubS2.executeUpdate(deleteQuery);
			//System.out.println(deleteQuery);
			
			
			if(PubCo!=null){
			
				
				for(int i = 0; i <PubCo.length; i++)
				{
					String PubCompanyName= (PubCo[i]);
					String PubLabel= (PubPack[i]);
					String Pubbrand= (PubBrand[i]);
					
					String Pubinsertsql="insert into mrd_census_publicity_types (id,census_id,publicity_type_company_id,publicity_type_type_id,quantity ) VALUES ('"+PUBTableMainID +"','"+pub_id +"','"+ PubCompanyName+"','"+PubLabel +"','"+Pubbrand +"')";
					
					//System.out.println(Pubinsertsql);
					PubS3.executeUpdate(Pubinsertsql);
				}
			}
			
			
			
			
			//CSD
			Statement s = ds.createStatement();
			Statement s2=ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement ss = ds.createStatement();
			String selectCenID="Select census_id FROM mrd_census_csd_types  where id="+TableMainID; 
			//System.out.println(selectCenID);
			 ResultSet rsselectCSD = ss.executeQuery(selectCenID);
		      
		     
		      while (rsselectCSD.next())
		      {
		        CSD_id = rsselectCSD.getString("census_id");
		      }
		        //System.out.println(CSD_id);
		        
		       
			String CSDdeleteQuery="Delete FROM mrd_census_csd_types  where id="+TableMainID;
			s2.executeUpdate(CSDdeleteQuery);
			//System.out.println(CSDdeleteQuery);
			if(CSDCo!=null){
			
				
				for(int i = 0; i <CSDCo.length; i++)
				{
					String CSDCompanyName= (CSDCo[i]);
					String Label= (CSDPack [i]);
					String brand= (CSDBrand[i]);
					
					String CSDinsertsql="insert into mrd_census_csd_types (id,census_id,csd_company_id,csd_package_id, csd_brand) VALUES ('"+TableMainID +"','"+CSD_id +"','"+ CSDCompanyName+"','"+Label +"','"+brand +"')";
					
					//System.out.println(CSDinsertsql);
					s1.executeUpdate(CSDinsertsql);
				}
			}
			
			//Cooler Placement
			Statement coolPS1 = ds.createStatement();
			Statement coolPS2=ds.createStatement();
			Statement coolPS3 = ds.createStatement();
			Statement coolPS4 = ds.createStatement();
			String coolPselectCenID="Select census_id FROM  mrd_census_pace where id="+CoolerTableMainID; 
			//System.out.println(coolPselectCenID);
			 ResultSet rsselectcoolP = coolPS1.executeQuery(coolPselectCenID);
		      
		     
		      while (rsselectcoolP.next())
		      {
		    	  CoolerPl_id = rsselectcoolP.getString("census_id");
		      }
		      //System.out.println(CoolerPl_id);
				 
		        // print the results
		       
			String coolPdeleteQuery="Delete FROM mrd_census_pace  where id="+CoolerTableMainID;
			coolPS2.executeUpdate(coolPdeleteQuery);
			//System.out.println(coolPdeleteQuery);
			
			
			if(CoolerPlCo!=null){
			
				
				for(int i = 0; i <CoolerPlCo.length; i++)
				{
					String coolPCompanyName= (CoolerPlCo[i]);
					String coolPLabel= (CoolerPlPack[i]);
					String coolPbrand= (CoolerPlBrand[i]);
					
					String coolPinsertsql="insert into mrd_census_pace (id,census_id,pace_company_id,pace_package_id,pace_brand ) VALUES ('"+CoolerTableMainID +"','"+CoolerPl_id +"','"+ coolPCompanyName+"','"+coolPLabel +"','"+coolPbrand +"')";
					
					//System.out.println(coolPinsertsql);
					coolPS3.executeUpdate(coolPinsertsql);
				}
			}
			
			//NCB
			Statement NcbS1 = ds.createStatement();
			Statement NcbS2=ds.createStatement();
			Statement NcbS3 = ds.createStatement();
			Statement NcbS4 = ds.createStatement();
			String NcbselectCenID="Select census_id FROM  mrd_census_ncb_types where id="+NCBTableMainID; 
			//System.out.println(NcbselectCenID);
			 ResultSet rsselectNcb = NcbS1.executeQuery(NcbselectCenID);
		      
		      
		      while (rsselectNcb.next())
		      {
		    	  NCB_id = rsselectNcb.getString("census_id");
		      }
		     // System.out.println(NCB_id);
				 
		        // print the results
		       
			String NcbdeleteQuery="Delete FROM mrd_census_ncb_types  where id="+NCBTableMainID;
			NcbS2.executeUpdate(NcbdeleteQuery);
			//System.out.println(NcbdeleteQuery);
			
			
			if(NCBCo!=null){
			
				
				for(int i = 0; i <NCBCo.length; i++)
				{
					String NcbCompanyName= (NCBCo[i]);
					String NcbLabel= (NCBPack[i]);
					String Ncbbrand= (NCBBrand[i]);
					
					String Ncbinsertsql="insert into mrd_census_ncb_types (id,census_id,ncb_company_id,ncb_package_id,ncb_brand ) VALUES ('"+NCBTableMainID +"','"+NCB_id +"','"+ NcbCompanyName+"','"+NcbLabel +"','"+Ncbbrand +"')";
					
					//System.out.println(Ncbinsertsql);
					NcbS3.executeUpdate(Ncbinsertsql);
				}
			}
			
			//VolCSD
			Statement VolCsdS1 = ds.createStatement();
			Statement VolCsdS2=ds.createStatement();
			Statement VolCsdS3 = ds.createStatement();
			Statement VolCsdS4 = ds.createStatement();
			String VolCsdselectCenID="Select census_id FROM  mrd_census_vol_csd where id="+VolCsdTableMainID; 
			//System.out.println(VolCsdselectCenID);
			 ResultSet rsselectVolCsd = VolCsdS1.executeQuery(VolCsdselectCenID);
		      
		      // iterate through the java resultset
		      while (rsselectVolCsd.next())
		      {
		    	  VolCsd_id = rsselectVolCsd.getString("census_id");
		      }
		     // System.out.println(VolCsd_id);
				 
		        // print the results
		       
			String VolCsddeleteQuery="Delete FROM mrd_census_vol_csd  where id="+VolCsdTableMainID;
			VolCsdS2.executeUpdate(VolCsddeleteQuery);
			//System.out.println(VolCsddeleteQuery);
			
			
			if(VolCsdCo!=null){
			
				
				for(int i = 0; i <VolCsdCo.length; i++)
				{
					String VolCsdCompanyName= (VolCsdCo[i]);
					String VolCsdLabel= (VolCsdPack[i]);
					String VolCsdbrand= (VolCsdBrand[i]);
					String VolCsdbrand1= (VolCsdBrand1[i]);
					
					String VolCsdinsertsql="insert into mrd_census_vol_csd (id,census_id,vol_csd_company_id,vol_csd_package_id,vol_csd_brand,vol_csd_mde ) VALUES ('"+VolCsdTableMainID +"','"+NCB_id +"','"+ VolCsdCompanyName+"','"+VolCsdLabel +"','"+VolCsdbrand +"','"+VolCsdbrand1 +"')";
					
					//System.out.println(VolCsdinsertsql);
					VolCsdS3.executeUpdate(VolCsdinsertsql);
				}
			}
			
			//VolJuice
			Statement VoljuiceS1 = ds.createStatement();
			Statement VoljuiceS2=ds.createStatement();
			Statement VoljuiceS3 = ds.createStatement();
			Statement VoljuiceS4 = ds.createStatement();
			String VoljuiceselectCenID="Select census_id FROM  mrd_census_vol_juice where id="+VolJuiceTableMainID; 
			//System.out.println(VoljuiceselectCenID);
			 ResultSet rsselectVoljuice = VoljuiceS1.executeQuery(VoljuiceselectCenID);
		      
		      // iterate through the java resultset
		      while (rsselectVoljuice.next())
		      {
		    	  VolJuice_id = rsselectVoljuice.getString("census_id");
		      }
		      //System.out.println(VolJuice_id);
				 
		        // print the results
		       
			String VoljuicedeleteQuery="Delete FROM mrd_census_vol_juice  where id="+VolJuiceTableMainID;
			VoljuiceS2.executeUpdate(VoljuicedeleteQuery);
			//System.out.println(VoljuicedeleteQuery);
			
			
			if(VolJuiceCo!=null){
			
				
				for(int i = 0; i <VolJuiceCo.length; i++)
				{
					String VolJuiceCompanyName= (VolJuiceCo[i]);
					String VolJuiceLabel= (VolJuicePack[i]);
					String VolJuicebrand= (VolJuiceBrand[i]);
					String VolJuicebrand1= (VolJuiceBrand1[i]);
					
					String VolJuiceinsertsql="insert into mrd_census_vol_juice (id,census_id,vol_juice_company_id,vol_juice_package_id,vol_juice_brand,vol_juice_mde ) VALUES ('"+VolJuiceTableMainID +"','"+VolJuice_id +"','"+ VolJuiceCompanyName+"','"+VolJuiceLabel +"','"+VolJuicebrand +"','"+VolJuicebrand1 +"')";
					
					//System.out.println(VolJuiceinsertsql);
					VoljuiceS3.executeUpdate(VolJuiceinsertsql);
				}
			}
			
			//VolDrinks
			Statement VoldrinkS1 = ds.createStatement();
			Statement VoldrinkS2=ds.createStatement();
			Statement VoldrinkS3 = ds.createStatement();
			Statement VoldrinkS4 = ds.createStatement();
			String VoldrinkselectCenID="Select census_id FROM  mrd_census_vol_drink where id="+VolJuiceTableMainID; 
			//System.out.println(VoldrinkselectCenID);
			 ResultSet rsselectVolDrink = VoldrinkS1.executeQuery(VoldrinkselectCenID);
		      
		      // iterate through the java resultset
		      while (rsselectVolDrink.next())
		      {
		    	  VolDrink_id = rsselectVolDrink.getString("census_id");
		      }
		      //System.out.println(VolJuice_id);
				 
		        // print the results
		       
			String VoldrinkdeleteQuery="Delete FROM mrd_census_vol_drink  where id="+VolJuiceTableMainID;
			VoldrinkS2.executeUpdate(VoldrinkdeleteQuery);
			//System.out.println(VoldrinkdeleteQuery);
			
			
			if(VolDrinkCo!=null){
			
				
				for(int i = 0; i <VolDrinkCo.length; i++)
				{
					String VolDrinkCompanyName= (VolDrinkCo[i]);
					String VolDrinkLabel= (VolDrinkPack[i]);
					String VolDrinkbrand= (VolDrinkBrand[i]);
					String VolDrinkbrand1= (VolDrinkBrand1[i]);
					
					String VolDrinkinsertsql="insert into mrd_census_vol_drink (id,census_id,vol_drink_company_id,vol_drink_package_id,vol_drink_brand,vol_drink_mde ) VALUES ('"+VolJuiceTableMainID +"','"+VolDrink_id +"','"+ VolDrinkCompanyName+"','"+VolDrinkLabel +"','"+VolDrinkbrand +"','"+VolDrinkbrand1 +"')";
					
					//System.out.println(VolDrinkinsertsql);
					VoldrinkS3.executeUpdate(VolDrinkinsertsql);
				}
			}
			
			
			
			obj.put("success", "true");
			//obj.put("DeskSaleID", DeskSaleEditID);
			
			
			
			GenShopStatusStat.close();
				//CoolType
			
			CoolTypeS2.close();
			CoolTypeS3 .close();
			CoolTypeS4.close();
			
				//Cool Code
			CoolCodeS1.close();
			CoolCodeS2.close();
			CoolCodeS3.close();
			CoolCodeS4.close();
			
				//Publicity
			PubS1.close();
			PubS2.close();
			PubS3.close();
			PubS4.close();
			
			//CSD
			s1.close();
			s.close();
			
			//Cooler Placement
			coolPS1.close();
			coolPS2.close();
			coolPS3.close();
			coolPS4.close();
			
			//NCB

			NcbS1.close();
			NcbS2.close();
			NcbS3.close();
			NcbS4.close();
			
			//VolCSD
			VolCsdS1.close();
			VolCsdS2.close();
			VolCsdS3.close();
			VolCsdS4.close();
			
			//VolJuice

			VoljuiceS1.close();
			VoljuiceS2.close();
			VoljuiceS3.close();
			VoljuiceS4.close();
			
			//VolDrinks
			VoldrinkS1.close();
			VoldrinkS2.close();
			VoldrinkS3.close();
			VoldrinkS4.close();
			
		}
			
			//---------------------- SAVE BUTTON ends here-------------------------------//
			
			
			//----------------------Delete button Starts here-------------------------------//
			
			else if(ButtonID==2){
				int deleteElementID=Integer.parseInt(request.getParameter("CenID"));
				
				String SQLDelete="Update mrd_census set is_deleted=1 where id="+deleteElementID;
				System.out.println("delete Query"+""+SQLDelete);
				
				S1.executeUpdate(SQLDelete);
				obj.put("successfull", "true");
				
			}
					
					
					
			//----------------------Delete button ends here-------------------------------//
			
			S1.close();
			ds.commit();
			ds.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally{
				try {
					ds.dropConnection();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}

		
		
		
		
		
		
				
				
				
				
		out.print(obj);
		out.close();
		
		
		
	}
		
	
}