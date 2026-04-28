<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 82;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
String DistributorName = "";

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("BeatPlanID"));
if(EditID > 0){
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

ResultSet rs2 = s.executeQuery("SELECT name FROM common_distributors where distributor_id="+DistributorID);
if(rs2.first()){
	DistributorName = rs2.getString("name");
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="lib/highcharts301/js/highcharts-more.js"></script>
		<script src="js/OutletBeatPlan.js?222=443"></script>
		<script src="js/lookups.js"></script>
		
		<script>
		
			var isEditCase = <%=isEditCase%>;
			var EditID = <%=EditID%>;
			
			if(isEditCase){
				getBeatPlanInfoJson(EditID);
			}
			
		
		</script>
		
		<style>
		
		.radio_style
		  {
		      display: block;
		      width: 80px;
		      height: 50px;
		      background-repeat: no-repeat;
		      background-position: -231px 0px;
		  }
		  
		  .ui-table-reflow.ui-responsive{
		  	display:block;
		  }
		
		</style>
		
		
		
	</head>
	
<body>
<%
String PageTitle = DistributorID + "-" + DistributorName;
%>

<div data-role="page" id="BeatPlanCreate" data-url="BeatPlanCreate" data-theme="d">

    <div data-role="header" data-id="LiftingHeader" data-theme="d">
    <h1>PJP</h1>
    <a href="BeatPlanListDistributor.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    
</div>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
        <table style="width:100%" border="0">
        	<tr>
        		<td style="width:40%" valign="top">
	        		<table border="0" style="width: 100%">
	        			
	        			
	        			
	        			<tr>
							<td colspan="2">
								<label for="BeatPlanCreateOutletGroupName">PJP Name</label>
								<input type="text" name="BeatPlanCreateOutletGroupName" id="BeatPlanCreateOutletGroupName" value="" data-mini="true" placeholder="">
							</td>
						</tr>
						
	        		</table>
        			<table border="0" style="width: 100%">
        				
        				<tr> 
							<td style="width:20%">
							<label for="BeatPlanCreateDistributorID">Distributor</label>
								<input type="text" name="BeatPlanCreateDistributorID" id="BeatPlanCreateDistributorID" placeholder="" value="<%=DistributorID%>" onChange="getDistributorName();" data-mini="true"  class="ui-disabled" >
							</td>
							<td style="width:70%" valign="bottom" >
								<input type="text" name="BeatPlanCreateDistributorName" id="BeatPlanCreateDistributorName" placeholder="" value="<%=DistributorName%>" tabindex="-1" data-mini="true" readonly="readonly" class="ui-disabled" >
							</td>
							<td style="text-align:right" valign="bottom">
								<input type="button" value="Change" data-inline="false" data-mini="true" data-icon="refresh" onclick="enableDistributorSelection()" >
							</td>
						</tr>
        			</table>
        		
	        		
        		</td>
        		
        		
        		<td style="width:40%" valign="bottom">
        			<table border="0" style="width: 100%">
        				
        				
        				
        				<tr> 
							<td style="width:20%">
								<label for="BeatPlanCreateEmployeeID">Order Booker</label>
								<input type="text" name="BeatPlanCreateEmployeeID" id="BeatPlanCreateEmployeeID" placeholder="" value="" onChange="getEmployeeName(this.id, 'BeatPlanCreateEmployeeName', this.value);" data-mini="true" class="ui-disabled" >
							</td>
							<td style="width:70%" valign="bottom">
								<input type="text" name="BeatPlanCreateEmployeeName" id="BeatPlanCreateEmployeeName" placeholder="" value="" tabindex="-1" data-mini="true" readonly="readonly" class="ui-disabled" >
							</td>
							<td style="text-align:right" valign="bottom">
								<input type="button" value="Change" data-inline="false" data-mini="true" data-icon="refresh" onclick="enableEmployeeSelection()" >
							</td>
						</tr>
        			</table>
        		</td>
        	</tr>
        	
        	<tr>
        		<td>
        		
        		<table border="0" style="width: 100%">
        				<tr>
							<td style="width:20%">
								<label for="BeatPlanCreateSMID">RSM</label>
								<input type="text" name="BeatPlanCreateSMID" id="BeatPlanCreateSMID" placeholder="" value="" onChange="getEmployeeName(this.id, 'BeatPlanCreateSMName', this.value);" data-mini="true" class="ui-disabled" >
							</td>
							<td style="width:70%" valign="bottom">
								<input type="text" name="BeatPlanCreateSMName" id="BeatPlanCreateSMName" placeholder="" value="" tabindex="-1" data-mini="true" readonly="readonly" class="ui-disabled" >
							</td>
							<td style="text-align:right" valign="bottom">
								<input type="button" value="Change" data-inline="false" data-mini="true" data-icon="refresh" onclick="enableSMSelection()" >
							</td>
						</tr>
        			</table>
        		
        		</td>
        		<td>
        			<table border="0" style="width: 100%">
        				<tr> 
							<td style="width:20%">
								<label for="BeatPlanCreateTDMID">ASM</label>
								<input type="text" name="BeatPlanCreateTDMID" id="BeatPlanCreateTDMID" placeholder="" value="" onChange="getEmployeeName(this.id, 'BeatPlanCreateTDMName', this.value);" data-mini="true" class="ui-disabled" >
							</td>
							<td style="width:70%" valign="bottom">
								<input type="text" name="BeatPlanCreateTDMName" id="BeatPlanCreateTDMName" placeholder="" value="" tabindex="-1" data-mini="true" readonly="readonly" class="ui-disabled" >
							</td>
							<td style="text-align:right" valign="bottom">
								<input type="button" value="Change" data-inline="false" data-mini="true" data-icon="refresh" onclick="enableTDMSelection()" >
							</td>
						</tr>
        			</table>		
        		</td>
        	</tr>
        	<tr>
        		<td>
        			<table border="0" style="width: 100%">
        				<tr> 
							<td style="width:20%">
								<label for="BeatPlanCreateASMID">TSO</label>
								<input type="text" name="BeatPlanCreateASMID" id="BeatPlanCreateASMID" placeholder="" value="" onChange="getEmployeeName(this.id, 'BeatPlanCreateASMName', this.value);" data-mini="true" class="ui-disabled" >
							</td>
							<td style="width:70%" valign="bottom">
								<input type="text" name="BeatPlanCreateASMName" id="BeatPlanCreateASMName" placeholder="" value="" tabindex="-1" data-mini="true" readonly="readonly" class="ui-disabled" >
							</td>
							<td style="text-align:right" valign="bottom">
								<input type="button" value="Change" data-inline="false" data-mini="true" data-icon="refresh" onclick="enableASMSelection()" >
							</td>
						</tr>
        			</table>        		
        		</td>
        	</tr>
        	
        </table>
        
        </li>
		
		<li data-role="list-divider">Outlets</li> 
		
		<li>
		
		<form name="OutletSelectionForm" id="OutletSelectionForm" data-ajax="false" action="#EmployeeDashboardBeatPlan" onSubmit="return getOutletName(true);">
			<table border="0" style="width: 50%">
				<tr> 
					<td style="width:20%">
						
						<input type="text" name="OutletID" id="OutletID" placeholder="Outlet ID" value="" onChange="getOutletName(false)" data-mini="true">
					</td>
					<td style="width:70%">
						<input type="text" name="OutletName" id="OutletName" readonly="readonly" value="" tabindex="-1" data-mini="true">
					</td>
					<td style="text-align:right">
						<input type="submit" value="&nbsp;&nbsp;&nbsp;Add&nbsp;&nbsp;&nbsp;" data-inline="false" data-mini="true" data-icon="plus">
					</td>
						
				</tr>
			</table>
		</form>
		
		
		<form action="test2.jsp" name="BeatPlanCreateMainForm" id="BeatPlanCreateMainForm">
		
    	<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
    	<input type="hidden" name="BeatPlanCreateMainFormOutletGroupName" id="BeatPlanCreateMainFormOutletGroupName" value="" >
    	<input type="hidden" name="DistributorID" id="DistributorID" value="<%=DistributorID%>" >
    	<input type="hidden" name="OrderBookerID" id="OrderBookerID" value="" >
    	
    	<input type="hidden" name="SMID" id="SMID" value="" >
    	<input type="hidden" name="TDMID" id="TDMID" value="" >
    	<input type="hidden" name="ASMID" id="ASMID" value="" >
    	
        <table style="width:100%" border="0">
        	<tr>
        		<td valign="top" style="width: 70%">
	        		<table border="0" data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
					  <thead>
					    
					    <tr class="ui-bar-c">
							<th data-priority="1" style="width:25%; padding-top:25px">Outlet</th>
							<th data-priority="1" style="width:5%; padding-top:25px">Active</th>
							<th data-priority="1" style="text-align:left" nowrap="nowrap">
							
							<fieldset data-role="controlgroup" data-type="horizontal">

				                <input type="checkbox" name="checkbox-61" id="checkbox-61" disabled="disabled">
				                <label for="checkbox-61" class="radio_style">Sun</label>
				                <input type="checkbox" name="checkbox-71" id="checkbox-71" disabled="disabled">
				                <label for="checkbox-71" class="radio_style">Mon</label>
				                <input type="checkbox" name="checkbox-81" id="checkbox-81" disabled="disabled">
				                <label for="checkbox-81" class="radio_style">Tue</label>
				                <input type="checkbox" name="checkbox-91" id="checkbox-91" disabled="disabled">
				                <label for="checkbox-91" class="radio_style">Wed</label>
				                <input type="checkbox" name="checkbox-101" id="checkbox-101" disabled="disabled">
				                <label for="checkbox-101" class="radio_style">Thu</label>
				                <input type="checkbox" name="checkbox-111" id="checkbox-111" disabled="disabled">
				                <label for="checkbox-111" class="radio_style">Fri</label>
				                <input type="checkbox" name="checkbox-121" id="checkbox-121" disabled="disabled">
				                <label for="checkbox-121" class="radio_style">Sat</label>

							</fieldset>
							
							</th>
							<th data-priority="1" style="width:5%">&nbsp;</th>
							<th data-priority="1" style="width:5%">&nbsp;</th>
					    </tr>
					  </thead>
					  
						<tbody id="BeatPlanCreateTableBody">
											
											<tr id="NoOutletRow">
							<td colspan="10" style="margin: 1px; padding: 0px;">
								<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No outlet added.</div>
							</td>
						</tr>
						</tbody>
					</table>
        		</td>
        		<td valign="top" style="width: 30%"> 
	        		<ul data-role="listview" data-filter="true" data-filter-placeholder="Search Outlets" data-inset="true">
	        		
	        		<%
	        		ResultSet rs = s.executeQuery("SELECT id as outlet_id, name as outlet_name FROM common_outlets where distributor_id="+DistributorID+" and id not in ( SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in (SELECT id FROM distributor_beat_plan where distributor_id="+DistributorID+") ) order by outlet_name");
	        		while(rs.next()){
	        		%>
					    <li><a href="javascript: OutletSearchCallBack(<%=rs.getString("outlet_id")%>, '<%=rs.getString("outlet_name")%>')" style="font-size: 12px"><%=rs.getString("outlet_id")+"-"+rs.getString("outlet_name")%></a></li>
					<%
	        		}
					%>
					</ul>
        		</td>
        		
        	</tr>
        	
        </table>
        	
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
		
		
	</form>
		</li>
    </ul>    
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="BeatPlanCreateSave" href="#" onClick="BeatPlanCreateSubmit();">Save</a>
						
	                    
					</td>
					<!-- <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="BeatPlanSearch" >Search</a>
					</td> -->
	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBack" name="CallBack" />
    	<jsp:param value="<%=FeatureID%>" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="setDistributorLookupAtBeatPlanCreate" name="CallBack" />
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="setEmployeeLookupAtBeatPlanCreate" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->

</div>
</body>
</html>

<%
s.close();
ds.dropConnection();
%>