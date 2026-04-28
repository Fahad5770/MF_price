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

if(Utilities.isAuthorized(215, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/ProductPromotionsDeactivation.js"></script>
        
        
        
</head>

<body>

<div data-role="page" id="ProductPromotionsDeactivationPage" data-url="ProductPromotionsDeactivationPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Product Prmotions Deactivation" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
		<div class="ui-grid-a" >
		    <div class="ui-block-a" style="width:75%">
		    	<div class="ui-bar " style="min-height:60px">
		    		<form id="ProductPromotionsDeactivationForm" data-ajax="false" action="#ProductPromotionsDeactivationForm" onSubmit="return false;">	
						<table style="width: 100%; margin-top:7px;">
							<tr>
								<td style="width: 30%">
									<input type="text" placeholder="Request ID" id="RequestID" name="RequestID" data-mini="true" onchange="GetPromotionLabel()">
								</td>
								<td style="width: 60%">
									<input type="text" placeholder="Description" id="Description" name="Description" data-mini="true" readonly="readonly">
								</td>
								<td style="width: 10%">
									<input type="button" id="Submit" value="Deactivate" data-inline="true" data-mini="true" data-icon="bars" onclick="FormSubmit()"  >
								</td>
							</tr>
						</table>
					</form>
		    	</div>
		    </div>
		</div><!-- /grid-a -->
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<button data-icon="check" data-theme="b" data-inline="true" id="Reset" onClick="javascript:window.location='ProductPromotionsDeactivation.jsp'" >Reset</button>
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