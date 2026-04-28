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

System.out.println("In LoadDispatchAdjustmentInvoiceBrandDIV");

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();


%>
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-right:10px; margin-top:10px;">
				<li data-role="list-divider" data-theme="a">Brand Discount</li>
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

			     </li>
			  
		
			</ul>


<%
s.close();
ds.dropConnection();
%>