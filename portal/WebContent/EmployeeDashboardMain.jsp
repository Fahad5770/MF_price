<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(41, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();



%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="lib/highcharts301/js/highcharts-more.js"></script>
		<script src="js/EmployeeDashboard.js"></script>
		<script src="js/lookups.js"></script>
	<style>
	a{
		font-size: 10pt;
	}
	</style>
	
	
			
	</head>
<body>

<div data-role="page" id="EmployeeDashboard" data-url="EmployeeDashboard" data-theme="d">
	
	<div data-role="header" style="overflow:hidden;" data-theme="d">
	    <h1>Employee Dashboard</h1>
	    <a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
	    
	</div><!-- /header -->

    <div data-role="content" data-theme="d">
        <form name="EmployeeDashboardForm" id="EmployeeDashboardForm" data-ajax="false" action="#EmployeeDashboardForm">
	        <table border="0" style="width: 100%">
	        	<tr>
	        		<td valign="top">
	        			<div align="center">
							<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="b" style="width:50%">
								<li data-role="list-divider">Select Employee</li>
								<li>
									<div id="container" style="width: 100%; height: 150px; margin: 0 auto;">
										<table style="width:100%">
											<tr>
												<td style="width:20%">
													<input type="text" name="EmployeeDashboardSapCode" id="EmployeeDashboardSapCode" placeholder="SAP Code" value="" onchange="getEmployeeName()" >
												</td>
												<td style="width:80%">
													<input type="text" name="EmployeeDashboardName" id="EmployeeDashboardName" readonly="readonly" >
												</td>
											</tr>
											<tr>
												<td style="text-align:center" colspan="2">
													<a id="EmployeeDashboardSubmitButton" data-role="button" onclick="return EmployeeDashboardSubmit()" class="ui-disabled" >Submit</a>
												</td>
											</tr>
										</table>
									</div>
								</li>
							</ul>
						</div>        		
	        		</td>
	        	</tr>
	        </table>
        </form>
    </div><!-- /content -->
    
    
    
    
    
	<jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="setEmployeeLookup" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->

    <jsp:include page="Footer1.jsp" /> <!-- /footer -->

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>