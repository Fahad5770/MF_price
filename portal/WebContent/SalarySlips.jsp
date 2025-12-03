<%@page import="com.pbc.sap.SAPUtilities"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.sap.SalarySlip"%>



<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

	<%@include file="include/ValidateSession.jsp" %>
	
	<%
	int SessionUserID = Integer.parseInt((String)session.getAttribute("UserID"));
	
	String UserDisplayName = (String)session.getAttribute("UserDisplayName");
	String UserPictureURL = (String)session.getAttribute("UserPictureURL");
	SAPUtilities SAP = new SAPUtilities();
	SalarySlip list[] = SAP.getPayrollList(SessionUserID);
	%>
</head>
<body>
<div data-role="page" id="SalarySlips" data-url="SalarySlips" data-theme="d">
	
	<jsp:include page="HomeNavigationPanel.jsp">
		<jsp:param name="title" value="<%= UserDisplayName%>" />
	</jsp:include> <!-- /Left Navigation Panel -->
	
    <jsp:include page="Header1.jsp" /> <!-- /header -->
    
    <div data-role="content" data-theme="d">
        <div style="max-width: 600px; margin-left: auto; margin-right: auto;margin-top: 40px;">
        <ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search...">
        	<li data-role="list-divider">Salary Slips</li>
        	<%
        	for (int i = list.length-1; i >= 0; i--){
        	%>
        	<li><a href="sap/SalarySlipExecute?sequence=<%=list[i].SEQUENCE_NO %>" data-transition="flow" data-ajax="false" target="_blank"><%=Utilities.getDisplayDateFormat(list[i].FROM_DATE) + " - " + Utilities.getDisplayDateFormat(list[i].TO_DATE)%></a><span class="ui-li-count">Generated On: <%=Utilities.getDisplayDateFormat(list[i].GENERATED_ON) %></span></li> 
        	<%
        	}
        	%>
        </ul>
        </div>
    </div><!-- /content -->
    
    <jsp:include page="Footer1.jsp" /> <!-- /footer -->
	
</div>

</body>
</html>