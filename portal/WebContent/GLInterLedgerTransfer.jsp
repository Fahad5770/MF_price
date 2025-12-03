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
int FeatureID = 186;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

String AccountOptions = "<option value='0'>Select</option>";

ResultSet rs = s.executeQuery("SELECT id, label FROM gl_accounts_types order by id");
while(rs.next()){
	AccountOptions += "<option value="+rs.getString("id")+">"+rs.getString("label")+"</option>";
}


%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLInterLedgerTransfer.js"></script>
		
</head>

<body>

<div data-role="page" id="GLInterLedgerTransferPage" data-url="GLInterLedgerTransferPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Inter Ledger Transfer" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="ExecuteForm" id="ExecuteForm" action="#" method="post" >
				
				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
				<input type="hidden" name="isCustomerValid" id="isCustomerValid" value="false" >
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    
				    <li>
				    
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerID" data-mini="true">Customer ID</label>
									<input type="text" name="CustomerID" id="CustomerID" value="" data-mini="true"  ondblclick="openDistributorSelectionPopup()" onchange="getDistributorName()" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerName" data-mini="true">Customer Name</label>
									<input type="text" name="CustomerName" id="CustomerName" value="" data-mini="true" readonly="readonly" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="DebitAccountTypeID" data-mini="true">Debit Account</label>
									<select name="DebitAccountTypeID" id="DebitAccountTypeID" data-mini="true" >
										<%=AccountOptions%>
									</select>
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CreditAccountTypeID" data-mini="true">Credit Account</label>
									<select name="CreditAccountTypeID" id="CreditAccountTypeID" data-mini="true" >
										<%=AccountOptions%>
									</select>
								</td>
								
							</tr>
							
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="Amount" data-mini="true">Amount</label>
									<input type="text" name="Amount" id="Amount" value="" data-mini="true" >
								</td>
								
								
							</tr>
							<tr>
								<td colspan="2" style="border: 0px; padding-right: 2px">
									<label for="Reason" data-mini="true">Reason</label>
									<input type="text" name="Reason" id="Reason" value="" data-mini="true" >
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
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" aclass="ui-disabled" >Post</a>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLInterLedgerTransfer.jsp'" >Reset</button>
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