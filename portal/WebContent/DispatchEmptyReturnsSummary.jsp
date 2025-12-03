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
int FeatureID = 85;
int FeatureEmptyID = 83;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false && Utilities.isAuthorized(FeatureEmptyID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();
Statement s2 = ds.createStatement();


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
	        				
	        				//ResultSet rs2 = s2.executeQuery("Select * from inventory_sales_dispatch_invoices where id="+Utilities.parseLong(request.getParameter("DispatchID")));
	        				//System.out.println("Select * from inventory_sales_dispatch_invoices where id="+Utilities.parseLong(request.getParameter("DispatchID")));
	        				//while(rs2.next())
	        				//{
	        					/*
	        					long TotalUnitsReturned=0;
	        					long TotalUnits15=0;
	        					System.out.println("SELECT isip.id,isip.product_id,isip.raw_cases,sum(isip.total_units) units,isip.liquid_in_ml,ipm.empty_product_id FROM inventory_sales_invoices_products isip,inventory_products_map ipm where isip.product_id=ipm.product_id and isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+Utilities.parseLong(request.getParameter("DispatchID"))+") group by ipm.product_id");
	        					ResultSet rs = s.executeQuery("SELECT isip.id,isip.product_id,isip.raw_cases,sum(isip.total_units) units,isip.liquid_in_ml,ipm.empty_product_id FROM inventory_sales_invoices_products isip,inventory_products_map ipm where isip.product_id=ipm.product_id and isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+Utilities.parseLong(request.getParameter("DispatchID"))+") group by ipm.product_id");
	        					
		        				while(rs.next())
		        				{
		        					
		        					if(rs.getString("empty_product_id") != null && rs.getInt("empty_product_id")!=0)
		        					{
		        						ResultSet rs2 = s2.executeQuery("select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id ="+rs.getInt("product_id"));
		        						if(rs2.first())
		        						{
		        							
		        							//System.out.println("select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id ="+rs.getInt("product_id"));
		        							TotalUnitsReturned = rs2.getLong("units_returned");
		        							//System.out.println("test "+TotalUnitsReturned);
		        							TotalUnits15 = rs.getLong("units") - TotalUnitsReturned;
		        						}
		        						if(TotalUnits15 !=0)
		        						{
		        							*/
		        							
		        							
		        							/*ResultSet rs1 = s1.executeQuery("select empty_product_id, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, sum(total_units-units_returned) total_units from ("+
		        									"SELECT isip.product_id, ipm.empty_product_id, sum(isip.total_units) total_units, ifnull(("+
		        										"select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id = isip.product_id"+
		        									"),0) units_returned FROM inventory_sales_invoices_products isip join inventory_products_map ipm on isip.product_id=ipm.product_id where isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+Utilities.parseLong(request.getParameter("DispatchID"))+") group by isip.product_id, ipm.empty_product_id union all select isdep.product_id, ipm.empty_product_id, sum(isdep.total_units), ifnull((select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id = isdep.product_id), 0) units_returned from inventory_sales_dispatch_extra_products isdep join inventory_products_map ipm on isdep.product_id = ipm.product_id where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" group by isdep.product_id , ipm.empty_product_id "+
		        								") tab1 join inventory_products_view ipv on tab1.empty_product_id = ipv.product_id group by empty_product_id");
		        							
		        							System.out.println("select empty_product_id, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, sum(total_units-units_returned) total_units from ("+
		        									"SELECT isip.product_id, ipm.empty_product_id, sum(isip.total_units) total_units, ifnull(("+
	        										"select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id = isip.product_id"+
	        									"),0) units_returned FROM inventory_sales_invoices_products isip join inventory_products_map ipm on isip.product_id=ipm.product_id where isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+Utilities.parseLong(request.getParameter("DispatchID"))+") group by isip.product_id, ipm.empty_product_id union all select isdep.product_id, ipm.empty_product_id, sum(isdep.total_units), ifnull((select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id = isdep.product_id), 0) units_returned from inventory_sales_dispatch_extra_products isdep join inventory_products_map ipm on isdep.product_id = ipm.product_id where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" group by isdep.product_id , ipm.empty_product_id "+
	        								") tab1 join inventory_products_view ipv on tab1.empty_product_id = ipv.product_id group by empty_product_id");
		        							*/
		        								
		        							ResultSet rs1 = s1.executeQuery("select empty_product_id, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, sum(total_units-units_returned) total_units from ("+
		        									" select empty_product_id, sum(total_units) total_units, ifnull((select sum(total_units) from inventory_sales_dispatch_returned_products isdrp join inventory_products_map ipm on isdrp.product_id = ipm.product_id where isdrp.dispatch_id = "+DispatchID+" and ipm.empty_product_id = tab1.empty_product_id),0) units_returned from ("+
		        											" SELECT isip.product_id, ipm.empty_product_id, isip.total_units FROM inventory_sales_invoices_products isip join inventory_products_map ipm on isip.product_id=ipm.product_id where isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+DispatchID+") union all select isdep.product_id, ipm.empty_product_id, isdep.total_units from inventory_sales_dispatch_extra_products isdep join inventory_products_map ipm on isdep.product_id=ipm.product_id where isdep.dispatch_id = "+DispatchID+
		        											" ) tab1 group by empty_product_id"+		        							
	        								" ) tab1 join inventory_products_view ipv on tab1.empty_product_id = ipv.product_id group by empty_product_id");
		        							
		        							/*
		        							System.out.println("select empty_product_id, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, sum(total_units-units_returned) total_units from ("+
		        									"SELECT isip.product_id, ipm.empty_product_id, sum(isip.total_units) total_units, ifnull(("+
	        										"select sum(total_units) units_returned from inventory_sales_dispatch_returned_products where dispatch_id = "+Utilities.parseLong(request.getParameter("DispatchID"))+" and product_id = isip.product_id"+
	        									"),0) units_returned FROM inventory_sales_invoices_products isip join inventory_products_map ipm on isip.product_id=ipm.product_id where isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= "+Utilities.parseLong(request.getParameter("DispatchID"))+") group by isip.product_id, ipm.empty_product_id"+
	        								") tab1 join inventory_products_view ipv on tab1.empty_product_id = ipv.product_id group by empty_product_id");
		        							*/
		        							
		        							
		        							
		        							//ResultSet rs1 = s1.executeQuery("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku FROM inventory_products_view ipv where ipv.product_id="+rs.getInt("empty_product_id"));	
			        						while(rs1.next())
			    	        				{	
			        							SumTotalUnits=0;
			    	        					UnitPerSKUU=0;
			        							//System.out.println("Hello "+rs.getLong("units"));
			        							long TotalUnits15 = rs1.getLong("total_units");
			    	        				%>
			    	        				<tr>
			    	        					<td><%=rs1.getString("sap_code") %><input type="hidden" name="DispatchReturnsSummaryProductSapCode" value="<%=rs1.getString("sap_code") %>"/></td>
			    	        					<td><%=rs1.getString("package_label")+" - "+rs1.getString("brand_label")%></td>
			    	        					<td><%=Utilities.convertToRawCases(TotalUnits15, rs1.getInt("unit_per_sku")) %><input type="hidden" name="DispatchReturnsSummaryTotalUnitsHidden" value="<%=rs1.getString("sap_code") %>|<%=TotalUnits15 %>"/></td>		    	        					
			    	        						<% 
					        						if(ProductID != null)
					        						{
					        							for( int i =0; i<ProductID.length;i++)
					        							{
					        								if(rs1.getInt("empty_product_id")==ProductID[i])
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
		        						
		    	        					    	        				
		        					
		        				
	        				//}
	        				
	        				
	        				
	        				
	        				//ResultSet rs = s.executeQuery("SELECT ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.unit_per_sku, sum(isp.total_units) units,isp.product_id FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN(select sales_id from inventory_sales_dispatch_invoices where id ="+Utilities.parseLong(request.getParameter("DispatchID"))+") group by ipv.sap_code");
	        				%>
	        			</tbody>
        			</table>		


<%
s.close();
ds.dropConnection();
%>