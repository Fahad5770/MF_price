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
int FeatureID = 165;
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

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLChequeReturn.js?51=51"></script>
		
		  
		
		
</head>

<body>

<div data-role="page" id="GLCashPayment" data-url="GLCashPayment" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Cheque Return" name="title"/>

    		<jsp:param value="<%=UserWarehouse[0].WAREHOUSE_NAME%>" name="HeaderValue"/>

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:100%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
	    			
	    			
	    				
					<table border=0 style="font-size:13px; font-weight: 400; width:75%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="width: 20%; border: 0px; padding-right: 2px">
								<label for="DistributorID" data-mini="true">Customer ID</label>
								
								
								<input type="text" name="DistributorID" id="DistributorID" value="" data-mini="true" ondblclick="openDistributorSelectionPopup()" onChange="getDistributorName()" data-mini="true" >
							</td>
							<td style="width: 40%; border: 0px; padding-left: 2px; ">
								<label for="DistributorName" data-mini="true">Customer Name</label>
								<input type="text" name="DistributorName" id="DistributorName" value="" data-mini="true" readonly="readonly" data-mini="true" >
								<input type="hidden" name="isDistributorValid" id="isDistributorValid" value="false"  >
							</td>
							<td style="width: 40%; border: 0px ">
								
								<label for="BankAccountID">Cheque Deposited In</label>
								<select name="BankAccountID" id="BankAccountID" data-mini="true"  >
									<option value="">Select</option>
									<%
									ResultSet rs2 = s.executeQuery("SELECT id, label FROM gl_accounts where category_id=3");
									while( rs2.next() ){
										%>
											<option value="<%=rs2.getString("id")%>" ><%=rs2.getString("label")%></option>
										<%
									}
									%>
								</select>
								
							</td>
						</tr>
						
						<tr>

							<td style="border: 0px;" colspan="3" >
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
    
						   <!--  <table style="width: 100%">
								<tr>
									
									<td valign="top" style="width: 30%">
										<select name="AddRowInstrumentType" id="AddRowInstrumentType" data-mini="true" onchange="setInstrumentAttributes()" >
											
											<%
											//ResultSet rs1 = s.executeQuery("SELECT * FROM gl_cash_instruments where id=2");
											//while( rs1.next() ){
												%>
												<option value="<%//=rs1.getString("id")%>"><%//=rs1.getString("label")%></option>
												<%
											//}
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
							 -->
						</form>
				    
				    	<form id="GLCashPaymentForm" data-ajax="false" action="#GLCashPayment" onSubmit="return showSearchContent();">
				    	
				    		<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
				    		<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
	    					<input type="hidden" name="WarehouseID" id="WarehouseID" value="<%=UserWarehouse[0].WAREHOUSE_ID%>"/>
	    					
	    					<input type="hidden" name="DistributorIDSubmitForm" id="DistributorIDSubmitForm" value=""/>
	    					<input type="hidden" name="BankAccountIDSubmitForm" id="BankAccountIDSubmitForm" value=""/>
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
						 </td>
						 <td valign="top" style="width: 25%"> <!-- 2nd column -->
						 		<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
	    
				    				<li data-role="list-divider">Bounced Cheques</li>
				    				
				    				<li>
				    					<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
										
										<thead>
										    <tr class="ui-bar-c">
												<th style="width: 50%">Reference#</th>
												<th style="width: 25%">Amount</th>
												<th style="width: 25%">Date</th>
										    </tr>
									    </thead>
				    					<%
				    						
				    					
				    						ResultSet rs = s.executeQuery("SELECT gci.label, gcri.amount, gcri.instrument_id, gci.category_id, gcri.instrument_no, gcri.instrument_date, gcr.created_on_date, gcri.serial_no, gcr.customer_id FROM gl_cash_receipts_instruments gcri, gl_cash_receipts gcr, gl_cash_instruments gci where gcri.instrument_id=gci.id and gcri.id=gcr.id and gcri.instrument_id in (2,3,4) and gcri.status_id=4 order by gcri.instrument_date ");
				    						while( rs.next() ){
				    							
			    							%>
			    								<tr onclick="AutoAddRowCheque('<%=rs.getString("label")%>', <%=rs.getString("amount")%>, <%=rs.getString("instrument_id")%>, <%=rs.getString("category_id")%>, '<%=rs.getString("instrument_no")%>', '<%=rs.getString("instrument_date")%>', '<%=rs.getString("serial_no")%>', <%=rs.getString("customer_id")%>)" style="cursor: pointer">
			    									<td><%=rs.getString("instrument_no")%></td>
			    									<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs.getDouble("amount"))%></td>
			    									<td><%=Utilities.getDisplayDateFormat(rs.getDate("instrument_date"))%></td>
			    								</tr>
			    							<%
				    							
				    						}
				    					%>
				    						</table>
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
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLChequeReturn.jsp'" >Reset</button>
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