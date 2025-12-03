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
int FeatureID = 160;
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

long GLCashReceiptID = Utilities.parseLong(request.getParameter("GLCashReceiptID"));

//Date FromDate = Utilities.parseDate(request.getParameter("CreditSlipPrintingMainFromDate"));
//Date ToDate = Utilities.parseDateTime(request.getParameter("CreditSlipPrintingMainToDate"), 23, 59);

//int Type = Utilities.parseInt(request.getParameter("type"));
//int Action = Utilities.parseInt(request.getParameter("action"));

Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body>
		  <%
		  
		  
		  
		  String MainSQL = "SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name, (SELECT display_name FROM pep.users where id=created_by) created_by_name FROM gl_cash_receipts where id="+GLCashReceiptID; 
		  //System.out.println(MainSQL);
		  ResultSet rs2 = s2.executeQuery(MainSQL);
		  while(rs2.next()){
			 
			
				
				  %>
				  <div style="width: 8.27in; min-height: 3.3in; border: solid 1px; margin-top: 5px;">
				  	<table style="width: 100%; margin-top: 20px;" border="0">
				  	
				  		<tr>
					  		<td style="width: 50%" valign="top">
					  			<img src="/barbecue/barcode?data=<%=rs2.getString("uvid")%>&height=50"><br>
					  			<p style="margin-left: 20px; margin-top: 4px; font-weight: 800;">
					  			Sr #<%=rs2.getString("id")%><br>
					  			<%=rs2.getString("customer_id")%> - <%=rs2.getString("customer_name")%>
					  			<br>
					  			<%=rs2.getString("narration")%><br><br>
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
						  				<th style="width: 85%; text-align: left">Description</th>
						  				<th style="width: 15%; text-align: right">Amount</th>
						  			</tr>
						  			<%
						  			
						  			double total = 0;
						  				ResultSet rs = s.executeQuery("SELECT ci.is_internal, sum(cri.amount) FROM gl_cash_receipts_instruments cri join gl_cash_instruments ci on cri.instrument_id = ci.id where cri.id="+GLCashReceiptID+" group by ci.is_internal");
						  				while( rs.next() ){
						  					
						  					String InstrumentLabel = "Amount Received";
						  					if (rs.getInt(1) == 1){
						  						InstrumentLabel = "Claim";
						  					}
						  					
						  					total += rs.getDouble(2);
						  			%>
							  			<tr>
							  				<td style="text-align:left; font-size: 15px"><%=InstrumentLabel%></td>
							  				<td style="text-align:right; font-size: 15px"><%= Utilities.getDisplayCurrencyFormat(rs.getDouble(2)) %></td>
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
				  			<td colspan="2" style="padding-top: 30px; text-align:right">
				  			
				  				<table style="width: 100%" border="0">
				  					<tr>
				  						<td style="width: 30%"></td>
				  						<td style="width: 40%; text-align:center">
				  							<p>Customer Copy</p>
				  						</td>
				  						<td style="width: 30%; text-align: right">
				  							Created By <%=rs2.getString("created_by")+"-"+rs2.getString("created_by_name")%>
				  						</td>
				  					</tr>
				  				</table>
				  				
				  				
				  			</td>
				  		</tr>
				  	</table>
				  </div>
				  <div style="width: 8.27in; min-height: 3.3in; border: solid 1px; margin-top: 5px;">
				  	<table style="width: 100%; margin-top: 20px;" border="0">
				  	
				  		<tr>
					  		<td style="width: 50%" valign="top">
					  			<img src="/barbecue/barcode?data=<%=rs2.getString("uvid")%>&height=50"><br>
					  			<p style="margin-left: 20px; margin-top: 4px; font-weight: 800;">
					  			Sr #<%=rs2.getString("id")%><br>
					  			<%=rs2.getString("customer_id")%> - <%=rs2.getString("customer_name")%>
					  			<br>
					  			<%=rs2.getString("narration")%><br><br>
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
						  				<th style="width: 85%; text-align: left">Description</th>
						  				<th style="width: 15%; text-align: right">Amount</th>
						  			</tr>
						  			<%
						  			
						  				total = 0;
						  				rs = s.executeQuery("SELECT ci.is_internal, sum(cri.amount) FROM gl_cash_receipts_instruments cri join gl_cash_instruments ci on cri.instrument_id = ci.id where cri.id="+GLCashReceiptID+" group by ci.is_internal");
						  				while( rs.next() ){
						  					
						  					String InstrumentLabel = "Amount Received";
						  					if (rs.getInt(1) == 1){
						  						InstrumentLabel = "Claim";
						  					}
						  					
						  					total += rs.getDouble(2);
						  			%>
							  			<tr>
							  				<td style="text-align:left; font-size: 15px"><%=InstrumentLabel%></td>
							  				<td style="text-align:right; font-size: 15px"><%= Utilities.getDisplayCurrencyFormat(rs.getDouble(2)) %></td>
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
				  			<td colspan="2" style="padding-top: 30px; text-align:right">
				  			
				  				<table style="width: 100%" border="0">
				  					<tr>
				  						<td style="width: 30%"></td>
				  						<td style="width: 40%; text-align:center">
				  							<p>Cash Office Copy</p>
				  						</td>
				  						<td style="width: 30%; text-align: right">
				  							Created By <%=rs2.getString("created_by")+"-"+rs2.getString("created_by_name")%>
				  						</td>
				  					</tr>
				  				</table>
				  				
				  				
				  			</td>
				  		</tr>
				  	</table>
				  </div>
				  <div style="width: 8.27in; min-height: 3.3in; border: solid 1px; margin-top: 5px;">
				  	<table style="width: 100%; margin-top: 20px;" border="0">
				  	
				  		<tr>
					  		<td style="width: 50%" valign="top">
					  			<img src="/barbecue/barcode?data=<%=rs2.getString("uvid")%>&height=50"><br>
					  			<p style="margin-left: 20px; margin-top: 4px; font-weight: 800;">
					  			Sr #<%=rs2.getString("id")%><br>
					  			<%=rs2.getString("customer_id")%> - <%=rs2.getString("customer_name")%>
					  			<br>
					  			<%=rs2.getString("narration")%><br><br>
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
						  				<th style="width: 85%; text-align: left">Description</th>
						  				<th style="width: 15%; text-align: right">Amount</th>
						  			</tr>
						  			<%
						  			
						  				total = 0;
						  				rs = s.executeQuery("SELECT ci.is_internal, sum(cri.amount) FROM gl_cash_receipts_instruments cri join gl_cash_instruments ci on cri.instrument_id = ci.id where cri.id="+GLCashReceiptID+" group by ci.is_internal");
						  				while( rs.next() ){
						  					
						  					String InstrumentLabel = "Amount Received";
						  					if (rs.getInt(1) == 1){
						  						InstrumentLabel = "Claim";
						  					}
						  					
						  					total += rs.getDouble(2);
						  			%>
							  			<tr>
							  				<td style="text-align:left; font-size: 15px"><%=InstrumentLabel%></td>
							  				<td style="text-align:right; font-size: 15px"><%= Utilities.getDisplayCurrencyFormat(rs.getDouble(2)) %></td>
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
				  			<td colspan="2" style="padding-top: 30px; text-align:right">
				  			
				  				<table style="width: 100%" border="0">
				  					<tr>
				  						<td style="width: 30%"></td>
				  						<td style="width: 40%; text-align:center">
				  							<p>Supply Chain Copy</p>
				  						</td>
				  						<td style="width: 30%; text-align: right">
				  							Created By <%=rs2.getString("created_by")+"-"+rs2.getString("created_by_name")%>
				  						</td>
				  					</tr>
				  				</table>
				  				
				  				
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