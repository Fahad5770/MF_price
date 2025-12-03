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
int FeatureID = 184;
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

		<script src="js/GLUnpostCashReceipts.js?87=87"></script>

</head>

<body>

<div data-role="page" id="GLUnpostCashReceiptsPage" data-url="GLUnpostCashReceiptsPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Unpost Cash Receipt" name="title"/>
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
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="Type" data-mini="true">Type</label>
								<input type="text" data-mini="true" name="Type" id="Type" readonly="readonly"  >
								
							</td>
						
							<td style="width: 80%; border: 0px; padding-left: 2px; " colspan="2" >
								<label for="Narration" data-mini="true">Narration</label>
								<input name="Narration" id="Narration" value="" data-mini="true" maxlength="1000" placeholder="Maximum 1000 characters" readonly="readonly" >							</td>
						</tr>
					</table>
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <li data-role="list-divider">Instruments</li>
				    <li>
				    
				    	<form id="GLCashReceiptsForm" data-ajax="false" action="#GLCashReceipts" onSubmit="return showSearchContent();">
				    	
				    		<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
				    		<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
	    					<input type="hidden" name="WarehouseID" id="WarehouseID" value="<%=UserWarehouse[0].WAREHOUSE_ID%>"/>
	    					
	    					<input type="hidden" name="DistributorIDSubmitForm" id="DistributorIDSubmitForm" value=""/>
	    					<input type="hidden" name="ReceiptTypeSubmitForm" id="ReceiptTypeSubmitForm" value=""/>
	    					<input type="hidden" name="NarrationSubmitForm" id="NarrationSubmitForm" value=""/>
				    	
							<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:70%">
								
								<thead>
								    <tr class="ui-bar-c">
										<th style="width: 30%">Instrument</th>
										<th style="width: 20%">Amount</th>
										<th style="width: 20%">Reference#</th>				
										<th style="width: 25%">Date</th>
										<th style="width: 5%"></th>
								    </tr>
							  </thead>
								
								<tbody id="GLUnpostCashReceiptTableBody">
									<tr id="NoProductRow">
										<td colspan="54" style="margin: 1px; padding: 0px;">
											<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No instrument added.</div>
										</td>
									</tr>
								</tbody>
							
								
									<tr>
										<td>Total</td>
										<td id="testcont" style="width: 15%">
											<input type="text" name="InstrumentAmountTotal" id="InstrumentAmountTotal" value="" data-mini="true" readonly="readonly" style="text-align: right" >
											<input type="hidden" name="InstrumentAmountTotalHidden" id="InstrumentAmountTotalHidden" value="" >
											
										</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										
									</tr>
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
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" class="ui-disabled" >Submit</a>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLUnpostCashReceipts.jsp'" >Reset</button>
						
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