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
int FeatureID = 354;

//if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
//}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();

Statement s7 = c.createStatement();
Statement s8 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


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

long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
	for(int i = 0; i < SelectedBrandsArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandsArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandsArray[i]+"";
		}
	}
	WhereBrand = " and brand_id in ("+BrandIDs+") ";
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
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
	WhereDistributors = " and co.distributor_id in ("+DistributorIDs+") ";
	
}




//OrderBooker

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWhere="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWhere =" and order_no in (select mobile_order_no from mobile_order_unedited mou where mou.created_by in ("+OrderBookerIDs+"))";
}



//outlet
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and isa.outlet_id in ("+OutletIds+") ";	
	//System.out.println(WhereOutlets);
}

//Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
//String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Sales Summary (Detailed)</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					 	<%
					 //	WherePJP=" and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in(974))";
					 	if(WherePJP!="" && WherePJP !=null){
					 	%>
					 	
					 	<tr style="font-size:11px;">
							<th  data-priority="1"  style="text-align:center; ">Outlet</th>
							<th  data-priority="1"  style="text-align:center; ">Channel</th>							
							<th  data-priority="1"  style="text-align:center; ">Order Booker</th>
							<th  data-priority="1"  style="text-align:center; ">Date</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">500GM</th>
							<th   data-priority="1"  style="text-align:center; "  colspan="5">250GM</th>
							<th   data-priority="1"  style="text-align:center; " >25GM</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">1 KG</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">2 KG</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">5 KG</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">70 G</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">140 G</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Jar</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Box</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Box 450G</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">RBP 450G</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">RBP Jar</th>
							
							<th  data-priority="1"  style="text-align:center; " colspan="1">EID GIFT PACK </th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">500 G </th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Jar 300 Fennel </th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Box 450G Fennel</th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Box 1005G Fennel</th>
							<th  data-priority="1"  style="text-align:center; " colspan="3">Wrapped Gurr Jar</th>
							<th  data-priority="1"  style="text-align:center; " colspan="3">Gur Box 450G </th>
							<th  data-priority="1"  style="text-align:center; " colspan="1">Box 425G </th>
							<th   data-priority="1"  style="text-align:center; ">Converted Sales</th>
							<th   data-priority="1"  style="text-align:center; ">Amount</th>
							
					    </tr>
					   
					  </thead> 
					<tbody>
						<tr style="font-size:11px;">
							<th  data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th  data-priority="1"  style="text-align:center; ">&nbsp;</th>							
							<th  data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th  data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th  data-priority="1"  style="text-align:center; " >BEETA Original</th>
							<!-- <th  data-priority="1"  style="text-align:center; " >BEETA Strawberry</th>
							<th  data-priority="1"  style="text-align:center; " >BEETA Choco Hazelnut</th> -->
							<th   data-priority="1"  style="text-align:center; "  >BEETA Original</th>
							<th   data-priority="1"  style="text-align:center; "  >BEETA Strawberry</th>
							<th   data-priority="1"  style="text-align:center; "  >BEETA Choco Hazelnut</th>
							<th   data-priority="1"  style="text-align:center; "  >BEETA Imli</th>
							<th   data-priority="1"  style="text-align:center; "  >BEETA Mango</th>
							<th   data-priority="1"  style="text-align:center; " >BEETA Original</th>
							<th   data-priority="1"  style="text-align:center; " >Sugar</th>
							<th   data-priority="1"  style="text-align:center; " >Sugar</th>
							<th   data-priority="1"  style="text-align:center; " >Sugar</th>
							<th   data-priority="1"  style="text-align:center; " >Sugar</th>
							<th   data-priority="1"  style="text-align:center; " >Sugar</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Shakkar</th>
							<th   data-priority="1"  style="text-align:center; ">Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; " >Gur</th>
							<th   data-priority="1"  style="text-align:center; ">Orange peal</th>
							<th   data-priority="1"  style="text-align:center; " >Ginger</th>
							<th   data-priority="1"  style="text-align:center; " >Coconut</th>
							<th   data-priority="1"  style="text-align:center; ">Orange peal</th>
							<th   data-priority="1"  style="text-align:center; " >Ginger</th>
							<th   data-priority="1"  style="text-align:center; " >Coconut</th>
							<th   data-priority="1"  style="text-align:center; " >Sugar</th>
							<th   data-priority="1"  style="text-align:center; "></th>
							<th   data-priority="1"  style="text-align:center; "></th>							
						</tr>
						<%
						
						
						
						long OutletID=0;
						String OutletName="";
						long PutOutletSeparator=0;
						String TrStyle="";
						double TotalArray[]=new double[31];
						String Channel="";
						int TotalUnitPerSKU70G=0;
						int TotalUnitPerSKU140G=0;
						
					//	System.out.println("Select co.id,co.name from common_outlets co where co.id in (select isa.outlet_id from inventory_sales_adjusted isa where isa.outlet_id=co.id and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereOutlets+WherePJP+")" );
						ResultSet rs=s.executeQuery("Select co.id,co.name,co.pic_channel_id,(select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id) channel, (select contact_nic from common_outlets_contacts coc where coc.outlet_id=co.id) outlet_cnic from common_outlets co where co.id in (select isa.outlet_id from inventory_sales_adjusted isa where isa.outlet_id=co.id and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereOutlets+WherePJP+" )" );
						while(rs.next()){
							OutletID=rs.getLong("id");
							
							OutletName=rs.getString("name");
							Date CurrentDate = new Date();
							
							String OutletCNIC = rs.getString("outlet_cnic");
							if(OutletCNIC==null){
								OutletCNIC="";
							}
							
							String OrderBooker="";
							
							Channel=rs.getString("channel");
						       
						       if(Channel==null){
						        Channel="";
						       }


						
									
									ResultSet rs212=s5.executeQuery("SELECT assigned_to,(SELECT DISPLAY_NAME FROM users where id=assigned_to) orderbooker_name  FROM pep.distributor_beat_plan_users where id in (SELECT id FROM pep.distributor_beat_plan_schedule where outlet_id="+OutletID+")");
									if(rs212.first()){
										OrderBooker =rs212.getString("assigned_to")+"-"+rs212.getString("orderbooker_name") ;
									}
						
									
								// System.out.println("SELECT distinct created_on FROM pep.inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and outlet_id="+OutletID);
									ResultSet rs34 = s3.executeQuery("SELECT distinct date(created_on) created_on,booked_by FROM pep.inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and outlet_id="+OutletID);
									while(rs34.next()){
												
										CurrentDate = rs34.getDate("created_on");
										
										double Units_250G=0;
										double Units_250G1=0;
										double Units_250G2=0;
										double Units_250G3=0;
										double Units_250G4=0;
										
										
										
										double Units_500G=0;
										double Units_500G1=0;
										double Units_500G2=0;
										
										double Units_25G=0;
										
										double Units_1KG=0;
										
										double Units_2KG=0;
										
										double Units_5KG=0;
										
										long Units_70G=0;
										long Units_140G=0;
										
										double Unit_Jar_Gur=0;
										double Unit_Box_Gur=0;
										double Unit_Box450G_Gur=0;
										long Unit_Ramzan450G_Gur=0;
										long Unit_Ramzan300G_Gur=0;
										
										//Added by awais 
										long EID_GIFT_PACK=0;
										long Unit_500G_Shakkar=0;
										long Unit_Jar_300_Fennel_Gur=0;
										long Unit_Box_450G_Fennel_Gur=0;
										long Unit_Box_1005G_Fennel_Gur=0;
										
										long WrappedGurrJarOrange=0;
										long WrappedGurrJarGinger=0;
										long WrappedGurrJarCoconut=0;
										
										long GurBox450Orange=0;
										long GurBox450Ginger=0;
										long GurBox450Coconut=0;
										long GurBox425G=0;
										//closed 
										double ConvertedUnits500GM=0;
										double ConvertedUnits500GM1=0;
										double ConvertedUnits500GM2=0;
										
										
										double ConvertedUnits250GM=0;
										double ConvertedUnits250GM1=0;
										double ConvertedUnits250GM2=0;
										double ConvertedUnits250GM3=0;
										double ConvertedUnits250GM4=0;
										
										double ConvertedUnits25GM=0;
										
										double ConvertedUnits1KGM=0;										
										double ConvertedUnits2KGM=0;										
										double ConvertedUnits5KGM=0;
										double ConvertedUnits70G=0;
										double ConvertedUnits140G=0;
										
										double ConvertedUnitsJarGur=0;
										double ConvertedUnitsBoxGur=0;
										double ConvertedUnitBox450GGur=0;
										double ConvertedUnit_Ramzan450G_Gur=0;										
										double ConvertedUnit_Ramzan300G_Gur=0;
										
										//added by awais 
										
										double ConvertedUnit_EID_GIFT_PACK=0;
										double ConvertedUnit_Unit_500G_Shakkar=0;
										
										double ConvertedUnit_Jar_300_Fennel_Gur=0;
										double ConvertedUnit_450G_Fennel_Gurr=0;
										double ConvertedUnit_Box_1005G_Fennel_Gur=0;
										
										
										
										double ConvertedWrappedGurrJarOrange=0;
										double ConvertedWrappedGurrJarGinger=0;
										double ConvertedWrappedGurrJarCoconut=0;
										
										double ConvertedGurBox450Orange=0;
										double ConvertedGurBox450Ginger=0;
										double ConvertedGurBox450Coconut=0;
										double ConvertedGurBox425G=0;
										//closed
										double Converted=0;
										
										 TotalUnitPerSKU70G=0;
										 TotalUnitPerSKU140G=0;
										
										ResultSet rs21=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1)  order by isa.id desc");
										if(rs21.first()){
											Units_250G = rs21.getDouble(1);
										}
										
										
										ResultSet rs211=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (4)  order by isa.id desc");
										if(rs211.first()){
											Units_250G1 = rs211.getDouble(1);
										}
										
										
										ResultSet rs213=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (5)  order by isa.id desc");
										if(rs213.first()){
											Units_250G2 = rs213.getDouble(1);
										}
										
										
										
										
										ResultSet rs2133=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (8)  order by isa.id desc");
										if(rs2133.first()){
											Units_250G3 = rs2133.getDouble(1);
										}
										
										ResultSet rs21331=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (9)  order by isa.id desc");
										if(rs21331.first()){
											Units_250G4 = rs21331.getDouble(1);
										}
										
										
										//ResultSet rs22=s2.executeQuery("SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletIds+" and isap.product_id=2 order by isa.id desc");
										ResultSet rs221=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (2) order by isa.id desc");
										if(rs221.first()){
											Units_500G = rs221.getDouble(1);
										}
										
										
										ResultSet rs222=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (6) order by isa.id desc");
										if(rs222.first()){
											Units_500G1 = rs222.getDouble(1);
										}
										
										ResultSet rs223=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (7) order by isa.id desc");
										if(rs223.first()){
											Units_500G2 = rs223.getDouble(1);
										}
										
										
										//ResultSet rs23=s2.executeQuery("SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletIds+" and isap.product_id=3 order by isa.id desc");
										
										// divide by 24 bec they count 24 pieces to 1 hanger
										
										ResultSet rs23=s2.executeQuery("SELECT sum(total_units/24) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
										if(rs23.first()){
											Units_25G = rs23.getDouble(1);
										}
										
										ResultSet rs24=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=10 order by isa.id desc");
										if(rs24.first()){
											Units_1KG = rs24.getDouble(1);
										}
										
										ResultSet rs25=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=11 order by isa.id desc");
										if(rs25.first()){
											Units_2KG = rs25.getDouble(1);
										}
										
										ResultSet rs26=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=12 order by isa.id desc");
										if(rs26.first()){
											Units_5KG = rs26.getDouble(1);
										}
										
										
										
										
										ResultSet rs27=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=13 order by isa.id desc");
										if(rs27.first()){
											Unit_Jar_Gur = rs27.getDouble(1);
										}
										
										
										ResultSet rs28=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=14 order by isa.id desc");
										if(rs28.first()){
											Unit_Box_Gur = rs28.getDouble(1);
										}
										
										ResultSet rs32=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=17 order by isa.id desc");
										if(rs32.first()){
											Unit_Box450G_Gur = rs32.getDouble(1);
										}
										
										int UnitPerSKU70G=0;
										String RawCases70G="";
										int UnitPerSKU140G=0;
										String RawCases140G="";
										int UnitPerSKURamzan450=0;
										String RawCases450="";
										int UnitPerSKURamzanJar=0;
										String RawCasesJar="";
										
										ResultSet rs29=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=15 order by isa.id desc");
										if(rs29.first()){
											Units_70G = rs29.getLong(1);
											UnitPerSKU70G = rs29.getInt(2);
											TotalUnitPerSKU70G=UnitPerSKU70G;
										}
										
										RawCases70G = Utilities.convertToRawCases(Units_70G, UnitPerSKU70G);
										
										ResultSet rs30=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=16 order by isa.id desc");
										if(rs30.first()){
											Units_140G = rs30.getLong(1);
											UnitPerSKU140G = rs30.getInt(2);
											TotalUnitPerSKU140G=UnitPerSKU140G;
										}
										
										RawCases140G = Utilities.convertToRawCases(Units_140G, UnitPerSKU140G);
										
									
										
										ResultSet rs31=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=21 order by isa.id desc");
										if(rs31.first()){
											Unit_Ramzan450G_Gur = rs31.getLong(1);
											UnitPerSKURamzan450 = rs31.getInt(2);
											//TotalUnitPerSKU140G=UnitPerSKURamzan450;
										}
										
										RawCases450 = Utilities.convertToRawCases(Unit_Ramzan450G_Gur, UnitPerSKURamzan450);
										
										ResultSet rs321=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=22 order by isa.id desc");
										if(rs321.first()){
											Unit_Ramzan300G_Gur = rs321.getLong(1);
											UnitPerSKURamzanJar = rs321.getInt(2);
											//TotalUnitPerSKU140G=UnitPerSKURamzan450;
										}
										
										RawCasesJar = Utilities.convertToRawCases(Unit_Ramzan300G_Gur, UnitPerSKURamzanJar);
										
										///Total Amount
										
										//System.out.println("SELECT sum(isap.net_amount) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1,2,3,4,5,6,7,8) order by isa.id desc");
										
										
										//added by awais 
										//System.out.println("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=23 order by isa.id desc");

										ResultSet rs323=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=23 order by isa.id desc");
										if(rs323.first()){
											Unit_500G_Shakkar = rs323.getLong(1);																				
										}
										
										ResultSet rs322=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=24 order by isa.id desc");
										if(rs322.first()){
											EID_GIFT_PACK = rs322.getLong(1);																				
										}
										
										ResultSet rs324=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=18 order by isa.id desc");
										if(rs324.first()){
											Unit_Jar_300_Fennel_Gur = rs324.getLong(1);																				
										}
										
										ResultSet rs325=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=19 order by isa.id desc");
										if(rs325.first()){
											Unit_Box_450G_Fennel_Gur = rs325.getLong(1);																				
										}
										
										ResultSet rs326=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=20 order by isa.id desc");
										if(rs326.first()){
											Unit_Box_1005G_Fennel_Gur = rs326.getLong(1);																				
										}
										
								
										ResultSet rs327=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=25 order by isa.id desc");
										if(rs327.first()){
											WrappedGurrJarOrange = rs327.getLong(1);																				
										}
										ResultSet rs328=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=26 order by isa.id desc");
										if(rs328.first()){
											WrappedGurrJarGinger = rs328.getLong(1);																				
										}
										ResultSet rs329=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=27 order by isa.id desc");
										if(rs329.first()){
											WrappedGurrJarCoconut = rs329.getLong(1);																				
										}

										
										ResultSet rs330=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=28 order by isa.id desc");
										if(rs330.first()){
											GurBox450Orange = rs330.getLong(1);																				
										}
										ResultSet rs331=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=29 order by isa.id desc");
										if(rs331.first()){
											GurBox450Ginger = rs331.getLong(1);																				
										}
										ResultSet rs332=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=30 order by isa.id desc");
										if(rs332.first()){
											GurBox450Coconut = rs332.getLong(1);																				
										}
										
										ResultSet rs333=s2.executeQuery("SELECT sum(total_units), ipv.unit_per_sku FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=31 order by isa.id desc");
										if(rs333.first()){
											GurBox425G = rs333.getLong(1);																				
										}
										
										
										//closed
										double TotalAmount=0;
										ResultSet rs2231=s2.executeQuery("SELECT sum(isap.net_amount) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31) order by isa.id desc");
										if(rs2231.first()){
											TotalAmount = rs2231.getDouble(1);
										}
										
										
										
										//////////////////////////////////////////
										
										
										
										//Converted
										
										//ResultSet rs231=s2.executeQuery("SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletIds+" order by isa.id desc");
										///ResultSet rs231=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletIds+" order by isa.id desc");
										//if(rs231.first()){
										//	ConvertedUnits = rs231.getLong(1);
										//}
										
										ConvertedUnits500GM = (500*Units_500G)/3000;
										ConvertedUnits500GM1 = (500*Units_500G1)/3000;
										ConvertedUnits500GM2 = (500*Units_500G2)/3000;
										
										
										
										ConvertedUnits250GM = (250*Units_250G)/3000;
										ConvertedUnits250GM1 = (250*Units_250G1)/3000;
										ConvertedUnits250GM2 = (250*Units_250G2)/3000;
										ConvertedUnits250GM3 = (250*Units_250G3)/3000;
										ConvertedUnits250GM4 = (250*Units_250G4)/3000;
										
										//System.out.println("hello "+ConvertedUnits250GM);
										
										ConvertedUnits25GM = (25*24*Units_25G)/3000;
										
										ConvertedUnits1KGM = Units_1KG;
										
										ConvertedUnits2KGM = Units_2KG;
										
										ConvertedUnits5KGM = Units_5KG;
										
										ConvertedUnitsJarGur = Unit_Jar_Gur;
										ConvertedUnitsBoxGur = Unit_Box_Gur;
										
										ConvertedUnitBox450GGur=Unit_Box450G_Gur;
										
										ConvertedUnits70G=Units_70G;
										ConvertedUnits140G=Units_140G;
										
										ConvertedUnit_Ramzan450G_Gur=Unit_Ramzan450G_Gur;
										ConvertedUnit_Ramzan300G_Gur=Unit_Ramzan300G_Gur;
										
										//added by awais 
										ConvertedUnit_EID_GIFT_PACK=EID_GIFT_PACK;
										ConvertedUnit_Unit_500G_Shakkar=Unit_500G_Shakkar;
										
										ConvertedUnit_Jar_300_Fennel_Gur=Unit_Jar_300_Fennel_Gur;
										ConvertedUnit_450G_Fennel_Gurr=Unit_Box_450G_Fennel_Gur;
										ConvertedUnit_Box_1005G_Fennel_Gur=Unit_Box_1005G_Fennel_Gur;
								
										 ConvertedWrappedGurrJarOrange=WrappedGurrJarOrange;
										 ConvertedWrappedGurrJarGinger=WrappedGurrJarGinger;
										 ConvertedWrappedGurrJarCoconut=WrappedGurrJarCoconut;
										
										 ConvertedGurBox450Orange=GurBox450Orange;
										 ConvertedGurBox450Ginger=GurBox450Ginger;
										 ConvertedGurBox450Coconut=GurBox450Coconut;
										 ConvertedGurBox425G=GurBox425G;
										
										//closed
										
										Converted = ConvertedUnits500GM+ ConvertedUnits500GM1+ ConvertedUnits500GM2+ConvertedUnits250GM+ConvertedUnits250GM1+ConvertedUnits250GM2+ConvertedUnits250GM3+ConvertedUnits250GM4+ConvertedUnits25GM+ConvertedUnits1KGM+ConvertedUnits2KGM+ConvertedUnits5KGM+ConvertedUnits70G+ConvertedUnits140G+ConvertedUnitBox450GGur+ConvertedUnit_Ramzan450G_Gur+ConvertedUnit_Ramzan300G_Gur+ConvertedUnit_EID_GIFT_PACK+ConvertedUnit_Unit_500G_Shakkar+ConvertedUnit_Jar_300_Fennel_Gur+ConvertedUnit_450G_Fennel_Gurr+ConvertedUnit_Box_1005G_Fennel_Gur+ConvertedWrappedGurrJarOrange+ConvertedWrappedGurrJarGinger+ConvertedWrappedGurrJarCoconut+ConvertedGurBox450Orange+ConvertedGurBox450Ginger+ConvertedGurBox450Coconut+ConvertedGurBox425G ;
									

									TotalArray[0]+=Units_500G;
									//TotalArray[1]+=Units_500G1;
									//TotalArray[2]+=Units_500G2;
									
									TotalArray[1]+=Units_250G;
									TotalArray[2]+=Units_250G1;
									TotalArray[3]+=Units_250G2;
									TotalArray[4]+=Units_250G3;
									TotalArray[5]+=Units_250G4;
									
									TotalArray[6]+=Units_25G;
									
									TotalArray[7]+=Units_1KG;
									
									TotalArray[8]+=Units_2KG;
									
									TotalArray[9]+=Units_5KG;
									
									TotalArray[10]+=(Units_70G);
									TotalArray[11]+=(Units_140G);
									
									TotalArray[12]+=Unit_Jar_Gur;
									TotalArray[13]+=Unit_Box_Gur;
									TotalArray[14]+=Unit_Box450G_Gur;
									TotalArray[15]+=Unit_Ramzan450G_Gur;
									TotalArray[16]+=Unit_Ramzan300G_Gur;
									TotalArray[17]+=EID_GIFT_PACK;
									TotalArray[18]+=Unit_500G_Shakkar;
									
										//System.out.println("Outlet "+" Shakkar case "+Unit_500G_Shakkar);
									
								
									TotalArray[19]+=Unit_Jar_300_Fennel_Gur;
									TotalArray[20]+=Unit_Box_450G_Fennel_Gur;
									TotalArray[21]+=Unit_Box_1005G_Fennel_Gur;
									
							
									
									TotalArray[22]+=WrappedGurrJarOrange;
									TotalArray[23]+=WrappedGurrJarGinger;
									TotalArray[24]+=WrappedGurrJarCoconut;
									
									
								
									TotalArray[25]+=GurBox450Orange;
									TotalArray[26]+=GurBox450Ginger;
									TotalArray[27]+=GurBox450Coconut;
									TotalArray[28]+=GurBox425G;
									
									TotalArray[29]+=Converted;
									TotalArray[30]+=TotalAmount;
									
									
									
									if(Units_500G!=0 || Units_250G!=0 || Units_25G!=0 || Units_500G1!=0 || Units_250G1!=0 || Units_500G2!=0 || Units_250G2!=0 ||  Units_250G3!=0 || Units_250G4!=0 || Units_1KG != 0 || Units_2KG !=0 || Units_5KG !=0 || Unit_Jar_Gur!=0 || Unit_Box_Gur!=0 || Units_70G!=0 || Units_140G!=0 || Unit_Box450G_Gur!=0 || Unit_Ramzan450G_Gur!=0 || Unit_Ramzan300G_Gur!=0 || Unit_500G_Shakkar!=0 || EID_GIFT_PACK!=0 || Unit_Jar_300_Fennel_Gur!=0 || Unit_Box_450G_Fennel_Gur!=0 || Unit_Box_1005G_Fennel_Gur!=0 || WrappedGurrJarOrange!=0 || WrappedGurrJarGinger!=0 || WrappedGurrJarCoconut!=0 || GurBox450Orange!=0 || GurBox450Ginger!=0 || GurBox450Coconut!=0 || GurBox425G!=0){
									
										if(OutletID!=PutOutletSeparator){
											
											PutOutletSeparator=OutletID;
											 //System.out.println("Outlet "+" Shakkar case "+Unit_500G_Shakkar);
											 %>
											
										<tr style="font-size:11px;background-color:#E8E8E8">
											<td Style="color:#E8E8E8" colspan="28"></td>
										</tr>
										<tr style="font-size:11px;">
											
											<td style="text-align:left;"><%=OutletID%> - <%=OutletName%> <%if(!OutletCNIC.equals("")){ %> (<%=OutletCNIC %>) <%} %></td>
											<td style="text-align:left;"><%=Channel%></td>
											<td style="text-align:left;"><%=OrderBooker%></td>
											<td style="text-align:left;"><%=Utilities.getDisplayDateFormat(CurrentDate) %></td>
											<td style="text-align:center;"><%if(Units_500G!=0){%><%=Units_500G %><%} %></td>
											<!-- <td style="text-align:center;"><%if(Units_500G1!=0){%><%=Units_500G1 %><%} %></td>
											<td style="text-align:center;"><%if(Units_500G2!=0){%><%=Units_500G2 %><%} %></td> -->
											<td style="text-align:center;"><%if(Units_250G!=0){%><%=Units_250G %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G1!=0){%><%=Units_250G1 %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G2!=0){%><%=Units_250G2 %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G3!=0){%><%=Units_250G3 %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G4!=0){%><%=Units_250G4 %><%} %></td>
											<td style="text-align:center;"><%if(Units_25G!=0){%><%=Units_25G %><%} %></td>
											<td style="text-align:center;"><%if(Units_1KG!=0){%><%=Units_1KG %><%} %></td>
											<td style="text-align:center;"><%if(Units_2KG!=0){%><%=Units_2KG %><%} %></td>
											<td style="text-align:center;"><%if(Units_5KG!=0){%><%=Units_5KG %><%} %></td>
											<td style="text-align:center;"><%if(Units_70G!=0){%><%=RawCases70G %><%} %></td>
											<td style="text-align:center;"><%if(Units_140G!=0){%><%=RawCases140G %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Jar_Gur!=0){%><%=Unit_Jar_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box_Gur!=0){%><%=Unit_Box_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box450G_Gur!=0){%><%=Unit_Box450G_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Ramzan450G_Gur!=0){%><%=Unit_Ramzan450G_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Ramzan300G_Gur!=0){%><%=Unit_Ramzan300G_Gur %><%} %></td>
											<td style="text-align:center;"><%if(EID_GIFT_PACK!=0){%><%=EID_GIFT_PACK %><%} %></td>
											<td style="text-align:center;"><%if(Unit_500G_Shakkar!=0){%><%=Unit_500G_Shakkar %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Jar_300_Fennel_Gur!=0){%><%=Unit_Jar_300_Fennel_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box_450G_Fennel_Gur!=0){%><%=Unit_Box_450G_Fennel_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box_1005G_Fennel_Gur!=0){%><%=Unit_Box_1005G_Fennel_Gur %><%} %></td>
											
											
											<td style="text-align:center;"><%if(WrappedGurrJarOrange!=0){%><%=WrappedGurrJarOrange %><%} %></td>
											<td style="text-align:center;"><%if(ConvertedWrappedGurrJarGinger!=0){%><%=ConvertedWrappedGurrJarGinger %><%} %></td>
											<td style="text-align:center;"><%if(ConvertedWrappedGurrJarCoconut!=0){%><%=ConvertedWrappedGurrJarCoconut %><%} %></td>
											
											<td style="text-align:center;"><%if(GurBox450Orange!=0){%><%=GurBox450Orange %><%} %></td>
											<td style="text-align:center;"><%if(GurBox450Ginger!=0){%><%=GurBox450Ginger %><%} %></td>
											<td style="text-align:center;"><%if(GurBox450Coconut!=0){%><%=GurBox450Coconut %><%} %></td>
											<td style="text-align:center;"><%if(GurBox425G!=0){%><%=GurBox425G %><%} %></td>
											
											<td style="text-align:center;"><%if(Converted!=0){%><%=Converted %><%} %></td>
											<td style="text-align:center;"><%if(TotalAmount!=0){%><%=TotalAmount %><%} %></td>
										</tr> 
											 
										
											<%
										}else{
											
										%>	
										
											<tr style="font-size:11px;">
											
											<td style="text-align:left;"><%=OutletID%> - <%=OutletName%><%if(!OutletCNIC.equals("")){ %> (<%=OutletCNIC %>) <%} %></td>
											<td style="text-align:left;"><%=Channel%></td>
											<td style="text-align:left;"><%=OrderBooker%></td>
											<td style="text-align:left;"><%=Utilities.getDisplayDateFormat(CurrentDate) %></td>
											<td style="text-align:center;"><%if(Units_500G!=0){%><%=Units_500G %><%} %></td>
											<!-- <td style="text-align:center;"><%if(Units_500G1!=0){%><%=Units_500G1 %><%} %></td>
											<td style="text-align:center;"><%if(Units_500G2!=0){%><%=Units_500G2 %><%} %></td> -->
											<td style="text-align:center;"><%if(Units_250G!=0){%><%=Units_250G %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G1!=0){%><%=Units_250G1 %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G2!=0){%><%=Units_250G2 %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G3!=0){%><%=Units_250G3 %><%} %></td>
											<td style="text-align:center;"><%if(Units_250G4!=0){%><%=Units_250G4 %><%} %></td>
											<td style="text-align:center;"><%if(Units_25G!=0){%><%=Units_25G %><%} %></td>
											<td style="text-align:center;"><%if(Units_1KG!=0){%><%=Units_1KG %><%} %></td>
											<td style="text-align:center;"><%if(Units_2KG!=0){%><%=Units_2KG %><%} %></td>
											<td style="text-align:center;"><%if(Units_5KG!=0){%><%=Units_5KG %><%} %></td>
											<td style="text-align:center;"><%if(Units_70G!=0){%><%=RawCases70G %><%} %></td>
											<td style="text-align:center;"><%if(Units_140G!=0){%><%=RawCases140G %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Jar_Gur!=0){%><%=Unit_Jar_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box_Gur!=0){%><%=Unit_Box_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box450G_Gur!=0){%><%=Unit_Box450G_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Ramzan450G_Gur!=0){%><%=Unit_Ramzan450G_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Ramzan300G_Gur!=0){%><%=Unit_Ramzan300G_Gur %><%} %></td>											
											<td style="text-align:center;"><%if(EID_GIFT_PACK!=0){%><%=EID_GIFT_PACK %><%} %></td>
											<td style="text-align:center;"><%if(Unit_500G_Shakkar!=0){%><%=Unit_500G_Shakkar %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Jar_300_Fennel_Gur!=0){%><%=Unit_Jar_300_Fennel_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box_450G_Fennel_Gur!=0){%><%=Unit_Box_450G_Fennel_Gur %><%} %></td>
											<td style="text-align:center;"><%if(Unit_Box_1005G_Fennel_Gur!=0){%><%=Unit_Box_1005G_Fennel_Gur %><%} %></td>
											
											<td style="text-align:center;"><%if(WrappedGurrJarOrange!=0){%><%=WrappedGurrJarOrange %><%} %></td>
											<td style="text-align:center;"><%if(ConvertedWrappedGurrJarGinger!=0){%><%=ConvertedWrappedGurrJarGinger %><%} %></td>
											<td style="text-align:center;"><%if(ConvertedWrappedGurrJarCoconut!=0){%><%=ConvertedWrappedGurrJarCoconut %><%} %></td>
											
											<td style="text-align:center;"><%if(GurBox450Orange!=0){%><%=GurBox450Orange %><%} %></td>
											<td style="text-align:center;"><%if(GurBox450Ginger!=0){%><%=GurBox450Ginger %><%} %></td>
											<td style="text-align:center;"><%if(GurBox450Coconut!=0){%><%=GurBox450Coconut %><%} %></td>
											<td style="text-align:center;"><%if(GurBox425G!=0){%><%=GurBox425G %><%} %></td>
											
											
											<td style="text-align:center;"><%if(Converted!=0){%><%=Converted %><%} %></td>
											<td style="text-align:center;"><%if(TotalAmount!=0){%><%=TotalAmount %><%} %></td>
										</tr>
										<%
									  }
								   }		
								%>
								
								
								
								
								
									
								<%
								
								///CurrentDate = Utilities.getDateByDays(CurrentDate,1);
								
								//System.out.println(CurrentDate);
								
								}
						
								
							}//End oF Main Result Set	
						
						
						
						%>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td colpan="2"><b>Total</b></td>
							<%
							
							for(int i=0;i<TotalArray.length;i++){
								//System.out.println(TotalArray.length+"-----"+TotalArray[i]);
								if(i==10){ //for 70G Total
									
									%>
									<td style="text-align:center;"><%=Utilities.convertToRawCases((long)TotalArray[i], TotalUnitPerSKU70G) %></td>
									<%
								}else if(i==11){ //for 140G
							%>
							<td style="text-align:center;"><%=Utilities.convertToRawCases((long)TotalArray[i], TotalUnitPerSKU140G) %></td>
							
							<%
								}else{
							%>
								<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalArray[i]) %></td>
							<%		
								}
							}
							%>
							
						</tr>
						
						
						<%
						
						
						}//End of IF
					 	
					 	else{
						%>	
						<b>Please Select PJP First.</b>
						
						<%
						}
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