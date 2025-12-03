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

String PageID = "MonthlyDiscountApproval";

if (request.getParameter("SecondPage") == null){
	PageID = "MonthlyDiscount";
}

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
long RequestID = Utilities.parseLong(request.getParameter("requestID"));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script>
	var LoadDiscountForm = true;
	var RequestID = <%=RequestID%>;
	</script>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/WorkflowManagerMonthlyDiscount.js"></script>
</head>
<body>

<div data-role="page" id="<%=PageID %>" data-url="<%=PageID %>" data-theme="d">
	<input type="hidden" name="RequestID" id="RequestID" value="<%=RequestID%>">
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
						<div id="WorkflowAttachmentsBar" style="min-height: 200px">
						</div>									    
		        </div><!-- /ui-block -->
		        <div class="ui-block-b" style="width: 77%; padding-left: 20px; margin-top: -10px;">
		        
		        	<jsp:include page="MonthlyDiscountMain.jsp" >
		        	
				    	<jsp:param value="<%=RequestID%>" name="RequestID"/>
				    	<jsp:param value="1" name="Subpage"/>
				    	<jsp:param value="1" name="ShowFilters"/>
				    	
				    </jsp:include>
		        
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
				
				
				<div data-role="popup" id="SamplingDetailPopup" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="max-width:800px;min-width:800px;max-height:600px;" class="ui-corner-all">
					<a href="#SamplingDashboard" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
				    <div data-role="content" data-theme="c" class="ui-corner-bottom ui-content" id="SamplingDetailContent">
						<img src="images/loader.gif">
				    </div>
				</div>    
				
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


</div>

 

<%
bean.close();
%>
</body>
</html>