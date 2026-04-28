<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>

<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}
</script>

<%


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

long DispatchID = Utilities.parseLong(request.getParameter("DispatchID"));
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

%>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top" style="width:30%">
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Invoices</li>
				
				
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="#" onclick="redirect('DispatchInvoicePrinting.jsp');" data-ajax="false" target="_blank">Print Invoices (English)</a></li>
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="#" onclick="redirect('DispatchInvoicePrintingUrdu.jsp');" data-ajax="false" target="_blank">Print Invoices (Urdu)</a></li>
				
				<%
					
				
					int counter = 0;
					String InvoiceIDs = "SELECT sales_id FROM inventory_sales_dispatch_invoices where id="+DispatchID;
					
					counter = 0;
					
					ResultSet rs2 = s.executeQuery("SELECT id, outlet_id, (select name from common_outlets where id=inventory_sales_invoices.outlet_id) outlet_name, net_amount FROM inventory_sales_invoices where id in ("+InvoiceIDs+") order by net_amount desc ");
					while(rs2.next()){
						%>
						<li style="font-size:13px; font-weight:normal"><%=rs2.getString("outlet_id")+" - "+rs2.getString("outlet_name")%><span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("net_amount"))%></span></li>
						<%
						counter++;
					}
				
					if(counter == 0){
						%>
						<li style="font-size:13px">&nbsp;</li>
						<%
					}
					
				%>
				</ul>
		</td>
		<td valign="top" style="width:30%; padding-left:5px">
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Summary</li>
				
				<!-- DispatchInvoicePrinting_tax -->
				
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="#" onclick="redirect('DispatchPickList.jsp');" data-ajax="false" target="_blank">Print Pick List (English)</a></li>
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="#" onclick="redirect('DispatchPickListUrdu.jsp');" data-ajax="false" target="_blank">Print Pick List (Urdu)</a></li>
				
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="#" onclick="redirect('DispatchInvoicePrinting_tax.jsp');" data-ajax="false" target="_blank">Print Pick List (English)</a></li>
				
				
				<li style="font-size:13px">
				
					<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <!-- <thead>
					    <tr>
					    	
							
							<th data-priority="2" >&nbsp;</th>
							
							<th data-priority="1"  style="text-align:right">Quantity</th>
							
							
					    </tr>
					  </thead>
					 -->
				<%
					ResultSet rs3 = s.executeQuery("SELECT isip.id, isip.product_id, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id in ("+InvoiceIDs+") group by ipv.package_id order by ipv.package_sort_order");
					while(rs3.next()){
						%>
						<tr style="background:#ececec">
	   	            		<td align="left"><%=rs3.getString("package_label")%></td>
	   	            		<td style="text-align:right; padding-right:10px"><b><%=Utilities.convertToRawCases(rs3.getLong("bottles"), rs3.getInt("unit_per_sku"))%></b></td>
	   	            	</tr>
						<%
						
						ResultSet rs4 = s2.executeQuery("SELECT isip.id, isip.product_id, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id in ("+InvoiceIDs+") and ipv.package_id="+rs3.getString("package_id")+" group by ipv.brand_id order by ipv.brand_label");
						while(rs4.next()){
							%>
							
							<tr>
		    					<td style="padding-left:20px"><%=rs4.getString("brand_label")%></td>
		    					<td style="text-align:right; padding-right:10px"><%=Utilities.convertToRawCases( rs4.getLong("bottles"), rs4.getInt("unit_per_sku"))%></td>
		    		    	</tr>
							
							<%
						}
						
					}
				%>
					</table>
				</li>
			
			</ul>
		</td>
	</tr>
</table>

<form name="check" id="check" action="DispatchInvoicePrinting.jsp" method="post" data-ajax="false" target="_blank">
	<input type="hidden" name="DispatchID" value="<%=DispatchID%>" >
</form>



<%
s2.close();
s.close();
c.close();
ds.dropConnection();
%>