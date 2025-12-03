<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="java.text.SimpleDateFormat"%>




<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->
<script src="js/lookups.js"></script>

<script >
function showOutletContent(){
	$.mobile.showPageLoadingMsg();	
	$.get('OutletActivationSummary.jsp?OUTLETID='+$('#SearchOutletID').val(), function(data) {	
 		$("#SelectOutlet").remove();
		$("#SearchContent").html(data);
		$("#SearchContent").trigger('create');
	//	getBusinessType();	
		$.mobile.hidePageLoadingMsg();
	});
	//$.mobile.hidePageLoadingMsg();
	
 }
</script>


<%
long SessionUserID = Utilities.parseLong((String) session.getAttribute("UserID"));

int FeatureID = 480;
if (Utilities.isAuthorized(FeatureID, SessionUserID) == false) {
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}



%>
<div data-role="page" id="OutletMain" data-url="OutletMain" data-theme="d">

	<div data-role="header" data-theme="c" data-position="fixed">
	    <div>
		    <div style="float:left; width:10%">
		    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
		    </div>
		    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;">
		    	<h2 style="font-size: 14pt;float:left; margin-left:35%; text-align:center;">Outlet Edit Master</h2>
			</div>
		</div>
	</div>
	<div data-role="content" data-theme="d">
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
			<li>
				
				<table style="width: 20%">
					<tr>
						<td>
							<input type="text" placeholder="Outlet ID" id="SearchOutletID" name="SearchOutletID" data-mini="true">
						</td>
						<td><button  value="Show" onclick="showOutletContent();" data-inline="true" data-mini="true" data-icon="search">Search</button></td>	
										
					</tr>
				</table>
				
			</li>
	     <li data-role="list-divider">Outlet Data</li>
    	<li>
    		<div style="margin-top:20px;margin-left:40%;width:20%" id="SelectOutlet">			
					<span >Please Select Outlet</span>		
			</div>	
			<div id="SearchContent" style="padding:10px"></div>
		</li>
	   </ul>
   		

</div>
	
</div>

</body>
</html>


