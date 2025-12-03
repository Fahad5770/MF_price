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

long DispatchID = Utilities.parseLong(request.getParameter("dispatchid"));

%>
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-right:10px; margin-top:10px;">
				<li data-role="list-divider" data-theme="a">Reconciliation Status</li>
				<li>
					<table style="width:100%;font-size: 10pt;">
	        			<thead style="text-align:left;">
	        			
	        				<tr>
	        					<th>Code</th>
	        					<th>Product</th>
	        					<th>Unadjusted</th>
	        					<th></th>
	        				</tr>
	        				
	        			</thead>
	        			<tbody>
	        				<% 	        				
	        				ResultSet rs2 = s.executeQuery("select ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, ep.product_id, sum(case when type='extra' then ep.units else 0 end) extra, sum(case when type='adjusted_extra' then ep.units else 0 end) adjusted_extra, sum(case when type='total_returns' then ep.units else 0 end) total_returns, sum(case when type='adjusted_returns' then ep.units else 0 end) adjusted_returns from ("+
								"SELECT isdep.product_id, sum(isdep.total_units) units, 'extra' type FROM inventory_sales_dispatch_extra_products isdep where isdep.dispatch_id = "+DispatchID+" group by isdep.product_id"+ 
								" union all "+ 
								"select isip.product_id, sum(isip.total_units) units, 'adjusted_extra' type from inventory_sales_dispatch_extra_invoices isdei join inventory_sales_invoices_products isip on isdei.sales_id = isip.id where isdei.id = "+DispatchID+" group by isip.product_id"+
								" union all "+
								"SELECT isp.product_id, sum(isp.total_units) units, 'total_returns' FROM inventory_sales_dispatch_returned_products isp where isp.dispatch_id = "+DispatchID+" and isp.is_empty = 0 group by isp.product_id"+
								" union all "+
								"SELECT product_id, sum(total_units), 'adjusted_returns' FROM inventory_sales_dispatch_adjusted_products where dispatch_id="+DispatchID+" group by product_id"+
							") ep join inventory_products_view ipv on ep.product_id = ipv.product_id group by ep.product_id");
	        				
	        				while(rs2.next()){
	        					
	        					long extra = rs2.getLong("extra");
	        					long AdjustedExtra = rs2.getLong("adjusted_extra");
	        					
	        					long difference =  rs2.getLong("total_returns") - ((rs2.getLong("extra") - rs2.getLong("adjusted_extra")) + rs2.getLong("adjusted_returns"));
	        					
	        					
	        					
	        					boolean isNegative = false;
	        					if (difference < 0){
	        						isNegative = true;
	        						difference = difference*-1;
	        					}
	        					

	        					
	        					if (difference != 0){
	        				%>
	        				<input type="hidden" name="ReconciliationSummaryProducts" id="ReconciliationSummaryProducts" value="<%=rs2.getString("sap_code") %>" >
	        				<tr>
	        					<td style="width:15%"><%=rs2.getString("sap_code") %></td>
	        					<td style="width:50%" nowrap="nowrap"><%=rs2.getString("package_label")+" - "+rs2.getString("brand_label")%></td>
	        					<td style="width:15%"><%=Utilities.convertToRawCases(difference, rs2.getInt("unit_per_sku"))   %></td>
	        					<td style="padding-left: 5px"><%if (isNegative){ out.print("Underinvoiced"); }else{ out.print("Overinvoiced"); }%></td>
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
s.close();
ds.dropConnection();
%>