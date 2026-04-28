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
int FeatureID = 105;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

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
	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
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
	WhereHOD = " and codv.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and codv.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{

	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
		
		long DistributorArray[] = new long[1];
		DistributorArray[0] = UserDistributor[x].DISTRIBUTOR_ID;
		session.setAttribute(UniqueSessionID+"_SR1SelectedDistributors",DistributorArray);
		break;
	}*/
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

//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and codv.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
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
String WhereOrderBookerIDs="";
if(OrderBookerIDs.length()>0){
	WhereOrderBookerIDs =" and isa.booked_by in ("+OrderBookerIDs+") ";
}


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Orders</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <%if(IsDistributorSelected){ %>
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<%
							
							double TotalOrderAmount = 0;
							
							int PackageCount = 0;
							
							ResultSet rs = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products) "+WherePackage+" order by package_sort_order");
							int PacakgeID=0;
							int OutletID=0;
							double GrandTotal=0;
							while(rs.next()){
							
								PackageCount++;
							
							%>
							<th data-priority="1"  style="text-align:center; "><%=rs.getString("package_label")%></th>
							<%
							}
							
							long PackageTotal[] = new long[PackageCount];
							int PackageTotalUnitPerSKU[] = new int[PackageCount];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalUnitPerSKU[i]=0;
							}
							%>
							<th data-priority="1"  style="text-align:center; ">Amount (Rs.)</th>
					    </tr>
					  </thead> 
					<tbody>
						<%
						//System.out.println("select codv.outlet_id,co.name from common_outlets_distributors_view codv,common_outlets co where co.id = codv.outlet_id and codv.distributor_id in ("+DistributorIDs+")  "+WherePJP+WhereHOD+WhereRSM);
						ResultSet rs1 = s.executeQuery("select codv.outlet_id,co.name from common_outlets_distributors_view codv,common_outlets co where co.id = codv.outlet_id and codv.distributor_id in ("+DistributorIDs+") and co.id in (select distinct outlet_id from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")  "+WherePJP+WhereHOD+WhereRSM); //distributor query
						while(rs1.next()){
							OutletID = rs1.getInt("outlet_id");
						%>
						<tr>
								<td><%=OutletID + " - "+ rs1.getString("name") %></td>
							<%
							
							//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
							rs.beforeFirst();
							int PackageIndex = 0;
							while(rs.next()){
							int unit_per_sku=0;
							PacakgeID=rs.getInt("package_id");
								
							
							
							
							//select sum(total_units) qty from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between '2014-03-01' and '2014-03-30' and isa.distributor_id = 100669 and ipv.package_id = 1	
								
									ResultSet rs3 = s3.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mo.outlet_id = "+OutletID+" and ipv.package_id ="+PacakgeID+WherePackage+WhereBrand+WhereOrderBookerIDs);	
								while(rs3.next()){
									long qty = rs3.getLong("qty");
									unit_per_sku = rs3.getInt("unit_per_sku");
									PackageTotal[PackageIndex] += qty;
									if(unit_per_sku > 0){
										PackageTotalUnitPerSKU[PackageIndex] = unit_per_sku;
									}
									
							%>
								<td style="text-align: right;"><%= Utilities.convertToRawCases(qty,unit_per_sku) %></td> 
							<%
								}
								
								
								PackageIndex++;
							}
							
							double OrderAmount = 0;
							ResultSet rs3 = s3.executeQuery("select sum(mo.net_amount) from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mo.outlet_id = "+OutletID);	
							if(rs3.first()){
								OrderAmount = rs3.getDouble(1);
							}
							TotalOrderAmount+= OrderAmount;
							%>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(OrderAmount) %></td>
						</tr>
						<%
						}
						%>
						<tr>
						<td style="font-weight:bold">Total</td>
						<%
						
						for (int i = 0; i < PackageTotal.length; i++){
							
							%>
							<td style="text-align: right;"><%=Utilities.convertToRawCases(PackageTotal[i],PackageTotalUnitPerSKU[i])%></td> 
						<%
						}
						%>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount) %></td>
						</tr>
						
					</tbody>
					<%} else{ %>
					
						<p style="padding-left:10px;padding-top:20px;">Please select at least one  distributor</p>
					
					
					
					<%} %>
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