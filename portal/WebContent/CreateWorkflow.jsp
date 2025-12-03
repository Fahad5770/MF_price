<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(26, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}


String UserDisplayName = (String)session.getAttribute("UserDisplayName");
String UserPictureURL = (String)session.getAttribute("UserPictureURL");
%>
<div data-role="page" id="CreateWorkflow" data-url="CreateWorkflow" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Create Workflow" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">

			
		<div class="ui-grid-a">
			<div class="ui-block-a" style="width: 25%;" style="margin-right: 10px;">
				
				<ul data-role="listview" data-inset="true" style="margin-right: 10px;" data-divider-theme="b" data-icon="false">
					<li data-role="list-divider">Workflow Type</li>
					<li>
						<select name="CreateWorkflowSelectType">
							<option>Sampling</option>
							<option>Distributor Credit</option>
							<option>Empty Credit</option>
							<option>Travel</option>
							<option>Customer Compaint</option>
							<option>IT Helpdesk</option>
						</select>
					</li>
					</ul>
				<ul data-role="listview" data-inset="true" style="margin-right: 10px;" data-divider-theme="b" data-icon="false">
					<li data-role="list-divider">Workflow Actions (Drag n Drop)</li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="Request" style="cursor:crosshair;"><a>Request</a></li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="Recommendation" style="cursor:crosshair;"><a>Recommendation</a></li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="Review" style="cursor:crosshair;"><a>Review</a></li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="Approval" style="cursor:crosshair;"><a>Approval</a></li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="FinalApproval" style="cursor:crosshair;"><a>Final Approval</a></li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="Processing" style="cursor:crosshair;"><a>Processing</a></li>
					<li draggable="true" ondragstart="CreateWorkflowDrag(event)" id="Decline" style="cursor:crosshair;"><a>Decline</a></li>
				</ul>
			
			</div>
			<div class="ui-block-b" style="width: 75%" ondrop="CreateWorkflowDrop(event)" ondragover="CreateWorkflowAllowDrop(event)">

		
			
				<ul data-role="listview" data-inset="true" data-split-icon="delete" data-split-theme="d">
					<li data-role="list-divider">Workflow Steps</li>
					<li id="CreateWorkflowDropZone">
						<fieldset class="ui-grid-d">
						    <div class="ui-block-a" style="padding: 5px;"><label>Action Type</label></div>
						    <div class="ui-block-b" style="padding: 5px;"><label>User ID</label></div>
						    <div class="ui-block-c" style="padding: 5px;"><label>Position Code</label></div>
						    <div class="ui-block-d" style="padding: 5px;"><label>Allow Editing</label></div>
						    <div class="ui-block-e" style="padding: 5px;"><label>Due Days</label></div>
						</fieldset>
						<fieldset class="ui-grid-d">
						    <div class="ui-block-a" style="padding: 5px;"><button data-corners="false" data-theme="b" >Request</button></div>
						    <div class="ui-block-b" style="padding: 5px;"><input type="text" value="Any" readonly="readonly" data-theme="c" class="ui-disabled"></div>
						    <div class="ui-block-c" style="padding: 5px;"><input type="text" value="Any" readonly="readonly" data-theme="c" class="ui-disabled"></div>
						    <div class="ui-block-d" style="padding: 5px;">
								<div class="CreateWorkflowAllowEdititing">
									<select name="flip-min" id="flip-min" data-role="slider">
										<option value="off">Allowed</option>
										<option value="on">Not Allowed</option>
									</select>
								</div>
						    </div>
						    <div class="ui-block-e" style="padding: 5px;"><input type="text" value="Not Applicable" readonly="readonly" data-theme="c" class="ui-disabled"></div>
						</fieldset>
						<fieldset class="ui-grid-d" data-theme="d">
						    <div class="ui-block-a" style="padding: 5px;"><button data-corners="false" data-theme="b" >Recommendation</button></div>
						    <div class="ui-block-b" style="padding: 5px;"><input type="text" value="1707" data-theme="d" ></div>
						    <div class="ui-block-c" style="padding: 5px;"><input type="text" placeholder="Position Code" readonly="readonly" data-theme="d"></div>
						    <div class="ui-block-d" style="padding: 5px;">
								<div class="CreateWorkflowAllowEdititing">
									<select name="flip-min" id="flip-min" data-role="slider">
										<option value="off">Allowed</option>
										<option value="on">Not Allowed</option>
									</select>
								</div>
						    </div>
						    <div class="ui-block-e" style="padding: 5px;"><input type="text" placeholder="Due Days" data-theme="d"></div>
						</fieldset>
						<fieldset class="ui-grid-d" data-theme="d">
						    <div class="ui-block-a" style="padding: 5px;"><button data-corners="false" data-theme="b" >Review</button></div>
						    <div class="ui-block-b" style="padding: 5px;"><input type="text" value="2381" data-theme="d" ></div>
						    <div class="ui-block-c" style="padding: 5px;"><input type="text" placeholder="Position Code" readonly="readonly" data-theme="d"></div>
						    <div class="ui-block-d" style="padding: 5px;">
								<div class="CreateWorkflowAllowEdititing">
									<select name="flip-min" id="flip-min" data-role="slider">
										<option value="off">Allowed</option>
										<option value="on">Not Allowed</option>
									</select>
								</div>
						    </div>
						    <div class="ui-block-e" style="padding: 5px;"><input type="text" placeholder="Due Days" data-theme="d"></div>
						</fieldset>
						<fieldset class="ui-grid-d" data-theme="d">
						    <div class="ui-block-a" style="padding: 5px;"><button data-corners="false" data-theme="b" >Approval</button></div>
						    <div class="ui-block-b" style="padding: 5px;"><input type="text" value="2062" data-theme="d" ></div>
						    <div class="ui-block-c" style="padding: 5px;"><input type="text" placeholder="Position Code" readonly="readonly" data-theme="d"></div>
						    <div class="ui-block-d" style="padding: 5px;">
								<div class="CreateWorkflowAllowEdititing">
									<select name="flip-min" id="flip-min" data-role="slider">
										<option value="off">Allowed</option>
										<option value="on">Not Allowed</option>
									</select>
								</div>
						    </div>
						    <div class="ui-block-e" style="padding: 5px;"><input type="text" placeholder="Due Days" data-theme="d"></div>
						</fieldset>
						<fieldset class="ui-grid-d" data-theme="d">
						    <div class="ui-block-a" style="padding: 5px;"><button data-corners="false" data-theme="b" >Final Approval</button></div>
						    <div class="ui-block-b" style="padding: 5px;"><input type="text" value="1707" data-theme="d" ></div>
						    <div class="ui-block-c" style="padding: 5px;"><input type="text" placeholder="Position Code" readonly="readonly" data-theme="d"></div>
						    <div class="ui-block-d" style="padding: 5px;">
								<div class="CreateWorkflowAllowEdititing">
									<select name="flip-min" id="flip-min" data-role="slider">
										<option value="off">Allowed</option>
										<option value="on">Not Allowed</option>
									</select>
								</div>
						    </div>
						    <div class="ui-block-e" style="padding: 5px;"><input type="text" placeholder="Due Days" data-theme="d"></div>
						</fieldset>

						
					</li>
				</ul>        
			
			
			</div>	   
		</div>	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true">Save & Close</button>
		<button data-icon="check" data-theme="b" data-inline="true">Save</button>
	</div>    	
    </div>

</div>

</body>
</html>