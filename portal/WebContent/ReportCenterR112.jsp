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
int FeatureID = 112;
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

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
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
	WhereOrderBooker = " and mo.created_by in ("+OrderBookerIDs+") ";
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


String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM;
String InvoiceIDsQuery = "select distinct id from inventory_sales_invoices where order_id in ("+OrderIDsQuery+")";

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sales Activity</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size: 11px;">
							<th data-priority="2" style="width: 20%" >&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width: 14%" colspan="2">Order</th>
							<th data-priority="1"  style="text-align:center; width: 12%" >Back Order</th>
							<th data-priority="1"  style="text-align:center; width: 14%">Invoiced</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Dispatched</th>
							<th data-priority="1"  style="text-align:center; width: 14%">Returned</th>
							<th data-priority="1"  style="text-align:center; width: 14%" colspan="2">Sales</th>
					    </tr>
					    <tr style="font-size: 9px;">
							<th data-priority="2" style="width: 20%" >&nbsp;</th>
							<th data-priority="1"  style="text-align:center;">Quantity</th>
							<th data-priority="1"  style="text-align:center;">Amount</th>							
							<th data-priority="1"  style="text-align:center;"></th>
							<th data-priority="1"  style="text-align:center;"></th>
							<th data-priority="1"  style="text-align:center;"></th>
							<th data-priority="1"  style="text-align:center;"></th>
							<th data-priority="1"  style="text-align:center;">Quantity</th>
							<th data-priority="1"  style="text-align:center;">Amount</th>	
					    </tr>
					  </thead> 
					
				<%
					double TotalAmountOrdersBooked = 0;
					double TotalAmountSales = 0;
					ResultSet rs = s.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where category_id=1 "+WherePackage+" order by package_sort_order");
					while(rs.next()){
						
						int PackageID = rs.getInt("package_id");
						
						%>
						<tr style="background:#ececec">
	   	            		<td align="left"><%=rs.getString("package_label")%></td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="8">&nbsp;</td>
	   	            	</tr>
						<%
						ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id, brand_label FROM inventory_products_view where is_visible = 1 and category_id = 1 and package_id="+PackageID+" "+WhereBrand+" order by brand_label");
						while(rs2.next()){
							int BrandID = rs2.getInt("brand_id");
							
							String QuantityOrdersBooked = "";
							double AmountOrdersBooked = 0;
							ResultSet rs3 = s3.executeQuery("SELECT sum(total_units) bottles, sum(if (mop.is_promotion=1, 0, mop.net_amount)) amount, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.id in ("+OrderIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID +" and ipv.category_id = 1 group by mop.product_id");
							if(rs3.first()){
								QuantityOrdersBooked = Utilities.convertToRawCases( rs3.getLong("bottles"), rs3.getInt("unit_per_sku"));
								AmountOrdersBooked = rs3.getDouble("amount");
							}
							TotalAmountOrdersBooked += AmountOrdersBooked;
									
							String QuantityInvoiced = "";
							ResultSet rs4 = s3.executeQuery("SELECT isi.id, isi.created_on, isip.product_id, ipv.package_id, ipv.brand_id, ipv.package_label, ipv.brand_label, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_invoices isi, inventory_sales_invoices_products isip, inventory_products_view ipv where isi.id = isip.id and isip.product_id=ipv.product_id and isi.id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs4.first()){
								QuantityInvoiced = Utilities.convertToRawCases( rs4.getLong("bottles"), rs4.getInt("unit_per_sku"));
							}
							
							String QuantityDispatched = "";
							ResultSet rs5 = s3.executeQuery("SELECT isd.id, isdi.sales_id, isip.product_id, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_dispatch isd, inventory_sales_dispatch_invoices isdi, inventory_sales_invoices_products isip, inventory_products_view ipv where isd.id=isdi.id and isdi.sales_id=isip.id and isip.product_id=ipv.product_id and isdi.sales_id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+"  and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs5.first()){
								QuantityDispatched = Utilities.convertToRawCases( rs5.getLong("bottles"), rs5.getInt("unit_per_sku"));
							}
							
							String QuantityDispatchedReturned = "";
							
							ResultSet rs6 = s3.executeQuery("SELECT isd.id, isdap.product_id, ipv.package_id, ipv.brand_id, sum(total_units) bottles, ipv.unit_per_sku FROM inventory_sales_dispatch isd, inventory_sales_dispatch_adjusted_products isdap, inventory_products_view ipv where isd.id=isdap.dispatch_id and isdap.product_id=ipv.product_id and isd.is_liquid_returned=1 and isdap.invoice_id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs6.first()){
								QuantityDispatchedReturned = Utilities.convertToRawCases( rs6.getLong("bottles"), rs6.getInt("unit_per_sku"));
							}
							
							String QuantitySales = "";
							double AmountSales = 0;
							ResultSet rs7 = s3.executeQuery("SELECT isa.id, isap.product_id, ipv.package_id, ipv.brand_id, sum(isap.total_units) bottles, sum(if (isap.is_promotion=1, 0, isap.net_amount)) amount, ipv.unit_per_sku FROM inventory_sales_adjusted isa, inventory_sales_adjusted_products isap, inventory_products_view ipv where isa.id=isap.id and isap.product_id=ipv.product_id and isa.id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs7.first()){
								QuantitySales = Utilities.convertToRawCases( rs7.getLong("bottles"), rs7.getInt("unit_per_sku"));
								AmountSales = rs7.getDouble("amount");
							}
							TotalAmountSales += AmountSales;
							
							
							String QuantityBackOrder = "";
							ResultSet rs8 = s3.executeQuery("SELECT mo.id, mopb.product_id, ipv.package_id, ipv.brand_id, sum(mopb.total_units) bottles, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products_backorder mopb, inventory_products_view ipv where mo.id=mopb.id and mopb.product_id=ipv.product_id and mo.is_backordered=1 and mo.id in ("+OrderIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs8.first()){
								QuantityBackOrder = Utilities.convertToRawCases( rs8.getLong("bottles"), rs8.getInt("unit_per_sku"));
							}
							
							%>
							
							<tr style="font-size: 12px;">
		    					<td style="padding-left:20px"><%=rs2.getString("brand_label")%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityOrdersBooked%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (AmountOrdersBooked!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(AmountOrdersBooked));} %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityBackOrder%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityInvoiced%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityDispatched%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityDispatchedReturned%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantitySales%></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (AmountSales!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(AmountSales));} %></td>
		    		    	</tr>
							
							<%
						}
						
					}
					double TotalInvoiceAmount = 0;
					double TotalInvoiceWHTax = 0;
					double TotalInvoiceDiscount = 0;
					double TotalInvoiceNetAmount = 0;
					ResultSet rs20 = s.executeQuery("select sum(invoice_amount), sum(wh_tax_amount), sum(discount), sum(net_amount) from inventory_sales_adjusted where id in ("+InvoiceIDsQuery+")");
					if (rs20.first()){
						TotalInvoiceAmount = rs20.getDouble(1);
						TotalInvoiceWHTax = rs20.getDouble(2);
						TotalInvoiceDiscount = rs20.getDouble(3);
						TotalInvoiceNetAmount = rs20.getDouble(4);
					}
					double TotalOrderAmount = 0;
					double TotalOrderWHTax = 0;
					double TotalOrderNetAmount = 0;
					
					ResultSet rs21 = s.executeQuery("select sum(invoice_amount), sum(wh_tax_amount), sum(net_amount) from mobile_order where id in ("+OrderIDsQuery+")");
					if (rs21.first()){
						TotalOrderAmount = rs21.getDouble(1);
						TotalOrderWHTax = rs21.getDouble(2);
						TotalOrderNetAmount = rs21.getDouble(3);
					}					
				%>
						<tr style="background:#ececec">
	   	            		<td align="left">Total</td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="8">&nbsp;</td>
	   	            	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Order Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalOrderAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount));} %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Invoice Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceAmount));} %></td>
		    		    	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">W.H. Tax</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalOrderWHTax!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderWHTax));} %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">W.H. Tax</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceWHTax!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceWHTax));} %></td>
		    		    	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Discount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Discount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceDiscount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceDiscount));} %></td>
		    		    	</tr>		
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Net Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalOrderNetAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderNetAmount));} %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Net Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceNetAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceNetAmount));} %></td>
		    		    	</tr>				
		    		    			
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