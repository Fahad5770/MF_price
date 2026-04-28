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
int FeatureID = 286;

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
	WhereHOD = " and ecer.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
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
	WhereRSM = " and ecer.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and ecer.distributor_id in ("+DistributorIDs+") ";
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
	WhereTDM = " and ecer.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";
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
	WhereRegion = " and ecer.distributor_id in (SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
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


String WarehouseIDs= UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID));
String WhereWarehouse1 ="";
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
}
String WhereWarehouse = "";
if (WarehouseIDs.length() > 0){
		WhereWarehouse = " and ect.warehouse_id in ("+WarehouseIDs+") ";
		
}



//Empty Receipt Type


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
							<th>ID</th>
							<th>Type</th>
							<th>Date</th>
							<th>Distributor</th>
							<th>Vehicle#</th>
							<th>Receipt Type</th>
							<th>Package</th>
							<th>Brand</th>
							<th>Cases</th>
							<th>Units</th>
													
					    </tr>
					    
					  </thead> 
						
						
						<tbody>
							<%
							long TotalTotalUnits=0;
							int UnitPerSKU=0;
							//System.out.println("SELECT ect.id,ect.distributor_id,(select name from common_distributors cd where cd.distributor_id= ect.distributor_id) distributor_name,(select vehicle_no from ec_empty_adjustment ecea where ecea.id=ect.empty_adjustment_id) vehicle_no,ect.type_id,(select label from ec_empty_receipt_types ecert where ecert.id=ect.type_id) type_name,ect.product_id,ip.package_id,(select label from inventory_packages ipp where ipp.id=ip.package_id) package_name, ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_name,ect.raw_cases_issued,ect.units_issued,ect.total_units_issued,ip.unit_per_sku,ect.created_on,ect.delivery_note_id,ect.empty_adjustment_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where 1=1 and ect.total_units_received !=0 and ((empty_adjustment_id is not null AND ect.type_id in (8,9,15,10,11,13,14) ) OR (empty_sale_id  is not null)) and ect.warehouse_id in("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereCustomerID+WherePackage+WhereBrand+WhereWarehouse+WhereEmptyReceiptType);
							
							
							//SELECT ect.id,ect.distributor_id,(select name from common_distributors cd where cd.distributor_id= ect.distributor_id) distributor_name,(select vehicle_no from inventory_delivery_note idn where idn.delivery_id=ect.delivery_note_id) vehicle_no,ect.type_id,(select label from ec_empty_receipt_types ecert where ecert.id=ect.type_id) type_name,ect.product_id,ip.package_id,(select label from inventory_packages ipp where ipp.id=ip.package_id) package_name, ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_name,ect.raw_cases_issued,ect.units_issued,ect.total_units_issued,ip.unit_per_sku,ect.created_on FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where 1=1 and ect.warehouse_id in(1,2,3,4,5,6) and ect.created_on between '2016-01-01' and '2016-01-10' and ect.warehouse_id in (1,2,3,4,5,6) 
							
									
							ResultSet rs = s.executeQuery("SELECT ect.id,ect.distributor_id,(select name from common_distributors cd where cd.distributor_id= ect.distributor_id) distributor_name,(select vehicle_no from ec_empty_adjustment ecea where ecea.id=ect.empty_adjustment_id) vehicle_no,ect.type_id,(select label from ec_empty_receipt_types ecert where ecert.id=ect.type_id) type_name,ect.product_id,ip.package_id,(select label from inventory_packages ipp where ipp.id=ip.package_id) package_name, ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_name,ect.raw_cases_received,ect.units_received,ect.total_units_received,ip.unit_per_sku,ect.created_on,ect.delivery_note_id,ect.empty_adjustment_id,ect.empty_sale_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where 1=1 and ect.total_units_received !=0 and ((empty_adjustment_id is not null AND ect.type_id in (8,9,15,10,11,13,14) ) OR (empty_sale_id  is not null)) and ect.warehouse_id in("+WarehouseIDs+") and ect.created_on between "+Utilities.getSQLDateTime(StartDateTimeConverted)+" and "+Utilities.getSQLDateTime(EndDateTimeConverted)+WhereCustomerID+WherePackage+WhereBrand+WhereWarehouse+WhereEmptyReceiptType);
							while(rs.next()){
							String TypeString ="";
								long[] ConvertedCases = Utilities.getRawCasesAndUnits(rs.getLong("total_units_received"),rs.getInt("unit_per_sku"));
								TotalTotalUnits += rs.getLong("total_units_received");
								UnitPerSKU=rs.getInt("unit_per_sku");
								
								if(rs.getLong("empty_sale_id")!=0){
									TypeString = "Sale";
								}else{
									TypeString = "Adjustment";
								}
								
								//if(rs.getLong("empty_adjustment_id")!=0){
								//	TypeString = "Adjustment";
								//} 
								
							%>
						<tr style="font-size:11px;">							
							<td><%=rs.getLong("id") %></td>
							<td><%=TypeString %></td>
							<td><%=Utilities.getDisplayDateFormat(rs.getDate("created_on")) %></td>
							<td><%=rs.getLong("distributor_id")+" - "+rs.getString("distributor_name") %></td>
							<td><%if(rs.getString("vehicle_no") !=null){ %><%=rs.getString("vehicle_no") %><%} %></td>
							<td><%=rs.getString("type_name") %></td>
							<td><%=rs.getString("package_name") %></td>
							<td><%=rs.getString("brand_name") %></td>
							<td><%if(ConvertedCases[0]!=0){%><%=ConvertedCases[0] %><%} %></td>
							<td><%if(ConvertedCases[1]!=0){%><%=ConvertedCases[1] %><%} %></td>
													
					    </tr>
							<%
							
							}							
							%>
							
							<%
							long[] ConvertedCasesTotal = Utilities.getRawCasesAndUnits(TotalTotalUnits,UnitPerSKU);
							
							if(SelectedPackagesArray!=null){
								if(SelectedPackagesArray.length==1){ //if only 1 pack then display total
									
								
							%>
							
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<th>Total</th>
								<td><%=ConvertedCasesTotal[0] %></td>
								<td><%=ConvertedCasesTotal[1] %></td>
							</tr>
							
							<%
								}
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