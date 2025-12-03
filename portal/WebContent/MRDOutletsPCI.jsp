<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%> 
<%@page import="com.pbc.util.UserAccess"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/MRDOutletsPCI.js"></script>
<script src="js/lookups.js"></script>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 262; 
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<div data-role="page" id="MRDOutletsPCIFormPage" data-url="MRDOutletsPCIFormPage" data-theme="d">

	
	<jsp:include page="Header2.jsp" >
    	<jsp:param value="MRD Outlets PCI" name="title"/>
    </jsp:include>
	 
    <div data-role="content" data-theme="d">
    	<form id="MRDOutletsPCIForm" name="MRDOutletsPCIForm" action="#" method="POST" data-ajax="false">
		<table style="width:25%; border-collapse:collapse;" border="0" >
        	<tr>
        		<td>
        			<label for="OutletID" style="width:100%">Outlet ID</label>        		
        			<input type="text" name="OutletID" id="OutletID" value="" onchange="getOutletInfoJson()" >
        		</td>
        	</tr>	        	
        </table>
		<hr>
		
			<table style="width:100%; border-collapse:collapse;" border="0" >
	        	<tr>
	        		<td>
	        			<label for="OutletName">Outlet Name</label>	        		
	        			<input type="text" name="OutletName" id="OutletName" value="" areadonly="readonly" >
	        		</td>
	        		<!-- 
	        		<td>
	        			<label for="BusinessType">Business Type</label>	        		
	        			<input type="text" name="BusinessType" id="BusinessType" value="" readonly="readonly" >
	        		</td> -->
	        		<td>
	        			<label for="Address">Address</label>	        		
	        			<input type="text" name="Address" id="Address" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="Region">Region</label>	        		
	        			<input type="text" name="Region" id="Region" value="" readonly="readonly" >
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>
	        			<label for="Lat">Lat</label>	        		
	        			<input type="text" name="Lat" id="Lat" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="Lng">Lng</label>	        		
	        			<input type="text" name="Lng" id="Lng" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="GPSAccuracy">GPS Accuracy</label>	        		
	        			<input type="text" name="GPSAccuracy" id="GPSAccuracy" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="Comments">Comments</label>	        		
	        			<input type="text" name="Comments" id="Comments" value="" areadonly="readonly" >
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>
	        			<label for="Pepsi">Pepsi</label>	        		
	        			<input type="text" name="Pepsi" id="Pepsi" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="Coke">Coke</label>
	        			<input type="text" name="Coke" id="Coke" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="Gourmet">Gourmet</label>
	        			<input type="text" name="Gourmet" id="Gourmet" value="" readonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="ContactPerson">Contact Person</label>	        		
	        			<input type="text" name="ContactPerson" id="ContactPerson" value="" areadonly="readonly" >
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>
	        			<label for="Phone">Phone</label>	        		
	        			<input type="text" name="Phone" id="Phone" value="" areadonly="readonly" >
	        		</td>
	        		<td>
	        			<label for="UserOutletID">User Outlet ID</label>
	        			<input type="text" name="UserOutletID" id="UserOutletID" value="" >
	        		</td>
	        		<td valign="bottom">
	        			<a data-icon="check" data-theme="c" data-role="button" data-inline="true" id="DeleteButton" href="#" onClick="DeleteSubmit()">Delete</a>
	        		</td>
	        	</tr>
	        </table>
		</form>
		
		<jsp:include page="LookupDistributorSearchPopup.jsp" >
	    	<jsp:param value="DistributorSearchCallBack" name="CallBack" />
	    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
	    </jsp:include><!-- Include Distributor Search -->

    </div><!-- /content -->    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
		<div>			
			<button data-icon="check" data-theme="a" data-inline="true" onclick="FormSubmit()" >Submit</button>
			<button data-icon="check" data-theme="b" data-inline="true" onClick="window.location='MRDOutletsPCI.jsp'">Reset</button>
			
		</div>    	
    </div>
	
</div>

</body>
</html>

<%
s.close();
ds.dropConnection();
%>