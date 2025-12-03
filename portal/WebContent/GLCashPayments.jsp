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
int FeatureID = 164;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);

if( UserWarehouse == null || UserWarehouse.length > 1 ){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCashPayments.js?88=88"></script>
		
		  
		
		
</head>

<body>

<div data-role="page" id="GLCashPayment" data-url="GLCashPayment" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Cash Payment" name="title"/>

    		<jsp:param value="<%=UserWarehouse[0].WAREHOUSE_NAME%>" name="HeaderValue"/>

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:100%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
	    			
	    			
	    				
					<table border=0 style="font-size:13px; font-weight: 400; width:75%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="AccountIDSelect" data-mini="true">Payment to</label>
								
								<input type="text" name="AccountIDSelect" id="AccountIDSelect" data-mini="true" value="" placeholder="Select Account" readonly="readonly" onclick="OpenAccountPopup()" >
								<input type="hidden" name="AccountID" id="AccountID" value="" >
								
							</td>
							 

							<td style="width: 80%; border: 0px; padding-left: 2px; ">
								<label for="Narration" data-mini="true">Narration</label>
								<input name="Narration" id="Narration" value="" data-mini="true" maxlength="1000" placeholder="Maximum 1000 characters" >
							</td>
						</tr>
					</table>
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <li data-role="list-divider">Instruments</li>
				    <li>
				    <table border="0" style="width: 100%">
				    	<tr>
				    		<td valign="top" style="width: 75%"> <!-- 1st column -->
				    	<form id="GLCashReceiptFormAddRow" data-ajax="false" action="#GLCashReceiptFormAddRow" onSubmit="return AddRow();">		
    
						    <table border="0" style="width: 100%">
								<tr>
									
									<td valign="top" style="width: 30%">
										<select name="AddRowInstrumentType" id="AddRowInstrumentType" data-mini="true" onchange="setInstrumentAttributes()" >
											<option value="" >Instrument</option>
											<%
											ResultSet rs1 = s.executeQuery("SELECT * FROM gl_cash_instruments where can_bounce = 0 and is_visible_cash_payment = 1 order by id");
											while( rs1.next() ){
												%>
												<option value="<%=rs1.getString("id")%>" <% if( rs1.getString("id").equals("1") ){ out.print("selected"); } %> ><%=rs1.getString("label")%></option>
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
				    
						    	<form id="GLCashPaymentForm" data-ajax="false" action="#GLCashPayment" onSubmit="return showSearchContent();">
						    	
						    		<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
						    		<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
			    					<input type="hidden" name="WarehouseID" id="WarehouseID" value="<%=UserWarehouse[0].WAREHOUSE_ID%>"/>
			    					
			    					<input type="hidden" name="AccountIDSubmitForm" id="AccountIDSubmitForm" value=""/>
			    					<input type="hidden" name="NarrationSubmitForm" id="NarrationSubmitForm" value=""/>
						    	
									<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:95%">
										
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
						 	</td>
						 	<td valign="top" style="width: 25%"> <!-- 2nd column -->
						 		<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
	    
				    				<li data-role="list-divider">Pending Instruments</li>
				    				
				    				<li>
				    					
				    					<div style="margin-top: 10px">
									     <ul data-role="listview" data-filter="true" data-filter-placeholder="Search Instruments..." data-inset="true">
				    					<%
				    						
				    						ResultSet rs = s.executeQuery("SELECT gci.label, gcri.amount, gcri.instrument_id, gci.category_id, gcri.instrument_no, gcri.instrument_date, gcr.created_on_date, gcri.serial_no FROM gl_cash_receipts_instruments gcri, gl_cash_receipts gcr, gl_cash_instruments gci where gcri.instrument_id=gci.id and gcri.id=gcr.id and gci.can_bounce=1 and gcri.status_id in (1,4) and gcr.warehouse_id = "+UserWarehouse[0].WAREHOUSE_ID+" order by gcri.instrument_date ");
				    						while( rs.next() ){
				    							
			    							%>
			    								<li>
			    									<a href="#" onclick="AutoAddRowCheque('<%=rs.getString("label")%>', <%=rs.getString("amount")%>, <%=rs.getString("instrument_id")%>, <%=rs.getString("category_id")%>, '<%=rs.getString("instrument_no")%>', '<%=rs.getString("instrument_date")%>', '<%=rs.getString("serial_no")%>')" >
			    										<table style="width: 100%; font-size: 12px">
			    											<tr>
			    												<td style="width: 30%"><%=rs.getString("label")%></td>
			    												<td style="width: 30%"><%=rs.getString("instrument_no")%></td>
			    												<td style="width: 20%"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs.getDouble("amount"))%></td>
			    												<td style="width: 20%"><span class="ui-li-count"><%=Utilities.getDisplayDateFormat(rs.getDate("instrument_date"))%></span></td>
			    											</tr>
			    										</table>
			    										
			    									</a>
			    								</li>
			    								
			    								
			    								<!-- <tr onclick="AutoAddRowCheque('<%//=rs.getString("label")%>', <%//=rs.getString("amount")%>, <%//=rs.getString("instrument_id")%>, <%//=rs.getString("category_id")%>, '<%//=rs.getString("instrument_no")%>', '<%//=rs.getString("instrument_date")%>')" style="cursor: pointer">
			    									<td><%//=rs.getString("label")%></td>
			    									<td><%//=rs.getString("instrument_no")%></td>
			    									<td style="text-align: right"><%//=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs.getDouble("amount"))%></td>
			    									<td><%//=Utilities.getDisplayDateFormat(rs.getDate("instrument_date"))%></td>
			    								</tr> -->
			    							<%
				    							
				    						}
				    					%>
				    					</ul>
				    					</div>
				    					
				    				</li>
				    				
				    			</ul>
						 	</td>
						 </tr>
					</table>
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
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" class="ui-disabled" >Submit</a>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLCashPayments.jsp'" >Reset</button>
					</td>
	                
				</tr>
			</table>
		</div>
    </div>
    
    <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width: 400px; max-width:400px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>Accounts List</h1>
	    </div>
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <ul data-role="listview" data-filter="true" data-filter-placeholder="Search Accounts ..." data-inset="true">
			    
			    
			    <%
				ResultSet rs3 = s.executeQuery("SELECT * FROM gl_accounts where category_id = 3");
				while( rs3.next() ){
					%>
					<li><a href="#" onclick="CloseAccountPopup(<%=rs3.getString("id")%>, '<%=rs3.getString("label")%>')"><%=rs3.getString("label")%></a></li>
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
    

</div>

</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();
%>