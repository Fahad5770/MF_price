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

if(Utilities.isAuthorized(274, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/PrimarySalesPriceList.js?111111111=111111111"></script>
        
        
        
</head>

<body>

<div data-role="page" id="GatePass" data-url="GatePass" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Primary Sales Price List" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		<form id="PriceListForm" data-ajax="false" action="#PriceList" onSubmit="return AddPriceRows();">	
					<table style="width: 100%; margin-top:7px;">
						<tr>
							<td style="width:25%;">
								<input type="text" placeholder="Distributor ID" id="DistributorID" name="DistributorID" data-mini="true">
							</td>
							<td style="width:25%;">
								<input type="text" placeholder="Product Code" id="ProductCode" name="ProductCode" data-mini="true">
							</td>
							<td style="width:25%;">
								<input type="text" placeholder="Price" id="Price" name="Price" data-mini="true">
							</td>
							<td style="width:25%;"><input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" ></td>			
						</tr>
					</table>
					<table style="width: 100%; margin-top:7px;" id="PriceListAddRows">
						
					</table>
						
						
						
						
						
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true"  id="GatePassSave" href="#" onClick="PriceListSubmit();">Save</a>
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