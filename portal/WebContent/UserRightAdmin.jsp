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

if(Utilities.isAuthorized(110, SessionUserID) == false){
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
        <script src="js/UserRightAdmin.js"></script>
        
        
        
</head>

<body>

<div data-role="page" id="UserRight" data-url="UserRight" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="User Administration" name="title"/>
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
			</tr>
			
		</table>
	</li>
    
    <li data-role="list-divider">Features</li>
    
   </ul>   	
    	<div style="width:50%;; min-height:500px; ba1ckground-color:red; float:left; margin-top:20px;">
    		<div id="ForCheckBoxID" style="b1ackground-color:yellow; width:95%">
    
   <% 
    		int counter=0;
            ResultSet rs = s.executeQuery("select group_id, group_name from feature_groups where active = 1 order by sort_order");
            while(rs.next()){
            %>
            <div data-role="collapsible" data-collapsed="true" data-theme="c" data-content-theme="d" id="AdditionalRightDiv" data-mini="true">
	    		<h4><%=rs.getString(2) %></h4>
			    		    
				    <%
	            	ResultSet rs2 = s2.executeQuery("select feature_id,feature_name, url, ajax_call from features where group_id = "+rs.getString(1)+" and active=1 order by sort_order");
	            	%>
	            	
				   
				    <fieldset data-role="controlgroup" > 
	            	<%
				    while(rs2.next()){
	            		%>
				   <div style="width:80%; float:left;">
				    
				    
				    
    
   						<input type="checkbox" name="FeatureCheckbox" id="FeatureCheckbox<%=rs2.getString(1) %>" value="<%=rs2.getString(1)%>" data-mini="true">
    					<label for="FeatureCheckbox<%=rs2.getString(1) %>" ><%=rs2.getString(2)%> </label>
    
					
				    
				    
				    </div>
				
				   <div style="width:10%; float:left; text-align:left; margin-left:10px;">
				   <% if( rs2.getString(1).equals("46") ){ %>
				   <a href="#" id="AdditionalUserRightsAddBtnID<%=rs2.getString(1) %>" data-role="button" data-corners="false" data-mini=true data-icon="arrow-r" data-iconpos="notext" data-inline="true" class="ui-disabled" onClick="SetTitle('<%=rs2.getString(2)%>','<%=rs2.getString(1)%>')">Add</a>
				   <% } %>
				   
				   </div>
	            		
	            		
	            		
					<% counter++;} %>
					 </fieldset>   
			
				   
				   
				   
				
			</div>
    		<%
            }
            %>
  </div>
    		</div>
    		<div style="width:50%;; min-height:200px; b1ackground-color:green; float:left;margin-top:20px;">
			   <input type="hidden" name="FeatureIDFOrWhole" id="FeatureIDFOrWhole"/> 
			<!-- <div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" style="display:none;">
	    		<h4 id="TitleGoesHere">Data Scope</h4>
	    		<input type="hidden" name="FeatureIDFOrWhole" id="FeatureIDFOrWhole"/>				
					<div data-role="fieldcontain">
					 	<fieldset data-role="controlgroup">
							<input type="checkbox" name="DoNotSpecify"  id="DoNotSpecify" class="custom" checked onClick="EnableCheckBoxes()"/>
							<label for="DoNotSpecify">Do not Specify</label>
							<input type="checkbox" name="Warehouse" id="Warehouse" value="Warehouse" class="custom" disabled onClick="LoadWareHouse()"/>
							<input type="hidden" id="LoadWareHouseToggle" value="0" />
							<label for="Warehouse">Warehouse</label>
							<input type="checkbox" name="Region" id="Region" class="custom" value="Region" disabled onClick="LoadRegion()"/>
							<input type="hidden" id="LoadRegionToggle" value="0" />
							<label for="Region">Region</label>
							<input type="checkbox" name="Distributor" id="Distributor" class="custom" value="Distributor" disabled onClick="LoadDistributor()"/>
							<input type="hidden" id="LoadDistributoriToggle" value="0" />
							<label for="Distributor">Distributor</label>
					    </fieldset>
					</div>
				</div> -->
				<div style="width:100%; min-height:300px; b1ackground-color:red; float:left;">
					
					<div id="LoadDataDrpDwn">
					
					
					</div>
					
					
					
				</div>
    		</div>
    		
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