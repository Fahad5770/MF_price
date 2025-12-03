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
int FeatureID = 225;

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
if (HODIDs.length() > 0){
	WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD3 = " and ipprd.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
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
String WhereRSM2 = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
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
if (DistributorID != 0){
	//WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	//WhereCustomerID1 = " and isa.distributor_id in ("+DistributorID+") ";
	//WhereCustomerID2 = " and distributor_id in ("+DistributorID+") ";

}else{
	
	//WhereCustomerID = "";//" and customer_id in ("+DistributorIDs+") ";
	//WhereCustomerID1 = "";//" and isa.distributor_id in ("+DistributorIDs+") ";
	//WhereCustomerID2 = "";//" and distributor_id in ("+DistributorIDs+") ";
	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
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
	//WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID2 = " and distributor_id in ("+DistributorIDs+") ";
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




Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String RegionIDs = UserAccess.getRegionQueryString(RegionObj);

long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";
String WhereRegion1 = "";
String WhereRegion2 = "";
String WhereRegion3 = "";

if (RegionIDs.length() > 0){
	//WhereRegion = " and cd.region_id in ("+RegionIDs+") ";	
	WhereRegion = " and customer_id in(SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
	WhereRegion1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";
	WhereRegion2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";
	WhereRegion3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";
	
}


%>





<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Average Price</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs.getInt("packge_count")%>"><%=rs.getString("type_name") %></th>
								<%
							}
							
							%>
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>					
					    </tr>
					    
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
							<%
							int ArrayCount=0;
							
							ResultSet rs1 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs1.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+rs1.getInt("product_type_id")+WherePackage);
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
								}
							}
							
							//System.out.println(ArrayCount);
							
							double GrossRevenueArray[] = new double [ArrayCount];
							double SalesPromotionArray[] = new double [ArrayCount];
							double UpfrontDiscountArray[] = new double [ArrayCount];
							double PerCaseDiscount[] = new double [ArrayCount];
							double PrimaryPerCaseDiscount[] = new double [ArrayCount];
							double FixedDiscountArray[] = new double [ArrayCount];
							double FreightArray[] = new double [ArrayCount];
							double UnloadingArray[] = new double [ArrayCount];
							double CasesSoldArray[] = new double [ArrayCount];
							double NetRevenueArray[] = new double [ArrayCount];
							
							
							%>
							<th data-priority="1"  style="text-align:center; ">Total</th>					
					    </tr>
					    <tr>
					    	<td>Gross Revenue</td>
					    	
					    	<%
					    	double ConvertedGrossRevenue=0;
					    	int i=0;
							ResultSet rs3 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs3.next()){
								int TypeID = rs3.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereRegion+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
									}
									
									GrossRevenueArray[i]=GrossValue;
									ConvertedGrossRevenue += GrossValue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								i++;
								}
							}
							
							%>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedGrossRevenue) %></td>
					    	</tr>
					    	<tr><th colspan="<%=ArrayCount%>">Discounts</th></tr>
					    	<tr><td>Sales Promotion</td>
					    	
					    	<%
					    	double ConvertedSalesPromotion=0;
					    	int j=0;
							ResultSet rs6 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs6.next()){
								int TypeID = rs6.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									ResultSet rs5 = s3.executeQuery("select sum(free_stock) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereRegion+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
									}
									SalesPromotionArray[j]=GrossValue;
									ConvertedSalesPromotion+=GrossValue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								j++;
								}
							}
							
							%>
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedSalesPromotion) %></td>
					    	</tr>
					    	<tr><td>Upfront Discount</td>
					    	
					    	<%
					    	int jj=0;
					    	double ConvertedUpfront=0;
							ResultSet rs7 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs7.next()){
								int TypeID = rs7.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereRegion+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
									}
									UpfrontDiscountArray[jj]=GrossValue;
									ConvertedUpfront+=GrossValue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								jj++;
								}
							}
							
							%>
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedUpfront) %></td>
					    	</tr>
					    	<tr><td>Per Case Discount</td>
					    	<%
					    	double TotalPrimaryPerCaseDiscount = 0;
					    	int jji=0;
							ResultSet rs711 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs711.next()){
								int TypeID = rs711.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									//double PrimaryPerCaseDiscount = 0;
									Date CurrentDate = StartDate;
									
									while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
										
										ResultSet rs34 = s3.executeQuery("select sum(if(lifting_today>lifting_total,lifting_total,lifting_today) * percase_discount_rate) discount from ("+
													" select ipprd.distributor_id,ipprpl.package_id, ipprpl.lrb_type_id, ipprp.percase_discount_rate, ippr.valid_from, ippr.valid_to, ifnull(ipprp.quantity - ("+
														" select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between ippr.valid_from and ippr.valid_to and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id"+
													" ),0) lifting_total, ifnull(("+
														" select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id"+
													" ),0) lifting_today from inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id = ipprd.product_promotion_id join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprpl on ippr.id=ipprpl.id and ipprp.package_id = ipprpl.package_id where date("+Utilities.getSQLDate(CurrentDate)+") between ippr.valid_from and ippr.valid_to and ippr.is_active = 1 "+WhereHOD3+" and ipprpl.package_id = "+PackageID+" and ipprpl.lrb_type_id = "+TypeID+" having lifting_total > 0"+
													" ) tab1");
										if (rs34.first()){
											PrimaryPerCaseDiscount[jji] += rs34.getDouble(1);
										}
										
										CurrentDate = Utilities.getDateByDays(CurrentDate,1);
									}
									
									TotalPrimaryPerCaseDiscount += PrimaryPerCaseDiscount[jji];
									
									
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(PrimaryPerCaseDiscount[jji]) %></td>
							<%
								jji++;
								}
							}
							
							%>
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalPrimaryPerCaseDiscount) %></td>
					    	</tr>
					    	
					    	<tr><td>Retailer - Variable</td>
					    	
					    	<%
					    	int kkk=0;
					    	double PerCaseDiscountConverted=0;
							ResultSet rs81 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs81.next()){
								int TypeID = rs81.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									
									Date CurrentDate = StartDate;
									
									while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
										//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
										
										
										
										
										ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
												 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
														 " select isa.outlet_id, ipv.package_id, ipv.brand_id, "+
														 
												 " case "+ 
													" when ((ipv.package_id=1 OR ipv.package_id=3) AND ipv.lrb_type_id=1) AND (isa.region_id=4 OR isa.region_id=5 OR isa.region_id=8 OR isa.region_id=9) AND (isa.created_on between '2016-03-22' and '2016-09-30') then 0 "+
									                " when (ipv.package_id=16) AND (isa.region_id=5 OR isa.region_id=8) AND (isa.created_on between '2016-04-01' and '2016-09-30') then 0 "+
									                " when ((ipv.package_id=2 OR ipv.package_id=24) AND ipv.lrb_type_id=1) /* AND (isa.region_id=5 OR isa.region_id=8)*/ AND (isa.created_on between '2016-02-10' and '2016-11-30') then 0 "+
													" else SUM(isap.raw_cases) "+
									             " end as qty, "+ 
														" ( "+
														 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
														  " union all "+
														 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
														  " ) discounted "+
														  " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+WhereHOD1+WhereRSM1+WhereCustomerID1+WhereGTMCategory1+WhereRegion1+" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
														  " ) tab1 group by package_id, brand_id "+
														  " ) tab2 where package_id = "+PackageID+" and product_type_id = "+TypeID);
										
										if(rs16.first()){
											GrossValue +=rs16.getDouble(1);
										}
										
										
										
										CurrentDate = Utilities.getDateByDays(CurrentDate,1);
									}
									
									
									
									PerCaseDiscount[kkk]=GrossValue;
									
									PerCaseDiscountConverted+=GrossValue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								kkk++;
								}
							}
							
							%>
					    	
					    	
					    	
					    	
					    	
					    	
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(PerCaseDiscountConverted) %></td></tr>
					    	
					    	<tr><td>Retailer - Fixed</td>
					    	
					    	
					    	<%
					    	int jjlm=0;
					    	double ConvertedFixed=0;
					    	double ConvertedFixed1=0;
							ResultSet rs712 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs712.next()){
								int TypeID = rs712.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									double GrossValue1 = 0;
									ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereGTMCategory+WhereRegion+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
									}
									
									
									
									
							%>
								<td style="text-align: right;">&nbsp;</td>
							<%
								jjlm++;
								}
							}
							
							
							Date CurrentDate = StartDate;
							
							while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
								//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
								
								/*
								System.out.println("select sum(fixed_company_share)/30 from ( "+
										" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0  "+
										" ) tab1 where 1=1 "+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory2);
								*/
								
								ResultSet rs16 = s3.executeQuery("select sum(fixed_company_share)/30 from ( "+
										" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0  "+
										" ) tab1 where 1=1 "+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory2+WhereRegion2);
								
								if(rs16.first()){
									ConvertedFixed +=rs16.getDouble(1);
								}
								
								
								
								CurrentDate = Utilities.getDateByDays(CurrentDate,1);
							}
							
							//System.out.println("Helllllo - "+ConvertedFixed);
							
							
							
							
							//System.out.println("Hello "+ConvertedFixed1);
							
							
							%>
					    	<td style="text-align: right;"><%if(ConvertedFixed!=0 && ConvertedFixed>=1){%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedFixed) %><%} %></td>
					    	</tr>
					    	
					    	
					    	
					    	<tr><th colspan="<%=ArrayCount%>">Other Costs</th></tr>
					    	<tr><td>Freight</td>
					    	
					    	<%
					    	int kk=0;
					    	double FreightConverted=0;
							ResultSet rs8 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs8.next()){
								int TypeID = rs8.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									ResultSet rs5 = s3.executeQuery("select sum(freight)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereRegion+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
									}
									FreightArray[kk]=GrossValue;
									
									FreightConverted+=GrossValue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								kk++;
								}
							}
							
							%>
					    	
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(FreightConverted) %></td>
					    	</tr>
					    	<tr><td>Unloading</td>
					    	
					    	<%
					    	int ll=0;
					    	double UnloadingConverted=0;
							ResultSet rs9 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs9.next()){
								int TypeID = rs9.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									ResultSet rs5 = s3.executeQuery("select sum(unloading)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereRegion+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
									}
									UnloadingArray[ll]=GrossValue;
											
									UnloadingConverted+=GrossValue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								ll++;
								}
							}
							
							%>
					    	
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(UnloadingConverted) %></td>
					    	</tr>
					    	
					    	<tr><td>Haulage</td>
					    	
					    	<%
					    	int jjl=0;
					    	double ConvertedHaulase=0;
							ResultSet rs71 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs71.next()){
								int TypeID = rs71.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									
							%>
								<td style="text-align: right;">&nbsp</td>
							<%
								jj++;
								}
							}
							
							ResultSet rs72 = s.executeQuery("SELECT sum(freight_amount) FROM inventory_delivery_note where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory3+WhereRegion3);
							if(rs72.first()){
								ConvertedHaulase = rs72.getDouble(1);
							}
							%>
					    	<td style="text-align: right;"><%if(ConvertedHaulase!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedHaulase) %><%} %></td>
					    	</tr>
					    	
					    	
					    	<tr><th>Cases Sold</th>
					    	
					    	
					    	
					    	
					    	<%
					    	int lm=0;
					    	double CasesSoldConverted=0;
							ResultSet rs10 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs10.next()){
								int TypeID = rs10.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									double GrossValue = 0;
									double CCGross = 0;
									ResultSet rs5 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) cc from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where 1=1 "+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+WhereRegion+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bppi.package_id="+PackageID+" and bppi.product_type_id="+TypeID);
									if (rs5.first()){
										GrossValue = rs5.getDouble(1);
										CCGross = rs5.getDouble(2);
									}
									CasesSoldArray[lm]=GrossValue;
									CasesSoldConverted+=CCGross;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(GrossValue) %></td>
							<%
								lm++;
								}
							}
							
							%>
					    	
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(CasesSoldConverted) %></td>
					    	</tr>
					    	<tr><th>Net Revenue</th>
					    	<%
					    	int km=0;
					    	
					    	double NetRevenueConvert=0;
					    	for(int i1=0;i1<ArrayCount;i1++){
					    		double NetRevenue=0;
					    		NetRevenue = GrossRevenueArray[i1] - (SalesPromotionArray[i1]+UpfrontDiscountArray[i1]+FreightArray[i1]+UnloadingArray[i1]+PerCaseDiscount[i1]+PrimaryPerCaseDiscount[i1]);
					    	
									
					    		NetRevenueArray[km]=NetRevenue;	
					    		NetRevenueConvert+=NetRevenue;
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(NetRevenue) %></td>
							<%
							km++;
					    	}	
							
					    	NetRevenueConvert = NetRevenueConvert-(ConvertedFixed+ConvertedHaulase);
							%>
					    	
					    	
					    	
					    	<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(NetRevenueConvert) %></td></tr>
					    	<tr><th>Rate</th>
					    	<%
					    	
					    	for(int i1=0;i1<ArrayCount;i1++){
					    	
					    		
					    		double Rate=0;
					    		
					    		if(CasesSoldArray[i1]!=0){
					    			Rate = NetRevenueArray[i1]/CasesSoldArray[i1];
					    		}
					    		
					    	
									
									 
							%>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(Rate) %></td>
							<%
							}	
							
							
							%>
					    	
					    	
					    	<td style="text-align: right;"><%if(CasesSoldConverted!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(NetRevenueConvert/CasesSoldConverted) %><%} %></td></tr>
					    </tr>
					    
					  </thead> 
					<tbody>
						<%
						
						
						
						
						%>
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>