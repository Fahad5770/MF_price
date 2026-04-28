<%@page import="com.pbc.employee.EmployeeHierarchy"%>
<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.workflow.WorkflowManager" scope="page" />
<jsp:setProperty name="bean" property="*" />
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowAttachment"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%

bean.isLoaded();

String PageID = "NewOutletRequest";

int SecondPage=Utilities.parseInt(request.getParameter("SecondPage"));

if (request.getParameter("SecondPage") == null) {
	PageID = "WorkflowManager";
}

long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="lib/jqm130/jquery.mobile-1.3.0.min.css">
<link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css"
	rel="stylesheet">

<script src="lib/jquery-1.9.1.min.js?123=123"></script>
<script src="lib/jqm130/jquery.mobile-1.3.0.min.js?123=123"></script>
<script src="lib/jquery-validation/jquery.validate.js?123=123"></script>
<script src="lib/jqueryui1102/js/jquery-ui-1.10.2.custom.min.js?123=123"></script>
<script src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>

<title>PBC Enterprise Portal</title>

<script src="js/WorkflowManagerRegisterOutlet.js?74855487=f711"></script>
<link rel="stylesheet" href="css/home.css">
</head>
<body>

	<div data-role="page" id="<%=PageID%>" data-url="<%=PageID%>"
		data-theme="d">
		<%
		long RequestID = Utilities.parseLong(request.getParameter("requestID"));
		Datasource ds = new Datasource();
		ds.createConnection();

		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		Statement s2 = ds.createStatement();
		long PromoID = 0;

		ResultSet rs11 = s.executeQuery("select id from gl_customer_credit_limit_request where request_id=" + RequestID);
		if (rs11.first()) {
			PromoID = rs11.getLong("id");
		}
		%>



		<jsp:include page="Header2.jsp">
			<jsp:param value="New Outlet Request" name="title" />
		</jsp:include>
		<!-- /header -->




		<div data-role="content" data-theme="d"
			style="padding: 0px; margin: 0;">
			<fieldset class="ui-grid-a">

				<div class="ui-block-a" style="width: 23%">
					<ul data-role="listview" data-icon="bars"
						style="margin-top: 1px; padding-top: 0px; margin-left: 0px;">
						<li data-role="list-divider" data-theme="c">Approval Chain<span
							class="ui-li-count"> <%
 if (bean.isNewRequest) {
 %>Level 1<%
 } else {
 %> Level <%=bean.STEPS.size()%> <%
 }
 %>
						</span></li>
						<%
						if (bean.isNewRequest == true) {
						%>
						<li><a href="#"> <img
								src="<%=session.getAttribute("UserPictureURL")%>" />
								<h3>Request</h3>
								<p>
									by
									<%=session.getAttribute("UserDisplayName")%><br> <b>In
										Process</b>
								</p>
						</a></li>
						<%
						} else {
						for (int i = (bean.STEPS.size() - 1); i >= 0; i--) {

							Date CompletedOn = bean.STEPS.get(i).COMPLETED_ON;
							String ActionLabel = bean.STEPS.get(i).ACTION_LABEL_PAST;

							if (CompletedOn == null) {
								ActionLabel = bean.STEPS.get(i).ACTION_LABEL;
							}

							String UserPicURL = Utilities.getUserPictureURL((bean.STEPS.get(i).USER_ID), request);
						%>
						<li><a href="#"> <img src="<%=UserPicURL%>" />
								<h3><%=ActionLabel%></h3>
								<p>
									by
									<%=bean.STEPS.get(i).USER_DISPLAY_NAME%><br> <b> <%
 if (CompletedOn == null) {
 	if (bean.isDeclined) {
 %>Declined<%
 } else {
 %>In Process<%
 }
 } else {
 %>On <%
 out.print(Utilities.getDisplayDateTimeFormat(bean.STEPS.get(i).COMPLETED_ON));
 }
 %>
									</b>
								</p>
						</a></li>
						<%
						}
						}
						%>
					</ul>
					<br> <br>
					<div id="WorkflowAttachmentsBar"></div>
				</div>
				<!-- /ui-block -->
				<div class="ui-block-b" style="width: 77%">
					<form name="WorkflowContainerForm" class="WorkflowForm"
						id="WorkflowContainerForm" method="POST" action="#">
						<ul data-role="listview" data-inset="true"
							style="margin-left: 22px; margin-top: 6px; margin-right: 5px;">
							<li data-role="list-divider">Outlet Details</li>
							<li>
								<%
								String OutletrequestId = "";
								String OutletName = "";
								String OutletAddress = "";
								String OwnerName = "";
								String OwnerNumber = "";
								String OwnerCNICNo = "";
								String DistributorName = "";
								String Area = "";
								String SubArea = "";
								String PurchaserName = "";
								String PurchaserNumber = "";
								String PSR = "";
								String Channel = "", day_name="";
								int is_owner_purchaser = 0, sub_channel_id=0;
								long distributor_id = 0;
								double lat=0.0,lng=0.0,Accuracy=0.0;

								ResultSet rs6 = s1.executeQuery(
										"SELECT  (SELECT long_name FROM common_days_of_week dw where dw.id=cor.day) as day_name,lat,lng,accuracy,cor.distributor_id,cor.sub_channel_id,is_owner_purchaser,(SELECT label FROM pep.pci_sub_channel psc where psc.id=cor.sub_channel_id) as channel,(select DISPLAY_NAME from users where id=created_by) as createdBy,purchaser_name,purchaser_number,area_label,sub_area_label,owner_cnic_number,outlet_id,outlet_name,outlet_address,owner_name,owner_contact_number,(select name from common_distributors cd where cd.distributor_id=cor.distributor_id) as dis_name FROM common_outlets_request cor  where request_id="
										+ RequestID);
								if (rs6.first()) {
									OutletrequestId = rs6.getString("outlet_id");
									OutletName = rs6.getString("outlet_name");
									OutletAddress = rs6.getString("outlet_address");
									OwnerName = rs6.getString("owner_name");
									OwnerNumber = rs6.getString("owner_contact_number");
									DistributorName = rs6.getString("dis_name");
									OwnerCNICNo = rs6.getString("owner_cnic_number");
									Area = rs6.getString("area_label");
									SubArea = rs6.getString("sub_area_label");
									PurchaserName = rs6.getString("owner_name");
									PurchaserNumber = rs6.getString("owner_contact_number");
									PSR = rs6.getString("createdBy");
									Channel = rs6.getString("channel");
									is_owner_purchaser = rs6.getInt("is_owner_purchaser");
									distributor_id =rs6.getLong("cor.distributor_id");
									sub_channel_id =rs6.getInt("cor.sub_channel_id");
									lat= rs6.getDouble("lat");
									lng= rs6.getDouble("lng");
									Accuracy= rs6.getDouble("accuracy");
									day_name= rs6.getString("day_name");
								}
								int checkStep=1;
								ResultSet rsS = s.executeQuery("SELECT max(step_id) FROM pep.workflow_requests_steps where request_id="+RequestID);
								if(rsS.first())
									%>
									<input type="hidden" id="DistributorId" name="DistributorId" value="<%=distributor_id%>" >
								<%-- <input type="hidden" id="ChannelId" name="ChannelId" value="<%=sub_channel_id%>" > --%>
								<input type="hidden" id="IsPurchaser" name="IsPurchaser" value="<%=is_owner_purchaser%>" >
								
									<%
								if((bean.CURRENT_STEP.STEP_ID == 2 || bean.CURRENT_STEP.STEP_ID ==3 || bean.CURRENT_STEP.STEP_ID == 4) && SecondPage!=1/*  && (bean.CURRENT_STEP.STEP_ID == 2 && checkStep<3) || (bean.CURRENT_STEP.STEP_ID == 3 && checkStep<4) */){
								%>
								
								<table style="width: 100%; margin-top: 15px;">
									<tr>
									</tr>
									<tr style="text-align: left">
										<th style="width: 20%">Outlet Request ID</th>
										<th style="width: 20%">Outlet Name</th>
										<th style="width: 20%">Outlet Address</th>
										<th style="width: 20%">Distributor Name</th>
										<th style="width: 20%">PSR</th>
									</tr>
									<tr>
										<td style="width: 20%"><input type="text" id="OutletrequestId" name="OutletrequestId" value="<%=OutletrequestId%>" readonly></td>

										<td style="width: 20%"><input type="text" id="OutletName" name="OutletName" value="<%=OutletName%>"></td>
										
										<td style="width: 20%"><input type="text" id="OutletAddress" name="OutletAddress" value="<%=OutletAddress%>" ></td>

										<td style="width: 20%"><input type="text" id="DistributorName" name="DistributorName" value="<%=DistributorName%>" readonly></td>

										<td style="width: 20%"><input type="text" id="PSR" name="PSR" value="<%=PSR%>" readonly></td>
											

									</tr>

									<tr style="text-align: left">
										<th style="width: 20%">Channel</th>
										<th style="width: 20%">Area</th>
										<th style="width: 20%">Sub Area</th>
										<th style="width: 20%">Frequency</th>
										<th style="width: 20%">Is Owner Purchaser</th>
									</tr>
									<tr>
										
										
										<%-- <td style="width: 20%"><input type="text" id="Channel" name="Channel" value="<%=Channel%>" readonly></td> --%>

										<td style="width: 20%"><select name="Channel"
											data-mini="true" id="Channel"
											onkeypress="if(event.keyCode==13) foucsnext()">
												<option value="-1">Select Sub channel</option>
												<%
												int SubID = 0;
												String Label = "";

												ResultSet rsSub = s1.executeQuery("SELECT * FROM pci_sub_channel  ");
												while (rsSub.next()) {
													SubID = rsSub.getInt("id");
													Label = rsSub.getString("label");
													String selected = (sub_channel_id == SubID) ? "Selected" : "";
												%>

												<option value="<%=SubID%>" <%=selected %> ><%=Label%></option>


												<%
												}
												%>
										</select></td>
										
										<td style="width: 20%"><input type="text"  id="Area" name="Area" value="<%=Area%>" ></td>

										<td style="width: 20%"><input type="text"  id="SubArea" name="SubArea" value="<%=SubArea%>" ></td>
										
										<td style="width: 20%"><input type="text" id="DayNumber" name="DayNumber" value="<%=day_name%>" readonly></td>
										
										<td style="width: 20%"><input type="text" id="IsPurchaserN" name="IsPurchaserN" value="<%=(is_owner_purchaser == 1) ? "yes" : "No"%>" readonly></td>
									</tr>

									<tr style="text-align: left">
										<th style="width: 20%">Owner Name</th>
										<th style="width: 20%">Owner Mobile</th>
										<th style="width: 20%">Owner CNIC</th>
										<th style="width: 20%">Purchaser Name</th>
										<th style="width: 20%">Purchaser Mobile</th>
									</tr>
									<tr>
										<td style="width: 20%"><input type="text" id="OwnerName" name="OwnerName" value="<%=OwnerName%>" ></td>
										
										<td style="width: 20%"><input type="text"  id="OwnerNumber" name="OwnerNumber" value="<%=OwnerNumber%>" ></td>
										
										<td style="width: 20%"><input type="text" id="OwnerCNICNo" name="OwnerCNICNo" value="<%=OwnerCNICNo%>" ></td>
										
										<td style="width: 20%"><input type="text"  id="PurchaserName" name="PurchaserName" value="<%=PurchaserName%>"></td>
										
										<td style="width: 20%"><input type="text"  id="PurchaserNumber" name="PurchaserNumber" value="<%=PurchaserNumber%>"></td>
										
										
									</tr>
									<tr style="text-align: left">
										<th style="width: 20%">Latitude</th>
										<th style="width: 20%">Longitude</th>
										<th style="width: 20%">Accuracy</th>
									</tr>

									<tr>
										
										<td style="width: 20%"><input type="text"  id="PurchaserNumber" name="PurchaserNumber" value="<%=lat%>" readonly></td>
										
										<td style="width: 20%"><input type="text"  id="PurchaserNumber" name="PurchaserNumber" value="<%=lng%>" readonly></td>
										
										<td style="width: 20%"><input type="text"  id="PurchaserNumber" name="PurchaserNumber" value="<%=Accuracy%>" readonly>
										</td>
									</tr>




								</table> 
								<%}else{ %>
								 <table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							<thead>
									
								<tr>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Outlet Request ID</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Outlet Name</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Outlet Address</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Owner Name</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Owner Number</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:25%;">Owner CNIC</th>
										
								</tr>	
							</thead>
							
							<tbody>
								<tr style="b1ackground-color:#efedee">
									<td style="width:15%; padding:5px;" valign="top"><%=OutletrequestId %></td>
									<td style="width:15%; padding:5px;"><%=OutletName %></td>
									<td style="width:15%; padding:5px;"valign="top"><%=OutletAddress %></td>
									<td style="width:15%; padding:5px;" valign="top"><%=OwnerName %></td>
									<td style="width:15%; padding:5px;"><%=OwnerNumber %></td>
									<td style="width:25%; padding:5px;"valign="top"><%=OwnerCNICNo %></td>
								</tr>	
								
							</tbody>
						</table> 
						 <table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							<thead>
									
								<tr>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Area</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Sub Area</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Purchaser Name</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Purchaser Number</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">PSR</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:25%;">Channel</th>
										
								</tr>	
							</thead>
							
							<tbody>
								<tr style="b1ackground-color:#efedee">
									<td style="width:15%; padding:5px;" valign="top"><%=Area %></td>
									<td style="width:15%; padding:5px;"><%=SubArea %></td>
									<td style="width:15%; padding:5px;"valign="top"><%=PurchaserName %></td>
									<td style="width:15%; padding:5px;" valign="top"><%=PurchaserNumber %></td>
									<td style="width:15%; padding:5px;"><%=PSR %></td>
									<td style="width:25%; padding:5px;"valign="top"><%=Channel %></td>
								</tr>	
								
							</tbody>
						</table> 
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							<thead>
									
								<tr>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">is_owner_purchaser</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Frequency</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">distributor_id</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Distributor</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Latitude</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Longitude</th>
										
								</tr>	
							</thead>
							
							<tbody>
								<tr style="b1ackground-color:#efedee">
									<td style="width:15%; padding:5px;" valign="top"><%=(is_owner_purchaser == 1) ? "yes" : "No" %></td>
									<td style="width:15%; padding:5px;"><%=day_name %></td>
									<td style="width:15%; padding:5px;"><%=distributor_id %></td>
									<td style="width:15%; padding:5px;"><%=DistributorName %></td>
									<td style="width:15%; padding:5px;"><%=lat %></td>
									<td style="width:15%; padding:5px;"><%=lng %></td>
								</tr>	
								
							</tbody>
						</table> 
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							<thead>
									
								<tr>
									<th  style="background-color:#efedee; padding:5px;font-size:12px;width:15%;">Accuracy</th>
										
								</tr>	
							</thead>
							
							<tbody>
								<tr style="b1ackground-color:#efedee">
									<td style="width:15%; padding:5px;"><%=Accuracy %></td>
								</tr>	
								
							</tbody>
						</table> 
						<%} %>
							</li>

							<li data-role="list-divider">Outlet Images</li>


							<table style="width: 100%; margin-top: -8px" cellpadding="0" cellspacing="0">
								<tr>
									<td>

										<table class="GridWithBorder">

											<%
											String[] arr = { "", "", "", "","","","","","","","","" };

											int i = 0;
											System.out.println("select * FROM mobile_outlets_request_files where outlet_request_id=" + OutletrequestId);
											ResultSet rsi = s.executeQuery("select * FROM mobile_outlets_request_files where outlet_request_id=" + OutletrequestId);
											while (rsi.next()) {
												arr[i] = rsi.getString("filename");
												i++;
											}
											System.out.println("i : "+i);
											System.out.println("arr[0] : "+arr[0]);
											if (i > 0) {
												System.out.println("arr[0] : "+arr[0]);
											%>

											<tr>

												<td>

													<h3></h3>
													<br />
													<center>
														<img
															src="mobile/MobileFileDownloadOutlet?file=<%=arr[0]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
												<%
												if (!arr[1].equals("")) {
													System.out.println("arr[1] : "+arr[1]);
												%>
												<td>

													<h3></h3>
													<br />
													<center>
														<img
															src="mobile/MobileFileDownloadOutlet?file=<%=arr[1]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
												<%
												}
												%>
											</tr>

											<tr>
												<%
												if (!arr[2].equals("")) {
													System.out.println("arr[2] : "+arr[2]);
												%>
												<td>

													<h3></h3>
													<br />
													<center>
														<img
															src="mobile/MobileFileDownloadOutlet?file=<%=arr[2]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>

												<td>
													<%
													}
													if (!arr[3].equals("")) {
														System.out.println("arr[3] : "+arr[3]);
													%>
													<h3></h3>
													<br />
													<center>
														<img
															src="mobile/MobileFileDownloadOutlet?file=<%=arr[3]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
												<%
												}
												%>
											</tr>


											<%
											} else {
											%><tr>
												<td><p style="margin-top:12px">No images Found</p></td>
											</tr>
											<%
											}
											%>
										</table>

									</td>
								</tr>
							</table>


							<li style="display: none;">
								<table>
									<thead>

									</thead>
									<tbody id="PerCaseSamplingContainer">
									</tbody>
								</table>


							</li>
							<li></li>

						</ul>
						<div data-role="popup" id="popup_workflow_forward"
							data-overlay-theme="a" data-theme="c" data-dismissible="false"
							style="max-width: 600px; min-width: 400px;" class="ui-corner-all">
							<div data-role="header" data-theme="a" class="ui-corner-top">
								<h1><%=bean.CURRENT_STEP.ACTION_BUTTON_LABEL%></h1>
							</div>
							<div data-role="content" data-theme="d"
								class="ui-corner-bottom ui-content">
								<%
								System.out.println("kk-1");
								if (bean.isLastStep == false) {
								%>
								<p>
									This document will be forwarded
									<%
								if (bean.isNewRequest == false) {
								%>to
								</p>
								<h3 class="ui-title"><%=bean.NEXT_STEP.USER_DISPLAY_NAME%>,
									<%=bean.NEXT_STEP.USER_DESIGNATION%><font
										style="font-weight: 400;"> <%
 }
 %>for <%=bean.NEXT_STEP.ACTION_LABEL%></font>
								</h3>
								<label for="textarea">Remarks</label>
								<%
								} else {
								%>
								<p>This request will be closed and stakeholders will be
									notified:</p>
								<label for="textarea">Message for stakeholders:</label>
								<%
								}
								%>
								<textarea cols="40" rows="10" name="WorkflowStepRemarksPopup"
									id="WorkflowStepRemarksPopup"></textarea>

								<a data-role="button" data-inline="true" data-theme="a"
									id="WorkflowFormSubmitButton" href="#"
									class="WorkflowSubmitConfirm">Proceed</a> <a
									href="#WorkflowManager" data-role="button" data-inline="true"
									data-theme="c">Cancel</a>

							</div>
						</div>
						<input type="hidden" name="DetailRows" id="DetailRows" value="0">
						<input type="hidden" name="RequestID" id="RequestID"
							value="<%=bean.REQUEST_ID%>"> <input type="hidden"
							name="SamplingID" id="SamplingID" value="0"> <input
							type="hidden" name="WorkflowStepRemarks" id="WorkflowStepRemarks"
							value=""> <input type="hidden" name="StepID" id="StepID"
							value="<%=bean.CURRENT_STEP.STEP_ID%>"> <input
							type="hidden" name="NextStepID" id="NextStepID"
							value="<%=bean.NEXT_STEP.STEP_ID%>"> <input type="hidden"
							name="NextActionID" id="NextActionID"
							value="<%=bean.NEXT_STEP.ACTION_ID%>"> <input
							type="hidden" name="NextUserID" id="NextUserID"
							value="<%=bean.NEXT_STEP.USER_ID%>"> <input type="hidden"
							name="isLastStep" id="isLastStep" value="<%=bean.isLastStep%>">
						<input type="hidden" name="uvid" id="uvid"
							value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>">
						<input type="hidden" name="CustomerName" id="CustomerName"
							value="<%=""%>" />
					</form>

				</div>
				<!-- /ui-block -->
			</fieldset>
			<div data-role="popup" id="AJAXErrorPopup" data-overlay-theme="a"
				data-theme="c" data-dismissible="false"
				style="max-width: 600px; min-width: 400px;" class="ui-corner-all">
				<div data-role="header" data-theme="a" class="ui-corner-top">
					<h1>Error</h1>
				</div>
				<div data-role="content" data-theme="d"
					class="ui-corner-bottom ui-content">

					<p>
						Sorry, an unexpected error has occurred.<br>Please contact
						system administrator if you need assistance.
					</p>
					<div data-role="collapsible" data-mini="true">
						<h4>View Detail</h4>
						<p id="AJAXErrorMessage"></p>
					</div>
					<a href="#WorkflowManager" data-role="button" data-inline="false"
						data-mini="true">Close</a>
				</div>
			</div>

		</div>
		<!-- /content -->
		<div data-role="footer" data-position="fixed" data-theme="b">
			<div>

				<%
				if (!bean.isDeclined) {
				%>


				<%--     <a href="#" style="float: right; margin-right: 10px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="b" 	<%if (bean.isNewRequest == true){%>class="ui-disabled"<%}else{ %>class="WorkflowAttachButton"<%} %>>Attach Documents</a>
	  		    <a href="#" style="float: right; margin-right: 10px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="a" 	<%if (bean.isNewRequest == true){%>class="ui-disabled"<%}else{ %>class="WorkflowConversationButton"<%} %>>Conversation</a> --%>

				<a href="#" id="WorkflowChatPreviewContainer"
					style="float: right; margin-right: 0px; display: none"
					data-role="button" data-corners="false" data-theme="a"
					class="ui-disabled"><span id="WorkflowChatPreview"></span></a>

				<%
				if (bean.CURRENT_STEP.USER_ID == SessionUserID || bean.isNewRequest == true) {
					if (bean.isCompleted == false) {
				%>
				<button class="WorkflowSubmitButton" id="outletBtn" data-icon="check"
					data-theme="a" style="float: left; padding-left: 10px;"
					data-inline="true"><%=bean.CURRENT_STEP.ACTION_BUTTON_LABEL%></button>
				<%
				}
				}
				%>
				<%
				if (bean.CURRENT_STEP.USER_ID == SessionUserID && bean.isNewRequest == false) {
					if (bean.isCompleted == false) {
				%>
				<button class="WorkflowDeclineButton" data-icon="check"
					data-theme="b" data-inline="true">Decline</button>
				<%
				}
				}
				%>
				<%
				}
				%>
			</div>

		</div>
		<!-- /footer -->



		<div data-role="popup" id="popup_workflow_decline"
			data-overlay-theme="a" data-theme="c" data-dismissible="false"
			style="max-width: 600px; min-width: 400px;" class="ui-corner-all">
			<div data-role="header" data-theme="b" class="ui-corner-top">
				<h1>Decline</h1>
			</div>
			<div data-role="content" data-theme="d"
				class="ui-corner-bottom ui-content">
				<p>Please confirm if you want to decline this request.</p>
				<h3 class="ui-title">
					<font size="2" style="font-weight: 400"><u></u></font>
				</h3>
				<label for="textarea">Reason:</label>
				<textarea cols="40" rows="8" name="textarea" id="textarea"></textarea>

				<a data-role="button" class="WorkflowDeclineConfirmButton"
					data-role="button" data-inline="true" data-theme="b">Decline</a> <a
					href="#<%=PageID%>" data-role="button" data-inline="true"
					data-theme="c">Return</a>
			</div>
		</div>

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
				<br> <label for="textarea">Description (if any):</label>
				<textarea cols="40" rows="10" name="WorkflowAttachmentDescription"
					id="WorkflowAttachmentDescription"></textarea>
				<button id="triggerUpload" data-role="button" data-inline="true"
					data-theme="b">Attach</button>
				<a href="#<%=PageID%>" data-role="button" data-inline="true"
					data-theme="c">Close</a>
			</div>
		</div>

		<div data-role="popup" id="popup_workflow_conversation"
			data-overlay-theme="a" data-theme="c" data-dismissible="false"
			style="max-width: 700px; min-width: 700px; max-height: 700px;"
			class="ui-corner-all">
			<a href="#<%=PageID%>" data-role="button" data-theme="a"
				data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
			<div data-role="content" data-theme="c"
				class="ui-corner-bottom ui-content" id="WorkflowChatContent">

			</div>
		</div>

		<div data-role="popup" id="popup_employee_info" class="ui-content"
			data-position-to="origin" style="max-width: 350px">
			<ul data-role="listview" data-icon="bars">
				<li data-role="list-divider" data-theme="a">Musawwar Rasheed</li>
				<li><a href="#workflow_sampling_new"> <img
						src="images/musawwar.jpg" />
						<p style="font-size: 10pt;">
							Employee ID: 3476<br> Area Sales Manager<br> Sales
							Department<br> Since March 13, 2009 (5.3 Years)
						</p>
				</a></li>
			</ul>
		</div>


	</div>
	<%
	bean.close();
	%>
</body>
</html>