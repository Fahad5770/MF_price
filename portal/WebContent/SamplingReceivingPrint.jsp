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
<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

long ReceivingID = Utilities.parseLong(request.getParameter("ReceivingID"));

Statement s = c.createStatement();
Statement s2 = c.createStatement();
%>
<html>

<head>

</head>
<body>
		  <%
		  
		  ResultSet rs = s.executeQuery("SELECT sr.distributor_id, sr.received_on, (select Customer_Name from outletmaster where Customer_ID = sr.distributor_id limit 1) distributor_name from sampling_receiving sr where sr.receiving_id = "+ReceivingID);
		  while (rs.next()){
			  Date ReceivedOn = rs.getTimestamp(2);
			  long DistributorID = rs.getLong(1);
			  String DistributorName = rs.getString(3);
			  %>
			  <div style="width: 8.27in; border: solid 1px; margin-top: 5px;">
			  	<table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px;"><tr><td style="width: 40%;font-weight: 700;">DISCOUNT SLIP RECEIPT</td><td style="text-align: center;font-weight: 700;"></td><td style="width: 20%; text-align: right;">Receiving ID: <%= ReceivingID %></td></tr></table>
			  	<hr />
			  	<table style="width: 100%;  margin: 20px;" border="0">
			  		<tr style="font-weight: 700;">
				  		<td style="width: 25%">
				  			Date
				  		</td>
				  		<td>
				  			Distributor
				  		</td>
			  		</tr>
			  		<tr>
				  		<td>
				  		<%= Utilities.getDisplayDateTimeFormat(ReceivedOn) %>
				  		</td>
				  		<td>
				  		<%= DistributorID + " - " + DistributorName %>
				  		</td>
			  		</tr>
			  	</table>
			  	<table style="width: 100%;  padding-left: 20px;padding-right: 20px;" border="0">
			  		<tr style="font-weight: 700;">
			  			<td>Distributor</td>
			  			<td>Outlet</td>
			  			<td>Month</td>
			  			<td style="text-align: right;">Amount</td>
			  		</tr>
			  	<%
			  	double TotalAmount = 0;
				ResultSet rs2 = s2.executeQuery("SELECT sri.approval_id, s.outlet_id, s.outlet_name, s.business_type, date_format(sma.month,'%M, %Y'), sma.net_payable, om.Customer_ID, om.Customer_Name from sampling_receiving_items sri, sampling_monthly_approval sma, sampling s, outletmaster om where s.outlet_id = om.outlet_id and sri.approval_id = sma.approval_id and sma.sampling_id = s.sampling_id and sri.receiving_id = "+ReceivingID);
				while (rs2.next()){
					double amount = rs2.getDouble(6);
					TotalAmount += amount;
			  	%>
			  		<tr>
			  			<td><%= rs2.getString(7) + " - " + rs2.getString(8) %></td>
			  			<td><%= rs2.getString(2) + " - " + rs2.getString(3) + " " + rs2.getString(4) %></td>
			  			<td><%=rs2.getString(5) %></td>
			  			<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(amount) %></td>
			  		</tr>
			  	<%
				}
				rs2.close();
			  	%>
			  		<tr>
			  			<td></td>
			  			<td></td>
			  			<td style="text-align: right;"><b><%=Utilities.getDisplayCurrencyFormat(TotalAmount) %></b></td>
			  		</tr>			  	
			  	</table>
			  </div>
			  <%
			
		  }
		  rs.close();
		  %>

</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();
%>