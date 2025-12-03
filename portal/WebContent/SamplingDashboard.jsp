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

String params = request.getParameter("params");

String PRegion = Utilities.filterString(request.getParameter("region"), 1, 20);
long PDistributorID = Utilities.parseLong(request.getParameter("distributor"));
long PRSMID = Utilities.parseLong(request.getParameter("rsm"));
long PASMID = Utilities.parseLong(request.getParameter("asm"));
long PCRID = Utilities.parseLong(request.getParameter("cr"));
String POutletID = Utilities.filterString(request.getParameter("outlet_id"), 1, 500);

int month = Utilities.parseInt(request.getParameter("month"));
int year = Utilities.parseInt(request.getParameter("year"));
int type = Utilities.parseInt(request.getParameter("type"));

Date StartDate = Utilities.getStartDateByMonth(month-1, year);
Date EndDate = Utilities.getEndDateByMonth(month-1, year);

String where = "";

if (PRegion != null && PRegion.length() > 1){
	where += " and s.region = '"+PRegion+"' ";
}
if (POutletID != null && POutletID.length() > 1){
	where += " and s.outlet_id in ( "+POutletID+" ) ";
}
if (PRSMID != 0){
	where += " and s.rsm = '"+PRSMID+"' ";
}
if (PASMID != 0){
	where += " and s.asm = '"+PASMID+"' ";
}
if (PCRID != 0){
	where += " and s.cr = '"+PCRID+"' ";
}

if (PDistributorID != 0){
	where += " and s.outlet_id in (select distinct outlet_id from sap_sales where month_zmonth = "+month+" and year_zyear = "+year+" and customer_kunnr = "+PDistributorID+")";
}

if (type == 1){
	where += " and s.sampling_id in (select sampling_id from sampling_monthly_approval where status_id = 1 and month = "+Utilities.getSQLDate(EndDate)+") ";
}else if (type == 2){
	where += " and s.sampling_id in (select sampling_id from sampling_monthly_approval where status_id = 2 and month = "+Utilities.getSQLDate(EndDate)+") ";
}else if (type == 0){
	where += " and s.sampling_id not in (select sampling_id from sampling_monthly_approval where month = "+Utilities.getSQLDate(EndDate)+") ";
}
else if (type == 3){
	where += " and s.sampling_id in (select sampling_id from sampling_monthly_approval where status_id = 3 and month = "+Utilities.getSQLDate(EndDate)+") ";
}

Statement s = c.createStatement();
Statement s2 = c.createStatement();

OutletDashboard op = new OutletDashboard();
%>
<html>

<head>

	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->

</head>

<div data-role="page" id="SamplingDashboard" data-url="SamplingDashboard" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Sampling Dashboard" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
		<form id="SamplingDashboardApprovalForm" method="POST" action="sampling/SamplingDashboardExecute" data-ajax="false">
		<table data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
		  <thead>
		    <tr>
				<th data-priority="2"></th>
				<th data-priority="2">Region</th>
				<th data-priority="3">ASM</th>
				<th data-priority="4">CR</th>
				<th data-priority="1">Outlet ID</th>
				<th data-priority="persist">Outlet Name</th>
				<th data-priority="4">Current Balance</th>
				<th data-priority="4">Sampling Amount</th>
				<th data-priority="4">Deduction against Advance</th>
				<th data-priority="4">Payable</th>
				<th data-priority="4">Manual Adjustment</th>
				<th data-priority="4">Net Payable</th>
				<th data-priority="4"></th>
		    </tr>
		  </thead>
		  <tbody>
		  <%
		  
		  s2.executeUpdate("create temporary table temp_monthly_sampling_summary (request_id int(11), sampling_id int(11), region varchar(10), asm_id int(11), cr_id int(11), outlet_id int(11),distributor_id int(11), outlet_name varchar(200), current_balance double, sampling_amount double, deduction_against_advance double, payable double)");
		  
		  double GTotalCurrentBalance = 0;
		  double GTotalSamplingAmount = 0;
		  double GTotalDeductionAgainstAdvance = 0;
		  double GTotalPayable = 0;
		  double GTotalNetPayable = 0;
		  
		  
		
		  ResultSet rs = s.executeQuery("SELECT s.request_id, s.sampling_id, s.outlet_id,m.Customer_ID , s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and s.activated_on <= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and s.deactivated_on >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) "+where+" order by s.region");
		  //out.print("SELECT s.request_id, s.sampling_id, s.outlet_id,m.Customer_ID , s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and s.activated_on <= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and s.deactivated_on >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) "+where+" order by s.region");
		  while (rs.next()){
		  	long RequestID = rs.getLong(1);
		  	long SamplingID = rs.getLong(2);
		  	long OutletID = rs.getLong(3);
		  	long DistributorID = rs.getLong(4);
		  	
		  	op.setOutletID(OutletID);
		  	
		  	double OpeningBalance = op.OUTLET.getCurrentBalance();
		  	double FixedSamplingAmount = op.getFixedDiscountAmount(EndDate);
		  	double FixedDeductionAgainstAdvance = op.getFixedDiscountDeductionAmount(EndDate);
		    double FixedNetPayable = FixedSamplingAmount - FixedDeductionAgainstAdvance;
		    
		    double PerCaseDiscountAmountAndDeduction[] = op.getPerCaseDiscountAmountAndDeductionAmount(EndDate);
		    double PerCaseSamplingAmount = PerCaseDiscountAmountAndDeduction[0];
		    double PerCaseDeductionAgainstAdvance = PerCaseDiscountAmountAndDeduction[1];
		    double PerCaseNetPayable = PerCaseSamplingAmount - PerCaseDeductionAgainstAdvance;
		    
		    double TotalSamplingAmount = FixedSamplingAmount + PerCaseSamplingAmount;
		    if (TotalSamplingAmount < 0){
		    	TotalSamplingAmount = 0;
		    }
		    
		    double TotalDeductionAgainstAdvance = FixedDeductionAgainstAdvance + PerCaseDeductionAgainstAdvance;
		    double TotalNetPayable = TotalSamplingAmount - TotalDeductionAgainstAdvance;
		    

			
			s2.executeUpdate("insert into temp_monthly_sampling_summary (request_id, sampling_id, region, asm_id, cr_id, outlet_id,distributor_id, outlet_name, current_balance, sampling_amount, deduction_against_advance, payable) values ("+RequestID+","+SamplingID+",'"+op.OUTLET.REGION_SHORT_NAME+"', "+op.OUTLET.ASM_ID+", "+op.OUTLET.CR_ID+","+OutletID+","+DistributorID+",'"+op.OUTLET.NAME+"',"+OpeningBalance+","+TotalSamplingAmount+","+TotalDeductionAgainstAdvance+", "+TotalNetPayable+")");
			
		  }
		  rs.close();
			
		  int counter = -1;
		  ResultSet rs2 = s.executeQuery("select * from temp_monthly_sampling_summary where sampling_amount != 0 order by sampling_amount desc");
		  while(rs2.next()){
			  
			  counter++;
		  %>
		    <tr>
				<td>
					<select name="SamplingAction" id="SamplingAction" data-role="slider" data-mini="true">
					    <option value="0"></option>
					    <option value="1" <%if (type == 0){%>selected<%} %>></option>
					</select>
					<input type="hidden" name="RequestID" value="<%=rs2.getString("request_id")%>">
					<input type="hidden" name="OutletID" value="<%=rs2.getString("outlet_id")%>">
					<input type="hidden" name="DistributorID" value="<%=rs2.getString("distributor_id")%>">
					<input type="hidden" name="SamplingID" value="<%=rs2.getString("sampling_id")%>">
					<input type="hidden" id="ReturnParams" name="ReturnParams" value="<%=params%>">
					
				</td>
				<td><%=rs2.getString("region") %></td>
				<td><%=rs2.getString("asm_id") %></td>
				<td><%=rs2.getString("cr_id") %></td>
				<td><%=rs2.getString("outlet_id") %></td>
				<td><%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></td>
				<td><input type="text" data-mini="true" style="text-align: right;" name="CurrentBalance" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("current_balance")) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right;" name="SamplingAmount" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("sampling_amount")) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right;" name="DeductionAgainstAdvance" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("deduction_against_advance")) %>" readonly="readonly"></td>
				<td><input type="text" id="FormPayable<%=counter%>" name="payable" style="text-align: right;" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %>" readonly="readonly"></td>
				<td><input type="text" id="FormAdjustment<%=counter%>" name="adjustment" style="text-align: right;" data-mini="true" onkeyup="SamplingDashboardCalc(<%=counter%>);"></td>
				<td><input type="text" id="FormNetPayable<%=counter%>" name="NetPayable" style="text-align: right;" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %>" readonly="readonly"></td>
				<td><a data-role="button" data-mini="true" data-theme="c" id="SamplingPopupButton" onClick="SamplingDashboardPopup(<%=rs2.getString("outlet_id")%>, <%=month%>, <%=year%>)">Detail</a></td>
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
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td><input type="text" data-mini="true" style="text-align: right;" value="<%=Utilities.getDisplayCurrencyFormat(GTotalCurrentBalance) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right;" value="<%=Utilities.getDisplayCurrencyFormat(GTotalSamplingAmount) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right;" value="<%=Utilities.getDisplayCurrencyFormat(GTotalDeductionAgainstAdvance) %>" readonly="readonly"></td>
				<td><input type="text" style="text-align: right;" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormat(GTotalPayable) %>" readonly="readonly"></td>
				<td></td>
				<td></td>
				<td></td>
		    </tr>
		  </tbody>
		</table>
		<input type="hidden" name="month" value="<%=Utilities.getSQLDate(EndDate)%>">
		<input type="hidden" name="status" id="SamplingDashboardStatus" value="1">
		</form>
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="SamplingDashboardApproveButton">Approve</button>
		<button data-icon="check" data-theme="b" data-inline="true" id="SamplingDashboardOnHoldButton">On Hold</button>
		<button data-icon="check" data-theme="b" data-inline="true" id="SamplingDashboardCancelledButton">Cancel</button>
	</div>    	
    </div>

	<div data-role="popup" id="SamplingDetailPopup" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="max-width:800px;min-width:800px;max-height:600px;" class="ui-corner-all">
	<a href="#SamplingDashboard" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
	    <div data-role="content" data-theme="c" class="ui-corner-bottom ui-content" id="SamplingDetailContent">
			<img src="images/loader.gif">
	    </div>
	</div>     

</div>


</html>
<%
s2.executeUpdate("drop table temp_monthly_sampling_summary");

op.close();
s2.close();
s.close();
ds.dropConnection();
%>