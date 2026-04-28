<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(30, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}
Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<title>PBC Enterprise Portal</title>
</head>
<body>

<div data-role="page" id="SamplingAdjustment" data-url="SamplingAdjustment" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Discount Adjustment" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
				
		        <form name="SamplingAdjustmentForm" id="SamplingAdjustmentForm" method="POST" data-ajax="false" action="sampling/SamplingAdjustmentExecute">
					<ul data-role="listview" data-inset="true" style="margin-left: 10px;margin-right: 10px;">
					<li data-role="list-divider">Outlet</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							<tr>
								<td style="width: 65%">
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="OutletID">Outlet ID</label><input type="text" id="OutletID" name="OutletID" data-mini="true"></div>
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="OutletName">Outlet Name</label><input type="text" id="OutletName" name="OutletName" data-mini="true" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="BusinessType">Business Type</label><input type="text" id="BusinessType" name="BusinessType" data-mini="true" readonly="readonly"></div>
								</fieldset>						
								<fieldset class="ui-grid-solo">
									<div class="ui-block-a" style="padding-left: 5px;"><label for="address">Address</label><input id="address" name="address" type="text" value="" data-mini="true" readonly="readonly"></div>
								</fieldset>
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="region">Region</label><input type="text" id="region" name="region" data-mini="true" readonly="readonly"></div>								    
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="asm">ASM</label><input type="text" id="asm" name="asm" data-mini="true" data-theme="b" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="cr">CR</label><input type="text" id="cr" name="cr" data-mini="true" data-theme="b" readonly="readonly"></div>
								</fieldset>						
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="market">Market</label><input type="text" id="market" name="market" data-mini="true" value="" readonly="readonly"></div>
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="vehicle">Vehicle</label><input type="text" id="vehicle" name="vehicle" data-mini="true" value="" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="CurrentBalance">Current Balance</label><input type="text" id="CurrentBalance" name="CurrentBalance" data-mini="true" value="" readonly="readonly"></div>
								</fieldset>						
								
								</td>
								<td style="width: 35%; text-align: right;">
									<img id="OutletMap" style="width: 285px; height: 220px; border: solid 0px;">
								</td>
							</tr>
						</table> 
						</li>
						
						<li data-role="list-divider">Adjustment</li>
						<li>
							<fieldset class="ui-grid-c">
							    <div class="ui-block-a" style="padding: 5px; width:25%"><label for="AdjustmentType"><span style="padding-left: 7px;">Adjustment Type</span></label>
								    <select id="AdjustmentType" name="AdjustmentType">
								    <%
								    ResultSet rs = s.executeQuery("select id, label from sampling_posting_types order by label");
							    	while(rs.next()){
							    		%>
							    		<option value="<%=rs.getString(1)%>"><%=rs.getString(2) %></option>
							    		<%
							    	}
								    %>
								    </select>
							    </div>
							    <div class="ui-block-b" style="padding: 5px; width:25%"><label for="AdjustmentAmount">Amount</label><input type="text" id="AdjustmentAmount" name="AdjustmentAmount" placeholder="Rs."></div>
							    <div class="ui-block-c" style="padding: 5px; width:50%"><label for="AdjustmentRemarks">Remarks</label><input type="text" id="AdjustmentRemarks" name="AdjustmentRemarks" placeholder="Remarks"></div>
							</fieldset>						
						</li>
						
						
					</ul> 
					</form>

						
    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="SamplingAdjustmentSaveButton">Save</button>
	</div>    	
    </div>

</div>
</body>
</html>
<%
ds.dropConnection();
String success = request.getParameter("success");
							    		
if (success != null && success.equals("false")){
	%>
	<script>
	alert("Error: Could not be saved");
	</script>
<%
}
%>