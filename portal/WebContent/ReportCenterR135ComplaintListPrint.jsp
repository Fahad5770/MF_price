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
int FeatureID = 139;

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

		
			  	<div style="width: 8.27in; border: solid 1px; margin-top: 5px; ">
			  	
			  	<table border="0" style="width: 100%">
	              	<tr>
	                	<td align="left" width="1"><img src="images/logo.svg" style="width: 30px"></td>
	                    <td align="left" valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td>
	                    <td align="right" style="padding-right: 20px"><b>Complaint List / TOT</b></td>
	                </tr>
	            </table>
	              
				<table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px">
	                <tr>
	                	<td style="width: 20%;">&nbsp;</td>
	                    <td style="text-align: center;font-weight: 700;">&nbsp;</td>
	                    <td style="width: 20%;text-align: right;" nowrap="nowrap"><%= Utilities.getDisplayDateTimeFormat(new Date()) %></td>
	                </tr>
	                
	            </table>
			  	<hr />
			  	<div align="left" style="padding-left:20px;font-weight: 700; margin-bottom:5px; font-size: 15px"></div>
			  	
			  	<table align="center" style="width: 95%; font-size:12px; margin-bottom:10px" border="0" cellspacing="0">
			  		
			  		<thead>
			  			<tr>			  				
			  				<th align="left" nowrap="nowrap" style="width: 5%; padding-left:5px">Complaint ID</th>
			  				<th align="left" nowrap="nowrap" style="width: 15%; padding-left:5px">Outlet</th>
			  				<th align="left" nowrap="nowrap" style="width: 10%; padding-left:5px">Outlet Contact</th>
			  				<th align="left" nowrap="nowrap" style="width: 5%; padding-left:5px">Complaint Type</th>
			  				<th align="left" nowrap="nowrap" style="width: 20%; padding-left:5px">Description</th>
			  				<th align="left" nowrap="nowrap" style="width: 20%; padding-left:5px">Description Urdu</th>			  			
			  				<th align="left" nowrap="nowrap" style="width: 25%; padding-left:15px">Remarks</th>
			  			</tr>
			  		</thead>
             		
             		<%
					ResultSet rs = s.executeQuery("SELECT cca.id,cca.outlet_id,cca.outlet_name,cca.outlet_address,cca.outlet_contact_no,cca.type_id,cct.label type_label,cca.description,cca.urdu_description,cca.department_id,cd.label department_label FROM crm_complaints_assigned cca join crm_complaints_types cct on cca.type_id = cct.id join crm_departments cd on cca.department_id = cd.id and cca.department_id=2 join crm_complaints cc on cc.id=cca.id "+WhereDate+" and cc.is_resolved != 1");
					while(rs.next()){
					%>
					
		  			<tr style="height: 50px">
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=rs.getLong("id") %></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=rs.getString("outlet_address") %>  <%=rs.getString("outlet_contact_no") %></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=rs.getString("type_label") %></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=rs.getString("description") %></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=rs.getString("urdu_description") %></td>	
		  				
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc;border-left: 1px solid #ccc; border-right: 1px solid #ccc">&nbsp;</td> 				
		  				
		  			</tr>
		  			
		  			<%
					}
					%>
				
		  </table>
		  
		  
	</div>
</body>
</html>
<%


s2.close();
s.close();
ds.dropConnection();
%>