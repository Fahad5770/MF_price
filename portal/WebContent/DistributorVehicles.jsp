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
<script src="js/DistributorVehicles.js"></script>
<%

int FeatureID = 55;
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 55);
if( UserDistributor == null ){
	response.sendRedirect("AccessDenied.jsp");
}

%>

<% 
String DistributorName = "";
long DistributorID=0;


if( UserDistributor != null ){
	if(UserDistributor.length>1) //if it has more than 1 distributor
	{
		response.sendRedirect("AccessDenied.jsp");
	}
	else
	{
		DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
		DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
	}
	
}


%>

<div data-role="page" id="DistributorVehiclesMain" data-url="DistributorVehiclesMain" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Distributor Vehicle" name="title"/>

    		<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
<%
long DistVehID = Utilities.parseLong(request.getParameter("DistVehID"));
String VehicleNumber="";
long VehicleType=0;
long DriverID=0;
int IsEdit=0;
String DriverName="";
%>

<%
if(DistVehID !=0) //edit case
{
	IsEdit=1;
	ResultSet rs2 = s.executeQuery("SELECT * FROM distribtuor_vehicles dv  where dv.id="+DistVehID);
	if(rs2.next())
	{
		VehicleNumber = Utilities.filterString(rs2.getString("vehicle_no"), 1, 100);
		VehicleType = Utilities.parseLong(rs2.getString("type_id"));
		DriverID = Utilities.parseLong(rs2.getString("driver_id"));	
		//DriverName = Utilities.filterString(rs2.getString("name"), 1, 100);
		//empid
	}
}
%>

	<form id="DistributorVehicleForm" name="DistributorVehicleForm" action="" method="POST">
	<input type="hidden" name="isEditCase" id="isEditCase" value="<%=IsEdit%>"/>
	<input type="hidden" name="DistVehIDForWhole" id="DistVehIDForWhole" value="<%=DistVehID%>"/>
	<input type="hidden" name="DistributorVehDistID" id="DistributorVehDistID" value="<%=DistributorID %>"/>
	
		<ul data-role="listview" data-inset="true">
	        <li data-role="fieldcontain">
	            <table style="width:100%" border="0">
	            <tr>
	            <td style="width:22%" ><label for="VehicleNum" style="width:100%">Vehicle Number:</label></td>
	            <td style="width:78%"><input type="text" name="VehicleNum" id="VehicleNum" value="<%=VehicleNumber%>" maxlength="10"></td>
	            </tr>
	            
	            
	            </table>
	            
	        </li>
	        <li data-role="fieldcontain">
	            <table style="width:100%" border="0">
	            	<tr>
	            	<td style="width:22%"><label for="DistributorDeptName" style="width:100%">Vehicle Type ID:</label></td>
	            	<td style="width:78%">
	            	<select name="VehiclesTypeIDSelect" id="VehiclesTypeIDSelect">
	            	<option value="">Select Vehicle Type</option>
	            	<%
	            	ResultSet rs = s.executeQuery("select * from distributor_vehicles_types");
	            	while(rs.next())
	            	{
	            	%>
	            	<option value="<%=Utilities.parseLong(rs.getString("id"))%>" <%if(Utilities.parseLong(rs.getString("id")) == VehicleType){ %>selected<%} %>><%=rs.getString("label") %></option>
	            	<% 
	            	}
	            	%>
	            	</select>
	            	</td>
	            	</tr>
	            	
	            	
	            </table>
	        </li>
	       <!--   
	        <li data-role="fieldcontain">
	            
	             <table style="width:100%" border="0">
	            	<tr>
	            	<td style="width:22%"><label for="DistributorDeptName" style="width:100%">Driver ID:</label></td>
	            	<td style="width:78%">
	            	<select name="DistributorDriverID" id="DistributorDriverID">
	            	<option value="">Select Driver</option>
	            	<%
	            	ResultSet rs2 = s.executeQuery("select * from distributor_employees");
	            	while(rs2.next())
	            	{
	            	%>
	            	<option value="<%=Utilities.parseLong(rs2.getString("id"))%>" <%if(Utilities.parseLong(rs2.getString("id")) == DriverID){ %>selected<%} %>><%=rs2.getString("id")+" - "+rs2.getString("name") %></option>
	            	<% 
	            	}
	            	%>
	            	</select>
	            	</td>
	            	</tr>
	            	
	            	
	            </table>
	           --> 
	            <!--  <table style="width:100%" border="0">
	            <tr>
	            <td style="width:22%" style="width:100%">
	            <label for="DistributorEmpDesignation">Driver ID:</label>
	            </td>
	            <td style="width:10%" id="DriverIDTD">
	            	 <input type="text" name="DistributorDriverID" id="DistributorDriverID" value="<%if(DriverID!=0){ %><%=DriverID%><%} %>" onChange="GetDistributorEmployeeName()">
	            </td>
	            <td style="width:68%">
	            	 <input type="text" name="DistributorEmpName" id="DistributorEmpName" value="<%=DriverName%>" readonly>
	            </td>
	            </tr>
	            </table>-->
	           
	            
	       <!--  </li>  -->
	        
	        

        </ul>
	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<table border="0" style="width: 100%;">
		<td  align="left">
			<button data-icon="check" data-theme="a" data-inline="true" id="DistributorVehSave" onClick="DistributorVehicleSubmit()">Save</button>
		</td>
		
		<td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DistributorSearch" >Search</a>
		</td>
		</table>
				
	</div>    	
    </div>



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
		<ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search Vehicles"> 
			<li data-role="list-divider">Distributor Vehicles</li>
			
	 		<%
				ResultSet rs1 = s.executeQuery("SELECT * FROM distribtuor_vehicles where distributor_id="+DistributorID);
			while(rs1.next())
			{
	        	
	 		%>
	 		<li>
	 			<a data-ajax="false" href="#" onClick="LoadPerticularDistributorVehicle(<%=rs1.getString("id")%>)">
						<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("vehicle_no") %></span>
						
						
				</a>
	 		</li>
	 		<%
	 		} 
	 		%> 
	 		
	 		
	 		
 		</ul>      
			
 		<form data-ajax="false" id="DistVehEditForm" method="POST" action="DistributorVehicles.jsp">
        	<input type="hidden" name="DistVehID" id="DistVehID">
        </form>
            
        </div>
    </div>

</div>



</body>
</html>
<%
s.close();
ds.dropConnection();
%>