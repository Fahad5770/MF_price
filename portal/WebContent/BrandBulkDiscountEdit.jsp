<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard"
	scope="page" />
<jsp:setProperty name="bean" property="*" />
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="java.io.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
String PageID = "PSRTargets";
long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 485;
if ((String) session.getAttribute("UserID") == null) {
	response.sendRedirect("index.jsp");
}

if (Utilities.isAuthorized(FeatureID, SessionUserID) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("Plist"));
if (EditID > 0) {
	isEditCase = true;
}

System.out.println("EditID " + EditID);

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

Date todaydate = new Date();
String FromDate = Utilities.getDisplayDateFormat(todaydate);
String ToDate = Utilities.getDisplayDateFormat(todaydate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->



<link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css"
	rel="stylesheet">



<script
	src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>
<script src="js/OrderBookerTargets.js"></script>
<script src="js/lookups.js"></script>


<style>
.radio_style {
	display: block;
	width: 80px;
	height: 50px;
	background-repeat: no-repeat;
	background-position: -231px 0px;
}

.ui-table-reflow.ui-responsive {
	display: block;
}
</style>

<script>
	function SaveBulkDiscount() {
		$.mobile.showPageLoadingMsg();
		$.ajax({
			url : "inventory/BrandBulkDiscountEditExecute",
			data : {
				EditID :
<%=EditID%>
	,
				Activeness : $("#Activeness").val()

			},
			type : "POST",
			dataType : "json",
			success : function(json) {
				$.mobile.hidePageLoadingMsg();
				if (json.success == "true") {
					window.location = "BrandBulkDiscount.jsp";
				} else {
					alert(json.error);
				}
			},
			error : function(xhr, status) {
				alert("Server could not be reached.");
			}

		});
	}
</script>

</head>

<body>

	<div data-role="page" id="PSRTargets" data-url="PSRTargets"
		data-theme="d">

		<jsp:include page="Header2.jsp">
			<jsp:param value="Edit Brand Bulk Discount" name="title" />
		</jsp:include>
		<!-- /header -->

		<div data-role="content" data-theme="d">

			<form action="test2.jsp" name="DistributorTargetsMainForm"
				id="DistributorTargetsMainForm">
				<%
				/* System.out.println(
						"SELECT label, valid_from, valid_to, is_active, (select label from inventory_products_lrb_types where id=brand_id) as brand, (select label from pci_sub_channel where id=pci_channel_id) as channel FROM pep.inventory_hand_to_hand_discount_brand where id="
						+ EditID); */
				ResultSet rsBrandDiscount = s.executeQuery(
						"SELECT label, valid_from, valid_to, is_active, (select label from inventory_products_lrb_types where id=brand_id) as brand, (select label from pci_sub_channel where id=pci_channel_id) as channel FROM pep.inventory_hand_to_hand_discount_brand where id="
						+ EditID);
				if (rsBrandDiscount.first()) {
				%>
				<ul data-role="listview" data-inset="false" data-divider-theme="c">

					<li><input type="hidden" name="EditID" id="EditID"
						value="<%=EditID%>">

						<table style="width: 100%" border="0">
							<tr>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly value="<%=rsBrandDiscount.getString("label")%>"></td>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly value="<%=rsBrandDiscount.getDate("valid_from")%>"></td>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly value="<%=rsBrandDiscount.getDate("valid_to")%>">
								</td>
							</tr>
							<tr>

								<td style="width: 30%"><select name="Activeness"
									id="Activeness" data-mini="true">
										<option value="1"
											<%=(rsBrandDiscount.getInt("is_active") == 1) ? "selected" : ""%>
											data-mini="true">Active</option>
										<option value="0"
											<%=(rsBrandDiscount.getInt("is_active") == 0) ? "selected" : ""%>
											data-mini="true">In Active</option>
								</select></td>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly value="<%=rsBrandDiscount.getString("brand")%>">
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly value="<%=rsBrandDiscount.getString("channel")%>">
							</tr>

						</table></li>


					<li data-role="list-divider">Discount Details</li>
					<li>
					<li>


						<table border="0" width="30%" id="MyTable23">
							<tr>
								<td>From Quantity</td>
								<td>To Quantity</td>
								<td>Discount</td>

							</tr>
							<%
						/* 	System.out.println(
									"select from_qty,to_qty,discount from pep.inventory_hand_to_hand_discount_brand_details where hand_discount_id="
									+ EditID); */
							ResultSet rsBrandDiscountDetails = s2.executeQuery(
									"select from_qty,to_qty,discount from pep.inventory_hand_to_hand_discount_brand_details where hand_discount_id="
									+ EditID);
							while (rsBrandDiscountDetails.next()) {
							%>
							<tr>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly
									value="<%=rsBrandDiscountDetails.getString("from_qty")%>"></td>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly
									value="<%=rsBrandDiscountDetails.getString("to_qty")%>"></td>
								<td style="width: 30%"><input type="text" data-mini="true"
									readonly
									value="<%=rsBrandDiscountDetails.getString("discount")%>">
								</td>
							</tr>
							<%
							}
							%>
						</table>
					</li>


				</ul>
				<%
				}
				%>
			</form>
		</div>
		<!-- /content -->


		</table>
		<div data-role="popup" id="popup_workflow_attach"
			data-overlay-theme="a" data-theme="c" data-dismissible="false"
			style="max-width: 600px; min-width: 500px; max-height: 500px;"
			class="ui-corner-all">
			<div data-role="header" data-theme="b" class="ui-corner-top">
				<h1>Attach Files</h1>
			</div>
			<div data-role="content" data-theme="d"
				class="ui-corner-bottom ui-content">

				<div id="manual-fine-uploader"></div>
				<br> <input type="hidden" id="month1" /> <input type="hidden"
					id="year1" /> <input type="hidden" id="userid" />

				<button id="triggerUpload" data-role="button" data-inline="true"
					data-theme="b">Attach</button>
				<a href="#<%=PageID%>" data-role="button" data-inline="true"
					data-theme="c">Close</a>
			</div>
		</div>


		<div data-role="footer" data-position="fixed" data-theme="b">

			<div>
				<table style="width: 100%;">
					<tr>
						<div>
							<table style="width: 100%;">
								<tr>
									<td><a data-icon="check" data-theme="a" data-role="button"
										data-inline="true" id="DeliveryNoteSave" href="#"
										onClick="SaveBulkDiscount();">Save</a></td>

								</tr>
							</table>
						</div>
			</div>

			<jsp:include page="LookupEmployeeSearchPopup.jsp">
				<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
			</jsp:include><!-- Include Employee Search -->


		</div>
</body>
</html>

<%
s.close();
ds.dropConnection();
%>