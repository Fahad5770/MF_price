<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<style>

.formattedRow{
	border-bottom:1px solid #000;
	padding-top:5px;
	padding-bottom:5px;

}

</style>

<%

long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 195;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long DistributorID = 0;
String DistributorName = "";

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

String WhereDate = "";


if(session.getAttribute(UniqueSessionID+"_SR1StartDate") != null && session.getAttribute(UniqueSessionID+"_SR1EndDate") != null){
	WhereDate =" and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
}





Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();



Statement s = c.createStatement();
Statement s2 = c.createStatement();



%>
<html>

<head>

</head>
<body style="font-family:Helvetica,Arial,sans-serif">

	<%
	long RequestID = Long.parseLong(request.getParameter("RequestID"));
	long SamplingID = Long.parseLong(request.getParameter("SamplingID"));
	
	%>	
			  
			  	
			  	
	              
				<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
			  	<li data-role="list-divider" data-theme="c">Planned vs Actual Sales</li>
			  <li>
			  	
			  	<table border=0 style="font-size:13px; font-weight: 400; width:700px; height:100%;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			  		
			  		<thead>
			  			<tr>			  				
			  				<th align="left" nowrap="nowrap" style="width:20%">Package</th>
			  				<th align="left" nowrap="nowrap" style="width:20%">Type</th>
			  				<th align="center" nowrap="nowrap" style="width:20%; text-align: right">Planned Cases</th>
			  				<th align="center" nowrap="nowrap" style="width:20%;text-align: right">Actual Sales</th>
			  				<th align="center" nowrap="nowrap" style="width:20%; text-align: right">Sales (%)</th>
			  			</tr>
			  		</thead>
             		
             		<%
					//System.out.println("SELECT *,(select label from inventory_packages ip where ip.id=sp.package) package_name,(select label from inventory_brands ib where ib.id=sp.brand_id) brand_name FROM sampling_percase sp where sampling_id="+SampleID);
             		ResultSet rs = s.executeQuery("SELECT *, (select package_label from inventory_products_view where package_id=spsp.package_id limit 1) package_label, (SELECT label FROM inventory_products_types where id=spsp.type_id) type_label, ifnull((SELECT sum(((ss.quty_quant))) converted_cases FROM sap_sales ss join inventory_products_view ipv on ss.material_matnr = ipv.sap_code where ss.outlet_id = sps.outlet_id and ipv.package_id = spsp.package_id and ipv.type_id = spsp.type_id and ss.created_on_erdat between sps.start_date and sps.end_date),0) actual_sales FROM sampling_planned_sales_packages spsp join sampling_planned_sales sps on spsp.id = sps.id where sps.request_id = "+RequestID+" order by spsp.type_id, package_id");
					while(rs.next()){
						
						double PlannedCases = rs.getDouble("quantity");
						double ActualSale = rs.getDouble("actual_sales");
						double SalesInPercent = (ActualSale/PlannedCases) * 100;
						
					%>
					
		  			<tr>
		  				<td><%=rs.getString("package_label") %></td>
		  				<td><%=rs.getString("type_label") %></td>
		  				<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("quantity"))%></td>
		  				<td style="text-align:right"><%if (rs.getDouble("actual_sales")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("actual_sales"))%><%} %></td>
		  				<td style="text-align:right"><%if (SalesInPercent!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(SalesInPercent)%>%<%} %></td>
		  			</tr>
		  			
		  			<%
					}
					%>
				
		  </table>
		  </li>
		  
		  <li data-role="list-divider" data-theme="c">Approved Discount</li>
		  
		  <li>
		  <table border=0 style="font-size:13px; font-weight: 400; width:700px; height:100%;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			  		
			  		<thead>
			  			<tr>			  				
			  				<th align="left" nowrap="nowrap" style="width:10%">Package</th>
			  				<th align="left" nowrap="nowrap" style="width:10%">Brand</th>
			  				
			  				<th align="left" nowrap="nowrap" style="width:10%">Discount / Case</th>
			  				
			  				<th align="left" nowrap="nowrap" style="width:10%">Hand to Hand</th>			  			
			  				<th align="left" nowrap="nowrap" style="width:20%">Valid From</th>
			  				<th align="left" nowrap="nowrap" style="width:20%">Valid To</th>
			  			</tr>
			  		</thead>
             		
             		<%
					//System.out.println("SELECT *,(select label from inventory_packages ip where ip.id=sp.package) package_name,(select label from inventory_brands ib where ib.id=sp.brand_id) brand_name FROM sampling_percase sp where sampling_id="+SampleID);
             		ResultSet rs2 = s.executeQuery("SELECT *,(select label from inventory_packages ip where ip.id=sp.package) package_name,(select label from inventory_brands ib where ib.id=sp.brand_id) brand_name FROM sampling_percase sp where sampling_id="+SamplingID);
					while(rs2.next()){
					%>
					
		  			<tr>
		  				<td><%=rs2.getString("package_name") %></td>
		  				<td><%if(rs2.getString("brand_name") !=null){%><%=rs2.getString("brand_name") %><%} else{ %>All<%} %></td>
		  				
		  				<td><%=rs2.getDouble("company_share") %></td>
		  				
		  				<td><%=rs2.getLong("hand_to_hand") %></td>	
		  				<td><%=Utilities.getDisplayDateFormat(rs2.getDate("valid_from")) %></td>	
		  				<td><%=Utilities.getDisplayDateFormat(rs2.getDate("valid_to")) %></td>	
		  				
		  			</tr>
		  			
		  			<%
					}
					%>
				
		  </table>
		  </li>
		  </ul>
	
</body>
</html>
<%


s2.close();
s.close();
ds.dropConnection();
%>