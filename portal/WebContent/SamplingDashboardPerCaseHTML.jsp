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
	where += " and o.region = '"+PRegion+"' ";
}
if (POutletID != 0){
	where += " and o.outlet_id = '"+POutletID+"' ";
}
if (PRSMID != 0){
	where += " and o.rsm_id = '"+PRSMID+"' ";
}
if (PASMID != 0){
	where += " and o.asm_id = '"+PASMID+"' ";
}
if (PCRID != 0){
	where += " and o.cr_id = '"+PCRID+"' ";
}

if (PDistributorID != 0){
	where += " and o.customer_id = '"+PDistributorID+"' ";
}

//where += " and s.sampling_id in (select sampling_id from sampling_monthly_approval where status_id = 1 and month = "+Utilities.getSQLDate(EndDate)+") ";

//if (type == 1){
	
//}else if (type == 2){
	//where += " and s.sampling_id in (select sampling_id from sampling_monthly_approval where status_id = 2 and month = "+Utilities.getSQLDate(EndDate)+") ";
//}else if (type == 0){
	//where += " and s.sampling_id not in (select sampling_id from sampling_monthly_approval where month = "+Utilities.getSQLDate(EndDate)+") ";
//}

Statement s = c.createStatement();
Statement s2 = c.createStatement();

//OutletDashboard op = new OutletDashboard();
%>
<table border="1">
	<tbody>
		<tr>
			<td>Region</td>
			<td>Distributor ID</td>
			<td>Distributor Name</td>
			<td>ASM</td>
			<td>CR</td>
			<td>Outlet ID</td>
			<td>Outlet Name</td>
			<td>Package</td>
			<td>Brand</td>
			<td>Current Balance</td>
			<td>Company Share</td>
			<td>Deduction Term</td>
			<td>Sales Quantity</td>
			<td>Sampling Amount</td>
			<td>Deduction against Advance</td>
			<td>Payable</td>
			<td>Manual Adjustment</td>
			<td>Net Payable</td>
			<td>Status</td>
			<td>Barcode</td>
		</tr>
		  <%
		  /*
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
		    
		    GTotalCurrentBalance += OpeningBalance;
			GTotalSamplingAmount += TotalSamplingAmount;
			GTotalDeductionAgainstAdvance += TotalDeductionAgainstAdvance;
			GTotalPayable += TotalNetPayable;
			
			s2.executeUpdate("insert into temp_monthly_sampling_summary (request_id, sampling_id, region, asm_id, cr_id, outlet_id, outlet_name, current_balance, sampling_amount, deduction_against_advance, payable) values ("+RequestID+","+SamplingID+",'"+op.OUTLET.REGION_SHORT_NAME+"', "+op.OUTLET.ASM_ID+", "+op.OUTLET.CR_ID+","+OutletID+",'"+op.OUTLET.NAME+"',"+OpeningBalance+","+TotalSamplingAmount+","+TotalDeductionAgainstAdvance+", "+TotalNetPayable+")");
			
		  }
		  rs.close();
			*/
		  
		  double GTotalCurrentBalance = 0;
		  double GTotalSamplingAmount = 0;
		  double GTotalDeductionAgainstAdvance = 0;
		  double GTotalPayable = 0;
		  double GTotalNetPayable = 0;
		  
		  ResultSet rs2 = s.executeQuery("SELECT * FROM sampling_monthly_approval sma, outletmaster o where sma.outlet_id = o.outlet_id and sma.status_id = 1 "+where);
		  while(rs2.next()){
			  
			  long OutletID = rs2.getLong("outlet_id");
			  
			  OutletDashboard op = new OutletDashboard();
			  op.setOutletID(OutletID);

			  double FixedSamplingAmount = op.getFixedDiscountAmount(EndDate);
			  double FixedDeductionAgainstAdvance = op.getFixedDiscountDeductionAmount(EndDate);
			  double FixedNetPayable = FixedSamplingAmount - FixedDeductionAgainstAdvance;

			  ArrayList<String> PackageID = new ArrayList<String>();
			  ArrayList<String> PackageName = new ArrayList<String>();
			  ArrayList<String> BrandName = new ArrayList<String>();
			  ArrayList<Double> PerCaseCompanyShare = new ArrayList<Double>();
			  ArrayList<Double> PerCaseDeductionTerm = new ArrayList<Double>();
			  ArrayList<Integer> PerCaseSales = new ArrayList<Integer>();

			  ResultSet rs5 = op.getPerCaseDiscountResultSet(EndDate);
			  while(rs5.next()){
			  	PackageID.add(rs5.getString("package"));
			  	PackageName.add(rs5.getString("package_name"));
			  	BrandName.add(rs5.getString("brand_name"));
			  	PerCaseCompanyShare.add(rs5.getDouble("company_share"));
			  	PerCaseDeductionTerm.add(rs5.getDouble("deduction_term"));
			  	PerCaseSales.add(rs5.getInt("qty"));
			  }		  	
			  
				op.close();
			  	
				%>
				  	<tr>
						<td><%=rs2.getString("region") %></td>
						<td><%=rs2.getString("customer_id") %></td>
						<td><%=rs2.getString("customer_name") %></td>
						<td><%=rs2.getString("asm_id") %></td>
						<td><%=rs2.getString("cr_id") %></td>
						<td><%=OutletID %></td>
						<td><%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></td>
						<td>Fixed</td>
						<td></td>
						<td></td>
						<td><%=FixedSamplingAmount %></td>
						<td><%=FixedDeductionAgainstAdvance %></td>
						<td></td>
						<td><%= Utilities.getDisplayCurrencyFormatSimple(FixedSamplingAmount) %></td>
						<td><%= Utilities.getDisplayCurrencyFormatSimple(FixedDeductionAgainstAdvance) %></td>
						<td><%= Utilities.getDisplayCurrencyFormatSimple(FixedNetPayable) %></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>	

				<%			  
			  
				for (int i = 0; i < PackageName.size(); i++){
					
					double PerCaseSamplingAmount = PerCaseSales.get(i).intValue() * PerCaseCompanyShare.get(i).doubleValue();
					double PerCaseDeductionAgainstAdvance = PerCaseSales.get(i).intValue() * PerCaseDeductionTerm.get(i).doubleValue();
					double PerCaseNetPayable = PerCaseSamplingAmount - PerCaseDeductionAgainstAdvance;
					
					if (PerCaseSamplingAmount != 0){
					%>
				  	<tr>
						<td><%=rs2.getString("region") %></td>
						<td><%=rs2.getString("customer_id") %></td>
						<td><%=rs2.getString("customer_name") %></td>
						<td><%=rs2.getString("asm_id") %></td>
						<td><%=rs2.getString("cr_id") %></td>
						<td><%=OutletID %></td>
						<td><%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></td>
						<td><%= PackageName.get(i) %></td>
						<td><%= BrandName.get(i) %></td>
						<td></td>
						<td><%=PerCaseCompanyShare.get(i) %></td>
						<td><%=PerCaseDeductionTerm.get(i) %></td>
						<td><%=PerCaseSales.get(i) %></td>
						<td><%= Utilities.getDisplayCurrencyFormatSimple(PerCaseSamplingAmount) %></td>
						<td><%= Utilities.getDisplayCurrencyFormatSimple(PerCaseDeductionAgainstAdvance) %></td>
						<td><%= Utilities.getDisplayCurrencyFormatSimple(PerCaseNetPayable) %></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>	
					<%
					}
				}
		  %>
		  
		  	<tr>
				<td><%=rs2.getString("region") %></td>
				<td><%=rs2.getString("customer_id") %></td>
				<td><%=rs2.getString("customer_name") %></td>
				<td><%=rs2.getString("asm_id") %></td>
				<td><%=rs2.getString("cr_id") %></td>
				<td><%=OutletID %></td>
				<td><%=WordUtils.capitalizeFully(rs2.getString("outlet_name"))%></td>
				<td></td>
				<td></td>
				<td><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("current_balance")) %></td>
				<td></td>
				<td></td>
				<td></td>
				<td><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("sampling_amount")) %></td>
				<td><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("deduction_against_advance")) %></td>
				<td><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %></td>
				<td><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("adjustment")) %></td>
				<td><%=Utilities.getDisplayCurrencyFormatSimple(rs2.getDouble("payable")) %></td>
				<td><%if (rs2.getInt("is_printed") == 1){out.print("Printed");}else{out.print("Not Printed");} %></td>
				<td><%=rs2.getString("barcode") %></td>
			</tr>	
		  <%
		    GTotalCurrentBalance += rs2.getDouble("current_balance");
			GTotalSamplingAmount += rs2.getDouble("sampling_amount");
			GTotalDeductionAgainstAdvance += rs2.getDouble("deduction_against_advance");
			GTotalPayable += rs2.getDouble("payable");  		  
		  }
		  %>
			<tr>
				<td width="auto"></td>
				<td></td>
				<td></td>
				<td width="auto"></td>
				<td width="auto"></td>
				<td width="auto"></td>
				<td width="auto"></td>
				<td></td>
				<td></td>
				<td width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalCurrentBalance) %></td>
				<td></td>
				<td></td>
				<td></td>
				<td width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalSamplingAmount) %></td>
				<td width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalDeductionAgainstAdvance) %></td>
				<td width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalPayable) %></td>
				<td width="auto"></td>
				<td width="auto"><%=Utilities.getDisplayCurrencyFormatSimple(GTotalPayable) %></td>
				<td></td>
				<td></td>
			</tr>		
	</tbody>
</table>
<%
//s2.executeUpdate("drop table temp_monthly_sampling_summary");

s2.close();
s.close();
ds.dropConnection();
%>