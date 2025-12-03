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

if (Utilities.isAuthorized(501, SessionUserID) == false) {
	response.sendRedirect("AccessDenied.jsp");
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
<script src="js/lookups.js"></script>

<script>
	function OutletInformation() {
		let outletID = $("#OutletID").val();
		if (outletID.trim() === "")
			return;

		$.ajax({
			url : "inventory/OutletInfoJson",
			type : "POST",
			data : {
				OutletID : outletID
			},
			dataType : "json",
			success : function(json) {
				if (json.success == "true") {

					//	alert(json.name);
					//	alert(json.address);
					//alert(json.region);
					//
					$("#OutletName").val(json.name);
					$("#address").val(json.address);
					$("#region").val(json.region);

				} else {
					alert(json.error || "Outlet not found.");
				}
			},
			error : function(xhr, status) {
				alert("Server could not be reached.");
			}
		});
	}

	function saveOutletInfo() {
		$.ajax({
			type : "POST",
			url : "inventory/SaveOutletInfo",
			data : $("#WorkflowContainerForm").serialize(),
			success : function(res) {
				if (res.success == "true") {
					alert("Saved successfully!");
					location = "RentalDiscountRequest.jsp";
				} else {
					alert("Save failed : " + res.error);
				}
			},
			error : function() {
				alert("Something went wrong with the request.");
			}
		});
	}
	
	
</script>

<style>
td {
	padding: 8px 12px;
}

select, input[type="text"] {
	width: 150px; /* Adjust as needed */
	padding: 4px;
}
</style>


</head>


<body>

	<div data-role="page" id="RentalDiscount" data-url="RentralDiscount"
		data-theme="d">

		<jsp:include page="Header2.jsp">
			<jsp:param value="Rentral Discount List" name="title" />
		</jsp:include>

		<div data-role="content" data-theme="d">



			<form name="WorkflowContainerForm" class="WorkflowForm"
				id="WorkflowContainerForm" method="POST" action="#">
				<ul data-role="listview" data-inset="true"
					style="margin-left: 22px; margin-top: 6px; margin-right: 5px;">
					<li data-role="list-divider">Outlet Information</li>
					<li>
						<table border="0"
							style="width: 100%; margin-top: 0px; padding: 0px;"
							class="workflow_form_font">
							<tr>
								<td style="width: 65%">
									<fieldset class="ui-grid-b">
										<div class="ui-block-a" style="padding-left: 5px;">
											<label for="OutletID">Outlet ID</label><input type="text"
												id="OutletID" name="OutletID" data-mini="true"
												onchange="OutletInformation()">
										</div>
										<div class="ui-block-b" style="padding-left: 5px;">
											<label for="OutletName">Outlet Name</label><input type="text"
												id="OutletName" name="OutletName" data-mini="true"
												readonly="readonly">
										</div>
									</fieldset>
									<fieldset class="ui-grid-solo">
										<div class="ui-block-a" style="padding-left: 5px;">
											<label for="address">Address</label><input id="address"
												name="address" type="text" value="" data-mini="true"
												readonly="readonly">
										</div>
									</fieldset>
									<fieldset class="ui-grid-b">
										<div class="ui-block-a" style="padding-left: 5px;">
											<label for="region">Region</label><input type="text"
												id="region" name="region" data-mini="true"
												readonly="readonly">
										</div>
										<div class="ui-block-b" style="padding-left: 5px;">
											<label for="asm">ASM</label><input type="text" id="asm"
												name="asm" data-mini="true" data-theme="b"
												readonly="readonly">
										</div>
										<div class="ui-block-c" style="padding-left: 5px;">
											<label for="cr">CR</label><input type="text" id="cr"
												name="cr" data-mini="true" data-theme="b"
												readonly="readonly">
										</div>
									</fieldset>
									<fieldset class="ui-grid-b">
										<div class="ui-block-a" style="padding-left: 5px;">
											<label for="market">Market</label><input type="text"
												id="market" name="market" data-mini="true" value=""
												readonly="readonly">
										</div>
										<div class="ui-block-b" style="padding-left: 5px;">
											<label for="vehicle">Vehicle</label><input type="text"
												id="vehicle" name="vehicle" data-mini="true" value=""
												readonly="readonly">
										</div>
										<div class="ui-block-c" style="padding-left: 5px;">
											<label for="SamplingStatus"></label>&nbsp;<br> <a
												id="WorkflowOutletDashboardLink" data-role="button"
												data-mini="true" data-inline="true"
												href="OutletDashboard.jsp?outletID=47" target="_blank"
												data-icon="grid">Outlet Dashboard</a>
										</div>
									</fieldset>

								</td>
								<td style="width: 35%; text-align: right;"><img
									id="OutletMap"
									style="width: 285px; height: 220px; border: solid 1px;">
								</td>
							</tr>
						</table>
					</li>

					<li data-role="list-divider">Fixed Discount</li>
					<li>
						<table>

							<thead>
								<tr>
									<td><label>Peak</label></td>

									<td><input type="text" id="FixedCompanyShare"
										name="FixedCompanyShare" data-mini="true" placeholder="Rs.">
									</td>

									<td><select id="FixedValidFrom" name="FixedValidFrom"
										data-mini="true">
											<option value="">Select Month</option>
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
									</select></td>

									<td><select id="FixedValidTo" name="FixedValidTo"
										data-mini="true">
											<option value="">Select Month</option>
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
									</select></td>
								</tr>
							</tbody>

						</table>

					</li>


					</li>

				</ul>

			</form>

			</ul>

		</div>
		<!-- /content -->

		<div data-role="footer" data-position="fixed" data-theme="b">

			<div>
				<table style="width: 100%;">
					<tr>
						<td><a data-icon="check" data-theme="a" data-role="button"
							data-inline="true" id="BrandPriceListSave" href="#"
							onClick="saveOutletInfo();">Save</a>
							<button data-icon="check" data-theme="b" data-inline="true"
								id="BrandExchangeReset"
								onClick="javascript:window.location='RentalDiscountRequest.jsp'">Reset</button>
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