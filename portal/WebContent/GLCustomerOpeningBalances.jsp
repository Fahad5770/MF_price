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
int FeatureID = 175;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isPrintCase = false;
long PrintID = Utilities.parseLong(request.getParameter("PrintID"));
if(PrintID > 0){
	isPrintCase = true;
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

		<script src="js/GLCustomerOpeningBalances.js"></script>
		
		  
		
		
</head>

<body>

<div data-role="page" id="GLCustomerOpeningBalancesPage" data-url="GLCustomerOpeningBalancesPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Customer Opening Balances" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
				    <li>
				    
				    	<form id="AddRowForm" data-ajax="false" action="#AddRowForm" onSubmit="return AddRow();">
						    <table style="width: 100%">
								<tr>
									<td style="width: 20%; border: 0px; padding-right: 2px">
										<input type="text" name="DistributorID" id="DistributorID" value="" data-mini="true" ondblclick="openDistributorSelectionPopup()" onChange="getDistributorName()" data-mini="true" placeholder="Customer ID" >
									</td>
									<td style="width: 40%; border: 0px; padding-left: 2px; ">
										<input type="text" name="DistributorName" id="DistributorName" value="" data-mini="true" readonly="readonly" data-mini="true" placeholder="Customer Name" >
										<input type="hidden" name="isDistributorValid" id="isDistributorValid" value="false"  >
									</td>
									<td valign="top" style="width: 20%">
										<input  type="text" placeholder="Amount" id="AddRowAmount" name="AddRowAmount" data-mini="true">
									</td>
									<td valign="top" style="width: 20%">
										<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
									</td>
								</tr>
							</table>
						</form>
				    
				    	<form id="MainForm" data-ajax="false" action="#MainForm" onSubmit="return false;">
				    	
				    		<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
				    		
							<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:70%">
								
								<thead>
								    <tr class="ui-bar-c">
										<th style="width: 30%">Customer</th>
										<th style="width: 20%">Amount</th>
										<th style="width: 5%"></th>
								    </tr>
							  </thead>
								
								<tbody id="TableBody">
								
									<%
									int counter = 0;
									double TotalAmount = 0;
									ResultSet rs = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name FROM gl_customer_opening_balances order by id");
									while(rs.next()){
										counter++;
										TotalAmount += rs.getDouble("amount");
										%>
										<tr id="TableRow<%=counter%>">
											<td>
												<%=rs.getString("customer_id")%> - <%=rs.getString("customer_name")%>
												<input type="hidden" name="CustomerID" id="CustomerID" value="<%=rs.getString("customer_id")%>" >
											</td>
											<td style="text-align: right">
												<span id="AmountSpan<%=counter%>"><%= Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs.getDouble("amount"))%></span>
												<input type="hidden" name="Amount" id="Amount" value="<%=rs.getString("amount")%>" >
											</td>
											<td><a data-role="button" data-mini="true" data-icon="delete" data-iconpos="notext" data-inline="true" onClick="DeleteRow(<%=counter%>);" >Delete</a></td>
										</tr>
										<%
									}
									%>
									<script> $('#RowMaxID').val('<%=counter%>'); </script>
								
									<tr id="NoItemRow">
										<td colspan="3" style="margin: 1px; padding: 0px; <% if(counter > 0){ %> display: none <% } %> ">
											<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No customer added.</div>
										</td>
									</tr>
								</tbody>
							
								
									<tr>
										<td>Total</td>
										<td id="testcont" style="width: 15%">
											<input type="text" name="AmountTotal" id="AmountTotal" value="<%= Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalAmount) %>" data-mini="true" readonly="readonly" style="text-align: right" >
											<input type="hidden" name="AmountTotalHidden" id="AmountTotalHidden" value="" >
										</td>
										<td>&nbsp;</td>
										
									</tr>
							</table>
						 </form>
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
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" aclass="ui-disabled" >Submit</a>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLCustomerOpeningBalances.jsp'" >Reset</button>
						
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