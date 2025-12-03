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
int FeatureID = 303;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}
%>

 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

<script type="text/javascript" src="js/CRMRegisterComplaint.js?1=1"></script>
</head>
<body>
<div style="margin-left: 10%;margin-right: 10%;" data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="CRM Helpdesk" name="title"/>
    </jsp:include>
    
  			 
			 <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="DeactivationDateExecute" id="DeactivationDateExecute"  method="post" >
				
				
				<ul id="CRMRegCOm" data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    	    
					
			<li>		
				
				
				<table   style="width: 70%;margin-left: 15%;">

									
					<tr>
						
						<td>
						 	<fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						        <legend>Category:</legend>
						        <input type="radio" name="COM1" onclick="populateSalesService()" id="SalesServices" checked="checked" value="1">
						        <label for="SalesServices">Sales  Services</label>
						        <input type="radio" name="COM1" onclick="populateQuality()"  id="Quality" value="2" >
						        <label for="Quality">Quality</label>
						        <input type="radio" name="COM1" onclick="populateMEM()"  id="MEM" value="3">
						        <label for="MEM">MEM</label>
							 </fieldset>
							<!-- <select name="category" data-mini="true" id="categoryddl"> -->
							      
						 </td>
						
					</tr>
					
					<tr>	
						
						<td>
							<div id="SubCat">
								<fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
							        <legend>Sub Category:</legend>
							        <input type="radio" name="COM" id="SalesServices4" value="1">
							        <label for="SalesServices4">General Complaint</label>
							        <input type="radio" name="COM"   id="Quality5" value="2" >
							        <label for="Quality5">Publicity issues</label>
							        <input type="radio" name="COM"   id="MEM6" value="3">
							        <label for="MEM6">Sales Team</label>
							        <input type="radio" name="COM"  id="SalesServices1" value="4">
							        <label for="SalesServices1">Claims/Schemes</label>
							        <input type="radio" name="COM"   id="Quality2" value="5" >
							        <label for="Quality2">Discounts/Incentives Issues</label>
							        <input type="radio" name="COM"   id="MEM3" value="6">
							        <label for="MEM3">Stock/Supply Issue</label>
								 </fieldset>
							
							
							</div>
						
						</td>
					</tr>
					
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					
					<tr>
					
						<td><input type="text" name="outlet_general" data-mini="true" id="outlet_general" placeholder="Name "></td>
					</tr>
						
					<tr>
						<td><input type="text" name="System_Outlet_Name_general" data-mini="true"  id="System_Outlet_Name_general" placeholder="Contact Number"></td>
					</tr>
					<tr  style="font-size:13px;">	
						
						<td>
							<textarea Style="height:100px" cols="50" placeholder="Description..."></textarea>
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
			<table style="width:15%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit()" aclass="ui-disabled" >Save</a>
						<!--  <input type="button" onClick="FormSubmit()" data-icon="check" data-theme="a" data-role="button" value="Click"/> -->
						
						
					</td>
					<td>
						
						<a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit()" aclass="ui-disabled" >Reset</a>
											
					</td>
	               
				</tr>
			</table>
		</div>
    </div>
   
    
    

</div>

</body>
</html>
