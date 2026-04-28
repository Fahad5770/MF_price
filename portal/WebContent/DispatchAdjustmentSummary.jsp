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


%>
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-right:10px; margin-top:10px;">
				<li data-role="list-divider" data-theme="a">Return Summary</li>
				<li>
					<table style="width:100%;font-size: 10pt;">
	        			<thead style="text-align:left;">
	        				<tr>
	        					<th>Code</th>
	        					<th>Product</th>
	        					<th>Total Return</th>
	        					<th>Adjusted</th>
	        				</tr>
	        				
	        			</thead>
	        			<tbody>
	        				<% 	        				
	        				ResultSet rs2 = s.executeQuery("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku,ipv.product_id, sum(isp.total_units) units FROM inventory_sales_dispatch_returned_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.dispatch_id = "+Utilities.parseLong(request.getParameter("dispatchid"))+" and isp.is_empty = 0 group by ipv.sap_code");
	        				while(rs2.next())
	        				{
	        					ResultSet rs3 = s1.executeQuery("SELECT sum(total_units) Adjusted FROM inventory_sales_dispatch_adjusted_products where dispatch_id="+Utilities.parseLong(request.getParameter("dispatchid"))+" and product_id="+Utilities.parseLong(rs2.getString("product_id")));
	        				%>
	        				<tr>
	        					<td style="width:15%"><%=rs2.getString("sap_code") %></td>
	        					<td style="width:45%"><%=rs2.getString("package_label")+" - "+rs2.getString("brand_label")%></td>
	        					<td style="width:20%"><%=Utilities.convertToRawCases(rs2.getLong("units"), rs2.getInt("unit_per_sku"))   %></td>
	        					<%
	        						if(rs3.first())
	        						{
	        					%>
	        						<td style="width:20%">
	        							<%=Utilities.convertToRawCases(rs3.getLong("Adjusted"), rs2.getInt("unit_per_sku"))%>
	        							
	        							<input type="hidden" name="DispatchedTotalUnits" id="DispatchedTotalUnits" value="<%=rs2.getLong("units")%>"/>
	        							<input type="hidden" name="AdjustedTotalUnits" id="AdjustedTotalUnits" value="<%=rs3.getLong("Adjusted")%>"/>
	        							
	        						</td>	
	        					<%
	        						}
	        					%>	        					
	        				</tr>
	        				<%
	        				}
	        				%>
	        			</tbody>
        			</table>

			     </li>
			     <li data-role="list-divider" data-theme="c">Short Returns</li>
				<li>
					<table style="width:100%;font-size: 10pt;">
	        			
	        			<tbody>
	        				<% 	        				
	        				//ResultSet rs3 = s.executeQuery("select ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku,ipv.product_id, sum(isdap.total_units) units FROM inventory_sales_dispatch_adjusted_products isdap, inventory_products_view ipv where isdap.product_id = ipv.product_id and is_promotion=1 and isdap.dispatch_id = "+Utilities.parseLong(request.getParameter("dispatchid"))+" group by ipv.sap_code");
	        				ResultSet rs3 = s.executeQuery("select adj.product_id, sum(adj.total_units) adjusted, ifnull((select sum(total_units) from inventory_sales_dispatch_returned_products where product_id = adj.product_id and dispatch_id  = adj.dispatch_id and is_empty = 0),0) ret, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku from inventory_sales_dispatch_adjusted_products adj, inventory_products_view ipv where adj.dispatch_id = "+Utilities.parseLong(request.getParameter("dispatchid"))+" and adj.product_id = ipv.product_id group by adj.product_id");
	        				while(rs3.next()){	 
	        					
	        					long NetShort = rs3.getLong("ret") - rs3.getLong("adjusted");
	        					if (NetShort < 0){
	        					
	        				%>
	        				<tr>
	        					<td style="width:15%"><%=rs3.getString("sap_code") %></td>
	        					<td style="width:45%"><%=rs3.getString("package_label")+" - "+rs3.getString("brand_label")%></td>
	        					<td style="width:20%"><%=Utilities.convertToRawCases((NetShort*-1), rs3.getInt("unit_per_sku"))   %></td>
	        					<td style="width:20%">&nbsp;</td>				
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