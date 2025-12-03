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
long EditID = Utilities.parseLong(request.getParameter("OrderBookerTargetID"));
if (EditID > 0) {
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

Date todaydate = new Date();
String FromDate = Utilities.getDisplayDateFormat(todaydate);
String ToDate = Utilities.getDisplayDateFormat(todaydate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->

<script>
	var isEditCase =
<%=isEditCase%>
	;
	var EditID =
<%=EditID%>
	;
</script>

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

function LoadPerticularBrandBulkDiscount(BulkDiscountID)
{
	//alert($("#isEditCase").val());
	//window.location="SpotDiscount.jsp?Plist="+BulkDiscountid;
	document.getElementById("BrandBulkDiscountEditID").value=BulkDiscountID;
	document.getElementById("BrandBulkDiscountEditForm").submit();
}

	$(document)
			.delegate(
					"#PSRTargets",
					"pageinit",
					function() {

						$(".WorkflowAttachButton")
								.on(
										"click",
										function(event, ui) {
											//  alert($("#BulkDiscountLabel").val());
											if ($("#BulkDiscountLabel").val() == "") {
												alert("Please Add Discount Label");
											} else if ($("#BrandSelect").val() == 0) {
												alert("Please Select Brand");
											} else if ($("#PCIChannelSelect")
													.val() == 0) {
												alert("Please Select Channel");
											} else {
												$("#popup_workflow_attach")
														.popup(
																"open",
																{
																	transition : "slidedown",
																	positionTo : "window"
																});
											}
										});

						var manualuploader = $('#manual-fine-uploader')
								.fineUploader(
										{
											autoUpload : false,
											text : {
												uploadButton : '<i class="icon-plus icon-white"></i> Select Files'
											},
											request : {
												endpoint : 'inventory/UploadFileBrandBulkDiscount',
												paramsInBody : true,
												params : {
													RequestID : 123,
													DiscountLabel : function() {
														return $(
																"#BulkDiscountLabel")
																.val();
													},
													DiscountValidFrom : function() {
														return $(
																"#BulkDiscountValidFrom")
																.val();
													},
													DiscountValidTo : function() {
														return $(
																"#BulkDiscountValidTo")
																.val();
													},
													Activeness : function() {
														return $("#Activeness")
																.val();
													},
													BrandSelect : function() {
														return $("#BrandSelect")
																.val();
													},
													PCIChannelSelect : function() {
														return $(
																"#PCIChannelSelect")
																.val();
													},
													UserID : function() {
														return $("#userid")
																.val();
													}
												}
											}
										})
								.on('error', function(event, id, name, reason) {

								})
								.on(
										'complete',
										function(event, id, name, responseJSON) {
											if (responseJSON.Success == 'true') {
												alert("File uploaded successfully ");
												window.location = "BrandBulkDiscount.jsp";
											} else {
												alert("This month targets already exist.");
												window.location = "BrandBulkDiscount.jsp";
											}

											//showAttachments(pageid);
											//$.mobile.changePage(pageid);

										});

						$('#triggerUpload').click(function() {
							// alert("in click");
							manualuploader.fineUploader('uploadStoredFiles');
						});

					});
</script>

</head>

<body>

	<div data-role="page" id="PSRTargets" data-url="PSRTargets"
		data-theme="d">

		<jsp:include page="Header2.jsp">
			<jsp:param value="Brand Bulk Discount" name="title" />
		</jsp:include>
		<!-- /header -->

		<div data-role="content" data-theme="d">

			<form action="test2.jsp" name="DistributorTargetsMainForm"
				id="DistributorTargetsMainForm">
				<ul data-role="listview" data-inset="false" data-divider-theme="c">

					<li><input type="hidden" name="EditID" id="EditID"
						value="<%=EditID%>">

						<table style="width: 100%" border="0">
							<tr>
								<td style="width: 30%"><input type="text"
									placeholder="Discount Label" id="BulkDiscountLabel"
									name="BulkDiscountLabel" data-mini="true" value=""></td>
								<td style="width: 30%"><input type="text"
									placeholder="DD/MM/YYYY" id="BulkDiscountValidFrom"
									name="BulkDiscountValidFrom" data-mini="true"
									value="<%=FromDate%>"></td>
								<td style="width: 30%"><input type="text"
									placeholder="DD/MM/YYYY" id="BulkDiscountValidTo"
									name="BulkDiscountValidTo" data-mini="true" value="<%=ToDate%>">

								</td>
							</tr>
							<tr>

								<td style="width: 30%"><select name="Activeness"
									id="Activeness" data-mini="true">
										<option value="1" data-mini="true">Active</option>
										<option value="0" data-mini="true">In Active</option>
								</select></td>
								<td style="width: 35%"><select name="BrandSelect"
									id="BrandSelect" data-mini="true">
										<option value="0" data-mini="true">Select Brand</option>
										<%
										ResultSet rsBrands = s.executeQuery("SELECT * FROM pep.inventory_products_lrb_types where id!=3");
										while (rsBrands.next()) {
										%>

										<option value="<%=rsBrands.getString("id")%>" data-mini="true"><%=rsBrands.getString("label")%></option>
										<%
										}
										%>
								</select></td>
								<td style="width: 35%"><select name="PCIChannelSelect"
									id="PCIChannelSelect" data-mini="true">
										<option value="0" data-mini="true">Select Channel</option>
										<%
										ResultSet rsChannel = s.executeQuery("SELECT * FROM pep.pci_sub_channel");
										while (rsChannel.next()) {
										%>

										<option value="<%=rsChannel.getString("id")%>"
											data-mini="true"><%=rsChannel.getString("label")%></option>
										<%
										}
										%>
								</select></td>

							</tr>
							<tr>
								<td style="width: 20px; font-weight: normal;">Select CSV
									file to upload Targets</td>
								<td style="width: 30%; font-weight: normal;"><a href="#"
									style="f1loat: left; m1argin-right: 10px; width: 250px;"
									data-role="button" data-icon="arrow-u" data-corners="false"
									data-theme="b" class="WorkflowAttachButton">Select File</a></td>




							</tr>

						</table></li>






				</ul>

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
				<td>
					<!-- <a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#"  onClick="SaveBulkDiscount();">Save</a>
                    
                   
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='BulkDiscount.jsp'" >Reset</button> -->
                    
				</td>
              <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id=SpotDiscountSearch" >Search</a>
				</td>
			</tr>
		</table>
	</div>

		</div>

		<jsp:include page="LookupEmployeeSearchPopup.jsp">
			<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
		</jsp:include><!-- Include Employee Search -->



		<div data-role="popup" id="popupDialog" data-overlay-theme="a"
			data-theme="c" data-dismissible="true"
			style="min-width: 700px; overflow-y: auto; min-height: 600px; max-height: 600px"
			aclass="ui-corner-all">
			 <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
		<ul data-role="listview" data-inset="true"> 
		<li data-role="list-divider">Bulk Discount</li>
		<%
			ResultSet rs1 = s.executeQuery("select * from inventory_hand_to_hand_discount_brand");
		while(rs1.next())
		{
        Date CreatedOn = rs1.getDate("created_on"); 
 		%>
 		<li><a data-ajax="false" href="#" onClick="LoadPerticularBrandBulkDiscount(<%=rs1.getString("id")%>)">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("label") %></span>
				 	<span class="ui-li-count"><%= Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
				 	
					
					</a>
 		</li>
 		<%
 		} 
 		%> 
 		</ul>          
        </div>
        <form data-ajax="false" action="BrandBulkDiscountEdit.jsp" id="BrandBulkDiscountEditForm" method="POST">
        	<input type="hidden" name="Plist" id="BrandBulkDiscountEditID">
        </form>
		</div>
	</div>
</body>
</html>

<%
s.close();
ds.dropConnection();
%>