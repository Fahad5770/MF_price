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

long SelectedRegions[] = {0};

if (session.getAttribute("MonthlyDiscountRegions") != null){
	SelectedRegions = (long[])session.getAttribute("MonthlyDiscountRegions");
}
String RegionQueryString = "";
for(int i = 0; i < SelectedRegions.length; i++ ){
	if(i > 0){
		RegionQueryString += ", ";
	}
	RegionQueryString += SelectedRegions[i]+"";
}

String PRegion = Utilities.filterString(request.getParameter("region"), 1, 20);



long SelectedDistributors[] = {0};

if (session.getAttribute("MonthlyDiscountDistributors") != null){
	SelectedDistributors = (long[])session.getAttribute("MonthlyDiscountDistributors");
}

//long PDistributorID = Utilities.parseLong(request.getParameter("distributor"));
long PDistributorID = SelectedDistributors[0];
String DistributorQueryString = "";
for(int i = 0; i < SelectedDistributors.length; i++ ){
	if(i > 0){
		DistributorQueryString += ", ";
	}
	DistributorQueryString += SelectedDistributors[i]+"";
}

long PRSMID = Utilities.parseLong(request.getParameter("rsm"));
long PASMID = Utilities.parseLong(request.getParameter("asm"));
long PCRID = Utilities.parseLong(request.getParameter("cr"));



String OutletQueryString = "";
if(session.getAttribute("MonthlyDiscountOutlets") != null){
	OutletQueryString = (String) session.getAttribute("MonthlyDiscountOutlets");
}

String POutletID = Utilities.filterString(request.getParameter("outlet_id"), 1, 500);
/*
int month = Utilities.parseInt(request.getParameter("month"));
int year = Utilities.parseInt(request.getParameter("year"));
int type = Utilities.parseInt(request.getParameter("type"));
*/

int month = 0;
if(session.getAttribute("MonthlyDiscountMonth") != null){
	month = (Integer) session.getAttribute("MonthlyDiscountMonth");
}

int year = 0;
if(session.getAttribute("MonthlyDiscountYear") != null){
	year = (Integer) session.getAttribute("MonthlyDiscountYear");
}

int type = -1;
if(session.getAttribute("MonthlyDiscountStatus") != null){
	type = (Integer) session.getAttribute("MonthlyDiscountStatus");
}

Date StartDate = Utilities.getStartDateByMonth(month-1, year);
Date EndDate = Utilities.getEndDateByMonth(month-1, year);

String where = "";

if (session.getAttribute("MonthlyDiscountRegions") != null){
	where += " and s.region in (SELECT region_short_name FROM common_regions where region_id in ("+RegionQueryString+")) ";
}
if (!OutletQueryString.equals("")){
	where += " and s.outlet_id in ( "+OutletQueryString+" ) ";
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

if (session.getAttribute("MonthlyDiscountDistributors") != null){
	where += " and s.outlet_id in (select distinct outlet_id from sap_sales where month_zmonth = "+month+" and year_zyear = "+year+" and customer_kunnr in ("+DistributorQueryString+") )";
}
//System.out.println("Type:"+type);
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

where += " and s.outlet_id not in ( SELECT outlet_id FROM sampling_monthly_request where month="+Utilities.getSQLDate(EndDate)+" ) ";

Statement s = c.createStatement();
Statement s2 = c.createStatement();

OutletDashboard op = new OutletDashboard();
%>


<!--
<ul data-role="listview" data-inset="true"  style="font-size:10px; margin-top:-10px; margin-left:0px" data-icon="false">
	    
		<li data-role="list-divider" data-theme="a">Monthly Discount List</li>
		
		<li>

		<h3>Monthly Discount List</h3>
		-->
		<table data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
		  <thead>
		    <tr>
				<th data-priority="2"></th>
				<!-- <th data-priority="2">Region</th>
				<th data-priority="3">ASM</th>
				<th data-priority="4">CR</th> -->
				<!-- <th data-priority="1">Outlet ID</th> -->
				<th data-priority="persist" style="font-size: 12px">Outlet Name</th>
				<th data-priority="4" style="font-size: 12px">Current Balance</th>
				<th data-priority="4" style="font-size: 12px">Sampling Amount</th>
				<th data-priority="4" style="font-size: 12px">Deduction against Advance</th>
				<th data-priority="4" style="font-size: 12px">Payable</th>
				<th data-priority="4" style="font-size: 12px">Manual Adjustment</th>
				<th data-priority="4" style="font-size: 12px">Net Payable</th>
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
		  
		  int TotalCount = 0;
		  
		  System.out.println("SELECT s.request_id, s.sampling_id, s.outlet_id,m.Customer_ID , s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and from_days(to_days(s.activated_on)-120) <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-121) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) /*  and s.active = 1 */ /* and s.outlet_id in (select id from common_outlets where cache_distributor_id in (100951,100287,100356,100657,100706,100835,100899,100902,100914,100961,100957,100959)) */ /* and m.customer_id not in (100951,100287,100356,100657,100706,100835,100899,100902,100914,100961,100957,100959) */ "+where+" order by s.region");
		  
		  ResultSet rs = s.executeQuery("SELECT s.request_id, s.sampling_id, s.outlet_id,m.Customer_ID , s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak, ifnull((select cache_distributor_id from common_outlets where id = s.outlet_id),m.Customer_ID) cache_distributor_id FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and from_days(to_days(s.activated_on)-120) <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-121) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) /*  and s.active = 1 */ /* and s.outlet_id in (select id from common_outlets where cache_distributor_id in (100951,100287,100356,100657,100706,100835,100899,100902,100914,100961,100957,100959)) */ /* and m.customer_id not in (100951,100287,100356,100657,100706,100835,100899,100902,100914,100961,100957,100959) */ and s.outlet_id  in (select id from common_outlets where account_number_bank_alfalah is null) "+where+" order by s.region");
		  //System.out.println();
		  //out.print("SELECT s.request_id, s.sampling_id, s.outlet_id,m.Customer_ID , s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and s.activated_on <= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and s.deactivated_on >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) "+where+" order by s.region");
			//System.out.println("SELECT s.request_id, s.sampling_id, s.outlet_id,m.Customer_ID , s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) /* and s.active = 1 */ "+where+" order by s.region");
		  while (rs.next()){
		  	long RequestID = rs.getLong(1);
		  	long SamplingID = rs.getLong(2);
		  	long OutletID = rs.getLong(3);
		  	long DistributorID = rs.getLong("cache_distributor_id");
		  	
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
			//System.out.println("insert into temp_monthly_sampling_summary (request_id, sampling_id, region, asm_id, cr_id, outlet_id,distributor_id, outlet_name, current_balance, sampling_amount, deduction_against_advance, payable) values ("+RequestID+","+SamplingID+",'"+op.OUTLET.REGION_SHORT_NAME+"', "+op.OUTLET.ASM_ID+", "+op.OUTLET.CR_ID+","+OutletID+","+DistributorID+",'"+op.OUTLET.NAME+"',"+OpeningBalance+","+TotalSamplingAmount+","+TotalDeductionAgainstAdvance+", "+TotalNetPayable+")");
			
			if (TotalSamplingAmount != 0){
				TotalCount++;
			}
			
		  }
		  rs.close();
			
		  int counter = -1;
		  ResultSet rs2 = s.executeQuery("select * from temp_monthly_sampling_summary where sampling_amount != 0 order by sampling_amount desc limit 700");
		  while(rs2.next()){
			  
			  counter++;
		  %>
		    <tr>
				<td>
					<select name="SamplingAction" id="SamplingAction" data-role="slider" data-mini="true">
					    <option value="0" >Cancel</option>
					    <option value="1" selected>Approve</option>
					    <!-- <option value="1" <%//if (type == 0){%>selected<%//} %> >Approve</option> -->
					</select>
					<input type="hidden" name="RequestID" value="<%=rs2.getString("request_id")%>">
					<input type="hidden" name="OutletID" value="<%=rs2.getString("outlet_id")%>">
					<input type="hidden" name="DistributorID" value="<%=rs2.getString("distributor_id")%>">
					<input type="hidden" name="SamplingID" value="<%=rs2.getString("sampling_id")%>">
					<input type="hidden" id="ReturnParams" name="ReturnParams" value="<%=params%>">
					
				</td>
				
				<!-- ><td><%//=rs2.getString("region") %></td>
				<td><%//=rs2.getString("asm_id") %></td>
				<td><%//=rs2.getString("cr_id") %></td>
				<td><%//=rs2.getString("outlet_id") %></td> -->
				<td style="font-size: 12px"><%=rs2.getString("outlet_id") %> - <%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></td>
				<td style="font-size: 12px; text-align:right"><input type="hidden" data-mini="true" style="text-align: right; width: 100px; " name="CurrentBalance" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("current_balance")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("current_balance")) %></span></td>
				<td style="font-size: 12px; text-align:right"><input type="hidden" data-mini="true" style="text-align: right; width: 100px; " name="SamplingAmount" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("sampling_amount")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("sampling_amount")) %></span></td>
				<td style="font-size: 12px; text-align:right"><input type="hidden" data-mini="true" style="text-align: right; width: 100px; " name="DeductionAgainstAdvance" value="<%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("deduction_against_advance")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("deduction_against_advance")) %></span></td>
				<td style="font-size: 12px; text-align:right"><input type="hidden" id="FormPayable<%=counter%>" name="payable" style="text-align: right; width: 100px; " data-mini="true" value="<%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %>" readonly="readonly"><span><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %></span></td>
				<td><input type="text" id="FormAdjustment<%=counter%>" name="adjustment" style="text-align: right; width: 50px; font-size: 12px" data-mini="true" onkeyup="SamplingDashboardCalc(<%=counter%>);"></td>
				<td><input type="text" id="FormNetPayable<%=counter%>" name="NetPayable" style="text-align: right; width: 50px; font-size: 12px" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %>" readonly="readonly"></td> 
				
				<td style="font-size: 12px"><a data-role="button" data-mini="true" data-theme="c" id="SamplingPopupButton" onClick="SamplingDashboardPopup2(<%=rs2.getString("outlet_id")%>, <%=month%>, <%=year%>)"  style="text-decoration: underline; cursor: pointer">Detail</a></td>
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
				<td><input type="text" data-mini="true" style="text-align: right; width: 80px" value="<%=Utilities.getDisplayCurrencyFormat(GTotalCurrentBalance) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right; width: 80px" value="<%=Utilities.getDisplayCurrencyFormat(GTotalSamplingAmount) %>" readonly="readonly"></td>
				<td><input type="text" data-mini="true" style="text-align: right; width: 80px" value="<%=Utilities.getDisplayCurrencyFormat(GTotalDeductionAgainstAdvance) %>" readonly="readonly"></td>
				<td><input type="text" style="text-align: right; width: 80px" data-mini="true" value="<%=Utilities.getDisplayCurrencyFormat(GTotalPayable) %>" readonly="readonly"></td>
				<td></td>
				<td></td>
				<td>Count:<%= (counter+1)%>/<%=TotalCount %></td>
		    </tr>
		  </tbody>
		</table>
	
		<input type="hidden" name="month" value="<%=Utilities.getSQLDate(EndDate)%>">
		<input type="hidden" name="status" id="SamplingDashboardStatus" value="1">
		<!-- 
		</li>
		 
	</ul>
	 -->
	

<%
s2.executeUpdate("drop table temp_monthly_sampling_summary");

op.close();
s2.close();
s.close();
ds.dropConnection();
%>