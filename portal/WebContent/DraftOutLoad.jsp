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
int FeatureID = 329;
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

		<script src="js/DraftOutLoad.js?111111=111111"></script>
		
		  
		
		
</head>

<body>

<div data-role="page" id="GLInvoicePostingPage" data-url="GLInvoicePostingPage" data-theme="d">

      <jsp:include page="Header2.jsp" >
    	<jsp:param value="Draft Out Load" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="InvoicePostingExecute" id="InvoicePostingExecute" action="#" method="post" >
				
				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
				
				<input type="hidden" name="Status" id="Status" value="" >
				<input type="hidden" name="InvoiceAmountHidden" id="InvoiceAmountHidden" value="" >
				<input type="hidden" name="AdvanceAgainstOrderHidden" id="AdvanceAgainstOrderHidden" value="" >
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <li data-role="list-divider">Order Information</li>
				    <li>
				    
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 60%; border: 0px; padding-right: 2px">
									<label for="OrderNo" data-mini="true">Order No</label>
									<input type="text" name="OrderNo" id="OrderNo" value="" data-mini="true" onChange="" data-mini="true" maxlength="10" >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="OrderNo" data-mini="true">&nbsp;</label>
									<input type="button" value="Generate" data-mini="true" onClick="GetSalesOrderInfo()"/>
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px" class="ui-disabled" id="alreadyGenerated">
									<label for="OrderNo" data-mini="true">&nbsp;</label>
									<input type="button"  value="View" data-mini="true" onClick="ViewAlreadyPrint()" />
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