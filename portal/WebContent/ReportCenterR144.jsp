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
int FeatureID = 159;
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
	WhereDistributors = " and isi.distributor_id in ("+DistributorIDs+") ";
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
	WhereOrderBooker = " and isi.created_by ("+OrderBookerIDs+") ";
}

//String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+" "+WhereOrderBooker+"";


//String InvoiceIDsQuery = "select distinct id from inventory_sales_invoices where order_id in ("+OrderIDsQuery+")";


//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and isi.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and isi.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereSM = " and isi.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and isi.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and isi.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


//Sales Type


String SalesTypeIDs="";
long SelectedSalesTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSalesType") != null){
	SelectedSalesTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSalesType");
	SalesTypeIDs = Utilities.serializeForSQL(SelectedSalesTypeArray);
}

String WhereSalesType = "";
if (SalesTypeIDs.length() > 0){
	WhereSalesType = " and isi.type_id in ("+SalesTypeIDs+")";	
}






//System.out.println(InvoiceIDsQuery);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sales Activity</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr>
					    <td>&nbsp;</td>
					    <th colspan="4" style="text-align:center;">Unadjusted</td>
					    <th  colspan="4" style="text-align:center;"> Adjusted</td>
					    </tr>
					    <tr style="font-size: 9px;">
							<th data-priority="2" style="width: 20%" >&nbsp;</th>							
							<th data-priority="1"  style="text-align:center;width: 10%" >Quantity Sold</th>
							<th data-priority="1"  style="text-align:center;width: 10%" >Quantity Promotion</th>
							<th data-priority="1"  style="text-align:center;width: 10%" >Total Quantity</th>
							<th data-priority="1"  style="text-align:center;width: 10%">Amount</th>	
							
							<th data-priority="1"  style="text-align:center;width: 10%" >Quantity Sold</th>
							<th data-priority="1"  style="text-align:center;width: 10%" >Quantity Promotion</th>
							<th data-priority="1"  style="text-align:center;width: 10%" >Total Quantity</th>
							<th data-priority="1"  style="text-align:center;width: 10%">Amount</th>	
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
							
							long QuantitySold = 0;
							int QuantitySoldUPSKU = 0;
							double Amount=0;
							
							long QuantitySoldPromotion = 0;
							int QuantitySoldUPSKUPromotion = 0;
							
							
							long TotalQuantity=0;
							int TotalQuantityUPSKU=0;
							
							//System.out.println("SELECT sum(isip.total_units) quanity_sold,ip.unit_per_sku,sum(isip.net_amount) net_amount FROM inventory_sales_invoices isi join inventory_sales_invoices_products isip on isi.id = isip.id join inventory_products ip on isip.product_id = ip.id where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+" and isip.is_promotion = 0 and ip.package_id="+PackageID+" and ip.brand_id="+BrandID+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							ResultSet rs3 = s3.executeQuery("SELECT sum(isip.total_units) quanity_sold,ip.unit_per_sku,sum(isip.net_amount) net_amount FROM inventory_sales_invoices isi join inventory_sales_invoices_products isip on isi.id = isip.id join inventory_products ip on isip.product_id = ip.id where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+" and isip.is_promotion = 0 and ip.package_id="+PackageID+" and ip.brand_id="+BrandID+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							if(rs3.next()){
								 QuantitySold = rs3.getLong("quanity_sold");
								 QuantitySoldUPSKU = rs3.getInt("unit_per_sku");
								 Amount = rs3.getDouble("net_amount");
							}
							
							ResultSet rs4 = s3.executeQuery("SELECT sum(isip.total_units) quanity_sold,ip.unit_per_sku FROM inventory_sales_invoices isi join inventory_sales_invoices_products isip on isi.id = isip.id join inventory_products ip on isip.product_id = ip.id where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+" and isip.is_promotion = 1 and ip.package_id="+PackageID+" and ip.brand_id="+BrandID+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							if(rs4.next()){
								 QuantitySoldPromotion = rs4.getLong("quanity_sold");
								 QuantitySoldUPSKUPromotion = rs4.getInt("unit_per_sku");
								
							}
							
							TotalQuantity += QuantitySold+QuantitySoldPromotion;							
							
							
							//For Adjusted
							
							long QuantitySoldAdjusted = 0;
							int QuantitySoldUPSKUAdjusted = 0;
							double AmountAdjusted=0;
							
							long QuantitySoldPromotionAdjusted = 0;
							int QuantitySoldUPSKUPromotionAdjusted = 0;
							
							
							long TotalQuantityAdjusted=0;
							
							ResultSet rs5 = s3.executeQuery("SELECT sum(isip.total_units) quanity_sold,ip.unit_per_sku,sum(isip.net_amount) net_amount FROM inventory_sales_adjusted isi join inventory_sales_adjusted_products isip on isi.id = isip.id join inventory_products ip on isip.product_id = ip.id where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+" and isip.is_promotion = 0 and ip.package_id="+PackageID+" and ip.brand_id="+BrandID+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							if(rs5.next()){
								QuantitySoldAdjusted = rs5.getLong("quanity_sold");
								QuantitySoldUPSKUAdjusted = rs5.getInt("unit_per_sku");
								AmountAdjusted = rs5.getDouble("net_amount");
							}
							
							ResultSet rs6 = s3.executeQuery("SELECT sum(isip.total_units) quanity_sold,ip.unit_per_sku FROM inventory_sales_adjusted isi join inventory_sales_adjusted_products isip on isi.id = isip.id join inventory_products ip on isip.product_id = ip.id where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+" and isip.is_promotion = 1 and ip.package_id="+PackageID+" and ip.brand_id="+BrandID+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							if(rs6.next()){
								QuantitySoldPromotionAdjusted = rs6.getLong("quanity_sold");
								QuantitySoldUPSKUPromotionAdjusted = rs6.getInt("unit_per_sku");
								
							}
							
							TotalQuantityAdjusted += QuantitySoldAdjusted+QuantitySoldPromotionAdjusted;
							
							
							
							//SELECT sum(isip.total_units) quanity_sold,ip.unit_per_sku FROM inventory_sales_adjusted isi join inventory_sales_adjusted_products isip on isi.id = isip.id join inventory_products ip on isip.product_id = ip.id where isi.created_on between '2014-06-01' and '2014-06-02' and isi.type_id in (1,2,3) and isip.is_promotion = 0 and ip.package_id=3 and ip.brand_id=1 and isi.created_by=2335 and isi.distributor_id=100762
							
							
							
							%>
							
							<tr style="font-size: 12px;">
		    					<td style="padding-left:20px"><%=rs2.getString("brand_label")%></td>
		    					
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(QuantitySold, QuantitySoldUPSKU) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(QuantitySoldPromotion, QuantitySoldUPSKUPromotion) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(TotalQuantity, QuantitySoldUPSKU) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if(Amount !=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Amount)%><%}%></td>
		    					
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(QuantitySoldAdjusted, QuantitySoldUPSKUAdjusted) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(QuantitySoldPromotionAdjusted, QuantitySoldUPSKUPromotionAdjusted) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.convertToRawCases(TotalQuantityAdjusted, QuantitySoldUPSKUAdjusted) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if(AmountAdjusted !=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AmountAdjusted)%><%} %></td>
		    		    	</tr>
							
							<%
							
							Amount =0;
							AmountAdjusted = 0;
						}
						
					}
					
					// for footer total 
					
					double TotalInvoiceAmount=0;
					double TotalWHTax=0;
					double TotalNetAmount=0;
					
					ResultSet rs7 = s3.executeQuery("SELECT sum(isi.invoice_amount) invoice_amount, sum(isi.wh_tax_amount) wh_tax_amount,sum(isi.net_amount) net_amount FROM inventory_sales_invoices isi where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							if(rs7.next()){
								TotalInvoiceAmount = rs7.getDouble("invoice_amount");
								TotalWHTax = rs7.getDouble("wh_tax_amount");
								TotalNetAmount = rs7.getDouble("net_amount");
							}
							
					double TotalInvoiceAmountAdjusted=0;
					double TotalWHTaxAdjusted=0;
					double TotalNetAmountAdjusted=0;
					
					ResultSet rs8 = s3.executeQuery("SELECT sum(isi.invoice_amount) invoice_amount, sum(isi.wh_tax_amount) wh_tax_amount,sum(isi.net_amount) net_amount FROM inventory_sales_adjusted isi where isi.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereSalesType+WhereOrderBooker+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
							if(rs8.next()){
								TotalInvoiceAmountAdjusted = rs8.getDouble("invoice_amount");
								TotalWHTaxAdjusted = rs8.getDouble("wh_tax_amount");
								TotalNetAmountAdjusted = rs8.getDouble("net_amount");
							}
					
					
				%>
						<tr style="background:#ececec">
	   	            		<td align="left">Total</td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="8">&nbsp;</td>
	   	            	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Invoice Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceAmount) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Invoice Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceAmountAdjusted) %></td>
		    		    	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"colspan="2">W.H. Tax</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalWHTax) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">W.H. Tax</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalWHTaxAdjusted) %></td>
		    		    	</tr>				
								

							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"colspan="2">Net Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalNetAmount) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    					
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Net Amount</td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalNetAmountAdjusted) %></td>
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