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
int FeatureID = 244;

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


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
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
String OrderBookerIDsWhere="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWhere =" and mo.created_by in ("+OrderBookerIDs+") ";
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


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Order Booker Sales</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					 <thead>
					 <tr style="font-size:11px;">
					<%
					Date CurrentDate = StartDate;
					int i = 0;
					while(true){
						%>
						
						
					 <%if(i==0){ %>	
					 <th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					 <th data-priority="1"  style="text-align:center; ">Distributor</th>
					<th colspan="3" style="text-align: center">Converted Cases</th>
					  <%} %>
					 <th colspan="3" style="text-align: center"><%=Utilities.getDisplayDateFormat(CurrentDate) %></th>
					 <%
					 
						if(CurrentDate.equals(EndDate)){
							break;
						}
						CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
						i++;
					}
					%> 
					 
					
					 
					 
					 </tr>
					 
					 
					 
					 
					    <tr style="font-size:11px;">
							
							<%
					 CurrentDate = StartDate;
					 i = 0;
					while(true){
						%>
						
						
						<%if(i==0){ %>	
						<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
						<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					<th data-priority="1"  style="text-align:center; min-width: 50px;">Orders</th>
							<th data-priority="1"  style="text-align:center; min-width: 50px;">Sales</th>
							<th data-priority="1"  style="text-align:center; ">Difference</th> 
						
						<%} %>
							<th data-priority="1"  style="text-align:center; min-width: 50px;">O</th>
							<th data-priority="1"  style="text-align:center; min-width: 50px;">S</th>
							<th data-priority="1"  style="text-align:center; ">D</th>
					 <%
					 
						if(CurrentDate.equals(EndDate)){
							break;
						}
						CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
						i++;
					}
					%> 
							
							
							
							
							
					    </tr>
						
						
						
						
					
					 
					  </thead> 
					
					
					<tbody>
						<%
						double TotalConvertedCases = 0;
						double TotalConvertedCasesOrders = 0;
						double TotalConvertedCasesDifference = 0;
						//System.out.println("select * from common_outlets_distributors_view codv,common_outlets co where co.id = codv.outlet_id and co.distributor_id in (select * from common_distributors where is_active=1 and type_id=2"+WhereHOD+WhereRSM+")");
						//ResultSet rs1 = s.executeQuery("select codv.outlet_id,co.name from common_outlets_distributors_view codv,common_outlets co where co.id = codv.outlet_id and codv.distributor_id in ("+DistributorIDs+")  "+WherePJP); //distributor query
						//System.out.println("SELECT sap_code, concat(first_name, ' ', last_name) name FROM employee_view where sap_code in (select distinct assigned_to from distributor_beat_plan_view where distributor_id in ("+DistributorIDs+") "+WhereRSM+")");
						
						ResultSet rs1 = s.executeQuery("SELECT u.id, u.display_name, (select concat(distributor_id,'-',distributor_name) from distributor_beat_plan_all_view where assigned_to = u.id limit 1) FROM users u where u.id in (select distinct assigned_to from distributor_beat_plan_view where distributor_id in ("+DistributorIDs+") "+WhereRSM+")"); //distributor query
						while(rs1.next()){
							//OutletID = rs1.getInt("outlet_id");
						%>
						<tr>
								<td><%=rs1.getString("id") + " - "+ rs1.getString("display_name") %></td>
								<td><%=Utilities.truncateStringToMax(rs1.getString(3),22) %></td>
							
							<%
							double ConvertedCases = 0;
							ResultSet rs31 = s3.executeQuery("select sum((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.is_promotion = 0 and isa.booked_by = "+rs1.getString("id"));	
							if(rs31.first()){
								ConvertedCases = rs31.getDouble(1);
							}
							TotalConvertedCases+= ConvertedCases;
							
							
							double ConvertedCasesOrders = 0;
							ResultSet rs41 = s3.executeQuery("select sum((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mop.is_promotion = 0 and mo.created_by = "+rs1.getString("id"));	
							if(rs41.first()){
								ConvertedCasesOrders = rs41.getDouble(1);
							}
							TotalConvertedCasesOrders += ConvertedCasesOrders;
							
							double difference = (ConvertedCasesOrders - ConvertedCases);
							TotalConvertedCasesDifference += difference; 
							
							
							%>
							
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesOrders) %></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases) %></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(difference) %></td>
							
							<%
							CurrentDate = StartDate;
							 i = 0;
							while(true){
							
							
							
							 ConvertedCases = 0;
							ResultSet rs3 = s3.executeQuery("select sum((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.order_created_on_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and isap.is_promotion = 0 and isa.booked_by = "+rs1.getString("id"));	
							if(rs3.first()){
								ConvertedCases = rs3.getDouble(1);
							}
							//TotalConvertedCases+= ConvertedCases;
							
							
							 ConvertedCasesOrders = 0;
							ResultSet rs4 = s3.executeQuery("select sum((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mop.is_promotion = 0 and mo.created_by = "+rs1.getString("id"));	
							if(rs4.first()){
								ConvertedCasesOrders = rs4.getDouble(1);
							}
							//TotalConvertedCasesOrders += ConvertedCasesOrders;
							
							 difference = (ConvertedCasesOrders - ConvertedCases);
							//TotalConvertedCasesDifference += difference;
							
							/*
							double BackordersCC = 0;
							ResultSet rs21 = s3.executeQuery("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in (select id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mo.created_by = "+rs1.getString("id")+")");
							if(rs21.first()){
								BackordersCC = rs21.getDouble(1);
							}
							TotalBackordersCC += BackordersCC;
							
							
							
							double SalesReturnCC = 0;
							ResultSet rs22 = s3.executeQuery("SELECT sum( ((isdap.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM inventory_sales_dispatch_adjusted_products isdap join inventory_products_view ipv on isdap.product_id = ipv.product_id where isdap.invoice_id in (select distinct id from inventory_sales_invoices where order_id in (select id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mo.created_by = "+rs1.getString("id")+")) and isdap.is_promotion = 0");
							if(rs22.first()){
								SalesReturnCC = rs22.getDouble(1);
							}
							
							TotalConvertedCasesSalesReturns+=SalesReturnCC;
							*/
							
							
							%>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesOrders) %></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases) %></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(difference) %></td>
						
						<%
						

						if(CurrentDate.equals(EndDate)){
							break;
						}
						CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
						i++;
						
						}
						
						%>	
						
						</tr>	
					<% 		
						}
						%>
						<tr>
						<td style="font-weight:bold">Total</td>
						<td style="font-weight:bold"></td>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesOrders) %></td>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases) %></td>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesDifference) %></td>

						
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