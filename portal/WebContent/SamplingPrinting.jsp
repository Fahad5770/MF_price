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

String PRegion = Utilities.filterString(request.getParameter("region"), 1, 20);
long PDistributorID = Utilities.parseLong(request.getParameter("distributor"));
long PRSMID = Utilities.parseLong(request.getParameter("rsm"));
long PASMID = Utilities.parseLong(request.getParameter("asm"));
long PCRID = Utilities.parseLong(request.getParameter("cr"));
String POutletID = Utilities.filterString(request.getParameter("outlet_id"), 1, 500);

int month = Utilities.parseInt(request.getParameter("month"));
int year = Utilities.parseInt(request.getParameter("year"));
int type = Utilities.parseInt(request.getParameter("type"));
int action = Utilities.parseInt(request.getParameter("action"));

Date EndDate = Utilities.getEndDateByMonth(month-1, year);

String where = "";

if (!PRegion.equals("0")){
	where += " and om.region = '" + PRegion +"'";
}

if (PDistributorID != 0){
	where += " and om.customer_id = '" + PDistributorID +"'";
} 

if (POutletID != null && POutletID.length() > 1){
	where += " and sma.outlet_id in ( " + POutletID +" ) ";
}

if (type == 1){
	where += " and sma.is_printed = 0 ";
}
if (type == 2){
	where += " and sma.is_printed = 1 ";
}



Statement s = c.createStatement();
Statement s2 = c.createStatement();
%>
<html>

<head>

</head>
<body>
		  <%
		  
		  int counter = -1;
		  //out.print("SELECT sma.outlet_id, sma.sampling_id, sma.request_id, sma.month, sma.net_payable, om.owner, om.address, om.telepohone, om.outlet_name, om.bsi_name, om.customer_id, om.customer_name, sma.barcode, sma.approval_id FROM sampling_monthly_approval sma, outletmaster om where sma.outlet_id = om.outlet_id and sma.status_id = 1 and sma.month = "+Utilities.getSQLDate(EndDate)+" and om.region = '"+PRegion+"' and sma.net_payable != 0 "+where);
		  ResultSet rs = s.executeQuery("SELECT sma.outlet_id, sma.sampling_id, sma.request_id, sma.month, sma.net_payable, om.owner, om.address, om.telepohone, om.outlet_name, om.bsi_name, om.customer_id, om.customer_name, sma.barcode, sma.approval_id FROM sampling_monthly_approval sma, outletmaster om where sma.outlet_id = om.outlet_id and sma.status_id = 1 and sma.month = "+Utilities.getSQLDate(EndDate)+" and sma.net_payable > 0 and sma.is_cancelled=0 "+where);
		  while (rs.next()){
			  
			long ApprovalID = rs.getLong("sma.approval_id");
			
			if (action == 2){
				s2.executeUpdate("update sampling_monthly_approval set is_printed = 1 where approval_id = "+ApprovalID);
			}
			
		  	counter++;
			  %>
			  <div style="width: 8.27in; height: 3.8in; border: solid 1px; margin-top: 5px; page-break-after: always">
			  	<table style="width: 100%; margin-top: 20px;" border="0">
			  	
			  		<tr>
			  		<td style="width: 50%" valign="top">
			  			<img src="/barbecue/barcode?data=<%=rs.getString("barcode")%>&height=50"><br>
			  			<p style="margin-left: 20px; margin-top: 4px; font-weight: 800;">
			  			OUTLET CODE# <%= rs.getString("outlet_id") %><br><br>

			  			M/S <%=rs.getString("owner") %><br>
			  			<%=rs.getString("outlet_name") + " " + rs.getString("bsi_name") %><br>
			  			<%=rs.getString("address") %><br>
			  			Phone #: <%=rs.getString("telepohone") %><br>
			  			</p>
			  		</td>
			  		<td style="width: 50%" valign="top">
			  			Generated On: <%= Utilities.getDisplayDateTimeFormat(new Date())  %><br>
			  			<br>
			  			Distributor Code: <b><%=rs.getString("customer_id") %></b><br>
			  			Distributor Name: <b><%=rs.getString("customer_name") %></b><br>
			  			<br>
			  			Sampling for the month of <b><%= new java.text.SimpleDateFormat("MMMMM").format(EndDate) %></b> is <b>Rs. <%= Utilities.getDisplayCurrencyFormat(rs.getDouble("net_payable")) %></b><br> 
			  			<b><%double NetPayable = rs.getDouble("net_payable"); if (NetPayable < 0){NetPayable = 0;}%><%= NumberToWords.convert((int)Math.round(NetPayable)) %> Rupees Only</b>
			  			<br>
			  			 <img src="images/rupee.png"> <span style="font-weight: 800; font-size: 16pt; vertical-align:text-bottom;"><%= Utilities.getDisplayCurrencyFormat(rs.getDouble("net_payable")) %></span> <img src="images/SamplingUrdu3.png"><img src="images/month<%=month%>.png">
			  			
			  			
			  		</td>
			  		</tr>
			  		<tr>
			  			<td colspan="2" align="right" style="padding-top: 30px;">
			  				<img src="images/SamplingUrdu1Small.png">
			  				<img src="images/SamplingUrdu2Small.png">
			  				<p align="center">Punjab Beverages (Pvt) Co. Faisalabad</p>
			  			</td>
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