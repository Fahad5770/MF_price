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

if(Utilities.isAuthorized(35, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/GatePass.js"></script>
        
        
        
</head>

<body>

<div data-role="page" id="GatePass" data-url="GatePass" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Gatepass" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		<form id="GatePassForm" data-ajax="false" action="#GatePass" onSubmit="return showSearchContent();">	
					<table style="width: 100%; margin-top:7px;">
						<tr>
							<td>
								<input type="text" placeholder="Barcode" id="GatePassBarcode" name="GatePassBarcode" data-mini="true">
							</td>
							<td><input  type="submit" value="Show" data-inline="true" data-mini="true" data-icon="bars"></td>			
						</tr>
					</table>
				</form>
					<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
		
	    
			    <li data-role="list-divider">Delivery Note</li>
			    <li>	
				<div id="SearchContent" style="padding:10px; "></div>	
					
				</li>
				
				</ul>
				
	    	</div>
	    </div>
	    <div class="ui-block-b" style="width:25%">
	    	<div class="ui-bar" style="min-height:60px; ">
	    	<div id="DeliveredGatePassQueue"></div>
	    	</div>
	    </div>
	</div><!-- /grid-a -->
	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" class="ui-disabled" id="GatePassSave" href="#" onClick="GatePassSubmit();">Deliver</a>
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