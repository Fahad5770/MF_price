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

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(29, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

long ReceivingID = Utilities.parseLong(request.getParameter("ReceivingID"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

Statement s = c.createStatement();

%>
<html>

<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
</head>

<body>

<div data-role="page" id="SamplingReceiving" data-url="SamplingReceiving" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Slip Receiving" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	<table style="width: 100%">
		<tr>
			<td style="width: 30%">
			<input type="text" placeholder="Distributor ID" id="SamplingReceivingDistributorID" onBlur="SamplingReceivingSetDistributor(this.value);">
			</td>
			<td>
			<input type="text" placeholder="Distributor Information" readonly="readonly" id="SamplingReceivingDistributorName">
			</td>
		</tr>
	</table>
	<form id="SamplingReceivingBarcodeForm" data-ajax="false" action="#SamplingReceiving" onSubmit="return SamplingReceivingAddBarcode();">
		<input type="text" placeholder="Scan barcode or enter manually" id="SamplingReceivingBarcodeField"><span style="visibility: hidden;"><button></button></span>
	</form>

	<form action="test2.jsp" name="SamplingReceivingMainForm" id="SamplingReceivingMainForm" style="visibility: hidden;">
	
		<table data-role="table" data-mode="reflow" class="ui-responsive table-stroke">
		  <thead>
		    <tr>
				<th data-priority="1">Distributor</th>
				<th data-priority="1">Outlet</th>				
				<th data-priority="1">Month</th>
				<th data-priority="1" style="text-align: right;">Sampling Amount</th>
				<th data-priority="1"></th>
		    </tr>
		  </thead>
			<tbody id="SamplingReceivingTableBody">

			</tbody>
		</table>
			
		<a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a>
		
		<input type="hidden" id="DistributorID" name="DistributorID">
		
	</form>
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<button data-icon="check" data-theme="a" data-inline="true" id="SamplingReceivingGenerateButton" onClick="SamplingReceivingSubmit();">Generate Receipt</button>
					<a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="SamplingReceivingPrintButton" href="SamplingReceivingPrint.jsp?ReceivingID=<%= ReceivingID%>" target="_blank" <%if (ReceivingID == 0){%>class="ui-disabled"<%}%>>Print Receipt</a>
				</td>
				<td align="right">
					<table>
						<tr>
							<td>
								<input type="text" data-inline="true" placeholder="Receiving ID" id="SamplingReceivingViewID">
							</td>
							<td>
								<a data-role="button" data-icon="grid" data-theme="b" data-inline="true" data-corners="false" id="SamplingReceivingListButton" onClick="window.open('SamplingReceivingPrint.jsp?ReceivingID='+$('#SamplingReceivingViewID').val(), '_blank');">View</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	    	
    </div>

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>