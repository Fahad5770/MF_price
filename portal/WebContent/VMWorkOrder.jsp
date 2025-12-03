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

if(Utilities.isAuthorized(279, SessionUserID) == false){
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
        <script src="js/VMWorkOrder.js?31111111111111=31111111111111"></script>
        <script src="js/lookups.js"></script>
        
        
</head>


<body>

<div data-role="page" id="BrandExchange" data-url="BrandExchange" data-theme="d">

   <jsp:include page="Header2.jsp" >
    	<jsp:param value="Vehicle Maintenance Work Order" name="title"/>
   </jsp:include>
    
    <div data-role="content" data-theme="d">
    <form  name="VMWorkOrderMainForm" id="VMWorkOrderMainForm">

<input type="hidden" id="outlet_id" name="outlet_id"/>
<input type="hidden" id="out_region_id" name="out_region_id"/>
<input type="hidden" id="out_rsm_id" name="out_rsm_id"/>
<input type="hidden" id="out_snd_id" name="out_snd_id"/>
<input type="hidden" id="out_distributor_id" name="out_distributor_id"/>
<input type="hidden" id="out_channel_id" name="out_channel_id"/>

 <input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>	

<input type="hidden" name="VisibleTable" id="VisibleTable" value="0"/>
	
	
	
	<div  id="NormalOutletTable">
	<table border="0" style="width: 100%;"> 
	
	
	
	
		<tr style="font-size: 10pt; font-weight: 400;">
			
			
			
			<td style="width: 25%;">
			    Vehicle No. 
			</td>
			<td style="width: 25%;" >
				Vehicle Type:  
			</td>
            <td style="width: 25%;">
				Meter Reading
			</td>
			<td style="width: 25%;">
				Requested to 
			</td>
		</tr>
		<tr>
			
			
			<td style="width: 25%;">
				<input type="text" placeholder="" id="VMVehicleNo" name="VMVehicleNo" data-mini="true" value="" >
			</td>
			<td style="width: 25%;">
				<input type="text" placeholder="" id="VMVehicleType" name="VMVehicleType" data-mini="true" value="" >
				
			</td>
			<td style="width: 25%;">
					<input type="text" placeholder="" id="VMMeterReading" name="VMMeterReading" data-mini="true" value="" >
				</td>	
            <td style="width: 25%;">
					
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMRequestTo" name="VMRequestTo" data-mini="true" value="" onChange="getEmployeeNameRequestedTo();"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMRequestToName" name="VMRequestToName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
					
					
					
				</td>
		</tr>
		<tr style="font-size: 10pt; font-weight: 400;">
			
			
			
			
			<td style="width: 25%;">
				Requested By 
			</td>
			<td style="width: 25%;">
				 Inspected By
			</td>
			<td style="width: 25%;">
				Department
			</td>
            	<td style="width: 25%;" >
				Custodian
			</td>
		</tr>
		<tr>
				
				
				
				
				<td style="width: 25%;">
					
				
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMRequestBy" name="VMRequestBy" data-mini="true" value="" onChange="getEmployeeNameRequestedBy()"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMRequestByName" name="VMRequestByName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
				
				
				
				</td>
				<td style="width: 25%;">
					
					
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMInspectedBy" name="VMInspectedBy" data-mini="true" value="" onChange="getEmployeeNameInspectedBy()"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMInspectedByName" name="VMInspectedByName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
				</td>
				
            <td style="width: 25%;">
					
					<select data-mini="true" id="VMDepartment" name="VMDepartment">
				 				<option value="">Select Department</option>
				 				<% 
				 				ResultSet rs3 = s.executeQuery("SELECT distinct department FROM users where department !=''");
				 				while(rs3.next()){
				 				%>	
				 					<option value="<%=rs3.getString("department")%>" ><%=rs3.getString("department") %></option>
				 				<%	
				 				}
				 				%>
				 				
				 				
				 				
				 			</select>
					
					
					
				</td>
				<td style="width: 25%;" >
					
					
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMCustodian" name="VMCustodian" data-mini="true" value="" onChange="getEmployeeNameCustodian()"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMCustodianName" name="VMCustodianName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
				</td>
				
			
		</tr>
		
		<tr style="font-size: 10pt; font-weight: 400;">
			
			
			
			<td style="width: 25%;">
				Job Assigned By 
			</td>
			<td style="width: 25%;">
			    Job Assign To
			</td>
			<td style="width: 25%;">
				Job Confirmed By
			</td>
			<td style="width: 25%;" >
				Job Completion Date
			</td>
            
		</tr>
		<tr>
				
				
				<td style="width: 25%;">
					
				
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMJobAssignedBy" name="VMJobAssignedBy" data-mini="true" value="" onChange="getEmployeeNameAssignedBy()"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMJobAssignedByName" name="VMJobAssignedByName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
				
				</td>
				<td style="width: 25%;">
					
				
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMJobAssignedTo" name="VMJobAssignedTo" data-mini="true" value="" onChange="getEmployeeNameAssignedTo()"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMJobAssignedToName" name="VMJobAssignedToName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
				</td>
				
            <td style="width: 25%;">
					
				
					<table style="width:100%; border-collapse:collapse;" border="0" >
						<tr>
							<td style="width:30%"><input type="text" placeholder="" id="VMJobConfirmedBy" name="VMJobConfirmedBy" data-mini="true" value="" onchange="getEmployeeNameConfirmedBy()"/></td>
							<td style="width:70%"><input type="text" placeholder="" id="VMJobConfirmedByName" name="VMJobConfirmedByName" data-mini="true" value="" readonly/></td>
						</tr>
					</table>
				</td>
				<td style="width: 25%;" >
					<input type="text" placeholder="" id="VMJobCompletedDate" name="VMJobCompletedDate" data-mini="true" value="" >
					
				</td>
				
		</tr>
		
		
		
		
		 
		
		 
		 
		
		</table>
	</div>	
		
		
		
		<br/>
		 
		 
		 <div class="ui-grid-a" style="width:100%;">
			<div class="ui-block-a">
				<table  border="0" style="width: 75%;">
				 	<tr>
				 		<td colspan="4">In-House Activities</td>
				 	</tr>
				 	
				 	<tr>
				 		<td style="w1idth:40%;">
				 			<select data-mini="true" id="VMInHouseAct" name="VMInHouseAct">
				 				<option value="">Select Type</option>
				 				<% 
				 				ResultSet rs = s.executeQuery("SELECT * FROM vm_activities where is_outsource=0");
				 				while(rs.next()){
				 				%>	
				 					<option value="<%=rs.getLong("id")%>" ><%=rs.getString("label") %></option>
				 				<%	
				 				}
				 				%>
				 				
				 				
				 				
				 			</select>
				 		</td>
				 		
			
				 		<td style="w1idth:20%;">
				 		<input type="button" value="Add" data-mini="true" id="SelectPCItemBtn" name="SelectPCItemBtn" onClick="AddSalesVolRows()"/>
				 		</td>
				 		<td style="w1idth:40%;"></td>
				 	</tr>
		 	 </table>
		 	 
		 	 <table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;width: 100%;" border="0">
		  <thead>
		    
		    
		    
		    <tr class="ui-bar-c">
				 <input type="hidden" id="VMHouseActRowCount" value="1"/>
				
				<th data-priority="1">Title</th>
				
				<th data-priority="1" ></th>
				
		    </tr>
		  </thead>
		  
			<tbody id="VMInHouseRow">
			<tr id="NoProductRow">
				<td colspan="6" style="margin: 1px; padding: 0px;">
					<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
				</td>
			</tr>
			</tbody>
		</table>
				
			</div>
			<div class="ui-block-b" >
				<table   style="width: 75%;">
				 	<tr>
				 		
				 		<td colspan="4">Outsource Activities</td>
				 	</tr>
				 	
				 	<tr>
				 		
				 		<td style="w1idth:40%;">
				 			<select data-mini="true" id="SelectOutSourceType" name="SelectOutSourceType" onChange="LoadPackageByType()">
				 				<option value="">Select Type</option>
				 				<% 
				 				ResultSet rs1 = s.executeQuery("SELECT * FROM vm_activities where is_outsource=1");
				 				while(rs1.next()){
				 				%>	
				 					<option value="<%=rs1.getLong("id")%>" ><%=rs1.getString("label") %></option>
				 				<%	
				 				}
				 				%>
				 				
				 				
				 				
				 			</select>
				 		</td>
				 		
			
				 		<td style="w1idth:20%;">
				 		<input type="button" value="Add" data-mini="true" id="SelectPCItemBtn" name="SelectPCItemBtn" onClick="AddOutsourcingRows()"/>
				 		</td>
				 		<td style="w1idth:40%;"></td>
				 	</tr>
		 	 </table>
			<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt;width: 100%;">
		  <thead>
		    
		    
		    
		    <tr class="ui-bar-c">
				 <input type="hidden" id="VMOutsourceRowCount" value="1"/>
				<th data-priority="1">Title</th>
				
				<th data-priority="1" ></th>
				
		    </tr>
		  </thead>
		  
			<tbody id="VMOutSourceRow">
			<tr id="NoProductRow1">
				<td colspan="6" style="margin: 1px; padding: 0px;">
					<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
				</td>
			</tr>
			</tbody>
		</table>
			</div>	   
		</div>
		 
		 
		 
		 
		 
		 

		 			
		 	
		 	
		 	
		 	
		 	
		 	
		 	<br/>
		 	
		 	<table  border="0" style="width: 100%;">
		 	
		 	
		 	
		 	
		 	
		 	<tr>
		 		<td>Parts Issuance Detail</td>
		 		<td>In-House Activities  Cost</td>
		 		<td>Outsource Activities Cost</td>
		 		<td>Total Cost</td>
		 	</tr>
		 	
		 	<tr>
		 		<td><input type="text" name="PartsIssuanceDetail" data-mini="true"/></td>
		 		<td><input type="text" name="InHouseCost" id="InHouseCost" data-mini="true" onChange="CalculateTotalCost()"/></td>
		 		<td><input type="text" name="OutSourceCost" id="OutSourceCost" data-mini="true" onChange="CalculateTotalCost()"/></td>
		 		
		 		<td><input type="text" name="TotalCost" id="TotalCost" data-mini="true" readonly/></td>
		 		
		 	
		 	</tr>
		 	<tr>
		 		<td colspan="4">
		 			<textarea name="comments" id="comments" placeholder="Comments"></textarea>
		 		</td>
		 	</tr>
		 	
		 	
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="CoolerInjectionSaveBtn" href="#"  onClick="VMWorkOrderSave();">Save</a>
                    
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