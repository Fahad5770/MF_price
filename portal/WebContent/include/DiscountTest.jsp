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
int FeatureID = 226;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();



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
String WhereHOD2 = "";
String WhereHOD3 = "";
String WhereHOD4 = "";
//if (HODIDs.length() > 0){
	WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in (2262,1804,2252)) ";	
	WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (2262,1804,2252)) ";	
	WhereHOD2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (2262,1804,2252)) ";
	WhereHOD3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (2262,1804,2252)) ";
	WhereHOD4 = " and ss.customer_kunnr in(SELECT distributor_id FROM common_distributors where snd_id in (2262,1804,2252)) ";
//}

/*
	WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in (1804)) ";	
	WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (1804)) ";	
	WhereHOD2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (1804)) ";
	WhereHOD3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (1804)) ";
	WhereHOD4 = " and ss.customer_kunnr in(SELECT distributor_id FROM common_distributors where snd_id in (1804)) ";

*/

//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
String WhereRSM1 = "";
String WhereRSM2 = "";
String WhereRSM3 = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
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
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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





long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";
String WhereCustomerID1 ="";
String WhereCustomerID2 ="";
String WhereCustomerID3 ="";
if (DistributorID != 0){
	WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorID+") ";
	WhereCustomerID2 = " and tab1.distributor_id in ("+DistributorID+") ";
	WhereCustomerID3 = " and distributor_id in ("+DistributorID+") ";

}else{
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID2 = " and tab1.distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID3 = " and distributor_id in ("+DistributorIDs+") ";
	
}


//System.out.println("Hello "+WhereCustomerID);



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



%>


									<%
									
									
									StartDate = Utilities.parseDateYYYYMMDDWithoutSeparator(request.getParameter("StartDate"));
									EndDate = Utilities.parseDateYYYYMMDDWithoutSeparator(request.getParameter("EndDate"));

									double SSalesConverted = 0;
									ResultSet rs17 = s3.executeQuery("select sum(((quty_quant * ipv.unit_per_sku) * ipv.liquid_in_ml)/6000) from sap_sales ss join inventory_products_view ipv on ss.material_matnr = ipv.sap_code where ss.created_on_erdat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and chkdis != 'portal' "+WhereHOD4);
									if (rs17.first()){
										//SSalesConverted += rs17.getDouble(1);
										//out.print("Hand Punch Sales: "+rs17.getDouble(1)+"<br>");
									}
									
									rs17 = s3.executeQuery("SELECT sum(isap.total_units)/ipv.unit_per_sku, sum(isap.total_units*ipv.liquid_in_ml)/6000 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isap.is_promotion = 0 "+WhereHOD3+WhereRSM3+WhereCustomerID3);
									if (rs17.first()){
										out.print("Theia Sales: "+rs17.getDouble(2)+"<br><br><hr>");
										SSalesConverted += rs17.getDouble(2);
									}
									
									
									Date CurrentDate = StartDate;
									double PerCaseSecondary = 0;
									double PerCasePrimary = 0;
									
									while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
										
										/*
										ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
												 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
												 " select ss.outlet_id, ipv.package_id, ipv.brand_id, sum(ss.quty_quant) qty, ( "+
												 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = ss.outlet_id "+
												 " union all "+
												 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = ss.outlet_id limit 1 "+
												 " ) discounted "+
												 " from sap_sales ss join inventory_products_view ipv on ss.material_matnr = ipv.sap_code where 1=1 "+WhereHOD4+" and ss.created_on_erdat between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDate(CurrentDate)+") group by ss.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
												 " ) tab1 group by package_id, brand_id "+
												 " ) tab2 ");
										if(rs16.first()){
											PerCaseSecondary +=rs16.getDouble(1);
										}
										*/
										
										ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
												 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
												 " select isa.outlet_id, ipv.package_id, ipv.brand_id, sum(isap.raw_cases) qty, ( "+
												 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
												 " union all "+
												 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
												 " ) discounted "+
												 " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+WhereHOD1+WhereRSM1+WhereCustomerID1+WhereGTMCategory1+" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
												 " ) tab1 group by package_id, brand_id "+
												 " ) tab2 ");
										if(rs16.first()){
											PerCaseSecondary +=rs16.getDouble(1);
										}
										
										CurrentDate = Utilities.getDateByDays(CurrentDate,1);
									}
									
									
									out.print("Discount:"+Utilities.getDisplayCurrencyFormat(PerCaseSecondary)+"<br>");
									out.print("Converted Cases:"+Utilities.getDisplayCurrencyFormat(SSalesConverted)+"<br>");
									out.print("Rate:"+(PerCaseSecondary/SSalesConverted)+"<br>");
									
							%>
							

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>