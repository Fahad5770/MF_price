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

if(Utilities.isAuthorized(214, SessionUserID) == false){
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
        <script src="js/ProductDiscountsRequest.js?311112311=311112311"></script>
        <script src="js/lookups.js"></script>
        
        
</head>


<body>

<div data-role="page" id="BrandExchange" data-url="BrandExchange" data-theme="d">

   <jsp:include page="Header2.jsp" >
    	<jsp:param value="Product Discounts Request" name="title"/>
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
				<div id="ProductPromotionsDataScope" style="width:100%"></div>
			</td>
		</tr>
			<tr>
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
				
				<td >
				<input type="hidden" name="PriceListMasterTableID" id="PriceListMasterTableID" value="" />
					<input type="text" placeholder="DD/MM/YYYY" id="ProductPromotionsValidFrom" name="ProductPromotionsValidFrom" data-mini="true" value="" onkeyup="CustomValidation()">
				</td>
				<td>
					<input type="text" placeholder="DD/MM/YYYY" id="ProductPromotionsValidTo" name="ProductPromotionsValidTo" data-mini="true" value="" onkeyup="CustomValidation()">
				</td>
				<td >
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
	<div class="ui-grid" style="width:100%">
    	<div class="ui-block" style="border:1px solid #eee;">
    		<div class="ui-bar " style="min-height:60px">
    		<ul data-role="listview" data-inset="false" data-divider-theme="c">
    		<li data-role="list-divider">Sales</li>
    		</ul>
    		<br/>
    		<table>
		<tr>
			<td valign="top" style="width:80%">
				<span id="SpanBrandExchangePackage">
					<select id='BrandExchangePackage' name='BrandExchangePackage' data-mini='true' >
						<option value=''>Select Package</option>
						<%
						ResultSet rs = s.executeQuery("SELECT distinct ip.package_id,(select label from inventory_packages ip where ip.id=ip.package_id) label FROM inventory_products_variable_costs ipvc join inventory_products ip on ipvc.sap_code=ip.sap_code order by label");
						while( rs.next() ){ 
							%>
							<option value='<%=rs.getInt("package_id")%>'><%=rs.getString("label")%></option>
							<%
						}
						
						%>
					</select>
				</span>
			</td>
		</tr>
		<tr>
			<td style="width:100%">
				
				
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
				<a data-role="button" data-ajax="false"  data-inline="true" data-mini="true" data-icon="plus" href="javascript:AddSalesToTable()">Add</a>
				
			</td>
		</tr>
		<tr>
			
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	

	
    
		<input type="hidden" name="BrandExchangeEditID" id="BrandExchangeEditID" value="<%=EditID%>" >
        <input type="hidden" name="BrandExchangeMainFormRemarks" id="BrandExchangeMainFormRemarks" value="" >
        <div style="width: 100%">
		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;">
		  <thead>
		    
		    <tr class="ui-bar-c">
				<th data-priority="1">Package</th>
				<th data-priority="1">Brand</th>
				<th data-priority="1">Quantity</th>
				<th data-priority="1">Distributor Rate</th>
				<th data-priority="1">Selling Price</th>
				<th data-priority="1">Variable Cost + Taxes</th>
				<th data-priority="1">Other Cost</th>
				<th data-priority="1">Promotion Cost</th>
				<th data-priority="1">Marginal Contribution</th>				
				
				
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
    	
    	
	</div><!-- /grid-a -->		
    
	
	</li>
    
    
	
	<li>	
	<div class="u1i-grid-a">
    	<div class="u1i-block-a" style="border:1px solid #eee;">
    		<div class="ui-bar " style="min-height:60px">
    		<ul data-role="listview" data-inset="false" data-divider-theme="c">
    		<li data-role="list-divider">Reason</li>
    		</ul>
    		<br/>
    		
	<textarea rows="10" cols="50" placeholder="Reason" id="comments" name="comments"></textarea>

	
    
		
      
    		</div>
    	</div>
    	
    	
	</div><!-- /grid-a -->		
    
	<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
	</li>
	
  
	
   
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
                    <button data-icon="check" data-theme="b" data-inline="true" id="BrandExchangeReset" onClick="javascript:window.location='ProductDiscountsRequest.jsp'" >Reset</button>
					<a href="#popupDialogDeactivate" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ProductPromotionDeactivate" class="ui-disabled" >Deactivate</a>
				</td>
                <td align="right">

				</td>
			</tr>
		</table>
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