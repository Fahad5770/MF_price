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
int FeatureID = 72;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();


int[] RawCases=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormRawCases"));
long[] Units=Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormUnits"));
long[] LiquidInMl=Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormLiquidInML"));
int[] UnitPerSKU = Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormUnitPerSKU"));
long[] TotalUnits = Utilities.parseLong(request.getParameterValues("TotalUnitsCalculatedForSummary"));
int[] ProductID = Utilities.parseInt(request.getParameterValues("ProductID"));


s.executeUpdate("create temporary table temp_extra_summary (product_id int(10), total_units int(10))");

if (ProductID != null && ProductID.length > 0){
	for( int i =0; i<ProductID.length;i++){
		s.executeUpdate("insert into temp_extra_summary values ("+ProductID[i]+","+TotalUnits[i]+")");
	}
}
%>
	<div class="ui-bar-c" style="text-align:left; padding:7px; font-size: 14px; margin-bottom: 10px"><%=Utilities.filterString(request.getParameter("VehicleName"), 1, 100) %></div>
        			
        			<table style="width:100%;font-size: 10pt;">
	        			<thead style="text-align:left;">
	        				<tr>
	        					<th>Code</th>
	        					<th>Product</th>
	        					<th>Quantity</th>
	        					<th>Extra</th>
	        					<th>Total</th>
	        				</tr>
	        				
	        			</thead>
	        			<tbody>
	        				<% 	        				
	        				ResultSet rs = s.executeQuery("select ipv.sap_code,ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku, tp.product_id, sum( case when tp.type = 's' then tp.units else 0 end) qty, sum( case when tp.type = 't' then tp.units else 0 end) extra, sum(tp.units) total from ("+
	        						"SELECT isp.product_id, sum(isp.total_units) units, 's' type FROM inventory_sales_invoices_products isp where isp.id IN (select sales_id from inventory_sales_dispatch_invoices where id ="+Utilities.parseLong(request.getParameter("DispatchID"))+") group by isp.product_id union SELECT tes.product_id, sum(tes.total_units) units, 't' FROM temp_extra_summary tes group by tes.product_id"+
	        						") tp join inventory_products_view ipv on tp.product_id = ipv.product_id group by tp.product_id");
	        				while(rs.next()){
	        					
	        				%>
	        				<tr>
	        					<td><%=rs.getString("sap_code") %><input type="hidden" name="DispatchReturnsSummaryProductSapCode" value="<%=rs.getString("sap_code") %>"/></td>
	        					<td><%=rs.getString("package_label")+" - "+rs.getString("brand_label")%></td>
	        					<td><%=Utilities.convertToRawCases(rs.getLong("qty"), rs.getInt("unit_per_sku"))%></td>
	        					<td><%=Utilities.convertToRawCases(rs.getLong("extra"), rs.getInt("unit_per_sku"))%></td>
	        					<td><%=Utilities.convertToRawCases(rs.getLong("total"), rs.getInt("unit_per_sku"))%></td> 
	        				</tr>
	        				<%
	        				}
	        				%>
	        			</tbody>
        			</table>		


<%
s.close();
ds.dropConnection();
%>