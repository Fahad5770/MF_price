<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.pbc.common.Region"%>


<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 252;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();
Statement s7 = c.createStatement();
Statement s8 = c.createStatement();
Statement s9 = c.createStatement();
Statement s10 = c.createStatement();
Statement s11= c.createStatement();
Statement s12 = c.createStatement();
Statement s13 = c.createStatement();
Statement s14 = c.createStatement();
Statement s15 = c.createStatement();
Statement s16 = c.createStatement();
Statement s17 = c.createStatement();
Statement s18 = c.createStatement();
Statement s19 = c.createStatement();
Statement s20 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");




if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
int SelectedYear = Utilities.getYearByDate(StartDate);



long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

String PackageIDs = "";
String WherePackage = "";

if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = " and package_id in ("+PackageIDs+") ";
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}


String WhereHOD = "";
String WhereHOD1 = "";

if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD1 = " and cache_snd_id in ("+HODIDs+") ";

}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
String WhereRSM1 = "";

if (RSMIDs.length() > 0){
	WhereRSM = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and cache_rsm_id in("+RSMIDs+") ";
	
}

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);


long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}


String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
	for(int i = 0; i < SelectedBrandArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandArray[i]+"";
		}
		
	}

	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}



/*
long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}

System.out.println("DistributorID = "+DistributorID);
String WhereCustomerID ="";
String WhereDistributorID ="";
if (DistributorID != 0){
	WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs+") ";
}else{
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs+") ";
}
*/

//System.out.println("Hello "+WhereCustomerID);

//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){

	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs2 = "";
String WhereCustomerID ="";
String WhereDistributorID ="";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs2 += SelectedDistributorsArray[i];
		}else{
			DistributorIDs2 += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereCustomerID = " and customer_id in ("+DistributorIDs2+") ";
	WhereDistributorID = " and distributor_id in ("+DistributorIDs2+") ";
	//out.print(WhereDistributors);
}

//GTM Category


String GTMCategoryIDs="";
long SelectedGTMCategoryArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory") != null){
	SelectedGTMCategoryArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory");
	GTMCategoryIDs = Utilities.serializeForSQL(SelectedGTMCategoryArray);
}

String WhereGTMCategory = "";
String WhereGTMCategory1 = "";
String WhereGTMCategory2 = "";
String WhereGTMCategory3 = "";
if (GTMCategoryIDs.length() > 0){
	WhereGTMCategory = " and customer_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";	
	WhereGTMCategory1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
}


//System.out.println("Hello "+WhereCustomerID);

//Region
String RegionIDs="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}


Region[] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String ScopeRegionIDs = UserAccess.getRegionQueryString(RegionObj);


String WhereRegion = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and region_id in ("+RegionIDs+") ";	
}


//Lifting Type

String SelectedLiftingTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType") != null){
	SelectedLiftingTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType");
}

String WhereLiftingType = "";
if(SelectedLiftingTypeArray!=null){
	for(int i=0;i<SelectedLiftingTypeArray.length;i++){
		if(SelectedLiftingTypeArray[i].equals("Internal")){
			WhereLiftingType = " and idn.outsourced_primary_sales_id is null ";
		}else if(SelectedLiftingTypeArray[i].equals("Other Plant")){
			WhereLiftingType = " and idn.outsourced_primary_sales_id is not null ";
		}
	}
}

//System.out.println("WhereLiftingType = "+WhereLiftingType);

s.executeUpdate("DROP TABLE IF EXISTS dist_targets_temp ");

s.executeUpdate("DROP TABLE IF EXISTS dist_targets_report_temp");


s.executeUpdate("CREATE  TABLE dist_targets_temp (order_number varchar(100) , distributor_id varchar(100) ,distributor_name varchar(100) ,entry_date datetime ,order_date datetime ,fksak varchar(100) ,abgru varchar(100) ,posnr varchar(100) ,sap_code varchar(100) ,arktx varchar(100) , type varchar(100) , raw_case int, units int,total_units int , pstyv varchar(100) )");

s.executeUpdate("CREATE  TABLE dist_targets_report_temp (distributor_id int,distributor_name varchar(100),sales decimal(18,2),target decimal(18,2), percentage varchar(50),package_id int,type_id int,snd_id int, rsm_id int, tdm_id int, target_mtd decimal(18,2))");
	

%>





<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>

<%
long SND_ID = -1;



 String WhereSNDID = " and (snd_id in ("+SND_ID+") or rsm_id in ("+SND_ID+")  or tdm_id in ("+SND_ID+") ) ";

if (SND_ID == -1){
	WhereSNDID = " and (snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";	
}






//creating temporary table



Datasource dsSAP = new Datasource();
dsSAP.createConnectionToSAPDB();
Statement sSAP = dsSAP.createStatement();


Date MinTargetDate = new Date();
Date MaxTargetDate = new Date();


//System.out.println(StartDate);

Date CurrentDate = StartDate;//Utilities.parseDate("01/12/2015");
//System.out.println(CurrentDate);

ResultSet rs14 = s3.executeQuery("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=2");
if(rs14.first()){
	MinTargetDate = rs14.getDate(1);
	MaxTargetDate = rs14.getDate(2); //Utilities.parseDate("10/01/2014");//rs14.getDate(1);
}

//System.out.println(MinTargetDate+" "+MaxTargetDate);
	
//s.executeUpdate("CREATE  TABLE dist_targets_temp (order_number varchar(100) , distributor_id varchar(100) ,distributor_name varchar(100) ,entry_date date ,order_date date ,fksak varchar(100) ,abgru varchar(100) ,posnr varchar(100) ,sap_code varchar(100) ,arktx varchar(100) , type varchar(100) , raw_case int, units int,total_units int , pstyv varchar(100) )");

//s.executeUpdate("CREATE  TABLE dist_targets_report_temp (distributor_id int,distributor_name varchar(100),sales decimal(18,2),target decimal(18,2), percentage varchar(50),package_id int,type_id int,snd_id int, rsm_id int, tdm_id int)");


String QueryPrt="";
int UnitPerSKU=0;
int TotalUnits=0;


SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");



ResultSet rs5 = s3.executeQuery("select idn.sap_order_no, idn.distributor_id, idn.created_on, ipv.sap_code, idnp.total_units, ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate)+" and ipv.category_id = 1"+WhereLiftingType);
//System.out.println("===> select idn.sap_order_no, idn.distributor_id, idn.created_on, ipv.sap_code, idnp.total_units, ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate)+" and ipv.category_id = 1"+WhereLiftingType);
while(rs5.next()){
	
	
	long OrderNo = rs5.getLong("sap_order_no");
	long SapCode=rs5.getLong("sap_code");
	//double RawCases = rs5.getDouble("raw_cases");
	double units = rs5.getDouble("total_units");
	
	
	if (units != 0){
		QueryPrt ="units";
		s.executeUpdate("insert into dist_targets_temp(order_number  , distributor_id  ,distributor_name  ,entry_date  ,order_date  ,fksak  ,abgru  ,posnr  ,sap_code  ,arktx  , type  , "+QueryPrt+" ,total_units  , pstyv ) values("+OrderNo+",'"+rs5.getString("distributor_id")+"',(select name from common_distributors cd where cd.distributor_id='"+rs5.getString("distributor_id")+"') ,'"+rs5.getString("created_on")+"','"+rs5.getString("created_on")+"','0','0','0',"+SapCode+",'0','0',0,"+units+",'0')");
	}
}



//-- Putting data in Temp Tabel -- //



String SAPCodes = "6150,6151,6152,6153";

//System.out.println("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate));

ResultSet rs60 = s.executeQuery("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate));
if (rs60.next()){
	SAPCodes += rs60.getString(1);
}

int PackageCount = 0;
int ArrayCount=0;
int PackageID=0;
int DistributorID=0;
double GrandTotal=0;
String DistributorName="";

int SNDID=0;
int RSMID=0;
int TDMID=0;

//System.out.println("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");

ResultSet rs11 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
while(rs11.next()){
ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs11.getInt("lrb_type_id")+" order by package_sort_order");


while(rs2.next()){
	
	PackageCount++;

	ArrayCount++;
}

//PackageCount = PackageCount+1; //1 plus for extra column of converted cases


}




String PackageArray[] = new String [ArrayCount];	
double TargetedSale[] = new double [ArrayCount];
double AttainedSale[] = new double [ArrayCount];
double TotalTargetedSalePercentage[] = new double [ArrayCount];
double TotalAttainedSalePercentage[] = new double [ArrayCount];




long SalesTotal[] = new long[ArrayCount+1];
int TargetTotal[] = new int[ArrayCount+1];
long PercentageTotal[] = new long[ArrayCount+1]; //+1 for converted cases

for (int i = 0; i < SalesTotal.length; i++){
SalesTotal[i] = 0;								
TargetTotal[i]=0;
PercentageTotal[i]=0;
}




				ResultSet rs1 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+" "+WhereSNDID+" and distributor_id in (select dt.distributor_id from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=2)"); //distributor query
				
				while(rs1.next()){
					
					//double TDMWiseTotalSales=0;
					//double TDMWiseTotalTarget=0;
					
					
					double TotalSalesConverted = 0;
					double TotalTargetConverted = 0;
					double TotalTargetConvertedMTD = 0;
					
					DistributorID = rs1.getInt("distributor_id");
					DistributorName = rs1.getString("name");
					SNDID = rs1.getInt("snd_id");
					RSMID = rs1.getInt("rsm_id");
					TDMID = rs1.getInt("tdm_id");
					
					
					Date TargetStartDate = new Date();
					Date TargetEndDate = new Date();

					ResultSet rs19 = s3.executeQuery("select dt.start_date, dt.end_date from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.distributor_id = "+DistributorID+" and dt.type_id=2");
					if(rs19.first()){
						TargetStartDate = rs19.getDate(1);
						TargetEndDate = rs19.getDate(2);
					}
					
			
					//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
					//rs.beforeFirst();
					
					int PackageIndex = 0;
					long SalesConvertedCases=0;
					long TargetConvertedCases=0;
					double ConvertedCasesPercentage=0;
					
					long SalesConvertedCasesCSD=0;
					long TargetConvertedCasesCSD=0;
					double ConvertedCasesPercentageCSD=0;
					
					
					long SalesConvertedCasesNCB=0;
					long TargetConvertedCasesNCB=0;
					double ConvertedCasesPercentageNCB=0;
					
					
					int TypeID=0;
					
					
					SalesTotal[PackageIndex] += SalesConvertedCases;
					TargetTotal[PackageIndex] += TargetConvertedCases;							
					
					
					
					
					//////////////////////////// ----------- Converted FOR CSD --------------------- ////////////////////////////////////
					
					
					
					
					PackageIndex++;
					
				
					
				
					ResultSet rs12 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
					while(rs12.next()){
						
						TypeID = rs12.getInt("lrb_type_id");
						
						ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku, liquid_in_ml FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs12.getInt("lrb_type_id")+" order by package_sort_order");
						while(rs.next()){
							
							int unit_per_sku=0;
							PackageID=rs.getInt("package_id");
						
							double target_sales_amount=0;
							double TargetUnitPerSKU = rs.getDouble(3);
							double TargetLiquidInML = rs.getDouble(4);
							
							double percentage=0;
							
							int TargetTotalDays = 0;
							int TargetDaysLapsed = 0;
							ResultSet rs99 = s3.executeQuery("select (to_days(dt.end_date)-to_days(dt.start_date)) TotalDays, to_days(date("+Utilities.getSQLDate(CurrentDate)+"))-to_days(dt.start_date) DaysLapsed from distributor_targets dt where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=2");
							if(rs99.first()){
								TargetTotalDays = rs99.getInt(1);
								TargetDaysLapsed = rs99.getInt(2);
							}
							
							
							ResultSet rs4 = s3.executeQuery("select dtp.quantity from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dtp.package_id="+PackageID+WherePackage+" and dt.type_id=2");
							if(rs4.first()){
								target_sales_amount=rs4.getDouble("quantity");	
							}
							
							if (PackageID == 2 || PackageID == 6 || PackageID == 3){ // 500ML, 1500ML
								if (TypeID == 1 || TypeID == 2 || TypeID == 3 || TypeID == 4){ // Water, Energy Drink, Juices
									//target_sales_amount = 0;
									ResultSet rs20 = s3.executeQuery("select sum(dtpb.quantity) from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join distributor_targets_packages_brands dtpb on dtp.id = dtpb.id and dtp.package_id = dtpb.package_id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dtp.package_id="+PackageID+" and dt.type_id=2 and dtpb.brand_id in (select brand_id from inventory_products where lrb_type_id = "+TypeID+")");
									if(rs20.first()){
										target_sales_amount=rs20.getDouble(1);	
									}
								}
							}
							
							double TargetMTD = (target_sales_amount / TargetTotalDays) * TargetDaysLapsed;
							
							long sales_amount=0;
							ResultSet rs3 = s3.executeQuery("select sum(total_units) sale_amount, ipv.unit_per_sku from dist_targets_temp dtt join inventory_products_view ipv ON dtt.sap_code = ipv.sap_code where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDateLifting(TargetStartDate)+" and "+Utilities.getSQLDateNextLifting(CurrentDate)+" and dtt.distributor_id = "+DistributorID+" and ipv.package_id="+PackageID+WherePackage+" and ipv.lrb_type_id="+TypeID);
							if(rs3.first()){
								sales_amount = rs3.getLong("sale_amount");									
								unit_per_sku = rs3.getInt("unit_per_sku");								
							}
							
							long SalesRawCases = Utilities.getRawCasesAndUnits(sales_amount, unit_per_sku)[0];
							
							
							/* Insert converted cases */
							
							int ConvertedTypeID = -2; // CSD
							if (TypeID != 1){
								ConvertedTypeID = -3; // NCB
							}
							
							double TargetConverted = (target_sales_amount *  TargetUnitPerSKU * TargetLiquidInML) / 6000;
							double SalesConverted = (SalesRawCases * TargetUnitPerSKU * TargetLiquidInML) / 6000;
							
							
							double TargetConvertedMTD = (TargetConverted / TargetTotalDays) * TargetDaysLapsed;
							TotalSalesConverted += SalesConverted;
							TotalTargetConverted += TargetConverted;
							TotalTargetConvertedMTD += TargetConvertedMTD;
							
							
							
							s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id, target_mtd) values("+DistributorID+",'"+DistributorName+"',"+SalesConverted+","+TargetConverted+",'0',"+ConvertedTypeID+","+ConvertedTypeID+","+SNDID+","+RSMID+","+TDMID+","+TargetConvertedMTD+")");
							
							
							/* end insert converted cases*/
							
							
							if(target_sales_amount!=0){
								percentage = (Utilities.parseDouble(SalesRawCases+"")/target_sales_amount)*100;	
							}	
							
							SalesTotal[PackageIndex] += SalesRawCases;
							TargetTotal[PackageIndex] += target_sales_amount;
							
					
							
							//insert it into temp table
							
							s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id, target_mtd) values("+DistributorID+",'"+DistributorName+"',"+SalesRawCases+","+target_sales_amount+",'"+percentage+"',"+PackageID+","+TypeID+","+SNDID+","+RSMID+","+TDMID+","+TargetMTD+")");
							
							PackageIndex++;
						}
					}
					
					double TotalConvertedPercentage = 0;
					if (TotalTargetConverted != 0){
						TotalConvertedPercentage = (TotalSalesConverted / TotalTargetConverted) * 100;
					}
					s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id, target_mtd) values("+DistributorID+",'"+DistributorName+"',"+TotalSalesConverted+","+TotalTargetConverted+",'"+TotalConvertedPercentage+"',-1,-1,"+SNDID+","+RSMID+","+TDMID+","+TotalTargetConvertedMTD+")");
			}

				
				
				
			


// -- ---------------------------- -- //

// Display Report


%>



<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					 	<tr>
					 		<td colspan="4" style="text-align:center; background-color:#3f4040; color:white;"></td>
					 		<td colspan="3" style="text-align:center; background-color:#3f4040; color:white;"></td>
					 		<%
					 		 ResultSet rs101 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
						while(rs101.next()){
							%>
							
							<td colspan="<%=rs101.getInt("packge_count")*3%>" style="text-align:center; background-color:#3f4040; color:white;"><%=rs101.getString("type_name") %></td>
							
							<%
						}
						 %>
					 	
					 	</tr>
					 	
					 	<tr>
					 	<td colspan="4" style="background-color:#dfdfdf;"></td>
					 	<td colspan="3" style="background-color:#dfdfdf;">Converted Cases</td>
					 	<%
					 	ResultSet rs111 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id =2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
						while(rs111.next()){
						ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id =2) ) "+WherePackage+ " and lrb_type_id="+rs111.getInt("lrb_type_id")+" order by package_sort_order");
						
						
						while(rs2.next()){
							
							PackageCount++;
						%>
							<td colspan="3" style="background-color:#dfdfdf;"><%=rs2.getString("package_label") %></td>
						<%		
						}
						}
						 %>
						 </tr>
						 
						 <tr>
						 	<td colspan="4" style="background-color:#dfdfdf;"></td>
							<td style="background-color:#dfdfdf;">S</td>
							<td style="background-color:#dfdfdf;">T</td>
							<td style="background-color:#dfdfdf;">P</td>
							<%
							for (int i=0; i<ArrayCount; i++){
							%>
							
							<td style="background-color:#dfdfdf;">S</td>
							<td style="background-color:#dfdfdf;">T</td>
							<td style="background-color:#dfdfdf;">P</td>
							
							<%	
							}
							%>
						 </tr>
						 
						 <tr>
						 <%
						 int PackArrayCount=0;
						%>
						
							<td colspan="4" style="background-color:#7b7b7b; color:white; ">Consolidated</td>
							<%
							
							ResultSet rs99 = s19.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)");
							if(rs99.first()){
								double CasesCovertedPercentage1=0;
								double SalesCoverted = rs99.getDouble("sum_converted");
								double TargetCoverted = rs99.getDouble("sum_targeted");
								
								if(TargetCoverted!=0){
									CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
								}
							
								if(SalesCoverted!=0){
									%>
									
									<td style="background-color:#7b7b7b;color:white;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)%></td>
								<%
								}else{
									%>
									
									<td style="background-color:#7b7b7b;color:white;"></td>
									<%
								}
								
								if(TargetCoverted!=0){
									%>
									
									<td style="background-color:#7b7b7b;color:white;"><%=Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)%></td>
								<%
								}else{
									%>
									<td style="background-color:#7b7b7b;color:white;"></td>
									
									<%
								}
									
								if(CasesCovertedPercentage1!=0){
									%>
									<td style="background-color:#7b7b7b;color:white;"><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)%>%</td>
								<%
								}else{
									%>
									<td style="background-color:#7b7b7b;color:white;"></td>
									
									<%
								}
							}
							
							
							
							%>
							
							<%
							//Normal 
							
							
							ResultSet rs12323 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs12323.next()){
								
								int TypeID3 = rs12323.getInt("lrb_type_id");
								String TypeName = rs12323.getString("type_name");
								ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku,liquid_in_ml FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs12323.getInt("lrb_type_id")+" order by package_sort_order");
								
								while(rs.next()){
								int unit_per_sku=0;
								PackageID=rs.getInt("package_id");
								String PackageLabel="";									
								PackageLabel = rs.getString("package_label");
								
								double UnitPerSKUPieChart=rs.getDouble("unit_per_sku");
								double LiquidInMLPieChart=rs.getDouble("liquid_in_ml");
								
								//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
								
								ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)");
								if(rs315.first()){
									double CasesPercentage1=0;
									double Sales = rs315.getDouble("sum_sales");
									double Target = rs315.getDouble("sum_target");
									
									if(Target!=0){	
										CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
									}
									
									
									if(Sales!=0){
										%>
										
										<td style="background-color:#7b7b7b; color:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Sales)%></td>
									<% 
									}else{
										%>	
										<td style="background-color:#7b7b7b; color:white; "></td>
									<%
									}
									
									if(Target!=0){
										%>
										<td style="background-color:#7b7b7b; color:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Target)%></td>
									<%
									}else{
										 %>
										<td style="background-color:#7b7b7b; color:white; "></td>
									<%
									}
									
									
									if(CasesPercentage1!=0){
										 %>
										<td style="background-color:#7b7b7b; color:white; "><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)%>%</td>
									<%
									}else{
										%>
										
										<td style="background-color:#7b7b7b; color:white; "></td>
									<%	
									}
									
									
									TargetedSale[PackArrayCount] = Math.round(((Target*UnitPerSKUPieChart)*LiquidInMLPieChart)/6000);
									AttainedSale[PackArrayCount] = Math.round(((Sales*UnitPerSKUPieChart)*LiquidInMLPieChart)/6000);
									
									
								}
								
								if(TypeName.equals("Energy Drinks")){
									TypeName="Sting";
								}
								if(PackageID==6 || PackageID==2){ //500ML and 1500ML
									PackageArray[PackArrayCount] = PackageLabel+" "+TypeName;
								}else{
									PackageArray[PackArrayCount] = PackageLabel;
								}
								
								
								
								
								PackArrayCount++;
								}
							}
							
							
							
							////////////////////////////////////////////////////////////////////
							
							
							
							
							%>
							
							
							
							<%

							
							
							long DistributorIDD1=0;
							long DistributorIDD=0;
							
							long RSMID1=0;
							String RSMName="";
							String RSMDisplay="";
							
							long SNDID1=0;
							String SNDName="";
							String SNDDisplay ="";
							
							
							ResultSet rs1132 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp where snd_id is not null");
							while(rs1132.next()){
								SNDID1 = rs1132.getLong("snd_id");
								SNDName = rs1132.getString("snd_name");
								
								
								
								SNDDisplay = SNDID1+ " - "+ SNDName;
								if (SNDID1 == 0){
									SNDDisplay = "Unassigned";
								}
								%>
								<tr>
								<td style="background-color:#c3c2c2;" colspan="4"><%=SNDDisplay%></td>
											
									<%		
											ResultSet rs3152 = s17.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID1);
											if(rs3152.first()){
												double CasesCovertedPercentage1=0;
												double SalesCoverted = rs3152.getDouble("sum_converted");
												double TargetCoverted = rs3152.getDouble("sum_targeted");
												
												if(TargetCoverted!=0){
													CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
												}
											
											if(SalesCoverted!=0){
												%>
												
												
												<td style="background-color:#c3c2c2;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)%></td>
											<%
											}else{
												%>
												<td style="background-color:#c3c2c2;"></td>
											<%
											}
											
											if(TargetCoverted!=0){
												%>
												
												<td style="background-color:#c3c2c2;"><%=Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)%></td>
											<%
											}else{
												%>
												
												<td style="background-color:#c3c2c2;"></td>
											<%
											}
												
											if(CasesCovertedPercentage1!=0){
												%>
												<td style="background-color:#c3c2c2;"><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)%>%</td>
											<%
											}else{
												%>
												<td style="background-color:#c3c2c2;"></td>
											<%
											}
												
												
												
											
											}
									ResultSet rs1232 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id  = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
									while(rs1232.next()){
										
										int TypeID3 = rs1232.getInt("lrb_type_id");
										String TypeName = rs1232.getString("type_name");
										ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku,liquid_in_ml FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs1232.getInt("lrb_type_id")+" order by package_sort_order");
										
										while(rs.next()){
										int unit_per_sku=0;
										PackageID=rs.getInt("package_id");
										String PackageLabel="";									
										PackageLabel = rs.getString("package_label");
										
										double UnitPerSKUPieChart=rs.getDouble("unit_per_sku");
										double LiquidInMLPieChart=rs.getDouble("liquid_in_ml");
										
										//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
										
										ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and snd_id="+SNDID1);
										if(rs315.first()){
											double CasesPercentage1=0;
											double Sales = rs315.getDouble("sum_sales");
											double Target = rs315.getDouble("sum_target");
											
											if(Target!=0){	
												CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
											}
											
											

											if(Sales!=0){
												%>
												
												<td style="background-color:#c3c2c2; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Sales)%></td>
											<% 
											}else{
												%>	
												<td style="background-color:#c3c2c2; c1olor:white; "></td>
											<%
											}
											
											if(Target!=0){
												%>
												<td style="background-color:#c3c2c2; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Target)%></td>
											<%
											}else{
												 %>
												<td style="background-color:#c3c2c2; c1olor:white; "></td>
											<%
											}
											
											if(CasesPercentage1!=0){
												 %>
												<td style="background-color:#c3c2c2; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)%>%</td>
											<%
											}else{
												%>
												
												<td style="background-color:#c3c2c2; c1olor:white; "></td>
											<%	
											}
											
											
											//TargetedSale[PackArrayCount] = Math.round(((Target*UnitPerSKUPieChart)*LiquidInMLPieChart)/6000);
											//AttainedSale[PackArrayCount] = Math.round(((Sales*UnitPerSKUPieChart)*LiquidInMLPieChart)/6000);
											
											
										}
										
										/*if(TypeName.equals("Energy Drinks")){
											TypeName="Sting";
										}
										if(PackageID==6 || PackageID==2){ //500ML and 1500ML
											PackageArray[PackArrayCount] = PackageLabel+" "+TypeName;
										}else{
											PackageArray[PackArrayCount] = PackageLabel;
										}
										
										*/
										
										
										PackArrayCount++;
										}
									}
							
							%>
							
							</tr>
							<tr>
							<%
							ResultSet rs1131 = s13.executeQuery("SELECT distinct rsm_id,(select display_name from users u where u.id=rsm_id) rsm_name FROM dist_targets_report_temp where snd_id="+SNDID1);
							while(rs1131.next()){
								RSMID1 = rs1131.getLong("rsm_id");
								RSMName = rs1131.getString("rsm_name");
								
								
								RSMDisplay = RSMID1+ " - "+ RSMName;
								if (RSMID1 == 0){
									RSMDisplay = "Unassigned";
								}
								%>
								
								<td colspan="4" style="background-color:#d3d4bb;"><%=RSMDisplay%></td>
									<%		
											
											ResultSet rs3151 = s14.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and rsm_id="+RSMID1+" and snd_id="+SNDID1);
											if(rs3151.first()){
												double CasesCovertedPercentage1=0;
												double SalesCoverted = rs3151.getDouble("sum_converted");
												double TargetCoverted = rs3151.getDouble("sum_targeted");
												
												if(TargetCoverted!=0){
													CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
												}
												
											
											
											
												if(SalesCoverted!=0){
													%>
													
													
													<td style="background-color:#d3d4bb;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)%></td>
												<%
												}else{
													%>
													<td style="background-color:#d3d4bb;"></td>
												<%
												}
												
												if(TargetCoverted!=0){
													%>
													
													<td style="background-color:#d3d4bb;"><%=Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)%></td>
												<%
												}else{
													%>
													
													<td style="background-color:#d3d4bb;"></td>
												<%
												}
													
												if(CasesCovertedPercentage1!=0){
													%>
													<td style="background-color:#d3d4bb;"><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)%>%</td>
												<%
												}else{
													%>
													<td style="background-color:#d3d4bb;"></td>
												<%
												}
									
											}
									ResultSet rs1231 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
									while(rs1231.next()){
										
										int TypeID3 = rs1231.getInt("lrb_type_id");
										ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs1231.getInt("lrb_type_id")+" order by package_sort_order");
										
										while(rs.next()){
										int unit_per_sku=0;
										PackageID=rs.getInt("package_id");
										//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
										
										ResultSet rs314 = s15.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
										if(rs314.first()){
											double CasesPercentage1=0;
											double Sales = rs314.getDouble("sum_sales");
											double Target = rs314.getDouble("sum_target");
											
											if(Target!=0){
												CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
											}
											
											
											if(Sales!=0){
												%>
												
												<td style="background-color:#d3d4bb; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Sales)%></td>
											<% 
											}else{
												%>	
												<td style="background-color:#d3d4bb; c1olor:white; "></td>
											<%
											}
											
											if(Target!=0){
												%>
												<td style="background-color:#d3d4bb; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Target)%></td>
											<%
											}else{
												 %>
												<td style="background-color:#d3d4bb; c1olor:white; "></td>
											<%
											}
											
											if(CasesPercentage1!=0){
												 %>
												<td style="background-color:#d3d4bb; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)%>%</td>
											<%
											}else{
												%>
												
												<td style="background-color:#d3d4bb; c1olor:white; "></td>
											<%	
											}
										
										}
										}
									}
									
									%>
									
									</tr>
									<tr>
									
									
									<%
									
									
									long TDMID1 = 0;
									String TDMName = "";
									
									
									
									ResultSet rs113 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID1+" and snd_id="+SNDID1);
									while(rs113.next()){
										TDMID1 = rs113.getLong("tdm_id");
										TDMName = rs113.getString("tdm_name");
										
										
										 String TDMDisplay = TDMID1+ " - "+ TDMName;
										if (TDMID1 == 0){
											TDMDisplay = "Unassigned";
										}
										
										%>
										
										
										<td colspan="4" style="background-color:#eeeee1;"><%=TDMDisplay%></td>	
										<%			
													ResultSet rs315 = s8.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
													if(rs315.first()){
														double CasesCovertedPercentage1=0;
														double SalesCoverted = rs315.getDouble("sum_converted");
														double TargetCoverted = rs315.getDouble("sum_targeted");
														
														if(TargetCoverted!=0){
															CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
														}
													
													
													
														if(SalesCoverted!=0){
															%>
															
															
															<td style="background-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)%></td>
														<%
														}else{
															%>
															<td style="background-color:#eeeee1;"></td>
														<%
														}
														
														if(TargetCoverted!=0){
															%>
															
															<td style="background-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)%></td>
														<%
														}else{
															%>
															
															<td style="background-color:#eeeee1;"></td>
														<%
														}
															
														if(CasesCovertedPercentage1!=0){
															%>
															<td style="background-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)%>%</td>
														<%
														}else{
															%>
															<td style="background-color:#eeeee1;"></td>
														<%
														}
													}
													
											ResultSet rs123 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
											while(rs123.next()){
												
												int TypeID3 = rs123.getInt("lrb_type_id");
												ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs123.getInt("lrb_type_id")+" order by package_sort_order");
												
												while(rs.next()){
												int unit_per_sku=0;
												PackageID=rs.getInt("package_id");
												//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
												
												ResultSet rs314 = s8.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
												if(rs314.first()){
													double CasesPercentage1=0;
													double Sales = rs314.getDouble("sum_sales");
													double Target = rs314.getDouble("sum_target");
													
													if(Target!=0){
														CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
													}
													
													
													if(Sales!=0){
														%>
														
														<td style="background-color:#eeeee1; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Sales)%></td>
													<% 
													}else{
														%>	
														<td style="background-color:#eeeee1; c1olor:white; "></td>
													<%
													}
													
													if(Target!=0){
														%>
														<td style="background-color:#eeeee1; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(Target)%></td>
													<%
													}else{
														 %>
														<td style="background-color:#eeeee1; c1olor:white; "></td>
													<%
													}
													
													if(CasesPercentage1!=0){
														 %>
														<td style="background-color:#eeeee1; c1olor:white; "><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)%>%</td>
													<%
													}else{
														%>
														
														<td style="background-color:#eeeee1; c1olor:white; "></td>
													<%	
													}
												
												}
												}
											}
											
											%>
											</tr>
											
											<%
											String WhereTDMi = " and tdm_id="+TDMID1;
											if (TDMID1 == 0){
												WhereTDMi = " and tdm_id is null ";
											}
											
											
											String WhereRSMi = " and rsm_id="+RSMID1;
											if (RSMID1 == 0){
												WhereRSMi = " and rsm_id is null ";
											}
											
											String WhereSNDi = " and snd_id="+SNDID1;
											if (SNDID1 == 0){
												WhereSNDi = " and snd_id is null ";
											}
											
											//System.out.println("select * from common_distributors where 1=1 "+WhereHOD+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select dt.distributor_id from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=3) "+WhereTDMi+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
											
											ResultSet rs112 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+" "+WhereSNDID+" and distributor_id in (select dt.distributor_id from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=2) "+WhereTDMi+WhereRSMi+WhereSNDi); //distributor query
											while(rs112.next()){
												
												//System.out.println("hello");
												DistributorIDD = rs112.getLong("distributor_id");
												
											 %>
												<tr>
												<td colspan="4"><%=Utilities.truncateStringToMax(DistributorIDD+ " - "+ rs112.getString("name"), 29)%></td>	
												
												<%	
												ResultSet rs314 = s8.executeQuery("select * from dist_targets_report_temp where package_id=-1 and type_id=-1 and distributor_id="+rs112.getLong("distributor_id"));
												while(rs314.next()){
													
													double SalesConvertedCases1 = rs314.getDouble("sales");
													double TargetConvertedCases1 = rs314.getDouble("target");
													int ConvertedCasesPercentage1 = rs314.getInt("percentage");
													
													
													if(SalesConvertedCases1!=0){
														%>
														
														
														<td style="b1ackground-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesConvertedCases1)%></td>
													<%
													}else{
														%>
														<td style="b1ackground-color:#eeeee1;"></td>
													<%
													}
													
													if(TargetConvertedCases1!=0){
														%>
														
														<td style="b1ackground-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(TargetConvertedCases1)%></td>
													<%
													}else{
														%>
														
														<td style="b1ackground-color:#eeeee1;"></td>
													<%
													}
														
													if(ConvertedCasesPercentage1!=0){
														%>
														<td style="b1ackground-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesPercentage1)%>%</td>
													<%
													}else{
														%>
														<td style="b1ackground-color:#eeeee1;"></td>
													<%
													}
													
													
													
												}
												
												
												ResultSet rs12 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
											while(rs12.next()){
												
												int TypeID = rs12.getInt("lrb_type_id");
												
												ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where ( sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) ) "+WherePackage+ " and lrb_type_id="+rs12.getInt("lrb_type_id")+" order by package_sort_order");
												
												while(rs.next()){
												int unit_per_sku=0;
												PackageID=rs.getInt("package_id");
												
												ResultSet rs313 = s8.executeQuery("select * from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID+" and distributor_id="+rs112.getLong("distributor_id"));
												while(rs313.next()){   
													
													
													
													
													double SalesRawCases1 = rs313.getDouble("sales");
													double target_sales_amount1 = rs313.getDouble("target");
													int percentage1 = rs313.getInt("percentage");
													
													
													DistributorIDD1=DistributorIDD; 
													
													
													
													if(SalesRawCases1!=0){
														%>
														
														
														<td style="b1ackground-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesRawCases1)%></td>
													<%
													}else{
														%>
														<td style="b1ackground-color:#eeeee1;"></td>
													<%
													}
													
													if(target_sales_amount1!=0){
														%>
														
														<td style="b1ackground-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(target_sales_amount1)%></td>
													<%
													}else{
														%>
														
														<td style="b1ackground-color:#eeeee1;"></td>
													<%
													}
														
													if(percentage1!=0){
														%>
														<td style="b1ackground-color:#eeeee1;"><%=Utilities.getDisplayCurrencyFormatRounded(percentage1)%>%</td>
													<%
													}else{
														%>
														<td style="b1ackground-color:#eeeee1;"></td>
													<%
													}
													
													
													
													
																				
													
													
												}
												
												
												}
												
											}		
									
									
									
									}
									%>
									</tr>
									
									<%		
									
							}//end of TDM loop	
							}//end of RSM loop
						    }//end of SND loop	
							
		  // -- -------------------------------- -- //
							
							
							%>
							
							
							
							
							
						</tr>
						 
						 
					 </thead>
			</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%

s.executeUpdate("DROP TABLE IF EXISTS dist_targets_temp ");

s.executeUpdate("DROP TABLE IF EXISTS dist_targets_report_temp");

s5.close();
s4.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>