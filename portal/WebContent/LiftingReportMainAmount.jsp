<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>





<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/LiftingReportAmount.js"></script>
<script src="js/lookups.js"></script>




<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 49;
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

/*String RegionOptions = "<option value=''>Select Region</option>";

ResultSet rs3 = s.executeQuery("select * from common_regions order by region_short_name");
while( rs3.next() ){
	RegionOptions += "<option value='"+rs3.getString("region_id")+"'>"+rs3.getString("region_short_name")+" - "+rs3.getString("region_name")+"</option>";
}*/

String RegionOptions = "<option value=''>All</option>";
Region [] RegionIdsObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
if(RegionIdsObj.length == 1)
{RegionOptions="";}
String RegionIds = UserAccess.getRegionQueryString(RegionIdsObj);
ResultSet rs6 = s.executeQuery("SELECT * from common_regions where region_id in("+RegionIds+") order by region_name");
while(rs6.next()){
	RegionOptions += "<option value='"+rs6.getString("region_id")+"'>"+rs6.getString("region_short_name")+" - "+rs6.getString("region_name")+"</option>";
}


String WarehouseOptions = "<option value=''>All</option>";
Warehouse [] WarehouseIdsObj = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);
if(WarehouseIdsObj.length ==1)
{WarehouseOptions="";}
String WarehouseIds = UserAccess.getWarehousesQueryString(WarehouseIdsObj);
ResultSet rs5 = s.executeQuery("SELECT id,label from common_warehouses where id in("+WarehouseIds+") order by label");
while(rs5.next()){
	WarehouseOptions += "<option value='"+rs5.getString("id")+"'>"+rs5.getString("label")+"</option>";
}

//distributor

Distributor [] DistributorObj = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);

String DistributorsIds = UserAccess.getDistributorQueryString(DistributorObj);
//ResultSet rs5 = s.executeQuery("SELECT id,label from common_warehouses where id in("+WarehouseIds+") order by label");
//while(rs5.next()){
	//WarehouseOptions += "<option value='"+rs5.getString("id")+"'>"+rs5.getString("label")+"</option>";
//}


/*
boolean isWarehouseFound = false;
String WarehouseOptions = "";
ResultSet rs5 = s.executeQuery("SELECT warehouse_id, (select label from common_warehouses where id=warehouse_id) warehouse_label FROM user_access_warehouses where user_id="+SessionUserID+" and feature_id="+FeatureID+" order by warehouse_label");
while(rs5.next()){
	isWarehouseFound = true;
	WarehouseOptions += "<option value='"+rs5.getString("warehouse_id")+"'>"+rs5.getString("warehouse_label")+"</option>";
}

if(isWarehouseFound== false){
	WarehouseOptions = "<option val=''>All</option>";
	//ResultSet rs6 = s.executeQuery("select id, label from common_warehouses order by label where");
	while(rs6.next()){
		WarehouseOptions += "<option value='"+rs6.getString("id")+"'>"+rs6.getString("label")+"</option>";
	}
} */

String SndOptions = "<option value=''>All</option>";
ResultSet rs7 = s.executeQuery("SELECT * FROM common_sd_groups where snd_id is not null order by short_name");
while(rs7.next()){
	SndOptions += "<option value='"+rs7.getString("snd_id")+"'>"+rs7.getString("short_name")+" - "+rs7.getString("long_name")+"</option>";
}


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

var DistributorIds = [<%=DistributorsIds%>];



</script>
<div data-role="page" id="LiftingReportMain" data-url="LiftingReportMain" data-theme="d">
<div data-role="header" data-theme="c" data-position="fixed">
    <div>
		

    
    <div style="float:left; width:10%">
    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
    </div>
    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Lifting Report</h1>
    
	    <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
	    	<ul>
	        	<li><a href="LiftingReportMain.jsp"  data-ajax="false">Raw Case</a></li>
	        	<li><a href="LiftingReportMainAmount.jsp" class="ui-btn-active" data-ajax="false">Invoice Value</a></li>
	    	</ul>
		</div><!-- /navbar -->
	</div>
    
	</div>
		


</div>
    
    <div data-role="content" data-theme="d">

	<form id="LiftingReportGenerrateForm" name="LiftingReportGenerrateForm" action="LiftingReport3CAmount.jsp" method="POST" data-ajax="false">
		
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
            				<label for="LiftingReportMainDistributor" style="width:100%">Distributor ID</label>
            			</td>
            			<td style="width:10%">
            				<input type="text" name="LiftingReportMainDistributor" id="LiftingReportMainDistributor" style="width: 100%" onChange="getDistributorName()">
            			</td>
            			<td style="width:68%">
            				<input type="text" name="LiftingReportMainDistributorName" id="LiftingReportMainDistributorName" readonly="readonly">
            			</td>
            		</tr>
            	</table>
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainSnd">SND:</label>
	            <select name="LiftingReportMainSnd" id="LiftingReportMainSnd" data-mini="true">
	            	<%=SndOptions%>
	            </select>
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
	         
	        <li data-role="fieldcontain">
	            <label for="LiftingReportMainType">Type:</label>
	            
	            <select name="LiftingReportMainType" id="LiftingReportMainType" data-mini="true">
	            	<option value="All">All</option>
	            	<option value="Internal">Internal</option>
	            	<option value="Outsourced">Outsourced</option>	            	
	            </select>
	        </li>
	        
	        <li data-role="fieldcontain">

		            <label for="isShiftedToOtherPlant"> Exclude customers shifted to other plants</label>	            
		            <input type="checkbox" id="isShiftedToOtherPlant" name="isShiftedToOtherPlant" value="1" >
	        </li>
	        
	        <li data-role="list-divider" data-theme="c">For the period</li>
	        <li>
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
				</li><li>
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
	        </li>


        </ul>
	</form>


	<jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBack" name="CallBack" />
    	<jsp:param value="49" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="LiftingReportGenerateButton" onClick="">Generate</button>
		
	</div>    	
    </div>
	<jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackLiftingReport" name="CallBack" />
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