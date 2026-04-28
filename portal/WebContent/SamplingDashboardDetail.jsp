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

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
int month = Utilities.parseInt(request.getParameter("month"));
int year = Utilities.parseInt(request.getParameter("year"));

Date StartDate = Utilities.getStartDateByMonth(month-1, year);
Date EndDate = Utilities.getEndDateByMonth(month-1, year);

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
		  	
		  %>

				<table data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
				  <thead>
				    <tr>
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
		    <tr>
				<td data-priority="1" style="font-weight: 400;">Fixed</td>
				<td data-priority="1">&nbsp;</td>
				<td data-priority="1"><input type="text" data-mini="true" value="<%=FixedSamplingAmount %>"></td>
				<td data-priority="1"><input type="text" data-mini="true" value="<%=FixedDeductionAgainstAdvance %>"></td>
				<td data-priority="2"><input type="text" data-mini="true" value=""></td>
				<td data-priority="3"><input type="text" data-mini="true" value="<%=FixedSamplingAmount %>"></td>
				<td data-priority="3"><input type="text" data-mini="true" value="<%=FixedDeductionAgainstAdvance %>"></td>
				<td data-priority="3"><input type="text" data-mini="true" value="<%= FixedNetPayable%>"></td>
						    
		    </tr>				<%
			for (int i = 0; i < PackageName.size(); i++){
				
				double PerCaseSamplingAmount = PerCaseSales.get(i).intValue() * PerCaseCompanyShare.get(i).doubleValue();
				double PerCaseDeductionAgainstAdvance = PerCaseSales.get(i).intValue() * PerCaseDeductionTerm.get(i).doubleValue();
				double PerCaseNetPayable = PerCaseSamplingAmount - PerCaseDeductionAgainstAdvance;
				
			%>
		    <tr>
				<td data-priority="1"><%= PackageName.get(i) %></td>
				<td data-priority="1"><%= BrandName.get(i) %></td>
				<td data-priority="1"><input type="text" data-mini="true" value="<%=PerCaseCompanyShare.get(i) %>"></td>
				<td data-priority="1"><input type="text" data-mini="true" value="<%=PerCaseDeductionTerm.get(i) %>"></td>
				<td data-priority="2"><input type="text" data-mini="true" value="<%=PerCaseSales.get(i) %>"></td>
				<td data-priority="3"><input type="text" data-mini="true" value="<%= Utilities.getDisplayCurrencyFormat(PerCaseSamplingAmount) %>"></td>		    
				
				<td data-priority="3"><input type="text" data-mini="true" value="<%= Utilities.getDisplayCurrencyFormat(PerCaseDeductionAgainstAdvance) %>"></td>		    
				<td data-priority="3"><input type="text" data-mini="true" value="<%= Utilities.getDisplayCurrencyFormat(PerCaseNetPayable) %>"></td>		    
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