<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.inventory.StockPosting"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.common.Distributor"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 115;
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
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(StartDate == null){
	StartDate = new Date();
}

if(EndDate == null){
	EndDate = new Date();
}

Date OpeningDate = DateUtils.addDays(StartDate, -1);

//out.print("StartDate = "+OpeningDate);
//out.print("EndDate = "+EndDate);

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



long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");           	
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

	WhereBrand = " and brand_id in ("+BrandIDs+") ";
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

String OrderBookerIDs = "";
String WhereOrderBooker = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
	
}


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Stock Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border="0" style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0" class="GridWithBorder">
					 <thead>
					    <tr style="font-size: 11px;">
							<th data-priority="2" style="width: 28%">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Opening Stock</th>
							<th data-priority="1"  style="text-align:center; width: 12%" colspan="2">Received</th>
							<th data-priority="1"  style="text-align:center; width: 12%" colspan="2">Issued</th>
							<th data-priority="1"  style="text-align:center; width: 12%" colspan="1">Pending Dispatch</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Closing Stock</th>
							<th data-priority="1"  style="text-align:center; width: 12%" colspan="1">Damage Stock</th>
							<th data-priority="1"  style="text-align:center; width: 12%" colspan="1">Expired Stock</th>
					    </tr>
					    <tr style="font-size: 9px;">
							<th data-priority="2" style="width: 28%">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width: 12%"></th>
							<th data-priority="1"  style="text-align:center; width: 12%">Lifting</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Others</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Dispatch</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Others</th>
							<th data-priority="1"  style="text-align:center; width: 12%"></th>
							<th data-priority="1"  style="text-align:center; width: 12%"></th>
							<th data-priority="1"  style="text-align:center; width: 12%"></th>
							<th data-priority="1"  style="text-align:center; width: 12%"></th>
					    </tr>					    
					  </thead> 
					
				<%
				StockPosting sp = new StockPosting(true);	
				
				
					ResultSet rs = s.executeQuery("SELECT distinct  package_id,package_sort_order, package_label FROM inventory_products_view where category_id=1 "+WherePackage+" order by package_sort_order");
					while(rs.next()){
						
						int PackageID = rs.getInt("package_id");
						
						%>
						<tr style="background:#ececec">
	   	            		<td align="left"><%=rs.getString("package_label")%></td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="9">&nbsp;</td>
	   	            	</tr>
						<%
						ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id, brand_label FROM inventory_products_view where is_visible = 1 and category_id = 1 and package_id="+PackageID+" "+WhereBrand+" order by brand_label");
						while(rs2.next()){
							int BrandID = rs2.getInt("brand_id");
							
							
							int ProductID = 0;
							int UnitPerSKU = 0;
							int damageStock = 0;
							int expireStock = 0;

							
							
							ResultSet rs3 = s3.executeQuery("SELECT ipv.product_id, ipv.unit_per_sku from inventory_products_view ipv where ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs3.first()){
								ProductID = rs3.getInt(1);
								UnitPerSKU = rs3.getInt(2);
							}
							
							System.out.println("ProductID : " + ProductID);
							
							ResultSet rsDamageStock = s4.executeQuery("select sum(raw_cases)  from inventory_sales_returns_products where id in(select id from inventory_sales_returns where return_type=2 and distributor_id in ("+DistributorIDs+") and product_id = "+ProductID+")");
							if(rsDamageStock.first()){
								damageStock = rsDamageStock.getInt(1);
							}
							
							ResultSet rsExpireStock = s5.executeQuery("select sum(raw_cases)  from inventory_sales_returns_products where id in(select id from inventory_sales_returns where return_type=3 and distributor_id in ("+DistributorIDs+") and product_id = "+ProductID+")");
							if(rsExpireStock.first()){
								expireStock = rsExpireStock.getInt(1);
							}
							
							
							
							long OpeningUnits = sp.getClosingBalance(SelectedDistributorsArray, ProductID, OpeningDate);
							long ClosingUnits = sp.getClosingBalance(SelectedDistributorsArray, ProductID, EndDate);
							long IssuedUnitsDispatchOnly = sp.getStockIssuanceDispatchOnly(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							long IssuedUnitsLessDispatch = sp.getStockIssuanceLessDispatch(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							
							long ReceivedUnitsLiftingOnly = sp.getStockReceiptsLiftingOnly(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							long ReceivedUnitsLessLifting = sp.getStockReceiptsLessLifting(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							
							
							long PendingDispatch = sp.getBalanceafterdispatch(SelectedDistributorsArray , ProductID);
							
							long totalUnits= ClosingUnits - PendingDispatch;
		
							%>
							
							<tr>
		    					<td style="padding-left:20px"><%=rs2.getString("brand_label")%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (OpeningUnits < 0){%>E<%}else{%><%=Utilities.convertToRawCases(OpeningUnits, UnitPerSKU) %><%} %></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(ReceivedUnitsLiftingOnly, UnitPerSKU)%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(ReceivedUnitsLessLifting, UnitPerSKU)%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(IssuedUnitsDispatchOnly, UnitPerSKU)%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(IssuedUnitsLessDispatch, UnitPerSKU)%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(PendingDispatch, UnitPerSKU)%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (totalUnits < 0){%>E<%}else{%><%=Utilities.convertToRawCases(totalUnits, UnitPerSKU)%><%} %></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=damageStock %></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=expireStock %></td>
		    				
		    		    	</tr>
							
							<%
						}
						
					}
					
					sp.close();
				%>
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