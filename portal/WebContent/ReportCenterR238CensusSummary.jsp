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
font-size: 8pt;
}

#map {
        width: 100%;
        height: 400px;
        margin-top: 10px;
      }

</style>





<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 299;
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

<script type="text/javascript">
function ExportToExcel(CensusID){
	$.ajax({
		
		url: "mobile/MobileCensusExcelMain",
		data: {
			CenID: CensusID
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				//alert("hello");
				//window.open('file:///D:/PushEmails/Mobile_Census_7233_20170110.xls');
				
				//window.location='file:///D:/PushEmails/Mobile_Census_7233_20170110.xls';
				 //var url='images/Mobile_Census_7233_20170110.xls';  
				 
				//var url = " file:///D:\\/PBC Development\\/EclipseWorkspaceFinal\\/portal\\/WebContent\\/images/Mobile_Census_7233_20170110.xlsx";
				 
				alert("File is created Successfully!");
				
				var url = "../OrderImages/"+json.FileName;
				
				//window.location=url; 
				 
				//window.open(url,"_blank");
				
				 //window.open(url+"", 'Excel', 'width=20,height=10,toolbar=0,menubar=0,scrollbars=no', '_blank');
				
				 
				 
				var mydiv = document.getElementById("ExcelFileReady");
				var aTag = document.createElement('a');
				aTag.setAttribute('href',url);
				aTag.setAttribute('target','_blank');
				aTag.innerHTML = "<label style='color:white; text-decoration:none; font-weight:bold;  cursor: pointer;'>File is ready.</label>";
				//mydiv.appendChild(aTag);
				
				$(mydiv).html(aTag);
				
				 
			}else{
				alert(json.error);
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
}



</script>


			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Outlet Summary <span style="f1loat:left; margin-left:200px;" id="ExcelFileReady"></span> <span style="float:right;"><a href="#"  id="t" onClick="ExportToExcel(<%=CensusID%>)">Export to Excel</a></span></li>
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
				
				
				 //Shopkeeper
				 
				 String IsShopClosed="";
				 String ShopClosedStatus="";
				 String DayOff="";
				 String PartialTiming="";
				 String SDistributor="";
				 String WholeSaller="";
				 String SubDistributor="";
				 String SuppliedBy="";
				 
				 String FinancialService="";
				 
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
				 String ShopHistory="";
				 String BusinessStructure="";
				 String SocioEco="";
				 String SShopType="";
				 String Wholesale="";
				 String Retailer="";
				 String ServiceType="";
				 String CSDTurnOver="";
				 String TradeChannel="";
				 String TradeSubChannel="";
				 String SupplyFrequency="";
				 String SupplyFrequencyKO="";
				 String SupplyFrequencyGormet="";
				 String SupplyFrequencyColaNext="";
				 
				 
				 
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
				 String IsCoveredByRCompnayDuration="";
				 
				 String StockStorageLocation1="";
				 String StockStorageLocation2="";
				 String StockStorageLocation3="";
				 String StockStorageLocation4="";
				 
				 String StockStorageLocation="";
				 
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
				 
				 
				 String VisibleColors="";
				 String AccessibleColors="";
				 
				 
				 //Shopkeeper
				 
				 String PerRawCaseDiscSSRB="";
				 String PerRawCaseDiscPET="";
				 
				 String AgreementExpDate="";
				 String AgreementType="";
				 String AgreementPeriod="";
				 
				 String AgreementType2="";
				 
				 
				 String PerRawCaseDiscSSRBPI="";
				 String PerRawCaseDiscPETPI="";
				 
				 String AgreementExpDatePI="";
				 String AgreementTypePI="";
				 String AgreementPeriodPI="";
				 
				 String AgreementType2PI="";
				 
				 String PerRawCaseDiscSSRBKO="";
				 String PerRawCaseDiscPETKO="";
				 
				 String AgreementExpDateKO="";
				 String AgreementTypeKO="";
				 String AgreementPeriodKO="";
				 
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
				 String OutletStructure="";
				 String AreaSqFt="";
				 
				 String CreatedBy="";
				 
				 double Lati=0;
				 double Longi=0;
				 double Accuracy=0;
				 
				//System.out.println("SELECT *,coc.label as trade_channel_label,(select label from common_outlets_channels_sub_channels cocsc where cocsc.parent_channel_id=coc.id and cocsc.id=mc.census_trader_channel_sub_channel) trade_sub_channel_labe FROM mrd_census mc join common_outlets_channels coc on mc.census_trader_channel=coc.id where mc.id="+CensusID);
				
				ResultSet rs = s.executeQuery("SELECT *,(select display_name from users u where u.id=mc.created_by) created_display_name FROM mrd_census mc where mc.id="+CensusID);
				while(rs.next()){
					 
					CensusIDMain = rs.getLong("census_id");
					TableMainID= rs.getLong("id");
					
					
					  VisibleColors= rs.getString("cooler_visible");
					  AccessibleColors= rs.getString("cooler_access");
					
					CreatedBy = rs.getLong("created_by")+" - "+rs.getString("created_display_name");
					  
					
					OutletID= rs.getString("outlet_id");
					// OutletName=rs.getString("outlet_name");;
					 
					 if(OutletID!="0" && OutletID!=""){
						 ResultSet rs231 = s2.executeQuery("select name from common_outlets where id="+OutletID);
						 while(rs231.next()){
							 OutletName=rs231.getString("name");
						 }
					 }
					 
					 
					 
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
					 
					 MVillage = rs.getString("census_distributor_town");
					 MLandMark = rs.getString("land_mark");
					 
					 //PJP ID
					 if(!rs.getString("pjp_id").equals("null")){
						 ResultSet rs23 = s2.executeQuery("SELECT * FROM pep.distributor_beat_plan where id="+rs.getLong("pjp_id"));
						 while(rs23.next()){
							PJP =  rs.getLong("pjp_id")+" - "+rs23.getString("label");
						 }
					 }
					 //Outlet Type
					 
					 if(rs.getLong("census_outlet_type")==1){
						 OutletType="Individual Outlet";
					 }else if(rs.getLong("census_outlet_type")==2){
						 OutletType="Clustered Outlet";
					 }
					 
					 //Shop Closed Status
					 
					 if(rs.getInt("is_census_shop_closed")==1){
						 if(rs.getInt("census_shop_closed_status")==1){
							 ShopClosedStatusN="Permanently";
						 }else{
							 ShopClosedStatusN="Temporarily";
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
					 
					 
					 if(rs.getInt("census_partially_timing")==1){
						 PartialTiming="Morning";
					 }else if(rs.getInt("census_partially_timing")==2){
						 PartialTiming="Noon";
					 }else if(rs.getInt("census_partially_timing")==3){
						 PartialTiming="Night";
					 }else if(rs.getInt("census_partially_timing")==4){
						 PartialTiming="None";
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
					 ShopCloseTime = rs.getString("census_owner_contact_person_shop_close_time");
					 
					 
					 //Status Tab
					 
					 if(rs.getInt("census_shop_status")==1){
						 ShopStatus="Seasonal";
					 }else if(rs.getInt("census_shop_status")==2){
						 ShopStatus="Permanent";
					 }
					 
					  
					  ShopHistory=rs.getString("census_shop_establishment_history");
					  
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
					  
					  if(rs.getInt("census_customer_type")==1){
						  SShopType="Wholesale";
					   }else if(rs.getInt("census_customer_type")==2){
						   SShopType="Retailer";
					   }else if(rs.getInt("census_customer_type")==3){
						   SShopType="Both";
					   }
					  
					  
					 
					  
					 
					  Wholesale=rs.getString("census_customer_type_wholesale_percent");
					  Retailer=rs.getString("census_customer_type_retailer_percent");
					  
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
					   SupplyFrequencyKO=rs.getString("supply_frequency_ko");
					   SupplyFrequencyGormet=rs.getString("supply_frequency_g");
					   SupplyFrequencyColaNext=rs.getString("supply_frequency_cn");
					   
					   
					   if(rs.getInt("census_exclusivity_agreement_1")==1){
						   ExPepsi="Pepsi";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_1")==2){
							 ExCoke="Coke";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_1")==3){
							 ExGourmet="Gourmet";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_1")==4){
							 ExMezan="Mezan";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_1")==5){
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
						 
						 
						 if(rs.getInt("census_shop_closed_status")==1){
							 IsCoveredByRCompnay="Yes";
							 
							 if(rs.getInt("census_shop_researcher_closed_status")==1){
								 IsCoveredByRCompnayDuration="Monthly";
							 }else if(rs.getInt("census_shop_researcher_closed_status")==2){
								 IsCoveredByRCompnayDuration="Quarterly";
							 }else if(rs.getInt("census_shop_researcher_closed_status")==3){
								 IsCoveredByRCompnayDuration="Annually";
							 }
							 
							 
							 
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
						 
						 
						 
						 
						  
						  if(rs.getInt("census_shop_selling_status_ssrb")==1){
							  BeveragesSellingFullCasesSSRB=rs.getInt("census_shop_selling_bev"); 
						  }
						  
						  if(rs.getInt("census_shop_selling_status_pet")==1){
							  BeveragesSellingFullCasesPET=rs.getInt("census_shop_selling_pet"); 
						  }
						  
						  if(rs.getInt("census_shop_selling_status_nrb")==1){
							  BeveragesSellingFullCasesTETRA=rs.getInt("census_shop_selling_nrb"); 
						  }
						  
						  if(rs.getInt("census_shop_selling_status_can")==1){
							  BeveragesSellingFullCasesCAN=rs.getInt("census_shop_selling_can"); 
						  }
						  
						  
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
						 
						 
						 //Per Raw Case Discount  - SSRB
						 
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
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDate = rs.getInt("aggr_exp_date_mm")+"/"+rs.getInt("aggr_exp_date_yy");
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type")==1){
							 AgreementType="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type")==2){
							 AgreementType="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type")==3){
							 AgreementType="Written-Not Witnessed";
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period")==1){
							 AgreementPeriod="1";
						 }else if(rs.getInt("census_shop_agreement_period")==2){
							 AgreementPeriod="2";
						 }else if(rs.getInt("census_shop_agreement_period")==3){
							 AgreementPeriod="3 & Above";
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
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDatePI = rs.getInt("aggr_exp_date_mm_pi")+"/"+rs.getInt("aggr_exp_date_yy_pi");
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type_pi")==1){
							 AgreementTypePI="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type_pi")==2){
							 AgreementTypePI="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type_pi")==3){
							 AgreementTypePI="Written-Not Witnessed";
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period_pi")==1){
							 AgreementPeriodPI="1";
						 }else if(rs.getInt("census_shop_agreement_period_pi")==2){
							 AgreementPeriodPI="2";
						 }else if(rs.getInt("census_shop_agreement_period_pi")==3){
							 AgreementPeriodPI="3 & Above";
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
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDateKO = rs.getInt("aggr_exp_date_mm_ko")+"/"+rs.getInt("aggr_exp_date_yy_ko");
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type_ko")==1){
							 AgreementTypeKO="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type_ko")==2){
							 AgreementTypeKO="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type_ko")==3){
							 AgreementTypeKO="Written-Not Witnessed";
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period_ko")==1){
							 AgreementPeriodKO="1";
						 }else if(rs.getInt("census_shop_agreement_period_ko")==2){
							 AgreementPeriodKO="2";
						 }else if(rs.getInt("census_shop_agreement_period_ko")==3){
							 AgreementPeriodKO="3 & Above";
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
						 
						 
						 
						 /////////////////////////////////////////////////////////
						 
						 
						 //// Supplied by Ko 
						 
						 //census_feeded_stock_percentage_1,census_feeded_stock_percentage_2,census_feeded_stock_percentage_3,census_feeded_stock_percentage_4
				
						if(!rs.getString("census_feeded_stock_percentage_1").equals("null")){
							SuppliedByDistributorKO=rs.getString("census_feeded_stock_percentage_1")+"%";
						}
						 
						if(!rs.getString("census_feeded_stock_percentage_2").equals("null")){
							SuppliedByWholeSellerKO=rs.getString("census_feeded_stock_percentage_2")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_percentage_3").equals("null")){
							SuppliedByMobilerKO=rs.getString("census_feeded_stock_percentage_3")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_percentage_4").equals("null")){
							SuppliedByDealerKO=rs.getString("census_feeded_stock_percentage_4")+"%";
						}
						 
						 
						 //Supplied by PI 
						 
						 //census_feeded_stock_1_p_pi_21,census_feeded_stock_2_p_pi_21,census_feeded_stock_3_p_pi_21,census_feeded_stock_4_p_pi_21
				
						if(!rs.getString("census_feeded_stock_1_p_pi_21").equals("null")){
							SuppliedByDistributorPI=rs.getString("census_feeded_stock_1_p_pi_21")+"%";
						}
						 
						if(!rs.getString("census_feeded_stock_2_p_pi_21").equals("null")){
							SuppliedByWholeSellerPI=rs.getString("census_feeded_stock_2_p_pi_21")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_3_p_pi_21").equals("null")){
							SuppliedByMobilerPI=rs.getString("census_feeded_stock_3_p_pi_21")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_4_p_pi_21").equals("null")){
							SuppliedByDealerPI=rs.getString("census_feeded_stock_4_p_pi_21")+"%";
						}
				}
				
				%>
				
				
				
				<table style="width:100%;">
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Outlet ID</th>
						<td><%=OutletID %></td>
						<th style="background-color:#f4f3f3">System Outlet Name</th>
						<td><%=OutletName %></td>
						<th style="background-color:#f4f3f3">Outlet Name On Board</th>
						<td><%=OutletNameOB %></td>						
						<th style="background-color:#f4f3f3">Actual Outlet Name</th>
						<td><%=OutletNameActual %></td>
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						
						<th style="background-color:#f4f3f3">Distributor</th>
						<td><%=Distributor %></td>
						
						<th style="background-color:#f4f3f3">PJP</th>
						<td><%=PJP %></td>
						
						<th style="background-color:#f4f3f3">Area</th>
						<td><%=Area %></td>
						<th style="background-color:#f4f3f3">Location</th>
						<td><%=Location %></td>
						
						
						
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						
						<th style="background-color:#f4f3f3">Shop Location</th>
						<td><%=ShopLocation %></td>
						
						<th style="background-color:#f4f3f3">Socio Economic Classification</th>
						<td><%=SocioEco %></td>
						
						<th style="background-color:#f4f3f3">Outlet Type</th>
						<td><%=OutletType %></td>
						
						<th style="background-color:#f4f3f3">Address (with key location)</th>
						<td><%=OutletAddress %></td>
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						
						<th style="background-color:#f4f3f3">District</th>
						<td><%=MDistrict %></td>
						<th style="background-color:#f4f3f3">Tehsil</th>
						<td><%=MTehsil %></td>
						<th style="background-color:#f4f3f3">Village</th>
						<td><%=MVillage %></td>
						<th style="background-color:#f4f3f3">Land Mark</th>
						<td><%=MLandMark %></td>
						
						</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						
						
						
						
						
						<th style="background-color:#f4f3f3">Shop Closed Status</th>
						<td><%=ShopClosedStatusN %></td>
						
						<th style="background-color:#f4f3f3">Outlet Structure</th>
						<td><%=OutletStructure %></td>
						
						<th style="background-color:#f4f3f3">Trade Sub-Channel</th>
						<td><%=TradeSubChannel %></td>
						
						<th style="background-color:#f4f3f3">Service Type</th>
						<td><%=ServiceType %></td>
						
						</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						
						
						<th style="background-color:#f4f3f3">Shop Type</th>
						<td><%=ShopType %></td>
						
						
						
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						
						<th style="background-color:#f4f3f3">Owner Name</th>
						<td><%=OwnerName %></td>
						<th style="background-color:#f4f3f3">Landline</th>
						<td><%=OwnerContactNo1 %></td>
						<th style="background-color:#f4f3f3">Mobile</th>
						<td><%=OwnerContactNo2 %></td>
						
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Owner CNIC</th>
						<td><%=OwnerCNIC %></td>
						<th style="background-color:#f4f3f3">Contact Person</th>
						<td><%=ContactPerson %></td>
						
						<th style="background-color:#f4f3f3">Contact Person Name</th>
						<td><%=ContactPersonName %></td>
						<th style="background-color:#f4f3f3">Contact Person No.</th>
						<td><%=ContactPersonNo %></td>
						
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Contact Person CNIC</th>
						<td><%=ContactPersonCNIC %></td>
						<th style="background-color:#f4f3f3">Email ID</th>
						<td><%=EmailID %></td>
						
						<th style="background-color:#f4f3f3">Area Sq. ft</th>
						<td><%=AreaSqFt %></td>
						<th style="background-color:#f4f3f3">CreatedBy</th>
						<td><%=CreatedBy %></td>
						
					
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Latitude</th>
						<td><%=Lati %></td>
						<th style="background-color:#f4f3f3">Longitude</th>
						<td><%=Longi %></td>
						
						<th style="background-color:#f4f3f3">Accuracy</th>
						<td><%=Accuracy %></td>
						
						
						
					</tr>
				</table>
				
				
			
				</li>	
				
				<li data-role="list-divider" data-theme="b">Shopkeeper</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Shop Status</th>
						<td><%=ShopStatus %></td>
						<th style="background-color:#f4f3f3">Off Day</th>
						<td><%=DayOff %></td>
						<th style="background-color:#f4f3f3">Shop Opening-Closing Time</th>
						<td><%=ShopOpeningTime %> - <%=ShopCloseTime %></td>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Financial Service</th>
						<td><%=FinancialService %></td>
						<th colspan="2">Outlet Selling Beverages in full cases?</th>
						
						<td>SSRB:<%=BeveragesSellingFullCasesSSRB %>%&nbsp;PET:<%=BeveragesSellingFullCasesPET %>%&nbsp;Tetra:<%=BeveragesSellingFullCasesTETRA %>%&nbsp;CAN:<%=BeveragesSellingFullCasesCAN %>%</td>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Exclusivity Agreement</th>
						<td><%=ExclusivityAgreementPI %><%if(ExclusivityAgreementKO!=""){ %><%=","+ExclusivityAgreementKO %><%} %><%if(ExclusivityAgreementGou!=""){ %><%=","+ExclusivityAgreementGou %><%} %><%if(ExclusivityAgreementMezan!=""){ %><%=","+ExclusivityAgreementMezan%><%} %><%if(ExclusivityAgreementOther!=""){ %><%=","+ExclusivityAgreementOther %><%} %></td>
						
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Per Raw Case Discount - SSRB</th>
						<td><%=PerRawCaseDiscSSRB %></td>
						<th style="background-color:#f4f3f3">Per Raw Case Discount - PET</th>
						<td><%=PerRawCaseDiscPET %></td>
						<th style="background-color:#f4f3f3">Agreement Expiry Date</th>
						<td><%=AgreementExpDate %></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Agreement Type</th>
						<td><%=AgreementType %></td>
						<th style="background-color:#f4f3f3">Agreement Period</th>
						<td><%=AgreementPeriod %></td>
						<th style="background-color:#f4f3f3">Agreement Type</th>
						<td><%=AgreementType2 %></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<th colspan="6" style="font-size:14px;">Discount Agreement PI</th>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Per Raw Case Discount - SSRB</th>
						<td><%=PerRawCaseDiscSSRBPI %></td>
						<th style="background-color:#f4f3f3">Per Raw Case Discount - PET</th>
						<td><%=PerRawCaseDiscPETPI %></td>
						<th style="background-color:#f4f3f3">Agreement Expiry Date</th>
						<td><%=AgreementExpDatePI %></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Agreement Type PI </th>
						<td><%=AgreementTypePI %></td>
						<th style="background-color:#f4f3f3">Agreement Period</th>
						<td><%=AgreementPeriodPI %></td>
						<th style="background-color:#f4f3f3">Agreement Type</th>
						<td><%=AgreementType2PI %></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<th colspan="6" style="font-size:14px;">Discount Agreement KO</th>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Per Raw Case Discount - SSRB</th>
						<td><%=PerRawCaseDiscSSRBKO %></td>
						<th style="background-color:#f4f3f3">Per Raw Case Discount - PET</th>
						<td><%=PerRawCaseDiscPETKO %></td>
						<th style="background-color:#f4f3f3">Agreement Expiry Date</th>
						<td><%=AgreementExpDateKO %></td>
					</tr>
					
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Agreement Type KO</th>
						<td><%=AgreementTypeKO %></td>
						<th style="background-color:#f4f3f3">Agreement Period</th>
						<td><%=AgreementPeriodKO %></td>
						<th style="background-color:#f4f3f3">Agreement Type</th>
						<td><%=AgreementType2KO %></td>
					</tr>
					
					
					
					
					
					
					
					<tr style="font-size:13px; text-align:left;">
						
						
						<th style="background-color:#f4f3f3">If Partially close, then timing</th>
						<td><%=PartialTiming %></td>
						
						
						
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<th colspan="6" style="font-size:14px;">Supplied By KO</th>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						
						
						<th style="background-color:#f4f3f3">Distributor</th>
						<td><%=SuppliedByDistributorKO %></td>
						<th style="background-color:#f4f3f3">Wholeseller</th>
						<td><%=SuppliedByWholeSellerKO %></td>
						<th style="background-color:#f4f3f3">Mobiler</th>
						<td><%=SuppliedByMobilerKO %></td>
						<th style="background-color:#f4f3f3">Dealer</th>
						<td><%=SuppliedByDealerKO %></td>
						
						
						
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					<tr>
						<th colspan="6" style="font-size:14px;">Supplied By PI</th>
					</tr>
					<tr style="font-size:13px; text-align:left;">
						
						
						<th style="background-color:#f4f3f3">Distributor</th>
						<td><%=SuppliedByDistributorPI %></td>
						<th style="background-color:#f4f3f3">Wholeseller</th>
						<td><%=SuppliedByWholeSellerPI %></td>
						<th style="background-color:#f4f3f3">Mobiler</th>
						<td><%=SuppliedByMobilerPI %></td>
						<th style="background-color:#f4f3f3">Dealer</th>
						<td><%=SuppliedByDealerPI %></td>
						
						
						
					</tr>
					
					
					
					
					
				</table>
				
				
			
				</li>	
				
				<li data-role="list-divider" data-theme="b">Status</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<tr style='font-size:13px; text-align:left;'>
						<th style="background-color:#f4f3f3">Supply Frequency</th>
						<td><%=SupplyFrequency %></td>
						<th style="background-color:#f4f3f3">Supply Frequency KO</th>
						<td><%if(SupplyFrequencyKO!=null){%><%=SupplyFrequencyKO %><%} %></td>
						<th style="background-color:#f4f3f3">Supply Frequency Gormet</th>
						<td><%if(SupplyFrequencyGormet!=null){%><%=SupplyFrequencyGormet %><%} %></td>
						<th style="background-color:#f4f3f3">Supply Frequency Cola Next</th>
						<td><%if(SupplyFrequencyColaNext!=null){%><%=SupplyFrequencyColaNext %><%} %></td>
					
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						<th>Covered by Research Company?</th>
						<td><%if(IsCoveredByRCompnay!=""){%><%=IsCoveredByRCompnay %> & <%=IsCoveredByRCompnayDuration %><%} %></td>
						<th>Stock Storage Location</th>
						<td><%=StockStorageLocation1 %><%if(StockStorageLocation2!=""){ %><%=","+StockStorageLocation2%><%} %><%if(StockStorageLocation3!=""){ %><%=","+StockStorageLocation3%><%} %><%if(StockStorageLocation4!=""){ %><%=","+StockStorageLocation4%><%} %></td>
						<th>Cash Machine</th>
						<td><%=CashMachineQuantity %></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">						
						<th style="background-color:#f4f3f3">Shop Establishment History</th>
						<td><%=ShopHistory %></td>
						<th style="background-color:#f4f3f3">Business Structure</th>
						<td><%=BusinessStructure %></td>
						<th style="background-color:#f4f3f3">Shop Type</th>
						<td><%=SShopType %></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">Wholesale %</th>
						<td><%=Wholesale %></td>
						<th style="background-color:#f4f3f3">Retailer %</th>
						<td><%=Retailer %></td>
						<!-- <th style="background-color:#f4f3f3">Discount Status</th> 
						<td><%=DiscountStatus %></td>-->
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr style="font-size:13px; text-align:left;">
						<th style="background-color:#f4f3f3">PI CSD</th>
						<td><%=PICSD %></td>
						<th style="background-color:#f4f3f3">PI Sting</th>
						<td><%=PISting %></td>
						<th style="background-color:#f4f3f3">KO</th>
						<td><%=KO %></td>
						
						
						
					</tr>
					
					
					
					
					
					
					
					
					
					
					
					
				</table>
				
				
			
				</li>
				
				
				
				<li data-role="list-divider" data-theme="b">Cooler Type</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					String CoolerTypeCompnayName="";
					String CoolerTypeTypeName="";
					
					ResultSet rs2 = s.executeQuery("SELECT * FROM mrd_census_cooler_types where id="+TableMainID);
					while(rs2.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:33%;"><%=CoolerTypeCompnayName %></td>						
						<td style="width:33%;"><%=CoolerTypeTypeName %></td>						
						<td style="width:33%;"><%=rs2.getInt("quantity") %></td>
						
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>
				
				
				
				<li data-role="list-divider" data-theme="b">Cooler Codes</li>
			<li>		
				
				
				<table style="width:100%;">
					
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:33%;"><b>Visible Colors:&nbsp;&nbsp;</b><%=VisibleColors %></td>						
						<td style="width:33%;"><b>Accessible Coolors:&nbsp;&nbsp;</b><%=AccessibleColors %></td>						
						
					</tr>
					
					<%
					String CoolerSerialNo="";
					
					ResultSet rs2c = s.executeQuery("SELECT * FROM mrd_census_cooler_code where id="+TableMainID);
					while(rs2c.next()){
						CoolerSerialNo +=rs2c.getString("tot_code")+",";
					%>
					
					
					
					<%
					}
					%>
					
					<tr style="font-size:13px; text-align:left;">						
						<th style="width:33%;">Codes</th>						
						<td style="width:33%;"><%=CoolerSerialNo %></td>						
						
						
					</tr>
					
					
					
					
					
				</table>
				
				
			
				</li>
					
				
				<li data-role="list-divider" data-theme="b">Publicity Type</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String PublicityTypeCompnayName="";
					String PublicityTypeTypeName="";
					
					ResultSet rs21 = s.executeQuery("SELECT * FROM mrd_census_publicity_types where id="+TableMainID);
					while(rs21.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:33%;"><%=PublicityTypeCompnayName %></td>						
						<td style="width:33%;"><%=PublicityTypeTypeName %></td>						
						<td style="width:33%;"><%=rs21.getInt("quantity") %></td>
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>	
				
				<li data-role="list-divider" data-theme="b">CSD</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String CSDCompanyName="";
					String CSDPackageName="";
					String CSDBrandName="";
					
					ResultSet rs211 = s.executeQuery("SELECT * FROM mrd_census_csd_types where id="+TableMainID);
					while(rs211.next()){
					
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
						
					%>
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:33%;"><%=CSDCompanyName %></td>						
						<td style="width:33%;"><%=CSDPackageName %></td>						
						<td style="width:33%;"><%=CSDBrandName%></td>
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>	
				
				
					<li data-role="list-divider" data-theme="b">NCB</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String NCBCompanyName="";
					String NCBPackageName="";
					String NCBBrandName="";
					
					ResultSet rs2112 = s.executeQuery("SELECT * FROM mrd_census_ncb_types where id="+TableMainID);
					while(rs2112.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:33%;"><%=NCBCompanyName %></td>						
						<td style="width:33%;"><%=NCBPackageName %></td>						
						<td style="width:33%;"><%=NCBBrandName%></td>
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>	
				
				<!--  New Addition  -->
				
				
				<li data-role="list-divider" data-theme="b">Vol CSD</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String VolCSDCompanyName="";
					String VolCSDPackageName="";
					int VolCSDBrandName=0;
					int VolCSDBrandNameMDE=0;
					
					ResultSet rs2112vc = s.executeQuery("SELECT * FROM mrd_census_vol_csd where id="+TableMainID);
					while(rs2112vc.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:25%;"><%=VolCSDCompanyName %></td>						
						<td style="width:25%;"><%=VolCSDPackageName %></td>						
						<td style="width:25%;"><%=VolCSDBrandName%></td>
						<td style="width:25%;"><%=VolCSDBrandNameMDE%></td>
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>
				
				
				<li data-role="list-divider" data-theme="b">Vol Juice</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String VolJuiceCompanyName="";
					String VolJuicePackageName="";
					int VolJuiceBrandName=0;
					int VolJuiceBrandNameMDE=0;
					
					ResultSet rs2112vj = s.executeQuery("SELECT * FROM mrd_census_vol_juice where id="+TableMainID);
					while(rs2112vj.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:25%;"><%=VolJuiceCompanyName %></td>						
						<td style="width:25%;"><%=VolJuicePackageName %></td>						
						<td style="width:25%;"><%=VolJuiceBrandName%></td>
						<td style="width:25%;"><%=VolJuiceBrandNameMDE%></td>
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>
				
				<li data-role="list-divider" data-theme="b">Vol Drinks</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String VolDrinkCompanyName="";
					String VolDrinkPackageName="";
					int VolDrinkBrandName=0;
					int VolDrinkBrandNameMDE=0;
					
					ResultSet rs2112vd = s.executeQuery("SELECT * FROM mrd_census_vol_drink where id="+TableMainID);
					while(rs2112vd.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:25%;"><%=VolDrinkCompanyName %></td>						
						<td style="width:25%;"><%=VolDrinkPackageName %></td>						
						<td style="width:25%;"><%=VolDrinkBrandName%></td>
						<td style="width:25%;"><%=VolDrinkBrandNameMDE%></td>
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>
				
				<li data-role="list-divider" data-theme="b">Cooler Placement</li>
			<li>		
				
				
				<table style="width:100%;">
					
					<%
					
					String PlaceCompanyName="";
					String PlacePackageName="";
					String PlaceBrandName="";
					
					//System.out.println("SELECT * FROM mrd_census_pace where id="+TableMainID);
					ResultSet rs2112p = s.executeQuery("SELECT * FROM mrd_census_pace where id="+TableMainID);
					while(rs2112p.next()){
					
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
					
					<tr style="font-size:13px; text-align:left;">
						
						<td style="width:25%;"><%=PlaceCompanyName %></td>						
						<td style="width:25%;"><%=PlacePackageName %></td>						
						<td style="width:25%;"><%=PlaceBrandName%></td>
						
						
						
					</tr>
					
					<%
					}
					%>
					
					
					
				</table>
				
				
			
				</li>
				
				
				<!-- ////////////////////////////// -->
				
				
				
				
					<li data-role="list-divider" data-theme="b">Images</li>
			<li>
				
				<table>
					
					<%
					//System.out.println("select * from mrd_census_files where id="+TableMainID);
					
					int i=1;
					ResultSet rsi = s.executeQuery("select * from mrd_census_files where id="+TableMainID);
					
					while(rsi.next()){
					%>
					
					
						<%if(i==1){ %>
						<tr>
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						<%} %>
						<td>&nbsp;</td>
						<%if(i==2){ %>
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						</tr>
						<%} %>
					
					<%if(i==3){%>
					<tr>
						 
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						<%} %>
						<td>&nbsp;</td>
						<%if(i==4){ %>
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						
					</tr>
					<%} %>
					<%if(i==5){%>
					<tr>
						 
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						
						
					</tr>
					<%} %>
					
					<%
					i++;
					}
					%>
				
				</table>
				
					
			</li>
			
			
			<!--  Map -->
				
				
				 <li>
    <li data-role="list-divider" data-theme="b">Map</li>
    <table>
     
     <script>
         initMap();

        function initMap(){
         
           var myLatlng = new google.maps.LatLng(31.427335740000000,73.094063400000000);
           var mapOptions = {
             zoom: 15,
             center: myLatlng
           };

           var map = new google.maps.Map(document.getElementById('map'), mapOptions);
       
          // alert("hello "+map);
           
           var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';

           var infowindow = new google.maps.InfoWindow({
               content: contentString,
               maxWidth: 200
           });
           
           var markers = new Array();
           
           
         map.setCenter(new google.maps.LatLng(<%=Lati%>,<%=Longi%>));
            
            
            markers[0] = new google.maps.Marker({
                position: new google.maps.LatLng(<%=Lati%>,<%=Longi%>),
                map: map,
                title: '<%=OutletName%>'
                
            });
            google.maps.event.addListener(markers[0], 'click', function() {
              infowindow.open(map,markers[0]);
              
              var infoWindowContent = "<%=OutletID%>-<%=OutletName%><br>"; 
              infoWindowContent += "<a href='#'>View Detail</a>";
              infowindow.setContent(infoWindowContent);
              //infowi
            });
            
        }  
           
           
         
        
        
        
        </script>
        
     </table>
        
        <div id="map" style=""><p>ABCKSKJD</p></div>
    
    </table>
    
     
   </li>
			
			
			<%} %>	
				
				
				

				
					
			</ul>



<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>