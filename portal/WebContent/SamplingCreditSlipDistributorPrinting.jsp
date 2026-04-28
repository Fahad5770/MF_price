<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 157;
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

Date FromDate = Utilities.parseDate(request.getParameter("CreditSlipPrintingMainFromDate"));
Date ToDate = Utilities.parseDateTime(request.getParameter("CreditSlipPrintingMainToDate"), 23, 59);

int Type = Utilities.parseInt(request.getParameter("type"));
int Action = Utilities.parseInt(request.getParameter("action"));

Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body>
		  <%
		  
		  String Where = "";
		  if(Type == 1){
			  Where = " and is_printed=0";
		  }else if(Type == 2){
			  Where = " and is_printed=1";
		  } 
		  
		  String MainSQL = "select *, (select name from common_distributors where distributor_id=scsd.distributor_id) distributor_name, (SELECT label FROM sampling_credit_slip_types where id = scsd.type_id) type from sampling_credit_slip_distributor scsd where created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" "+Where+" order by created_on desc "; 
		  //System.out.println(MainSQL);
		  ResultSet rs2 = s2.executeQuery(MainSQL);
		  while(rs2.next()){
			 
			  
			  String type = rs2.getString("type");
			  if( Action == 2 ){
				  s3.executeUpdate("update sampling_credit_slip_distributor set is_printed=1, printed_on=now(), printed_by="+SessionUserID+" where uvid="+rs2.getString("uvid"));
			  }
				
				  %>
				  <div style="width: 8.27in; min-height: 3.8in; border: solid 1px; margin-top: 5px; page-break-after: always">
				  	<table style="width: 100%; margin-top: 20px;" border="0">
				  	
				  		<tr>
					  		<td style="width: 50%" valign="top">
					  			<img src="/barbecue/barcode?data=<%=rs2.getString("uvid")%>&height=50"><br>
					  			<p style="margin-left: 20px; margin-top: 4px; font-weight: 800;">
					  			Sr #<%=rs2.getString("id")%><br>
					  			<%= Utilities.getMonthNameByNumber(rs2.getInt("month")) %> <%=rs2.getInt("year")%> <span style="font-weight: normal;"><%= type%></span><br>
					  			<%=rs2.getString("distributor_id")%> - <%=rs2.getString("distributor_name")%>
					  			<br>
					  			<%=rs2.getString("slip_description")%><br><br>
					  			</p>
					  		</td>
					  		<td style="width: 50%; text-align: right" valign="top">
					  			<%= Utilities.getDisplayDateTimeFormat(rs2.getTimestamp("created_on"))  %><br>
					  		</td>
				  		</tr>
				  		<tr>
				  			<td colspan="2">
				  				<table align="center" style="width: 90%">
						  			<tr>
						  				<th style="width: 85%; text-align: left">List</th>
						  				<th style="width: 15%; text-align: right">Amount</th>
						  			</tr>
						  			<%
						  			double total = 0;
						  				ResultSet rs = s.executeQuery("select * from sampling_credit_slip_distributor_outlets where id="+rs2.getString("id"));
						  				while( rs.next() ){
						  					String OutletInfo = rs.getString("outlet_id")+" - "+rs.getString("outlet_name");
						  					if( rs.getString("outlet_id").equals("0") ){
						  						OutletInfo = rs.getString("outlet_name");
						  					}
						  					total += rs.getDouble("amount");
						  			%>
							  			<tr>
							  				<td style="text-align:left; font-size: 15px"><%=OutletInfo%></td>
							  				<td style="text-align:right; font-size: 15px"><%= Utilities.getDisplayCurrencyFormat(rs.getDouble("amount")) %></td>
							  			</tr>
						  			<%
						  			}
						  			%>
							  			<tr>
							  				<td style="text-align:right; font-size: 15px">Total</td>
							  				<td style="text-align:right; font-size: 15px; border-top: thin solid black;border-bottom: thin solid black;"><%= Utilities.getDisplayCurrencyFormat(total) %></td>
							  			</tr>
						  			
						  		</table>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td colspan="2" align="right" style="padding-top: 30px;">
				  				<p align="center">Punjab Beverages (Pvt) Co. Faisalabad</p>
				  			</td>
				  		</tr>
				  	</table>
				  </div>
				  <%
				
			  
		  }
		  
		  %>

</body>
</html>
<%
s3.close();
s2.close();
s.close();
ds.dropConnection();

%>