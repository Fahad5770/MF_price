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
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.common.Distributor"%>

<jsp:useBean id="bean" class="com.pbc.inventory.BrandExchange" scope="page"/>
<jsp:setProperty name="bean" property="*"/>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 39;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp"); 
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);

String DistributorName = "";
long DistributorID = 0;

System.out.println("UserDistributor.length = "+UserDistributor.length);

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



String EditID = "0";
boolean isEditCase = false;

if( request.getParameter("BrandExchangeID") != null ){
	EditID = request.getParameter("BrandExchangeID");
	isEditCase = true;
}


%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/BrandExchange.js"></script>
        
        <script>
		
		<% if( isEditCase){ %>
		
		getBrandExchangeInfoJson('<%=EditID%>');
		
		<% } %>
		
		
		
        </script>
        
</head>
<style>

	.ui-select{
		width:200px !important;
	}

</style>

<body>

<div data-role="page" id="BrandExchange" data-url="BrandExchange" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Brand Exchange" name="title"/>
    	<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<table style="width: 100%">
		<tr>
			<td style="width: 35%;" colspan="2">
				<input type="text" placeholder="Remarks" id="BrandExchangeRemarks" name="BrandExchangeRemarks" data-mini="true">
			</td>			
		</tr>
		</table>
	</li>
    
    <li data-role="list-divider">Products</li>
    <li>	
	<form id="BrandExchangeForm" data-ajax="false" action="BrandExchange" onSubmit="return BrandExchangeAddProduct();">		
    <table>
		<tr>
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			
			<td valign="top">
				<span id="SpanBrandExchangePackage">
					<%=bean.getPackageList()%>
				</span>
			</td>
			
			<td valign="top">
				
				<span id="SpanBrandExchangeProductCodeIssue">
					<select id='BrandExchangeProductCodeIssue' name='BrandExchangeProductCodeIssue' data-mini='true' >
						<option value="">Select Brand</option>
					</select>
				</span>
				
			</td>
			<td valign="top">
				<span id="SpanBrandExchangeProductCodeReceive">
					<select id="BrandExchangeProductCodeReceive" name="BrandExchangeProductCodeReceive" data-mini="true" >
						<option value="">Exchange To</option>
					</select>
				</span>
				
			</td>
			<td valign="top">
				<input  type="text" placeholder="Raw Cases" id="BrandExchangeRawCases" name="BrandExchangeRawCases" data-mini="true">
			</td>
			<td valign="top">
				<input  type="text" placeholder="Bottles" id="BrandExchangeUnits" name="BrandExchangeUnits" data-mini="true">
			</td>
			
			<td valign="top">
				<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
				
			</td>
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	</form>

	<form action="test2.jsp" name="BrandExchangeMainForm" id="BrandExchangeMainForm">
    
		<input type="hidden" name="BrandExchangeEditID" id="BrandExchangeEditID" value="<%=EditID%>" >
        <input type="hidden" name="BrandExchangeMainFormRemarks" id="BrandExchangeMainFormRemarks" value="" >
        <input type="hidden" name="UserDistributorID" id="UserDistributorID" value="<%=DistributorID%>" >
        
        
		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;">
		  <thead>
		    
		    <tr class="ui-bar-c">
				<th data-priority="1">Package</th>
				<th data-priority="1">Brand</th>
				<th data-priority="1">Exchange To</th>
				<th data-priority="1" >Raw Cases</th>
				<th data-priority="1" >Bottles</th>
				<th data-priority="1"></th>
		    </tr>
		  </thead>
		  
			<tbody id="BrandExchangeTableBody">
			<tr id="NoProductRow">
				<td colspan="6" style="margin: 1px; padding: 0px;">
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="BrandExchangeSave" href="#" class="ui-disabled" onClick="BrandExchangeSubmit();">Save</a>
                    <button data-icon="check" data-theme="b" data-inline="true" id="BrandExchangeReset" onClick="javascript:window.location='BrandExchange.jsp'" >Reset</button>
				</td>
                <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="BrandExchangeSearch" >Search</a>
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

			<form data-ajax="false" id="BrandExchangeFormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>
                        <span id="FromDateSpan"><input type="text" data-mini="true" name="BrandExchangeFromDate" id="BrandExchangeFromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="BrandExchangeToDate" id="BrandExchangeToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="BrandExchangeDateButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
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