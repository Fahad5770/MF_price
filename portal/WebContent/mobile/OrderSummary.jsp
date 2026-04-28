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

long UserID = Utilities.parseLong(request.getParameter("UserID"));
String Password = Utilities.filterString(request.getParameter("Identity"), 1, 100);
String DeviceID = Utilities.filterString(request.getParameter("UVID"), 1, 100);

long OutletID = Utilities.parseLong(request.getParameter("OutletID"));


int FeatureID = 65;


boolean isDeviceValid = UserAccess.isMobileDeviceValid(DeviceID);
if(isDeviceValid){
	boolean isUserValid = UserAccess.isMobileUserValid(UserID, Password);
	if(isUserValid){
		if(Utilities.isAuthorized(FeatureID, UserID) == false){
			response.sendRedirect("AccessDenied.jsp");
		}
	}else{
		response.sendRedirect("AccessDenied.jsp");
	}
}else{
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();


Date StartDate = Utilities.parseDate(request.getParameter("FromDate"));
Date EndDate = Utilities.parseDate(request.getParameter("ToDate"));

if(request.getParameter("FromDate") == null){
	StartDate = new Date();
}

if(request.getParameter("ToDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.outlet_id in (SELECT ebps.outlet_id FROM employee_beat_plan ebp, employee_beat_plan_schedule ebps where ebp.beat_plan_id=ebps.beat_plan_id and ebp.assigned_to="+UserID+") and mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);

if(OutletID > 0){
	OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.outlet_id in ("+OutletID+") and mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
}
//System.out.println(OrderIDsQuery);
String InvoiceIDsQuery = "select distinct id from inventory_sales_invoices where order_id in ("+OrderIDsQuery+")";

%>

<table style="width: 100%" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					 <thead>
					    <tr style="font-size: 11px;">
							<th data-priority="2" style="width: 20%" >&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width: 14%" colspan="2">Order</th>
							
							<th data-priority="1"  style="text-align:center; width: 14%" colspan="2">Sales</th>
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
					ResultSet rs = s.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where category_id=1 order by package_sort_order");
					while(rs.next()){
						
						int PackageID = rs.getInt("package_id");
						
						%>
						<tr style="background:#6facd5">
	   	            		<td align="left" style="color: #fff; padding-left: 5px; border-right: 1px solid #6facd5" colspan="5"><%=rs.getString("package_label")%></td>
	   	            	</tr>
						<%
						ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id, brand_label FROM inventory_products_view where is_visible = 1 and category_id = 1 and package_id="+PackageID+" order by brand_label");
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
									
							
							
							String QuantitySales = "";
							double AmountSales = 0;
							ResultSet rs7 = s3.executeQuery("SELECT isa.id, isap.product_id, ipv.package_id, ipv.brand_id, sum(isap.total_units) bottles, sum(if (isap.is_promotion=1, 0, isap.net_amount)) amount, ipv.unit_per_sku FROM inventory_sales_adjusted isa, inventory_sales_adjusted_products isap, inventory_products_view ipv where isa.id=isap.id and isap.product_id=ipv.product_id and isa.id in ("+InvoiceIDsQuery+") and ipv.package_id="+PackageID+" and ipv.brand_id="+BrandID+" and ipv.category_id = 1");
							if(rs7.first()){
								QuantitySales = Utilities.convertToRawCases( rs7.getLong("bottles"), rs7.getInt("unit_per_sku"));
								AmountSales = rs7.getDouble("amount");
							}
							TotalAmountSales += AmountSales;
							
							
							
							%>
							
							<tr style="font-size: 12px;">
		    					<td style="padding-left:20px"><%=rs2.getString("brand_label")%></td>
		    					<td style="text-align:right; border-left: 1px solid #606060; border-right: 1px solid #606060; padding-right: 5px"><%=QuantityOrdersBooked%></td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px"><%if (AmountOrdersBooked!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(AmountOrdersBooked));} %></td>
		    					
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px"><%=QuantitySales%></td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 0px solid #606060; padding-right: 5px"><%if (AmountSales!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(AmountSales));} %></td>
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
						<tr style="background:#6facd5">
	   	            		<td align="left" style="color: #fff; padding-left: 5px" colspan="5">Total</td>
	   	            	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" colspan="2">Total</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px"><%if (TotalOrderAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount));} %></td>
		    					
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" acolspan="2">Total</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 0px solid #606060; padding-right: 5px"><%if (TotalInvoiceAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceAmount));} %></td>
		    		    	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" colspan="2">W.H. Tax</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px"><%if (TotalOrderWHTax!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderWHTax));} %></td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" acolspan="2">W.H. Tax</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 0px solid #606060; padding-right: 5px"><%if (TotalInvoiceWHTax!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceWHTax));} %></td>
		    		    	</tr>				
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" colspan="2">Discount</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px"></td>
		    					
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" acolspan="2">Discount</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 0px solid #606060; padding-right: 5px"><%if (TotalInvoiceDiscount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceDiscount));} %></td>
		    		    	</tr>		
							<tr style="font-size: 12px;">
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" colspan="2">Net</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px"><%if (TotalOrderNetAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalOrderNetAmount));} %></td>
		    					
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 1px solid #606060; padding-right: 5px" acolspan="2">Net</td>
		    					<td style="text-align:right; border-left: 0px solid #606060; border-right: 0px solid #606060; padding-right: 5px"><%if (TotalInvoiceNetAmount!=0){ out.print(Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceNetAmount));} %></td>
		    		    	</tr>				
		    		    			
				</table>
		</td>
	</tr>
</table>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>