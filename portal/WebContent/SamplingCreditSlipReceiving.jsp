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


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(155, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        
        <script src="js/SamplingCreditSlipReceiving.js?1=1"></script>
        
        
</head>

<body>

<div data-role="page" id="SamplingCreditSlipReceiving" data-url="SamplingCreditSlipReceiving" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Sampling Credit Slip Receiving" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		<form id="SamplingCreditSlipReceivingForm" data-ajax="false" action="#SamplingCreditSlipReceiving" onSubmit="return showSearchContent();">	
					<table style="width: 100%; margin-top:7px;">
						<tr>
							<td>
								<input type="text" placeholder="Barcode" id="SamplingCreditSlipReceivingBarcode" name="SamplingCreditSlipReceivingBarcode" data-mini="true">
							</td>
							<td><input  type="submit" value="Show" data-inline="true" data-mini="true" data-icon="bars"></td>			
						</tr>
					</table>
				</form>
					<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
		
	    
			    <li data-role="list-divider">Sampling Credit Slip Receiving</li>
			    <li>	
				<div id="SearchContent" style="padding:10px; "></div>
					
				</li>
				
				</ul>
				
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" class="ui-disabled" id="SamplingCreditSlipReceivingSave" href="#" onClick="SamplingCreditSlipReceivingSubmit();">Receive</a>
				</td>
                
			</tr>
		</table>
	</div>
	    	
    </div>
    

</div>

</body>
</html>
