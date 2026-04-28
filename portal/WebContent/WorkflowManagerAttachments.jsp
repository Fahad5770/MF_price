<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowAttachment"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
Workflow workflow = new Workflow();
WorkflowAttachment items[] = workflow.getAttachments(Long.parseLong(request.getParameter("RequestID")));
%>
<ul data-role="listview" data-inset="false" data-divider-theme="c" data-icon="arrow-d">
    <li data-role="list-divider" style="font-size: 8pt;">&nbsp;Attached Documents<span class="ui-li-count"><%=items.length %></span></li>
    <%
    if (items.length == 0){
    %>
    <li style="font-size: 8pt;">There are no attached documents.</li>
    <%
    }
    for (int i = 0; i < items.length; i++){
    	if (items[i].DESCRIPTION.length() > 100){
    		items[i].DESCRIPTION = items[i].DESCRIPTION.substring(0, 99);
    	}
    %>
	<li style="padding-left: 4px;"><a href="workflow/WorkflowFileDownload?file=<%=items[i].FILENAME%>" style="font-size: 10pt;white-space: normal;" data-ajax="false" target="_blank"><%=items[i].FILENAME.substring(items[i].FILENAME.indexOf("_")+1, items[i].FILENAME.length()) %><br>
	<%if (items[i].DESCRIPTION.length() > 1){ %><span style="font-weight: 400;"><%= items[i].DESCRIPTION %></span><br><%} %>
	<span style="font-weight: 400;">by <%= items[i].ATTACHED_BY_DISPLAY_NAME%> on <%= Utilities.getDisplayDateTimeFormat(items[i].ATTACHED_ON) %></span></a></li>
	<%
    }
	%>
</ul>									    
<%
workflow.close();
%>
</body>
</html>