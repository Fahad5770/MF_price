<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>

<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(94, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/GRNWarehouse.js"></script>
        
        
        
</head>

<body>

<div data-role="page" id="GRNProduction" data-url="GRN" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="GRN Warehouse" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<form id="GRNForm" data-ajax="false" action="GRN" onSubmit="return showSearchContent();">	
	<table style="width: 20%">
		<tr>
			<td>
				<input type="text" placeholder="Barcode" id="GRNBarcode" name="GRNBarcode" data-mini="true">
			</td>
			<td><input  type="submit" value="Show" data-inline="true" data-mini="true" data-icon="bars"></td>			
		</tr>
	</table>
	</form>
		
	</li>
    
    <li data-role="list-divider">GRN Warehouse</li>
    <li>	
		<div id="SearchContent" style="padding:10px"></div>
	</li>
	</ul>
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" class="ui-disabled" id="GRNSave" href="#" onClick="GRNSubmit();">Receive</a>
				</td>
                
			</tr>
		</table>
	</div>
	    	
    </div>
    

</div>

</body>
</html>
<%

bean.close();
%>