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
int FeatureID = 230;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("EditID"));
if(EditID > 0){
	isEditCase = true;
}

int DeActivate = Utilities.parseInt(request.getParameter("DeActivate"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

String CustomerID = "";
String CustomerName = "";
String CreditLimit = "";
String ValidFrom = "";
String ValidTo = "";
String CreditLimitComments = "";

if(isEditCase){
	ResultSet rs = s.executeQuery("select *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name from gl_customer_credit_limit_request where id="+EditID);
	if(rs.first()){
		CustomerID = rs.getString("customer_id");
		CustomerName = rs.getString("customer_name");
		CreditLimit = rs.getString("credit_limit");
		ValidFrom = rs.getString("valid_from");
		ValidTo = rs.getString("valid_to");
		CreditLimitComments = rs.getString("comments");
	}
}


if(DeActivate > 0){
	s.executeUpdate("update gl_customer_credit_limit_request set is_active=0, deactivated_on=now(), deactivated_by="+SessionUserID+" where id="+DeActivate);
	response.sendRedirect("GLCreditLimitRequest.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCreditLimitRequest.js"></script>
		
</head>

<body>

<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Credit Limit Request" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="GLCreditLimitExecute" id="GLCreditLimitExecute" action="#" method="post" >
				
				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
				<input type="hidden" name="isCustomerValid" id="isCustomerValid" value="false" >
				
				<input type="hidden" name="ValidFromDate" id="ValidFromDate" value="" >
				<input type="hidden" name="ValidToDate" id="ValidToDate" value="" >
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    
				    <li>
				    
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerID" data-mini="true">Customer ID</label>
									<input type="text" name="CustomerID" id="CustomerID" value="<%=CustomerID%>" data-mini="true" data-mini="true"  ondblclick="openDistributorSelectionPopup()" onchange="getDistributorName()" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerName" data-mini="true">Customer Name</label>
									<input type="text" name="CustomerName" id="CustomerName" value="<%=CustomerName%>" data-mini="true" data-mini="true" readonly="readonly" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CreditLimit" data-mini="true">Credit Limit</label>
									<input type="text" name="CreditLimit" id="CreditLimit" value="<%=CreditLimit%>" data-mini="true"  data-mini="true" >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ActiveStatus" data-mini="true">Status</label>
									<input type="text" name="ActiveStatus" id="ActiveStatus" value="<%=CreditLimit%>" readonly="readonly" data-mini="true"  data-mini="true" >
								</td>
								
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ValidFrom" data-mini="true">Valid From</label>
									<input type="date" name="ValidFrom" id="ValidFrom" value="<%=ValidFrom%>" data-mini="true"  data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ValidTo" data-mini="true">Valid To</label>
									<input type="date" name="ValidTo" id="ValidTo" value="<%=ValidTo%>" data-mini="true"  data-mini="true" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px" colspan="2">
									<label for="CreditLimitComments" data-mini="true">Reason</label>
									<input type="text" name="CreditLimitComments" id="CreditLimitComments" value="<%=CreditLimitComments%>" data-mini="true"  data-mini="true" >
								</td>
								
								
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px; background-color: grey; color: white; font-weight: normal;" colspan="2">Security Cheque Information
								</td>								
							</tr>
														
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CheckNumber" data-mini="true">Cheque Number</label>
									<input type="text" name="CheckNumber" id="CheckNumber" value="" data-mini="true"  data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="Bank" data-mini="true">Bank</label>
									<input type="text" name="Bank" id="Bank" value="" data-mini="true"  data-mini="true" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px" colspan="2">
									<label for="Branch" data-mini="true">Branch</label>
									<input type="text" name="Branch" id="Branch" value="" data-mini="true"  data-mini="true" >
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
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLCreditLimitRequest.jsp'" >Reset</button>
						<% if( isEditCase ){ %>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLCreditLimitRequest.jsp?DeActivate=<%=EditID%>'" >De-Activate</button>
						<% } %>
					</td>
	                <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ViewAllButton" >View All</a>
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
				ResultSet rs3 = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name FROM gl_customer_credit_limit_request where is_active=1 order by activated_on ");
				while( rs3.next() ){
					%>
					 <li><a href="#" onclick="window.location='GLCreditLimitRequest.jsp?EditID=<%=rs3.getString("id")%>'"><%=rs3.getString("customer_name")%><span class="ui-li-count"><%= Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs3.getDouble("credit_limit"))%></span></a></li> 
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
s.close();
ds.dropConnection();
%>