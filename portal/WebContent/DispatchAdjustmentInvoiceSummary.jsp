<%@page import="com.pbc.inventory.PromotionItem"%>
<%@page import="com.pbc.inventory.Product"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="com.pbc.inventory.SalesPosting"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 60;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();
Statement s2 = ds.createStatement();


%>
					<ul data-role="listview" data-inset="true"  style="font-size:10pt;margin-right:10px;margin-top:10px;">
						<li data-role="list-divider" data-theme="a">Invoice Summary</li>
						<li>
						



							<table style="width:100%;font-size: 10pt;">
	        			<thead style="text-align:left;">
	        				<tr>
	        					<th>Code</th>
	        					<th>Product</th>
	        					<th>Quantity</th>	        					
	        				</tr>
	        				
	        			</thead>
	        			<tbody>
	        				<% 	        				
	        				
	        				
	        				ResultSet rs2 = s.executeQuery("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku,ipv.product_id,sum(isip.total_units) units FROM pep.inventory_sales_invoices_products isip, inventory_products_view ipv where isip.id="+Utilities.parseLong(request.getParameter("invoiceid"))+" and isip.product_id=ipv.product_id and isip.is_promotion = 0 group by isip.product_id");
	        				while(rs2.next())
	        				{
	        				%>
	        				<tr>
	        					<td style="width:20%"><%=rs2.getString("sap_code") %></td>
	        					<td style="width:60%"><%=rs2.getString("package_label")+" - "+rs2.getString("brand_label")%></td>
	        					<td style="width:20%"><%=Utilities.convertToRawCases(rs2.getLong("units"), rs2.getInt("unit_per_sku"))   %></td>	        									
	        				</tr>
	        				<%
	        				}
	        				%>
	        			</tbody>
        			</table>

						</li>
						
						<li data-role="list-divider" data-theme="c">Promotion Items</li>						
						<li>							
						<table style="width:100%;font-size: 10pt;">
	        			<tbody>
	        				<%
	        				
	        				s.executeUpdate("create temporary table temp_promotion_adjustment (product_id int(4), package_id int(4), brand_id int(4), total_units int(10), promotion_id int(10) )");

	        				
		       				ArrayList<Integer> MatchPackageIDs = new ArrayList<Integer>();
		       				ArrayList<Integer> MatchBrandIDs = new ArrayList<Integer>();	        				
		       				ArrayList<Integer> PromotionIDs = new ArrayList<Integer>();	
		       				
	        				rs2 = s.executeQuery("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku,ipv.product_id,sum(isip.total_units) units, isip.promotion_id FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.id="+Utilities.parseLong(request.getParameter("invoiceid"))+" and isip.product_id=ipv.product_id and isip.is_promotion = 1 group by isip.product_id, isip.promotion_id");
	        				while(rs2.next())
	        				{
	        					MatchPackageIDs.add(rs2.getInt("package_id"));
	        					MatchBrandIDs.add(rs2.getInt("brand_id"));
	        					
	        					int PromotionID = rs2.getInt("promotion_id");
	        					
	        					s1.executeUpdate("insert into temp_promotion_adjustment (product_id, package_id, brand_id, total_units, promotion_id)values ("+rs2.getInt("product_id")+","+rs2.getString("package_id")+","+rs2.getString("brand_id")+", "+rs2.getString("units")+", "+PromotionID+")");
	        					
	        				%>
	        				<tr>
	        					<td style="width:20%"><%=rs2.getString("sap_code") %></td>
	        					<td style="width:60%"><%=rs2.getString("package_label")+" - "+rs2.getString("brand_label")%></td>
	        					<td style="width:20%"><%=Utilities.convertToRawCases(rs2.getLong("units"), rs2.getInt("unit_per_sku"))   %></td>	        									
	        				</tr>
	        				<%
	        				}
	        				
	        				rs2 = s.executeQuery("SELECT distinct isip.promotion_id FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.id="+Utilities.parseLong(request.getParameter("invoiceid"))+" and isip.product_id=ipv.product_id and isip.is_promotion = 1");
	        				while(rs2.next())
	        				{
	        					PromotionIDs.add(rs2.getInt(1));
	        				}
	        				
	        				
	        				int nMatchPackageIDs[] = ArrayUtils.toPrimitive(MatchPackageIDs.toArray(new Integer[MatchPackageIDs.size()]));
	        				int nMatchBrandIDs[] = ArrayUtils.toPrimitive(MatchBrandIDs.toArray(new Integer[MatchBrandIDs.size()]));
	        				%>
	        			</tbody>
        			</table>
						</li>						
						<li data-role="list-divider" data-theme="c">Promotions Cancelled</li>
						
						<li>
	        		<table style="width:100%;font-size: 10pt;">
	        			<tbody>
	        										
	       				<%
	       				
	       				ArrayList<Integer> ProductIDs = new ArrayList<Integer>();
	       				ArrayList<Long> TotalUnits = new ArrayList<Long>();
	       				
	       				
	       				
	       				rs2 = s.executeQuery("SELECT ipv.product_id, (sum(isip.total_units) -	ifnull((select sum(total_units) from inventory_sales_dispatch_adjusted_products where dispatch_id = "+Utilities.parseLong(request.getParameter("dispatchid"))+" and invoice_id = "+Utilities.parseLong(request.getParameter("invoiceid"))+" and product_id = ipv.product_id and is_promotion = 0),0)) net_qty"+
	       									 " FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.id = "+Utilities.parseLong(request.getParameter("invoiceid"))+" and isip.product_id = ipv.product_id and isip.is_promotion = 0 group by isip.product_id;");
	       				while(rs2.next()){
	       					ProductIDs.add(rs2.getInt(1));
	       					TotalUnits.add(rs2.getLong(2));
	       				}
	       				
	       				int nProductIDs[] = ArrayUtils.toPrimitive(ProductIDs.toArray(new Integer[ProductIDs.size()]));
	       				long nTotalUnits[] = ArrayUtils.toPrimitive(TotalUnits.toArray(new Long[ProductIDs.size()]));
	       				
	       				for (int k = 0; k < PromotionIDs.size(); k++){
	       					PromotionItem promotions[] = Product.getPromotionItemsMatched(PromotionIDs.get(k), nProductIDs, nTotalUnits, nMatchPackageIDs, nMatchBrandIDs);
	       					
		       				for (int i = 0; i < promotions.length; i++){
		       					
		       					int iBrandID = 0;
		       					if (promotions[i].BRANDS.size() > 0){
		       						iBrandID = promotions[i].BRANDS.get(0);
		       					}
		       					s1.executeUpdate("update temp_promotion_adjustment  set total_units = total_units - "+promotions[i].TOTAL_UNITS+" where package_id = "+promotions[i].PACKAGE_ID+" and brand_id = "+iBrandID+" and promotion_id = "+PromotionIDs.get(k));
		       					
		       				}
	       				}
	       				/*
	       				PromotionItem promotions[] = Product.getPromotionItems(Utilities.parseLong(request.getParameter("outletid")), nProductIDs, nTotalUnits);

	       				for (int i = 0; i < promotions.length; i++){
	       					//System.out.println("update temp_promotion_adjustment  set total_units = total_units - "+promotions[i].TOTAL_UNITS+" where package_id = "+promotions[i].PACKAGE_ID+" and total_units > 0 limit 1");
	       					s1.executeUpdate("update temp_promotion_adjustment  set total_units = total_units - "+promotions[i].TOTAL_UNITS+" where package_id = "+promotions[i].PACKAGE_ID+" and total_units > 0 limit 1");
	       					
	       				}
	       				*/
	       				
	       				
	       				ResultSet rs3 = s.executeQuery("select * from temp_promotion_adjustment");
	       				while(rs3.next()){
	       					//System.out.println(rs3.getInt("product_id") + " " + rs3.getLong("total_units"));
	       					
	       					ResultSet rs4 = s1.executeQuery("SELECT * FROM inventory_products_view where product_id = "+rs3.getInt("product_id"));
	       					if(rs4.first())
	       					{
	       						long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(rs3.getLong("total_units"), rs4.getInt("unit_per_sku")); // [0] rawcases , [1]=bottles
	       						long LiquInML = rs3.getLong("total_units")*rs4.getLong("liquid_in_ml");
	       						if(rs3.getLong("total_units") !=0)
	       						{
	       							s2.executeUpdate("replace into inventory_sales_dispatch_adjusted_products(dispatch_id,outlet_id,product_id,raw_cases,units,total_units,liquid_in_ml,invoice_id,is_promotion, promotion_id) values("+Utilities.parseLong(request.getParameter("dispatchid"))+","+Utilities.parseLong(request.getParameter("outletid"))+","+rs3.getInt("product_id")+","+RawCasesAndUnits[0]+","+RawCasesAndUnits[1]+","+rs3.getLong("total_units")+","+LiquInML+","+Utilities.parseLong(request.getParameter("invoiceid"))+",1, "+rs3.getLong("promotion_id")+")");
	       							
	       							boolean posted = SalesPosting.post(Utilities.parseLong(request.getParameter("invoiceid")), SessionUserID);
	       							
	       							
	       					%>
	       						<tr>
	        						<td style="width:20%"><%=rs4.getLong("sap_code")%></td>
	        						<td style="width:60%"><%=rs4.getString("package_label")+" - "+rs4.getString("brand_label")%></td>
	        						<td style="width:20%"><%=Utilities.convertToRawCases(rs3.getLong("total_units"), rs4.getInt("unit_per_sku"))%></td>	        									
	        					</tr>	 
	       					<%
	       						}
	       					}
	       					
	       					
	       					%>
	        				      					
	       					<%
	       					
	       				}
	       				
	       				%>

	        			</tbody>
        			</table>												
						</li>
						<%-- <li data-role="list-divider" data-theme="c">Brand Discount</li>
						<li>
						<table style="width:100%;font-size: 10pt;">
						<thead style="text-align:left;">
	        				<tr>
	        					<th>Brand</th>
	        					<th>Cartons</th>
	        					<th>Amount</th>					
	        				</tr>
	        			</thead>
	        			<tbody>
	        		
	        		<%
	        		long orderId=0;
	        		double brandAmount=0;
	        		System.out.println("select order_id, brand_discount_amount from inventory_sales_invoices where id="+Utilities.parseLong(request.getParameter("invoiceid")));
	        		ResultSet rsOrderID = s.executeQuery("select order_id, brand_discount_amount from inventory_sales_invoices where id="+Utilities.parseLong(request.getParameter("invoiceid")));
	        		if(rsOrderID.first()){
	        			orderId=rsOrderID.getLong("order_id");
	        			brandAmount= rsOrderID.getDouble("brand_discount_amount");
	        		}
						if(brandAmount!=0){
							ResultSet rsBrandDiscount = s2.executeQuery("select (select label from inventory_products_lrb_types lrb where lrb.id=mobd.brand_id) as brand, mobd.cartons, mobd.brand_discount_amount from mobile_order_brand_discount mobd  where mobd.id="+orderId);
							while(rsBrandDiscount.next()){
						%>
						<tr>
						<td  ><%=rsBrandDiscount.getString("brand")%></td>
						<td ><%=rsBrandDiscount.getInt("cartons")%></td>
						<td ><%=Utilities.getDisplayCurrencyFormat(rsBrandDiscount.getDouble("brand_discount_amount"))%></td>
						</tr>	
						<%}} %>
	        			</tbody>
	        			</table>
						</li> --%>
					</ul>

<%
s.close();
ds.dropConnection();
%>