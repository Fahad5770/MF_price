<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>

<style>

.formattedRow{
	border-bottom:1px solid #000;
	padding-top:5px;
	padding-bottom:5px;

}

</style>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

/*if(Utilities.isAuthorized(34, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}*/

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();



long SapCode = 0;
if( request.getParameter("SapCode") != null ){
	SapCode = Utilities.parseLong(request.getParameter("SapCode"));
	
	
	
}



Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body style="font-family:Helvetica,Arial,sans-serif">
		  <table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
			<thead>
					    
				<tr class="ui-bar-c">
				  	<th>User</th>
				  	<th>Browser ID</th>
				  	<th>Created On</th>
				  	<th>Browser Agent</th>
				  	<th>Status</th>
				  	<th>Action</th>
		 		 </tr>
		  </thead>
		  <tbody >
			<%
			ResultSet rs = s.executeQuery("select * from user_access_chrome_activation uaca join users u on uaca.user_id=u.id  where uaca.user_id="+SapCode+" order by uaca.created_on desc");
			while(rs.next())
			{			
			%>			
			<tr>
				<td><%=rs.getLong("user_id") %> - <%=rs.getString("display_name") %></td>
				<td><%=rs.getLong("browser_id") %></td>
				<td><%if(rs.getString("created_on")!=null){%><%=Utilities.getDisplayDateFormat(rs.getDate("created_on")) %><%} %></td>
				<td><%if(rs.getString("browser_agent")!=null){%><%=rs.getString("browser_agent") %><%} %></td>
				<td><%if(rs.getInt("is_active")==1){%>Active<%}else if (rs.getString("deactivated_on") != null){%>Deactivated<%}else{ %>Pending<%} %></td>
				<td><%if(rs.getString("deactivated_on") == null){%><%if(rs.getInt("is_active")==1){%><a href="#" onClick="SubmitProcess(1,<%=rs.getLong("browser_id") %>)">Deactivate</a><%}else{ %><a href="#" onClick="SubmitProcess(2,<%=rs.getLong("browser_id") %>)">Activate</a><%} %><%} %></td>
			</tr>
			<%
			}
			%>
		</tbody>
		  
		  </table>
		  

</body>
</html>
<%

	

s3.close();
s2.close();
s.close();
ds.dropConnection();
%>