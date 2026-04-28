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
int FeatureID = 160;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isPrintCase = false;
long PrintID = Utilities.parseLong(request.getParameter("PrintID"));
if(PrintID > 0){
	isPrintCase = true;
}

int Msg = Utilities.parseInt(request.getParameter("msg"));

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);

if( UserWarehouse == null || UserWarehouse.length > 1 ){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<html>


<head>

<script>
var msg = 0;
msg = <%=Msg%>;
</script>

		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCashReceipts.js?5=5"></script>
		
		  
		
		
</head>

<body>

<div data-role="page" id="GLCashReceipts" data-url="GLCashReceipts" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Cash Receipt" name="title"/>

    		<jsp:param value="<%=UserWarehouse[0].WAREHOUSE_NAME%>" name="HeaderValue"/>

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
	    			
	    			
	    				
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="DistributorID" data-mini="true">Customer ID</label>
								
								
								<input type="text" name="DistributorID" id="DistributorID" value="" data-mini="true" ondblclick="openDistributorSelectionPopup()" onChange="getDistributorName()" data-mini="true" >
							</td>
							<td style="width: 80%; border: 0px; padding-left: 2px; ">
								<label for="DistributorName" data-mini="true">Customer Name</label>
								<input type="text" name="DistributorName" id="DistributorName" value="" data-mini="true" readonly="readonly" data-mini="true" >
								<input type="hidden" name="isDistributorValid" id="isDistributorValid" value="false"  >
							</td>
						</tr>
						<tr>
							<td style="width: 20%; border: 0px;">
								<label for="Type" data-mini="true">Type</label>
								<select name="Type" id="Type" data-mini="true" >
									
								<%
									ResultSet rs = s.executeQuery("SELECT * FROM gl_cash_receipts_types");
									while( rs.next() ){
								%>
									<option value="<%=rs.getString("id")%>" ><%=rs.getString("label")%></option>
								<%
									}
								%>
								</select>
							</td>
						
							<td style="border: 0px;" colspan="2" >
								<label for="Narration" data-mini="true">Narration</label>
								<input name="Narration" id="Narration" value="" data-mini="true" maxlength="1000" placeholder="Maximum 1000 characters" >
							</td>
						</tr>
					</table>
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <li data-role="list-divider">Instruments</li>
				    <li>
				    
				    	<form id="GLCashReceiptFormAddRow" data-ajax="false" action="#GLCashReceiptFormAddRow" onSubmit="return AddRow();">		
    
						    <table style="width: 100%">
								<tr>
									
									<td valign="top" style="width: 30%">
										<select name="AddRowInstrumentType" id="AddRowInstrumentType" data-mini="true" onchange="setInstrumentAttributes()" >
											<option value="" >Instrument</option>
											<%
											ResultSet rs1 = s.executeQuery("SELECT * FROM gl_cash_instruments order by id");
											while( rs1.next() ){
												%>
												<option value="<%=rs1.getString("id")%>"><%=rs1.getString("label")%></option>
												<%
											}
											%>
										</select>
										
										
										
									</td>
									<td valign="top" style="width: 15%">
										<input  type="text" placeholder="Amount" id="AddRowAmount" name="AddRowAmount" data-mini="true">
									</td>
									<td valign="top" style="width: 15%">
										<input  type="text" placeholder="Reference#" id="AddRowReferenceNo" name="AddRowReferenceNo" data-mini="true">
										<input type="hidden" name="CaptureReferenceNo" id="CaptureReferenceNo" value="true" >
									</td>
									<td valign="top" style="width: 20%">
										<input type="date" id="AddRowDate" name="AddRowDate" data-mini="true">
										<input type="hidden" name="CaptureDate" id="CaptureDate" value="true" >
									</td>
									
									<td valign="top" style="width: 20%">
										<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
										
									</td>
									
								</tr>
							</table>
						</form>
				    
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
								
								<tbody id="GLCashReceiptTableBody">
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
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLCashReceipts.jsp'" >Reset</button>
						<% if( isPrintCase ){ %>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:doPrint(<%=PrintID%>)" >Print Receipt</button>
						<% } %>
					</td>
	                <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ViewAllButton" >Print</a>
					</td>
				</tr>
			</table>
		</div>
    </div>
    
    <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width: 500px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>View All</h1>
	    </div>
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <ul data-role="listview" data-filter="true" data-filter-placeholder="Search ..." data-inset="true">
			    
			    
			    <%
			    
				ResultSet rs3 = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name FROM gl_cash_receipts where created_on_date=curdate() and warehouse_id = "+UserWarehouse[0].WAREHOUSE_ID+" and receipt_type in (1,2) order by created_on desc ");
				while( rs3.next() ){
					%>
					 <li><a href="#" onclick="javascript:doPrint(<%=rs3.getString("id")%>)"><%=rs3.getString("customer_name")%></a></li> 
					<%
				}
				%>
			    
			    
			</ul>
	    </div>
	</div>
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackCashReceipt" name="CallBack" /> 
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    
    <div data-role="popup" id="MsgPopupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="width:420px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>Cash Receipt</h1>
	    </div>
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" style="text-align:center">
	        <h3 class="ui-title">Receipt Saved</p>
	        <a href="#" data-role="button" data-inline="true" data-rel="back" data-theme="c">OK</a>
	        <!-- <a href="#" data-role="button" data-inline="true" data-rel="back" data-transition="flow" data-theme="b">Delete</a> -->
	    </div>
	</div>

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>