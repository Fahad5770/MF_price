<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 154;
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();



%>

<script>

function generatePrinting(){
	$('#CreditSlipPrintingGenerateForm').submit();
}

</script>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<div data-role="page" id="SamplingPrintingMain" data-url="SamplingPrintingMain" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Credit Slip Printing" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">

	<form id="CreditSlipPrintingGenerateForm" name="CreditSlipPrintingGenerateForm" action="SamplingCreditSlipPrinting.jsp" method="POST" data-ajax="false" target="_blank">
		<ul data-role="listview" data-inset="true">
	        
	        <li data-role="list-divider" data-theme="c">For the period</li>
	        <li>
				<div class="ui-grid-d" style="padding: .7em 15px;">
					<div class="ui-block-a" style="width:20%; margin: 0 2% 0 0;"><label for="CreditSlipPrintingMainFromDate">From Date:</label></div>
					<div class="ui-block-b" style="width:20%;"><input id="CreditSlipPrintingMainFromDate" name="CreditSlipPrintingMainFromDate" type="text" data-mini="true" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></div>
				</div>
			</li>
			<li>
				<div class="ui-grid-d" style="padding: .7em 15px;">
					<div class="ui-block-a" style="width:20%; margin: 0 2% 0 0;"><label for="CreditSlipPrintingMainToDate">To Date:</label></div>
					<div class="ui-block-b" style="width:20%;"><input id="CreditSlipPrintingMainToDate" name="CreditSlipPrintingMainToDate" type="text" data-mini="true" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateNext(new java.util.Date())%>"></div>
				</div>
	        </li>
	        <li data-role="list-divider" data-theme="c">Type</li>
	        <li data-role="fieldcontain">
	            <label for="type">Show:</label>
	            <select name="type" id="type">
	            	<option value="1">Pending for Printing</option>
	            	<option value="2">Already Printed</option>
	            	<option value="3">All</option>
	            </select>
	        </li>
	        <li data-role="fieldcontain">
	            <label for="action">Action:</label>
	            <select name="action" id="action">
	            	<option value="1">Preview Only</option>
	            	<option value="2">Print</option>
	            </select>
	        </li>

        </ul>
	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="CreditSlipPrintingGenerateButton" onclick="generatePrinting()">Generate</button>
	</div>    	
    </div>

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>