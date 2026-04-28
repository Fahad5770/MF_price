<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID=75;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long DistributorID = Utilities.parseLong(request.getParameter("DistributorFormDistributorID"));

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
%>

<script>
	var DistributorIDGlobal = <%=DistributorID%>;
</script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/lookups.js"></script>
    <script src="js/DistributorAdministration.js?4=554"></script>
	<title>PBC Enterprise Portal</title>
</head>
<body>

<div data-role="page" id="DistributorAdministration" data-url="DistributorAdministration" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Distributors" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
				
		        <form name="DistributorAdministrationForm" id="DistributorAdministrationForm" data-ajax="false" >
				<input type="hidden" name="DistributorAdiministrationActionID" id="DistributorAdiministrationActionID" value="1"/>
				<input type="hidden" name="FeatureID" id="FeatureID" value="<%=FeatureID%>"/>	
					<ul data-role="listview" data-inset="true" style="margin-left: 10px;margin-right: 10px;">
					<li data-role="list-divider">Distributor</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							<tr>
								<td style="width: 65%">
								<fieldset class="ui-grid-b">
								    <div class="ui-block-a" style="padding-left: 5px;"><label for="DistributorID">Distributor ID</label><input type="text" id="DistributorID" name="DistributorID" data-mini="true" onChange="LoadDistributor()"></div>
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="DistributorName">Distributor Name</label><input type="text" id="DistributorName" name="DistributorName" data-mini="true" readonly="readonly"></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="DistributorCity">City</label><input type="text" id="DistributorCity" name="DistributorCity" data-mini="true" readonly="readonly"></div>
								</fieldset>						
								<fieldset class="ui-grid-solo">
									<div class="ui-block-a" style="padding-left: 5px;"><label for="DistributorAddress">Address</label><input id="DistributorAddress" name="DistributorAddress" type="text" value="" data-mini="true" readonly="readonly"></div>
								</fieldset>
								<fieldset class="ui-grid-b">
								    <!-- <div class="ui-block-a" style="padding-left: 5px;"><label for="DistributorRegion">Region</label><input type="text" id="DistributorRegion" name="DistributorRegion" data-mini="true" ></div>	 -->							    
								   <div class="ui-block-a" style="padding-left: 5px;width:25%">
								   <label for="SelectProductGroupID">Region</label>
								   <select id="DistributorRegion" name="DistributorRegion" data-mini="true">
								   <option value="-1">Select Region</option>
								    <%
								    ResultSet rs2 = s.executeQuery("select * from common_regions");
							    	while(rs2.next()){
							    		%>
							    		<option value="<%=rs2.getString("region_id")%>"><%=rs2.getString("region_name") %></option>
							    		<%
							    	}
								    %>
								    </select>
								   </div>
								    <div class="ui-block-b" style="padding-left: 5px;"><label for="DistributorRoute">Route</label><input type="text" id="DistributorRoute" name="DistributorRoute" data-mini="true"  ></div>
								    <div class="ui-block-c" style="padding-left: 5px;"><label for="DistributorContactNo">Contact No</label><input type="text" id="DistributorContactNo" name="DistributorContactNo" data-mini="true"></div>
								</fieldset>	
								</td>
								<!-- <td style="width: 35%; text-align: right;">
									<img id="OutletMap" style="width: 285px; height: 220px; border: solid 0px;">
								</td> -->
							</tr>
						</table> 
						</li>
						
						<li data-role="list-divider">Configuration</li>
						<li>
							<fieldset class="ui-grid-c">
							    <div class="ui-block-a" style="padding: 5px; width:25%">
							       <label for="SelectProductGroupID">Product Group</label>
								   <select id="SelectProductGroupID" name="SelectProductGroupID" data-mini="true">
								   <option value="-1">Select Product Group</option>
								    <%
								    ResultSet rs = s.executeQuery("select * from employee_product_groups");
							    	while(rs.next()){
							    		%>
							    		<option value="<%=rs.getString("product_group_id")%>"><%=rs.getString("product_group_name") %></option>
							    		<%
							    	}
								    %>
								    </select>
							    </div>
							    <div class="ui-block-b" style="padding: 5px; width:25%">
							    	<label for="SelectDistributorTypeID">Type</label>
							    	
							    	<select id="SelectDistributorTypeID" name="SelectDistributorTypeID" data-mini="true">
									    <%
									    ResultSet rs3 = s.executeQuery("SELECT id, label FROM common_distributors_types");
								    	while(rs3.next()){
								    		%>
								    		<option value="<%=rs3.getString("id")%>"><%=rs3.getString("label") %></option>
								    		<%
								    	}
									    %>
									</select>
							    	
							    </div>
							    
							    <div class="ui-block-c" style="padding: 5px; width:50%">
							    	<label for="DistributorMonthCycle">Month Cycle (Day of Month)</label>
							    	
							    	<input type="text" id="DistributorMonthCycle" name="DistributorMonthCycle" data-mini="true">
									
							    </div>
							    
							    
							    
							</fieldset>						
						</li>
						<li>
						<fieldset>
						<div class="ui-block-c" style="padding: 5px; width:10%">
							    	<label for="DeskSaleOutletID">Desk Outlet</label>
							    	
							    	<input type="text"  id="DeskSaleOutletID" name="DeskSaleOutletID" data-mini="true" onChange="getOutletName(); "  maxlength="10">
							    	
									
							    </div>
							    <div class="ui-block-c" style="padding: 5px; width:40%">
							    	<label for="DeskSaleOutletName">&nbsp;</label>
							    	
							    	
							    	<input type="text"  id="DeskSaleOutletName" name="DeskSaleOutletName" data-mini="true" readonly="readonly">
									
							    </div>
						</fieldset>
						
						</li>
						
						
					</ul> 
					</form>

						
    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="DistributorAddProductSaveButton" onClick="DistributorAdministrationSubmit()">Save</button>
	</div>    	
    </div>
<jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBackDeskSale" name="CallBack" />
    	<jsp:param value="75" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
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