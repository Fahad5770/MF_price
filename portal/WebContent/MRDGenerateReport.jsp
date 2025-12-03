<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/MRDGenerateReport.js"></script>
<script src="js/lookups.js"></script>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 244;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<div data-role="page" id="MRDGenerateReportPage" data-url="MRDGenerateReportPage" data-theme="d">

	
	<jsp:include page="Header2.jsp" >
    	<jsp:param value="Generate Report" name="title"/>
    </jsp:include>
	 
    <div data-role="content" data-theme="d">
		<form id="MRDGenerateReportForm" name="MRDGenerateReportForm" action="#" method="POST" data-ajax="false">
			<table style="width:100%; border-collapse:collapse;" border="0" >
	        	<tr>
	        		<td style="width:10%">
	        			<label for="DistributorID" style="width:100%">Distributor ID</label>
	        		</td>
	        		<td style="width:30%">
	        			&nbsp;
	        		</td>
	        		<td style="width:20%">
	        			<label for="Date" style="width:100%">Date</label>
	        		</td>
	        		<td style="width:40%">
	        			&nbsp;
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>
	        			<input type="text" name="DistributorID" data-mini="true" id="DistributorID" style="width: 100%" onChange="getDistributorName()">
	        		</td>
	        		<td>
	        			<input type="text" name="DistributorName" data-mini="true" id="DistributorName" readonly="readonly" tabindex="-1">
	        		</td>
	        		<td>
	        			<input id="Date" name="Date" type="text" data-mini="true" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateNext(new java.util.Date())%>">
	        		</td>
	        		<td>
	        			<div style="width: 40%">
	        				<input id="Date" name="Date" type="button" data-mini="true" value="Generate Report" onclick="FormSubmit()">
	        			</div>
	        		</td>
	        	</tr>
	        </table>
	        <hr>
	        <table style="width:50%; border-collapse:collapse;" border="0" >
	        	<tr>
	        		<td style="width:10%">
	        			<label for="OutcomeID" style="width:100%">Outcome</label>
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>
	        			<select name="OutcomeID" id="OutcomeID" data-mini="true" class="ui-disabled">
	        				<option value="">Select</option>
	        			<%
	        			ResultSet rs = s.executeQuery("SELECT * FROM mrd_survey_summary_outcomes");
	        			while(rs.next()){
	        				%>
	        					<option value="<%=rs.getString("id")%>"><%=rs.getString("label")%></option>
	        				<%
	        			}
	        			%>
	        			</select>
	        		</td>	        		
	        	</tr>
	        	
	        	<tr>
	        		<td style="width:10%">
	        			<label for="Observations" style="width:100%">Observations</label>
	        		</td>
	        	</tr>
	        	<tr>
	        		<td style="width:10%">
	        			<textarea name="Observations" id="Observations" data-mini="true" style="height: 100px" class="ui-disabled"></textarea>
	        		</td>
	        	</tr>
	        	<tr>
	        		<td style="width:10%">
	        			<div style="width: 40%">
	        				<input type="hidden" name="SurveySummaryID" id="SurveySummaryID" value="" >
	        				<input type="button" id="UpdateButton" data-mini="true" value="Update" disabled="disabled" onclick="FormUpdate()">
	        			</div>
	        		</td>
	        	</tr>
	        </table>
		</form>
	
		<jsp:include page="LookupDistributorSearchPopup.jsp" >
	    	<jsp:param value="DistributorSearchCallBack" name="CallBack" />
	    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
	    </jsp:include><!-- Include Distributor Search -->

    </div><!-- /content -->    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
		<div>			
			<button data-icon="check" data-theme="a" data-inline="true" id="TestMailButton" >Test Mail</button>
			<button data-icon="check" data-theme="a" data-inline="true" id="LiveMailButton" >Live Mail</button>
			<button data-icon="check" data-theme="b" data-inline="true" onClick="window.location='MRDGenerateReport.jsp'">Reset</button>
		</div>    	
    </div>
	
</div>

</body>
</html>

<%
s.close();
ds.dropConnection();
%>