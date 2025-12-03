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

if(Utilities.isAuthorized(33, SessionUserID) == false){
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

<div data-role="page" id="SlipDispatch" data-url="SlipDispatch" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Slip Dispatch" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	<form id="SlipDispatchBarcodeForm" data-ajax="false" action="#SamplingReceiving" onSubmit="return SlipDispatchingAddBarcode();">
	
	<table style="width: 100%">
	<!--<tr>
			<td style="width: 30%">
			<input type="text" placeholder="Distributor ID" id="SlipDispatchID" onBlur="SamplingReceivingSetDistributor(this.value);">
			</td>
			<td>
			<input type="text" placeholder="Distributor Information" readonly="readonly" id="SamplingReceivingDistributorName">
			</td>
		</tr>-->
	
	
		<tr>
			<td colspan="2">
				<select id="DispatchMethod" name="DispatchMethod" onchange="populateDispatchForm(this.value)">
					<option value="0">Dispatch Method</option>
					<option value="2">By TCS</option>
					<option value="1">By Hand</option>
				</select>
			</td>
		</tr>
		<tr style="display:none" id="TCSBarcodeTD">
			<td style="width: 50%;">
				<input type="text" placeholder="Scan barcode or enter manually" id="SlipDispatchBarcodeFieldTCS" name="SlipDispatchBarcodeFieldTCS"><span style="visibility: hidden;"><button></button></span>
			</td>
			<td>
				<input  type="text" placeholder="Scan TCS-barcode or enter manually" id="TCSBarcodeField" name="TCSBarcodeField"><span style="visibility: hidden;"><button></button></span>
			</td>
		</tr>
		<tr style="display:none" id="UserIDTD">
			<td style="width: 50%;">
				<input type="text" placeholder="Scan barcode or enter manually" id="SlipDispatchBarcodeFieldByHand" name="SlipDispatchBarcodeFieldByHand"><span style="visibility: hidden;"><button></button></span>
			</td>
			<td style="width: 50%;">
				<input  type="text" placeholder="Employee SAP Code" id="SlipDispatchUserID" name="SlipDispatchUserID" ><span style="visibility: hidden;"><button></button></span>
			</td>
		</tr>
	
	</table>
	</form>
	<form action="test2.jsp" name="SlipDispatchMainForm" id="SlipDispatchMainForm" style="visibility: hidden;">
		<input type="hidden" name="DispatchMethodMainForm" id="DispatchMethodMainForm" value="" />
		<table data-role="table" data-mode="reflow" class="ui-responsive table-stroke">
		  <thead>
		    <tr>
				<th data-priority="1">Distributor</th>
				<th data-priority="1">Outlet</th>				
				<th data-priority="1">Month</th>
				<th data-priority="1" style="text-align: right;">Sampling Amount</th>
				<th data-priority="1" style="text-align: right;" id="DispatchMethodLabel">TCS Barcode</th>
				<th data-priority="1"></th>
		    </tr>
		  </thead>
			<tbody id="SamplingReceivingTableBody">

			</tbody>
		</table>
			
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
		<input type="hidden" id="DistributorID" name="DistributorID">
		
	</form>
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<button data-icon="check" data-theme="a" data-inline="true" id="SlipDispatchSave" onClick="SlipDispatchSubmit();">Save</button>
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