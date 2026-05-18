<%@page import="java.util.Date"%>
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
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>


<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));

	if (Utilities.isAuthorized(511, SessionUserID) == false) {
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
%>
<html>


<head>
<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->
<script src="js/PriceDiscount.js?<%=Math.random()%>=<%=Math.random()%>"></script>
<script src="js/lookups.js"></script>

<!-- ✅ Small UI polish without changing theme -->
<style>
/* Keep Product Discount table compact and clean */
#ProductDiscountTable {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

#ProductDiscountTable th, #ProductDiscountTable td {
	border: 1px solid #cfcfcf;
	padding: 8px;
	vertical-align: middle;
}

#ProductDiscountTable th {
	background: #f0f0f0;
	font-weight: bold;
	text-align: left;
}

/* Discount input width + alignment */
.discountInput {
	width: 120px;
}

/* Keep radio buttons aligned nicely */
.typeCell {
	white-space: nowrap;
}

.typeGroup {
	margin: 0;
	display: inline-block;
}

.typeGroup .ui-controlgroup-controls {
	display: inline-flex;
}

.typeGroup .ui-btn {
	padding-top: 6px;
	padding-bottom: 6px;
}

/* ✅ Outlet dropdown wrapper look like scope cards */
.scopeBox {
	border: 1px solid #d6d6d6;
	border-radius: 8px;
	padding: 10px;
	margin-top: 10px;
	background: #fff;
}

.scopeBoxTitle {
	font-weight: 600;
	margin-bottom: 6px;
}
</style>

</head>


<body>

	<div data-role="page" id="PriceDiscount" data-url="PriceDiscount"
		data-theme="d">

		<jsp:include page="Header2.jsp">
			<jsp:param value="Price Discount" name="title" />
		</jsp:include>

		<div data-role="content" data-theme="d">

			<form name="PriceDiscountsMainForm" id="PriceDiscountsMainForm">
				<input type="hidden" name="DoSave" id="DoSave" value="0" /> <input
					type="hidden" name="isEditCase" id="isEditCase" value="0" /> <input
					type="hidden" name="PriceDiscountMasterTableID"
					id="PriceDiscountMasterTableID" value="0" /> <input
					type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>" />
				<input type="hidden" id="IsCheckedAllCheckboxes" value="0" /> <input
					type="hidden" id="IsCheckedAllCheckboxesSales" value="0" />

				<table border="0" style="width: 100%;">

					<tr style="font-size: 10pt; font-weight: 400;">
						<td style="width: 25%;">Discount Name</td>
						<td style="width: 25%;">Valid From</td>
						<td style="width: 25%;">Valid To</td>
						<td style="width: 25%;">Active</td>
					</tr>
					<tr>
						<td style="width: 25%;"><input type="text" placeholder=""
							id="PriceDiscountLabel" name="PriceDiscountLabel"
							data-mini="true" value=""> <input type="hidden"
							name="PriceDiscountMasterTableID" id="PriceDiscountMasterTableID"
							value="" /></td>
						<td style="width: 25%;"><input type="text"
							placeholder="DD/MM/YYYY" id="PriceDiscountValidFrom"
							name="PriceDiscountValidFrom" data-mini="true" value="">
						</td>
						<td style="width: 25%;"><input type="text"
							placeholder="DD/MM/YYYY" id="PriceDiscountValidTo"
							name="PriceDiscountValidTo" data-mini="true" value="">
						</td>
						<td style="width: 25%;"><select
							id="PriceDiscountIsActive" name="PriceDiscountIsActive"
							data-mini="true" class="ui-disabled">
								<option value="1">Active</option>
								<option value="0">Inactive</option>
						</select></td>
					</tr>
				</table>

				<br />
				<br />

				<ul data-role="listview" data-inset="false" data-divider-theme="c">

					<li></li>

					<!-- ✅ Scope first -->
					<li data-role="list-divider">Scope</li>

					<li>
						<div id="PriceDiscountDataScope" style="width: 100%"></div>

					</li>

					<!-- ✅ Product Discount -->
					<li data-role="list-divider">Product Discount</li>

					<li>
						<div id="ProductDiscountScopeForm"
							style="width: 100%; text-align: center;">

							<%
								ResultSet rsProducts = s
										.executeQuery("SELECT  sap_code, product_label, product_id FROM inventory_products_view "
												+ "WHERE sap_code IS NOT NULL AND sap_code <> '' " + "ORDER BY sap_code ");
							%>

							<table id="ProductDiscountTable"
								style="width: 80%; margin: 15px auto; border-collapse: collapse;">

								<tr style="background: #f3f3f3; font-weight: 600;">
									<th
										style="border: 1px solid #cfcfcf; padding: 8px; width: 160px;">Product
										Code</th>
									<th style="border: 1px solid #cfcfcf; padding: 8px;">Product</th>
									<th
										style="border: 1px solid #cfcfcf; padding: 8px; width: 140px;">Discount</th>
									<th
										style="border: 1px solid #cfcfcf; padding: 8px; width: 220px;">Is Percentage</th>
									<th
										style="border: 1px solid #cfcfcf; padding: 8px; width: 220px;">With Tax</th>
								</tr>

								<%
									int rowIndex = 0;
									while (rsProducts.next()) {
										String code = rsProducts.getString("sap_code");
										String label = rsProducts.getString("product_label");
								%>

								<tr>

									<!-- Product Code -->
									<td style="border: 1px solid #cfcfcf; padding: 8px;"><input
										type="hidden" name="ProductCode_<%=rowIndex%>"
										value="<%=rsProducts.getInt("product_id")%>" /> <%=code%></td>

									<!-- Product -->
									<td style="border: 1px solid #cfcfcf; padding: 8px;"><input
										type="hidden" name="ProductLabel_<%=rowIndex%>"
										value="<%=label%>" /> <%=label%></td>

									<!-- Discount -->
									<td style="border: 1px solid #cfcfcf; padding: 8px;"><input
										type="number" name="Discount_<%=rowIndex%>" step="0.01"
										value="0" data-mini="true" style="width: 110px;" /></td>

									<!-- Type Buttons -->
									<%-- <td style="border: 1px solid #cfcfcf; padding: 8px;">

										<fieldset data-role="controlgroup" data-type="horizontal"
											data-mini="true" style="margin: 0; display: inline-block;">

											<input type="radio" name="DiscountType_<%=rowIndex%>"
												id="DiscountTypeAmount_<%=rowIndex%>" value="1"
												checked="checked" /> <label
												for="DiscountTypeAmount_<%=rowIndex%>"> Amount </label> <input
												type="radio" name="DiscountType_<%=rowIndex%>"
												id="DiscountTypePercentage_<%=rowIndex%>" value="2" />

											<label for="DiscountTypePercentage_<%=rowIndex%>">
												Percentage </label>

										</fieldset>

									</td> --%>
									<td style="border: 1px solid #cfcfcf; padding: 8px;">

										<input
										type="checkbox" name="PercentageCheckBox_<%=rowIndex%>"
										value="2" style="width: 50px; height: 25px;" />

									</td>
									<td style="border: 1px solid #cfcfcf; padding: 8px;">

										<input
										type="checkbox" name="CheckBox_<%=rowIndex%>"
										value="1" style="width: 50px; height: 25px;" />

									</td>

								</tr>

								<%
									rowIndex++;
									}
									rsProducts.close();
								%>

							</table>

							<input type="hidden" name="TotalPriceDiscountRows"
								value="<%=rowIndex%>" />

						</div>
					</li>
			</form>
			</ul>

		</div>
		<!-- /content -->

		<jsp:include page="LookupDistributorSearchPopup.jsp">
			<jsp:param value="DistributorSearchCallBackForUserRights"
				name="CallBack" />
			<jsp:param value="53" name="DistributorSearchFeatureID" />
		</jsp:include>

		<jsp:include page="LookupOutletSearchPopup.jsp">
			<jsp:param value="OutletSearchCallBackDeskSale" name="CallBack" />
			<jsp:param value="53" name="OutletSearchFeatureID" />
		</jsp:include>

		<jsp:include page="LookupEmployeeSearchPopup.jsp">
			<jsp:param value="EmployeeSearchCallBackLiftingReport"
				name="CallBack" />
		</jsp:include>


		<div data-role="footer" data-position="fixed" data-theme="b">
			<div>
				<table style="width: 100%;">
					<tr>
						<td><a data-icon="check" data-theme="a" data-role="button"
							data-inline="true" id="ProductPromotionsSave" href="#"
							onClick="PriceDiscountSubmit();">Save</a>
							<button data-icon="check" data-theme="b" data-inline="true"
								id="BrandExchangeReset"
								onClick="javascript:window.location='ProductDiscount.jsp'">Reset</button>
							<a href="#popupDialogDeactivate" data-rel="popup"
							data-icon="check" data-theme="b" data-role="button"
							data-inline="true" data-position-to="window"
							data-transition="pop" id="ProductPromotionDeactivate"
							class="ui-disabled">Deactivate</a></td>
						<td align="right"><a href="#popupDialog" data-rel="popup"
							data-icon="check" data-theme="b" data-role="button"
							data-inline="true" data-position-to="window"
							data-transition="pop" id="BrandExchangeSearch">Search</a></td>
					</tr>
				</table>
			</div>
		</div>


		<div data-role="popup" id="popupDialog" data-overlay-theme="a"
			data-theme="c" data-dismissible="true"
			style="min-width: 700px; overflow-y: auto; min-height: 600px; max-height: 600px"
			aclass="ui-corner-all">
			<div data-role="header" data-theme="a" class="ui-corner-top">
				<h1>Search</h1>
			</div>
			<div data-role="content" data-theme="d"
				class="ui-corner-bottom ui-content">
				<ul data-role="listview" data-inset="true">
					<li data-role="list-divider">Active Product Promotions</li>
					<%
						ResultSet rs1 = s.executeQuery("select * from inventory_price_discount where is_active=1");
						while (rs1.next()) {
							Date CreatedOn = rs1.getDate("created_on");
					%>
					<li><a data-ajax="false" href="#"
						onClick="LoadPriceDiscount(<%=rs1.getString("id")%>)">
							<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("discount_name")%></span>
							<span class="ui-li-count"><%=Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
					</a></li>
					<%
						}
					%>

					<li data-role="list-divider">Deactivated Promotions</li>
					<%
						ResultSet rs2 = s.executeQuery("select * from inventory_price_discount where is_active=0");
						while (rs2.next()) {
							Date CreatedOn = rs2.getDate("created_on");
					%>
					<li><a data-ajax="false" href="#"
						onClick="LoadPriceDiscount(<%=rs2.getString("id")%>)">
							<span style="font-size: 10pt; font-weight: 400;"><%=rs2.getString("discount_name")%></span>
							<span class="ui-li-count"><%=Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
					</a></li>
					<%
						}
					%>
				</ul>
			</div>
		</div>

		<div data-role="popup" id="popupDialogDeactivate"
			data-overlay-theme="a" data-theme="c" data-dismissible="true"
			style="min-width: 700px; overflow-y: auto; min-height: 600px; max-height: 600px"
			aclass="ui-corner-all">
			<div data-role="header" data-theme="a" class="ui-corner-top">
				<h1>Deactivate</h1>
			</div>
			<div data-role="content" data-theme="d"
				class="ui-corner-bottom ui-content">
				<ul data-role="listview" data-inset="true">
					<li data-role="list-divider">Deactivate Product Promotion</li>
					<li><textarea name="textarea"
							placeholder="Reason to deactivate" id="ProductPromotionReason"></textarea>
						<button data-theme="b" data-inline="true"
							id="ProductPromotionDeactivateSubmit"
							onClick="DeactivateProductPromotion()">Deactivate</button></li>
				</ul>
			</div>
		</div>

	</div>

</body>
</html>
<%
	s.close();
	ds.dropConnection();
%>
