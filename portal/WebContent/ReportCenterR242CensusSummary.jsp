<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<style>
td{
font-size: 8pt;
}
th{
font-size: 9pt;
}

#map {
        width: 100%;
        height: 400px;
        margin-top: 10px;
      }

</style>



<script type="text/javascript" src="js/Report242.js?111=111"></script>



<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 306;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

int CensusID = Utilities.parseInt(request.getParameter("CensusID"));
String WhereCensusID ="";

//System.out.println(CensusIDString);




//Distributor

boolean IsDistributorSelected=false;
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");  
	IsDistributorSelected = true;
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";

if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and census_distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereDistributors = " and dbpauov.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Outlet Type


String OutletTypes="";
String SelectedOutletTypeArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType") != null){
	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll ="";


for(int i=0;i<SelectedOutletTypeArray.length;i++){
	if(SelectedOutletTypeArray[i].equals("Discounted - All")){	
		WhereDiscountedAll = " and co.id in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Fixed")){	
		WhereDiscountedFixed = " and co.id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Per Case")){	
		WhereDiscountedPerCase = " and co.id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Non Discounted")){	
		WhereNonDiscountedAll = " and co.id not in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Active")){	
		WhereActive = " and co.is_active=1 ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Deactivated")){	
		WhereDeactivated = " and co.is_active=0 ";
	}
}



%>

			
<form id="general_tab">
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			
				
			<li data-role="list-divider" data-theme="a">Outlet Summary</li>
			<%if(CensusID!=0){ %>
			
			<li data-role="list-divider" data-theme="b">General</li>
			
			<li>		
				
				
				<%
				
				long CensusIDMain=0;
				long TableMainID=0;
				
				String OutletID="";
				String OutletName="";
				String OutletNameOB="";
				String Region="";
				String Distributor="";
				String Area="";
				String ShopType="";
				String District="";
				String Village="";
				String LandMark="";
				String Location="";
				String ShopLocation="";
				String OwnerName="";
				String OwnerContactNo1="";
				String OwnerContactNo2="";
				String OwnerCNIC="";
				String ContactPersonName="";
				String ContactPerson="";
				String ContactPersonNo="";
				String ContactPersonCNIC="";
				String FBID="";
				String EmailID="";
				String Distributor_name ="";	
				String Distributor_id="";
				Long PJPID=null ;
				String PJPName="";
				Long CreatedBYId=null;
				String CreatedByName="";
				
				
				 int OutletDistrictDDL=0;
				 int OutletTehsilDDL=0;
				 int OutletTehsil=0;
				 int TradeSubChannelDDL=0;
				 int ContactPersonDDL=0;
				
				
				 //Shopkeeper
				 
				 String IsShopClosed="";
				 String ShopClosedStatus="";
				 String DayOff="";
				 String BreakTime="";
				 String PartialTiming="";
				 int PartialTimingRbtn=0;
				 String SDistributor="";
				 String WholeSaller="";
				 String SubDistributor="";
				 String SuppliedBy="";
				 int DayOffDDL=0;
				 String FinancialService="";
				 int FinancialServiceVal=0;
				 int FinancialServiceChbtn1=0;
				 int FinancialServiceChbtn2=0;
				 int FinancialServiceChbtn3=0;
				 int FinancialServiceChbtn4=0;
				 int FinancialServiceChbtn5=0;
				 int FinancialServiceChbtn6=0;
				 int PerRawCaseDiscSSRBPIRbtn=0;
				 
				 String ShopOpeningTimeDDL="";
				 String ShopCloseTimeDDL="";
				 
				 String EasyPaisa="";
				 String Upaisa="";
				 String MobiCash="";
				 String Warid="";
				 String TimePay="";
				 String Other="";
				 
				 
				 String ShopOpeningTime="";
				 String ShopCloseTime="";
				 
				 
				 //Status
				 
				 String ShopStatus="";
				 int ShopStatusVal=0;
				 int ShopStatusRbtn=0;
				 String ShopHistory="";
				 int ShopHistoryDDL=0;
				 String BusinessStructure="";
				 int BusinessStructureVal=0;
				 int BusinessStructureRbtn=0;
				 String SocioEco="";
				 String SShopType="";
				 int SShopTypeVal=0;
				 int  SShopTypeRbtn=0;
				 String Wholesale="";
				 String Retailer="";
				 String ServiceType="";
				 String CSDTurnOver="";
				 String TradeChannel="";
				 String TradeSubChannel="";
				 String SupplyFrequency="";
				 int SupplyFrequencyDDL=0;
				 String SupplyFrequencyKO="";
				 int SupplyFrequencyDDLKO=0;
				 String SupplyFrequencyGormet="";
				 int SupplyFrequencyDDLGO=0;
				 String SupplyFrequencyColaNext="";
				 int SupplyFrequencyDDLCN=0;
				 
				 
				 
				 //Beverages
				 
				 int PICSD=0;
				 int PISting=0;
				 int KO=0;
				
				 String Exclusivity="";
				 String ExPepsi="";
				 String ExCoke="";
				 String ExGourmet="";
				 String ExMezan="";
				 String ExOther="";
				 String DiscountStatus="";
				 String DPerCase="";
				 String DTradePaymentFix="";
				 String DTradePaymentTarget="";

				 
				 String IsCoveredByRCompnay="";
				 int IsCoveredByRCompnayVal=0;
				 int IsCoveredByRCompnayRbtn=0;
				 String IsCoveredByRCompnayDuration="";
				 
				 String StockStorageLocation1="";
				 String StockStorageLocation2="";
				 String StockStorageLocation3="";
				 String StockStorageLocation4="";
				 
				 String StockStorageLocation="";
				 int StockStorageLocationChbtn=0;
				 int StockStorageLocationChbtn1=0;
				 int StockStorageLocationChbtn2=0;
				 int StockStorageLocationChbtn3=0;
				 int StockStorageLocationChbtn4=0;
				 
				 int CashMachineQuantity=0;
				 
				 int BeveragesSellingFullCasesSSRB=0;
				 int BeveragesSellingFullCasesPET=0;
				 int BeveragesSellingFullCasesTETRA=0;
				 int BeveragesSellingFullCasesCAN=0;
				 
				 String ExclusivityAgreementPI="";
				 String ExclusivityAgreementKO="";
				 String ExclusivityAgreementGou="";
				 String ExclusivityAgreementMezan="";
				 String ExclusivityAgreementOther="";
				 int ExclusivityAgreementPIRbtn=0;
				 int ExclusivityAgreementKORbtn=0;
				 int ExclusivityAgreementGouRbtn=0;
				 int ExclusivityAgreementMezanRbtn=0;
				 int ExclusivityAgreementOtherRbtn=0;
				 
				 
				 String VisibleColors="";
				 String AccessibleColors="";
				 
				 
				 //Shopkeeper
				//per raw case SSRB
				 String PerRawCaseDiscSSRB="";
				 int PerRawCaseDiscSSRBRadbtn=0;
				 int  PerRawCaseDiscSSRBRadbtnKO=0;
				 int  PerRawCaseDiscSSRBRadbtnPI=0;
				 int perCaseSSRBKORbtn=0;
				 
				 //per raw case PET
				 int perCasePETKORbtn=0;
				 int perCaseSSRBPIRbtn=0;
				 String PerRawCaseDiscPET="";
				 int PerRawCaseDiscPETRadbtn=0;
				 int PerRawCaseDiscPETPIRadbtn=0;
				 int PerRawCaseDiscPETRadbtnKO=0;
				 int AgreementType2DiscPIRbtn=0;
				int AgreementType2SigPIRbtn=0;
				int AgreementType2AnnualPIRbtn=0;
				int AgreementType2ExculPIRbtn=0;
				int AgreementType2IncPIRbtn=0;
			    String AgreementType2OtherPIRbtn="";
				int AgreementType2Rbtn1=0;
				int AgreementType2Rbtn2=0;
				int AgreementType2Rbtn3=0;
				int AgreementType2Rbtn4=0;
				int AgreementType2Rbtn5=0;
				String AgreementType2Rbtn6="";
				int AgreementType2DKORbtn=0;
				int AgreementType2SKORbtn=0;
				int AgreementType2AKORbtn=0;
				int AgreementType2EXKORbtn=0;
				int AgreementType2VolKORbtn=0;
				String AgreementTypeOKORbtn="";
				 
				 
				 String AgreementExpDate="";
				 int AgreementExpdateMonth=0;
				 int AgreementExpdateMonthDDL=0;
				 int AgreementExpdateYear=0;
				 int AgreementExpdateYearDDL=0;
				 String AgreementType="";
				 int AgreementTypeRbtn=0;
				 String AgreementPeriod="";
				 int AgreementPeriodRbtn=0;
				 
				 String AgreementType2="";
				 int AgreementType2Rbtn=0;
				 
				 
				 String PerRawCaseDiscSSRBPI="";
				 String PerRawCaseDiscPETPI="";
				 
				 String AgreementExpDatePI="";
				 int AgreementExpdateMonthPI=0;
				 int AgreementExpdateMonthDDLPI=0;
				 int AgreementExpdateYearPI=0;
				 int AgreementExpdateYearDDLPI=0;
				 
				 String AgreementTypePI="";
				 int AgreementTypePIRbtn=0;
				
				 String AgreementPeriodPI="";
				 int AgreementPeriodPIRbtn=0;
				 
				 String AgreementType2PI="";
				 int AgreementType2PIRbtn=0;
				 
				 String PerRawCaseDiscSSRBKO="";
				 String PerRawCaseDiscPETKO="";
				 
				 String AgreementExpDateKO="";
				 int AgreementExpdateMonthKO=0;
				 int AgreementExpdateMonthDDLKO=0;
				 int AgreementExpdateYearKO=0;
				 int AgreementExpdateYearDDLKO=0;
				 
				 String AgreementTypeKO="";
				 int AgreementTypeKORbtn=0;
				 String AgreementPeriodKO="";
				 int AgreementPeriodKORbtn=0;
				 
				 String AgreementType2KO="";
				 
				 
				 
				 String SuppliedByDistributorKO="";
				 String SuppliedByWholeSellerKO="";
				 String SuppliedByMobilerKO="";
				 String SuppliedByDealerKO="";
				 
				 String SuppliedByDistributorPI="";
				 String SuppliedByWholeSellerPI="";
				 String SuppliedByMobilerPI="";
				 String SuppliedByDealerPI="";
				 
				 
				 //General Tab Modified
				 
				 String OutletNameActual="";
				 String PJP="";
				 String OutletType="";
				 String OutletAddress="";
				 String MDistrict="";
				 String MTehsil="";
				 String MVillage="";
				 String MLandMark="";
				 String ShopClosedStatusN="";
				 int ShopClosedStatusNRbtn=0;
				 int IsShopClosedStatusNRbtn=0;
				 String OutletStructure="";
				 String AreaSqFt="";
				 
				 String CreatedBy="";
				 
				 double Lati=0;
				 double Longi=0;
				 double Accuracy=0;
				 int AreaRadioBtn=0;
				 
				
				 
				//System.out.println("SELECT *,(select display_name from users u where u.id=mc.created_by) created_display_name FROM mrd_census mc where mc.id="+CensusID);
				
				ResultSet rs = s.executeQuery("SELECT *,(select display_name from users u where u.id=mc.created_by) created_display_name FROM mrd_census mc where mc.id="+CensusID);
				while(rs.next()){
					BreakTime=rs.getString("shop_break_time");
					CensusIDMain = rs.getLong("census_id");
					TableMainID= rs.getLong("id");
					
					
					  VisibleColors= rs.getString("cooler_visible");
					  AccessibleColors= rs.getString("cooler_access");
					
					CreatedBYId=rs.getLong("created_by");
					CreatedByName=rs.getString("created_display_name");
					CreatedBy = rs.getLong("created_by")+" - "+rs.getString("created_display_name");
					  
					
					OutletID= rs.getString("outlet_id");
					 OutletName=rs.getString("outlet_name");;
					 OutletNameOB=rs.getString("outlet_board_name");
					 OutletNameActual = rs.getString("outlet_name_actual");
					 OutletAddress=rs.getString("census_outlet_address");
					 
					 
					 
					  Lati=rs.getDouble("lat");
					  Longi=rs.getDouble("lng");
					  Accuracy=rs.getDouble("accuracy");
					 
					 //Outlet District
					 if(rs.getInt("census_distributor_district")==1){
						 MDistrict="FAISALABAD"; 
					 }else if(rs.getInt("census_distributor_district")==2){
						 MDistrict="TOBA TEKSINGH"; 
					 }else if(rs.getInt("census_distributor_district")==3){
						 MDistrict="JHANG"; 
					 }else if(rs.getInt("census_distributor_district")==4){
						 MDistrict="CHINIOT"; 
					 }
					 
					
						//Outlet District
							 if(rs.getInt("census_distributor_district")==1){
								 OutletDistrictDDL=1; 
							 }else if(rs.getInt("census_distributor_district")==2){
								 OutletDistrictDDL=2;  
							 }else if(rs.getInt("census_distributor_district")==3){
								 OutletDistrictDDL=3; 
							 }else if(rs.getInt("census_distributor_district")==4){
								 OutletDistrictDDL=4; 
							 }
					 
					 //Outlet Tehsil
					 
					 if(rs.getInt("census_distributor_tehsil")==1){
						 MTehsil="CHINIOT";
					 }else if(rs.getInt("census_distributor_tehsil")==2){
						 MTehsil="BHOWANA";
					 }else if(rs.getInt("census_distributor_tehsil")==3){
						 MTehsil="JHANG";
					 }else if(rs.getInt("census_distributor_tehsil")==4){
						 MTehsil="GOJRA";
					 }else if(rs.getInt("census_distributor_tehsil")==5){
						 MTehsil="FAISALABAD";
					 }else if(rs.getInt("census_distributor_tehsil")==6){
						 MTehsil="TANDLIANWALA";
					 }else if(rs.getInt("census_distributor_tehsil")==7){
						 MTehsil="SAMUNDRI";
					 }else if(rs.getInt("census_distributor_tehsil")==8){
						 MTehsil="JARANWALA";
					 }else if(rs.getInt("census_distributor_tehsil")==9){
						 MTehsil="JHUMRA";
					 }
					 
 					//Outlet Tehsil
					 
					 if(rs.getInt("census_distributor_tehsil")==1){
						 OutletTehsilDDL=1;
					 }else if(rs.getInt("census_distributor_tehsil")==2){
						 OutletTehsilDDL=2;
					 }else if(rs.getInt("census_distributor_tehsil")==3){
						 OutletTehsilDDL=3;
					 }else if(rs.getInt("census_distributor_tehsil")==4){
						 OutletTehsilDDL=4;
					 }else if(rs.getInt("census_distributor_tehsil")==5){
						 OutletTehsilDDL=5;
					 }else if(rs.getInt("census_distributor_tehsil")==6){
						 OutletTehsilDDL=6;
					 }else if(rs.getInt("census_distributor_tehsil")==7){
						 OutletTehsilDDL=7;
					 }else if(rs.getInt("census_distributor_tehsil")==8){
						 OutletTehsilDDL=8;
					 }else if(rs.getInt("census_distributor_tehsil")==9){
						 OutletTehsilDDL=9;
					 }
					 
					 MVillage = rs.getString("census_distributor_town");
					 MLandMark = rs.getString("land_mark");
					 
					 //PJP ID
					 if(!rs.getString("pjp_id").equals("null")){
						 ResultSet rs23 = s2.executeQuery("SELECT * FROM pep.distributor_beat_plan where id="+rs.getLong("pjp_id"));
						 while(rs23.next()){
							PJPID= rs.getLong("pjp_id");
							PJPName=rs23.getString("label");
							 PJP =  rs.getLong("pjp_id")+" - "+rs23.getString("label");
						 }
					 }
					 //Outlet Type
					 
					 if(rs.getLong("census_outlet_type")==1){
						 OutletType="Individual Outlet";
					 }else if(rs.getLong("census_outlet_type")==2){
						 OutletType="Clustered Outlet";
					 }
					 //outlettype Radiuo bttn
					 
					 if(rs.getLong("census_outlet_type")==1){
						 OutletType="Individual Outlet";
					 }else if(rs.getLong("census_outlet_type")==2){
						 OutletType="Clustered Outlet";
					 }
					 
					 
					 //Shop Closed Status
					 
					 //if(rs.getInt("is_census_shop_closed")==1){
						 if(rs.getInt("census_shop_closed_status")==1){
							 ShopClosedStatusN="Permanently";
						 }else{
							 ShopClosedStatusN="Temporarily";
						 }
					 //}
 					//Shop Closed Status radiobtn
					 if(rs.getInt("is_census_shop_closed")==1){
					IsShopClosedStatusNRbtn=1; 
					 }		
 					if(rs.getInt("is_census_shop_closed")==1){
						 if(rs.getInt("census_shop_closed_status")==1){
							 ShopClosedStatusNRbtn=1;
						 }else{
							 ShopClosedStatusNRbtn=2;
						 }
					}
					 
					 //Outlet Structure
					 if(rs.getInt("census_shop_structure")==1){
						 OutletStructure = "Fixed Structure";
					 }else if(rs.getInt("census_shop_structure")==2){
						 OutletStructure = "Non-Fix Structure";
					 }
					 
					 // Trade Sub channel
					 
					 if(rs.getInt("census_trader_channel_sub_channel")==1){
						 TradeSubChannel="Kiryana Store";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==2){
						 TradeSubChannel="General Store";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==3){
						 TradeSubChannel="Pan/Cigarette Shop";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==4){
						 TradeSubChannel="Beverage Street Vendor";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==5){
						 TradeSubChannel="Modern General Store";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==6){
						 TradeSubChannel="Supermarket";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==7){
						 TradeSubChannel="Hypermarket";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==8){
						 TradeSubChannel="Fine Restaurant";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==9){
						 TradeSubChannel="Fast Food Restaurant";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==10){
						 TradeSubChannel="Food Courts";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==11){
						 TradeSubChannel="Conventional Restaurant";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==12){
						 TradeSubChannel="Food Street Outlets";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==13){
						 TradeSubChannel="Local Food Stand";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==14){
						 TradeSubChannel="Airport";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==15){
						 TradeSubChannel="Airline";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==16){
						 TradeSubChannel="Railway Station";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==17){
						 TradeSubChannel="Railways";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==18){
						 TradeSubChannel="Bus Stand";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==19){
						 TradeSubChannel="Bus Service";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==20){
						 TradeSubChannel="Bakeries/Sweet Shops";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==21){
						 TradeSubChannel="Medical Stores";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==22){
						 TradeSubChannel="Petroleum Food Marts";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==23){
						 TradeSubChannel="School/Colleges/Universitie";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==24){
						 TradeSubChannel="Hospitals";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==25){
						 TradeSubChannel="Hotels";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==26){
						 TradeSubChannel="Clubs";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==27){
						 TradeSubChannel="Marriage Hall/Caterers";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==28){
						 TradeSubChannel="Cinemas";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==29){
						 TradeSubChannel="Park/Zoo/Play Land";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==30){
						 TradeSubChannel="Utility Stores/CSD";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==31){
						 TradeSubChannel="Institutional Canteens";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==32){
						 TradeSubChannel="Wholesaler";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==33){
						 TradeSubChannel="Modern Meat Shop";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==34){
						 TradeSubChannel="PCO";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==35){
						 TradeSubChannel="Snooker Club";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==36){
						 TradeSubChannel="Other Beverage Selling Outlets";
					 }
					 // Trade Sub channel
					 
					 if(rs.getInt("census_trader_channel_sub_channel")==1){
						 TradeSubChannelDDL=1;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==2){
						 TradeSubChannelDDL=2;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==3){
						 TradeSubChannelDDL=3;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==4){
						 TradeSubChannelDDL=4;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==5){
						 TradeSubChannelDDL=5;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==6){
						 TradeSubChannelDDL=6;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==7){
						 TradeSubChannelDDL=7;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==8){
						 TradeSubChannelDDL=8;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==9){
						 TradeSubChannelDDL=9;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==10){
						 TradeSubChannelDDL=10;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==11){
						 TradeSubChannelDDL=11;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==12){
						 TradeSubChannelDDL=12;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==13){
						 TradeSubChannelDDL=13;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==14){
						 TradeSubChannelDDL=14;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==15){
						 TradeSubChannelDDL=15;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==16){
						 TradeSubChannelDDL=16;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==17){
						 TradeSubChannelDDL=17;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==18){
						 TradeSubChannelDDL=18;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==19){
						 TradeSubChannelDDL=19;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==20){
						 TradeSubChannelDDL=20;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==21){
						 TradeSubChannelDDL=21;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==22){
						 TradeSubChannelDDL=22;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==23){
						 TradeSubChannelDDL=23;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==24){
						 TradeSubChannelDDL=24;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==25){
						 TradeSubChannelDDL=25;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==26){
						 TradeSubChannelDDL=26;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==27){
						 TradeSubChannelDDL=27;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==28){
						 TradeSubChannelDDL=28;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==29){
						 TradeSubChannelDDL=29;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==30){
						 TradeSubChannelDDL=30;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==31){
						 TradeSubChannelDDL=31;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==32){
						 TradeSubChannelDDL=32;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==33){
						 TradeSubChannelDDL=33;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==34){
						 TradeSubChannelDDL=34;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==35){
						 TradeSubChannelDDL=35;
					 }else if(rs.getInt("census_trader_channel_sub_channel")==36){
						 TradeSubChannelDDL=36;
					 }
					 
					 //Area Sq.ft
					 
					 if(rs.getInt("census_area_seq_feet")==1){
						AreaSqFt="Upto 100";
					 }else if(rs.getInt("census_area_seq_feet")==2){
						 AreaSqFt="100 - 500"; 
					 }else if(rs.getInt("census_area_seq_feet")==3){
						 AreaSqFt="500 - 1000";
					 }else if(rs.getInt("census_area_seq_feet")==4){
						 AreaSqFt="1000 & Above";
					 }
					 
					 
					 if(rs.getInt("census_region_id")==1){
						 Region= "RM1 (Sargodha)";
					 }else if(rs.getInt("census_region_id")==2){
						 Region= "RM2 (Okara)";
					 }else if(rs.getInt("census_region_id")==3){
						 Region= "RM3 (Jhang)";
					 }else if(rs.getInt("census_region_id")==4)	{
						 Region= "RM4 (Faisalabad Out-1)";
					 }else if(rs.getInt("census_region_id")==5){
						 Region= "RM5 (FD Base Market)";
					 }else if(rs.getInt("census_region_id")==6){
						 Region= "RM6 (Toba Tek Singh)";
					 }else if(rs.getInt("census_region_id")==7){
						 Region= "RM9 (Faisalabad out-2)";
					 }else if(rs.getInt("census_region_id")==8){
						 Region= "Misc";
					 }else if(rs.getInt("census_region_id")==9){
						 Region= "None";
					 }else if(rs.getInt("census_region_id")==10){
						 Region= "RM7 Sargodha-II";
					 }else if(rs.getInt("census_region_id")==11){
						 Region= "RM8 FSD Base-II";
					 }
					 
						
					Distributor_name =rs.getString("census_distributor_name");
					Distributor_id= rs.getString("census_distributor_id");
						
					Distributor = rs.getString("census_distributor_id")+" - "+rs.getString("census_distributor_name");
					
					
					if(rs.getInt("census_area")==1){
						Area="Urban";
					}else{
						Area="Rural";	
					}
					if(rs.getInt("census_shop_type")==1){
						ShopType="Beverage Selling";
					}else{
						ShopType="Non Beverage Selling";
					}
					
					 
					LandMark=rs.getString("land_mark");
					
					if(rs.getInt("census_segment")==1){
						Location="Main Street";
					}else if(rs.getInt("census_segment")==2){
						Location="Secondary Street";
					}else if(rs.getInt("census_segment")==3){
						Location="Highway";
					}/*else if(rs.getInt("census_segment")==4){
						Location="Captive";
					}*/
					 
					
					
					 if(rs.getInt("census_shop_location")==1){
						 ShopLocation="Residential"; 
					 }else if(rs.getInt("census_shop_location")==2){
						 ShopLocation="Commercial"; 
					 }else if(rs.getInt("census_shop_location")==3){
						 ShopLocation= "Industrial";
					 }
					 
					
					 OwnerName=rs.getString("census_outlet_contact_person");
					
					
					 OwnerContactNo1=rs.getString("census_outlet_contact_1");
					 OwnerContactNo2=rs.getString("census_outlet_contact_2");
					 OwnerCNIC=rs.getString("census_owner_cnic_1");
					 
					 if(rs.getInt("census_owner_contact_person_relation")==1){
						 ContactPerson="Owner";
					 }else if(rs.getInt("census_owner_contact_person_relation")==2){
						 ContactPerson="Employee";
					 }else if(rs.getInt("census_owner_contact_person_relation")==3){
						 ContactPerson="First Relation";
					 }else if(rs.getInt("census_owner_contact_person_relation")==4){
						 ContactPerson="Second Relation";
					 }else if(rs.getInt("census_owner_contact_person_relation")==5){
						 ContactPerson="Friend";
					 }else if(rs.getInt("census_owner_contact_person_relation")==6){
						 ContactPerson="Partner";
					 }
					 //for dropdown
					 if(rs.getInt("census_owner_contact_person_relation")==1){
						 ContactPersonDDL=1;
					 }else if(rs.getInt("census_owner_contact_person_relation")==2){
						 ContactPersonDDL=2;
					 }else if(rs.getInt("census_owner_contact_person_relation")==3){
						 ContactPersonDDL=3;
					 }else if(rs.getInt("census_owner_contact_person_relation")==4){
						 ContactPersonDDL=4;
					 }else if(rs.getInt("census_owner_contact_person_relation")==5){
						 ContactPersonDDL=5;
					 }else if(rs.getInt("census_owner_contact_person_relation")==6){
						 ContactPersonDDL=6;
					 }
					 
					 ContactPersonName=rs.getString("census_outlet_contact_name");
					 
					 ContactPersonNo=rs.getString("census_outlet_contact_3");
					 ContactPersonCNIC=rs.getString("census_owner_cnic_2");
					 FBID=rs.getString("census_facebook_id");
					 EmailID=rs.getString("census_email_id");
					 
					//shopkeeper
					 
					 if(rs.getInt("is_census_shop_closed")!=0){
						 IsShopClosed = "Closed";
						 
						 if(rs.getInt("census_shop_closed_status")==1){
							 ShopClosedStatus="Permanently";
						 }else{
							 ShopClosedStatus="Temporarily";
						 }
					 }
					 
					 
					 if(rs.getInt("census_day_off")==1){
						 DayOff="Sunday";
					 }else  if(rs.getInt("census_day_off")==2){
						 DayOff="Monday";
					 }else  if(rs.getInt("census_day_off")==3){
						 DayOff="Tuesday";
					 }else  if(rs.getInt("census_day_off")==4){
						 DayOff="Wednesday";
					 }else  if(rs.getInt("census_day_off")==5){
						 DayOff="Thursday";
					 }else  if(rs.getInt("census_day_off")==6){
						 DayOff="Friday";
					 }else  if(rs.getInt("census_day_off")==7){
						 DayOff="Saturday";
					 }else{
						 DayOff="None";
					 }
					 
					 //dayOff Drop down list					 
					 if(rs.getInt("census_day_off")==1){
						 DayOffDDL=1;
					 }else  if(rs.getInt("census_day_off")==2){
						 DayOffDDL=2;
					 }else  if(rs.getInt("census_day_off")==3){
						 DayOffDDL=3;
					 }else  if(rs.getInt("census_day_off")==4){
						 DayOffDDL=4;
					 }else  if(rs.getInt("census_day_off")==5){
						 DayOffDDL=5;
					 }else  if(rs.getInt("census_day_off")==6){
						 DayOffDDL=6;
					 }else  if(rs.getInt("census_day_off")==7){
						 DayOffDDL=7;
					 }else{
						 DayOffDDL=8;
					 }
					 
					 
					 if(rs.getInt("census_partially_timing")==1){
						 PartialTiming="Morning";
					 }else if(rs.getInt("census_partially_timing")==2){
						 PartialTiming="Noon";
					 }else if(rs.getInt("census_partially_timing")==3){
						 PartialTiming="Night";
					 }else if(rs.getInt("census_partially_timing")==4){
						 PartialTiming="None";
					 }
					 //partial timings radio btn
					 if(rs.getInt("census_partially_timing")==1){
						 PartialTimingRbtn=1;
					 }else if(rs.getInt("census_partially_timing")==2){
						 PartialTimingRbtn=2;
					 }else if(rs.getInt("census_partially_timing")==3){
						 PartialTimingRbtn=3;
					 }else if(rs.getInt("census_partially_timing")==4){
						 PartialTimingRbtn=4;
					 }
					 
					 
					 if(rs.getInt("census_feeded_stock_1")==1){
						 SDistributor="Distributor";
					 }else{
						 SDistributor=""; 
					 }
					 
					 if(rs.getInt("census_feeded_stock_2")==1){
						 WholeSaller=", Wholeseller";
					 }else{
						 WholeSaller="";
					 }
					 
					 if(rs.getInt("census_feeded_stock_3")==1){
						 SubDistributor=", Sub-Distributor";
					 }else{
						 SubDistributor="";
					 }
					 
					 SuppliedBy=SDistributor+""+WholeSaller+""+SubDistributor;
					
					 if(rs.getInt("census_financial_service_1")==1){
						 EasyPaisa="Easy Paisa";
					 }
					 
					 if(rs.getInt("census_financial_service_2")==1){
						 Upaisa="U Paisa";
					 }
					 
					 if(rs.getInt("census_financial_service_3")==1){
						 MobiCash="Mobi Cash";
					 }
					 
					 if(rs.getInt("census_financial_service_4")==1){
						 Warid="Warid Mobile Paisa";
					 }
					 
					 if(rs.getInt("census_financial_service_5")==1){
						 TimePay="Time Pay";
					 }
					 
					 if(rs.getInt("census_financial_service_6")==1){
						 Other="Other";
					 }
					 
					 //checkBox
					 FinancialServiceVal=rs.getInt("census_financial_service_1");
					 if(rs.getInt("census_financial_service_1") ==1){
						  FinancialServiceChbtn1=1;
					 }
					 
					 if(rs.getInt("census_financial_service_2")==1){
						 FinancialServiceChbtn2=1;
					 }
					 
					 if(rs.getInt("census_financial_service_3")==1){
						 FinancialServiceChbtn3=1;
					 }
					 
					 if(rs.getInt("census_financial_service_4")==1){
						 FinancialServiceChbtn4=1;
					 }
					 
					 if(rs.getInt("census_financial_service_5")==1){
						 FinancialServiceChbtn5=1;
					 }
					 
					 if(rs.getInt("census_financial_service_6")==1){
						 FinancialServiceChbtn6=1;
					 }
					 
					
					 
					 if(EasyPaisa!=""){
						 EasyPaisa+="<br/>"; 
					 }
					 if(Upaisa!=""){
						 Upaisa+="<br/>"; 
					 }
					 if(MobiCash!=""){
						 MobiCash+="<br/>"; 
					 }
					 if(Warid!=""){
						 Warid+="<br/>"; 
					 }
					 if(TimePay!=""){
						 TimePay+="<br/>"; 
					 }
					 if(Other!=""){
						 Other+="<br/>"; 
					 }
					 
					 FinancialService += EasyPaisa+""+Upaisa+""+MobiCash+""+Warid+""+TimePay+""+Other;
					 
					 
					 ShopOpeningTime = rs.getString("census_owner_contact_person_shop_opening_time");
					//drop Down
					 if(rs.getString("census_owner_contact_person_shop_opening_time").equals("1 AM")){
						 ShopOpeningTimeDDL="1 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("2 AM")){
						 ShopOpeningTimeDDL="2 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("3 AM")){
						 ShopOpeningTimeDDL="3 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("4 AM")){
						 ShopOpeningTimeDDL="4 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("5 AM")){
						 ShopOpeningTimeDDL="5 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("6 AM")){
						 ShopOpeningTimeDDL="6 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("7 AM")){
						 ShopOpeningTimeDDL="7 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("8 AM")){
						 ShopOpeningTimeDDL="8 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("9 AM")){
						 ShopOpeningTimeDDL="9 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("10 AM")){
						 ShopOpeningTimeDDL="10 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("11 AM")){
						 ShopOpeningTimeDDL="11 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("12 PM")){
						 ShopOpeningTimeDDL="12 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("1 PM")){
						 ShopOpeningTimeDDL="1 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("2 PM")){
						 ShopOpeningTimeDDL="2 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("3 PM")){
						 ShopOpeningTimeDDL="3 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("4 PM")){
						 ShopOpeningTimeDDL="4 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("5 PM")){
						 ShopOpeningTimeDDL="5 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("6 PM")){
						 ShopOpeningTimeDDL="6 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("7 PM")){
						 ShopOpeningTimeDDL="7 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("8 PM")){
						 ShopOpeningTimeDDL="8 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("9 PM")){
						 ShopOpeningTimeDDL="9 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("10 PM")){
						 ShopOpeningTimeDDL="10 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_opening_time").equals("11 PM")){
						 ShopOpeningTimeDDL="11 PM";
					 }
					
					ShopCloseTime = rs.getString("census_owner_contact_person_shop_close_time");
					 
					//ShopcloseTime Drop down
					 if(rs.getString("census_owner_contact_person_shop_close_time").equals("1 AM")){
						 ShopCloseTimeDDL="1 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("2 AM")){
						 ShopCloseTimeDDL="2 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("3 AM")){
						 ShopCloseTimeDDL="3 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("4 AM")){
						 ShopCloseTimeDDL="4 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("5 AM")){
						 ShopCloseTimeDDL="5 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("6 AM")){
						 ShopCloseTimeDDL="6 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("7 AM")){
						 ShopCloseTimeDDL="7 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("8 AM")){
						 ShopCloseTimeDDL="8 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("9 AM")){
						 ShopCloseTimeDDL="9 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("10 AM")){
						 ShopCloseTimeDDL="10 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("11 AM")){
						 ShopCloseTimeDDL="11 AM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("12 PM")){
						 ShopCloseTimeDDL="12 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("1 PM")){
						 ShopCloseTimeDDL="1 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("2 PM ")){
						 ShopCloseTimeDDL="2 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("3 PM")){
						 ShopCloseTimeDDL="3 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("4 PM")){
						 ShopCloseTimeDDL="4 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("5 PM")){
						 ShopCloseTimeDDL="5 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("6 PM")){
						 ShopCloseTimeDDL="6 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("7 PM")){
						 ShopCloseTimeDDL="7 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("8 PM")){
						 ShopCloseTimeDDL="8 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("9 PM")){
						 ShopCloseTimeDDL="9 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("10 PM")){
						 ShopCloseTimeDDL="10 PM";
					 }else if(rs.getString("census_owner_contact_person_shop_close_time").equals("11 PM")){
						 ShopCloseTimeDDL="11 PM";
					 }
					 //Status Tab
					 
					 if(rs.getInt("census_shop_status")==1){
						 ShopStatus="Seasonal";
					 }else if(rs.getInt("census_shop_status")==2){
						 ShopStatus="Permanent";
					 }
					 //ShopStatusradio
					 ShopStatusVal=rs.getInt("census_shop_status");
					 if(ShopStatusVal==1){
						 ShopStatusRbtn=1;
					 }else if(ShopStatusVal==2){
						 ShopStatusRbtn=2;
					 }
					 
					  
					  ShopHistory=rs.getString("census_shop_establishment_history");
					  //ShopHistoryDDL 
					  if((rs.getString("census_shop_establishment_history")).equals("")){
						  ShopHistory="";
						 }else if((rs.getString("census_shop_establishment_history")).equals("Before 2005")){
						  ShopHistory="Before 2005";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2005")){
							 ShopHistory="2005";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2006")){
							 ShopHistory="2006";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2007")){
							 ShopHistory="2007";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2008")){
							 ShopHistory="2008";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2009")){
							 ShopHistory="2009";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2010")){
							 ShopHistory="2010";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2011")){
							 ShopHistory="2011";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2012")){
							 ShopHistory="2012";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2013")){
							 ShopHistory="2013";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2014")){
							 ShopHistory="2014";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2015")){
							 ShopHistory="2015";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2016")){
							 ShopHistory="2016";
						 }else if((rs.getString("census_shop_establishment_history")).equals("2017")){
							 ShopHistory="2017";
						 }
					  
					 
					  
					  BusinessStructureVal=rs.getInt("census_business_structure");
					  if(rs.getInt("census_business_structure")==1){
						  BusinessStructure="Self";
					   }else if(rs.getInt("census_business_structure")==2){
						   BusinessStructure="Chain";
					   }else if(rs.getInt("census_business_structure")==3){
						   BusinessStructure="Partnership";
					   }
					  
					  if(rs.getInt("census_shop_potential")==1){
						  SocioEco="High";
					   }else if(rs.getInt("census_shop_potential")==2){
						   SocioEco="Medium";
					   }else if(rs.getInt("census_shop_potential")==3){
						   SocioEco="Low";
					   }
					  
					  SShopTypeVal=rs.getInt("census_customer_type");
					  if(rs.getInt("census_customer_type")==1){
						  SShopType="Wholesale";
					   }else if(rs.getInt("census_customer_type")==2){
						   SShopType="Retailer";
					   }else if(rs.getInt("census_customer_type")==3){
						   SShopType="Both";
					   }
					  
					  
					 
					  
					
					  Wholesale=rs.getString("census_customer_type_wholesale_percent");
					  Retailer=rs.getString("census_customer_type_retailer_percent");
					  
					  int ServiceTypeVal=rs.getInt("census_service_type");
					  if(rs.getInt("census_service_type")==1){
						  ServiceType="Self";
					   }else if(rs.getInt("census_service_type")==2){
						   ServiceType="Counter";
					   }else if(rs.getInt("census_service_type")==3){
						   ServiceType="Both";
					   }
					  
					  if(rs.getInt("census_csd_turnover_per_day")==1){
						  CSDTurnOver="Less than 2 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==2){
						   CSDTurnOver="2-5 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==3){
						   CSDTurnOver="5-10 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==4){
						   CSDTurnOver="10-15 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==5){
						   CSDTurnOver="More than 15";
					   }
					  
					  
					  //TradeChannel=rs.getString("trade_channel_label");
					 // TradeSubChannel=rs.getString("trade_sub_channel_labe");
					 
				
					  //Beverages
					  
					   PICSD= rs.getInt("pi_csd");
					   PISting=rs.getInt("pi_sting");
					   KO=rs.getInt("ko");
					  
						 
						
						 
					   SupplyFrequency=rs.getString("supply_frequency");
					   //SupplyFrequency DropDown
					   if(rs.getInt("supply_frequency")==0){
						   SupplyFrequencyDDL=0;
						 }else if(rs.getInt("supply_frequency")==1){
						   SupplyFrequencyDDL=1;
						 }else if(rs.getInt("supply_frequency")==2){
							 SupplyFrequencyDDL=2;
						 }else if(rs.getInt("supply_frequency")==3){
							 SupplyFrequencyDDL=3;
						 }else if(rs.getInt("supply_frequency")==4){
							 SupplyFrequencyDDL=4;
						 }else if(rs.getInt("supply_frequency")==5){
							 SupplyFrequencyDDL=5;
						 }else if(rs.getInt("supply_frequency")==6){
							 SupplyFrequencyDDL=6;
						 }else if(rs.getInt("supply_frequency")==7){
							 SupplyFrequencyDDL=7;
						 }
					  
					   SupplyFrequencyKO=rs.getString("supply_frequency_ko");
					 //SupplyFrequencyKO DropDown
					   if(rs.getInt("supply_frequency_ko")==0){
						   SupplyFrequencyDDLKO=0;
						 }else if(rs.getInt("supply_frequency_ko")==1){
						   SupplyFrequencyDDLKO=1;
						 }else if(rs.getInt("supply_frequency_ko")==2){
							 SupplyFrequencyDDLKO=2;
						 }else if(rs.getInt("supply_frequency_ko")==3){
							 SupplyFrequencyDDLKO=3;
						 }else if(rs.getInt("supply_frequency_ko")==4){
							 SupplyFrequencyDDLKO=4;
						 }else if(rs.getInt("supply_frequency_ko")==5){
							 SupplyFrequencyDDLKO=5;
						 }else if(rs.getInt("supply_frequency_ko")==6){
							 SupplyFrequencyDDLKO=6;
						 }else if(rs.getInt("supply_frequency_ko")==7){
							 SupplyFrequencyDDLKO=7;
						 }
					   SupplyFrequencyGormet=rs.getString("supply_frequency_g");
					 //SupplyFrequencyGO DropDown
					   if(rs.getInt("supply_frequency_g")==0){
						   SupplyFrequencyDDLGO=0;
						 }else if(rs.getInt("supply_frequency_g")==1){
						   SupplyFrequencyDDLGO=1;
						 }else if(rs.getInt("supply_frequency_g")==2){
							 SupplyFrequencyDDLGO=2;
						 }else if(rs.getInt("supply_frequency_g")==3){
							 SupplyFrequencyDDLGO=3;
						 }else if(rs.getInt("supply_frequency_g")==4){
							 SupplyFrequencyDDLGO=4;
						 }else if(rs.getInt("supply_frequency_g")==5){
							 SupplyFrequencyDDLGO=5;
						 }else if(rs.getInt("supply_frequency_g")==6){
							 SupplyFrequencyDDLGO=6;
						 }else if(rs.getInt("supply_frequency_g")==7){
							 SupplyFrequencyDDLGO=7;
						 }
					   SupplyFrequencyColaNext=rs.getString("supply_frequency_cn");
					   
					   //SupplyFrequencyCN DropDown
					   if(rs.getInt("supply_frequency_cn")==0){
						   SupplyFrequencyDDLCN=0;
						 }else if(rs.getInt("supply_frequency_cn")==1){
						   SupplyFrequencyDDLCN=1;
						 }else if(rs.getInt("supply_frequency_cn")==2){
							 SupplyFrequencyDDLCN=2;
						 }else if(rs.getInt("supply_frequency_cn")==3){
							 SupplyFrequencyDDLCN=3;
						 }else if(rs.getInt("supply_frequency_cn")==4){
							 SupplyFrequencyDDLCN=4;
						 }else if(rs.getInt("supply_frequency_cn")==5){
							 SupplyFrequencyDDLCN=5;
						 }else if(rs.getInt("supply_frequency_cn")==6){
							 SupplyFrequencyDDLCN=6;
						 }else if(rs.getInt("supply_frequency_cn")==7){
							 SupplyFrequencyDDLCN=7;
						 }
					   
					   
					   if(rs.getInt("census_exclusivity_agreement_1")==1){
						   ExPepsi="Pepsi";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_2")==1){
							 ExCoke="Coke";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_3")==1){
							 ExGourmet="Gourmet";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_4")==1){
							 ExMezan="Mezan";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_5")==1){
							 ExOther="Other";
						 }
						 
						 
						 
						 if(ExPepsi!=""){
							 ExPepsi+="<br/>";
						 }
						 
						 if(ExCoke!=""){
							 ExCoke+="<br/>";
						 }
						 
						 if(ExGourmet!=""){
							 ExGourmet+="<br/>";
						 }
						 
						 if(ExMezan!=""){
							 ExMezan+="<br/>";
						 }
						 
						 if(ExOther!=""){
							 ExOther+="<br/>";
						 }
						 
						 
					   
					   Exclusivity=ExPepsi+""+ExCoke+""+ExGourmet+""+ExMezan+""+ExOther ;
					  
					   
					   DiscountStatus="";
					   
					   
					   if(rs.getInt("census_discount_status_1")==1){
						   DPerCase="Per-Case";
						 }
						 
						 if(rs.getInt("census_discount_status_2")==1){
							 DTradePaymentFix="Trade Payment(Period Fixed)";
						 }
						 
						 if(rs.getInt("census_discount_status_3")==1){
							 DTradePaymentTarget="Trade Payment(Target Fixed)";
						 }
						 
						 
						 if(DPerCase!=""){
							 DPerCase+="<br/>";
						 }
						 
						 if(DTradePaymentFix!=""){
							 DTradePaymentFix+="<br/>";
						 }
						 
						 if(DTradePaymentTarget!=""){
							 DTradePaymentTarget+="<br/>";
						 }
						 
					   
						 DiscountStatus=DPerCase+""+DTradePaymentFix+""+DTradePaymentTarget;
						 
						 
						 IsCoveredByRCompnayVal=rs.getInt("census_shop_closed_status");
						 
						 
						 //if(rs.getInt("census_shop_closed_status")==1){
							 //IsCoveredByRCompnay="Yes";
							 
							 if(rs.getInt("census_shop_researcher_closed_status")==1){
								 IsCoveredByRCompnayDuration="Monthly";
							 }else if(rs.getInt("census_shop_researcher_closed_status")==2){
								 IsCoveredByRCompnayDuration="Quarterly";
							 }else if(rs.getInt("census_shop_researcher_closed_status")==3){
								 IsCoveredByRCompnayDuration="Annually";
							 }
							 
						//}
						
							 if(rs.getInt("census_shop_researcher_closed_status")==1){
								 IsCoveredByRCompnayRbtn=1;
							 }else if(rs.getInt("census_shop_researcher_closed_status")==2){
								 IsCoveredByRCompnayRbtn=2;
							 }else if(rs.getInt("census_shop_researcher_closed_status")==3){
								 IsCoveredByRCompnayRbtn=3;
							 }
							 
						
						 
						 
						 if(rs.getInt("stock_storage_loc1")==1){
							 StockStorageLocation1="Within Outlet";
						 }
						 
						 if(rs.getInt("stock_storage_loc2")==1){
							 StockStorageLocation2="Attached with Shop";
						 }
						 
						 if(rs.getInt("stock_storage_loc3")==1){
							 StockStorageLocation3="Elsewhere";
						 }
						 
						 if(rs.getInt("stock_storage_loc4")==1){
							 StockStorageLocation4="No Storage";
						 }
						 
						 if(rs.getInt("cash_machine_status")==1){
							 CashMachineQuantity=rs.getInt("cash_machine_quantity");
						 }
						 
						 //Checkboxes
						 if(rs.getInt("stock_storage_loc1")==1){
							 StockStorageLocationChbtn1=1;
							 
						 }
						 
						 if(rs.getInt("stock_storage_loc2")==1){
							 StockStorageLocationChbtn2=1;
						 }
						 
						 if(rs.getInt("stock_storage_loc3")==1){
							 StockStorageLocationChbtn3=1;
						 }
						 
						 if(rs.getInt("stock_storage_loc4")==1){
							 StockStorageLocationChbtn4=1;
						 }
						 
						 if(rs.getInt("cash_machine_status")==1){
							 CashMachineQuantity=rs.getInt("cash_machine_quantity");
						 }
						 
						 
						 
						 
						  
						 
							  BeveragesSellingFullCasesSSRB=rs.getInt("census_shop_selling_bev"); 
						
						  
						  
							  BeveragesSellingFullCasesPET=rs.getInt("census_shop_selling_pet"); 
						 
						  
						 
							  BeveragesSellingFullCasesTETRA=rs.getInt("census_shop_selling_nrb"); 
						 
						  
						 
							  BeveragesSellingFullCasesCAN=rs.getInt("census_shop_selling_can"); 
						 
						  
						  //Exclusivity agreement
						  
						   
						   
						 
						 if(rs.getInt("census_exclusivity_agreement_1")==1){
							 ExclusivityAgreementPI = "Pepsi"; 
						 }
						 
						 
						 if(rs.getInt("census_exclusivity_agreement_2")==1){
							 ExclusivityAgreementKO = "Coke"; 
						 }
						 
						
						 if(rs.getInt("census_exclusivity_agreement_3")==1){
							 ExclusivityAgreementGou = "Gormet"; 
						 }
						 
						
						 if(rs.getInt("census_exclusivity_agreement_4")==1){
							 ExclusivityAgreementMezan = "Mezan"; 
						 }
						 
						
						 if(rs.getInt("census_exclusivity_agreement_5")==1){
							 ExclusivityAgreementOther = "Other"; 
						 }
						 
						 
						 
						 //RadioButtonsExculsivelyAgreement
						
						// int ExAgreePI= rs.getInt("census_exclusivity_agreement_1");
						 if(rs.getInt("census_exclusivity_agreement_1")==1){
							 ExclusivityAgreementPIRbtn=1; 
						 }else if(rs.getInt("census_exclusivity_agreement_1")==2){
							 ExclusivityAgreementPIRbtn=2; 
						 }else if(rs.getInt("census_exclusivity_agreement_1")==3){
							 ExclusivityAgreementPIRbtn=3; 
						 }else if(rs.getInt("census_exclusivity_agreement_1")==4){
							 ExclusivityAgreementPIRbtn=4; 
						 }else if(rs.getInt("census_exclusivity_agreement_1")==5){
							 ExclusivityAgreementPIRbtn=5; 
						 }
						 
						 
						 
						/* 
						 
						 int ExAgreeKO= rs.getInt("census_exclusivity_agreement_2");
						 if(ExAgreeKO==1){
							 ExclusivityAgreementKORbtn=1; 
						 }
						 
						 int ExAgreeGOU= rs.getInt("census_exclusivity_agreement_3");
						 if(ExAgreeGOU==1){
							 ExclusivityAgreementGouRbtn=1; 
						 }
						 
						 int ExAgreeMezaan= rs.getInt("census_exclusivity_agreement_4");
						 if(ExAgreeMezaan==1){
							 ExclusivityAgreementMezanRbtn=1; 
						 }
						 
						 int ExAgreeOther= rs.getInt("census_exclusivity_agreement_5");
						 if(ExAgreeOther==1){
							 ExclusivityAgreementOtherRbtn=1; 
						 }
						 
						 */
						 
						 //Per Raw Case Discount  - SSRB
						 int PerRawCaseDiscSSRBVal=rs.getInt("census_exclusivity_agreement_pkr_ssrb_1");
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==1){
							  PerRawCaseDiscSSRB="1-25";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==2){
							  PerRawCaseDiscSSRB="26-35";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==3){
							  PerRawCaseDiscSSRB="36-45";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==4){
							  PerRawCaseDiscSSRB="46-75";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==5){
							  PerRawCaseDiscSSRB="76 Above";
							  //PerRawCaseDiscPET="";
						 }
						 //perRaw Case Discount radioBttn
						 if(PerRawCaseDiscSSRBVal==1){
							 PerRawCaseDiscSSRBRadbtn=1;
							  //PerRawCaseDiscPET="";
						 }else if(PerRawCaseDiscSSRBVal==2){
							 PerRawCaseDiscSSRBRadbtn=2;
							  //PerRawCaseDiscPET="";
						 }else if(PerRawCaseDiscSSRBVal==3){
							 PerRawCaseDiscSSRBRadbtn=3;
							  //PerRawCaseDiscPET="";
						 }else if(PerRawCaseDiscSSRBVal==4){
							 PerRawCaseDiscSSRBRadbtn=4;
							  //PerRawCaseDiscPET="";
						 }else if(PerRawCaseDiscSSRBVal==5){
							 PerRawCaseDiscSSRBRadbtn=5;
							  //PerRawCaseDiscPET="";
						 }else if(PerRawCaseDiscSSRBVal==6){
							 PerRawCaseDiscSSRBRadbtn=6;
							  //PerRawCaseDiscPET="";
						 }
						 
						 //Per Raw Case Discount  - PET
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==1){
							 PerRawCaseDiscPET="1-25";
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==2){
							 PerRawCaseDiscPET="26-35";
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==3){
							 PerRawCaseDiscPET="36-45";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==4){
							 PerRawCaseDiscPET="46-75";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==5){
							 PerRawCaseDiscPET="76 Above";
							  
						 }
						 //Per Raw Case Discount  - PET radio button
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==1){
							 PerRawCaseDiscPETRadbtn=1;
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==2){
							 PerRawCaseDiscPETRadbtn=2;
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==3){
							 PerRawCaseDiscPETRadbtn=3;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==4){
							 PerRawCaseDiscPETRadbtn=4;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==5){
							 PerRawCaseDiscPETRadbtn=5;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==6){
							 PerRawCaseDiscPETRadbtn=6;
							  
						 }
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDate = rs.getInt("aggr_exp_date_mm")+"/"+rs.getInt("aggr_exp_date_yy");
						 
						 AgreementExpdateMonth=rs.getInt("aggr_exp_date_mm");
						 
						 //AgreementExpDateMonth drop dwon
						 
						 if(rs.getInt("aggr_exp_date_mm")==0){
							 AgreementExpdateMonthDDL=0;
						 }else if(rs.getInt("aggr_exp_date_mm")==1){
							 AgreementExpdateMonthDDL=1;
						 }else if(rs.getInt("aggr_exp_date_mm")==2){
							 AgreementExpdateMonthDDL=2;
						 }else if(rs.getInt("aggr_exp_date_mm")==3){
							 AgreementExpdateMonthDDL=3;
						 }else if(rs.getInt("aggr_exp_date_mm")==4){
							 AgreementExpdateMonthDDL=4;
						 }else if(rs.getInt("aggr_exp_date_mm")==5){
							 AgreementExpdateMonthDDL=5;
						 }else if(rs.getInt("aggr_exp_date_mm")==6){
							 AgreementExpdateMonthDDL=6;
						 }else if(rs.getInt("aggr_exp_date_mm")==7){
							 AgreementExpdateMonthDDL=7;
						 }else if(rs.getInt("aggr_exp_date_mm")==8){
							 AgreementExpdateMonthDDL=8;
						 }else if(rs.getInt("aggr_exp_date_mm")==9){
							 AgreementExpdateMonthDDL=9;
						 }else if(rs.getInt("aggr_exp_date_mm")==10){
							 AgreementExpdateMonthDDL=10;
						 }else if(rs.getInt("aggr_exp_date_mm")==11){
							 AgreementExpdateMonthDDL=11;
						 }else if(rs.getInt("aggr_exp_date_mm")==12){
							 AgreementExpdateMonthDDL=12;
						 }
						 
						 AgreementExpdateYear=rs.getInt("aggr_exp_date_yy");
						 //AgreementExpdateYear Drop Down List
						 if(rs.getInt("aggr_exp_date_yy")==0){
							 AgreementExpdateYearDDL=0;
						 }else if(rs.getInt("aggr_exp_date_yy")==1){
							 AgreementExpdateYearDDL=1;
						 }else if(rs.getInt("aggr_exp_date_yy")==2){
							 AgreementExpdateYearDDL=2;
						 }else if(rs.getInt("aggr_exp_date_yy")==3){
							 AgreementExpdateYearDDL=3;
						 }else if(rs.getInt("aggr_exp_date_yy")==4){
							 AgreementExpdateYearDDL=4;
						 }else if(rs.getInt("aggr_exp_date_yy")==5){
							 AgreementExpdateYearDDL=5;
						 }else if(rs.getInt("aggr_exp_date_yy")==6){
							 AgreementExpdateYearDDL=6;
						 }else if(rs.getInt("aggr_exp_date_yy")==7){
							 AgreementExpdateYearDDL=7;
						 }else if(rs.getInt("aggr_exp_date_yy")==8){
							 AgreementExpdateYearDDL=8;
						 }else if(rs.getInt("aggr_exp_date_yy")==9){
							 AgreementExpdateYearDDL=9;
						 }else if(rs.getInt("aggr_exp_date_yy")==10){
							 AgreementExpdateYearDDL=10;
						 }else if(rs.getInt("aggr_exp_date_yy")==11){
							 AgreementExpdateYearDDL=11;
						 }else if(rs.getInt("aggr_exp_date_yy")==12){
							 AgreementExpdateYearDDL=12;
						 }
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type")==1){
							 AgreementType="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type")==2){
							 AgreementType="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type")==3){
							 AgreementType="Written-Not Witnessed";
						 }
						 //Agreement Type Radiobutton
						 if(rs.getInt("census_shop_agreement_type")==1){
							 AgreementTypeRbtn=1;
						 }else if(rs.getInt("census_shop_agreement_type")==2){
							 AgreementTypeRbtn=2;
						 }else if(rs.getInt("census_shop_agreement_type")==3){
							 AgreementTypeRbtn=3;
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period")==1){
							 AgreementPeriod="1";
						 }else if(rs.getInt("census_shop_agreement_period")==2){
							 AgreementPeriod="2";
						 }else if(rs.getInt("census_shop_agreement_period")==3){
							 AgreementPeriod="3 & Above";
						 }
						 //Agreement period radio button
						 if(rs.getInt("census_shop_agreement_period")==1){
							 AgreementPeriodRbtn=1;
						 }else if(rs.getInt("census_shop_agreement_period")==2){
							 AgreementPeriodRbtn=2;
						 }else if(rs.getInt("census_shop_agreement_period")==3){
							 AgreementPeriodRbtn=3;
						 }
						 
						 //Agreement Type - 2
						 
						 if(rs.getInt("census_shop_agreement_type1_1")==1){
							 AgreementType2 += "Discount<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_2")==1){
							 AgreementType2 += "Signage<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_3")==1){
							 AgreementType2 += "Annual Support<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_4")==1){
							 AgreementType2 += "Exclusivity Fee<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_5")==1){
							 AgreementType2 += "Vol Incentive<br/>";
						 } if(rs.getString("census_shop_agreement_type_other")!=""){
							 AgreementType2 += "Any Other<br/>";
						 }
 						//Agreement Type  - 2 RAdio button
						
						 if(rs.getInt("census_shop_agreement_type1_1")==1){
							 AgreementType2Rbtn1=1;
						 } if(rs.getInt("census_shop_agreement_type1_2")==1){
							 AgreementType2Rbtn2=1;
						 } if(rs.getInt("census_shop_agreement_type1_3")==1){
							 AgreementType2Rbtn3=1;
						 } if(rs.getInt("census_shop_agreement_type1_4")==1){
							 AgreementType2Rbtn4=1;
						 } if(rs.getInt("census_shop_agreement_type1_5")==1){
							 AgreementType2Rbtn5=1;
						 } if(rs.getString("census_shop_agreement_type_other")!=""){
							 AgreementType2Rbtn6=rs.getString("census_shop_agreement_type_other");
						 }
						 
						 
						 /////////////////////////////////
						 ////////////////Discount Agreement - PI//////////
						 
							//Per Raw Case Discount  - SSRB
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==1){
							  PerRawCaseDiscSSRBPI="1-25";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==2){
							  PerRawCaseDiscSSRBPI="26-35";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==3){
							  PerRawCaseDiscSSRBPI="36-45";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==4){
							  PerRawCaseDiscSSRBPI="46-75";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==5){
							  PerRawCaseDiscSSRBPI="76 Above";
							  //PerRawCaseDiscPET="";
						 }
						 
						//Per Raw Case Discount  - SSRB radio btn
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==1){
							  PerRawCaseDiscSSRBPIRbtn=1;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==2){
							 PerRawCaseDiscSSRBPIRbtn=2;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==3){
							 PerRawCaseDiscSSRBPIRbtn=3;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==4){
							 PerRawCaseDiscSSRBPIRbtn=4;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==5){
							 PerRawCaseDiscSSRBPIRbtn=5;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==6){
							 PerRawCaseDiscSSRBPIRbtn=6;
							  //PerRawCaseDiscPET="";
						 }
						 
						 //Per Raw Case Discount  - PET
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==1){
							 PerRawCaseDiscPETPI="1-25";
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==2){
							 PerRawCaseDiscPETPI="26-35";
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==3){
							 PerRawCaseDiscPETPI="36-45";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==4){
							 PerRawCaseDiscPETPI="46-75";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==5){
							 PerRawCaseDiscPETPI="76 Above";
							  
						 }
						//Per Raw Case Discount  - PET Radio button
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==1){
							 PerRawCaseDiscPETPIRadbtn=1;
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==2){
							 PerRawCaseDiscPETPIRadbtn=2;
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==3){
							 PerRawCaseDiscPETPIRadbtn=3;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==4){
							 PerRawCaseDiscPETPIRadbtn=4;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==5){
							 PerRawCaseDiscPETPIRadbtn=5;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==6){
							 PerRawCaseDiscPETPIRadbtn=6;
							  
						 }
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDatePI = rs.getInt("aggr_exp_date_mm_pi")+"/"+rs.getInt("aggr_exp_date_yy_pi");
						 AgreementExpdateMonthPI=rs.getInt("aggr_exp_date_mm_pi");
						 //AgreementExpdateMonthPI drop down
						 if(rs.getInt("aggr_exp_date_mm_pi")==0){
							 AgreementExpdateMonthDDLPI=0;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==1){
							 AgreementExpdateMonthDDLPI=1;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==2){
							 AgreementExpdateMonthDDLPI=2;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==3){
							 AgreementExpdateMonthDDLPI=3;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==4){
							 AgreementExpdateMonthDDLPI=4;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==5){
							 AgreementExpdateMonthDDLPI=5;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==6){
							 AgreementExpdateMonthDDLPI=6;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==7){
							 AgreementExpdateMonthDDLPI=7;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==8){
							 AgreementExpdateMonthDDLPI=8;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==9){
							 AgreementExpdateMonthDDLPI=9;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==10){
							 AgreementExpdateMonthDDLPI=10;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==11){
							 AgreementExpdateMonthDDLPI=11;
						 }else if(rs.getInt("aggr_exp_date_mm_pi")==12){
							 AgreementExpdateMonthDDLPI=12;
						 }
						 
						
						 AgreementExpdateYearPI=rs.getInt("aggr_exp_date_yy_pi");
						 
						 //AgreementExpDateYearPI drop down
						 if(rs.getInt("aggr_exp_date_yy_pi")==0){
							 AgreementExpdateYearDDLPI=0;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==1){
							 AgreementExpdateYearDDLPI=1;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==2){
							 AgreementExpdateYearDDLPI=2;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==3){
							 AgreementExpdateYearDDLPI=3;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==4){
							 AgreementExpdateYearDDLPI=4;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==5){
							 AgreementExpdateYearDDLPI=5;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==6){
							 AgreementExpdateYearDDLPI=6;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==7){
							 AgreementExpdateYearDDLPI=7;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==8){
							 AgreementExpdateYearDDLPI=8;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==9){
							 AgreementExpdateYearDDLPI=9;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==10){
							 AgreementExpdateYearDDLPI=10;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==11){
							 AgreementExpdateYearDDLPI=11;
						 }else if(rs.getInt("aggr_exp_date_yy_pi")==12){
							 AgreementExpdateYearDDLPI=12;
						 }
						
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type_pi")==1){
							 AgreementTypePI="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type_pi")==2){
							 AgreementTypePI="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type_pi")==3){
							 AgreementTypePI="Written-Not Witnessed";
						 }
						 
						//Agreement Type Radio button
						 
						 if(rs.getInt("census_shop_agreement_type_pi")==1){
							 AgreementTypePIRbtn=1;
						 }else if(rs.getInt("census_shop_agreement_type_pi")==2){
							 AgreementTypePIRbtn=2;
						 }else if(rs.getInt("census_shop_agreement_type_pi")==3){
							 AgreementTypePIRbtn=3;
						 }
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period_pi")==1){
							 AgreementPeriodPI="1";
						 }else if(rs.getInt("census_shop_agreement_period_pi")==2){
							 AgreementPeriodPI="2";
						 }else if(rs.getInt("census_shop_agreement_period_pi")==3){
							 AgreementPeriodPI="3 & Above";
						 }
						 
 						//Agreement Period radio btn
						 
						 if(rs.getInt("census_shop_agreement_period_pi")==1){
							 AgreementPeriodPIRbtn=1;
						 }else if(rs.getInt("census_shop_agreement_period_pi")==2){
							 AgreementPeriodPIRbtn=2;
						 }else if(rs.getInt("census_shop_agreement_period_pi")==3){
							 AgreementPeriodPIRbtn=3;
						 }
						 
						 //Agreement Type - 2
						 
						 if(rs.getInt("census_shop_agreement_type1_pi_1")==1){
							 AgreementType2PI += "Discount<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_pi_2")==1){
							 AgreementType2PI += "Signage<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_pi_3")==1){
							 AgreementType2PI += "Annual Support<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_pi_4")==1){
							 AgreementType2PI += "Exclusivity Fee<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_pi_5")==1){
							 AgreementType2PI += "Vol Incentive<br/>";
						 } if(rs.getString("census_shop_agreement_type_pi_other")!=""){
							 AgreementType2PI += "Any Other<br/>";
						 }
						 //Agreement Type - 2 radio buttn
						 
						
						 if(rs.getInt("census_shop_agreement_type1_pi_1")==1){
							 AgreementType2DiscPIRbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_pi_2")==1){
							 AgreementType2SigPIRbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_pi_3")==1){
							 AgreementType2AnnualPIRbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_pi_4")==1){
							 AgreementType2ExculPIRbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_pi_5")==1){
							 AgreementType2IncPIRbtn=1;
						 } if(rs.getString("census_shop_agreement_type_pi_other")!=""){
							 AgreementType2OtherPIRbtn=rs.getString("census_shop_agreement_type_pi_other");
						 }
						 
						 
						 /////////////////////////////////////////////////////////
						 
						 /////////////////////////////////
						 ////////////////Discount Agreement - KO//////////
						 
							//Per Raw Case Discount  - SSRB
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==1){
							  PerRawCaseDiscSSRBKO="1-25";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==2){
							  PerRawCaseDiscSSRBKO="26-35";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==3){
							  PerRawCaseDiscSSRBKO="36-45";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==4){
							  PerRawCaseDiscSSRBKO="46-75";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==5){
							  PerRawCaseDiscSSRBKO="76 Above";
							  //PerRawCaseDiscPET="";
						 }
						//Per Raw Case Discount  - SSRB radio button KO
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==1){
							 perCaseSSRBKORbtn=1;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==2){
							 perCaseSSRBKORbtn=2;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==3){
							 perCaseSSRBKORbtn=3;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==4){
							 perCaseSSRBKORbtn=4;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==5){
							 perCaseSSRBKORbtn=5;
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==6){
							 perCaseSSRBKORbtn=6;
							  //PerRawCaseDiscPET="";
						 }
						 
						 //Per Raw Case Discount  - PET
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==1){
							 PerRawCaseDiscPETKO="1-25";
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==2){
							 PerRawCaseDiscPETKO="26-35";
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==3){
							 PerRawCaseDiscPETKO="36-45";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==4){
							 PerRawCaseDiscPETKO="46-75";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==5){
							 PerRawCaseDiscPETKO="76 Above";
							  
						 }
						 //Per Raw Case Discount  - PET radio button KO
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==1){
							 perCasePETKORbtn=1;
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==2){
							 perCasePETKORbtn=2;
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==3){
							 perCasePETKORbtn=3;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==4){
							 perCasePETKORbtn=4;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==5){
							 perCasePETKORbtn=5;
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==6){
							 perCasePETKORbtn=6;
							  
						 }
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDateKO = rs.getInt("aggr_exp_date_mm_ko")+"/"+rs.getInt("aggr_exp_date_yy_ko");
						 
						 AgreementExpdateMonthKO= rs.getInt("aggr_exp_date_mm_ko");
						 //AgreementExpdateMonth DropDown
						 if(rs.getInt("aggr_exp_date_mm_ko")==0){
							 AgreementExpdateMonthDDLKO=0;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==1){
							 AgreementExpdateMonthDDLKO=1;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==2){
							 AgreementExpdateMonthDDLKO=2;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==3){
							 AgreementExpdateMonthDDLKO=3;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==4){
							 AgreementExpdateMonthDDLKO=4;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==5){
							 AgreementExpdateMonthDDLKO=5;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==6){
							 AgreementExpdateMonthDDLKO=6;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==7){
							 AgreementExpdateMonthDDLKO=7;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==8){
							 AgreementExpdateMonthDDLKO=8;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==9){
							 AgreementExpdateMonthDDLKO=9;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==10){
							 AgreementExpdateMonthDDLKO=10;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==11){
							 AgreementExpdateMonthDDLKO=11;
						 }else if(rs.getInt("aggr_exp_date_mm_ko")==12){
							 AgreementExpdateMonthDDLKO=12;
						 }
						 
						
						 AgreementExpdateYearKO=rs.getInt("aggr_exp_date_yy_ko");
						 
						 //AgreementExpDateYearKO drop down
						 if(rs.getInt("aggr_exp_date_yy_ko")==1){
							 AgreementExpdateYearDDLKO=1;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==2){
							 AgreementExpdateYearDDLKO=2;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==3){
							 AgreementExpdateYearDDLKO=3;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==4){
							 AgreementExpdateYearDDLKO=4;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==5){
							 AgreementExpdateYearDDLKO=5;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==6){
							 AgreementExpdateYearDDLKO=6;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==7){
							 AgreementExpdateYearDDLKO=7;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==8){
							 AgreementExpdateYearDDLKO=8;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==9){
							 AgreementExpdateYearDDLKO=9;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==10){
							 AgreementExpdateYearDDLKO=10;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==11){
							 AgreementExpdateYearDDLKO=11;
						 }else if(rs.getInt("aggr_exp_date_yy_ko")==12){
							 AgreementExpdateYearDDLKO=12;
						 }
						
						 
						 
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type_ko")==1){
							 AgreementTypeKO="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type_ko")==2){
							 AgreementTypeKO="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type_ko")==3){
							 AgreementTypeKO="Written-Not Witnessed";
						 }
 						//Agreement Type KO radio button
						 
						 if(rs.getInt("census_shop_agreement_type_ko")==1){
							 AgreementTypeKORbtn=1;
						 }else if(rs.getInt("census_shop_agreement_type_ko")==2){
							 AgreementTypeKORbtn=2;
						 }else if(rs.getInt("census_shop_agreement_type_ko")==3){
							 AgreementTypeKORbtn=3;
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period_ko")==1){
							 AgreementPeriodKO="1";
						 }else if(rs.getInt("census_shop_agreement_period_ko")==2){
							 AgreementPeriodKO="2";
						 }else if(rs.getInt("census_shop_agreement_period_ko")==3){
							 AgreementPeriodKO="3 & Above";
						 }
						 
						//Agreement Period radio Btn
						 
						 if(rs.getInt("census_shop_agreement_period_ko")==1){
							 AgreementPeriodKORbtn=1;
						 }else if(rs.getInt("census_shop_agreement_period_ko")==2){
							 AgreementPeriodKORbtn=2;
						 }else if(rs.getInt("census_shop_agreement_period_ko")==3){
							 AgreementPeriodKORbtn=3;
						 }
						 //Agreement Type - 2
						 
						 if(rs.getInt("census_shop_agreement_type1_ko_1")==1){
							 AgreementType2KO += "Discount<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_ko_2")==1){
							 AgreementType2KO += "Signage<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_ko_3")==1){
							 AgreementType2KO += "Annual Support<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_ko_4")==1){
							 AgreementType2KO += "Exclusivity Fee<br/>";
						 } if(rs.getInt("census_shop_agreement_type1_ko_5")==1){
							 AgreementType2KO += "Vol Incentive<br/>";
						 } if(rs.getString("census_shop_agreement_type_ko_other")!=""){
							 AgreementType2KO += "Any Other<br/>";
						 }
						 //Agreement Type - 2
						
				 
						 if(rs.getInt("census_shop_agreement_type1_ko_1")==1){
							 AgreementType2DKORbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_ko_2")==1){
							 AgreementType2SKORbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_ko_3")==1){
							 AgreementType2AKORbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_ko_4")==1){
							 AgreementType2EXKORbtn=1;
						 } if(rs.getInt("census_shop_agreement_type1_ko_5")==1){
							 AgreementType2VolKORbtn=1;
						 } if(rs.getString("census_shop_agreement_type_ko_other")!=""){
							 AgreementTypeOKORbtn=rs.getString("census_shop_agreement_type_ko_other");
						 }
						 
						 
						 
						 /////////////////////////////////////////////////////////
						 
						 
						 //// Supplied by Ko 
						 
						 //census_feeded_stock_percentage_1,census_feeded_stock_percentage_2,census_feeded_stock_percentage_3,census_feeded_stock_percentage_4
				
						if(!rs.getString("census_feeded_stock_percentage_1").equals("null")){
							SuppliedByDistributorKO=rs.getString("census_feeded_stock_percentage_1");
						}
						 
						if(!rs.getString("census_feeded_stock_percentage_2").equals("null")){
							SuppliedByWholeSellerKO=rs.getString("census_feeded_stock_percentage_2");
						}
						
						if(!rs.getString("census_feeded_stock_percentage_3").equals("null")){
							SuppliedByMobilerKO=rs.getString("census_feeded_stock_percentage_3");
						}
						
						if(!rs.getString("census_feeded_stock_percentage_4").equals("null")){
							SuppliedByDealerKO=rs.getString("census_feeded_stock_percentage_4");
						}
						 
						 
						 //Supplied by PI 
						 
						 //census_feeded_stock_1_p_pi_21,census_feeded_stock_2_p_pi_21,census_feeded_stock_3_p_pi_21,census_feeded_stock_4_p_pi_21
				
						if(!rs.getString("census_feeded_stock_1_p_pi_21").equals("null")){
							SuppliedByDistributorPI=rs.getString("census_feeded_stock_1_p_pi_21");
						}
						 
						if(!rs.getString("census_feeded_stock_2_p_pi_21").equals("null")){
							SuppliedByWholeSellerPI=rs.getString("census_feeded_stock_2_p_pi_21");
						}
						
						if(!rs.getString("census_feeded_stock_3_p_pi_21").equals("null")){
							SuppliedByMobilerPI=rs.getString("census_feeded_stock_3_p_pi_21");
						}
						
						if(!rs.getString("census_feeded_stock_4_p_pi_21").equals("null")){
							SuppliedByDealerPI=rs.getString("census_feeded_stock_4_p_pi_21");
						}
				}
				
				%>
				
				
				
			
				<input type="hidden" name="ID" value="<%=CensusID%>">
				<table style="width:100%;">
				
					<tr style="font-size:13px; text-align:left;">
						<th >Outlet ID</th>
						<td><input type="text" name="outlet_general" data-mini="true" id="outlet_general" value=<%=OutletID %>></td>
						<th >System Outlet Name</th>
						<td><input type="text" name="System_Outlet_Name_general" data-mini="true"  id="System_Outlet_Name_general" value="<%=OutletName %>"></td>
					</tr >
					<tr  style="font-size:13px; text-align:left;">	
						<th >Outlet Name On Board</th>
						<td><input type="text" name="Outlet_Name_On_Board_general" data-mini="true"  id="Outlet_Name_On_Board_general" value="<%=OutletNameOB %>"></td>						
						<th >Actual Outlet Name</th>
						<td><input type="text" name="Actual_Outlet_Name_general"  data-mini="true" id="Actual_Outlet_Name_general" value="<%=OutletNameActual %>"></td>
						
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						
						<th >Distributor ID:</th>
						<td><input type="text" name="DistributorID_general" data-mini="true"  id="Distributor_general" value=<%=Distributor_id %>></td>
						<th >Distributor Name</th>
						<td><input type="text" name="DistributorName_general" readonly="readonly" data-mini="true"  id="DistributorName_general" value="<%=Distributor_name %>"></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">	
						<th >PJP ID</th>
						<td><input type="text" name="PJPID_general"  data-mini="true"  onchange="getPJPName()" id="PJP_ID" value=<%=PJPID %>></td>
						<th >PJP Name</th>
						<td><input type="text" name="PJPName_general"  readonly="readonly" data-mini="true"  id="PJP_name" value="<%=PJPName %>"></td>
					</tr>
					
					
					
					<tr style="font-size:13px; text-align:left;">
						<th> Village</th>
						<td><input type="text" name="MVillage_general" data-mini="true"  id="MVillage_general" value="<%=MVillage %>"></td>
					
						<th >Land Mark</th>
						<td><input type="text" name="MLandMark_general" data-mini="true"  id="System_Outlet_Name_general" value=<%=MLandMark %>></td>
					</tr>
						<tr style="font-size:13px; text-align:left;">
						<th >Owner Name</th>
						<td><input type="text" name="OwnerName_general" data-mini="true" id="outlet_general" value="<%=OwnerName %>"></td>
						<th >Landline</th>
						<td><input type="text" name="OwnerContactNo1_general" data-mini="true"  id="System_Outlet_Name_general" value="<%=OwnerContactNo1 %>"></td>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th >Mobile</th>
						<td><input type="text" name="OwnerContactNo2_general" data-mini="true"  id="Outlet_Name_On_Board_general" value="<%=OwnerContactNo2 %>"></td>						
						<th >Owner CNIC</th>
						<td><input type="text" name="OwnerCNIC_general"  data-mini="true" id="Actual_Outlet_Name_general" value=<%=OwnerCNIC %>></td>
						
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						<th>Contact Person</th>
						
						<td >
                            <div data-role="fieldcontain">
							  
							    <select  name="ContactPerson" data-mini="true" id="select-native-17">
							        <option value="1" <%if(ContactPersonDDL==1){ %> selected<%} %>>Owner</option>
							        <option value="2" <%if(ContactPersonDDL==2){ %> selected<%} %>>Employee</option>
							        <option value="3" <%if(ContactPersonDDL==3){ %> selected<%} %>>First Relation</option>
							        <option value="4" <%if(ContactPersonDDL==4){ %> selected<%} %>>Friend</option>
							        <option value="5" <%if(ContactPersonDDL==5){ %> selected<%} %>>Partner</option>
							    </select>
							</div>
						</td>
						<th>Address (with key location)</th>
						<td ><input type="text" name="OutletAddress" data-mini="true"  id="System_Outlet_Name_general" value="<%=OutletAddress %>"></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						
						<th >Contact Person Name</th>
						<td><input type="text" name="ContactPersonName_general" data-mini="true"  id="Distributor_general"  value="<%=ContactPersonName %>"></td>
						
						<th>Conact person No</th>
						<td><input type="text" name="ContactPersonNo_general" data-mini="true"  id="PJP_general" value=<%=ContactPersonNo %>></td>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						
						<th>Contact Person CNIC</th>
						<td><input type="text" name="ContactPersonCNIC_general" data-mini="true"  id="Distributor_general" value=<%=ContactPersonCNIC %>></td>
						
						<th>Email</th>
						<td><input type="text" name="EmailID_general" data-mini="true"  id="PJP_general" value=<%=EmailID %>></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						
						<th  >Createdby ID</th>
						<td ><input type="text" name="Createdbyid_general"    data-mini="true"  id="Distributor_general" value=<%=CreatedBYId %>></td>
						<th  >Createdby Name</th>
						<td ><input type="text" name="Createdbyname_general"   readonly="readonly" data-mini="true"  id="Distributor_general" value="<%=CreatedByName %>"></td>
					</tr>
					 
					 
					 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						<%
						
						if(Area.equals("Urban")){
							AreaRadioBtn=1;
						}else{
							AreaRadioBtn=2;
						}
						
						%>
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Area:</legend>
						        <input type="radio" name="Area_general" id="radio-choice-w-6a" value="1" <%if(AreaRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Urban</label>
						        <input type="radio" name="Area_general" id="radio-choice-w-6b" value="2"  <%if(AreaRadioBtn==2){ %>checked=checked<%} %>>
						        
						        <label for="radio-choice-w-6b">Rural</label>
						       
						    </fieldset>
					    </td>
					<!--	<th style="background-color:#f4f3f3">Area</th>
				<td><input type="text" name="Area_general" id="Area_general" value=<%=Area %>></td>
					-->
					</tr>
					<tr style="font-size:13px; text-align:left;">
					<td  colspan="4">
					<%
						int LocationRadioBtn=0;
						if(Location.equals ("Main Street")){
							LocationRadioBtn=1;
						}else if(Location.equals("Secondary Street")){
							LocationRadioBtn=2;
						}else{
							LocationRadioBtn=3;	
						}
						
						%>
						
					 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
				        <legend>Location:</legend>
				        <input type="radio" name="location_general" id="radio-choice-w-6a" value="1" <%if(LocationRadioBtn==1){ %>checked=checked<%} %>>
				        <label for="radio-choice-w-6a">main Street</label>
				        <input type="radio" name="location_general" id="radio-choice-w-6b" value="2" <%if(LocationRadioBtn==2){ %>checked=checked<%} %>>
				        <label for="radio-choice-w-6b">Secondary Street</label>
				        <input type="radio" name="location_general" id="radio-choice-w-6c" value="3" <%if(LocationRadioBtn==3){ %>checked=checked<%} %>>
				        <label for="radio-choice-w-6c">Highway</label>
				    </fieldset>
				    
				    </td>
				   </tr> 
					<!--*****************************  LOCATION ***************************************
						<th style="background-color:#f4f3f3">Location</th>
						<td><%=Location %></td>
						
						-->
						
						
					
					<tr style="font-size:13px; text-align:left;">   
						<td  colspan="4">
						<%
						int shopLocationRadioBtn=0;
						if(ShopLocation.equals ("Residential")){
							shopLocationRadioBtn=1;
						}else if(ShopLocation.equals("Commercial")){
							shopLocationRadioBtn=2;
						}else{
							shopLocationRadioBtn=3;	
						}
						
						%>
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Shop Location:</legend>
						        <input type="radio" name="Shop_location_general" id="radio-choice-w-6a" value="1" <%if(shopLocationRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Residential</label>
						        <input type="radio" name="Shop_location_general" id="radio-choice-w-6b" value="2" <%if(shopLocationRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">Commercial</label>
						        <input type="radio" name="Shop_location_general" id="radio-choice-w-6c" value="3" <%if(shopLocationRadioBtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Industrial</label>
						    </fieldset>
						    
					    </td>
				    </tr>
				   <tr style="font-size:13px; text-align:left;"> 
						<td  colspan="4">
						<%
						int ecoRadioBtn=0;
						if(SocioEco.equals ("High")){
							ecoRadioBtn=1;
						}else if(SocioEco.equals("Medium")){
							ecoRadioBtn=2;
						}else{
							ecoRadioBtn=3;	
						}
						
						%>
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Socio Econimic Classification:</legend>
						        <input type="radio" name="SocioEconimicClassification_general" id="radio-choice-w-6a" value="1" <%if(ecoRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">High </label>
						        <input type="radio" name="SocioEconimicClassification_general" id="radio-choice-w-6b" value="2" <%if(ecoRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">Medium</label>
						        <input type="radio" name="SocioEconimicClassification_general" id="radio-choice-w-6c" value="3" <%if(ecoRadioBtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Low</label>
							 </fieldset>
						    
					    </td>
				    </tr>	     
					<tr style="font-size:13px; text-align:left;">	
						<td  colspan="4">
						<%
						int OTRadioBtn=0;
						if(OutletType.equals ("Individual Outlet")){
							OTRadioBtn=1;
						}else if(OutletType.equals("Clustered Outlet")){
							OTRadioBtn=2;
						}
						
						%>
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Outlet Type:</legend>
						        <input type="radio" name="OutletType_general" id="radio-choice-w-6a" value="1" <%if(OTRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Individual Outlet</label>
						        <input type="radio" name="OutletType_general" id="radio-choice-w-6b" value="2" <%if(OTRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">Clustered Outlet</label>
						       
						    </fieldset>
						    
					  	</td>
					</tr>
					
					
				 
					
					
					<tr style="font-size:13px; text-align:left;">
						
						
						<td colspan="4">
							 <fieldset data-role="controlgroup" data-mini="true" data-iconpos="right">
						        <legend>Shop Closed Status:</legend>
						        <input type="checkbox" name="IsShopClosedStatus" id="checkbox-h-isStatus" onclick="DoRadBtnDisable()" value="1" <%if(IsShopClosedStatusNRbtn==1){ %>checked=checked<%} %>>
						        <label for="checkbox-h-isStatus">Closed</label>
						        <input type="radio" name="ShopClosedStatus" id="checkbox-h-6a" value="1"  <%if(ShopClosedStatusNRbtn==1){ %>checked=checked<%} %>>
						        <label for="checkbox-h-6a">Permanently</label>
						        <input type="radio" name="ShopClosedStatus" id="checkbox-h-6b" value="2"  <%if(ShopClosedStatusNRbtn==2){ %>checked=checked<%} %>>
						        <label for="checkbox-h-6b">Temporarily</label>
						    </fieldset>
						</td>	
					</tr>	
					
					<tr style="font-size:13px; text-align:left;">	
						<td  colspan="4">
						<%
						int OSRadioBtn=0;
						if(OutletStructure.equals ("Fixed Structure")){
							OSRadioBtn=1;
						}else if(OutletStructure.equals("Non-Fix Structure")){
							OSRadioBtn=2;
						}
						
						%>
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Outlet Structure:</legend>
						        <input type="radio" name="OutletStructure_general" id="radio-choice-w-6a" value="1" <%if(OSRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Fixed Structure</label>
						        <input type="radio" name="OutletStructure_general" id="radio-choice-w-6b" value="2"<%if(OSRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">Non-Fix Structure</label>
						       
						    </fieldset>
						    
					  	</td>
					</tr>
						<!--  census_distributor_tehsil='"+Address +"',census_distributor_district='"+age +"',census_owner_contact_person_relation='"+Address +"',census_trader_channel_sub_channel='"+age +"'-->
					
					
					<tr style="font-size:13px; text-align:left;">   
						<td  colspan="4">
						<%
						int STRadioBtn=0;
						if(ServiceType.equals ("Self")){
							STRadioBtn=1;
						}else if(ServiceType.equals("Counter")){
							STRadioBtn=2;
						}else{
							STRadioBtn=3;	
						}
						
						%>
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Service Type:</legend>
						        <input type="radio" name="ServiceType_general" id="radio-choice-w-6a" value="1" <%if(STRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Self</label>
						        <input type="radio" name="ServiceType_general" id="radio-choice-w-6b" value="2"<%if(STRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">Counter</label>
						        <input type="radio" name="ServiceType_general" id="radio-choice-w-6c" value="3"<%if(STRadioBtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Both</label>
						    </fieldset>
						    
					    </td>
				    </tr>
				    
				   <tr style="font-size:13px; text-align:left;">	
						<td  colspan="4">
						<%
						int ShopTypeRadioBtn=0;
						if(ShopType.equals ("Beverage Selling")){
							ShopTypeRadioBtn=1;
						}else if(ShopType.equals("Non Beverage Selling")){
							ShopTypeRadioBtn=2;
						}					
						%>
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Shop Type:</legend>
						        <input type="radio" name="ShopType_general" id="radio-choice-w-6a" value="1" <%if(ShopTypeRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Beverage Selling</label>
						        <input type="radio" name="ShopType_general" id="radio-choice-w-6b" value="2" <%if(ShopTypeRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">Non Beverage Selling</label>
						       
						    </fieldset>
						    
					  	</td>
					</tr> 
					
					
				
					
					<tr style="font-size:13px; text-align:left;">   
						<td  colspan="4">
						<%
						int ASFRadioBtn=0;
						if(AreaSqFt.equals ("Upto 100")){
							ASFRadioBtn=1;
						}else if(AreaSqFt.equals("100-500")){
							ASFRadioBtn=2;
						}else if(AreaSqFt.equals("500-1000")){
							ASFRadioBtn=3;
						}else{
							ASFRadioBtn=4;	
						}
						
						%>
						
						
							 <fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Area (Sq.ft):</legend>
						        <input type="radio" name="AreaSqFT" id="radio-choice-w-6a" value="1" <%if(ASFRadioBtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Upto 100</label>
						        <input type="radio" name="AreaSqFT" id="radio-choice-w-6b" value="2" <%if(ASFRadioBtn==2){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6b">100-500</label>
						        <input type="radio" name="AreaSqFT" id="radio-choice-w-6c" value="3" <%if(ASFRadioBtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">500-100</label>
						        <input type="radio" name="AreaSqFT" id="radio-choice-w-6d" value="4"<%if(ASFRadioBtn==4){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6d">1000 & Above</label>
						    </fieldset>
						    
					    </td>
				    </tr>
						
					<tr >
						<th style=" text-align:left;" >District</th>
						<td >
                            <div data-role="fieldcontain">
							  
							
							  
							  
							    <select name="District" data-mini="true" id="select-native-17">
							        <option value="1" <%if(OutletDistrictDDL==1){ %> selected<%} %>>FAISALABAD</option>
							        <option value="2" <%if(OutletDistrictDDL==2){ %> selected<%} %>>TOBA TEKSINGH</option>
							        <option value="3"<%if(OutletDistrictDDL==3){ %> selected<%} %>>JHANG</option>
							        <option value="4" <%if(OutletDistrictDDL==4){ %> selected<%} %>>CHINIOT</option>
							    </select>
							</div>
						</td>
						<th  style=" text-align:left;">Tehsil</th>
					
						
						<td >
							<div data-role="fieldcontain">
						 	
						    <select name="Tehsil" data-mini="true" id="select-native-17">
						       
						        <option value="1"  <%if(OutletTehsilDDL==1){ %> selected<%} %>>CHINIOT</option>
						        <option value="2" <%if(OutletTehsilDDL==2){ %> selected<%} %>>BHOWANA</option>
						        <option value="3"  <%if(OutletTehsilDDL==3){ %> selected<%} %>> JHANG</option>
						        <option value="4" <%if(OutletTehsilDDL==4){ %> selected<%} %>>GOJRA</option>
						        <option value="5"  <%if(OutletTehsilDDL==5){ %> selected<%} %>>FAISALABAD</option>
						        <option value="6" <%if(OutletTehsilDDL==6){ %> selected<%} %>>TANDLIANWALA</option>
						        <option value="7"  <%if(OutletTehsilDDL==7){ %> selected<%} %>>SAMUNDRI</option>
						        <option value="8" <%if(OutletTehsilDDL==8){ %> selected<%} %>>JARANWALA</option>
						         <option value="9" <%if(OutletTehsilDDL==9){ %> selected<%} %>>JHUMRA</option>
						    </select>
							</div>
						</td>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th  > trade Sub Channel</th>
						<td >
							<div data-role="fieldcontain">
						
					 
						 	
						    <select name="TradeSubChannel" data-mini="true" id="select-native-17">
						       	<option value="1"  <%if(TradeSubChannelDDL==1){ %> selected<%} %>>Kiryana Store</option>
						   	    <option value="2"  <%if(TradeSubChannelDDL==2){ %> selected<%} %>>General Store</option>
						        <option value="3" <%if(TradeSubChannelDDL==3){ %> selected<%} %>>Pan/Cigarette Shop</option>
						        <option value="4"  <%if(TradeSubChannelDDL==4){ %> selected<%} %>>Beverage Street Vendor</option>
						        <option value="5" <%if(TradeSubChannelDDL==5){ %> selected<%} %>>Modern General Store</option>
						        <option value="6"  <%if(TradeSubChannelDDL==6){ %> selected<%} %>>Supermarket"</option>
						        <option value="7" <%if(TradeSubChannelDDL==7){ %> selected<%} %>>Hypermarket</option>
						        <option value="8"  <%if(TradeSubChannelDDL==8){ %> selected<%} %>>Fine Restaurant</option>
						        <option value="9" <%if(TradeSubChannelDDL==9){ %> selected<%} %>>Fast Food Restaurant</option>
						        <option value="10" <%if(TradeSubChannelDDL==10){ %> selected<%} %>>Food Courts</option>
						        <option value="11"  <%if(TradeSubChannelDDL==11){ %> selected<%} %>>Conventional Restaurant</option>
						        <option value="12" <%if(TradeSubChannelDDL==12){ %> selected<%} %>>Food Street Outlets</option>
						        <option value="13"  <%if(TradeSubChannelDDL==13){ %> selected<%} %>>Local Food Stand</option>
						        <option value="14" <%if(TradeSubChannelDDL==14){ %> selected<%} %>>Airport</option>
						        <option value="15"  <%if(TradeSubChannelDDL==15){ %> selected<%} %>>AirLine</option>
						        <option value="16" <%if(TradeSubChannelDDL==16){ %> selected<%} %>>Railway Station</option>
						        <option value="17"  <%if(TradeSubChannelDDL==17){ %> selected<%} %>>Railways</option>
						        <option value="18" <%if(TradeSubChannelDDL==18){ %> selected<%} %>>Bus Stand</option>
						        <option value="19" <%if(TradeSubChannelDDL==19){ %> selected<%} %>>Bus Service</option>
						        <option value="20"  <%if(TradeSubChannelDDL==20){ %> selected<%} %>>"Bakeries/Sweet Shops</option>
						        <option value="21" <%if(TradeSubChannelDDL==21){ %> selected<%} %>>Medical Stores</option>
						        <option value="22"  <%if(TradeSubChannelDDL==22){ %> selected<%} %>>Petroleum Food Marts</option>
						        <option value="23" <%if(TradeSubChannelDDL==23){ %> selected<%} %>>School/Colleges/Universities</option>
						        <option value="24"  <%if(TradeSubChannelDDL==24){ %> selected<%} %>>Hospitals</option>
						        <option value="25" <%if(TradeSubChannelDDL==25){ %> selected<%} %>>Hotels</option>
						        <option value="26"  <%if(TradeSubChannelDDL==26){ %> selected<%} %>>Clubs</option>
						        <option value="27" <%if(TradeSubChannelDDL==27){ %> selected<%} %>>Marriage Hall/Caterers</option>
						        <option value="28" <%if(TradeSubChannelDDL==28){ %> selected<%} %>>Cinemas"</option>
						        <option value="29"  <%if(TradeSubChannelDDL==29){ %> selected<%} %>>Park/Zoo/Play Land</option>
						        <option value="30" <%if(TradeSubChannelDDL==30){ %> selected<%} %>>Utility Stores/CSD</option>
						        <option value="31"  <%if(TradeSubChannelDDL==31){ %> selected<%} %>>Institutional Canteens</option>
						        <option value="32" <%if(TradeSubChannelDDL==32){ %> selected<%} %>>Wholesaler"</option>
						        <option value="33"  <%if(TradeSubChannelDDL==33){ %> selected<%} %>>FAISALABAD</option>
						        <option value="34" <%if(TradeSubChannelDDL==34){ %> selected<%} %>>Modern Meat Shop</option>
						        <option value="35"  <%if(TradeSubChannelDDL==35){ %> selected<%} %>>PCO</option>
						        <option value="36" <%if(TradeSubChannelDDL==36){ %> selected<%} %>>Snooker Club</option>
						        <option value="37" <%if(TradeSubChannelDDL==37){ %> selected<%} %>>Other Beverage Selling Outlets</option>
						    </select>
							</div>
						</td>
					</tr>	
					
						
						  
				
				
					
				</table>
				
				
			
				</li>	
				
				<li data-role="list-divider" data-theme="b">Shopkeeper</li>
			<li>		
					
		
		
				<table style="width:100%;">
				
				<tr>
				<td colspan="2">
				 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Outlet Selling Beverages in full cases?</legend>
				 </fieldset>
				</td>
				</tr>
				 <tr style="font-size:13px; text-align:left;">	
						
						<th>SSRB-Percentage</th>
						<td><input type="text" name="SSRB" data-mini="true"  id="Distributor_general" value=<%=BeveragesSellingFullCasesSSRB %>></td>
				
						<th>PET-Percentage</th>
						<td><input type="text" name="PET" data-mini="true"  id="Distributor_general" value=<%=BeveragesSellingFullCasesPET %>></td>
				</tr>	
				
				 <tr style="font-size:13px; text-align:left;">	
						
						<th >Tetra-Percentage</th>
						<td ><input type="text" name="TEtra" data-mini="true"  id="Distributor_general" value=<%=BeveragesSellingFullCasesTETRA %>></td>
				
						<th >CAN-Percentage</th>
						<td ><input type="text" name="CAN" data-mini="true"  id="Distributor_general" value=<%=BeveragesSellingFullCasesCAN %>></td>
				</tr>	
				
				
				 <tr style="font-size:13px; text-align:left;">	
				 	<td><fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Supplied By -KO</legend>
						        
						         </fieldset>
						 </td>
				 </tr>
						
				 <tr style="font-size:13px; text-align:left;">	
						
					
						<th >Distributor-Percentage</th>
						<td><input type="text" name="SuppliedKoDistributor" data-mini="true" id="outlet_general" value=<%=SuppliedByDistributorKO %>></td>
						
						<th >Wholeseller-Percentage</th>
						<td><input type="text" name="SuppliedKoWholeseller" data-mini="true" id="outlet_general" value=<%=SuppliedByWholeSellerKO %>></td>
						
					</tr>	
					 <tr style="font-size:13px; text-align:left;">	
						
						
						<th >Mobiler-Percentage</th>
						<td><input type="text" name="SuppliedKoMobiler" data-mini="true" id="outlet_general" value=<%=SuppliedByMobilerKO %>></td>
						
						<th >Dealer-Percentage</th>
						<td><input type="text" name="SuppliedKoDealer" data-mini="true" id="outlet_general" value=<%=SuppliedByDealerKO %>></td>
						
					</tr>	
					
					
				<tr style="font-size:13px; text-align:left;">	
				 	<td><fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Supplied By -PI</legend>
						       
						         </fieldset>
						 </td>
				 </tr>
					
					 <tr style="font-size:13px; text-align:left;">	
						
						
						<th >Distributor-Percentage</th>
						<td><input type="text" name="SuppliedPIDistributor" data-mini="true" id="outlet_general" value=<%=SuppliedByDistributorPI %>></td>
						
						<th >Wholeseller-Percentage</th>
						<td><input type="text" name="SuppliedPIWholeseller" data-mini="true" id="outlet_general" value=<%=SuppliedByWholeSellerPI %>></td>
						
					</tr>	
					 <tr style="font-size:13px; text-align:left;">	
						
						
						<th>Mobiler-Percentage</th>
						<td><input type="text" name="SuppliedPIMobiler" data-mini="true" id="outlet_general" value=<%=SuppliedByMobilerPI %>></td>
						
						<th>Dealer-Percentage</th>
						<td><input type="text" name="SuppliedPIDealer" data-mini="true" id="outlet_general" value=<%=SuppliedByDealerPI %>></td>
						
					</tr>	
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
					
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Shop Status</legend>
						        <input type="radio" name="ShopStatusShopkeeper" id="radio-choice-w-6a" value="1"<%if(ShopStatusRbtn==1){ %>checked=checked<%} %> >
						        <label for="radio-choice-w-6a">Seasonal</label>
						        <input type="radio" name="ShopStatusShopkeeper" id="radio-choice-w-6b" value="2" <%if(ShopStatusRbtn==2){ %>checked=checked<%} %> >
						        
						        <label for="radio-choice-w-6b">Permanent</label>
						       
						    </fieldset>
					    </td>
				</tr>
				
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
				 
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Exculsivity (Agreement Based)</legend>
						        <input type="radio" name="ExAgreeBase" id="radio-choice-w-6a" value="1" <%if(ExclusivityAgreementPIRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Pepsi</label>
						        <input type="radio" name="ExAgreeBase" id="radio-choice-w-6b" value="2"  <%if(ExclusivityAgreementPIRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Coke</label>
						        <input type="radio" name="ExAgreeBase" id="radio-choice-w-6c" value="3" <%if(ExclusivityAgreementPIRbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Gourment</label>
						        <input type="radio" name="ExAgreeBase" id="radio-choice-w-6d" value="4"  <%if(ExclusivityAgreementPIRbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">Mezan</label>
						     	<input type="radio" name="ExAgreeBase" id="radio-choice-w-6e" value="5"  <%if(ExclusivityAgreementPIRbtn==5){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6e">Other</label>
						       
						    </fieldset>
					    </td>
				</tr>
					
					 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						 
					
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Per Raw Case Discount - SSRB</legend>
						        <input type="radio" name="PerRawCaseSSRB" id="radio-choice-w-6a" value="1"<%if(PerRawCaseDiscSSRBRadbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1-25</label>
						        <input type="radio" name="PerRawCaseSSRB" id="radio-choice-w-6b" value="2"  <%if(PerRawCaseDiscSSRBRadbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">26-35</label>
						        <input type="radio" name="PerRawCaseSSRB" id="radio-choice-w-6c" value="3" <%if(PerRawCaseDiscSSRBRadbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">36-45</label>
						        <input type="radio" name="PerRawCaseSSRB" id="radio-choice-w-6d" value="4"  <%if(PerRawCaseDiscSSRBRadbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">46-75</label>
						     	 <input type="radio" name="PerRawCaseSSRB" id="radio-choice-w-6e" value="5" <%if(PerRawCaseDiscSSRBRadbtn==5){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">76 Above</label>
						        <input type="radio" name="PerRawCaseSSRB" id="radio-choice-w-6f" value="6"  <%if(PerRawCaseDiscSSRBRadbtn==6){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6f">None</label>
						       
						    </fieldset>
					    </td>
				</tr>
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Per Raw Case Discount - PET</legend>
						        <input type="radio" name="PerRawCasePET" id="radio-choice-w-6a" value="1" <%if(PerRawCaseDiscPETRadbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1-25</label>
						        <input type="radio" name="PerRawCasePET" id="radio-choice-w-6b" value="2"  <%if(PerRawCaseDiscPETRadbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">26-35</label>
						        <input type="radio" name="PerRawCasePET" id="radio-choice-w-6c" value="3" <%if(PerRawCaseDiscPETRadbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">36-45</label>
						        <input type="radio" name="PerRawCasePET" id="radio-choice-w-6d" value="4"  <%if(PerRawCaseDiscPETRadbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">46-75</label>
						     	 <input type="radio" name="PerRawCasePET" id="radio-choice-w-6e" value="5" <%if(PerRawCaseDiscPETRadbtn==5){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">76 Above</label>
						        <input type="radio" name="PerRawCasePET" id="radio-choice-w-6f" value="6"  <%if(PerRawCaseDiscPETRadbtn==6){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6f">None</label>
						       
						    </fieldset>
					    </td>
				</tr>
					<tr>
					<td colspan="2">
								<fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
									      	  <legend>Agreement Expiry Date</legend>
								</fieldset>
					</td>
					
				</tr>
				<tr style="font-size:13px; text-align:left;">
						<th >Month</th>
						<td >
							 <div data-role="fieldcontain">
							  
							    <select name="Month1" data-mini="true" id="select-native-17">
							    	<option value="0"<%if(  AgreementExpdateMonthDDL==0){ %> selected<%} %>>Select </option>
							        <option value="1"<%if(  AgreementExpdateMonthDDL==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if(  AgreementExpdateMonthDDL==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if(  AgreementExpdateMonthDDL==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if(  AgreementExpdateMonthDDL==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if(  AgreementExpdateMonthDDL==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if(  AgreementExpdateMonthDDL==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if(  AgreementExpdateMonthDDL==7){ %> selected<%} %>>7 </option>
							        <option value="8"<%if(  AgreementExpdateMonthDDL==8){ %> selected<%} %>>8 </option>
							        <option value="9"<%if(  AgreementExpdateMonthDDL==9){ %> selected<%} %>>9 </option>
							        <option value="10"<%if(  AgreementExpdateMonthDDL==10){ %> selected<%} %>>10 </option>
							        <option value="11"<%if(  AgreementExpdateMonthDDL==11){ %> selected<%} %>>11 </option>
							        <option value="12"<%if(  AgreementExpdateMonthDDL==12){ %> selected<%} %>>12 </option>
							    </select>
							</div>
						
						</td>
					
						<th >Year</th>
						<td >
							 <div data-role="fieldcontain">
							  
							    <select name="Year1" data-mini="true" id="select-native-17">
							       <option value="0"<%if(  AgreementExpdateYearDDL==0){ %> selected<%} %>>Select </option>
							        <option value="1"<%if( AgreementExpdateYearDDL==1){ %> selected<%} %>>2010 </option>
							        <option value="2"<%if( AgreementExpdateYearDDL==2){ %> selected<%} %>>2011 </option>
							        <option value="3"<%if( AgreementExpdateYearDDL==3){ %> selected<%} %>>2012 </option>
							        <option value="4"<%if( AgreementExpdateYearDDL==4){ %> selected<%} %>>2013 </option>
							       	<option value="5"<%if( AgreementExpdateYearDDL==5){ %> selected<%} %>>2014 </option>
							        <option value="6"<%if( AgreementExpdateYearDDL==6){ %> selected<%} %>>2015 </option>
							        <option value="7"<%if( AgreementExpdateYearDDL==7){ %> selected<%} %>>2016 </option>
							        <option value="8"<%if( AgreementExpdateYearDDL==8){ %> selected<%} %>>2017 </option>
							        <option value="9"<%if( AgreementExpdateYearDDL==9){ %> selected<%} %>>2018 </option>
							        <option value="10"<%if(AgreementExpdateYearDDL==10){ %> selected<%} %>>2019 </option>
							        <option value="11"<%if( AgreementExpdateYearDDL==11){ %> selected<%} %>>2020 </option>
							        <option value="12"<%if( AgreementExpdateYearDDL==12){ %> selected<%} %>>2021</option>
							    </select>
							</div>
						
						</td>
					</tr>
					 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Type</legend>
						        <input type="radio" name="AgreeType" id="radio-choice-w-6a" value="1" <%if(AgreementTypeRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Verbal</label>
						        <input type="radio" name="AgreeType" id="radio-choice-w-6b" value="2"  <%if(AgreementTypeRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Writen-Witnessed</label>
						        <input type="radio" name="AgreeType" id="radio-choice-w-6c" value="3" <%if(AgreementTypeRbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Writen-Not Witnessed</label>
						       
						       
						    </fieldset>
					    </td>
				</tr>
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Period (year)</legend>
						        <input type="radio" name="AgreePeriod" id="radio-choice-w-6a" value="1" <%if(AgreementPeriodRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1</label>
						        <input type="radio" name="AgreePeriod" id="radio-choice-w-6b" value="2"  <%if(AgreementPeriodRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">2</label>
						        <input type="radio" name="AgreePeriod" id="radio-choice-w-6c" value="3" <%if(AgreementPeriodRbtn==3){ %>checked=checked <%} %>>
						        <label for="radio-choice-w-6c">3 & Above</label>
						       
						       
						    </fieldset>
					    </td>
				</tr>
				
				<tr>
					<th style="text-align: cenetr" colspan="4"> Discount Agreemnet -PI</th>
				</tr>
				
					<tr>
								
					<td colspan="2">
						<fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
							      	  <legend>Agreement Expiry Date - PI</legend>
						</fieldset>
					</td>
				</tr>
				<tr style="font-size:13px; text-align:left;">
						<th >Month</th>
						<td  >
							 <div data-role="fieldcontain">
							  
							    <select name="MonthPI" data-mini="true" id="select-native-17">
							      <option value="0"<%if(  AgreementExpdateMonthDDLPI==0){ %> selected<%} %>>Select </option>
							      <option value="1"<%if(  AgreementExpdateMonthDDLPI==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if(  AgreementExpdateMonthDDLPI==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if(  AgreementExpdateMonthDDLPI==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if(  AgreementExpdateMonthDDLPI==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if(  AgreementExpdateMonthDDLPI==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if(  AgreementExpdateMonthDDLPI==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if(  AgreementExpdateMonthDDLPI==7){ %> selected<%} %>>7 </option>
							        <option value="8"<%if(  AgreementExpdateMonthDDLPI==8){ %> selected<%} %>>8 </option>
							        <option value="9"<%if(  AgreementExpdateMonthDDLPI==9){ %> selected<%} %>>9 </option>
							        <option value="10"<%if(  AgreementExpdateMonthDDLPI==10){ %> selected<%} %>>10 </option>
							        <option value="11"<%if(  AgreementExpdateMonthDDLPI==11){ %> selected<%} %>>11 </option>
							        <option value="12"<%if(  AgreementExpdateMonthDDLPI==12){ %> selected<%} %>>12 </option>
							    </select>
							</div>
						
						</td>
			
						<th >Year</th>
						<td >
							 <div data-role="fieldcontain">
							  
							    <select name="YearPI" data-mini="true" id="select-native-17">
							         <option value="0"<%if(  AgreementExpdateYearDDLPI==0){ %> selected<%} %>>Select </option>
							        <option value="1"<%if( AgreementExpdateYearDDLPI==1){ %> selected<%} %>>2010 </option>
							        <option value="2"<%if( AgreementExpdateYearDDLPI==2){ %> selected<%} %>>2011 </option>
							        <option value="3"<%if( AgreementExpdateYearDDLPI==3){ %> selected<%} %>>2012 </option>
							        <option value="4"<%if( AgreementExpdateYearDDLPI==4){ %> selected<%} %>>2013 </option>
							       	<option value="5"<%if( AgreementExpdateYearDDLPI==5){ %> selected<%} %>>2014 </option>
							        <option value="6"<%if( AgreementExpdateYearDDLPI==6){ %> selected<%} %>>2015 </option>
							        <option value="7"<%if( AgreementExpdateYearDDLPI==7){ %> selected<%} %>>2016 </option>
							        <option value="8"<%if( AgreementExpdateYearDDLPI==8){ %> selected<%} %>>2017 </option>
							        <option value="9"<%if( AgreementExpdateYearDDLPI==9){ %> selected<%} %>>2018 </option>
							        <option value="10"<%if(AgreementExpdateYearDDLPI==10){ %> selected<%} %>>2019 </option>
							        <option value="11"<%if( AgreementExpdateYearDDLPI==11){ %> selected<%} %>>2020 </option>
							        <option value="12"<%if( AgreementExpdateYearDDLPI==12){ %> selected<%} %>>2021</option>
							    </select>
							</div>
						
						</td>
					</tr>
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Period (year) - PI</legend>
						        <input type="radio" name="AgreePeriodPI" id="radio-choice-w-6a" value="1" <%if(AgreementPeriodPIRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1</label>
						        <input type="radio" name="AgreePeriodPI" id="radio-choice-w-6b" value="2"  <%if(AgreementPeriodPIRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">2</label>
						        <input type="radio" name="AgreePeriodPI" id="radio-choice-w-6c" value="3" <%if(AgreementPeriodPIRbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">3 & Above</label>
						       
						       
						    </fieldset>
					    </td>
				</tr>
				
				<tr style="font-size:13px; text-align:left;">	  
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Per Raw Case Discount - SSRB (PI)</legend>
						        <input type="radio" name="PerRawCaseSSRBPI" id="radio-choice-w-6a" value="1" <%if(PerRawCaseDiscSSRBPIRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1-25</label>
						        <input type="radio" name="PerRawCaseSSRBPI" id="radio-choice-w-6b" value="2"  <%if(PerRawCaseDiscSSRBPIRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">26-35</label>
						        <input type="radio" name="PerRawCaseSSRBPI" id="radio-choice-w-6c" value="3" <%if(PerRawCaseDiscSSRBPIRbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">36-45</label>
						        <input type="radio" name="PerRawCaseSSRBPI" id="radio-choice-w-6d" value="4"  <%if(PerRawCaseDiscSSRBPIRbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">46-75</label>
						     	 <input type="radio" name="PerRawCaseSSRBPI" id="radio-choice-w-6e" value="5" <%if(PerRawCaseDiscSSRBPIRbtn==5){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">76 Above</label>
						        <input type="radio" name="PerRawCaseSSRBPI" id="radio-choice-w-6f" value="6"  <%if(PerRawCaseDiscSSRBPIRbtn==6){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6f">None</label>
						       
						    </fieldset>
					    </td>
				</tr>
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
				
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Type - PI</legend>
						        <input type="radio" name="AgreeTypePI" id="radio-choice-w-6a" value="1" <%if(AgreementTypePIRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Verbal</label>
						        <input type="radio" name="AgreeTypePI" id="radio-choice-w-6b" value="2"  <%if(AgreementTypePIRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Writen-Witnessed</label>
						        <input type="radio" name="AgreeTypePI" id="radio-choice-w-6c" value="3" <%if(AgreementTypePIRbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Writen-Not Witnessed</label>
						       
						       
						    </fieldset>
					    </td>
				</tr>
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Per Raw Case Discount - PET (PI)</legend>
						        <input type="radio" name="PerRawCasePETPI" id="radio-choice-w-6a" value="1" <%if(PerRawCaseDiscPETPIRadbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1-25</label>
						        <input type="radio" name="PerRawCasePETPI" id="radio-choice-w-6b" value="2"  <%if(PerRawCaseDiscPETPIRadbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">26-35</label>
						        <input type="radio" name="PerRawCasePETPI" id="radio-choice-w-6c" value="3" <%if(PerRawCaseDiscPETPIRadbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">36-45</label>
						        <input type="radio" name="PerRawCasePETPI" id="radio-choice-w-6d" value="4" <%if(PerRawCaseDiscPETPIRadbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">46-75</label>
						     	 <input type="radio" name="PerRawCasePETPI" id="radio-choice-w-6f" value="5" <%if(PerRawCaseDiscPETPIRadbtn==5){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6f">76 Above</label>
						        <input type="radio" name="PerRawCasePETPI" id="radio-choice-w-6g" value="6"  <%if(PerRawCaseDiscPETPIRadbtn==6){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6g">None</label>
						       
						    </fieldset>
					    </td>
				</tr>
				<tr>
					<th style="text-align: cenetr" colspan="4"> Discount Agreemnet -KO</th>
				</tr>
					<td colspan="2">
							<fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
								      	  <legend>Agreement Expiry Date-KO</legend>
							</fieldset>
					</td>
					
				</tr>
					<tr style="font-size:13px; text-align:left;">
						<th  >Month</th>
						<td  >
							 <div data-role="fieldcontain">
							  
							    <select name="MonthKO" data-mini="true" id="select-native-17">
							      <option value="0"<%if(  AgreementExpdateMonthDDLKO==0){ %> selected<%} %>>Select </option>
							      <option value="1"<%if(  AgreementExpdateMonthDDLKO==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if(  AgreementExpdateMonthDDLKO==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if(  AgreementExpdateMonthDDLKO==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if(  AgreementExpdateMonthDDLKO==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if(  AgreementExpdateMonthDDLKO==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if(  AgreementExpdateMonthDDLKO==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if(  AgreementExpdateMonthDDLKO==7){ %> selected<%} %>>7 </option>
							        <option value="8"<%if(  AgreementExpdateMonthDDLKO==8){ %> selected<%} %>>8 </option>
							        <option value="9"<%if(  AgreementExpdateMonthDDLKO==9){ %> selected<%} %>>9 </option>
							        <option value="10"<%if(  AgreementExpdateMonthDDLKO==10){ %> selected<%} %>>10 </option>
							        <option value="11"<%if(  AgreementExpdateMonthDDLKO==11){ %> selected<%} %>>11 </option>
							        <option value="12"<%if(  AgreementExpdateMonthDDLKO==12){ %> selected<%} %>>12 </option>
							    </select>
							</div>
						
						</td>
					
						<th >Year</th>
						<td >
							 <div data-role="fieldcontain">
							  
							    <select name="YearKO" data-mini="true" id="select-native-17">
							        <option value="0"<%if( AgreementExpdateYearDDLKO==0){ %> selected<%} %>>Select </option>
							        <option value="1"<%if( AgreementExpdateYearDDLKO==1){ %> selected<%} %>>2010 </option>
							        <option value="2"<%if( AgreementExpdateYearDDLKO==2){ %> selected<%} %>>2011 </option>
							        <option value="3"<%if( AgreementExpdateYearDDLKO==3){ %> selected<%} %>>2012 </option>
							        <option value="4"<%if( AgreementExpdateYearDDLKO==4){ %> selected<%} %>>2013 </option>
							       	<option value="5"<%if( AgreementExpdateYearDDLKO==5){ %> selected<%} %>>2014 </option>
							        <option value="6"<%if( AgreementExpdateYearDDLKO==6){ %> selected<%} %>>2015 </option>
							        <option value="7"<%if( AgreementExpdateYearDDLKO==7){ %> selected<%} %>>2016 </option>
							        <option value="8"<%if( AgreementExpdateYearDDLKO==8){ %> selected<%} %>>2017 </option>
							        <option value="9"<%if( AgreementExpdateYearDDLKO==9){ %> selected<%} %>>2018 </option>
							        <option value="10"<%if(AgreementExpdateYearDDLKO==10){ %> selected<%} %>>2019 </option>
							        <option value="11"<%if( AgreementExpdateYearDDLKO==11){ %> selected<%} %>>2020 </option>
							        <option value="12"<%if( AgreementExpdateYearDDLKO==12){ %> selected<%} %>>2021</option>
							    </select>
							</div>
						
						</td>
					</tr>
					
					 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Type -KO</legend>
						        <input type="radio" name="AgreeTypeKO" id="radio-choice-w-6a" value="1"  <%if(AgreementTypeKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Verbal</label>
						        <input type="radio" name="AgreeTypeKO" id="radio-choice-w-6b" value="2"   <%if(AgreementTypeKORbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Writen-Witnessed</label>
						        <input type="radio" name="AgreeTypeKO" id="radio-choice-w-6c" value="3"  <%if(AgreementTypeKORbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Writen-Not Witnessed</label>
						       
						       
						    </fieldset>
					    </td>
				</tr>
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Period (year)-KO</legend>
						        <input type="radio" name="AgreePeriodKO" id="radio-choice-w-6a" value="1" <%if(AgreementPeriodKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1</label>
						        <input type="radio" name="AgreePeriodKO" id="radio-choice-w-6b" value="2"  <%if( AgreementPeriodKORbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">2</label>
						        <input type="radio" name="AgreePeriodKO" id="radio-choice-w-6c" value="3" <%if( AgreementPeriodKORbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">3 & Above</label>
						       
						       
						    </fieldset>
					    </td>
				</tr>
				
				<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Per Raw Case Discount - SSRB (KO)</legend>
						        <input type="radio" name="PerCaseSSRBKO" id="radio-choice-w-6a" value="1" <%if(perCaseSSRBKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1-25</label>
						        <input type="radio" name="PerCaseSSRBKO" id="radio-choice-w-6b" value="2"  <%if(perCaseSSRBKORbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">26-35</label>
						        <input type="radio" name="PerCaseSSRBKO" id="radio-choice-w-6c" value="3" <%if(perCaseSSRBKORbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">36-45</label>
						        <input type="radio" name="PerCaseSSRBKO" id="radio-choice-w-6d" value="4"  <%if(perCaseSSRBKORbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">46-75</label>
						     	 <input type="radio" name="PerCaseSSRBKO" id="radio-choice-w-6e" value="5" <%if(perCaseSSRBKORbtn==5){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">76 Above</label>
						        <input type="radio" name="PerCaseSSRBKO" id="radio-choice-w-6f" value="6"  <%if(perCaseSSRBKORbtn==6){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6f">None</label>
						       
						    </fieldset>
					    </td>
				</tr>
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Per Raw Case Discount - PET (KO)</legend>
						        <input type="radio" name="PerCasePETKO" id="radio-choice-w-6a" value="1" <%if(perCasePETKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">1-25</label>
						        <input type="radio" name="PerCasePETKO" id="radio-choice-w-6b" value="2"  <%if(perCasePETKORbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">26-35</label>
						        <input type="radio" name="PerCasePETKO" id="radio-choice-w-6c" value="3" <%if(perCasePETKORbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">36-45</label>
						        <input type="radio" name="PerCasePETKO" id="radio-choice-w-6d" value="4"  <%if(perCasePETKORbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">46-75</label>
						     	 <input type="radio" name="PerCasePETKO" id="radio-choice-w-6f" value="5" <%if(perCasePETKORbtn==5){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6f">76 Above</label>
						        <input type="radio" name="PerCasePETKO" id="radio-choice-w-6g" value="6"  <%if(perCasePETKORbtn==6){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6g">None</label>
						       
						    </fieldset>
					    </td>
				</tr>
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>If Partially closed Then timings</legend>
						        <input type="radio" name="PartiallycloseTime" id="radio-choice-w-6a" value="1" <%if(PartialTimingRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Morning</label>
						        <input type="radio" name="PartiallycloseTime" id="radio-choice-w-6b" value="2"  <%if(PartialTimingRbtn==2){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Noon</label>
						        <input type="radio" name="PartiallycloseTime" id="radio-choice-w-6c" value="3" <%if(PartialTimingRbtn==3){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Night</label>
						        <input type="radio" name="PartiallycloseTime" id="radio-choice-w-6d" value="4"  <%if(PartialTimingRbtn==4){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">None</label>
						     							       
						    </fieldset>
					    </td>
				</tr>
				
					
					<tr>
					
					
					<tr>
					<td colspan="2">
							<fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
								      	  <legend>Shop Timings</legend>
							</fieldset>
					</td>
					
				</tr>
					<tr style="font-size:13px; text-align:left;">
						<th >Day Off </th>
						<td  >
							 <div data-role="fieldcontain">
							
							 
							    <select name="DayOff_Shopkeeper" data-mini="true" id="select-native-17">
							        <option value="1" <%if( DayOffDDL==1){ %> selected<%} %>>Sunday</option>
							        <option value="2" <%if( DayOffDDL==2){ %> selected<%} %>>Monday</option>
							        <option value="3" <%if( DayOffDDL==3){ %> selected<%} %>>Tuesday</option>
							        <option value="4" <%if( DayOffDDL==4){ %> selected<%} %>>Wednesday</option>
							         <option value="5" <%if(DayOffDDL==5){ %> selected<%} %>>Thursday</option>
							        <option value="6" <%if( DayOffDDL==6){ %> selected<%} %>>Friday</option>
							        <option value="7" <%if( DayOffDDL==7){ %> selected<%} %>>Saturday</option>
							        <option value="8" <%if( DayOffDDL==8){ %> selected<%} %>>None</option>
							        
							        
							    </select>
							</div>
						
						</td>
				
						<th >Shop Opening time </th>
						<td  >
							 <div data-role="fieldcontain">
							  
							    <select name="ShopOpeningTime_Shopkeeper" data-mini="true" id="select-native-17">
							        <option value="1 AM"<%if( ShopOpeningTimeDDL=="1 AM"){ %> selected<%} %>>1 AM</option>
							        <option value="2 AM"<%if( ShopOpeningTimeDDL=="2 AM" ){ %> selected<%} %>>2 AM</option>
							        <option value="3 AM"<%if( ShopOpeningTimeDDL=="3 AM"){ %> selected<%} %>>3 AM</option>
							        <option value="4 AM"<%if( ShopOpeningTimeDDL=="4 AM"){ %> selected<%} %>>4 AM</option>
							       	<option value="5 AM"<%if( ShopOpeningTimeDDL=="5 AM"){ %> selected<%} %>>5 AM</option>
							        <option value="6 AM"<%if( ShopOpeningTimeDDL=="6 AM"){ %> selected<%} %>>6 AM</option>
							        <option value="7 AM"<%if( ShopOpeningTimeDDL=="7 AM"){ %> selected<%} %>>7 AM</option>
							        <option value="8 AM"<%if( ShopOpeningTimeDDL=="8 AM"){ %> selected<%} %>>8 AM</option>
							        <option value="9 AM"<%if( ShopOpeningTimeDDL=="9 AM"){ %> selected<%} %>>9 AM</option>
							        <option value="10 AM"<%if( ShopOpeningTimeDDL=="10 AM"){ %> selected<%} %>>10 AM</option>
							        <option value="11 AM"<%if( ShopOpeningTimeDDL=="11 AM"){ %> selected<%} %>>11 AM</option>
							        <option value="12 PM"<%if( ShopOpeningTimeDDL=="12 PM"){ %> selected<%} %>>12 PM</option>
							        <option value="1 PM"<%if( ShopOpeningTimeDDL=="1 PM"){ %> selected<%} %>>1 PM</option>
							        <option value="2 PM"<%if( ShopOpeningTimeDDL=="2 PM"){ %> selected<%} %>>2 PM</option>
							        <option value="3 PM"<%if( ShopOpeningTimeDDL=="3 PM"){ %> selected<%} %>>3 PM</option>
							        <option value="4 PM"<%if( ShopOpeningTimeDDL=="4 PM"){ %> selected<%} %>>4 PM</option>
							        <option value="5 PM"<%if( ShopOpeningTimeDDL=="5 PM"){ %> selected<%} %>>5 PM</option>
							        <option value="6 PM"<%if( ShopOpeningTimeDDL=="6 PM"){ %> selected<%} %>>6 PM</option>
							        <option value="7 PM"<%if( ShopOpeningTimeDDL=="7 PM"){ %> selected<%} %>>7 PM</option>
							        <option value="8 PM"<%if( ShopOpeningTimeDDL=="8 PM"){ %> selected<%} %>>8 PM</option>
							        <option value="9 PM"<%if( ShopOpeningTimeDDL=="9 PM"){ %> selected<%} %>>9 PM</option>
							        <option value="10 PM"<%if( ShopOpeningTimeDDL=="10 PM"){ %> selected<%} %>>10 PM</option>
							        <option value="11 PM"<%if( ShopOpeningTimeDDL=="11 PM"){ %> selected<%} %>>11 PM</option>
							    </select>
							</div>
						
						</td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						<th  >Shop Closing time</th>
						<td  >
							 <div data-role="fieldcontain">
							  
							    <select name="ShopClosingTime_Shopkeeper" data-mini="true" id="select-native-17">
							     <option value="1 AM"<%if( ShopCloseTimeDDL=="1 AM"){ %> selected<%} %>>1 AM</option>
							        <option value="2 AM"<%if( ShopCloseTimeDDL=="2 AM" ){ %> selected<%} %>>2 AM</option>
							        <option value="3 AM"<%if( ShopCloseTimeDDL=="3 AM"){ %> selected<%} %>>3 AM</option>
							        <option value="4 AM"<%if( ShopCloseTimeDDL=="4 AM"){ %> selected<%} %>>4 AM</option>
							       	<option value="5 AM"<%if( ShopCloseTimeDDL=="5 AM"){ %> selected<%} %>>5 AM</option>
							        <option value="6 AM"<%if( ShopCloseTimeDDL=="6 AM"){ %> selected<%} %>>6 AM</option>
							        <option value="7 AM"<%if( ShopCloseTimeDDL=="7 AM"){ %> selected<%} %>>7 AM</option>
							        <option value="8 AM"<%if( ShopCloseTimeDDL=="8 AM"){ %> selected<%} %>>8 AM</option>
							        <option value="9 AM"<%if( ShopCloseTimeDDL=="9 AM"){ %> selected<%} %>>9 AM</option>
							        <option value="10 AM"<%if( ShopCloseTimeDDL=="10 AM"){ %> selected<%} %>>10 AM</option>
							        <option value="11 AM"<%if( ShopCloseTimeDDL=="11 AM"){ %> selected<%} %>>11 AM</option>
							        <option value="12 PM"<%if( ShopCloseTimeDDL=="12 PM"){ %> selected<%} %>>12 PM</option>
							        <option value="1 PM"<%if( ShopCloseTimeDDL=="1 PM"){ %> selected<%} %>>1 PM</option>
							        <option value="2 PM"<%if( ShopCloseTimeDDL=="2 PM"){ %> selected<%} %>>2 PM</option>
							        <option value="3 PM"<%if( ShopCloseTimeDDL=="3 PM"){ %> selected<%} %>>3 PM</option>
							        <option value="4 PM"<%if( ShopCloseTimeDDL=="4 PM"){ %> selected<%} %>>4 PM</option>
							        <option value="5 PM"<%if( ShopCloseTimeDDL=="5 PM"){ %> selected<%} %>>5 PM</option>
							        <option value="6 PM"<%if( ShopCloseTimeDDL=="6 PM"){ %> selected<%} %>>6 PM</option>
							        <option value="7 PM"<%if( ShopCloseTimeDDL=="7 PM"){ %> selected<%} %>>7 PM</option>
							        <option value="8 PM"<%if( ShopCloseTimeDDL=="8 PM"){ %> selected<%} %>>8 PM</option>
							        <option value="9 PM"<%if( ShopCloseTimeDDL=="9 PM"){ %> selected<%} %>>9 PM</option>
							        <option value="10 PM"<%if( ShopCloseTimeDDL=="10 PM"){ %> selected<%} %>>10 PM</option>
							        <option value="11 PM"<%if( ShopCloseTimeDDL=="11 PM"){ %> selected<%} %>>11 PM</option>
							    </select>
							</div>
						
						</td>
					
						
						<th >Break-Time</th>
						<td ><input  type="text" name="BreakTime_Shopkeeper" data-mini="true"  id="Distributor_general" value="<%=BreakTime %>" ></td>
						
						
					</tr>
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Financial Service (If Available)</legend>
						        <input type="Checkbox" name="FinancialServicebtn1" id="radio-choice-w-6a" value="1" <%if( FinancialServiceChbtn1==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Easy Paisa</label>
						        <input type="checkbox" name="FinancialServicebtn2" id="radio-choice-w-6b" value="1"  <%if( FinancialServiceChbtn2==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">U Paisa</label>
						        <input type="Checkbox" name="FinancialServicebtn3" id="radio-choice-w-6c" value="1" <%if( FinancialServiceChbtn3==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Mobi Cash</label>
						        <input type="checkbox" name="FinancialServicebtn4" id="radio-choice-w-6d" value="1"  <%if( FinancialServiceChbtn4==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">Warid Mobile Paisa</label>
						     	 <input type="Checkbox" name="FinancialServicebtn5" id="radio-choice-w-6e" value="1" <%if( FinancialServiceChbtn5==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">Time pay</label>
						        <input type="checkbox" name="FinancialServicebtn6" id="radio-choice-w-6f" value="1"  <%if( FinancialServiceChbtn6==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6f">Other</label>
						       
						    </fieldset>
					    </td>
				</tr>
				
				 <tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Type </legend>
						        <input type="Checkbox" name="AgreeType1a" id="radio-choice-w-6a" value="1" <%if(AgreementType2Rbtn1==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Discount</label>
						        <input type="checkbox" name="AgreeType1b" id="radio-choice-w-6b" value="1"  <%if(AgreementType2Rbtn2==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Signage</label>
						        <input type="Checkbox" name="AgreeType1c" id="radio-choice-w-6c" value="1" <%if(AgreementType2Rbtn3==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Annual Support</label>
						        <input type="checkbox" name="AgreeType1d" id="radio-choice-w-6d" value="1"  <%if(AgreementType2Rbtn4==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">Exclusivity Fee</label>
						     	 <input type="Checkbox" name="AgreeType1e" id="radio-choice-w-6e" value="1" <%if(AgreementType2Rbtn5==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">Vol Incentive</label>
						        				       
						    </fieldset>
					    </td>
				</tr>
				
				<tr style="font-size:13px; text-align:left;">
						
						<th colspan="2" >Any Other</th>
						<td colspan="2" ><input type="text" name="AgreeType1f"   data-mini="true"  id="AgreeType1f" value="<%=AgreementType2Rbtn6 %>"></td>
						
				</tr>
					
				<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Type - PI</legend>
						        <input type="Checkbox" name="AgreeType2PIa" id="radio-choice-w-6a" value="1" <%if(AgreementType2DiscPIRbtn==1){ %>checked=checked<%} %> >
						        <label for="radio-choice-w-6a">Discount</label>
						        <input type="checkbox" name="AgreeType2PIb" id="radio-choice-w-6b" value="1" <%if(AgreementType2SigPIRbtn==1){ %>checked=checked<%} %> >
						     	<label for="radio-choice-w-6b">Signage</label>
						        <input type="Checkbox" name="AgreeType2PIc" id="radio-choice-w-6c" value="1" <%if(AgreementType2AnnualPIRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Annual Support</label>
						        <input type="checkbox" name="AgreeType2PId" id="radio-choice-w-6d" value="1" <%if(AgreementType2ExculPIRbtn==1){ %>checked=checked<%} %> >
						     	<label for="radio-choice-w-6d">Exclusivity Fee</label>
						     	<input type="Checkbox" name="AgreeType2PIe" id="radio-choice-w-6e" value="1" <%if(AgreementType2IncPIRbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">Vol Incentive</label>
						       
						     							       
						    </fieldset>
					    </td>
				</tr>
				<tr style="font-size:13px; text-align:left;">
						
						<th colspan="2" >Any Other</th>
						<td colspan="2" ><input type="text" name="AgreeType2PIf"   data-mini="true"  id="AgreeType2PIf" value="<%=AgreementType2OtherPIRbtn %>"></td>
						
				</tr>
				
				 <tr style="font-size:13px; text-align:right;">	
						<td colspan="4">
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Agreement Type-KO</legend>
						        <input type="Checkbox" name="AgreeTypeKOa" id="radio-choice-w-6a" value="1" <%if(AgreementType2DKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Discount</label>
						        <input type="checkbox" name="AgreeTypeKOb" id="radio-choice-w-6b" value="1"  <%if(AgreementType2SKORbtn==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Signage</label>
						        <input type="Checkbox" name="AgreeTypeKOc" id="radio-choice-w-6c" value="1" <%if(AgreementType2AKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Annual Support</label>
						        <input type="checkbox" name="AgreeTypeKOd" id="radio-choice-w-6d" value="1"  <%if(AgreementType2EXKORbtn==1){ %>checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">Exclusivity Fee</label>
						     	 <input type="Checkbox" name="AgreeTypeKOe" id="radio-choice-w-6e" value="1" <%if(AgreementType2VolKORbtn==1){ %>checked=checked<%} %>>
						        <label for="radio-choice-w-6e">Vol Incentive</label>
						        
						     							       
						    </fieldset>
					    </td>
				</tr>
				<tr style="font-size:13px; text-align:left;">
						
						<th colspan="2" >Any Other</th>
						<td colspan="2" ><input type="text" name="AgreeTypeKOf"   data-mini="true"  id="AgreeTypeKOf" value="<%=AgreementTypeOKORbtn %>"></td>
						
				</tr>
				
				
					
			</table>
		
		
		
		
			
				</li>	
				
				<li data-role="list-divider" data-theme="b">Status</li>
			<li>		
				
			
				<table style="width:100%;">
					<tr style="font-size:13px; text-align:left;">
						<th colspan="2"  colspan="2">Supply Frequency PI</th>
						<th colspan="2"  colspan="2">Supply Frequency KO</th>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<td colspan="2">
                            <div  data-role="fieldcontain">
							  
							    <select name="SupplyFrequencyPI" data-mini="true" id="select-native-17">
							          <option value="0"<%if( SupplyFrequencyDDL==0){ %> selected<%} %>>Select </option>
							         <option value="1"<%if( SupplyFrequencyDDL==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if( SupplyFrequencyDDL==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if( SupplyFrequencyDDL==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if( SupplyFrequencyDDL==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if( SupplyFrequencyDDL==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if( SupplyFrequencyDDL==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if( SupplyFrequencyDDL==7){ %> selected<%} %>>7 </option>
							    </select>
							</div>
						</td>
						<td colspan="2">
							<div data-role="fieldcontain">
						 
						    <select name="SupplyFrequencyKO" data-mini="true" id="select-native-17">
						       		  <option value="0"<%if( SupplyFrequencyDDLKO==0){ %> selected<%} %>>Select </option>
						      		<option value="1"<%if( SupplyFrequencyDDLKO==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if( SupplyFrequencyDDLKO==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if( SupplyFrequencyDDLKO==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if( SupplyFrequencyDDLKO==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if( SupplyFrequencyDDLKO==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if( SupplyFrequencyDDLKO==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if( SupplyFrequencyDDLKO==7){ %> selected<%} %>>7 </option>
						    </select>
							</div>
						</td>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th  colspan="2">Supply Frequency Gourment</th>
						<th  colspan="2">Supply Frequency Cola Next</th>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<td colspan="2">
                            <div data-role="fieldcontain">
							  
							    <select name="SupplyFrequencyGourment" data-mini="true" id="select-native-17">
							      	 <option value="0"<%if( SupplyFrequencyDDLGO==0){ %> selected<%} %>>Select </option>
							      	<option value="1"<%if( SupplyFrequencyDDLGO==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if( SupplyFrequencyDDLGO==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if( SupplyFrequencyDDLGO==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if( SupplyFrequencyDDLGO==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if( SupplyFrequencyDDLGO==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if( SupplyFrequencyDDLGO==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if( SupplyFrequencyDDLGO==7){ %> selected<%} %>>7 </option>
							    </select>
							</div>
						</td>
						<td colspan="2">
							<div data-role="fieldcontain">
						 
						    <select name="SupplyFrequencyColaNext" data-mini="true" id="select-native-17">
						       	<option value="0"<%if( SupplyFrequencyDDLCN==0){ %> selected<%} %>>Select </option>
						      		<option value="1"<%if( SupplyFrequencyDDLCN==1){ %> selected<%} %>>1 </option>
							        <option value="2"<%if( SupplyFrequencyDDLCN==2){ %> selected<%} %>>2 </option>
							        <option value="3"<%if( SupplyFrequencyDDLCN==3){ %> selected<%} %>>3 </option>
							        <option value="4"<%if( SupplyFrequencyDDLCN==4){ %> selected<%} %>>4 </option>
							       	<option value="5"<%if( SupplyFrequencyDDLCN==5){ %> selected<%} %>>5 </option>
							        <option value="6"<%if( SupplyFrequencyDDLCN==6){ %> selected<%} %>>6 </option>
							        <option value="7"<%if( SupplyFrequencyDDLCN==7){ %> selected<%} %>>7 </option>
						    </select>
							</div>
						</td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Covered by researcher Company?</legend>
						        
						        <input type="radio" name="CoveredbyresearcherCompany" id="radio-choice-w-6b" value="1" <%if(IsCoveredByRCompnayRbtn==1){ %> checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Monthly</label>
						        <input type="radio" name="CoveredbyresearcherCompany" id="radio-choice-w-6c" value="2"  <%if(IsCoveredByRCompnayRbtn==2){ %> checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Quartely</label>
						        <input type="radio" name="CoveredbyresearcherCompany" id="radio-choice-w-6d" value="3"   <%if(IsCoveredByRCompnayRbtn==3){ %> checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">Annually</label>
						     	
						    </fieldset>
					    </td>
				</tr>
				
				<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Stock Storage Location</legend>
						        <input type="Checkbox" name="StockStoragelocation1" id="radio-choice-w-6a" value="1" <%if(StockStorageLocationChbtn1==1){ %> checked=checked<%} %>>
						        <label for="radio-choice-w-6a">Within Outlet</label>
						        <input type="checkbox" name="StockStoragelocation2" id="radio-choice-w-6b" value="1"  <%if(StockStorageLocationChbtn2==1){ %> checked=checked<%} %>>
						     	<label for="radio-choice-w-6b">Attached with Shop</label>
						        <input type="checkbox" name="StockStoragelocation3" id="radio-choice-w-6c" value="1" <%if(StockStorageLocationChbtn3==1){ %> checked=checked<%} %>>
						        <label for="radio-choice-w-6c">Elsewhere</label>
						        <input type="checkbox" name="StockStoragelocation4" id="radio-choice-w-6d" value="1"  <%if(StockStorageLocationChbtn4==1){ %> checked=checked<%} %>>
						     	<label for="radio-choice-w-6d">No storage</label>
						     	
						    </fieldset>
					    </td>
				</tr>
				<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						
						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Cash Machine</legend>
						     
						     </fieldset>
					    </td>
				</tr>
				<tr>
				 	 <th>Quantity</th>
					<td><input type="text"  name="Quantity_Status" data-mini="true" id="outlet_general" value=<%=CashMachineQuantity%>></td>
				
					<th>Shop Established history</th>				
					<td>
                            <div data-role="fieldcontain">
							  
							    <select name="ShopEstablishedHistory_status" data-mini="true" id="select-native-17">
							       <option value=""<%if( ShopHistory.equals("")){ %> selected<%} %>>Select </option>
							        <option value="Before 2005"<%if( ShopHistory.equals("Before 2005")){ %> selected<%} %>>before 2005 </option>
							        <option value="2005"<%if( ShopHistory.equals("2005")){ %> selected<%} %>>2005 </option>
							        <option value="2006"<%if( ShopHistory.equals("2006")){ %> selected<%} %>>2006 </option>
							        <option value="2007"<%if( ShopHistory.equals("2007")){ %> selected<%} %>>2007 </option>
							       	<option value="2008"<%if( ShopHistory.equals("2008")){ %> selected<%} %>>2008 </option>
							        <option value="2009"<%if( ShopHistory.equals("2009")){ %> selected<%} %>>2009 </option>
							        <option value="2010"<%if( ShopHistory.equals("2010")){ %> selected<%} %>>2010 </option>
							        <option value="2011"<%if( ShopHistory.equals("2011")){ %> selected<%} %>>2011 </option>
							        <option value="2012"<%if( ShopHistory.equals("2012")){ %> selected<%} %>>2012 </option>
							        <option value="2013"<%if(ShopHistory.equals("2013")){ %> selected<%} %>>2013 </option>
							        <option value="2014"<%if( ShopHistory.equals("2014")){ %> selected<%} %>>2014 </option>
							        <option value="2015"<%if( ShopHistory.equals("2015")){ %> selected<%} %>>2015</option>
							        <option value="2016"<%if( ShopHistory.equals("2016")){ %> selected<%} %>>2016 </option>
							        <option value="2017"<%if( ShopHistory.equals("2017")){ %> selected<%} %>>2017</option>
							    </select>
							</div>
					</td>
				</tr>
					<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						 <% 
					  if(BusinessStructureVal==1){
						  BusinessStructureRbtn=1;
					   }else if(BusinessStructureVal==2){
						   BusinessStructureRbtn=2;
					   }else if(BusinessStructureVal==3){
						   BusinessStructureRbtn=3;
					   }
						 %>
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Business Structure</legend>
						        <input type="radio" name="BusinessStrutcure" id="radio-choice-w-6a" value="1" <%if( BusinessStructureRbtn==1){%>checked=checked<%}%>>
						        <label for="radio-choice-w-6a">Self</label>
						        <input type="radio" name="BusinessStrutcure" id="radio-choice-w-6b" value="2"  <%if( BusinessStructureRbtn==2){%>checked=checked<%}%>>
						     	<label for="radio-choice-w-6b">Chain</label>
						     	<input type="radio" name="BusinessStrutcure" id="radio-choice-w-6c" value="3"  <%if( BusinessStructureRbtn==3){%>checked=checked<%}%>>
						     	<label for="radio-choice-w-6c">Patnership</label>
						       
						     	
						    </fieldset>
					    </td>
				</tr>
				
				<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">
						<%  
						if(SShopTypeVal==1){
						  SShopTypeRbtn=1;
					   }else if(SShopTypeVal==2){
						   SShopTypeRbtn=2;
					   }else if(SShopTypeVal==3){
						   SShopTypeRbtn=3;
					   }
						%>
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right" >
						        <legend>Shop Type</legend>
						        <input type="radio" name="ShopType_status" id="radio-choice-w-6a" value="1" <%if(SShopTypeRbtn==1){%>checked=checked<%}%>>
						        <label for="radio-choice-w-6a">Wholesale</label>
						        <input type="radio" name="ShopType_status"" id="radio-choice-w-6b" value="2"  <%if(SShopTypeRbtn==2){%>checked=checked<%}%>>
						     	<label for="radio-choice-w-6b">Retailer</label>
						     	<input type="radio" name="ShopType_status"" id="radio-choice-w-6c" value="3"  <%if(SShopTypeRbtn==3){%>checked=checked<%}%>>
						     	<label for="radio-choice-w-6c">Both</label>
						       
						     	
						    </fieldset>
					    </td>
				</tr>
				<tr style="font-size:13px;">	
						<td colspan="4">						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>If Both</legend>
						     
						     </fieldset>
					    </td>
				</tr>
				
				<tr style="font-size:13px;">
						<th style=" text-align:left;">Wholesale %</th>
						<td><input type="text" name="wholeSalePer_status" data-mini="true" id="outlet_general" value=<%=Wholesale %>></td>
						<th >Retailer %</th>
						<td><input type="text" name="RetailerPer_status" data-mini="true"  id="System_Outlet_Name_general" value=<%=Retailer %>></td>
					</tr>
					
				<tr style="font-size:13px; text-align:left;">	
						<td colspan="4">						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>Empty SSRB</legend>
						     
						     </fieldset>
					    </td>
				</tr>	
				
				<tr style="font-size:13px; ">
						<th  style=" text-align:left;">PI CSD</th>
						<td><input type="text" name="PICSD_status" data-mini="true" id="outlet_general" value=<%=PICSD %>></td>
						<th  >PI Sting</th>
						<td><input type="text" name="PISting_status" data-mini="true"  id="System_Outlet_Name_general" value=<%=PISting %>></td>
						
					</tr>
				<tr>
				<th  style=" text-align:left;">KO</th>
						<td ><input type="text" name="KO_status" data-mini="true" id="outlet_general" value=<%=KO %>></td>
				</tr>
				
		</table>
				
				
			</li>
				
				
				
				<li data-role="list-divider" data-theme="b">Cooler Type</li>
			<li>		
				
				
			<%int CoolerTypeRowCounter=0; %>
				
				<table id="CoolerType_table" style="width:100%;">
				<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th>Quantity</th>
					</tr>
					<tr>
						<td>
							<select name="CoolerTypeCompany" data-mini="true" id="CoolerTypeCompanyDDL">
							        <option value="-1">Select Company </option>
							        <option value="1">PI </option>
							        <option value="2">KO </option>
							        <option value="3">NESTLE </option>
							        <option value="4">REDBULL </option>
							        <option value="5">ICE CREAM</option>
							         <option value="6">PERSONAL </option>
							        <option value="7">OTHERS</option>
							        
							 </select>
						 </td>
						<td>
							<select name="CoolerTypePack" data-mini="true" id="CoolerTypePackDDL">
							        <option value="-1">Select Package </option>
							        <option value="1">V/C 250SAX </option>
							        <option value="2">V/C 400SAX </option>
							        <option value="3">V/C 550SAX</option>
							        <option value="4">V/C 1000SAX</option>
							        <option value="5">C/C 8CFT</option>
							        <option value="6">C/C 10CFT </option>
							        <option value="7">C/C 12CFT</option>
							        <option value="8">C/C 15CFT</option>
							        <option value="9">Other V/C </option>
							        <option value="10">Other C/C </option>
							        <option value="11">V/C 260SAX</option>
							         <option value="11">V/C 1000SAX DD</option>
							</select>
						</td>
						<td>
							<select name="CoolerTypeBrand" data-mini="true" id="CoolerTypeBrandDDL">
							         <option value="-1">Select Quantity </option>
							        <option value="1">1</option>
							        <option value="2">2 </option>
							        <option value="3">3 </option>
							        <option value="4">4 </option>
							        <option value="5">5 </option>
							        <option value="6">6 </option>
							        <option value="7">7</option>
							        <option value="8">8 </option>
							        <option value="9">9 </option>
							        <option value="10">10</option>
							        <option value="11">11</option>
							        <option value="12">12</option>
							        <option value="13">13 </option>
							        <option value="14">14</option>
							        <option value="15">15 </option>
							        <option value="16">16 </option>
							        <option value="17">17 </option>
							        <option value="18">18 </option>
							        <option value="19">19 </option>
							        <option value="20">20</option>
							        <option value="21">21 </option>
							        <option value="22">22 </option>
							        <option value="23">23 </option>
							        <option value="24">24 </option>
							        <option value="25">25 </option>
							        <option value="26">26 </option>
							        <option value="27">27 </option>
							        <option value="28">28 </option>
							        <option value="29">29 </option>
							</select>
						
						</td>
						<td>
						<a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewCoolerType("<%=CoolerTypeRowCounter%>")'>Plus</a>
						</td>
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
					<%
					int CoolType_company_id=0;
					int CoolType_package_id=0;
					int CoolType_brand_id=0;
					String CoolerTypeCompnayName="";
					String CoolerTypeTypeName="";
					
					ResultSet rs2 = s.executeQuery("SELECT * FROM mrd_census_cooler_types where id="+TableMainID);
					while(rs2.next()){
					
						CoolType_company_id=rs2.getInt("cooler_type_company_id");
						CoolType_package_id=rs2.getInt("cooler_type_type_id");
						
						
						if(rs2.getInt("cooler_type_company_id")==1){
							CoolerTypeCompnayName="PI";
						}else if(rs2.getInt("cooler_type_company_id")==2){
							CoolerTypeCompnayName="KO";
						}else if(rs2.getInt("cooler_type_company_id")==3){
							CoolerTypeCompnayName="NESTLE";
						}else if(rs2.getInt("cooler_type_company_id")==4){
							CoolerTypeCompnayName="REDBULL";
						}else if(rs2.getInt("cooler_type_company_id")==5){
							CoolerTypeCompnayName="ICE CREAM";
						}else if(rs2.getInt("cooler_type_company_id")==6){
							CoolerTypeCompnayName="PERSONAL";
						}else if(rs2.getInt("cooler_type_company_id")==7){
							CoolerTypeCompnayName="OTHERS";
						}
						
						
						if(rs2.getInt("cooler_type_type_id")==1){
							CoolerTypeTypeName="V/C 250SAX";
						}else if(rs2.getInt("cooler_type_type_id")==2){
							CoolerTypeTypeName="V/C 400SAX";
						}else if(rs2.getInt("cooler_type_type_id")==3){
							CoolerTypeTypeName="V/C 550SAX";
						}else if(rs2.getInt("cooler_type_type_id")==4){
							CoolerTypeTypeName="V/C 1000SAX";
						}else if(rs2.getInt("cooler_type_type_id")==5){
							CoolerTypeTypeName="C/C 8CFT";
						}else if(rs2.getInt("cooler_type_type_id")==6){
							CoolerTypeTypeName="C/C 10CFT";
						}else if(rs2.getInt("cooler_type_type_id")==7){
							CoolerTypeTypeName="C/C 12CFT";
						}else if(rs2.getInt("cooler_type_type_id")==8){
							CoolerTypeTypeName="C/C 15CFT";
						}else if(rs2.getInt("cooler_type_type_id")==9){
							CoolerTypeTypeName="Other V/C";
						}else if(rs2.getInt("cooler_type_type_id")==10){
							CoolerTypeTypeName="Other C/C";
						}else if(rs2.getInt("cooler_type_type_id")==11){
							CoolerTypeTypeName="V/C 260SAX";
						}else if(rs2.getInt("cooler_type_type_id")==12){
							CoolerTypeTypeName="V/C 1000SAX DD";
						}
						
						
					%>
						
					
				
						
						
					<tr id="coolType_<%=CoolerTypeRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:33%;"><%=CoolerTypeCompnayName %><input type="hidden" name="cooltype_co" id="co<%=CoolerTypeRowCounter%>"  value=<%=CoolType_company_id%>> </td>						
						<td style="width:33%;"><%=CoolerTypeTypeName %><input type="hidden" name="cooltype_pack" id="pac<%=CoolerTypeRowCounter%>"  value=<%=CoolType_package_id%>></td>						
						<td style="width:33%;"><%=rs2.getInt("quantity") %><input type="hidden"  name="cooltype_brand" id="brand<%=CoolerTypeRowCounter%>"  value=<%=rs2.getInt("quantity")%>></td>
						<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemoveCoolerType("coolType_<%=CoolerTypeRowCounter%>")'>Delete</a></td>
					
				
						
						
					</tr>
					
					<%
					CoolerTypeRowCounter++;
					}
					%>
					
					
					<input type="hidden" id="CoolerTypeRowCounterHidden" value="<%=CoolerTypeRowCounter%>">
				</table>
			
				
				
			
				</li>
				
				
				
				<li data-role="list-divider" data-theme="b">Cooler Codes</li>
			<li>		
				
				
				<table style="width:100%;">
					
					
					<tr style="font-size:13px; text-align:left;">
					
						
						<th colspan="2">Visible Coolers:</th>
						<td colspan="2"><input type="text" name="VisibleCooler"    data-mini="true"  id="VisibleCooler" value=<%=VisibleColors %>></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">	
						<th colspan="2">Accessible Coolers:</th>
						<td colspan="2"><input type="text" name="AccessibleCooler"  data-mini="true"  id="AccessibleCooler" value=<%=AccessibleColors %>></td>
					
						
										
						
					</tr>
					<tr style="font-size:13px;">	
						<td colspan="4">						
							 <fieldset data-role="controlgroup"  data-mini="true"  data-iconpos="right">
						        <legend>SN Codes:</legend>
						     
						     </fieldset>
					    </td>
				</tr>
					<%
					String CoolerSerialNo="";
					int Valuecounter=0;
					
					ResultSet rs2c = s.executeQuery("SELECT * FROM mrd_census_cooler_code where id="+TableMainID);
					while(rs2c.next()){
						CoolerSerialNo =rs2c.getString("tot_code");
						
					%>
					
					
					
					
					<tr style="font-size:13px; text-align:left;">		
						<th colspan="2" style="background-color:#f4f3f3">Codes:</th>
						<td colspan="2"><input type="text" name="SnCooler"    data-mini="true"  id="serial" value=<%=CoolerSerialNo %>></td>				
						
						
					</tr>
					
					
					<%
					Valuecounter++;
					}
					
					int inputboxvalue=15-Valuecounter;
					for(int i=0;i<inputboxvalue;i++){
					%>
					<tr style="font-size:13px; text-align:left">		
						<th colspan="2" style="background-color:#f4f3f3">Codes:</th>
						<td colspan="2"><input type="text" name="SnCooler"    data-mini="true"  id="serial" ></td>				
						
						
					</tr>
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>
					
				
				<li data-role="list-divider" data-theme="b">Publicity Type</li>
			<li>		
				
				
				<%
				
				 int PublicityRowCounter=0;
			
				%>
				<table id="PUBLICITY_table" style="width:100%;">
						<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th>Quantity</th>
					</tr>
					<tr>
						<td>
							<select name="PublicityCompany" data-mini="true" id="PublicityCompanyDDL">
							        <option value="-1">Select Comapny </option>
							        <option value="1">PI </option>
							        <option value="2">KO </option>
							        <option value="3">NESTLE </option>
							        <option value="4">OTHERS </option>
							        <option value="5">TELECOM </option>
							         <option value="6">ICE CREAM </option>
							       
							        
							 </select>
						 </td>
						<td>
							<select name="PublicityPackage" data-mini="true" id="PublicityPackageDDL">
							       <option value="-1">Select Package </option>
							        <option value="1">FLAG </option>
							        <option value="2">RACK </option>
							        <option value="3">CANOPY</option>
							        <option value="4">SINAGE F/L</option>
							        <option value="5">SIGNAGE B/L</option>
							        <option value="6">SIGNAGE TIN</option>
							        <option value="7">COUNTER</option>
							        <option value="8">PAINT</option>
							        <option value="9">POSTER</option>
							        <option value="10">Gondola </option>
							        <option value="11">Cabin</option>
							        
							</select>
						</td>
						<td>
							<select name="PublicityBrand" data-mini="true" id="PublicityBrandDDL">
							       <option value="-1">Select Quantity</option>
							        <option value="1">1</option>
							        <option value="2">2 </option>
							        <option value="3">3 </option>
							        <option value="4">4 </option>
							        <option value="5">5 </option>
							        <option value="6">6 </option>
							        <option value="7">7</option>
							        <option value="8">8 </option>
							        <option value="9">9 </option>
							        <option value="10">10</option>
							        <option value="11">11</option>
							        <option value="12">12</option>
							        <option value="13">13 </option>
							        <option value="14">14</option>
							        <option value="15">15 </option>
							        <option value="16">16 </option>
							        <option value="17">17 </option>
							        <option value="18">18 </option>
							        <option value="19">19 </option>
							        <option value="20">20</option>
							        <option value="21">21 </option>
							        <option value="22">22 </option>
							        <option value="23">23 </option>
							        <option value="24">24 </option>
							        <option value="25">25 </option>
							        <option value="26">26 </option>
							        <option value="27">27 </option>
							        <option value="28">28 </option>
							        <option value="29">29 </option>
							</select>
						
						</td>
						<td>
							
						 	<a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewPUB("<%=PublicityRowCounter%>")'>Plus</a> 
						</td>
					</tr>
					
					<%
					
					String PublicityTypeCompnayName="";
					String PublicityTypeTypeName="";
					int pub_type_company_id=0;
					int pub_type_type_id=0;
					ResultSet rs21 = s.executeQuery("SELECT * FROM mrd_census_publicity_types where id="+TableMainID);
					while(rs21.next()){
					pub_type_company_id=rs21.getInt("publicity_type_company_id");
					pub_type_type_id=rs21.getInt("publicity_type_type_id");
					
						if(rs21.getInt("publicity_type_company_id")==1){
							PublicityTypeCompnayName="PI";
						}else if(rs21.getInt("publicity_type_company_id")==2){
							PublicityTypeCompnayName="KO";
						}else if(rs21.getInt("publicity_type_company_id")==3){
							PublicityTypeCompnayName="NESTLE";
						}else if(rs21.getInt("publicity_type_company_id")==4){
							PublicityTypeCompnayName="OTHERS";
						}else if(rs21.getInt("publicity_type_company_id")==5){
							PublicityTypeCompnayName="TELECOM";
						}else if(rs21.getInt("publicity_type_company_id")==6){
							PublicityTypeCompnayName="ICECREAM";
						}
						
						
						
						if(rs21.getInt("publicity_type_type_id")==1){
							PublicityTypeTypeName="FLAG";
						}else if(rs21.getInt("publicity_type_type_id")==2){
							PublicityTypeTypeName="RACK";
						}else if(rs21.getInt("publicity_type_type_id")==3){
							PublicityTypeTypeName="CANOPY";
						}else if(rs21.getInt("publicity_type_type_id")==4){
							PublicityTypeTypeName="SIGNAGE F/L";
						}else if(rs21.getInt("publicity_type_type_id")==5){
							PublicityTypeTypeName="SIGNAGE B/L";
						}else if(rs21.getInt("publicity_type_type_id")==6){
							PublicityTypeTypeName="SIGNAGE TIN";
						}else if(rs21.getInt("publicity_type_type_id")==7){
							PublicityTypeTypeName="COUNTER";
						}else if(rs21.getInt("publicity_type_type_id")==8){
							PublicityTypeTypeName="PAINT";
						}else if(rs21.getInt("publicity_type_type_id")==9){
							PublicityTypeTypeName="POSTER";
						}else if(rs21.getInt("publicity_type_type_id")==10){
							PublicityTypeTypeName="Gondola";
						}else if(rs21.getInt("publicity_type_type_id")==11){
							PublicityTypeTypeName="Cabin";
						}
						
						
						
					%>
					<tr id="pub_<%=PublicityRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:33%;"><%=PublicityTypeCompnayName %><input type="hidden" name="pub_co" id="pubco<%=PublicityRowCounter%>"  value=<%=pub_type_company_id%>> </td>						
						<td style="width:33%;"><%=PublicityTypeTypeName %><input type="hidden" name="pub_pack" id="pubpac<%=PublicityRowCounter%>"  value=<%=pub_type_type_id%>></td>						
						<td style="width:33%;"><%=rs21.getInt("quantity") %><input type="hidden"  name="pub_brand" id="pubbrand<%=PublicityRowCounter%>"  value=<%=rs21.getInt("quantity")%>></td>
						<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemovePub("pub_<%=PublicityRowCounter%>")'>Delete</a></td>
					
				
					</tr>
					
				
					
					<%
					PublicityRowCounter++;
					}
					%>
					
					
					
				<input type="hidden" id="PublicityRowCounterHidden" value="<%=PublicityRowCounter%>">
				
				</table>
		
				</li>	
				
				<li data-role="list-divider" data-theme="b">CSD</li>
			<li>		
				
				
				<table id="CSD_table" style="width:100%;">

					<%int CSDRowCounter=0;%>					
					<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th>Brand Name</th>
					</tr>
					<tr>
						<td>
							<select name="company" data-mini="true" id="companyddl">
							      <option value="-1">Select Company </option>
							        <option value="1">Pepsi </option>
							        <option value="2">Coke </option>
							        <option value="3">Cola Next </option>
							        <option value="4">Gourmet </option>
							        <option value="5">None </option>
							 </select>
						 </td>
						<td>
							<select name="package" data-mini="true" id="packddl">
							        <option value="-1">Select Package </option>
							        <option value="1">CAN-NORMAL </option>
							        <option value="2">CAN-SLIM </option>
							        <option value="3">250ML SSRB</option>
							        <option value="4">250 NRB</option>
							        <option value="5">345ML PET </option>
							        <option value="6">500ML PET </option>
							        <option value="7">1000ML PET</option>
							        <option value="8">1500-1750ML PET</option>
							        <option value="9">2000ML PET </option>
							        <option value="10">2250ML PET </option>
							        <option value="11">2500ML PET</option>
							</select>
						</td>
						<td>
							<select name="Brand" data-mini="true" id="brandddl">
							        
							</select>
						
						</td>
						<td>
							<a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNew("<%=CSDRowCounter%>")'>Plus</a>
						</td>
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
					
					<%
					
					String CSDCompanyName="";
					String CSDPackageName="";
					String CSDBrandName="";
					
					int csd_company_id=0;
					int csd_package_id=0;
					int csd_brand_id=0;
					
					ResultSet rs211 = s.executeQuery("SELECT * FROM mrd_census_csd_types where id="+TableMainID);
					while(rs211.next()){
						csd_package_id=rs211.getInt("csd_package_id");
						csd_company_id=rs211.getInt("csd_company_id");	
						csd_brand_id=rs211.getInt("csd_brand");
						
						if(rs211.getInt("csd_company_id")==1){
							CSDCompanyName="Pepsi";
						}else if(rs211.getInt("csd_company_id")==2){
							CSDCompanyName="Coke";
						}else if(rs211.getInt("csd_company_id")==3){
							CSDCompanyName="Cola Next";
						}else if(rs211.getInt("csd_company_id")==4){
							CSDCompanyName="Gourmet";
						}else if(rs211.getInt("csd_company_id")==5){
							CSDCompanyName="None";
						}
						
						
						
						if(rs211.getInt("csd_package_id")==1){
							CSDPackageName="CAN - NORMAL";
						}else if(rs211.getInt("csd_package_id")==2){
							CSDPackageName="CAN - SLIM";
						}else if(rs211.getInt("csd_package_id")==3){
							CSDPackageName="250ML SSRB";
						}else if(rs211.getInt("csd_package_id")==4){
							CSDPackageName="250 NRB";
						}else if(rs211.getInt("csd_package_id")==5){
							CSDPackageName="345ML PET";
						}else if(rs211.getInt("csd_package_id")==6){
							CSDPackageName="500ML PET";
						}else if(rs211.getInt("csd_package_id")==7){
							CSDPackageName="1000ML PET";
						}else if(rs211.getInt("csd_package_id")==8){
							CSDPackageName="1500-1750ML PET";
						}else if(rs211.getInt("csd_package_id")==9){
							CSDPackageName="2000ML PET";
						}else if(rs211.getInt("csd_package_id")==10){
							CSDPackageName="2250ML PET";
						}else if(rs211.getInt("csd_package_id")==11){
							CSDPackageName="2500ML PET";
						}
						
						
						//Pepsi Brands
						if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="Pepsi";
						}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="7-UP";
						}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="M. Dew";
						}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="Mirinda";
						}else if(rs211.getInt("csd_brand")==5 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="M.Dew BP";
						}else if(rs211.getInt("csd_brand")==6 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="7UP-F";
						}else if(rs211.getInt("csd_brand")==7 && rs211.getInt("csd_company_id")==1){
							CSDBrandName="Diet Pepsi";
						}
						
						//Coke
						if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Coke";
						}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Coke Zero";
						}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Diet Coke";
						}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Sprite";
						}else if(rs211.getInt("csd_brand")==5 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Spr. Zero";
						}else if(rs211.getInt("csd_brand")==6 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Fanta";
						}else if(rs211.getInt("csd_brand")==7 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Fanta Citrus";
						}else if(rs211.getInt("csd_brand")==8 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Fanta Apple";
						}else if(rs211.getInt("csd_brand")==9 && rs211.getInt("csd_company_id")==2){
							CSDBrandName="Fanta Grape";
						}
						
						//Cola Next
						if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==3){
							CSDBrandName="Storm";
						}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==3){
							CSDBrandName="Fiz Up";
						}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==3){
							CSDBrandName="Rango";
						}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==3){
							CSDBrandName="Mount Dare";
						}
						
						//Gourmet
						
						if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Gourmet Lemon";
						}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Ice Soda";
						}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Twister";
						}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Red Annar";
						}if(rs211.getInt("csd_brand")==5 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Malta";
						}else if(rs211.getInt("csd_brand")==6 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Spark";
						}else if(rs211.getInt("csd_brand")==7 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Diet Cola";
						}else if(rs211.getInt("csd_brand")==8 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Dite Lemon";
						}if(rs211.getInt("csd_brand")==9 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Gourmet Apple";
						}else if(rs211.getInt("csd_brand")==10 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Moje Mango";
						}else if(rs211.getInt("csd_brand")==11 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Pulpy Orange";
						}else if(rs211.getInt("csd_brand")==12 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Gava";
						}else if(rs211.getInt("csd_brand")==13 && rs211.getInt("csd_company_id")==4){
							CSDBrandName="Gourmet Cola";
						}
						
						
						if(rs211.getInt("csd_company_id")==5){  //for none
							CSDPackageName="";
							CSDBrandName="";
						}
						int coid=1;
					%>
					
					
					
				<tr id="csd_<%=CSDRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:33%;"><%=CSDCompanyName %><input type="hidden" name="csd_co" id="csdco<%=CSDRowCounter%>"  value=<%=csd_company_id%>> </td>						
						<td style="width:33%;"><%=CSDPackageName %><input type="hidden" name="csd_pack" id="csdpac<%=CSDRowCounter%>"  value=<%=csd_package_id%>></td>						
						<td style="width:33%;"><%=CSDBrandName%><input type="hidden" name="csd_brand"  id="csdbrand<%=CSDRowCounter%>"  value=<%=csd_brand_id%>></td>
						<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='Remove("csd_<%=CSDRowCounter%>")'>Delete</a></td>
					
					</tr>
					
					
				
					
					
				<%
					
					CSDRowCounter++;
					}
					
					%>
					<input type="hidden" id="CSDRowCountHidden" value="<%=CSDRowCounter%>">
					
					
				</table>
				
			
				</li>	
				
				
					<li data-role="list-divider" data-theme="b">NCB</li>
			<li>		
				
				
				
					<table id="NCB_table" style="width:100%;">

					<%int NCBRowCounter=0;%>					
					<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th>Brand Name</th>
					</tr>
					<tr>
						<td>
							<select name="NCBcompany" data-mini="true" id="NCBcompanyddl">
							        <option value="-1">Select Company </option>
							        <option value="1">WATER </option>
							        <option value="2">JUICE</option>
							        <option value="3">ENERGY DRINKS </option>
							        <option value="4">FLAVOURED </option>
							        <option value="5">None </option>
							 </select>
						 </td>
						<td>
							<select name="NCBpackage" data-mini="true" id="NCBpackddl">
							       <option value="-1">Select Package </option>
							        <option value="1">TP </option>
							        <option value="2">SSRB </option>
							        <option value="3">NRB</option>
							        <option value="4">355ml</option>
							        <option value="5">500ml </option>
							        <option value="6">1000ml </option>
							        <option value="7">1500ml</option>
							        
							</select>
						</td>
						<td>
							<select name="NCBBrand" data-mini="true" id="NCBbrandddl">
							        
							</select>
						
						</td>
						<td>
							<a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewNCB("<%=NCBRowCounter%>")'>Plus</a>
						</td>
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
					
					<%
					
					String NCBCompanyName="";
					String NCBPackageName="";
					String NCBBrandName="";
					int ncb_company_id=0;
					int ncb_package_id=0;
					int ncb_brand_id=0;
					
					
					ResultSet rs2112 = s.executeQuery("SELECT * FROM mrd_census_ncb_types where id="+TableMainID);
					while(rs2112.next()){
					
						 ncb_company_id=rs2112.getInt("ncb_company_id");
						 ncb_package_id=rs2112.getInt("ncb_package_id");
						 ncb_brand_id=rs2112.getInt("ncb_brand");
						
						if(rs2112.getInt("ncb_company_id")==1){
							NCBCompanyName="WATER";
						}else if(rs2112.getInt("ncb_company_id")==2){
							NCBCompanyName="JUICE";
						}else if(rs2112.getInt("ncb_company_id")==3){
							NCBCompanyName="ENERGY DRINK";
						}else if(rs2112.getInt("ncb_company_id")==4){
							NCBCompanyName="FLAVOURED MILK";
						}else if(rs2112.getInt("ncb_company_id")==5){
							NCBCompanyName="None";
						}
						
						
						
						if(rs2112.getInt("ncb_package_id")==1){
							NCBPackageName="TP";
						}else if(rs2112.getInt("ncb_package_id")==2){
							NCBPackageName="SSRB";
						}else if(rs2112.getInt("ncb_package_id")==3){
							NCBPackageName="NRB";
						}else if(rs2112.getInt("ncb_package_id")==4){
							NCBPackageName="355ml";
						}else if(rs2112.getInt("ncb_package_id")==5){
							NCBPackageName="500ml";
						}else if(rs2112.getInt("ncb_package_id")==6){
							NCBPackageName="1000ml";
						}else if(rs2112.getInt("ncb_package_id")==7){
							NCBPackageName="1500ml";
						}
						
						
						//Water Brands
						if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==1){
							NCBBrandName="A.F";
						}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==1){
							NCBBrandName="NESTLE";
						}else if(rs2112.getInt("ncb_brand")==3 && rs2112.getInt("ncb_company_id")==1){
							NCBBrandName="KINLEY";
						}else if(rs2112.getInt("ncb_brand")==4 && rs2112.getInt("ncb_company_id")==1){
							NCBBrandName="GOURMET";
						}else if(rs2112.getInt("ncb_brand")==5 && rs2112.getInt("ncb_company_id")==1){
							NCBBrandName="OTHERS";
						}
						
						//Juice Brands
						if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==2){
							NCBBrandName="SLICE";
						}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==2){
							NCBBrandName="NESTLE";
						}else if(rs2112.getInt("ncb_brand")==3 && rs2112.getInt("ncb_company_id")==2){
							NCBBrandName="SHEZAN";
						}else if(rs2112.getInt("ncb_brand")==4 && rs2112.getInt("ncb_company_id")==2){
							NCBBrandName="FRUITIEN";
						}else if(rs2112.getInt("ncb_brand")==5 && rs2112.getInt("ncb_company_id")==2){
							NCBBrandName="RANI";
						}else if(rs2112.getInt("ncb_brand")==6 && rs2112.getInt("ncb_company_id")==2){
							NCBBrandName="OTHER";
						}
						
						//Energy Drinks Brands
						if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==3){
							NCBBrandName="STING";
						}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==3){
							NCBBrandName="REDBULL";
						}else if(rs2112.getInt("ncb_brand")==3 && rs2112.getInt("ncb_company_id")==3){
							NCBBrandName="OTHER";
						}
						
						//Falavored Milk Brands
						if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==4){
							NCBBrandName="NESTLE";
						}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==4){
							NCBBrandName="OLPER";
						}
						
						 if(rs2112.getInt("ncb_company_id")==5){ //for None
							NCBPackageName="";
							NCBBrandName="";
						}
						
							
							%>
							
							<tr id="ncb_<%=NCBRowCounter%>" style="font-size:13px; text-align:left;">
								<td style="width:33%;"><%=NCBCompanyName %><input type="hidden" name="ncb_co" id="ncbco<%=NCBRowCounter%>"  value=<%=ncb_company_id%>> </td>						
								<td style="width:33%;"><%=NCBPackageName %><input type="hidden" name="ncb_pack" id="ncbpac<%=NCBRowCounter%>"  value=<%=ncb_package_id%>></td>						
								<td style="width:33%;"><%=NCBBrandName%><input type="hidden" name="ncb_brand"  id="ncbbrand<%=NCBRowCounter%>"  value=<%=ncb_brand_id%>></td>
								<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemoveNCB("ncb_<%=NCBRowCounter%>")'>Delete</a></td>
							
							</tr>
							
							
							
							<%
							NCBRowCounter++;
							}
							%>
							
							
							
							<input type="hidden" id="NCBRowCounterHidden" value="<%=NCBRowCounter%>">
							
							
						</table>
				
			
				</li>	
				
				<!--  New Addition  -->
				
				
				<li data-role="list-divider" data-theme="b">Vol CSD</li>
			<li>		
				
				
				<table id="VolCSD_table" style="width:100%;">

					<%int VolCSDRowCounter=0;%>					
					<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th style="text-align: end" >Shopkeeper </th>
						<th>MDE</th>
						<th >Action</th>
					</tr>
					<tr>
						<td>
							<select name="VolCSDcompany" data-mini="true" id="VolCSDcompanyddl">
							       <option value="-1">Select Company</option>
							        <option value="1">PI </option>
							        <option value="2">KO</option>
							        <option value="3">Others </option>
							        <option value="4">None </option>
							       
							 </select>
						 </td>
						<td>
							<select name="VolCSDpackage" data-mini="true" id="VolCSDpackddl">
							        <option value="-1">Select Package</option>
							        <option value="1">SSRB </option>
							        <option value="2">PET-SS </option>
							        <option value="3">PET-MS</option>
							       
							        
							</select>
						</td>
						
									<td><input  type="text" name="outlet_general" data-mini="true" id="shopkeeper_volcsd" placeholder="Shopkeeper"></td>
									<td><input  type="text" name="System_Outlet_Name_general" data-mini="true"  id="MDE_volcsd" placeholder="MDE"></td>
									<td><a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewVolCSD("<%=VolCSDRowCounter%>")'>Plus</a></td>
								
								</tr>
							
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
					
					<%
					
					String VolCSDCompanyName="";
					String VolCSDPackageName="";
					int VolCSDBrandName=0;
					int VolCSDBrandNameMDE=0;
					int VolCSD_company_id=0;
					int VolCSD_package_id=0;
					
					ResultSet rs2112vc = s.executeQuery("SELECT * FROM mrd_census_vol_csd where id="+TableMainID);
					while(rs2112vc.next()){
						 VolCSD_company_id=rs2112vc.getInt("vol_csd_company_id");
						 VolCSD_package_id=rs2112vc.getInt("vol_csd_package_id");
					
						if(rs2112vc.getInt("vol_csd_company_id")==1){
							VolCSDCompanyName="PI";
						}else if(rs2112vc.getInt("vol_csd_company_id")==2){
							VolCSDCompanyName="KO";
						}else if(rs2112vc.getInt("vol_csd_company_id")==3){
							VolCSDCompanyName="Others";
						}else if(rs2112vc.getInt("vol_csd_company_id")==4){
							VolCSDCompanyName="None";
						}
						
						
						
						if(rs2112vc.getInt("vol_csd_package_id")==1){
							VolCSDPackageName="SSRB";
						}else if(rs2112vc.getInt("vol_csd_package_id")==2){
							VolCSDPackageName="PET - SS";
						}else if(rs2112vc.getInt("vol_csd_package_id")==3){
							VolCSDPackageName="PET - MS";
						}
						
						
						VolCSDBrandName = rs2112vc.getInt("vol_csd_brand");
						VolCSDBrandNameMDE = rs2112vc.getInt("vol_csd_mde");
						
						
						 if(rs2112vc.getInt("vol_csd_company_id")==4){ //for None
							 VolCSDBrandName=0;
							 VolCSDBrandNameMDE=0;
							}
						
						
						
					%>
					
				<tr id="volcsd_<%=VolCSDRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:25%;"><%=VolCSDCompanyName %><input type="hidden" name="volcsd_co" id="volcsdco<%=VolCSDRowCounter%>"  value=<%=VolCSD_company_id%>> </td>						
						<td style="width:25%;"><%=VolCSDPackageName %><input type="hidden" name="volcsd_pack" id="volcsdpac<%=VolCSDRowCounter%>"  value=<%=VolCSD_package_id%>></td>						
						<td style="width:25%;"><%=VolCSDBrandName%><input type="hidden" name="volcsd_brand1"  id="volcsdbrand1<%=VolCSDRowCounter%>"  value=<%=VolCSDBrandName%>></td>
						<td style="width:25%;"><%=VolCSDBrandNameMDE%><input type="hidden" name="volcsd_brand2"  id="volcsdbrand2<%=VolCSDRowCounter%>"  value=<%=VolCSDBrandNameMDE%>></td>
						<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemoveVolCSD("volcsd_<%=VolCSDRowCounter%>")'>Delete</a></td>
										
						
					</tr>
					
					<%
					VolCSDRowCounter++;
					}
					%>
					<input type="hidden" id="VolCSDRowCounterHidden" value="<%=VolCSDRowCounter%>">
					
					
					
				</table>
				
				
			
				</li>
				
				
				<li data-role="list-divider" data-theme="b">Vol Juice</li>
			<li>		
				
				
				<table id="VolJuice_table" style="width:100%;">

					<%int VolJuiceRowCounter=0;%>					
					<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th  >Shopkeeper </th>
						<th> MDE</th>
						<th >Action</th>
					</tr>
					<tr>
						<td>
							<select name="VolJuicecompany" data-mini="true" id="VolJuicecompanyddl">
							       <option value="-1">Select Comapny</option>
							        <option value="1">PI </option>
							        <option value="2">KO</option>
							        <option value="3">Others </option>
							        <option value="4">None </option>
							       
							 </select>
						 </td>
						<td>
							<select name="VolJuicepackage" data-mini="true" id="VolJuicepackddl">
							      
							        
							</select>
						</td>
						
									<td><input  type="text" name="outlet_general" data-mini="true" id="shopkeeper_voljuice" placeholder="Shopkeeper"></td>
									<td><input  type="text" name="System_Outlet_Name_general" data-mini="true"  id="MDE_voljuice" placeholder="MDE"></td>
									<td><a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewVolJuice("<%=VolJuiceRowCounter%>")'>Plus</a></td>
								
							
							
						</td>
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
					
					<%
					
					String VolJuiceCompanyName="";
					String VolJuicePackageName="";
					int VolJuiceBrandName=0;
					int VolJuiceBrandNameMDE=0;
					int VolJuice_company_id=0;
					int VolJuice_package_id=0;
					
					ResultSet rs2112vj = s.executeQuery("SELECT * FROM mrd_census_vol_juice where id="+TableMainID);
					while(rs2112vj.next()){
					
						 VolJuice_company_id=rs2112vj.getInt("vol_juice_company_id");
						 VolJuice_package_id=rs2112vj.getInt("vol_juice_package_id");
						
						if(rs2112vj.getInt("vol_juice_company_id")==1){
							VolJuiceCompanyName="PI";
						}else if(rs2112vj.getInt("vol_juice_company_id")==2){
							VolJuiceCompanyName="KO";
						}else if(rs2112vj.getInt("vol_juice_company_id")==3){
							VolJuiceCompanyName="Others";
						}else if(rs2112vj.getInt("vol_juice_company_id")==4){
							VolJuiceCompanyName="None";
						}
						
						
						
						if(rs2112vj.getInt("vol_juice_company_id")==1 && rs2112vj.getInt("vol_juice_package_id")==1){
							VolJuicePackageName="TETRA";
						}
						
						
						if(rs2112vj.getInt("vol_juice_company_id")==2 && rs2112vj.getInt("vol_juice_package_id")==1){
							VolJuicePackageName="SSRB";
						}else if(rs2112vj.getInt("vol_juice_company_id")==2 && rs2112vj.getInt("vol_juice_package_id")==2){
							VolJuicePackageName="PET";
						}
						
						
						if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==1){
							VolJuicePackageName="SSRB";
						}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==2){
							VolJuicePackageName="NRB";
						}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==3){
							VolJuicePackageName="PET";
						}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==4){
							VolJuicePackageName="TETRA";
						}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==5){
							VolJuicePackageName="CAN";
						}
						
						
						VolJuiceBrandName = rs2112vj.getInt("vol_juice_brand");
						VolJuiceBrandNameMDE = rs2112vj.getInt("vol_juice_mde");
						if(rs2112vj.getInt("vol_juice_company_id")==4){
							VolJuiceBrandName=0;
							VolJuiceBrandNameMDE=0;
						}
						
						
						
						
						
					%>
					
						
					<tr id="voljuice_<%=VolJuiceRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:25%;"><%=VolJuiceCompanyName %><input type="hidden" name="voljuice_co" id="voljuiceco<%=VolJuiceRowCounter%>"  value=<%=VolJuice_company_id%>> </td>						
						<td style="width:25%;"><%=VolJuicePackageName %><input type="hidden" name="voljuice_pack" id="voljuicepac<%=VolJuiceRowCounter%>"  value=<%=VolJuice_package_id%>></td>						
						<td style="width:25%;"><%=VolJuiceBrandName%><input type="hidden" name="voljuice_brand1"  id="voljuicebrand1<%=VolJuiceRowCounter%>"  value=<%=VolJuiceBrandName%>></td>
						<td style="width:25%;"><%=VolJuiceBrandNameMDE%><input type="hidden" name="voljuice_brand2"  id="voljuicebrand2<%=VolJuiceRowCounter%>"  value=<%=VolJuiceBrandNameMDE%>></td>
						<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemoveVolJuice("voljuice_<%=VolJuiceRowCounter%>")'>Delete</a></td>
					
					</tr>
					
					<%
					VolJuiceRowCounter++;
					}
					%>
					
			<input type="hidden" id="VolJuiceRowCounterHidden" value="<%=VolJuiceRowCounter%>">
					
					
				</table>
				
			
			
				
			
				</li>
				
				<li data-role="list-divider" data-theme="b">Vol Drinks</li>
			<li>		
				
				
					
				<table id="VolDrinks_table"  style="width:100%;">

					<%int VolDrinkRowCounter=0;%>					
					<tr>
						<th>Company Name</th>
						<th>Package Name</th>
						<th >Shopkeeper </th>
						<th > MDE</th>
						<th>Action</th>
					</tr>
					<tr>
						<td>
							<select name="VolDrinkcompany" data-mini="true" id="VolDrinkcompanyddl">
							       <option value="-1">Select Company</option>
							        <option value="1">Water </option>
							        <option value="2">Energy Drink</option>
							        <option value="3">None </option>
							        
							       
							 </select>
						 </td>
						<td>
							<select name="VolDrinkpackage" data-mini="true" id="VolDrinkpackddl">
							     
							</select>
						</td>
						
								
						<td><input  type="text" name="shopkeepervoldrink" data-mini="true" id="shopkeepervoldrink" placeholder="Shopkeeper"></td>
						<td><input  type="text" name="MDEvoldrink" data-mini="true"  id="MDEvoldrink" placeholder="MDE"></td>
						<td><a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewVolDrink("<%=VolDrinkRowCounter%>")'>Plus</a></td>
								
						
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
					
					<%
					
					String VolDrinkCompanyName="";
					String VolDrinkPackageName="";
					int VolDrinkBrandName=0;
					int VolDrinkBrandNameMDE=0;
					int VolDrink_company_id=0;
					int VolDrink_package_id=0;
					
					ResultSet rs2112vd = s.executeQuery("SELECT * FROM mrd_census_vol_drink where id="+TableMainID);
					while(rs2112vd.next()){
						
						 VolDrink_company_id=rs2112vd.getInt("vol_drink_company_id");
						 VolDrink_package_id=rs2112vd.getInt("vol_drink_package_id");
						if(rs2112vd.getInt("vol_drink_company_id")==1){
							VolDrinkCompanyName="Water";
						}else if(rs2112vd.getInt("vol_drink_company_id")==2){
							VolDrinkCompanyName="Energy Drink";
						}else if(rs2112vd.getInt("vol_drink_company_id")==3){
							VolDrinkCompanyName="None";
						}
						
						
						if(rs2112vd.getInt("vol_drink_company_id")==1 && rs2112vd.getInt("vol_drink_package_id")==1){
							VolDrinkPackageName="PI";
						}else if(rs2112vd.getInt("vol_drink_company_id")==1 && rs2112vd.getInt("vol_drink_package_id")==2){
							VolDrinkPackageName="KO";
						}else if(rs2112vd.getInt("vol_drink_company_id")==1 && rs2112vd.getInt("vol_drink_package_id")==3){
							VolDrinkPackageName="Others";
						}
						
						if(rs2112vd.getInt("vol_drink_company_id")==2 && rs2112vd.getInt("vol_drink_package_id")==1){
							VolDrinkPackageName="Sting";
						}else if(rs2112vd.getInt("vol_drink_company_id")==2 && rs2112vd.getInt("vol_drink_package_id")==2){
							VolDrinkPackageName="RedBull";
						}else if(rs2112vd.getInt("vol_drink_company_id")==2 && rs2112vd.getInt("vol_drink_package_id")==3){
							VolDrinkPackageName="Others";
						}
				
						
						
						
						
						VolDrinkBrandName = rs2112vd.getInt("vol_drink_brand");
						VolDrinkBrandNameMDE = rs2112vd.getInt("vol_drink_mde");
						
						 if(rs2112vd.getInt("vol_drink_company_id")==3){ //for None
							 VolDrinkBrandName=0;
							 VolDrinkBrandNameMDE=0;
							}
						
						
						
						
					%>
					<tr id="voldrink_<%=VolDrinkRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:25%;"><%=VolDrinkCompanyName %><input type="hidden" name="voldrink_co" id="voldrinkco<%=VolDrinkRowCounter%>"  value=<%=VolDrink_company_id%>> </td>						
						<td style="width:25%;"><%=VolDrinkPackageName %><input type="hidden" name="voldrink_pack" id="voldrinkpac<%=VolDrinkRowCounter%>"  value=<%=VolDrink_package_id%>></td>						
						<td style="width:25%;"><%=VolDrinkBrandName%><input type="hidden" name="voldrink_brand1"  id="voldrinkbrand1<%=VolDrinkRowCounter%>"  value=<%=VolDrinkBrandName%>></td>
						<td style="width:25%;"><%=VolDrinkBrandNameMDE%><input type="hidden" name="voldrink_brand2"  id="voldrinkbrand2<%=VolDrinkRowCounter%>"  value=<%=VolDrinkBrandNameMDE%>></td>
						<td ><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemoveVolDrink("voldrink_<%=VolDrinkRowCounter%>")'>Delete</a></td>
					
					</tr>
					
				
					
					<%
					VolDrinkRowCounter++;
					}
					%>
					
					<input type="hidden" id="VolDrinkRowCounterHidden" value="<%=VolDrinkRowCounter%>">
					
				</table>
				
				
				
			
				</li>
				
				<li data-role="list-divider" data-theme="b">Cooler Placement</li>
			<li>		
				
				
				<%int CoolerPlaceRowCounter=0; %>
				
				<table id="CoolerPlace_table" style="width:100%;">
					<tr>
						<td>
							<select name="CoolPlaceCompany" data-mini="true" id="CoolPlaceCompanyDDL">
							        <option value="-1">Select Company</option>
							        <option value="1">PI </option>
							        <option value="2">KO </option>
							        <option value="3">Nestle </option>
							        <option value="4">Redbull </option>
							        <option value="5">Ice Cream</option>
							         <option value="6">Personal </option>
							        <option value="7">Others</option>
							        
							 </select>
						 </td>
						<td>
							<select name="CoolPlacePack" data-mini="true" id="CoolPlacePackDDL">
							        <option value="1">Placement </option>
							       
							</select>
						</td>
						<td>
							<select name="CoolPlaceBrand" data-mini="true" id="CoolPlaceBrandDDL">
							       <option value="-1">Select Brand</option>
							        <option value="1">I/S</option>
							        <option value="2">O/S </option>
							        <option value="3">B/S </option>
							      
							</select>
						
						</td>
						<td>
						<a href="#" data-role="button" data-icon="plus" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='addNewCoolerPlace("<%=CoolerPlaceRowCounter%>")'>Plus</a>
						</td>
					</tr>
					<input type="hidden" name="tableId" value=<%=TableMainID %>>
						
					</tr>
					
					<%
					int CoolPlace_company_id=0;
					int CoolPlace_package_id=0;
					int CoolPlace_brand_id=0;
				
					String PlaceCompanyName="";
					String PlacePackageName="";
					String PlaceBrandName="";
					
					//System.out.println("SELECT * FROM mrd_census_pace where id="+TableMainID);
					ResultSet rs2112p = s.executeQuery("SELECT * FROM mrd_census_pace where id="+TableMainID);
					while(rs2112p.next()){
						 CoolPlace_company_id=rs2112p.getInt("pace_company_id");
						 CoolPlace_package_id=rs2112p.getInt("pace_package_id");
						 CoolPlace_brand_id=rs2112p.getInt("pace_brand");
					
						if(rs2112p.getInt("pace_company_id")==1){
							PlaceCompanyName="PI";
						}else if(rs2112p.getInt("pace_company_id")==2){
							PlaceCompanyName="KO";
						}else if(rs2112p.getInt("pace_company_id")==3){
							PlaceCompanyName="Nestle";
						}else if(rs2112p.getInt("pace_company_id")==4){
							PlaceCompanyName="Redbull";
						}else if(rs2112p.getInt("pace_company_id")==5){
							PlaceCompanyName="Ice Cream";
						}else if(rs2112p.getInt("pace_company_id")==6){
							PlaceCompanyName="Personal";
						}else if(rs2112p.getInt("pace_company_id")==7){
							PlaceCompanyName="Others";
						}
						
						
						//
						
						if(rs2112p.getInt("pace_package_id")==1){
							PlacePackageName="Placement";
						}else if(rs2112p.getInt("pace_package_id")==2){
							PlacePackageName="Visible";
						}else if(rs2112p.getInt("pace_package_id")==3){
							PlacePackageName="Access";
						}
						
						
						if(rs2112p.getInt("pace_package_id")==1 && rs2112p.getInt("pace_brand")==1){
							PlaceBrandName="I/S";
						}else if(rs2112p.getInt("pace_package_id")==1 && rs2112p.getInt("pace_brand")==2){
							PlaceBrandName="O/S";
						}else if(rs2112p.getInt("pace_package_id")==1 && rs2112p.getInt("pace_brand")==3){
							PlaceBrandName="B/S";
						}
					/*	
						if(rs2112p.getInt("pace_package_id")==2 && rs2112p.getInt("pace_brand")==1){
							PlaceBrandName="Yes";
						}else if(rs2112p.getInt("pace_package_id")==2 && rs2112p.getInt("pace_brand")==2){
							PlaceBrandName="No";
						}
						
						if(rs2112p.getInt("pace_package_id")==3 && rs2112p.getInt("pace_brand")==1){
							PlaceBrandName="Yes";
						}else if(rs2112p.getInt("pace_package_id")==3 && rs2112p.getInt("pace_brand")==2){
							PlaceBrandName="No";
						}
						*/
						
						//VolDrinkBrandName = rs2112vd.getInt("vol_drink_brand");
						
						
						
						
						
						
					%>
					<tr id="coolplace_<%=CoolerPlaceRowCounter%>" style="font-size:13px; text-align:left;">
						<td style="width:33%;"><%=PlaceCompanyName %><input type="hidden" name="coolplace_co" id="clco<%=CoolerPlaceRowCounter%>"  value=<%=CoolPlace_company_id%>> </td>						
						<td style="width:33%;"><%=PlacePackageName %><input type="hidden" name="coolplace_pack" id="clpac<%=CoolerPlaceRowCounter%>"  value=<%=CoolPlace_package_id%>></td>						
						<td style="width:33%;"><%=PlaceBrandName %><input type="hidden"  name="coolplace_brand" id="clbrand<%=CoolerPlaceRowCounter%>"  value=<%=CoolPlace_brand_id%>></td>
						<td><a href="#" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-ajax="false" data-inline="true" onclick='RemoveCoolerPlace("coolplace_<%=CoolerPlaceRowCounter%>")'>Delete</a></td>
					
				
						
						
					</tr>
				
					
					
					
					<%
					CoolerPlaceRowCounter++;
					}
					%>
					
					<input type="hidden" id="CoolerPlaceRowCounterHidden" value="<%=CoolerPlaceRowCounter%>">
					
				</table>
				
				
			
				</li>
				
<table width="100%">
  	<tr style="text-align:right">
  		<td width="80%"></td>
  		<td></td>
  		<td></td>
  		<td style="text-align:right">
  		<input type="hidden" name="btnId" id="btnId" value="1">
  			<a href="#" data-icon="check" data-ajax="false" data-theme="b"  data-role="button" data-inline="true" data-mini="true" onclick="FormSubmit(1)"   >Save</a>
	   	</td>
	   	<td style="text-align:right">
	   		<a href="#" data-icon="check" data-ajax="false" data-theme="a"  data-role="button" data-inline="true"  data-mini="true" onclick="isdelete(<%=CensusID %>,2)">Delete</a>
	   	</td>
   	</tr>
   </table>
			
			
			<%} %>	
				
				
				
				
					
			
	
  <div data-role="footer" class="ui-bar" data-theme="b" >
  
    
</div>
              
	    	
   
    </ul>
			

	</form>


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>