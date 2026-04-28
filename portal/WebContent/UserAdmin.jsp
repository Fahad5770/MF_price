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

if(Utilities.isAuthorized(192, SessionUserID) == false){
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
        <script src="js/UserAdmin.js"></script>
        
        
        
</head>

<body>

<div data-role="page" id="UserRight" data-url="UserRight" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="User Admin" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	<form id="UserRightForm" data-ajax="false" >

	<input type="hidden" id="isSecondDistCall" value="0"/>
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	
		<table border="0" style="width: 100%">
			<tr>
			<td style="width:25%">
				<select name="SaleTypeID" id="SaleTypeID" data-mini="true">
				    <option value="-1">Select Type</option>
				    <option value="1" selected>Primary</option>
				    <option value="2">Secondary</option>				   
				</select>
			</td>
			
				<td id="DistributroIDTD" >
					<table border="0" width="100%">
					<tr>
						<td style="width:30%">
							<input type="text" name="DistributorID" id="DistributorID" placeholder="Distributor ID" data-mini="true" onChange="getDistributorName()" />
						</td>
						<td style="width:70%">
							<input type="text" name="DistributorName" id="DistributorName" placeholder="Distributor Name" data-mini="true" readonly /> 
							<input type="hidden" name="isEdit" id="isEdit" value="0"/>
							<input type="hidden" name="isEditForSelection" id="isEditForSelection" value="0"/>
							
						</td>
					</tr>
					</table>
					
					 
					
				</td>
				<td   id="DistributroNameTD" >
					&nbsp;
				</td>
				<td>&nbsp;</td>
			
			</tr>
			
			<tr>
			
			<td style="width: 25%;"><input type="text" name="SapCode" id="SapCode" placeholder="SAP Code" title="SAP Code" data-mini="true" maxlength="15"/></td>
			
			<td style="width: 25%;"><input type="password" name="password" id="password"  placeholder="Password"  title="Password data-mini="true"/></td>
			
			<td style="width: 25%;"><input type="text" name="FirstName" id="FirstName" placeholder="First Name" title="First Name" data-mini="true"/></td>
			<td style="width: 25%;"><input type="text" name="LastName" id="LastName" placeholder="Last Name"  title="Last Name" data-mini="true"/></td>
			</tr>
			<tr>
			<td style="width: 25%;"><input type="text" name="DisplayName" id="DisplayName" placeholder="Display Name" title="Display Name" data-mini="true"/></td>			
			<td style="width: 25%;"><input type="text" name="Email" id="Email" placeholder="Email" title="Email" data-mini="true"/></td>
			<td style="width: 25%;"><input type="text" name="Disignation" id="Disignation" placeholder="Designation" title="Designation" data-mini="true"/></td>			
			<td style="width: 25%;"><input type="text" name="Department" id="Department" placeholder="Department" title="Department" data-mini="true"/></td>
			
			</tr>
			<tr>
			<td ><input type="text" name="payrollId" id="payrollId" placeholder="Payroll ID" title="Payroll ID" data-mini="true"/></td>
			
			<td>
			<select name="UserRightDistributorGrp" id="UserRightDistributorGrp" data-mini="true">
			<option value='0'>Select Distributor Group</option>
			<%
			ResultSet rs5 = s.executeQuery("SELECT * FROM common_distributor_groups");
			while(rs5.next()){
				%>
				<option value='<%=rs5.getString("id")%>'><%=rs5.getString("label")%></option>
			<%}			
			%>
			</select>
			</td>
			
			<td>
			<select name="UserRightUserActive" id="UserRightUserActive" data-mini="true">
				<option value="1">Active</option>
				<option value="0">In Active</option>
			</select>
			</td>
			
			<td>
			    <label for="UserRightUserBA"></label>
			    <select name="UserRightUserBA" id="UserRightUserBA" data-mini="true">
			    	<option value="-1">Select Is Brand Ambassador </option>
			        <option value="1">Yes</option>
			        <option value="0">No</option>
			    </select>
			</td>

			
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#" onClick="UserRightSubmit();">Save</a>
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='UserRightAdmin.jsp'" >Reset</button>
                   
				</td>
                <td align="right">
                    <a href="#"  data-icon="check" data-theme="b" data-role="button" data-inline="true"   id="UserSearchDateButton" onclick="UserSearch();">Search</a>
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
    
    <jsp:include page="LookupUserSearchPopup.jsp" > 
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