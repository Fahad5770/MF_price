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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>




<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));

	 if (Utilities.isAuthorized(400, SessionUserID) == false) {
		response.sendRedirect("AccessDenied.jsp");
	} 

	String EditID = "0";
	boolean isEditCase = false;

	if (request.getParameter("BrandExchangeID") != null) {
		EditID = request.getParameter("BrandExchangeID");
		isEditCase = true;
	}

	Datasource ds = new Datasource();
	ds.createConnection();

	Statement s = ds.createStatement();
	Statement s1 = ds.createStatement();
	Statement s2 = ds.createStatement();
	Statement s3 = ds.createStatement();
	Statement s4 = ds.createStatement();
%>
<html>


<head>
<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->
<script src="js/SyncAttendance.js?2345=34567"></script>
<script src="js/lookups.js"></script>


</head>


<body>

	<div data-role="page" id="BrandExchange" data-url="BrandExchange"
		data-theme="d">

		<jsp:include page="Header2.jsp">
			<jsp:param value="Synchronize Attendance" name="title" />
		</jsp:include>

		<div data-role="content" data-theme="d">
			<form name="ProductPromotionsMainForm" id="ProductPromotionsMainForm">
				<input type="hidden" name="isEditCase" id="isEditCase" value="0" />
				<input type="hidden" name="ProductPromotionMasterTableID"
					id="ProductPromotionMasterTableID" value="0" /> <input
					type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>" />
				<input type="hidden" id="IsCheckedAllCheckboxes" value="0" /> <input
					type="hidden" id="IsCheckedAllCheckboxesSales" value="0" />

				<table border="0" style="width: 60%;">

					<tr style="font-size: 10pt; font-weight: 400;" >
						<td style="width: 100%;" colspan="2">
							<input type="text" name="SapCode" id="SapCode" placeholder="SAP Code" title="SAP Code" data-mini="true" maxlength="15" /></td>
						
						</tr>
						<tr>
						<td style="width: 10%;">Start Date</td>
						<td style="width: 90%;"><input type="date"
							placeholder="DD/MM/YYYY" id="SyncAttendanceValidFrom"
							name="SyncAttendanceValidFrom" data-mini="true" value="">
						</td>



					</tr>


				</table>

				<br /> <br />

			
		</div>
		<!-- /content -->

		<div data-role="footer" data-position="fixed" data-theme="b">

			<div>
				<table style="width: 100%;">
					<tr>
						<td><a data-icon="check" data-theme="a" data-role="button"
							data-inline="true" id="BrandPriceListSave" href="#"
							aclass="ui-disabled" onClick="SyncAttendanceSubmit();">Save</a>
							<button data-icon="check" data-theme="b" data-inline="true"
								id="BrandExchangeReset"
								onClick="javascript:window.location='SyncAttendance.jsp'">Reset</button>
						</td>
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