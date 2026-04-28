<%@page import="java.lang.reflect.Array"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>
<%@page import="com.pbc.common.Distributor"%>

<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));

	if (Utilities.isAuthorized(395, SessionUserID) == false) {
		response.sendRedirect("AccessDenied.jsp");
	}

	Datasource ds = new Datasource();
	ds.createConnection();
	Connection c = ds.getConnection();
	Statement s = c.createStatement();
%>
<html>


<head>



<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/GLCreditCreditLimitDeactive.js?21111111=21111111"></script>




</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>

	<div data-role="page" id="DispatchReturnsMainInner"
		data-url="DispatchReturnsMainInner" data-theme="d">

		<div data-role="header" data-theme="c" data-position="fixed">
			<div>
				<div style="float: left; width: 10%">
					<a href="home.jsp" data-role="button" data-theme="d"
						data-inline="true" data-ajax="false"><img
						src="images/logofull.svg" style="width: 50px"></a>
				</div>
				<div
					style=" width: 90%; b1ackground-color: Red; text-align: center;">
					<h1
						style="font-size: 14pt; text-align: center;">Deactivate
						Credit Limit</h1>


				</div>
			</div>
		</div>


		<div data-role="content" data-theme="d">

			

			<div class="ui-grid-a">
				<div class="ui-block-a"></div>
				<div class="ui-block-b" style="width: 100%">
					<div class="ui-ba ui-ba1r-e" style="min-height: 60px">
						<ul data-role='listview' data-inset='true'
							style='font-size: 10pt; margin-top: -2px; margin-left: -8px'
							data-icon='false'>
							<li data-role='list-divider' data-theme='a'>Deactivate
						Credit Limit</li>
							<li>
								<form action="" name="ReturnMainForm" id="ReturnMainForm">
								<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerID" data-mini="true">Customer ID</label>
									<input type="text"  name="CustomerID" id="CustomerID"  data-mini="true" data-mini="true"  onblur="GetClientInfo()"  >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerName" data-mini="true">Customer Name</label>
									<input  type="text" name="CustomerName" id="CustomerName"  data-mini="true" data-mini="true" readonly >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CreditLimit" data-mini="true">Credit Limit</label>
									<input type="text" name="CreditLimit" id="CreditLimit"  data-mini="true" readonly data-mini="true" >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									
								</td>
								
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ValidFrom" data-mini="true">Valid From</label>
									<input  type="text" name="ValidFrom" id="ValidFrom"  data-mini="true" readonly data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ValidTo" data-mini="true">Valid To</label>
									<input  type="text" name="ValidTo" id="ValidTo"  data-mini="true" readonly data-mini="true" >
								</td>
							</tr>
							
							
							
							
						</table>
									<!-- <table border="0" style="width: 100%">


										<tr>
											<td>
											<label for="CustomerID" style="margin: 0px">CustomerID</label>
											</td>
										</tr>

										<tr>
											<td><input type="text" name="CustomerID" data-mini="true"
												id="CustomerID" style="width: 100%" /></td>
										</tr>
									</table> -->
									<table style="width: 100%" border=0>
										<tr>

											<td>Deactivation Remarks</td>
										</tr>
										<tr>
											<td><input type="text" name="Deactivation_Remarks"
												data-mini="true" id="Deactivation_Remarks"
												style="width: 100%" placeholder="Deactivation Remarks"
												 /></td>
										</tr>
									</table>

								</form>

							</li>
						</ul>
					</div>
				</div>
			</div>
			<!-- /grid-a -->






		</div>
		<!-- /content -->

		<div data-role="footer" data-position="fixed" data-theme="b">

			<div>
				<table style="width: 100%;" border="0">
					<tr>
						<td id="SaveCustomerID" style="width: 30%">

							<table style="width: 100%;">
								<tr>
									<td style="width: 20%;"><a data-icon="check"
										data-theme="a" data-role="button" data-inline="true"
										id="DispatchReturnsSave" class="ui-disabled" href="#"
										onClick="ProductionReceiptSubmit();">Save</a></td>

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
	c.close();
	ds.dropConnection();
%>