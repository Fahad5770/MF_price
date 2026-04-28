<%@page import="com.pbc.outlet.OutletDashboard"%>

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
Statement s = c.createStatement();
Statement s2 = c.createStatement();

Long EditID = Utilities.parseLong(request.getParameter("EditID"));

%>


<!-- <ul data-role="listview" data-inset="true"  style="font-size:10px; margin-top:-10px; margin-left:0px" data-icon="false"> -->
<!-- <ul data-role="listview" data-inset="true"  style="font-size:10px;" data-icon="false"> 
	    
		<li data-role="list-divider" data-theme="a">Monthly Discount List</li>
		
		<li>
-->
		<!-- 
		<form id="SamplingDashboardApprovalForm" method="POST" action="sampling/MonthlyDiscountRequestExecute" data-ajax="false">
		 
		 
		<h3>Monthly Discount List</h3>-->
		
		<table data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
		  <thead>
		    <tr>
				<th data-priority="2"></th>
				<th data-priority="persist" >Outlet</th>
				<th data-priority="4" >Current Balance</th>
				<th data-priority="4" >Sampling Amount</th>
				<th data-priority="4" >Deduction against<br>Advance</th>
				<th data-priority="4" >Payable</th>
				<th data-priority="4" >Manual Adjustment</th>
				<th data-priority="4" >Net Payable</th>
				<th data-priority="4"></th>
		    </tr>
		  </thead>
		  <tbody>
		  <%
		  
		  
		  double GTotalCurrentBalance = 0;
		  double GTotalSamplingAmount = 0;
		  double GTotalDeductionAgainstAdvance = 0;
		  double GTotalPayable = 0;
		  double GTotalNetPayable = 0;
		  
		  int Year = 0;
		  int Month = 0;
		  
		  long DocumentID = 0;
		  ResultSet rs_doc = s2.executeQuery("select id from sampling_monthly_request_document where request_id="+EditID);
		  if(rs_doc.first()){
			  DocumentID = rs_doc.getLong("id");
		  }
		  
		  int counter = -1;
		  ResultSet rs2 = s2.executeQuery("select *, (SELECT name FROM common_outlets where id=outlet_id) outlet_name from sampling_monthly_request where document_id="+DocumentID);
		  while(rs2.next()){
			  
			  String DateArray[] = rs2.getString("month").split("-");
			  Year = Utilities.parseInt(DateArray[0]);
			  Month = Utilities.parseInt(DateArray[1]);
			  
			  counter++;
		  %>
		    <tr>
				<td>
					<select name="SamplingAction" id="SamplingAction" data-role="slider" data-mini="true">
					    <option value="0">Cancel</option>
					    <option value="1" selected>Approve</option>
					</select>
					<input type="hidden" name="EditID" value="<%=DocumentID%>">
					<input type="hidden" name="RequestID" value="<%=rs2.getString("request_id")%>">
					<input type="hidden" name="OutletID" value="<%=rs2.getString("outlet_id")%>">
					<input type="hidden" name="DistributorID" value="<%=rs2.getString("distributor_id")%>">
					<input type="hidden" name="SamplingID" value="<%=rs2.getString("sampling_id")%>">
					<input type="hidden" id="ReturnParams" name="ReturnParams" value="">
					
				</td>
				<!-- ><td><%//=rs2.getString("region") %></td>
				<td><%//=rs2.getString("asm_id") %></td>
				<td><%//=rs2.getString("cr_id") %></td>
				<td><%//=rs2.getString("outlet_id") %></td> -->
				<td style="font-weight: normal"><%=rs2.getString("outlet_id") %> - <%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></td>
				<td style="font-weight: normal; text-align:right"><input type="hidden" data-mini="true" style="text-align: right; width: 100px" name="CurrentBalance" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("current_balance")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("current_balance")) %></span></td>
				<td style="font-weight: normal; text-align:right"><input type="hidden" data-mini="true" style="text-align: right; width: 100px" name="SamplingAmount" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("sampling_amount")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("sampling_amount")) %></span></td>
				<td style="font-weight: normal; text-align:right"><input type="hidden" data-mini="true" style="text-align: right; width: 100px" name="DeductionAgainstAdvance" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("deduction_against_advance")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("deduction_against_advance")) %></span></td>
				<td style="font-weight: normal; text-align:right"><input type="hidden" id="FormPayable<%=counter%>" name="payable" style="text-align: right; width: 100px" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %></span></td>
				<td><input type="text" id="FormAdjustment<%=counter%>" name="adjustment" style="text-align: right; width: 100px" data-mini="true" onkeyup="SamplingDashboardCalc(<%=counter%>);" value="<%=rs2.getString("adjustment")%>"></td>
				<td><input type="text" id="FormNetPayable<%=counter%>" name="NetPayable" style="text-align: right; width: 100px" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("net_payable")) %>" readonly="readonly"></td>
				<td style="font-weight: normal">
					<a data-role="button" data-mini="true" data-theme="c" id="SamplingPopupButton" onClick="SamplingDashboardPopup2(<%=rs2.getString("outlet_id")%>, <%=Month%>, <%=Year%>)" style="text-decoration: underline; cursor: pointer" >Detail</a>
				</td>
		    </tr>
		  <%
		    GTotalCurrentBalance += rs2.getDouble("current_balance");
			GTotalSamplingAmount += rs2.getDouble("sampling_amount");
			GTotalDeductionAgainstAdvance += rs2.getDouble("deduction_against_advance");
			GTotalPayable += rs2.getDouble("payable");
		  }
		  %>


		    <tr>
				<td></td>
				<!-- <td></td>
				<td></td>
				<td></td> 
				<td></td> -->
				<td></td>
				<td><input type="text" data-mini="true" style="text-align: right; width: 100px" value="<%=Utilities.getDisplayCurrencyFormat(GTotalCurrentBalance) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right; width: 100px" value="<%=Utilities.getDisplayCurrencyFormat(GTotalSamplingAmount) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right; width: 100px" value="<%=Utilities.getDisplayCurrencyFormat(GTotalDeductionAgainstAdvance) %>" readonly="readonly"></td>
				<td><input type="text" style="text-align: right; width: 100px" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormat(GTotalPayable) %>" readonly="readonly"></td>
				<td></td>
				<td></td>
				<td></td>
		    </tr>
		  </tbody>
		</table>
		<%
		Date EndDate = Utilities.getEndDateByMonth(Month-1, Year);
		%>
		<input type="hidden" name="month" value="<%=Utilities.getSQLDate(EndDate)%>">
		<input type="hidden" name="status" id="SamplingDashboardStatus" value="1">
	<!-- 	</form>  
		</li>
		 
	</ul>
		-->

<%

s2.close();
s.close();
ds.dropConnection();



%>