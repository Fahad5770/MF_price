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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(27, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
int month = Utilities.parseInt(request.getParameter("month"));
int year = Utilities.parseInt(request.getParameter("year"));
String region = Utilities.filterString(request.getParameter("region"), 1, 20);
long DistributorID = Utilities.parseLong(request.getParameter("distributor"));


Date StartDate = Utilities.getStartDateByMonth(month-1, year);
Date EndDate = Utilities.getEndDateByMonth(month-1, year);

OutletDashboard op = new OutletDashboard();

Statement s10 = ds.createStatement();

String where = "";
if (region != null && region.length() > 1){
	where += " and cr.region_short_name = '"+region+"' ";
}
if (DistributorID != 0){
	where += " and co.distributor_id = "+DistributorID;
}
%>
				<table border="1" style="border-collapse: collapse;">
				  <thead>
				    <tr>
				    	<th data-priority="1" astyle="font-weight: 400;">Outlet</th>
						<th data-priority="1" astyle="font-weight: 400;">Package</th>
						<th data-priority="1" astyle="font-weight: 400;">Brand</th>
						<th data-priority="1" astyle="font-weight: 400;">Company Share</th>
						<th data-priority="1" astyle="font-weight: 400;">Deduction Term</th>
						<th data-priority="2" astyle="font-weight: 400;">Sales Quantity</th>
						<th data-priority="3" astyle="font-weight: 400;">Sampling Amount</th>
						<th data-priority="3" astyle="font-weight: 400;">Deduction against Advance</th>
						<th data-priority="3" astyle="font-weight: 400;">Payable</th>
				    </tr>
				  </thead>
				  <tbody>
<%
ResultSet rs = s10.executeQuery("select co.id from common_outlets co join common_regions cr on co.region_id = cr.region_id where co.id in (select outlet_id from sampling where active = 1) "+where);
while(rs.next()){

op.setOutletID(rs.getLong(1));


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
		  	
		  %>


		    <tr>
		    	<td data-priority="1" style="font-weight: 400;"><%=op.OUTLET.ID + " - "+op.OUTLET.NAME %></td>
				<td data-priority="1" style="font-weight: 400;">Fixed</td>
				<td data-priority="1">&nbsp;</td>
				<td data-priority="1"><%=FixedSamplingAmount %></td>
				<td data-priority="1"><%=FixedDeductionAgainstAdvance %></td>
				<td data-priority="2"></td>
				<td data-priority="3"><%=FixedSamplingAmount %></td>
				<td data-priority="3"><%=FixedDeductionAgainstAdvance %></td>
				<td data-priority="3"><%= FixedNetPayable%></td>
						    
		    </tr>				<%
		    
		    double TotalPerCaseSamplingAmount = 0;
		    double TotalPerCaseDeductionAgainstAdvance = 0;
		    double TotalPerCaseNetPayable = 0;
		    
			for (int i = 0; i < PackageName.size(); i++){
				
				double PerCaseSamplingAmount = PerCaseSales.get(i).intValue() * PerCaseCompanyShare.get(i).doubleValue();
				double PerCaseDeductionAgainstAdvance = PerCaseSales.get(i).intValue() * PerCaseDeductionTerm.get(i).doubleValue();
				double PerCaseNetPayable = PerCaseSamplingAmount - PerCaseDeductionAgainstAdvance;
				TotalPerCaseSamplingAmount += PerCaseSamplingAmount;
				TotalPerCaseDeductionAgainstAdvance += PerCaseDeductionAgainstAdvance;
				TotalPerCaseNetPayable += PerCaseNetPayable;
			%>
		    <tr>
		    
		    	<td data-priority="1"><%=op.OUTLET.ID + " - "+op.OUTLET.NAME %></td>
				<td data-priority="1"><%= PackageName.get(i) %></td>
				<td data-priority="1"><%= BrandName.get(i) %></td>
				<td data-priority="1"><%=PerCaseCompanyShare.get(i) %></td>
				<td data-priority="1"><%=PerCaseDeductionTerm.get(i) %></td>
				<td data-priority="2"><%=PerCaseSales.get(i) %></td>
				<td data-priority="3"><%= Utilities.getDisplayCurrencyFormat(PerCaseSamplingAmount) %></td>		    
				
				<td data-priority="3"><%= Utilities.getDisplayCurrencyFormat(PerCaseDeductionAgainstAdvance) %></td>		    
				<td data-priority="3"><%= Utilities.getDisplayCurrencyFormat(PerCaseNetPayable) %></td>		    
		    </tr>	
		    <%
			}
		    %>
		    		    <tr>
		    
		    	<td data-priority="1">Total: <%=op.OUTLET.ID + " - "+op.OUTLET.NAME %></td>
		    	
				<td data-priority="1"></td>
				<td data-priority="1"></td>
				<td data-priority="1"></td>
				<td data-priority="1"></td>
				<td data-priority="2"></td>
				<td data-priority="3"><%= Utilities.getDisplayCurrencyFormat(TotalPerCaseSamplingAmount) %></td>		    
				
				<td data-priority="3"><%= Utilities.getDisplayCurrencyFormat(TotalPerCaseDeductionAgainstAdvance) %></td>		    
				<td data-priority="3"><%= Utilities.getDisplayCurrencyFormat(TotalPerCaseNetPayable) %></td>	   
		    </tr>

<%
}
%>
		    </tbody>
		    </table>
<%
op.close();
ds.dropConnection();
%>