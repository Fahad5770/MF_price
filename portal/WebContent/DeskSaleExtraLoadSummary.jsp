<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>

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

			<ul data-role="listview" data-inset="true"  style="font-size:9pt; margin-left: 20px; margin-top: -10px; margin-right: 10px">
				<li data-role="list-divider" data-theme="a">Extra Load Summary</li>
				<li>
					<table style="width:100%;font-size: 9pt;">
	        			<thead style="text-align:left;">
	        				<tr>
	        					<th>Code</th>
	        					<th>Product</th>
	        					<th>Available</th>
	        					<th>Invoiced</th>
	        					<!-- <th>Unadjusted</th> -->
	        				</tr>
	        				
	        			</thead>
	        			<tbody>
	        				<% 	      
	        				long DispatchID = Utilities.parseLong(request.getParameter("dispatchid"));
	        				
	        				long SumTotalUnits =0;
	        				int UnitPerSKUU=0;
	        				
	        				ResultSet rs2 = s.executeQuery("select ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, ep.product_id, sum(case when type='e' then ep.units else 0 end) extra_units, sum(case when type='a' then ep.units else 0 end) adjusted_units from ("+
	        						"SELECT isdep.product_id, sum(isdep.total_units) units, 'e' type FROM inventory_sales_dispatch_extra_products isdep where isdep.dispatch_id = "+DispatchID+" group by isdep.product_id union all select isip.product_id, sum(isip.total_units) units, 'a' type from inventory_sales_dispatch_extra_invoices isdei join inventory_sales_invoices_products isip on isdei.sales_id = isip.id where isdei.id = "+DispatchID+" group by isip.product_id"+
	        						") ep join inventory_products_view ipv on ep.product_id = ipv.product_id where ipv.category_id = 1 group by ep.product_id");
	        				while(rs2.next()){
	        				%>
	        				
	        				<input type="hidden" name="DeskSaleExtraLoadSummaryProducts" id="DeskSaleExtraLoadSummaryProducts" value="<%=rs2.getString("sap_code") %>" >
	        				
	        				<tr>
	        					<td style="width:10%"><%=rs2.getString("sap_code") %></td>
	        					<td style="width:50%"><%=rs2.getString("package_label")+" - "+rs2.getString("brand_label")%></td>
	        					<td style="width:15%"><%=Utilities.convertToRawCases(rs2.getLong("extra_units"), rs2.getInt("unit_per_sku"))   %></td>
	        					
        						<td style="width:15%">
        							<%=Utilities.convertToRawCases(rs2.getLong("adjusted_units"), rs2.getInt("unit_per_sku")) %>
        						</td>
        						<!-- <td style="width:15%">
        							<%//=Utilities.convertToRawCases((rs2.getLong("extra_units")-rs2.getLong("adjusted_units")), rs2.getInt("unit_per_sku"))   %>
        						</td>
        						 -->	        						        					
	        				</tr>
	        				<%
	        				}
	        				%>
	        				
	        				<tr>
	        					<td colspan="4"><b>Short Empty</b></td>
	        				</tr>
	        				
	        				<%
	        				
	        				ResultSet rs1 = s1.executeQuery("select empty_product_id, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, sum(total_units-units_returned) total_units, ifnull((select sum(isip.total_units) units from inventory_sales_dispatch_extra_invoices isdei join inventory_sales_invoices_products isip on isdei.sales_id = isip.id where isdei.id = "+DispatchID+" and isip.product_id = tab1.empty_product_id),0) units_invoiced, ifnull((SELECT sum(total_units) FROM inventory_sales_dispatch_returned_products where dispatch_id="+DispatchID+" and product_id=tab1.empty_product_id and is_empty=1),0) units_returned from ("+
									" select empty_product_id, sum(total_units) total_units, ifnull((select sum(total_units) from inventory_sales_dispatch_returned_products isdrp join inventory_products_map ipm on isdrp.product_id = ipm.product_id where isdrp.dispatch_id = "+DispatchID+" and ipm.empty_product_id = tab1.empty_product_id),0) units_returned from ("+
											" SELECT isip.product_id, ipm.empty_product_id, isip.total_units FROM inventory_sales_invoices_products isip join inventory_products_map ipm on isip.product_id=ipm.product_id where isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+DispatchID+") union all select isdep.product_id, ipm.empty_product_id, isdep.total_units from inventory_sales_dispatch_extra_products isdep join inventory_products_map ipm on isdep.product_id=ipm.product_id where isdep.dispatch_id = "+DispatchID+
											" ) tab1 group by empty_product_id"+		        							
							" ) tab1 join inventory_products_view ipv on tab1.empty_product_id = ipv.product_id group by empty_product_id");
	        				while(rs1.next()){
	        					
	        					SumTotalUnits=0;
	        					UnitPerSKUU=0;
    							//System.out.println("Hello "+rs.getLong("units"));
    							long TotalUnits15 = (rs1.getLong("total_units") - rs1.getLong("units_returned"));
	        					long InvoicedTotalUnits = rs1.getLong("units_invoiced");
	        					if (TotalUnits15 != 0){
	    							%>
	    							
	    							<input type="hidden" name="DeskSaleExtraLoadSummaryProducts" id="DeskSaleExtraLoadSummaryProducts" value="<%=rs1.getString("sap_code") %>" >
	    							
	    	        				<tr>
	    	        					<td><%=rs1.getString("sap_code") %><input type="hidden" name="DispatchReturnsSummaryProductSapCode" value="<%=rs1.getString("sap_code") %>"/></td>
	    	        					<td><%=rs1.getString("package_label")+" - "+rs1.getString("brand_label")%></td>
	    	        					<td><%=Utilities.convertToRawCases(TotalUnits15, rs1.getInt("unit_per_sku")) %><input type="hidden" name="DispatchReturnsSummaryTotalUnitsHidden" value="<%=rs1.getString("sap_code") %>|<%=TotalUnits15 %>"/></td>		    	        					
	    	        					<td><%=Utilities.convertToRawCases(InvoicedTotalUnits, rs1.getInt("unit_per_sku")) %></td>	
	    	        				
	    	        				</tr>
	    	        				<%
	        					}
    							
	        				}
	        				
	        				%>
	        			</tbody>
        			</table>

			     </li>

			</ul>


<%
s2.close();
s1.close();
s.close();
ds.dropConnection();
%>