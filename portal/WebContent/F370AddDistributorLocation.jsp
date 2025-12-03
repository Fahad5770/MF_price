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
int FeatureID = 370;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("EditID"));
if(EditID > 0){
	isEditCase = true;
}

int DeActivate = Utilities.parseInt(request.getParameter("DeActivate"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

//s3


%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		 <script type="text/javascript" src="js/F370AddDistributorLocation.js?121=121211"></script>
		
</head>

<body>

<div data-role="page" id="AddDistributorLocationPage" data-url="AddDistributorLocationPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Add Distributor Location" name="title"/>
    </jsp:include>
    
   
    <div data-role="content" data-theme="d">
	
	<form id="DistributorLocationForm">
		<ul data-role="listview"  data-divider-theme="b" style="mar1gin-top:5px;">
			<li data-role="list-divider">Distributor</li> 
		</ul>
		
		<table  cellpadding="5" style="width:50%; margin-top:35px;margin-bottom:35px;">
			<tr>
				
				<td><input type = "text"   placeholder = "Distributor ID"  id="DistributorID" name= "DistributorID" ></td>
				<td><input type = "text" placeholder = "Distributor Name" id="DistributorName" name="DistributorName"></td>	
				
			</tr>
		</table>
	
		
	
		<ul data-role="listview" data-divider-theme="b" style="margin-top:5px;">
			<li data-role="list-divider"> Detail</li> 
		</ul>
		<table  cellpadding="5" style="width:100%; margin-top:35px;margin-bottom:35px;" >
		
			<tr > 
				
				<th  style="text-align:left">Latitude</th>
				<th style="text-align:left">Longitude</th>
				<th style="text-align:left">Address</th>
				
				<th style="text-align:left">Phone #</th>
				<th style="text-align:left">Location Description</th>
				
				
			</tr>
			<tr > 
				<td><input type = "text" data-mini="true" placeholder = "Latitude"  id="Latitude"  name="Latitude"></td>
				<td><input type = "text" data-mini="true" placeholder = "Longitude"  id="Longitude"  name="Longitude"></td>
				<td><input type = "text" data-mini="true"  placeholder = "Address"   id="Address" name="Address"></td>
				
				<td><input type = "text"  data-mini="true" placeholder = "Phone"   id="Phone" name="Phone"></td>
				<td><input type = "text"  data-mini="true" placeholder = "Location Description"   id="LocationDescription" name="LocationDescription"></td>
				
				<td><a data-icon="plus"  data-mini="true"  data-theme="a" data-role="button" data-inline="true" id="Addlocationbtn"  onClick="Addlocation()" aclass="ui-disabled" >Add</a></td>
			
			</tr>
			
			<tr >
				<td ><input type="hidden" id="MaxRowNumber" value="0"></td>
			</tr>
		</table>
		
		
		
		
		<table id="DistributorLocationTableBody" style="width:93%; margin-top:35px;margin-bottom:35px;">
			
		</table>

	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="DistributorLocationSubmit()"  >Save</a>
						<button data-icon="check" data-theme="b" data-inline="true" id="ResetButton" onClick="javascript:window.location='F370AddDistributorLocation.jsp'" >Reset</button>
						
					</td>
	                <!-- <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ViewAllButton" >View All</a>
					</td> -->
				</tr>
			</table>
		</div>
    </div>
    
   <%--  <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width: 500px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>View All</h1>
	    </div>
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <ul data-role="listview" data-filter="true" data-filter-placeholder="Search ..." data-inset="true">
			    
			    
			    <%
				ResultSet rs3 = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name FROM gl_customer_credit_limit where is_active=1 order by activated_on ");
				while( rs3.next() ){
					%>
					 <li><a href="#" onclick="window.location='GLCreditLimit.jsp?EditID=<%=rs3.getString("id")%>'"><%=rs3.getString("customer_name")%><span class="ui-li-count"><%= Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs3.getDouble("credit_limit"))%></span></a></li> 
					<%
				}
				%>
			    
			    
			</ul>
	    </div>
	</div> --%>
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackCashReceipt" name="CallBack" /> 
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>