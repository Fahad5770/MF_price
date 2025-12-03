<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<style>
#map {
        width: 100%;
        height: 300px;
        margin-top: 10px;
      }
</style>

<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 116;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


int Flag=Utilities.parseInt(request.getParameter("flag"));
int outletId=Utilities.parseInt(request.getParameter("outletId"));
long MobileOrder=Utilities.parseLong(request.getParameter("MOrder"));

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

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	//
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}*/
}

long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");           	
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and mo.outlet_id in ("+OutletIds+") ";	
}

System.out.println("OutletIds "+OutletIds);


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

long OrderID = Utilities.parseLong(request.getParameter("OrderID"));

String WherePackage = "";
String WhereBrand = "";
String OutletName = "Orders Activity";

String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  "+WhereOrderBooker+" "+WhereOutlets+" "+WhereDistributors;

boolean IsMapShow=false;
double latt=0;
double lang=0;

double olat = 0;
double olng = 0;



boolean IsMapShow2=false;
double latt2=0;
double lang2=0;

double olat2 = 0;
double olng2 = 0;

if(OrderID > 0){
	IsMapShow = true;
	System.out.println("SELECT concat(co.id, ' - ', co.name) outlet_name, mo.created_on,mo.lat,mo.lng, co.lat olat, co.lng olng  FROM mobile_order mo, common_outlets co where mo.outlet_id=co.id and mo.id="+OrderID);
	ResultSet rs3 = s.executeQuery("SELECT concat(co.id, ' - ', co.name) outlet_name, mo.created_on,mo.lat,mo.lng, co.lat olat, co.lng olng  FROM mobile_order mo, common_outlets co where mo.outlet_id=co.id and mo.id="+OrderID);
	if(rs3.first()){
		OutletName = rs3.getString("outlet_name") + " | "+ Utilities.getDisplayFullDateFormatShort(rs3.getDate(2));
		latt = rs3.getDouble("lat");
		lang = rs3.getDouble("lng");
		olat = rs3.getDouble("olat");
		olng = rs3.getDouble("olng");
		
	}
	
	
	//For no order
	IsMapShow2 = true;
	System.out.println("SELECT concat(co.id, ' - ', co.name) outlet_name, moz.created_on,moz.lat,moz.lng, co.lat olat, co.lng olng  FROM mobile_order_zero moz, common_outlets co where moz.outlet_id=co.id and moz.id="+OrderID);
	ResultSet rs32 = s.executeQuery("SELECT concat(co.id, ' - ', co.name) outlet_name, moz.created_on,moz.lat,moz.lng, co.lat olat, co.lng olng  FROM mobile_order_zero moz, common_outlets co where moz.outlet_id=co.id and moz.id="+OrderID);
	if(rs32.first()){
		OutletName = rs32.getString("outlet_name") + " | "+ Utilities.getDisplayFullDateFormatShort(rs32.getDate(2));
		latt2 = rs32.getDouble("lat");
		lang2 = rs32.getDouble("lng");
		olat2 = rs32.getDouble("olat");
		olng2 = rs32.getDouble("olng");
		
	}
	
	
	
	OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2,3) and mo.id="+OrderID;
	
	WherePackage = " and package_id in (";
	
	ResultSet rs = s.executeQuery("SELECT distinct(ipv.package_id) FROM mobile_order_products mop, inventory_products_view ipv where mop.product_id=ipv.product_id and mop.id="+OrderID);
	int counter = 0;
	while(rs.next()){
		if(counter > 0){
			WherePackage += ", ";
		}
		WherePackage += rs.getString("package_id");
		counter++;
	}
	WherePackage += ")";
	
	WhereBrand = " and brand_id in (";
	
	ResultSet rs2 = s.executeQuery("SELECT distinct(ipv.brand_id) FROM mobile_order_products mop, inventory_products_view ipv where mop.product_id=ipv.product_id and mop.id="+OrderID);
	int counter2 = 0;
	while(rs2.next()){
		if(counter2 > 0){
			WhereBrand += ", ";
		}
		WhereBrand += rs2.getString("brand_id");
		counter2++;
	}
	WhereBrand += ")";
	
	
}

String InvoiceIDsQuery = "select distinct id from inventory_sales_invoices where order_id in ("+OrderIDsQuery+")";


%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a"><%=OutletName%></li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							
							<%if(Flag==1){ //For no order don't need to show this%>
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
									 <thead>
									    <tr style="font-size: 11px;">
											<th data-priority="2" style="width: 20%" >&nbsp;</th>
											<th data-priority="1"  style="text-align:center; width: 40%" colspan="2">Order</th>
											
											<th data-priority="1"  style="text-align:center; width: 40%" colspan="2">Sales</th>
									    </tr>
									    <tr style="font-size: 9px;">
											<th data-priority="2" style="width: 20%" >&nbsp;</th>
											<th data-priority="1"  style="text-align:center;">Quantity</th>
											<th data-priority="1"  style="text-align:center;">Amount</th>							
											
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
										
										
										//For Order
										double PackwiseTotalOrder =0;
										double QtyTotalOrder=0;
										
										ResultSet rs323 = s2.executeQuery("SELECT sum(total_units) bottles, sum(if (mop.is_promotion=1, 0, mop.net_amount)) amount, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.id in ("+OrderIDsQuery+") and ipv.package_id="+PackageID+" and ipv.category_id = 1 "+WhereDistributors+"");
										if(rs323.first()){
											PackwiseTotalOrder = rs323.getDouble("amount");
											QtyTotalOrder = rs323.getDouble("bottles");
											
										}
										
										//For Sales
										
										double PackwiseTotalSales =0;
										double QtyTotalSales =0;
										ResultSet rs3231 = s2.executeQuery("SELECT isa.id, isap.product_id, ipv.package_id, ipv.brand_id, sum(isap.total_units) bottles, sum(if (isap.is_promotion=1, 0, isap.net_amount)) amount, ipv.unit_per_sku FROM inventory_sales_adjusted isa, inventory_sales_adjusted_products isap, inventory_products_view ipv where isa.id=isap.id and isap.product_id=ipv.product_id and isa.id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+" and ipv.category_id = 1 "+WhereDistributors);
										if(rs3231.first()){
											PackwiseTotalSales = rs3231.getDouble("amount");
											QtyTotalSales = rs3231.getDouble("bottles");
										}
										
										
										%>
										<tr style="background:#ececec">
					   	            		<td align="left"><%=rs.getString("package_label")%> </td>
					   	            		
					   	            		<td align="right" style="text-align:right;"> <%if(QtyTotalOrder!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(QtyTotalOrder) %><%} %> </td>
					   	            		
					   	            		<td align="right" style="text-align:right;"> <%if(PackwiseTotalOrder!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(PackwiseTotalOrder) %><%} %> </td>
					   	            		<td align="right" style="text-align:right;"> <%if(QtyTotalSales!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(QtyTotalSales) %><%} %> </td>					   	            	
					   	            		
					   	            		<td align="right" style="text-align:right;"> <%if(PackwiseTotalSales!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(PackwiseTotalSales) %><%} %> </td>
					   	            	</tr>
										<%
										
										
										ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id, brand_label FROM inventory_products_view where is_visible = 1 and category_id = 1 and package_id="+PackageID+" "+WhereBrand+" order by brand_label");
										while(rs2.next()){
											int BrandID = rs2.getInt("brand_id");
											
											String QuantityOrdersBooked = "";
											double AmountOrdersBooked = 0;
											ResultSet rs3 = s3.executeQuery("SELECT sum(total_units) bottles, sum(if (mop.is_promotion=1, 0, mop.net_amount)) amount, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.id in ("+OrderIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID +" and ipv.category_id = 1 "+WhereDistributors+" group by mop.product_id");
											//System.out.println("SELECT sum(total_units) bottles, sum(if (mop.is_promotion=1, 0, mop.net_amount)) amount, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.id in ("+OrderIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID +" and ipv.category_id = 1 "+WhereDistributors+" group by mop.product_id");
											if(rs3.first()){
												QuantityOrdersBooked = Utilities.convertToRawCases( rs3.getLong("bottles"), rs3.getInt("unit_per_sku"));
												AmountOrdersBooked = rs3.getDouble("amount");
												
											}
											TotalAmountOrdersBooked += AmountOrdersBooked;
													
											
											
											
											
											String QuantitySales = "";
											double AmountSales = 0;
											ResultSet rs7 = s3.executeQuery("SELECT isa.id, isap.product_id, ipv.package_id, ipv.brand_id, sum(isap.total_units) bottles, sum(if (isap.is_promotion=1, 0, isap.net_amount)) amount, ipv.unit_per_sku FROM inventory_sales_adjusted isa, inventory_sales_adjusted_products isap, inventory_products_view ipv where isa.id=isap.id and isap.product_id=ipv.product_id and isa.id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1 "+WhereDistributors);
											if(rs7.first()){
												QuantitySales = Utilities.convertToRawCases( rs7.getLong("bottles"), rs7.getInt("unit_per_sku"));
												AmountSales = rs7.getDouble("amount");
											}
											TotalAmountSales += AmountSales;
											
											
											
											
											%>
											
											<tr style="font-size: 12px;">
						    					<td style="padding-left:20px"><%=rs2.getString("brand_label")%></td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantityOrdersBooked%></td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (AmountOrdersBooked!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(AmountOrdersBooked));} %></td>
						    					
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=QuantitySales%></td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" ><%if (AmountSales!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(AmountSales));} %></td>
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
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Amount</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalOrderAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount));} %></td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" >Amount</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" ><%if (TotalInvoiceAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceAmount));} %></td>
						    		    	</tr>				
											<tr style="font-size: 12px;">
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">W.H. Tax</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalOrderWHTax!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderWHTax));} %></td>
					    					
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec">W.H. Tax</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceWHTax!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceWHTax));} %></td>
						    		    	</tr>				
											<tr style="font-size: 12px;">
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Disc.</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
						    					
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec">Disc.</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceDiscount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceDiscount));} %></td>
						    		    	</tr>		
											<tr style="font-size: 12px;">
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="2">Net</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalOrderNetAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderNetAmount));} %></td>
						    					
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec">Net</td>
						    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (TotalInvoiceNetAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceNetAmount));} %></td>
						    		    	</tr>				
						    		    			
								</table>
								
								
								<table border=0 style="font-size:13px; font-weight: 400; width:100%;margin-top:5%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							
									<tr>
										<td style="width:50%">
										
											Distance From Shop
										</td>
										<td style="width:50%"> 
											<%
											
											//Addition Starts 
											//Distance from Shop				
														
																	
											double TotalDistance =0;
																	
										//	System.out.println("SELECT 	avg((( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "+ 
						                  //          " FROM mobile_order mo join  common_outlets co on  mo.outlet_id=co.id where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  and mo.id="+OrderID+" and mo.lat!=0 and mo.lng!=0 and co.lat!=0 and co.lng!=0");						
											ResultSet rs232 = s2.executeQuery("SELECT 	avg((( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "+ 
						                            " FROM mobile_order mo join  common_outlets co on  mo.outlet_id=co.id where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  and mo.id="+OrderID+" and mo.lat!=0 and mo.lng!=0 and co.lat!=0 and co.lng!=0");
							        		if(rs232.first()){
							        			TotalDistance = rs232.getDouble("distance");
							        		}


											////////////////////////////////////////////////////////////////////////////////////////////
											/////////////////////////////////////////////////////
										
											%>
											<%=Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalDistance)%> Meters 
											
										
										</td>
									</tr>
								</table>  
								
								<%
							}else{
								
								
								
								
								%>
								
								<table border=0 style="font-size:13px; margin-top:20px ; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
										
									
									
									<% 
							         ResultSet rs = s.executeQuery("SELECT moz.comments,moz.no_order_reason_type_v2,(select label from mobile_order_no_order_reason_type where mobile_order_no_order_reason_type.id=moz.no_order_reason_type_v2) name from mobile_order_zero moz where moz.id="+OrderID );
							         while(rs.next()){
							          
							          String Reasonlabel = rs.getString("name");
							          String ReasonComments = rs.getString("comments");
							          int  Reasontype = rs.getInt("no_order_reason_type_v2");
							          
							          %>
							          <tr style="background:#ececec">
							                       <td align="left"><b>Reason</b> : <%=Reasonlabel %> </td>
							                      
							          
							           <%
							                       if(Reasontype==6){
							                        
							                        %> 
							                        <tr style="background:#ececec">  
							                        <td align="left"><b>Comments : </b> : <%=ReasonComments %> </td>
							                       </tr>
							                       
							                       
							                       <%
							                       }
							                       
							                       
							                       
							          
							          
							         }
							         %>
							         
							        
							         
							         	<tr style="background:#ececec">
							                       <td align="left"><b>Distance From Shop</b> :
							                       <%
											
														//Addition Starts 
														//Distance from Shop				
																	
																				
														double TotalDistance =0;
																				
														/* System.out.println("SELECT 	avg((( 3959 * acos( cos( radians(moz.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(moz.lng) ) + sin ( radians(moz.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "+ 
									                            " FROM mobile_order_zero moz join  common_outlets co on  moz.outlet_id=co.id where moz.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  and moz.id="+OrderID+" and moz.lat!=0 and moz.lng!=0 and co.lat!=0 and co.lng!=0");						
													 */	ResultSet rs232 = s2.executeQuery("SELECT 	avg((( 3959 * acos( cos( radians(moz.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(moz.lng) ) + sin ( radians(moz.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "+ 
									                            " FROM mobile_order_zero moz join  common_outlets co on  moz.outlet_id=co.id where moz.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  and moz.id="+OrderID+" and moz.lat!=0 and moz.lng!=0 and co.lat!=0 and co.lng!=0");
										        		if(rs232.first()){
										        			TotalDistance = rs232.getDouble("distance");
										        		}
			
			
														////////////////////////////////////////////////////////////////////////////////////////////
														/////////////////////////////////////////////////////
													
														%>
														<%=Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalDistance)%> Meters 
											
							                       	
							                       </td>
							          </tr> 
									  
									
									 <tr style="background:#ececec">
							                       <td align="left"><b>Device</b> :
							                       <%
							                       ResultSet rsDevice = s.executeQuery("SELECT uuid FROM mobile_order mo where mo.status_type_id in (1,2,3) and mo.id="+OrderID);
							                   	String device = (rsDevice.first()) ? rsDevice.getString("uuid") : "" ; 
													
														%>
														<%=device%>  
											
							                       	
							                       </td>
							          </tr> 
									
								</table>
								<%
								
							}
								%>
								
								
						</td>
					</tr>
					
					 <tr style="background:#ececec">
							                       <td align="left"><b>Device</b> :
							                       <%
							                       ResultSet rsDevice = s.executeQuery("SELECT uuid FROM mobile_order mo where mo.status_type_id in (1,2,3) and mo.id="+OrderID);
							                   	String device = (rsDevice.first()) ? rsDevice.getString("uuid") : "" ; 
													
														%>
														<%=device%>  
											
							                       	
							                       </td>
							          </tr> 
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr><th>Available Stock</th></tr>
					<tr>
						<td>
						<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							 <thead>
							 	<tr style="font-size: 11px;">
								 	<th>Package</th>
								 	<th>Brand</th>
								 	<th>Raw Quantity</th>
								 	<th>Units</th>
							 	</tr>
							 </thead>
							 <tbody>
							 <%
							 
								long UneditedOrderID = 0;
								//double TotalOrderWHTax = 0;
								//double TotalOrderNetAmount = 0;
								 ResultSet rs2122 = s.executeQuery("select unedited_order_id from mobile_order where id="+OrderID);
								if (rs2122.first()){
									UneditedOrderID= rs2122.getLong(1);
								}
								
									long retailerOrderID=0;
									ResultSet rs212233 = s.executeQuery("select mobile_order_no from mobile_order_unedited where id="+UneditedOrderID);
									if (rs212233.first()){
											retailerOrderID= rs212233.getLong(1);
											
									
									}
								
									String StockQuery="SELECT * FROM pep.mobile_retailer_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where mrs.order_no="+retailerOrderID;
									
								if(Flag==1){
									StockQuery="SELECT * FROM pep.mobile_retailer_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where mrs.order_no="+retailerOrderID;
								}else if(Flag==2){
									
									StockQuery="SELECT * FROM pep.mobile_retailer_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where mrs.order_no="+MobileOrder;
								}
								ResultSet rs212 = s2.executeQuery(StockQuery);
								while(rs212.next()){
							%>
							
							
								<tr>
							 		<td><%=rs212.getString("package_label") %></td>
							 		<td><%=rs212.getString("brand_label") %></td>
							 		<td><%=rs212.getString("raw_cases") %></td>
							 		<td><%=rs212.getString("units") %></td>
							 	</tr>
							
							<%
									
							
								}		
							 
							 
							 %>
							 
							 
							 	
							 <%
							 
							 
							 %>
							 </tbody>
						</table>
							
						</td>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
				     <tr><td>&nbsp;</td></tr>
				     <%
				     
				     ResultSet rs51 = s2.executeQuery("SELECT * FROM pep.mobile_order_unedited where id in (select unedited_order_id from mobile_order mo where mo.id="+OrderID+")");
				     while(rs51.next()){
				      if(rs51.getString("comments")!="" && rs51.getString("comments")!=null ){
				     %>
				    
				     <tr><td><b>Comments:</b><%=rs51.getString("comments") %></td></tr>
				     
				    
				     <%
				      }
				    
				     }  
				     
				     
				     %>
				     <%if(Flag==1){ %>
				     <tr><td>&nbsp;</td></tr>
				     <tr><td><b>Outlet Pictures</b></td></tr>
				     
				    
				     <tr>
				     	<td>
				     
				     		<table class="GridWithBorder">
								<%
								
								long UnEditedID=0;
								ResultSet rs22 = s.executeQuery("select unedited_order_id from mobile_order where id="+OrderID);
								if(rs22.first()){
									UnEditedID = rs22.getLong("unedited_order_id");
								}
								
								
									int i = 1, year=0;
									ResultSet rsi = s.executeQuery("select * from mobile_order_unedited_files where id=" + UnEditedID+" ");
									while (rsi.next()) {
										year = rsi.getInt("year");
								%>
								<tr>
									
									
									<td>
										<h3></h3><br/>
									 <center>
									
										 <img src="<%=( (year!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadMDE" ) %>?file=<%=rsi.getString("filename")%>&filePath=<%=rsi.getString("uri")%>" style="width:500px; height:500px;" />
										
									</center>
									
									
									
									
									
								<%
									i++;
									}
								%>
							</table>
				     
				     	</td>
				    </tr>
				     
				    
				     <%
				     }else{
				     
				     %>
				     <tr><td>&nbsp;</td></tr>
				     <tr><td><b>Outlet Pictures</b></td></tr>
				     
				    
				     <tr>
				     	<td>
				     
				     		<table class="GridWithBorder">
								<%
								
								
								
								
									int i = 1;
									ResultSet rsi = s.executeQuery("select * from mobile_order_zero_files where id=" + OrderID+" ");
									while (rsi.next()) {
								%>
								<tr>
									
									
									<td>
										<h3></h3><br/>
									 <center>
										<%-- <img src="mobile/MobileFileDownloadMDE?file=<%=rsi.getString("filename")%>" style="width:500px; height:500px;" /> --%>
									</center>
									
									
									
									
									
								<%
									i++;
									}
								%>
							</table>
				     
				     	</td>
				    </tr>
				     
				     <%} %>
					<tr><td>&nbsp;</td></tr>
				    <%--  <tr><td><b>Channel Tagging Pictures</b></td></tr>
				     <tr>
				     	<td>
				     
				     		<table class="GridWithBorder">
				     		<tr>
								<%
								
								
								
								
									int i = 1;
								//System.out.println("select * from mobile_common_outlets_files where outlet_id=" + outletId);
									ResultSet rsi = s.executeQuery("select * from mobile_common_outlets_files where outlet_id=" + outletId);
									while (rsi.next()) {
							
								%>
								
									
									
									<td>
										<h3></h3><br/>
									<center>
									 	<img src="mobile/MobileFileDownloadOutlet?file=<%=rsi.getString("filename")%>" style="width:200px; height:200px;" />
										</center>
									</td>
									
									
									
									
								<%
									i++;
									}
								%>
								</tr>
							</table>
				     
				     	</td>
				    </tr> --%>
				</table>
			
				</li>	
			</ul>
			<%
			
			if(Flag==1){

				if(IsMapShow && latt !=0 && lang !=0){
				%>
				<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-5px;" data-icon="false">
				
					<li data-role="list-divider" data-theme="a">Order Location</li>
					<li>
						
						<script>
				    	 initMap();
	
				    	function initMap(){			    		
				    		  <%
				    		  System.out.println("1 => "+latt+"-"+lang);
				    		  %>
				    		  var myLatlng = new google.maps.LatLng(<%=latt%>,<%=lang%>);
				    		  var mapOptions = {
				    		    zoom: 15,
				    		    center: myLatlng
				    		  };
	
				    		  var map = new google.maps.Map(document.getElementById('map'), mapOptions);
	
				    		  var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';
	
				    		  var infowindow = new google.maps.InfoWindow({
				    		      content: contentString,
				    		      maxWidth: 200
				    		  });
				    		  
				    		  var markers = new Array();
				    		  
				    			 
				    		map.setCenter(new google.maps.LatLng(<%=latt%>,<%=lang%>));
				    				  
				    			  
				    			  markers[0] = new google.maps.Marker({
				    			      position: new google.maps.LatLng(<%=latt%>,<%=lang%>),
				    			      map: map,
				    			      title: '<%=OutletName%>'
				    			      
				    			  });
				    			  markers[1] = new google.maps.Marker({
				    			      position: new google.maps.LatLng(<%=olat%>,<%=olng%>),
				    			      map: map,
				    			      title: '<%=OutletName%>',
				    			      icon: 'images/markers/letter_o.png'
				    			  });
				    			  
				    			  google.maps.event.addListener(markers[0], 'click', function() {
				    			    infowindow.open(map,markers[0]);
				    			    var infoWindowContent = "<%=OutletName%><br>"; 
				    			    infoWindowContent += "<a href='#'>View Detail</a>";
				    			    infowindow.setContent(infoWindowContent);
				    			    
				    			  });
				    			  
				    	}	 
				    		  
				    			 
				    		
				    	
				    	
				    	
				    	</script>
						<div id="map" style=""></div>			
					</li>
				</ul>
				<%
				}
			}else {
				
				
				
				
				if(IsMapShow2 && latt2 !=0 && lang2 !=0){
					%>
					<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-5px;" data-icon="false">
					
						<li data-role="list-divider" data-theme="a">Order Location</li>
						<li>
							
							<script>
					    	 initMap();
		
					    	function initMap(){			    		
					    		   var myLatlng = new google.maps.LatLng(<%=latt2%>,<%=lang2%>);
					    		   <%
						    		  System.out.println("2 => "+latt2+"-"+lang2);
						    		  %>
					    		  var mapOptions = {
					    		    zoom: 15,
					    		    center: myLatlng
					    		  };
		
					    		  var map = new google.maps.Map(document.getElementById('map'), mapOptions);
		
					    		  var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';
		
					    		  var infowindow = new google.maps.InfoWindow({
					    		      content: contentString,
					    		      maxWidth: 200
					    		  });
					    		  
					    		  var markers = new Array();
					    		  
					    			 
					    		map.setCenter(new google.maps.LatLng(<%=latt2%>,<%=lang2%>));
					    				  
					    			  
					    			  markers[0] = new google.maps.Marker({
					    			      position: new google.maps.LatLng(<%=latt2%>,<%=lang2%>),
					    			      map: map,
					    			      title: '<%=OutletName%>'
					    			      
					    			  });
					    			  markers[1] = new google.maps.Marker({
					    			      position: new google.maps.LatLng(<%=olat2%>,<%=olng2%>),
					    			      map: map,
					    			      title: '<%=OutletName%>',
					    			      icon: 'images/markers/letter_o.png'
					    			  });
					    			  
					    			  google.maps.event.addListener(markers[0], 'click', function() {
					    			    infowindow.open(map,markers[0]);
					    			    var infoWindowContent = "<%=OutletName%><br>"; 
					    			    infoWindowContent += "<a href='#'>View Detail</a>";
					    			    infowindow.setContent(infoWindowContent);
					    			    
					    			  });
					    			  
					    	}	 
					    		  
					    			 
					    		
					    	
					    	
					    	
					    	</script>
							<div id="map" style=""></div>			
						</li>
					</ul>
					<%
					}
			}
			%>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>