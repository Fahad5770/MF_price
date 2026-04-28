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
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@include file="include/ValidateSession.jsp" %>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 182;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<html>

<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLOrderStatus.js"></script>
		
</head>

<body>

<div data-role="page" id="GLOrderStatusPage" data-url="GLOrderStatusPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Order Status" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="OrderStatusExecute" id="OrderStatusExecute" action="#" method="post" >
				
				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
				
				<input type="hidden" name="Status" id="Status" value="" >
				<input type="hidden" name="InvoiceAmountHidden" id="InvoiceAmountHidden" value="" >
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <li data-role="list-divider">Order Information</li>
				    <li>
				    
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="OrderNo" data-mini="true">Order No</label>
									<input type="text" name="OrderNo" id="OrderNo" value="" data-mini="true" onChange="GetSaleOrderStatus()" data-mini="true" maxlength="10" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="OrderDate" data-mini="true">Order Date</label>
									<input type="text" name="OrderDate" id="OrderDate" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="InvoiceNo" data-mini="true">Invoice No</label>
									<input type="text" name="InvoiceNo" id="InvoiceNo" value="" data-mini="true"  data-mini="true" readonly="readonly" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="InvoiceDate" data-mini="true">Invoice Date</label>
									<input type="text" name="InvoiceDate" id="InvoiceDate" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
							
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="InvoiceAmount" data-mini="true">Invoice Amount</label>
									<input type="text" name="InvoiceAmount" id="InvoiceAmount" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerName" data-mini="true">Customer</label>
									<input type="text" name="CustomerName" id="CustomerName" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
							
							
						</table>
				    	
					</li>
					
					
				    <li data-role="list-divider">SAP Status</li>
				    <li>
				    	<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="SAPPosted" data-mini="true">Posted</label>
									<input type="text" name="SAPPosted" id="SAPPosted" value="" data-mini="true" readonly="readonly" data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="SAPLifted" data-mini="true">Lifted</label>
									<input type="text" name="SAPLifted" id="SAPLifted" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
						</table>
				    </li>
				    
				    <li data-role="list-divider">Portal Status</li>
				    <li>
				    	<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="PortalPosted" data-mini="true">Posted</label>
									<input type="text" name="PortalPosted" id="PortalPosted" value="" data-mini="true" readonly="readonly" data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="PortalLifted" data-mini="true">Lifted</label>
									<input type="text" name="PortalLifted" id="PortalLifted" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="PortalPostedOn" data-mini="true">Posted On</label>
									<input type="text" name="PortalPostedOn" id="PortalPostedOn" value="" data-mini="true" readonly="readonly" data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="PortalLiftedOn" data-mini="true">Lifted On</label>
									<input type="text" name="PortalLiftedOn" id="PortalLiftedOn" value="" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
						</table>
				    </li>
				    
				
				</ul>
				</form>
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
	
    </div>
    
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackCashReceipt" name="CallBack" /> 
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>