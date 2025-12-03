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
int FeatureID = 154;
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

String where = "";

Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body>
		  <%
		  String MainSQL = "select *, ( SELECT label FROM sampling_credit_slip_types where id = sampling_credit_slip.type_id ) type_label from sampling_credit_slip where created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" order by created_on desc "; 
		  //System.out.println(MainSQL);
		  ResultSet rs2 = s2.executeQuery(MainSQL);
		  while(rs2.next()){
			  
			  String Where = "";
			  if(Type == 1){
				  Where = " and scso.is_printed=0";
			  }else if(Type == 2){
				  Where = " and scso.is_printed=1";
			  } 
			  
			//out.print("SELECT sma.outlet_id, sma.sampling_id, sma.request_id, sma.month, sma.net_payable, om.owner, om.address, om.telepohone, om.outlet_name, om.bsi_name, om.customer_id, om.customer_name, sma.barcode, sma.approval_id FROM sampling_monthly_approval sma, outletmaster om where sma.outlet_id = om.outlet_id and sma.status_id = 1 and sma.month = "+Utilities.getSQLDate(EndDate)+" and om.region = '"+PRegion+"' and sma.net_payable != 0 "+where);
			  String SQL = "SELECT scso.sr_no, scso.outlet_id, scso.barcode, scso.amount, co.name outlet_name, co.address, coc.contact_name, coc.contact_number, cd.distributor_id, cd.name distributor_name FROM sampling_credit_slip_outlets scso, common_outlets co, common_outlets_contacts coc, common_distributors cd where scso.outlet_id=co.id and co.id=coc.outlet_id and co.distributor_id = cd.distributor_id "+Where+" and scso.id="+rs2.getString("id");
			  //System.out.println(SQL);
			  ResultSet rs = s.executeQuery(SQL);
			  while (rs.next()){
				  
				  if( Action == 2 ){
					  s3.executeUpdate("update sampling_credit_slip_outlets set is_printed=1, printed_on=now(), printed_by="+SessionUserID+" where barcode="+rs.getString("barcode"));
				  }
				
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
					  			
					  			
					  			<%=rs2.getString("slip_description")%><br><br><br>
					  			
					  			______________________<br>
					  			Signature
					  			
					  			
					  			</p>
					  		</td>
					  		<td style="width: 50%; text-align: right" valign="top">
					  			<%= Utilities.getDisplayDateTimeFormat(rs2.getTimestamp("created_on"))  %><br><br><br><br><br>
					  			
					  			<%= Utilities.getMonthNameByNumber(rs2.getInt("month")) %> <%=rs2.getInt("year")%> (<%=rs2.getString("type_label")%>)<br>
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
				  <%
				
			  }
			  rs.close();
			  
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