<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@page import="java.net.URL"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URLEncoder"%>

<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>

<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

long CaseNo = Utilities.parseLong(request.getParameter("CaseNo"));
if(CaseNo > 0){
	int StatusID = Utilities.parseInt(request.getParameter("StatusID"));
	s.executeUpdate("update fda_complaints set status_id="+StatusID+", status_on=now() where case_no="+CaseNo);
	response.sendRedirect("FDAChangeComplaintStatus.jsp");
}

%>

<html>


<head>


			<meta charset="utf-8">
			<meta name="viewport" content="width=device-width, initial-scale=1">
			<link rel="stylesheet"  href="../lib/jqm130/jquery.mobile-1.3.0.min.css">
			 <link href="../lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
			
			<script src="../lib/jquery-1.9.1.min.js"></script>
			<script src="../lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
			<script src="../lib/jquery-validation/jquery.validate.js"></script>
			<script src="../lib/jqueryui1102/js/jquery-ui-1.10.2.custom.min.js"></script>
			<script src="../lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js"></script>
			
			<title>PBC Enterprise Portal</title>
			<script src="../js/home.js"></script>
			<link rel="stylesheet"  href="../css/home.css">
		<script src="FDAChangeComplaintStatus.js"></script>
		
</head>

<body>

<div data-role="page" id="FDAChangeComplaintStatusPage" data-url="FDAChangeComplaintStatusPage" data-theme="d">

	<jsp:include page="Header.jsp" >
    	<jsp:param value="Change Complaint Status" name="title"/>
    </jsp:include>
    <div data-role="content" data-theme="d">
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:100%">
	    	<div class="ui-bar " style="min-height:60px">
		    	<form name="FDAChangeComplaintStatusExecute" id="FDAChangeComplaintStatusExecute" action="#" method="post" data-ajax="false" >
		    		<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="CaseNo" data-mini="true">Case No</label>
								<input type="text" name="CaseNo" id="CaseNo" value="" data-mini="true" onchange="showCaseInfo()" >
							</td>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="StatusID" data-mini="true">Status</label>
								<select name="StatusID" id="StatusID" data-mini="true">
									<option value="">Select</option>
									<option value="1">In Process</option>
									<option value="2">Completed</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="border: 0px;"><hr></td>
						</tr>
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="FirstName" data-mini="true">First Name</label>
								<input type="text" name="FirstName" id="FirstName" value="" data-mini="true" readonly="readonly" >
							</td>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="LastName" data-mini="true">Last Name</label>
								<input type="text" name="LastName" id="LastName" value="" data-mini="true" readonly="readonly" >
							</td>
						</tr>
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="Type" data-mini="true">Type</label>
								<input type="text" name="Type" id="Type" value="" data-mini="true" readonly="readonly" >
							</td>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="DateofEntry" data-mini="true">Date of Entry</label>
								<input type="text" name="DateofEntry" id="DateofEntry" value="" data-mini="true" readonly="readonly" >
							</td>
						</tr>
					</table>
				</form>
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<table style="width: 100%">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="Submit()" >Change</a>
                    <button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='FDAChangeComplaintStatus.jsp'" >Reset</button>
				</td>
			</tr>
		</table>
    </div>

</div>

</body>
</html>

<%
s.close();
c.close();
%>
