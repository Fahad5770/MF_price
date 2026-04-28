<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.common.User"%>
<%@page import="com.pbc.workflow.WorkflowChat"%>
<%@page import="com.pbc.workflow.Conversation"%>
<%@page import="com.pbc.workflow.ConversationMessage"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%

long RequestID = Long.parseLong(request.getParameter("RequestID"));

WorkflowChat chat = new WorkflowChat(RequestID);

String PageID = request.getParameter("PageID");

ArrayList<Conversation> conversations = chat.getConversations();
User stakeholders[] = chat.getStakeholders();

%><div data-role="collapsible-set" data-inset="true" data-collapsed="false">
	<%
	for (int i = 0; i < conversations.size(); i++){
		String to = conversations.get(i).TO_USER_DISPLAY_NAME;
		if (to == null){
			to = "Everyone";
		}
	%>
	<div data-role="collapsible" data-theme="c" data-content-theme="d" data-mini="true" <%if (i == 0){%>data-collapsed="false"<%}%>>
	    <h4><%=conversations.get(i).FROM_USER_DISPLAY_NAME + " > " + to %></h4>
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
		<%
		for (int j = 0; j < conversations.get(i).MESSAGES.size(); j++){
		%>
	    <li style="font-size: 10pt; font-weight: 400;"><b><%=conversations.get(i).MESSAGES.get(j).SENT_BY_USER_DISPLAY_NAME %></b><br><%=conversations.get(i).MESSAGES.get(j).MESSAGE.replace("\n","<br>") %><span class="ui-li-count" style="top: 12px;"><%= Utilities.getDisplayDateTimeFormat(conversations.get(i).MESSAGES.get(j).SENT_ON) %></span></li>
	    <%
		}
	    %>
	    <li style="font-size: 10pt;" data-role="list-divider">
	    <form id="WorkflowChatReplyForm" data-ajax="false" action="#" onSubmit="return WorkflowChatReply(<%=RequestID%>, <%=conversations.get(i).CONVERSATION_ID %>, this.WorkflowChatReplyMessage.value, '<%=PageID %>');">
				    	<input type="text" name="text-basic" id="WorkflowChatReplyMessage" name="WorkflowChatReplyMessage" placeholder="Reply" data-mini="true">
	    </form>
	    </li>
	</ul>				    
	</div>
	<%
	}
	%>
	
	<div data-role="collapsible" data-theme="c" data-content-theme="d" data-mini="true" <%if (conversations.size() == 0){%>data-collapsed="false"<%}%>>
	    <h4>Start a conversation</h4>
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	    <li style="font-size: 10pt;">
	    
			<form method="get" id="WorkflowChatSendNewForm">
				<textarea cols="40" rows="8" name="WorkflowChatSendNewMessage" id="WorkflowChatSendNewMessage" placeholder="Message"></textarea>
				
				<fieldset class="ui-grid-a">
				    <div class="ui-block-a" style="width: 80%">
						<select data-mini="true" name="WorkflowChatSendNewTo" id="WorkflowChatSendNewTo" data-corners="false">
							<option value="0">Send to: Everyone</option>
							<%
							for (int i = 0; i < stakeholders.length; i++){
							%>
							<option value="<%=stakeholders[i].USER_ID%>"><%=stakeholders[i].USER_DISPLAY_NAME%></option>
							<%
							}
							%>
							
						</select>						    
				    </div>
				    <div class="ui-block-b" style="padding-left: 0px; padding-top: 0px; width: 20%;">
				    	<a data-role="button" data-mini="true" data-theme="b" data-corners="false" id="WorkflowChatSendNewButton">Send</a>
				    </div>
				</fieldset>									
				<input type="hidden" name="WorkflowChatRequestID" value="<%=Long.parseLong(request.getParameter("RequestID"))%>">
				<input type="hidden" name="WorkflowChatLastMessage" id="WorkflowChatLastMessage" value="<%if (chat.RECENT_MESSAGE != null){%><%=chat.RECENT_MESSAGE.SENT_BY_USER_DISPLAY_NAME + ": " + Utilities.truncateStringToMax(chat.RECENT_MESSAGE.MESSAGE, 60)%><%}%>">
			</form>
	
	    
	    </li>
	</ul>				    
	</div>
</div>
<%
chat.close();
%>