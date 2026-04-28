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
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>



<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(42, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

boolean isEditCase = false;
int EditID = Utilities.parseInt(request.getParameter("EmployeeProductGroupID"));
if(EditID > 0){
	isEditCase = true;
}


%>
<html>


<head>
        <script>
        	var GlobalEditID = <%=EditID%>;
        </script>
      		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
      		<script src="js/EmployeeProductGroup.js"></script>
          
</head>

<body>

<div data-role="page" id="EmployeeProductGroup" data-url="EmployeeProductGroup" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Employee Product Group" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
    
    	<ul data-role="listview" data-inset="false" data-divider-theme="c">
    	
    	<table style="width:100%; padding-top:10px; padding-bottom:10px">
    		<tr>
    			<td>
    				<input type="text" placeholder="Product Group Name" id="EmployeeProductGroupName" name="EmployeeProductGroupName" data-mini="true" >
    			</td>
    		</tr>
    	</table>
    	
    	
    	<li data-role="list-divider">Products</li>
    	<form id="EmployeeProductGroupForm" name="EmployeeProductGroupForm" data-ajax="false" action="#EmployeeProductGroupForm">	
    	
    	<input type="hidden" name="EmployeeProductGroupFormName" id="EmployeeProductGroupFormName" value="" >
    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
    	
    	<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:50%; margin-top:20px; margin-left:20px; margin-bottom:20px;">
    		<thead>
			    <tr class="ui-bar-c">
					<th data-priority="1">
						<select name="selectAll" id="selectAll" data-role="slider" data-mini="true" onchange="setAllSliders()">
						    <option value="off" selected=""></option>
						    <option value="on"></option>
						</select>
					</th>
					<th data-priority="1">Package</th>
					<th data-priority="1">Brand</th>
					<th data-priority="1">Product Code</th>				
					
			    </tr>
			</thead>
    	<%
    	ResultSet rs = s.executeQuery("SELECT category_id, package_id, brand_id, (select label from inventory_packages where id=package_id) package_name, (select label from inventory_brands where id=brand_id) brand_name, sap_code, id FROM inventory_products where category_id=1 order by package_name, brand_name");
    	while(rs.next()){
    		%>
    		<tr>
    			<td>
    				<input type="hidden" name="ProductID" id="ProductID" value="<%=rs.getString("id")%>" >
    				<select name="IsSelected" id="IsSelected<%=rs.getString("id")%>" data-role="slider" data-mini="true">
					    <option value="off"></option>
					    <option value="on"></option>
					</select>
    			</td>
    			<td><%=rs.getString("package_name")%></td>
    			<td><%=rs.getString("brand_name")%></td>
    			<td><%=rs.getString("sap_code")%></td>
    		</tr>
    		<%
    	}
    	%>
    	</table>
    	</ul>
    </form>
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="EmployeeProductGroupSave" href="#"  onClick="EmployeeProductGroupSubmit();">Save</a>
						<button data-icon="check" data-theme="b" data-inline="true" id="EmployeeProductGroupResetButton" onClick="EmployeeProductGroupReset()" >Reset</button>
					</td>
	                <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="EmployeeProductGroupSearch" >Search</a>
					</td>
				</tr>
			</table>
		</div>
	    	
    </div>


	<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			
        <div id="SearchContent">
        </div>
            
        </div>
    </div>

</div>

</body>
</html>

<%
c.close();
ds.dropConnection();
%>
