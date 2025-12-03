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

<jsp:useBean id="bean" class="com.pbc.inventory.DamagedStock" scope="page"/>
<jsp:setProperty name="bean" property="*"/>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(38, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

String EditID = "0";
boolean isEditCase = false;

if( request.getParameter("DamagedStockID") != null ){
	EditID = request.getParameter("DamagedStockID");
	isEditCase = true;
}

String SessionUserDistributorName = (String)session.getAttribute("UserDistributorName");

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/DamagedStock.js"></script>
        
        <script>
		
		<% if( isEditCase){ %>
		
			getDamagedStockInfoJson('<%=EditID%>');
		
		<% } %>
		
		
		
        </script>
        
</head>

<body>

<div data-role="page" id="DamagedStock" data-url="DamagedStock" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Damaged Stock" name="title"/>
    	<jsp:param value="<%=SessionUserDistributorName%>" name="HeaderValue"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<table style="width: 100%">
		<tr>
			<td style="width: 35%;" colspan="2">
				<input type="text" placeholder="Remarks" id="DamagedStockRemarks" name="DamagedStockRemarks" data-mini="true">
			</td>			
		</tr>
		</table>
	</li>
    
    <li data-role="list-divider">Products</li>
    <li>	
	<form id="DamagedStockForm" data-ajax="false" action="#DamagedStock" onSubmit="return DamagedStockAddProduct();">		
    <table>
		<tr>
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			<td valign="top">
				<input type="text" placeholder="Product Code" id="DamagedStockProductCode" name="DamagedStockProductCode" data-mini="true" onBlur="getProductInfoJson(this.value)" >
			</td>
			<td valign="top">
				<input  type="text" placeholder="Raw Cases" id="DamagedStockRawCases" name="DamagedStockRawCases" data-mini="true">
			</td>
			<td valign="top">
				<input  type="text" placeholder="Bottles" id="DamagedStockUnits" name="DamagedStockUnits" data-mini="true">
			</td>
			<td valign="top">
				<input  type="text" placeholder="Batch Code" id="DamagedStockBatchCode" name="DamagedStockBatchCode" data-mini="true">
			</td>
            <td valign="top">
				<select name="DamagedStockType" id="DamagedStockType" data-mini="true" data-inline="true">
					<option value="">Select Type</option>
                	
                </select>
			</td>
			
			<td valign="top">
				<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
				
			</td>
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	</form>

	<form action="test2.jsp" name="DamagedStockMainForm" id="DamagedStockMainForm">
    
		<input type="hidden" name="DamagedStockEditID" id="DamagedStockEditID" value="<%=EditID%>" >
        <input type="hidden" name="DamagedStockMainFormRemarks" id="DamagedStockMainFormRemarks" value="" >
        
		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;">
		  <thead>
		    
		    <tr class="ui-bar-c">
				<th data-priority="1">Product Code</th>
				<th data-priority="1">Package</th>
				<th data-priority="1">Brand</th>
				<th data-priority="1" >Raw Cases</th>
				<th data-priority="1" >Bottles</th>
				<th data-priority="1" >Batch Code</th>
                <th data-priority="1" >Damage Type</th>
				<th data-priority="1"></th>
		    </tr>
		  </thead>
		  
			<tbody id="DamagedStockTableBody">
			<tr id="NoProductRow">
				<td colspan="8" style="margin: 1px; padding: 0px;">
					<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
				</td>
			</tr>
			</tbody>
		</table>
		
	</form>
	</li>
	</ul>
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DamagedStockSave" href="#" class="ui-disabled" onClick="DamagedStockSubmit();">Save</a>
                    <button data-icon="check" data-theme="b" data-inline="true" id="DamagedStockReset" onClick="javascript:window.location='DamagedStock.jsp'" >Reset</button>
				</td>
                <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DamagedStockSearch" >Search</a>
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

			<form data-ajax="false" id="DamagedStockFormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>
                        <span id="FromDateSpan"><input type="text" data-mini="true" name="DamagedStockFromDate" id="DamagedStockFromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="DamagedStockToDate" id="DamagedStockToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="DamagedStockDateButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="SearchContent">
        </div>
            
        </div>
    </div>

</div>

</body>
</html>
<%

bean.close();
%>