<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>


<link rel="stylesheet"  href="lib/jqm130/jquery.mobile-1.3.0.min.css">
<script src="lib/jquery-1.9.1.min.js"></script>
<script src="lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
<script src="js/InventorySalesDispatchUnlock.js?122323r=223222"></script>
<%

int FeatureID = 375;
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Statement s2 = c.createStatement();
boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("DistributorTargetID"));
if(EditID > 0){
	isEditCase = true;
}
String h="";
%>


<div data-role="page" id="InventryMain" data-url="DistributorVehiclesMain" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Open Dispatch" name="title"/>

    		<jsp:param value="<%=h%>" name="HeaderValue"/>

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">



	<form id="DistributorVehicleForm" name="DistributorVehicleForm" action="" method="POST">
	<%-- <input type="hidden" name="isEditCase" id="isEditCase" value="<%=IsEdit%>"/>
	<input type="hidden" name="DistVehIDForWhole" id="DistVehIDForWhole" value="<%=DistVehID%>"/>
	<input type="hidden" name="DistributorVehDistID" id="DistributorVehDistID" value="<%=DistributorID %>"/> --%>
	
		<ul data-role="listview" data-inset="true">
		<li data-role="fieldcontain">
		    <table style="width:100%; border-collapse:collapse;"    border="0">
            		<tr>
            			<td style="width:22%">
            				 <label for="InventoryID">Dispatch ID:</label>
            			</td>
            			<td style="width:100%">
            				<input type="text" name="InventoryID" id="InventoryID" placeholder="Dispatch ID" onchange="getInventoryInfoJson()">
            			</td>
            		</tr>
            	</table>
		</li>
		   <li data-role="fieldcontain">
		    <table style="width:100%; border-collapse:collapse;"    border="0">
            		<tr>
            			<td style="width:22%">
            				 <label for="DistributorID">Distributor ID:</label>
            			</td>
            			<td style="width:12%">
            				<input type="text" name="DistributorID" id="DistributorID"  placeholder="Distributor ID">
            			</td>
            			<td style="width:66%">
            				<input type="text" name="DistributorName" id="DistributorName" placeholder="Distributor Name"  readonly="readonly">
            			</td>
            		</tr>
            	</table>
		</li>
		
	        <li data-role="fieldcontain">
	            <table style="width:100%" border="0">
	            <tr>
	            <td style="width:22%" ><label for="IsAdjusted" style="width:100%">Is Adjusted:</label></td>
	            <td style="width:78%"><input type="text" name="IsAdjusted" id="IsAdjusted" ></td>
	            </tr>
	            
	            </table>
	            
	        </li>
	        <li data-role="fieldcontain">
	            <table style="width:100%" border="0">
	            <tr>
	            <td style="width:22%" ><label for="IsBlocked" style="width:100%">Is Blocked:</label></td>
	            <td style="width:78%"><input type="text" name="IsBlocked" id="IsBlocked" ></td>
	            </tr>
	            
	            </table>
	            
	        </li>
	         <li data-role="fieldcontain">
	            <table style="width:100%" border="0">
	            <tr>
	            <td style="width:22%" ><label for="IsBlocked" style="width:100%">Dispatch Created On:</label></td>
	            <td style="width:78%"><input type="text" name="DispatchCreatedOn" id="DispatchCreatedOn" ></td>
	            </tr>
	            
	            </table>
	            
	        </li>
	            	
	        
	        

        </ul>
	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<table border="0" style="width: 100%;">
		<td  align="left">
			<button data-icon="check" data-theme="a" data-inline="true" id="DistributorVehSave" onClick="FormSubmit()">Open</button>
		</td>
		</table>
				
	</div>    	
    </div>


</div>



</body>
</html>
<%
s.close();
ds.dropConnection();
%>