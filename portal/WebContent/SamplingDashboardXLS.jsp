<%@page autoFlush="false" buffer="100kb" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://digamma.ch/taglib/excel.tld" prefix="xls" %>
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
long POutletID = Utilities.parseLong(request.getParameter("outlet_id"));

int month = Utilities.parseInt(request.getParameter("month"));
int year = Utilities.parseInt(request.getParameter("year"));
int type = Utilities.parseInt(request.getParameter("type"));

Date StartDate = Utilities.getStartDateByMonth(month-1, year);
Date EndDate = Utilities.getEndDateByMonth(month-1, year);

String where = "";

if (PRegion != null && PRegion.length() > 1){
	where += " and s.region = '"+PRegion+"' ";
}
if (POutletID != 0){
	where += " and s.outlet_id = '"+POutletID+"' ";
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

Statement s = c.createStatement();
Statement s2 = c.createStatement();

OutletDashboard op = new OutletDashboard();
%>
<xls:workbook filename="SamplingSummary.xls">
	<xls:sheet name="Summary">
		<xls:row>
			<xls:cell>Region</xls:cell>
			<xls:cell>ASM</xls:cell>
			<xls:cell>CR</xls:cell>
			<xls:cell>Outlet ID</xls:cell>
			<xls:cell>Outlet Name</xls:cell>
			<xls:cell>Current Balance</xls:cell>
			<xls:cell>Sampling Amount</xls:cell>
			<xls:cell>Deduction against Advance</xls:cell>
			<xls:cell>Payable</xls:cell>
			<xls:cell>Manual Adjustment</xls:cell>
			<xls:cell>Net Payable</xls:cell>
		</xls:row>
		  <%
		  
		  s2.executeUpdate("create temporary table temp_monthly_sampling_summary (request_id int(11), sampling_id int(11), region varchar(10), asm_id int(11), cr_id int(11), outlet_id int(11), outlet_name varchar(200), current_balance double, sampling_amount double, deduction_against_advance double, payable double)");
		  
		  double GTotalCurrentBalance = 0;
		  double GTotalSamplingAmount = 0;
		  double GTotalDeductionAgainstAdvance = 0;
		  double GTotalPayable = 0;
		  double GTotalNetPayable = 0;
		  
		  int counter = -1;

		  ResultSet rs = s.executeQuery("SELECT s.request_id, s.sampling_id, s.outlet_id, s.outlet_name, s.business_type, s.address, s.region region, s.asm asm, s.cr cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude, s.fixed_company_share_offpeak, s.fixed_agency_share_offpeak, s.fixed_deduction_term_offpeak FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2 and s.activated_on <= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and s.deactivated_on >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) "+where+" order by s.region");
		  while (rs.next()){
			
		  	counter++;
			
		  	long RequestID = rs.getLong(1);
		  	long SamplingID = rs.getLong(2);
		  	long OutletID = rs.getLong(3);
		  	
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
		    double TotalDeductionAgainstAdvance = FixedDeductionAgainstAdvance + PerCaseDeductionAgainstAdvance;
		    double TotalNetPayable = TotalSamplingAmount - TotalDeductionAgainstAdvance;
			
			s2.executeUpdate("insert into temp_monthly_sampling_summary (request_id, sampling_id, region, asm_id, cr_id, outlet_id, outlet_name, current_balance, sampling_amount, deduction_against_advance, payable) values ("+RequestID+","+SamplingID+",'"+op.OUTLET.REGION_SHORT_NAME+"', "+op.OUTLET.ASM_ID+", "+op.OUTLET.CR_ID+","+OutletID+",'"+op.OUTLET.NAME+"',"+OpeningBalance+","+TotalSamplingAmount+","+TotalDeductionAgainstAdvance+", "+TotalNetPayable+")");
			
		  }
		  rs.close();
			
		  
		  ResultSet rs2 = s.executeQuery("select * from temp_monthly_sampling_summary where sampling_amount != 0 order by sampling_amount desc");
		  while(rs2.next()){
		  %>
		  
		  			<xls:row>
				<xls:cell><%=rs2.getString("region") %></xls:cell>
				<xls:cell><%=rs2.getString("asm_id") %></xls:cell>
				<xls:cell><%=rs2.getString("cr_id") %></xls:cell>
				<xls:cell><%=rs2.getString("outlet_id") %></xls:cell>
				<xls:cell><%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></xls:cell>
				<xls:cell><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("current_balance")) %></xls:cell>
				<xls:cell><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("sampling_amount")) %></xls:cell>
				<xls:cell><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("deduction_against_advance")) %></xls:cell>
				<xls:cell><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %></xls:cell>
				<xls:cell></xls:cell>
				<xls:cell><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %></xls:cell>
			</xls:row>	
		  <%
		    GTotalCurrentBalance += rs2.getDouble("current_balance");
			GTotalSamplingAmount += rs2.getDouble("sampling_amount");
			GTotalDeductionAgainstAdvance += rs2.getDouble("deduction_against_advance");
			GTotalPayable += rs2.getDouble("payable");  		  
		  }
		  %>
			<xls:row>
				<xls:cell width="auto"></xls:cell>
				<xls:cell width="auto"></xls:cell>
				<xls:cell width="auto"></xls:cell>
				<xls:cell width="auto"></xls:cell>
				<xls:cell width="auto"></xls:cell>
				<xls:cell width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalCurrentBalance) %></xls:cell>
				<xls:cell width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalSamplingAmount) %></xls:cell>
				<xls:cell width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalDeductionAgainstAdvance) %></xls:cell>
				<xls:cell width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalPayable) %></xls:cell>
				<xls:cell width="auto"></xls:cell>
				<xls:cell width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalPayable) %></xls:cell>
			</xls:row>		
	</xls:sheet>
</xls:workbook>
<%
s2.executeUpdate("drop table temp_monthly_sampling_summary");

op.close();
s2.close();
s.close();
ds.dropConnection();
%>