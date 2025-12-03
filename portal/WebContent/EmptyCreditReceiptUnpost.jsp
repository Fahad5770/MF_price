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
int FeatureID = 234;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isPrintCase = false;
long PrintID = Utilities.parseLong(request.getParameter("PrintID"));
if(PrintID > 0){
	isPrintCase = true;
}

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);

if( UserWarehouse == null || UserWarehouse.length > 1 ){
	//response.sendRedirect("AccessDenied.jsp");
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

		<script src="js/EmptyCreditReceiptUnpost.js?8711=8711"></script>

</head>

<body>

<div data-role="page" id="GLUnpostCashReceiptsPage" data-url="GLUnpostCashReceiptsPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Unpost Empty Receipt" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    			
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="ReceiptID" data-mini="true">Receipt ID</label>
								<input type="text" name="ReceiptID" id="ReceiptID" value="" data-mini="true" onChange="getEditInfoJson()" data-mini="true" >
							</td>
							<td style="width: 80%; border: 0px; padding-left: 2px">
								<label for="Reason" data-mini="true">Reason</label>
								<input type="text" name="Reason" id="Reason" value="" data-mini="true" >
							</td>
						</tr>
						</table>
						<hr>
						<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="DistributorID" data-mini="true">Customer ID</label>
								
								
								<input type="text" name="DistributorID" id="DistributorID" value="" data-mini="true" ondblclick="openDistributorSelectionPopup()" onChange="getDistributorName()" data-mini="true" readonly="readonly" >
							</td>
							<td style="width: 80%; border: 0px; padding-left: 2px; ">
								<label for="DistributorName" data-mini="true">Customer Name</label>
								<input type="text" name="DistributorName" id="DistributorName" value="" data-mini="true" readonly="readonly" data-mini="true" >
								<input type="hidden" name="isDistributorValid" id="isDistributorValid" value="false"  >
							</td>
						</tr>
						
					</table>
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <li data-role="list-divider">Details</li>
				    <li>
				    
				    	<form id="GLCashReceiptsForm" data-ajax="false" action="#GLCashReceipts" onSubmit="return showSearchContent();">
				    	
				    		
				    	
							<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:70%">
								
								<thead>
								    <tr class="ui-bar-c">
										<th data-priority="1">Product Code</th>
										<th data-priority="1">Package</th>
										<th data-priority="1">Brand</th>				
										<th data-priority="1" >Raw Cases</th>
										<th data-priority="1" >Bottles</th>
										<th data-priority="1" >Receipt Type</th>
										<th data-priority="1">&nbsp;</th>
								    </tr>
							  </thead>
								
								<tbody id="GLUnpostCashReceiptTableBody">
									<tr id="NoProductRow">
										<td colspan="54" style="margin: 1px; padding: 0px;">
											<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No details available.</div>
										</td>
									</tr>
								</tbody>
							
								
									
							</table>
						 </form>
					</li>
				
				</ul>
				
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
	
	
	<form name="GLCashReceiptsPrintExecute" id="GLCashReceiptsPrintExecute" action="GLCashReceiptPrinting.jsp" method="post" data-ajax="false" target="_blank" >
		<input type="hidden" name="GLCashReceiptID" id="GLCashReceiptID" value="" >
	</form>
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" class="ui-disabled" >Unpost</a>
						
						
					</td>
	              
				</tr>
			</table>
		</div>
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