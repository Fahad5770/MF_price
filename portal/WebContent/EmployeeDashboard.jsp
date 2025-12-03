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

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(41, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


long EmployeeCode = Utilities.parseLong(request.getParameter("EmployeeCode"));
String EmployeeName = "";

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();


boolean isEditCase = false;
int EditID = 0;

int BeatPlanID = Utilities.parseInt(request.getParameter("BeatPlanID"));

if(BeatPlanID > 0){
	EditID = BeatPlanID;
	isEditCase = true;
}else{
	ResultSet rs_edit = s.executeQuery("select beat_plan_id from employee_beat_plan where assigned_to="+EmployeeCode);
	if(rs_edit.first()){
		
		EditID = rs_edit.getInt("beat_plan_id");
		isEditCase = true;
	}

}


ResultSet rs = s.executeQuery("select vorna, nachn from sap_pa0002 where pernr="+EmployeeCode);
if(rs.first()){
	EmployeeName = rs.getString("vorna")+" "+rs.getString("nachn");
}


s.close();
ds.dropConnection();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="lib/highcharts301/js/highcharts-more.js"></script>
		<script src="js/BeatPlan.js"></script>
		<script src="js/lookups.js"></script>
		
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
		
		<script>
			<% if(isEditCase){
				
				%>
				getBeatPlanInfoJson(<%=EditID%>);
				<%
				
			} %>
		</script>
		
	</head>
	
<body>
<%
//int OutletID = Utilities.parseInt(request.getParameter("OutletID"));
%>

<div data-role="page" id="EmployeeBeatPlan" data-url="EmployeeBeatPlan" data-theme="d">

    <jsp:include page="EmployeeDashboardHeader.jsp" >
    	<jsp:param value="<%=EmployeeName%>" name="title"/>
    	<jsp:param value="1" name="tab"/>
    	<jsp:param value="<%=EmployeeCode%>" name="EmployeeCode"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">

        <table style="width:100%" border="0">
        	<tr>
        		<td style="width:40%" valign="top">
	        		<form name="OutletSelectionForm" id="OutletSelectionForm" data-ajax="false" action="#EmployeeDashboardBeatPlan" onSubmit="return getOutletName(true);">
						<table border="0" style="width: 100%">
						<tr>
							<td style="width:20%">
								<input type="text" name="EmployeeSapCode" id="EmployeeSapCode" placeholder="SAP Code" value="<%=EmployeeCode%>" onChange="getEmployeeName()" class="ui-disabled" data-mini="true">
							</td>
							<td style="width:70%">
								<input type="text" name="EmployeeName" id="EmployeeName" readonly="readonly" value="<%=EmployeeName%>" class="ui-disabled"  data-mini="true">
							</td>
							<td style="text-align:right">
								<input type="button" value="Change" data-inline="false" data-mini="true" data-icon="edit" onClick="enableEmployeeSelection()" >
							</td>
						</tr>
						
							<tr> 
								<td style="width:20%">
									
									<input type="text" name="OutletID" id="OutletID" placeholder="Outlet ID" value="" onChange="getOutletName(false)" data-mini="true">
								</td>
								<td style="width:70%">
									<input type="text" name="OutletName" id="OutletName" readonly="readonly" value="" tabindex="-1" data-mini="true">
								</td>
								<td style="text-align:right">
									<input type="submit" value="&nbsp;&nbsp;Add&nbsp;&nbsp;" data-inline="false" data-mini="true" data-icon="plus">
								</td>
									
							</tr>
						</table>
					</form>
        		</td>
        		
        		<td style="width:50%" valign="top">
	        		<table border="0" style="width: 100%">
					</table>
        		</td>
        	</tr>
        </table>
        
		 
		
		
		<form action="test2.jsp" name="EmployeeBeatPlanMainForm" id="EmployeeBeatPlanMainForm">
		
    	<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
    	<input type="hidden" name="EmployeeCode" id="EmployeeCode" value="<%=EmployeeCode%>" >
    	<input type="hidden" name="EmployeeBeatPlanMainFormAssignTo" id="EmployeeBeatPlanMainFormAssignTo" value="" >
        
        
        <table style="width:100%" border="0">
        	<tr>
        		<td valign="top">
	        		<table border="0" data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
					  <thead>
					    
					    <tr class="ui-bar-c">
							<th data-priority="1" style="width:25%; padding-top:25px">Outlet</th>
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
					    </tr>
					  </thead>
					  
						<tbody id="EmployeeBeatPlanTableBody">
						<tr id="NoOutletRow">
							<td colspan="9" style="margin: 1px; padding: 0px;">
								<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No outlet added.</div>
							</td>
						</tr>
						</tbody>
					</table>
        		</td>
        		
        	</tr>
        	
        </table>
        	
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
		
		
	</form>
		
        
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="BeatPlanSave" href="#" onClick="BeatPlanSubmit();">Save</a>
						<button data-icon="check" data-theme="b" data-inline="true" id="BeatPlanReset" onClick="BeatPlanReset()" >Reset</button>
	                    
					</td>
					<!-- <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="BeatPlanSearch" >Search</a>
					</td> -->
	                
				</tr>
			</table>
		</div>
	    	
    </div>


	<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="BeatPlanEditForm" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>

						<ul id="autocomplete_search" data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search by Employee ID or by Name" data-filter-theme="d"></ul>
						
                    
                    </td>
                    <td>
                    
						<span id="EmployeeNameSearch" style="padding-left:20px"></span>
						<input type="hidden" name="EmployeeIDSearch" id="EmployeeIDSearch" value="" >
                    
                    </td>
                    
                </tr>
                <tr>
                	<td>&nbsp;</td>
                </tr>
                <tr>
                	<td>

						<ul id="autocomplete_outlet_search" data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search by Outlet ID or by Name" data-filter-theme="d"></ul>
						
                    
                    </td>
                    <td>
                    
						<span id="OutletNameSearch" style="padding-left:20px"></span>
						<input type="hidden" name="OutletIDSearch" id="OutletIDSearch" value="" >
                    
                    </td>
                    
                </tr>
                
                <tr>
                	<td>
                    	<button data-role="button" data-icon="search" id="BeatPlanSearchButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="EmployeeBeatPlanSearchContent">
        </div>
            
        </div>
    </div>

    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Outlet Search -->

</div>
</body>
</html>