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
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

long DispatchID = Utilities.parseLong(request.getParameter("DispatchID"));

%>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top" style="width:30%">
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Invoices</li>
				
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="javascript:redirect('DispatchAdjustedInvoicePrinting.jsp');" data-ajax="false" target="_blank">Print Invoices</a></li>
				
				<%
					
				
					int counter = 0;
					String InvoiceIDs = "SELECT sales_id FROM inventory_sales_dispatch_invoices where id="+DispatchID;
					String InvoiceIDs2 = "SELECT sales_id FROM inventory_sales_dispatch_extra_invoices where id = "+DispatchID;
					counter = 0;
					
					ResultSet rs2 = s.executeQuery("SELECT id, outlet_id, (select name from common_outlets where id=isa.outlet_id) outlet_name, net_amount FROM inventory_sales_adjusted isa where id in (select sales_id from ("+InvoiceIDs+" union "+InvoiceIDs2+") tab2) order by net_amount desc ");
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
				
				<li style="font-size:13px; font-weight:normal" data-theme="d"><a href="javascript: redirect('DispatchAdjustedPickList.jsp');" data-ajax="false" target="_blank">Print Pick List</a></li>
				
				<li style="font-size:13px">
				
					<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <!-- <thead>
					    <tr>
					    	
							
							<th data-priority="2" >&nbsp;</th>
							
							<th data-priority="1"  style="text-align:right">Quantity</th>
							
							
					    </tr>
					  </thead>
					 -->
				
					</table>
				</li>
			
			</ul>
		</td>
	</tr>
</table>

<form name="check" id="check" action="DispatchAdjustedInvoicePrinting.jsp" method="post" data-ajax="false" target="_blank">
	<input type="hidden" name="DispatchID" value="<%=DispatchID%>" >
</form>



<%
s2.close();
s.close();
c.close();
ds.dropConnection();
%>