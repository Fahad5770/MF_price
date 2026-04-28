<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="com.pbc.util.Utilities"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>


<%

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(450, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();

%>

<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/CreateDistributer.js?33655=6555"></script>
        
        
        
</head>

<body>

<div data-role="page" id="UserRight" data-url="UserRight" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Create Distributer" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	<form id="CreateDistributorForm" data-ajax="false" >

	<input type="hidden" id="isSecondDistCall" value="0"/>
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	
		<table border="0" style="width: 100%">
			<tr>
			<td   id="DistributroNameTD" >
					&nbsp;
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
			<td style="width: 25%;"><input type="text" name="DistributorID" id="DistributorID" placeholder="Distributor ID" title="Distributor ID" data-mini="true" required="required"/></td>
		
			<td style="width: 25%;"><input type="text" name="DistributorName" id="DistributorName" placeholder="Distributor Name" title="Distributor Name" data-mini="true" required="required"/></td>
			
			
			<td style="width: 25%;"><input type="text" name="Address" id="Address" placeholder="Address" title="Address" data-mini="true" required="required"/></td>
			
			<td>
			<select name="region" id="region" data-mini="true">
			<option value='0'>Select Region</option>
			<%
			ResultSet rs5 = s.executeQuery("SELECT * FROM common_regions");
			while(rs5.next()){
				%>
				<option value='<%=rs5.getString("region_id")%>'><%=rs5.getString("region_name")%></option>
			<%}			
			%>
			</select>
			
			</td>
			
			</tr>
			<tr>
			<td>
			<select name="city" id="city" data-mini="true">
			<option value='0'>Select City</option>
			<%
			ResultSet rs6 = s.executeQuery("SELECT * FROM common_cities");
			while(rs6.next()){
				%>
				<option value='<%=rs6.getString("id")%>'><%=rs6.getString("label")%></option>
			<%}			
			%>
			</select>
			
			</td>
			 <td style="width:15%"><a data-icon="plus" data-role="button" data-mini="true" style="width: 50%;" data-theme="b" id="add_city" href="#" onClick="addCity();" >New City</a></td>
			</tr>
		</table>
	</li>
    
   </ul>   	
    		</form>
	
    	
    	</div>
	
   
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#" onClick="CreateDistributor()">Save</a>
                                       
				</td>
              
			</tr>
		</table>
	</div>
	    	
    </div>
    
    
	
	<jsp:include page="LookupProductSearchPopup.jsp" > 
    	<jsp:param value="ProductSearchCallBack" name="CallBack" />
    </jsp:include><!-- 
    Include Product Search -->

	

	<jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackForUserRights" name="CallBack" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="AddCityPopup.jsp" > 
    	<jsp:param value="UserRightsUserEdit" name="CallBack" />
    </jsp:include><!-- Include User Search -->

 </div><!-- /content -->	
</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();
%>