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
<%@page import="com.pbc.common.Region"%>

<script src="js/lookups.js"></script>
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
int FeatureID = 241;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();

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
	WherePackage = " and ip.package_id in ("+PackageIDs+") ";
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
	WhereHOD = " and ect.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD1 = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
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
	WhereRSM = " and ect.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
	WhereRSM1 = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
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
String WhereTDM1 = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and ect.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";
	WhereTDM1 = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";
}

//region

Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String RegionIDs = UserAccess.getRegionQueryString(RegionObj);

long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";
String WhereRegion1 = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and ect.distributor_id in (SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
	WhereRegion1 = " and distributor_id in (SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
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


//Sales Type


String AccountTypeIDs="";
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	AccountTypeIDs = Utilities.serializeForSQL(SelectedAccountTypeArray);
}

String WhereSalesType = "";
if (AccountTypeIDs.length() > 0){
	WhereSalesType = " and type_id in ("+AccountTypeIDs+")";	
}

//customer filter

long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID != 0){
	WhereCustomerID = " and ect.distributor_id in ("+DistributorID+") ";

}

//System.out.println("Hello "+WhereCustomerID);

//EmptyLossType


String EmptyLossTypeIDs="";
long SelectedEmptyLossTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType") != null){
	SelectedEmptyLossTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType");
	EmptyLossTypeIDs = Utilities.serializeForSQL(SelectedEmptyLossTypeArray);
}

String WhereEmptyLossType = "";
String WhereEmptyLossType1 = "";
if (EmptyLossTypeIDs.length() > 0){
	WhereEmptyLossType = " and type_id in ("+EmptyLossTypeIDs+") ";	
	WhereEmptyLossType1 = " and ect.type_id in ("+EmptyLossTypeIDs+") ";	
}


//Brand

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
	WhereBrand = " and ip.brand_id in ("+BrandIDs+") ";
}


//Datetime

Date StartDateTime=null;
int StartDateTimeHour=0;
int StartDateTimeMinutes=0;
String StartDateTimeString="";
Date StartDateTimeConverted=null;

if(session.getAttribute(UniqueSessionID+"_SR1StartDateTime")!=null){
	 StartDateTime = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDateTime");
	 StartDateTimeHour = Utilities.parseInt(session.getAttribute(UniqueSessionID+"_SR1StartDateTimeHour").toString());
	 StartDateTimeMinutes = Utilities.parseInt(session.getAttribute(UniqueSessionID+"_SR1StartDateTimeMinutes").toString());
	 
	  StartDateTimeString=Utilities.getDisplayDateFormat(StartDateTime)+" "+StartDateTimeHour+":"+StartDateTimeMinutes;
	  StartDateTimeConverted = Utilities.parseDateTime(StartDateTimeString); //dd/MM/yyyy HH:mm
}


Date EndDateTime = null;
int EndDateTimeHour = 0;
int EndDateTimeMinutes =0;
String EndDateTimeString="";
Date EndDateTimeConverted = null;

if(session.getAttribute(UniqueSessionID+"_SR1EndDateTime")!=null){
	 EndDateTime = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDateTime");
	 EndDateTimeHour = Utilities.parseInt(session.getAttribute(UniqueSessionID+"_SR1EndDateTimeHour").toString());
	 EndDateTimeMinutes = Utilities.parseInt(session.getAttribute(UniqueSessionID+"_SR1EndDateTimeMinutes").toString());
	 EndDateTimeString=Utilities.getDisplayDateFormat(EndDateTime)+" "+EndDateTimeHour+":"+EndDateTimeMinutes;
	 EndDateTimeConverted = Utilities.parseDateTime(EndDateTimeString); //dd/MM/yyyy HH:mm
}


//System.out.println(StartDateTimeConverted);


String EmptyReceiptTypeIDs="";
long SelectedEmptyReceiptTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReceiptType") != null){
	SelectedEmptyReceiptTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReceiptType");
	EmptyReceiptTypeIDs = Utilities.serializeForSQL(SelectedEmptyReceiptTypeArray);
}

String WhereEmptyReceiptType = "";

if (EmptyReceiptTypeIDs.length() > 0){
	WhereEmptyReceiptType = " and ect.type_id in ("+EmptyReceiptTypeIDs+") ";
}


String WarehouseIDs= UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID));
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
}
String WhereWarehouse = "";
if (WarehouseIDs.length() > 0){
		WhereWarehouse = " and ect.warehouse_id in ("+WarehouseIDs+") ";	
}

//System.out.println(WarehouseIDs);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					
					 
					<tbody>
						
						
						<thead>
					 
					    <tr style="font-size:11px;">							
							
							
							<th data-priority="1"  style="text-align:center;  width:15%"></th>
							
							<%
							int ArrayCount=0;
							//for array count
							
							//System.out.println("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2) and package_id not in (10) and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+" group by ip.package_id order by ipack.sort_order, ip.package_id");
							ResultSet rs6 = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2,3) and package_id not in (10) and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereEmptyReceiptType+WhereCustomerID+" group by ip.package_id order by ipack.sort_order, ip.package_id");
							while(rs6.next()){
								ArrayCount++;
							}
							
							int PackageBrandArray [] = new int[ArrayCount];
							
							
							%>
							
							
							
							<%
							int co=0;
							ResultSet rs = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2,3) and package_id not in (10) and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereEmptyReceiptType+WhereCustomerID+" group by ip.package_id order by ipack.sort_order, ip.package_id");
							while(rs.next()){
								
								PackageBrandArray[co]=rs.getInt("count");
							%>
							<th data-priority="1"  style="text-align:center; " colspan="<%=rs.getInt("count")+1%>"><%=rs.getString("package_label") %></th>
																
							<%
							co++;
							}
							
							
							//Total Array
							
							int GrandArraySize=0;
							
							for(int x=0;x<PackageBrandArray.length;x++){
								GrandArraySize += PackageBrandArray[x];
							}
							
							//System.out.println(TotalArrayCount);
										
							int PackageBrandGrandTotalArray [] = new int[GrandArraySize]; //Grand Total Array size = All brands + Count of Package because each package has total column
							int PackageBrandGrandTotalUnitPerSKUArray [] = new int[GrandArraySize];
							int PackageBrandGrandTotalOfToTalArray [] = new int[PackageBrandArray.length];
							int PackageBrandGrandTotalOfToTalUPerSKUArray [] = new int[PackageBrandArray.length];
							
							%>
							
							 </tr>
							 <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center;  width:15%"></th>
							<%
							
							int ix=0;
							//System.out.println("SELECT ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2) and package_id not in (10) and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+" order by ipack.sort_order, ip.package_id");
							
							ResultSet rs19 = s.executeQuery("SELECT distinct ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2,3) and package_id not in (10) and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereEmptyReceiptType+WhereCustomerID+" order by ipack.sort_order, ip.package_id");
							while(rs19.next()){
							
								int counter=1;
								
								//System.out.println("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2) and package_id not in (10) and package_id = "+rs19.getLong("package_id")+" and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+" order by ipack.sort_order, ip.package_id");
								
							ResultSet rs5 = s2.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2,3) and package_id not in (10) and package_id = "+rs19.getLong("package_id")+" and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereEmptyReceiptType+WhereCustomerID+" order by ipack.sort_order, ip.package_id");
							while(rs5.next()){
						%>
							<th data-priority="1"  style="text-align:center; "><%=rs5.getString("brand_label") %></th>
							<%
							//System.out.println("hello "+ix);
							
							if(ix<=ArrayCount){
							
								if(counter==PackageBrandArray[ix]){
									%>
									<th data-priority="1"  style="text-align:center; ">Total</th>
									<%
									counter=0;
									ix++;
								}
							}
							counter++;
							}
							}
							%>
							
							
													
					    </tr>
					    
					  </thead> 
						
						
						<tbody>
						
						<%
						ResultSet rs13 = s.executeQuery("select * from common_warehouses where id in ("+WarehouseIDs+")");
						while(rs13.next()){
						
						%>
						
							<tr>
							<%
							int counter11=1;
							int ix11=0;
							int cat=0;
							int cat1=0;
							
							
							%>
								<td><%=rs13.getString("label") %></td>
								<%
								ResultSet rs21 = s2.executeQuery("SELECT distinct ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2,3) and package_id not in (10) and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereEmptyReceiptType+WhereCustomerID+" order by ipack.sort_order, ip.package_id");
								while(rs21.next()){									
									
									ResultSet rs51 = s5.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id join inventory_packages ipack on ip.package_id=ipack.id where category_id in(2,3) and package_id not in (10) and package_id = "+rs21.getLong("package_id")+" and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereEmptyReceiptType+WhereCustomerID+" order by ipack.sort_order, ip.package_id");
									while(rs51.next()){
										
									//System.out.println("Select sum(total_units_received),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs21.getLong("package_id")+" and ip.brand_id="+rs51.getLong("brand_id")+" and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" and ect.warehouse_id="+rs13.getLong("id")+WhereEmptyReceiptType+" order by ip.package_id desc");
									long Balance=0;
									int UnitPerSku=0;
									
									
									
									ResultSet rs3 = s3.executeQuery("Select sum(total_units_received),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs21.getLong("package_id")+" and ip.brand_id="+rs51.getLong("brand_id")+" and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+WhereCustomerID+" and ect.warehouse_id="+rs13.getLong("id")+WhereEmptyReceiptType+" order by ip.package_id desc");
									if(rs3.first()){
										 Balance = rs3.getLong(1);
										 UnitPerSku = rs3.getInt(2);
										 
										 PackageBrandGrandTotalArray[cat] += Balance;
										 
										 if(UnitPerSku!=0){
											 PackageBrandGrandTotalUnitPerSKUArray[cat] = UnitPerSku; 
										 }
										 
										 
										// System.out.println(UnitPerSku);
										 
										// System.out.println("Hello "+PackageBrandGrandTotalArray[cat]+" - "+PackageBrandGrandTotalUnitPerSKUArray[cat]);
										 
										 cat++;
										 
										 
									}	
								%>
								<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
								<%
								Balance=0;
								UnitPerSku=0;
								if(counter11==PackageBrandArray[ix11]){
									
									
									ResultSet rs131 = s6.executeQuery("select unit_per_sku from inventory_products where package_id="+rs21.getLong("package_id"));
									if(rs131.first()){
										UnitPerSku = rs131.getInt(1);
									}
									
									ResultSet rs12 = s6.executeQuery("Select sum(total_units_received),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs21.getLong("package_id")+" and ect.warehouse_id in ("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+WhereCustomerID+" and ect.warehouse_id="+rs13.getLong("id")+WhereEmptyReceiptType+" order by ip.package_id desc");
									if(rs12.first()){
										Balance = rs12.getLong(1);
										 //UnitPerSku = rs12.getInt(2);
										 
										 PackageBrandGrandTotalOfToTalArray [cat1]+=Balance;
										 PackageBrandGrandTotalOfToTalUPerSKUArray [cat1]=UnitPerSku;
									%>
									<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
									<%
									counter11=0;
									ix11++;
									cat1++;
									}
								}
								
							counter11++;
								
								%>
								
								
																	
								<%
								
									}
									
									
								}
								
								
							%>
							
							</tr>
						
						<%
						} //warehouse loop end
						%>
						<tr>
						<td></td>
						<%
						int c1=1;
						int totalindex=0;
						for(int i=0;i<PackageBrandGrandTotalArray.length;i++){
							
							//System.out.println(PackageBrandGrandTotalArray[i]+" - "+PackageBrandGrandTotalUnitPerSKUArray[i]);
							%>
							
							<td style="text-align:right;"><%=Utilities.convertToRawCasesAccounting(PackageBrandGrandTotalArray[i],PackageBrandGrandTotalUnitPerSKUArray[i]) %></td>
							
							<%
							if(c1==PackageBrandArray[totalindex]){
								%>
								<td style="text-align:right;"><%if(PackageBrandGrandTotalOfToTalArray[totalindex]!=0){%><%=Utilities.convertToRawCasesAccounting(PackageBrandGrandTotalOfToTalArray[totalindex],PackageBrandGrandTotalOfToTalUPerSKUArray[totalindex]) %><%} %></td>
								<%
								c1=0;
								totalindex++;
							}
							c1++;
						}
						
						
						
						%>
						</tr>
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