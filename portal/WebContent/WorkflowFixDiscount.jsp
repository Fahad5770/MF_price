<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.workflow.WorkflowManager" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowAttachment"%>
<%@page import="java.util.Date"%>
 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
bean.isLoaded();

String PageID = "WorkflowManagerApproval";

if (request.getParameter("SecondPage") == null){
	PageID = "WorkflowManager";
}

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

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
<script
	src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>

<title>PBC Enterprise Portal</title>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/WorkflowManagerFixDiscount.js"></script>
	<link rel="stylesheet" href="css/home.css">
</head>
<body>

<div data-role="page" id="<%=PageID %>" data-url="<%=PageID %>" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Discount Request" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
        <fieldset class="ui-grid-a">
				
		        <div class="ui-block-a" style="width: 23%">
						<ul data-role="listview" data-icon="bars" style="margin-top: 1px; padding-top: 0px; margin-left: 0px;">
							<li data-role="list-divider" data-theme="c">Approval Chain<span class="ui-li-count"><%if (bean.isNewRequest){ %>Level 1<%}else{ %> Level <%=bean.STEPS.size() %><%} %></span></li>
							<%
							if (bean.isNewRequest == true){
								%>
								<li>
									<a href="#" >
										<img src="<%=session.getAttribute("UserPictureURL") %>" />
										<h3>Request</h3>
										<p>by <%=session.getAttribute("UserDisplayName") %><br>
										<b>In Process</b></p>
									</a>
								</li>
								<%
							}else{
								for (int i = (bean.STEPS.size()-1); i >= 0 ; i--){

									Date CompletedOn = bean.STEPS.get(i).COMPLETED_ON;
									String ActionLabel = bean.STEPS.get(i).ACTION_LABEL_PAST;
									
									if (CompletedOn == null){
										ActionLabel = bean.STEPS.get(i).ACTION_LABEL;
									}
									
									String UserPicURL = Utilities.getUserPictureURL((bean.STEPS.get(i).USER_ID), request);
								%>
								<li>
									<a href="#" >
										<img src="<%=UserPicURL%>" />
										<h3><%=ActionLabel%></h3>
										<p>by <%=bean.STEPS.get(i).USER_DISPLAY_NAME %><br>
										<b><%
										if (CompletedOn == null){
											if (bean.isDeclined){%>Declined<%}else{%>In Process<%}
										}else{
											%>On <%out.print(Utilities.getDisplayDateTimeFormat(bean.STEPS.get(i).COMPLETED_ON));
										}
										%></b>
										</p>
									</a>
								</li>
								<%
								}
							}
							%>
						</ul>
						<br><br>
						<div id="WorkflowAttachmentsBar">
						</div>									    
		        </div><!-- /ui-block -->
		        <div class="ui-block-b" style="width: 77%">
		        <form name="WorkflowContainerForm" class="WorkflowForm" id="WorkflowContainerForm" method="POST" action="#">
					<ul data-role="listview" data-inset="true" style="margin-left: 22px;margin-top: 6px;margin-right: 5px;">
						<li data-role="list-divider">Outlet Information</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							<tr>
								<td style="width: 65%">
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="OutletID">Outlet ID</label><input type="text" id="OutletID" name="OutletID" data-mini="true"></div>
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="OutletName">Outlet Name</label><input type="text" id="OutletName" name="OutletName" data-mini="true" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="BusinessType">Business Type</label><input type="text" id="BusinessType" name="BusinessType" data-mini="true" readonly="readonly"></div>
								</fieldset>						
								<fieldset class="ui-grid-solo">
									<div class="ui-block-a" style="padding-left: 5px;"><label for="address">Address</label><input id="address" name="address" type="text" value="" data-mini="true" readonly="readonly"></div>
								</fieldset>
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="region">Region</label><input type="text" id="region" name="region" data-mini="true" readonly="readonly"></div>								    
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="asm">ASM</label><input type="text" id="asm" name="asm" data-mini="true" data-theme="b" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="cr">CR</label><input type="text" id="cr" name="cr" data-mini="true" data-theme="b" readonly="readonly"></div>
								</fieldset>						
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="market">Market</label><input type="text" id="market" name="market" data-mini="true" value="" readonly="readonly"></div>
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="vehicle">Vehicle</label><input type="text" id="vehicle" name="vehicle" data-mini="true" value="" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="SamplingStatus"></label>&nbsp;<br><a id="WorkflowOutletDashboardLink" data-role="button" data-mini="true" data-inline="true" href="OutletDashboard.jsp?outletID=47" target="_blank" data-icon="grid">Outlet Dashboard</a></div>
								</fieldset>						
								
								</td>
								<td style="width: 35%; text-align: right;">
									<img id="OutletMap" style="width: 285px; height: 220px; border: solid 1px;">
								</td>
							</tr>
						</table> 
						</li>
						
					<!-- 	<li data-role="list-divider">Advance</li>
						<li>
							<fieldset class="ui-grid-c">
							    <div class="ui-block-a" style="padding: 5px;"><label for="AdvanceCompanyShare">Company Share</label><input type="text" id="AdvanceCompanyShare" name="AdvanceCompanyShare" data-mini="true" placeholder="Rs."></div>
							    <div class="ui-block-b" style="padding: 5px;"><label for="AdvanceAgencyShare">Agency Share</label><input type="text" id="AdvanceAgencyShare" name="AdvanceAgencyShare" data-mini="true" placeholder="Rs."></div>
							    <div class="ui-block-c" style="padding: 5px;"><label for="AdvanceValidFrom">Valid From</label><input type="text" id="AdvanceValidFrom" name="AdvanceValidFrom" data-mini="true" placeholder="DD/MM/YYYY"></div>
							    <div class="ui-block-d" style="padding: 5px;"><label for="AdvanceValidTo">Valid To</label><input type="text" id="AdvanceValidTo" name="AdvanceValidTo" data-mini="true" placeholder="DD/MM/YYYY"></div>
							</fieldset>						
					
						</li> -->
						
						<li data-role="list-divider">Fixed Discount</li>
						<li>
						<table>
							
							<thead>
							<tr>
							    <td style="width: 70px;"><label></label></td>
							    <td><label>Company Share</label></td>
							    <td><label>Agency Share</label></td>
							    <td><label>Deduction Term</label></td>
							    <td><label>Validity From</label></td>
							    <td><label>Validity To</label></td>
							</tr>						
							</thead>
							
							<tbody>
							<tr>
							    <td><label>Peak</label></td>
							    <td><input type="text" id="FixedCompanyShare" name="FixedCompanyShare" data-mini="true" placeholder="Rs."></td>
							    <td><input type="text" id="FixedAgencyShare" name="FixedAgencyShare" data-mini="true" placeholder="Rs."></td>
							    <td><input type="text" id="FixedDeductionTerm" name="FixedDeductionTerm" data-mini="true" placeholder="Rs. / Month"></td>
							    <td><input type="text" id="FixedValidFrom" name="FixedValidFrom" data-mini="true" placeholder="DD/MM/YYYY"></td>
							    <td><input type="text" id="FixedValidTo" name="FixedValidTo" data-mini="true" placeholder="DD/MM/YYYY"></td>
							</tr>						
							<tr>
							    <td><label>Off Peak</label></td>
							    <td><input type="text" id="FixedCompanyShareOP" name="FixedCompanyShareOP" data-mini="true" placeholder="Rs."></td>
							    <td><input type="text" id="FixedAgencyShareOP" name="FixedAgencyShareOP" data-mini="true" placeholder="Rs."></td>
							    <td><input type="text" id="FixedDeductionTermOP" name="FixedDeductionTermOP" data-mini="true" placeholder="Rs. / Month"></td>
							    <td><input type="text" id="FixedValidFromOP" name="FixedValidFromOP" data-mini="true" placeholder="Same as above" readonly="readonly"></td>
							    <td><input type="text" id="FixedValidToOP" name="FixedValidToOP" data-mini="true" placeholder="Same as above" readonly="readonly"></td>
							</tr>
							
							<tr>
								<td><label></label></td>
								<td colspan="5">
									<div data-role="collapsible" data-mini="true" data-theme="c" data-content-theme="d">
									    <h4>Sales Threshold</h4>
										<table>
											<thead>
											<tr>
											    <td style="text-align: center;"><label>Percentage</label></td>
											    <td style="text-align: center;"><label>Sales (Converted Cases)</label></td>
											    <td style="text-align: center;"><label>Discount</label></td>
											</tr>						
											</thead>
											<tbody>
											<tr>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdPercentage1" name="SalesThresholdPercentage1" value="100" data-mini="true"></td>
											    <td><input type="text" style="text-align: center;" id="SalesThresholdSales1" name="SalesThresholdSales1" data-mini="true"></td>
											    <td><input type="text" style="text-align: center;" id="SalesThresholdDiscount1" name="SalesThresholdDiscount1" data-mini="true"></td>
											</tr>						
											<tr>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdPercentage2" name="SalesThresholdPercentage2" value="60" data-mini="true"></td>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdSales2" name="SalesThresholdSales2" data-mini="true"></td>
											    <td><input type="text" style="text-align: center;" id="SalesThresholdDiscount2" name="SalesThresholdDiscount2" data-mini="true"></td>
											</tr>						
											<tr>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdPercentage3" name="SalesThresholdPercentage3" value="30" data-mini="true"></td>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdSales3" name="SalesThresholdSales3" data-mini="true"></td>
											    <td><input type="text" style="text-align: center;" id="SalesThresholdDiscount3" name="SalesThresholdDiscount3" data-mini="true"></td>
											</tr>						
											<tr>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdPercentage4" name="SalesThresholdPercentage4" value="15" data-mini="true"></td>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdSales4" name="SalesThresholdSales4" data-mini="true"></td>
											    <td><input type="text" style="text-align: center;" id="SalesThresholdDiscount4" name="SalesThresholdDiscount4" data-mini="true"></td>
											</tr>						
											<tr>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdPercentage5" name="SalesThresholdPercentage5" value="5" data-mini="true"></td>
											    <td><input type="text" readonly="readonly" style="text-align: center;" id="SalesThresholdSales5" name="SalesThresholdSales5" data-mini="true"></td>
											    <td><input type="text" style="text-align: center;" id="SalesThresholdDiscount5" name="SalesThresholdDiscount5" data-mini="true"></td>
											</tr>						
										</tbody>
										</table>
									    <label for="SalesThresholdApply">Apply sales threshold in addition to Peak and Off Peak discounts (whichever lower will be paid)</label>
									    <input type="checkbox" name="SalesThresholdApply" id="SalesThresholdApply" value="1" class="custom" data-mini="true" checked />
									    
									    
									</div>
																		
								</td>
							</tr>						
							</tbody>

						</table>
													
						</li>


						<!-- <li data-role="list-divider">Per Case Discount</li>
						<li>
						<table>
							<thead>
							<tr>
							    <td style="width: 150px;"><label><span style="padding-left: 8px;">Package</span></label></td>
							    <td style="width: 150px;"><label><span style="padding-left: 8px;">Brand</span></label></td>
							    <td><label>Company Share</label></td>
							    <td><label>Agency Share</label></td>
							    <td><label>Deduction Term</label></td>
							    <td><label>Hand to Hand</label></td>
							    <td><label>Validity From</label></td>
							    <td><label>Validity To</label></td>
							    
							</tr>						
							</thead>
							<tbody id="PerCaseSamplingContainer">
							</tbody>
						</table>
						</li>
						<li> -->
							<%-- <div>
								<a data-role="button" href="#" data-theme="b" data-icon="plus" data-inline="true" data-mini="true" id="AddPerCasePackage" <% if (bean.isNewRequest == false){%>class="ui-disabled"<%}%>>Add Case</a>
							</div> --%>
						</li>

					</ul> 
					<div data-role="popup" id="popup_workflow_forward" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:400px;" class="ui-corner-all">
					    <div data-role="header" data-theme="a" class="ui-corner-top">
					        <h1><%=bean.CURRENT_STEP.ACTION_BUTTON_LABEL %></h1>
					    </div>
					    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
							<%
							if (bean.isLastStep == false){
							%>
					        <p>This document will be forwarded <%if (bean.isNewRequest == false){ %>to</p>
					        <h3 class="ui-title"><%=bean.NEXT_STEP.USER_DISPLAY_NAME %>, <%=bean.NEXT_STEP.USER_DESIGNATION %><font style="font-weight: 400;"> <%}%>for <%=bean.NEXT_STEP.ACTION_LABEL %></font></h3>
					        <label for="textarea">Remarks</label>
					        <%}else{ %>
					        <p>This request will be closed and stakeholders will be notified:</p>
					        <label for="textarea">Message for stakeholders:</label>
					        <%} %>
							<textarea cols="40" rows="10" name="WorkflowStepRemarksPopup" id="WorkflowStepRemarksPopup"></textarea>
							
					        <a data-role="button" data-inline="true" data-theme="a" id="WorkflowFormSubmitButton" href="#" class="WorkflowSubmitConfirm">Proceed</a>
					        <a href="#WorkflowManager" data-role="button" data-inline="true" data-theme="c">Cancel</a>
	
					    </div>
					</div>
					<input type="hidden" name="DetailRows" id="DetailRows" value="0"> 
					<input type="hidden" name="RequestID" id="RequestID" value="<%=bean.REQUEST_ID%>">
					<input type="hidden" name="SamplingID" id="SamplingID" value="0">
					<input type="hidden" name="WorkflowStepRemarks" id="WorkflowStepRemarks" value="">
					<input type="hidden" name="StepID" id="StepID" value="<%=bean.CURRENT_STEP.STEP_ID%>">
					<input type="hidden" name="NextStepID" id="NextStepID" value="<%=bean.NEXT_STEP.STEP_ID%>">
					<input type="hidden" name="NextActionID" id="NextActionID" value="<%=bean.NEXT_STEP.ACTION_ID%>">
					<input type="hidden" name="NextUserID" id="NextUserID" value="<%=bean.NEXT_STEP.USER_ID%>">
					<input type="hidden" name="isLastStep" id="isLastStep" value="<%=bean.isLastStep%>">
					<input type="hidden" name="uvid" id="uvid" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>">
					</form>

				</div><!-- /ui-block -->
			</fieldset>
		<div data-role="popup" id="AJAXErrorPopup" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:400px;" class="ui-corner-all">
		    <div data-role="header" data-theme="a" class="ui-corner-top">
		        <h1>Error</h1>
		    </div>
		    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
		
		        <p>Sorry, an unexpected error has occurred.<br>Please contact system administrator if you need assistance.</p>
				<div data-role="collapsible" data-mini="true">
				    <h4>View Detail</h4>
				    <p id="AJAXErrorMessage"></p>
				</div>
				<a href="#WorkflowManager" data-role="button" data-inline="false" data-mini="true">Close</a>
		    </div>
		</div>   					      
						
    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="b">
       	<div >
			
			<%
			if (!bean.isDeclined){
			%>
			
	  		    
	  		    <a href="#" style="float: right; margin-right: 10px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="b" 	<%if (bean.isNewRequest == true){%>class="ui-disabled"<%}else{ %>class="WorkflowAttachButton"<%} %>>Attach Documents</a>
	  		    <a href="#" style="float: right; margin-right: 10px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="a" 	<%if (bean.isNewRequest == true){%>class="ui-disabled"<%}else{ %>class="WorkflowConversationButton"<%} %>>Conversation</a>

				<a href="#" id="WorkflowChatPreviewContainer" style="float: right; margin-right: 0px; display:none" data-role="button" data-corners="false" data-theme="a" class="ui-disabled"><span id="WorkflowChatPreview"></span></a>

				<%
				if (bean.CURRENT_STEP.USER_ID == SessionUserID || bean.isNewRequest == true){
					if (bean.isCompleted == false){
				%>
				<button class="WorkflowSubmitButton" data-icon="check" data-theme="a" style="float: left; padding-left: 10px;" data-inline="true"><%=bean.CURRENT_STEP.ACTION_BUTTON_LABEL %></button>
				<%
					}
				}
				%>
				<%
				if (bean.CURRENT_STEP.USER_ID == SessionUserID && bean.isNewRequest == false){
					if (bean.isCompleted == false){
				%>
				<button class="WorkflowDeclineButton" data-icon="check" data-theme="b" data-inline="true">Decline</button>
				<%
					}
				}
				%>
			<%
			}
			%>
		</div>
        
    </div><!-- /footer -->

      

				<div data-role="popup" id="popup_workflow_decline" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:400px;" class="ui-corner-all">
				    <div data-role="header" data-theme="b" class="ui-corner-top">
				        <h1>Decline</h1>
				    </div>
				    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
				        <p>Please confirm if you want to decline this request.</p>
				        <h3 class="ui-title"><font size="2" style="font-weight: 400"><u></u></font></h3>
				        <label for="textarea">Reason:</label>
						<textarea cols="40" rows="8" name="textarea" id="textarea"></textarea>
						
				        <a data-role="button" class="WorkflowDeclineConfirmButton" data-role="button" data-inline="true" data-theme="b">Decline</a>
				        <a href="#<%=PageID %>" data-role="button" data-inline="true" data-theme="c">Return</a>
				    </div>
				</div>         

				<div data-role="popup" id="popup_workflow_attach" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:500px;max-height:500px;" class="ui-corner-all">
				    <div data-role="header" data-theme="b" class="ui-corner-top">
				        <h1>Attach Files</h1>
				    </div>				
				    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">

						<div id="manual-fine-uploader"></div>
						<br>
						<label for="textarea">Description (if any):</label>
						<textarea cols="40" rows="10" name="WorkflowAttachmentDescription" id="WorkflowAttachmentDescription"></textarea>						
				        <button id="triggerUpload" data-role="button" data-inline="true" data-theme="b">Attach</button>
				        <a href="#<%= PageID %>" data-role="button" data-inline="true" data-theme="c">Close</a>
				    </div>
				</div>         

				<div data-role="popup" id="popup_workflow_conversation" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:700px;min-width:700px;max-height:700px;" class="ui-corner-all">
				<a href="#<%=PageID %>" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
				    <div data-role="content" data-theme="c" class="ui-corner-bottom ui-content" id="WorkflowChatContent">
					
				    </div>
				</div>         

				<div data-role="popup" id="popup_employee_info" class="ui-content" data-position-to="origin" style="max-width:350px">
					<ul data-role="listview" data-icon="bars">
						<li data-role="list-divider" data-theme="a">Musawwar Rasheed</li>
						<li>
							<a href="#workflow_sampling_new" >
								<img src="images/musawwar.jpg" />
								<p style="font-size: 10pt;">Employee ID: 3476<br>
								Area Sales Manager<br>
								Sales Department<br>
								Since March 13, 2009 (5.3 Years)
								</p>
							</a>
						</li>
					</ul>
				</div>         


</div>
<%
bean.close();
%>
</body>
</html>