<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.inventory.Product"%>
<%@page import="com.pbc.inventory.BackOrderProduct"%>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));



long OrderIDs[] = Utilities.parseLong(request.getParameterValues("OrderID"));
long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));

BackOrderProduct items[] = Product.getBackOrderProducts(DistributorID, OrderIDs);

if(items != null && items.length > 0){
	
	Datasource ds = new Datasource();
	ds.createConnection();
	Connection c = ds.getConnection();
	Statement s = c.createStatement();

%>
	<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
		<li data-role="list-divider">Back Order Summary</li>
		<li>
		<table border="0" cellspacing="0" style="font-size:13px; width:100%;"  data-mode="reflow" class="ui-responsive table-stroke">
		
		<thead>
			<tr>
				<th align="left">Product</th>
				<th align="right">Available Stock</th>
				<th align="right">This Order</th>
				<th align="right">Already Invoiced</th>
				<th align="right">Short Stock</th>
			</tr>
		</thead>
		<thead>
			<tr>
				<th colspan="5">&nbsp;</th>
			</tr>
		</thead>
		<%
		for(int i = 0; i < items.length; i++){
			
			String Product = "";
			int UnitsPerSKU = 0;
			System.out.println("SELECT concat(package_label, ' ', brand_label) product, unit_per_sku FROM inventory_products_view where product_id="+items[i].PRODUCT_ID);
			ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product, unit_per_sku FROM inventory_products_view where product_id="+items[i].PRODUCT_ID);
			if(rs.first()){
				Product = rs.getString("product");
				UnitsPerSKU = rs.getInt("unit_per_sku");
			}
			
			System.out.println("Product ID : "+items[i].PRODUCT_ID + "-"+Product);
			System.out.println("Unit per SKU : "+UnitsPerSKU);
			System.out.println("Available : "+items[i].UNITS_AVAILABLE);
			System.out.println("ORDERED : "+items[i].UNITS_ORDERED);
			System.out.println("INVOICED : "+items[i].UNITS_INVOICED);
			System.out.println("SHORT : "+items[i].UNITS_SHORT);
		%>
			<tr>
				<td nowrap="nowrap"><%=Product%></td>
				<td align="right"><%= Utilities.convertToRawCases(items[i].UNITS_AVAILABLE, UnitsPerSKU) %></td>
				<td align="right"><%=Utilities.convertToRawCases(items[i].UNITS_ORDERED, UnitsPerSKU)%></td>
				<td align="right"><%=Utilities.convertToRawCases(items[i].UNITS_INVOICED, UnitsPerSKU)%></td>
				<td align="right"><%=Utilities.convertToRawCases(items[i].UNITS_SHORT, UnitsPerSKU)%></td>
			</tr>
		<%
		}
		%>
		</table>
		
		</li>
		
		
		
		
	</ul>
<%

c.close();
ds.dropConnection();

}
%>