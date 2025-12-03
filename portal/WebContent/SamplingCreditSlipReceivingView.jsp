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
int FeatureID = 155;
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


long Barcode = Utilities.parseLong(request.getParameter("Barcode"));

Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body>
		  <%
		  
		  
		  //out.print("SELECT sma.outlet_id, sma.sampling_id, sma.request_id, sma.month, sma.net_payable, om.owner, om.address, om.telepohone, om.outlet_name, om.bsi_name, om.customer_id, om.customer_name, sma.barcode, sma.approval_id FROM sampling_monthly_approval sma, outletmaster om where sma.outlet_id = om.outlet_id and sma.status_id = 1 and sma.month = "+Utilities.getSQLDate(EndDate)+" and om.region = '"+PRegion+"' and sma.net_payable != 0 "+where);
		  String SQL = "SELECT scso.sr_no, scso.id, scso.barcode, scso.outlet_id, scs.month, scs.year, scso.amount, scs.slip_description, scs.internal_description, scs.type_id, scs.created_on, scs.created_by, co.name outlet_name, co.address, coc.contact_name, coc.contact_number, cd.distributor_id, cd.name distributor_name, ( SELECT label FROM sampling_credit_slip_types where id = scs.type_id ) type_label FROM sampling_credit_slip scs, sampling_credit_slip_outlets scso, common_outlets co, common_outlets_contacts coc, common_distributors cd where scso.id=scs.id and scso.outlet_id=co.id and co.id=coc.outlet_id and co.distributor_id = cd.distributor_id and scso.barcode="+Barcode+" and scso.is_received=0 ";
		  //System.out.println(SQL);
		  ResultSet rs = s.executeQuery(SQL);
		  if (rs.first()){
			
			  %>
			  <div style="width: 8.27in; height: 3.8in; border: solid 1px; margin-top: 5px; page-break-after: always">
			  	<table style="width: 100%; margin-top: 20px;" border="0">
			  	
			  		<tr>
				  		<td style="width: 50%" valign="top">
				  			<img src="/barbecue/barcode?data=<%=rs.getString("barcode")%>&height=50"><br>
				  			<p style="margin-left: 20px; margin-top: 4px; font-weight: 800;">
				  				Sr #<%=rs.getString("sr_no")%><br>
					  			OUTLET CODE# <%= rs.getString("outlet_id") %><br><br>
		
					  			M/S <%=rs.getString("contact_name") %><br>
					  			<%=rs.getString("outlet_name")%><br>
					  			<%=rs.getString("address") %><br>
					  			Phone #: <%=rs.getString("contact_number") %><br><br>
					  			
					  			
					  			<%=rs.getString("slip_description")%><br><br><br>
					  			
					  			______________________<br>
					  			Signature
					  			
					  			
					  			</p>
				  		</td>
				  		<td style="width: 50%; text-align: right" valign="top">
				  			<%= Utilities.getDisplayDateTimeFormat(rs.getTimestamp("created_on"))  %><br><br><br><br><br>
					  			
					  			<%= Utilities.getMonthNameByNumber(rs.getInt("month")) %> <%=rs.getInt("year")%> (<%=rs.getString("type_label")%>)<br>
					  			<b>Rs.<%= Utilities.getDisplayCurrencyFormat(rs.getDouble("amount")) %></b> (<%= NumberToWords.convert((int)Math.round(rs.getDouble("amount"))) %> Rupees Only)<br><br>
					  			
				  			
				  			
				  		</td>
			  		</tr>
			  		<tr>
			  			<td colspan="2" align="right" style="padding-top: 30px;">
			  				<p align="center">Punjab Beverages (Pvt) Co. Faisalabad</p>
			  			</td>
			  		</tr>
			  	</table>
			  </div>
			  
			  <script>
			  window.parent.setSubmitButton(true, 1);
			  </script>
			  
			  <%
			
		  }else{
			  
			  String SQL2 = "select *, (select name from common_distributors where distributor_id=scsd.distributor_id) distributor_name, (SELECT label FROM sampling_credit_slip_types where id = scsd.type_id) type from sampling_credit_slip_distributor scsd where is_received=0 and uvid="+Barcode;
			  //System.out.println(SQL);
			  ResultSet rs2 = s2.executeQuery(SQL2);
			  if(rs2.first()){
				  String type = rs2.getString("type");
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
						  				<th style="width: 70%; text-align: left">List</th>
						  				<th style="width: 30%; text-align: right">Amount</th>
						  			</tr>
						  			<%
						  			double total = 0;
						  				ResultSet rs3 = s3.executeQuery("select * from sampling_credit_slip_distributor_outlets where id="+rs2.getString("id"));
						  				while( rs3.next() ){
						  					String OutletInfo = rs3.getString("outlet_id")+" - "+rs3.getString("outlet_name");
						  					if( rs3.getString("outlet_id").equals("0") ){
						  						OutletInfo = rs3.getString("outlet_name");
						  					}
						  					total += rs3.getDouble("amount");
						  			%>
							  			<tr>
							  				<td style="text-align:left; font-size: 15px"><%=OutletInfo%></td>
							  				<td style="text-align:right; font-size: 15px"><%= Utilities.getDisplayCurrencyFormat(rs3.getDouble("amount")) %></td>
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
				  <script>
				  	window.parent.setSubmitButton(true, 2);
				  </script>
				  <%
				  
				  
			  }else{
				  %>
				  <script>
				  window.parent.setSubmitButton(false, 0);
				  </script>
				  <%
			  }
			  
			  
		  }
		  rs.close();
		  %>

</body>
</html>
<%
s3.close();
s2.close();
s.close();
ds.dropConnection();

%>