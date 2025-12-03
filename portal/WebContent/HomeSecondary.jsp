<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.workflow.ConversationMessage"%>
<%@page import="com.pbc.workflow.WorkflowChat"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%
	if ((String)session.getAttribute("UserID") == null){
		response.sendRedirect("index.jsp");
	}
	
	long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
	
	long SessionUserDistributorID = Utilities.parseLong((String)session.getAttribute("UserDistributorID"));
	
	String UserDisplayName = (String)session.getAttribute("UserDisplayName");
	String UserPictureURL = (String)session.getAttribute("UserPictureURL");
	
	// Counts
	int DefaultDays = 30;
	int DefaultTypeID = 1;
	
	%>

<div data-role="content" data-theme="d">
	<div id="homediv">
		<ul data-role="listview" data-inset="true" data-theme="d">
			<li >
				<img src="<%=UserPictureURL%>">
			
				<div style="float:left">
					<h2><%=SessionUserID + " - " +UserDisplayName %></h2>
					<p><%=SessionUserDistributorID+"-"+Utilities.getDistributorName(SessionUserDistributorID) %></p>
				</div>
			
				<div style="float:right">
					<div style="float:left">
						<h2><span id="temperature"></span></h2>
						<p><span id="condition" class="contents"></span>, <span id="humidity" class="contents"></span></p>
					</div>
							
					<div style="float:right;">
						<img style="margin-right: 10px;" src="" width="70" id="weatherimage"/>
					</div>
				</div>
			</li>
		</ul>
    </div>
        
	<span id="WorkflowDefaultTypeID" style="visibility:hidden"><%=DefaultTypeID %></span>
		<div data-role="popup" id="popup_change_password" data-overlay-theme="d" data-theme="c" data-dismissible="false" style="max-width:400px;min-width:400px;max-height:700px;" class="ui-corner-all">
		
			<a href="#HomePage" data-role="button" data-theme="b" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
		    <div data-role="header" data-theme="b" class="ui-corner-top">
		        <h1>Change Password</h1>
		    </div>
			
		    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
				<form id="ChangePasswordForm" action="#" data-ajax="false" method="POST" onSubmit="return ChangePasswordSubmit();">
					<input type="password" name="CurrentPassword" id="CurrentPassword" placeholder="Current Password">
					<input type="password" name="NewPassword" id="NewPassword" placeholder="New Password">
					<input type="password" name="ConfirmPassword" id="ConfirmPassword" placeholder="Confirm Password">
			        <button data-role="submit" data-inline="false" data-theme="b" id="ChangePasswordSubmitButton">Change</button>
		        </form>

		    </div>
		</div>        
</div><!-- /content -->
