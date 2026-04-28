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
int FeatureID = 172;
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

		<script src="js/GLOneTimeCredit.js?11=11"></script>
		
		  
		
		
</head>

<body>

<div data-role="page" id="GLOneTimeCreditPage" data-url="GLOneTimeCreditPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="One Time Credit" name="title"/>

    		

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar" style="min-height:60px">
	    		
	    			<form name="GLOneTimeCreditForm" id="GLOneTimeCreditForm" action="#GLOneTimeCreditForm" method="post" data-ajax="false" >
	    			
	    				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
	    				
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
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ApproverID" data-mini="true">Approver ID</label>
									
									
									<input type="text" name="ApproverID" id="ApproverID" value="" data-mini="true" ondblclick="openApproverSelectionPopup()" onChange="getUserName()" data-mini="true" >
								</td>
								<td style="width: 80%; border: 0px; padding-left: 2px; ">
									<label for="ApproverName" data-mini="true">Approver Name</label>
									<input type="text" name="ApproverName" id="ApproverName" value="" data-mini="true" readonly="readonly" data-mini="true" >
									<input type="hidden" name="isApproverValid" id="isApproverValid" value="false"  >
								</td>
							</tr>
							
							<tr>
								<td style="border: 0px;" >
									<label for="Amount" data-mini="true">Amount</label>
									<input name="Amount" id="Amount" value="" data-mini="true"  >
								</td>
							</tr>
							
							<tr>
								
							
								<td style="border: 0px;" colspan="2" >
									<label for="Narration" data-mini="true">Narration</label>
									<input name="Narration" id="Narration" value="" data-mini="true" maxlength="1000" placeholder="Maximum 1000 characters" >
								</td>
							</tr>
							
						</table>
				
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
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" >Submit</a>
						
						
						
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='GLOneTimeCredit.jsp'" >Reset</button>
					</td>
	                
				</tr>
			</table>
		</div>
    </div>
    
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackCashReceipt" name="CallBack" /> 
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackOneTimeCredit" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>