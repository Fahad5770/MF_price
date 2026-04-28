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
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 56;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long[] InventorySalesID = Utilities.parseLong(request.getParameterValues("InventorySalesID"));
int[] DispatchSalesSelect = Utilities.parseInt(request.getParameterValues("DispatchSalesSelect"));

String SelectedInventorySalesID="";
if(InventorySalesID !=null)
{
	for(int x=0;x<InventorySalesID.length;x++)
	{
		if(DispatchSalesSelect[x] == 1) //if selected
		{
			//making comma seperated string of ids			
			//System.out.println("Me in if");
			SelectedInventorySalesID = SelectedInventorySalesID + InventorySalesID[x]+",";
		}
		else if(DispatchSalesSelect[x] == 0)
		{
			//System.out.println("Me in else");
			SelectedInventorySalesID += "0,";
		}
		
	}
	//removing last comma
	 SelectedInventorySalesID = SelectedInventorySalesID.substring(0, SelectedInventorySalesID.length()-1);
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
%>	   	

									<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
										<li data-role="list-divider">Summary</li>
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
												<tbody> 
												 	<%
												 	
												 	ResultSet rs = s.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.unit_per_sku, sum(isp.total_units) units FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN("+SelectedInventorySalesID+") group by ipv.package_id, ipv.package_label");
												 	long RawCasesUnits[];
												 	long RawCasesSum=0;
												 	long UnitsSum=0;
												 	while(rs.next())
												 	{
												 		RawCasesUnits = Utilities.getRawCasesAndUnits(rs.getLong("units"), rs.getInt(3));
												 		//System.out.println("Raw Cases "+RawCasesUnits[0]+" Units"+RawCasesUnits[1]);
												 		RawCasesSum += RawCasesUnits[0];
												 		UnitsSum += RawCasesUnits[1];
												 	%>
													 	<tr>
															<td><%=rs.getString("package_label") %></td>
															<td style="text-align:right;"><%=Utilities.convertToRawCases(rs.getLong("units"), rs.getInt(3))  %></td>
													    </tr>
												    <%
												 	}
												    %>
												    <tr>
												    	<th style="text-align:left;">Total</th>												    	
												    	<td style="text-align:right;"><%=Utilities.formatCasesAndUnits(RawCasesSum, UnitsSum)%></td>
												    </tr>
												 </tbody>
											</table>
										</li>
									</ul>


			    			
						
<%
s.close();
ds.dropConnection();
%>