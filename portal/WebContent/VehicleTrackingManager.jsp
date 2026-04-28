<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID=98;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<title>PBC Enterprise Portal</title>
	<script>
	
	
	$( document ).delegate("#VehicleTrackerPage", "pageinit", function() {
		
		$.mobile.loading( 'show');
		
		$.ajax({ 
		    url: "/track/Track",
		    data: $("#TrackerLogonForm").serialize(),
		    type: "POST",
		    success: function( json ) {
				$.mobile.loading( 'hide');
				location = '/track/Track?page=map.device';
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	$.mobile.loading( 'hide');
		    }
		});
		
	});
	</script>
	
</head>
<body>

<div data-role="page" id="VehicleTrackerPage" data-url="VehicleTrackerPage" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Vehicle Tracker" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
	
		<br>
		<div style="width: 100%; text-align: center; margin-top: 200px;">
			<h4>Connecting GPS Server .....</h4>
		</div>
		
		<form name="TrackerLogonForm" id="TrackerLogonForm" method="POST">
			<input type="hidden" name="account" value="pbc">
			<input type="hidden" name="user" value="pbc">
			<input type="hidden" name="password" value="smooking">
			<input type="hidden" name="submit" value="Login">
		</form>
						
    </div><!-- /content -->

</div>
</body>
</html>
