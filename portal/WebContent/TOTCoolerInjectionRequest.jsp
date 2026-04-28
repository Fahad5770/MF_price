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

if(Utilities.isAuthorized(206, SessionUserID) == false){
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
        <script src="js/TOTCoolerInjectionRequest.js?31112341111=31112341111"></script>
        <script src="js/lookups.js"></script>
        
        
</head>


<body>

<div data-role="page" id="BrandExchange" data-url="BrandExchange" data-theme="d">

   <jsp:include page="Header2.jsp" >
    	<jsp:param value="Cooler Injection" name="title"/>
   </jsp:include>
    
    <div data-role="content" data-theme="d">
    <form  name="CoolerInjectionMainForm" id="CoolerInjectionMainForm">

<input type="hidden" id="outlet_id" name="outlet_id"/>
<input type="hidden" id="out_region_id" name="out_region_id"/>
<input type="hidden" id="out_rsm_id" name="out_rsm_id"/>
<input type="hidden" id="out_snd_id" name="out_snd_id"/>
<input type="hidden" id="out_distributor_id" name="out_distributor_id"/>
<input type="hidden" id="out_channel_id" name="out_channel_id"/>

 <input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>	

<input type="hidden" name="VisibleTable" id="VisibleTable" value="0"/>
	
	<table border="0" style="width:100%;">
	<tr>
		<td style="width: 25%;" >
					 <input type="checkbox" name="NewOutletBox" id="NewOutletBox" data-mini="true" value= "1" onClick="ShowHideTable();">
    					<label for="NewOutletBox">New Outlet</label>
					
				</td>
	</tr>
	</table>
	
	<div  id="NormalOutletTable">
	<table border="0" style="width: 100%;"> 
	
	
	
	
		<tr style="font-size: 10pt; font-weight: 400;">
			
			<td style="width: 25%;" >
				Outlet ID
			</td>
			<td style="width: 25%;">
				Outlet Name 
			</td>
			<td style="width: 25%;">
			    Outlet Address
			</td>
			<td style="width: 25%;" >
				Contact Number
			</td>
            
		</tr>
		<tr>
				
				
				<td style="width: 25%;" >
					<input type="text" placeholder="" id="ProductCoolerInjectionOutletID" name="ProductCoolerInjectionOutletID" data-mini="true" value="" onchange="getCoolerInjectionInfoJson()">
					
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletName" name="OutletName" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletAddress" name="OutletAddress" data-mini="true" value="" >
				</td>
				<td style="width: 25%;" >
					<input type="text" placeholder="" id="OutletContactNumber" name="OutletContactNumber" data-mini="true" value="" >
					
				</td>
				
            
		</tr>
		<tr style="font-size: 10pt; font-weight: 400;">
			
			
			<td style="width: 25%;">
				Outlet Contact Name
			</td>
			<td style="width: 25%;">
				CNIC 
			</td>
			<td style="width: 25%;">
				Channel 
			</td>
			<td style="width: 25%;">
				Meter Number 
			</td>
			
            
		</tr>
		<tr>
				
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletContactName" name="OutletContactName" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletContactNic" name="OutletContactNic" data-mini="true" value="" >
				</td>
				
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletChannel" name="OutletChannel" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="MeterNumber" name="MeterNumber" data-mini="true" value="" >
				</td>
				
            
		</tr>
		
		<tr style="font-size: 10pt; font-weight: 400;">
			<td style="width: 25%;">
				Region
			</td>
			
			<td style="width: 25%;" >
				RSM
			</td>
			<td style="width: 25%;">
				SND 
			</td>
			<td style="width: 25%;">
			    Distributor
			</td>
			
			
            
		</tr>
		<tr>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletRegion" name="OutletRegion" data-mini="true" value="" >
				</td>
				<td style="width: 25%;" >
					<input type="text" placeholder="" id="OutletRSM" name="OutletRSM" data-mini="true" value="" >
					
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletSND" name="OutletSND" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletDistributor" name="OutletDistributor" data-mini="true" value="" >
				</td>
				
            
		</tr>
		
		
		 
		
		 
		 
		
		</table>
	</div>	
		<!--  New Outlet -->
		
		<div id="NewOutletTable" style="display:none;">
		<table border="0" style="width: 100%; " >
	
	
	
	
		<tr style="font-size: 10pt; font-weight: 400;">
			
			
			<td style="width: 25%;">
				Outlet Name 
			</td>
			<td style="width: 25%;">
			    Outlet Address
			</td>
			<td style="width: 25%;" >
				Contact Number
			</td>
			<td style="width: 25%;">
				Outlet Contact Name
			</td>
            
		</tr>
		<tr>
				
				
				
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletNameN" name="OutletNameN" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletAddressN" name="OutletAddressN" data-mini="true" value="" >
				</td>
				<td style="width: 25%;" >
					<input type="text" placeholder="" id="OutletContactNumberN" name="OutletContactNumberN" data-mini="true" value="" >
					
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletContactNameN" name="OutletContactNameN" data-mini="true" value="" >
				</td>
				
            
		</tr>
		<tr style="font-size: 10pt; font-weight: 400;">
			
			
			<td style="width: 25%;">
				CNIC 
			</td>
			<td style="width: 25%;">
				Channel 
			</td>
			<td style="width: 25%;">
			    Distributor ID
			</td>
			<td>
				Distributor Name
			</td>
			
            
		</tr>
		<tr>
				
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletContactNicN" name="OutletContactNicN" data-mini="true" value="" >
				</td>
				
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletChannelN" name="OutletChannelN" data-mini="true" value="" >
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletDistributorN" name="OutletDistributorN" data-mini="true" value="" onChange="getDistributorName12(); ">
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="" id="OutletDistributorNameN" name="OutletDistributorNameN" data-mini="true" value="" readonly>
				</td>
				
            
		</tr>
		<tr style="font-size: 10pt; font-weight: 400;">
			
			<td style="width: 25%;">
				Meter Number 
			</td>
		<tr>
		
		
		<td style="width: 25%;">
					<input type="text" placeholder="" id="MeterNumberN" name="MeterNumberN" data-mini="true" value="" >
				</td>
		</tr>
		
		
		
		 
		
		 
		 
		
		</table>
		</div>
		
		
		<br/>
		 <table  border="0" style="width: 75%;">
		 	<tr>
		 		<td colspan="4">Sales Volume-P/C: Per Year</td>
		 	</tr>
		 	
		 	<tr>
		 		<td>
		 			<select data-mini="true" id="SelectPCItemType" name="SelectPCItemType" onChange="LoadPackageByType()">
		 				<option value="">Select Type</option>
		 				<% 
		 				ResultSet rs = s.executeQuery("SELECT * FROM inventory_products_lrb_types");
		 				while(rs.next()){
		 				%>	
		 					<option value="<%=rs.getLong("id")%>" ><%=rs.getString("label") %></option>
		 				<%	
		 				}
		 				%>
		 				
		 				
		 				
		 			</select>
		 		</td>
		 		<td id="LRBTypePackgeFilterRow">
		 			<select data-mini="true" id="SelectPCItemPack" name="SelectPCItemPack">
		 				<option value="">Select Package</option>
		 				<% 
		 				ResultSet rs1 = s.executeQuery("SELECT * FROM inventory_packages");
		 				while(rs1.next()){
		 				%>	
		 					<option value="<%=rs1.getLong("id")%>"><%=rs1.getString("label") %></option>
		 				<%	
		 				}
		 				%>
		 			</select>
		 		</td>
		 		<td>
		 			<input type="text" placeholder="" id="SelectPCItemQuantity" name="SelectPCItemQuantity" data-mini="true" value="" >
		 		</td>
		 		<td>
		 		<input type="button" value="Add" data-mini="true" id="SelectPCItemBtn" name="SelectPCItemBtn" onClick="AddSalesVolRows()"/>
		 		</td>
		 		
		 	</tr>
		 	 </table>

		 			
		 	
		 	<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;width: 75%;">
		  <thead>
		    
		    <input type="hidden" id="SalesVolPerYearRowCount" value="1"/>
		    
		    <tr class="ui-bar-c">
				<th data-priority="1">LRB Type</th>
				<th data-priority="1">Package</th>				
				<th data-priority="1" >Quantity</th>
				<th data-priority="1" ></th>
				
		    </tr>
		  </thead>
		  
			<tbody id="SalesVolPerYearRow">
			<tr id="NoProductRow">
				<td colspan="6" style="margin: 1px; padding: 0px;">
					<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
				</td>
			</tr>
			</tbody>
		</table>
		 	
		 	
		 	
		 	
		 	<br/>
		 	
		 	<table  border="0" style="width: 100%;">
		 	<tr>
		 		<td style="text-align:left;">Beat Plan</td>
		 	</tr>
		 	
		 	<tr>
			
			 	<td style="width:17%;">
			 	 <input type="checkbox" name="outletbeatplan" id="BeatplanD7" data-mini="true" value="7"><label for="BeatplanD7">Saturday</label>
			 	
			 	</td>
			 	<td style="width:17%;"> 
			 		<input type="checkbox" name="outletbeatplan" id="BeatplanD1" data-mini="true" value="1"><label for="BeatplanD1">Sunday</label>
			 	</td>
			 	<td style="width:17%;"> 
			 		<input type="checkbox" name="outletbeatplan" id="BeatplanD2" data-mini="true" value="2"><label for="BeatplanD2">Monday</label>
			 	</td>
			 	<td style="width:17%;"> 
			 		<input type="checkbox" name="outletbeatplan" id="BeatplanD3" data-mini="true" value="3"><label for="BeatplanD3">Tuesday</label>
			 	</td>
			 	<td style="width:17%;"> 
			 		<input type="checkbox" name="outletbeatplan" id="BeatplanD4" data-mini="true" value="4"><label for="BeatplanD4">Wednesday</label>
			 	</td>
			 	<td style="width:17%;"> 
			 		<input type="checkbox" name="outletbeatplan" id="BeatplanD5" data-mini="true" value="5"><label for="BeatplanD5">Thursday</label>
			 	</td>
			 	<td style="width:17%;"> 
			 		<input type="checkbox" name="outletbeatplan" id="BeatplanD6" data-mini="true" value="6"><label for="BeatplanD6">Friday</label>
			 	</td>
		 	</tr>
		 	
		 	<tr>
		 		<td>&nbsp;</td>
		 		<td>&nbsp;</td>
		 	</tr>
		 	<tr>
		 		<td>Empty Status</td>
		 		<td>Existing TOT Size</td>
		 		<td>TOT Code</td>
		 	</tr>
		 	
		 	<tr>
		 		<td><input type="text" name="EmptyStatus" data-mini="true"/></td>
		 		<td>
		 			<select name="ExistingTOTSize" data-mini="true">
		 				<%
		 				ResultSet rs14 = s.executeQuery("SELECT * FROM common_assets_tot_sizes");
		 				while(rs14.next()){
		 				%>
		 				<option value="<%=rs14.getInt("id")%>"><%=rs14.getString("label") %></option>
		 				<%
		 				}
		 				%>
		 				
		 			</select>
		 		</td>
		 		
		 		<td><input type="text" name="TOTCode" data-mini="true"/></td>
		 		
		 	
		 	</tr>
		 	
		 	</table>
		 	
		 	<table  style='font-size: 10pt; width:100%; margin-top:10px'>

				<tr style="font-size:13pt;"><td>Existing TOT</td></tr>
				<tr>
					<th style="width:25%; text-align:left;">Asset Number</th>
					<th style="width:25%;text-align:left;">Inventory Number</th>	   							
					<th style="width:25%;text-align:left;">Tot Status</th>
					<th style="width:25%;text-align:left;">Movement Date</th>
					
				</tr>
	   						
				    	
			</table>
			<table  style='font-size: 10pt; width:100%;' id="ExistingTOTTable">
				
  						
  						
  						
		    	
			</table>
		 	
		 	
		 	<table>
		 	
		 	<tr>
		 		<td>&nbsp;</td>
		 		<td>&nbsp;</td>
		 	</tr>
		 	
		 	<tr>
		 		<td colspan="2" style="text-align:left;">Required TOT Size</td>
		 	</tr>
		 	
		 	<%
		 	ResultSet rs12 = s.executeQuery("SELECT * FROM common_assets_tot_sizes");
		 	while(rs12.next()){		 		
		 	
		 	%>
		 	
		 	<tr>
		 		<td ><input type="checkbox" name="TOTSize" id="TOTSize<%=rs12.getInt("id") %>" data-mini="true" value="<%=rs12.getInt("id") %>"><label for="TOTSize<%=rs12.getInt("id") %>"><%=rs12.getString("label") %></label></td>
		 		<td><input type="text" data-mini="true" name="TOTSizeQuantity<%=rs12.getInt("id") %>"/></td>
		 	</tr>
		 	
		 	<%
		 	}
		 	
		 	%>
		 	
		 	
		 	
		 	
		 	
		 	</table>
		 	
		 	
		 	
		</form> 	
		 	
		 	
		 	
		 
		
		
	
	
	
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="CoolerInjectionSaveBtn" href="#"  onClick="CoolerInjectionSave();">Save</a>
                    
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