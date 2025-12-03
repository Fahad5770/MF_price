<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.inventory.Product"%>
<%@page import="com.pbc.inventory.BackOrderProduct"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.pbc.util.AlmoizFormulas"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

int OrderID = Utilities.parseInt(request.getParameter("OrderID"));

%>
		<%
		
		double InvoiceAmount = 0;
		double TotalWHTaxAmount = 0;
		double NetAmount = 0;
		long UserID=0, outletId=0;
		java.util.Date CreatedOn = null;	
		
		ResultSet rs4 = s.executeQuery("SELECT outlet_id,created_by,invoice_amount, wh_tax_amount, net_amount, created_on FROM mobile_order where id="+OrderID);
		if(rs4.first()){
			InvoiceAmount = rs4.getDouble("invoice_amount");
			//WHTaxAmount = rs4.getDouble("wh_tax_amount");
			NetAmount = rs4.getDouble("net_amount");
			CreatedOn = rs4.getTimestamp("created_on");
			UserID= rs4.getLong("created_by");
			outletId= rs4.getLong("outlet_id");
		}
		
	
        
        
       
		%>
	<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
		<li data-role="list-divider">Order<div style="float: right"><%= com.pbc.util.Utilities.getDisplayDateTimeFormat(CreatedOn) %></div></li>
		<li>
		<table border="0" cellspacing="0" style="font-size:13px; width:100%;">
		<tr>
		<td></td><td style="text-align: right;">Order ID: <%= OrderID%></td>
		</tr>
		</table>
		<table border="0" cellspacing="0" style="font-size:13px; width:100%;"  data-mode="reflow" class="ui-responsive table-stroke">
		
		<thead>
			<tr>
				<th align="left">Product</th>
				<th align="right">Quantity</th>
				<th align="right">Rate</th>
				<th align="right">Tax</th>
				<th align="right">Amount</th>
			</tr>
		</thead>
		<thead>
			<tr>
				<th colspan="4">&nbsp;</th>
			</tr>
		</thead>
		
		<%
		
		double totalWH=0, totalSalesTax=0;
		double brandDiscount = 0.0;
		ResultSet rs3 = s.executeQuery("SELECT mo.brand_discount_amount,mop.wh_tax_amount,(select lrb_type_id from inventory_products where id=product_id) lrb_type_id,product_id, (select package_id from inventory_products where id=product_id) package_id, (select label from inventory_packages where id=package_id) package_name, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_brands where id=brand_id) brand_name, total_units, rate_raw_cases, mop.total_amount, mo.created_on, mop.is_promotion FROM mobile_order_products mop join mobile_order mo on mop.id = mo.id where mo.id="+OrderID+" order by mop.is_promotion");
		while(rs3.next()){
			
			double WHTaxAmount=0;
			
			int isPromotion = rs3.getInt("is_promotion");
			if(rs3.getInt("lrb_type_id")==2) {
				WHTaxAmount += rs3.getDouble("wh_tax_amount");
			}
			
			 brandDiscount = rs3.getDouble("brand_discount_amount");
			
			
			 double saleTax = 0.0;
		     double wh_tax_amount = 0.0;
		      //  double[] ProductTax = new double[2];
		        
		        if(UserID == 9867) {
		        /******************* Tax Calculation *********************/
		        
		  //  HashMap<String, Double>    ProductTax = AlmoizFormulas.ProductTax(rs3.getInt("product_id"), outletId);
		       /*  System.out.println("Units : "+rs3.getLong("total_units"));
                saleTax = ProductTax[0] * (double) rs3.getLong("total_units");
                incomeTax = ProductTax[1] * (double) rs3.getLong("total_units"); */
                saleTax = rs3.getDouble("sales_tax_amount");
                wh_tax_amount = rs3.getDouble("wh_tax_amount");
               
                totalWH += wh_tax_amount;
                totalSalesTax += saleTax;
		        /******************* Tax Calculation *********************/
		        }/* else {
		          if (rs3.getInt("lrb_type_id") !=1) {
		          WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(rs3.getDouble("total_amount") * WHTaxRate / 100.0));
		      }else {
		     	 WHTaxAmount = rs3.getDouble("total_units") * AlmoizFormulas.BeetaTaxValue();
		      }
		      if (rs3.getInt("lrb_type_id") == 5 || rs3.getInt("lrb_type_id") == 6 ) {
		          WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(rs3.getDouble("total_amount") * WHTaxRate / 100.0));
		      }
		      else {
		          WHTaxRate = 0.0;
		      }
		        } */
		        
		    
			%>
			<tr>
				<td><%if (isPromotion == 1){%><i><%}%><%=rs3.getString("package_name")+" - "+rs3.getString("brand_name")%></td>
				<td align="right"><%=Utilities.convertToRawCases(rs3.getLong("total_units"), rs3.getInt("unit_per_case"))%></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(rs3.getDouble("rate_raw_cases"))%></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(WHTaxAmount)%></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(rs3.getDouble("total_amount"))%></td>
			</tr>
			<%
		}
		%>
		</table>
		

		<div align="right">
		<table cellspacing="0" border="0" style="font-size:13px; width:50%;">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="right"><b>Invoice Amount</b></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(InvoiceAmount-(totalWH + totalSalesTax)) %></td>
			</tr>
			<tr>
				<td align="right"><b>W.H. Tax</b></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(totalWH)%></td>
			</tr>
			<tr>
				<td align="right"><b>Sales Tax</b></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(totalSalesTax)%></td>
			</tr>
			<tr>
				<td align="right"><b>Brand Discount</b></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(brandDiscount)%></td>
			</tr>
			<tr>
				<td align="right"><b>Net Amount</b></td>
				<td align="right"><%=Utilities.getDisplayCurrencyFormat(NetAmount - brandDiscount )%></td>
			</tr>
			
		</table>
		</div>
		
		
		
		
		
		</li>
		<%
		long OrderIDs[] = {OrderID};
		
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		BackOrderProduct items[] = Product.getBackOrderProducts(DistributorID, OrderIDs);
		if(items != null && items.length > 0){
			
		
		%>
			<li data-role="list-divider" data-theme="c">Back Order</li>
			<li>
				<table border="0" cellspacing="0" style="font-size:13px; width:100%;"  data-mode="reflow" class="ui-responsive table-stroke">
			
					<thead>
						<tr>
							<th align="left">Product</th>
							<th align="right">Quantity</th>
							
						</tr>
					</thead>
					<thead>
						<tr>
							<th colspan="4">&nbsp;</th>
						</tr>
					</thead>
					
					<%
					for(int i = 0; i < items.length; i++){
						
						String Product = "";
						int UnitsPerSKU = 0;
						
						ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product, unit_per_sku FROM inventory_products_view where product_id="+items[i].PRODUCT_ID);
						if(rs.first()){
							Product = rs.getString("product");
							UnitsPerSKU = rs.getInt("unit_per_sku");
						}
						
					%>
						<tr>
							<td nowrap="nowrap"><%=Product%></td>
							<td align="right"><%= Utilities.convertToRawCases(items[i].UNITS_SHORT_THIS_ORDER, UnitsPerSKU) %></td>
							
							
						</tr>
					<%
					}
					%>
					
				</table>
			</li>
		<%
		}
		%>
		<li data-role="list-divider" data-theme="c">Action</li>
		<li>
		
			<table>
				<tr>
					<td><input type="button" value="Invoice" data-mini="true" data-theme="a" onclick="OrderInvoicingDoInvoice('&OrderID=<%=OrderID%>' )" ></td>
					<td><input type="button" value="Cancel" data-mini="true" onclick="OrderInvoicingDoCancel(<%=OrderID%>)" ><!--  <input type="button" value="Edit Order" data-mini="true" onclick="OrderInvoicingDoEditAndInvoice(<%=OrderID%>)" >  --></td>
					<td></td>
				</tr>
			</table>
		
		</li>
		
		
		
	</ul>
<%

c.close();
ds.dropConnection();
%>