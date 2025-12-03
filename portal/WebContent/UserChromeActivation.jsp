<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>




<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(UserAccess.isAuthorized(150, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/UserChromeActivation.js"></script>
        
        
        
</head>

<body>

<div data-role="page" id="UserChromeActivation" data-url="UserChromeActivation" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Chrome User Activation" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<form id="GatePassForm" data-ajax="false" action="#UserChromeActivation" onSubmit="return showSearchContent();">	
		<table style="width: 100%; margin-top:7px;">
			<tr>
				<td style="width:30%">
					<input type="text" placeholder="Sap Code" id="SapCode" name="SapCode" data-mini="true">
				</td>
				<td><input  type="submit" value="Show" data-inline="true" data-mini="true"></td>			
			</tr>
		</table>
    </form>
	
	
	
	<div id="SearchContent" style="padding:10px; "></div>	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	
    </div>
    

</div>

</body>
</html>
