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

if(Utilities.isAuthorized(32, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}
int FeatureID = 32;  

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<title>PBC Enterprise Portal</title>
	<script>
	
	$( document ).delegate("#SamplingDeactivation", "pageinit", function() {
		
		$("#SamplingDeactivationSaveButton").on ("click", function( event, ui ) {
			$('#SamplingDeactivationForm').submit();
		});

		
		var pageid = "#SamplingDeactivation";
		
		$(pageid + ' #OutletID').change(function() {
			$.ajax({
			    url: "sampling/GetOutletInfoJson",
			    data: {
			        OutletID: this.value
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.exists == "true"){
			    		$(pageid + ' #OutletName').val(json.OutletName);
			    		$(pageid + ' #BusinessType').val(json.BusinessType);
			    		$(pageid + ' #address').val(json.address);
			    		$(pageid + ' #region').val(json.region);
			    		$(pageid + ' #asm').val(json.asm);
			    		$(pageid + ' #cr').val(json.cr);
			    		$(pageid + ' #market').val(json.market);
			    		$(pageid + ' #vehicle').val(json.vehicle);
			    		$(pageid + ' #CurrentBalance').val(json.CurrentBalance);
			    		
			    		var lat = json.latitude;
			    		var lng = json.longitude;
			    		
			    		if (parseFloat(lat) > 1){
			    			$(pageid + ' #OutletMap').attr("src","http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C"+lat+","+lng+"&sensor=false");
			    		}else{
			    			$(pageid + ' #OutletMap').attr("src","images/GPSUnavailable.png");
			    		}
			    		
			    		$.mobile.showPageLoadingMsg();	
			    		$.get('SamplingDeactivationStatus.jsp?OutletID='+$(pageid + ' #OutletID').val(), function(data) {
							$("#DeactivationDiscountStatus").html(data);
							$("#DeactivationDiscountStatus").trigger('create');
							$.mobile.hidePageLoadingMsg();
							
							var RequestID = parseFloat($("#DeactivationRequestID").val());
							
							
							if (!isNaN(RequestID)){
								$("#SamplingDeactivationOpenButton").attr("href","WorkflowManager.jsp?requestID="+$("#DeactivationRequestID").val()+"&processID=1");
								$("#SamplingDeactivationOpenButton").removeClass("ui-disabled");
							}else{
								$("#SamplingDeactivationOpenButton").addClass("ui-disabled");
							}
			    		});
			    		
			    		
			    		
			    	}else{
			    		$(pageid + ' #BusinessType').val("");
			    		$(pageid + ' #address').val("");
			    		$(pageid + ' #region').val("");
			    		$(pageid + ' #asm').val("");
			    		$(pageid + ' #cr').val("");
			    		$(pageid + ' #market').val("");
			    		$(pageid + ' #vehicle').val("");
			    		
			    		$(pageid + ' #OutletName').val("Outlet doesn't exist.");
			    		$(pageid + ' #OutletID').focus();
			    		$(pageid + ' #OutletID').select();
			    		
			    		$("#SamplingDeactivationOpenButton").addClass("ui-disabled");
			    	}
			    },
			    error: function( xhr, status ) {
			        alert( "Sorry, the server did not respond." );
			    }
			});
			
		});	

	});

	</script>
</head>
<body>

<div data-role="page" id="SamplingDeactivation" data-url="SamplingDeactivation" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Deactivate Discount" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
				
		        <form name="SamplingDeactivationForm" id="SamplingDeactivationForm" method="POST" data-ajax="false" action="sampling/SamplingDeactivationExecute">
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
						</ul>
						
						<div id="DeactivationDiscountStatus">
							
						</div>
					</form>

						
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<table>
		<tr>
		<td>
		<button data-icon="check" data-theme="a" data-inline="true" id="SamplingDeactivationSaveButton">Deactivate</button>
		</td>
		<td>
		<a data-role="button" href="#" data-icon="grid" data-theme="b" id="SamplingDeactivationOpenButton" class="ui-disabled" target="_blank">Open Approved Document</a>
		</td>
		</tr>
		</table>
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
	alert("Error: Could not be deactivated");
	</script>
<%
}
%>