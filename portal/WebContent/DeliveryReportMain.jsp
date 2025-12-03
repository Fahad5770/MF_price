<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/DeliveryReport.js"></script>
<script src="js/lookups.js"></script>
<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 40;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();

String Options = "<option value=''>Select Payment Method</option>";

ResultSet rs = s.executeQuery("select id, label from inventory_delivery_note_payment_methods order by label");
while( rs.next() ){
	Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
}

String VehicleTypeOptions = "<option value=''>Select Vehicle Type</option>";

ResultSet rs2 = s.executeQuery("select id, label from inventory_delivery_note_vehicle_types order by label");
while( rs2.next() ){
	VehicleTypeOptions += "<option value='"+rs2.getString("id")+"'>"+rs2.getString("label")+"</option>";
}

String RegionOptions = "<option value=''>All</option>";
Region [] RegionIdsObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
if(RegionIdsObj.length == 1)
{RegionOptions="";}
String RegionIds = UserAccess.getRegionQueryString(RegionIdsObj);
ResultSet rs6 = s.executeQuery("SELECT * from common_regions where region_id in("+RegionIds+") order by region_name");
while(rs6.next()){
	RegionOptions += "<option value='"+rs6.getString("region_id")+"'>"+rs6.getString("region_short_name")+" - "+rs6.getString("region_name")+"</option>";
}

/*String RegionOptions = "<option value=''>Select Region</option>";

ResultSet rs3 = s.executeQuery("select * from common_regions order by region_short_name");
while( rs3.next() ){
	RegionOptions += "<option value='"+rs3.getString("region_id")+"'>"+rs3.getString("region_short_name")+" - "+rs3.getString("region_name")+"</option>";
}*/

String WarehouseOptions = "<option value=''>All</option>";
Warehouse [] WarehouseIdsObj = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);
if(WarehouseIdsObj.length ==1)
{WarehouseOptions="";}
String WarehouseIds = UserAccess.getWarehousesQueryString(WarehouseIdsObj);
ResultSet rs5 = s.executeQuery("SELECT id,label from common_warehouses where id in("+WarehouseIds+") order by label");
while(rs5.next()){
	WarehouseOptions += "<option value='"+rs5.getString("id")+"'>"+rs5.getString("label")+"</option>";
}


/*boolean isWarehouseFound = false;
String WarehouseOptions = "";
ResultSet rs5 = s.executeQuery("SELECT warehouse_id, (select label from common_warehouses where id=warehouse_id) warehouse_label FROM user_access_warehouses where user_id="+SessionUserID+" and feature_id="+FeatureID+" order by warehouse_label");
while(rs5.next()){
	isWarehouseFound = true;
	WarehouseOptions += "<option value='"+rs5.getString("warehouse_id")+"'>"+rs5.getString("warehouse_label")+"</option>";
}

if(isWarehouseFound== false){
	WarehouseOptions = "<option val=''>All</option>";
	ResultSet rs6 = s.executeQuery("select id, label from common_warehouses order by label");
	while(rs6.next()){
		WarehouseOptions += "<option value='"+rs6.getString("id")+"'>"+rs6.getString("label")+"</option>";
	}
}*/

//distributor

Distributor [] DistributorObj = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);

String DistributorsIds = UserAccess.getDistributorQueryString(DistributorObj);

%>
<script>

var hours;
for(var i=0; i < 24; i++){
	var ZeroDigit = '';
	if(i < 10){
		ZeroDigit = '0';
	}
	hours += "<option value="+i+">"+ZeroDigit+i+"</option>";
}

var minutes;
for(var i=0; i < 60; i++){
	var ZeroDigit = '';
	if(i < 10){
		ZeroDigit = '0';
	}
	minutes += "<option value="+i+">"+ZeroDigit+i+"</option>";
}

var DistributorIds = '<%=DistributorsIds%>';
DistributorIds = DistributorIds.split(',');

</script>

<div data-role="page" id="DeliveryReportMain" data-url="DeliveryReportMain" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Delivery Report" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	<form id="DeliveryReportGenerrateForm" name="DeliveryReportGenerrateForm" action="DeliveryReport.jsp" method="POST" data-ajax="false">
		<ul data-role="listview" data-inset="true">
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainWarehouseID">Warehouse:</label>
	            <select name="LiftingReportMainWarehouseID" id="LiftingReportMainWarehouseID">
	            	<%=WarehouseOptions%>
	            </select>
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainRegion">Region:</label>
	            <select name="LiftingReportMainRegion" id="LiftingReportMainRegion">
	            	<%=RegionOptions%>
	            </select>
	        </li>
	        
	        <li data-role="fieldcontain">
	           
	            
	            <table style="width:100%; border-collapse:collapse;" border="0" >
            		<tr>
            			<td style="width:22%">
            				 <label for="LiftingReportMainDistributor">Distributor ID:</label>
            			</td>
            			<td style="width:10%">
            				<input type="text" name="LiftingReportMainDistributor" id="LiftingReportMainDistributor" onChange="getDistributorName()">
            			</td>
            			<td style="width:68%">
            				<input type="text" name="LiftingReportMainDistributorName" id="LiftingReportMainDistributorName" readonly="readonly">
            			</td>
            		</tr>
            	</table>
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainInvoiceNumber">Invoice #:</label>
	            <input type="text" name="LiftingReportMainInvoiceNumber" id="LiftingReportMainInvoiceNumber">
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainPaymentMethod">Payment Method:</label>
	            <select name="LiftingReportMainPaymentMethod" id="LiftingReportMainPaymentMethod">
	            	<%=Options%> 
	            </select>
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainSaleOrderNumber">SaleOrder #:</label>
	            <input type="text" name="LiftingReportMainSaleOrderNumber" id="LiftingReportMainSaleOrderNumber">
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainVehicleNumber">Vehicle #:</label>
	            <input type="text" name="LiftingReportMainVehicleNumber" id="LiftingReportMainVehicleNumber">
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainVehicleType">Vehicle Type #:</label>
	            <select name="LiftingReportMainVehicleType" id="LiftingReportMainVehicleType">
	            	<%=VehicleTypeOptions%>
	            </select>
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainRemakrs">Remarks:</label>
	            <input type="text" name="LiftingReportMainRemakrs" id="LiftingReportMainRemakrs">
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportBatchCode">Batch Code:</label>
	            <input type="text" name="LiftingReportBatchCode" id="LiftingReportBatchCode">
	        </li>
	        
	        <li data-role="fieldcontain">
	            
	            
	            <table style="width:100%; border-collapse:collapse;" border="0" >
            		<tr>
            			<td style="width:22%">
            				<label for="LiftingReportUser">User:</label>
            			</td>
            			<td style="width:10%">
            				<input type="text" name="LiftingReportUser" id="LiftingReportUser" onChange="getUserName()">
            			</td>
            			<td style="width:68%">
            				<input type="text" name="LiftingReportUserName" id="LiftingReportUserName" readonly>
            			</td>
            		</tr>
            	</table>
	            
	        </li>
	        
	        
	        <li data-role="list-divider" data-theme="c">For the period</li>
	        
				<div class="ui-grid-d" style="padding: .7em 15px;">
					<div class="ui-block-a" style="width:20%; margin: 0 2% 0 0;"><label for="LiftingReportMainFromDate">From Date:</label></div>
					<div class="ui-block-b" style="width:20%;"><input id="LiftingReportMainFromDate" name="LiftingReportMainFromDate" type="text" data-mini="true" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></div>
					<div class="ui-block-c" style="width:15%; padding:10px; cursor: default; text-align:right"><label for="LiftingReportMainFromDateHour">Time (HH:MM)</label></div>
					<div class="ui-block-d" style="width:20%;">
						<select name="LiftingReportMainFromDateHour" id="LiftingReportMainFromDateHour" data-mini="true">
						  	<script>document.write(hours);</script>
						</select>
					</div>
					<div class="ui-block-e" style="width:20%;">
						
						<select name="LiftingReportMainFromDateMinutes" id="LiftingReportMainFromDateMinutes" data-mini="true">
						  	<script>document.write(minutes);</script>
						</select>
					
					</div>
				</div>
				
				<div class="ui-grid-d" style="padding: .7em 15px;">
					<div class="ui-block-a" style="width:20%; margin: 0 2% 0 0;"><label for="LiftingReportMainToDate">To Date:</label></div>
					<div class="ui-block-b" style="width:20%;"><input id="LiftingReportMainToDate" name="LiftingReportMainToDate" type="text" data-mini="true" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateNext(new java.util.Date())%>"></div>
					<div class="ui-block-c" style="width:15%; padding:10px; cursor: default; text-align:right"><label for="LiftingReportMainToDateHour">Time (HH:MM)</label></div>
					<div class="ui-block-d" style="width:20%;">
						<select name="LiftingReportMainToDateHour" id="LiftingReportMainToDateHour" data-mini="true">
						  	<script>document.write(hours);</script>
						</select>
					</div>
					<div class="ui-block-e" style="width:20%;">
					
						<select name="LiftingReportMainToDateMinutes" id="LiftingReportMainToDateMinutes" data-mini="true">
						  	<script>document.write(minutes);</script>
						</select>
					
					</div>
				</div>
	        
	        <li data-role="list-divider" data-theme="c">Status Type</li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportStatus">Status:</label>
	            <select name="LiftingReportStatus" id="LiftingReportStatus">
	            	<option value="0">All</option>
	            	<option value="1">Generated</option>
	            	<option value="2">Delivered</option>
	            	<option value="3">Received</option>
	            </select>
	        </li>

        </ul>
	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="DeliveryReportGenerateButton" onClick="">Generate</button>
	</div>    	
    </div>
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackDeliveryRpt" name="CallBack" />
    	<jsp:param value="40" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    
	<jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackDeliveryReport" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
</div>


</body>
</html>

<script type="text/javascript">

$('#LiftingReportMainFromDateHour').val('6').change();
$('#LiftingReportMainFromDateMinutes').val('0').change();

$('#LiftingReportMainToDateHour').val('5').change();
$('#LiftingReportMainToDateMinutes').val('59').change();

</script>

<%
s.close();
ds.dropConnection();
%>