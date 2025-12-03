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
	long SampleID = Long.parseLong(request.getParameter("SampleID"));
	
	%>	
			  
			  	
			  	
	              
				
			  	
			  
			  	
			  	<table border=0 style="font-size:13px; font-weight: 400; width:700px; height:100%;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			  		
			  		<thead>
			  			<tr>			  				
			  				<th align="left" nowrap="nowrap" style="width:10%">Package</th>
			  				<th align="left" nowrap="nowrap" style="width:10%">Brand</th>
			  				
			  				<th align="left" nowrap="nowrap" style="width:10%">Company Share</th>
			  				<th align="left" nowrap="nowrap" style="width:10%">Deduction Term</th>
			  				<th align="left" nowrap="nowrap" style="width:10%">Hand to Hand</th>			  			
			  				<th align="left" nowrap="nowrap" style="width:20%">Valid From</th>
			  				<th align="left" nowrap="nowrap" style="width:20%">Valid To</th>
			  			</tr>
			  		</thead>
             		
             		<%
					//System.out.println("SELECT *,(select label from inventory_packages ip where ip.id=sp.package) package_name,(select label from inventory_brands ib where ib.id=sp.brand_id) brand_name FROM sampling_percase sp where sampling_id="+SampleID);
             		ResultSet rs = s.executeQuery("SELECT *,(select label from inventory_packages ip where ip.id=sp.package) package_name,(select label from inventory_brands ib where ib.id=sp.brand_id) brand_name FROM sampling_percase sp where sampling_id="+SampleID);
					while(rs.next()){
					%>
					
		  			<tr>
		  				<td><%=rs.getString("package_name") %></td>
		  				<td><%if(rs.getString("brand_name") !=null){%><%=rs.getString("brand_name") %><%} else{ %>All<%} %></td>
		  				
		  				<td><%=rs.getDouble("company_share") %></td>
		  				<td><%=rs.getDouble("deduction_term") %></td>
		  				<td><%=rs.getLong("hand_to_hand") %></td>	
		  				<td><%=Utilities.getDisplayDateFormat(rs.getDate("valid_from")) %></td>	
		  				<td><%=Utilities.getDisplayDateFormat(rs.getDate("valid_to")) %></td>	
		  				
		  			</tr>
		  			
		  			<%
					}
					%>
				
		  </table>
		  
		  
	
</body>
</html>
<%


s2.close();
s.close();
ds.dropConnection();
%>