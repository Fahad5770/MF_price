<%@page import="com.pbc.workflow.ConversationMessage"%>
<%@page import="com.pbc.workflow.WorkflowChat"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%
	if ((String)session.getAttribute("UserID") == null){
		response.sendRedirect("index.jsp");
	}
	
	long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
	
	String UserDisplayName = (String)session.getAttribute("UserDisplayName");
	String UserPictureURL = (String)session.getAttribute("UserPictureURL");
	
	
	// Counts
	int DefaultDays = 30;
	int DefaultTypeID = 1;
	
	Workflow workflow = new Workflow();
	int CountActionAwaited = workflow.getActionAwaited(SessionUserID).length;
	int CountInProcess = workflow.getInProcess(SessionUserID, DefaultDays).length;
	int CountRequested = workflow.getRequested(SessionUserID, DefaultDays).length;
	
	if (CountActionAwaited == 0){
		DefaultTypeID = 2;
	}
	if (CountInProcess == 0){
		DefaultTypeID = 3;
	}
	if (CountRequested == 0){
		DefaultTypeID = 1;
	}
	workflow.close();
	
	WorkflowChat WChat = new WorkflowChat();
	ArrayList<ConversationMessage> LastMessages = WChat.getLastMessages(SessionUserID);
	WChat.close();
	
	
	%>

<div data-role="content" data-theme="d">
        
		<div id="homediv">
		
			<fieldset class="ui-grid-a">
			    <div class="ui-block-a" style="width: 25%; padding-right: 5px; margin-top: 0px;">

					<ul data-role="listview" data-inset="true" data-theme="d">
					
					    <li>
					    		<img src="<%=UserPictureURL%>">
						        <h2><%=UserDisplayName %></h2>
						        <p><%=session.getAttribute("UserDesignation") %></p>
					    </li>
					    
<li data-icon="arrow-r" data-theme="d" data-role="list-divider">Quick Links</li>
						<%
						Datasource ds = new Datasource();
						ds.createConnection();
						
						
						Statement s  = ds.createStatement();
						String FeatureIDs = "-1";
						/*
						ResultSet rs = s.executeQuery("SELECT feature_id FROM "+ds.logDatabaseName()+".log_user_access where user_id = "+SessionUserID+" and is_authorized = 1 group by feature_id order by created_on desc limit 5");
						while(rs.next()){
							FeatureIDs += ","+rs.getString(1);
						}
						*/
		            	
		            	ResultSet rs2 = s.executeQuery("select feature_name, url, ajax_call from features f where feature_id in ("+FeatureIDs+") and active = 1 and visible = 1 and feature_id in (select feature_id from user_access where user_id = "+SessionUserID+") limit 4");
		            	while(rs2.next()){
		            		%>
		            		<li data-icon="arrow-r"><a style="font-size: 10pt; font-weight: normal;" href="<%=rs2.getString(2) %>" data-transition="flow" <%if (rs2.getInt(3) == 0){out.print("data-ajax=\"false\"");} %>><%=rs2.getString(1) %></a></li>
		            		<%
		            	}
		            	%>					
			            <li data-icon="arrow-r" data-theme="d" data-role="list-divider">Workflow</li>
			            <li><a class="workflow_left_nav_item" onClick="showWorkflowStatus(1);">Action Awaited<span class="ui-li-count"><%=CountActionAwaited %></span></a></li>
			            <li><a class="workflow_left_nav_item" onClick="showWorkflowStatus(2);">In Process<span class="ui-li-count"><%=CountInProcess %></span></a></li>
			            <li><a class="workflow_left_nav_item" onClick="showWorkflowStatus(3);">Requested<span class="ui-li-count"><%=CountRequested %></span></a></li>
			            <li><a class="workflow_left_nav_item" onClick="showWorkflowStatus(4);">Completed</a></li>
			            <li><a class="workflow_left_nav_item" onClick="showWorkflowStatus(5);">Declined</span></a></li>
			            <li data-icon="arrow-r" data-theme="d" data-role="list-divider">Recent Messages</li>
			            <%
			            boolean isNoMessage = true;
			            for (ConversationMessage msg: LastMessages){
			            	isNoMessage = false;
			            %>
					    <li>
						    <a style="margin-left: 10px; padding: 0px;" href="WorkflowManager.jsp?requestID=<%=msg.REQUEST_ID %>&processID=1" data-transition="flow">
						    <h2><span style="font-weight: 400; font-size: 13px;white-space:normal;"><%=msg.MESSAGE %></span></h2>
						    <p><span style='font-weight: 400; font-size: 13px;'><%=msg.SENT_BY_USER_DISPLAY_NAME %></span></p>
						    </a>
					    </li>
					    <%
			            }
			            if (isNoMessage == true){
			            	%>
					    <li>
						    <span style="font-weight: 400; font-size: 13px;white-space:normal;">There are no messages.</span>
					    </li>			            	
			            	<%
			            }
			            
			    		s.close();
			    		ds.dropConnection();

					    %>
					    <!-- 
			            <li data-icon="arrow-r" data-theme="d" data-role="list-divider">Faisalabad Today</li>
					    <li>
					    		<img style="margin-right: 10px;" src="" width="70" id="weatherimage"/>
						        <h2><span id="temperature"></span></h2>
						        <p><span id="condition" class="contents"></span>, <span id="humidity" class="contents"></span></p>
					    </li> -->
						
			        </ul>			    
			    
			    </div>
			    <div class="ui-block-b" style="width: 75%;">


			    		<div style="height: 25px;"></div>
						<span id="WorkflowStatusContent">
							<img src="images/loader.gif">
						</span>

			    </div>
			</fieldset>
			<br>
			<form>
			    <label for="slider-2">Show documents that are at most</label>
			    <input type="range" name="HistorySlider" id="HistorySlider" data-highlight="true" min="0" max="200" value="30" >
			    <label for="slider-2">days old</label>
			</form>
			<br>
			
        </div>
        
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
        
        <span id="WorkflowDefaultTypeID" style="visibility:hidden"><%=DefaultTypeID %></span>
      
        
    </div><!-- /content -->