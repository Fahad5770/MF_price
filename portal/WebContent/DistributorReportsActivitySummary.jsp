<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 67;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

long SelectedPackagesArray[] = null;
if (session.getAttribute("SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute("SR1SelectedPackages");           	
}

long SelectedBrandArray[] = null;
if (session.getAttribute("SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute("SR1SelectedBrands");           	
}

Date StartDate = (Date)session.getAttribute("SR1StartDate");
Date EndDate = (Date)session.getAttribute("SR1EndDate");

if(session.getAttribute("SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute("SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

long SelectedDistributorsArray[] = null;
if (session.getAttribute("SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute("SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

long SelectedOrderBookerArray[] = null;
if (session.getAttribute("SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");           	
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
<li data-role="list-divider" data-theme="a">Activity Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="2" style="width: 28%" >&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Booked</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Back Order</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Invoiced</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Dispatched</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Returned</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Sales</th>
					    </tr>
					  </thead> 
					
				<%
					
					ResultSet rs = s.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where category_id=1 "+WherePackage+" order by package_sort_order");
					while(rs.next()){
						
						int PackageID = rs.getInt("package_id");
						
						%>
						<tr style="background:#ececec">
	   	            		<td align="left"><%=rs.getString("package_label")%></td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="6">&nbsp;</td>
	   	            	</tr>
						<%
						ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id, brand_label FROM inventory_products_view where is_visible = 1 and category_id = 1 and package_id="+PackageID+" "+WhereBrand+" order by brand_label");
						while(rs2.next()){
							int BrandID = rs2.getInt("brand_id");
							
							/*if(OrderBookerIDs != ""){
								WhereOrderBooker = " and mo.created_by in ( "+OrderBookerIDs+" ) ";
							}else{
								WhereOrderBooker = "";
							}*/
							
							String QuantityOrdersBooked = "";
							ResultSet rs3 = s3.executeQuery("SELECT sum(total_units) bottles, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+" and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID +" and ipv.category_id = 1 group by mop.product_id");
							if(rs3.first()){
								QuantityOrdersBooked = Utilities.convertToRawCases( rs3.getLong("bottles"), rs3.getInt("unit_per_sku"));
							}
							
							/*if(OrderBookerIDs != ""){
								WhereOrderBooker = " and isi.booked_by in ( "+OrderBookerIDs+" ) ";
							}else{
								WhereOrderBooker = "";
							}*/
							
							String QuantityInvoiced = "";
							ResultSet rs4 = s3.executeQuery("SELECT isi.id, isi.created_on, isip.product_id, ipv.package_id, ipv.brand_id, ipv.package_label, ipv.brand_label, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_invoices isi, inventory_sales_invoices_products isip, inventory_products_view ipv where isi.id = isip.id and isip.product_id=ipv.product_id and isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+" and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs4.first()){
								QuantityInvoiced = Utilities.convertToRawCases( rs4.getLong("bottles"), rs4.getInt("unit_per_sku"));
							}
							
							/*if(OrderBookerIDs != ""){
								WhereOrderBooker = " and isi.booked_by in ( "+OrderBookerIDs+" ) ";
							}else{
								WhereOrderBooker = "";
							}*/
							
							String QuantityDispatched = "";
							ResultSet rs5 = s3.executeQuery("SELECT isd.id, isdi.sales_id, isip.product_id, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_dispatch isd, inventory_sales_dispatch_invoices isdi, inventory_sales_invoices_products isip, inventory_products_view ipv where isd.id=isdi.id and isdi.sales_id=isip.id and isip.product_id=ipv.product_id and isd.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+" and ipv.package_id="+PackageID+"  and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs5.first()){
								QuantityDispatched = Utilities.convertToRawCases( rs5.getLong("bottles"), rs5.getInt("unit_per_sku"));
							}
							
							String QuantityDispatchedReturned = "";
							ResultSet rs6 = s3.executeQuery("SELECT isd.id, isdrp.product_id, ipv.package_id, ipv.brand_id, sum(total_units) bottles, ipv.unit_per_sku FROM inventory_sales_dispatch isd, inventory_sales_dispatch_returned_products isdrp, inventory_products_view ipv where isd.id=isdrp.dispatch_id and isdrp.product_id=ipv.product_id and isd.is_liquid_returned=1 and isd.liquid_returned_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+" and isdrp.is_empty=0 and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs6.first()){
								QuantityDispatchedReturned = Utilities.convertToRawCases( rs6.getLong("bottles"), rs6.getInt("unit_per_sku"));
							}
							
							String QuantitySales = "";
							ResultSet rs7 = s3.executeQuery("SELECT isa.id, isap.product_id, ipv.package_id, ipv.brand_id, sum(isap.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_adjusted isa, inventory_sales_adjusted_products isap, inventory_products_view ipv where isa.id=isap.id and isap.product_id=ipv.product_id and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+" and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs7.first()){
								QuantitySales = Utilities.convertToRawCases( rs7.getLong("bottles"), rs7.getInt("unit_per_sku"));
							}
							
							String QuantityBackOrder = "";
							ResultSet rs8 = s3.executeQuery("SELECT mo.id, mopb.product_id, ipv.package_id, ipv.brand_id, sum(mopb.total_units) bottles, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products_backorder mopb, inventory_products_view ipv where mo.id=mopb.id and mopb.product_id=ipv.product_id and mo.is_backordered=1 and mo.backordered_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+" and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs8.first()){
								QuantityBackOrder = Utilities.convertToRawCases( rs8.getLong("bottles"), rs8.getInt("unit_per_sku"));
							}
							
							%>
							
							<tr>
		    					<td style="padding-left:20px"><%=rs2.getString("brand_label")%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityOrdersBooked%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityBackOrder%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityInvoiced%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityDispatched%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityDispatchedReturned%></td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantitySales%></td>
		    		    	</tr>
							
							<%
						}
						
					}
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