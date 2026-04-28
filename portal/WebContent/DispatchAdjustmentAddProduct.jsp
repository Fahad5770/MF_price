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
Invoice # <%=Utilities.parseLong(request.getParameter("invoiceid"))%>
							<table style="width:100%;font-size:10pt;'" data-mini="true;" >
									
									<tr>								
										<td valign="top" style="width:30%">
											<input type="text" placeholder="Product Code" id="DispatchAdjustmentProductCode" name="DispatchAdjustmentProductCode" data-mini="true" >
										</td>
										<td valign="top" style="width:30%">
											<input  type="text" placeholder="Raw Cases" id="DispatchAdjustmentRawCases" name="DispatchAdjustmentRawCases" data-mini="true">
										</td>
										<td valign="top" style="width:30%">
											<input  type="text" placeholder="Bottles" id="DispatchAdjustmentUnits" readonly="readonly" name="DispatchAdjustmentUnits" data-mini="true" >
										</td>
										
										
										<td valign="top">
											<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
										</td>
										<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
										
										<input type="hidden" name="DispatchIDHidden" id="DispatchIDHidden" value="<%=Utilities.parseLong(request.getParameter("dispatchid"))%>"/>
										<input type="hidden" name="OutletIDHidden" id="OutletIDHidden" value="<%=Utilities.parseLong(request.getParameter("outletid"))%>"/>
										<input type="hidden" name="InvoiceIDHidden" id="InvoiceIDHidden" value="<%=Utilities.parseLong(request.getParameter("invoiceid"))%>"/>
									</tr>
									
								</table>

						<ul data-role="listview" data-inset="true" data-mini="true">
						<li>
						    		<table style="width:100%;font-size:10pt;"  data-mini="true">
						    			<tr class="ui-bar-c">
						    				<th style="text-align:left;">Product Code</th>
						    				<th style="text-align:left;">Package</th>
						    				<th style="text-align:left;">Brand</th>
						    				<th style="text-align:left;">Raw Cases</th>
						    				<th style="text-align:left;">Bottles</th>
						    			</tr>
						    			<%
						    				ResultSet rs = s.executeQuery("SELECT isdap.dispatch_id,isdap.is_promotion,isdap.outlet_id,isdap.product_id,(select ipv.package_label from inventory_products_view ipv where ipv.product_id=isdap.product_id) package_label,(select ipv.brand_label from inventory_products_view ipv where ipv.product_id=isdap.product_id) brand_label,(select ipv.sap_code from inventory_products_view ipv where ipv.product_id=isdap.product_id) sap_code,isdap.raw_cases,isdap.units,isdap.invoice_id FROM inventory_sales_dispatch_adjusted_products isdap where isdap.dispatch_id="+Utilities.parseLong(request.getParameter("dispatchid"))+" and isdap.outlet_id="+Utilities.parseLong(request.getParameter("outletid"))+" and isdap.invoice_id="+Utilities.parseLong(request.getParameter("invoiceid")));
						    				int i=0;
						    				while(rs.next())
						    				{
						    			%>	
						    			<tr id="DispatchAdjustment<%=i %>">
						    				<td><%=Utilities.parseLong(rs.getString("sap_code")) %></td>
						    				<td><%=Utilities.filterString(rs.getString("package_label"), 1, 200) %></td>
						    				<td><%=Utilities.filterString(rs.getString("brand_label"), 1, 200) %></td>
						    				<td><%=Utilities.parseInt(rs.getString("raw_cases")) %></td>
						    				<td><%=Utilities.parseInt(rs.getString("units")) %></td>
						    				<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick='DispatchAdjustmentDeleteRow("DispatchAdjustment<%=i %>","<%=Utilities.parseLong(rs.getString("dispatch_id")) %>","<%=Utilities.parseLong(rs.getString("outlet_id")) %>","<%=Utilities.parseLong(rs.getString("product_id")) %>","<%=Utilities.parseLong(rs.getString("invoice_id"))%>","<%=rs.getInt("is_promotion")%>","<%=Utilities.parseInt(rs.getString("raw_cases"))%>")'>Delete</a></td>
						    			</tr>	
						    			<% 
						    			i++;
						    				}
						    			%>
						    			
						    		</table>
						    </li>
						    </ul>	




<%
s.close();
ds.dropConnection();
%>