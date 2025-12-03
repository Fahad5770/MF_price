<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
int FeatureID = 302;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}
%>
<script >
function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}
function FormSubmit(){
	

	if( $('#request_id').val() == "" ){
		alert("Please enter Valid Request");
		$('#request_id').focus();
		return false;
	}
	if( $('#Deactivation_date').val() == "" ){
		alert("Please enter Valid Date");
		$('#Deactivation_date').focus();
		return false;
	}
	if( $('#reason').val() == "" ){
		alert("Please enter Valid Reason");
		$('#reason').focus();
		return false;
	}
	$('#RequestDateHidden').val(setDateFormat($('#Deactivation_date').val()));
	
	
	
	
	$.ajax({
			url:'inventory/ProductPerCaseDiscountsRequestDeactivateExecute',
			data: {req:$('#request_id').val(),res:$('#reason').val(),dateD:$('#RequestDateHidden').val() } ,
			type: 'post',
			dataType :'json',
			success:function( json ) 
			{
				
				 alert(json.Message);
		    	window.location="ProductPerCaseDiscountsRequestDeactivate.jsp";  
			} 
			
				});
		}


function setDateFormat(DateVal){
	if(DateVal == ''){
		return '';
	}
	var Year = DateVal.substr(0, 4);
	var Month = DateVal.substr(5, 2);
	var Day = DateVal.substr(8, 2);
	return Day+"/"+Month+"/"+Year;
}

</script>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>


</head>
<body>

<div style="margin-left: 10%;margin-right: 10%;" data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Deactivation Date" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="DeactivationDateExecute" id="DeactivationDateExecute"  method="post" >
				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value=""/>
				<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value=""/>
				<input type="hidden" name="isCustomerValid" id="isCustomerValid" value="false" >
				
				<input type="hidden" name="RequestDateHidden" id="RequestDateHidden" value="" >
				
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    
				    <li>
				    
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerID" data-mini="true">Request ID</label>
									<input type="text" name="request_id" id="request_id" value="" data-mini="true"  data-mini="true"   >
								</td>
								
								
							</tr>
							<tr>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CustomerName" data-mini="true">Deactivation-Date:</label>
									<input type="date" name="Deactivation_date" id="Deactivation_date" value="" data-mini="true" data-mini="true"  >
								</td>
								
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ValidFrom" data-mini="true">Reason</label>
									<input type="text" name="reason" id="reason" value="" data-mini="true"  placeholder="comments" data-mini="true" >
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
    
    <div style="position:fixed" data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit()" aclass="ui-disabled" >Save</a>
						<!--  <input type="button" onClick="FormSubmit()" data-icon="check" data-theme="a" data-role="button" value="Click"/> -->
						
						
					</td>
	               
				</tr>
			</table>
		</div>
    </div>
   
    
    

</div>

</body>
</html>
