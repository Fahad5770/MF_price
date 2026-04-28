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


Statement s = c.createStatement();



%>
<div data-role="page" id="SamplingDashboardMain" data-url="SamplingDashboardMain" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Monthly Discounts" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">

	<form id="SamplingDashboardGenerrateForm" name="SamplingDashboardGenerrateForm" action="SamplingDashboard.jsp" method="POST">
		<ul data-role="listview" data-inset="true">
	        <li data-role="fieldcontain">
	            <label for="region">Region:</label>
	            <select name="region" id="region">
	            <option value="0"></option>
	            <%
	            ResultSet rs = s.executeQuery("select distinct region from outletmaster order by region");
	            while(rs.next()){
	            	%>
	            	<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
	            	<%
	            }
	            %>
	            
	            </select>
	        </li>
	        <li data-role="fieldcontain">
	            <label for="distributor">Distributor ID:</label>
	            <input type="text" name="distributor" id="distributor">
	        </li>
	        <li data-role="fieldcontain">
	            <label for="rsm">RSM:</label>
	            <select name="rsm" id="rsm">
	            <option value="0"></option>
	            <%
	            rs = s.executeQuery("select distinct rsm_id from outletmaster order by rsm_id");
	            while(rs.next()){
	            	%>
	            	<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
	            	<%
	            }
	            %>	            
	            
	            </select>
	        </li>
	        <li data-role="fieldcontain">
	            <label for="asm">ASM:</label>
	            <input type="text" name="asm" id="asm">
	        </li>
	        <li data-role="fieldcontain">
	            <label for="cr">CR:</label>
	            <input type="text" name="cr" id="cr">
	        </li>
	        <li data-role="fieldcontain">
	            <label for="outlet_id">Outlet ID:</label>
	            <input type="text" name="outlet_id" id="outlet_id">
	        </li>
	        <li data-role="list-divider" data-theme="c">For the period</li>
	        <li data-role="fieldcontain">
	            <label for="month">Month:</label>
	           <select name="month" id="month">
	           <option value="1">January</option>
	           <option value="2">February</option>
	           <option value="3">March</option>
	           <option value="4">April</option>
	           <option value="5">May</option>
	           <option value="6">June</option>
	           <option value="7">July</option>
	           <option value="8">August</option>
	           <option value="9">September</option>
	           <option value="10">October</option>
	           <option value="11">November</option>
	           <option value="12">December</option>
	           </select>
	        </li>
	        <li data-role="fieldcontain">
	            <label for="year">Year:</label>
	            <select name="year" id="year">
	            <%
	            int year = Utilities.getYearByDate(new java.util.Date());
	            
	            for (int i = 2012; i < (year+2); i++){
	            %>
	            <option value="<%=i%>" <%if (i == year){out.print("selected");} %>><%=i %></option>
	            <%
	            }
	            %>

	            </select>
	        </li>
	        <li data-role="list-divider" data-theme="c">Sampling Type</li>
	        <li data-role="fieldcontain">
	            <label for="type">Show:</label>
	            <select name="type" id="type">
	            <option value="-1">All Outlets</option>
	            <option value="0">Pending</option>
	            <option value="1">Approved</option>
	            <option value="2">On Hold</option>
	            <option value="3">Cancelled</option>
	            </select>
	        </li>

        </ul>
	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="SamplingDashboardGenerateButton">Generate</button>
		<button data-icon="check" data-theme="b" data-inline="true" id="SamplingDashboardGenerateButtonXLS">Preview in Excel</button>
		<button data-icon="check" data-theme="b" data-inline="true" id="SamplingDashboardPerCaseButtonXLS">Projected Sampling</button>
	</div>    	
    </div>

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>