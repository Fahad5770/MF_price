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

<jsp:useBean id="bean" class="com.pbc.inventory.BrandExchange" scope="page"/>
<jsp:setProperty name="bean" property="*"/>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(53, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp"); 
}

String EditID = "0";
boolean isEditCase = false;

if( request.getParameter("BrandExchangeID") != null ){
	EditID = request.getParameter("BrandExchangeID");
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/ProductPromotions.js?1=1"></script>
        <script src="js/lookups.js"></script>
        
        
</head>


<body>

<div data-role="page" id="BrandExchange" data-url="BrandExchange" data-theme="d">

   <jsp:include page="Header2.jsp" >
    	<jsp:param value="Product Promotions" name="title"/>
   </jsp:include>
    
    <div data-role="content" data-theme="d">
    <form  name="ProductPromotionsMainForm" id="ProductPromotionsMainForm">
	<input type="hidden" name="isEditCase" id="isEditCase" value="0"/>
	<input type="hidden" name="ProductPromotionMasterTableID" id="ProductPromotionMasterTableID" value="0"/>
	<input type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>"/>
	<input type="hidden" id="IsCheckedAllCheckboxes" value="0"/>
	<input type="hidden" id="IsCheckedAllCheckboxesSales" value="0"/>
	
	<table border="0" style="width: 100%;">
	
		<tr style="font-size: 10pt; font-weight: 400;">
			<td style="width: 25%;" >
				Product Promotion Name
			</td>
			<td style="width: 25%;">
				Valid From 
			</td>
			<td style="width: 25%;">
			    Valid To
			</td>
			<td style="width: 25%;">
				Active
			</td>
			
            
		</tr>
		<tr>
				<td style="width: 25%;" >
					<input type="text" placeholder="" id="ProductPromotionsLabel" name="ProductPromotionsLabel" data-mini="true" value="" >
					<input type="hidden" name="PriceListMasterTableID" id="PriceListMasterTableID" value="" />
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="DD/MM/YYYY" id="ProductPromotionsValidFrom" name="ProductPromotionsValidFrom" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="DD/MM/YYYY" id="ProductPromotionsValidTo" name="ProductPromotionsValidTo" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<select id="ProductPromotionsIsActive" name="ProductPromotionsIsActive" data-mini="true" class="ui-disabled">
					<option value="1" >Active</option>
					<option value="0" >Inactive</option>
					
					</select>
				</td>
            
		</tr>
		 
		
		</table>

	<br/><br/>
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	
    
    
    <li>	
	<div class="ui-grid-a">
    	<div class="ui-block-a" style="border:1px solid #eee;">
    		<div class="ui-bar " style="min-height:60px">
    		<ul data-role="listview" data-inset="false" data-divider-theme="c">
    		<li data-role="list-divider">Sales</li>
    		</ul>
    		<br/>
    		<table>
		<tr>
			<td valign="top" style="width:80%">
				<span id="SpanBrandExchangePackage">
					<%=bean.getPackageList()%>
				</span>
			</td>
		</tr>
		<tr>
			<td style="width:80%">
				
				
					<!--<select id='ProductPromotionsProductCodeIssue' name='ProductPromotionsProductCodeIssue' data-mini='true' >
						<option value="">Select Brand</option>						
					</select>-->
					<fieldset data-role="controlgroup" > 
					<div style="width:500px; float:left; font-weight:400; font-size:10pt;" id="SpanProductPromotionsIssue">
					
					</div>
					</fieldset>  
					
			</td>
		</tr>
		<tr>
			<td valign="top">
				<input  type="text" placeholder="Raw Cases" id="ProductPromotionsRawCases" name="ProductPromotionsRawCases" data-mini="true">
			</td>
			<tr>
			<td valign="top">
				<input  type="text" placeholder="Bottles" id="ProductPromotionsUnits" name="ProductPromotionsUnits" data-mini="true">
			</td>
			<td valign="top">
				<a data-role="button" data-ajax="false"  data-inline="true" data-mini="true" data-icon="plus" href="javascript:AddSalesToTable()">Add</a>
				
			</td>
		</tr>
		<tr>
			
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	

	
    
		<input type="hidden" name="BrandExchangeEditID" id="BrandExchangeEditID" value="<%=EditID%>" >
        <input type="hidden" name="BrandExchangeMainFormRemarks" id="BrandExchangeMainFormRemarks" value="" >
        <div style="width: 80%">
		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;">
		  <thead>
		    
		    <tr class="ui-bar-c">
				<th data-priority="1">Package</th>
				<th data-priority="1">Brand</th>				
				<th data-priority="1" >Raw Cases</th>
				<th data-priority="1" >Bottles</th>
				<th data-priority="1"></th>
		    </tr>
		  </thead>
		  
			<tbody id="ProductPromotionsTableBody">
			<tr id="NoProductRow">
				<td colspan="6" style="margin: 1px; padding: 0px;">
					<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
				</td>
			</tr>
			</tbody>
		</table>
		</div>
    		</div>
    	</div>
    	
    	<div class="ui-block-b" style="border:1px solid #eee;">
    		<div class="ui-bar " style="min-height:60px">
    		<ul data-role="listview" data-inset="false" data-divider-theme="c">
    		<li data-role="list-divider">Promotions</li>
    		</ul>
    		<br/>
    		<table>
		<tr>
			<td valign="top" style="width:20%">
				<span id="SpanBrandExchangePackagePromotions">
					<%=bean.getPackageListPromotions()%>
				</span>
			</td>
		</tr>
		<tr>	
			<td valign="top" style="width:20%">
				
				
					<!--<select id='ProductPromotionsPProductCodeIssue' name='ProductPromotionsPProductCodeIssue' data-mini='true' >
						<option value="">Select Brand</option>
						
					</select>-->
					
					<fieldset data-role="controlgroup" > 
					<div style="width:500px; float:left; font-weight:400; font-size:10pt;" id="SpanProductPromotionsIssuePromotions">
					
					</div>
					</fieldset>  
				
				
			</td>
		</tr>
		<tr>
			
			<td valign="top">
				<input  type="text" placeholder="Raw Cases" id="ProductPromotionsPRawCases" name="ProductPromotionsPRawCases" data-mini="true">
			</td>
		</tr>
		<tr>
			<td valign="top">
				<input  type="text" placeholder="Bottles" id="ProductPromotionsPUnits" name="ProductPromotionsPUnits" data-mini="true">
			</td>
			<td>
			<a data-role="button" data-ajax="false" data-inline="true" data-mini="true" data-icon="plus" href="javascript:AddSalesToTablePromotions()">Add</a>
			</td>
		</tr>
		<tr>
			
			<td valign="top">
				
				<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
			</td>
		</tr>
	</table>
		<input type="hidden" name="BrandExchangeEditID" id="BrandExchangeEditID" value="<%=EditID%>" >
        <input type="hidden" name="BrandExchangeMainFormRemarks" id="BrandExchangeMainFormRemarks" value="" >
        <div style="width: 80%">
		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;">
		  <thead>		    
		    <tr class="ui-bar-c">
				<th data-priority="1">Package</th>
				<th data-priority="1">Brand</th>				
				<th data-priority="1" >Raw Cases</th>
				<th data-priority="1" >Bottles</th>
				<th data-priority="1"></th>
		    </tr>
		  </thead>
		  
			<tbody id="ProductPromotionsTableBody1">
			<tr id="NoProductRow1">
				<td colspan="6" style="margin: 1px; padding: 0px;">
					<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
				</td>
			</tr>
			</tbody>
		</table>
		</div>
    		
    		</div>
    	</div>
	</div><!-- /grid-a -->		
    
	
	</li>
	
	
    <li>	
			
    
	
	</li>
	<li data-role="list-divider">Scope</li>
    <div id="ProductPromotionsDataScope" style="width:100%">
	
	</div>
	</form>
	
	</ul>
	
    </div><!-- /content -->
     <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackForUserRights" name="CallBack" />
    	<jsp:param value="53" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBackDeskSale" name="CallBack" />
    	<jsp:param value="53" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackLiftingReport" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="ProductPromotionsSave" href="#" class="ui-disabled" onClick="ProductPromotionsSubmit();">Save</a>
                    <button data-icon="check" data-theme="b" data-inline="true" id="BrandExchangeReset" onClick="javascript:window.location='ProductPromotions.jsp'" >Reset</button>
					<a href="#popupDialogDeactivate" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ProductPromotionDeactivate" class="ui-disabled" >Deactivate</a>
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
		<ul data-role="listview" data-inset="true"> 
			<li data-role="list-divider">Active Product Promotions</li>
			<%
				ResultSet rs1 = s.executeQuery("select * from inventory_sales_promotions where is_active=1");
			while(rs1.next())
			{
	        	Date CreatedOn = rs1.getDate("created_on");
	 		%>
	 		<li>
	 			<a data-ajax="false" href="#" onClick="LoadPerticularProductPromotions(<%=rs1.getString("id")%>)">
						<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("label") %></span>
						<span class="ui-li-count"><%= Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
						
				</a>
	 		</li>
	 		<%
	 		} 
	 		%> 
	 		
	 		<li data-role="list-divider">Deactivated Promotions</li>
			<%
				ResultSet rs2 = s.executeQuery("select * from inventory_sales_promotions where is_active=0");
			while(rs2.next())
			{
	        	Date CreatedOn = rs2.getDate("created_on");
	 		%>
	 		<li>
	 			<a data-ajax="false" href="#" onClick="LoadPerticularProductPromotions(<%=rs2.getString("id")%>)">
						<span style="font-size: 10pt; font-weight: 400;"><%=rs2.getString("label") %></span>
						<span class="ui-li-count"><%= Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
						
				</a>
	 		</li>
	 		<%
	 		} 
	 		%> 
 		</ul>      
			

            
        </div>
    </div>
    
    <div data-role="popup" id="popupDialogDeactivate" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Deactivate</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			
				<ul data-role="listview" data-inset="true"> 
				<li data-role="list-divider">Deactivate Product Promotion</li>
				<li>
				<textarea name="textarea" placeholder="Reason to deactivate" id="ProductPromotionReason"></textarea>
				<button  data-theme="b" data-inline="true" id="ProductPromotionDeactivateSubmit" onClick="DeactivateProductPromotion()" >Deactivate</button>
				</li>
		 		</ul> 
        </div>
        
    </div>

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
bean.close();
%>