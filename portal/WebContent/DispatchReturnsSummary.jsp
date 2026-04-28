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
int FeatureID = 59;
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

long SumTotalUnits =0;
int UnitPerSKUU=0;



%>
	<div class="ui-bar-c" style="text-align:left; padding:7px; font-size: 14px; margin-bottom: 10px"><%=Utilities.filterString(request.getParameter("VehicleName"), 1, 100) %></div>
        			
        			<table style="width:100%;font-size: 10pt;">
	        			<thead style="text-align:left;">
	        				<tr>
	        					<th>Code</th>
	        					<th>Product</th>
	        					<th>Quantity</th>
	        					<th>Returned</th>
	        				</tr>
	        				
	        			</thead>
	        			<tbody>
	        				<% 	    
	        				
	        				long DispatchID = Utilities.parseLong(request.getParameter("DispatchID"));
	        				
	        				String qry = "select sap_code, package_id, package_label, brand_id, brand_label, unit_per_sku, sum(units) units, product_id from ("+
	        						" SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku, isp.total_units units,isp.product_id FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN (select sales_id from inventory_sales_dispatch_invoices where id = "+DispatchID+")"+
	        						" union all "+
	        						" SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku, isdep.total_units, isdep.product_id FROM inventory_sales_dispatch_extra_products isdep join inventory_products_view ipv on isdep.product_id = ipv.product_id where dispatch_id ="+DispatchID+
	        						" ) tab1 group by sap_code";
	        				
	        				
	        				//ResultSet rs = s.executeQuery("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku, sum(isp.total_units) units,isp.product_id FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN(select sales_id from inventory_sales_dispatch_invoices where id ="+DispatchID+") group by ipv.sap_code");
	        				//System.out.println("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku, sum(isp.total_units) units,isp.product_id FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN(select sales_id from inventory_sales_dispatch_invoices where id ="+Utilities.parseLong(request.getParameter("DispatchID"))+") group by ipv.sap_code");
	        				
	        				
	        				ResultSet rs = s.executeQuery(qry);
	        				while(rs.next())
	        				{
	        					SumTotalUnits=0;
	        					UnitPerSKUU=0;
	        				%>
	        				<tr>
	        					<td><%=rs.getString("sap_code") %><input type="hidden" name="DispatchReturnsSummaryProductSapCode" value="<%=rs.getString("sap_code") %>"/></td>
	        					<td><%=rs.getString("package_label")+" - "+rs.getString("brand_label")%></td>
	        					<td><%=Utilities.convertToRawCases(rs.getLong("units"), rs.getInt("unit_per_sku"))   %><input type="hidden" name="DispatchReturnsSummaryTotalUnitsHidden" value="<%=rs.getString("sap_code") %>|<%=rs.getLong("units") %>"/></td>
	        					<% 
	        						if(ProductID != null)
	        						{
	        							for( int i =0; i<ProductID.length;i++)
	        							{
	        								if(rs.getInt("product_id")==ProductID[i])
	        								{
	        									SumTotalUnits = TotalUnits[i];
	        									UnitPerSKUU = UnitPerSKU[i];
	        									//System.out.println("Hello "+SumTotalUnits);
	        								}	        								
	        							}
	        						%>
	        							<td><%=Utilities.convertToRawCases(SumTotalUnits, UnitPerSKUU)%></td>
	        					<%
	        						}
	        						%>
	        						
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