<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 76;  
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp"); 
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
%>

<script>

var FeatureIDValue = <%=FeatureID%>;

</script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/lookups.js"></script>
	<script src="js/OutletAdministration.js?11114=1145"></script>   
	  
	<title>PBC Enterprise Portal</title>
	
</head>
<body>

<div data-role="page" id="OutletAdministration" data-url="OutletAdministration" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Outlets" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
				
		        <form name="OutletAdministrationForm" id="OutletAdministrationForm" method="POST" data-ajax="false" action="#">
					<ul data-role="listview" data-inset="true" style="margin-left: 10px;margin-right: 10px;">
					<li data-role="list-divider">Outlet</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							<tr>
								<td style="width: 65%" valign="top">
									<fieldset class="ui-grid-b">
									    <div class="ui-block-a" style="padding-left: 5px;"><label for="OutletID">Outlet ID</label><input type="text" id="OutletID" name="OutletID" data-mini="true"></div>
									    <div class="ui-block-b" style="padding-left: 5px;"><label for="OutletName">Outlet Name</label><input type="text" id="OutletName" name="OutletName" data-mini="true" readonly="readonly"></div>
									    <div class="ui-block-c" style="padding-left: 5px;"><label for="region">Region</label><input type="text" id="region" name="region" data-mini="true" readonly="readonly"></div>
									</fieldset >
									
									<fieldset class="ui-grid-b">
									    <div class="ui-block-a" style="padding-left: 5px;"><label for="PrimaryDistributorName">Primary Distributor</label><input type="text" id="PrimaryDistributorName" name="PrimaryDistributorName" data-mini="true"></div>
									    <div class="ui-block-b" style="padding-left: 5px;"><label for="Filer">&nbsp;&nbsp;</label><label ><input type="checkbox" name="isFiler" id="isFilerID" data-mini="true"/>Filer</label></div>
									    
									</fieldset>						
									<fieldset class="ui-grid-solo">
										<div class="ui-block-a" style="padding-left: 5px;"><label for="address">Address</label><input id="address" name="address" type="text" value="" data-mini="true" readonly="readonly"></div>
									</fieldset>
									<fieldset class="ui-grid-b" >
										<div class="ui-block-a" style="padding-left: 5px;" id="SMSDID"><label for="smsnumber">SMS Number (923451234567)</label><input id="smsnumber" name="smsnumber" type="text" value="" data-mini="true" maxlength="12"></div>
										<div class="ui-block-b" style="padding-left: 5px;"><label for="ChannelID">Channel</label>
											<select name="ChannelID" id="ChannelID" data-mini="true" onchange="populateCaptiveByChannel(this.value)" >
												<option value="">Select</option>
												<%
												ResultSet rs = s.executeQuery("SELECT * FROM common_outlets_channels order by id");
												while(rs.next()){
													%>
													<option value="<%=rs.getString("id")%>"><%=rs.getString("label")%></option>
													<%
												}
												%>
											</select>
										</div>
										<div class="ui-block-c" style="padding-left: 5px;"><label for="NfcTagID">NFC Tag ID</label>
											<input type="text" id="NfcTagID" name="NfcTagID" data-mini="true">
										</div>
									</fieldset>
									<fieldset class="ui-grid-b" >
										<div class="ui-block-a" style="padding-left: 5px;"><label for="SAPCustomerID">SAP Customer ID</label><input id="SAPCustomerID" name="SAPCustomerID" type="text" value="" data-mini="true" ></div>
										<div class="ui-block-b" style="padding-left: 5px;"><label for="SegmentID">Segment</label>
											<select name="SegmentID" id="SegmentID" data-mini="true" onchange="populateCaptiveBySegment(this.value)" >
												<option value="">Select</option>
												<%
												ResultSet rs2 = s.executeQuery("SELECT * FROM common_outlets_segments");
												while(rs2.next()){
													%>
													<option value="<%=rs2.getString("id")%>"><%=rs2.getString("label")%></option>
													<%
												}
												%>
											</select>
										</div>
										<div class="ui-block-c" style="padding-left: 5px;"><label for="AgreedDailyAverageSales">Agreed Daily Average Sales</label>
											<input type="text" id="AgreedDailyAverageSales" name="AgreedDailyAverageSales" data-mini="true" aclass="ui-disabled" atabindex="-1" >
										</div>
									</fieldset>
									
									
									<fieldset class="ui-grid-b" >
										
										<div class="ui-block-a" style="padding-left: 5px;"><label for="VPOClassifications">VPO Classification</label>
											<select name="VPOClassifications" id="VPOClassifications" data-mini="true" onchange="populateCaptiveBySegment(this.value)" >
												<option value="0">Select</option>
												<%
												ResultSet rs3 = s.executeQuery("SELECT * FROM common_outlets_vpo_classifications");
												while(rs3.next()){
													%>
													<option value="<%=rs3.getString("id")%>"><%=rs3.getString("label")%></option>
													<%
												}
												%>
											</select>
										</div>
										<div class="ui-block-b" style="padding-left: 5px;">
									    	<label for="Anumber">Bank alfalah Account Number</label>
									    		<input type="text" id="Anumber" name="Anumber" data-mini="true">
									    </div>
									    <div class="ui-block-c" style="padding-left: 5px;"><label for="discountdisbursement">Discount Disbursement</label>
											<select name="discountdisbursement" id="discountdisbursement" data-mini="true" onchange="populateCaptiveBySegment(this.value)" >
												<option value="0">Select</option>
												<%
												ResultSet rs4 = s.executeQuery("SELECT * FROM common_outlets_discount_disbursement_types");
												while(rs4.next()){
													%>
													<option value="<%=rs4.getString("id")%>"><%=rs4.getString("label")%></option>
													<%
												}
												%>
											</select>
										</div>
										
									</fieldset>
									
									
									
									<div id="ContactDivContent" style="margin-top:10px; width: 100%">
									
									</div>
												
								
								</td>
								<td style="width: 35%; text-align: right;" valign="top">
									<img id="OutletMap" style="width: 285px; height: 220px; border: solid 0px;">
								</td>
							</tr>
						</table> 
						</li>
						</ul>
						
						<div>
							<table style="width:100%">
								<tr>
									<td style="width:50%" valign="top">
										<ul data-role="listview" data-inset="true" style="margin-left: 10px;margin-right: 10px;">
											<li data-role="list-divider">PJPs</li>
											<li>
												<div id="PJPContent">&nbsp;</div>
											<!-- 
												<table border="0" width="100%" id="OutletAdministrationDistributorTable">
													<tr>
														<td style="width:20%">
															<input type="text" name="DistributorID2" id="DistributorID2" placeholder="Distributor ID" data-mini="true" onChange="getDistributorName2()"/>
														</td>
														<td style="width:50%">
															<input type="text" name="DistributorName2" id="DistributorName2" placeholder="Distributor Name" data-mini="true" readonly /> 
															<input type="hidden" name="isSecondDistCall" id="isSecondDistCall" value="0"/>
														</td>
														<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="OutletAdministrationAddDistributor()" data-mini="true">Add</a></td>
													</tr>
												</table>
											 
												<div>
													<ul data-role="listview" data-inset="true" id="populateGroupDistributorDataUl" data-mini="true">					    
													</ul>
												</div>
											-->
											</li>
										</ul>
									</td>
									<td style="width:50%" valign="top">
										<!-- 
										<ul data-role="listview" data-inset="true" style="margin-left: 10px;margin-right: 10px;">
											<li data-role="list-divider">Contacts</li>
											<li>
												abc
											</li>
										</ul>
										 -->
									</td>
								</tr>
							</table>
							
						</div>
						
						
					</form>
					
<form name="PJPForm" id="PJPForm" action="BeatPlanCreate.jsp" method="post" data-ajax="false" >
	<input type="hidden" name="BeatPlanID" id="BeatPlanID" value="" >
	<input type="hidden" name="DistributorID" id="DistributorID" value="" >
</form>

<form name="DistributorForm" id="DistributorForm" action="DistributorAdministration.jsp" method="post" data-ajax="false" >
	<input type="hidden" name="DistributorFormDistributorID" id="DistributorFormDistributorID" value="" >
</form>
						
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<table>
		<tr>
		<td>
		<button data-icon="check" data-theme="a" data-inline="true" id="OutletAdministrationSaveButton" onclick="OutletAdministrationSubmit()">Save</button>
		</td>
		<td>
		<!-- 
			<a data-role="button" href="#" data-icon="grid" data-theme="b" id="SamplingDeactivationOpenButton" class="ui-disabled" target="_blank">Open Approved Document</a>
		 -->
		</td>
		</tr>
		</table>
	</div>    	
    </div>


<jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackOutletAdministration" name="CallBack" />
    	<jsp:param value="76" name="DistributorSearchFeatureID" />
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
	alert("Error: Could not be deactivated");
	</script>
<%
}
%>